package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.InternalAIChatService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "SmartRecommendationServlet", urlPatterns = {"/ai/smart-recommendations"})
public class SmartRecommendationServlet extends HttpServlet {
    
    private InternalAIChatService aiChatService;
    private EbookDAO ebookDAO;
    
    @Override
    public void init() throws ServletException {
        try {
            aiChatService = new InternalAIChatService();
            ebookDAO = new EbookDAO();
            System.out.println("✅ SmartRecommendationServlet initialized with Real OpenAI and EbookDAO");
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize Smart Recommendation service: " + e.getMessage());
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
        
        // Show recommendations page
        try {
            showRecommendationsPage(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/ai/smart-recommendations.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Handle API requests
        handleAPIRequest(request, response);
    }
    
    /**
     * Hiển thị trang recommendations với dữ liệu ban đầu
     */
    private void showRecommendationsPage(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        User currentUser = (User) request.getSession().getAttribute("user");
        
        // Lấy một số sách phổ biến từ database
        List<Ebook> allBooks = ebookDAO.selectEbooksByVisibility("public");
        List<Ebook> latestBooks = allBooks.size() > 6 ? allBooks.subList(0, 6) : allBooks;
        
        // Lấy thống kê
        int totalBooks = allBooks.size();
        
        // Lấy danh sách thể loại có sẵn
        List<String> availableGenres = getAvailableGenres();
        
        // Chuẩn bị dữ liệu cho JSP
        request.setAttribute("userName", currentUser.getUsername());
        request.setAttribute("latestBooks", latestBooks);
        request.setAttribute("totalBooks", totalBooks);
        request.setAttribute("availableGenres", availableGenres);
        
        request.getRequestDispatcher("/ai/smart-recommendations.jsp").forward(request, response);
    }
    
    /**
     * Xử lý API requests cho smart recommendations
     */
    private void handleAPIRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        System.out.println("🔥 SmartRecommendationServlet - handleAPIRequest called!");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("❌ User not logged in - session: " + session);
            try (PrintWriter out = response.getWriter()) {
                out.write("{\"success\": false, \"error\": \"Bạn cần đăng nhập để sử dụng tính năng này\"}");
            }
            return;
        }
        
        try (PrintWriter out = response.getWriter()) {
            User currentUser = (User) session.getAttribute("user");
            
            // Lấy filter parameters
            String genre = request.getParameter("genre");
            String mood = request.getParameter("mood");
            String length = request.getParameter("length");
            String complexity = request.getParameter("complexity");
            
            System.out.println("🎯 Smart Recommendation Request - Genre: " + genre + ", Mood: " + mood + 
                               ", Length: " + length + ", Complexity: " + complexity);
            
            // Lấy tất cả sách public từ database
            List<Ebook> allBooks = ebookDAO.selectEbooksByVisibility("public");
            System.out.println("📚 Found " + allBooks.size() + " books in database");
            
            // Apply filters
            List<Ebook> filteredBooks = applyFilters(allBooks, genre, mood, length, complexity);
            System.out.println("🔍 After filtering: " + filteredBooks.size() + " books remain");
            
            // Generate AI recommendations
            String aiRecommendations = generateAIRecommendations(filteredBooks, genre, mood, length, complexity);
            
            // Log recommendation
            System.out.println("[Smart AI Recommendation] User: " + currentUser.getUsername() + 
                              " | Found " + filteredBooks.size() + " books | AI Response length: " + aiRecommendations.length());
            
            String jsonResponse = String.format(
                "{\"success\": true, \"recommendations\": \"%s\", \"totalFound\": %d, \"timestamp\": %d}",
                escapeJson(aiRecommendations),
                filteredBooks.size(),
                System.currentTimeMillis()
            );
            
            out.write(jsonResponse);
            
        } catch (Exception e) {
            e.printStackTrace();
            try (PrintWriter out = response.getWriter()) {
                String errorMsg = "Có lỗi xảy ra khi tạo đề xuất thông minh: " + e.getMessage();
                out.write("{\"success\": false, \"error\": \"" + escapeJson(errorMsg) + "\"}");
            }
        }
    }
    
    /**
     * 📊 Get available genres from database
     */
    private List<String> getAvailableGenres() throws Exception {
        List<Ebook> allBooks = ebookDAO.selectEbooksByVisibility("public");
        return allBooks.stream()
            .map(Ebook::getReleaseType)
            .filter(genre -> genre != null && !genre.trim().isEmpty())
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }
    
    /**
     * 🔧 Apply filters to book list
     */
    private List<Ebook> applyFilters(List<Ebook> books, String genre, String mood, String length, String complexity) {
        List<Ebook> filtered = new ArrayList<>(books);
        
        // Filter by genre
        if (genre != null && !genre.trim().isEmpty()) {
            filtered = filtered.stream()
                .filter(book -> matchesGenre(book, genre))
                .collect(Collectors.toList());
        }
        
        // Filter by mood
        if (mood != null && !mood.trim().isEmpty()) {
            filtered = filtered.stream()
                .filter(book -> matchesMood(book, mood))
                .collect(Collectors.toList());
        }
        
        // Filter by length
        if (length != null && !length.trim().isEmpty()) {
            filtered = filtered.stream()
                .filter(book -> matchesLength(book, length))
                .collect(Collectors.toList());
        }
        
        // Filter by complexity
        if (complexity != null && !complexity.trim().isEmpty()) {
            filtered = filtered.stream()
                .filter(book -> matchesComplexity(book, complexity))
                .collect(Collectors.toList());
        }
        
        return filtered;
    }
    
    /**
     * 🎨 Check if book matches genre
     */
    private boolean matchesGenre(Ebook book, String genre) {
        if (book.getReleaseType() == null) return false;
        
        String bookGenre = book.getReleaseType().toLowerCase();
        String targetGenre = genre.toLowerCase();
        
        // Direct match
        if (bookGenre.contains(targetGenre)) {
            return true;
        }
        
        // Genre mapping for better matching
        Map<String, List<String>> genreMap = getGenreMapping();
        if (genreMap.containsKey(targetGenre)) {
            for (String synonym : genreMap.get(targetGenre)) {
                if (bookGenre.contains(synonym)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 🎭 Check if book matches mood
     */
    private boolean matchesMood(Ebook book, String mood) {
        if (book.getDescription() == null || book.getReleaseType() == null) return false;
        
        String content = (book.getDescription() + " " + book.getReleaseType()).toLowerCase();
        
        Map<String, List<String>> moodMap = getMoodMapping();
        if (moodMap.containsKey(mood.toLowerCase())) {
            for (String keyword : moodMap.get(mood.toLowerCase())) {
                if (content.contains(keyword)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 📏 Check if book matches length preference
     */
    private boolean matchesLength(Ebook book, String length) {
        int estimatedLength = estimateBookLength(book);
        
        switch (length.toLowerCase()) {
            case "short":
                return estimatedLength <= 200;
            case "medium":
                return estimatedLength > 200 && estimatedLength <= 500;
            case "long":
                return estimatedLength > 500;
            default:
                return true;
        }
    }
    
    /**
     * 🧩 Check if book matches complexity preference
     */
    private boolean matchesComplexity(Ebook book, String complexity) {
        int complexityScore = estimateComplexity(book);
        
        switch (complexity.toLowerCase()) {
            case "easy":
                return complexityScore <= 3;
            case "moderate":
                return complexityScore > 3 && complexityScore <= 7;
            case "challenging":
                return complexityScore > 7;
            default:
                return true;
        }
    }
    
    /**
     * 🤖 Generate AI recommendations based on filtered books
     */
    private String generateAIRecommendations(List<Ebook> filteredBooks, String genre, String mood, String length, String complexity) throws Exception {
        if (filteredBooks.isEmpty()) {
            return "Rất tiếc, không tìm thấy sách nào phù hợp với các tiêu chí bạn đã chọn. Hãy thử điều chỉnh bộ lọc để có nhiều lựa chọn hơn!";
        }
        
        // Take top 10 books for AI processing
        List<Ebook> topBooks = filteredBooks.stream()
            .sorted((a, b) -> Integer.compare(b.getViewCount(), a.getViewCount()))
            .limit(10)
            .collect(Collectors.toList());
        
        // Build context for AI
        StringBuilder context = new StringBuilder();
        context.append("Danh sách sách đã được lọc theo sở thích của người dùng:\n");
        
        for (int i = 0; i < topBooks.size(); i++) {
            Ebook book = topBooks.get(i);
            context.append(String.format("%d. %s (Thể loại: %s, Lượt xem: %d)\n",
                i + 1, book.getTitle(), book.getReleaseType(), book.getViewCount()));
            
            if (book.getDescription() != null && !book.getDescription().trim().isEmpty()) {
                context.append("   Mô tả: " + 
                    book.getDescription().substring(0, Math.min(book.getDescription().length(), 150)) + "...\n");
            }
            context.append("\n");
        }
        
        // Build AI prompt
        String prompt = buildAIPrompt(genre, mood, length, complexity, context.toString(), filteredBooks.size());
        
        // Get AI response
        return aiChatService.chat(prompt, null);
    }
    
    /**
     * 🎯 Build AI prompt based on user preferences
     */
    private String buildAIPrompt(String genre, String mood, String length, String complexity, String bookContext, int totalFound) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Bạn là một chuyên gia tư vấn sách thông minh và nhiệt tình. ");
        prompt.append("Dựa trên các sở thích sau của người dùng:\n\n");
        
        if (genre != null && !genre.trim().isEmpty()) {
            prompt.append("📚 Thể loại yêu thích: " + genre + "\n");
        }
        
        if (mood != null && !mood.trim().isEmpty()) {
            prompt.append("🎭 Tâm trạng: " + mood + "\n");
        }
        
        if (length != null && !length.trim().isEmpty()) {
            prompt.append("📏 Độ dài: " + length + "\n");
        }
        
        if (complexity != null && !complexity.trim().isEmpty()) {
            prompt.append("🧩 Độ phức tạp: " + complexity + "\n");
        }
        
        prompt.append("\n");
        prompt.append(bookContext);
        prompt.append("\n");
        prompt.append("Hãy đề xuất 3-5 cuốn sách phù hợp nhất từ danh sách trên và giải thích tại sao chúng phù hợp với sở thích của người dùng. ");
        prompt.append("Định dạng câu trả lời theo dạng:\n\n");
        prompt.append("🌟 **TÊN SÁCH**\n");
        prompt.append("🎯 **Lý do phù hợp:** [Giải thích chi tiết]\n");
        prompt.append("📖 **Điểm nổi bật:** [Những gì đặc biệt]\n\n");
        prompt.append("Hãy viết một cách hấp dẫn và thuyết phục để người dùng muốn đọc những cuốn sách này!");
        
        return prompt.toString();
    }
    
    /**
     * 🎨 Get genre mapping for better matching
     */
    private Map<String, List<String>> getGenreMapping() {
        Map<String, List<String>> mapping = new HashMap<>();
        
        mapping.put("fantasy", List.of("fantasy", "ma thuật", "phép thuật", "thần thoại", "huyền bí"));
        mapping.put("romance", List.of("romance", "lãng mạn", "tình yêu", "ngôn tình", "romantic"));
        mapping.put("mystery", List.of("mystery", "trinh thám", "bí ẩn", "detective", "suspense"));
        mapping.put("sci-fi", List.of("sci-fi", "khoa học viễn tưởng", "science fiction", "công nghệ", "tương lai"));
        mapping.put("horror", List.of("horror", "kinh dị", "ma quỷ", "rùng rợn", "scary"));
        mapping.put("adventure", List.of("adventure", "phiêu lưu", "mạo hiểm", "exploration", "journey"));
        mapping.put("drama", List.of("drama", "kịch", "bi kịch", "dramatic", "emotional"));
        mapping.put("comedy", List.of("comedy", "hài", "vui nhộn", "funny", "humor"));
        mapping.put("self-help", List.of("self-help", "tự lực", "phát triển bản thân", "kỹ năng", "motivational"));
        mapping.put("tiểu thuyết", List.of("tiểu thuyết", "novel", "fiction", "story"));
        
        return mapping;
    }
    
    /**
     * 🎭 Get mood mapping for better matching
     */
    private Map<String, List<String>> getMoodMapping() {
        Map<String, List<String>> mapping = new HashMap<>();
        
        mapping.put("relaxed", List.of("thư giãn", "yên bình", "nhẹ nhàng", "peaceful", "calm"));
        mapping.put("adventurous", List.of("phiêu lưu", "mạo hiểm", "hành động", "exciting", "thrilling"));
        mapping.put("emotional", List.of("cảm xúc", "xúc động", "tình cảm", "emotional", "touching"));
        mapping.put("intellectual", List.of("trí tuệ", "học hỏi", "suy nghĩ", "intellectual", "thoughtful"));
        mapping.put("fun", List.of("vui vẻ", "hài hước", "giải trí", "funny", "entertaining"));
        
        return mapping;
    }
    
    /**
     * 📏 Estimate book length from available data
     */
    private int estimateBookLength(Ebook book) {
        int baseLength = 200; // Default assumption
        
        if (book.getDescription() != null) {
            // Longer descriptions often mean longer books
            int descLength = book.getDescription().length();
            if (descLength > 500) baseLength += 200;
            else if (descLength > 200) baseLength += 100;
        }
        
        // Popular books (more views) might be longer
        if (book.getViewCount() > 100) baseLength += 150;
        else if (book.getViewCount() > 50) baseLength += 75;
        
        return baseLength;
    }
    
    /**
     * 🧩 Estimate complexity from genre and description
     */
    private int estimateComplexity(Ebook book) {
        int complexity = 5; // Default moderate complexity
        
        if (book.getReleaseType() != null) {
            String genre = book.getReleaseType().toLowerCase();
            
            // Complex genres
            if (genre.contains("sci-fi") || genre.contains("khoa học") || 
                genre.contains("philosophy") || genre.contains("trinh thám")) {
                complexity += 3;
            }
            // Easy genres
            else if (genre.contains("romance") || genre.contains("comedy") || 
                     genre.contains("hài") || genre.contains("vui")) {
                complexity -= 2;
            }
        }
        
        if (book.getDescription() != null) {
            String desc = book.getDescription().toLowerCase();
            
            // Complex themes
            if (desc.contains("phức tạp") || desc.contains("triết học") || 
                desc.contains("sâu sắc") || desc.contains("thách thức")) {
                complexity += 2;
            }
            // Simple themes
            else if (desc.contains("đơn giản") || desc.contains("dễ hiểu") || 
                     desc.contains("nhẹ nhàng") || desc.contains("vui")) {
                complexity -= 1;
            }
        }
        
        return Math.max(1, Math.min(10, complexity));
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
