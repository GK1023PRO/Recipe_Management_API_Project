package com.recipemanagement.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS configuration for OpenAPI/Swagger documentation
 * Enables cross-origin requests for API documentation endpoints
 */
@Configuration
public class OpenApiCorsConfig implements WebMvcConfigurer {
    public OpenApiCorsConfig(){

    }
    /**
     * Configures CORS mappings for all endpoints
     * @param registry CORS registry to configure
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }
}