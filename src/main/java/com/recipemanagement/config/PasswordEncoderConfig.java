package com.recipemanagement.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * Configuration class for application-wide beans and settings.
 * Configures JWT utility with properties from application configuration.
 */
@Configuration
public class PasswordEncoderConfig {
    public PasswordEncoderConfig(){

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}