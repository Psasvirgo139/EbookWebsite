package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.service.CommentService;
import com.mycompany.ebookwebsite.utils.EbookValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/comment/delete")
public class DeleteCommentServlet extends HttpServlet {
    private CommentService commentService;

    @Override
    public void init() {
        commentService = new CommentService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        try {
            int commentId = EbookValidation.validateId(request.getParameter("commentId"));
            int ebookId = EbookValidation.validateId(request.getParameter("ebookId"));

            HttpSession session = request.getSession(false);
            Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Kiểm tra quyền xóa (nếu cần kiểm tra tác giả bình luận hoặc quyền admin)
            commentService.deleteComment(commentId, userId);

            response.sendRedirect(request.getContextPath() + "/book/detail?id=" + ebookId);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
