package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model view để hiển thị thông tin truyện mới nhất
 * Tuân thủ nguyên tắc không sửa model cũ, tạo model view mới
 * 
 * @author ADMIN
 */
public class LatestBookView {
    private int id;
    private String title;
    private String description;
    private String coverUrl;
    private String releaseType;
    private String language;
    private String status;
    private String visibility;
    private Integer uploaderId;
    private LocalDateTime createdAt;
    private int viewCount;
    private String uploaderName; // Tên người upload
    private int chapterCount; // Số chapter
    private String formattedCreatedAt; // Ngày tạo đã format
    private String timeAgo; // Thời gian tương đối (ví dụ: "2 giờ trước")
    
    public LatestBookView() {
    }
    
    public LatestBookView(Ebook ebook) {
        this.id = ebook.getId();
        this.title = ebook.getTitle();
        this.description = ebook.getDescription();
        this.coverUrl = ebook.getCoverUrl();
        this.releaseType = ebook.getReleaseType();
        this.language = ebook.getLanguage();
        this.status = ebook.getStatus();
        this.visibility = ebook.getVisibility();
        this.uploaderId = ebook.getUploaderId();
        this.createdAt = ebook.getCreatedAt();
        this.viewCount = ebook.getViewCount();
        
        // Format thời gian tạo
        if (this.createdAt != null) {
            this.formattedCreatedAt = this.createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            this.timeAgo = calculateTimeAgo(this.createdAt);
        }
    }
    
    /**
     * Tính thời gian tương đối từ thời điểm tạo đến hiện tại
     */
    private String calculateTimeAgo(LocalDateTime createdTime) {
        LocalDateTime now = LocalDateTime.now();
        long seconds = java.time.Duration.between(createdTime, now).getSeconds();
        
        if (seconds < 60) {
            return "Vừa xong";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " phút trước";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + " giờ trước";
        } else if (seconds < 2592000) {
            long days = seconds / 86400;
            return days + " ngày trước";
        } else if (seconds < 31536000) {
            long months = seconds / 2592000;
            return months + " tháng trước";
        } else {
            long years = seconds / 31536000;
            return years + " năm trước";
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
    
    public String getCoverUrl() {
        return coverUrl;
    }
    
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
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
    
    public String getUploaderName() {
        return uploaderName;
    }
    
    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }
    
    public int getChapterCount() {
        return chapterCount;
    }
    
    public void setChapterCount(int chapterCount) {
        this.chapterCount = chapterCount;
    }
    
    public String getFormattedCreatedAt() {
        return formattedCreatedAt;
    }
    
    public void setFormattedCreatedAt(String formattedCreatedAt) {
        this.formattedCreatedAt = formattedCreatedAt;
    }
    
    public String getTimeAgo() {
        return timeAgo;
    }
    
    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
    
    @Override
    public String toString() {
        return "LatestBookView{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseType='" + releaseType + '\'' +
                ", createdAt=" + createdAt +
                ", viewCount=" + viewCount +
                ", timeAgo='" + timeAgo + '\'' +
                '}';
    }
} 