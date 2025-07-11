package com.mycompany.ebookwebsite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.dao.EbookAuthorDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.EbookAuthor;
import com.mycompany.ebookwebsite.utils.Utils;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
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

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 🔗 Advanced Book Link Coordination Service
 * 
 * Implements advanced book relationship features:
 * - Cross-book References: Find connections between books
 * - Series Detection: Automatically detect book series
 * - Author Network: Analyze author relationships and collaborations
 * - Content Similarity: Find similar books based on content
 * - Genre Relationships: Map genre connections and trends
 * - Reading Paths: Suggest optimal reading sequences
 */
public class AdvancedBookLinkService {
    
    private static final Logger logger = LoggerFactory.getLogger(AdvancedBookLinkService.class);
    
    private final EmbeddingStore<TextSegment> bookLinkStore;
    private final EmbeddingModel embeddingModel;
    private final ChatLanguageModel chatModel;
    private final BookLinkAssistant bookLinkAssistant;
    private final EbookDAO ebookDAO;
    private final EbookAuthorDAO ebookAuthorDAO;
    private final Map<String, List<String>> seriesCache = new ConcurrentHashMap<>();
    private final Map<Integer, List<Integer>> authorNetworkCache = new ConcurrentHashMap<>();
    
    public AdvancedBookLinkService() {
        try {
            // Initialize DAOs
            this.ebookDAO = new EbookDAO();
            this.ebookAuthorDAO = new EbookAuthorDAO();
            
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
            
            // Initialize Redis for book link storage
            this.bookLinkStore = RedisEmbeddingStore.builder()
                    .host("localhost")
                    .port(6379)
                    .dimension(1536)
                    .indexName("ebook_links")
                    .build();
            
            // Initialize book link assistant
            this.bookLinkAssistant = AiServices.builder(BookLinkAssistant.class)
                    .chatLanguageModel(chatModel)
                    .build();
            
            logger.info("✅ AdvancedBookLinkService initialized successfully");
            
        } catch (Exception e) {
            logger.error("❌ Failed to initialize AdvancedBookLinkService: " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize book link service", e);
        }
    }
    
    /**
     * 🔍 Find cross-book references
     */
    public String findCrossBookReferences(int bookId) {
        try {
            Ebook book = ebookDAO.selectEbook(bookId);
            if (book == null) {
                return "Không tìm thấy sách với ID: " + bookId;
            }
            
            // Get book content for analysis
            String bookContent = getBookContent(book);
            
            // Find similar books
            List<Ebook> similarBooks = findSimilarBooks(bookContent, 5);
            
            // Analyze cross-references
            String analysis = bookLinkAssistant.analyzeCrossReferences(book.getTitle(), similarBooks);
            
            // Store book relationships
            storeBookRelationships(book, similarBooks);
            
            return analysis;
            
        } catch (Exception e) {
            logger.error("❌ Failed to find cross-book references: " + e.getMessage(), e);
            return "Không thể tìm thấy tham chiếu chéo cho sách này. 😔";
        }
    }
    
    /**
     * 📚 Detect book series
     */
    public String detectBookSeries(int bookId) {
        try {
            Ebook book = ebookDAO.selectEbook(bookId);
            if (book == null) {
                return "Không tìm thấy sách với ID: " + bookId;
            }
            
            // Check cache first
            String cacheKey = "series_" + book.getTitle();
            if (seriesCache.containsKey(cacheKey)) {
                return formatSeriesInfo(book.getTitle(), seriesCache.get(cacheKey));
            }
            
            // Find potential series books
            List<Ebook> seriesBooks = findPotentialSeriesBooks(book);
            
            // Analyze series patterns
            String seriesAnalysis = bookLinkAssistant.analyzeSeriesPattern(book.getTitle(), seriesBooks);
            
            // Cache the result
            List<String> seriesTitles = seriesBooks.stream()
                    .map(Ebook::getTitle)
                    .collect(Collectors.toList());
            seriesCache.put(cacheKey, seriesTitles);
            
            return seriesAnalysis;
            
        } catch (Exception e) {
            logger.error("❌ Failed to detect book series: " + e.getMessage(), e);
            return "Không thể phát hiện series cho sách này. 😔";
        }
    }
    
    /**
     * 👥 Analyze author network
     */
    public String analyzeAuthorNetwork(int authorId) {
        try {
            // Get author's books
            List<EbookAuthor> authorBooks = ebookAuthorDAO.getEbooksByAuthor(authorId);
            
            // Find collaborating authors
            List<Integer> collaboratingAuthors = findCollaboratingAuthors(authorId);
            
            // Analyze author relationships
            String networkAnalysis = bookLinkAssistant.analyzeAuthorNetwork(authorId, authorBooks, collaboratingAuthors);
            
            // Cache author network
            authorNetworkCache.put(authorId, collaboratingAuthors);
            
            return networkAnalysis;
            
        } catch (Exception e) {
            logger.error("❌ Failed to analyze author network: " + e.getMessage(), e);
            return "Không thể phân tích mạng lưới tác giả. 😔";
        }
    }
    
    /**
     * 🎯 Find similar books based on content
     */
    private List<Ebook> findSimilarBooks(String bookContent, int maxResults) {
        try {
            // Create embedding for book content
            var contentEmbedding = embeddingModel.embed(bookContent).content();
            
            // Search for similar content in vector store
            var searchResult = bookLinkStore.findRelevant(contentEmbedding, maxResults, 0.7);
            
            List<Ebook> similarBooks = new ArrayList<>();
            for (var match : searchResult) {
                TextSegment segment = match.embedded();
                String bookId = segment.metadata().getString("bookId");
                if (bookId != null) {
                    try {
                        Ebook book = ebookDAO.selectEbook(Integer.parseInt(bookId));
                        if (book != null) {
                            similarBooks.add(book);
                        }
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid book ID in metadata: {}", bookId);
                    }
                }
            }
            
            return similarBooks;
            
        } catch (Exception e) {
            logger.error("❌ Failed to find similar books: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 📖 Get book content for analysis
     */
    private String getBookContent(Ebook book) {
        // This would typically read the actual book file
        // For now, we'll use the title and description
        return String.format("Title: %s\nDescription: %s", 
            book.getTitle(), book.getDescription());
    }
    
    /**
     * 🔗 Find potential series books
     */
    private List<Ebook> findPotentialSeriesBooks(Ebook book) {
        try {
            List<Ebook> allBooks = ebookDAO.selectAllEbooks();
            
            return allBooks.stream()
                    .filter(otherBook -> isPotentialSeriesBook(book, otherBook))
                    .limit(10)
                    .collect(Collectors.toList());
                    
        } catch (SQLException e) {
            logger.error("❌ Failed to find potential series books: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 🔍 Check if books are potential series
     */
    private boolean isPotentialSeriesBook(Ebook book1, Ebook book2) {
        if (book1.getId() == book2.getId()) {
            return false;
        }
        
        String title1 = book1.getTitle().toLowerCase();
        String title2 = book2.getTitle().toLowerCase();
        
        // Check for common patterns in series
        return title1.contains(title2) || title2.contains(title1) ||
               title1.matches(".*\\d+.*") && title2.matches(".*\\d+.*") ||
               title1.contains("series") || title2.contains("series");
    }
    
    /**
     * 👥 Find collaborating authors
     */
    private List<Integer> findCollaboratingAuthors(int authorId) {
        try {
            List<EbookAuthor> authorBooks = ebookAuthorDAO.getEbooksByAuthor(authorId);
            Set<Integer> collaboratingAuthors = new HashSet<>();
            
            for (EbookAuthor authorBook : authorBooks) {
                List<EbookAuthor> bookAuthors = ebookAuthorDAO.getAuthorsByEbook(authorBook.getEbookID());
                
                for (EbookAuthor bookAuthor : bookAuthors) {
                    if (bookAuthor.getAuthorID() != authorId) {
                        collaboratingAuthors.add(bookAuthor.getAuthorID());
                    }
                }
            }
            
            return new ArrayList<>(collaboratingAuthors);
            
        } catch (SQLException e) {
            logger.error("❌ Failed to find collaborating authors: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 💾 Store book relationships
     */
    private void storeBookRelationships(Ebook book, List<Ebook> relatedBooks) {
        try {
            String relationshipText = String.format(
                "Book: %s\nRelated Books: %s\nTimestamp: %s",
                book.getTitle(),
                relatedBooks.stream().map(Ebook::getTitle).collect(Collectors.joining(", ")),
                LocalDateTime.now()
            );
            
            Document relationshipDoc = Document.from(relationshipText);
            relationshipDoc.metadata().put("bookId", String.valueOf(book.getId()));
            relationshipDoc.metadata().put("timestamp", LocalDateTime.now().toString());
            relationshipDoc.metadata().put("type", "book_relationship");
            
            var embedding = embeddingModel.embed(relationshipText).content();
            TextSegment segment = TextSegment.from(relationshipText, relationshipDoc.metadata());
            bookLinkStore.add(embedding, segment);
            
            logger.info("💾 Stored book relationships for book: {}", book.getTitle());
            
        } catch (Exception e) {
            logger.error("❌ Failed to store book relationships: " + e.getMessage(), e);
        }
    }
    
    /**
     * 📋 Format series information
     */
    private String formatSeriesInfo(String bookTitle, List<String> seriesTitles) {
        StringBuilder result = new StringBuilder();
        result.append("📚 Series Detection for: ").append(bookTitle).append("\n\n");
        
        if (seriesTitles.isEmpty()) {
            result.append("Không tìm thấy series cho sách này.");
        } else {
            result.append("Các sách trong cùng series:\n");
            for (int i = 0; i < seriesTitles.size(); i++) {
                result.append(i + 1).append(". ").append(seriesTitles.get(i)).append("\n");
            }
        }
        
        return result.toString();
    }
    
    /**
     * 📊 Get book link statistics
     */
    public String getBookLinkStats() {
        return String.format(
            "Book Link Store: %s | Series Cache: %d | Author Networks: %d | Embedding Model: %s",
            bookLinkStore.getClass().getSimpleName(),
            seriesCache.size(),
            authorNetworkCache.size(),
            embeddingModel.getClass().getSimpleName()
        );
    }
    
    /**
     * 🧹 Clear caches
     */
    public void clearCaches() {
        seriesCache.clear();
        authorNetworkCache.clear();
        logger.info("🧹 Book link caches cleared");
    }
    
    /**
     * 🤖 Book Link Assistant Interface
     */
    public interface BookLinkAssistant {
        
        @SystemMessage("Bạn là AI chuyên gia phân tích mối quan hệ giữa các sách. " +
                      "Tìm kiếm các tham chiếu chéo, series, và mạng lưới tác giả. " +
                      "Cung cấp phân tích chi tiết về các mối quan hệ này.")
        String analyzeCrossReferences(@V("bookTitle") String bookTitle, 
                                   @V("similarBooks") List<Ebook> similarBooks);
        
        @SystemMessage("Phân tích các mẫu series trong sách. " +
                      "Tìm kiếm các đặc điểm chung và thứ tự đọc.")
        String analyzeSeriesPattern(@V("bookTitle") String bookTitle, 
                                  @V("seriesBooks") List<Ebook> seriesBooks);
        
        @SystemMessage("Phân tích mạng lưới tác giả và các mối quan hệ hợp tác. " +
                      "Tìm kiếm các tác giả có xu hướng làm việc cùng nhau.")
        String analyzeAuthorNetwork(@V("authorId") int authorId,
                                  @V("authorBooks") List<EbookAuthor> authorBooks,
                                  @V("collaboratingAuthors") List<Integer> collaboratingAuthors);
    }
} 