package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.service.EnhancedRAGService;
import java.util.List;
import java.util.Map;

/**
 * 🧪 Test Enhanced RAG Service
 */
public class EnhancedRAGTest {
    
    public static void main(String[] args) {
        System.out.println("🚀 Testing Enhanced RAG Service");
        System.out.println("================================");
        
        try {
            EnhancedRAGService ragService = new EnhancedRAGService();
            
            // Test content
            String testContent = "Đây là nội dung sách về khoa học viễn tưởng. " +
                "Cuốn sách kể về một thế giới tương lai nơi con người sống trên các hành tinh khác. " +
                "Nhân vật chính là một phi hành gia trẻ tuổi tên Alex, người phát hiện ra một bí mật lớn " +
                "về nguồn gốc của loài người trong vũ trụ.";
            
            Map<String, String> metadata = Map.of(
                "title", "Khoa học viễn tưởng",
                "author", "Tác giả test",
                "category", "Sci-Fi"
            );
            
            // Test 1: Content indexing
            System.out.println("\n📝 Test 1: Content indexing");
            ragService.indexContent(testContent, metadata);
            System.out.println("✅ Content indexed successfully");
            
            // Test 2: Content retrieval
            System.out.println("\n📝 Test 2: Content retrieval");
            List<dev.langchain4j.data.segment.TextSegment> segments = 
                ragService.retrieveRelevantContent("nhân vật chính", 3, null);
            System.out.println("✅ Retrieved " + segments.size() + " relevant segments");
            
            // Test 3: Summary generation
            System.out.println("\n📝 Test 3: Summary generation");
            String summary = ragService.generateSummary(testContent);
            System.out.println("✅ Summary: " + summary.substring(0, Math.min(100, summary.length())) + "...");
            
            // Test 4: Category classification
            System.out.println("\n📝 Test 4: Category classification");
            String category = ragService.classifyCategory(testContent);
            System.out.println("✅ Category: " + category.substring(0, Math.min(100, category.length())) + "...");
            
            // Test 5: Service stats
            System.out.println("\n📝 Test 5: Service stats");
            Map<String, Object> stats = ragService.getStats();
            System.out.println("✅ Stats: " + stats);
            
            // Test 6: Clear cache
            System.out.println("\n📝 Test 6: Clear cache");
            ragService.clearCache();
            System.out.println("✅ Cache cleared");
            
            System.out.println("\n✅ All Enhanced RAG Service tests completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Enhanced RAG Service test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 