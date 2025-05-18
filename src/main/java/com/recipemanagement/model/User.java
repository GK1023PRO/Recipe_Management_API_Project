package com.recipemanagement.model;

import jakarta.validation.constraints.NotBlank;
//import lombok.Data;
//import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Entity class representing application users.
 * Implements Spring Security UserDetails for authentication.
 * Maps to MongoDB collection with validation constraints.
 */
//@Data
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String username;

    @NotBlank
    //@ToString.Exclude
    private String password;

    @NotBlank
    @Indexed(unique = true)
    private String email;
    /**
     * User's roles in the system
     */
    //@ToString.Exclude
    @JsonIgnore
    private List<Role> roles ;

    // Default constructor: Assigns CLIENT role by default
    public User() {
        this.roles = new ArrayList<>(List.of(Role.CLIENT));
    }

    // Constructor for manual role assignment (admins only)
    public User(String id, String username, String password, String email, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = (roles != null && !roles.isEmpty())
                ? new ArrayList<>(roles) // Allow multiple roles
                : new ArrayList<>(List.of(Role.CLIENT)); // Default to CLIENT
    }

    // Constructor for registration with a single role choice
    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = new ArrayList<>(List.of(
                role != null ? role : Role.CLIENT // Default to CLIENT if role is null
        ));
    }

    // New constructor for multiple roles (optional)
    public User(String username, String password, String email, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = (roles != null && !roles.isEmpty())
                ? new ArrayList<>(roles)
                : new ArrayList<>(List.of(Role.CLIENT));
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Role> getRoles() { return roles; }
    public void setRoles(List roles) { this.roles = roles; }


    // 👇 Add this method to convert roles to authorities
    /**
     * Converts roles to Spring Security authorities
     * @return Collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
    }

    // 👇 Required methods from UserDetails (defaults for simplicity)
    @Override
    public boolean isAccountNonExpired() {
        return true; // Set to false for account expiration logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Set to false for locked accounts
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Set to false for expired credentials
    }

    @Override
    public boolean isEnabled() {
        return true; // Set false to disable users
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                // Exclude roles and password
                '}';
    }



}