package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.UserService;
import com.mycompany.ebookwebsite.utils.UserValidation;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
     * Hiển thị danh sách users
     */
    private void listUsers(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        List<User> users = userService.getAllUsers();
        request.setAttribute("users", users);
        
        // CHUYỂN HƯỚNG: Forward tới trang danh sách users
        request.getRequestDispatcher("/views/user/list.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị chi tiết user
     */
    private void viewUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            UserValidation.validateUserId(userId);
            
            User user = userService.getUserById(userId);
            if (user == null) {
                request.setAttribute("error", "User không tồn tại");
                // CHUYỂN HƯỚNG: Forward về trang danh sách khi user không tồn tại
                request.getRequestDispatcher("/views/user/list.jsp").forward(request, response);
                return;
            }
            
            request.setAttribute("user", user);
            // CHUYỂN HƯỚNG: Forward tới trang chi tiết user
            request.getRequestDispatcher("/views/user/view.jsp").forward(request, response);
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            // CHUYỂN HƯỚNG: Forward về trang danh sách khi có lỗi validation
            request.getRequestDispatcher("/views/user/list.jsp").forward(request, response);
        }
    }
    
    /**
     * Hiển thị form tạo user mới
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // CHUYỂN HƯỚNG: Forward tới trang form tạo user
        request.getRequestDispatcher("/views/user/new.jsp").forward(request, response);
    }
    
    /**
     * Hiển thị form chỉnh sửa user
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            UserValidation.validateUserId(userId);
            
            User user = userService.getUserById(userId);
            if (user == null) {
                request.setAttribute("error", "User không tồn tại");
                // CHUYỂN HƯỚNG: Forward về trang danh sách khi user không tồn tại
                request.getRequestDispatcher("/views/user/list.jsp").forward(request, response);
                return;
            }
            
            request.setAttribute("user", user);
            // CHUYỂN HƯỚNG: Forward tới trang form chỉnh sửa
            request.getRequestDispatcher("/views/user/edit.jsp").forward(request, response);
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            // CHUYỂN HƯỚNG: Forward về trang danh sách khi có lỗi validation
            request.getRequestDispatcher("/views/user/list.jsp").forward(request, response);
        }
    }
    
    /**
     * Tạo user mới
     */
    private void createUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            // Lấy dữ liệu từ form
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            String status = request.getParameter("status");
            
            // Tạo đối tượng User
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPasswordHash(password);
            user.setRole(role);
            user.setStatus(status);
            
            // Validate dữ liệu
            UserValidation.validateUserData(user, true);
            
            // Gọi service để tạo user
            User createdUser = userService.createUser(user);
            
            request.setAttribute("success", "Tạo user thành công");
            
            // CHUYỂN HƯỚNG: Redirect về trang danh sách sau khi tạo thành công
            response.sendRedirect(request.getContextPath() + "/user?action=list");
            
        } catch (IllegalArgumentException e) {
            // Validation hoặc business logic error
            request.setAttribute("error", e.getMessage());
            
            // Giữ lại dữ liệu đã nhập
            request.setAttribute("username", request.getParameter("username"));
            request.setAttribute("email", request.getParameter("email"));
            request.setAttribute("role", request.getParameter("role"));
            request.setAttribute("status", request.getParameter("status"));
            
            // CHUYỂN HƯỚNG: Forward về trang form khi có lỗi (giữ nguyên trang)
            request.getRequestDispatcher("/views/user/new.jsp").forward(request, response);
        }
    }
    
    /**
     * Cập nhật thông tin user
     */
    private void updateUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            // Lấy dữ liệu từ form
            int userId = Integer.parseInt(request.getParameter("id"));
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String role = request.getParameter("role");
            String status = request.getParameter("status");
            
            // Validate user ID
            UserValidation.validateUserId(userId);
            
            // Tạo đối tượng User
            User user = new User();
            user.setId(userId);
            user.setUsername(username);
            user.setEmail(email);
            user.setRole(role);
            user.setStatus(status);
            
            // Validate dữ liệu
            UserValidation.validateUserData(user, false);
            
            // Gọi service để cập nhật user
            boolean updated = userService.updateUser(user);
            
            if (updated) {
                request.setAttribute("success", "Cập nhật user thành công");
                // CHUYỂN HƯỚNG: Redirect về trang chi tiết user sau khi cập nhật thành công
                response.sendRedirect(request.getContextPath() + "/user?action=view&id=" + userId);
            } else {
                request.setAttribute("error", "Không thể cập nhật user");
                request.setAttribute("user", user);
                // CHUYỂN HƯỚNG: Forward về trang form khi cập nhật thất bại (giữ nguyên trang)
                request.getRequestDispatcher("/views/user/edit.jsp").forward(request, response);
            }
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            
            // Giữ lại dữ liệu đã nhập
            User user = new User();
            try {
                user.setId(Integer.parseInt(request.getParameter("id")));
            } catch (NumberFormatException ex) {
                user.setId(0);
            }
            user.setUsername(request.getParameter("username"));
            user.setEmail(request.getParameter("email"));
            user.setRole(request.getParameter("role"));
            user.setStatus(request.getParameter("status"));
            request.setAttribute("user", user);
            
            // CHUYỂN HƯỚNG: Forward về trang form khi có lỗi validation (giữ nguyên trang)
            request.getRequestDispatcher("/views/user/edit.jsp").forward(request, response);
        }
    }
    
    /**
     * Cập nhật mật khẩu user
     */
    private void updatePassword(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            
            // Validate
            UserValidation.validateUserId(userId);
            UserValidation.validatePassword(newPassword);
            
            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("Mật khẩu xác nhận không khớp");
            }
            
            // Gọi service để cập nhật mật khẩu
            boolean updated = userService.updatePassword(userId, newPassword);
            
            if (updated) {
                request.setAttribute("success", "Cập nhật mật khẩu thành công");
                // CHUYỂN HƯỚNG: Redirect về trang chi tiết user sau khi cập nhật thành công
                response.sendRedirect(request.getContextPath() + "/user?action=view&id=" + userId);
            } else {
                request.setAttribute("error", "Không thể cập nhật mật khẩu");
                // CHUYỂN HƯỚNG: Forward về trang đổi mật khẩu khi thất bại (giữ nguyên trang)
                request.getRequestDispatcher("/views/user/changepassword.jsp").forward(request, response);
            }
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            // CHUYỂN HƯỚNG: Forward về trang đổi mật khẩu khi có lỗi (giữ nguyên trang)
            request.getRequestDispatcher("/views/user/changepassword.jsp").forward(request, response);
        }
    }
    
    /**
     * Xóa user
     */
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            UserValidation.validateUserId(userId);
            
            boolean deleted = userService.deleteUser(userId);
            
            if (deleted) {
                request.setAttribute("success", "Xóa user thành công");
            } else {
                request.setAttribute("error", "Không thể xóa user");
            }
            
            // CHUYỂN HƯỚNG: Redirect về trang danh sách sau khi xóa
            response.sendRedirect(request.getContextPath() + "/user?action=list");
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            // CHUYỂN HƯỚNG: Redirect về trang danh sách khi có lỗi
            response.sendRedirect(request.getContextPath() + "/user?action=list");
        }
    }
    
    /**
     * Tìm kiếm users
     */
    private void searchUsers(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            String searchTerm = request.getParameter("search");
            UserValidation.validateSearchTerm(searchTerm);
            
            List<User> users = userService.searchUsers(searchTerm);
            request.setAttribute("users", users);
            request.setAttribute("searchTerm", searchTerm);
            
            // CHUYỂN HƯỚNG: Forward tới trang danh sách với kết quả tìm kiếm
            request.getRequestDispatcher("/views/user/list.jsp").forward(request, response);
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            List<User> users = userService.getAllUsers();
            request.setAttribute("users", users);
            // CHUYỂN HƯỚNG: Forward về trang danh sách khi có lỗi tìm kiếm (giữ nguyên trang)
            request.getRequestDispatcher("/views/user/list.jsp").forward(request, response);
        }
    }
    
    /**
     * Hiển thị form đăng nhập
     */
    private void showLoginForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // CHUYỂN HƯỚNG: Forward tới trang đăng nhập
        request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
    }
    
    /**
     * Xác thực đăng nhập
     */
    private void authenticateUser(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            // Validate dữ liệu đăng nhập
            UserValidation.validateLoginCredentials(username, password);
            
            // Gọi service để xác thực
            User user = userService.authenticateUser(username, password);
            
            if (user != null) {
                // Đăng nhập thành công
                HttpSession session = request.getSession();
                session.setAttribute("loggedInUser", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());
                
                // CHUYỂN HƯỚNG: Redirect tới trang chủ sau khi đăng nhập thành công
                response.sendRedirect(request.getContextPath() + "/home");
                
            } else {
                // Đăng nhập thất bại
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
                request.setAttribute("username", username); // Giữ lại username đã nhập
                
                // CHUYỂN HƯỚNG: Forward về trang đăng nhập khi thất bại (giữ nguyên trang)
                request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
            }
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("username", request.getParameter("username")); // Giữ lại username đã nhập
            
            // CHUYỂN HƯỚNG: Forward về trang đăng nhập khi có lỗi validation (giữ nguyên trang)
            request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Đăng xuất
     */
    private void logout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        // CHUYỂN HƯỚNG: Redirect về trang đăng nhập sau khi đăng xuất
        response.sendRedirect(request.getContextPath() + "/user?action=login");
    }
}