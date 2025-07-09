package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

/**
 * Model để quản lý coin của user
 * @author ADMIN
 */
public class UserCoin {
    private int userId;
    private int coins;
    private LocalDateTime lastUpdated;

    public UserCoin() {
    }

    public UserCoin(int userId, int coins, LocalDateTime lastUpdated) {
        this.userId = userId;
        this.coins = coins;
        this.lastUpdated = lastUpdated;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "UserCoin{" + "userId=" + userId + ", coins=" + coins + ", lastUpdated=" + lastUpdated + '}';
    }
} 