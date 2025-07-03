package com.mycompany.ebookwebsite.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

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

        req.getRequestDispatcher("/admin/dashboard.jsp")
           .forward(req, resp);
    }
}
