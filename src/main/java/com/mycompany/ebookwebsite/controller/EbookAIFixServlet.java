package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.EbookAIFixService;
import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.Ebook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet để admin quản lý và fix EbookAI records
 * URL: /admin/fix-ebook-ai
 */
@WebServlet("/admin/fix-ebook-ai")
public class EbookAIFixServlet extends HttpServlet {
    
    private EbookAIFixService fixService;
    private EbookDAO ebookDAO;
    
    @Override
    public void init() throws ServletException {
        fixService = new EbookAIFixService();
        ebookDAO = new EbookDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra quyền admin
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ admin mới có quyền truy cập");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("check".equals(action)) {
            handleCheckStatus(request, response);
        } else if ("fix".equals(action)) {
            handleFixAction(request, response);
        } else if ("fix-single".equals(action)) {
            handleFixSingle(request, response);
        } else {
            showMainPage(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    /**
     * Kiểm tra user có phải admin không
     */
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            return user != null && "admin".equals(user.getRole());
        }
        return false;
    }
    
    /**
     * Hiển thị trang chính
     */
    private void showMainPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>EbookAI Fix Tool - Admin</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println(".button { padding: 10px 20px; margin: 5px; background: #007cba; color: white; text-decoration: none; border-radius: 5px; display: inline-block; }");
        out.println(".button:hover { background: #005a87; }");
        out.println(".danger { background: #dc3545; }");
        out.println(".success { background: #28a745; }");
        out.println(".info { background: #17a2b8; }");
        out.println(".container { max-width: 1200px; margin: 0 auto; }");
        out.println("</style>");
        out.println("</head><body>");
        
        out.println("<div class='container'>");
        out.println("<h1>🔧 EbookAI Fix Tool - Admin Panel</h1>");
        out.println("<p>Tool để quản lý và sửa các EbookAI records thiếu trong hệ thống.</p>");
        
        out.println("<h2>📋 Các chức năng:</h2>");
        out.println("<div>");
        out.println("<a href='?action=check' class='button info'>📊 Kiểm tra trạng thái tất cả sách</a>");
        out.println("<a href='?action=fix' class='button success'>🔧 Auto-fix tất cả sách thiếu EbookAI</a>");
        out.println("</div>");
        
        out.println("<h2>🔍 Fix sách cụ thể:</h2>");
        out.println("<form method='GET'>");
        out.println("<input type='hidden' name='action' value='fix-single'>");
        out.println("<input type='number' name='bookId' placeholder='Book ID' required>");
        out.println("<button type='submit' class='button'>🔧 Fix sách này</button>");
        out.println("</form>");
        
        out.println("<h2>📖 Hướng dẫn:</h2>");
        out.println("<ul>");
        out.println("<li><strong>Kiểm tra trạng thái:</strong> Xem tất cả sách và trạng thái EbookAI record</li>");
        out.println("<li><strong>Auto-fix tất cả:</strong> Tự động tạo EbookAI records cho tất cả sách thiếu</li>");
        out.println("<li><strong>Fix sách cụ thể:</strong> Sửa một sách cụ thể theo Book ID</li>");
        out.println("</ul>");
        
        out.println("<p><a href='" + request.getContextPath() + "/admin/dashboard' class='button'>⬅️ Quay về Admin Dashboard</a></p>");
        out.println("</div>");
        
        out.println("</body></html>");
    }
    
    /**
     * Kiểm tra trạng thái tất cả sách
     */
    private void handleCheckStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Kiểm tra trạng thái EbookAI</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println(".status-ok { color: green; }");
        out.println(".status-missing { color: red; font-weight: bold; }");
        out.println(".status-warning { color: orange; }");
        out.println(".button { padding: 5px 10px; margin: 2px; background: #007cba; color: white; text-decoration: none; border-radius: 3px; }");
        out.println("</style>");
        out.println("</head><body>");
        
        out.println("<h1>📊 Trạng thái EbookAI Records</h1>");
        
        try {
            List<Ebook> allBooks = ebookDAO.selectAllEbooks();
            
            int totalBooks = 0;
            int missingAI = 0;
            int okCount = 0;
            int warningCount = 0;
            
            out.println("<table>");
            out.println("<tr><th>Book ID</th><th>Tiêu đề</th><th>Trạng thái</th><th>Trạng thái EbookAI</th><th>Action</th></tr>");
            
            for (Ebook book : allBooks) {
                if ("deleted".equals(book.getStatus())) {
                    continue;
                }
                
                totalBooks++;
                String status = fixService.checkEbookAIStatus(book.getId());
                
                String cssClass = "status-ok";
                if (status.contains("THIẾU")) {
                    cssClass = "status-missing";
                    missingAI++;
                } else if (status.contains("⚠️")) {
                    cssClass = "status-warning";
                    warningCount++;
                } else {
                    okCount++;
                }
                
                out.println("<tr>");
                out.println("<td>" + book.getId() + "</td>");
                out.println("<td>" + escapeHtml(book.getTitle()) + "</td>");
                out.println("<td>" + book.getStatus() + "</td>");
                out.println("<td class='" + cssClass + "'>" + escapeHtml(status) + "</td>");
                out.println("<td>");
                if (status.contains("THIẾU")) {
                    out.println("<a href='?action=fix-single&bookId=" + book.getId() + "' class='button'>🔧 Fix</a>");
                }
                out.println("</td>");
                out.println("</tr>");
            }
            
            out.println("</table>");
            
            out.println("<h2>📈 Thống kê:</h2>");
            out.println("<ul>");
            out.println("<li><strong>Tổng số sách:</strong> " + totalBooks + "</li>");
            out.println("<li><strong>✅ EbookAI OK:</strong> " + okCount + "</li>");
            out.println("<li><strong>⚠️ Cảnh báo:</strong> " + warningCount + "</li>");
            out.println("<li><strong>❌ Thiếu EbookAI:</strong> " + missingAI + "</li>");
            out.println("</ul>");
            
            if (missingAI > 0) {
                out.println("<p><a href='?action=fix' class='button' style='background: #28a745; padding: 10px 20px;'>🔧 Auto-fix tất cả " + missingAI + " sách thiếu</a></p>");
            }
            
        } catch (SQLException e) {
            out.println("<p style='color: red;'>❌ Lỗi database: " + escapeHtml(e.getMessage()) + "</p>");
        }
        
        out.println("<p><a href='?' class='button'>⬅️ Quay về</a></p>");
        out.println("</body></html>");
    }
    
    /**
     * Auto-fix tất cả sách
     */
    private void handleFixAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Auto-fix EbookAI Records</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println(".button { padding: 10px 20px; margin: 5px; background: #007cba; color: white; text-decoration: none; border-radius: 5px; }");
        out.println("</style>");
        out.println("</head><body>");
        
        out.println("<h1>🔧 Auto-fix EbookAI Records</h1>");
        out.println("<p>Đang chạy auto-fix cho tất cả sách thiếu EbookAI records...</p>");
        
        long startTime = System.currentTimeMillis();
        int fixedCount = fixService.autoFixAllMissingEbookAI();
        long endTime = System.currentTimeMillis();
        
        out.println("<h2>✅ Kết quả:</h2>");
        out.println("<ul>");
        out.println("<li><strong>Số EbookAI records đã tạo:</strong> " + fixedCount + "</li>");
        out.println("<li><strong>Thời gian thực hiện:</strong> " + (endTime - startTime) + "ms</li>");
        out.println("</ul>");
        
        if (fixedCount > 0) {
            out.println("<p style='color: green; font-weight: bold;'>🎉 Auto-fix hoàn thành thành công!</p>");
        } else {
            out.println("<p style='color: blue;'>ℹ️ Không có EbookAI records nào cần tạo.</p>");
        }
        
        out.println("<p><a href='?action=check' class='button'>📊 Kiểm tra trạng thái</a></p>");
        out.println("<p><a href='?' class='button'>⬅️ Quay về</a></p>");
        
        out.println("</body></html>");
    }
    
    /**
     * Fix một sách cụ thể
     */
    private void handleFixSingle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String bookIdStr = request.getParameter("bookId");
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Fix Single Book</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println(".button { padding: 10px 20px; margin: 5px; background: #007cba; color: white; text-decoration: none; border-radius: 5px; }");
        out.println("</style>");
        out.println("</head><body>");
        
        out.println("<h1>🔧 Fix EbookAI cho sách cụ thể</h1>");
        
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
            out.println("<p style='color: red;'>❌ Thiếu Book ID</p>");
        } else {
            try {
                int bookId = Integer.parseInt(bookIdStr);
                
                // Kiểm tra trạng thái trước khi fix
                String statusBefore = fixService.checkEbookAIStatus(bookId);
                out.println("<p><strong>Trạng thái trước fix:</strong> " + escapeHtml(statusBefore) + "</p>");
                
                // Thực hiện fix
                boolean success = fixService.autoFixEbookAI(bookId);
                
                // Kiểm tra trạng thái sau fix
                String statusAfter = fixService.checkEbookAIStatus(bookId);
                out.println("<p><strong>Trạng thái sau fix:</strong> " + escapeHtml(statusAfter) + "</p>");
                
                if (success) {
                    out.println("<p style='color: green; font-weight: bold;'>✅ Fix thành công!</p>");
                } else {
                    out.println("<p style='color: red; font-weight: bold;'>❌ Fix thất bại!</p>");
                }
                
            } catch (NumberFormatException e) {
                out.println("<p style='color: red;'>❌ Book ID không hợp lệ: " + escapeHtml(bookIdStr) + "</p>");
            }
        }
        
        out.println("<p><a href='?' class='button'>⬅️ Quay về</a></p>");
        out.println("</body></html>");
    }
    
    /**
     * Escape HTML để tránh XSS
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#x27;");
    }
} 