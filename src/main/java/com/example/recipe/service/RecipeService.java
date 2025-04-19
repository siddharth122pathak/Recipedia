package com.example.recipe.service;

import com.example.recipe.model.Recipe;
import com.example.recipe.model.UserPrompt;
import com.example.recipe.util.AIUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.time.LocalDateTime;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class RecipeService {

    private final AIUtil aiUtil;

    @Value("${supabase.api.url}")
    private String supabaseApiUrl;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();

    public RecipeService(AIUtil aiUtil) {
        this.aiUtil = aiUtil;
    }

    public List<Recipe> generateRecipes(List<String> ingredients) {
        String prompt = createPrompt(ingredients);
        String response = aiUtil.callOpenAI(prompt);
        return parseRecipes(response);
    }

    public List<Recipe> processImage(MultipartFile file) {
        try {
            byte[] imageBytes = file.getBytes();
            List<String> ingredients = aiUtil.extractIngredientsFromImage(imageBytes);
            return generateRecipes(ingredients);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private String createPrompt(List<String> ingredients) {
        return "Generate 2 recipes using the following ingredients: " + String.join(", ", ingredients);
    }

    private List<Recipe> parseRecipes(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String content = objectMapper.readTree(response)
                                         .path("choices")
                                         .get(0)
                                         .path("message")
                                         .path("content")
                                         .asText();
    
            System.out.println("Parsed Content: " + content); // Debugging
    
            String[] recipeStrings = content.split("\n\nRecipe ");
            List<Recipe> recipes = new ArrayList<>();
    
            for (String recipeString : recipeStrings) {
                String[] lines = recipeString.split("\n");
                if (lines.length > 1) {
                    Recipe recipe = new Recipe();
                    
                    recipe.setTitle(lines[0].replaceAll("^\\d+\\.\\s*", "").trim());
    
                    // Extract ingredients and instructions
                    List<String> ingredientsList = new ArrayList<>();
                    List<String> instructionsList = new ArrayList<>();
                    boolean isIngredients = false;
    
                    for (int i = 1; i < lines.length; i++) {
                        String line = lines[i].trim();
    
                        if (line.toLowerCase().startsWith("ingredients:")) {
                            isIngredients = true;
                            ingredientsList.add("");
                            continue;
                        }
    
                        if (line.toLowerCase().startsWith("instructions:")) {
                            isIngredients = false;
                            continue;
                        }
    
                        if (isIngredients) {
                            ingredientsList.add(line);
                        } else {
                            instructionsList.add(line);
                        }
                    }
    
                    recipe.setIngredients(ingredientsList);
                    recipe.setInstructions(String.join("\n", instructionsList)); // Converts list to a single string
    
                    if (!recipe.getInstructions().isEmpty()) {
                        recipes.add(recipe);
                    }
                }
            }
            System.out.println("Parsed Recipes: " + recipes); // Debugging
            return recipes;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void storeUserPrompt(int userId, List<String> ingredients, List<Recipe> recipes) {
        try {
            String url = supabaseApiUrl + "/rest/v1/user_prompt?apikey=" + supabaseApiKey;
            
            // Create UserPrompt object
            UserPrompt userPrompt = new UserPrompt();
            userPrompt.setId(userId);
            userPrompt.setUser_ask(ingredients);
            userPrompt.setUser_recipe(recipes);
            userPrompt.setCreated_at(LocalDateTime.now().toString()); // Set current DateTime as the created_at
            
            // Convert UserPrompt to JSON
            String payload = new ObjectMapper().writeValueAsString(userPrompt);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Failed to store user prompt: " + response.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error storing user prompt: " + e.getMessage());
        }
    }

    public List<UserPrompt> getUserPromptsByUserId(int userId) {
        List<UserPrompt> userPrompts = new ArrayList<>();
        try {
            String url = supabaseApiUrl + "/rest/v1/user_prompt?id=eq." + userId + "&apikey=" + supabaseApiKey;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, entity);
    
            if (response.getStatusCode().is2xxSuccessful()) {
                JSONArray jsonArray = new JSONArray(response.getBody());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject userPromptObj = jsonArray.getJSONObject(i);
                    UserPrompt userPrompt = new UserPrompt();
    
                    userPrompt.setId(userPromptObj.getInt("id"));
                    System.out.println("User Prompt ID: " + userPromptObj.getInt("id")); // Debugging
                    userPrompt.setUser_ask(Arrays.asList(userPromptObj.getString("user_ask").replaceAll("[\\[\\]\"]", "").split(",")));
                    System.out.println("User Ask: " + userPromptObj.getString("user_ask")); // Debugging
                    userPrompt.setUser_recipe(objectMapper.readValue(userPromptObj.getString("user_recipe"), new TypeReference<List<Recipe>>() {}));
                    System.out.println("User Recipe: " + userPromptObj.getString("user_recipe")); // Debugging
                    userPrompt.setCreated_at(userPromptObj.getString("created_at"));
                    System.out.println("Created At: " + userPromptObj.getString("created_at")); // Debugging
    
                    userPrompts.add(userPrompt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userPrompts;
    }
}
