package com.mycompany.ebookwebsite.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 🔒 AI Security & Validation Service
 * 
 * Comprehensive security features:
 * - Input sanitization
 * - Rate limiting per user
 * - API key rotation
 * - XSS protection
 * - SQL injection prevention
 * - Request validation
 * - Security monitoring
 */
public class AISecurityService {
    
    private static final Logger logger = LoggerFactory.getLogger(AISecurityService.class);
    
    // Rate limiting
    private final Map<String, UserRateLimit> userRateLimits = new ConcurrentHashMap<>();
    private final int MAX_REQUESTS_PER_MINUTE = 20;
    private final int MAX_REQUESTS_PER_HOUR = 100;
    
    // API key management
    private final Map<String, APIKeyInfo> apiKeys = new ConcurrentHashMap<>();
    private final String DEFAULT_API_KEY = "demo-key-2024";
    
    // Security patterns
    private static final Pattern XSS_PATTERN = Pattern.compile(
        "<script[^>]*>.*?</script>|javascript:|on\\w+\\s*=|<iframe|<object|<embed",
        Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );
    
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        "(?i)(union|select|insert|update|delete|drop|create|alter|exec|execute|script|javascript)",
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern VALID_INPUT_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9\\s\\p{L}.,!?@#$%&*()\\-_+=:;\"'<>/\\[\\]{}|\\\\]{1,1000}$"
    );
    
    // Security monitoring
    private final List<SecurityEvent> securityEvents = new ArrayList<>();
    private final int MAX_SECURITY_EVENTS = 1000;
    
    public AISecurityService() {
        logger.info("🔒 AISecurityService initialized with comprehensive security features");
        initializeDefaultAPIKey();
    }
    
    /**
     * 🔐 Validate and sanitize user input
     */
    public ValidationResult validateInput(String input, String userId, String apiKey) {
        ValidationResult result = new ValidationResult();
        
        try {
            // 1. API Key validation
            if (!validateAPIKey(apiKey)) {
                result.setValid(false);
                result.setError("❌ Invalid API key");
                logSecurityEvent("INVALID_API_KEY", userId, "Invalid API key provided");
                return result;
            }
            
            // 2. Rate limiting check
            if (isRateLimited(userId)) {
                result.setValid(false);
                result.setError("⏰ Rate limit exceeded. Please try again later.");
                logSecurityEvent("RATE_LIMIT_EXCEEDED", userId, "User exceeded rate limit");
                return result;
            }
            
            // 3. Input sanitization
            String sanitizedInput = sanitizeInput(input);
            if (sanitizedInput == null) {
                result.setValid(false);
                result.setError("❌ Input contains suspicious content");
                logSecurityEvent("SUSPICIOUS_INPUT", userId, "Suspicious input detected: " + input);
                return result;
            }
            
            // 4. Length validation
            if (sanitizedInput.length() > 1000) {
                result.setValid(false);
                result.setError("❌ Input too long (max 1000 characters)");
                logSecurityEvent("INPUT_TOO_LONG", userId, "Input length: " + sanitizedInput.length());
                return result;
            }
            
            // 5. Pattern validation
            if (!VALID_INPUT_PATTERN.matcher(sanitizedInput).matches()) {
                result.setValid(false);
                result.setError("❌ Input contains invalid characters");
                logSecurityEvent("INVALID_CHARACTERS", userId, "Invalid characters in input");
                return result;
            }
            
            // 6. Update rate limiting
            updateRateLimit(userId);
            
            result.setValid(true);
            result.setSanitizedInput(sanitizedInput);
            result.setUserId(userId);
            
            logger.info("✅ Input validation successful for user {}", userId);
            
        } catch (Exception e) {
            logger.error("❌ Error during input validation: {}", e.getMessage());
            result.setValid(false);
            result.setError("❌ Internal validation error");
            logSecurityEvent("VALIDATION_ERROR", userId, "Validation error: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 🛡️ Sanitize input to prevent attacks
     */
    private String sanitizeInput(String input) {
        if (input == null) return null;
        
        // Check for XSS
        if (XSS_PATTERN.matcher(input).find()) {
            logger.warn("🚨 XSS attack attempt detected");
            return null;
        }
        
        // Check for SQL injection
        if (SQL_INJECTION_PATTERN.matcher(input).find()) {
            logger.warn("🚨 SQL injection attempt detected");
            return null;
        }
        
        // Basic HTML encoding
        String sanitized = input
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
            .replace("&", "&amp;");
        
        // Remove null bytes
        sanitized = sanitized.replace("\0", "");
        
        // Trim whitespace
        sanitized = sanitized.trim();
        
        return sanitized;
    }
    
    /**
     * 🔑 Validate API key
     */
    private boolean validateAPIKey(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return false;
        }
        
        // Check if API key exists and is not expired
        APIKeyInfo keyInfo = apiKeys.get(apiKey);
        if (keyInfo == null) {
            // Check default key
            return DEFAULT_API_KEY.equals(apiKey);
        }
        
        // Check if key is expired
        if (keyInfo.isExpired()) {
            logger.warn("🔑 Expired API key: {}", apiKey);
            return false;
        }
        
        return true;
    }
    
    /**
     * ⏰ Check rate limiting
     */
    private boolean isRateLimited(String userId) {
        UserRateLimit rateLimit = userRateLimits.get(userId);
        if (rateLimit == null) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        // Check minute limit
        long minuteRequests = rateLimit.getMinuteRequests().stream()
            .filter(time -> ChronoUnit.MINUTES.between(time, now) < 1)
            .count();
        
        if (minuteRequests >= MAX_REQUESTS_PER_MINUTE) {
            return true;
        }
        
        // Check hour limit
        long hourRequests = rateLimit.getHourRequests().stream()
            .filter(time -> ChronoUnit.HOURS.between(time, now) < 1)
            .count();
        
        return hourRequests >= MAX_REQUESTS_PER_HOUR;
    }
    
    /**
     * 📈 Update rate limiting
     */
    private void updateRateLimit(String userId) {
        UserRateLimit rateLimit = userRateLimits.computeIfAbsent(userId, k -> new UserRateLimit());
        LocalDateTime now = LocalDateTime.now();
        
        rateLimit.getMinuteRequests().add(now);
        rateLimit.getHourRequests().add(now);
        
        // Clean old entries
        rateLimit.getMinuteRequests().removeIf(time -> 
            ChronoUnit.MINUTES.between(time, now) >= 1);
        rateLimit.getHourRequests().removeIf(time -> 
            ChronoUnit.HOURS.between(time, now) >= 1);
    }
    
    /**
     * 🔑 Generate new API key
     */
    public String generateAPIKey(String userId, int expirationDays) {
        try {
            String baseKey = userId + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(baseKey.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            String apiKey = hexString.toString().substring(0, 32);
            
            // Store API key info
            LocalDateTime expiration = LocalDateTime.now().plusDays(expirationDays);
            APIKeyInfo keyInfo = new APIKeyInfo(userId, expiration);
            apiKeys.put(apiKey, keyInfo);
            
            logger.info("🔑 Generated new API key for user {}: {}", userId, apiKey);
            return apiKey;
            
        } catch (NoSuchAlgorithmException e) {
            logger.error("❌ Error generating API key: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 🚨 Log security event
     */
    private void logSecurityEvent(String eventType, String userId, String details) {
        SecurityEvent event = new SecurityEvent(eventType, userId, details, LocalDateTime.now());
        securityEvents.add(event);
        
        // Keep only recent events
        if (securityEvents.size() > MAX_SECURITY_EVENTS) {
            securityEvents.subList(0, securityEvents.size() - MAX_SECURITY_EVENTS).clear();
        }
        
        logger.warn("🚨 Security event: {} - User: {} - Details: {}", eventType, userId, details);
    }
    
    /**
     * 📊 Get security report
     */
    public String getSecurityReport() {
        StringBuilder report = new StringBuilder();
        report.append("🔒 **AI Security Report**\n");
        report.append("Generated: ").append(LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");
        
        // Rate limiting stats
        report.append("⏰ **Rate Limiting Statistics:**\n");
        report.append("• Active users: ").append(userRateLimits.size()).append("\n");
        report.append("• Max requests per minute: ").append(MAX_REQUESTS_PER_MINUTE).append("\n");
        report.append("• Max requests per hour: ").append(MAX_REQUESTS_PER_HOUR).append("\n\n");
        
        // API key stats
        report.append("🔑 **API Key Statistics:**\n");
        report.append("• Total API keys: ").append(apiKeys.size()).append("\n");
        long activeKeys = apiKeys.values().stream().filter(key -> !key.isExpired()).count();
        report.append("• Active API keys: ").append(activeKeys).append("\n\n");
        
        // Security events
        report.append("🚨 **Recent Security Events:**\n");
        List<SecurityEvent> recentEvents = securityEvents.subList(
            Math.max(0, securityEvents.size() - 10), securityEvents.size());
        
        if (recentEvents.isEmpty()) {
            report.append("✅ No recent security events\n");
        } else {
            for (SecurityEvent event : recentEvents) {
                report.append("• [").append(event.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")))
                      .append("] ").append(event.getEventType()).append(" - ").append(event.getDetails()).append("\n");
            }
        }
        
        return report.toString();
    }
    
    /**
     * 🧹 Cleanup expired data
     */
    public void cleanup() {
        // Remove expired API keys
        apiKeys.entrySet().removeIf(entry -> entry.getValue().isExpired());
        
        // Clean old rate limit data
        LocalDateTime now = LocalDateTime.now();
        userRateLimits.values().forEach(rateLimit -> {
            rateLimit.getMinuteRequests().removeIf(time -> 
                ChronoUnit.MINUTES.between(time, now) >= 1);
            rateLimit.getHourRequests().removeIf(time -> 
                ChronoUnit.HOURS.between(time, now) >= 1);
        });
        
        logger.info("🧹 Security service cleanup completed");
    }
    
    /**
     * 🔑 Initialize default API key
     */
    private void initializeDefaultAPIKey() {
        APIKeyInfo defaultKeyInfo = new APIKeyInfo("system", LocalDateTime.now().plusYears(1));
        apiKeys.put(DEFAULT_API_KEY, defaultKeyInfo);
    }
    
    // Data classes
    public static class ValidationResult {
        private boolean valid;
        private String error;
        private String sanitizedInput;
        private String userId;
        
        // Getters and setters
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public String getSanitizedInput() { return sanitizedInput; }
        public void setSanitizedInput(String sanitizedInput) { this.sanitizedInput = sanitizedInput; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
    
    private static class UserRateLimit {
        private final List<LocalDateTime> minuteRequests = new ArrayList<>();
        private final List<LocalDateTime> hourRequests = new ArrayList<>();
        
        public List<LocalDateTime> getMinuteRequests() { return minuteRequests; }
        public List<LocalDateTime> getHourRequests() { return hourRequests; }
    }
    
    private static class APIKeyInfo {
        private final String userId;
        private final LocalDateTime expiration;
        
        public APIKeyInfo(String userId, LocalDateTime expiration) {
            this.userId = userId;
            this.expiration = expiration;
        }
        
        public String getUserId() { return userId; }
        public LocalDateTime getExpiration() { return expiration; }
        public boolean isExpired() { return LocalDateTime.now().isAfter(expiration); }
    }
    
    private static class SecurityEvent {
        private final String eventType;
        private final String userId;
        private final String details;
        private final LocalDateTime timestamp;
        
        public SecurityEvent(String eventType, String userId, String details, LocalDateTime timestamp) {
            this.eventType = eventType;
            this.userId = userId;
            this.details = details;
            this.timestamp = timestamp;
        }
        
        public String getEventType() { return eventType; }
        public String getUserId() { return userId; }
        public String getDetails() { return details; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
} 