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
}
