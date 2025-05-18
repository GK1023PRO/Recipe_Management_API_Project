package com.recipemanagement.config;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling the root URL of the application.
 * Redirects visitors to the JavaDoc documentation.
 */
@Controller
public class WebController {
    /**
     * Redirects root URL requests to the JavaDoc documentation.
     * @return A redirect URL to the JavaDoc documentation
     */
    @GetMapping("/")
    public String redirectToDocumentation() {
        return "redirect:/OOPDocumentationJavaDoc/index.html";
    }
}