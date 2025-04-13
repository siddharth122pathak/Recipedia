package com.example.recipe.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @GetMapping("/")
    public String home() {
        logger.info("Redirecting to index.html (login page)");
        return "redirect:/index.html"; // Redirect to the renamed index.html (previously login.html)
    }

    @GetMapping("/main")
    public String main() {
        logger.info("Serving main.html (home page)");
        return "forward:/main.html"; // Serve the renamed main.html (previously index.html)
    }
}