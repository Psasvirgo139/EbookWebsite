package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.service.CommentService;
import com.mycompany.ebookwebsite.utils.CommentValidation;
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

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");

        try {
            int commentId = CommentValidation.validateId(request.getParameter("commentId"));
            int ebookId = CommentValidation.validateId(request.getParameter("ebookId"));

            // Kiểm tra comment có tồn tại không
            var comment = commentService.getCommentById(commentId);
            if (comment == null) {
                request.setAttribute("error", "Bình luận không tồn tại");
                response.sendRedirect(request.getContextPath() + "/book/detail?id=" + ebookId);
                return;
            }

            // Kiểm tra quyền xóa
            boolean canDelete = false;
            
            // Admin có thể xóa mọi comment
            if ("admin".equals(userRole)) {
                canDelete = true;
            }
            // User chỉ có thể xóa comment của mình
            else if (commentService.canUserDeleteComment(commentId, userId)) {
                canDelete = true;
            }

            if (!canDelete) {
                request.setAttribute("error", "Bạn không có quyền xóa bình luận này");
                response.sendRedirect(request.getContextPath() + "/book/detail?id=" + ebookId);
                return;
            }

            // Xóa comment
            commentService.deleteComment(commentId, userId);
            
            request.setAttribute("success", "Đã xóa bình luận thành công");
            response.sendRedirect(request.getContextPath() + "/book/detail?id=" + ebookId);
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Tham số không hợp lệ: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/book/list");
        } catch (Exception e) {
            request.setAttribute("error", "Không thể xóa bình luận: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/book/detail?id=" + request.getParameter("ebookId"));
        }
    }
}
