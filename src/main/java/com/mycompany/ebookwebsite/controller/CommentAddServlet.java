package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.service.CommentService;
import com.mycompany.ebookwebsite.utils.EbookValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/add-comment")
public class CommentAddServlet extends HttpServlet {
    private final CommentService commentService = new CommentService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        try {
            int ebookId = EbookValidation.validateId(request.getParameter("ebookId"));
            String content = request.getParameter("content");

            HttpSession session = request.getSession(false);
            Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            Comment comment = new Comment();
            comment.setUserID(userId);
            comment.setEbookID(ebookId);
            comment.setContent(content);
            comment.setCreatedAt(LocalDateTime.now());

            commentService.addComment(comment);

            response.sendRedirect(request.getContextPath() + "/book/detail?id=" + ebookId);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
