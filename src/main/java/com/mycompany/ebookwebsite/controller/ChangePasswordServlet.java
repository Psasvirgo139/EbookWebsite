package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/change-password"})
public class ChangePasswordServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ChangePasswordServlet.class.getName());
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("user");
        }
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Hiển thị trang đổi mật khẩu
        request.getRequestDispatcher("user/change_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = null;
        if (session != null) {
            currentUser = (User) session.getAttribute("user");
        }
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String message = "";
        String messageType = "success";

        try {
            // Lấy thông tin từ form
            String currentPassword = request.getParameter("current_password");
            String newPassword = request.getParameter("new_password");
            String confirmPassword = request.getParameter("confirm_password");

            LOGGER.info("User " + currentUser.getUsername() + " attempting to change password");

            // Validate dữ liệu
            if (currentPassword == null || currentPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập mật khẩu hiện tại");
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập mật khẩu mới");
            }
            if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng xác nhận mật khẩu mới");
            }

            // Kiểm tra độ dài mật khẩu mới
            if (newPassword.length() < 6) {
                throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 6 ký tự");
            }

            // Kiểm tra mật khẩu xác nhận
            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
            }

            // Kiểm tra mật khẩu mới không trùng với mật khẩu hiện tại
            if (newPassword.equals(currentPassword)) {
                throw new IllegalArgumentException("Mật khẩu mới không được trùng với mật khẩu hiện tại");
            }

            // Băm mật khẩu hiện tại để đối chiếu với password_hash trong database
            String currentPasswordHash = hashPassword(currentPassword);
            
            // Kiểm tra mật khẩu hiện tại
            if (!currentPasswordHash.equals(currentUser.getPasswordHash())) {
                throw new IllegalArgumentException("Mật khẩu hiện tại không đúng");
            }

            // Băm mật khẩu mới thành password_hash và lưu vào database
            String newPasswordHash = hashPassword(newPassword);
            LOGGER.info("Generated new SHA-256 hash for user " + currentUser.getUsername());

            // Cập nhật mật khẩu trong database
            boolean success = userDAO.updatePassword(currentUser.getId(), newPasswordHash);

            if (success) {
                // Cập nhật session với mật khẩu mới
                currentUser.setPasswordHash(newPasswordHash);
                session.setAttribute("user", currentUser);
                
                LOGGER.info("Password changed successfully for user " + currentUser.getUsername());
                message = "Đổi mật khẩu thành công!";
                messageType = "success";
                
                // Redirect về trang profile với thông báo thành công
                session.setAttribute("successMessage", message);
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            } else {
                LOGGER.warning("Failed to update password in database for user " + currentUser.getUsername());
                message = "Có lỗi xảy ra khi cập nhật mật khẩu!";
                messageType = "error";
            }

        } catch (IllegalArgumentException e) {
            LOGGER.warning("Validation error: " + e.getMessage());
            message = e.getMessage();
            messageType = "error";
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: " + e.getMessage(), e);
            message = "Lỗi database: " + e.getMessage();
            messageType = "error";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
            message = "Có lỗi xảy ra: " + e.getMessage();
            messageType = "error";
        }

        // Truyền thông báo cho JSP
        request.setAttribute("message", message);
        request.setAttribute("messageType", messageType);
        request.getRequestDispatcher("user/change_password.jsp").forward(request, response);
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

    @Override
    public String getServletInfo() {
        return "Servlet xử lý đổi mật khẩu người dùng";
    }
} 