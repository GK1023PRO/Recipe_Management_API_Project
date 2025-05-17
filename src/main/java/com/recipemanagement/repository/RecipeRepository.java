package com.recipemanagement.repository;

import com.recipemanagement.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for recipe data access.
 * Extends MongoDB repository with custom methods.
 */
public interface RecipeRepository extends MongoRepository<Recipe,String> {
    /**
     * Checks if recipe exists by title (case-insensitive)
     * @param title Recipe title to check
     * @return true if title exists
     */
    boolean existsByTitleIgnoreCase(String title);


}
