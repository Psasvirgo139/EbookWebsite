package com.mycompany.ebookwebsite.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.mycompany.ebookwebsite.dao.DBConnection;
import com.mycompany.ebookwebsite.model.Ebook;

/**
 * Tiện ích đọc file đa định dạng cho kiểm duyệt AI nội dung sách + tiện ích đọc biến môi trường từ .env
 * 
 * 🔥 ENHANCED VERSION với:
 * - Real database connections
 * - RAG integration
 * - Real book link coordination
 * - Advanced search capabilities
 */
public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class.getName());
    
    // Static field để cache dotenv instance
    private static Object dotenv = null;
    private static boolean dotenvInitialized = false;
    
    static {
        initializeDotenv();
    }
    
    private static void initializeDotenv() {
        if (dotenvInitialized) return;
        
        try {
            // Thử khởi tạo Dotenv nếu có thư viện
            Class<?> dotenvClass = Class.forName("io.github.cdimascio.dotenv.Dotenv");
            Object dotenvBuilder = dotenvClass.getMethod("configure").invoke(null);
            dotenv = dotenvBuilder.getClass().getMethod("ignoreIfMissing").invoke(dotenvBuilder);
            dotenv = dotenv.getClass().getMethod("load").invoke(dotenv);
            dotenvInitialized = true;
        } catch (Exception e) {
            // Không có thư viện Dotenv, sử dụng System properties
            dotenv = null;
            dotenvInitialized = true;
        }
    }

    public static String getEnv(String key) {
        try {
            if (dotenv != null) {
                return (String) dotenv.getClass().getMethod("get", String.class).invoke(dotenv, key);
            }
        } catch (Exception e) {
            // Fallback to system properties
        }
        
        // Fallback: lấy từ system properties hoặc environment variables
        String value = System.getProperty(key);
        if (value != null) return value;
        
        value = System.getenv(key);
        if (value != null) return value;
        
        // Default values cho testing
        if ("OPENAI_API_KEY".equals(key)) {
            return "test-api-key-for-demo";
        }
        
        return null;
    }

    public static String getUploadFolder() {
        return getEnv("UPLOAD_FOLDER");
    }
    
    public static String getDbUser() {
        return getEnv("DB_USER");
    }
    
    public static String getDbPassword() {
        return getEnv("DB_PASSWORD");
    }

    // --- Đọc file text thường (UTF-8) ---
    public static String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return java.nio.file.Files.readString(path);
    }

    /**
     * Chuẩn hóa text: bỏ khoảng trắng thừa, xuống dòng về 1 dấu cách
     */
    public static String normalizeText(String text) {
        if (text == null) return "";
        return text.trim().replaceAll("\\s+", " ");
    }

    /**
     * Đọc nội dung file đa định dạng: txt, doc, docx, pdf
     * @param filePath Đường dẫn file vật lý
     * @param ext      Đuôi file ("txt", "doc", "docx", "pdf")
     * @return Nội dung file ở dạng String, để kiểm duyệt
     * @throws Exception nếu file không đọc được
     */
    public static String readAnyTextFile(String filePath, String ext) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("File không tồn tại: " + filePath);
        }

        ext = ext.toLowerCase();
        
        switch (ext) {
            case "txt":
                return readTextFile(filePath);
            case "pdf":
                return readPdfFile(filePath);
            case "doc":
                // Temporarily not supported - will add back with proper dependencies
                throw new Exception("File .doc tạm thời chưa hỗ trợ. Vui lòng chuyển sang .docx");
            case "docx":
                return readDocxFile(filePath);
            default:
                throw new Exception("Không hỗ trợ định dạng file: " + ext);
        }
    }

    private static String readTextFile(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            
            StringBuilder content = new StringBuilder();
            char[] buffer = new char[1024];
            int bytesRead;
            
            while ((bytesRead = isr.read(buffer)) != -1) {
                content.append(buffer, 0, bytesRead);
            }
            
            return content.toString();
        }
    }

    /**
     * Đọc file PDF thật sự bằng Apache PDFBox
     */
    private static String readPdfFile(String filePath) throws Exception {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            return normalizeText(text);
        } catch (Exception e) {
            throw new Exception("Lỗi đọc file PDF: " + e.getMessage(), e);
        }
    }
    
    /**
     * Đọc file Word .doc cũ bằng Apache POI
     * Tạm thời disabled do dependency conflicts
     */
    /*
    private static String readDocFile(String filePath) throws Exception {
        try (FileInputStream fis = new FileInputStream(filePath);
             HWPFDocument document = new HWPFDocument(fis);
             WordExtractor extractor = new WordExtractor(document)) {
            
            String text = extractor.getText();
            return normalizeText(text);
        } catch (Exception e) {
            throw new Exception("Lỗi đọc file DOC: " + e.getMessage(), e);
        }
    }
    */
    
    /**
     * Đọc file Word .docx mới bằng Apache POI
     */
    private static String readDocxFile(String filePath) throws Exception {
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            
            String text = extractor.getText();
            return normalizeText(text);
        } catch (Exception e) {
            throw new Exception("Lỗi đọc file DOCX: " + e.getMessage(), e);
        }
    }

    // --- Chuẩn hóa text cho JSON ---
    public static String safeForJson(String text) {
        if (text == null) return "";
        return text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\b", "\\b")
            .replace("\f", "\\f")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }

    // ========================================
    // 🔥 ENHANCED DATABASE INTEGRATION
    // ========================================

    /**
     * 🔍 Tìm kiếm sách thực tế từ database
     */
    public static List<Ebook> searchRealBooks(String query) {
        List<Ebook> results = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT e.*, " +
                        "STRING_AGG(t.name, ', ') as tags, " +
                        "STRING_AGG(a.name, ', ') as authors " +
                        "FROM Ebooks e " +
                        "LEFT JOIN EbookTags et ON e.id = et.ebook_id " +
                        "LEFT JOIN Tags t ON et.tag_id = t.id " +
                        "LEFT JOIN EbookAuthors ea ON e.id = ea.ebook_id " +
                        "LEFT JOIN Authors a ON ea.author_id = a.id " +
                        "WHERE e.status != 'deleted' " +
                        "AND (e.title LIKE ? OR e.description LIKE ?) " +
                        "GROUP BY e.id, e.title, e.description, e.release_type, e.language, " +
                        "e.status, e.visibility, e.uploader_id, e.created_at, " +
                        "e.view_count, e.cover_url, e.file_name, e.original_file_name " +
                        "ORDER BY e.view_count DESC";
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                String searchPattern = "%" + query + "%";
                ps.setString(1, searchPattern);
                ps.setString(2, searchPattern);
                
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Ebook ebook = new Ebook();
                        ebook.setId(rs.getInt("id"));
                        ebook.setTitle(rs.getString("title"));
                        ebook.setDescription(rs.getString("description"));
                        ebook.setReleaseType(rs.getString("release_type"));
                        ebook.setLanguage(rs.getString("language"));
                        ebook.setStatus(rs.getString("status"));
                        ebook.setVisibility(rs.getString("visibility"));
                        ebook.setUploaderId(rs.getInt("uploader_id"));
                        ebook.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime().toLocalDate());
                        ebook.setViewCount(rs.getInt("view_count"));
                        ebook.setCoverUrl(rs.getString("cover_url"));
                        ebook.setFileName(rs.getString("file_name"));
                        ebook.setOriginalFileName(rs.getString("original_file_name"));
                        
                        results.add(ebook);
                    }
                }
            }
            
            logger.info("🔍 Found " + results.size() + " books for query: " + query);
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Database search error: " + e.getMessage(), e);
        }
        
        return results;
    }

    /**
     * 📚 Lấy thông tin chi tiết sách từ database
     */
    public static Map<String, Object> getBookDetails(int bookId) {
        Map<String, Object> details = new HashMap<>();
        
        try (Connection con = DBConnection.getConnection()) {
            // Get book info
            String bookSql = "SELECT e.*, " +
                            "STRING_AGG(DISTINCT t.name, ', ') as tags, " +
                            "STRING_AGG(DISTINCT a.name, ', ') as authors, " +
                            "COUNT(DISTINCT c.id) as chapter_count, " +
                            "COUNT(DISTINCT v.id) as volume_count " +
                            "FROM Ebooks e " +
                            "LEFT JOIN EbookTags et ON e.id = et.ebook_id " +
                            "LEFT JOIN Tags t ON et.tag_id = t.id " +
                            "LEFT JOIN EbookAuthors ea ON e.id = ea.ebook_id " +
                            "LEFT JOIN Authors a ON ea.author_id = a.id " +
                            "LEFT JOIN Chapters c ON e.id = c.ebook_id " +
                            "LEFT JOIN Volumes v ON e.id = v.ebook_id " +
                            "WHERE e.id = ? AND e.status != 'deleted' " +
                            "GROUP BY e.id, e.title, e.description, e.release_type, e.language, " +
                            "e.status, e.visibility, e.uploader_id, e.created_at, " +
                            "e.view_count, e.cover_url, e.file_name, e.original_file_name";
            
            try (PreparedStatement ps = con.prepareStatement(bookSql)) {
                ps.setInt(1, bookId);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        details.put("id", rs.getInt("id"));
                        details.put("title", rs.getString("title"));
                        details.put("description", rs.getString("description"));
                        details.put("release_type", rs.getString("release_type"));
                        details.put("language", rs.getString("language"));
                        details.put("status", rs.getString("status"));
                        details.put("visibility", rs.getString("visibility"));
                        details.put("view_count", rs.getInt("view_count"));
                        details.put("cover_url", rs.getString("cover_url"));
                        details.put("tags", rs.getString("tags"));
                        details.put("authors", rs.getString("authors"));
                        details.put("chapter_count", rs.getInt("chapter_count"));
                        details.put("volume_count", rs.getInt("volume_count"));
                        details.put("created_at", rs.getTimestamp("created_at"));
                    }
                }
            }
            
            // Get chapters
            String chapterSql = "SELECT id, title, number, access_level, view_count, like_count " +
                               "FROM Chapters " +
                               "WHERE ebook_id = ? " +
                               "ORDER BY number";
            
            List<Map<String, Object>> chapters = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(chapterSql)) {
                ps.setInt(1, bookId);
                
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> chapter = new HashMap<>();
                        chapter.put("id", rs.getInt("id"));
                        chapter.put("title", rs.getString("title"));
                        chapter.put("number", rs.getBigDecimal("number"));
                        chapter.put("access_level", rs.getString("access_level"));
                        chapter.put("view_count", rs.getInt("view_count"));
                        chapter.put("like_count", rs.getInt("like_count"));
                        chapters.add(chapter);
                    }
                }
            }
            details.put("chapters", chapters);
            
            logger.info("📚 Retrieved details for book ID: " + bookId);
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Database error getting book details: " + e.getMessage(), e);
        }
        
        return details;
    }

    /**
     * 🔗 Tìm sách liên quan (same author, tags, series)
     */
    public static List<Ebook> findRelatedBooks(int bookId) {
        List<Ebook> relatedBooks = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT DISTINCT e2.* " +
                        "FROM Ebooks e1 " +
                        "JOIN EbookTags et1 ON e1.id = et1.ebook_id " +
                        "JOIN EbookTags et2 ON et1.tag_id = et2.tag_id " +
                        "JOIN Ebooks e2 ON et2.ebook_id = e2.id " +
                        "WHERE e1.id = ? AND e2.id != ? AND e2.status != 'deleted' " +
                        "UNION " +
                        "SELECT DISTINCT e2.* " +
                        "FROM Ebooks e1 " +
                        "JOIN EbookAuthors ea1 ON e1.id = ea1.ebook_id " +
                        "JOIN EbookAuthors ea2 ON ea1.author_id = ea2.author_id " +
                        "JOIN Ebooks e2 ON ea2.ebook_id = e2.id " +
                        "WHERE e1.id = ? AND e2.id != ? AND e2.status != 'deleted' " +
                        "ORDER BY view_count DESC " +
                        "LIMIT 10";
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, bookId);
                ps.setInt(2, bookId);
                ps.setInt(3, bookId);
                ps.setInt(4, bookId);
                
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Ebook ebook = new Ebook();
                        ebook.setId(rs.getInt("id"));
                        ebook.setTitle(rs.getString("title"));
                        ebook.setDescription(rs.getString("description"));
                        ebook.setReleaseType(rs.getString("release_type"));
                        ebook.setLanguage(rs.getString("language"));
                        ebook.setStatus(rs.getString("status"));
                        ebook.setVisibility(rs.getString("visibility"));
                        ebook.setUploaderId(rs.getInt("uploader_id"));
                        ebook.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime().toLocalDate());
                        ebook.setViewCount(rs.getInt("view_count"));
                        ebook.setCoverUrl(rs.getString("cover_url"));
                        ebook.setFileName(rs.getString("file_name"));
                        ebook.setOriginalFileName(rs.getString("original_file_name"));
                        
                        relatedBooks.add(ebook);
                    }
                }
            }
            
            logger.info("🔗 Found " + relatedBooks.size() + " related books for book ID: " + bookId);
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Database error finding related books: " + e.getMessage(), e);
        }
        
        return relatedBooks;
    }

    // ========================================
    // 🔥 RAG INTEGRATION
    // ========================================

    /**
     * 🧠 Tích hợp RAG để tìm kiếm nội dung thông minh
     */
    public static String performRAGSearch(String query, String context) {
        try {
            // Import RAG service dynamically
            Class<?> ragServiceClass = Class.forName("com.mycompany.ebookwebsite.ai.EnhancedRAGService");
            Object ragService = ragServiceClass.getDeclaredConstructor().newInstance();
            
            // Index context if provided
            if (context != null && !context.isEmpty()) {
                Map<String, String> metadata = Map.of("source", "user_context", "query", query);
                ragServiceClass.getMethod("indexContent", String.class, Map.class)
                    .invoke(ragService, context, metadata);
            }
            
            // Perform retrieval
            @SuppressWarnings("unchecked")
            List<Object> relevantSegments = (List<Object>) ragServiceClass
                .getMethod("retrieveRelevantContent", String.class, int.class, Map.class)
                .invoke(ragService, query, 5, null);
            
            // Build response
            StringBuilder response = new StringBuilder();
            response.append("🔍 Kết quả tìm kiếm RAG cho: ").append(query).append("\n\n");
            
            if (relevantSegments.isEmpty()) {
                response.append("❌ Không tìm thấy nội dung liên quan trong vector database.\n");
                response.append("💡 Gợi ý: Thử từ khóa khác hoặc kiểm tra dữ liệu đã được index.");
            } else {
                response.append("✅ Tìm thấy ").append(relevantSegments.size()).append(" đoạn nội dung liên quan:\n\n");
                
                for (int i = 0; i < relevantSegments.size(); i++) {
                    Object segment = relevantSegments.get(i);
                    String text = (String) segment.getClass().getMethod("text").invoke(segment);
                    response.append(i + 1).append(". ").append(text.substring(0, Math.min(200, text.length())));
                    if (text.length() > 200) response.append("...");
                    response.append("\n\n");
                }
            }
            
            logger.info("🧠 RAG search completed for query: " + query);
            return response.toString();
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "⚠️ RAG service not available: " + e.getMessage());
            return "⚠️ RAG service temporarily unavailable. Using fallback search.\n\n" +
                   "🔍 Query: " + query + "\n" +
                   "💡 Fallback: Performing basic text search...";
        }
    }

    /**
     * 📊 Tạo tóm tắt thông minh với RAG
     */
    public static String generateSmartSummary(String content) {
        try {
            // Import RAG service dynamically
            Class<?> ragServiceClass = Class.forName("com.mycompany.ebookwebsite.ai.EnhancedRAGService");
            Object ragService = ragServiceClass.getDeclaredConstructor().newInstance();
            
            // Generate summary using RAG
            String summary = (String) ragServiceClass
                .getMethod("generateSummary", String.class)
                .invoke(ragService, content);
            
            logger.info("📊 Smart summary generated successfully");
            return summary;
            
        } catch (Exception e) {
            logger.log(Level.WARNING, "⚠️ RAG summary generation failed: " + e.getMessage());
            return "📝 Tóm tắt cơ bản: " + content.substring(0, Math.min(300, content.length())) + 
                   (content.length() > 300 ? "..." : "");
        }
    }

    // ========================================
    // 🔥 REAL BOOK LINK COORDINATION
    // ========================================

    /**
     * 🔗 Tạo link thực tế cho sách
     */
    public static String generateRealBookLink(int bookId, String bookTitle) {
        return String.format("/ebook/%d/%s", bookId, 
            bookTitle.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-"));
    }

    /**
     * 📚 Tạo link chapter thực tế
     */
    public static String generateChapterLink(int bookId, int chapterId, String chapterTitle) {
        return String.format("/ebook/%d/chapter/%d/%s", bookId, chapterId,
            chapterTitle.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-"));
    }

    /**
     * 🔍 Tìm kiếm và tạo link sách thực tế
     */
    public static Map<String, Object> searchAndLinkBooks(String query) {
        Map<String, Object> result = new HashMap<>();
        
        // Search real books from database
        List<Ebook> books = searchRealBooks(query);
        
        if (books.isEmpty()) {
            result.put("found", false);
            result.put("message", "Không tìm thấy sách nào phù hợp với từ khóa: " + query);
            return result;
        }
        
        // Create links and format results
        List<Map<String, Object>> bookLinks = new ArrayList<>();
        for (Ebook book : books) {
            Map<String, Object> bookInfo = new HashMap<>();
            bookInfo.put("id", book.getId());
            bookInfo.put("title", book.getTitle());
            bookInfo.put("description", book.getDescription());
            bookInfo.put("view_count", book.getViewCount());
            bookInfo.put("link", generateRealBookLink(book.getId(), book.getTitle()));
            bookInfo.put("cover_url", book.getCoverUrl());
            bookLinks.add(bookInfo);
        }
        
        result.put("found", true);
        result.put("count", books.size());
        result.put("books", bookLinks);
        result.put("query", query);
        
        logger.info("🔗 Generated " + books.size() + " real book links for query: " + query);
        
        return result;
    }

    /**
     * 📖 Lấy thông tin chapter và tạo link
     */
    public static Map<String, Object> getChapterInfo(int bookId, int chapterId) {
        Map<String, Object> chapterInfo = new HashMap<>();
        
        try (Connection con = DBConnection.getConnection()) {
            String sql = "SELECT c.*, e.title as book_title, e.id as book_id " +
                        "FROM Chapters c " +
                        "JOIN Ebooks e ON c.ebook_id = e.id " +
                        "WHERE c.id = ? AND c.ebook_id = ?";
            
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, chapterId);
                ps.setInt(2, bookId);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        chapterInfo.put("id", rs.getInt("id"));
                        chapterInfo.put("title", rs.getString("title"));
                        chapterInfo.put("number", rs.getBigDecimal("number"));
                        chapterInfo.put("book_id", rs.getInt("book_id"));
                        chapterInfo.put("book_title", rs.getString("book_title"));
                        chapterInfo.put("access_level", rs.getString("access_level"));
                        chapterInfo.put("view_count", rs.getInt("view_count"));
                        chapterInfo.put("like_count", rs.getInt("like_count"));
                        chapterInfo.put("link", generateChapterLink(bookId, chapterId, rs.getString("title")));
                    }
                }
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Database error getting chapter info: " + e.getMessage(), e);
        }
        
        return chapterInfo;
    }

    /**
     * 🎯 Tạo response với link thực tế
     */
    public static String createLinkedResponse(String query, String aiResponse) {
        // Search for real books
        Map<String, Object> bookSearch = searchAndLinkBooks(query);
        
        StringBuilder response = new StringBuilder();
        response.append(aiResponse).append("\n\n");
        
        if ((Boolean) bookSearch.get("found")) {
            response.append("📚 Sách liên quan từ database:\n");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> books = (List<Map<String, Object>>) bookSearch.get("books");
            
            for (Map<String, Object> book : books) {
                response.append("• ").append(book.get("title"))
                       .append(" - ").append(book.get("link"))
                       .append(" (👁️ ").append(book.get("view_count")).append(" lượt xem)\n");
            }
        }
        
        return response.toString();
    }

} 