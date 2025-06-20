package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.bean.LoginBean;
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

        // Lấy LoginBean đã được binding (nếu có)
        LoginBean login = (LoginBean) request.getAttribute("login");
        if (login == null) { // fallback khi POST trực tiếp không qua JSP
            login = new LoginBean();
            login.setUsernameOrEmail(request.getParameter("usernameOrEmail"));
            login.setPassword(request.getParameter("password"));
        }

        // Gọi service kiểm tra đăng nhập
        User user = userService.checkLogin(
                login.getUsernameOrEmail(), login.getPassword());

        if (user == null) {
            // Sai thông tin
            login.setError("Sai tài khoản hoặc mật khẩu!");
            request.setAttribute("login", login);
            request.getRequestDispatcher("user/login.jsp").forward(request, response);
            return;
        }

        // Đăng nhập thành công, lưu user vào session
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        // Chuyển hướng tuỳ theo vai trò
        String target = "admin".equalsIgnoreCase(user.getRole())
                ? "/admin/dashboard"
                : "/profile";
        response.sendRedirect(request.getContextPath() + target);
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý đăng nhập người dùng, phân quyền user/admin";
    }
}
