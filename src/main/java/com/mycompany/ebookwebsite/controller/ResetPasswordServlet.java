/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.ebookwebsite.service.UserService;
import com.mycompany.ebookwebsite.utils.TokenUtil;
import com.mycompany.ebookwebsite.utils.UserValidation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Admin
 */
@WebServlet(name="ResetPasswordServlet", urlPatterns={"/reset-password"})
public class ResetPasswordServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(ResetPasswordServlet.class.getName());

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ResetPasswordServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ResetPasswordServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // Lấy token từ query string
        String token = request.getParameter("token");
        String message = null;
        String messageType = "error";

        if (token == null || token.trim().isEmpty()) {
            message = "Link đặt lại mật khẩu không hợp lệ!";
        } else {
            // Kiểm tra token hợp lệ (còn thời hạn, chưa dùng...)
            try {
                int userId = TokenUtil.checkResetToken(token); // Trả về userId nếu hợp lệ, lỗi thì ném exception

                // Cho phép hiển thị form đổi mật khẩu
                request.setAttribute("token", token);
                request.setAttribute("userId", userId);
                request.setAttribute("messageType", "success");
                request.getRequestDispatcher("/user/reset_password.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Token không hợp lệ hoặc đã hết hạn: " + token, e);
                message = "Link đặt lại mật khẩu đã hết hạn hoặc không hợp lệ!";
            }
        }

        request.setAttribute("message", message);
        request.setAttribute("messageType", messageType);
        request.getRequestDispatcher("/user/reset_password.jsp").forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String message = null;
        String messageType = "error";

        if (token == null || token.trim().isEmpty()) {
            message = "Link đặt lại mật khẩu không hợp lệ!";
        } else if (password == null || password.trim().isEmpty()) {
            message = "Vui lòng nhập mật khẩu mới!";
        } else if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            message = "Vui lòng nhập lại mật khẩu!";
        } else if (!password.equals(confirmPassword)) {
            message = "Nhập lại mật khẩu không khớp!";
        } else {
            try {
                // Validate password
                UserValidation.validatePassword(password);
                
                // Kiểm tra token còn hợp lệ
                int userId = TokenUtil.checkResetToken(token);

                // Đổi mật khẩu trong DB
                UserService userService = new UserService();
                boolean updated = userService.updatePassword(userId, password);

                if (updated) {
                    // Đánh dấu token đã dùng
                    TokenUtil.markTokenUsed(token);
                    
                    LOGGER.info("Password đã được reset thành công cho user ID: " + userId);
                    message = "Đặt lại mật khẩu thành công! Bạn có thể <a href=\"" + request.getContextPath() + "/login\">đăng nhập</a> ngay bây giờ.";
                    messageType = "success";
                } else {
                    message = "Không thể đặt lại mật khẩu, vui lòng thử lại sau.";
                }
            } catch (IllegalArgumentException e) {
                message = e.getMessage();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi reset password cho token: " + token, e);
                message = "Link đặt lại mật khẩu đã hết hạn hoặc không hợp lệ!";
            }
        }

        request.setAttribute("message", message);
        request.setAttribute("messageType", messageType);
        request.setAttribute("token", token);
        request.getRequestDispatcher("/user/reset_password.jsp").forward(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Reset Password Servlet";
    }// </editor-fold>
}
