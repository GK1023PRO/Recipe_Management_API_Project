package com.recipemanagement.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling the root URL and JavaDoc documentation routes.
 * Avoids ambiguous mappings by combining all routes into one controller.
 */
@Controller
public class WebController {

    /**
     * Handles the root URL and forwards to the JavaDoc documentation.
     *
     * @return Forward path to the JavaDoc documentation index page
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/OOPDocumentationJavaDoc/index.html";
    }



    /**
     * Endpoint to access documentation via /javadoc.
     *
     * @return Forward path to the JavaDoc documentation
     */
    @GetMapping("/javadoc")
    public String documentation() {
        return "redirect:/OOPDocumentationJavaDoc/index.html";
    }

    /**
     * Endpoint to access documentation via /docs.
     *
     * @return Forward path to the JavaDoc documentation
     */
    @GetMapping("/docs")
    public String docs() {
        return "redirect:/OOPDocumentationJavaDoc/index.html";
    }
}
