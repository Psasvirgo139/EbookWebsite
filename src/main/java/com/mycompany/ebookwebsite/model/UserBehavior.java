package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

public class UserBehavior {
    private int id;
    private int userId;
    private String actionType;    // 'view_book', 'search', 'chat_question', 'favorite', 'read'
    private Integer targetId;     // book_id, author_id, etc
    private String targetType;    // 'book', 'author', 'genre'
    private String actionData;    // JSON metadata với additional info
    private LocalDateTime createdAt;

    // Constructors
    public UserBehavior() {}

    public UserBehavior(int userId, String actionType, Integer targetId, 
                       String targetType, String actionData) {
        this.userId = userId;
        this.actionType = actionType;
        this.targetId = targetId;
        this.targetType = targetType;
        this.actionData = actionData;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public Integer getTargetId() { return targetId; }
    public void setTargetId(Integer targetId) { this.targetId = targetId; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public String getActionData() { return actionData; }
    public void setActionData(String actionData) { this.actionData = actionData; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 