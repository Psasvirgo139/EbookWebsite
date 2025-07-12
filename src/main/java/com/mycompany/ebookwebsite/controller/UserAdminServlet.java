package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.AdminUserView;
import com.mycompany.ebookwebsite.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserAdminServlet", urlPatterns = {"/admin/users"})
public class UserAdminServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        com.mycompany.ebookwebsite.model.User user =
                (session != null) ? (com.mycompany.ebookwebsite.model.User) session.getAttribute("user") : null;
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String editId = req.getParameter("editId");
        if (editId != null) {
            try {
                com.mycompany.ebookwebsite.model.User editUser = null;
                try {
                    editUser = new UserService().getUserById(Integer.parseInt(editId));
                } catch (java.sql.SQLException ex) {
                    throw new ServletException("Lỗi truy vấn user khi sửa", ex);
                }
                req.setAttribute("editUser", editUser);
                req.getRequestDispatcher("/admin/edit_user.jsp").forward(req, resp);
                return;
            } catch (Exception e) {
                throw new ServletException("Lỗi lấy thông tin user để sửa", e);
            }
        }
        try {
            List<AdminUserView> users = userService.getAdminUserViews();
            req.setAttribute("users", users);
        } catch (Exception e) {
            throw new ServletException("Lỗi lấy danh sách người dùng cho admin", e);
        }
        req.getRequestDispatcher("/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("update".equals(action)) {
            int userId = Integer.parseInt(req.getParameter("userId"));
            String username = req.getParameter("username");
            String email = req.getParameter("email");
            String role = req.getParameter("role");
            String status = req.getParameter("status");
            // Validate nếu cần
            if (username == null || username.trim().isEmpty()) {
                com.mycompany.ebookwebsite.model.User editUser = null;
                try {
                    editUser = new UserService().getUserById(userId);
                } catch (java.sql.SQLException ex) {
                    throw new ServletException("Lỗi truy vấn user khi validate", ex);
                }
                req.setAttribute("error", "Tên đăng nhập không hợp lệ!");
                req.setAttribute("editUser", editUser);
                req.getRequestDispatcher("/admin/edit_user.jsp").forward(req, resp);
                return;
            }
            try {
                UserService userService = new UserService();
                com.mycompany.ebookwebsite.model.User user = null;
                try {
                    user = userService.getUserById(userId);
                } catch (java.sql.SQLException ex) {
                    throw new ServletException("Lỗi truy vấn user khi cập nhật", ex);
                }
                if (user == null) {
                    throw new ServletException("Không tìm thấy user để cập nhật");
                }
                user.setUsername(username);
                user.setEmail(email);
                user.setRole(role);
                user.setStatus(status);
                try {
                    userService.updateUser(user);
                } catch (java.sql.SQLException ex) {
                    throw new ServletException("Lỗi cập nhật user vào DB", ex);
                }
                resp.sendRedirect(req.getContextPath() + "/admin/users");
            } catch (Exception e) {
                throw new ServletException("Lỗi cập nhật user", e);
            }
            return;
        }
        if ("delete".equals(action)) {
            int userId = Integer.parseInt(req.getParameter("userId"));
            HttpSession session = req.getSession(false);
            com.mycompany.ebookwebsite.model.User user =
                (session != null) ? (com.mycompany.ebookwebsite.model.User) session.getAttribute("user") : null;
            if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            try {
                try {
                    userService.deleteUser(userId);
                } catch (java.sql.SQLException ex) {
                    throw new ServletException("Lỗi xóa user trong DB", ex);
                }
                resp.sendRedirect(req.getContextPath() + "/admin/users");
            } catch (Exception e) {
                throw new ServletException("Lỗi xóa user", e);
            }
            return;
        }
    }
} 