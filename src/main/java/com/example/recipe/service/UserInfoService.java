package com.example.recipe.service;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

    private RestTemplate restTemplate = new RestTemplate();

    public UserInfoService() {
        // Configure RestTemplate to support PATCH
        this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

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

    public boolean resetPassword(String username, String newPassword) {
        try {
            // Step 1: Verify if the user exists
            String verifyUrl = supabaseApiUrl + "/rest/v1/user_info?username=eq." + username + "&apikey=" + supabaseApiKey;
    
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
    
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.getForEntity(verifyUrl, String.class, entity);
    
            if (response.getBody() == null || response.getBody().equals("[]")) {
                System.out.println("User not found.");
                return false;
            }
    
            // Step 2: Update the user's password
            JSONArray jsonArray = new JSONArray(response.getBody());
            JSONObject userObject = jsonArray.getJSONObject(0);
            int userId = userObject.getInt("id"); // Assuming "id" is the primary key
    
            String updateUrl = supabaseApiUrl + "/rest/v1/user_info?id=eq." + userId + "&apikey=" + supabaseApiKey;
    
            // Create a JSON payload with only the password field
            JSONObject payload = new JSONObject();
            payload.put("password", newPassword);
    
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> updateEntity = new HttpEntity<>(payload.toString(), headers);
    
            ResponseEntity<String> updateResponse = restTemplate.exchange(updateUrl, HttpMethod.PATCH, updateEntity, String.class);
    
            return updateResponse.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserInfo getUserInfoById(int userId) {
        try {
            String url = supabaseApiUrl + "/rest/v1/user_info?id=eq." + userId + "&apikey=" + supabaseApiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, entity);

            if (response.getBody() != null && !response.getBody().equals("[]")) {
                JSONArray jsonArray = new JSONArray(response.getBody());
                JSONObject userObject = jsonArray.getJSONObject(0);

                UserInfo userInfo = new UserInfo();
                userInfo.setId(userObject.getInt("id"));
                userInfo.setFirst_name(userObject.getString("first_name"));
                userInfo.setLast_name(userObject.getString("last_name"));
                userInfo.setUsername(userObject.getString("username"));
                userInfo.setEmail(userObject.getString("email"));
                userInfo.setPassword(userObject.getString("password"));

                return userInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUserInfo(int userId, UserInfo updatedUserInfo) {
        try {
            String url = supabaseApiUrl + "/rest/v1/user_info?id=eq." + userId + "&apikey=" + supabaseApiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper objectMapper = new ObjectMapper();
            String payload = objectMapper.writeValueAsString(updatedUserInfo);

            HttpEntity<String> entity = new HttpEntity<>(payload, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}