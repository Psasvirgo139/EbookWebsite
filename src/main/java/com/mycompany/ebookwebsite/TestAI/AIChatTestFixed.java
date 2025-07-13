package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.service.SimpleEnhancedAIChatService;
import com.mycompany.ebookwebsite.utils.Utils;
import com.mycompany.ebookwebsite.model.Ebook;

import java.util.List;

/**
 * 🧪 Test AI Chat đã được sửa để sử dụng sách thực từ database
 */
public class AIChatTestFixed {
    
    public static void main(String[] args) {
        System.out.println("🚀 Testing Fixed AI Chat Service");
        System.out.println("=================================");
        
        try {
            // Test 1: Kiểm tra lấy sách có sẵn từ database
            System.out.println("\n📚 Test 1: Lấy sách có sẵn từ database");
            List<Ebook> availableBooks = Utils.getAvailableBooks(5);
            System.out.println("✅ Found " + availableBooks.size() + " books in database:");
            for (Ebook book : availableBooks) {
                System.out.println("  • " + book.getTitle() + " (ID: " + book.getId() + ")");
            }
            
            // Test 2: Test AI Chat với sách thực
            System.out.println("\n🤖 Test 2: AI Chat với sách thực");
            SimpleEnhancedAIChatService chatService = new SimpleEnhancedAIChatService();
            
            String userMessage = "gợi ý cho tôi 3 cuốn sách";
            String response = chatService.processEnhancedChat(1, "test-session", userMessage, null);
            
            System.out.println("👤 User: " + userMessage);
            System.out.println("🤖 AI: " + response);
            
            // Test 3: Test với sách khác
            System.out.println("\n🤖 Test 3: AI Chat với yêu cầu khác");
            String userMessage2 = "có sách nào về lập trình không?";
            String response2 = chatService.processEnhancedChat(1, "test-session", userMessage2, null);
            
            System.out.println("👤 User: " + userMessage2);
            System.out.println("🤖 AI: " + response2);
            
            System.out.println("\n✅ All tests completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 