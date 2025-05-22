package com.recipemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//hello
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
