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
}
