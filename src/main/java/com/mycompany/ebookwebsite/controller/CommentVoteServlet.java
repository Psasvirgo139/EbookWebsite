package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.service.CommentVoteService;
import com.mycompany.ebookwebsite.model.User;
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
            
            // Get userId from session
            HttpSession session = request.getSession(false);
            Integer userId = null;
            if (session != null) {
                userId = (Integer) session.getAttribute("userId");
                if (userId == null) {
                    User user = (User) session.getAttribute("user");
                    if (user != null) {
                        userId = user.getId();
                    }
                }
            }
            
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/user/login.jsp");
                return;
            }
            
            if (commentId == null || type == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
                return;
            }
            
            int cid = Integer.parseInt(commentId);
            
            // Process vote
            if ("like".equals(type)) {
                voteService.toggleLike(userId, cid);
            } else if ("dislike".equals(type)) {
                voteService.toggleDislike(userId, cid);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid type");
                return;
            }
            
            // Smart redirect with proper parameter preservation
            String referer = request.getHeader("Referer");
            if (referer != null) {
                // Check if this is a book detail page that needs id parameter
                if (referer.contains("/book/detail") && !referer.contains("id=")) {
                    try {
                        // Get bookId from comment
                        com.mycompany.ebookwebsite.dao.CommentDAO commentDAO = new com.mycompany.ebookwebsite.dao.CommentDAO();
                        com.mycompany.ebookwebsite.model.Comment comment = commentDAO.getCommentById(cid);
                        if (comment != null && comment.getEbookID() != 0) {
                            String separator = referer.contains("?") ? "&" : "?";
                            String redirectUrl = referer + separator + "id=" + comment.getEbookID();
                            response.sendRedirect(redirectUrl);
                            return;
                        }
                    } catch (Exception e) {
                        // Fall back to normal referer redirect
                    }
                }
                response.sendRedirect(referer);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
} 