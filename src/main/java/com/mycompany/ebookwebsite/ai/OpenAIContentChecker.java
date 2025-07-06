package com.mycompany.ebookwebsite.ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mycompany.ebookwebsite.utils.Utils;

public class OpenAIContentChecker {

    private String apiKey;
    private HttpClient httpClient;
    
    public OpenAIContentChecker() {
        this.apiKey = Utils.getEnv("OPENAI_API_KEY");
        this.httpClient = HttpClient.newHttpClient();
    }

    public String check(String text) {
        // Kiểm tra API key
        if (apiKey == null || apiKey.trim().isEmpty() || "your-openai-api-key-here".equals(apiKey)) {
            // Fallback to demo if no valid API key
            if (text != null && text.toLowerCase().contains("sex")) {
                return "{\"status\":\"violation\", \"reason\":\"Nội dung nhạy cảm\"}";
            }
            return "OK";
        }

        try {
            // Gọi OpenAI Moderation API thực
            return callOpenAIModerationAPI(text);
        } catch (Exception e) {
            System.err.println("Lỗi khi gọi OpenAI Moderation API: " + e.getMessage());
            // Fallback to demo on error
            if (text != null && text.toLowerCase().contains("sex")) {
                return "{\"status\":\"violation\", \"reason\":\"Nội dung nhạy cảm\"}";
            }
            return "OK";
        }
    }
    
    private String callOpenAIModerationAPI(String text) throws Exception {
        // Tạo request body cho OpenAI Moderation API
        JSONObject requestBody = new JSONObject();
        requestBody.put("input", text);
        
        // Tạo HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/moderations"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
        
        // Gửi request và nhận response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new Exception("OpenAI Moderation API error: " + response.statusCode() + " - " + response.body());
        }
        
        // Parse response
        JSONObject responseJson = new JSONObject(response.body());
        JSONArray results = responseJson.getJSONArray("results");
        
        if (results.length() > 0) {
            JSONObject result = results.getJSONObject(0);
            boolean flagged = result.getBoolean("flagged");
            
            if (flagged) {
                JSONObject categories = result.getJSONObject("categories");
                StringBuilder reasons = new StringBuilder();
                
                if (categories.optBoolean("hate", false)) reasons.append("Hate speech, ");
                if (categories.optBoolean("violence", false)) reasons.append("Violence, ");
                if (categories.optBoolean("sexual", false)) reasons.append("Sexual content, ");
                if (categories.optBoolean("self-harm", false)) reasons.append("Self-harm, ");
                if (categories.optBoolean("harassment", false)) reasons.append("Harassment, ");
                
                String reasonText = reasons.toString();
                if (reasonText.endsWith(", ")) {
                    reasonText = reasonText.substring(0, reasonText.length() - 2);
                }
                
                return "{\"status\":\"violation\", \"reason\":\"" + reasonText + "\"}";
            }
        }
        
        return "OK";
    }
}
