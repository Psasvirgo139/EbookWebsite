package com.mycompany.ebookwebsite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * 🧪 Simple AI Test Tool
 * 
 * Tests basic AI features:
 * - Session Context Memory: Basic memory management
 * - Online Training: Simple learning capabilities
 * - Book Link Coordination: Basic book relationships
 */
public class SimpleAITestTool {
    
    private static final Logger logger = LoggerFactory.getLogger(SimpleAITestTool.class);
    
    private final SimpleEnhancedAIChatService simpleAI;
    
    public SimpleAITestTool() {
        try {
            logger.info("🚀 Initializing Simple AI Test Tool...");
            
            this.simpleAI = new SimpleEnhancedAIChatService();
            
            logger.info("✅ Simple AI Test Tool initialized successfully!");
            
        } catch (Exception e) {
            logger.error("❌ Failed to initialize Simple AI Test Tool: " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize test tool", e);
        }
    }
    
    /**
     * 🎯 Run comprehensive AI tests
     */
    public void runAllTests() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🧪 SIMPLE AI TEST TOOL - EbookWebsite");
        System.out.println("=".repeat(80));
        
        try {
            // Test 1: Session Context Memory
            testSessionContextMemory();
            
            // Test 2: Basic Chat Features
            testBasicChatFeatures();
            
            // Test 3: Book-specific Queries
            testBookSpecificQueries();
            
            // Test 4: Author Network Queries
            testAuthorNetworkQueries();
            
            // Test 5: Performance and Statistics
            testPerformanceAndStats();
            
            System.out.println("\n✅ All Simple AI tests completed successfully!");
            
        } catch (Exception e) {
            logger.error("❌ Test execution failed: " + e.getMessage(), e);
            System.out.println("❌ Test execution failed: " + e.getMessage());
        }
    }
    
    /**
     * 🧠 Test Session Context Memory
     */
    private void testSessionContextMemory() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("🧠 TESTING SESSION CONTEXT MEMORY");
        System.out.println("-".repeat(60));
        
        try {
            int userId = 1;
            String sessionId = "test_session_memory";
            
            // Test 1: Basic memory chat
            System.out.println("📝 Test 1: Basic Memory Chat");
            String response1 = simpleAI.processEnhancedChat(userId, sessionId, 
                "Tôi thích đọc sách khoa học viễn tưởng", "User preference: sci-fi");
            System.out.println("Response: " + response1.substring(0, Math.min(100, response1.length())) + "...");
            
            // Test 2: Context persistence
            System.out.println("\n📝 Test 2: Context Persistence");
            String response2 = simpleAI.processEnhancedChat(userId, sessionId, 
                "Bạn có thể gợi ý thêm sách tương tự không?", "");
            System.out.println("Response: " + response2.substring(0, Math.min(100, response2.length())) + "...");
            
            // Test 3: New session memory
            System.out.println("\n📝 Test 3: New Session Memory");
            String response3 = simpleAI.processEnhancedChat(userId, "new_session", 
                "Tôi đã từng hỏi về sách khoa học viễn tưởng", "");
            System.out.println("Response: " + response3.substring(0, Math.min(100, response3.length())) + "...");
            
            System.out.println("✅ Session Context Memory tests completed!");
            
        } catch (Exception e) {
            logger.error("❌ Session Context Memory test failed: " + e.getMessage(), e);
            System.out.println("❌ Session Context Memory test failed: " + e.getMessage());
        }
    }
    
    /**
     * 💬 Test Basic Chat Features
     */
    private void testBasicChatFeatures() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("💬 TESTING BASIC CHAT FEATURES");
        System.out.println("-".repeat(60));
        
        try {
            int userId = 2;
            String sessionId = "test_basic_chat";
            
            // Test 1: General chat
            System.out.println("📝 Test 1: General Chat");
            String response1 = simpleAI.processEnhancedChat(userId, sessionId, 
                "Xin chào! Bạn có thể giúp tôi tìm sách không?", "General greeting");
            System.out.println("Response: " + response1.substring(0, Math.min(100, response1.length())) + "...");
            
            // Test 2: Book recommendation
            System.out.println("\n📝 Test 2: Book Recommendation");
            String response2 = simpleAI.processEnhancedChat(userId, sessionId, 
                "Tôi muốn tìm sách về lập trình Java", "Programming context");
            System.out.println("Response: " + response2.substring(0, Math.min(100, response2.length())) + "...");
            
            // Test 3: Follow-up question
            System.out.println("\n📝 Test 3: Follow-up Question");
            String response3 = simpleAI.processEnhancedChat(userId, sessionId, 
                "Còn sách nào khác về Python không?", "Programming context");
            System.out.println("Response: " + response3.substring(0, Math.min(100, response3.length())) + "...");
            
            System.out.println("✅ Basic Chat Features tests completed!");
            
        } catch (Exception e) {
            logger.error("❌ Basic Chat Features test failed: " + e.getMessage(), e);
            System.out.println("❌ Basic Chat Features test failed: " + e.getMessage());
        }
    }
    
    /**
     * 📚 Test Book-specific Queries
     */
    private void testBookSpecificQueries() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("📚 TESTING BOOK-SPECIFIC QUERIES");
        System.out.println("-".repeat(60));
        
        try {
            int userId = 3;
            
            // Test 1: Book information query
            System.out.println("📝 Test 1: Book Information Query");
            String response1 = simpleAI.processBookQuery(userId, 
                "Sách này có nội dung gì?", 1);
            System.out.println("Response: " + response1.substring(0, Math.min(100, response1.length())) + "...");
            
            // Test 2: Book series query
            System.out.println("\n📝 Test 2: Book Series Query");
            String response2 = simpleAI.processBookQuery(userId, 
                "Sách này có series không?", 1);
            System.out.println("Response: " + response2.substring(0, Math.min(100, response2.length())) + "...");
            
            // Test 3: Book recommendation query
            System.out.println("\n📝 Test 3: Book Recommendation Query");
            String response3 = simpleAI.processBookQuery(userId, 
                "Sách nào tương tự với sách này?", 1);
            System.out.println("Response: " + response3.substring(0, Math.min(100, response3.length())) + "...");
            
            System.out.println("✅ Book-specific Queries tests completed!");
            
        } catch (Exception e) {
            logger.error("❌ Book-specific Queries test failed: " + e.getMessage(), e);
            System.out.println("❌ Book-specific Queries test failed: " + e.getMessage());
        }
    }
    
    /**
     * 👥 Test Author Network Queries
     */
    private void testAuthorNetworkQueries() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("👥 TESTING AUTHOR NETWORK QUERIES");
        System.out.println("-".repeat(60));
        
        try {
            int userId = 4;
            
            // Test 1: Author information query
            System.out.println("📝 Test 1: Author Information Query");
            String response1 = simpleAI.processAuthorQuery(userId, 
                "Tác giả này có viết sách gì?", 1);
            System.out.println("Response: " + response1.substring(0, Math.min(100, response1.length())) + "...");
            
            // Test 2: Author collaboration query
            System.out.println("\n📝 Test 2: Author Collaboration Query");
            String response2 = simpleAI.processAuthorQuery(userId, 
                "Tác giả này có hợp tác với ai khác không?", 1);
            System.out.println("Response: " + response2.substring(0, Math.min(100, response2.length())) + "...");
            
            // Test 3: Author style query
            System.out.println("\n📝 Test 3: Author Style Query");
            String response3 = simpleAI.processAuthorQuery(userId, 
                "Phong cách viết của tác giả này như thế nào?", 1);
            System.out.println("Response: " + response3.substring(0, Math.min(100, response3.length())) + "...");
            
            System.out.println("✅ Author Network Queries tests completed!");
            
        } catch (Exception e) {
            logger.error("❌ Author Network Queries test failed: " + e.getMessage(), e);
            System.out.println("❌ Author Network Queries test failed: " + e.getMessage());
        }
    }
    
    /**
     * 📊 Test Performance and Statistics
     */
    private void testPerformanceAndStats() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("📊 TESTING PERFORMANCE AND STATISTICS");
        System.out.println("-".repeat(60));
        
        try {
            // Get service statistics
            System.out.println("📝 Test 1: Service Statistics");
            String stats = simpleAI.getSimpleStats();
            System.out.println("Stats: " + stats);
            
            // Test performance
            System.out.println("\n📝 Test 2: Performance Test");
            long startTime = System.currentTimeMillis();
            String response = simpleAI.processEnhancedChat(5, "perf_test", 
                "Test performance", "Performance context");
            long endTime = System.currentTimeMillis();
            
            System.out.println("Response time: " + (endTime - startTime) + "ms");
            System.out.println("Response length: " + response.length() + " characters");
            
            System.out.println("✅ Performance and Statistics tests completed!");
            
        } catch (Exception e) {
            logger.error("❌ Performance and Statistics test failed: " + e.getMessage(), e);
            System.out.println("❌ Performance and Statistics test failed: " + e.getMessage());
        }
    }
    
    /**
     * 🎮 Run interactive mode
     */
    public void runInteractiveMode() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🎮 INTERACTIVE MODE - Simple AI Test Tool");
        System.out.println("=".repeat(80));
        System.out.println("Type 'quit' to exit");
        System.out.println("Type 'stats' to see service statistics");
        System.out.println("Type 'clear' to clear session memory");
        System.out.println();
        
        Scanner scanner = new Scanner(System.in);
        int userId = 999;
        String sessionId = "interactive_session";
        
        while (true) {
            System.out.print("You: ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("quit")) {
                break;
            } else if (input.equalsIgnoreCase("stats")) {
                System.out.println("AI: " + simpleAI.getSimpleStats());
            } else if (input.equalsIgnoreCase("clear")) {
                simpleAI.clearSessionMemory(sessionId);
                System.out.println("AI: Session memory cleared!");
            } else if (!input.isEmpty()) {
                try {
                    String response = simpleAI.processEnhancedChat(userId, sessionId, input, "Interactive mode");
                    System.out.println("AI: " + response);
                } catch (Exception e) {
                    System.out.println("AI: Sorry, there was an error: " + e.getMessage());
                }
            }
        }
        
        scanner.close();
        System.out.println("👋 Interactive mode ended!");
    }
    
    /**
     * 🧹 Cleanup resources
     */
    public void cleanup() {
        try {
            // Clear all session memories
            simpleAI.clearSessionMemory("test_session_memory");
            simpleAI.clearSessionMemory("test_basic_chat");
            simpleAI.clearSessionMemory("new_session");
            simpleAI.clearSessionMemory("perf_test");
            simpleAI.clearSessionMemory("interactive_session");
            
            logger.info("🧹 Simple AI Test Tool cleanup completed");
            
        } catch (Exception e) {
            logger.error("❌ Cleanup failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * 🚀 Main method for testing
     */
    public static void main(String[] args) {
        try {
            SimpleAITestTool testTool = new SimpleAITestTool();
            
            if (args.length > 0 && args[0].equals("interactive")) {
                testTool.runInteractiveMode();
            } else {
                testTool.runAllTests();
            }
            
            testTool.cleanup();
            
        } catch (Exception e) {
            System.err.println("❌ Test tool failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 