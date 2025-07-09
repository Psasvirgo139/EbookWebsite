package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.UserService;
import com.mycompany.ebookwebsite.utils.UserValidation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        // Khởi tạo UserService
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

        // Lấy thông tin từ form
        String usernameOrEmail = request.getParameter("usernameOrEmail");
        String password = request.getParameter("password");

        // Validate dữ liệu đầu vào
        try {
            UserValidation.validateLoginCredentials(usernameOrEmail, password);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("user/login.jsp").forward(request, response);
            return;
        }

        // Gọi service kiểm tra đăng nhập
        User user = null;
        try {
            user = userService.authenticateUserByUsernameOrEmail(usernameOrEmail, password);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống, vui lòng thử lại sau!");
            request.getRequestDispatcher("user/login.jsp").forward(request, response);
            return;
        }

        if (user == null) {
            // Sai thông tin
            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            request.getRequestDispatcher("user/login.jsp").forward(request, response);
            return;
        }

        // Đăng nhập thành công, lưu user vào session
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        // Kiểm tra xem có URL gốc cần quay lại không
        String redirect = (String) session.getAttribute("redirectAfterLogin");
        if (redirect != null) {
            session.removeAttribute("redirectAfterLogin");
            response.sendRedirect(request.getContextPath() + redirect);
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý đăng nhập người dùng, chuyển hướng về trang chủ";
    }
}
