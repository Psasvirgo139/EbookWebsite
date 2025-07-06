package com.mycompany.ebookwebsite.agent;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mycompany.ebookwebsite.utils.Utils;

/**
 * LLM là adapter trung gian giao tiếp với AI provider (OpenAI, Anthropic, ...).
 * Tất cả class agent khác chỉ cần truyền danh sách Message và nhận về chuỗi phản hồi.
 * Khi muốn đổi provider, chỉ cần thay đổi duy nhất class này.
 */
public class LLM {
    
    private String apiKey;
    private HttpClient httpClient;
    
    public LLM() {
        this.apiKey = Utils.getEnv("OPENAI_API_KEY");
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Gọi AI provider để sinh phản hồi dựa trên danh sách message hội thoại.
     *
     * @param messages Danh sách Message (system, user, assistant...)
     * @return Phản hồi (response) dạng text từ AI
     */
    public String generateResponse(List<Message> messages) {
        // ======== LOG PROMPT gửi đi =========
        System.out.println("[LLM] Gửi prompt tới AI: " + messages); // LOG 1

        // Kiểm tra API key
        if (apiKey == null || apiKey.trim().isEmpty() || "your-openai-api-key-here".equals(apiKey)) {
            // Fallback to demo if no valid API key
            StringBuilder prompt = new StringBuilder();
            for (Message m : messages) {
                prompt.append("[").append(m.getRole()).append("] ").append(m.getContent()).append("\n");
            }
            String response;
            // Demo response: AI luôn trả về OK nếu có từ "kiểm duyệt"
            if (prompt.toString().toLowerCase().contains("kiểm duyệt")) {
                response = "OK";
            } else {
                response = "Nội dung vi phạm: phát hiện từ cấm!";
            }
            System.out.println("[LLM] Sử dụng demo mode - Nhận phản hồi: " + response); // LOG 2
            return response;
        }

        try {
            // ======== Gọi OpenAI API thực =========
            String response = callOpenAIAPI(messages);
            
            // ======== LOG RESPONSE nhận lại =========
            System.out.println("[LLM] Nhận phản hồi từ AI: " + response); // LOG 2
            
            return response;
        } catch (Exception e) {
            System.err.println("[LLM] Lỗi khi gọi OpenAI API: " + e.getMessage());
            // Fallback to demo on error
            return "Lỗi khi gọi AI: " + e.getMessage();
        }
    }
    
    private String callOpenAIAPI(List<Message> messages) throws Exception {
        // Tạo request body cho OpenAI Chat Completion API
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        
        JSONArray messageArray = new JSONArray();
        for (Message msg : messages) {
            JSONObject msgObj = new JSONObject();
            msgObj.put("role", msg.getRole());
            msgObj.put("content", msg.getContent());
            messageArray.put(msgObj);
        }
        
        requestBody.put("messages", messageArray);
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.7);
        
        // Tạo HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
        
        // Gửi request và nhận response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new Exception("OpenAI API error: " + response.statusCode() + " - " + response.body());
        }
        
        // Parse response
        JSONObject responseJson = new JSONObject(response.body());
        JSONArray choices = responseJson.getJSONArray("choices");
        if (choices.length() > 0) {
            JSONObject firstChoice = choices.getJSONObject(0);
            JSONObject message = firstChoice.getJSONObject("message");
            return message.getString("content").trim();
        }
        
        throw new Exception("Không nhận được phản hồi từ OpenAI API");
    }
} 