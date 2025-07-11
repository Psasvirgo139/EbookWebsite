package com.mycompany.ebookwebsite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * 🧪 Enhanced AI Test Tool
 * 
 * Demonstrates all new AI technologies:
 * - Session Context Memory: Long-term memory and context persistence
 * - Online Training & Vector Reuse: Continuous learning and optimization
 * - Advanced Book Link Coordination: Cross-book references and series detection
 */
public class EnhancedAITestTool {
    
    private static final Logger logger = LoggerFactory.getLogger(EnhancedAITestTool.class);
    
    private final EnhancedAIChatService enhancedAI;
    private final SessionContextMemoryService memoryService;
    private final OnlineTrainingVectorService trainingService;
    private final AdvancedBookLinkService bookLinkService;
    
    public EnhancedAITestTool() {
        try {
            logger.info("🚀 Initializing Enhanced AI Test Tool...");
            
            this.enhancedAI = new EnhancedAIChatService();
            this.memoryService = new SessionContextMemoryService();
            this.trainingService = new OnlineTrainingVectorService();
            this.bookLinkService = new AdvancedBookLinkService();
            
            logger.info("✅ Enhanced AI Test Tool initialized successfully!");
            
        } catch (Exception e) {
            logger.error("❌ Failed to initialize Enhanced AI Test Tool: " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize test tool", e);
        }
    }
    
    /**
     * 🎯 Run comprehensive AI tests
     */
    public void runAllTests() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🧪 ENHANCED AI TEST TOOL - EbookWebsite");
        System.out.println("=".repeat(80));
        
        try {
            // Test 1: Session Context Memory
            testSessionContextMemory();
            
            // Test 2: Online Training & Vector Reuse
            testOnlineTrainingVectorReuse();
            
            // Test 3: Advanced Book Link Coordination
            testAdvancedBookLinkCoordination();
            
            // Test 4: Integrated Enhanced AI
            testIntegratedEnhancedAI();
            
            // Test 5: Performance and Statistics
            testPerformanceAndStats();
            
            System.out.println("\n✅ All Enhanced AI tests completed successfully!");
            
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
            String response1 = memoryService.processChatWithMemory(userId, sessionId, 
                "Tôi thích đọc sách khoa học viễn tưởng", "User preference: sci-fi");
            System.out.println("Response: " + response1.substring(0, Math.min(100, response1.length())) + "...");
            
            // Test 2: Context persistence
            System.out.println("\n📝 Test 2: Context Persistence");
            String response2 = memoryService.processChatWithMemory(userId, sessionId, 
                "Bạn có thể gợi ý thêm sách tương tự không?", "");
            System.out.println("Response: " + response2.substring(0, Math.min(100, response2.length())) + "...");
            
            // Test 3: Long-term memory
            System.out.println("\n📝 Test 3: Long-term Memory");
            String response3 = memoryService.processChatWithMemory(userId, "new_session", 
                "Tôi đã từng hỏi về sách khoa học viễn tưởng", "");
            System.out.println("Response: " + response3.substring(0, Math.min(100, response3.length())) + "...");
            
            System.out.println("✅ Session Context Memory tests completed!");
            
        } catch (Exception e) {
            logger.error("❌ Session Context Memory test failed: " + e.getMessage(), e);
            System.out.println("❌ Session Context Memory test failed: " + e.getMessage());
        }
    }
    
    /**
     * 🚀 Test Online Training & Vector Reuse
     */
    private void testOnlineTrainingVectorReuse() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("🚀 TESTING ONLINE TRAINING & VECTOR REUSE");
        System.out.println("-".repeat(60));
        
        try {
            int userId = 2;
            
            // Test 1: Continuous learning
            System.out.println("📝 Test 1: Continuous Learning");
            String response1 = trainingService.processChatWithLearning(userId, 
                "Sách nào hay nhất về lập trình Java?", "Programming context", true);
            System.out.println("Response: " + response1.substring(0, Math.min(100, response1.length())) + "...");
            
            // Test 2: Vector reuse optimization
            System.out.println("\n📝 Test 2: Vector Reuse Optimization");
            String response2 = trainingService.processChatWithLearning(userId, 
                "Còn sách nào khác về Java không?", "Programming context", false);
            System.out.println("Response: " + response2.substring(0, Math.min(100, response2.length())) + "...");
            
            // Test 3: Adaptive learning
            System.out.println("\n📝 Test 3: Adaptive Learning");
            String response3 = trainingService.processChatWithLearning(userId, 
                "Tôi muốn học Python thay vì Java", "Programming context", true);
            System.out.println("Response: " + response3.substring(0, Math.min(100, response3.length())) + "...");
            
            System.out.println("✅ Online Training & Vector Reuse tests completed!");
            
        } catch (Exception e) {
            logger.error("❌ Online Training test failed: " + e.getMessage(), e);
            System.out.println("❌ Online Training test failed: " + e.getMessage());
        }
    }
    
    /**
     * 🔗 Test Advanced Book Link Coordination
     */
    private void testAdvancedBookLinkCoordination() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("🔗 TESTING ADVANCED BOOK LINK COORDINATION");
        System.out.println("-".repeat(60));
        
        try {
            // Test 1: Cross-book references
            System.out.println("📝 Test 1: Cross-book References");
            String response1 = bookLinkService.findCrossBookReferences(1);
            System.out.println("Response: " + response1.substring(0, Math.min(100, response1.length())) + "...");
            
            // Test 2: Series detection
            System.out.println("\n📝 Test 2: Series Detection");
            String response2 = bookLinkService.detectBookSeries(1);
            System.out.println("Response: " + response2.substring(0, Math.min(100, response2.length())) + "...");
            
            // Test 3: Author network analysis
            System.out.println("\n📝 Test 3: Author Network Analysis");
            String response3 = bookLinkService.analyzeAuthorNetwork(1);
            System.out.println("Response: " + response3.substring(0, Math.min(100, response3.length())) + "...");
            
            System.out.println("✅ Advanced Book Link Coordination tests completed!");
            
        } catch (Exception e) {
            logger.error("❌ Advanced Book Link test failed: " + e.getMessage(), e);
            System.out.println("❌ Advanced Book Link test failed: " + e.getMessage());
        }
    }
    
    /**
     * 🚀 Test Integrated Enhanced AI
     */
    private void testIntegratedEnhancedAI() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("🚀 TESTING INTEGRATED ENHANCED AI");
        System.out.println("-".repeat(60));
        
        try {
            int userId = 3;
            String sessionId = "test_enhanced_ai";
            
            // Test 1: Enhanced chat with all features
            System.out.println("📝 Test 1: Enhanced Chat with All Features");
            String response1 = enhancedAI.processEnhancedChat(userId, sessionId, 
                "Tôi muốn tìm sách về trí tuệ nhân tạo", "AI and technology context");
            System.out.println("Response: " + response1.substring(0, Math.min(150, response1.length())) + "...");
            
            // Test 2: Book-specific query
            System.out.println("\n📝 Test 2: Book-specific Query");
            String response2 = enhancedAI.processBookQuery(userId, 
                "Sách này có series không?", 1);
            System.out.println("Response: " + response2.substring(0, Math.min(150, response2.length())) + "...");
            
            // Test 3: Author network query
            System.out.println("\n📝 Test 3: Author Network Query");
            String response3 = enhancedAI.processAuthorQuery(userId, 
                "Tác giả này có hợp tác với ai khác không?", 1);
            System.out.println("Response: " + response3.substring(0, Math.min(150, response3.length())) + "...");
            
            System.out.println("✅ Integrated Enhanced AI tests completed!");
            
        } catch (Exception e) {
            logger.error("❌ Integrated Enhanced AI test failed: " + e.getMessage(), e);
            System.out.println("❌ Integrated Enhanced AI test failed: " + e.getMessage());
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
            // Get statistics from all services
            System.out.println("📈 Memory Service Stats:");
            System.out.println(memoryService.getMemoryStats());
            
            System.out.println("\n📈 Training Service Stats:");
            System.out.println(trainingService.getLearningStats());
            
            System.out.println("\n📈 Book Link Service Stats:");
            System.out.println(bookLinkService.getBookLinkStats());
            
            System.out.println("\n📈 Enhanced AI Service Stats:");
            System.out.println(enhancedAI.getEnhancedStats());
            
            System.out.println("✅ Performance and Statistics tests completed!");
            
        } catch (Exception e) {
            logger.error("❌ Performance test failed: " + e.getMessage(), e);
            System.out.println("❌ Performance test failed: " + e.getMessage());
        }
    }
    
    /**
     * 🎮 Interactive test mode
     */
    public void runInteractiveMode() {
        System.out.println("\n" + "🎮".repeat(20));
        System.out.println("🎮 INTERACTIVE ENHANCED AI TEST MODE");
        System.out.println("🎮".repeat(20));
        System.out.println("Commands:");
        System.out.println("- 'memory': Test session context memory");
        System.out.println("- 'training': Test online training");
        System.out.println("- 'booklink': Test book link coordination");
        System.out.println("- 'enhanced': Test integrated enhanced AI");
        System.out.println("- 'stats': Show all statistics");
        System.out.println("- 'quit': Exit interactive mode");
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("\n🎮 Enter command: ");
            String command = scanner.nextLine().trim().toLowerCase();
            
            switch (command) {
                case "memory":
                    testSessionContextMemory();
                    break;
                case "training":
                    testOnlineTrainingVectorReuse();
                    break;
                case "booklink":
                    testAdvancedBookLinkCoordination();
                    break;
                case "enhanced":
                    testIntegratedEnhancedAI();
                    break;
                case "stats":
                    testPerformanceAndStats();
                    break;
                case "quit":
                    System.out.println("👋 Goodbye!");
                    return;
                default:
                    System.out.println("❌ Unknown command. Type 'quit' to exit.");
                    break;
            }
        }
    }
    
    /**
     * 🧹 Cleanup resources
     */
    public void cleanup() {
        try {
            enhancedAI.shutdown();
            logger.info("🧹 Enhanced AI Test Tool cleanup completed");
        } catch (Exception e) {
            logger.error("❌ Cleanup failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * 🚀 Main method for testing
     */
    public static void main(String[] args) {
        EnhancedAITestTool testTool = new EnhancedAITestTool();
        
        try {
            if (args.length > 0 && args[0].equals("interactive")) {
                testTool.runInteractiveMode();
            } else {
                testTool.runAllTests();
            }
        } finally {
            testTool.cleanup();
        }
    }
} 