package com.mycompany.ebookwebsite.ai.test;

import com.mycompany.ebookwebsite.ai.SimpleEnhancedAIChatService;
import java.util.Scanner;

/**
 * 🤖 AI Chat CLI - Chat trực tiếp với AI (tích hợp tất cả tính năng mới)
 * 
 * Enhanced version with:
 * - Proper input validation
 * - Better user experience
 * - Context continuity support
 * - Cross-topic connection awareness
 */
public class AIChatTester {
    public static void main(String[] args) {
        System.out.println("🤖 AI CHAT CLI (TÍCH HỢP ĐẦY ĐỦ)");
        System.out.println("====================================");
        System.out.println("Nhập 'exit' để thoát.");
        System.out.println();
        
        SimpleEnhancedAIChatService aiService = new SimpleEnhancedAIChatService();
        Scanner scanner = new Scanner(System.in);
        int userId = 1;
        String sessionId = "cli-session";
        
        while (true) {
            System.out.print("👤 Bạn: ");
            String userInput = scanner.nextLine();
            
            // Let AI service handle input validation
            if (userInput.equalsIgnoreCase("exit")) {
                System.out.println("👋 Tạm biệt!");
                break;
            }
            
            try {
                // Let the AI service handle all input validation
                String response = aiService.processEnhancedChat(userId, sessionId, userInput, "CLI chat mode");
                System.out.println("🤖 AI: " + response);
            } catch (Exception e) {
                System.out.println("❌ Lỗi AI: " + e.getMessage());
            }
        }
        scanner.close();
    }
} 