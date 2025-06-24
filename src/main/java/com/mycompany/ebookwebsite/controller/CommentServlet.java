package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.service.CommentService;
import com.mycompany.ebookwebsite.utils.EbookValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
    private CommentService commentService;

    @Override
    public void init() {
        commentService = new CommentService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        try {
            int ebookId = EbookValidation.validateId(request.getParameter("ebookId"));
            List<Comment> comments = commentService.getCommentsByEbookId(ebookId);
            request.setAttribute("comments", comments);
            request.getRequestDispatcher("/book/comments.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Không thể tải bình luận", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        try {
            int userId = EbookValidation.validateId(request.getParameter("userId"));
            int ebookId = EbookValidation.validateId(request.getParameter("ebookId"));
            String content = request.getParameter("content");

            Comment comment = new Comment();
            comment.setUserID(userId);
            comment.setEbookID(ebookId);
            comment.setContent(content.trim());

            commentService.addComment(comment);
            response.sendRedirect(request.getContextPath() + "/comment?ebookId=" + ebookId);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }
    }
}
