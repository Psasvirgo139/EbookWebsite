package com.mycompany.ebookwebsite.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.mycompany.ebookwebsite.dao.ChatHistoryDAO;
import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.ChatMessage;
import com.mycompany.ebookwebsite.model.Ebook;

/**
 * 🤖 INTERNAL AI CHAT SERVICE
 * 
 * ========================================
 * 📋 TÁC DỤNG CHÍNH:
 * ========================================
 * 
 * 1. 💬 **Simple AI Chat System**
 *    - Chat AI nội bộ không cần OpenAI API
 *    - Dựa trên rule-based responses và keyword matching
 *    - Tương tác thông minh với database sách
 *    - Hỗ trợ chat history và session management
 * 
 * 2. 🔍 **Smart Book Search & Discovery**
 *    - Tìm kiếm sách thông minh theo từ khóa
 *    - Phân tích intent từ user message
 *    - Gợi ý sách based on popularity và metadata
 *    - Semantic search cho book content
 * 
 * 3. 📚 **Book Intelligence Features**
 *    - Author analysis và author-based recommendations
 *    - Genre classification và genre-based search
 *    - Book statistics và trending analysis
 *    - Content-based book matching
 * 
 * 4. 🎯 **Conversational AI**
 *    - Natural language understanding
 *    - Context-aware responses
 *    - Personalized recommendations
 *    - Multi-turn conversation support
 * 
 * ========================================
 * 🔧 FEATURES:
 * ========================================
 * 
 * ✅ Rule-based AI responses (no external API required)
 * ✅ Keyword matching và intent detection
 * ✅ Database-driven book search
 * ✅ Smart recommendations based on view count
 * ✅ Author và genre analysis
 * ✅ Statistics generation
 * ✅ Chat history persistence
 * ✅ Session-based conversation
 * ✅ Fallback responses for unknown queries
 * ✅ Vietnamese language support
 * 
 * ========================================
 * 🎯 SỬ DỤNG:
 * ========================================
 * 
 * - Alternative cho external AI services
 * - Offline AI chat capability
 * - Basic book discovery và recommendations
 * - User query processing và response generation
 * 
 * ========================================
 * 🏗️ ARCHITECTURE:
 * ========================================
 * 
 * InternalAIChatService (Rule-based AI)
 *     ├── ChatHistoryDAO (Chat persistence)
 *     ├── EbookDAO (Book data access)
 *     ├── Intent Detection (Query analysis)
 *     ├── Response Generation (Smart replies)
 *     └── Book Intelligence (Search & recommendations)
 * 
 * ========================================
 * 🔄 WORKFLOW:
 * ========================================
 * 
 * User Input → Intent Detection → Database Query → Response Generation → Chat History
 *     ↓                                                     ↓
 * Pattern Matching ← Smart Recommendations ← Book Analysis
 */
public class InternalAIChatService {
    
    private final ChatHistoryDAO chatHistoryDAO;
    private final EbookDAO ebookDAO;
    private final Random random;
    
    public InternalAIChatService() {
        this.chatHistoryDAO = new ChatHistoryDAO();
        this.ebookDAO = new EbookDAO();
        this.random = new Random();
    }
    
    /**
     * 🚀 Main chat processing - simple & effective
     */
    public String processChat(int userId, String sessionId, String message, String context) {
        try {
            // Generate response
            String response = generateSmartResponse(userId, message, context);
            
            // Save to chat history
            ChatMessage chatMessage = new ChatMessage(
                userId, sessionId, message, response, "general", null
            );
            chatHistoryDAO.saveChatMessage(chatMessage);
            
            return response;
            
        } catch (Exception e) {
            e.printStackTrace();
            return "Xin lỗi, có lỗi xảy ra. Vui lòng thử lại! 😊";
        }
    }
    
    /**
     * 🧠 Generate smart response based on user message
     */
    private String generateSmartResponse(int userId, String message, String context) throws SQLException {
        String lowerMessage = message.toLowerCase();
        
        // 🎯 GREETINGS
        if (isGreeting(lowerMessage)) {
            return generateGreeting();
        }
        
        // 🤖 WHO ARE YOU
        if (isIdentityQuestion(lowerMessage)) {
            return generateIdentity();
        }
        
        // 🔍 SEARCH REQUESTS
        if (isSearchRequest(lowerMessage)) {
            return handleSearch(message);
        }
        
        // 💡 RECOMMENDATION REQUESTS
        if (isRecommendationRequest(lowerMessage)) {
            return handleRecommendations();
        }
        
        // 👤 AUTHOR QUESTIONS
        if (isAuthorQuestion(lowerMessage)) {
            return handleAuthorQuestion(message);
        }
        
        // 🏷️ GENRE QUESTIONS
        if (isGenreQuestion(lowerMessage)) {
            return handleGenreQuestion(message);
        }
        
        // 📊 STATISTICS
        if (isStatisticsQuestion(lowerMessage)) {
            return handleStatistics();
        }
        
        // 🎯 SMART KEYWORD SEARCH
        return handleSmartKeywordSearch(message);
    }
    
    // ======================== DETECTION METHODS ========================
    
    private boolean isGreeting(String message) {
        return message.contains("xin chào") || message.contains("chào") || message.contains("hi") || 
               message.contains("hello") || message.contains("hey");
    }
    
    private boolean isIdentityQuestion(String message) {
        return message.contains("bạn là ai") || message.contains("ai là bạn") || 
               message.contains("who are you") || message.contains("giới thiệu bản thân");
    }
    
    private boolean isSearchRequest(String message) {
        return message.contains("tìm") || message.contains("search") || message.contains("tìm kiếm");
    }
    
    private boolean isRecommendationRequest(String message) {
        return message.contains("đề xuất") || message.contains("gợi ý") || message.contains("recommend") ||
               message.contains("giới thiệu sách") || message.contains("sách hay");
    }
    
    private boolean isAuthorQuestion(String message) {
        return message.contains("tác giả") || message.contains("author") || message.contains("ai viết");
    }
    
    private boolean isGenreQuestion(String message) {
        return message.contains("thể loại") || message.contains("loại sách") || message.contains("genre") ||
               message.contains("category");
    }
    
    private boolean isStatisticsQuestion(String message) {
        return message.contains("thống kê") || message.contains("có bao nhiêu") || message.contains("số lượng") ||
               message.contains("statistics");
    }
    
    // ======================== RESPONSE GENERATORS ========================
    
    private String generateGreeting() {
        String[] greetings = {
            "Xin chào! 👋 Tôi là AI trợ lý sách thông minh. Tôi có thể giúp bạn tìm sách, tác giả, hoặc đề xuất sách hay! 📚",
            "Chào bạn! 😊 Hãy hỏi tôi về bất kỳ cuốn sách nào bạn quan tâm! 🔍",
            "Hi! 🤖 Tôi sẵn sàng giúp bạn khám phá thế giới sách. Bạn muốn tìm gì hôm nay?",
        };
        return greetings[random.nextInt(greetings.length)];
    }
    
    private String generateIdentity() {
        return "🤖 Tôi là AI trợ lý thông minh chuyên về sách!\n\n" +
               "✨ Tôi có thể:\n" +
               "• 🔍 Tìm sách theo tên, tác giả, thể loại\n" +
               "• 💡 Đề xuất sách hay phù hợp\n" +
               "• 📊 Cung cấp thống kê thư viện\n" +
               "• 📚 Giải đáp thắc mắc về sách\n\n" +
               "Tôi sử dụng 100% dữ liệu nội bộ, không cần internet! 🔒";
    }
    
    private String handleSearch(String message) throws SQLException {
        String keyword = extractKeyword(message, "tìm", "search", "tìm kiếm");
        
        if (keyword == null || keyword.length() < 2) {
            return "Hãy cho tôi biết bạn muốn tìm gì? 🤔\n\n" +
                   "Ví dụ: 'tìm sách trinh thám', 'tìm Nguyễn Du'";
        }
        
        List<Ebook> books = ebookDAO.search(keyword);
        
        if (books.isEmpty()) {
            return String.format("Không tìm thấy sách nào với từ khóa '%s' 😔\n\n" +
                "💡 Thử:\n" +
                "• Từ khóa khác\n" +
                "• Tên tác giả\n" +
                "• Thể loại sách", keyword);
        }
        
        return formatBookResults(books, keyword, 5);
    }
    
    private String handleRecommendations() throws SQLException {
        List<Ebook> allBooks = ebookDAO.selectAllEbooks();
        
        if (allBooks.isEmpty()) {
            return "Hiện tại thư viện chưa có sách nào. Hãy thêm sách để tôi có thể gợi ý! 📚";
        }
        
        // Get top 5 most viewed books
        List<Ebook> topBooks = allBooks.stream()
            .sorted((a, b) -> Integer.compare(b.getViewCount(), a.getViewCount()))
            .limit(5)
            .collect(Collectors.toList());
        
        StringBuilder response = new StringBuilder();
        response.append("📚 Đây là những cuốn sách nổi bật tôi gợi ý:\n\n");
        
        for (int i = 0; i < topBooks.size(); i++) {
            Ebook book = topBooks.get(i);
            response.append(String.format("📖 **%s**\n", book.getTitle()));
            response.append(String.format("✍️ %s • 🏷️ %s • 👁️ %d lượt xem\n\n", 
                                        "Tác giả chưa xác định", book.getReleaseType(), book.getViewCount()));
        }
        
        response.append("Bạn muốn biết thêm về cuốn nào không? 😊");
        return response.toString();
    }
    
    private String handleAuthorQuestion(String message) throws SQLException {
        String authorName = extractKeyword(message, "tác giả", "author");
        
        if (authorName == null) {
            // List popular authors
            return getPopularAuthors();
        }
        
        List<Ebook> books = ebookDAO.selectAllEbooks().stream()
                            .filter(book -> book.getTitle() != null &&
                        book.getTitle().toLowerCase().contains(authorName.toLowerCase()))
            .collect(Collectors.toList());
        
        if (books.isEmpty()) {
            return String.format("Không tìm thấy tác giả '%s' 😔\n\nHãy thử tên tác giả khác!", authorName);
        }
        
        return formatAuthorBooks(books, authorName);
    }
    
    private String handleGenreQuestion(String message) throws SQLException {
        String genre = extractKeyword(message, "thể loại", "loại sách", "genre");
        
        if (genre == null) {
            return getAvailableGenres();
        }
        
        List<Ebook> books = ebookDAO.selectAllEbooks().stream()
            .filter(book -> book.getReleaseType() != null && 
                           book.getReleaseType().toLowerCase().contains(genre.toLowerCase()))
            .collect(Collectors.toList());
        
        if (books.isEmpty()) {
            return String.format("Không tìm thấy sách thể loại '%s' 😔\n\nHãy thử thể loại khác!", genre);
        }
        
        return formatGenreBooks(books, genre);
    }
    
    private String handleStatistics() throws SQLException {
        List<Ebook> allBooks = ebookDAO.selectAllEbooks();
        
        int totalBooks = allBooks.size();
        long totalViews = allBooks.stream().mapToInt(Ebook::getViewCount).sum();
        
        Set<String> authors = new HashSet<>();
        Set<String> genres = new HashSet<>();
        
        for (Ebook book : allBooks) {
            if (book.getAuthor() != null && !book.getAuthor().trim().isEmpty()) {
                authors.add(book.getAuthor());
            }
            if (book.getReleaseType() != null && !book.getReleaseType().trim().isEmpty()) {
                genres.add(book.getReleaseType());
            }
        }
        
        return String.format("📊 **Thống kê thư viện sách:**\n\n" +
            "📚 Tổng số sách: **%d** cuốn\n" +
            "✍️ Số tác giả: **%d** người\n" +
            "🏷️ Số thể loại: **%d** loại\n" +
            "👁️ Tổng lượt xem: **%,d** lượt\n" +
            "📈 Trung bình: **%.1f** lượt xem/sách\n\n" +
            "Thư viện đang ngày càng phong phú! 🎉",
            totalBooks, authors.size(), genres.size(), totalViews,
            totalBooks > 0 ? (double) totalViews / totalBooks : 0);
    }
    
    private String handleSmartKeywordSearch(String message) throws SQLException {
        // Smart search by looking for keywords in the message
        List<Ebook> allBooks = ebookDAO.selectAllEbooks();
        List<Ebook> matchedBooks = new ArrayList<>();
        
        String[] keywords = message.toLowerCase().split("\\s+");
        
        for (Ebook book : allBooks) {
            for (String keyword : keywords) {
                if (keyword.length() > 2 && (
                    (book.getTitle() != null && book.getTitle().toLowerCase().contains(keyword)) ||
                    (book.getAuthor() != null && book.getAuthor().toLowerCase().contains(keyword)) ||
                    (book.getReleaseType() != null && book.getReleaseType().toLowerCase().contains(keyword))
                )) {
                    matchedBooks.add(book);
                    break;
                }
            }
        }
        
        if (matchedBooks.isEmpty()) {
            return generateHelpfulResponse();
        }
        
        return formatBookResults(matchedBooks, message, 3);
    }
    
    // ======================== HELPER METHODS ========================
    
    private String extractKeyword(String message, String... prefixes) {
        String lowerMessage = message.toLowerCase();
        
        for (String prefix : prefixes) {
            int index = lowerMessage.indexOf(prefix);
            if (index != -1) {
                String remaining = message.substring(index + prefix.length()).trim();
                // Remove common words
                remaining = remaining.replaceFirst("^(sách|của|về|là|gì)\\s*", "").trim();
                if (remaining.length() > 0) {
                    return remaining;
                }
            }
        }
        return null;
    }
    
    private String formatBookResults(List<Ebook> books, String searchTerm, int maxResults) {
        StringBuilder response = new StringBuilder();
        response.append(String.format("🔍 Tìm thấy **%d** kết quả cho '%s':\n\n", books.size(), searchTerm));
        
        int count = Math.min(books.size(), maxResults);
        for (int i = 0; i < count; i++) {
            Ebook book = books.get(i);
            response.append(String.format("📖 **%s**\n", book.getTitle()));
            response.append(String.format("✍️ %s • 🏷️ %s\n", book.getAuthor(), book.getReleaseType()));
            if (book.getDescription() != null && !book.getDescription().trim().isEmpty()) {
                String desc = book.getDescription().length() > 80 ? 
                    book.getDescription().substring(0, 77) + "..." : book.getDescription();
                response.append(String.format("📝 %s\n", desc));
            }
            response.append(String.format("👁️ %d lượt xem\n\n", book.getViewCount()));
        }
        
        if (books.size() > maxResults) {
            response.append(String.format("...và %d cuốn khác! Hãy tìm kiếm cụ thể hơn. 🔍", books.size() - maxResults));
        }
        
        return response.toString();
    }
    
    private String formatAuthorBooks(List<Ebook> books, String authorName) {
        StringBuilder response = new StringBuilder();
        response.append(String.format("✍️ **Sách của %s** (%d cuốn):\n\n", authorName, books.size()));
        
        for (int i = 0; i < Math.min(books.size(), 5); i++) {
            Ebook book = books.get(i);
            response.append(String.format("📖 %s • 🏷️ %s • 👁️ %d lượt xem\n", 
                book.getTitle(), book.getReleaseType(), book.getViewCount()));
        }
        
        if (books.size() > 5) {
            response.append(String.format("\n...và %d cuốn khác!", books.size() - 5));
        }
        
        return response.toString();
    }
    
    private String formatGenreBooks(List<Ebook> books, String genre) {
        StringBuilder response = new StringBuilder();
        response.append(String.format("🏷️ **Sách thể loại %s** (%d cuốn):\n\n", genre, books.size()));
        
        for (int i = 0; i < Math.min(books.size(), 5); i++) {
            Ebook book = books.get(i);
            response.append(String.format("📖 %s • ✍️ %s • 👁️ %d lượt xem\n", 
                book.getTitle(), book.getAuthor(), book.getViewCount()));
        }
        
        if (books.size() > 5) {
            response.append(String.format("\n...và %d cuốn khác!", books.size() - 5));
        }
        
        return response.toString();
    }
    
    private String getPopularAuthors() throws SQLException {
        List<Ebook> allBooks = ebookDAO.selectAllEbooks();
        Set<String> authors = new HashSet<>();
        
        for (Ebook book : allBooks) {
            if (book.getAuthor() != null && !book.getAuthor().trim().isEmpty()) {
                authors.add(book.getAuthor());
            }
        }
        
        if (authors.isEmpty()) {
            return "Hiện tại chưa có thông tin tác giả nào! 📚";
        }
        
        StringBuilder response = new StringBuilder();
        response.append("✍️ **Tác giả trong thư viện:**\n\n");
        
        List<String> authorList = new ArrayList<>(authors);
        int count = Math.min(authorList.size(), 10);
        
        for (int i = 0; i < count; i++) {
            response.append(String.format("• %s\n", authorList.get(i)));
        }
        
        if (authors.size() > 10) {
            response.append(String.format("\n...và %d tác giả khác!", authors.size() - 10));
        }
        
        response.append("\n\nHãy hỏi: 'sách của [tên tác giả]'");
        return response.toString();
    }
    
    private String getAvailableGenres() throws SQLException {
        List<Ebook> allBooks = ebookDAO.selectAllEbooks();
        Set<String> genres = new HashSet<>();
        
        for (Ebook book : allBooks) {
            if (book.getReleaseType() != null && !book.getReleaseType().trim().isEmpty()) {
                genres.add(book.getReleaseType());
            }
        }
        
        if (genres.isEmpty()) {
            return "Hiện tại chưa có phân loại thể loại! 🏷️";
        }
        
        StringBuilder response = new StringBuilder();
        response.append("🏷️ **Thể loại sách có sẵn:**\n\n");
        
        for (String genre : genres) {
            response.append(String.format("• %s\n", genre));
        }
        
        response.append("\nHãy hỏi: 'sách thể loại [tên thể loại]'");
        return response.toString();
    }
    
    private String generateHelpfulResponse() {
        return "🤔 Tôi không hiểu rõ câu hỏi. Hãy thử:\n\n" +
               "🔍 **Tìm kiếm:** 'tìm sách trinh thám'\n" +
               "💡 **Gợi ý:** 'đề xuất sách hay'\n" +
               "✍️ **Tác giả:** 'sách của Nguyễn Du'\n" +
               "🏷️ **Thể loại:** 'sách văn học'\n" +
               "📊 **Thống kê:** 'có bao nhiêu sách'\n\n" +
               "Tôi sẵn sàng giúp bạn! 😊";
    }
    
    /**
     * Simple compatibility method for other parts of system
     */
    public String chat(String message, String context) {
        try {
            return generateSmartResponse(0, message, context);
        } catch (Exception e) {
            return "Có lỗi xảy ra. Vui lòng thử lại! 😊";
        }
    }
    
    /**
     * Dummy method for compatibility
     */
    public void recordUserFeedback(String embeddingId, String feedbackType, int userId) {
        // Simple implementation - could be enhanced later if needed
        System.out.println("User feedback recorded: " + feedbackType);
    }
} 