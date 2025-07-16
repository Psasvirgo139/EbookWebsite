/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

/**
 *
 * @author ADMIN
 */
public class Ebook {
    private int id;
    private String title;
    private String description;
    private String releaseType;
    private String language;
    private String status;
    private String visibility;
    private Integer uploaderId;
    private LocalDateTime createdAt;
    private int viewCount;
    private String coverUrl;

    public Ebook() {
    }

    public Ebook(int id, String title, String description, String releaseType, String language, String status, String visibility, Integer uploaderId, LocalDateTime createdAt, int viewCount, String coverUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseType = releaseType;
        this.language = language;
        this.status = status;
        this.visibility = visibility;
        this.uploaderId = uploaderId;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.coverUrl = coverUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Integer getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(Integer uploaderId) {
        this.uploaderId = uploaderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getSummary() {
        return null;
    }

    // ===== AI COMPATIBILITY METHODS =====
    
    /**
     * Compatibility method for AI services
     * @return default author (TODO: join with Authors table)
     */
    public String getAuthor() {
        // TODO: Implement proper author relationship with Authors table
        return "Tác giả chưa xác định";
    }
    
    /**
     * Compatibility method for AI services
     * @return genre/category (alias for releaseType)
     * @deprecated Use getReleaseType() instead
     */
    public String getGenre() {
        return this.releaseType != null ? this.releaseType : "Chưa phân loại";
    }

    @Override
    public String toString() {
        return "Ebook{" + "id=" + id + ", title=" + title + ", description=" + description + ", releaseType=" + releaseType + ", language=" + language + ", status=" + status + ", visibility=" + visibility + ", uploaderId=" + uploaderId + ", createdAt=" + createdAt + ", viewCount=" + viewCount + ", coverUrl=" + coverUrl + '}';
    }
}
