package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.BookService;
import com.mycompany.ebookwebsite.service.InternalAIChatService;
import com.mycompany.ebookwebsite.service.AIRecommendationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AIRecommendationServlet", urlPatterns = {"/ai/recommendations"})
public class AIRecommendationServlet extends HttpServlet {
    
    private InternalAIChatService aiChatService;
    private AIRecommendationService recommendationService;
    private BookService bookService;
    
    @Override
    public void init() throws ServletException {
        try {
            aiChatService = new InternalAIChatService();
            recommendationService = new AIRecommendationService();
            bookService = new BookService();
            System.out.println("✅ AIRecommendationServlet initialized successfully");
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize AI Recommendation service: " + e.getMessage());
            throw new ServletException("Failed to initialize AI services", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        if (action == null) action = "view";
        
        try {
            switch (action) {
                case "view":
                    showRecommendationsPage(request, response);
                    break;
                case "api":
                    handleAPIRequest(request, response);
                    break;
                default:
                    showRecommendationsPage(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/ai/recommendations.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // All POST requests are API calls
        handleAPIRequest(request, response);
    }
    
    /**
     * Hiển thị trang recommendations với dữ liệu ban đầu
     */
    private void showRecommendationsPage(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        User currentUser = (User) request.getSession().getAttribute("user");
        
        // Lấy một số sách phổ biến để hiển thị ban đầu
        List<Ebook> allBooks = bookService.getAllBooks();
        List<Ebook> latestBooks = allBooks.size() > 5 ? allBooks.subList(0, 5) : allBooks;
        
        // Lấy recommendations theo genre nếu có
        String genre = request.getParameter("genre");
        List<Ebook> genreBooks = null;
        if (genre != null && !genre.trim().isEmpty()) {
            genreBooks = bookService.getBooksByCategory(genre);
        }
        
        // Chuẩn bị dữ liệu cho JSP
        request.setAttribute("userName", currentUser.getUsername());
        request.setAttribute("latestBooks", latestBooks);
        request.setAttribute("selectedGenre", genre);
        request.setAttribute("genreBooks", genreBooks);
        
        // Lấy danh sách thể loại để hiển thị filter
        List<String> availableGenres = bookService.getAllGenres();
        request.setAttribute("availableGenres", availableGenres);
        
        request.getRequestDispatcher("/ai/recommendations.jsp").forward(request, response);
    }
    
    /**
     * Xử lý API requests cho recommendations
     */
    private void handleAPIRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            try (PrintWriter out = response.getWriter()) {
                out.write("{\"success\": false, \"error\": \"Bạn cần đăng nhập để sử dụng tính năng này\"}");
            }
            return;
        }
        
        try (PrintWriter out = response.getWriter()) {
            String type = request.getParameter("type");
            User currentUser = (User) session.getAttribute("user");
            
            if (type == null) type = "general";
            
            switch (type) {
                case "smart":
                    handleSmartRecommendations(request, response, currentUser, out);
                    break;
                case "genre":
                    handleGenreRecommendations(request, response, currentUser, out);
                    break;
                case "search":
                    handleSearchRecommendations(request, response, currentUser, out);
                    break;
                default:
                    handleGeneralRecommendations(request, response, currentUser, out);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            try (PrintWriter out = response.getWriter()) {
                String errorMsg = "Có lỗi xảy ra khi tạo recommendations: " + e.getMessage();
                out.write("{\"success\": false, \"error\": \"" + escapeJson(errorMsg) + "\"}");
            }
        }
    }
    
    /**
     * Smart AI recommendations sử dụng OpenAI
     */
    private void handleSmartRecommendations(HttpServletRequest request, HttpServletResponse response,
                                          User user, PrintWriter out) throws Exception {
        
        String preferences = request.getParameter("preferences");
        if (preferences == null || preferences.trim().isEmpty()) {
            preferences = "Hãy đề xuất một số sách hay cho tôi";
        }
        
        // Lấy context về sách hiện có
        List<Ebook> availableBooks = bookService.getAllBooks();
        StringBuilder context = new StringBuilder();
        context.append("Các sách hiện có trên hệ thống: ");
        
        for (int i = 0; i < Math.min(10, availableBooks.size()); i++) {
            Ebook Ebook = availableBooks.get(i);
            context.append(Ebook.getTitle()).append(" (").append(Ebook.getAuthor()).append("), ");
        }
        
        // Tạo prompt cho AI
        String aiPrompt = "Dựa trên sở thích: '" + preferences + 
                         "' và danh sách sách có sẵn: " + context.toString() +
                         ". Hãy đề xuất 3-5 cuốn sách phù hợp nhất và giải thích tại sao.";
        
        String aiResponse = aiChatService.chat(aiPrompt, null);
        
        // Log recommendation
        System.out.println("[AI Recommendation] User: " + user.getUsername() + 
                          " | Preferences: " + preferences + 
                          " | AI Response length: " + aiResponse.length());
        
        String jsonResponse = String.format(
            "{\"success\": true, \"type\": \"smart\", \"recommendations\": \"%s\", \"timestamp\": %d}",
            escapeJson(aiResponse),
            System.currentTimeMillis()
        );
        
        out.write(jsonResponse);
    }
    
    /**
     * Recommendations theo thể loại
     */
    private void handleGenreRecommendations(HttpServletRequest request, HttpServletResponse response,
                                          User user, PrintWriter out) throws Exception {
        
        String genre = request.getParameter("genre");
        if (genre == null || genre.trim().isEmpty()) {
            out.write("{\"success\": false, \"error\": \"Vui lòng chọn thể loại\"}");
            return;
        }
        
        List<Ebook> genreBooks = bookService.getBooksByCategory(genre);
        
        if (genreBooks.isEmpty()) {
            out.write("{\"success\": false, \"error\": \"Không tìm thấy sách thuộc thể loại này\"}");
            return;
        }
        
        // Format kết quả
        StringBuilder result = new StringBuilder();
        result.append("Sách thể loại ").append(genre).append(":\\n");
        
        for (int i = 0; i < Math.min(5, genreBooks.size()); i++) {
            Ebook Ebook = genreBooks.get(i);
            result.append("• ").append(Ebook.getTitle())
                  .append(" - ").append(Ebook.getAuthor());
            if (Ebook.getSummary() != null && !Ebook.getSummary().isEmpty()) {
                result.append("\\n  Tóm tắt: ").append(Ebook.getSummary().substring(0, Math.min(100, Ebook.getSummary().length()))).append("...");
            }
            result.append("\\n\\n");
        }
        
        String jsonResponse = String.format(
            "{\"success\": true, \"type\": \"genre\", \"genre\": \"%s\", \"count\": %d, \"recommendations\": \"%s\"}",
            escapeJson(genre),
            genreBooks.size(),
            escapeJson(result.toString())
        );
        
        out.write(jsonResponse);
    }
    
    /**
     * Search-based recommendations
     */
    private void handleSearchRecommendations(HttpServletRequest request, HttpServletResponse response,
                                           User user, PrintWriter out) throws Exception {
        
        String query = request.getParameter("query");
        if (query == null || query.trim().isEmpty()) {
            out.write("{\"success\": false, \"error\": \"Vui lòng nhập từ khóa tìm kiếm\"}");
            return;
        }
        
        List<Ebook> searchResults = bookService.searchBooks(query);
        
        StringBuilder result = new StringBuilder();
        if (searchResults.isEmpty()) {
            result.append("Không tìm thấy sách phù hợp với từ khóa '").append(query).append("'.");
        } else {
            result.append("Kết quả tìm kiếm cho '").append(query).append("':\\n\\n");
            
            for (int i = 0; i < Math.min(5, searchResults.size()); i++) {
                Ebook Ebook = searchResults.get(i);
                result.append("📚 ").append(Ebook.getTitle())
                      .append("\\n👤 Tác giả: ").append(Ebook.getAuthor())
                      .append("\\n🏷️ Thể loại: ").append(Ebook.getGenre());
                if (Ebook.getSummary() != null && !Ebook.getSummary().isEmpty()) {
                    result.append("\\n📝 ").append(Ebook.getSummary().substring(0, Math.min(150, Ebook.getSummary().length()))).append("...");
                }
                result.append("\\n\\n");
            }
        }
        
        String jsonResponse = String.format(
            "{\"success\": true, \"type\": \"search\", \"query\": \"%s\", \"count\": %d, \"recommendations\": \"%s\"}",
            escapeJson(query),
            searchResults.size(),
            escapeJson(result.toString())
        );
        
        out.write(jsonResponse);
    }
    
    /**
     * General recommendations
     */
    private void handleGeneralRecommendations(HttpServletRequest request, HttpServletResponse response,
                                            User user, PrintWriter out) throws Exception {
        
        List<Ebook> allBooks = bookService.getAllBooks();
        
        if (allBooks.isEmpty()) {
            out.write("{\"success\": false, \"error\": \"Chưa có sách nào trong hệ thống\"}");
            return;
        }
        
        StringBuilder result = new StringBuilder();
        result.append("📚 SÁCH ĐỀ XUẤT CHO BẠN\\n\\n");
        
        // Lấy 5 sách đầu tiên làm đề xuất
        for (int i = 0; i < Math.min(5, allBooks.size()); i++) {
            Ebook Ebook = allBooks.get(i);
            result.append("🌟 ").append(Ebook.getTitle())
                  .append("\\n👤 ").append(Ebook.getAuthor())
                  .append("\\n📚 ").append(Ebook.getGenre());
            if (Ebook.getSummary() != null && !Ebook.getSummary().isEmpty()) {
                result.append("\\n📖 ").append(Ebook.getSummary().substring(0, Math.min(120, Ebook.getSummary().length()))).append("...");
            }
            result.append("\\n\\n");
        }
        
        String jsonResponse = String.format(
            "{\"success\": true, \"type\": \"general\", \"count\": %d, \"recommendations\": \"%s\"}",
            allBooks.size(),
            escapeJson(result.toString())
        );
        
        out.write(jsonResponse);
    }
    
    /**
     * Escape JSON string để tránh lỗi syntax
     */
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t")
                   .replace("\b", "\\b")
                   .replace("\f", "\\f");
    }
} 
