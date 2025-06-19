package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.UserService;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        // Khởi tạo UserService (nếu dùng DI có thể thay thế)
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chuyển hướng về trang login (jsp)
        request.getRequestDispatcher("user/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usernameOrEmail = request.getParameter("usernameOrEmail");
        String password = request.getParameter("password");

        if (usernameOrEmail != null) usernameOrEmail = usernameOrEmail.trim();
        if (password != null) password = password.trim();

        User user = userService.checkLogin(usernameOrEmail, password);

        if (user != null) {
            if ("banned".equalsIgnoreCase(user.getStatus())) {
                request.setAttribute("error", "Tài khoản của bạn đã bị khóa!");
                request.getRequestDispatcher("user/login.jsp").forward(request, response);
                return;
            }
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Điều hướng theo role
            if ("admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                return;
            } else {
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }
        } else {
            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            request.setAttribute("username", usernameOrEmail);
            request.getRequestDispatcher("user/login.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý đăng nhập người dùng, phân quyền user/admin";
    }
}
