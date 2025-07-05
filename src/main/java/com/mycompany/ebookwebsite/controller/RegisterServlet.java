package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.UserService;
import com.mycompany.ebookwebsite.utils.UserValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        // Kiểm tra nếu đã đăng nhập thì redirect về trang chủ
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        try {
            // Validation
            if (username == null || username.trim().isEmpty()) {
                request.setAttribute("error", "Tên đăng nhập không được để trống");
                setFormData(request, username, email, null);
                doGet(request, response);
                return;
            }
            
            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("error", "Email không được để trống");
                setFormData(request, username, email, null);
                doGet(request, response);
                return;
            }
            
            if (password == null || password.trim().isEmpty()) {
                request.setAttribute("error", "Mật khẩu không được để trống");
                setFormData(request, username, email, null);
                doGet(request, response);
                return;
            }
            
            if (confirmPassword == null || !password.equals(confirmPassword)) {
                request.setAttribute("error", "Mật khẩu xác nhận không khớp");
                setFormData(request, username, email, null);
                doGet(request, response);
                return;
            }
            
            // Validate format
            username = username.trim();
            email = email.trim();
            password = password.trim();
            
            if (!UserValidation.isValidUsername(username)) {
                request.setAttribute("error", "Tên đăng nhập chỉ được chứa chữ cái, số và dấu gạch dưới (3-20 ký tự)");
                setFormData(request, username, email, null);
                doGet(request, response);
                return;
            }
            
            if (!UserValidation.isValidEmail(email)) {
                request.setAttribute("error", "Email không hợp lệ");
                setFormData(request, username, email, null);
                doGet(request, response);
                return;
            }
            
            if (!UserValidation.isValidPassword(password)) {
                request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
                setFormData(request, username, email, null);
                doGet(request, response);
                return;
            }
            
            // Kiểm tra username và email đã tồn tại
            if (userService.isUsernameExists(username)) {
                request.setAttribute("error", "Tên đăng nhập đã tồn tại");
                setFormData(request, username, email, null);
                doGet(request, response);
                return;
            }
            
            if (userService.isEmailExists(email)) {
                request.setAttribute("error", "Email đã được sử dụng");
                setFormData(request, username, email, null);
                doGet(request, response);
                return;
            }
            
            // Tạo user mới
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password); // Service sẽ hash password
            user.setRole("user"); // Mặc định là user
            user.setStatus("active");
            user.setCreatedAt(LocalDateTime.now());
            
            userService.registerUser(user);
            
            // Redirect về trang login với thông báo thành công
            request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            response.sendRedirect(request.getContextPath() + "/login");
            
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi đăng ký: " + e.getMessage());
            setFormData(request, username, email, null);
            doGet(request, response);
        }
    }
    
    private void setFormData(HttpServletRequest request, String username, String email, String fullName) {
        request.setAttribute("username", username);
        request.setAttribute("email", email);
    }
} 