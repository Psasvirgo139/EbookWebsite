package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.service.OpenAIContentSummaryService;
import com.mycompany.ebookwebsite.utils.Utils;

import java.io.File;

/**
 * 🔍 SIMPLE FILE TEST
 * 
 * Quick test to verify file path and AI summary
 */
public class SimpleFileTest {
    
    public static void main(String[] args) {
        System.out.println("🔍 SIMPLE FILE TEST");
        System.out.println("=".repeat(40));
        
        // Test exact file path user confirmed
        String filePath = "D:\\EbookWebsite\\uploads\\Nhà Thờ Đức Bà Paris.pdf";
        
        System.out.println("📁 Testing file: " + filePath);
        
        File file = new File(filePath);
        
        if (file.exists()) {
            System.out.println("✅ File exists!");
            System.out.println("📏 Size: " + file.length() + " bytes");
            System.out.println("📅 Last modified: " + new java.util.Date(file.lastModified()));
            
            try {
                System.out.println("\n📖 Reading file content...");
                String content = Utils.readAnyTextFile(filePath, "pdf");
                
                System.out.println("✅ File readable: " + content.length() + " characters");
                System.out.println("📝 Preview: " + content.substring(0, Math.min(300, content.length())) + "...");
                
                System.out.println("\n🤖 Generating AI summary...");
                OpenAIContentSummaryService summaryService = new OpenAIContentSummaryService();
                String summary = summaryService.summarize(content);
                
                System.out.println("✅ AI Summary generated!");
                System.out.println("📄 Summary length: " + summary.length() + " characters");
                System.out.println("📝 AI Summary: " + summary);
                
            } catch (Exception e) {
                System.err.println("❌ Error reading file: " + e.getMessage());
                e.printStackTrace();
            }
            
        } else {
            System.out.println("❌ File NOT found!");
            
            // Check parent directory
            File parent = file.getParentFile();
            if (parent != null && parent.exists()) {
                System.out.println("📂 Parent directory exists: " + parent.getAbsolutePath());
                File[] files = parent.listFiles();
                if (files != null) {
                    System.out.println("📁 Files in directory:");
                    for (File f : files) {
                        System.out.println("  - " + f.getName());
                    }
                }
            } else {
                System.out.println("❌ Parent directory also not found!");
            }
        }
        
        System.out.println("\n" + "=".repeat(40));
        System.out.println("🎯 TEST COMPLETED");
    }
} 