package com.recipemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Recipe Management application.
 * <p>
 * This class bootstraps the Spring Boot application using
 * {@link SpringApplication}. It delegates execution to the
 * {@link RecipeManagementApplication} class.
 * </p>
 *
 * <p><strong>Usage:</strong></p>
 * <pre>{@code
 * java -jar recipe-management.jar
 * }</pre>
 *
 * @author Georges Khoury
 * @version 1.0
 * @since 2025-05-23
 */
@SpringBootApplication
public class Main {
    /**
     * Main application runner
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(RecipeManagementApplication.class, args);
    }
}
