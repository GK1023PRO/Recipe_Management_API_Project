package com.recipemanagement.repository;

import com.recipemanagement.model.Recipe;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

/**
 * Custom repository interface for recipe queries.
 * Defines custom search methods for recipe data.
 */
public interface CustomRecipeRepository {
    List<Recipe> findByFilters(String category, Integer maxCookingTime);
}
