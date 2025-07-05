/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Favorite;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class FavoriteDAO {
    
    private static final String INSERT = "INSERT INTO Favorites (user_id, ebook_id, chapter_id) VALUES (?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Favorites";
    private static final String SELECT_BY_USER = "SELECT * FROM Favorites WHERE user_id = ? ORDER BY created_at DESC";
    private static final String SELECT_BY_EBOOK = "SELECT * FROM Favorites WHERE ebook_id = ?";
    private static final String SELECT_BY_USER_AND_EBOOK = "SELECT * FROM Favorites WHERE user_id = ? AND ebook_id = ?";
    private static final String SELECT_BY_USER_AND_CHAPTER = "SELECT * FROM Favorites WHERE user_id = ? AND chapter_id = ?";
    private static final String CHECK_FAVORITE = "SELECT COUNT(*) FROM Favorites WHERE user_id = ? AND ebook_id = ? AND (chapter_id = ? OR (chapter_id IS NULL AND ? IS NULL))";
    private static final String DELETE = "DELETE FROM Favorites WHERE user_id = ? AND ebook_id = ? AND (chapter_id = ? OR (chapter_id IS NULL AND ? IS NULL))";
    private static final String DELETE_BY_USER = "DELETE FROM Favorites WHERE user_id = ?";
    private static final String DELETE_BY_EBOOK = "DELETE FROM Favorites WHERE ebook_id = ?";
    private static final String DELETE_BY_CHAPTER = "DELETE FROM Favorites WHERE chapter_id = ?";
    
    public void insertFavorite(Favorite favorite) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT)) {
            
            ps.setInt(1, favorite.getUserID());
            ps.setInt(2, favorite.getEbookID());
            
            if (favorite.getChapterID() != null) {
                ps.setInt(3, favorite.getChapterID());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            ps.executeUpdate();
        }
    }
    
    public List<Favorite> selectAllFavorites() throws SQLException {
        List<Favorite> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapFavorite(rs));
            }
        }
        return list;
    }
    
    public List<Favorite> getFavoritesByUser(int userId) throws SQLException {
        List<Favorite> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapFavorite(rs));
            }
        }
        return list;
    }
    
    public List<Favorite> getFavoritesByEbook(int ebookId) throws SQLException {
        List<Favorite> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_EBOOK)) {
            
            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapFavorite(rs));
            }
        }
        return list;
    }
    
    public List<Favorite> getFavoritesByUserAndEbook(int userId, int ebookId) throws SQLException {
        List<Favorite> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER_AND_EBOOK)) {
            
            ps.setInt(1, userId);
            ps.setInt(2, ebookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapFavorite(rs));
            }
        }
        return list;
    }
    
    public List<Favorite> getFavoritesByUserAndChapter(int userId, int chapterId) throws SQLException {
        List<Favorite> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER_AND_CHAPTER)) {
            
            ps.setInt(1, userId);
            ps.setInt(2, chapterId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapFavorite(rs));
            }
        }
        return list;
    }
    
    public boolean isFavorite(int userId, int ebookId, Integer chapterId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(CHECK_FAVORITE)) {
            
            ps.setInt(1, userId);
            ps.setInt(2, ebookId);
            
            if (chapterId != null) {
                ps.setInt(3, chapterId);
                ps.setInt(4, chapterId);
            } else {
                ps.setNull(3, Types.INTEGER);
                ps.setNull(4, Types.INTEGER);
            }
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    public boolean deleteFavorite(int userId, int ebookId, Integer chapterId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            
            ps.setInt(1, userId);
            ps.setInt(2, ebookId);
            
            if (chapterId != null) {
                ps.setInt(3, chapterId);
                ps.setInt(4, chapterId);
            } else {
                ps.setNull(3, Types.INTEGER);
                ps.setNull(4, Types.INTEGER);
            }
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteByUser(int userId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE_BY_USER)) {
            
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteByEbook(int ebookId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE_BY_EBOOK)) {
            
            ps.setInt(1, ebookId);
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
    
    public int getTotalFavorites() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Favorites";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    private Favorite mapFavorite(ResultSet rs) throws SQLException {
        return new Favorite(
                rs.getInt("user_id"),
                rs.getInt("ebook_id"),
                (rs.getObject("chapter_id") != null) ? rs.getInt("chapter_id") : null,
                (rs.getDate("created_at") != null) ? rs.getDate("created_at").toLocalDate() : null
        );
    }
}