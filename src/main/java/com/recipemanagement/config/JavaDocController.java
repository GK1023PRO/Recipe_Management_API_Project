package com.recipemanagement.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Dedicated controller for handling JavaDoc redirects.
 *
 * <p>This controller specifically handles redirecting the root URL to the JavaDoc
 * documentation. It uses a view-based approach rather than a REST approach to
 * ensure proper forwarding to static resources.</p>
 *
 * @author Georges Khoury
 * @version 1.0
 * @since 1.0
 */
@Controller
public class JavaDocController {

    /**
     * Redirects the root URL to the JavaDoc documentation.
     *
     * @return A string representing the view name to redirect to
     */
    @GetMapping("/")
    public String redirectToJavadoc() {
        return "redirect:/OOPDocumentationJavaDoc/index.html";
    }

    /**
     * Alternative endpoint for accessing JavaDoc.
     * Provides a backup access point for documentation.
     *
     * @return A string representing the view name to redirect to
     */
    @GetMapping("/docs")
    public String docs() {
        return "redirect:/OOPDocumentationJavaDoc/index.html";
    }
}