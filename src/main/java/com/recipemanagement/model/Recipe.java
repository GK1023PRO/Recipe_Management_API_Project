package com.recipemanagement.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
//import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * Entity class representing a recipe.
 * Maps to MongoDB collection with validation constraints.
 */
//@Data
@Document(collection = "recipes")
public class Recipe {
    /**
     * Unique identifier for recipe
     */
    @Id
    private String id;
    /**
     * Unique recipe title
     */
    @NotBlank
    @Indexed(unique = true)
    private String title;
    @NotEmpty
    private List<String> ingredients;
    private String instructions;
    private Integer cookingTime;
    private String category;

    public Recipe(){

    }
    public Recipe(String title,List<String> ingredients,String instructions,Integer cookingTime,String category){
        this.title=title;
        this.ingredients=ingredients;
        this.instructions=instructions;
        this.cookingTime=cookingTime;
        this.category=category;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", ingredients=" + ingredients +
                ", cookingTime=" + cookingTime +
                ", category='" + category + '\'' +
                '}';
    }


}
