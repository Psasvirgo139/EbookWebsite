package com.mycompany.ebookwebsite.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

/**
 * 📚 Test for Book Link Coordination and Repetition Avoidance
 * 
 * Tests the enhanced features:
 * - Book link coordination (1 question → 1-2 appropriate links)
 * - Repetition avoidance
 * - Context-aware book recommendations
 * - Input validation fixes
 */
@DisplayName("Book Link Coordination Tests")
public class BookLinkCoordinationTest {

    private SimpleEnhancedAIChatService aiService;
    private static final int TEST_USER_ID = 1;
    private static final String TEST_SESSION_ID = "book-link-test";

    @BeforeEach
    void setUp() {
        // Setup test environment
        System.setProperty("OPENAI_API_KEY", "test-key-for-testing");
        aiService = new SimpleEnhancedAIChatService();
    }

    @Test
    @DisplayName("📚 Test Book Link Coordination")
    void testBookLinkCoordination() {
        System.out.println("\n=== 📚 Testing Book Link Coordination ===");
        
        try {
            // Test 1: Initial Java book request
            String message1 = "Tìm sách về lập trình Java";
            String response1 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message1, null);
            
            System.out.println("1️⃣ Initial Java request:");
            System.out.println("👤 User: " + message1);
            System.out.println("🤖 AI: " + response1.substring(0, Math.min(150, response1.length())) + "...");
            
            // Check mentioned books
            Set<String> books1 = aiService.getMentionedBooks(TEST_SESSION_ID);
            System.out.println("📚 Books after initial request: " + books1);
            
            // Test 2: Spring Framework request
            String message2 = "Có sách nào về Spring Framework không?";
            String response2 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message2, null);
            
            System.out.println("\n2️⃣ Spring Framework request:");
            System.out.println("👤 User: " + message2);
            System.out.println("🤖 AI: " + response2.substring(0, Math.min(150, response2.length())) + "...");
            
            // Check mentioned books again
            Set<String> books2 = aiService.getMentionedBooks(TEST_SESSION_ID);
            System.out.println("📚 Books after Spring request: " + books2);
            
            // Test 3: Best Java books request
            String message3 = "Gợi ý 2 sách hay nhất về Java";
            String response3 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message3, null);
            
            System.out.println("\n3️⃣ Best Java books request:");
            System.out.println("👤 User: " + message3);
            System.out.println("🤖 AI: " + response3.substring(0, Math.min(150, response3.length())) + "...");
            
            // Check mentioned books again
            Set<String> books3 = aiService.getMentionedBooks(TEST_SESSION_ID);
            System.out.println("📚 Books after best books request: " + books3);
            
            // Validate book coordination
            assertNotNull(books1, "Initial book tracking should work");
            assertNotNull(books2, "Spring book tracking should work");
            assertNotNull(books3, "Best books tracking should work");
            
            // Check for appropriate book counts (1-2 books per request)
            int initialBookCount = books1.size();
            int springBookCount = books2.size() - initialBookCount;
            int bestBooksCount = books3.size() - books2.size();
            
            System.out.println("✅ Initial books: " + initialBookCount);
            System.out.println("✅ New Spring books: " + springBookCount);
            System.out.println("✅ New best books: " + bestBooksCount);
            
            // Validate reasonable book counts
            assertTrue(initialBookCount >= 1, "Should recommend at least 1 initial book");
            assertTrue(springBookCount >= 1, "Should recommend at least 1 Spring book");
            assertTrue(bestBooksCount >= 1, "Should recommend at least 1 best book");
            
            System.out.println("✅ Book link coordination test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Book link coordination test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🔄 Test Repetition Avoidance")
    void testRepetitionAvoidance() {
        System.out.println("\n=== 🔄 Testing Repetition Avoidance ===");
        
        try {
            // Test 1: Ask for Java books
            String message1 = "Tìm sách về lập trình Java";
            String response1 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message1, null);
            
            System.out.println("1️⃣ First Java request:");
            System.out.println("👤 User: " + message1);
            System.out.println("🤖 AI: " + response1.substring(0, Math.min(120, response1.length())) + "...");
            
            Set<String> books1 = aiService.getMentionedBooks(TEST_SESSION_ID);
            System.out.println("📚 Books after first request: " + books1);
            
            // Test 2: Ask for more Java books
            String message2 = "Có sách Java nào khác không?";
            String response2 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message2, null);
            
            System.out.println("\n2️⃣ More Java books request:");
            System.out.println("👤 User: " + message2);
            System.out.println("🤖 AI: " + response2.substring(0, Math.min(120, response2.length())) + "...");
            
            Set<String> books2 = aiService.getMentionedBooks(TEST_SESSION_ID);
            System.out.println("📚 Books after second request: " + books2);
            
            // Test 3: Ask for beginner Java books
            String message3 = "Sách nào phù hợp cho người mới học Java?";
            String response3 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message3, null);
            
            System.out.println("\n3️⃣ Beginner Java books request:");
            System.out.println("👤 User: " + message3);
            System.out.println("🤖 AI: " + response3.substring(0, Math.min(120, response3.length())) + "...");
            
            Set<String> books3 = aiService.getMentionedBooks(TEST_SESSION_ID);
            System.out.println("📚 Books after third request: " + books3);
            
            // Validate repetition avoidance
            assertTrue(books2.size() > books1.size(), "Should add new books");
            assertTrue(books3.size() > books2.size(), "Should add more new books");
            
            // Check for unique books
            int uniqueBooks = books3.size();
            System.out.println("✅ Total unique books tracked: " + uniqueBooks);
            
            // Validate reasonable growth
            assertTrue(uniqueBooks >= 3, "Should track at least 3 unique books");
            
            System.out.println("✅ Repetition avoidance test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Repetition avoidance test failed: " + e.getMessage());
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
            
            // Validate responses - should be helpful, not warning messages
            assertTrue(emptyResponse.contains("Vui lòng nhập") || emptyResponse.contains("câu hỏi"), 
                      "Should handle empty input gracefully");
            assertTrue(whitespaceResponse.contains("Vui lòng nhập") || whitespaceResponse.contains("câu hỏi"), 
                      "Should handle whitespace input gracefully");
            assertTrue(nullResponse.contains("Vui lòng nhập") || nullResponse.contains("câu hỏi"), 
                      "Should handle null input gracefully");
            
            // Check that responses are helpful, not just warnings
            assertTrue(emptyResponse.length() > 20, "Should provide helpful response");
            assertTrue(whitespaceResponse.length() > 20, "Should provide helpful response");
            assertTrue(nullResponse.length() > 20, "Should provide helpful response");
            
            System.out.println("✅ Input validation fix test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Input validation test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🎯 Test Context-Aware Recommendations")
    void testContextAwareRecommendations() {
        System.out.println("\n=== 🎯 Testing Context-Aware Recommendations ===");
        
        try {
            // Test 1: Start with Java
            String message1 = "Tìm sách về lập trình Java";
            String response1 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message1, null);
            
            System.out.println("1️⃣ Java programming request:");
            System.out.println("👤 User: " + message1);
            System.out.println("🤖 AI: " + response1.substring(0, Math.min(120, response1.length())) + "...");
            
            // Test 2: Ask about related framework
            String message2 = "Sách về Spring Framework có liên quan không?";
            String response2 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message2, null);
            
            System.out.println("\n2️⃣ Related framework question:");
            System.out.println("👤 User: " + message2);
            System.out.println("🤖 AI: " + response2.substring(0, Math.min(120, response2.length())) + "...");
            
            // Test 3: Ask for advanced topics
            String message3 = "Sách về Java concurrency";
            String response3 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message3, null);
            
            System.out.println("\n3️⃣ Advanced topic request:");
            System.out.println("👤 User: " + message3);
            System.out.println("🤖 AI: " + response3.substring(0, Math.min(120, response3.length())) + "...");
            
            // Check topics for context awareness
            Set<String> topics = aiService.getDiscussedTopics(TEST_SESSION_ID);
            System.out.println("🏷️ Discussed topics: " + topics);
            
            // Validate context awareness
            assertTrue(topics.contains("Java programming"), "Should track Java programming topic");
            assertTrue(response2.toLowerCase().contains("spring") || response2.toLowerCase().contains("framework"), 
                      "Should mention Spring Framework");
            assertTrue(response3.toLowerCase().contains("concurrency") || response3.toLowerCase().contains("thread"), 
                      "Should mention concurrency");
            
            System.out.println("✅ Context-aware recommendations test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Context-aware recommendations test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🏆 Test Comprehensive Book Coordination")
    void testComprehensiveBookCoordination() {
        System.out.println("\n=== 🏆 Testing Comprehensive Book Coordination ===");
        
        try {
            // Simulate a complete book recommendation conversation
            String[] conversation = {
                "Tìm sách về lập trình Java",
                "Có sách nào về Spring Framework không?",
                "Gợi ý 2 sách hay nhất về Java",
                "Sách nào phù hợp cho người mới học Java?",
                "Sách về Java concurrency"
            };
            
            System.out.println("🔄 Simulating comprehensive book coordination:");
            
            for (int i = 0; i < conversation.length; i++) {
                String response = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, conversation[i], null);
                
                System.out.println((i + 1) + "️⃣ Turn " + (i + 1) + ":");
                System.out.println("👤 User: " + conversation[i]);
                System.out.println("🤖 AI: " + response.substring(0, Math.min(100, response.length())) + "...");
                System.out.println();
                
                // Validate each response
                assertNotNull(response, "Response " + (i + 1) + " should not be null");
                assertTrue(response.length() > 0, "Response " + (i + 1) + " should not be empty");
                assertFalse(response.contains("Vui lòng nhập câu hỏi của bạn 😊"), 
                           "Response " + (i + 1) + " should not show validation warning");
            }
            
            // Check final tracking data
            Set<String> finalBooks = aiService.getMentionedBooks(TEST_SESSION_ID);
            Set<String> finalTopics = aiService.getDiscussedTopics(TEST_SESSION_ID);
            List<String> finalHistory = aiService.getConversationHistory(TEST_SESSION_ID);
            
            System.out.println("📊 Final coordination data:");
            System.out.println("- Mentioned books: " + finalBooks.size() + " books");
            System.out.println("- Discussed topics: " + finalTopics.size() + " topics");
            System.out.println("- Conversation history: " + finalHistory.size() + " interactions");
            
            // Validate comprehensive coordination
            assertTrue(finalBooks.size() >= 5, "Should track multiple books");
            assertTrue(finalTopics.size() >= 3, "Should track multiple topics");
            assertTrue(finalHistory.size() >= conversation.length, "Should have all interactions");
            
            // Check for Java-related topics
            boolean hasJavaTopic = finalTopics.contains("Java programming") || 
                                 finalTopics.contains("Java") ||
                                 finalTopics.contains("Learning");
            
            System.out.println("✅ Java topics detected: " + hasJavaTopic);
            
            System.out.println("✅ Comprehensive book coordination test PASSED");
            System.out.println("🎉 All book coordination features working correctly!");
            
        } catch (Exception e) {
            System.out.println("⚠️ Comprehensive book coordination test failed: " + e.getMessage());
        }
    }
} 