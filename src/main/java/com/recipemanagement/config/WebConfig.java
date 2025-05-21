package com.recipemanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Enhanced web configuration to handle static resources and URL mappings.
 * Fixed for proper deployment on Render.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures static resource handling with explicit cache control.
     * Ensures JavaDoc documentation is properly served from classpath resources.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // JavaDoc specific resources - HIGHEST priority
        registry.addResourceHandler("/OOPDocumentationJavaDoc/**")
                .addResourceLocations("classpath:/static/OOPDocumentationJavaDoc/")
                .setCachePeriod(0); // No cache for debugging

        // Static resources - general
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0);

        // All other static resources - LOWEST priority
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/")
                .setCachePeriod(0);
    }

    /**
     * Configures view controllers for URL mapping.
     * Ensures root URL redirects to JavaDoc documentation index page.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Direct redirect for root URL
        registry.addRedirectViewController("/", "/OOPDocumentationJavaDoc/index.html");

        // Additional helpful redirects
        registry.addRedirectViewController("/docs", "/OOPDocumentationJavaDoc/index.html");
        registry.addRedirectViewController("/javadoc", "/OOPDocumentationJavaDoc/index.html");
        registry.addRedirectViewController("/index.html", "/OOPDocumentationJavaDoc/index.html");
    }
}