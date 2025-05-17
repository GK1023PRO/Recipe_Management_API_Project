package com.recipemanagement.repository;

import com.recipemanagement.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;


import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of custom recipe repository methods.
 * Provides MongoDB query implementations using MongoTemplate.
 */
public class CustomRecipeRepositoryImpl implements CustomRecipeRepository {

    @Autowired
    private final MongoTemplate mongoTemplate;

    public CustomRecipeRepositoryImpl(MongoTemplate mongoTemplate){
         this.mongoTemplate=mongoTemplate;
    }
    /**
     * Finds recipes by category and max cooking time
     * @param category Recipe category filter
     * @param maxCookingTime Maximum cooking time filter
     * @return List of matching recipes
     */
    @Override
    public List<Recipe> findByFilters(String category, Integer maxCookingTime) {
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();

        if (category != null) {
            criteria.add(Criteria.where("category").is(category));
        }

        if (maxCookingTime != null) {
            criteria.add(Criteria.where("cookingTime").lte(maxCookingTime));
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        return mongoTemplate.find(query, Recipe.class);
    }
}
