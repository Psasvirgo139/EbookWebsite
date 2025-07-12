package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

public class AdminBookView {
    private int id;
    private String title;
    private String releaseType;
    private LocalDateTime createdAt;
    private String status;
    private int viewCount;
    // Có thể bổ sung thêm trường nếu cần cho view

    public AdminBookView(int id, String title, String releaseType, LocalDateTime createdAt, String status, int viewCount) {
        this.id = id;
        this.title = title;
        this.releaseType = releaseType;
        this.createdAt = createdAt;
        this.status = status;
        this.viewCount = viewCount;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getReleaseType() { return releaseType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public int getViewCount() { return viewCount; }
} 