package com.recipemanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.concurrent.TimeUnit;

/**
 * Enhanced web configuration to handle static resources for both local and cloud deployment.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures static resource handling with explicit cache control.
     * Enhanced configuration to support cloud deployment.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // JavaDoc specific resources with multiple location patterns for flexibility
        registry.addResourceHandler("/OOPDocumentationJavaDoc/**")
                .addResourceLocations(
                        "classpath:/static/OOPDocumentationJavaDoc/",
                        "classpath:/META-INF/resources/OOPDocumentationJavaDoc/",
                        "classpath:/public/OOPDocumentationJavaDoc/",
                        "classpath:/OOPDocumentationJavaDoc/"
                )
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS));

        // Swagger UI resources
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS));

        // All other static resources with multiple resource locations for flexibility
        registry.addResourceHandler("/**")
                .addResourceLocations(
                        "classpath:/static/",
                        "classpath:/public/",
                        "classpath:/META-INF/resources/",
                        "classpath:/resources/"
                )
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS));
    }
}