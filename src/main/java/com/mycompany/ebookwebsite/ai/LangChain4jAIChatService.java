package com.mycompany.ebookwebsite.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.ebookwebsite.utils.Utils;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

/**
 * 🚀 LangChain4j-based AI Chat Service
 * 
 * Thay thế IntelligentAIChatService với LangChain4j AiServices
 * Features:
 * - AiServices với automatic RAG integration
 * - Chat memory management
 * - Query transformation
 * - Content retrieval
 * - Conversation enhancement
 */
public class LangChain4jAIChatService {
    
    private static final Logger logger = LoggerFactory.getLogger(LangChain4jAIChatService.class);
    
    private final Assistant assistant;
    private final ChatLanguageModel chatModel;
    
    public LangChain4jAIChatService() {
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
            
            // Initialize chat memory
            ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
            
            // Build AI service
            this.assistant = AiServices.builder(Assistant.class)
                    .chatLanguageModel(chatModel)
                    .chatMemory(chatMemory)
                    .build();
            
            logger.info("✅ LangChain4jAIChatService initialized successfully");
            
        } catch (Exception e) {
            logger.error("❌ Failed to initialize LangChain4jAIChatService: " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize AI chat service", e);
        }
    }
    
    /**
     * 💬 Process chat message
     */
    public String processChat(int userId, String sessionId, String message, String context) {
        try {
            logger.info("💬 Processing chat for user {}: {}", userId, message);
            
            String response;
            if (context != null && !context.isEmpty()) {
                response = assistant.chatWithContext(message, context);
            } else {
                response = assistant.chat(message);
            }
            
            logger.info("✅ Chat response generated: {} chars", response.length());
            return response;
            
        } catch (Exception e) {
            logger.error("❌ Chat processing failed: " + e.getMessage(), e);
            return "Xin lỗi, có lỗi xảy ra. Vui lòng thử lại sau! 😊";
        }
    }
    
    /**
     * 📚 Chat about specific book
     */
    public String chatAboutBook(String message, String bookId) {
        try {
            logger.info("📚 Chat about book {}: {}", bookId, message);
            return assistant.chatAboutBook(bookId, message);
        } catch (Exception e) {
            logger.error("❌ Book chat failed: " + e.getMessage(), e);
            return "Không thể tìm thấy thông tin về sách này. 😔";
        }
    }
    
    /**
     * 🎯 Get recommendations
     */
    public String getRecommendations(String message) {
        try {
            logger.info("🎯 Getting recommendations: {}", message);
            return assistant.getRecommendations(message);
        } catch (Exception e) {
            logger.error("❌ Recommendations failed: " + e.getMessage(), e);
            return "Không thể tạo gợi ý lúc này. 😔";
        }
    }
    
    /**
     * 📖 Analyze book content
     */
    public String analyzeBookContent(String bookContent) {
        try {
            logger.info("📖 Analyzing book content: {} chars", bookContent.length());
            return assistant.analyzeBookContent(bookContent);
        } catch (Exception e) {
            logger.error("❌ Book analysis failed: " + e.getMessage(), e);
            return "Không thể phân tích nội dung sách. 😔";
        }
    }
    
    /**
     * 🔍 Search books
     */
    public String searchBooks(String searchQuery) {
        try {
            logger.info("🔍 Searching books: {}", searchQuery);
            return assistant.searchBooks(searchQuery);
        } catch (Exception e) {
            logger.error("❌ Book search failed: " + e.getMessage(), e);
            return "Không thể tìm kiếm sách lúc này. 😔";
        }
    }
    
    /**
     * 📊 Get service statistics
     */
    public String getServiceStats() {
        return String.format("LangChain4j AI Chat Service - ChatModel: %s", 
            chatModel.getClass().getSimpleName());
    }
} 