package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.ChapterDAO;
import com.mycompany.ebookwebsite.dao.FavoriteDAO;
import com.mycompany.ebookwebsite.model.Chapter;
import com.mycompany.ebookwebsite.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ChapterService {
    private final ChapterDAO chapterDAO = new ChapterDAO();
    private final FavoriteDAO favoriteDAO = new FavoriteDAO();

    public List<Chapter> getChaptersByBookId(int ebookId) throws SQLException {
        return chapterDAO.getChaptersByBookId(ebookId);
    }

    public Chapter getChapterByBookAndIndex(int ebookId, double number) throws SQLException {
        return chapterDAO.getChapterByBookAndIndex(ebookId, number);
    }

    public void addChapter(Chapter chapter) throws SQLException {
        chapterDAO.insertChapter(chapter);
    }

    public boolean updateChapter(Chapter chapter) throws SQLException {
        return chapterDAO.updateChapter(chapter);
    }

    public void deleteChapterAndRelated(int chapterId, User user) throws SQLException {
        if (!"admin".equals(user.getRole())) {
            throw new SecurityException("Bạn không có quyền xóa chapter!");
        }
        Connection conn = null;
        try {
            conn = com.mycompany.ebookwebsite.dao.DBConnection.getConnection();
            conn.setAutoCommit(false);
            chapterDAO.deleteChapter(chapterId);
            favoriteDAO.deleteByChapter(chapterId);
            // Có thể thêm xóa các liên kết khác nếu cần
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
