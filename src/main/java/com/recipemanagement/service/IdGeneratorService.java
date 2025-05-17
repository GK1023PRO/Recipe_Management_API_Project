package com.recipemanagement.service;

import com.recipemanagement.model.Role;
import com.recipemanagement.model.User;
import com.recipemanagement.repository.RecipeRepository;
import com.recipemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Service for generating unique IDs for entities.
 * Implements sequence-based ID generation for users and recipes.
 */
@Service
public class IdGeneratorService {

    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    private AtomicInteger lastRecipeNumber = null;
    private AtomicInteger lastClientNumber = null;
    private AtomicInteger lastAdminNumber = null;

    @Autowired
    public IdGeneratorService(MongoTemplate mongoTemplate,
                              UserRepository userRepository,
                              RecipeRepository recipeRepository) {
        this.mongoTemplate = mongoTemplate;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    /**
     * Generates a recipe ID like 1, 2, 3...
     */
    public String generateRecipeId() {
        if (lastRecipeNumber == null) {
            Query query = new Query();
            query.fields().include("_id");

            int maxNumber = 0;
            var recipes = mongoTemplate.find(query, com.recipemanagement.model.Recipe.class);

            for (var recipe : recipes) {
                String id = recipe.getId();
                try {
                    int number = Integer.parseInt(id);
                    maxNumber = Math.max(maxNumber, number);
                } catch (NumberFormatException e) {
                    // Ignore non-numeric IDs
                }
            }

            lastRecipeNumber = new AtomicInteger(maxNumber);
        }

        return String.valueOf(lastRecipeNumber.incrementAndGet());
    }

    /**
     * Generates a user ID like 1, 2, 3... independently for each role
     */
    public String generateUserId() {
        if (lastClientNumber == null) {
            lastClientNumber = new AtomicInteger(getMaxUserNumberGlobal());
        }
        int nextNumber = lastClientNumber.incrementAndGet();
        return String.valueOf(nextNumber);
    }


    private int getMaxUserNumberGlobal() {
        Query query = new Query();
        query.fields().include("_id");

        int maxNumber = 0;
        var users = mongoTemplate.find(query, User.class);

        for (var user : users) {
            String id = user.getId();
            if (id != null) {
                try {
                    int number = Integer.parseInt(id);
                    maxNumber = Math.max(maxNumber, number);
                } catch (NumberFormatException e) {
                    // Skip if _id is not numeric (legacy cases)
                }
            }
        }

        return maxNumber;
    }
}
