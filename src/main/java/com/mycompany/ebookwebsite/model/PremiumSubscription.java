package com.mycompany.ebookwebsite.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 👑 Premium Subscription Model
 * 
 * Theo dõi thời hạn premium subscription của user
 * Mỗi subscription kéo dài 1 tháng từ ngày đăng ký
 */
public class PremiumSubscription {
    
    private int id;
    private int userId;
    private LocalDate startDate;        // Ngày bắt đầu premium
    private LocalDate expiryDate;       // Ngày hết hạn premium 
    private String paymentMethod;       // "coin" hoặc "vnd"
    private double amount;              // Số tiền hoặc coin đã trả
    private String status;              // "active", "expired", "cancelled"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public PremiumSubscription() {
    }
    
    public PremiumSubscription(int userId, LocalDate startDate, LocalDate expiryDate, 
                              String paymentMethod, double amount) {
        this.userId = userId;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.status = "active";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
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
    
    /**
     * ✅ Kiểm tra subscription có còn active không
     */
    public boolean isActive() {
        return "active".equals(status) && 
               expiryDate != null && 
               !expiryDate.isBefore(LocalDate.now());
    }
    
    /**
     * ⏰ Kiểm tra subscription có bị expired không
     */
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }
    
    /**
     * 📅 Lấy số ngày còn lại
     */
    public long getDaysRemaining() {
        if (expiryDate == null || isExpired()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }
    
    @Override
    public String toString() {
        return "PremiumSubscription{" +
                "id=" + id +
                ", userId=" + userId +
                ", startDate=" + startDate +
                ", expiryDate=" + expiryDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", active=" + isActive() +
                ", daysRemaining=" + getDaysRemaining() +
                '}';
    }
} 