package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.Ebook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "LatestBooksServlet", urlPatterns = {"/latest"})
public class LatestBooksServlet extends HttpServlet {
    
    private EbookDAO ebookDAO;
    
    @Override
    public void init() throws ServletException {
        ebookDAO = new EbookDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // Add CORS headers for frontend access
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        try {
            // Lấy truyện mới nhất từ database
            List<Ebook> latestBooks = ebookDAO.selectAllEbooks(); // Lấy tất cả truyện
            
            // Chuyển đổi thành JSON
            JSONArray jsonArray = new JSONArray();
            for (Ebook book : latestBooks) {
                JSONObject bookJson = new JSONObject();
                bookJson.put("id", book.getId());
                bookJson.put("title", book.getTitle());
                bookJson.put("description", book.getDescription());
                bookJson.put("coverUrl", book.getCoverUrl());
                bookJson.put("status", book.getStatus());
                bookJson.put("releaseType", book.getReleaseType());
                bookJson.put("language", book.getLanguage());
                bookJson.put("visibility", book.getVisibility());
                bookJson.put("createdAt", book.getCreatedAt() != null ? book.getCreatedAt().toString() : null);
                bookJson.put("viewCount", book.getViewCount());
                jsonArray.put(bookJson);
            }
            
            response.getWriter().write(jsonArray.toString());
            
        } catch (SQLException e) {
            e.printStackTrace();
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Lỗi database: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(errorResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Lỗi server: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(errorResponse.toString());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
} 