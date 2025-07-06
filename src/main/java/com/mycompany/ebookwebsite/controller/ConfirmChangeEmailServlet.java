package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.mycompany.ebookwebsite.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ConfirmChangeEmailServlet", urlPatterns = {"/confirm-change-email"})
public class ConfirmChangeEmailServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        if (token == null || token.isEmpty()) {
            request.setAttribute("error", "Token không hợp lệ!");
            request.getRequestDispatcher("/user/change_email.jsp").forward(request, response);
            return;
        }
        try {
            boolean success = userService.confirmChangeEmail(token);
            if (success) {
                int userId = getUserIdByToken(token);
                if (userId > 0) {
                    com.mycompany.ebookwebsite.model.User updatedUser = userService.getUserById(userId);
                    jakarta.servlet.http.HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.setAttribute("user", updatedUser);
                    }
                }
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            } else {
                request.setAttribute("error", "Token không hợp lệ hoặc đã hết hạn!");
            }
            request.getRequestDispatcher("/user/change_email.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống, vui lòng thử lại!");
            request.getRequestDispatcher("/user/change_email.jsp").forward(request, response);
        }
    }

    private int getUserIdByToken(String token) {
        try {
            com.mycompany.ebookwebsite.dao.UserDAO dao = new com.mycompany.ebookwebsite.dao.UserDAO();
            String sql = "SELECT id FROM Users WHERE changeEmailToken=?";
            try (java.sql.Connection con = com.mycompany.ebookwebsite.dao.DBConnection.getConnection();
                 java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, token);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
} 