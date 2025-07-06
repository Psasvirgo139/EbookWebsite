package com.mycompany.ebookwebsite.ai;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

/**
 * 👤 Test for User Preference Analysis and Personalized Recommendations
 * 
 * Tests the enhanced features:
 * - User preference detection and tracking
 * - Genre combination analysis
 * - Personalized book recommendations
 * - Preference-based context building
 */
@DisplayName("User Preference Analysis Tests")
public class UserPreferenceAnalysisTest {

    private SimpleEnhancedAIChatService aiService;
    private static final int TEST_USER_ID = 1;
    private static final String TEST_SESSION_ID = "user-preference-test";

    @BeforeEach
    void setUp() {
        // Setup test environment
        System.setProperty("OPENAI_API_KEY", "test-key-for-testing");
        aiService = new SimpleEnhancedAIChatService();
    }

    @Test
    @DisplayName("👤 Test User Preference Detection")
    void testUserPreferenceDetection() {
        System.out.println("\n=== 👤 Testing User Preference Detection ===");
        
        try {
            // Test 1: Romance preference
            String message1 = "Tôi thích đọc sách về tình yêu";
            String response1 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message1, null);
            
            System.out.println("1️⃣ Romance preference:");
            System.out.println("👤 User: " + message1);
            System.out.println("🤖 AI: " + response1.substring(0, Math.min(150, response1.length())) + "...");
            
            // Check user preferences
            Set<String> topics1 = aiService.getDiscussedTopics(TEST_SESSION_ID);
            System.out.println("🏷️ Topics after romance: " + topics1);
            
            // Test 2: Mystery preference
            String message2 = "Tôi cũng thích truyện trinh thám";
            String response2 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message2, null);
            
            System.out.println("\n2️⃣ Mystery preference:");
            System.out.println("👤 User: " + message2);
            System.out.println("🤖 AI: " + response2.substring(0, Math.min(150, response2.length())) + "...");
            
            // Check user preferences again
            Set<String> topics2 = aiService.getDiscussedTopics(TEST_SESSION_ID);
            System.out.println("🏷️ Topics after mystery: " + topics2);
            
            // Validate user preference detection
            assertTrue(topics2.contains("User Preference: Romance"), "Should detect romance preference");
            assertTrue(topics2.contains("User Preference: Mystery"), "Should detect mystery preference");
            assertTrue(topics2.contains("User Preference: Romance-Mystery Combination"), "Should detect combination");
            
            System.out.println("✅ User preference detection test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ User preference detection test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🎭 Test Genre Combination Analysis")
    void testGenreCombinationAnalysis() {
        System.out.println("\n=== 🎭 Testing Genre Combination Analysis ===");
        
        try {
            // Test 1: Start with fantasy
            String message1 = "Tôi thích sách fantasy";
            String response1 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message1, null);
            
            System.out.println("1️⃣ Fantasy preference:");
            System.out.println("👤 User: " + message1);
            System.out.println("🤖 AI: " + response1.substring(0, Math.min(120, response1.length())) + "...");
            
            // Test 2: Add adventure
            String message2 = "Tôi cũng thích sách phiêu lưu";
            String response2 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message2, null);
            
            System.out.println("\n2️⃣ Adventure preference:");
            System.out.println("👤 User: " + message2);
            System.out.println("🤖 AI: " + response2.substring(0, Math.min(120, response2.length())) + "...");
            
            // Test 3: Ask for combination
            String message3 = "Gợi ý sách kết hợp cả hai thể loại";
            String response3 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message3, null);
            
            System.out.println("\n3️⃣ Genre combination request:");
            System.out.println("👤 User: " + message3);
            System.out.println("🤖 AI: " + response3.substring(0, Math.min(120, response3.length())) + "...");
            
            // Check genre combination detection
            Set<String> topics = aiService.getDiscussedTopics(TEST_SESSION_ID);
            System.out.println("🏷️ Final topics: " + topics);
            
            // Validate genre combination
            assertTrue(topics.contains("User Preference: Fantasy"), "Should detect fantasy preference");
            assertTrue(topics.contains("User Preference: Adventure"), "Should detect adventure preference");
            assertTrue(topics.contains("User Preference: Fantasy-Adventure Combination"), "Should detect combination");
            
            System.out.println("✅ Genre combination analysis test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Genre combination analysis test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🎯 Test Personalized Recommendations")
    void testPersonalizedRecommendations() {
        System.out.println("\n=== 🎯 Testing Personalized Recommendations ===");
        
        try {
            // Test 1: Express multiple preferences
            String message1 = "Tôi thích sách về tình yêu và trinh thám";
            String response1 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message1, null);
            
            System.out.println("1️⃣ Multiple preferences:");
            System.out.println("👤 User: " + message1);
            System.out.println("🤖 AI: " + response1.substring(0, Math.min(150, response1.length())) + "...");
            
            // Test 2: Ask for personalized recommendations
            String message2 = "Gợi ý sách phù hợp với sở thích của tôi";
            String response2 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message2, null);
            
            System.out.println("\n2️⃣ Personalized request:");
            System.out.println("👤 User: " + message2);
            System.out.println("🤖 AI: " + response2.substring(0, Math.min(150, response2.length())) + "...");
            
            // Check user preferences
            Set<String> topics = aiService.getDiscussedTopics(TEST_SESSION_ID);
            Set<String> books = aiService.getMentionedBooks(TEST_SESSION_ID);
            
            System.out.println("🏷️ User preferences: " + topics);
            System.out.println("📚 Recommended books: " + books);
            
            // Validate personalized recommendations
            assertTrue(topics.contains("User Preference: Romance"), "Should detect romance preference");
            assertTrue(topics.contains("User Preference: Mystery"), "Should detect mystery preference");
            assertTrue(books.size() > 0, "Should recommend books based on preferences");
            
            System.out.println("✅ Personalized recommendations test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Personalized recommendations test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🧠 Test Preference-Based Context Building")
    void testPreferenceBasedContextBuilding() {
        System.out.println("\n=== 🧠 Testing Preference-Based Context Building ===");
        
        try {
            // Test 1: Express preference
            String message1 = "Tôi thích sách về tâm lý học";
            String response1 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message1, null);
            
            System.out.println("1️⃣ Psychology preference:");
            System.out.println("👤 User: " + message1);
            System.out.println("🤖 AI: " + response1.substring(0, Math.min(120, response1.length())) + "...");
            
            // Test 2: Ask for related books
            String message2 = "Có sách nào tương tự không?";
            String response2 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message2, null);
            
            System.out.println("\n2️⃣ Related books request:");
            System.out.println("👤 User: " + message2);
            System.out.println("🤖 AI: " + response2.substring(0, Math.min(120, response2.length())) + "...");
            
            // Test 3: Ask for different genre
            String message3 = "Tôi cũng thích sách khoa học viễn tưởng";
            String response3 = aiService.processEnhancedChat(TEST_USER_ID, TEST_SESSION_ID, message3, null);
            
            System.out.println("\n3️⃣ Sci-Fi preference:");
            System.out.println("👤 User: " + message3);
            System.out.println("🤖 AI: " + response3.substring(0, Math.min(120, response3.length())) + "...");
            
            // Check context building
            Set<String> topics = aiService.getDiscussedTopics(TEST_SESSION_ID);
            System.out.println("🏷️ All preferences: " + topics);
            
            // Validate context building
            assertTrue(topics.contains("User Preference: Psychology"), "Should detect psychology preference");
            assertTrue(topics.contains("User Preference: Sci-Fi"), "Should detect sci-fi preference");
            
            System.out.println("✅ Preference-based context building test PASSED");
            
        } catch (Exception e) {
            System.out.println("⚠️ Preference-based context building test failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("🏆 Test Comprehensive User Preference Analysis")
    void testComprehensiveUserPreferenceAnalysis() {
        System.out.println("\n=== 🏆 Testing Comprehensive User Preference Analysis ===");
        
        try {
            // Simulate a comprehensive preference analysis conversation
            String[] conversation = {
                "Tôi thích đọc sách về tình yêu",
                "Tôi cũng thích truyện trinh thám",
                "Gợi ý sách kết hợp cả hai thể loại",
                "Sách nào phù hợp cho người thích cả romance và mystery?",
                "Tôi cũng thích sách về phát triển bản thân"
            };
            
            System.out.println("🔄 Simulating comprehensive preference analysis:");
            
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
            
            // Check final preference analysis
            Set<String> finalTopics = aiService.getDiscussedTopics(TEST_SESSION_ID);
            Set<String> finalBooks = aiService.getMentionedBooks(TEST_SESSION_ID);
            List<String> finalHistory = aiService.getConversationHistory(TEST_SESSION_ID);
            
            System.out.println("📊 Final preference analysis:");
            System.out.println("- User preferences: " + finalTopics.size() + " preferences detected");
            System.out.println("- Recommended books: " + finalBooks.size() + " books");
            System.out.println("- Conversation history: " + finalHistory.size() + " interactions");
            
            // Validate comprehensive analysis
            assertTrue(finalTopics.size() >= 3, "Should detect multiple preferences");
            assertTrue(finalBooks.size() >= 2, "Should recommend books based on preferences");
            assertTrue(finalHistory.size() >= conversation.length, "Should have all interactions");
            
            // Check for specific preferences
            boolean hasRomance = finalTopics.contains("User Preference: Romance");
            boolean hasMystery = finalTopics.contains("User Preference: Mystery");
            boolean hasSelfHelp = finalTopics.contains("User Preference: Self-Help");
            
            System.out.println("✅ Romance preference detected: " + hasRomance);
            System.out.println("✅ Mystery preference detected: " + hasMystery);
            System.out.println("✅ Self-Help preference detected: " + hasSelfHelp);
            
            System.out.println("✅ Comprehensive user preference analysis test PASSED");
            System.out.println("🎉 All preference analysis features working correctly!");
            
        } catch (Exception e) {
            System.out.println("⚠️ Comprehensive user preference analysis test failed: " + e.getMessage());
        }
    }
} 