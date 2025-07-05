package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.User;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final String SELECT_BY_USERNAME_AND_PASSWORD = "SELECT * FROM Users WHERE username = ? AND password_hash = ? AND (status != 'deleted' OR status IS NULL)";
    private static final String INSERT = "INSERT INTO Users (username, email, password_hash, avatar_url, role, status, userinfor_id, created_at, last_login, is_premium) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Users";
    private static final String SELECT_ACTIVE = "SELECT * FROM Users WHERE status != 'deleted' OR status IS NULL";
    private static final String SELECT_BY_ID = "SELECT * FROM Users WHERE id = ?";
    private static final String SELECT_BY_USERNAME = "SELECT * FROM Users WHERE username = ?";
    private static final String SELECT_BY_EMAIL = "SELECT * FROM Users WHERE email = ?";
    private static final String SEARCH_BY_NAME = "SELECT * FROM Users WHERE username LIKE ? AND (status != 'deleted' OR status IS NULL)";
    private static final String DELETE = "UPDATE Users SET status = 'deleted' WHERE id = ?";
    private static final String UPDATE = "UPDATE Users SET username = ?, email = ?, password_hash = ?, avatar_url = ?, role = ?, status = ?, userinfor_id = ?, last_login = ?, is_premium = ? WHERE id = ?";
    private static final String UPDATE_LAST_LOGIN = "UPDATE Users SET last_login = ? WHERE id = ?";
    private static final String COUNT_BY_USERNAME = "SELECT COUNT(*) FROM Users WHERE username = ? AND id != ? AND (status != 'deleted' OR status IS NULL)";
    private static final String COUNT_BY_EMAIL = "SELECT COUNT(*) FROM Users WHERE email = ? AND id != ? AND (status != 'deleted' OR status IS NULL)";

    // New SQL constants for premium user management
    private static final String SELECT_PREMIUM_USERS = "SELECT * FROM Users WHERE is_premium = 1 AND (status != 'deleted' OR status IS NULL)";
    private static final String SELECT_FREE_USERS = "SELECT * FROM Users WHERE is_premium = 0 AND (status != 'deleted' OR status IS NULL)";
    private static final String COUNT_PREMIUM_USERS = "SELECT COUNT(*) FROM Users WHERE is_premium = 1 AND (status != 'deleted' OR status IS NULL)";
    private static final String COUNT_FREE_USERS = "SELECT COUNT(*) FROM Users WHERE is_premium = 0 AND (status != 'deleted' OR status IS NULL)";
    private static final String UPDATE_PREMIUM_STATUS = "UPDATE Users SET is_premium = ? WHERE id = ?";

    public User findByUsernameAndPassword(String username, String passwordHash) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USERNAME_AND_PASSWORD)) {

            ps.setString(1, username);
            ps.setString(2, passwordHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }

    public void insert(User user) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getAvatarUrl());
            ps.setString(5, user.getRole());
            ps.setString(6, user.getStatus());

            if (user.getUserinforId() != null) {
                ps.setInt(7, user.getUserinforId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            if (user.getCreatedAt() != null) {
                ps.setTimestamp(8, Timestamp.valueOf(user.getCreatedAt()));
            } else {
                ps.setNull(8, Types.TIMESTAMP);
            }

            if (user.getLastLogin() != null) {
                ps.setTimestamp(9, Timestamp.valueOf(user.getLastLogin()));
            } else {
                ps.setNull(9, Types.TIMESTAMP);
            }

            ps.setBoolean(10, user.isPremium());

            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<User> searchByUsername(String searchName) throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SEARCH_BY_NAME)) {

            ps.setString(1, "%" + searchName + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUser(rs));
                }
            }
        }
        return list;
    }

    public User findById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }

    public User findByUsername(String username) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USERNAME)) {

            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }

    public User findByEmail(String email) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_EMAIL)) {

            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }

    public List<User> findAll() throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUser(rs));
                }
            }
        }
        return list;
    }

    public List<User> findAllActive() throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ACTIVE)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUser(rs));
                }
            }
        }
        return list;
    }

    public boolean delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(User user) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getAvatarUrl());
            ps.setString(5, user.getRole());
            ps.setString(6, user.getStatus());

            if (user.getUserinforId() != null) {
                ps.setInt(7, user.getUserinforId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            if (user.getLastLogin() != null) {
                ps.setTimestamp(8, Timestamp.valueOf(user.getLastLogin()));
            } else {
                ps.setNull(8, Types.TIMESTAMP);
            }

            ps.setBoolean(9, user.isPremium());

            ps.setInt(10, user.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateLastLogin(int userId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE_LAST_LOGIN)) {

            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public int countByUsername(String username, int excludeUserId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(COUNT_BY_USERNAME)) {

            ps.setString(1, username);
            ps.setInt(2, excludeUserId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int countByEmail(String email, int excludeUserId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(COUNT_BY_EMAIL)) {

            ps.setString(1, email);
            ps.setInt(2, excludeUserId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    // New methods for premium user management
    public List<User> getPremiumUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_PREMIUM_USERS)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUser(rs));
                }
            }
        }
        return list;
    }

    public List<User> getFreeUsers() throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_FREE_USERS)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUser(rs));
                }
            }
        }
        return list;
    }

    public int countPremiumUsers() throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(COUNT_PREMIUM_USERS)) {

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int countFreeUsers() throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(COUNT_FREE_USERS)) {

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int countAllUsers() throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM Users WHERE (status != 'deleted' OR status IS NULL)")) {

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean updatePremiumStatus(int userId, boolean isPremium) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE_PREMIUM_STATUS)) {

            ps.setBoolean(1, isPremium);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<User> getRecentPremiumUsers(int limit) throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE is_premium = 1 AND (status != 'deleted' OR status IS NULL) ORDER BY created_at DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUser(rs));
                }
            }
        }
        return list;
    }

    public List<User> getRecentFreeUsers(int limit) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE is_premium = 0 AND (status != 'deleted' OR status IS NULL) ORDER BY created_at DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        }

        return users;
    }

    public List<User> getRecentUsers(int limit) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE (status != 'deleted' OR status IS NULL) ORDER BY created_at DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        }

        return users;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("avatar_url"),
                rs.getString("role"),
                (rs.getTimestamp("created_at") != null) ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                (rs.getObject("userinfor_id") != null) ? rs.getInt("userinfor_id") : null,
                rs.getString("status"),
                (rs.getTimestamp("last_login") != null) ? rs.getTimestamp("last_login").toLocalDateTime() : null,
                rs.getBoolean("is_premium")
        );
    }
}