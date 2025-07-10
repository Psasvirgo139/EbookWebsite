package com.mycompany.ebookwebsite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.ebookwebsite.dao.DBConnection;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Favorite;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.model.UserRead;

public class AIRecommendationDAO {
    
    // Lấy lịch sử đọc của user
    public List<UserRead> getUserReadHistory(int userId) throws SQLException {
        List<UserRead> history = new ArrayList<>();
        String sql = "SELECT * FROM UserReads WHERE user_id = ? ORDER BY last_read_at DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UserRead read = new UserRead();
                    read.setUserID(rs.getInt("user_id"));
                    read.setEbookID(rs.getInt("ebook_id"));
                    read.setLastReadChapterID(rs.getInt("last_read_chapter_id"));
                    read.setLastReadAt(rs.getDate("last_read_at").toLocalDate());
                    history.add(read);
                }
            }
        }
        return history;
    }
    
    // Lấy danh sách yêu thích của user
    public List<Favorite> getUserFavorites(int userId) throws SQLException {
        List<Favorite> favorites = new ArrayList<>();
        String sql = "SELECT * FROM Favorites WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Favorite fav = new Favorite();
                    fav.setUserID(rs.getInt("user_id"));
                    fav.setEbookID(rs.getInt("ebook_id"));
                    fav.setChapterID(rs.getInt("chapter_id"));
                    fav.setCreatedAt(rs.getDate("created_at").toLocalDate());
                    favorites.add(fav);
                }
            }
        }
        return favorites;
    }
    
    // Lấy ebook theo ID
    public Ebook getEbookById(int ebookId) throws SQLException {
        String sql = "SELECT * FROM Ebooks WHERE id = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ebookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ebook ebook = new Ebook();
                    ebook.setId(rs.getInt("id"));
                    ebook.setTitle(rs.getString("title"));
                    ebook.setDescription(rs.getString("description"));
                    ebook.setReleaseType(rs.getString("release_type"));
                    ebook.setLanguage(rs.getString("language"));
                    ebook.setStatus(rs.getString("status"));
                    ebook.setVisibility(rs.getString("visibility"));
                    ebook.setUploaderId(rs.getInt("uploader_id"));
                    ebook.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    ebook.setViewCount(rs.getInt("view_count"));
                    ebook.setCoverUrl(rs.getString("cover_url"));
                    return ebook;
                }
            }
        }
        return null;
    }
    
    // Tìm kiếm ebook theo từ khóa
    public List<Ebook> searchEbooks(String keyword) throws SQLException {
        List<Ebook> ebooks = new ArrayList<>();
        String sql = "SELECT * FROM Ebooks WHERE title LIKE ? OR description LIKE ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String searchTerm = "%" + keyword + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ebook ebook = new Ebook();
                    ebook.setId(rs.getInt("id"));
                    ebook.setTitle(rs.getString("title"));
                    ebook.setDescription(rs.getString("description"));
                    ebook.setReleaseType(rs.getString("release_type"));
                    ebook.setLanguage(rs.getString("language"));
                    ebook.setStatus(rs.getString("status"));
                    ebook.setVisibility(rs.getString("visibility"));
                    ebook.setUploaderId(rs.getInt("uploader_id"));
                    ebook.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    ebook.setViewCount(rs.getInt("view_count"));
                    ebook.setCoverUrl(rs.getString("cover_url"));
                    ebooks.add(ebook);
                }
            }
        }
        return ebooks;
    }
    
    // Lấy ebook theo tác giả
    public List<Ebook> getEbooksByAuthor(String authorName) throws SQLException {
        List<Ebook> ebooks = new ArrayList<>();
        String sql = "SELECT e.* FROM Ebooks e " +
                    "JOIN EbookAuthors ea ON e.id = ea.ebook_id " +
                    "JOIN Authors a ON ea.author_id = a.id " +
                    "WHERE a.name LIKE ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + authorName + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ebook ebook = new Ebook();
                    ebook.setId(rs.getInt("id"));
                    ebook.setTitle(rs.getString("title"));
                    ebook.setDescription(rs.getString("description"));
                    ebook.setReleaseType(rs.getString("release_type"));
                    ebook.setLanguage(rs.getString("language"));
                    ebook.setStatus(rs.getString("status"));
                    ebook.setVisibility(rs.getString("visibility"));
                    ebook.setUploaderId(rs.getInt("uploader_id"));
                    ebook.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    ebook.setViewCount(rs.getInt("view_count"));
                    ebook.setCoverUrl(rs.getString("cover_url"));
                    ebooks.add(ebook);
                }
            }
        }
        return ebooks;
    }
    
    // Lấy ebook theo thể loại (tag)
    public List<Ebook> getEbooksByTag(String tagName) throws SQLException {
        List<Ebook> ebooks = new ArrayList<>();
        String sql = "SELECT e.* FROM Ebooks e " +
                    "JOIN EbookTags et ON e.id = et.ebook_id " +
                    "JOIN Tags t ON et.tag_id = t.id " +
                    "WHERE t.name LIKE ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + tagName + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ebook ebook = new Ebook();
                    ebook.setId(rs.getInt("id"));
                    ebook.setTitle(rs.getString("title"));
                    ebook.setDescription(rs.getString("description"));
                    ebook.setReleaseType(rs.getString("release_type"));
                    ebook.setLanguage(rs.getString("language"));
                    ebook.setStatus(rs.getString("status"));
                    ebook.setVisibility(rs.getString("visibility"));
                    ebook.setUploaderId(rs.getInt("uploader_id"));
                    ebook.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    ebook.setViewCount(rs.getInt("view_count"));
                    ebook.setCoverUrl(rs.getString("cover_url"));
                    ebooks.add(ebook);
                }
            }
        }
        return ebooks;
    }
    
    // Lấy user theo ID
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM Users WHERE id = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setAvatarUrl(rs.getString("avatar_url"));
                    user.setRole(rs.getString("role"));
                    user.setCreatedAt(rs.getDate("created_at").toLocalDate());
                    user.setUserinforId(rs.getInt("userinfor_id"));
                    user.setStatus(rs.getString("status"));
                    user.setLastLogin(rs.getDate("last_login") != null ? 
                                   rs.getDate("last_login").toLocalDate() : null);
                    return user;
                }
            }
        }
        return null;
    }
} 