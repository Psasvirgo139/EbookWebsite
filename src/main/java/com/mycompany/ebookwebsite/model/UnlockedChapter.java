package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

/**
 * Model để lưu trữ thông tin chapter premium đã được user mở khóa
 * @author ADMIN
 */
public class UnlockedChapter {
    private int id;
    private int userId;
    private int chapterId;
    private int coinSpent;
    private LocalDateTime unlockedAt;

    public UnlockedChapter() {
    }

    public UnlockedChapter(int id, int userId, int chapterId, int coinSpent, LocalDateTime unlockedAt) {
        this.id = id;
        this.userId = userId;
        this.chapterId = chapterId;
        this.coinSpent = coinSpent;
        this.unlockedAt = unlockedAt;
    }

    public UnlockedChapter(int userId, int chapterId, int coinSpent, LocalDateTime unlockedAt) {
        this.userId = userId;
        this.chapterId = chapterId;
        this.coinSpent = coinSpent;
        this.unlockedAt = unlockedAt;
    }

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

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getCoinSpent() {
        return coinSpent;
    }

    public void setCoinSpent(int coinSpent) {
        this.coinSpent = coinSpent;
    }

    public LocalDateTime getUnlockedAt() {
        return unlockedAt;
    }

    public void setUnlockedAt(LocalDateTime unlockedAt) {
        this.unlockedAt = unlockedAt;
    }

    @Override
    public String toString() {
        return "UnlockedChapter{" + "id=" + id + ", userId=" + userId + ", chapterId=" + chapterId + ", coinSpent=" + coinSpent + ", unlockedAt=" + unlockedAt + '}';
    }
} 