package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.service.LangChain4jBookContentRetriever;
import java.util.List;
import java.util.Map;

/**
 * 🧪 Test Book Content Retriever
 */
public class BookContentRetrieverTest {
    
    public static void main(String[] args) {
        System.out.println("🚀 Testing Book Content Retriever");
        System.out.println("==================================");
        
        try {
            LangChain4jBookContentRetriever retriever = new LangChain4jBookContentRetriever();
            
            // Test content
            String bookContent = "Đây là nội dung sách về khoa học viễn tưởng. " +
                "Cuốn sách kể về một thế giới tương lai nơi con người sống trên các hành tinh khác. " +
                "Nhân vật chính là một phi hành gia trẻ tuổi tên Alex, người phát hiện ra một bí mật lớn " +
                "về nguồn gốc của loài người trong vũ trụ.";
            
            Map<String, String> metadata = Map.of(
                "title", "Khoa học viễn tưởng",
                "author", "Tác giả test",
                "category", "Sci-Fi"
            );
            
            // Test 1: Book content indexing
            System.out.println("\n📝 Test 1: Book content indexing");
            retriever.indexBookContent("book123", bookContent, metadata);
            System.out.println("✅ Book content indexed successfully");
            
            // Test 2: Content retrieval
            System.out.println("\n📝 Test 2: Content retrieval");
            List<String> relevantContent = retriever.retrieveRelevantContent("nhân vật chính", 3);
            System.out.println("✅ Retrieved " + relevantContent.size() + " relevant content pieces");
            for (String content : relevantContent) {
                System.out.println("   - " + content.substring(0, Math.min(50, content.length())) + "...");
            }
            
            // Test 3: Semantic search
            System.out.println("\n📝 Test 3: Semantic search");
            List<String> searchResults = retriever.semanticSearch("khoa học viễn tưởng", 5);
            System.out.println("✅ Semantic search found " + searchResults.size() + " results");
            for (String result : searchResults) {
                System.out.println("   - " + result.substring(0, Math.min(50, result.length())) + "...");
            }
            
            // Test 4: Content similarity
            System.out.println("\n📝 Test 4: Content similarity");
            double similarity1 = retriever.calculateSimilarity("khoa học viễn tưởng", "thế giới tương lai");
            System.out.println("✅ Similarity 1: " + similarity1);
            
            double similarity2 = retriever.calculateSimilarity("nhân vật chính", "phi hành gia");
            System.out.println("✅ Similarity 2: " + similarity2);
            
            double similarity3 = retriever.calculateSimilarity("sách", "truyện");
            System.out.println("✅ Similarity 3: " + similarity3);
            
            // Test 5: Service stats
            System.out.println("\n📝 Test 5: Service stats");
            Map<String, Object> stats = retriever.getStats();
            System.out.println("✅ Stats: " + stats);
            
            // Test 6: Clear cache
            System.out.println("\n📝 Test 6: Clear cache");
            retriever.clearCache();
            System.out.println("✅ Cache cleared");
            
            System.out.println("\n✅ All Book Content Retriever tests completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Book Content Retriever test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 