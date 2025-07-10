package com.mycompany.ebookwebsite.service;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.transformer.QueryTransformer;

import com.mycompany.ebookwebsite.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

/**
 * 🔄 Query Transformation Pipeline
 * 
 * Xử lý queries thông minh với multiple transformation strategies
 * Features:
 * - Query compression
 * - Query expansion
 * - Intent classification
 * - Context enhancement
 */
public class QueryTransformationPipeline {
    
    private static final Logger logger = LoggerFactory.getLogger(QueryTransformationPipeline.class);
    
    private final ChatLanguageModel chatModel;
    private final QueryTransformer compressingTransformer;
    private final QueryTransformer expandingTransformer;
    
    public QueryTransformationPipeline() {
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
                    .temperature(0.3)
                    .build();
            
            // Initialize transformers
            this.compressingTransformer = new CompressingQueryTransformer(chatModel);
            this.expandingTransformer = new ExpandingQueryTransformer(chatModel);
            
            logger.info("✅ QueryTransformationPipeline initialized successfully");
            
        } catch (Exception e) {
            logger.error("❌ Failed to initialize QueryTransformationPipeline: " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize query transformation pipeline", e);
        }
    }
    
    /**
     * 🔄 Transform query using multiple strategies
     */
    public String transformQuery(String originalQuery) {
        try {
            logger.info("🔄 Transforming query: {}", originalQuery);
            
            // Create original query
            Query query = Query.from(originalQuery);
            
            // Apply compressing transformation
            List<Query> compressedQueries = new ArrayList<>(compressingTransformer.transform(query));
            
            // Apply expanding transformation
            List<Query> expandedQueries = new ArrayList<>(expandingTransformer.transform(query));
            
            // Combine all transformations
            List<Query> allQueries = new ArrayList<>();
            allQueries.add(query); // Original query
            allQueries.addAll(compressedQueries);
            allQueries.addAll(expandedQueries);
            
            logger.info("✅ Query transformed into {} variations", allQueries.size());
            
            // Return the most relevant transformed query (first one)
            return allQueries.get(0).text();
            
        } catch (Exception e) {
            logger.error("❌ Query transformation failed: " + e.getMessage(), e);
            return originalQuery; // Return original as fallback
        }
    }
    
    /**
     * 🚀 Enhance query with additional context
     */
    public String enhanceQuery(String originalQuery) {
        try {
            logger.info("🚀 Enhancing query: {}", originalQuery);
            
            String systemPrompt = "Bạn là chuyên gia cải thiện câu hỏi tìm kiếm.\n" +
                "Nhiệm vụ: Cải thiện câu hỏi để tìm kiếm hiệu quả hơn.\n" +
                "Trả về câu hỏi đã cải thiện, không có giải thích.";
            
            String userPrompt = "Cải thiện câu hỏi này: " + originalQuery;
            
            List<ChatMessage> messages = List.of(
                new SystemMessage(systemPrompt),
                new UserMessage(userPrompt)
            );
            
            String enhancedQuery = chatModel.generate(messages).content().text();
            
            logger.info("✅ Query enhanced successfully");
            return enhancedQuery;
            
        } catch (Exception e) {
            logger.error("❌ Query enhancement failed: " + e.getMessage(), e);
            return originalQuery; // Return original as fallback
        }
    }
    
    /**
     * 🏷️ Classify query type
     */
    public String classifyQueryType(String query) {
        try {
            logger.info("🏷️ Classifying query type: {}", query);
            
            String systemPrompt = "Bạn là chuyên gia phân loại câu hỏi.\n" +
                "Nhiệm vụ: Phân loại câu hỏi thành một trong các loại sau:\n" +
                "- SEARCH: Tìm kiếm sách\n" +
                "- RECOMMENDATION: Gợi ý sách\n" +
                "- ANALYSIS: Phân tích nội dung\n" +
                "- CHAT: Trò chuyện chung\n" +
                "Trả về chỉ tên loại, không có giải thích.";
            
            String userPrompt = "Phân loại câu hỏi này: " + query;
            
            List<ChatMessage> messages = List.of(
                new SystemMessage(systemPrompt),
                new UserMessage(userPrompt)
            );
            
            String queryType = chatModel.generate(messages).content().text();
            
            logger.info("✅ Query type classified: {}", queryType);
            return queryType;
            
        } catch (Exception e) {
            logger.error("❌ Query classification failed: " + e.getMessage(), e);
            return "CHAT"; // Default fallback
        }
    }
    
    /**
     * 🎯 Compressing Query Transformer
     */
    private static class CompressingQueryTransformer implements QueryTransformer {
        
        private final ChatLanguageModel chatModel;
        
        public CompressingQueryTransformer(ChatLanguageModel chatModel) {
            this.chatModel = chatModel;
        }
        
        @Override
        public List<Query> transform(Query query) {
            try {
                String systemPrompt = "Bạn là chuyên gia nén câu hỏi.\n" +
                    "Nhiệm vụ: Nén câu hỏi thành từ khóa ngắn gọn nhưng vẫn giữ ý nghĩa.\n" +
                    "Trả về chỉ từ khóa, không có giải thích.";
                
                String userPrompt = "Nén câu hỏi này: " + query.text();
                
                List<ChatMessage> messages = List.of(
                    new SystemMessage(systemPrompt),
                    new UserMessage(userPrompt)
                );
                
                String compressedText = chatModel.generate(messages).content().text();
                
                return List.of(Query.from(compressedText));
                
            } catch (Exception e) {
                logger.error("❌ Query compression failed: " + e.getMessage());
                return List.of(query); // Return original as fallback
            }
        }
    }
    
    /**
     * 🚀 Expanding Query Transformer
     */
    private static class ExpandingQueryTransformer implements QueryTransformer {
        
        private final ChatLanguageModel chatModel;
        
        public ExpandingQueryTransformer(ChatLanguageModel chatModel) {
            this.chatModel = chatModel;
        }
        
        @Override
        public List<Query> transform(Query query) {
            try {
                String systemPrompt = "Bạn là chuyên gia mở rộng câu hỏi.\n" +
                    "Nhiệm vụ: Mở rộng câu hỏi với từ khóa liên quan để tìm kiếm hiệu quả hơn.\n" +
                    "Trả về câu hỏi đã mở rộng, không có giải thích.";
                
                String userPrompt = "Mở rộng câu hỏi này: " + query.text();
                
                List<ChatMessage> messages = List.of(
                    new SystemMessage(systemPrompt),
                    new UserMessage(userPrompt)
                );
                
                String expandedText = chatModel.generate(messages).content().text();
                
                return List.of(Query.from(expandedText));
                
            } catch (Exception e) {
                logger.error("❌ Query expansion failed: " + e.getMessage());
                return List.of(query); // Return original as fallback
            }
        }
    }
    
    /**
     * 📊 Get pipeline statistics
     */
    public String getPipelineStats() {
        return String.format("QueryTransformationPipeline - ChatModel: %s, Compressing: %s, Expanding: %s", 
            chatModel.getClass().getSimpleName(),
            compressingTransformer.getClass().getSimpleName(),
            expandingTransformer.getClass().getSimpleName());
    }
} 