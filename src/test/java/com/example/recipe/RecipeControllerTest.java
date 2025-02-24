package com.example.recipe;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.recipe.controller.RecipeController;
import com.example.recipe.service.RecipeService;
import com.example.recipe.model.Recipe;
import com.example.recipe.util.AIUtil;
import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RecipeController.class)
@ContextConfiguration(classes = {RecipeController.class, RecipeService.class, AIUtil.class})
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private AIUtil aiUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRecipesByIngredients() throws Exception {
        List<String> ingredients = Arrays.asList("chicken", "rice", "broccoli");
        when(recipeService.generateRecipes(ingredients)).thenReturn(Arrays.asList(new Recipe(), new Recipe()));
        when(aiUtil.callOpenAI(anyString())).thenReturn("mock response");

        mockMvc.perform(get("/api/recipes/v1/recipes/by-ingredients")
                .param("ingredients", ingredients.toArray(new String[0]))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testUploadImage() throws Exception {
        when(recipeService.processImage(any())).thenReturn(Arrays.asList(new Recipe(), new Recipe()));
        when(aiUtil.extractIngredientsFromImage(any())).thenReturn(Arrays.asList("ingredient1", "ingredient2"));

        mockMvc.perform(multipart("/api/recipes/v1/recipes/upload-image")
                .file("file", "mock image content".getBytes()) // mock image byte array
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }
}