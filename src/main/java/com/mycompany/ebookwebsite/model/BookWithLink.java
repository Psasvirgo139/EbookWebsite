package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

/**
 * Model view để hiển thị sách với link trực tiếp
 * Tuân thủ nguyên tắc không sửa model cũ, tạo model view mới
 * 
 * @author ADMIN
 */
public class BookWithLink {
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
    private String directLink; // Link trực tiếp đến trang chi tiết sách
    private String shortDescription; // Mô tả ngắn gọn
    
    public BookWithLink() {}
    
    public BookWithLink(Ebook ebook) {
        this.id = ebook.getId();
        this.title = ebook.getTitle();
        this.description = ebook.getDescription();
        this.releaseType = ebook.getReleaseType();
        this.language = ebook.getLanguage();
        this.status = ebook.getStatus();
        this.visibility = ebook.getVisibility();
        this.uploaderId = ebook.getUploaderId();
        this.createdAt = ebook.getCreatedAt();
        this.viewCount = ebook.getViewCount();
        this.coverUrl = ebook.getCoverUrl();
        
        // Tạo link trực tiếp
        this.directLink = "http://localhost:8080/EbookWebsite/book/detail?id=" + this.id;
        
        // Tạo mô tả ngắn gọn
        if (this.description != null && !this.description.isEmpty()) {
            if (this.description.length() > 100) {
                this.shortDescription = this.description.substring(0, 100) + "...";
            } else {
                this.shortDescription = this.description;
            }
        } else {
            this.shortDescription = "Chưa có mô tả";
        }
    }
    
    // Getters and Setters
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
    
    public String getDirectLink() {
        return directLink;
    }
    
    public void setDirectLink(String directLink) {
        this.directLink = directLink;
    }
    
    public String getShortDescription() {
        return shortDescription;
    }
    
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    
    /**
     * Tạo response text với link trực tiếp
     */
    public String toResponseText() {
        StringBuilder sb = new StringBuilder();
        sb.append("📖 **").append(title).append("**");
        if (releaseType != null && !releaseType.isEmpty()) {
            sb.append(" (").append(releaseType).append(")");
        }
        sb.append("\n");
        sb.append("🔗 Link: ").append(directLink).append("\n");
        sb.append("📝 ").append(shortDescription).append("\n");
        sb.append("👁️ ").append(viewCount).append(" lượt xem\n");
        return sb.toString();
    }
    
    /**
     * Tạo response text ngắn gọn với link
     */
    public String toShortResponseText() {
        return "📖 **" + title + "**: " + directLink;
    }
} 