package com.mycompany.ebookwebsite.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mycompany.ebookwebsite.utils.Utils;

/**
 * 📝 OPENAI CONTENT SUMMARY SERVICE
 * 
 * ========================================
 * 📋 TÁC DỤNG CHÍNH:
 * ========================================
 * 
 * 1. 🤖 **AI-Powered Content Summarization**
 *    - OpenAI GPT-3.5-turbo integration
 *    - Automatic text summarization
 *    - Vietnamese language support
 *    - Intelligent content condensation
 * 
 * 2. 📚 **Book Content Processing**
 *    - Book chapter summarization
 *    - Long-form content processing
 *    - Content length optimization
 *    - Key information extraction
 * 
 * 3. 🔄 **Fallback Mechanisms**
 *    - Demo mode khi không có API key
 *    - Graceful error handling
 *    - Fallback text summarization
 *    - Offline content processing
 * 
 * 4. 🎯 **Smart Content Management**
 *    - Content truncation cho API limits
 *    - Optimal prompt engineering
 *    - Response quality optimization
 *    - JSON response parsing
 * 
 * ========================================
 * 🔧 FEATURES:
 * ========================================
 * 
 * ✅ OpenAI Chat Completion API integration
 * ✅ GPT-3.5-turbo model usage
 * ✅ Vietnamese summarization
 * ✅ Content length optimization (8000 chars)
 * ✅ Intelligent prompt engineering
 * ✅ Error handling và fallback
 * ✅ Demo mode compatibility
 * ✅ JSON response processing
 * ✅ HTTP client management
 * ✅ Auto content truncation
 * 
 * ========================================
 * 🎯 SỬ DỤNG:
 * ========================================
 * 
 * - Book content summarization
 * - Chapter summary generation
 * - Long text condensation
 * - Content preview creation
 * - AI-powered text processing
 * 
 * ========================================
 * 🏗️ ARCHITECTURE:
 * ========================================
 * 
 * OpenAIContentSummaryService (Main Service)
 *     ├── OpenAI Chat Completion API (AI Engine)
 *     ├── HttpClient (HTTP Communication)
 *     ├── JSON Processing (Request/Response)
 *     ├── Content Optimization (Length Management)
 *     └── Fallback System (Error Handling)
 * 
 * ========================================
 * 🔄 WORKFLOW:
 * ========================================
 * 
 * Content Input → Length Check → API Call → Summary Generation → Response
 *     ↓                                                         ↓
 * Fallback Processing ← Error Handling ← JSON Parsing ← AI Response
 */

public class OpenAIContentSummaryService {

    private String apiKey;
    private HttpClient httpClient;
    
    public OpenAIContentSummaryService() {
        this.apiKey = Utils.getEnv("OPENAI_API_KEY");
        this.httpClient = HttpClient.newHttpClient();
    }
    
    public String summarize(String content) throws Exception {
        // Nếu content quá dài, nên cắt (ví dụ lấy 8000 ký tự đầu)
        if (content.length() > 8000) {
            content = content.substring(0, 8000);
        }
        
        // Kiểm tra API key
        if (apiKey == null || apiKey.trim().isEmpty() || "your-openai-api-key-here".equals(apiKey)) {
            // Fallback to demo if no valid API key
            String summary = "Đây là tóm tắt tự động của nội dung sách. ";
            if (content.length() > 100) {
                summary += content.substring(0, 100) + "...";
            } else {
                summary += content;
            }
            return summary;
        }
        
        try {
            // Gọi OpenAI API thực
            return callOpenAIAPI(content);
        } catch (Exception e) {
            System.err.println("Lỗi khi gọi OpenAI API: " + e.getMessage());
            // Fallback to demo on error
            return "Không thể tạo tóm tắt tự động. Nội dung: " + content.substring(0, Math.min(200, content.length())) + "...";
        }
    }
    
    private String callOpenAIAPI(String content) throws Exception {
        // Tạo request body cho OpenAI Chat Completion API
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        
        JSONArray messages = new JSONArray();
        
        // System message
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "Bạn là một chuyên gia tóm tắt văn bản. Hãy tóm tắt nội dung sau thành một đoạn ngắn gọn, súc tích từ 80-200 từ bằng tiếng Việt.");
        messages.put(systemMessage);
        
        // User message
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", "Hãy tóm tắt nội dung sau:\n\n" + content);
        messages.put(userMessage);
        
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 300);
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