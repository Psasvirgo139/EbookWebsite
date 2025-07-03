package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.CommentService;
import com.mycompany.ebookwebsite.service.UserService;
import com.mycompany.ebookwebsite.utils.EbookValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/comment/delete")
public class DeleteCommentServlet extends HttpServlet {
    private CommentService commentService;
    private UserService userService;

    @Override
    public void init() {
        commentService = new CommentService();
        userService = new UserService();
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
            User user = (User) userService.getUserById(userId);

            // Chỉ cho phép xóa nếu là admin hoặc chủ sở hữu comment
            commentService.deleteComment(commentId, user);

            response.sendRedirect(request.getContextPath() + "/book/detail?id=" + ebookId);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(DeleteCommentServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
