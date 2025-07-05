package com.mycompany.ebookwebsite.utils;

import com.mycompany.ebookwebsite.model.User;
import java.util.regex.Pattern;

public class UserValidation {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{3,20}$"
    );
    
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
    public static String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống");
        }
        
        String trimmedUsername = username.trim();
        if (trimmedUsername.length() < 3 || trimmedUsername.length() > 20) {
            throw new IllegalArgumentException("Tên đăng nhập phải có 3-20 ký tự");
        }
        
        if (!USERNAME_PATTERN.matcher(trimmedUsername).matches()) {
            throw new IllegalArgumentException("Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới");
        }
        
        return trimmedUsername;
    }
    
    /**
     * Validate email
     */
    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
        }
        
        String trimmedEmail = email.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
        
        return trimmedEmail;
    }
    
    /**
     * Validate password
     */
    public static String validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
        
        String trimmedPassword = password.trim();
        if (trimmedPassword.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự");
        }
        
        return trimmedPassword;
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
    public static int validateId(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) {
            throw new IllegalArgumentException("ID không được để trống");
        }
        
        try {
            int id = Integer.parseInt(idStr.trim());
            if (id <= 0) {
                throw new IllegalArgumentException("ID phải là số nguyên dương");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID không hợp lệ: " + idStr);
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
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim().toLowerCase()).matches();
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
    
    // Static methods để kiểm tra format
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username.trim()).matches();
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.trim().length() >= 6;
    }
    
    public static String validateFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống");
        }
        
        String trimmedFullName = fullName.trim();
        if (trimmedFullName.length() < 2 || trimmedFullName.length() > 100) {
            throw new IllegalArgumentException("Họ tên phải có 2-100 ký tự");
        }
        
        return trimmedFullName;
    }
    
    public static boolean isValidFullName(String fullName) {
        return fullName != null && fullName.trim().length() >= 2 && fullName.trim().length() <= 100;
    }
}