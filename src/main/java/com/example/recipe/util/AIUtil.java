package com.example.recipe.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Base64;
import java.util.List;

@Component
public class AIUtil {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions"; // Updated URL

    public String callOpenAI(String ingredients) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"system\", \"content\": \"Generate recipes using the following ingredients: " + ingredients + "\"}], \"max_tokens\": 500}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_API_URL, entity, String.class);

        System.out.println("API Response: " + response.getBody()); // Debugging
        return response.getBody();
    }

    public List<String> extractIngredientsFromImage(byte[] imageBytes) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Encode the image bytes to Base64
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes);

        String requestBody = "{"
                + "\"model\": \"gpt-4o-mini\","
                + "\"messages\": [{\"role\": \"user\", \"content\": ["
                + "{\"type\": \"text\", \"text\": \"What's in this image?\"},"
                + "{\"type\": \"image_url\", \"image_url\": {\"url\": \"data:image/jpeg;base64," + encodedImage + "\"}}"
                + "]}],"
                + "\"max_tokens\": 500"
                + "}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(OPENAI_API_URL, entity, String.class);

        System.out.println("API Response: " + response.getBody()); // Debugging
        // Parse the response to extract ingredients
        // This is a placeholder for actual implementation
        // Assuming the response body contains a JSON array of ingredients
        return List.of(); // Replace with actual parsing logic
    }
}