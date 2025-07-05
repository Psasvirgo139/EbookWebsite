package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String email = request.getParameter("email");
        String otp = request.getParameter("otp");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");
        try {
            if (email == null || otp == null || password == null || confirm == null ||
                email.trim().isEmpty() || otp.trim().isEmpty() || password.trim().isEmpty() || confirm.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
                request.setAttribute("email", email);
                doGet(request, response);
                return;
            }
            String realOtp = ForgotPasswordServlet.getOtpForEmail(email.trim());
            if (realOtp == null || !realOtp.equals(otp.trim())) {
                request.setAttribute("error", "Mã xác nhận không đúng hoặc đã hết hạn.");
                request.setAttribute("email", email);
                doGet(request, response);
                return;
            }
            if (!password.equals(confirm)) {
                request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
                request.setAttribute("email", email);
                doGet(request, response);
                return;
            }
            // Đổi mật khẩu
            boolean ok = userService.resetPassword(email.trim(), password);
            if (ok) {
                ForgotPasswordServlet.removeOtpForEmail(email.trim());
                request.setAttribute("success", "Đổi mật khẩu thành công. Bạn có thể đăng nhập lại.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Đổi mật khẩu thất bại. Vui lòng thử lại.");
                request.setAttribute("email", email);
                doGet(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            request.setAttribute("email", email);
            doGet(request, response);
        }
    }
} 