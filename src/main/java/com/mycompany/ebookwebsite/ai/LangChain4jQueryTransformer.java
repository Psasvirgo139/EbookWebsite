package com.mycompany.ebookwebsite.ai;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.transformer.QueryTransformer;

import com.mycompany.ebookwebsite.model.QuestionContext;
import com.mycompany.ebookwebsite.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

/**
 * 🔄 LangChain4j-based Query Transformer
 * 
 * Thay thế QuestionAnalysisService với AI-powered query transformation
 * Features:
 * - Query compression cho follow-up questions
 * - Query expansion cho better retrieval
 * - Intent classification
 * - Context-aware query enhancement
 */
public class LangChain4jQueryTransformer implements QueryTransformer {
    
    private static final Logger logger = LoggerFactory.getLogger(LangChain4jQueryTransformer.class);
    
    private final ChatLanguageModel chatModel;
    private final QuestionContextAnalyzer contextAnalyzer;
    
    public LangChain4jQueryTransformer() {
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
            
            this.contextAnalyzer = new QuestionContextAnalyzer();
            
            logger.info("✅ LangChain4jQueryTransformer initialized successfully");
            
        } catch (Exception e) {
            logger.error("❌ Failed to initialize LangChain4jQueryTransformer: " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize query transformer", e);
        }
    }
    
    /**
     * 🔄 Transform query using AI-powered analysis
     */
    @Override
    public List<Query> transform(Query query) {
        try {
            String originalQuery = query.text();
            logger.info("🔄 Transforming query: {}", originalQuery);
            
            // Analyze question context
            QuestionContext context = contextAnalyzer.analyzeQuestion(originalQuery);
            
            // Transform based on question type
            List<Query> transformedQueries;
            switch (context.getQuestionType()) {
                case BOOK_SEARCH:
                    transformedQueries = transformBookSearchQuery(query, context);
                    break;
                case CHARACTER_QUESTION:
                    transformedQueries = transformCharacterQuery(query, context);
                    break;
                case PLOT_QUESTION:
                    transformedQueries = transformPlotQuery(query, context);
                    break;
                case RECOMMENDATION:
                    transformedQueries = transformRecommendationQuery(query, context);
                    break;
                case CONTENT_INQUIRY:
                    transformedQueries = transformContentQuery(query, context);
                    break;
                default:
                    transformedQueries = List.of(enhanceQuery(query, context));
                    break;
            }
            
            logger.info("✅ Query transformed into {} queries", transformedQueries.size());
            return transformedQueries;
            
        } catch (Exception e) {
            logger.error("❌ Query transformation failed: " + e.getMessage(), e);
            // Return original query as fallback
            return List.of(query);
        }
    }
    
    /**
     * 📚 Transform book search queries
     */
    private List<Query> transformBookSearchQuery(Query originalQuery, QuestionContext context) {
        Query enhancedQuery = enhanceQuery(originalQuery, context);
        
        // Create multiple search strategies
        List<Query> queries = new ArrayList<>();
        queries.add(enhancedQuery); // Original enhanced query
        queries.add(Query.from("tìm sách " + String.join(" ", context.getKeywords()))); // Keyword-based
        queries.add(Query.from("sách hay " + context.getIntent())); // Intent-based
        
        return queries;
    }
    
    /**
     * 👥 Transform character-related queries
     */
    private List<Query> transformCharacterQuery(Query originalQuery, QuestionContext context) {
        Query enhancedQuery = enhanceQuery(originalQuery, context);
        
        List<Query> queries = new ArrayList<>();
        queries.add(enhancedQuery);
        queries.add(Query.from("nhân vật " + String.join(" ", context.getKeywords())));
        queries.add(Query.from("tính cách nhân vật " + context.getIntent()));
        
        return queries;
    }
    
    /**
     * 📖 Transform plot-related queries
     */
    private List<Query> transformPlotQuery(Query originalQuery, QuestionContext context) {
        Query enhancedQuery = enhanceQuery(originalQuery, context);
        
        List<Query> queries = new ArrayList<>();
        queries.add(enhancedQuery);
        queries.add(Query.from("cốt truyện " + String.join(" ", context.getKeywords())));
        queries.add(Query.from("diễn biến " + context.getIntent()));
        
        return queries;
    }
    
    /**
     * 🎯 Transform recommendation queries
     */
    private List<Query> transformRecommendationQuery(Query originalQuery, QuestionContext context) {
        Query enhancedQuery = enhanceQuery(originalQuery, context);
        
        List<Query> queries = new ArrayList<>();
        queries.add(enhancedQuery);
        queries.add(Query.from("gợi ý sách " + String.join(" ", context.getKeywords())));
        queries.add(Query.from("sách tương tự " + context.getIntent()));
        
        return queries;
    }
    
    /**
     * 📝 Transform content inquiry queries
     */
    private List<Query> transformContentQuery(Query originalQuery, QuestionContext context) {
        Query enhancedQuery = enhanceQuery(originalQuery, context);
        
        List<Query> queries = new ArrayList<>();
        queries.add(enhancedQuery);
        queries.add(Query.from("nội dung " + String.join(" ", context.getKeywords())));
        queries.add(Query.from("chi tiết " + context.getIntent()));
        
        return queries;
    }
    
    /**
     * 🚀 Enhance query using AI
     */
    private Query enhanceQuery(Query originalQuery, QuestionContext context) {
        try {
            String systemPrompt = buildSystemPrompt(context);
            String userPrompt = buildUserPrompt(originalQuery.text(), context);
            
            List<ChatMessage> messages = List.of(
                new SystemMessage(systemPrompt),
                new UserMessage(userPrompt)
            );
            
            String enhancedText = chatModel.generate(messages).content().text();
            
            return Query.from(enhancedText);
            
        } catch (Exception e) {
            logger.error("❌ Query enhancement failed: " + e.getMessage());
            return originalQuery;
        }
    }
    
    /**
     * 📝 Build system prompt for query enhancement
     */
    private String buildSystemPrompt(QuestionContext context) {
        return String.format(
            "Bạn là chuyên gia phân tích câu hỏi và tìm kiếm sách.\n\n" +
            "Nhiệm vụ: Cải thiện câu hỏi để tìm kiếm hiệu quả hơn.\n\n" +
            "Thông tin ngữ cảnh:\n" +
            "- Loại câu hỏi: %s\n" +
            "- Ý định: %s\n" +
            "- Từ khóa: %s\n" +
            "- Độ tin cậy: %.2f\n\n" +
            "Yêu cầu:\n" +
            "- Giữ nguyên ý nghĩa gốc\n" +
            "- Thêm từ khóa liên quan\n" +
            "- Tối ưu cho tìm kiếm semantic\n" +
            "- Trả về câu hỏi đã cải thiện",
            context.getQuestionType(),
            context.getIntent(),
            String.join(", ", context.getKeywords()),
            context.getConfidence()
        );
    }
    
    /**
     * 📝 Build user prompt for query enhancement
     */
    private String buildUserPrompt(String originalQuery, QuestionContext context) {
        return String.format(
            "Câu hỏi gốc: %s\n\n" +
            "Hãy cải thiện câu hỏi này để tìm kiếm hiệu quả hơn.\n" +
            "Trả về chỉ câu hỏi đã cải thiện, không có giải thích thêm.",
            originalQuery
        );
    }
    
    /**
     * 🔍 Question Context Analyzer (simplified version)
     */
    private static class QuestionContextAnalyzer {
        
        public QuestionContext analyzeQuestion(String question) {
            QuestionContext context = new QuestionContext(question);
            
            // Simple classification logic (can be enhanced)
            String lowerQuestion = question.toLowerCase();
            
            if (lowerQuestion.contains("tìm") || lowerQuestion.contains("search")) {
                context.setQuestionType(QuestionContext.QuestionType.BOOK_SEARCH);
            } else if (lowerQuestion.contains("nhân vật") || lowerQuestion.contains("character")) {
                context.setQuestionType(QuestionContext.QuestionType.CHARACTER_QUESTION);
            } else if (lowerQuestion.contains("cốt truyện") || lowerQuestion.contains("plot")) {
                context.setQuestionType(QuestionContext.QuestionType.PLOT_QUESTION);
            } else if (lowerQuestion.contains("gợi ý") || lowerQuestion.contains("recommend")) {
                context.setQuestionType(QuestionContext.QuestionType.RECOMMENDATION);
            } else {
                context.setQuestionType(QuestionContext.QuestionType.CONTENT_INQUIRY);
            }
            
            // Extract keywords
            String[] words = lowerQuestion.split("\\s+");
            context.setKeywords(List.of(words));
            
            // Set intent
            context.setIntent("Tìm kiếm thông tin");
            
            // Set confidence
            context.setConfidence(0.8);
            
            return context;
        }
    }
} 