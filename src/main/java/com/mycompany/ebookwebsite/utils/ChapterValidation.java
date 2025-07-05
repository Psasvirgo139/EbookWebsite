package com.mycompany.ebookwebsite.utils;

public class ChapterValidation {

    public static double validateChapter(String chapterStr) {
        try {
            double chapter = Double.parseDouble(chapterStr);
            if (chapter <= 0) throw new IllegalArgumentException("Chapter number must be > 0");
            return chapter;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid chapter number");
        }
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
}
