package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.dao.EbookAIDAO;
import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.EbookAI;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.utils.PathManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * 🔧 FixEncodingEbookAIServlet - Fix encoding issues trong EbookAI records
 * 
 * URL: /admin/fix-encoding-ebook-ai
 * Updated to use PathManager for better path management
 */
@WebServlet("/admin/fix-encoding-ebook-ai")
public class FixEncodingEbookAIServlet extends HttpServlet {
    
    private EbookAIDAO ebookAIDAO;
    private EbookDAO ebookDAO;
    
    @Override
    public void init() throws ServletException {
        ebookAIDAO = new EbookAIDAO();
        ebookDAO = new EbookDAO();
        
        // 🗂️ Log PathManager info for debugging
        System.out.println("📁 FixEncodingEbookAIServlet initialized with uploads path: " + PathManager.getUploadsPath());
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
        
        if ("fix".equals(action)) {
            handleFixEncoding(request, response);
        } else if ("fix-single".equals(action)) {
            handleFixSingleEncoding(request, response);
        } else {
            showMainPage(request, response);
        }
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
        out.println("<title>Fix Encoding EbookAI - Admin</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println(".button { padding: 10px 20px; margin: 5px; background: #007cba; color: white; text-decoration: none; border-radius: 5px; display: inline-block; }");
        out.println(".danger { background: #dc3545; }");
        out.println(".success { background: #28a745; }");
        out.println(".info { background: #17a2b8; }");
        out.println("table { border-collapse: collapse; width: 100%; margin: 20px 0; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println(".encoding-error { background-color: #ffe6e6; }");
        out.println(".encoding-ok { background-color: #e6ffe6; }");
        out.println("</style>");
        out.println("</head><body>");
        
        out.println("<h1>🔧 Fix Encoding EbookAI Records</h1>");
        out.println("<p>Tool để sửa các file name bị lỗi encoding UTF-8 trong bảng EbookAI.</p>");
        
        try {
            // Liệt kê các EbookAI records có vấn đề encoding
            List<EncodingIssue> issues = findEncodingIssues();
            
            if (!issues.isEmpty()) {
                out.println("<h2>📋 Các EbookAI records có vấn đề encoding:</h2>");
                out.println("<table>");
                out.println("<tr><th>Book ID</th><th>Tên sách</th><th>File name hiện tại</th><th>File gợi ý</th><th>Action</th></tr>");
                
                for (EncodingIssue issue : issues) {
                    out.println("<tr class='encoding-error'>");
                    out.println("<td>" + issue.bookId + "</td>");
                    out.println("<td>" + escapeHtml(issue.bookTitle) + "</td>");
                    out.println("<td>" + escapeHtml(issue.currentFileName) + "</td>");
                    out.println("<td>" + escapeHtml(issue.suggestedFileName) + "</td>");
                    out.println("<td><a href='?action=fix-single&bookId=" + issue.bookId + "&newFileName=" + 
                               java.net.URLEncoder.encode(issue.suggestedFileName, "UTF-8") + "' class='button'>🔧 Fix</a></td>");
                    out.println("</tr>");
                }
                
                out.println("</table>");
                
                out.println("<p><a href='?action=fix' class='button success'>🔧 Fix tất cả " + issues.size() + " records</a></p>");
            } else {
                out.println("<div class='success'>✅ Không có EbookAI records nào có vấn đề encoding!</div>");
            }
            
        } catch (SQLException e) {
            out.println("<div class='error'>❌ Lỗi database: " + escapeHtml(e.getMessage()) + "</div>");
        }
        
        out.println("<p><a href='" + request.getContextPath() + "/admin/fix-ebook-ai' class='button info'>🔙 Quay về EbookAI Fix Tool</a></p>");
        out.println("</body></html>");
    }
    
    /**
     * Tìm các EbookAI records có vấn đề encoding
     */
    private List<EncodingIssue> findEncodingIssues() throws SQLException {
        List<EncodingIssue> issues = new ArrayList<>();
        
        // Lấy tất cả EbookAI records
        List<Ebook> allBooks = ebookDAO.selectAllEbooks();
        
        for (Ebook book : allBooks) {
            try {
                EbookAI aiRecord = ebookAIDAO.getByEbookId(book.getId());
                if (aiRecord != null && aiRecord.getFileName() != null) {
                    String fileName = aiRecord.getFileName();
                    
                    // Kiểm tra có ký tự lỗi encoding không
                    if (hasEncodingIssues(fileName)) {
                        // Tìm file phù hợp trong uploads
                        String suggestedFile = findMatchingFileInUploads(book.getTitle());
                        if (suggestedFile != null) {
                            EncodingIssue issue = new EncodingIssue();
                            issue.bookId = book.getId();
                            issue.bookTitle = book.getTitle();
                            issue.currentFileName = fileName;
                            issue.suggestedFileName = suggestedFile;
                            issues.add(issue);
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Lỗi kiểm tra book ID " + book.getId() + ": " + e.getMessage());
            }
        }
        
        return issues;
    }
    
    /**
     * Kiểm tra file name có ký tự lỗi encoding không
     */
    private boolean hasEncodingIssues(String fileName) {
        // Các ký tự thường gặp khi encoding UTF-8 bị lỗi
        String[] encodingErrorChars = {"?", "Ð", "â", "Ã", "Â"};
        
        for (String errorChar : encodingErrorChars) {
            if (fileName.contains(errorChar)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Tìm file phù hợp trong uploads folder
     */
    private String findMatchingFileInUploads(String bookTitle) {
        // 🗂️ Sử dụng PathManager thay vì hard-coded path
        String uploadsPath = PathManager.getUploadsPath();
        File uploadsDir = new File(uploadsPath);
        
        if (!uploadsDir.exists() || !uploadsDir.isDirectory()) {
            System.out.println("⚠️ Uploads directory not found: " + uploadsPath);
            return null;
        }
        
        File[] files = uploadsDir.listFiles();
        if (files == null) {
            return null;
        }
        
        // Tìm file có tên gần giống với tên sách
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String fileNameWithoutExt = removeFileExtension(fileName);
                
                // So sánh với tên sách
                if (isSimilar(fileNameWithoutExt, bookTitle)) {
                    return fileName;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Kiểm tra hai string có tương tự không (ignore encoding issues)
     */
    private boolean isSimilar(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }
        
        // Normalize và so sánh
        String normalized1 = normalizeForComparison(str1);
        String normalized2 = normalizeForComparison(str2);
        
        // Tính tỷ lệ giống nhau
        return calculateSimilarity(normalized1, normalized2) > 0.7;
    }
    
    /**
     * Normalize string để so sánh (loại bỏ encoding issues)
     */
    private String normalizeForComparison(String str) {
        return str.toLowerCase()
                  .replaceAll("[^a-z0-9\\s]", "") // Chỉ giữ chữ, số và space
                  .replaceAll("\\s+", " ")       // Normalize space
                  .trim();
    }
    
    /**
     * Tính độ tương tự giữa hai string
     */
    private double calculateSimilarity(String str1, String str2) {
        String[] words1 = str1.split("\\s+");
        String[] words2 = str2.split("\\s+");
        
        int matches = 0;
        for (String word1 : words1) {
            for (String word2 : words2) {
                if (word1.length() > 2 && word2.length() > 2 && 
                    word1.contains(word2) || word2.contains(word1)) {
                    matches++;
                    break;
                }
            }
        }
        
        return (double) matches / Math.max(words1.length, words2.length);
    }
    
    /**
     * Loại bỏ file extension
     */
    private String removeFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }
    
    /**
     * Fix encoding cho tất cả records
     */
    private void handleFixEncoding(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset='UTF-8'><title>Fix Encoding Results</title>");
        out.println("<style>body { font-family: Arial, sans-serif; margin: 20px; }</style>");
        out.println("</head><body>");
        
        out.println("<h1>🔧 Fix Encoding Results</h1>");
        
        try {
            List<EncodingIssue> issues = findEncodingIssues();
            int fixedCount = 0;
            
            for (EncodingIssue issue : issues) {
                boolean fixed = fixSingleRecord(issue.bookId, issue.suggestedFileName);
                if (fixed) {
                    fixedCount++;
                    out.println("<p style='color: green;'>✅ Fixed book ID " + issue.bookId + 
                               ": " + escapeHtml(issue.suggestedFileName) + "</p>");
                } else {
                    out.println("<p style='color: red;'>❌ Failed to fix book ID " + issue.bookId + "</p>");
                }
            }
            
            out.println("<h2>📊 Kết quả:</h2>");
            out.println("<p><strong>Tổng số records cần fix:</strong> " + issues.size() + "</p>");
            out.println("<p><strong>Số records đã fix thành công:</strong> " + fixedCount + "</p>");
            
        } catch (SQLException e) {
            out.println("<p style='color: red;'>❌ Lỗi database: " + escapeHtml(e.getMessage()) + "</p>");
        }
        
        out.println("<p><a href='?'>🔙 Quay về</a></p>");
        out.println("</body></html>");
    }
    
    /**
     * Fix encoding cho một record cụ thể
     */
    private void handleFixSingleEncoding(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String bookIdStr = request.getParameter("bookId");
        String newFileName = request.getParameter("newFileName");
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset='UTF-8'><title>Fix Single Record</title>");
        out.println("<style>body { font-family: Arial, sans-serif; margin: 20px; }</style>");
        out.println("</head><body>");
        
        out.println("<h1>🔧 Fix Single Record</h1>");
        
        if (bookIdStr == null || newFileName == null) {
            out.println("<p style='color: red;'>❌ Thiếu parameters</p>");
        } else {
            try {
                int bookId = Integer.parseInt(bookIdStr);
                boolean success = fixSingleRecord(bookId, newFileName);
                
                if (success) {
                    out.println("<p style='color: green;'>✅ Fixed book ID " + bookId + 
                               " với file: " + escapeHtml(newFileName) + "</p>");
                } else {
                    out.println("<p style='color: red;'>❌ Failed to fix book ID " + bookId + "</p>");
                }
                
            } catch (NumberFormatException e) {
                out.println("<p style='color: red;'>❌ Book ID không hợp lệ</p>");
            }
        }
        
        out.println("<p><a href='?'>🔙 Quay về</a></p>");
        out.println("</body></html>");
    }
    
    /**
     * Fix một record cụ thể
     */
    private boolean fixSingleRecord(int bookId, String newFileName) {
        try {
            EbookAI aiRecord = ebookAIDAO.getByEbookId(bookId);
            if (aiRecord != null) {
                aiRecord.setFileName(newFileName);
                aiRecord.setOriginalFileName(newFileName);
                return ebookAIDAO.updateEbookAI(aiRecord);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi update EbookAI record cho book ID " + bookId + ": " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Escape HTML để tránh XSS
     */
    private String escapeHtml(String text) {
        if (text == null) return "NULL";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#x27;");
    }
    
    /**
     * Class để hold thông tin encoding issue
     */
    private static class EncodingIssue {
        int bookId;
        String bookTitle;
        String currentFileName;
        String suggestedFileName;
    }
} 