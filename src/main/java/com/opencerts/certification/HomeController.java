package com.opencerts.certification;

import com.opencerts.util.Page;
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
    public String home(Model model) {
        model.addAttribute("certifications", certificationService.listAll());
        return Page.HOME;
    }
}
