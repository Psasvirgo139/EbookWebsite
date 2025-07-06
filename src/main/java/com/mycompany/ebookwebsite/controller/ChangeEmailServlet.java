package com.mycompany.ebookwebsite.controller;
/*ALTER TABLE Users
ADD
    changeEmailToken VARCHAR(255),
    changeEmailExpiry DATETIME,
    changeEmailNew VARCHAR(255);

	select*from users;
Thêm vào database để lưu mail tạm thời - sau khi người dùng nhấn xác nhận ở mail thì mới xóa mail tạm thời và chuyển qua email
*/
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.UserService;
import com.mycompany.ebookwebsite.utils.EmailUtil;
import com.mycompany.ebookwebsite.utils.TokenUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ChangeEmailServlet", urlPatterns = {"/change-email"})
public class ChangeEmailServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/user/change_email.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String newEmail = request.getParameter("newEmail");
        String password = request.getParameter("password");
        // Kiểm tra email mới đã tồn tại chưa
        try {
            if (userService.isEmailExists(newEmail, user.getId())) {
                request.setAttribute("error", "Email này đã được sử dụng!");
                request.getRequestDispatcher("/user/change_email.jsp").forward(request, response);
                return;
            }
            // Kiểm tra mật khẩu
            if (!userService.checkPassword(user.getId(), password)) {
                request.setAttribute("error", "Mật khẩu không đúng!");
                request.getRequestDispatcher("/user/change_email.jsp").forward(request, response);
                return;
            }
            // Sinh token xác nhận
            String token = TokenUtil.generateToken();
            Timestamp expiry = Timestamp.from(Instant.now().plus(30, ChronoUnit.MINUTES));
            userService.setChangeEmailToken(user.getId(), token, expiry, newEmail);
            // Gửi email xác nhận
            String confirmLink = request.getRequestURL().toString().replace("/change-email", "/confirm-change-email") + "?token=" + token;
            String subject = "Xác nhận đổi email tài khoản";
            String content = "<p>Bạn vừa yêu cầu đổi email cho tài khoản trên EbookWebsite.</p>" +
                    "<p>Nhấn vào <a href='" + confirmLink + "'>đây</a> để xác nhận đổi email. Link có hiệu lực trong 30 phút.</p>";
            try {
                EmailUtil.sendEmail(newEmail, subject, content);
            } catch (MessagingException mex) {
                mex.printStackTrace();
                request.setAttribute("error", "Không gửi được email xác nhận. Vui lòng thử lại!");
                request.getRequestDispatcher("/user/change_email.jsp").forward(request, response);
                return;
            }
            request.setAttribute("message", "Đã gửi email xác nhận tới địa chỉ mới. Vui lòng kiểm tra hộp thư!");
            request.getRequestDispatcher("/user/change_email.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống, vui lòng thử lại!");
            request.getRequestDispatcher("/user/change_email.jsp").forward(request, response);
        }
    }
} 