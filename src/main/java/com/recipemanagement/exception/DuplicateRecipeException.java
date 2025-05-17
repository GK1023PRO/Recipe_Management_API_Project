package com.recipemanagement.exception;
/**
 * Exception thrown when duplicate recipe creation is attempted.
 */
public class DuplicateRecipeException extends RuntimeException {
    public DuplicateRecipeException(String message) {
        super(message);
    }
}
