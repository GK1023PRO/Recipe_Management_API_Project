package com.recipemanagement.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .info(new Info()
                        .title("Recipe Management API")
                        .version("1.0")
                        .description("API for managing recipes (secured with JWT)")
                );
    }

    @Bean
    public GroupedOpenApi recipeApi() {
        return GroupedOpenApi.builder()
                .group("Recipe API")
                .packagesToScan("com.recipemanagement.controller") // Your controller package
                .pathsToMatch("/api/recipes/**") // Your API endpoints
                .build();
    }
}