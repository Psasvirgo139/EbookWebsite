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

    public List<Chapter> getChaptersByVolumeId(int volumeId) throws SQLException {
        return chapterDAO.getChaptersByVolumeId(volumeId);
    }

    public Chapter getChapterByNumber(int volumeId, double chapterNumber) throws SQLException {
        return chapterDAO.getChapterByNumber(volumeId, chapterNumber);
    }

    public Chapter getNextChapter(int volumeId, double currentChapterNumber) throws SQLException {
        return chapterDAO.getNextChapter(volumeId, currentChapterNumber);
    }

    public Chapter getPrevChapter(int volumeId, double currentChapterNumber) throws SQLException {
        return chapterDAO.getPrevChapter(volumeId, currentChapterNumber);
    }

    public Chapter getChapterById(int id) throws SQLException {
        return chapterDAO.getChapterById(id);
    }

    public void createChapter(Chapter chapter) throws SQLException {
        chapterDAO.createChapter(chapter);
    }

    public boolean updateChapter(Chapter chapter) throws SQLException {
        return chapterDAO.updateChapter(chapter);
    }

    public boolean deleteChapter(int id) throws SQLException {
        return chapterDAO.deleteChapter(id);
    }
}
