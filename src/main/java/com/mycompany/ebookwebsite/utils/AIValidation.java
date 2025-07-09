package com.mycompany.ebookwebsite.utils;

public class AIValidation {
    
    /**
     * Validate user ID
     */
    public static void validateUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID phải lớn hơn 0");
        }
    }
    
    /**
     * Validate ebook ID
     */
    public static void validateEbookId(int ebookId) {
        if (ebookId <= 0) {
            throw new IllegalArgumentException("Ebook ID phải lớn hơn 0");
        }
    }
    
    /**
     * Validate search keyword
     */
    public static void validateSearchKeyword(String keyword) {
        if (keyword == null) {
            throw new IllegalArgumentException("Từ khóa tìm kiếm không được null");
        }
        if (keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Từ khóa tìm kiếm không được rỗng");
        }
        if (keyword.length() > 100) {
            throw new IllegalArgumentException("Từ khóa tìm kiếm không được quá 100 ký tự");
        }
    }
    
    /**
     * Validate author name
     */
    public static void validateAuthorName(String authorName) {
        if (authorName == null) {
            throw new IllegalArgumentException("Tên tác giả không được null");
        }
        if (authorName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên tác giả không được rỗng");
        }
        if (authorName.length() > 255) {
            throw new IllegalArgumentException("Tên tác giả không được quá 255 ký tự");
        }
    }
    
    /**
     * Validate category name
     */
    public static void validateCategoryName(String categoryName) {
        if (categoryName == null) {
            throw new IllegalArgumentException("Tên thể loại không được null");
        }
        if (categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thể loại không được rỗng");
        }
        if (categoryName.length() > 100) {
            throw new IllegalArgumentException("Tên thể loại không được quá 100 ký tự");
        }
    }
    
    /**
     * Validate chat message
     */
    public static void validateChatMessage(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Tin nhắn không được null");
        }
        if (message.trim().isEmpty()) {
            throw new IllegalArgumentException("Tin nhắn không được rỗng");
        }
        if (message.length() > 1000) {
            throw new IllegalArgumentException("Tin nhắn không được quá 1000 ký tự");
        }
    }
    
    /**
     * Validate content for AI processing
     */
    public static void validateContent(String content) {
        if (content == null) {
            throw new IllegalArgumentException("Nội dung không được null");
        }
        if (content.trim().isEmpty()) {
            throw new IllegalArgumentException("Nội dung không được rỗng");
        }
        if (content.length() > 50000) {
            throw new IllegalArgumentException("Nội dung không được quá 50000 ký tự");
        }
    }
} 