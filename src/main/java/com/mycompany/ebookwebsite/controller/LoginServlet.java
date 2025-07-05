package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.UserService;
import com.mycompany.ebookwebsite.utils.UserValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
        
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        
        try {
            // Validation
            if (username == null || username.trim().isEmpty()) {
                request.setAttribute("error", "Tên đăng nhập không được để trống");
                doGet(request, response);
                return;
            }
            
            if (password == null || password.trim().isEmpty()) {
                request.setAttribute("error", "Mật khẩu không được để trống");
                doGet(request, response);
                return;
            }
            
            // Xử lý đăng nhập
            User user = userService.authenticateUser(username.trim(), password);
            
            if (user != null) {
                // Tạo session
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("userRole", user.getRole());
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("user", user);
                
                // Set session timeout (30 phút)
                session.setMaxInactiveInterval(30 * 60);
                
                // Remember me functionality
                if ("on".equals(rememberMe)) {
                    Cookie usernameCookie = new Cookie("rememberedUsername", username);
                    usernameCookie.setMaxAge(30 * 24 * 60 * 60); // 30 ngày
                    usernameCookie.setPath("/");
                    response.addCookie(usernameCookie);
                } else {
                    // Xóa cookie nếu không chọn remember me
                    Cookie usernameCookie = new Cookie("rememberedUsername", "");
                    usernameCookie.setMaxAge(0);
                    usernameCookie.setPath("/");
                    response.addCookie(usernameCookie);
                }
                
                // Redirect dựa trên role và redirect param
                String redirect = request.getParameter("redirect");
                if (redirect != null && !redirect.isEmpty()) {
                    response.sendRedirect(redirect);
                } else if ("admin".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/");
                }
                
            } else {
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
                request.setAttribute("username", username);
                doGet(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi đăng nhập: " + e.getMessage());
            request.setAttribute("username", username);
            doGet(request, response);
        }
    }
} 