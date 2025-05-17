package com.recipemanagement.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for ModelMapper bean setup
 * Provides object mapping capabilities throughout the application
 */
@Configuration
public class ModelMapperConfig {

    public ModelMapperConfig(){

    }

    /**
     * Creates and configures ModelMapper instance
     * @return New ModelMapper bean
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}