package com.recipemanagement.config;


import com.recipemanagement.repository.CustomRecipeRepository;
import com.recipemanagement.repository.CustomRecipeRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
/**
 * Configuration class for repository setup.
 * Registers custom repository implementations.
 */
@Configuration
public class RepositoryConfig {

    public RepositoryConfig(){

    }

    @Bean
    public CustomRecipeRepository customRecipeRepository(MongoTemplate mongoTemplate) {
        return new CustomRecipeRepositoryImpl(mongoTemplate);
    }
}