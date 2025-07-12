package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.service.CommentReportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "CommentReportServlet", urlPatterns = {"/comment/report"})
public class CommentReportServlet extends HttpServlet {
    private final CommentReportService reportService = new CommentReportService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        com.mycompany.ebookwebsite.model.User user =
                (session != null) ? (com.mycompany.ebookwebsite.model.User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String commentIdStr = req.getParameter("commentId");
        String reason = req.getParameter("reason");
        if (commentIdStr == null || reason == null || reason.trim().isEmpty()) {
            req.setAttribute("error", "Vui lòng nhập lý do báo cáo!");
            req.getRequestDispatcher(req.getHeader("Referer")).forward(req, resp);
            return;
        }
        int commentId = Integer.parseInt(commentIdStr);
        try {
            reportService.reportComment(commentId, user.getId(), reason);
            req.setAttribute("success", "Đã gửi báo cáo thành công!");
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi gửi báo cáo: " + e.getMessage());
        }
        resp.sendRedirect(req.getHeader("Referer"));
    }
} 