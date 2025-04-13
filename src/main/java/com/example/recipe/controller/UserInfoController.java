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
}