package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.service.CommentVoteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/comment/vote")
public class CommentVoteServlet extends HttpServlet {
    private final CommentVoteService voteService = new CommentVoteService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String commentId = request.getParameter("commentId");
            String type = request.getParameter("type");
            HttpSession session = request.getSession(false);
            Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            if (commentId == null || type == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu commentId hoặc type");
                return;
            }
            int cid = Integer.parseInt(commentId);
            if ("like".equals(type)) {
                voteService.toggleLike(userId, cid);
            } else if ("dislike".equals(type)) {
                voteService.toggleDislike(userId, cid);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Type không hợp lệ");
                return;
            }
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