package com.example.recipe.controller;

import com.example.recipe.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        System.out.println("Received username: " + username);
        System.out.println("Received password: " + password);

        if (userInfoService.validateUser(username, password)) {
            return "redirect:/main.html"; // Redirect to main.html after successful login
        } else {
            return "redirect:/index.html?error=Invalid username or password"; // Redirect back to index.html with error
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam String firstName, @RequestParam String lastName,
                        @RequestParam String username, @RequestParam String email,
                        @RequestParam String password) {
        
        // Generate random 8-digit ID
        int randomId = (int) (Math.random() * 90000000) + 10000000; // Generates a random number between 10000000 and 99999999
        
        if (userInfoService.registerUser(randomId, firstName, lastName, username, email, password)) {
            return "redirect:/index.html?success=Registration successful! Please login with your credentials.";
        } else {
            return "redirect:/register.html?error=Registration failed. Please try again.";
        }
    }
}