package com.recipemanagement.exception;

/**
 * Exception thrown when username is not found during authentication.
 */
public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String message) {
        super(message);
    }
}
