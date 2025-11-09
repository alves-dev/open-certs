package com.opencerts.certification;

import com.opencerts.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/new")
    public String showForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("certifications", Certifications.values());
        model.addAttribute("user", user);

        return "question-form";
    }

    @PostMapping
    public String saveQuestion(
            @RequestParam UUID certification,
            @RequestParam String topic,
            @RequestParam String description,
            @RequestParam List<String> responses,
            @RequestParam int correctIndex,
            @AuthenticationPrincipal User user
    ) {
        List<Response> responseList = new ArrayList<>();
        for (String response : responses) {
            Response r = new Response(response);
            responseList.add(r);
        }

        Response correct = responseList.get(correctIndex);

        Question question = new Question(certification, description, topic, responseList, correct, user);
        questionService.save(question);

        return "redirect:/questions/new";
    }
}
