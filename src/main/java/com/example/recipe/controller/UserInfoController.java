package com.example.recipe.controller;

import com.example.recipe.service.UserInfoService;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        System.out.println("Received username: " + username);
        System.out.println("Received password: " + password);

        Integer userId = userInfoService.validateUser(username, password);

        if (userId != null) {
            // Store user ID in the session
            request.getSession().setAttribute("userId", userId);
            return "redirect:/main.html";
        } else {
            return "redirect:/index.html?error=Invalid username or password";
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

    @PostMapping("/forget-password")
    public ResponseEntity<Map<String, String>> forgetPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String newPassword = request.get("newPassword");

        boolean isUpdated = userInfoService.resetPassword(username, newPassword);

        if (isUpdated) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password reset successfully.");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid username.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}