package com.example.recipe.model;

import java.util.List;

public class UserPrompt {
    private int id;
    private List<String> user_ask;
    private List<Recipe> user_recipe;

    // Getters and Setters
    public int getId() { 
        return id; 
    }

    public void setId(int id) { 
        this.id = id; 
    }

    public List<String> getUser_ask() { 
        return user_ask; 
    }

    public void setUser_ask(List<String> user_ask) { 
        this.user_ask = user_ask; 
    }

    public List<Recipe> getUser_recipe() { 
        return user_recipe; 
    }

    public void setUser_recipe(List<Recipe> user_recipe) { 
        this.user_recipe = user_recipe; 
    }
}
