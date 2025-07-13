package com.mycompany.ebookwebsite.model;

import java.time.LocalDate;

public class AdminUserView {
    private int id;
    private String username;
    private String email;
    private String role;
    private String status;
    private LocalDate createdAt;
    private LocalDate lastLogin;

    public AdminUserView(int id, String username, String email, String role, String status, LocalDate createdAt, LocalDate lastLogin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
    public LocalDate getCreatedAt() { return createdAt; }
    public LocalDate getLastLogin() { return lastLogin; }
} 