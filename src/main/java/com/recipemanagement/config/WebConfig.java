package com.recipemanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Enhanced web configuration to handle static resources and URL mappings.
 * Modified for proper deployment on Render.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures static resource handling with explicit cache control.
     * Ensures JavaDoc documentation is properly served from classpath resources.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // JavaDoc specific resources - high priority
        registry.addResourceHandler("/OOPDocumentationJavaDoc/**")
                .addResourceLocations("classpath:/static/OOPDocumentationJavaDoc/")
                .setCachePeriod(3600); // Cache for 1 hour

        // All other static resources
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
    }

    /**
     * Configures view controllers for URL mapping.
     * Ensures root URL redirects to JavaDoc documentation index page.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Forward the root URL to JavaDoc index
        registry.addViewController("/").setViewName("forward:/OOPDocumentationJavaDoc/index.html");

        // Add another redirect as backup
        registry.addRedirectViewController("/docs", "/OOPDocumentationJavaDoc/index.html");
    }
}