package com.example.recipe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

@Service
public class UserInfoService {

    @Value("${supabase.api.url}")
    private String supabaseApiUrl;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean validateUser(String username, String password) {
        String url = supabaseApiUrl + "/rest/v1/user_info?username=eq." + username + "&password=eq." + password + "&apikey=" + supabaseApiKey;

        System.out.println("Generated URL: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseApiKey); // Add the Bearer token

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, entity);
            System.out.println("Supabase API Response: " + response.getBody());

            // Check if the response body is empty or equals "[]"
            if (response.getBody() == null || response.getBody().equals("[]")) {
                System.out.println("No matching user found.");
                return false;
            }

            return true; // User exists
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return false;
        }
    }

    public boolean registerUser(int id, String firstName, String lastName, String username, String email, String password) {
        try {
            String url = supabaseApiUrl + "/rest/v1/user_info?apikey=" + supabaseApiKey;
            
            // Create JSON payload
            String payload = "{\"id\":\"" + id + "\",\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + 
                            "\",\"username\":\"" + username + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}