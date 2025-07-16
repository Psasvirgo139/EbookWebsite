package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.ChapterContentUrlService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet để cập nhật contentUrl cho chapter
 * Chỉ admin mới có quyền truy cập
 */
@WebServlet("/admin/chapter-content-update")
public class ChapterContentUpdateServlet extends HttpServlet {
    
    private final ChapterContentUrlService contentUrlService = new ChapterContentUrlService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra quyền admin
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp?error=admin_required");
            return;
        }
        
        String action = request.getParameter("action");
        String bookIdStr = request.getParameter("bookId");
        
        try {
            if ("update_specific".equals(action) && bookIdStr != null) {
                // Cập nhật cho sách cụ thể
                int bookId = Integer.parseInt(bookIdStr);
                updateSpecificBook(request, response, bookId);
                
            } else if ("update_all".equals(action)) {
                // Cập nhật tất cả sách
                updateAllBooks(request, response);
                
            } else if ("validate".equals(action) && bookIdStr != null) {
                // Kiểm tra file tồn tại
                int bookId = Integer.parseInt(bookIdStr);
                validateBookFiles(request, response, bookId);
                
            } else {
                // Hiển thị form
                showUpdateForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID sách không hợp lệ");
            showUpdateForm(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Lỗi database: " + e.getMessage());
            showUpdateForm(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
            showUpdateForm(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private void showUpdateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setAttribute("pageTitle", "Cập nhật Content URL cho Chapter");
        request.getRequestDispatcher("/admin/chapter_content_update.jsp").forward(request, response);
    }
    
    private void updateSpecificBook(HttpServletRequest request, HttpServletResponse response, int bookId)
            throws ServletException, IOException, SQLException {
        
        List<String> messages = new ArrayList<>();
        messages.add("🔄 Bắt đầu cập nhật contentUrl cho sách ID: " + bookId);
        
        try {
            contentUrlService.updateContentUrlsForBook(bookId);
            messages.add("✅ Cập nhật thành công!");
            
            // Kiểm tra file sau khi cập nhật
            messages.add("🔍 Kiểm tra file tồn tại...");
            contentUrlService.validateChapterFiles(bookId);
            
        } catch (Exception e) {
            messages.add("❌ Lỗi: " + e.getMessage());
        }
        
        request.setAttribute("messages", messages);
        request.setAttribute("bookId", bookId);
        showUpdateForm(request, response);
    }
    
    private void updateAllBooks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        
        List<String> messages = new ArrayList<>();
        messages.add("🔄 Bắt đầu cập nhật contentUrl cho tất cả sách...");
        
        try {
            contentUrlService.updateAllContentUrls();
            messages.add("✅ Cập nhật tất cả sách thành công!");
            
        } catch (Exception e) {
            messages.add("❌ Lỗi: " + e.getMessage());
        }
        
        request.setAttribute("messages", messages);
        showUpdateForm(request, response);
    }
    
    private void validateBookFiles(HttpServletRequest request, HttpServletResponse response, int bookId)
            throws ServletException, IOException, SQLException {
        
        List<String> messages = new ArrayList<>();
        messages.add("🔍 Kiểm tra file cho sách ID: " + bookId);
        
        try {
            contentUrlService.validateChapterFiles(bookId);
            messages.add("✅ Kiểm tra hoàn thành!");
            
        } catch (Exception e) {
            messages.add("❌ Lỗi: " + e.getMessage());
        }
        
        request.setAttribute("messages", messages);
        request.setAttribute("bookId", bookId);
        showUpdateForm(request, response);
    }
} 