package com.recipemanagement.dto;

import jakarta.validation.constraints.NotBlank;
//import lombok.Data;

/**
 * Data Transfer Object for authentication requests.
 * Contains validation constraints for login input.
 */
//@Data
public class LoginRequest {
    /**
     * User's unique username
     */
    @NotBlank private String username;
    /**
     * User's password
     */
    @NotBlank private String password;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
