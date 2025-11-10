package com.opencerts;

import com.opencerts.certification.CertificationService;
import com.opencerts.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final CertificationService certificationService;

    public HomeController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        model.addAttribute("certifications", certificationService.listAll());
        return "index";
    }
}
