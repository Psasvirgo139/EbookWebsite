package com.mycompany.ebookwebsite.model;

import java.sql.Timestamp;

public class ChatMessage {
    private int id;
    private int userId;
    private String sessionId;
    private String message;
    private String response;
    private String type;
    private Integer bookId;
    private Timestamp createdAt;
    private boolean embeddingUsed;
    private String metadata; // NEW: For Smart Training data
    
    // NEW: Context fields
    private String contextType;
    private Integer contextId;

    // Constructors
    public ChatMessage() {}

    public ChatMessage(int userId, String sessionId, String message, String response, String type, Integer bookId) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.message = message;
        this.response = response;
        this.type = type;
        this.bookId = bookId;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.embeddingUsed = false;
        this.metadata = null;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public boolean isEmbeddingUsed() { return embeddingUsed; }
    public void setEmbeddingUsed(boolean embeddingUsed) { this.embeddingUsed = embeddingUsed; }

    // NEW: Metadata getter/setter for Smart Training
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    // NEW: Context getters/setters
    public String getContextType() { return contextType; }
    public void setContextType(String contextType) { this.contextType = contextType; }

    public Integer getContextId() { return contextId; }
    public void setContextId(Integer contextId) { this.contextId = contextId; }

    @Override
    public String toString() {
        return String.format("ChatMessage{id=%d, userId=%d, sessionId='%s', message='%s', embeddingUsed=%s}", 
                           id, userId, sessionId, 
                           message != null ? message.substring(0, Math.min(50, message.length())) : null,
                           embeddingUsed);
    }
} 