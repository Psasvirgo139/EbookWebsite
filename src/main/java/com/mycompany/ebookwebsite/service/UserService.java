package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.model.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    /**
     * Xác thực đăng nhập người dùng
     */
    public User authenticateUser(String username, String password) throws SQLException {
        String hashedPassword = hashPassword(password);
        User user = userDAO.findByUsernameAndPassword(username, hashedPassword);
        
        if (user != null) {
            // Cập nhật last login
            userDAO.updateLastLogin(user.getId());
        }
        
        return user;
    }
    
    /**
     * Đăng ký user mới
     */
    public void registerUser(User user) throws SQLException {
        // Kiểm tra username đã tồn tại chưa
        if (isUsernameExists(user.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại: " + user.getUsername());
        }
        
        // Kiểm tra email đã tồn tại chưa
        if (isEmailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại: " + user.getEmail());
        }
        
        // Set default values
        setDefaultValues(user);
        
        // Hash password
        user.setPasswordHash(hashPassword(user.getPassword()));
        
        userDAO.insert(user);
    }
    
    /**
     * Tạo user mới
     */
    public User createUser(User user) throws SQLException {
        // Kiểm tra username đã tồn tại chưa
        if (isUsernameExists(user.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại: " + user.getUsername());
        }
        
        // Kiểm tra email đã tồn tại chưa
        if (isEmailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại: " + user.getEmail());
        }
        
        // Set default values
        setDefaultValues(user);
        
        // Hash password
        user.setPasswordHash(hashPassword(user.getPassword()));
        
        userDAO.insert(user);
        return user;
    }
    
    /**
     * Cập nhật thông tin user
     */
    public boolean updateUser(User user) throws SQLException {
        // Kiểm tra user có tồn tại không
        User existingUser = userDAO.findById(user.getId());
        if (existingUser == null) {
            throw new IllegalArgumentException("User không tồn tại với ID: " + user.getId());
        }
        
        // Kiểm tra username đã tồn tại chưa (trừ chính user này)
        if (isUsernameExists(user.getUsername(), user.getId())) {
            throw new IllegalArgumentException("Username đã tồn tại: " + user.getUsername());
        }
        
        // Kiểm tra email đã tồn tại chưa (trừ chính user này)
        if (isEmailExists(user.getEmail(), user.getId())) {
            throw new IllegalArgumentException("Email đã tồn tại: " + user.getEmail());
        }
        
        return userDAO.update(user);
    }
    
    /**
     * Xóa user (soft delete)
     */
    public boolean deleteUser(int userId) throws SQLException {
        User existingUser = userDAO.findById(userId);
        if (existingUser == null) {
            throw new IllegalArgumentException("User không tồn tại với ID: " + userId);
        }
        
        return userDAO.delete(userId);
    }
    
    /**
     * Lấy user theo ID
     */
    public User getUserById(int id) throws SQLException {
        return userDAO.findById(id);
    }
    
    /**
     * Lấy user theo username
     */
    public User getUserByUsername(String username) throws SQLException {
        return userDAO.findByUsername(username);
    }
    
    /**
     * Lấy user theo email
     */
    public User getUserByEmail(String email) throws SQLException {
        return userDAO.findByEmail(email);
    }
    
    /**
     * Tìm kiếm user theo tên
     */
    public List<User> searchUsers(String searchName) throws SQLException {
        return userDAO.searchByUsername(searchName.trim());
    }
    
    /**
     * Lấy tất cả user
     */
    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }
    
    /**
     * Lấy tất cả user đang hoạt động
     */
    public List<User> getActiveUsers() throws SQLException {
        return userDAO.findAllActive();
    }
    
    /**
     * Cập nhật mật khẩu user
     */
    public boolean updatePassword(int userId, String newPassword) throws SQLException {
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User không tồn tại với ID: " + userId);
        }
        
        user.setPasswordHash(hashPassword(newPassword));
        return userDAO.update(user);
    }
    
    /**
     * Kiểm tra username đã tồn tại chưa
     */
    public boolean isUsernameExists(String username) throws SQLException {
        return userDAO.countByUsername(username, 0) > 0;
    }
    
    /**
     * Kiểm tra username đã tồn tại chưa (trừ user có ID)
     */
    public boolean isUsernameExists(String username, int excludeUserId) throws SQLException {
        return userDAO.countByUsername(username, excludeUserId) > 0;
    }
    
    /**
     * Kiểm tra email đã tồn tại chưa
     */
    public boolean isEmailExists(String email) throws SQLException {
        return userDAO.countByEmail(email, 0) > 0;
    }
    
    /**
     * Kiểm tra email đã tồn tại chưa (trừ user có ID)
     */
    public boolean isEmailExists(String email, int excludeUserId) throws SQLException {
        return userDAO.countByEmail(email, excludeUserId) > 0;
    }
    
    /**
     * Set default values cho user
     */
    private void setDefaultValues(User user) {
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("user");
        }
        
        if (user.getStatus() == null || user.getStatus().trim().isEmpty()) {
            user.setStatus("active");
        }
        
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
    }
    
    /**
     * Hash password bằng SHA-256
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Đặt lại mật khẩu theo email (quên mật khẩu)
     */
    public boolean resetPassword(String email, String newPassword) throws SQLException {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email không tồn tại: " + email);
        }
        user.setPasswordHash(hashPassword(newPassword));
        return userDAO.update(user);
    }
}