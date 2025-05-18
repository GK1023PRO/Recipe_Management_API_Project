package com.recipemanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration to handle static resources and URL mappings.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures static resource handling.
     * Ensures JavaDoc documentation is properly served.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/OOPDocumentationJavaDoc/**")
                .addResourceLocations("classpath:/static/OOPDocumentationJavaDoc/");

        // Add a fallback for other static resources
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    /**
     * Adds view controllers for direct mapping of paths to views.
     * Maps the root URL to redirect to JavaDoc documentation.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect root to JavaDoc
        registry.addRedirectViewController("/", "/OOPDocumentationJavaDoc/index.html");
    }
}