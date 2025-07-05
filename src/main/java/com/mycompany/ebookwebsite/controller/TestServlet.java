package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.dao.DBConnection;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.TagService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        StringBuilder result = new StringBuilder();
        result.append("<html><head><title>Test Database</title></head><body>");
        result.append("<h1>Test Database Connection</h1>");
        
        try {
            // Test 1: Database Connection
            result.append("<h2>1. Testing Database Connection</h2>");
            try (Connection conn = DBConnection.getConnection()) {
                result.append("<p style='color: green;'>✅ Database connection successful!</p>");
                
                // Test 2: Count books
                result.append("<h2>2. Testing Book Count</h2>");
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM Ebooks")) {
                    if (rs.next()) {
                        int count = rs.getInt("count");
                        result.append("<p>Total books in database: <strong>").append(count).append("</strong></p>");
                    }
                }
                
                // Test 3: Count active books
                result.append("<h2>3. Testing Active Books</h2>");
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM Ebooks WHERE status = 'active'")) {
                    if (rs.next()) {
                        int count = rs.getInt("count");
                        result.append("<p>Active books: <strong>").append(count).append("</strong></p>");
                    }
                }
                
                // Test 4: Show all books
                result.append("<h2>4. All Books in Database</h2>");
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id, title, status, is_premium, view_count FROM Ebooks ORDER BY id")) {
                    result.append("<table border='1' style='border-collapse: collapse; width: 100%;'>");
                    result.append("<tr><th>ID</th><th>Title</th><th>Status</th><th>Premium</th><th>Views</th></tr>");
                    while (rs.next()) {
                        result.append("<tr>");
                        result.append("<td>").append(rs.getInt("id")).append("</td>");
                        result.append("<td>").append(rs.getString("title")).append("</td>");
                        result.append("<td>").append(rs.getString("status")).append("</td>");
                        result.append("<td>").append(rs.getBoolean("is_premium") ? "Yes" : "No").append("</td>");
                        result.append("<td>").append(rs.getInt("view_count")).append("</td>");
                        result.append("</tr>");
                    }
                    result.append("</table>");
                }
                
                // Test 5: Count tags
                result.append("<h2>5. Testing Tags</h2>");
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM Tags")) {
                    if (rs.next()) {
                        int count = rs.getInt("count");
                        result.append("<p>Total tags: <strong>").append(count).append("</strong></p>");
                    }
                }
                
            } catch (Exception e) {
                result.append("<p style='color: red;'>❌ Database connection failed: ").append(e.getMessage()).append("</p>");
                e.printStackTrace();
            }
            
            // Test 6: Service Layer
            result.append("<h2>6. Testing Service Layer</h2>");
            try {
                EbookService ebookService = new EbookService();
                TagService tagService = new TagService();
                
                List<?> topBooks = ebookService.getTopPremiumBooks(5);
                result.append("<p>Top premium books count: <strong>").append(topBooks.size()).append("</strong></p>");
                
                List<?> newBooks = ebookService.getRecentBooks(5);
                result.append("<p>Recent books count: <strong>").append(newBooks.size()).append("</strong></p>");
                
                List<?> freeBooks = ebookService.getTopFreeBooks(5);
                result.append("<p>Free books count: <strong>").append(freeBooks.size()).append("</strong></p>");
                
                List<?> categories = tagService.getAllTags();
                result.append("<p>Categories count: <strong>").append(categories.size()).append("</strong></p>");
                
            } catch (Exception e) {
                result.append("<p style='color: red;'>❌ Service layer error: ").append(e.getMessage()).append("</p>");
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            result.append("<p style='color: red;'>❌ General error: ").append(e.getMessage()).append("</p>");
            e.printStackTrace();
        }
        
        result.append("<br><br><a href='/'>Back to Home</a>");
        result.append("</body></html>");
        
        resp.getWriter().write(result.toString());
    }
} 