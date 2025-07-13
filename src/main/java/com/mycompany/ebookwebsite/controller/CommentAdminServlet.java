package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.AdminCommentView;
import com.mycompany.ebookwebsite.service.CommentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CommentAdminServlet", urlPatterns = {"/admin/comments"})
public class CommentAdminServlet extends HttpServlet {
    private final CommentService commentService = new CommentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        com.mycompany.ebookwebsite.model.User user =
                (session != null) ? (com.mycompany.ebookwebsite.model.User) session.getAttribute("user") : null;
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String reports = req.getParameter("reports");
        if ("1".equals(reports)) {
            try {
                java.util.List<com.mycompany.ebookwebsite.model.ReportedCommentView> reportsList = new com.mycompany.ebookwebsite.service.CommentReportService().getAllReports();
                req.setAttribute("reports", reportsList);
            } catch (Exception e) {
                throw new ServletException("Lỗi lấy danh sách báo cáo bình luận cho admin", e);
            }
            req.getRequestDispatcher("/admin/comment_reports.jsp").forward(req, resp);
            return;
        }
        try {
            List<AdminCommentView> comments = commentService.getAdminCommentViewsReportedOnly();
            req.setAttribute("comments", comments);
            // Lấy danh sách report cho các comment này
            java.util.List<com.mycompany.ebookwebsite.model.ReportedCommentView> reportsList = new com.mycompany.ebookwebsite.service.CommentReportService().getAllReports();
            req.setAttribute("reports", reportsList);
        } catch (Exception e) {
            throw new ServletException("Lỗi lấy danh sách bình luận cho admin", e);
        }
        req.getRequestDispatcher("/admin/comments.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            int commentId = Integer.parseInt(req.getParameter("commentId"));
            HttpSession session = req.getSession(false);
            com.mycompany.ebookwebsite.model.User user =
                (session != null) ? (com.mycompany.ebookwebsite.model.User) session.getAttribute("user") : null;
            if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            try {
                commentService.deleteComment(commentId, user);
                resp.sendRedirect(req.getContextPath() + "/admin/comments");
            } catch (Exception e) {
                throw new ServletException("Lỗi xóa bình luận", e);
            }
            return;
        }
    }
} 