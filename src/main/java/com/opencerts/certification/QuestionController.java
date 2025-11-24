package com.opencerts.certification;

import com.opencerts.util.Page;
import com.opencerts.certification.request.AnswerFormDTO;
import com.opencerts.test.TestSessionDTO;
import com.opencerts.test.TestSessionService;
import com.opencerts.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final CertificationService certificationService;
    private final TestSessionService testSessionService;

    public QuestionController(QuestionService questionService, CertificationService certificationService, TestSessionService testSessionService) {
        this.questionService = questionService;
        this.certificationService = certificationService;
        this.testSessionService = testSessionService;
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("certifications", certificationService.listAll());

        return Page.QUESTION_FORM;
    }

    @PostMapping
    public String saveQuestion(
            @RequestParam String certification,
            @RequestParam String topic,
            @RequestParam String description,
            @RequestParam List<String> responses,
            @RequestParam List<Integer> correctIndexes,
            @AuthenticationPrincipal User user
    ) {
        if (topic.isEmpty())
            topic = "not_defined";

        List<Response> responseList = new ArrayList<>();
        for (String response : responses) {
            if (response.isEmpty()) continue;
            Response r = new Response(response);
            responseList.add(r);
        }

        List<Response> correctAnswers = new ArrayList<>();
        correctIndexes.forEach(e -> correctAnswers.add(responseList.get(e)));

        Question question = new Question(certification, description, topic, responseList, correctAnswers, user);
        questionService.save(question);

        return "redirect:/questions/new";
    }

    @GetMapping("/{certificationId}")
    public String showQuestion(@PathVariable String certificationId,
                               Model model,
                               @RequestParam(value = "testIdentifier", defaultValue = "") String testIdentifier
    ) {
        var question = questionService.findRandomByCertification(certificationId, testIdentifier);

        Optional<Certification> optionalCertification = certificationService.getById(certificationId);
        if (optionalCertification.isEmpty()) {
            model.addAttribute("errorMessage", "Certification not found");
            model.addAttribute("status", "400");
            return Page.ERROR;
        }

        model.addAttribute("certification", optionalCertification.get());
        model.addAttribute("question", question);

        if (question != null)
            model.addAttribute("answers", question.responses());

        TestSessionDTO testSession = null;
        if (!testIdentifier.isEmpty())
            testSession = testSessionService.findByIdentifierAndCertification(testIdentifier, certificationId);

        model.addAttribute("testSession", testSession);

        return Page.QUESTION;
    }

    @PostMapping("/{certificationId}/check")
    public String checkAnswer(@PathVariable String certificationId,
                              @ModelAttribute AnswerFormDTO form,
                              Model model) {

        var question = questionService.getQuestionById(form.questionId());
        var isCorrect = question.checkAnswerByString(form.selectedOptions());

        TestSessionDTO testSession = null;
        if (!form.testIdentifier().isEmpty())
            testSession = testSessionService.addQuestionInTest(
                    form.testIdentifier(), certificationId, form.questionId(), isCorrect
            );


        model.addAttribute("certification", certificationService.getById(certificationId).get());
        model.addAttribute("question", question);
        model.addAttribute("answers", question.responses());
        model.addAttribute("isCorrect", isCorrect);
        model.addAttribute("testSession", testSession);
        model.addAttribute("correctAnswers", question.correctAnswers());

        return Page.QUESTION;
    }
}
