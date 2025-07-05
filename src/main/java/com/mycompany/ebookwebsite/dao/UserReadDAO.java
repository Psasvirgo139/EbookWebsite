package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.UserRead;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserReadDAO {
    
    private static final String INSERT_OR_UPDATE = "MERGE UserRead AS target " +
            "USING (VALUES (?, ?, ?, ?)) AS source (user_id, ebook_id, last_read_chapter_id, last_read_at) " +
            "ON target.user_id = source.user_id AND target.ebook_id = source.ebook_id " +
            "WHEN MATCHED THEN UPDATE SET last_read_chapter_id = source.last_read_chapter_id, last_read_at = source.last_read_at " +
            "WHEN NOT MATCHED THEN INSERT (user_id, ebook_id, last_read_chapter_id, last_read_at) " +
            "VALUES (source.user_id, source.ebook_id, source.last_read_chapter_id, source.last_read_at);";
    
    private static final String SELECT_BY_USER_EBOOK = "SELECT * FROM UserRead WHERE user_id = ? AND ebook_id = ?";
    private static final String SELECT_BY_USER = "SELECT * FROM UserRead WHERE user_id = ?";
    private static final String SELECT_BY_EBOOK = "SELECT * FROM UserRead WHERE ebook_id = ?";
    private static final String SELECT_ALL = "SELECT * FROM UserRead";
    private static final String DELETE = "DELETE FROM UserRead WHERE user_id = ? AND ebook_id = ?";
    private static final String DELETE_BY_USER = "DELETE FROM UserRead WHERE user_id = ?";
    private static final String DELETE_BY_EBOOK = "DELETE FROM UserRead WHERE ebook_id = ?";

    public void insertOrUpdateUserRead(UserRead userRead) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT_OR_UPDATE)) {
            
            ps.setInt(1, userRead.getUserID());
            ps.setInt(2, userRead.getEbookID());
            
            if (userRead.getLastReadChapterID() != null) {
                ps.setInt(3, userRead.getLastReadChapterID());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            if (userRead.getLastReadAt() != null) {
                ps.setDate(4, Date.valueOf(userRead.getLastReadAt()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            
            ps.executeUpdate();
        }
    }

    public UserRead selectUserRead(int userID, int ebookID) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER_EBOOK)) {
            
            ps.setInt(1, userID);
            ps.setInt(2, ebookID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapUserRead(rs);
            }
        }
        return null;
    }

    public List<UserRead> selectByUser(int userID) throws SQLException {
        List<UserRead> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER)) {
            
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapUserRead(rs));
            }
        }
        return list;
    }

    public List<UserRead> selectByEbook(int ebookID) throws SQLException {
        List<UserRead> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_EBOOK)) {
            
            ps.setInt(1, ebookID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapUserRead(rs));
            }
        }
        return list;
    }

    public List<UserRead> selectAllUserRead() throws SQLException {
        List<UserRead> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapUserRead(rs));
            }
        }
        return list;
    }

    public boolean deleteUserRead(int userID, int ebookID) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            
            ps.setInt(1, userID);
            ps.setInt(2, ebookID);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteByUser(int userID) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE_BY_USER)) {
            
            ps.setInt(1, userID);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteByEbook(int ebookID) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE_BY_EBOOK)) {
            
            ps.setInt(1, ebookID);
            return ps.executeUpdate() > 0;
        }
    }

    public int getTotalReads() throws SQLException {
        String sql = "SELECT COUNT(*) FROM UserRead";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Ebook> getTopBooksByReads(int limit) throws SQLException {
        List<Ebook> books = new ArrayList<>();
        String sql = "SELECT e.*, COUNT(ur.user_id) as read_count FROM Ebooks e " +
                    "LEFT JOIN UserRead ur ON e.id = ur.ebook_id " +
                    "GROUP BY e.id, e.title, e.description, e.release_type, e.language, e.status, e.visibility, e.uploader_id, e.created_at, e.view_count, e.cover_url, e.is_premium, e.price " +
                    "ORDER BY read_count DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Ebook book = new Ebook();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setDescription(rs.getString("description"));
                book.setReleaseType(rs.getString("release_type"));
                book.setLanguage(rs.getString("language"));
                book.setStatus(rs.getString("status"));
                book.setVisibility(rs.getString("visibility"));
                book.setUploaderId(rs.getInt("uploader_id"));
                book.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                book.setViewCount(rs.getInt("view_count"));
                book.setCoverUrl(rs.getString("cover_url"));
                books.add(book);
            }
        }
        return books;
    }

    public List<User> getTopUsersByActivity(int limit) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, COUNT(ur.ebook_id) as read_count FROM Users u " +
                    "LEFT JOIN UserRead ur ON u.id = ur.user_id " +
                    "WHERE (u.status != 'deleted' OR u.status IS NULL) " +
                    "GROUP BY u.id, u.username, u.email, u.password_hash, u.avatar_url, u.role, u.created_at, u.userinfor_id, u.status, u.last_login, u.is_premium " +
                    "ORDER BY read_count DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setAvatarUrl(rs.getString("avatar_url"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setUserinforId(rs.getInt("userinfor_id"));
                user.setStatus(rs.getString("status"));
                user.setLastLogin(rs.getTimestamp("last_login") != null ? rs.getTimestamp("last_login").toLocalDateTime() : null);
                user.setPremium(rs.getBoolean("is_premium"));
                users.add(user);
            }
        }
        return users;
    }

    private UserRead mapUserRead(ResultSet rs) throws SQLException {
        return new UserRead(
                rs.getInt("user_id"),
                rs.getInt("ebook_id"),
                (rs.getObject("last_read_chapter_id") != null) ? rs.getInt("last_read_chapter_id") : null,
                (rs.getDate("last_read_at") != null) ? rs.getDate("last_read_at").toLocalDate() : null
        );
    }
}