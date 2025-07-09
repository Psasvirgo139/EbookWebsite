package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.service.CommentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/comment/edit")
public class CommentEditServlet extends HttpServlet {
    private final CommentService commentService = new CommentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String commentIdStr = request.getParameter("commentId");
            String bookIdStr = request.getParameter("bookId");
            String content = request.getParameter("content");

            if (commentIdStr == null || bookIdStr == null || content == null || content.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu dữ liệu");
                return;
            }
            int commentId = Integer.parseInt(commentIdStr);
            int bookId = Integer.parseInt(bookIdStr);

            HttpSession session = request.getSession(false);
            Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
            String role = (session != null) ? (String) session.getAttribute("role") : null;
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Chỉ cho phép chủ sở hữu hoặc admin sửa
            if (!commentService.isCommentOwner(commentId, userId) && (role == null || !role.equals("admin"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền sửa comment này!");
                return;
            }

            commentService.updateCommentContent(commentId, content);
            response.sendRedirect(request.getContextPath() + "/book/detail?id=" + bookId);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
} 