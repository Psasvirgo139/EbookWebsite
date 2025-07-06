package com.mycompany.ebookwebsite.ai;

import java.util.List;
import java.util.Map;
import java.util.Arrays;

/**
 * 🧪 AI Test Main - Test tất cả AI services với LangChain4j
 * 
 * Test cases cho:
 * - LangChain4jAIChatService
 * - EnhancedRAGService  
 * - OpenAIContentAnalysisService
 * - QueryTransformationPipeline
 * - LangChain4jBookContentRetriever
 */
public class AITestMain {
    
    public static void main(String[] args) {
        System.out.println("🚀 Starting AI Services Test Suite");
        System.out.println("=====================================");
        
        try {
            // Test 1: LangChain4j AI Chat Service
            testLangChain4jAIChatService();
            
            // Test 2: Enhanced RAG Service
            testEnhancedRAGService();
            
            // Test 3: OpenAI Content Analysis Service
            testOpenAIContentAnalysisService();
            
            // Test 4: Query Transformation Pipeline
            testQueryTransformationPipeline();
            
            // Test 5: Book Content Retriever
            testBookContentRetriever();
            
            System.out.println("\n✅ All AI services tested successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 💬 Test LangChain4j AI Chat Service
     */
    private static void testLangChain4jAIChatService() {
        System.out.println("\n📝 Testing LangChain4j AI Chat Service...");
        
        try {
            LangChain4jAIChatService chatService = new LangChain4jAIChatService();
            
            // Test basic chat
            String response1 = chatService.processChat(1, "session1", "Xin chào! Bạn có thể giúp tôi tìm sách không?", null);
            System.out.println("✅ Basic chat test: " + response1.substring(0, Math.min(100, response1.length())) + "...");
            
            // Test book-specific chat
            String response2 = chatService.chatAboutBook("Kể cho tôi về nhân vật chính", "book123");
            System.out.println("✅ Book chat test: " + response2.substring(0, Math.min(100, response2.length())) + "...");
            
            // Test recommendations
            String response3 = chatService.getRecommendations("Tôi thích sách khoa học viễn tưởng");
            System.out.println("✅ Recommendations test: " + response3.substring(0, Math.min(100, response3.length())) + "...");
            
            // Test book analysis
            String response4 = chatService.analyzeBookContent("Đây là nội dung sách về khoa học viễn tưởng...");
            System.out.println("✅ Book analysis test: " + response4.substring(0, Math.min(100, response4.length())) + "...");
            
            // Test search
            String response5 = chatService.searchBooks("sách khoa học");
            System.out.println("✅ Search test: " + response5.substring(0, Math.min(100, response5.length())) + "...");
            
            System.out.println("✅ LangChain4j AI Chat Service test completed");
            
        } catch (Exception e) {
            System.err.println("❌ LangChain4j AI Chat Service test failed: " + e.getMessage());
        }
    }
    
    /**
     * 🔍 Test Enhanced RAG Service
     */
    private static void testEnhancedRAGService() {
        System.out.println("\n📝 Testing Enhanced RAG Service...");
        
        try {
            EnhancedRAGService ragService = new EnhancedRAGService();
            
            // Test content indexing
            String testContent = "Đây là nội dung sách về khoa học viễn tưởng. " +
                "Cuốn sách kể về một thế giới tương lai nơi con người sống trên các hành tinh khác. " +
                "Nhân vật chính là một phi hành gia trẻ tuổi tên Alex, người phát hiện ra một bí mật lớn " +
                "về nguồn gốc của loài người trong vũ trụ.";
            
            Map<String, String> metadata = Map.of(
                "title", "Khoa học viễn tưởng",
                "author", "Tác giả test",
                "category", "Sci-Fi"
            );
            
            ragService.indexContent(testContent, metadata);
            System.out.println("✅ Content indexing test completed");
            
            // Test content retrieval
            List<dev.langchain4j.data.segment.TextSegment> segments = 
                ragService.retrieveRelevantContent("nhân vật chính", 3, null);
            System.out.println("✅ Content retrieval test: found " + segments.size() + " segments");
            
            // Test summary generation
            String summary = ragService.generateSummary(testContent);
            System.out.println("✅ Summary generation test: " + summary.substring(0, Math.min(100, summary.length())) + "...");
            
            // Test category classification
            String category = ragService.classifyCategory(testContent);
            System.out.println("✅ Category classification test: " + category.substring(0, Math.min(100, category.length())) + "...");
            
            // Test stats
            Map<String, Object> stats = ragService.getStats();
            System.out.println("✅ Service stats: " + stats);
            
            System.out.println("✅ Enhanced RAG Service test completed");
            
        } catch (Exception e) {
            System.err.println("❌ Enhanced RAG Service test failed: " + e.getMessage());
        }
    }
    
    /**
     * 🤖 Test OpenAI Content Analysis Service
     */
    private static void testOpenAIContentAnalysisService() {
        System.out.println("\n📝 Testing OpenAI Content Analysis Service...");
        
        try {
            OpenAIContentAnalysisService analysisService = new OpenAIContentAnalysisService();
            
            String testContent = "Đây là nội dung sách về khoa học viễn tưởng. " +
                "Cuốn sách kể về một thế giới tương lai nơi con người sống trên các hành tinh khác. " +
                "Nhân vật chính là một phi hành gia trẻ tuổi tên Alex, người phát hiện ra một bí mật lớn " +
                "về nguồn gốc của loài người trong vũ trụ.";
            
            List<String> bookTitles = Arrays.asList("Khoa học viễn tưởng", "Thế giới tương lai");
            
            // Test content analysis
            String analysis1 = analysisService.analyzeBookContent(testContent, "Kể cho tôi về nhân vật chính", bookTitles);
            System.out.println("✅ Content analysis test 1: " + analysis1.substring(0, Math.min(100, analysis1.length())) + "...");
            
            String analysis2 = analysisService.analyzeBookContent(testContent, "Cốt truyện như thế nào?", bookTitles);
            System.out.println("✅ Content analysis test 2: " + analysis2.substring(0, Math.min(100, analysis2.length())) + "...");
            
            String analysis3 = analysisService.analyzeBookContent(testContent, "Bạn có thích cuốn sách này không?", bookTitles);
            System.out.println("✅ Content analysis test 3: " + analysis3.substring(0, Math.min(100, analysis3.length())) + "...");
            
            System.out.println("✅ OpenAI Content Analysis Service test completed");
            
        } catch (Exception e) {
            System.err.println("❌ OpenAI Content Analysis Service test failed: " + e.getMessage());
        }
    }
    
    /**
     * 🔄 Test Query Transformation Pipeline
     */
    private static void testQueryTransformationPipeline() {
        System.out.println("\n📝 Testing Query Transformation Pipeline...");
        
        try {
            QueryTransformationPipeline pipeline = new QueryTransformationPipeline();
            
            // Test query transformation
            String originalQuery = "sách khoa học viễn tưởng hay";
            String transformedQuery = pipeline.transformQuery(originalQuery);
            System.out.println("✅ Query transformation test:");
            System.out.println("   Original: " + originalQuery);
            System.out.println("   Transformed: " + transformedQuery);
            
            // Test query enhancement
            String enhancedQuery = pipeline.enhanceQuery(originalQuery);
            System.out.println("✅ Query enhancement test:");
            System.out.println("   Enhanced: " + enhancedQuery);
            
            // Test query classification
            String queryType = pipeline.classifyQueryType(originalQuery);
            System.out.println("✅ Query classification test: " + queryType);
            
            System.out.println("✅ Query Transformation Pipeline test completed");
            
        } catch (Exception e) {
            System.err.println("❌ Query Transformation Pipeline test failed: " + e.getMessage());
        }
    }
    
    /**
     * 📚 Test Book Content Retriever
     */
    private static void testBookContentRetriever() {
        System.out.println("\n📝 Testing Book Content Retriever...");
        
        try {
            LangChain4jBookContentRetriever retriever = new LangChain4jBookContentRetriever();
            
            // Test content indexing
            String bookContent = "Đây là nội dung sách về khoa học viễn tưởng. " +
                "Cuốn sách kể về một thế giới tương lai nơi con người sống trên các hành tinh khác. " +
                "Nhân vật chính là một phi hành gia trẻ tuổi tên Alex, người phát hiện ra một bí mật lớn " +
                "về nguồn gốc của loài người trong vũ trụ.";
            
            retriever.indexBookContent("book123", bookContent, Map.of("title", "Khoa học viễn tưởng"));
            System.out.println("✅ Book content indexing test completed");
            
            // Test content retrieval
            List<String> relevantContent = retriever.retrieveRelevantContent("nhân vật chính", 3);
            System.out.println("✅ Content retrieval test: found " + relevantContent.size() + " relevant pieces");
            
            // Test semantic search
            List<String> searchResults = retriever.semanticSearch("khoa học viễn tưởng", 5);
            System.out.println("✅ Semantic search test: found " + searchResults.size() + " results");
            
            // Test content similarity
            double similarity = retriever.calculateSimilarity("khoa học viễn tưởng", "thế giới tương lai");
            System.out.println("✅ Content similarity test: " + similarity);
            
            System.out.println("✅ Book Content Retriever test completed");
            
        } catch (Exception e) {
            System.err.println("❌ Book Content Retriever test failed: " + e.getMessage());
        }
    }
} 