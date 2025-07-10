package com.mycompany.ebookwebsite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 📊 AI Monitoring & Metrics Service
 * 
 * Comprehensive monitoring system for all AI services:
 * - Performance tracking
 * - Error rate monitoring  
 * - API usage statistics
 * - Real-time alerts
 * - Historical data analysis
 */
public class AIMonitoringService {
    
    private static final Logger logger = LoggerFactory.getLogger(AIMonitoringService.class);
    
    // Performance metrics
    private final Map<String, AtomicLong> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> errorCounts = new ConcurrentHashMap<>();
    private final Map<String, List<Long>> responseTimes = new ConcurrentHashMap<>();
    
    // Alert thresholds
    private final double ERROR_RATE_THRESHOLD = 0.1; // 10%
    private final long RESPONSE_TIME_THRESHOLD = 5000; // 5 seconds
    private final int MAX_REQUESTS_PER_MINUTE = 100;
    
    // Alert system
    private final List<String> alerts = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService alertScheduler = Executors.newScheduledThreadPool(1);
    
    // Historical data
    private final Map<String, Queue<ServiceMetrics>> historicalData = new ConcurrentHashMap<>();
    private final int MAX_HISTORY_SIZE = 1000;
    
    public AIMonitoringService() {
        logger.info("📊 AIMonitoringService initialized");
        startAlertMonitoring();
    }
    
    /**
     * 📈 Record service performance
     */
    public void recordServiceCall(String serviceName, long responseTime, boolean success) {
        // Update request count
        requestCounts.computeIfAbsent(serviceName, k -> new AtomicLong(0)).incrementAndGet();
        
        // Update error count
        if (!success) {
            errorCounts.computeIfAbsent(serviceName, k -> new AtomicLong(0)).incrementAndGet();
        }
        
        // Record response time
        responseTimes.computeIfAbsent(serviceName, k -> new CopyOnWriteArrayList<>())
                    .add(responseTime);
        
        // Keep only last 100 response times
        List<Long> times = responseTimes.get(serviceName);
        if (times.size() > 100) {
            times.subList(0, times.size() - 100).clear();
        }
        
        // Check for alerts
        checkAlerts(serviceName, responseTime, success);
        
        // Store historical data
        storeHistoricalData(serviceName, responseTime, success);
    }
    
    /**
     * 🚨 Check for performance alerts
     */
    private void checkAlerts(String serviceName, long responseTime, boolean success) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        // High error rate alert
        double errorRate = getErrorRate(serviceName);
        if (errorRate > ERROR_RATE_THRESHOLD) {
            String alert = String.format("🚨 [%s] High error rate for %s: %.2f%%", 
                timestamp, serviceName, errorRate * 100);
            alerts.add(alert);
            logger.warn(alert);
        }
        
        // Slow response time alert
        if (responseTime > RESPONSE_TIME_THRESHOLD) {
            String alert = String.format("🐌 [%s] Slow response for %s: %dms", 
                timestamp, serviceName, responseTime);
            alerts.add(alert);
            logger.warn(alert);
        }
        
        // High request volume alert
        long requestCount = getRequestCount(serviceName);
        if (requestCount > MAX_REQUESTS_PER_MINUTE) {
            String alert = String.format("📈 [%s] High request volume for %s: %d requests", 
                timestamp, serviceName, requestCount);
            alerts.add(alert);
            logger.warn(alert);
        }
    }
    
    /**
     * 📊 Store historical metrics
     */
    private void storeHistoricalData(String serviceName, long responseTime, boolean success) {
        ServiceMetrics metrics = new ServiceMetrics(
            serviceName,
            LocalDateTime.now(),
            responseTime,
            success
        );
        
        historicalData.computeIfAbsent(serviceName, k -> new ConcurrentLinkedQueue<>())
                     .add(metrics);
        
        // Keep only recent data
        Queue<ServiceMetrics> queue = historicalData.get(serviceName);
        while (queue.size() > MAX_HISTORY_SIZE) {
            queue.poll();
        }
    }
    
    /**
     * 📈 Get comprehensive metrics report
     */
    public Map<String, Object> getComprehensiveMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // Overall statistics
        metrics.put("totalServices", requestCounts.size());
        metrics.put("totalRequests", getTotalRequests());
        metrics.put("totalErrors", getTotalErrors());
        metrics.put("overallErrorRate", getOverallErrorRate());
        metrics.put("averageResponseTime", getAverageResponseTime());
        
        // Per-service metrics
        Map<String, Map<String, Object>> serviceMetrics = new HashMap<>();
        for (String serviceName : requestCounts.keySet()) {
            Map<String, Object> serviceData = new HashMap<>();
            serviceData.put("requests", getRequestCount(serviceName));
            serviceData.put("errors", getErrorCount(serviceName));
            serviceData.put("errorRate", getErrorRate(serviceName));
            serviceData.put("avgResponseTime", getAverageResponseTime(serviceName));
            serviceData.put("minResponseTime", getMinResponseTime(serviceName));
            serviceData.put("maxResponseTime", getMaxResponseTime(serviceName));
            
            serviceMetrics.put(serviceName, serviceData);
        }
        metrics.put("services", serviceMetrics);
        
        // Recent alerts
        metrics.put("recentAlerts", getRecentAlerts(10));
        
        // Performance trends
        metrics.put("performanceTrends", getPerformanceTrends());
        
        return metrics;
    }
    
    /**
     * 📊 Get detailed service report
     */
    public String getDetailedReport() {
        StringBuilder report = new StringBuilder();
        report.append("📊 **AI Services Performance Report**\n");
        report.append("Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");
        
        // Overall statistics
        report.append("🎯 **Overall Statistics:**\n");
        report.append("• Total Services: ").append(requestCounts.size()).append("\n");
        report.append("• Total Requests: ").append(getTotalRequests()).append("\n");
        report.append("• Total Errors: ").append(getTotalErrors()).append("\n");
        report.append("• Error Rate: ").append(String.format("%.2f%%", getOverallErrorRate() * 100)).append("\n");
        report.append("• Avg Response Time: ").append(String.format("%.0fms", getAverageResponseTime())).append("\n\n");
        
        // Per-service breakdown
        report.append("🔍 **Per-Service Breakdown:**\n");
        for (String serviceName : requestCounts.keySet()) {
            report.append("📋 **").append(serviceName).append(":**\n");
            report.append("  • Requests: ").append(getRequestCount(serviceName)).append("\n");
            report.append("  • Errors: ").append(getErrorCount(serviceName)).append("\n");
            report.append("  • Error Rate: ").append(String.format("%.2f%%", getErrorRate(serviceName) * 100)).append("\n");
            report.append("  • Avg Response: ").append(String.format("%.0fms", getAverageResponseTime(serviceName))).append("\n");
            report.append("  • Min/Max: ").append(getMinResponseTime(serviceName)).append("ms / ")
                  .append(getMaxResponseTime(serviceName)).append("ms\n\n");
        }
        
        // Recent alerts
        report.append("🚨 **Recent Alerts:**\n");
        List<String> recentAlerts = getRecentAlerts(5);
        if (recentAlerts.isEmpty()) {
            report.append("✅ No recent alerts\n");
        } else {
            for (String alert : recentAlerts) {
                report.append("• ").append(alert).append("\n");
            }
        }
        
        return report.toString();
    }
    
    /**
     * 🚨 Get recent alerts
     */
    public List<String> getRecentAlerts(int count) {
        int size = Math.min(count, alerts.size());
        return alerts.subList(Math.max(0, alerts.size() - size), alerts.size());
    }
    
    /**
     * 📈 Get performance trends
     */
    public Map<String, Object> getPerformanceTrends() {
        Map<String, Object> trends = new HashMap<>();
        
        // Calculate trends for each service
        for (String serviceName : historicalData.keySet()) {
            Queue<ServiceMetrics> history = historicalData.get(serviceName);
            if (history.size() < 10) continue;
            
            // Calculate moving average
            double recentAvg = history.stream()
                .skip(Math.max(0, history.size() - 10))
                .mapToLong(ServiceMetrics::getResponseTime)
                .average()
                .orElse(0.0);
            
            double olderAvg = history.stream()
                .limit(Math.max(0, history.size() - 10))
                .mapToLong(ServiceMetrics::getResponseTime)
                .average()
                .orElse(0.0);
            
            double trend = olderAvg > 0 ? (recentAvg - olderAvg) / olderAvg : 0.0;
            
            trends.put(serviceName + "_trend", trend);
        }
        
        return trends;
    }
    
    /**
     * 🔄 Start alert monitoring
     */
    private void startAlertMonitoring() {
        alertScheduler.scheduleAtFixedRate(() -> {
            try {
                // Clean old alerts (keep only last 100)
                if (alerts.size() > 100) {
                    alerts.subList(0, alerts.size() - 100).clear();
                }
                
                // Log summary every 5 minutes
                logger.info("📊 Monitoring Summary - Services: {}, Total Requests: {}, Error Rate: {:.2f}%",
                    requestCounts.size(), getTotalRequests(), getOverallErrorRate() * 100);
                    
            } catch (Exception e) {
                logger.error("❌ Error in alert monitoring: {}", e.getMessage());
            }
        }, 5, 5, TimeUnit.MINUTES);
    }
    
    // Helper methods for metrics calculation
    private long getTotalRequests() {
        return requestCounts.values().stream().mapToLong(AtomicLong::get).sum();
    }
    
    private long getTotalErrors() {
        return errorCounts.values().stream().mapToLong(AtomicLong::get).sum();
    }
    
    private double getOverallErrorRate() {
        long totalRequests = getTotalRequests();
        return totalRequests > 0 ? (double) getTotalErrors() / totalRequests : 0.0;
    }
    
    private double getAverageResponseTime() {
        return responseTimes.values().stream()
            .flatMapToLong(times -> times.stream().mapToLong(Long::longValue))
            .average()
            .orElse(0.0);
    }
    
    private long getRequestCount(String serviceName) {
        return requestCounts.getOrDefault(serviceName, new AtomicLong(0)).get();
    }
    
    private long getErrorCount(String serviceName) {
        return errorCounts.getOrDefault(serviceName, new AtomicLong(0)).get();
    }
    
    private double getErrorRate(String serviceName) {
        long requests = getRequestCount(serviceName);
        return requests > 0 ? (double) getErrorCount(serviceName) / requests : 0.0;
    }
    
    private double getAverageResponseTime(String serviceName) {
        List<Long> times = responseTimes.get(serviceName);
        return times != null ? times.stream().mapToLong(Long::longValue).average().orElse(0.0) : 0.0;
    }
    
    private long getMinResponseTime(String serviceName) {
        List<Long> times = responseTimes.get(serviceName);
        return times != null ? times.stream().mapToLong(Long::longValue).min().orElse(0) : 0;
    }
    
    private long getMaxResponseTime(String serviceName) {
        List<Long> times = responseTimes.get(serviceName);
        return times != null ? times.stream().mapToLong(Long::longValue).max().orElse(0) : 0;
    }
    
    /**
     * 🧹 Cleanup resources
     */
    public void shutdown() {
        alertScheduler.shutdown();
        try {
            if (!alertScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                alertScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            alertScheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        logger.info("🧹 AIMonitoringService shutdown completed");
    }
    
    /**
     * 📊 Service metrics data class
     */
    private static class ServiceMetrics {
        private final String serviceName;
        private final LocalDateTime timestamp;
        private final long responseTime;
        private final boolean success;
        
        public ServiceMetrics(String serviceName, LocalDateTime timestamp, long responseTime, boolean success) {
            this.serviceName = serviceName;
            this.timestamp = timestamp;
            this.responseTime = responseTime;
            this.success = success;
        }
        
        public String getServiceName() { return serviceName; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public long getResponseTime() { return responseTime; }
        public boolean isSuccess() { return success; }
    }
} 