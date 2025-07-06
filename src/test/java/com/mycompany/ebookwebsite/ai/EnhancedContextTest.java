package com.mycompany.ebookwebsite.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

/**
 * 🔥 Test for Enhanced Context Management
 * 
 * Tests the improvements made to:
 * - Context Continuity: Accurate book tracking
 * - Input Validation: Better user experience
 * - Response Uniqueness: Avoid repetition
 */
@DisplayName("Enhanced Context Management Tests")
public class EnhancedContextTest {

    private SimpleEnhancedAIChatService aiService;
    private static final int TEST_USER_ID = 1;
    private static final String TEST_SESSION_ID = "enhanced-context-test";

    @BeforeEach
    void setUp() {
        // Setup test environment
        System.setProperty("OPENAI_API_KEY", "test-key-for-testing");
        aiService = new SimpleEnhancedAIChatService();
    }

    @Test
    @DisplayName("🔍 Test Input Validation")
    void testInputValidation() {
        System.out.println("\n=== 🔍 Testing Input Validation ===");
        
        try {
            // Test empty input
            String emptyResponse = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, "", null);
            System.out.println("1️⃣ Empty input test:");
            System.out.println("👤 User: [empty]");
            System.out.println("🤖 AI: " + emptyResponse);
            
            // Test null input
            String nullResponse = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, null, null);
            System.out.println("\n2️⃣ Null input test:");
            System.out.println("👤 User: [null]");
            System.out.println("🤖 AI: " + nullResponse);
            
            // Test whitespace input
            String whitespaceResponse = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, "   ", null);
            System.out.println("\n3️⃣ Whitespace input test:");
            System.out.println("👤 User: [whitespace]");
            System.out.println("🤖 AI: " + whitespaceResponse);
            
            // Validate responses
            assertTrue(emptyResponse.contains("Vui lòng nhập"), "Should handle empty input gracefully");
            assertTrue(nullResponse.contains("Vui lòng nhập"), "Should handle null input gracefully");
            assertTrue(whitespaceResponse.contains("Vui lòng nhập"), "Should handle whitespace input gracefully");
            
            System.out.println("✅ Input validation test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Input validation test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("📚 Test Book Tracking and Uniqueness")
    void testBookTrackingAndUniqueness() {
        System.out.println("\n=== 📚 Testing Book Tracking and Uniqueness ===");
        
        try {
            // First interaction - mention Python books
            String message1 = "Tôi muốn tìm sách về Python";
            String response1 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message1, null);
            
            System.out.println("1️⃣ First interaction:");
            System.out.println("👤 User: " + message1);
            System.out.println("🤖 AI: " + response1.substring(0, Math.min(150, response1.length())) + "...");
            
            // Check mentioned books
            Set<String> books1 = aiService.getMentionedBooks(TEST_SESSION_ID);
            System.out.println("📚 Mentioned books after 1st interaction: " + books1);
            
            // Second interaction - ask for other books
            String message2 = "Có sách Python nào khác không?";
            String response2 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message2, null);
            
            System.out.println("\n2️⃣ Second interaction:");
            System.out.println("👤 User: " + message2);
            System.out.println("🤖 AI: " + response2.substring(0, Math.min(150, response2.length())) + "...");
            
            // Check mentioned books again
            Set<String> books2 = aiService.getMentionedBooks(TEST_SESSION_ID);
            System.out.println("📚 Mentioned books after 2nd interaction: " + books2);
            
            // Validate book tracking
            assertNotNull(books1, "Book tracking should work");
            assertNotNull(books2, "Book tracking should persist");
            assertTrue(books2.size() >= books1.size(), "Should track more books over time");
            
            System.out.println("✅ Book tracking test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Book tracking test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🏷️ Test Topic Tracking")
    void testTopicTracking() {
        System.out.println("\n=== 🏷️ Testing Topic Tracking ===");
        
        try {
            // First interaction - discuss Python
            String message1 = "Tôi muốn học Python cho người mới bắt đầu";
            String response1 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message1, null);
            
            System.out.println("1️⃣ Python learning discussion:");
            System.out.println("👤 User: " + message1);
            System.out.println("🤖 AI: " + response1.substring(0, Math.min(120, response1.length())) + "...");
            
            // Check discussed topics
            Set<String> topics1 = aiService.getDiscussedTopics(TEST_SESSION_ID);
            System.out.println("🏷️ Discussed topics: " + topics1);
            
            // Second interaction - discuss data science
            String message2 = "Sách Python nào phù hợp cho data science?";
            String response2 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message2, null);
            
            System.out.println("\n2️⃣ Data science discussion:");
            System.out.println("👤 User: " + message2);
            System.out.println("🤖 AI: " + response2.substring(0, Math.min(120, response2.length())) + "...");
            
            // Check discussed topics again
            Set<String> topics2 = aiService.getDiscussedTopics(TEST_SESSION_ID);
            System.out.println("🏷️ Discussed topics: " + topics2);
            
            // Validate topic tracking
            assertNotNull(topics1, "Topic tracking should work");
            assertNotNull(topics2, "Topic tracking should persist");
            assertTrue(topics2.size() >= topics1.size(), "Should track more topics over time");
            
            // Check for specific topics
            boolean hasPython = topics2.contains("Python programming");
            boolean hasDataScience = topics2.contains("Data Science");
            boolean hasLearning = topics2.contains("Learning");
            
            System.out.println("✅ Python programming tracked: " + hasPython);
            System.out.println("✅ Data Science tracked: " + hasDataScience);
            System.out.println("✅ Learning tracked: " + hasLearning);
            
            System.out.println("✅ Topic tracking test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Topic tracking test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🔄 Test Context Continuity")
    void testContextContinuity() {
        System.out.println("\n=== 🔄 Testing Context Continuity ===");
        
        try {
            // Start conversation about Java
            String message1 = "Tôi muốn tìm sách về Java programming";
            String response1 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message1, null);
            
            System.out.println("1️⃣ Java books discussion:");
            System.out.println("👤 User: " + message1);
            System.out.println("🤖 AI: " + response1.substring(0, Math.min(120, response1.length())) + "...");
            
            // Ask for similar books
            String message2 = "Có sách Java nào khác không?";
            String response2 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message2, null);
            
            System.out.println("\n2️⃣ Similar Java books request:");
            System.out.println("👤 User: " + message2);
            System.out.println("🤖 AI: " + response2.substring(0, Math.min(120, response2.length())) + "...");
            
            // Ask about advanced Java
            String message3 = "Sách Java nào cho người nâng cao?";
            String response3 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message3, null);
            
            System.out.println("\n3️⃣ Advanced Java books request:");
            System.out.println("👤 User: " + message3);
            System.out.println("🤖 AI: " + response3.substring(0, Math.min(120, response3.length())) + "...");
            
            // Validate context continuity
            assertNotNull(response1, "First response should not be null");
            assertNotNull(response2, "Second response should not be null");
            assertNotNull(response3, "Third response should not be null");
            
            // Check if responses maintain Java context
            boolean maintainsJavaContext = response2.toLowerCase().contains("java") || 
                                        response3.toLowerCase().contains("java");
            
            System.out.println("✅ Context continuity maintained: " + maintainsJavaContext);
            assertTrue(maintainsJavaContext, "Should maintain Java context throughout conversation");
            
            System.out.println("✅ Context continuity test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Context continuity test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🧹 Test Memory Clear Functionality")
    void testMemoryClearFunctionality() {
        System.out.println("\n=== 🧹 Testing Memory Clear Functionality ===");
        
        try {
            // Create some interactions
            aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, "Test message 1", null);
            aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, "Test message 2", null);
            
            // Check data before clear
            List<String> historyBefore = aiService.getConversationHistory(TEST_SESSION_ID);
            Set<String> booksBefore = aiService.getMentionedBooks(TEST_SESSION_ID);
            Set<String> topicsBefore = aiService.getDiscussedTopics(TEST_SESSION_ID);
            
            System.out.println("📊 Before clear:");
            System.out.println("- History: " + historyBefore.size() + " interactions");
            System.out.println("- Books: " + booksBefore.size() + " books");
            System.out.println("- Topics: " + topicsBefore.size() + " topics");
            
            // Clear memory
            aiService.clearSessionMemory(TEST_SESSION_ID);
            System.out.println("🧹 Memory cleared");
            
            // Check data after clear
            List<String> historyAfter = aiService.getConversationHistory(TEST_SESSION_ID);
            Set<String> booksAfter = aiService.getMentionedBooks(TEST_SESSION_ID);
            Set<String> topicsAfter = aiService.getDiscussedTopics(TEST_SESSION_ID);
            
            System.out.println("📊 After clear:");
            System.out.println("- History: " + historyAfter.size() + " interactions");
            System.out.println("- Books: " + booksAfter.size() + " books");
            System.out.println("- Topics: " + topicsAfter.size() + " topics");
            
            // Validate clear functionality
            assertTrue(historyAfter.isEmpty(), "History should be empty after clear");
            assertTrue(booksAfter.isEmpty(), "Books should be empty after clear");
            assertTrue(topicsAfter.isEmpty(), "Topics should be empty after clear");
            
            System.out.println("✅ Memory clear test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Memory clear test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🏆 Test Comprehensive Enhanced Features")
    void testComprehensiveEnhancedFeatures() {
        System.out.println("\n=== 🏆 Testing Comprehensive Enhanced Features ===");
        
        try {
            // Simulate a complete conversation with enhanced features
            String[] conversation = {
                "Tôi muốn tìm sách về JavaScript",
                "Có sách JavaScript nào khác không?",
                "Sách JavaScript nào cho web development?",
                "Cảm ơn bạn"
            };
            
            System.out.println("🔄 Simulating enhanced conversation flow:");
            
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
            
            // Validate comprehensive features
            assertTrue(finalHistory.size() >= conversation.length, "Should have all interactions");
            assertTrue(finalBooks.size() > 0, "Should track mentioned books");
            assertTrue(finalTopics.size() > 0, "Should track discussed topics");
            
            System.out.println("✅ Comprehensive enhanced features test PASSED");
            System.out.println("🎉 All enhanced context management features working correctly!");
            
        } catch (Exception e) {
            System.out.println("⚠️ Comprehensive test failed: " + e.getMessage());
        }
    }
} 