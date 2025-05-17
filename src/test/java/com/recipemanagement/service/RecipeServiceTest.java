/**
 * Service layer classes containing business logic and operations.
 */
package com.recipemanagement.service;

import com.recipemanagement.dto.RecipeRequest;
import com.recipemanagement.exception.RecipeNotFoundException;
import com.recipemanagement.model.Recipe;
import com.recipemanagement.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
/**
 * Unit tests for RecipeService business logic.
 * Verifies service layer operations including CRUD functionality, ID generation,
 * and proper repository interaction through mocking.
 */
@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IdGeneratorService idGeneratorService;

    @InjectMocks
    private RecipeService recipeService;

    private final ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() throws Exception {
        // Inject ModelMapper into RecipeService using reflection
        Field modelMapperField = RecipeService.class.getDeclaredField("modelMapper");
        modelMapperField.setAccessible(true);
        modelMapperField.set(recipeService, modelMapper);
    }

    @Test
    void createRecipe_ValidRequest_GeneratesIdAndSaves() {
        RecipeRequest request = new RecipeRequest("Pizza", List.of("Dough"), "Bake", 15, "Italian");
        String generatedId = UUID.randomUUID().toString();

        // Mock ID generation
        when(idGeneratorService.generateRecipeId()).thenReturn(generatedId);
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> {
            Recipe recipe = invocation.getArgument(0);
            assertEquals(generatedId, recipe.getId());
            return recipe;
        });

        Recipe result = recipeService.createRecipe(request);

        assertNotNull(result.getId());
        assertEquals("Pizza", result.getTitle());
        verify(idGeneratorService).generateRecipeId();
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    void getRecipeById_Exists_ReturnsRecipe() {
        Recipe recipe = new Recipe("Test", List.of("ing"), "instr", 10, "cat");
        recipe.setId("1");
        // Mock repository to return Recipe directly
        when(recipeRepository.findById("1")).thenReturn(Optional.of(recipe));

        Recipe result = recipeService.getRecipeById("1");

        assertNotNull(result);
        assertEquals("Test", result.getTitle());
    }

    @Test
    void getRecipeById_NotExists_ThrowsException() {
        // Mock repository to return empty
        when(recipeRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.getRecipeById("99"));
    }

    @Test
    void updateRecipe_Exists_UpdatesAndReturnsRecipe() {
        Recipe existing = new Recipe("Old", List.of("a"), "old", 5, "cat");
        existing.setId("1");
        RecipeRequest update = new RecipeRequest("New", List.of("b"), "new", 10, "new");

        // Mock repository to return existing recipe
        when(recipeRepository.findById("1")).thenReturn(Optional.of(existing));
        when(recipeRepository.save(any(Recipe.class))).thenAnswer(inv -> inv.getArgument(0));

        Recipe updated = recipeService.updateRecipe("1", update);

        assertEquals("New", updated.getTitle());
        assertEquals(10, updated.getCookingTime());
        verify(recipeRepository).save(existing);
    }

    @Test
    void updateRecipe_NotExists_ThrowsException() {
        RecipeRequest update = new RecipeRequest("New", List.of("b"), "new", 10, "new");
        // Mock repository to return empty
        when(recipeRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.updateRecipe("99", update));
    }

    @Test
    void deleteRecipe_Exists_DeletesFromRepository() {
        Recipe recipe = new Recipe("Test", List.of("ing"), "instr", 10, "cat");
        recipe.setId("1");

        when(recipeRepository.findById("1")).thenReturn(Optional.of(recipe));
        doNothing().when(recipeRepository).deleteById("1");

        recipeService.deleteRecipe("1");

        verify(recipeRepository).findById("1");
        verify(recipeRepository).deleteById("1");
    }
    @Test
    void deleteRecipe_NotExists_ThrowsException() {
        when(recipeRepository.findById("99")).thenReturn(Optional.empty());

        assertThrows(RecipeNotFoundException.class, () -> recipeService.deleteRecipe("99"));

        verify(recipeRepository).findById("99");
        verify(recipeRepository, never()).deleteById(anyString());
    }

    @Test
    void getAllRecipes_WithRecipes_ReturnsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Recipe recipe = new Recipe("Test", List.of("ing"), "instr", 10, "cat");
        Page<Recipe> expectedPage = new PageImpl<>(List.of(recipe));

        when(recipeRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Recipe> result = recipeService.getAllRecipes(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Test", result.getContent().get(0).getTitle());
        verify(recipeRepository).findAll(pageable);
    }

    @Test
    void getAllRecipes_EmptyPage_ReturnsEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recipe> emptyPage = new PageImpl<>(List.of());

        when(recipeRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<Recipe> result = recipeService.getAllRecipes(pageable);

        assertTrue(result.isEmpty());
        verify(recipeRepository).findAll(pageable);
    }
}