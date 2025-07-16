package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

/**
 * Model cho AI-related data của Ebook
 * Tách riêng khỏi Ebook để tuân thủ Single Responsibility Principle
 * 
 * @author ADMIN
 */
public class EbookAI {
    private int id;
    private int ebookId;                  // Foreign key to Ebooks table
    private String fileName;              // File path/name for content reading
    private String originalFileName;      // Original uploaded file name
    private String summary;               // AI-generated summary (separate from description)
    private LocalDateTime createdAt;      // When AI data was first created
    private LocalDateTime updatedAt;      // When AI data was last updated
    private String status;                // Status of AI processing (processing, completed, failed)
    private String coverUrl;

    public EbookAI() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "completed";
    }

    public EbookAI(int ebookId, String fileName, String originalFileName, String summary) {
        this();
        this.ebookId = ebookId;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.summary = summary;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEbookId() {
        return ebookId;
    }

    public void setEbookId(int ebookId) {
        this.ebookId = ebookId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        this.updatedAt = LocalDateTime.now();
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
        this.updatedAt = LocalDateTime.now();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return "EbookAI{" +
                "id=" + id +
                ", ebookId=" + ebookId +
                ", fileName='" + fileName + '\'' +
                ", originalFileName='" + originalFileName + '\'' +
                ", summary='" + (summary != null ? summary.substring(0, Math.min(50, summary.length())) + "..." : "null") + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status='" + status + '\'' +
                '}';
    }
} 