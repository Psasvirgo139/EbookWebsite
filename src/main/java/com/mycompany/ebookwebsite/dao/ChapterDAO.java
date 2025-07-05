package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Chapter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChapterDAO {

    private Chapter mapRow(ResultSet rs) throws SQLException {
        Chapter chapter = new Chapter();
        chapter.setId(rs.getInt("id"));
        chapter.setEbookID(rs.getInt("ebook_id"));
        chapter.setVolumeID((Integer) rs.getObject("volume_id"));
        chapter.setTitle(rs.getString("title"));
        chapter.setNumber(rs.getDouble("number"));
        chapter.setContentUrl(rs.getString("content_url"));
        chapter.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        chapter.setAccessLevel(rs.getString("access_level"));
        chapter.setViewCount(rs.getInt("view_count"));
        chapter.setLikeCount(rs.getInt("like_count"));
        return chapter;
    }

    public List<Chapter> getChaptersByBookId(int ebookId) throws SQLException {
        String sql = "SELECT * FROM Chapters WHERE ebook_id = ? ORDER BY number";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();
            List<Chapter> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public Chapter getChapterByBookAndIndex(int ebookId, double number) throws SQLException {
        String sql = "SELECT * FROM Chapters WHERE ebook_id = ? AND number = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ebookId);
            ps.setDouble(2, number);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    // Methods required by ChapterService
    public List<Chapter> getChaptersByVolumeId(int volumeId) throws SQLException {
        String sql = "SELECT * FROM Chapters WHERE volume_id = ? ORDER BY number";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, volumeId);
            ResultSet rs = ps.executeQuery();
            List<Chapter> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public Chapter getChapterByNumber(int ebookId, double number) throws SQLException {
        return getChapterByBookAndIndex(ebookId, number);
    }

    public Chapter getNextChapter(int ebookId, double currentNumber) throws SQLException {
        String sql = "SELECT * FROM Chapters WHERE ebook_id = ? AND number > ? ORDER BY number OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ebookId);
            ps.setDouble(2, currentNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    public Chapter getPrevChapter(int ebookId, double currentNumber) throws SQLException {
        String sql = "SELECT * FROM Chapters WHERE ebook_id = ? AND number < ? ORDER BY number DESC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ebookId);
            ps.setDouble(2, currentNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    public void createChapter(Chapter chapter) throws SQLException {
        String sql = "INSERT INTO Chapters (ebook_id, volume_id, title, number, content_url, access_level, is_free) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, chapter.getEbookID());
            ps.setObject(2, chapter.getVolumeID());
            ps.setString(3, chapter.getTitle());
            ps.setDouble(4, chapter.getNumber());
            ps.setString(5, chapter.getContentUrl());
            ps.setString(6, chapter.getAccessLevel());
            
            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    chapter.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public boolean updateChapter(Chapter chapter) throws SQLException {
        String sql = "UPDATE Chapters SET ebook_id = ?, volume_id = ?, title = ?, number = ?, content_url = ?, access_level = ?, is_free = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, chapter.getEbookID());
            ps.setObject(2, chapter.getVolumeID());
            ps.setString(3, chapter.getTitle());
            ps.setDouble(4, chapter.getNumber());
            ps.setString(5, chapter.getContentUrl());
            ps.setString(6, chapter.getAccessLevel());
            ps.setInt(8, chapter.getId());
            
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteChapter(int chapterId) throws SQLException {
        String sql = "DELETE FROM Chapters WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, chapterId);
            return ps.executeUpdate() > 0;
        }
    }

    // New methods for premium chapter management
    public List<Chapter> getFreeChaptersByBookId(int ebookId) throws SQLException {
        String sql = "SELECT * FROM Chapters WHERE ebook_id = ? AND is_free = 1 ORDER BY number";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();
            List<Chapter> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public List<Chapter> getPremiumChaptersByBookId(int ebookId) throws SQLException {
        String sql = "SELECT * FROM Chapters WHERE ebook_id = ? AND is_free = 0 ORDER BY number";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();
            List<Chapter> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public int countFreeChaptersByBookId(int ebookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Chapters WHERE ebook_id = ? AND is_free = 1";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int countPremiumChaptersByBookId(int ebookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Chapters WHERE ebook_id = ? AND is_free = 0";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public void updateChapterFreeStatus(int chapterId, boolean isFree) throws SQLException {
        String sql = "UPDATE Chapters SET is_free = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isFree);
            ps.setInt(2, chapterId);
            ps.executeUpdate();
        }
    }

    public Chapter getChapterById(int chapterId) throws SQLException {
        String sql = "SELECT * FROM Chapters WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, chapterId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    public List<Chapter> getTopViewedChapters(int limit) throws SQLException {
        String sql = "SELECT * FROM Chapters ORDER BY view_count DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            List<Chapter> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public List<Chapter> getTopViewedFreeChapters(int limit) throws SQLException {
        String sql = "SELECT * FROM Chapters WHERE is_free = 1 ORDER BY view_count DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            List<Chapter> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    public void incrementViewCount(int chapterId) throws SQLException {
        String sql = "UPDATE Chapters SET view_count = view_count + 1 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, chapterId);
            ps.executeUpdate();
        }
    }
}
