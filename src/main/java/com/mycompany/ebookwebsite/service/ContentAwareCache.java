package com.mycompany.ebookwebsite.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.mycompany.ebookwebsite.utils.Utils;

/**
 * 🚀 Content Aware Cache Service
 * 
 * Advanced caching với content awareness và performance optimization
 */
public class ContentAwareCache {
    
    private final Map<String, CacheEntry> cache;
    private final Map<String, String> contentHashMap;
    private final boolean cacheEnabled;
    private final int ttlMinutes;
    private final int maxCacheSize;
    
    public ContentAwareCache() {
        this.cache = new ConcurrentHashMap<>();
        this.contentHashMap = new ConcurrentHashMap<>();
        this.cacheEnabled = Boolean.parseBoolean(Utils.getEnv("CACHE_ENABLED"));
        this.ttlMinutes = Integer.parseInt(Utils.getEnv("CACHE_TTL_MINUTES") != null ? 
            Utils.getEnv("CACHE_TTL_MINUTES") : "60");
        this.maxCacheSize = 1000; // Maximum cache entries
        
        System.out.println("🚀 Content Aware Cache initialized (enabled: " + cacheEnabled + ")");
    }
    
    /**
     * 🔍 Get cached response for question with content context
     */
    public String getCachedResponse(String question, List<Integer> bookIds, String contentHash) {
        if (!cacheEnabled) {
            return null;
        }
        
        String cacheKey = generateCacheKey(question, bookIds, contentHash);
        CacheEntry entry = cache.get(cacheKey);
        
        if (entry != null) {
            if (isEntryValid(entry)) {
                entry.incrementHitCount();
                System.out.println("📈 Cache HIT: " + cacheKey.substring(0, Math.min(30, cacheKey.length())) + "...");
                return entry.getResponse();
            } else {
                // Remove expired entry
                cache.remove(cacheKey);
                System.out.println("⏰ Cache EXPIRED: " + cacheKey.substring(0, Math.min(30, cacheKey.length())) + "...");
            }
        }
        
        System.out.println("📉 Cache MISS: " + cacheKey.substring(0, Math.min(30, cacheKey.length())) + "...");
        return null;
    }
    
    /**
     * 💾 Cache response with content awareness
     */
    public void cacheResponse(String question, List<Integer> bookIds, String contentHash, 
                             String response, double qualityScore) {
        if (!cacheEnabled || response == null || response.trim().isEmpty()) {
            return;
        }
        
        // Only cache high-quality responses
        if (qualityScore < 3.0) {
            System.out.println("❌ Not caching low-quality response (score: " + qualityScore + ")");
            return;
        }
        
        String cacheKey = generateCacheKey(question, bookIds, contentHash);
        
        // Check cache size limit
        if (cache.size() >= maxCacheSize) {
            cleanupOldEntries();
        }
        
        CacheEntry entry = new CacheEntry(question, response, bookIds, contentHash, qualityScore);
        cache.put(cacheKey, entry);
        
        // Store content hash mapping
        if (contentHash != null) {
            contentHashMap.put(String.join(",", bookIds.stream().map(String::valueOf).collect(Collectors.toList())), contentHash);
        }
        
        System.out.println("💾 Cached response: " + cacheKey.substring(0, Math.min(30, cacheKey.length())) + 
            "... (quality: " + String.format("%.1f", qualityScore) + ")");
    }
    
    /**
     * 🧠 Get similar cached responses using content similarity
     */
    public List<CacheEntry> getSimilarResponses(String question, List<Integer> bookIds, double similarityThreshold) {
        if (!cacheEnabled) {
            return new ArrayList<>();
        }
        
        List<CacheEntry> similarEntries = new ArrayList<>();
        
        for (CacheEntry entry : cache.values()) {
            if (isEntryValid(entry)) {
                double similarity = calculateSimilarity(question, entry.getQuestion(), bookIds, entry.getBookIds());
                
                if (similarity >= similarityThreshold) {
                    similarEntries.add(entry);
                }
            }
        }
        
        // Sort by similarity and quality
        return similarEntries.stream()
            .sorted((a, b) -> {
                double scoreA = a.getQualityScore() + (a.getHitCount() * 0.1);
                double scoreB = b.getQualityScore() + (b.getHitCount() * 0.1);
                return Double.compare(scoreB, scoreA);
            })
            .limit(5)
            .collect(Collectors.toList());
    }
    
    /**
     * 🔄 Invalidate cache entries for specific books
     */
    public void invalidateBookCache(int bookId) {
        List<String> keysToRemove = cache.entrySet().stream()
            .filter(entry -> entry.getValue().getBookIds().contains(bookId))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        for (String key : keysToRemove) {
            cache.remove(key);
        }
        
        System.out.println("🗑️ Invalidated " + keysToRemove.size() + " cache entries for book: " + bookId);
    }
    
    /**
     * 📊 Get cache statistics
     */
    public CacheStats getStats() {
        int totalEntries = cache.size();
        int validEntries = 0;
        int totalHits = 0;
        double avgQuality = 0.0;
        
        for (CacheEntry entry : cache.values()) {
            if (isEntryValid(entry)) {
                validEntries++;
                totalHits += entry.getHitCount();
                avgQuality += entry.getQualityScore();
            }
        }
        
        if (validEntries > 0) {
            avgQuality /= validEntries;
        }
        
        return new CacheStats(totalEntries, validEntries, totalHits, avgQuality);
    }
    
    /**
     * 🧹 Clean up expired and low-quality entries
     */
    public void cleanupOldEntries() {
        LocalDateTime cutoff = LocalDateTime.now().minus(ttlMinutes * 2, ChronoUnit.MINUTES);
        
        List<String> keysToRemove = cache.entrySet().stream()
            .filter(entry -> entry.getValue().getCreatedAt().isBefore(cutoff) || 
                           entry.getValue().getQualityScore() < 2.0)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        for (String key : keysToRemove) {
            cache.remove(key);
        }
        
        System.out.println("🧹 Cleaned up " + keysToRemove.size() + " old/low-quality cache entries");
    }
    
    /**
     * 🔄 Refresh cache with updated content
     */
    public void refreshContentCache(List<Integer> bookIds, String newContentHash) {
        String bookIdsKey = String.join(",", bookIds.stream().map(String::valueOf).collect(Collectors.toList()));
        String oldContentHash = contentHashMap.get(bookIdsKey);
        
        if (oldContentHash != null && !oldContentHash.equals(newContentHash)) {
            // Invalidate entries with old content hash
            List<String> keysToRemove = cache.entrySet().stream()
                .filter(entry -> oldContentHash.equals(entry.getValue().getContentHash()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
            
            for (String key : keysToRemove) {
                cache.remove(key);
            }
            
            contentHashMap.put(bookIdsKey, newContentHash);
            System.out.println("🔄 Refreshed cache for updated content: " + bookIds.size() + " books");
        }
    }
    
    // Helper methods
    private String generateCacheKey(String question, List<Integer> bookIds, String contentHash) {
        StringBuilder key = new StringBuilder();
        key.append(normalizeQuestion(question));
        key.append("_");
        key.append(bookIds.stream()
            .sorted()
            .map(String::valueOf)
            .collect(Collectors.joining(",")));
        
        if (contentHash != null) {
            key.append("_").append(contentHash.substring(0, Math.min(8, contentHash.length())));
        }
        
        return Integer.toString(key.toString().hashCode());
    }
    
    private String normalizeQuestion(String question) {
        return question.toLowerCase()
            .replaceAll("[^a-zA-Z0-9\\sàáạảãâấầậẩẫăắằặẳẵêếềệểễôốồộổỗơớờợởỡủũúùụûưứừựửữỳýỷỹỵđ]", "")
            .replaceAll("\\s+", " ")
            .trim();
    }
    
    private boolean isEntryValid(CacheEntry entry) {
        LocalDateTime expiry = entry.getCreatedAt().plus(ttlMinutes, ChronoUnit.MINUTES);
        return LocalDateTime.now().isBefore(expiry);
    }
    
    private double calculateSimilarity(String question1, String question2, List<Integer> bookIds1, List<Integer> bookIds2) {
        // Text similarity (simple word overlap)
        String[] words1 = normalizeQuestion(question1).split("\\s+");
        String[] words2 = normalizeQuestion(question2).split("\\s+");
        
        int commonWords = 0;
        for (String word1 : words1) {
            for (String word2 : words2) {
                if (word1.equals(word2) && word1.length() > 2) {
                    commonWords++;
                    break;
                }
            }
        }
        
        double textSimilarity = words1.length > 0 ? (double) commonWords / Math.max(words1.length, words2.length) : 0.0;
        
        // Book overlap similarity
        long commonBooks = bookIds1.stream().filter(bookIds2::contains).count();
        double bookSimilarity = !bookIds1.isEmpty() ? (double) commonBooks / Math.max(bookIds1.size(), bookIds2.size()) : 0.0;
        
        // Combined similarity (60% text, 40% books)
        return textSimilarity * 0.6 + bookSimilarity * 0.4;
    }
    
    // Inner classes
    public static class CacheEntry {
        private final String question;
        private final String response;
        private final List<Integer> bookIds;
        private final String contentHash;
        private final double qualityScore;
        private final LocalDateTime createdAt;
        private int hitCount;
        
        public CacheEntry(String question, String response, List<Integer> bookIds, String contentHash, double qualityScore) {
            this.question = question;
            this.response = response;
            this.bookIds = new ArrayList<>(bookIds);
            this.contentHash = contentHash;
            this.qualityScore = qualityScore;
            this.createdAt = LocalDateTime.now();
            this.hitCount = 0;
        }
        
        public void incrementHitCount() {
            this.hitCount++;
        }
        
        // Getters
        public String getQuestion() { return question; }
        public String getResponse() { return response; }
        public List<Integer> getBookIds() { return bookIds; }
        public String getContentHash() { return contentHash; }
        public double getQualityScore() { return qualityScore; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public int getHitCount() { return hitCount; }
    }
    
    public static class CacheStats {
        private final int totalEntries;
        private final int validEntries;
        private final int totalHits;
        private final double avgQuality;
        
        public CacheStats(int totalEntries, int validEntries, int totalHits, double avgQuality) {
            this.totalEntries = totalEntries;
            this.validEntries = validEntries;
            this.totalHits = totalHits;
            this.avgQuality = avgQuality;
        }
        
        // Getters
        public int getTotalEntries() { return totalEntries; }
        public int getValidEntries() { return validEntries; }
        public int getTotalHits() { return totalHits; }
        public double getAvgQuality() { return avgQuality; }
        
        @Override
        public String toString() {
            return String.format("CacheStats{total=%d, valid=%d, hits=%d, avgQuality=%.1f}", 
                totalEntries, validEntries, totalHits, avgQuality);
        }
    }
} 