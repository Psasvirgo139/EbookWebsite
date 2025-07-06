/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.model;

import java.sql.Timestamp;
import java.time.LocalDate;

/**
 *
 * @author ADMIN
 */
public class User {
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private String avatarUrl;
    private String role;
    private LocalDate createdAt;
    private Integer userinforId;
    private String status;
    private LocalDate lastLogin;
    private String resetToken;
    private Timestamp resetTokenExpiry;
    
    // Thêm fields cho change email
    private String changeEmailToken;
    private Timestamp changeEmailExpiry;
    private String changeEmailNew;

    public User() {
    }

    public User(int id, String username, String email, String passwordHash, String avatarUrl, String role, LocalDate createdAt, Integer userinforId, String status, LocalDate lastLogin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.createdAt = createdAt;
        this.userinforId = userinforId;
        this.status = status;
        this.lastLogin = lastLogin;
    }

    public User(int id, String username, String email, String passwordHash, String avatarUrl, String role, LocalDate createdAt, Integer userinforId, String status, LocalDate lastLogin, String resetToken, Timestamp resetTokenExpiry) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.createdAt = createdAt;
        this.userinforId = userinforId;
        this.status = status;
        this.lastLogin = lastLogin;
        this.resetToken = resetToken;
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public User(int id, String username, String email, String passwordHash, String avatarUrl, String role, LocalDate createdAt, Integer userinforId, String status, LocalDate lastLogin, String resetToken, Timestamp resetTokenExpiry, String changeEmailToken, Timestamp changeEmailExpiry, String changeEmailNew) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.createdAt = createdAt;
        this.userinforId = userinforId;
        this.status = status;
        this.lastLogin = lastLogin;
        this.resetToken = resetToken;
        this.resetTokenExpiry = resetTokenExpiry;
        this.changeEmailToken = changeEmailToken;
        this.changeEmailExpiry = changeEmailExpiry;
        this.changeEmailNew = changeEmailNew;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUserinforId() {
        return userinforId;
    }

    public void setUserinforId(Integer userinforId) {
        this.userinforId = userinforId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Timestamp getResetTokenExpiry() {
        return resetTokenExpiry;
    }

    public void setResetTokenExpiry(Timestamp resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public String getChangeEmailToken() {
        return changeEmailToken;
    }

    public void setChangeEmailToken(String changeEmailToken) {
        this.changeEmailToken = changeEmailToken;
    }

    public Timestamp getChangeEmailExpiry() {
        return changeEmailExpiry;
    }

    public void setChangeEmailExpiry(Timestamp changeEmailExpiry) {
        this.changeEmailExpiry = changeEmailExpiry;
    }

    public String getChangeEmailNew() {
        return changeEmailNew;
    }

    public void setChangeEmailNew(String changeEmailNew) {
        this.changeEmailNew = changeEmailNew;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", email=" + email + ", passwordHash=" + passwordHash + ", avatarUrl=" + avatarUrl + ", role=" + role + ", createdAt=" + createdAt + ", userinforId=" + userinforId + ", status=" + status + ", lastLogin=" + lastLogin + ", resetToken=" + resetToken + ", resetTokenExpiry=" + resetTokenExpiry + ", changeEmailToken=" + changeEmailToken + ", changeEmailExpiry=" + changeEmailExpiry + ", changeEmailNew=" + changeEmailNew + '}';
    }
}
