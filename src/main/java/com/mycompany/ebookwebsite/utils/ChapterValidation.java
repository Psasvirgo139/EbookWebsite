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
}
