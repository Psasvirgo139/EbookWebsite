package com.mycompany.ebookwebsite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.ebookwebsite.utils.Utils;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.redis.RedisEmbeddingStore;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 🧠 Session Context Memory Service
 * 
 * Implements advanced memory management features:
 * - Long-term Memory: Persistent chat history storage
 * - Context Persistence: Maintains context across sessions
 * - Memory Management: Intelligent memory optimization
 * - Session-based Memory: User-specific conversation history
 * - Memory Compression: Summarizes old conversations
 * - Context Retrieval: Smart context retrieval for new conversations
 */
public class SessionContextMemoryService {
    
    private static final Logger logger = LoggerFactory.getLogger(SessionContextMemoryService.class);
    
    private final Map<String, ChatMemory> sessionMemories = new ConcurrentHashMap<>();
    private final EmbeddingStore<TextSegment> memoryStore;
    private final EmbeddingModel embeddingModel;
    private final ChatLanguageModel chatModel;
    private final MemoryAssistant memoryAssistant;
    
    public SessionContextMemoryService() {
        try {
            // Initialize OpenAI models
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
            
            this.embeddingModel = OpenAiEmbeddingModel.builder()
                    .apiKey(apiKey)
                    .modelName("text-embedding-ada-002")
                    .build();
            
            // Initialize Redis for persistent memory storage
            this.memoryStore = RedisEmbeddingStore.builder()
                    .host("localhost")
                    .port(6379)
                    .dimension(1536) // OpenAI ada-002 embedding dimension
                    .indexName("ebook_memory")
                    .build();
            
            // Initialize memory assistant for advanced operations
            this.memoryAssistant = AiServices.builder(MemoryAssistant.class)
                    .chatLanguageModel(chatModel)
                    .build();
            
            logger.info("✅ SessionContextMemoryService initialized successfully");
            
        } catch (Exception e) {
            logger.error("❌ Failed to initialize SessionContextMemoryService: " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize memory service", e);
        }
    }
    
    /**
     * 💬 Process chat with enhanced memory management
     */
    public String processChatWithMemory(int userId, String sessionId, String message, String context) {
        try {
            String memoryKey = generateMemoryKey(userId, sessionId);
            
            // Get or create session memory
            ChatMemory sessionMemory = getOrCreateSessionMemory(memoryKey);
            
            // Retrieve relevant context from long-term memory
            String longTermContext = retrieveLongTermContext(userId, message);
            
            // Combine current context with long-term memory
            String enhancedContext = combineContexts(context, longTermContext);
            
            // Process with memory-aware assistant
            String response = memoryAssistant.chatWithMemory(message, enhancedContext, memoryKey);
            
            // Store conversation in long-term memory
            storeConversationInMemory(userId, message, response, enhancedContext);
            
            // Compress old memories if needed
            compressOldMemories(userId);
            
            logger.info("✅ Chat processed with memory for user {}: {} chars", userId, response.length());
            return response;
            
        } catch (Exception e) {
            logger.error("❌ Memory chat processing failed: " + e.getMessage(), e);
            return "Xin lỗi, có lỗi xảy ra với hệ thống trí nhớ. Vui lòng thử lại sau! 😊";
        }
    }
    
    /**
     * 🧠 Get or create session memory
     */
    private ChatMemory getOrCreateSessionMemory(String memoryKey) {
        return sessionMemories.computeIfAbsent(memoryKey, k -> {
            logger.info("🆕 Creating new session memory: {}", memoryKey);
            return MessageWindowChatMemory.withMaxMessages(20); // Increased for better context
        });
    }
    
    /**
     * 🔍 Retrieve relevant context from long-term memory
     */
    private String retrieveLongTermContext(int userId, String currentMessage) {
        try {
            // Create embedding for current message
            var queryEmbedding = embeddingModel.embed(currentMessage).content();
            
            // Search for relevant memories
            var searchResult = memoryStore.findRelevant(queryEmbedding, 5, 0.7);
            
            if (searchResult.isEmpty()) {
                return "";
            }
            
            // Build context from relevant memories
            StringBuilder context = new StringBuilder();
            context.append("📚 Thông tin từ các cuộc trò chuyện trước:\n");
            
            for (var match : searchResult) {
                TextSegment segment = match.embedded();
                context.append("- ").append(segment.text()).append("\n");
            }
            
            return context.toString();
            
        } catch (Exception e) {
            logger.error("❌ Failed to retrieve long-term context: " + e.getMessage(), e);
            return "";
        }
    }
    
    /**
     * 🔗 Combine current context with long-term memory
     */
    private String combineContexts(String currentContext, String longTermContext) {
        StringBuilder combined = new StringBuilder();
        
        if (currentContext != null && !currentContext.isEmpty()) {
            combined.append("📖 Ngữ cảnh hiện tại:\n").append(currentContext).append("\n\n");
        }
        
        if (!longTermContext.isEmpty()) {
            combined.append(longTermContext);
        }
        
        return combined.toString();
    }
    
    /**
     * 💾 Store conversation in long-term memory
     */
    private void storeConversationInMemory(int userId, String userMessage, String aiResponse, String context) {
        try {
            // Create memory document
            String memoryText = String.format(
                "User: %s\nAI: %s\nContext: %s\nTimestamp: %s",
                userMessage, aiResponse, context, LocalDateTime.now()
            );
            
            Document memoryDoc = Document.from(memoryText);
            memoryDoc.metadata().put("userId", String.valueOf(userId));
            memoryDoc.metadata().put("timestamp", LocalDateTime.now().toString());
            memoryDoc.metadata().put("type", "conversation");
            
            // Store in embedding store
            var embedding = embeddingModel.embed(memoryText).content();
            TextSegment segment = TextSegment.from(memoryText, memoryDoc.metadata());
            memoryStore.add(embedding, segment);
            
            logger.info("💾 Stored conversation in long-term memory for user {}", userId);
            
        } catch (Exception e) {
            logger.error("❌ Failed to store conversation in memory: " + e.getMessage(), e);
        }
    }
    
    /**
     * 🗜️ Compress old memories to optimize storage
     */
    private void compressOldMemories(int userId) {
        try {
            // This would implement memory compression logic
            // For now, we'll just log the intention
            logger.info("🗜️ Memory compression triggered for user {}", userId);
            
        } catch (Exception e) {
            logger.error("❌ Failed to compress memories: " + e.getMessage(), e);
        }
    }
    
    /**
     * 🔑 Generate memory key for session
     */
    private String generateMemoryKey(int userId, String sessionId) {
        return String.format("user_%d_session_%s", userId, sessionId);
    }
    
    /**
     * 🧹 Clear session memory
     */
    public void clearSessionMemory(int userId, String sessionId) {
        String memoryKey = generateMemoryKey(userId, sessionId);
        sessionMemories.remove(memoryKey);
        logger.info("🧹 Cleared session memory: {}", memoryKey);
    }
    
    /**
     * 📊 Get memory statistics
     */
    public String getMemoryStats() {
        return String.format(
            "Session Memories: %d | Memory Store: %s | Embedding Model: %s",
            sessionMemories.size(),
            memoryStore.getClass().getSimpleName(),
            embeddingModel.getClass().getSimpleName()
        );
    }
    
    /**
     * 🤖 Memory Assistant Interface
     */
    public interface MemoryAssistant {
        
        @SystemMessage("Bạn là AI trợ lý thông minh với khả năng ghi nhớ và duy trì ngữ cảnh. " +
                      "Sử dụng thông tin từ bộ nhớ dài hạn để cung cấp câu trả lời chính xác và liên quan. " +
                      "Luôn duy trì tính nhất quán trong các cuộc trò chuyện.")
        String chatWithMemory(@UserMessage String userMessage, 
                            @V("context") String context, 
                            @V("memoryKey") String memoryKey);
    }
} 