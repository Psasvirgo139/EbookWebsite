/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.model;

import java.time.LocalDate;

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
    private LocalDate createdAt;
    private int viewCount;
    private String coverUrl;
    private String summary;
    
    // ✅ Thêm field để mapping file
    private String fileName;        // Tên file thực tế trong thư mục uploads (normalized)
    private String originalFileName; // Tên file gốc khi user upload

    public Ebook() {
    }

    public Ebook(int id, String title, String description, String releaseType, String language, String status, String visibility, Integer uploaderId, LocalDate createdAt, int viewCount, String coverUrl) {
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

    // ========== Getters và Setters ==========

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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
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
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    // ✅ Getter và Setter cho fileName
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    // ✅ Getter và Setter cho originalFileName
    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
    
    // Alias methods cho compatibility với Book model
    public String getContentUrl() {
        return fileName != null ? "uploads/" + fileName : null;
    }
    
    public void setContentUrl(String contentUrl) {
        if (contentUrl != null && contentUrl.startsWith("uploads/")) {
            this.fileName = contentUrl.substring(8);
        } else {
            this.fileName = contentUrl;
        }
    }
    
    // Alias cho genre field
    public String getGenre() {
        return releaseType;
    }

    public void setGenre(String genre) {
        this.releaseType = genre;
    }
    
    // Default author getter cho compatibility  
    public String getAuthor() {
        return "Unknown Author";  // Có thể extend sau để lấy từ bảng Authors
    }

    @Override
    public String toString() {
        return "Ebook{" + 
               "id=" + id + 
               ", title='" + title + '\'' + 
               ", description='" + description + '\'' + 
               ", releaseType='" + releaseType + '\'' + 
               ", language='" + language + '\'' + 
               ", status='" + status + '\'' + 
               ", visibility='" + visibility + '\'' + 
               ", uploaderId=" + uploaderId + 
               ", createdAt=" + createdAt + 
               ", viewCount=" + viewCount + 
               ", coverUrl='" + coverUrl + '\'' + 
               ", fileName='" + fileName + '\'' + 
               ", originalFileName='" + originalFileName + '\'' +
               '}';
    }
}
