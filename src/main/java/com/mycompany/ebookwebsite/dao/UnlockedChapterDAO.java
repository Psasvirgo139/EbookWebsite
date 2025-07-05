package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.UnlockedChapter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO để quản lý chapter premium đã được mở khóa
 * @author ADMIN
 */
public class UnlockedChapterDAO {
    
    private static final String INSERT = "INSERT INTO UnlockedChapters (user_id, chapter_id, coin_spent, unlocked_at) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_USER_AND_CHAPTER = "SELECT * FROM UnlockedChapters WHERE user_id = ? AND chapter_id = ?";
    private static final String SELECT_BY_USER = "SELECT * FROM UnlockedChapters WHERE user_id = ?";
    private static final String SELECT_BY_CHAPTER = "SELECT * FROM UnlockedChapters WHERE chapter_id = ?";
    private static final String CHECK_UNLOCKED = "SELECT COUNT(*) FROM UnlockedChapters WHERE user_id = ? AND chapter_id = ?";
    private static final String DELETE_BY_USER = "DELETE FROM UnlockedChapters WHERE user_id = ?";
    private static final String DELETE_BY_CHAPTER = "DELETE FROM UnlockedChapters WHERE chapter_id = ?";
    
    public void insertUnlockedChapter(UnlockedChapter unlockedChapter) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, unlockedChapter.getUserId());
            ps.setInt(2, unlockedChapter.getChapterId());
            ps.setInt(3, unlockedChapter.getCoinSpent());
            ps.setTimestamp(4, Timestamp.valueOf(unlockedChapter.getUnlockedAt()));
            
            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    unlockedChapter.setId(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    public UnlockedChapter getUnlockedChapter(int userId, int chapterId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER_AND_CHAPTER)) {
            
            ps.setInt(1, userId);
            ps.setInt(2, chapterId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapUnlockedChapter(rs);
            }
        }
        return null;
    }
    
    public List<UnlockedChapter> getUnlockedChaptersByUser(int userId) throws SQLException {
        List<UnlockedChapter> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapUnlockedChapter(rs));
            }
        }
        return list;
    }
    
    public List<UnlockedChapter> getUnlockedChaptersByChapter(int chapterId) throws SQLException {
        List<UnlockedChapter> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_CHAPTER)) {
            
            ps.setInt(1, chapterId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapUnlockedChapter(rs));
            }
        }
        return list;
    }
    
    public boolean isChapterUnlocked(int userId, int chapterId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(CHECK_UNLOCKED)) {
            
            ps.setInt(1, userId);
            ps.setInt(2, chapterId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    public boolean deleteByUser(int userId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_BY_USER)) {
            
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteByChapter(int chapterId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_BY_CHAPTER)) {
            
            ps.setInt(1, chapterId);
            return ps.executeUpdate() > 0;
        }
    }
    
    private UnlockedChapter mapUnlockedChapter(ResultSet rs) throws SQLException {
        return new UnlockedChapter(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("chapter_id"),
                rs.getInt("coin_spent"),
                rs.getTimestamp("unlocked_at").toLocalDateTime()
        );
    }
} 