package com.mycompany.ebookwebsite.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminDashboard", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Kiểm tra role lần cuối (nếu cần)
        HttpSession session = req.getSession(false);
        com.mycompany.ebookwebsite.model.User user =
                (session != null) ? (com.mycompany.ebookwebsite.model.User) session.getAttribute("user") : null;
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            // Thống kê tài chính
            double totalRevenue = new com.mycompany.ebookwebsite.service.OrderService().getTotalRevenue();
            int orderCount = new com.mycompany.ebookwebsite.service.OrderService().getAllOrders().size();
            // TODO: Thống kê tổng số coin đã bán (nếu cần)

            // Lấy danh sách sách mới nhất và user mới nhất
            java.util.List<com.mycompany.ebookwebsite.model.Ebook> latestBooks = new com.mycompany.ebookwebsite.service.EbookService().getLatestBooks(5);
            java.util.List<com.mycompany.ebookwebsite.model.User> latestUsers = new com.mycompany.ebookwebsite.service.UserService().getLatestUsers(5);

            req.setAttribute("totalRevenue", totalRevenue);
            req.setAttribute("orderCount", orderCount);
            req.setAttribute("latestBooks", latestBooks);
            req.setAttribute("latestUsers", latestUsers);
            // ... (có thể set thêm các thống kê khác nếu cần)
        } catch (Exception e) {
            throw new ServletException("Lỗi khi lấy dữ liệu dashboard", e);
        }

        req.getRequestDispatcher("/admin/dashboard.jsp")
           .forward(req, resp);
    }
}
