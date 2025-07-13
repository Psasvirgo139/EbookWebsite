package com.mycompany.ebookwebsite.utils;

public class EbookValidation {

    public static int validatePage(String pageStr) {
        try {
            int page = Integer.parseInt(pageStr);
            if (page < 1) throw new IllegalArgumentException("Page must be >= 1");
            return page;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid page number");
        }
    }

    public static int validateId(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            if (id < 1) throw new IllegalArgumentException("ID must be >= 1");
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID");
        }
    }

    public static boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty() && title.length() <= 255;
    }
}
