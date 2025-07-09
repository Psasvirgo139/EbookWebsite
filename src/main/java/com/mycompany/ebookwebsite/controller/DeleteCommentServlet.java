package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.service.CommentService;
import com.mycompany.ebookwebsite.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/comment/delete")
public class DeleteCommentServlet extends HttpServlet {
    private final CommentService commentService = new CommentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String commentId = request.getParameter("commentId");
            HttpSession session = request.getSession(false);
            User user = (session != null) ? (User) session.getAttribute("user") : null;
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            if (commentId == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu commentId");
                return;
            }
            commentService.deleteComment(Integer.parseInt(commentId), user);
            // Redirect về trang trước đó
            String referer = request.getHeader("Referer");
            if (referer != null) {
                response.sendRedirect(referer);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
} 