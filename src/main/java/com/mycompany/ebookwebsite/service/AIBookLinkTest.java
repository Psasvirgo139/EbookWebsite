package com.mycompany.ebookwebsite.service;

import java.util.List;
import com.mycompany.ebookwebsite.model.BookWithLink;
import com.mycompany.ebookwebsite.utils.Utils;

/**
 * 🧪 Test AI Book Link Feature
 */
public class AIBookLinkTest {
    
    public static void main(String[] args) {
        System.out.println("🧪 Testing AI Book Link Feature");
        System.out.println("=================================");
        
        try {
            // Test 1: Lấy sách với link
            System.out.println("\n📚 Test 1: Lấy sách với link");
            List<BookWithLink> books = Utils.getAvailableBooksWithLinks(5);
            System.out.println("Số sách có link: " + books.size());
            
            for (int i = 0; i < Math.min(3, books.size()); i++) {
                BookWithLink book = books.get(i);
                System.out.println((i+1) + ". " + book.getTitle());
                System.out.println("   Link: " + book.getDirectLink());
                System.out.println("   Mô tả: " + book.getShortDescription());
                System.out.println();
            }
            
            // Test 2: Tìm sách theo số thứ tự
            System.out.println("\n🔍 Test 2: Tìm sách theo số thứ tự");
            BookWithLink firstBook = Utils.findBookByIndex(1);
            if (firstBook != null) {
                System.out.println("Sách đầu tiên: " + firstBook.getTitle());
                System.out.println("Link: " + firstBook.getDirectLink());
            }
            
            // Test 3: Tìm sách theo tên
            System.out.println("\n🔍 Test 3: Tìm sách theo tên");
            if (firstBook != null) {
                BookWithLink foundBook = Utils.findBookByTitle(firstBook.getTitle());
                if (foundBook != null) {
                    System.out.println("Tìm thấy: " + foundBook.getTitle());
                    System.out.println("Link: " + foundBook.getDirectLink());
                }
            }
            
            // Test 4: Test AI Chat với câu hỏi đọc sách
            System.out.println("\n🤖 Test 4: AI Chat với câu hỏi đọc sách");
            SimpleEnhancedAIChatService aiService = new SimpleEnhancedAIChatService();
            
            String[] testQuestions = {
                "Hãy gợi ý cho tôi 3 cuốn sách",
                "Tôi muốn đọc cuốn sách đầu tiên",
                "Đọc cuốn sách thứ hai",
                "Tôi muốn đọc Nhà Thờ Đức Bà Paris"
            };
            
            for (String question : testQuestions) {
                System.out.println("\n❓ Câu hỏi: " + question);
                String response = aiService.processEnhancedChat(1, "test-session", question, null);
                System.out.println("🤖 Trả lời: " + response.substring(0, Math.min(300, response.length())) + "...");
            }
            
            System.out.println("\n✅ Test hoàn thành!");
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi trong test: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 