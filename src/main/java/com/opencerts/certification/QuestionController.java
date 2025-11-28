package com.opencerts.certification;

import com.opencerts.certification.request.AnswerFormDTO;
import com.opencerts.shared.TestSessionDTO;
import com.opencerts.test.TestSessionService;
import com.opencerts.user.User;
import com.opencerts.util.Page;
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

    public QuestionController(QuestionService questionService, CertificationService certificationService,
                              TestSessionService testSessionService) {
        this.questionService = questionService;
        this.certificationService = certificationService;
        this.testSessionService = testSessionService;
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("certifications", certificationService.listAllToDTO());

        return Page.QUESTION_FORM;
    }

    @PostMapping
    public String saveQuestion(
            @RequestParam String certification,
            @RequestParam String topic,
            @RequestParam String description,
            @RequestParam List<String> responses,
            @RequestParam List<Integer> correctIndexes,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        Certification objectCertification = certificationService.getById(certification).orElse(null);
        if (objectCertification == null) {
            model.addAttribute("errorMessage", "Certification not found");
            model.addAttribute("status", "400");
            return Page.ERROR;
        }

        List<Question.Answer> answers = new ArrayList<>();
        for (int i = 0; i < responses.size(); i++) {
            if (responses.get(i).isEmpty()) continue;
            boolean isCorrect = correctIndexes.contains(i);
            Question.Answer answer = new Question.Answer(responses.get(i), isCorrect);
            answers.add(answer);
        }

        Question question = new Question(objectCertification, description, topic, answers, user);
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
            model.addAttribute("answers", question.answersText());

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

        var question = questionService.getById(form.questionId());
        var isCorrect = question.checkAnswers(form.selectedOptions());

        TestSessionDTO testSession = null;
        if (!form.testIdentifier().isEmpty())
            testSession = testSessionService.addQuestionInTest(
                    form.testIdentifier(), certificationId, form.questionId(), isCorrect
            );


        model.addAttribute("certification", certificationService.getById(certificationId).get());
        model.addAttribute("question", question);
        model.addAttribute("answers", question.answersText());
        model.addAttribute("isCorrect", isCorrect);
        model.addAttribute("testSession", testSession);
        model.addAttribute("correctAnswers", question.correctAnswers());

        return Page.QUESTION;
    }
}
