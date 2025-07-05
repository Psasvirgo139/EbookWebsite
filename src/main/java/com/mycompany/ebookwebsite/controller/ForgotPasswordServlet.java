package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.UserService;
import com.mycompany.ebookwebsite.utils.EmailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private UserService userService;
    // Lưu tạm OTP cho từng email (demo, thực tế nên lưu vào DB hoặc cache)
    private static final Map<String, String> otpMap = new HashMap<>();

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        request.getRequestDispatcher("/forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String email = request.getParameter("email");
        try {
            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("error", "Vui lòng nhập email.");
                doGet(request, response);
                return;
            }
            User user = userService.getUserByEmail(email.trim());
            if (user == null) {
                request.setAttribute("error", "Email không tồn tại trong hệ thống.");
                doGet(request, response);
                return;
            }
            // Sinh mã OTP
            String otp = generateOTP();
            otpMap.put(email.trim(), otp);
            // Gửi email thực tế
            try {
                String subject = "Mã xác nhận đặt lại mật khẩu Ebook Website";
                String content = "<p>Xin chào,</p>" +
                        "<p>Bạn hoặc ai đó vừa yêu cầu đặt lại mật khẩu cho tài khoản tại Ebook Website.</p>" +
                        "<p>Mã xác nhận (OTP) của bạn là: <b>" + otp + "</b></p>" +
                        "<p>Mã này có hiệu lực trong vài phút. Nếu không phải bạn thực hiện, hãy bỏ qua email này.</p>";
                EmailUtil.sendEmail(email.trim(), subject, content);
                request.setAttribute("success", "Mã xác nhận đã được gửi tới email của bạn. Vui lòng kiểm tra hộp thư.");
            } catch (MessagingException ex) {
                request.setAttribute("error", "Không thể gửi email. Vui lòng thử lại sau. Lỗi: " + ex.getMessage());
                doGet(request, response);
                return;
            }
            request.setAttribute("email", email.trim());
            request.getRequestDispatcher("/reset-password.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            doGet(request, response);
        }
    }

    public static String getOtpForEmail(String email) {
        return otpMap.get(email);
    }
    public static void removeOtpForEmail(String email) {
        otpMap.remove(email);
    }
    private String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // 6 số
        return String.valueOf(otp);
    }
} 