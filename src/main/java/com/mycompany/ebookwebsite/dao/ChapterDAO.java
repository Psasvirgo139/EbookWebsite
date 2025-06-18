package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Chapter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChapterDAO {
    
    private static final String INSERT = "INSERT INTO Chapters (ebook_id, volume_id, title, number, content_url, access_level, view_count, like_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Chapters ORDER BY ebook_id, number";
    private static final String SELECT_BY_ID = "SELECT * FROM Chapters WHERE id = ?";
    private static final String SELECT_BY_EBOOK = "SELECT * FROM Chapters WHERE ebook_id = ? ORDER BY number";
    private static final String SELECT_BY_VOLUME = "SELECT * FROM Chapters WHERE volume_id = ? ORDER BY number";
    private static final String SEARCH_BY_TITLE = "SELECT * FROM Chapters WHERE title LIKE ? ORDER BY ebook_id, number";
    private static final String SELECT_BY_ACCESS_LEVEL = "SELECT * FROM Chapters WHERE access_level = ? ORDER BY ebook_id, number";
    private static final String UPDATE = "UPDATE Chapters SET ebook_id = ?, volume_id = ?, title = ?, number = ?, content_url = ?, access_level = ?, view_count = ?, like_count = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM Chapters WHERE id = ?";
    private static final String INCREMENT_VIEW = "UPDATE Chapters SET view_count = view_count + 1 WHERE id = ?";
    private static final String INCREMENT_LIKE = "UPDATE Chapters SET like_count = like_count + 1 WHERE id = ?";
    private static final String DECREMENT_LIKE = "UPDATE Chapters SET like_count = like_count - 1 WHERE id = ? AND like_count > 0";
    
    public void insertChapter(Chapter chapter) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT)) {
            
            ps.setInt(1, chapter.getEbookID());
            
            if (chapter.getVolumeID() != null) {
                ps.setInt(2, chapter.getVolumeID());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            
            ps.setString(3, chapter.getTitle());
            ps.setDouble(4, chapter.getNumber());
            ps.setString(5, chapter.getContentUrl());
            ps.setString(6, chapter.getAccessLevel());
            ps.setInt(7, chapter.getViewCount());
            ps.setInt(8, chapter.getLikeCount());
            
            ps.executeUpdate();
        }
    }
    
    public List<Chapter> selectAllChapters() throws SQLException {
        List<Chapter> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapChapter(rs));
            }
        }
        return list;
    }
    
    public Chapter selectChapter(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapChapter(rs);
            }
        }
        return null;
    }
    
    public List<Chapter> selectChaptersByEbook(int ebookId) throws SQLException {
        List<Chapter> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_EBOOK)) {
            
            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapChapter(rs));
            }
        }
        return list;
    }
    
    public List<Chapter> selectChaptersByVolume(int volumeId) throws SQLException {
        List<Chapter> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_VOLUME)) {
            
            ps.setInt(1, volumeId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapChapter(rs));
            }
        }
        return list;
    }
    
    public List<Chapter> search(String searchTitle) throws SQLException {
        List<Chapter> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SEARCH_BY_TITLE)) {
            
            ps.setString(1, "%" + searchTitle + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapChapter(rs));
            }
        }
        return list;
    }
    
    public List<Chapter> selectChaptersByAccessLevel(String accessLevel) throws SQLException {
        List<Chapter> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ACCESS_LEVEL)) {
            
            ps.setString(1, accessLevel);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapChapter(rs));
            }
        }
        return list;
    }
    
    public boolean updateChapter(Chapter chapter) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            
            ps.setInt(1, chapter.getEbookID());
            
            if (chapter.getVolumeID() != null) {
                ps.setInt(2, chapter.getVolumeID());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            
            ps.setString(3, chapter.getTitle());
            ps.setDouble(4, chapter.getNumber());
            ps.setString(5, chapter.getContentUrl());
            ps.setString(6, chapter.getAccessLevel());
            ps.setInt(7, chapter.getViewCount());
            ps.setInt(8, chapter.getLikeCount());
            ps.setInt(9, chapter.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteChapter(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean incrementViewCount(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INCREMENT_VIEW)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean incrementLikeCount(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INCREMENT_LIKE)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean decrementLikeCount(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DECREMENT_LIKE)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    private Chapter mapChapter(ResultSet rs) throws SQLException {
        return new Chapter(
            rs.getInt("id"),
            rs.getInt("ebook_id"),
            (rs.getObject("volume_id") != null) ? rs.getInt("volume_id") : null,
            rs.getString("title"),
            rs.getDouble("number"),
            rs.getString("content_url"),
            (rs.getDate("created_at") != null) ? rs.getDate("created_at").toLocalDate() : null,
            rs.getString("access_level"),
            rs.getInt("view_count"),
            rs.getInt("like_count")
        );
    }
}