package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.io.PrintWriter;

import com.mycompany.ebookwebsite.filter.FilterResult;
import com.mycompany.ebookwebsite.service.ContentFilterFactory;
import com.mycompany.ebookwebsite.service.OpenAIContentFilterService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ModerationServlet", urlPatterns = {"/moderate"})
public class ModerationServlet extends HttpServlet {

    private OpenAIContentFilterService contentFilterService;

    @Override
    public void init() throws ServletException {
        contentFilterService = ContentFilterFactory.getContentFilterService();
        System.out.println("[Servlet] Đã khởi tạo ContentFilterService: " + contentFilterService.getClass().getSimpleName());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String content = request.getParameter("content");
        System.out.println("[Servlet] Nhận request kiểm duyệt: " + content);

        try (PrintWriter out = response.getWriter()) {
            if (content == null || content.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"status\":\"error\",\"message\":\"Thiếu nội dung cần kiểm duyệt\"}");
                System.out.println("[Servlet] Lỗi: Không có nội dung gửi lên.");
                return;
            }

            // ✅ Gọi service kiểm duyệt nội dung – dùng FilterResult mới
            try {
                FilterResult result = contentFilterService.check(content);
            System.out.println("[Servlet] Kết quả kiểm duyệt trả về: " + (result.isPassed() ? "OK" : result.getReason()));

            if (result.isPassed()) {
                out.print("{\"status\":\"OK\"}");
            } else {
                out.print("{\"status\":\"violate\",\"message\":\"" + escapeJson(result.getReason()) + "\"}");
                }
            } catch (Exception e) {
                System.out.println("[Servlet] Lỗi kiểm duyệt: " + e.getMessage());
                out.print("{\"status\":\"error\",\"message\":\"Lỗi hệ thống kiểm duyệt\"}");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>ModerationServlet Test</title></head>");
            out.println("<body>");
            out.println("<h1>ModerationServlet is running at " + request.getContextPath() + "</h1>");
            out.println("</body></html>");
        }
        System.out.println("[Servlet] GET /moderation đã được gọi để kiểm tra health.");
    }

    @Override
    public String getServletInfo() {
        return "ModerationServlet - kiểm duyệt nội dung bằng AI agent hoặc rule-based service";
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "");
    }
} 