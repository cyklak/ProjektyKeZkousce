package org.example.controllers;

import org.example.data.entities.UserEntity;
import org.example.data.repositories.PojistenecRepository;
import org.example.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;



@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String renderIndex() {
        return "pages/home/index";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping("home")
    public String renderIndex2() {
        return "pages/home/index";
    }

    @Secured({"ROLE_ADMIN", "ROLE_POJISTNIK", "ROLE_POJISTENY"})
    @GetMapping({"home/credits"})
    public String renderCredits(Model model) {
        model.addAttribute("credits", 1);
        return "pages/home/credits";
    }
}
