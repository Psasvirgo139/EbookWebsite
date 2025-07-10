package com.mycompany.ebookwebsite.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.ebookwebsite.model.User;

public class UserDAO {

    private static final String SELECT_BY_USERNAME_AND_PASSWORD = "SELECT * FROM Users WHERE username = ? AND password_hash = ? AND (status != 'deleted' OR status IS NULL)";
    private static final String SELECT_BY_EMAIL_AND_PASSWORD = "SELECT * FROM Users WHERE email = ? AND password_hash = ? AND (status != 'deleted' OR status IS NULL)";
    private static final String INSERT = "INSERT INTO Users (username, email, password_hash, avatar_url, role, status, userinfor_id, created_at, last_login) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Users";
    private static final String SELECT_ACTIVE = "SELECT * FROM Users WHERE status != 'deleted' OR status IS NULL";
    private static final String SELECT_BY_ID = "SELECT * FROM Users WHERE id = ?";
    private static final String SELECT_BY_USERNAME = "SELECT * FROM Users WHERE username = ?";
    private static final String SELECT_BY_EMAIL = "SELECT * FROM Users WHERE email = ?";
    private static final String SEARCH_BY_NAME = "SELECT * FROM Users WHERE username LIKE ? AND (status != 'deleted' OR status IS NULL)";
    private static final String DELETE = "UPDATE Users SET status = 'deleted' WHERE id = ?";
    private static final String UPDATE = "UPDATE Users SET username = ?, email = ?, password_hash = ?, avatar_url = ?, role = ?, status = ?, userinfor_id = ?, last_login = ? WHERE id = ?";
    private static final String UPDATE_LAST_LOGIN = "UPDATE Users SET last_login = ? WHERE id = ?";
    private static final String COUNT_BY_USERNAME = "SELECT COUNT(*) FROM Users WHERE username = ? AND id != ? AND (status != 'deleted' OR status IS NULL)";
    private static final String COUNT_BY_EMAIL = "SELECT COUNT(*) FROM Users WHERE email = ? AND id != ? AND (status != 'deleted' OR status IS NULL)";

    // ===== Thêm kiểm tra tồn tại username / email =====
    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT 1 FROM Users WHERE username = ? AND (status != 'deleted' OR status IS NULL)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM Users WHERE email = ? AND (status != 'deleted' OR status IS NULL)";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ===== Đăng ký user mới =====
    public boolean insert(User user) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

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
                ps.setDate(8, Date.valueOf(user.getCreatedAt()));
            } else {
                ps.setNull(8, Types.DATE);
            }

            if (user.getLastLogin() != null) {
                ps.setDate(9, Date.valueOf(user.getLastLogin()));
            } else {
                ps.setNull(9, Types.DATE);
            }

            int rows = ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
            return rows > 0;
        }
    }

    public User findByUsernameAndPassword(String username, String passwordHash) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(SELECT_BY_USERNAME_AND_PASSWORD)) {

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

    public User findByEmailAndPassword(String email, String passwordHash) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(SELECT_BY_EMAIL_AND_PASSWORD)) {

            ps.setString(1, email);
            ps.setString(2, passwordHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }

    public List<User> searchByUsername(String searchName) throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(SEARCH_BY_NAME)) {

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
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {

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
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(SELECT_BY_USERNAME)) {

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
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(SELECT_BY_EMAIL)) {

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
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {

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
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(SELECT_ACTIVE)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUser(rs));
                }
            }
        }
        return list;
    }

    public boolean delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(DELETE)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(User user) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(UPDATE)) {

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
                ps.setDate(8, Date.valueOf(user.getLastLogin()));
            } else {
                ps.setNull(8, Types.DATE);
            }

            ps.setInt(9, user.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateLastLogin(int userId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(UPDATE_LAST_LOGIN)) {

            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public int countByUsername(String username, int excludeUserId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(COUNT_BY_USERNAME)) {

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
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(COUNT_BY_EMAIL)) {

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

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("avatar_url"),
                rs.getString("role"),
                (rs.getDate("created_at") != null) ? rs.getDate("created_at").toLocalDate() : null,
                (rs.getObject("userinfor_id") != null) ? rs.getInt("userinfor_id") : null,
                rs.getString("status"),
                (rs.getDate("last_login") != null) ? rs.getDate("last_login").toLocalDate() : null
        );
    }


    // 1. Đặt token reset cho user

    public boolean setResetToken(String email, String token, Timestamp expiry) throws SQLException {
        String sql = "UPDATE Users SET reset_token = ?, reset_token_expiry = ? WHERE email = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setTimestamp(2, expiry);
            ps.setString(3, email);
            return ps.executeUpdate() > 0;
        }
    }

// 2. Tìm user qua reset_token (và kiểm tra token chưa hết hạn)
    public User findByResetToken(String token) throws SQLException {
        String sql = "SELECT * FROM Users WHERE reset_token = ? AND reset_token_expiry > ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }

// 3. Xóa token reset sau khi đã đổi mật khẩu xong
    public boolean clearResetToken(int userId) throws SQLException {
        String sql = "UPDATE Users SET reset_token = NULL, reset_token_expiry = NULL WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

// 4. Cập nhật mật khẩu mới
    public boolean updatePassword(int userId, String newPasswordHash) throws SQLException {
        String sql = "UPDATE Users SET password_hash = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newPasswordHash);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // ===== COMPATIBILITY METHOD FOR UserServiceImpl =====
    
    /**
     * Login method for UserServiceImpl compatibility
     * @param usernameOrEmail username or email
     * @param passwordHash hashed password
     * @return User if found, null otherwise
     * @throws SQLException if database error
     */
    public User login(String usernameOrEmail, String passwordHash) throws SQLException {
        // Try username first
        User user = findByUsernameAndPassword(usernameOrEmail, passwordHash);
        if (user != null) {
            return user;
        }
        
        // Try email if username fails
        return findByEmailAndPassword(usernameOrEmail, passwordHash);
    }

}
