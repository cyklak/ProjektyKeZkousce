package org.example.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String renderIndex() {
        return "pages/home/index";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POLICYHOLDER", "ROLE_INSURED"})
    @GetMapping("home")
    public String renderIndex2() {
        return "pages/home/index";
    }

    @Secured({"ROLE_ADMIN",  "ROLE_POLICYHOLDER", "ROLE_INSURED"})
    @GetMapping({"home/credits"})
    public String renderCredits(Model model) {
        model.addAttribute("credits", 1);
        return "pages/home/credits";
    }
}
