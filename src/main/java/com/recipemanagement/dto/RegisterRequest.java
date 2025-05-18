package com.recipemanagement.dto;


import com.recipemanagement.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
//import lombok.Data;

/**
 * Data Transfer Object for user registration.
 * Contains validation constraints for user registration.
 */
//@Data
public class RegisterRequest {
    @NotBlank private String username;
    @NotBlank @Email private String email;
    @Size(min=6) private String password;
    @NotNull private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
