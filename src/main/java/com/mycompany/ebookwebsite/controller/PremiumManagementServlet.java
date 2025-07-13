package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.PremiumService;
import com.mycompany.ebookwebsite.service.PremiumExpirationScheduler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * 👑 Premium Management Servlet
 * 
 * Admin servlet để quản lý premium subscriptions và trigger expiration checks
 */
@WebServlet("/admin/premium-management")
public class PremiumManagementServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(PremiumManagementServlet.class.getName());
    private final PremiumService premiumService;
    private final PremiumExpirationScheduler scheduler;

    public PremiumManagementServlet() {
        this.premiumService = new PremiumService();
        this.scheduler = PremiumExpirationScheduler.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Kiểm tra admin access
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. Admin role required.");
            return;
        }

        String action = request.getParameter("action");

        if (action == null) {
            // Hiển thị dashboard
            showPremiumDashboard(request, response);
        } else {
            switch (action) {
                case "status":
                    showSchedulerStatus(request, response);
                    break;
                case "trigger":
                    triggerManualCheck(request, response);
                    break;
                case "stats":
                    showPremiumStats(request, response);
                    break;
                default:
                    showPremiumDashboard(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * 📊 Hiển thị Premium Dashboard
     */
    private void showPremiumDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>👑 Premium Management Dashboard</title>");
        out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/assets/css/style.css'>");
        out.println("<style>");
        out.println(".premium-dashboard { max-width: 1200px; margin: 20px auto; padding: 20px; }");
        out.println(".card { background: white; border-radius: 8px; padding: 20px; margin: 15px 0; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        out.println(".btn { padding: 10px 20px; margin: 5px; text-decoration: none; border-radius: 5px; border: none; cursor: pointer; }");
        out.println(".btn-primary { background: #007bff; color: white; }");
        out.println(".btn-success { background: #28a745; color: white; }");
        out.println(".btn-warning { background: #ffc107; color: black; }");
        out.println(".btn-info { background: #17a2b8; color: white; }");
        out.println(".status-active { color: #28a745; }");
        out.println(".status-inactive { color: #dc3545; }");
        out.println(".timestamp { color: #6c757d; font-size: 0.9em; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        out.println("<div class='premium-dashboard'>");
        out.println("<h1>👑 Premium Management Dashboard</h1>");

        // Scheduler Status Card
        PremiumExpirationScheduler.SchedulerStatus status = scheduler.getStatus();
        out.println("<div class='card'>");
        out.println("<h2>⏰ Scheduler Status</h2>");
        out.println("<p>Running: <span class='" + (status.isRunning() ? "status-active" : "status-inactive") + "'>" +
                (status.isRunning() ? "✅ Active" : "❌ Inactive") + "</span></p>");
        out.println("<p>Shutdown: <span class='" + (status.isShutdown() ? "status-inactive" : "status-active") + "'>" +
                (status.isShutdown() ? "Yes" : "No") + "</span></p>");
        out.println("<p class='timestamp'>Last checked: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</p>");
        out.println("</div>");

        // Actions Card
        out.println("<div class='card'>");
        out.println("<h2>🎮 Actions</h2>");
        out.println("<a href='?action=trigger' class='btn btn-warning'>🔧 Trigger Manual Check</a>");
        out.println("<a href='?action=status' class='btn btn-info'>📊 View Detailed Status</a>");
        out.println("<a href='?action=stats' class='btn btn-primary'>📈 View Premium Stats</a>");
        out.println("<a href='" + request.getContextPath() + "/admin/dashboard.jsp' class='btn btn-success'>🏠 Back to Admin Dashboard</a>");
        out.println("</div>");

        // Instructions Card
        out.println("<div class='card'>");
        out.println("<h2>📖 How It Works</h2>");
        out.println("<ul>");
        out.println("<li><strong>Automatic Checks:</strong> Scheduler runs daily at 00:30 and every 6 hours</li>");
        out.println("<li><strong>Manual Trigger:</strong> Use the manual check button for immediate processing</li>");
        out.println("<li><strong>Expiration Logic:</strong> Users are downgraded when their premium subscription expires</li>");
        out.println("<li><strong>Edge Cases:</strong> Dates like 31/1 + 1 month = 1/3 (handled automatically)</li>");
        out.println("</ul>");
        out.println("</div>");

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * 📊 Hiển thị Scheduler Status chi tiết
     */
    private void showSchedulerStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        PremiumExpirationScheduler.SchedulerStatus status = scheduler.getStatus();

        out.println("{");
        out.println("  \"status\": \"" + (status.isRunning() ? "running" : "stopped") + "\",");
        out.println("  \"running\": " + status.isRunning() + ",");
        out.println("  \"shutdown\": " + status.isShutdown() + ",");
        out.println("  \"terminated\": " + status.isTerminated() + ",");
        out.println("  \"timestamp\": \"" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\"");
        out.println("}");
    }

    /**
     * 🔧 Trigger Manual Premium Expiration Check
     */
    private void triggerManualCheck(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        logger.info("Admin triggered manual premium expiration check");

        try {
            LocalDateTime startTime = LocalDateTime.now();
            int processedCount = scheduler.triggerManualCheck();
            LocalDateTime endTime = LocalDateTime.now();
            long durationMs = java.time.Duration.between(startTime, endTime).toMillis();

            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>🔧 Manual Check Result</title>");
            out.println("<link rel='stylesheet' href='" + request.getContextPath() + "/assets/css/style.css'>");
            out.println("<style>");
            out.println(".result-container { max-width: 800px; margin: 20px auto; padding: 20px; }");
            out.println(".result-card { background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
            out.println(".success-result { border-left: 4px solid #28a745; }");
            out.println(".info-result { border-left: 4px solid #17a2b8; }");
            out.println(".btn { padding: 10px 20px; margin: 10px 5px; text-decoration: none; border-radius: 5px; }");
            out.println(".btn-primary { background: #007bff; color: white; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            out.println("<div class='result-container'>");
            out.println("<div class='result-card " + (processedCount > 0 ? "success-result" : "info-result") + "'>");
            out.println("<h1>🔧 Manual Premium Expiration Check</h1>");

            if (processedCount > 0) {
                out.println("<h2>✅ Check Completed Successfully</h2>");
                out.println("<p><strong>Processed:</strong> " + processedCount + " expired subscriptions</p>");
            } else {
                out.println("<h2>ℹ️ Check Completed</h2>");
                out.println("<p><strong>Result:</strong> No expired subscriptions found</p>");
            }

            out.println("<p><strong>Started:</strong> " + startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</p>");
            out.println("<p><strong>Completed:</strong> " + endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</p>");
            out.println("<p><strong>Duration:</strong> " + durationMs + " ms</p>");

            out.println("<div style='margin-top: 20px;'>");
            out.println("<a href='?action=stats' class='btn btn-primary'>📈 View Premium Stats</a>");
            out.println("<a href='.' class='btn btn-primary'>🏠 Back to Dashboard</a>");
            out.println("</div>");

            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            logger.severe("Error during manual premium expiration check: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Error during manual check: " + e.getMessage());
        }
    }

    /**
     * 📈 Hiển thị Premium Statistics
     */
    private void showPremiumStats(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // TODO: Implement premium statistics
        // Có thể add thêm methods vào PremiumService để lấy stats tổng quan

        out.println("{");
        out.println("  \"message\": \"Premium statistics feature coming soon\",");
        out.println("  \"timestamp\": \"" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\",");
        out.println("  \"scheduler_status\": \"" + (scheduler.getStatus().isRunning() ? "running" : "stopped") + "\"");
        out.println("}");
    }
} 