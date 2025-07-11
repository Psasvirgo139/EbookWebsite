package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.service.OpenAIContentAnalysisService;
import java.util.List;
import java.util.Arrays;

/**
 * 🧪 Test OpenAI Content Analysis Service
 */
public class OpenAIContentAnalysisTest {
    
    public static void main(String[] args) {
        System.out.println("🚀 Testing OpenAI Content Analysis Service");
        System.out.println("===========================================");
        
        try {
            OpenAIContentAnalysisService analysisService = new OpenAIContentAnalysisService();
            
            // Test content
            String testContent = "Đây là nội dung sách về khoa học viễn tưởng. " +
                "Cuốn sách kể về một thế giới tương lai nơi con người sống trên các hành tinh khác. " +
                "Nhân vật chính là một phi hành gia trẻ tuổi tên Alex, người phát hiện ra một bí mật lớn " +
                "về nguồn gốc của loài người trong vũ trụ.";
            
            List<String> bookTitles = Arrays.asList("Khoa học viễn tưởng", "Thế giới tương lai");
            
            // Test 1: Content analysis - Character question
            System.out.println("\n📝 Test 1: Content analysis - Character question");
            String analysis1 = analysisService.analyzeBookContent(testContent, "Kể cho tôi về nhân vật chính", bookTitles);
            System.out.println("✅ Response: " + analysis1.substring(0, Math.min(100, analysis1.length())) + "...");
            
            // Test 2: Content analysis - Plot question
            System.out.println("\n📝 Test 2: Content analysis - Plot question");
            String analysis2 = analysisService.analyzeBookContent(testContent, "Cốt truyện như thế nào?", bookTitles);
            System.out.println("✅ Response: " + analysis2.substring(0, Math.min(100, analysis2.length())) + "...");
            
            // Test 3: Content analysis - Opinion question
            System.out.println("\n📝 Test 3: Content analysis - Opinion question");
            String analysis3 = analysisService.analyzeBookContent(testContent, "Bạn có thích cuốn sách này không?", bookTitles);
            System.out.println("✅ Response: " + analysis3.substring(0, Math.min(100, analysis3.length())) + "...");
            
            // Test 4: Content analysis - General question
            System.out.println("\n📝 Test 4: Content analysis - General question");
            String analysis4 = analysisService.analyzeBookContent(testContent, "Cuốn sách này có gì đặc biệt?", bookTitles);
            System.out.println("✅ Response: " + analysis4.substring(0, Math.min(100, analysis4.length())) + "...");
            
            // Test 5: Content analysis - Theme question
            System.out.println("\n📝 Test 5: Content analysis - Theme question");
            String analysis5 = analysisService.analyzeBookContent(testContent, "Chủ đề chính của cuốn sách là gì?", bookTitles);
            System.out.println("✅ Response: " + analysis5.substring(0, Math.min(100, analysis5.length())) + "...");
            
            System.out.println("\n✅ All OpenAI Content Analysis Service tests completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ OpenAI Content Analysis Service test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 