package com.recipemanagement.exception;

import com.recipemanagement.controller.RecipeController;
import com.recipemanagement.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Exception thrown when requested recipe is not found
 * @see RecipeService
 * @see RecipeController
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecipeNotFoundException extends RuntimeException {
    /**
     * Constructs exception with error message
     * @param message Description of the error
     */
    public RecipeNotFoundException(String message) {
        super(message);
    }
}
