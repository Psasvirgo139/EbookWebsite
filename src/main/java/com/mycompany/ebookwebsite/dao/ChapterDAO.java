package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Chapter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChapterDAO {

    private static final String INSERT = "INSERT INTO Chapters (ebook_id, volume_id, title, number, content_url, created_at, access_level, view_count, like_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_BY_EBOOK = "SELECT * FROM Chapters WHERE ebook_id = ? ORDER BY number";
    private static final String SELECT_BY_BOOK_AND_INDEX = "SELECT * FROM Chapters WHERE ebook_id = ? AND number = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM Chapters WHERE id = ?";
    private static final String UPDATE = "UPDATE Chapters SET ebook_id=?, volume_id=?, title=?, number=?, content_url=?, created_at=?, access_level=?, view_count=?, like_count=? WHERE id=?";
    private static final String DELETE = "DELETE FROM Chapters WHERE id = ?";

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
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SELECT_ALL_BY_EBOOK)) {
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
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SELECT_BY_BOOK_AND_INDEX)) {
            ps.setInt(1, ebookId);
            ps.setDouble(2, number);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    public Chapter getChapterById(int chapterId) throws SQLException {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, chapterId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        }
    }

    public void insertChapter(Chapter chapter) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(INSERT)) {
            ps.setInt(1, chapter.getEbookID());
            if (chapter.getVolumeID() != null) {
                ps.setInt(2, chapter.getVolumeID());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, chapter.getTitle());
            ps.setDouble(4, chapter.getNumber());
            ps.setString(5, chapter.getContentUrl());
            ps.setTimestamp(6, Timestamp.valueOf(chapter.getCreatedAt()));
            ps.setString(7, chapter.getAccessLevel());
            ps.setInt(8, chapter.getViewCount());
            ps.setInt(9, chapter.getLikeCount());
            ps.executeUpdate();
        }
    }

    public boolean updateChapter(Chapter chapter) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(UPDATE)) {
            ps.setInt(1, chapter.getEbookID());
            if (chapter.getVolumeID() != null) {
                ps.setInt(2, chapter.getVolumeID());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, chapter.getTitle());
            ps.setDouble(4, chapter.getNumber());
            ps.setString(5, chapter.getContentUrl());
            ps.setTimestamp(6, Timestamp.valueOf(chapter.getCreatedAt()));
            ps.setString(7, chapter.getAccessLevel());
            ps.setInt(8, chapter.getViewCount());
            ps.setInt(9, chapter.getLikeCount());
            ps.setInt(10, chapter.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteChapter(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Lấy n chương mới nhất, mỗi chương thuộc một ebook khác nhau
     */
    public List<Chapter> getLatestUniqueChapters(int limit) throws SQLException {
        String sql = "SELECT * FROM ( " +
                "SELECT *, ROW_NUMBER() OVER (PARTITION BY ebook_id ORDER BY created_at DESC) as rn " +
                "FROM Chapters " +
                ") t WHERE t.rn = 1 ORDER BY created_at DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        List<Chapter> chapters = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                chapters.add(mapRow(rs));
            }
        }
        return chapters;
    }
}
