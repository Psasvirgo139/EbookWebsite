package com.mycompany.ebookwebsite.utils;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.model.User;

public class TokenUtil {
    private static final Logger LOGGER = Logger.getLogger(TokenUtil.class.getName());
    private static final int TOKEN_EXPIRY_HOURS = 24; // Token có hiệu lực 24 giờ
    
    /**
     * Tạo token reset password mới
     */
    public static String generateResetToken() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Kiểm tra token có hợp lệ không và trả về userId
     */
    public static int checkResetToken(String token) throws Exception {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token không được để trống");
        }
        
        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.findByResetToken(token);
            
            if (user == null) {
                throw new Exception("Token không tồn tại hoặc đã được sử dụng");
            }
            
            // Kiểm tra token có hết hạn không
            if (user.getResetTokenExpiry() != null && 
                user.getResetTokenExpiry().before(new Timestamp(System.currentTimeMillis()))) {
                throw new Exception("Token đã hết hạn");
            }
            
            return user.getId();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi kiểm tra token", e);
            throw new Exception("Lỗi hệ thống khi kiểm tra token");
        }
    }
    
    /**
     * Đánh dấu token đã được sử dụng
     */
    public static void markTokenUsed(String token) {
        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.findByResetToken(token);
            if (user != null) {
                userDAO.clearResetToken(user.getId());
                LOGGER.info("Token đã được đánh dấu sử dụng: " + token);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi đánh dấu token đã sử dụng", e);
        }
    }
    
    /**
     * Tạo token và lưu vào database cho user
     */
    public static String createAndSaveToken(String email) throws Exception {
        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.findByEmail(email);
            
            if (user == null) {
                throw new Exception("Email không tồn tại trong hệ thống");
            }
            
            String token = generateResetToken();
            Timestamp expiry = new Timestamp(System.currentTimeMillis() + (TOKEN_EXPIRY_HOURS * 60 * 60 * 1000));
            
            boolean saved = userDAO.setResetToken(email, token, expiry);
            if (!saved) {
                throw new Exception("Không thể tạo token reset password");
            }
            
            LOGGER.info("Token reset password đã được tạo cho user: " + email);
            return token;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tạo token reset password", e);
            throw new Exception("Lỗi hệ thống khi tạo token");
        }
    }
    
    public static String generateToken() {
        return java.util.UUID.randomUUID().toString().replace("-", "") + Long.toHexString(System.nanoTime());
    }
}
