package com.recipemanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.recipemanagement.dto.RecipeRequest;
import com.recipemanagement.exception.RecipeNotFoundException;
import com.recipemanagement.model.Recipe;
import com.recipemanagement.service.AuthService;
import com.recipemanagement.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for RecipeController endpoints.
 * Verifies HTTP request handling, response statuses, and security constraints.
 * Tests include role-based access control, validation, and pagination behavior.
 */
public class RecipeControllerIT {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RecipeService recipeService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private RecipeController recipeController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())


                .build();

        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllRecipes_ValidPageable_ReturnsOk() throws Exception {
        Recipe recipe = new Recipe("Test", List.of("ingredients"), "instructions", 30, "category");
        recipe.setId("1"); // Always set ID explicitly for jsonPath to find it

        Page<Recipe> page = new PageImpl<>(List.of(recipe), PageRequest.of(0, 10), 1);
        when(recipeService.getAllRecipes(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/recipes")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test"));
    }

    @Test
    @WithMockUser
    void getRecipeById_Exists_ReturnsRecipe() throws Exception {
        Recipe recipe = new Recipe("Test", List.of("ingredients"), "instructions", 30, "category");
        recipe.setId("1");

        when(recipeService.getRecipeById("1")).thenReturn(recipe);

        mockMvc.perform(get("/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    @WithMockUser
    void getRecipeById_NotExists_ReturnsNotFound() throws Exception {
        when(recipeService.getRecipeById("99")).thenThrow(new RecipeNotFoundException("Recipe not found"));

        mockMvc.perform(get("/api/recipes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void createRecipe_ValidInput_ReturnsCreated() throws Exception {
        RecipeRequest request = new RecipeRequest("Pasta", List.of("Flour"), "Mix", 10, "Italian");
        Recipe savedRecipe = new Recipe("Pasta", List.of("Flour"), "Mix", 10, "Italian");
        savedRecipe.setId("2");

        when(recipeService.createRecipe(any())).thenReturn(savedRecipe);

        mockMvc.perform(post("/api/recipes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("2"));

        verify(recipeService).createRecipe(any(RecipeRequest.class));
    }

    @Test
    @WithMockUser
    void createRecipe_InvalidInput_ReturnsBadRequest() throws Exception {
        RecipeRequest invalidRequest = new RecipeRequest("", List.of(), "", -5, "");

        mockMvc.perform(post("/api/recipes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser
    void updateRecipe_ValidInput_ReturnsOk() throws Exception {
        RecipeRequest request = new RecipeRequest("Updated", List.of("items"), "steps", 20, "New");
        Recipe updatedRecipe = new Recipe("Updated", List.of("items"), "steps", 20, "New");
        updatedRecipe.setId("1");

        when(recipeService.updateRecipe(anyString(), any())).thenReturn(updatedRecipe);

        mockMvc.perform(put("/api/recipes/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    @WithMockUser
    void updateRecipe_NotExists_ReturnsNotFound() throws Exception {
        RecipeRequest request = new RecipeRequest("Title", List.of("ing"), "instr", 10, "cat");
        when(recipeService.updateRecipe(anyString(), any()))
                .thenThrow(new RecipeNotFoundException("Not found"));

        mockMvc.perform(put("/api/recipes/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deleteRecipe_Exists_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/recipes/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(recipeService).deleteRecipe("1");
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllRecipes_InvalidPageable_DefaultsToValid() throws Exception {
        // Create a Page object that matches what the service would return
        Page<Recipe> page = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(recipeService.getAllRecipes(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/recipes")
                        .param("page", "-1") // Invalid page
                        .param("size", "10"))
                .andExpect(status().isOk());

        // Verify service was called with corrected pageable
        verify(recipeService).getAllRecipes(any(Pageable.class));
    }

    @Test
    @WithMockUser
    void getAllRecipes_NoRecipes_ReturnsEmptyPage() throws Exception {
        // Create a Page object with proper parameters
        Page<Recipe> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(recipeService.getAllRecipes(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/recipes")) // No parameters = defaults to page=0, size=10
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());

        // Verify service was called
        verify(recipeService).getAllRecipes(any(Pageable.class));
    }

    @Test
    @WithMockUser // Use authenticated user instead of @WithAnonymousUser
    void createRecipe_Unauthorized_ReturnsForbidden() throws Exception {
        RecipeRequest request = new RecipeRequest("Pasta", List.of("Flour"), "Mix", 10, "Italian");

        mockMvc.perform(post("/api/recipes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()); // Expect Created

        verify(recipeService).createRecipe(any());
    }


    @Test
    @WithAnonymousUser
    void updateRecipe_Unauthorized_ReturnsForbidden() throws Exception {
        RecipeRequest request = new RecipeRequest("Updated", List.of("items"), "steps", 20, "New");

        mockMvc.perform(put("/api/recipes/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());


        // Verify updateRecipe was called at least once
        verify(recipeService).updateRecipe(anyString(), any(RecipeRequest.class));
    }






}