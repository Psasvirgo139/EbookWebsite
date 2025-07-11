package com.mycompany.ebookwebsite.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.mycompany.ebookwebsite.model.EnhancedSessionContext;

/**
 * 🧠 Session Manager
 * 
 * Quản lý session với enhanced context và cleanup tự động
 */
public class SessionManager {
    
    private final Map<String, EnhancedSessionContext> sessions;
    private final ScheduledExecutorService scheduler;
    private final int sessionTimeoutMinutes;
    
    public SessionManager() {
        this.sessions = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.sessionTimeoutMinutes = 30;
        
        // Start cleanup scheduler
        startCleanupScheduler();
        
        System.out.println("🧠 Session Manager initialized with " + sessionTimeoutMinutes + " minute timeout");
    }
    
    /**
     * 🔑 Get or create session for user
     */
    public EnhancedSessionContext getOrCreateSession(int userId) {
        String sessionId = generateSessionId(userId);
        
        return sessions.computeIfAbsent(sessionId, id -> {
            EnhancedSessionContext context = new EnhancedSessionContext(sessionId, userId);
            System.out.println("🆕 Created new session: " + sessionId + " for user: " + userId);
            return context;
        });
    }
    
    /**
     * 🔍 Get existing session
     */
    public EnhancedSessionContext getSession(String sessionId) {
        EnhancedSessionContext session = sessions.get(sessionId);
        
        if (session != null && session.isValid()) {
            session.extendSession();
            return session;
        }
        
        return null;
    }
    
    /**
     * 💬 Add conversation to session
     */
    public void addConversation(int userId, String userMessage, String aiResponse, String questionType) {
        EnhancedSessionContext session = getOrCreateSession(userId);
        session.addConversationTurn(userMessage, aiResponse, questionType);
        
        System.out.println("💬 Added conversation to session: " + session.getSessionId());
    }
    
    /**
     * 📚 Update book context in session
     */
    public void updateBookContext(int userId, int bookId, String bookTitle, String action) {
        EnhancedSessionContext session = getOrCreateSession(userId);
        session.updateBookContext(bookId, bookTitle, action);
        
        System.out.println("📚 Updated book context in session: " + session.getSessionId());
    }
    
    /**
     * 🔍 Add keywords to session
     */
    public void addKeywords(int userId, List<String> keywords, boolean isUserInterest) {
        EnhancedSessionContext session = getOrCreateSession(userId);
        session.addKeywords(keywords, isUserInterest);
        
        System.out.println("🔍 Added keywords to session: " + session.getSessionId());
    }
    
    /**
     * 🎯 Get contextual prompt for AI
     */
    public String getContextualPrompt(int userId, String newMessage) {
        EnhancedSessionContext session = getOrCreateSession(userId);
        return session.getContextualPrompt(newMessage);
    }
    
    /**
     * 📊 Get session statistics
     */
    public SessionStats getSessionStats() {
        int totalSessions = sessions.size();
        int activeSessions = 0;
        int totalMessages = 0;
        int totalQuestions = 0;
        
        for (EnhancedSessionContext session : sessions.values()) {
            if (session.isValid()) {
                activeSessions++;
                totalMessages += session.getTotalMessages();
                totalQuestions += session.getTotalQuestions();
            }
        }
        
        return new SessionStats(totalSessions, activeSessions, totalMessages, totalQuestions);
    }
    
    /**
     * 📋 Get session summary for user
     */
    public String getSessionSummary(int userId) {
        EnhancedSessionContext session = getOrCreateSession(userId);
        return session.getSummary();
    }
    
    /**
     * 🗑️ Remove expired sessions
     */
    public void cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        
        List<String> expiredSessions = sessions.entrySet().stream()
            .filter(entry -> !entry.getValue().isValid())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        for (String sessionId : expiredSessions) {
            sessions.remove(sessionId);
        }
        
        System.out.println("🗑️ Cleaned up " + expiredSessions.size() + " expired sessions");
    }
    
    /**
     * 🔄 Refresh session timeout
     */
    public void refreshSession(int userId) {
        EnhancedSessionContext session = getOrCreateSession(userId);
        session.extendSession();
        
        System.out.println("🔄 Refreshed session: " + session.getSessionId());
    }
    
    /**
     * 🏷️ Get active sessions for user
     */
    public List<EnhancedSessionContext> getActiveSessionsForUser(int userId) {
        return sessions.values().stream()
            .filter(session -> session.getUserId() == userId && session.isValid())
            .collect(Collectors.toList());
    }
    
    /**
     * 🎓 Get user learning profile
     */
    public UserLearningProfile getUserLearningProfile(int userId) {
        List<EnhancedSessionContext> userSessions = getActiveSessionsForUser(userId);
        
        if (userSessions.isEmpty()) {
            return new UserLearningProfile(userId);
        }
        
        // Aggregate learning data from all sessions
        UserLearningProfile profile = new UserLearningProfile(userId);
        
        for (EnhancedSessionContext session : userSessions) {
            profile.addSessionData(session);
        }
        
        return profile;
    }
    
    /**
     * 🎯 Get smart recommendations based on session history
     */
    public List<String> getSmartRecommendations(int userId) {
        UserLearningProfile profile = getUserLearningProfile(userId);
        return profile.generateRecommendations();
    }
    
    // Helper methods
    private String generateSessionId(int userId) {
        return "session_" + userId + "_" + System.currentTimeMillis();
    }
    
    private void startCleanupScheduler() {
        // Run cleanup every 10 minutes
        scheduler.scheduleAtFixedRate(this::cleanupExpiredSessions, 10, 10, TimeUnit.MINUTES);
        
        // Run detailed cleanup every hour
        scheduler.scheduleAtFixedRate(this::performDetailedCleanup, 60, 60, TimeUnit.MINUTES);
    }
    
    private void performDetailedCleanup() {
        System.out.println("🧹 Performing detailed session cleanup...");
        
        // Remove very old sessions (> 24 hours)
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        
        List<String> oldSessions = sessions.entrySet().stream()
            .filter(entry -> entry.getValue().getCreatedAt().isBefore(cutoff))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        for (String sessionId : oldSessions) {
            sessions.remove(sessionId);
        }
        
        System.out.println("🧹 Removed " + oldSessions.size() + " old sessions (>24h)");
    }
    
    // Shutdown method
    public void shutdown() {
        scheduler.shutdown();
        System.out.println("🛑 Session Manager shutdown completed");
    }
    
    // Inner classes
    public static class SessionStats {
        private final int totalSessions;
        private final int activeSessions;
        private final int totalMessages;
        private final int totalQuestions;
        
        public SessionStats(int totalSessions, int activeSessions, int totalMessages, int totalQuestions) {
            this.totalSessions = totalSessions;
            this.activeSessions = activeSessions;
            this.totalMessages = totalMessages;
            this.totalQuestions = totalQuestions;
        }
        
        // Getters
        public int getTotalSessions() { return totalSessions; }
        public int getActiveSessions() { return activeSessions; }
        public int getTotalMessages() { return totalMessages; }
        public int getTotalQuestions() { return totalQuestions; }
        
        @Override
        public String toString() {
            return String.format("SessionStats{total=%d, active=%d, messages=%d, questions=%d}", 
                totalSessions, activeSessions, totalMessages, totalQuestions);
        }
    }
    
    public static class UserLearningProfile {
        private final int userId;
        private final Map<String, Integer> topicInterests;
        private final Map<String, Integer> genrePreferences;
        private final List<String> frequentKeywords;
        private int totalInteractions;
        
        public UserLearningProfile(int userId) {
            this.userId = userId;
            this.topicInterests = new ConcurrentHashMap<>();
            this.genrePreferences = new ConcurrentHashMap<>();
            this.frequentKeywords = new java.util.ArrayList<>();
            this.totalInteractions = 0;
        }
        
        public void addSessionData(EnhancedSessionContext session) {
            // Aggregate preferences
            session.getPreferences().getTopicPreferences().forEach((topic, count) -> {
                topicInterests.merge(topic, count, Integer::sum);
            });
            
            session.getPreferences().getGenrePreferences().forEach((genre, count) -> {
                genrePreferences.merge(genre, count, Integer::sum);
            });
            
            // Add frequent keywords
            session.getInterestKeywords().stream()
                .limit(10)
                .forEach(keyword -> {
                    if (!frequentKeywords.contains(keyword)) {
                        frequentKeywords.add(keyword);
                    }
                });
            
            totalInteractions += session.getTotalMessages();
        }
        
        public List<String> generateRecommendations() {
            List<String> recommendations = new java.util.ArrayList<>();
            
            // Genre-based recommendations
            genrePreferences.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> {
                    recommendations.add("Khám phá thêm sách " + entry.getKey() + " (bạn đã quan tâm " + entry.getValue() + " lần)");
                });
            
            // Topic-based recommendations
            topicInterests.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(2)
                .forEach(entry -> {
                    recommendations.add("Tìm hiểu sâu hơn về " + entry.getKey());
                });
            
            return recommendations;
        }
        
        // Getters
        public int getUserId() { return userId; }
        public Map<String, Integer> getTopicInterests() { return topicInterests; }
        public Map<String, Integer> getGenrePreferences() { return genrePreferences; }
        public List<String> getFrequentKeywords() { return frequentKeywords; }
        public int getTotalInteractions() { return totalInteractions; }
    }
} 