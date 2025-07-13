package com.mycompany.ebookwebsite.service;

import java.util.List;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.utils.Utils;

/**
 * 🧪 Test AI Chat để kiểm tra việc lấy sách từ database
 */
public class AITestMain {
    
    public static void main(String[] args) {
        System.out.println("🧪 Testing AI Chat Database Integration");
        System.out.println("=====================================");
        
        try {
            // Test 1: Kiểm tra lấy sách từ database
            System.out.println("\n📚 Test 1: Lấy sách từ database");
            List<Ebook> books = Utils.getAvailableBooks(10);
            System.out.println("Số sách có trong database: " + books.size());
            
            for (int i = 0; i < Math.min(5, books.size()); i++) {
                Ebook book = books.get(i);
                System.out.println((i+1) + ". " + book.getTitle() + 
                                 " (ID: " + book.getId() + ")");
            }
            
            // Test 2: Test AI Chat với câu hỏi gợi ý sách
            System.out.println("\n🤖 Test 2: AI Chat với câu hỏi gợi ý sách");
            SimpleEnhancedAIChatService aiService = new SimpleEnhancedAIChatService();
            
            String[] testQuestions = {
                "Hãy gợi ý cho tôi 3 cuốn sách",
                "Bạn có thể đề xuất sách nào không?",
                "Suggest some books",
                "Có sách nào hay không?"
            };
            
            for (String question : testQuestions) {
                System.out.println("\n❓ Câu hỏi: " + question);
                String response = aiService.processEnhancedChat(1, "test-session", question, null);
                System.out.println("🤖 Trả lời: " + response.substring(0, Math.min(200, response.length())) + "...");
            }
            
            System.out.println("\n✅ Test hoàn thành!");
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi trong test: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 