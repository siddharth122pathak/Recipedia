package com.example.recipe.service;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.recipe.model.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;

@Service
public class UserInfoService {

    @Value("${supabase.api.url}")
    private String supabaseApiUrl;

    @Value("${supabase.api.key}")
    private String supabaseApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Integer validateUser(String username, String password) {
        try {
            String url = supabaseApiUrl + "/rest/v1/user_info?username=eq." + username + "&password=eq." + password + "&apikey=" + supabaseApiKey;
    
            System.out.println("Generated URL: " + url);
    
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
    
            HttpEntity<String> entity = new HttpEntity<>(headers);
    
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, entity);
            System.out.println("Supabase API Response: " + response.getBody());
    
            // Check if the response body is empty or equals "[]"
            if (response.getBody() == null || response.getBody().equals("[]")) {
                System.out.println("No matching user found.");
                return null;
            }
    
            if (response.getStatusCode().is2xxSuccessful()) {
                String body = response.getBody();
                JSONArray jsonArray = new JSONArray(body);
    
                if (jsonArray.length() > 0) {
                    JSONObject userObj = jsonArray.getJSONObject(0);
                    System.out.println("User ID: " + userObj.getInt("id"));
                    return userObj.getInt("id"); // Extracts ID directly
                }
            }
    
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
        

    public boolean registerUser(int id, String firstName, String lastName, String username, String email, String password) {
        try {
            String url = supabaseApiUrl + "/rest/v1/user_info?apikey=" + supabaseApiKey;
            
            // Create UserInfo object
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setFirst_name(firstName);
            userInfo.setLast_name(lastName);
            userInfo.setUsername(username);
            userInfo.setEmail(email);
            userInfo.setPassword(password);
            
            // Convert UserInfo to JSON
            String payload = new ObjectMapper().writeValueAsString(userInfo);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            System.out.println("Supabase API Response: " + response.getBody());
            
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}