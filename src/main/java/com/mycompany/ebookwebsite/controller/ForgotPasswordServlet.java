/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.utils.EmailUtil;
import com.mycompany.ebookwebsite.utils.TokenUtil;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Admin
 */
@WebServlet(name="ForgotPasswordServlet", urlPatterns={"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(ForgotPasswordServlet.class.getName());

    /** 
     * Handles the HTTP <code>GET</code> method.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // Hiển thị form quên mật khẩu
        request.getRequestDispatcher("/user/forgot_password.jsp").forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String email = request.getParameter("email");
        String message;
        String messageType = "error"; // error, success

        if (email == null || email.trim().isEmpty()) {
            message = "Vui lòng nhập email đã đăng ký!";
        } else {
            try {
                // Validate email format
                if (!isValidEmail(email)) {
                    message = "Email không đúng định dạng!";
                } else {
                    UserDAO userDAO = new UserDAO();
                    User user = userDAO.findByEmail(email);

                    if (user == null) {
                        // Không cho biết email có tồn tại hay không để bảo mật
                        message = "Nếu email tồn tại trong hệ thống, chúng tôi sẽ gửi hướng dẫn đặt lại mật khẩu.";
                        messageType = "info";
                    } else {
                        // Tạo token và lưu vào database
                        String token = TokenUtil.createAndSaveToken(email);
                        
                        // Tạo link reset password
                        String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + 
                                         request.getContextPath() + "/reset-password?token=" + token;

                        // Gửi email
                        String subject = "Yêu cầu đặt lại mật khẩu - Scroll";
                        String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                                + "<h2 style='color: #3e2f92;'>Xin chào " + user.getUsername() + "!</h2>"
                                + "<p>Bạn vừa yêu cầu đặt lại mật khẩu cho tài khoản trên <strong>Scroll</strong>.</p>"
                                + "<p>Nhấn vào nút bên dưới để đặt lại mật khẩu:</p>"
                                + "<div style='text-align: center; margin: 30px 0;'>"
                                + "<a href='" + resetLink + "' style='background: #3e2f92; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; display: inline-block;'>Đặt lại mật khẩu</a>"
                                + "</div>"
                                + "<p><strong>Lưu ý:</strong></p>"
                                + "<ul>"
                                + "<li>Link này có hiệu lực trong 24 giờ</li>"
                                + "<li>Nếu không phải bạn yêu cầu, hãy bỏ qua email này</li>"
                                + "<li>Để bảo mật, không chia sẻ link này với người khác</li>"
                                + "</ul>"
                                + "<hr style='margin: 30px 0; border: none; border-top: 1px solid #eee;'>"
                                + "<p style='color: #666; font-size: 12px;'>Email này được gửi tự động, vui lòng không trả lời.</p>"
                                + "</div>";

                        // Gửi mail
                        EmailUtil.sendEmail(email, subject, content);
                        
                        LOGGER.info("Email reset password đã được gửi cho: " + email);
                        message = "Chúng tôi đã gửi email hướng dẫn đặt lại mật khẩu. Vui lòng kiểm tra hộp thư của bạn.";
                        messageType = "success";
                    }
                }
            } catch (MessagingException e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi gửi email cho: " + email, e);
                message = "Không thể gửi email, vui lòng thử lại sau!";
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi xử lý quên mật khẩu cho email: " + email, e);
                message = "Có lỗi xảy ra, vui lòng thử lại sau!";
            }
        }

        // Truyền thông báo cho JSP
        request.setAttribute("message", message);
        request.setAttribute("messageType", messageType);
        request.getRequestDispatcher("/user/forgot_password.jsp").forward(request, response);
    }
    
    /**
     * Kiểm tra email có đúng định dạng không
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    /** 
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Forgot Password Servlet";
    }
}
