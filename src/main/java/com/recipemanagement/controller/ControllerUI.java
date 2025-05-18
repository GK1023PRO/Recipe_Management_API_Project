package com.recipemanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerUI {
    @GetMapping("/ui")
    public String showUI(Model model) {
        model.addAttribute("message", "Welcome!"); // Make sure this matches your template
        return "index"; // Maps to templates/index.html
    }


}
