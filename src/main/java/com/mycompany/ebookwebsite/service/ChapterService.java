package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.ChapterDAO;
import com.mycompany.ebookwebsite.model.Chapter;

import java.sql.SQLException;
import java.util.List;

public class ChapterService {
    private final ChapterDAO chapterDAO = new ChapterDAO();

    public List<Chapter> getChaptersByBookId(int ebookId) throws SQLException {
        return chapterDAO.getChaptersByBookId(ebookId);
    }

    public Chapter getChapterByBookAndIndex(int ebookId, double number) throws SQLException {
        return chapterDAO.getChapterByBookAndIndex(ebookId, number);
    }
}
