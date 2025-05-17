package com.recipemanagement.config;

import com.recipemanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central configuration class for application-wide beans.
 * Configures JWT utility with properties from application configuration.
 *
 * @author Georges ElKhoury
 * @version 1.0
 * @since 2024-03-20
 */
@Configuration
public class AppConfig {

    public AppConfig(){

    }

    // Inject the secret from properties
    /**
     * JWT secret key injected from application properties
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Inject the expiration time from properties, with a default value
    /**
     * JWT expiration time in milliseconds with default value
     */
    @Value("${jwt.expirationMs:86400000}")
    private int jwtExpirationMs;

    /**
     * Creates and configures JWT utility bean
     * @return Configured JwtUtil instance with secret and expiration
     */
    @Bean
    public JwtUtil jwtUtil() {
        // Pass both the secret and expiration time to JwtUtil's constructor
        return new JwtUtil(jwtSecret, jwtExpirationMs);
    }
}