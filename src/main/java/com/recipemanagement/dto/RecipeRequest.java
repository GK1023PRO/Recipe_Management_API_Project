package com.recipemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
//import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object for recipe operations.
 * Contains validation constraints for recipe creation/update.
 */
//@Data
public class RecipeRequest {
    /**
     * Recipe title (required)
     */
    @NotBlank(message = "Title is required")
    private String title;
    /**
     * List of ingredients (minimum 1 required)
     */
    @NotNull(message = "Ingredients cannot be null")
    @Size(min = 1, message = "At least one ingredient is required")
    private List<String> ingredients;

    @NotBlank(message = "Instructions are required")
    private String instructions;

    @NotNull(message = "Cooking time is required")
    private Integer cookingTime;

    private String category;

    public RecipeRequest(){

    }

    public RecipeRequest(String title,List<String>ingredients,String instructions,Integer cookingTime,String category){
        this.title=title;
        this.ingredients=ingredients;
        this.instructions=instructions;
        this.cookingTime=cookingTime;
        this.category=category;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public List<String> getIngredients(){
        return ingredients;
    }

    public void setIngredients(List<String> ingredients){
        this.ingredients = ingredients;
    }

    public String getInstructions(){
        return instructions;
    }

    public void setInstructions(String instructions){
        this.instructions = instructions;
    }

    public Integer getCookingTime(){
        return cookingTime;
    }

    public void setCookingTime(Integer cookingTime){
        this.cookingTime = cookingTime;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
