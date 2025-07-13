package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

public class AdminCommentView {
    private int id;
    private int userId;
    private String username;
    private int ebookId;
    private String ebookTitle;
    private Integer chapterId;
    private String content;
    private LocalDateTime createdAt;

    public AdminCommentView(int id, int userId, String username, int ebookId, String ebookTitle, Integer chapterId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.ebookId = ebookId;
        this.ebookTitle = ebookTitle;
        this.chapterId = chapterId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public int getEbookId() { return ebookId; }
    public String getEbookTitle() { return ebookTitle; }
    public Integer getChapterId() { return chapterId; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
} 