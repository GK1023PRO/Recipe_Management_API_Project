package com.recipemanagement.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for handling the root URL and JavaDoc documentation routes.
 * Enhanced with proper logging and redirect handling.
 */
@Controller
public class WebController {
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    /**
     * Handles the root URL and forwards to the JavaDoc documentation.
     *
     * @return Forward path to the JavaDoc documentation index page
     */
    @GetMapping("/")
    public RedirectView home() {
        logger.info("Accessing root path, redirecting to JavaDoc");
        return new RedirectView("/OOPDocumentationJavaDoc/index.html");
    }

    /**
     * Endpoint to access documentation via /javadoc.
     *
     * @return Forward path to the JavaDoc documentation
     */
    @GetMapping("/javadoc")
    public RedirectView documentation() {
        logger.info("Accessing /javadoc path, redirecting to JavaDoc");
        return new RedirectView("/OOPDocumentationJavaDoc/index.html");
    }

    /**
     * Endpoint to access documentation via /docs.
     *
     * @return Forward path to the JavaDoc documentation
     */
    @GetMapping("/docs")
    public RedirectView docs() {
        logger.info("Accessing /docs path, redirecting to JavaDoc");
        return new RedirectView("/OOPDocumentationJavaDoc/index.html");
    }

    /**
     * Explicit mapping for direct access to JavaDoc.
     * This helps ensure the path is correctly recognized.
     */
    @GetMapping("/OOPDocumentationJavaDoc/index.html")
    public ModelAndView javaDocIndex() {
        logger.info("Direct access to JavaDoc index");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/OOPDocumentationJavaDoc/index.html");
        return modelAndView;
    }
}