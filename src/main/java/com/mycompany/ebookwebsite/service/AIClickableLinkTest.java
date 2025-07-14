package com.mycompany.ebookwebsite.service;

import java.util.List;
import com.mycompany.ebookwebsite.model.BookWithLink;
import com.mycompany.ebookwebsite.utils.Utils;

/**
 * 🧪 Test AI Clickable Link Feature
 */
public class AIClickableLinkTest {
    
    public static void main(String[] args) {
        System.out.println("🧪 Testing AI Clickable Link Feature");
        System.out.println("====================================");
        
        try {
            // Test 1: Lấy sách với link click được
            System.out.println("\n📚 Test 1: Lấy sách với link click được");
            List<BookWithLink> books = Utils.getAvailableBooksWithLinks(3);
            System.out.println("Số sách có link: " + books.size());
            
            for (int i = 0; i < Math.min(2, books.size()); i++) {
                BookWithLink book = books.get(i);
                System.out.println((i+1) + ". " + book.getTitle());
                System.out.println("   Link: " + book.getDirectLink());
                System.out.println("   HTML Link: <a href='" + book.getDirectLink() + "' target='_blank'>" + book.getDirectLink() + "</a>");
                System.out.println();
            }
            
            // Test 2: Test AI Chat với câu hỏi gợi ý sách
            System.out.println("\n🤖 Test 2: AI Chat với câu hỏi gợi ý sách");
            SimpleEnhancedAIChatService aiService = new SimpleEnhancedAIChatService();
            
            String response1 = aiService.processEnhancedChat(1, "test-session", "Hãy gợi ý cho tôi 3 cuốn sách", null);
            System.out.println("❓ Câu hỏi: Hãy gợi ý cho tôi 3 cuốn sách");
            System.out.println("🤖 Trả lời (HTML): " + response1.substring(0, Math.min(500, response1.length())) + "...");
            
            // Test 3: Test AI Chat với câu hỏi đọc sách cụ thể
            System.out.println("\n🤖 Test 3: AI Chat với câu hỏi đọc sách cụ thể");
            String response2 = aiService.processEnhancedChat(1, "test-session", "Tôi muốn đọc cuốn sách đầu tiên", null);
            System.out.println("❓ Câu hỏi: Tôi muốn đọc cuốn sách đầu tiên");
            System.out.println("🤖 Trả lời (HTML): " + response2);
            
            // Test 4: Test AI Chat với tên sách cụ thể
            System.out.println("\n🤖 Test 4: AI Chat với tên sách cụ thể");
            if (!books.isEmpty()) {
                String bookTitle = books.get(0).getTitle();
                String response3 = aiService.processEnhancedChat(1, "test-session", "Tôi muốn đọc " + bookTitle, null);
                System.out.println("❓ Câu hỏi: Tôi muốn đọc " + bookTitle);
                System.out.println("🤖 Trả lời (HTML): " + response3);
            }
            
            System.out.println("\n✅ Test hoàn thành!");
            System.out.println("\n📝 Lưu ý: Links sẽ hiển thị dưới dạng HTML trong chat box và có thể click được!");
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi trong test: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 