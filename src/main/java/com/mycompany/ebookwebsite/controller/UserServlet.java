package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.UserService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * UserServlet - Comprehensive user management for admin and authentication
 * Replaces UserController with full admin functionality
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
    
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        this.userService = new UserService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            switch (action != null ? action : "list") {
                case "list":
                    listUsers(request, response);
                    break;
                case "view":
                    viewUser(request, response);
                    break;
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                case "search":
                    searchUsers(request, response);
                    break;
                case "login":
                    showLoginForm(request, response);
                    break;
                case "logout":
                    logout(request, response);
                    break;
                default:
                    listUsers(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            switch (action != null ? action : "") {
                case "create":
                    createUser(request, response);
                    break;
                case "update":
                    updateUser(request, response);
                    break;
                case "updatePassword":
                    updatePassword(request, response);
                    break;
                case "login":
                    authenticateUser(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
    
    /**
     * Admin: List all users
     */
    private void listUsers(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/admin/user/list.jsp").forward(request, response);
    }
    
    /**
     * Admin: View user details
     */
    private void viewUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            User user = userService.getUserById(userId);
            
            if (user == null) {
                request.setAttribute("error", "User không tồn tại");
                listUsers(request, response);
                return;
            }
            
            request.setAttribute("user", user);
            request.getRequestDispatcher("/admin/user/view.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID user không hợp lệ");
            listUsers(request, response);
        }
    }
    
    /**
     * Admin: Show create user form
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/admin/user/new.jsp").forward(request, response);
    }
    
    /**
     * Admin: Show edit user form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            User user = userService.getUserById(userId);
            
            if (user == null) {
                request.setAttribute("error", "User không tồn tại");
                listUsers(request, response);
                return;
            }
            
            request.setAttribute("user", user);
            request.getRequestDispatcher("/admin/user/edit.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID user không hợp lệ");
            listUsers(request, response);
        }
    }
    
    /**
     * Admin: Create new user
     */
    private void createUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            String status = request.getParameter("status");
            
            // Basic validation
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username không được để trống");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email không được để trống");
            }
            if (password == null || password.length() < 6) {
                throw new IllegalArgumentException("Password phải có ít nhất 6 ký tự");
            }
            
            User user = new User();
            user.setUsername(username.trim());
            user.setEmail(email.trim());
            user.setPasswordHash(password); // UserService will handle hashing
            user.setRole(role != null ? role : "user");
            user.setStatus(status != null ? status : "active");
            
            User createdUser = userService.createUser(user);
            response.sendRedirect(request.getContextPath() + "/user?action=list&success=created");
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("username", request.getParameter("username"));
            request.setAttribute("email", request.getParameter("email"));
            request.setAttribute("role", request.getParameter("role"));
            request.setAttribute("status", request.getParameter("status"));
            request.getRequestDispatcher("/admin/user/new.jsp").forward(request, response);
        }
    }
    
    /**
     * Admin: Update user information
     */
    private void updateUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String role = request.getParameter("role");
            String status = request.getParameter("status");
            
            User user = userService.getUserById(userId);
            if (user == null) {
                request.setAttribute("error", "User không tồn tại");
                response.sendRedirect(request.getContextPath() + "/user?action=list");
                return;
            }
            
            user.setUsername(username.trim());
            user.setEmail(email.trim());
            user.setRole(role);
            user.setStatus(status);
            
            boolean updated = userService.updateUser(user);
            
            if (updated) {
                response.sendRedirect(request.getContextPath() + "/user?action=view&id=" + userId + "&success=updated");
            } else {
                request.setAttribute("error", "Không thể cập nhật user");
                request.setAttribute("user", user);
                request.getRequestDispatcher("/admin/user/edit.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/user?action=list");
        }
    }
    
    /**
     * Admin: Update user password
     */
    private void updatePassword(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            
            if (newPassword == null || newPassword.length() < 6) {
                throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự");
            }
            
            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
            }
            
            boolean updated = userService.updatePassword(userId, newPassword);
            
            if (updated) {
                response.sendRedirect(request.getContextPath() + "/user?action=view&id=" + userId + "&success=password_updated");
            } else {
                request.setAttribute("error", "Không thể cập nhật mật khẩu");
                request.getRequestDispatcher("/admin/user/changepassword.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/admin/user/changepassword.jsp").forward(request, response);
        }
    }
    
    /**
     * Admin: Delete user
     */
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            boolean deleted = userService.deleteUser(userId);
            
            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/user?action=list&success=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/user?action=list&error=delete_failed");
            }
            
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/user?action=list&error=" + e.getMessage());
        }
    }
    
    /**
     * Admin: Search users
     */
    private void searchUsers(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            String searchTerm = request.getParameter("search");
            
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                listUsers(request, response);
                return;
            }
            
            List<User> users = userService.searchUsers(searchTerm.trim());
            request.setAttribute("users", users);
            request.setAttribute("searchTerm", searchTerm.trim());
            request.getRequestDispatcher("/admin/user/list.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listUsers(request, response);
        }
    }
    
    /**
     * Show login form
     */
    private void showLoginForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/user/login.jsp").forward(request, response);
    }
    
    /**
     * Authenticate user login
     */
    private void authenticateUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            String usernameOrEmail = request.getParameter("usernameOrEmail");
            String password = request.getParameter("password");
            
            if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên đăng nhập không được để trống");
            }
            
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Mật khẩu không được để trống");
            }
            
            User user = userService.authenticateUserByUsernameOrEmail(usernameOrEmail.trim(), password);
            
            if (user != null) {
                // Login successful
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());
                
                // Redirect based on role
                if ("admin".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/user?action=list");
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }
                
            } else {
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
                request.setAttribute("usernameOrEmail", usernameOrEmail);
                request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("usernameOrEmail", request.getParameter("usernameOrEmail"));
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Logout user
     */
    private void logout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        response.sendRedirect(request.getContextPath() + "/user?action=login");
    }
} 