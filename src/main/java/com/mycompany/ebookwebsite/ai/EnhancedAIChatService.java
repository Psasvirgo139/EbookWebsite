package com.mycompany.ebookwebsite.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.ebookwebsite.utils.Utils;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 🚀 Enhanced AI Chat Service
 * 
 * Integrates all advanced AI technologies:
 * - Session Context Memory: Long-term memory and context persistence
 * - Online Training & Vector Reuse: Continuous learning and optimization
 * - Advanced Book Link Coordination: Cross-book references and series detection
 * - Intelligent Response Generation: Context-aware responses
 * - Multi-modal Integration: Combines all AI services seamlessly
 */
public class EnhancedAIChatService {
    
    private static final Logger logger = LoggerFactory.getLogger(EnhancedAIChatService.class);
    
    private final SessionContextMemoryService memoryService;
    private final OnlineTrainingVectorService trainingService;
    private final AdvancedBookLinkService bookLinkService;
    private final ChatLanguageModel chatModel;
    private final EnhancedAssistant enhancedAssistant;
    private final Map<String, ChatMemory> sessionMemories = new ConcurrentHashMap<>();
    
    public EnhancedAIChatService() {
        try {
            // Initialize OpenAI chat model
            String apiKey = Utils.getEnv("OPENAI_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                logger.warn("OPENAI_API_KEY not set, using dummy key");
                apiKey = "dummy-key-for-initialization";
            }
            
            this.chatModel = OpenAiChatModel.builder()
                    .apiKey(apiKey)
                    .modelName("gpt-3.5-turbo")
                    .temperature(0.7)
                    .build();
            
            // Initialize all AI services
            this.memoryService = new SessionContextMemoryService();
            this.trainingService = new OnlineTrainingVectorService();
            this.bookLinkService = new AdvancedBookLinkService();
            
            // Initialize enhanced assistant
            this.enhancedAssistant = AiServices.builder(EnhancedAssistant.class)
                    .chatLanguageModel(chatModel)
                    .build();
            
            logger.info("✅ EnhancedAIChatService initialized with all AI technologies!");
            
        } catch (Exception e) {
            logger.error("❌ Failed to initialize EnhancedAIChatService: " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize enhanced AI chat service", e);
        }
    }
    
    /**
     * 💬 Process chat with all enhanced features
     */
    public String processEnhancedChat(int userId, String sessionId, String message, String context) {
        try {
            logger.info("🚀 Processing enhanced chat for user {}: {}", userId, message);
            
            // Get session memory
            ChatMemory sessionMemory = getOrCreateSessionMemory(sessionId);
            
            // Process with memory service
            String memoryResponse = memoryService.processChatWithMemory(userId, sessionId, message, context);
            
            // Process with training service
            String trainingResponse = trainingService.processChatWithLearning(userId, message, context, true);
            
            // Analyze message for book-related queries
            String bookAnalysis = analyzeBookRelatedQueries(message);
            
            // Combine all responses
            String enhancedResponse = enhancedAssistant.combineResponses(
                memoryResponse, trainingResponse, bookAnalysis, message, context
            );
            
            // Store enhanced interaction
            storeEnhancedInteraction(userId, message, enhancedResponse, context);
            
            logger.info("✅ Enhanced chat processed: {} chars", enhancedResponse.length());
            return enhancedResponse;
            
        } catch (Exception e) {
            logger.error("❌ Enhanced chat processing failed: " + e.getMessage(), e);
            return "Xin lỗi, có lỗi xảy ra với hệ thống AI nâng cao. Vui lòng thử lại sau! 😊";
        }
    }
    
    /**
     * 📚 Process book-specific queries
     */
    public String processBookQuery(int userId, String message, int bookId) {
        try {
            logger.info("📚 Processing book query for user {}: {}", userId, message);
            
            // Get cross-book references
            String crossReferences = bookLinkService.findCrossBookReferences(bookId);
            
            // Detect series
            String seriesInfo = bookLinkService.detectBookSeries(bookId);
            
            // Process with enhanced assistant
            String response = enhancedAssistant.processBookQuery(message, crossReferences, seriesInfo, bookId);
            
            // Store book interaction
            storeBookInteraction(userId, message, response, bookId);
            
            return response;
            
        } catch (Exception e) {
            logger.error("❌ Book query processing failed: " + e.getMessage(), e);
            return "Không thể xử lý truy vấn sách. 😔";
        }
    }
    
    /**
     * 👥 Process author network queries
     */
    public String processAuthorQuery(int userId, String message, int authorId) {
        try {
            logger.info("👥 Processing author query for user {}: {}", userId, message);
            
            // Analyze author network
            String authorNetwork = bookLinkService.analyzeAuthorNetwork(authorId);
            
            // Process with enhanced assistant
            String response = enhancedAssistant.processAuthorQuery(message, authorNetwork, authorId);
            
            return response;
            
        } catch (Exception e) {
            logger.error("❌ Author query processing failed: " + e.getMessage(), e);
            return "Không thể xử lý truy vấn tác giả. 😔";
        }
    }
    
    /**
     * 🔍 Analyze message for book-related queries
     */
    private String analyzeBookRelatedQueries(String message) {
        try {
            String lowerMessage = message.toLowerCase();
            
            if (lowerMessage.contains("series") || lowerMessage.contains("tập")) {
                return "📚 Phát hiện truy vấn về series sách";
            } else if (lowerMessage.contains("tác giả") || lowerMessage.contains("author")) {
                return "👥 Phát hiện truy vấn về tác giả";
            } else if (lowerMessage.contains("tham chiếu") || lowerMessage.contains("reference")) {
                return "🔗 Phát hiện truy vấn về tham chiếu chéo";
            } else if (lowerMessage.contains("tương tự") || lowerMessage.contains("similar")) {
                return "🎯 Phát hiện truy vấn về sách tương tự";
            }
            
            return "";
            
        } catch (Exception e) {
            logger.error("❌ Failed to analyze book queries: " + e.getMessage(), e);
            return "";
        }
    }
    
    /**
     * 🧠 Get or create session memory
     */
    private ChatMemory getOrCreateSessionMemory(String sessionId) {
        return sessionMemories.computeIfAbsent(sessionId, k -> {
            logger.info("🆕 Creating new enhanced session memory: {}", sessionId);
            return MessageWindowChatMemory.withMaxMessages(25); // Increased for enhanced context
        });
    }
    
    /**
     * 💾 Store enhanced interaction
     */
    private void storeEnhancedInteraction(int userId, String userMessage, String aiResponse, String context) {
        try {
            // Store in memory service
            memoryService.processChatWithMemory(userId, "enhanced_session", userMessage, context);
            
            // Store in training service
            trainingService.processChatWithLearning(userId, userMessage, context, true);
            
            logger.info("💾 Stored enhanced interaction for user {}", userId);
            
        } catch (Exception e) {
            logger.error("❌ Failed to store enhanced interaction: " + e.getMessage(), e);
        }
    }
    
    /**
     * 📚 Store book interaction
     */
    private void storeBookInteraction(int userId, String userMessage, String aiResponse, int bookId) {
        try {
            String bookContext = "Book ID: " + bookId;
            
            // Store in all services
            memoryService.processChatWithMemory(userId, "book_session", userMessage, bookContext);
            trainingService.processChatWithLearning(userId, userMessage, bookContext, true);
            
            logger.info("📚 Stored book interaction for user {} and book {}", userId, bookId);
            
        } catch (Exception e) {
            logger.error("❌ Failed to store book interaction: " + e.getMessage(), e);
        }
    }
    
    /**
     * 🧹 Clear session memory
     */
    public void clearSessionMemory(String sessionId) {
        sessionMemories.remove(sessionId);
        logger.info("🧹 Cleared enhanced session memory: {}", sessionId);
    }
    
    /**
     * 📊 Get enhanced service statistics
     */
    public String getEnhancedStats() {
        return String.format(
            "Enhanced AI Chat Service Stats:\n" +
            "- Memory Service: %s\n" +
            "- Training Service: %s\n" +
            "- Book Link Service: %s\n" +
            "- Active Sessions: %d\n" +
            "- Chat Model: %s",
            memoryService.getMemoryStats(),
            trainingService.getLearningStats(),
            bookLinkService.getBookLinkStats(),
            sessionMemories.size(),
            chatModel.getClass().getSimpleName()
        );
    }
    
    /**
     * 🧹 Shutdown all services
     */
    public void shutdown() {
        try {
            trainingService.shutdown();
            logger.info("🧹 Enhanced AI Chat Service shutdown completed");
        } catch (Exception e) {
            logger.error("❌ Enhanced service shutdown failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * 🤖 Enhanced Assistant Interface
     */
    public interface EnhancedAssistant {
        
        @SystemMessage("Bạn là AI trợ lý nâng cao với khả năng tích hợp nhiều công nghệ AI. " +
                      "Kết hợp thông tin từ bộ nhớ, học tập liên tục, và phân tích sách. " +
                      "Cung cấp câu trả lời thông minh và toàn diện.")
        String combineResponses(@V("memoryResponse") String memoryResponse,
                              @V("trainingResponse") String trainingResponse,
                              @V("bookAnalysis") String bookAnalysis,
                              @UserMessage String userMessage,
                              @V("context") String context);
        
        @SystemMessage("Xử lý truy vấn sách với thông tin series và tham chiếu chéo. " +
                      "Cung cấp thông tin chi tiết về sách và các mối quan hệ.")
        String processBookQuery(@UserMessage String userMessage,
                              @V("crossReferences") String crossReferences,
                              @V("seriesInfo") String seriesInfo,
                              @V("bookId") int bookId);
        
        @SystemMessage("Xử lý truy vấn tác giả với phân tích mạng lưới. " +
                      "Cung cấp thông tin về tác giả và các mối quan hệ hợp tác.")
        String processAuthorQuery(@UserMessage String userMessage,
                                @V("authorNetwork") String authorNetwork,
                                @V("authorId") int authorId);
    }
} 