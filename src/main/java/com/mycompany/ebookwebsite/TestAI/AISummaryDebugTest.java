package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.service.OpenAIContentSummaryService;
import com.mycompany.ebookwebsite.service.EbookWithAIService;
import com.mycompany.ebookwebsite.service.EbookWithAIService.EbookWithAI;
import com.mycompany.ebookwebsite.utils.Utils;
import com.mycompany.ebookwebsite.utils.PathManager;

/**
 * 🔍 AI SUMMARY DEBUG TEST
 * 
 * Tool để debug và test AI summary functionality
 * Chạy standalone để xác định nguyên nhân lỗi
 * Updated to use PathManager for better path management
 */
public class AISummaryDebugTest {
    
    public static void main(String[] args) {
        System.out.println("🔍 AI SUMMARY DEBUG TEST");
        System.out.println("=".repeat(50));
        
        try {
            // Test 1: Check OpenAI API Key
            testOpenAIAPIKey();
            
            // Test 2: Test AI Summary Service
            testAISummaryService();
            
            // Test 3: Test Database Integration
            testDatabaseIntegration();
            
            // Test 4: Test Book 47 Specifically
            testBook47Specifically();
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test 1: Check OpenAI API Key Configuration
     */
    private static void testOpenAIAPIKey() {
        System.out.println("\n🔑 TEST 1: OpenAI API Key");
        System.out.println("-".repeat(30));
        
        String apiKey = Utils.getEnv("OPENAI_API_KEY");
        System.out.println("API Key: " + (apiKey != null ? "SET (" + apiKey.length() + " chars)" : "NOT SET"));
        
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.out.println("⚠️ No API key - will use fallback mode");
        } else if ("your-openai-api-key-here".equals(apiKey)) {
            System.out.println("⚠️ Default placeholder API key - will use fallback mode");
        } else {
            System.out.println("✅ Valid API key detected");
        }
    }
    
    /**
     * Test 2: Test AI Summary Service with Sample Content
     */
    private static void testAISummaryService() {
        System.out.println("\n🤖 TEST 2: AI Summary Service");
        System.out.println("-".repeat(30));
        
        try {
            OpenAIContentSummaryService summaryService = new OpenAIContentSummaryService();
            
            String testContent = "Đây là một câu chuyện về Victor Hugo và cuốn tiểu thuyết nổi tiếng 'Nhà Thờ Đức Bà Paris'. " +
                    "Câu chuyện kể về Quasimodo, một người gù lưng sống trong nhà thờ Đức Bà Paris. " +
                    "Anh ta yêu thầm Esmeralda, một cô gái đẹp nhảy múa trên đường phố. " +
                    "Tác phẩm thể hiện sự đối lập giữa cái đẹp và cái xấu, giữa lòng tốt và sự tàn ác. " +
                    "Victor Hugo đã vẽ nên một bức tranh sống động về Paris thế kỷ 15 với những mâu thuẫn xã hội sâu sắc.";
            
            System.out.println("📝 Test content: " + testContent.length() + " characters");
            System.out.println("Content preview: " + testContent.substring(0, Math.min(100, testContent.length())) + "...");
            
            String summary = summaryService.summarize(testContent);
            
            System.out.println("✅ Summary generated successfully!");
            System.out.println("📄 Summary length: " + (summary != null ? summary.length() : 0) + " characters");
            System.out.println("📝 Generated summary: " + summary);
            
        } catch (Exception e) {
            System.err.println("❌ AI Summary Service failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test 3: Test Database Integration
     */
    private static void testDatabaseIntegration() {
        System.out.println("\n💾 TEST 3: Database Integration");
        System.out.println("-".repeat(30));
        
        try {
            EbookWithAIService ebookService = new EbookWithAIService();
            
            // Test getting book 47
            EbookWithAI book47 = ebookService.getEbookWithAI(47);
            if (book47 != null) {
                System.out.println("✅ Book 47 found: " + book47.getTitle());
                
                if (book47.getAiData() != null) {
                    System.out.println("✅ EbookAI data exists");
                    System.out.println("📁 file_name: " + book47.getAiData().getFileName());
                    System.out.println("📄 original_file_name: " + book47.getAiData().getOriginalFileName());
                    System.out.println("📝 summary: " + (book47.getAiData().getSummary() != null ? "EXISTS (" + book47.getAiData().getSummary().length() + " chars)" : "NULL"));
                } else {
                    System.out.println("⚠️ No EbookAI data found");
                }
                
                // Test summary update
                System.out.println("\n🔄 Testing summary update...");
                boolean updated = ebookService.updateSummary(47, "Test summary from debug tool");
                System.out.println("Summary update result: " + (updated ? "✅ SUCCESS" : "❌ FAILED"));
                
            } else {
                System.out.println("❌ Book 47 not found in database");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Database integration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test 4: Test Book 47 File Reading Specifically
     */
    private static void testBook47Specifically() {
        System.out.println("\n📚 TEST 4: Book 47 File Reading");
        System.out.println("-".repeat(30));
        
        try {
            // 🗂️ Sử dụng PathManager để tạo test paths
            String uploadsPath = PathManager.getUploadsPath();
            System.out.println("📁 Using uploads path: " + uploadsPath);
            
            String[] possiblePaths = {
                PathManager.getUploadFilePath("Nhà Thờ Đức Bà Paris.pdf"),
                "uploads/Nhà Thờ Đức Bà Paris.pdf",
                "uploads/book_47_1751596645234_nh_th_c_b_paris.pdf",
                "uploads/nh_th_c_b_paris.pdf",
                "book_47_1751596645234_nh_th_c_b_paris.pdf",
                "nh_th_c_b_paris.pdf"
            };
            
            for (String path : possiblePaths) {
                java.io.File file = new java.io.File(path);
                System.out.println("📁 " + path + ": " + (file.exists() ? "✅ EXISTS" : "❌ NOT FOUND"));
                if (file.exists()) {
                    System.out.println("   📏 Size: " + file.length() + " bytes");
                    System.out.println("   📅 Last modified: " + new java.util.Date(file.lastModified()));
                    
                    // Try to read the file
                    try {
                        String content = Utils.readAnyTextFile(file.getAbsolutePath(), "pdf");
                        System.out.println("   ✅ File readable: " + content.length() + " characters");
                        System.out.println("   📝 Preview: " + content.substring(0, Math.min(200, content.length())) + "...");
                        
                        // Test AI summary generation with actual file content
                        System.out.println("   🤖 Testing AI summary generation...");
                        OpenAIContentSummaryService summaryService = new OpenAIContentSummaryService();
                        String summary = summaryService.summarize(content);
                        System.out.println("   ✅ AI Summary generated: " + summary.length() + " characters");
                        System.out.println("   📝 AI Summary: " + summary);
                        
                    } catch (Exception e) {
                        System.out.println("   ❌ File read error: " + e.getMessage());
                    }
                }
            }
            
            // 🗂️ Check project uploads directory specifically using PathManager
            System.out.println("\n📂 Project uploads directory scan:");
            String[] uploadsDirs = {
                uploadsPath,  // PathManager uploads path
                "uploads",
                System.getProperty("user.dir") + "\\uploads"
            };
            
            for (String checkPath : uploadsDirs) {
                java.io.File uploadsDir = new java.io.File(checkPath);
                System.out.println("📁 Checking: " + checkPath);
                
                if (uploadsDir.exists() && uploadsDir.isDirectory()) {
                    java.io.File[] files = uploadsDir.listFiles();
                    System.out.println("   ✅ Directory exists with " + (files != null ? files.length : 0) + " files");
                    
                    if (files != null) {
                        boolean foundTarget = false;
                        for (java.io.File file : files) {
                            if (file.getName().contains("47") || file.getName().contains("paris") || 
                                file.getName().contains("Paris") || file.getName().contains("Nhà Thờ")) {
                                System.out.println("   🎯 " + file.getName() + " (" + file.length() + " bytes)");
                                foundTarget = true;
                            }
                        }
                        
                        if (!foundTarget) {
                            System.out.println("   ⚠️ No matching files found, showing all files:");
                            for (int i = 0; i < Math.min(10, files.length); i++) {
                                System.out.println("     - " + files[i].getName());
                            }
                        }
                    }
                } else {
                    System.out.println("   ❌ Directory not found");
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Book 47 file test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 