package com.mycompany.ebookwebsite.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 🚀 Performance Optimizer
 * 
 * Tối ưu hóa performance cho AI Chat System
 */
public class PerformanceOptimizer {
    
    private final Map<String, MetricData> metrics;
    private final AtomicInteger requestCount;
    private final AtomicLong totalResponseTime;
    private final AtomicInteger errorCount;
    private final ScheduledExecutorService scheduler;
    
    // Circuit breaker pattern
    private final Map<String, CircuitBreaker> circuitBreakers;
    
    // Rate limiting
    private final Map<String, RateLimiter> rateLimiters;
    
    public PerformanceOptimizer() {
        this.metrics = new ConcurrentHashMap<>();
        this.requestCount = new AtomicInteger(0);
        this.totalResponseTime = new AtomicLong(0);
        this.errorCount = new AtomicInteger(0);
        this.scheduler = Executors.newScheduledThreadPool(3);
        this.circuitBreakers = new ConcurrentHashMap<>();
        this.rateLimiters = new ConcurrentHashMap<>();
        
        // Initialize monitoring
        startMetricsCollection();
        
        System.out.println("🚀 Performance Optimizer initialized");
    }
    
    /**
     * 📊 Track request performance
     */
    public void trackRequest(String operation, long responseTimeMs, boolean success) {
        requestCount.incrementAndGet();
        totalResponseTime.addAndGet(responseTimeMs);
        
        if (!success) {
            errorCount.incrementAndGet();
        }
        
        // Update operation-specific metrics
        MetricData metric = metrics.computeIfAbsent(operation, k -> new MetricData());
        metric.addData(responseTimeMs, success);
        
        System.out.println("📊 Tracked: " + operation + " (" + responseTimeMs + "ms, " + (success ? "✅" : "❌") + ")");
    }
    
    /**
     * 🔄 Execute with circuit breaker protection
     */
    public <T> CompletableFuture<T> executeWithCircuitBreaker(String operation, 
                                                               java.util.function.Supplier<T> task) {
        CircuitBreaker breaker = circuitBreakers.computeIfAbsent(operation, k -> new CircuitBreaker());
        
        if (breaker.isOpen()) {
            System.out.println("🚫 Circuit breaker OPEN for: " + operation);
            return CompletableFuture.failedFuture(new RuntimeException("Circuit breaker open"));
        }
        
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            try {
                T result = task.get();
                long responseTime = System.currentTimeMillis() - startTime;
                
                breaker.recordSuccess();
                trackRequest(operation, responseTime, true);
                
                return result;
            } catch (Exception e) {
                long responseTime = System.currentTimeMillis() - startTime;
                
                breaker.recordFailure();
                trackRequest(operation, responseTime, false);
                
                throw new RuntimeException(e);
            }
        });
    }
    
    /**
     * 🛡️ Check rate limiting
     */
    public boolean checkRateLimit(String identifier, int maxRequests, int windowMinutes) {
        RateLimiter limiter = rateLimiters.computeIfAbsent(identifier, 
            k -> new RateLimiter(maxRequests, windowMinutes));
        
        return limiter.allowRequest();
    }
    
    /**
     * 📈 Get performance metrics
     */
    public PerformanceMetrics getMetrics() {
        double avgResponseTime = requestCount.get() > 0 ? 
            (double) totalResponseTime.get() / requestCount.get() : 0.0;
        
        double errorRate = requestCount.get() > 0 ? 
            (double) errorCount.get() / requestCount.get() * 100 : 0.0;
        
        return new PerformanceMetrics(
            requestCount.get(),
            avgResponseTime,
            errorRate,
            getSlowOperations(),
            getCircuitBreakerStatus()
        );
    }
    
    /**
     * 🎯 Get optimization recommendations
     */
    public List<String> getOptimizationRecommendations() {
        List<String> recommendations = new java.util.ArrayList<>();
        
        PerformanceMetrics metrics = getMetrics();
        
        // Response time recommendations
        if (metrics.getAvgResponseTime() > 5000) {
            recommendations.add("⚠️ Thời gian phản hồi trung bình cao (" + 
                String.format("%.1f", metrics.getAvgResponseTime()) + "ms). Cần tối ưu hóa.");
        }
        
        // Error rate recommendations
        if (metrics.getErrorRate() > 5.0) {
            recommendations.add("❌ Tỷ lệ lỗi cao (" + 
                String.format("%.1f", metrics.getErrorRate()) + "%). Cần kiểm tra và sửa lỗi.");
        }
        
        // Circuit breaker recommendations
        if (hasOpenCircuits()) {
            recommendations.add("🚫 Có circuit breaker đang mở. Cần kiểm tra dịch vụ.");
        }
        
        // Memory recommendations
        if (shouldOptimizeMemory()) {
            recommendations.add("💾 Sử dụng bộ nhớ cao. Cần dọn dẹp cache.");
        }
        
        return recommendations;
    }
    
    /**
     * 🧹 Optimize system performance
     */
    public void optimizePerformance() {
        System.out.println("🧹 Starting performance optimization...");
        
        // Clean old metrics
        cleanOldMetrics();
        
        // Reset circuit breakers if needed
        resetCircuitBreakers();
        
        // Clean rate limiters
        cleanRateLimiters();
        
        // Suggest GC if needed
        if (shouldRunGC()) {
            System.gc();
            System.out.println("🗑️ Suggested garbage collection");
        }
        
        System.out.println("✅ Performance optimization completed");
    }
    
    /**
     * 🔍 Get detailed operation metrics
     */
    public Map<String, OperationMetrics> getOperationMetrics() {
        Map<String, OperationMetrics> result = new ConcurrentHashMap<>();
        
        for (Map.Entry<String, MetricData> entry : metrics.entrySet()) {
            MetricData data = entry.getValue();
            result.put(entry.getKey(), new OperationMetrics(
                data.getRequestCount(),
                data.getAverageResponseTime(),
                data.getErrorRate(),
                data.getP95ResponseTime()
            ));
        }
        
        return result;
    }
    
    // Helper methods
    private void startMetricsCollection() {
        // Collect metrics every minute
        scheduler.scheduleAtFixedRate(() -> {
            collectSystemMetrics();
        }, 0, 1, TimeUnit.MINUTES);
        
        // Cleanup every hour
        scheduler.scheduleAtFixedRate(() -> {
            optimizePerformance();
        }, 0, 1, TimeUnit.HOURS);
    }
    
    private void collectSystemMetrics() {
        // Collect system-wide metrics
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        System.out.println("📊 System metrics - Memory: " + (usedMemory / 1024 / 1024) + "MB / " + 
            (totalMemory / 1024 / 1024) + "MB");
    }
    
    private List<String> getSlowOperations() {
        return metrics.entrySet().stream()
            .filter(entry -> entry.getValue().getAverageResponseTime() > 3000)
            .map(entry -> entry.getKey() + " (" + 
                String.format("%.1f", entry.getValue().getAverageResponseTime()) + "ms)")
            .collect(java.util.stream.Collectors.toList());
    }
    
    private Map<String, String> getCircuitBreakerStatus() {
        Map<String, String> status = new ConcurrentHashMap<>();
        
        for (Map.Entry<String, CircuitBreaker> entry : circuitBreakers.entrySet()) {
            CircuitBreaker breaker = entry.getValue();
            status.put(entry.getKey(), breaker.isOpen() ? "OPEN" : "CLOSED");
        }
        
        return status;
    }
    
    private boolean hasOpenCircuits() {
        return circuitBreakers.values().stream().anyMatch(CircuitBreaker::isOpen);
    }
    
    private boolean shouldOptimizeMemory() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        
        return (double) (totalMemory - freeMemory) / totalMemory > 0.85; // 85% memory usage
    }
    
    private void cleanOldMetrics() {
        LocalDateTime cutoff = LocalDateTime.now().minus(24, ChronoUnit.HOURS);
        
        for (MetricData data : metrics.values()) {
            data.cleanOldData(cutoff);
        }
    }
    
    private void resetCircuitBreakers() {
        for (CircuitBreaker breaker : circuitBreakers.values()) {
            breaker.tryReset();
        }
    }
    
    private void cleanRateLimiters() {
        rateLimiters.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    
    private boolean shouldRunGC() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        
        return (double) (totalMemory - freeMemory) / totalMemory > 0.90; // 90% memory usage
    }
    
    // Inner classes
    public static class MetricData {
        private final AtomicInteger requestCount = new AtomicInteger(0);
        private final AtomicLong totalResponseTime = new AtomicLong(0);
        private final AtomicInteger errorCount = new AtomicInteger(0);
        private final List<Long> responseTimes = new java.util.concurrent.CopyOnWriteArrayList<>();
        private final List<LocalDateTime> timestamps = new java.util.concurrent.CopyOnWriteArrayList<>();
        
        public void addData(long responseTime, boolean success) {
            requestCount.incrementAndGet();
            totalResponseTime.addAndGet(responseTime);
            
            if (!success) {
                errorCount.incrementAndGet();
            }
            
            responseTimes.add(responseTime);
            timestamps.add(LocalDateTime.now());
            
            // Keep only last 1000 entries
            if (responseTimes.size() > 1000) {
                responseTimes.remove(0);
                timestamps.remove(0);
            }
        }
        
        public void cleanOldData(LocalDateTime cutoff) {
            for (int i = timestamps.size() - 1; i >= 0; i--) {
                if (timestamps.get(i).isBefore(cutoff)) {
                    timestamps.remove(i);
                    responseTimes.remove(i);
                }
            }
        }
        
        public int getRequestCount() { return requestCount.get(); }
        public double getAverageResponseTime() { 
            return requestCount.get() > 0 ? (double) totalResponseTime.get() / requestCount.get() : 0.0; 
        }
        public double getErrorRate() { 
            return requestCount.get() > 0 ? (double) errorCount.get() / requestCount.get() * 100 : 0.0; 
        }
        public double getP95ResponseTime() {
            if (responseTimes.isEmpty()) return 0.0;
            
            List<Long> sorted = responseTimes.stream()
                .sorted()
                .collect(java.util.stream.Collectors.toList());
            
            int p95Index = (int) (sorted.size() * 0.95);
            return sorted.get(Math.min(p95Index, sorted.size() - 1));
        }
    }
    
    public static class CircuitBreaker {
        private final AtomicInteger failureCount = new AtomicInteger(0);
        private final int threshold = 5;
        private volatile LocalDateTime lastFailureTime;
        private volatile boolean isOpen = false;
        
        public void recordSuccess() {
            failureCount.set(0);
            isOpen = false;
        }
        
        public void recordFailure() {
            int failures = failureCount.incrementAndGet();
            lastFailureTime = LocalDateTime.now();
            
            if (failures >= threshold) {
                isOpen = true;
            }
        }
        
        public boolean isOpen() {
            return isOpen;
        }
        
        public void tryReset() {
            if (isOpen && lastFailureTime != null) {
                LocalDateTime resetTime = lastFailureTime.plus(5, ChronoUnit.MINUTES);
                if (LocalDateTime.now().isAfter(resetTime)) {
                    isOpen = false;
                    failureCount.set(0);
                }
            }
        }
    }
    
    public static class RateLimiter {
        private final int maxRequests;
        private final int windowMinutes;
        private final List<LocalDateTime> requests = new java.util.concurrent.CopyOnWriteArrayList<>();
        
        public RateLimiter(int maxRequests, int windowMinutes) {
            this.maxRequests = maxRequests;
            this.windowMinutes = windowMinutes;
        }
        
        public boolean allowRequest() {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime windowStart = now.minus(windowMinutes, ChronoUnit.MINUTES);
            
            // Clean old requests
            requests.removeIf(time -> time.isBefore(windowStart));
            
            if (requests.size() < maxRequests) {
                requests.add(now);
                return true;
            }
            
            return false;
        }
        
        public boolean isExpired() {
            return requests.isEmpty() || 
                   requests.stream().allMatch(time -> time.isBefore(LocalDateTime.now().minus(windowMinutes * 2, ChronoUnit.MINUTES)));
        }
    }
    
    public static class PerformanceMetrics {
        private final int totalRequests;
        private final double avgResponseTime;
        private final double errorRate;
        private final List<String> slowOperations;
        private final Map<String, String> circuitBreakerStatus;
        
        public PerformanceMetrics(int totalRequests, double avgResponseTime, double errorRate, 
                                 List<String> slowOperations, Map<String, String> circuitBreakerStatus) {
            this.totalRequests = totalRequests;
            this.avgResponseTime = avgResponseTime;
            this.errorRate = errorRate;
            this.slowOperations = slowOperations;
            this.circuitBreakerStatus = circuitBreakerStatus;
        }
        
        // Getters
        public int getTotalRequests() { return totalRequests; }
        public double getAvgResponseTime() { return avgResponseTime; }
        public double getErrorRate() { return errorRate; }
        public List<String> getSlowOperations() { return slowOperations; }
        public Map<String, String> getCircuitBreakerStatus() { return circuitBreakerStatus; }
    }
    
    public static class OperationMetrics {
        private final int requestCount;
        private final double avgResponseTime;
        private final double errorRate;
        private final double p95ResponseTime;
        
        public OperationMetrics(int requestCount, double avgResponseTime, double errorRate, double p95ResponseTime) {
            this.requestCount = requestCount;
            this.avgResponseTime = avgResponseTime;
            this.errorRate = errorRate;
            this.p95ResponseTime = p95ResponseTime;
        }
        
        // Getters
        public int getRequestCount() { return requestCount; }
        public double getAvgResponseTime() { return avgResponseTime; }
        public double getErrorRate() { return errorRate; }
        public double getP95ResponseTime() { return p95ResponseTime; }
    }
    
    // Shutdown method
    public void shutdown() {
        scheduler.shutdown();
        System.out.println("🛑 Performance Optimizer shutdown completed");
    }
} 