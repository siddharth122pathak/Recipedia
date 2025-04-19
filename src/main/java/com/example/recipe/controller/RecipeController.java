package com.example.recipe.controller;

import com.example.recipe.model.Recipe;
import com.example.recipe.model.UserPrompt;
import com.example.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/recipes/v1/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/by-ingredients")
    public ResponseEntity<List<Recipe>> getRecipesByIngredients(@RequestParam List<String> ingredients, HttpServletRequest request) {
        List<Recipe> recipes = recipeService.generateRecipes(ingredients);
        
        // Get user ID from the session
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        
        if (userId != null) {
            recipeService.storeUserPrompt(userId, ingredients, recipes);
        }
        
        return ResponseEntity.ok(recipes);
    }

    @PostMapping("/upload-image")
    public ResponseEntity<List<Recipe>> uploadImage(@RequestParam("file") MultipartFile file) {
        List<Recipe> recipes = recipeService.processImage(file);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<UserPrompt>> getrecent(HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<UserPrompt> userPrompts = recipeService.getUserPromptsByUserId(userId);
        return ResponseEntity.ok(userPrompts);
    }
}
