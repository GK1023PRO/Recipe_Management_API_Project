package com.recipemanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("Configuring resource handlers for static content");

        // Add resource handler for JavaDoc
        registry.addResourceHandler("/OOPDocumentationJavaDoc/**")
                .addResourceLocations("classpath:/static/OOPDocumentationJavaDoc/")
                .setCachePeriod(0);

        // Add general static resource handlers
        registry.addResourceHandler("/**")
                .addResourceLocations(
                        "classpath:/static/",
                        "classpath:/public/",
                        "classpath:/META-INF/resources/"
                )
                .setCachePeriod(0);

        logger.info("Resource handlers configured successfully");
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