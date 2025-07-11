package com.mycompany.ebookwebsite.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.dao.UserInforDAO;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.model.UserInfor;

public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private UserDAO userDAO;
    private UserInforDAO userInforDAO; 

    public UserService() {
        this.userDAO = new UserDAO();
        this.userInforDAO = new UserInforDAO();
    }

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.userInforDAO = new UserInforDAO();
    }

    /**
     * Xác thực đăng nhập người dùng
     */
    public User authenticateUser(String username, String password) throws SQLException {
        // Lấy user theo username
        User user = userDAO.findByUsername(username);
        if (user == null) {
            return null;
        }

        // Băm password_hash từ database thành mật khẩu để đối chiếu
        String hashedPassword = hashPassword(password);
        if (hashedPassword.equals(user.getPasswordHash())) {
            // Cập nhật last login
            userDAO.updateLastLogin(user.getId());
            return user;
        }

        return null;
    }

    /**
     * Xác thực đăng nhập người dùng bằng username hoặc email
     */
    public User authenticateUserByUsernameOrEmail(String usernameOrEmail, String password) throws SQLException {
        // Thử tìm theo username trước
        User user = userDAO.findByUsername(usernameOrEmail);
        
        // Nếu không tìm thấy, thử tìm theo email
        if (user == null) {
            user = userDAO.findByEmail(usernameOrEmail);
        }

        if (user == null) {
            return null;
        }

        // Băm password_hash từ database thành mật khẩu để đối chiếu
        String hashedPassword = hashPassword(password);
        if (hashedPassword.equals(user.getPasswordHash())) {
            // Cập nhật last login
            userDAO.updateLastLogin(user.getId());
            return user;
        }

        return null;
    }

    /**
     * Tạo user mới
     */
    public User createUser(User user) throws SQLException {
        // Set default values
        setDefaultValues(user);

        // Băm mật khẩu thành password_hash và lưu vào database
        user.setPasswordHash(hashPassword(user.getPasswordHash()));

        userDAO.insert(user);
        return user;
    }

    /**
     * Cập nhật thông tin user
     */
    public boolean updateUser(User user) throws SQLException {
        return userDAO.update(user);
    }

    /**
     * Xóa user (soft delete)
     */
    public boolean deleteUser(int userId) throws SQLException {
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
        // Băm mật khẩu mới thành password_hash và lưu vào database
        String newPasswordHash = hashPassword(newPassword);
        return userDAO.updatePassword(userId, newPasswordHash);
    }

    /**
     * Kiểm tra username đã tồn tại chưa
     */
    public boolean isUsernameExists(String username, int excludeUserId) throws SQLException {
        return userDAO.countByUsername(username, excludeUserId) > 0;
    }

    /**
     * Kiểm tra email đã tồn tại chưa
     */
    public boolean isEmailExists(String email, int excludeUserId) throws SQLException {
        return userDAO.countByEmail(email, excludeUserId) > 0;
    }

    /**
     * Set default values cho user mới
     */
    private void setDefaultValues(User user) {
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("user");
        }
        if (user.getStatus() == null || user.getStatus().trim().isEmpty()) {
            user.setStatus("active");
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDate.now());
        }
    }

    /**
     * Băm mật khẩu bằng SHA-256
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Error hashing password", e);
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Kiểm tra đăng nhập (legacy method - giữ lại để tương thích)
     */
    public User checkLogin(String usernameOrEmail, String password) throws SQLException {
        return authenticateUserByUsernameOrEmail(usernameOrEmail, password);
    }

    /**
     * Lấy thông tin user
     */
    public UserInfor getUserInforById(Integer id) throws SQLException {
        if (id == null) {
            return null;
        }
        return userInforDAO.selectUserInfor(id);
    }

    public int countAllUsers() throws SQLException {
        return userDAO.countAllUsers();
    }

    public List<User> getLatestUsers(int limit) throws SQLException {
        return userDAO.getLatestUsers(limit);
    }

    public java.util.List<com.mycompany.ebookwebsite.model.AdminUserView> getAdminUserViews() throws java.sql.SQLException {
        java.util.List<User> users = getAllUsers();
        java.util.List<com.mycompany.ebookwebsite.model.AdminUserView> views = new java.util.ArrayList<>();
        for (User u : users) {
            views.add(new com.mycompany.ebookwebsite.model.AdminUserView(
                u.getId(), u.getUsername(), u.getEmail(), u.getRole(), u.getStatus(), u.getCreatedAt(), u.getLastLogin()
            ));
        }
        return views;
    }
}


