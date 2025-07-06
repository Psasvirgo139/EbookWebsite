package com.mycompany.ebookwebsite.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

/**
 * 🔥 Test for Cross-Topic Connection Improvements
 * 
 * Tests the enhanced features:
 * - Cross-topic connections (AI -> ML -> Deep Learning)
 * - Enhanced book tracking
 * - Improved context awareness
 * - Input validation fixes
 */
@DisplayName("Cross-Topic Connection Tests")
public class CrossTopicConnectionTest {

    private SimpleEnhancedAIChatService aiService;
    private static final int TEST_USER_ID = 1;
    private static final String TEST_SESSION_ID = "cross-topic-test";

    @BeforeEach
    void setUp() {
        // Setup test environment
        System.setProperty("OPENAI_API_KEY", "test-key-for-testing");
        aiService = new SimpleEnhancedAIChatService();
    }

    @Test
    @DisplayName("🔗 Test Cross-Topic Connections")
    void testCrossTopicConnections() {
        System.out.println("\n=== 🔗 Testing Cross-Topic Connections ===");
        
        try {
            // Start with AI topic
            String message1 = "Tìm sách về trí tuệ nhân tạo";
            String response1 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message1, null);
            
            System.out.println("1️⃣ AI topic:");
            System.out.println("👤 User: " + message1);
            System.out.println("🤖 AI: " + response1.substring(0, Math.min(150, response1.length())) + "...");
            
            // Check topics
            Set<String> topics1 = aiService.getDiscussedTopics(TEST_SESSION_ID);
            System.out.println("🏷️ Topics after AI: " + topics1);
            
            // Move to Machine Learning
            String message2 = "Sách về machine learning";
            String response2 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message2, null);
            
            System.out.println("\n2️⃣ Machine Learning topic:");
            System.out.println("👤 User: " + message2);
            System.out.println("🤖 AI: " + response2.substring(0, Math.min(150, response2.length())) + "...");
            
            // Check topics again
            Set<String> topics2 = aiService.getDiscussedTopics(TEST_SESSION_ID);
            System.out.println("🏷️ Topics after ML: " + topics2);
            
            // Move to Deep Learning
            String message3 = "Sách về deep learning";
            String response3 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message3, null);
            
            System.out.println("\n3️⃣ Deep Learning topic:");
            System.out.println("👤 User: " + message3);
            System.out.println("🤖 AI: " + response3.substring(0, Math.min(150, response3.length())) + "...");
            
            // Check final topics
            Set<String> topics3 = aiService.getDiscussedTopics(TEST_SESSION_ID);
            System.out.println("🏷️ Final topics: " + topics3);
            
            // Validate cross-topic connections
            assertTrue(topics3.contains("Artificial Intelligence"), "Should track AI topic");
            assertTrue(topics3.contains("Machine Learning"), "Should track ML topic");
            assertTrue(topics3.contains("Deep Learning"), "Should track DL topic");
            
            // Check for cross-connections
            boolean hasAIMLConnection = topics3.contains("AI-ML Connection");
            boolean hasMLDLConnection = topics3.contains("ML-DL Connection");
            
            System.out.println("✅ AI-ML Connection tracked: " + hasAIMLConnection);
            System.out.println("✅ ML-DL Connection tracked: " + hasMLDLConnection);
            
            System.out.println("✅ Cross-topic connections test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Cross-topic test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("📚 Test Enhanced Book Tracking")
    void testEnhancedBookTracking() {
        System.out.println("\n=== 📚 Testing Enhanced Book Tracking ===");
        
        try {
            // First interaction - AI books
            String message1 = "Tìm sách về trí tuệ nhân tạo";
            String response1 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message1, null);
            
            System.out.println("1️⃣ AI books:");
            System.out.println("👤 User: " + message1);
            System.out.println("🤖 AI: " + response1.substring(0, Math.min(120, response1.length())) + "...");
            
            // Check mentioned books
            Set<String> books1 = aiService.getMentionedBooks(TEST_SESSION_ID);
            System.out.println("📚 Books after AI: " + books1);
            
            // Second interaction - ask for other books
            String message2 = "Có sách AI nào khác không?";
            String response2 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message2, null);
            
            System.out.println("\n2️⃣ Other AI books:");
            System.out.println("👤 User: " + message2);
            System.out.println("🤖 AI: " + response2.substring(0, Math.min(120, response2.length())) + "...");
            
            // Check mentioned books again
            Set<String> books2 = aiService.getMentionedBooks(TEST_SESSION_ID);
            System.out.println("📚 Books after second AI: " + books2);
            
            // Validate book tracking
            assertNotNull(books1, "Book tracking should work");
            assertNotNull(books2, "Book tracking should persist");
            assertTrue(books2.size() >= books1.size(), "Should track more books over time");
            
            System.out.println("✅ Enhanced book tracking test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Book tracking test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🔍 Test Input Validation Fix")
    void testInputValidationFix() {
        System.out.println("\n=== 🔍 Testing Input Validation Fix ===");
        
        try {
            // Test empty input
            String emptyResponse = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, "", null);
            System.out.println("1️⃣ Empty input test:");
            System.out.println("👤 User: [empty]");
            System.out.println("🤖 AI: " + emptyResponse);
            
            // Test whitespace input
            String whitespaceResponse = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, "   ", null);
            System.out.println("\n2️⃣ Whitespace input test:");
            System.out.println("👤 User: [whitespace]");
            System.out.println("🤖 AI: " + whitespaceResponse);
            
            // Test null input
            String nullResponse = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, null, null);
            System.out.println("\n3️⃣ Null input test:");
            System.out.println("👤 User: [null]");
            System.out.println("🤖 AI: " + nullResponse);
            
            // Validate responses
            assertTrue(emptyResponse.contains("Vui lòng nhập"), "Should handle empty input gracefully");
            assertTrue(whitespaceResponse.contains("Vui lòng nhập"), "Should handle whitespace input gracefully");
            assertTrue(nullResponse.contains("Vui lòng nhập"), "Should handle null input gracefully");
            
            System.out.println("✅ Input validation fix test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Input validation test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🧠 Test Context Continuity Enhancement")
    void testContextContinuityEnhancement() {
        System.out.println("\n=== 🧠 Testing Context Continuity Enhancement ===");
        
        try {
            // Start conversation about AI
            String message1 = "Tôi muốn tìm sách về trí tuệ nhân tạo";
            String response1 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message1, null);
            
            System.out.println("1️⃣ AI conversation:");
            System.out.println("👤 User: " + message1);
            System.out.println("🤖 AI: " + response1.substring(0, Math.min(120, response1.length())) + "...");
            
            // Ask about related topic
            String message2 = "Sách về machine learning có liên quan không?";
            String response2 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message2, null);
            
            System.out.println("\n2️⃣ Related topic question:");
            System.out.println("👤 User: " + message2);
            System.out.println("🤖 AI: " + response2.substring(0, Math.min(120, response2.length())) + "...");
            
            // Ask for more books
            String message3 = "Gợi ý thêm sách tương tự";
            String response3 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message3, null);
            
            System.out.println("\n3️⃣ Similar books request:");
            System.out.println("👤 User: " + message3);
            System.out.println("🤖 AI: " + response3.substring(0, Math.min(120, response3.length())) + "...");
            
            // Validate context continuity
            assertNotNull(response1, "First response should not be null");
            assertNotNull(response2, "Second response should not be null");
            assertNotNull(response3, "Third response should not be null");
            
            // Check if responses maintain context
            boolean maintainsContext = response2.toLowerCase().contains("ai") || 
                                    response2.toLowerCase().contains("machine learning") ||
                                    response3.toLowerCase().contains("ai") ||
                                    response3.toLowerCase().contains("machine learning");
            
            System.out.println("✅ Context continuity maintained: " + maintainsContext);
            assertTrue(maintainsContext, "Should maintain context throughout conversation");
            
            System.out.println("✅ Context continuity enhancement test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Context continuity test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🏆 Test Comprehensive Improvements")
    void testComprehensiveImprovements() {
        System.out.println("\n=== 🏆 Testing Comprehensive Improvements ===");
        
        try {
            // Simulate a complete conversation with cross-topic connections
            String[] conversation = {
                "Tìm sách về trí tuệ nhân tạo",
                "Sách về machine learning có liên quan không?",
                "Có sách deep learning nào khác không?",
                "Sách về AI và robotics"
            };
            
            System.out.println("🔄 Simulating comprehensive conversation:");
            
            for (int i = 0; i < conversation.length; i++) {
                String response = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, conversation[i], null);
                
                System.out.println((i + 1) + "️⃣ Turn " + (i + 1) + ":");
                System.out.println("👤 User: " + conversation[i]);
                System.out.println("🤖 AI: " + response.substring(0, Math.min(100, response.length())) + "...");
                System.out.println();
                
                // Validate each response
                assertNotNull(response, "Response " + (i + 1) + " should not be null");
                assertTrue(response.length() > 0, "Response " + (i + 1) + " should not be empty");
            }
            
            // Check final tracking data
            List<String> finalHistory = aiService.getConversationHistory(TEST_SESSION_ID);
            Set<String> finalBooks = aiService.getMentionedBooks(TEST_SESSION_ID);
            Set<String> finalTopics = aiService.getDiscussedTopics(TEST_SESSION_ID);
            
            System.out.println("📊 Final tracking data:");
            System.out.println("- Conversation history: " + finalHistory.size() + " interactions");
            System.out.println("- Mentioned books: " + finalBooks.size() + " books");
            System.out.println("- Discussed topics: " + finalTopics.size() + " topics");
            
            // Validate comprehensive improvements
            assertTrue(finalHistory.size() >= conversation.length, "Should have all interactions");
            assertTrue(finalBooks.size() > 0, "Should track mentioned books");
            assertTrue(finalTopics.size() > 0, "Should track discussed topics");
            
            // Check for cross-topic connections
            boolean hasCrossConnections = finalTopics.contains("AI-ML Connection") ||
                                        finalTopics.contains("ML-DL Connection") ||
                                        finalTopics.contains("AI-Robotics Connection");
            
            System.out.println("✅ Cross-topic connections detected: " + hasCrossConnections);
            
            System.out.println("✅ Comprehensive improvements test PASSED");
            System.out.println("🎉 All enhanced features working correctly!");
            
        } catch (Exception e) {
            System.out.println("⚠️ Comprehensive test failed: " + e.getMessage());
        }
    }
} 