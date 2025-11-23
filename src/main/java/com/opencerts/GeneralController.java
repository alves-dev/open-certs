package com.opencerts;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {

    @GetMapping("/documentation")
    public String documentation(Model model) {
        return "docs";
    }

    @GetMapping("/privacy")
    public String privacy(Model model) {
        return "privacy";
    }

    @GetMapping("/terms")
    public String terms(Model model) {
        return "terms";
    }
}
