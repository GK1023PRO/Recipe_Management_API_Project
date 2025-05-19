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
     * Handles the root URL and forwards to the JavaDoc documentation.
     * This is a backup approach in case the WebMvcConfigurer redirect doesn't work.
     *
     * @return Forward path to the JavaDoc documentation index page
     */
    @GetMapping("/")
    public String home() {
        return "forward:/OOPDocumentationJavaDoc/index.html";
    }

    /**
     * Alternative endpoint to access documentation.
     * Provides a backup access point in case the root mapping has issues.
     *
     * @return Forward path to the JavaDoc documentation
     */
    @GetMapping("/javadoc")
    public String documentation() {
        return "forward:/OOPDocumentationJavaDoc/index.html";
    }
}