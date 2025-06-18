package com.mycompany.ebookwebsite.utils;

import com.mycompany.ebookwebsite.model.User;

public class UserValidation {
    
    /**
     * Validate dữ liệu user
     */
    public static void validateUserData(User user, boolean isCreate) {
        if (user == null) {
            throw new IllegalArgumentException("User data không được null");
        }
        
        validateUsername(user.getUsername());
        validateEmail(user.getEmail());
        
        // Validate password (chỉ khi tạo mới)
        if (isCreate) {
            validatePassword(user.getPasswordHash());
        }
        
        // Validate role
        if (user.getRole() != null && !user.getRole().trim().isEmpty()) {
            validateRole(user.getRole());
        }
        
        // Validate status
        if (user.getStatus() != null && !user.getStatus().trim().isEmpty()) {
            validateStatus(user.getStatus());
        }
    }
    
    /**
     * Validate username
     */
    public static void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username không được để trống");
        }
        
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Username phải có độ dài từ 3-50 ký tự");
        }
        
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Username chỉ được chứa chữ cái, số và dấu gạch dưới");
        }
    }
    
    /**
     * Validate email
     */
    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Email không đúng định dạng");
        }
    }
    
    /**
     * Validate password
     */
    public static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password không được để trống");
        }
        
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password phải có ít nhất 6 ký tự");
        }
    }
    
    /**
     * Validate role
     */
    public static void validateRole(String role) {
        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Role không hợp lệ: " + role);
        }
    }
    
    /**
     * Validate status
     */
    public static void validateStatus(String status) {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Status không hợp lệ: " + status);
        }
    }
    
    /**
     * Validate user ID
     */
    public static void validateUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID không hợp lệ");
        }
    }
    
    /**
     * Validate search term
     */
    public static void validateSearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Từ khóa tìm kiếm không được để trống");
        }
    }
    
    /**
     * Validate login credentials
     */
    public static void validateLoginCredentials(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username không được để trống");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password không được để trống");
        }
    }
    
    /**
     * Kiểm tra email hợp lệ
     */
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
    
    /**
     * Kiểm tra role hợp lệ
     */
    private static boolean isValidRole(String role) {
        return role.equals("admin") || role.equals("user") || role.equals("moderator");
    }
    
    /**
     * Kiểm tra status hợp lệ
     */
    private static boolean isValidStatus(String status) {
        return status.equals("active") || status.equals("inactive") || status.equals("deleted") || status.equals("banned");
    }
}