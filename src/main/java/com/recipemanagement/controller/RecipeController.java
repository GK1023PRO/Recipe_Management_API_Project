package com.recipemanagement.controller;

import com.recipemanagement.dto.RecipeRequest;
import com.recipemanagement.model.Recipe;
import com.recipemanagement.service.AuthService;
import com.recipemanagement.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for recipe management operations.
 * Provides CRUD endpoints for recipes with role-based access control.
 * Includes API documentation endpoint for developer reference.
 */
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;
    private final AuthService authService;

    public RecipeController(RecipeService recipeService,AuthService authService){
        this.recipeService=recipeService;
        this.authService=authService;

    }

    /**
     * Creates a new recipe (Admin only)
     * @param request Recipe data transfer object
     * @return ResponseEntity with created recipe
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody RecipeRequest request) {
        authService.checkIfAdmin(); // <-- Add this
        Recipe created = recipeService.createRecipe(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Retrieves paginated list of all recipes
     * @param pageable Pagination and sorting parameters
     * @return Page of recipes
     */
    @GetMapping("")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public Page<Recipe> getAllRecipes(Pageable pageable) {
        if (pageable.getPageNumber() < 0) {
            pageable = PageRequest.of(0, pageable.getPageSize());
        }
        return recipeService.getAllRecipes(pageable);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public Recipe getRecipeById(@PathVariable String id) {
        return recipeService.getRecipeById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable String id, @RequestBody RecipeRequest request) {
        authService.checkIfAdmin(); // <-- Add this
        Recipe updated = recipeService.updateRecipe(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRecipe(@PathVariable String id) {
        recipeService.deleteRecipe(id);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public List<Recipe> filterRecipes(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer maxCookingTime) {
        return recipeService.filterRecipes(category, maxCookingTime);
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/recipes")
    public String createRecipe(
            @RequestParam("recipeImage") MultipartFile file,
            @RequestParam String targetUsers,
            @RequestParam String instructions,
            @RequestParam int cookingTime,
            @RequestParam String category) {
        // Your processing logic
        return "redirect:/";
    }

    @GetMapping("/docs")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Map<String, Map<String, Object>>> getApiDocumentation() {
        Map<String, Map<String, Object>> endpoints = new HashMap<>();

        endpoints.put("createRecipe", createEndpointMap(
                "POST",
                "/api/recipes",
                "Create a new recipe",
                "ADMIN",
                List.of(paramMap("body", "RecipeRequest", "Recipe details", true)),
                HttpStatus.CREATED.value()
        ));

        endpoints.put("getAllRecipes", createEndpointMap(
                "GET",
                "/api/recipes",
                "Get all recipes with pagination and sorting",
                "CLIENT, ADMIN",
                List.of(paramMap("pageable", "Pageable", "Pagination/sorting parameters (page, size, sort)", true)),
                HttpStatus.OK.value()
        ));

        endpoints.put("getRecipeById", createEndpointMap(
                "GET",
                "/api/recipes/{id}",
                "Get recipe by ID",
                "CLIENT, ADMIN",
                List.of(paramMap("id", "String", "Recipe ID", true)),
                HttpStatus.OK.value()
        ));

        endpoints.put("updateRecipe", createEndpointMap(
                "PUT",
                "/api/recipes/{id}",
                "Update recipe by ID",
                "ADMIN",
                List.of(
                        paramMap("id", "String", "Recipe ID", true),
                        paramMap("body", "RecipeRequest", "Updated recipe details", true)
                ),
                HttpStatus.OK.value()
        ));

        endpoints.put("deleteRecipe", createEndpointMap(
                "DELETE",
                "/api/recipes/{id}",
                "Delete recipe by ID",
                "ADMIN",
                List.of(paramMap("id", "String", "Recipe ID", true)),
                HttpStatus.NO_CONTENT.value()
        ));

        endpoints.put("filterRecipes", createEndpointMap(
                "GET",
                "/api/recipes/filter",
                "Filter recipes by category and/or max cooking time",
                "CLIENT, ADMIN",
                List.of(
                        paramMap("category", "String", "Recipe category", false),
                        paramMap("maxCookingTime", "Integer", "Maximum cooking time in minutes", false)
                ),
                HttpStatus.OK.value()
        ));

        endpoints.put("createRecipeWithImage", createEndpointMap(
                "POST",
                "/api/recipes/recipes",
                "Create recipe with image upload",
                "None specified",
                List.of(
                        paramMap("recipeImage", "MultipartFile", "Recipe image file", true),
                        paramMap("targetUsers", "String", "Target users", true),
                        paramMap("instructions", "String", "Cooking instructions", true),
                        paramMap("cookingTime", "int", "Cooking time in minutes", true),
                        paramMap("category", "String", "Recipe category", true)
                ),
                302
        ));

        return ResponseEntity.ok(endpoints);
    }

    private Map<String, Object> createEndpointMap(String method, String path, String description, String roles,
                                                  List<Map<String, Object>> parameters, int responseStatus) {
        Map<String, Object> endpoint = new HashMap<>();
        endpoint.put("method", method);
        endpoint.put("path", path);
        endpoint.put("description", description);
        endpoint.put("requiredRoles", roles);
        endpoint.put("parameters", parameters);
        endpoint.put("responseStatus", responseStatus);
        return endpoint;
    }

    private Map<String, Object> paramMap(String name, String type, String description, boolean required) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", name);
        param.put("type", type);
        param.put("description", description);
        param.put("required", required);
        return param;
    }
}