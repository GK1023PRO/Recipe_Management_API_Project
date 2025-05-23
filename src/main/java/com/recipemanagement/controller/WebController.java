package com.recipemanagement.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling the root URL and JavaDoc documentation routes.
 */
@Controller
public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    /**
     * Handles the root URL and forwards to the JavaDoc documentation.
     */
    @GetMapping("/")
    public String home() {
        logger.info("Root URL accessed, redirecting to JavaDoc");
        // Check if JavaDoc exists
        ClassPathResource resource = new ClassPathResource("static/OOPDocumentationJavaDoc/index.html");
        if (resource.exists()) {
            return "redirect:/OOPDocumentationJavaDoc/index.html";
        } else {
            logger.warn("JavaDoc not found, showing fallback message");
            return "redirect:/api/health";
        }
    }

    /**
     * Fallback endpoint for documentation access
     */
    @GetMapping("/docs")
    @ResponseBody
    public ResponseEntity<String> docs() {
        ClassPathResource resource = new ClassPathResource("static/OOPDocumentationJavaDoc/index.html");
        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header("Content-Type", "text/html")
                    .body("<html><head><meta http-equiv='refresh' content='0;url=/OOPDocumentationJavaDoc/index.html'></head><body>Redirecting to documentation...</body></html>");
        } else {
            return ResponseEntity.ok()
                    .header("Content-Type", "text/html")
                    .body("<html><body><h1>Recipe Management API</h1><p>Documentation is being generated. Please check back later.</p><p><a href='/swagger-ui.html'>View API Documentation</a></p></body></html>");
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/api/health")
    @ResponseBody
    public ResponseEntity<String> health() {
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body("{\"status\":\"UP\",\"message\":\"Recipe Management API is running\"}");
    }
}