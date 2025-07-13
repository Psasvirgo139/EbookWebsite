package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.mycompany.ebookwebsite.model.LatestBookView;
import com.mycompany.ebookwebsite.service.EbookService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "LatestBooksServlet", urlPatterns = {"/latest"})
public class LatestBooksServlet extends HttpServlet {
    
    private EbookService ebookService;
    
    @Override
    public void init() throws ServletException {
        ebookService = new EbookService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Lấy tham số limit từ request, mặc định là 20
            int limit = 20;
            try {
                String limitParam = request.getParameter("limit");
                if (limitParam != null && !limitParam.isEmpty()) {
                    limit = Integer.parseInt(limitParam);
                    if (limit < 1) limit = 20;
                    if (limit > 100) limit = 100; // Giới hạn tối đa 100
                }
            } catch (NumberFormatException e) {
                limit = 20; // Mặc định nếu parse lỗi
            }
            
            // Lấy truyện mới nhất từ service với LatestBookView
            List<LatestBookView> latestBooks = ebookService.getLatestBookViews(limit);
            
            // Đặt attributes cho JSP
            request.setAttribute("latestBooks", latestBooks);
            request.setAttribute("totalBooks", latestBooks.size());
            request.setAttribute("limit", limit);
            
            // Forward đến JSP để render HTML
            request.getRequestDispatcher("/latest.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi database: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi server: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
} 