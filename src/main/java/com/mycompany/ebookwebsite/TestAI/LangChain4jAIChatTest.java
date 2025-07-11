package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.service.LangChain4jAIChatService;

/**
 * 🧪 Test LangChain4j AI Chat Service
 */
public class LangChain4jAIChatTest {
    
    public static void main(String[] args) {
        System.out.println("🚀 Testing LangChain4j AI Chat Service");
        System.out.println("========================================");
        
        try {
            LangChain4jAIChatService chatService = new LangChain4jAIChatService();
            
            // Test 1: Basic chat
            System.out.println("\n📝 Test 1: Basic chat");
            String response1 = chatService.processChat(1, "session1", "Xin chào! Bạn có thể giúp tôi tìm sách không?", null);
            System.out.println("✅ Response: " + response1.substring(0, Math.min(100, response1.length())) + "...");
            
            // Test 2: Book-specific chat
            System.out.println("\n📝 Test 2: Book-specific chat");
            String response2 = chatService.chatAboutBook("Kể cho tôi về nhân vật chính", "book123");
            System.out.println("✅ Response: " + response2.substring(0, Math.min(100, response2.length())) + "...");
            
            // Test 3: Recommendations
            System.out.println("\n📝 Test 3: Recommendations");
            String response3 = chatService.getRecommendations("Tôi thích sách khoa học viễn tưởng");
            System.out.println("✅ Response: " + response3.substring(0, Math.min(100, response3.length())) + "...");
            
            // Test 4: Book analysis
            System.out.println("\n📝 Test 4: Book analysis");
            String response4 = chatService.analyzeBookContent("Đây là nội dung sách về khoa học viễn tưởng...");
            System.out.println("✅ Response: " + response4.substring(0, Math.min(100, response4.length())) + "...");
            
            // Test 5: Search books
            System.out.println("\n📝 Test 5: Search books");
            String response5 = chatService.searchBooks("sách khoa học");
            System.out.println("✅ Response: " + response5.substring(0, Math.min(100, response5.length())) + "...");
            
            // Test 6: Service stats
            System.out.println("\n📝 Test 6: Service stats");
            String stats = chatService.getServiceStats();
            System.out.println("✅ Stats: " + stats);
            
            System.out.println("\n✅ All LangChain4j AI Chat Service tests completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ LangChain4j AI Chat Service test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 