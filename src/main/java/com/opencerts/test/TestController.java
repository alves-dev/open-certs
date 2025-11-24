package com.opencerts.test;

import com.opencerts.util.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tests")
public class TestController {

    private final TestSessionService testSessionService;

    public TestController(TestSessionService testSessionService) {
        this.testSessionService = testSessionService;
    }

    @GetMapping
    public String showQuestion(Model model) {
        model.addAttribute("testSessions", testSessionService.listByUser());
        return Page.TESTS;
    }
}
