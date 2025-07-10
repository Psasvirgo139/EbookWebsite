package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.service.QueryTransformationPipeline;

/**
 * 🧪 Test Query Transformation Pipeline
 */
public class QueryTransformationTest {
    
    public static void main(String[] args) {
        System.out.println("🚀 Testing Query Transformation Pipeline");
        System.out.println("========================================");
        
        try {
            QueryTransformationPipeline pipeline = new QueryTransformationPipeline();
            
            // Test queries
            String[] testQueries = {
                "sách khoa học viễn tưởng hay",
                "tìm sách về nhân vật chính",
                "gợi ý sách tương tự",
                "phân tích nội dung sách",
                "trò chuyện về sách"
            };
            
            // Test 1: Query transformation
            System.out.println("\n📝 Test 1: Query transformation");
            for (String query : testQueries) {
                String transformedQuery = pipeline.transformQuery(query);
                System.out.println("Original: " + query);
                System.out.println("Transformed: " + transformedQuery);
                System.out.println("---");
            }
            
            // Test 2: Query enhancement
            System.out.println("\n📝 Test 2: Query enhancement");
            for (String query : testQueries) {
                String enhancedQuery = pipeline.enhanceQuery(query);
                System.out.println("Original: " + query);
                System.out.println("Enhanced: " + enhancedQuery);
                System.out.println("---");
            }
            
            // Test 3: Query classification
            System.out.println("\n📝 Test 3: Query classification");
            for (String query : testQueries) {
                String queryType = pipeline.classifyQueryType(query);
                System.out.println("Query: " + query);
                System.out.println("Type: " + queryType);
                System.out.println("---");
            }
            
            // Test 4: Pipeline stats
            System.out.println("\n📝 Test 4: Pipeline stats");
            String stats = pipeline.getPipelineStats();
            System.out.println("✅ Stats: " + stats);
            
            System.out.println("\n✅ All Query Transformation Pipeline tests completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Query Transformation Pipeline test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 