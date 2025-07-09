package com.mycompany.ebookwebsite.utils;

public class CommentValidation {

    public static int validateId(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0) throw new IllegalArgumentException("ID phải lớn hơn 0");
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID không hợp lệ");
        }
    }

    public static String validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Nội dung bình luận không được để trống");
        }
        return content.trim();
    }

    public static int validateUserId(String userIdStr) {
        try {
            int userId = Integer.parseInt(userIdStr);
            if (userId <= 0) throw new IllegalArgumentException("User ID không hợp lệ");
            return userId;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("User ID không hợp lệ");
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
