package com.recipemanagement.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller for handling the root URL and JavaDoc documentation routes.
 * Enhanced to support both local and cloud deployments.
 */
@Controller
public class WebController {

    /**
     * Simple health check endpoint to verify the application is running.
     * This helps diagnose deployment issues.
     *
     * @return A simple status message
     */
    @GetMapping("/api/health")
    @ResponseBody
    public String healthCheck() {
        return "Recipe Management API is running";
    }

    /**
     * Handles the root URL with explicit RedirectView.
     * This ensures proper redirection in cloud environments.
     *
     * @return RedirectView to the JavaDoc documentation
     */
    @GetMapping("/")
    public RedirectView home() {
        return new RedirectView("/OOPDocumentationJavaDoc/index.html");
    }

    /**
     * Fallback handler for the root context.
     * Useful when the main handler might be bypassed.
     *
     * @return RedirectView to the JavaDoc documentation
     */
    @RequestMapping("")
    public RedirectView rootFallback() {
        return new RedirectView("/OOPDocumentationJavaDoc/index.html");
    }

    /**
     * Endpoint to access documentation via /javadoc.
     *
     * @return RedirectView to the JavaDoc documentation
     */
    @GetMapping("/javadoc")
    public RedirectView documentation() {
        return new RedirectView("/OOPDocumentationJavaDoc/index.html");
    }

    /**
     * Endpoint to access documentation via /docs.
     *
     * @return RedirectView to the JavaDoc documentation
     */
    @GetMapping("/docs")
    public RedirectView docs() {
        return new RedirectView("/OOPDocumentationJavaDoc/index.html");
    }
}