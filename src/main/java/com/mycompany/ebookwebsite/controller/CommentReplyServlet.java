package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.service.CommentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/comment/reply")
public class CommentReplyServlet extends HttpServlet {
    private final CommentService commentService = new CommentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String bookId = request.getParameter("bookId");
            String parentCommentId = request.getParameter("parentCommentId");
            String content = request.getParameter("content");
            String chapterId = request.getParameter("chapterId");

            HttpSession session = request.getSession(false);
            Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            Comment reply = new Comment();
            reply.setUserID(userId);
            if (bookId != null) reply.setEbookID(Integer.parseInt(bookId));
            if (chapterId != null && !chapterId.isEmpty()) reply.setChapterID(Integer.parseInt(chapterId));
            reply.setParentCommentID(Integer.parseInt(parentCommentId));
            reply.setContent(content);
            reply.setCreatedAt(LocalDateTime.now());

            try {
                commentService.addReply(reply);
            } catch (java.sql.SQLException e) {
                throw new ServletException("Database error", e);
            }

            // Redirect về trang chi tiết sách/chapter (tùy context)
            if (bookId != null && chapterId != null && !chapterId.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/book/read?bookId=" + bookId + "&chapterId=" + chapterId);
            } else if (bookId != null) {
                response.sendRedirect(request.getContextPath() + "/book/detail?id=" + bookId);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
} 