package com.mycompany.ebookwebsite.service;

import java.util.List;
import com.mycompany.ebookwebsite.model.BookWithLink;
import com.mycompany.ebookwebsite.utils.Utils;

/**
 * 🧪 Test AI Book Count Feature
 */
public class AIBookCountTest {
    
    public static void main(String[] args) {
        System.out.println("🧪 Testing AI Book Count Feature");
        System.out.println("=================================");
        
        try {
            // Test 1: Test các câu hỏi khác nhau với số lượng sách
            System.out.println("\n📚 Test 1: Test các câu hỏi với số lượng sách khác nhau");
            SimpleEnhancedAIChatService aiService = new SimpleEnhancedAIChatService();
            
            String[] testQuestions = {
                "Hãy gợi ý cho tôi 3 cuốn sách",
                "Đề xuất 5 cuốn sách",
                "Gợi ý 1 cuốn sách",
                "Hãy gợi ý cho tôi 10 cuốn sách",
                "Đề xuất sách", // Không có số, default 3
                "Gợi ý hai cuốn sách",
                "Đề xuất ba cuốn sách",
                "Hãy gợi ý cho tôi 15 cuốn sách" // Vượt quá giới hạn, sẽ giới hạn về 10
            };
            
            for (String question : testQuestions) {
                System.out.println("\n❓ Câu hỏi: " + question);
                String response = aiService.processEnhancedChat(1, "test-session", question, null);
                System.out.println("🤖 Trả lời: " + response.substring(0, Math.min(300, response.length())) + "...");
            }
            
            // Test 2: Test method extractBookCountFromMessage
            System.out.println("\n🔢 Test 2: Test method extractBookCountFromMessage");
            testExtractBookCount(aiService);
            
            // Test 3: Test với số lượng sách thực tế
            System.out.println("\n📊 Test 3: Test với số lượng sách thực tế");
            List<BookWithLink> allBooks = Utils.getAvailableBooksWithLinks(20);
            System.out.println("Tổng số sách có trong database: " + allBooks.size());
            
            for (int i = 1; i <= 5; i++) {
                List<BookWithLink> books = Utils.getAvailableBooksWithLinks(i);
                System.out.println("Yêu cầu " + i + " sách → Nhận được " + books.size() + " sách");
            }
            
            System.out.println("\n✅ Test hoàn thành!");
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi trong test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testExtractBookCount(SimpleEnhancedAIChatService aiService) {
        try {
            // Sử dụng reflection để test private method
            java.lang.reflect.Method method = SimpleEnhancedAIChatService.class
                .getDeclaredMethod("extractBookCountFromMessage", String.class);
            method.setAccessible(true);
            
            String[] testMessages = {
                "Hãy gợi ý cho tôi 3 cuốn sách",
                "Đề xuất 5 cuốn sách",
                "Gợi ý 1 cuốn sách",
                "Hãy gợi ý cho tôi 10 cuốn sách",
                "Đề xuất sách",
                "Gợi ý hai cuốn sách",
                "Đề xuất ba cuốn sách",
                "Hãy gợi ý cho tôi 15 cuốn sách"
            };
            
            for (String message : testMessages) {
                int count = (Integer) method.invoke(aiService, message);
                System.out.println("Message: \"" + message + "\" → Count: " + count);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi test extractBookCount: " + e.getMessage());
        }
    }
} 