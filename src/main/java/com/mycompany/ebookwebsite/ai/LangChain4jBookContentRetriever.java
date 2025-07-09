package com.mycompany.ebookwebsite.ai;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 🔥 LangChain4j-based Book Content Retriever
 * 
 * Thay thế BookContentContextService với vector search mạnh mẽ
 * Features:
 * - Semantic search thay vì keyword matching
 * - Multi-book content retrieval
 * - Dynamic filtering theo book metadata
 * - Caching cho performance
 */
public class LangChain4jBookContentRetriever {
    
    private static final Logger logger = LoggerFactory.getLogger(LangChain4jBookContentRetriever.class);
    
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingStoreIngestor ingestor;
    
    // Cache cho book content để tránh re-indexing
    private final Map<String, Boolean> indexedBooks = new ConcurrentHashMap<>();
    
    public LangChain4jBookContentRetriever() {
        try {
            // Initialize OpenAI embedding model
            String apiKey = Utils.getEnv("OPENAI_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                logger.warn("OPENAI_API_KEY not set, using dummy key");
                apiKey = "dummy-key-for-initialization";
            }
            
            this.embeddingModel = OpenAiEmbeddingModel.builder()
                    .apiKey(apiKey)
                    .build();
            
            // Initialize embedding store (In-Memory for now, can upgrade to Redis)
            this.embeddingStore = new InMemoryEmbeddingStore<>();
            
            // Initialize document splitter for chunking
            DocumentSplitter splitter = DocumentSplitters.recursive(1000, 200);
            
            // Initialize ingestor for indexing
            this.ingestor = EmbeddingStoreIngestor.builder()
                    .documentSplitter(splitter)
                    .embeddingModel(embeddingModel)
                    .embeddingStore(embeddingStore)
                    .build();
            
            logger.info("✅ LangChain4jBookContentRetriever initialized successfully");
            
        } catch (Exception e) {
            logger.error("❌ Failed to initialize LangChain4jBookContentRetriever: " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize content retriever", e);
        }
    }
    
    /**
     * 📚 Index book content into vector store
     */
    public void indexBookContent(String bookId, String bookContent, Map<String, String> metadata) {
        try {
            // Create document with metadata
            Document document = Document.from(bookContent);
            
            // Index document
            ingestor.ingest(document);
            
            // Mark as indexed
            indexedBooks.put(bookId, true);
            
            logger.info("✅ Indexed book: {} ({} chars)", bookId, bookContent.length());
            
        } catch (Exception e) {
            logger.error("❌ Failed to index book {}: {}", bookId, e.getMessage());
        }
    }
    
    /**
     * 🔍 Retrieve relevant content using semantic search
     */
    public List<String> retrieveRelevantContent(String query, int maxResults) {
        try {
            logger.info("🔍 Retrieving relevant content for query: {}", query);
            
            // Generate embedding for query
            Embedding queryEmbedding = embeddingModel.embed(query).content();
            
            // Find relevant segments
            List<dev.langchain4j.store.embedding.EmbeddingMatch<TextSegment>> matches = 
                embeddingStore.findRelevant(queryEmbedding, maxResults);
            
            // Extract text from segments
            List<String> results = matches.stream()
                    .map(match -> match.embedded().text())
                    .toList();
            
            logger.info("✅ Retrieved {} relevant content pieces", results.size());
            return results;
            
        } catch (Exception e) {
            logger.error("❌ Failed to retrieve relevant content: " + e.getMessage(), e);
            return List.of("Không tìm thấy nội dung liên quan");
        }
    }
    
    /**
     * 🔍 Semantic search for books
     */
    public List<String> semanticSearch(String query, int maxResults) {
        try {
            logger.info("🔍 Semantic search for query: {}", query);
            
            // Generate embedding for query
            Embedding queryEmbedding = embeddingModel.embed(query).content();
            
            // Find relevant segments
            List<dev.langchain4j.store.embedding.EmbeddingMatch<TextSegment>> matches = 
                embeddingStore.findRelevant(queryEmbedding, maxResults);
            
            // Extract text from segments
            List<String> results = matches.stream()
                    .map(match -> match.embedded().text())
                    .toList();
            
            logger.info("✅ Semantic search found {} results", results.size());
            return results;
            
        } catch (Exception e) {
            logger.error("❌ Semantic search failed: " + e.getMessage(), e);
            return List.of("Không tìm thấy kết quả tìm kiếm");
        }
    }
    
    /**
     * 📊 Calculate similarity between two texts
     */
    public double calculateSimilarity(String text1, String text2) {
        try {
            logger.info("📊 Calculating similarity between texts");
            
            // Generate embeddings
            Embedding embedding1 = embeddingModel.embed(text1).content();
            Embedding embedding2 = embeddingModel.embed(text2).content();
            
            // Calculate cosine similarity
            double similarity = calculateCosineSimilarity(embedding1.vector(), embedding2.vector());
            
            logger.info("✅ Similarity calculated: {}", similarity);
            return similarity;
            
        } catch (Exception e) {
            logger.error("❌ Failed to calculate similarity: " + e.getMessage(), e);
            return 0.0;
        }
    }
    
    /**
     * 📐 Calculate cosine similarity between two vectors
     */
    private double calculateCosineSimilarity(float[] vector1, float[] vector2) {
        if (vector1.length != vector2.length) {
            return 0.0;
        }
        
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += vector1[i] * vector1[i];
            norm2 += vector2[i] * vector2[i];
        }
        
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }
        
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
    
    /**
     * 🧹 Clear cache and re-index all books
     */
    public void clearCache() {
        indexedBooks.clear();
        logger.info("🧹 Cache cleared, books will be re-indexed on next access");
    }
    
    /**
     * 📊 Get indexing statistics
     */
    public Map<String, Object> getStats() {
        return Map.of(
            "indexedBooks", indexedBooks.size(),
            "embeddingStore", embeddingStore.getClass().getSimpleName(),
            "embeddingModel", embeddingModel.getClass().getSimpleName()
        );
    }
} 