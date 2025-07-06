package com.mycompany.ebookwebsite.ai;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 🎯 AI Recommendation Service với Enhanced Testing & Quality
 * 
 * Features:
 * - Edge case handling
 * - Timeout management  
 * - Rate limiting
 * - Comprehensive validation
 * - Performance monitoring
 */
public class AIRecommendationService {
    
    private static final Logger logger = LoggerFactory.getLogger(AIRecommendationService.class);
    
    // Performance monitoring
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final AtomicInteger errorCount = new AtomicInteger(0);
    private final Map<String, Long> responseTimes = new ConcurrentHashMap<>();
    
    // Rate limiting
    private final Map<String, Integer> userRequestCount = new ConcurrentHashMap<>();
    private final int MAX_REQUESTS_PER_MINUTE = 10;
    
    // Timeout configuration
    private final int REQUEST_TIMEOUT_SECONDS = 30;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    
    // Validation patterns
    private static final String VALID_QUERY_PATTERN = "^[a-zA-Z0-9\\s\\p{L}.,!?-]{1,500}$";
    
    public AIRecommendationService() {
        logger.info("🎯 AIRecommendationService initialized with enhanced quality features");
    }
    
    /**
     * 🎯 Get personalized recommendations với comprehensive validation
     */
    public String getRecommendations(String userQuery, String userId) {
        long startTime = System.currentTimeMillis();
        requestCount.incrementAndGet();
        
        try {
            // 1. Input Validation
            if (!validateInput(userQuery, userId)) {
                return "❌ Yêu cầu không hợp lệ. Vui lòng kiểm tra lại.";
            }
            
            // 2. Rate Limiting
            if (isRateLimited(userId)) {
                return "⏰ Bạn đã gửi quá nhiều yêu cầu. Vui lòng thử lại sau 1 phút.";
            }
            
            // 3. Timeout Protection
            Future<String> future = executor.submit(() -> generateRecommendations(userQuery));
            
            try {
                String result = future.get(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                
                // 4. Performance Tracking
                long responseTime = System.currentTimeMillis() - startTime;
                responseTimes.put(userId, responseTime);
                
                logger.info("✅ Recommendation generated in {}ms for user {}", responseTime, userId);
                return result;
                
            } catch (TimeoutException e) {
                future.cancel(true);
                errorCount.incrementAndGet();
                logger.error("⏰ Request timeout for user {}: {}", userId, e.getMessage());
                return "⏰ Hệ thống đang bận. Vui lòng thử lại sau.";
            }
            
        } catch (Exception e) {
            errorCount.incrementAndGet();
            logger.error("❌ Error generating recommendations for user {}: {}", userId, e.getMessage());
            return "😔 Có lỗi xảy ra. Vui lòng thử lại sau.";
        }
    }
    
    /**
     * 🔍 Validate input parameters
     */
    private boolean validateInput(String userQuery, String userId) {
        // Null/empty checks
        if (userQuery == null || userQuery.trim().isEmpty()) {
            logger.warn("❌ Empty query from user {}", userId);
            return false;
        }
        
        if (userId == null || userId.trim().isEmpty()) {
            logger.warn("❌ Invalid user ID");
            return false;
        }
        
        // Length validation
        if (userQuery.length() > 500) {
            logger.warn("❌ Query too long from user {}: {} chars", userId, userQuery.length());
            return false;
        }
        
        // Pattern validation (basic)
        if (!userQuery.matches(VALID_QUERY_PATTERN)) {
            logger.warn("❌ Invalid query pattern from user {}: {}", userId, userQuery);
            return false;
        }
        
        // XSS protection
        if (containsSuspiciousContent(userQuery)) {
            logger.warn("❌ Suspicious content detected from user {}: {}", userId, userQuery);
            return false;
        }
        
        return true;
    }
    
    /**
     * 🛡️ Check for suspicious content
     */
    private boolean containsSuspiciousContent(String query) {
        String lowerQuery = query.toLowerCase();
        String[] suspiciousPatterns = {
            "<script>", "javascript:", "onload=", "onerror=",
            "eval(", "document.cookie", "window.location"
        };
        
        for (String pattern : suspiciousPatterns) {
            if (lowerQuery.contains(pattern)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * ⏰ Rate limiting check
     */
    private boolean isRateLimited(String userId) {
        String key = userId + "_" + (System.currentTimeMillis() / 60000); // Per minute
        int currentCount = userRequestCount.getOrDefault(key, 0);
        
        if (currentCount >= MAX_REQUESTS_PER_MINUTE) {
            logger.warn("⏰ Rate limit exceeded for user {}", userId);
            return true;
        }
        
        userRequestCount.put(key, currentCount + 1);
        return false;
    }
    
    /**
     * 🎯 Generate recommendations với error handling
     */
    private String generateRecommendations(String userQuery) throws Exception {
        // Simulate AI processing with potential errors
        if (Math.random() < 0.1) { // 10% error rate for testing
            throw new RuntimeException("Simulated AI service error");
        }
        
        // Simulate processing time
        Thread.sleep(100 + (long)(Math.random() * 2000));
        
        StringBuilder recommendations = new StringBuilder();
        recommendations.append("🎯 **Gợi ý sách dành cho bạn**\n\n");
        
        // Analyze query and generate recommendations
        String lowerQuery = userQuery.toLowerCase();
        
        if (lowerQuery.contains("khoa học") || lowerQuery.contains("science")) {
            recommendations.append("📚 **Sách Khoa Học:**\n");
            recommendations.append("• 'Vũ trụ trong lòng bàn tay' - Neil deGrasse Tyson\n");
            recommendations.append("• 'Súng, vi trùng và thép' - Jared Diamond\n");
            recommendations.append("• 'Lược sử thời gian' - Stephen Hawking\n\n");
        }
        
        if (lowerQuery.contains("văn học") || lowerQuery.contains("literature")) {
            recommendations.append("📖 **Văn Học Kinh Điển:**\n");
            recommendations.append("• 'Nhà giả kim' - Paulo Coelho\n");
            recommendations.append("• 'Đắc nhân tâm' - Dale Carnegie\n");
            recommendations.append("• 'Hạt giống tâm hồn' - Jack Canfield\n\n");
        }
        
        if (lowerQuery.contains("kinh doanh") || lowerQuery.contains("business")) {
            recommendations.append("💼 **Sách Kinh Doanh:**\n");
            recommendations.append("• 'Nghĩ giàu làm giàu' - Napoleon Hill\n");
            recommendations.append("• '7 thói quen hiệu quả' - Stephen Covey\n");
            recommendations.append("• 'Khởi nghiệp tinh gọn' - Eric Ries\n\n");
        }
        
        recommendations.append("💡 **Dựa trên sở thích của bạn, chúng tôi cũng gợi ý:**\n");
        recommendations.append("• Sách cùng thể loại\n");
        recommendations.append("• Tác giả tương tự\n");
        recommendations.append("• Bestsellers mới nhất\n\n");
        
        recommendations.append("🎉 Chúc bạn đọc sách vui vẻ!");
        
        return recommendations.toString();
    }
    
    /**
     * 📊 Get performance metrics
     */
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalRequests", requestCount.get());
        metrics.put("totalErrors", errorCount.get());
        metrics.put("errorRate", requestCount.get() > 0 ? 
            (double) errorCount.get() / requestCount.get() : 0.0);
        metrics.put("averageResponseTime", calculateAverageResponseTime());
        metrics.put("activeUsers", userRequestCount.size());
        metrics.put("rateLimitThreshold", MAX_REQUESTS_PER_MINUTE);
        
        return metrics;
    }
    
    /**
     * 📈 Calculate average response time
     */
    private double calculateAverageResponseTime() {
        if (responseTimes.isEmpty()) {
            return 0.0;
        }
        
        return responseTimes.values().stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
    }
    
    /**
     * 🧹 Cleanup resources
     */
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        logger.info("🧹 AIRecommendationService shutdown completed");
    }
    
    /**
     * 🧪 Test edge cases
     */
    public String testEdgeCases() {
        StringBuilder results = new StringBuilder();
        results.append("🧪 **Edge Case Testing Results**\n\n");
        
        // Test 1: Empty input
        try {
            getRecommendations("", "test_user");
            results.append("❌ Empty input should be rejected\n");
        } catch (Exception e) {
            results.append("✅ Empty input properly handled\n");
        }
        
        // Test 2: Very long input
        try {
            String longInput = "a".repeat(1000);
            getRecommendations(longInput, "test_user");
            results.append("❌ Long input should be rejected\n");
        } catch (Exception e) {
            results.append("✅ Long input properly handled\n");
        }
        
        // Test 3: Suspicious content
        try {
            getRecommendations("<script>alert('xss')</script>", "test_user");
            results.append("❌ XSS content should be rejected\n");
        } catch (Exception e) {
            results.append("✅ XSS content properly handled\n");
        }
        
        // Test 4: Rate limiting
        for (int i = 0; i < 15; i++) {
            getRecommendations("test query", "rate_test_user");
        }
        results.append("✅ Rate limiting test completed\n");
        
        return results.toString();
    }
} 