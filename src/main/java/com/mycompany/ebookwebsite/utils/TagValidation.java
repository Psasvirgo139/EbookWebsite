package com.mycompany.ebookwebsite.utils;

import com.mycompany.ebookwebsite.model.Tag;

public class TagValidation {
    public static Tag validate(String idStr, String name) {
        int id = 0;
        if (idStr != null && !idStr.isEmpty()) {
            try {
                id = Integer.parseInt(idStr);
                if (id < 1) throw new IllegalArgumentException("ID must be positive");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid ID");
            }
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be empty");
        }

        Tag tag = new Tag();
        tag.setId(id);
        tag.setName(name.trim());
        return tag;
    }

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
    
    public static String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên tag không được để trống");
        }
        
        String trimmedName = name.trim();
        if (trimmedName.length() > 50) {
            throw new IllegalArgumentException("Tên tag không được vượt quá 50 ký tự");
        }
        
        // Kiểm tra ký tự đặc biệt không hợp lệ
        if (!trimmedName.matches("^[a-zA-Z0-9\\s\\u00C0-\\u1EF9]+$")) {
            throw new IllegalArgumentException("Tên tag chỉ được chứa chữ cái, số và dấu cách");
        }
        
        return trimmedName;
    }
}
