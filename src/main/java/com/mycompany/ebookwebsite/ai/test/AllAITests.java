package com.mycompany.ebookwebsite.ai.test;

/**
 * 🧪 All AI Tests - Chạy tất cả AI service tests
 * 
 * Test suite cho:
 * - LangChain4j AI Chat Service
 * - Enhanced RAG Service
 * - OpenAI Content Analysis Service
 * - Query Transformation Pipeline
 * - Book Content Retriever
 */
public class AllAITests {
    
    public static void main(String[] args) {
        System.out.println("🚀 Starting All AI Services Test Suite");
        System.out.println("======================================");
        System.out.println("Testing all AI services with LangChain4j technology");
        System.out.println();
        
        int totalTests = 0;
        int passedTests = 0;
        int failedTests = 0;
        
        try {
            // Test 1: LangChain4j AI Chat Service
            System.out.println("📝 Testing LangChain4j AI Chat Service...");
            try {
                LangChain4jAIChatTest.main(args);
                System.out.println("✅ LangChain4j AI Chat Service: PASSED");
                passedTests++;
            } catch (Exception e) {
                System.err.println("❌ LangChain4j AI Chat Service: FAILED - " + e.getMessage());
                failedTests++;
            }
            totalTests++;
            
            System.out.println();
            
            // Test 2: Enhanced RAG Service
            System.out.println("📝 Testing Enhanced RAG Service...");
            try {
                EnhancedRAGTest.main(args);
                System.out.println("✅ Enhanced RAG Service: PASSED");
                passedTests++;
            } catch (Exception e) {
                System.err.println("❌ Enhanced RAG Service: FAILED - " + e.getMessage());
                failedTests++;
            }
            totalTests++;
            
            System.out.println();
            
            // Test 3: OpenAI Content Analysis Service
            System.out.println("📝 Testing OpenAI Content Analysis Service...");
            try {
                OpenAIContentAnalysisTest.main(args);
                System.out.println("✅ OpenAI Content Analysis Service: PASSED");
                passedTests++;
            } catch (Exception e) {
                System.err.println("❌ OpenAI Content Analysis Service: FAILED - " + e.getMessage());
                failedTests++;
            }
            totalTests++;
            
            System.out.println();
            
            // Test 4: Query Transformation Pipeline
            System.out.println("📝 Testing Query Transformation Pipeline...");
            try {
                QueryTransformationTest.main(args);
                System.out.println("✅ Query Transformation Pipeline: PASSED");
                passedTests++;
            } catch (Exception e) {
                System.err.println("❌ Query Transformation Pipeline: FAILED - " + e.getMessage());
                failedTests++;
            }
            totalTests++;
            
            System.out.println();
            
            // Test 5: Book Content Retriever
            System.out.println("📝 Testing Book Content Retriever...");
            try {
                BookContentRetrieverTest.main(args);
                System.out.println("✅ Book Content Retriever: PASSED");
                passedTests++;
            } catch (Exception e) {
                System.err.println("❌ Book Content Retriever: FAILED - " + e.getMessage());
                failedTests++;
            }
            totalTests++;
            
        } catch (Exception e) {
            System.err.println("❌ Test suite failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Print summary
        System.out.println();
        System.out.println("📊 Test Summary");
        System.out.println("===============");
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + passedTests + " ✅");
        System.out.println("Failed: " + failedTests + " ❌");
        System.out.println("Success Rate: " + (totalTests > 0 ? (passedTests * 100 / totalTests) : 0) + "%");
        
        if (failedTests == 0) {
            System.out.println("\n🎉 All AI services are working correctly with LangChain4j!");
        } else {
            System.out.println("\n⚠️ Some AI services need attention. Check the error messages above.");
        }
        
        System.out.println("\n🔧 AI Services Tested:");
        System.out.println("   • LangChain4j AI Chat Service");
        System.out.println("   • Enhanced RAG Service");
        System.out.println("   • OpenAI Content Analysis Service");
        System.out.println("   • Query Transformation Pipeline");
        System.out.println("   • Book Content Retriever");
    }
} 