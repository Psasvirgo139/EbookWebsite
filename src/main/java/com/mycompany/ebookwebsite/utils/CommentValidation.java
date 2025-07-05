package com.mycompany.ebookwebsite.utils;

public class CommentValidation {

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

    public static String validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Nội dung bình luận không được để trống");
        }
        
        String trimmedContent = content.trim();
        if (trimmedContent.length() < 5) {
            throw new IllegalArgumentException("Nội dung bình luận phải có ít nhất 5 ký tự");
        }
        
        if (trimmedContent.length() > 1000) {
            throw new IllegalArgumentException("Nội dung bình luận không được vượt quá 1000 ký tự");
        }
        
        // Kiểm tra nội dung spam hoặc không phù hợp
        String lowerContent = trimmedContent.toLowerCase();
        String[] spamWords = {"spam", "advertisement", "click here", "buy now", "free money"};
        for (String spamWord : spamWords) {
            if (lowerContent.contains(spamWord)) {
                throw new IllegalArgumentException("Nội dung bình luận chứa từ không phù hợp");
            }
        }
        
        return trimmedContent;
    }

    public static int validateUserId(String userIdStr) {
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID không được để trống");
        }
        
        try {
            int userId = Integer.parseInt(userIdStr.trim());
            if (userId <= 0) {
                throw new IllegalArgumentException("User ID phải là số nguyên dương");
            }
            return userId;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("User ID không hợp lệ: " + userIdStr);
        }
    }

    public static Integer validateNullableChapterId(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) return null;
        try {
            int id = Integer.parseInt(idStr);
            return id > 0 ? id : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
