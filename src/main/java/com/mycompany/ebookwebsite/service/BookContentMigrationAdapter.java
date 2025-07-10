package com.mycompany.ebookwebsite.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.ebookwebsite.model.Ebook;

/**
 * 🔄 Migration Adapter for BookContentContextService
 * 
 * Provides backward compatibility while migrating to LangChain4j
 */
public class BookContentMigrationAdapter {
    
    private static final Logger logger = LoggerFactory.getLogger(BookContentMigrationAdapter.class);
    
    private final LangChain4jBookContentRetriever modernService;
    
    public BookContentMigrationAdapter() {
        this.modernService = new LangChain4jBookContentRetriever();
        
        logger.info("🔄 BookContentMigrationAdapter initialized with LangChain4j service");
    }
    
    /**
     * 🔍 Extract relevant content with modern service
     */
    public String extractRelevantContent(List<Ebook> books, String userQuestion) {
        try {
            return extractWithModernService(books, userQuestion);
        } catch (Exception e) {
            logger.error("❌ Modern service failed, using fallback: " + e.getMessage());
            return extractWithFallback(books, userQuestion);
        }
    }
    
    /**
     * 🚀 Use modern LangChain4j service
     */
    private String extractWithModernService(List<Ebook> books, String userQuestion) {
        logger.info("🚀 Using LangChain4j content retriever");
        
        StringBuilder result = new StringBuilder();
        result.append("=== Kết quả tìm kiếm ===\n\n");
        
        for (Ebook book : books) {
            try {
                // Index book content
                String bookContent = book.getDescription() != null ? book.getDescription() : book.getTitle();
                Map<String, String> metadata = Map.of(
                    "bookId", String.valueOf(book.getId()),
                    "bookTitle", book.getTitle(),
                    "bookType", book.getReleaseType() != null ? book.getReleaseType() : "Unknown"
                );
                
                modernService.indexBookContent(String.valueOf(book.getId()), bookContent, metadata);
                
                // Retrieve relevant content
                List<String> relevantContent = modernService.retrieveRelevantContent(userQuestion, 3);
                
                result.append("📚 ").append(book.getTitle()).append("\n");
                if (!relevantContent.isEmpty()) {
                    result.append("🔍 Nội dung liên quan:\n");
                    for (String content : relevantContent) {
                        result.append("   • ").append(content.substring(0, Math.min(100, content.length()))).append("...\n");
                    }
                }
                result.append("\n");
                
            } catch (Exception e) {
                logger.error("Error processing book {}: {}", book.getTitle(), e.getMessage());
                result.append("📚 ").append(book.getTitle()).append(" - Đang xử lý...\n\n");
            }
        }
        
        return result.toString();
    }
    
    /**
     * 🛡️ Fallback method if modern service fails
     */
    private String extractWithFallback(List<Ebook> books, String userQuestion) {
        StringBuilder fallbackContent = new StringBuilder();
        fallbackContent.append("=== Kết quả tìm kiếm ===\n\n");
        
        for (Ebook book : books) {
            fallbackContent.append("📚 ").append(book.getTitle()).append("\n");
            if (book.getDescription() != null && !book.getDescription().isEmpty()) {
                fallbackContent.append("📝 ").append(book.getDescription().substring(0, 
                    Math.min(200, book.getDescription().length()))).append("...\n");
            }
            fallbackContent.append("\n");
        }
        
        fallbackContent.append("💡 Hệ thống đang được nâng cấp. Vui lòng thử lại sau!");
        return fallbackContent.toString();
    }
    
    /**
     * 📊 Get service statistics
     */
    public String getServiceStats() {
        return "Modern LangChain4j Service - " + modernService.getStats();
    }
} 