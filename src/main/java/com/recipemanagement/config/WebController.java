package com.recipemanagement.config;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String home() {
        return "redirect:/OOPDocumentationJavaDoc/index.html"; // Matches the template name (index.html)
    }
}