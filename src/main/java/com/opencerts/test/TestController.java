package com.opencerts.test;

import com.opencerts.certification.QuestionService;
import com.opencerts.util.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/tests")
public class TestController {

    private final TestSessionService testSessionService;
    private final QuestionService questionService;

    public TestController(TestSessionService testSessionService, QuestionService questionService) {
        this.testSessionService = testSessionService;
        this.questionService = questionService;
    }

    @GetMapping
    public String listTests(Model model) {
        model.addAttribute("testSessions", testSessionService.listByUser());
        return Page.TESTS;
    }

    @GetMapping("/details/{identifier}")
    public String testDetails(@PathVariable String identifier, Model model) {
        Optional<TestSession> optTest = testSessionService.findByIdentifier(identifier);
        if (optTest.isEmpty()) {
            model.addAttribute("errorMessage", "Test not found");
            model.addAttribute("status", "400");
            return Page.ERROR;
        }

        model.addAttribute("testSession", new TestSessionDetailsDTO(optTest.get(), questionService));
        return Page.TESTS_DETAILS;
    }

    @PostMapping("/finish/{identifier}")
    public String finishTest(@PathVariable String identifier) {
        testSessionService.finishByIdentifier(identifier);

        return "redirect:/tests/details/" + identifier;
    }
}
