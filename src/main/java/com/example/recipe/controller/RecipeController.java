package com.example.recipe.controller;

import com.example.recipe.model.Recipe;
import com.example.recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/recipes/v1/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/by-ingredients")
    public ResponseEntity<List<Recipe>> getRecipesByIngredients(@RequestParam List<String> ingredients) {
        List<Recipe> recipes = recipeService.generateRecipes(ingredients);
        return ResponseEntity.ok(recipes);
    }

    @PostMapping("/upload-image")
    public ResponseEntity<List<Recipe>> uploadImage(@RequestParam("file") MultipartFile file) {
        List<Recipe> recipes = recipeService.processImage(file);
        return ResponseEntity.ok(recipes);
    }
}