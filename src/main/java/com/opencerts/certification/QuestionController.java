package com.opencerts.certification;

import com.opencerts.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final CertificationService certificationService;

    public QuestionController(QuestionService questionService, CertificationService certificationService) {
        this.questionService = questionService;
        this.certificationService = certificationService;
    }

    @GetMapping("/new")
    public String showForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("certifications", certificationService.listAll());
        model.addAttribute("user", user);

        return "question-form";
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
    public String showQuestion(@PathVariable String certificationId, Model model, @AuthenticationPrincipal User user) {
        var question = questionService.findRandomByCertification(certificationId);

        model.addAttribute("certification", certificationService.getById(certificationId));

        if (question != null) {
            model.addAttribute("question", question);
            model.addAttribute("answers", question.responses());
        } else {
            model.addAttribute("noQuestion", true);
        }
        model.addAttribute("user", user);

        return "question";
    }

    @PostMapping("/{certificationId}/check")
    public String checkAnswer(@PathVariable String certificationId,
                              @RequestParam("questionId") UUID questionId,
                              @RequestParam("selectedOptions") List<String> selectedOptions,
                              Model model,
                              @AuthenticationPrincipal User user) {

        var question = questionService.getQuestionById(questionId);
        var isCorrect = question.checkAnswerByString(selectedOptions);

        model.addAttribute("certification", certificationService.getById(certificationId));
        model.addAttribute("question", question);
        model.addAttribute("answers", question.responses());
        model.addAttribute("user", user);
        model.addAttribute("isCorrect", isCorrect);

        return "question";
    }
}
