package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 🧠 Enhanced Session Context
 * 
 * Quản lý context session nâng cao với memory và learning
 */
public class EnhancedSessionContext {
    
    private String sessionId;
    private int userId;
    private LocalDateTime createdAt;
    private LocalDateTime lastActivity;
    
    // Conversation tracking
    private List<ConversationTurn> conversationHistory;
    private Map<String, String> contextVariables;
    
    // Book reading context
    private Map<Integer, BookReading> currentBooks;
    private Set<String> askedKeywords;
    private Set<String> interestKeywords;
    
    // User preferences learned
    private UserPreferences preferences;
    
    // Analytics
    private int totalMessages;
    private int totalQuestions;
    private LocalDateTime sessionTimeout;
    
    public EnhancedSessionContext(String sessionId, int userId) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.lastActivity = LocalDateTime.now();
        
        this.conversationHistory = new ArrayList<>();
        this.contextVariables = new HashMap<>();
        this.currentBooks = new HashMap<>();
        this.askedKeywords = new HashSet<>();
        this.interestKeywords = new HashSet<>();
        this.preferences = new UserPreferences();
        
        this.totalMessages = 0;
        this.totalQuestions = 0;
        this.sessionTimeout = LocalDateTime.now().plusMinutes(30);
    }
    
    /**
     * 💬 Add conversation turn to history
     */
    public void addConversationTurn(String userMessage, String aiResponse, String questionType) {
        ConversationTurn turn = new ConversationTurn(userMessage, aiResponse, questionType);
        conversationHistory.add(turn);
        
        // Keep only last 20 turns for memory efficiency
        if (conversationHistory.size() > 20) {
            conversationHistory.remove(0);
        }
        
        updateMetrics(userMessage);
        extendSession();
    }
    
    /**
     * 📚 Update book reading context
     */
    public void updateBookContext(int bookId, String bookTitle, String action) {
        BookReading reading = currentBooks.getOrDefault(bookId, new BookReading(bookId, bookTitle));
        reading.updateActivity(action);
        currentBooks.put(bookId, reading);
    }
    
    /**
     * 🔍 Add keywords from user interactions
     */
    public void addKeywords(List<String> keywords, boolean isUserInterest) {
        if (isUserInterest) {
            interestKeywords.addAll(keywords);
        } else {
            askedKeywords.addAll(keywords);
        }
        
        // Learn user preferences
        preferences.updateFromKeywords(keywords);
    }
    
    /**
     * 🎯 Get contextual prompt for AI
     */
    public String getContextualPrompt(String newMessage) {
        StringBuilder prompt = new StringBuilder();
        
        // Recent conversation context
        if (!conversationHistory.isEmpty()) {
            prompt.append("Context cuộc trò chuyện gần đây:\n");
            
            int contextSize = Math.min(3, conversationHistory.size());
            for (int i = conversationHistory.size() - contextSize; i < conversationHistory.size(); i++) {
                ConversationTurn turn = conversationHistory.get(i);
                prompt.append("User: ").append(turn.getUserMessage()).append("\n");
                prompt.append("AI: ").append(turn.getAiResponse().substring(0, Math.min(100, turn.getAiResponse().length()))).append("...\n");
            }
            prompt.append("\n");
        }
        
        // Current books context
        if (!currentBooks.isEmpty()) {
            prompt.append("Sách đang được thảo luận:\n");
            currentBooks.values().forEach(book -> {
                prompt.append("- ").append(book.getTitle()).append(" (").append(book.getLastAction()).append(")\n");
            });
            prompt.append("\n");
        }
        
        // User interests
        if (!interestKeywords.isEmpty()) {
            prompt.append("Sở thích người dùng: ").append(String.join(", ", 
                interestKeywords.stream().limit(5).toArray(String[]::new))).append("\n");
        }
        
        // Current question
        prompt.append("Câu hỏi hiện tại: ").append(newMessage).append("\n");
        
        return prompt.toString();
    }
    
    /**
     * 📊 Get session summary
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        
        summary.append(String.format("Session: %s | User: %d | Messages: %d | Questions: %d\n",
            sessionId, userId, totalMessages, totalQuestions));
        
        if (!currentBooks.isEmpty()) {
            summary.append("Books discussed: ").append(currentBooks.size()).append("\n");
        }
        
        if (!interestKeywords.isEmpty()) {
            summary.append("Interests: ").append(String.join(", ", 
                interestKeywords.stream().limit(3).toArray(String[]::new))).append("\n");
        }
        
        return summary.toString();
    }
    
    /**
     * ⏰ Check if session is still valid
     */
    public boolean isValid() {
        return LocalDateTime.now().isBefore(sessionTimeout);
    }
    
    /**
     * 🔄 Extend session timeout
     */
    public void extendSession() {
        this.lastActivity = LocalDateTime.now();
        this.sessionTimeout = LocalDateTime.now().plusMinutes(30);
    }
    
    // Helper methods
    private void updateMetrics(String userMessage) {
        totalMessages++;
        if (userMessage.contains("?") || userMessage.toLowerCase().matches(".*\\b(gì|nào|ai|sao|thế nào|như thế nào)\\b.*")) {
            totalQuestions++;
        }
    }
    
    // Inner classes
    public static class ConversationTurn {
        private String userMessage;
        private String aiResponse;
        private String questionType;
        private LocalDateTime timestamp;
        
        public ConversationTurn(String userMessage, String aiResponse, String questionType) {
            this.userMessage = userMessage;
            this.aiResponse = aiResponse;
            this.questionType = questionType;
            this.timestamp = LocalDateTime.now();
        }
        
        // Getters
        public String getUserMessage() { return userMessage; }
        public String getAiResponse() { return aiResponse; }
        public String getQuestionType() { return questionType; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }
    
    public static class BookReading {
        private int bookId;
        private String title;
        private String lastAction;
        private LocalDateTime lastAccess;
        private int accessCount;
        
        public BookReading(int bookId, String title) {
            this.bookId = bookId;
            this.title = title;
            this.lastAction = "mentioned";
            this.lastAccess = LocalDateTime.now();
            this.accessCount = 1;
        }
        
        public void updateActivity(String action) {
            this.lastAction = action;
            this.lastAccess = LocalDateTime.now();
            this.accessCount++;
        }
        
        // Getters
        public int getBookId() { return bookId; }
        public String getTitle() { return title; }
        public String getLastAction() { return lastAction; }
        public LocalDateTime getLastAccess() { return lastAccess; }
        public int getAccessCount() { return accessCount; }
    }
    
    public static class UserPreferences {
        private Map<String, Integer> genrePreferences;
        private Map<String, Integer> topicPreferences;
        private List<String> favoriteAuthors;
        
        public UserPreferences() {
            this.genrePreferences = new HashMap<>();
            this.topicPreferences = new HashMap<>();
            this.favoriteAuthors = new ArrayList<>();
        }
        
        public void updateFromKeywords(List<String> keywords) {
            // Simple preference learning from keywords
            for (String keyword : keywords) {
                String lowerKeyword = keyword.toLowerCase();
                
                // Genre preferences
                if (isGenreKeyword(lowerKeyword)) {
                    genrePreferences.put(lowerKeyword, genrePreferences.getOrDefault(lowerKeyword, 0) + 1);
                }
                
                // Topic preferences
                topicPreferences.put(lowerKeyword, topicPreferences.getOrDefault(lowerKeyword, 0) + 1);
            }
        }
        
        private boolean isGenreKeyword(String keyword) {
            List<String> genres = List.of("tiểu thuyết", "khoa học", "trinh thám", "tình cảm", 
                "fantasy", "kinh dị", "huyền bí", "lịch sử", "văn học");
            return genres.contains(keyword);
        }
        
        public Map<String, Integer> getGenrePreferences() { return genrePreferences; }
        public Map<String, Integer> getTopicPreferences() { return topicPreferences; }
        public List<String> getFavoriteAuthors() { return favoriteAuthors; }
    }
    
    // Getters and Setters
    public String getSessionId() { return sessionId; }
    public int getUserId() { return userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastActivity() { return lastActivity; }
    public List<ConversationTurn> getConversationHistory() { return conversationHistory; }
    public Map<String, String> getContextVariables() { return contextVariables; }
    public Map<Integer, BookReading> getCurrentBooks() { return currentBooks; }
    public Set<String> getAskedKeywords() { return askedKeywords; }
    public Set<String> getInterestKeywords() { return interestKeywords; }
    public UserPreferences getPreferences() { return preferences; }
    public int getTotalMessages() { return totalMessages; }
    public int getTotalQuestions() { return totalQuestions; }
    public LocalDateTime getSessionTimeout() { return sessionTimeout; }
} 