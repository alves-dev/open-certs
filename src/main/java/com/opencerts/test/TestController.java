package com.opencerts.test;

import com.opencerts.certification.CertificationService;
import com.opencerts.certification.QuestionService;
import com.opencerts.util.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/tests")
public class TestController {

    private final TestSessionService testSessionService;
    private final QuestionService questionService;
    private final CertificationService certificationService;

    public TestController(TestSessionService testSessionService, QuestionService questionService,
                          CertificationService certificationService) {
        this.testSessionService = testSessionService;
        this.questionService = questionService;
        this.certificationService = certificationService;
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

        model.addAttribute("testSession", new TestSessionDetailsDTO(optTest.get(), certificationService, questionService));
        return Page.TESTS_DETAILS;
    }
}
