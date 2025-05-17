package com.recipemanagement.service;

import com.recipemanagement.dto.RecipeRequest;
import com.recipemanagement.exception.DuplicateRecipeException;
import com.recipemanagement.exception.RecipeNotFoundException;
import com.recipemanagement.model.Recipe;
import com.recipemanagement.repository.CustomRecipeRepository;
import com.recipemanagement.repository.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * Service class for recipe management operations.
 * Contains business logic for recipe CRUD operations and filtering.
 */
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;
    private final CustomRecipeRepository customRecipeRepository;
    private final IdGeneratorService idGeneratorService;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository,ModelMapper modelMapper, MongoTemplate mongoTemplate,CustomRecipeRepository customRecipeRepository, IdGeneratorService idGeneratorService) {
        this.recipeRepository = recipeRepository;
        this.modelMapper=modelMapper;
        this.mongoTemplate = mongoTemplate;
        this.customRecipeRepository=customRecipeRepository;
        this.idGeneratorService = idGeneratorService;
    }






    /**
     * Creates new recipe with validation checks
     * @param request Recipe data
     * @return Created recipe entity
     * @throws DuplicateRecipeException if title exists
     */
    public Recipe createRecipe(RecipeRequest request) {
        // Check for existing recipe with same title
        if (recipeRepository.existsByTitleIgnoreCase(request.getTitle())) {
            throw new DuplicateRecipeException(
                    "Recipe with title '" + request.getTitle() + "' already exists"
            );
        }

        Recipe recipe = modelMapper.map(request, Recipe.class);
        recipe.setId(idGeneratorService.generateRecipeId());
        return recipeRepository.save(recipe);
    }

    public Page<Recipe> getAllRecipes(Pageable pageable){
        return recipeRepository.findAll(pageable);
    }
    /**
     * Retrieves recipe by ID
     * @param id Recipe identifier
     * @return Found recipe entity
     * @throws RecipeNotFoundException if not found
     */
    public Recipe getRecipeById(String id){
        return recipeRepository.findById(id).orElseThrow(()->new RecipeNotFoundException("Recipe not found with id: " + id));
    }

    public Recipe updateRecipe(String id,RecipeRequest request){
        Recipe existing=getRecipeById(id);
        modelMapper.map(request,existing);
        return recipeRepository.save(existing);
    }

    public void deleteRecipe(String id){
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
        recipeRepository.deleteById(id);

    }

    public List<Recipe> filterRecipes(String category, Integer maxCookingTime) {
        return customRecipeRepository.findByFilters(category, maxCookingTime);
    }

}
