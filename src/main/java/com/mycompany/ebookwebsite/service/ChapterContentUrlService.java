package com.mycompany.ebookwebsite.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mycompany.ebookwebsite.dao.ChapterDAO;
import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.Chapter;
import com.mycompany.ebookwebsite.model.Ebook;

/**
 * Service để tự động cập nhật contentUrl cho chapter
 * dựa trên cấu trúc thư mục bookreal
 */
public class ChapterContentUrlService {
    
    private final ChapterDAO chapterDAO = new ChapterDAO();
    private final EbookDAO ebookDAO = new EbookDAO();
    
    // Mapping giữa release_type và tên folder trong bookreal
    private static final Map<String, String> CATEGORY_MAPPING = new HashMap<>();
    
    static {
        CATEGORY_MAPPING.put("Văn học Việt Nam", "Văn học Việt Nam");
        CATEGORY_MAPPING.put("Tiểu thuyết phương Tây", "Tiểu thuyết phương Tây");
        CATEGORY_MAPPING.put("Tiểu thuyết Trung Quốc", "Tiểu thuyết Trung Quốc");
        CATEGORY_MAPPING.put("Truyện ngắn", "Truyện ngắn");
        CATEGORY_MAPPING.put("Truyện ma", "Truyện ma");
        CATEGORY_MAPPING.put("Truyện cười", "Truyện cười");
        CATEGORY_MAPPING.put("Truyện tuổi teen", "Truyện tuổi teen");
        CATEGORY_MAPPING.put("Trinh thám", "Trinh thám");
        CATEGORY_MAPPING.put("Phiêu lưu", "Phiêu lưu");
        CATEGORY_MAPPING.put("Huyền bí - Giả tưởng", "Huyền bí - Giả tưởng");
        CATEGORY_MAPPING.put("Kiếm hiệp - Tiên hiệp", "Kiếm hiệp - Tiên hiệp");
        CATEGORY_MAPPING.put("Cổ tích - Thần thoại", "Cổ tích - Thần thoại");
        CATEGORY_MAPPING.put("Hồi ký - Tùy bút", "Hồi ký - Tùy bút");
        CATEGORY_MAPPING.put("Thơ hay", "Thơ hay");
        CATEGORY_MAPPING.put("Kinh tế", "Kinh tế");
        CATEGORY_MAPPING.put("Marketing", "Marketing");
        CATEGORY_MAPPING.put("Khoa học - Kỹ thuật", "Khoa học - Kỹ thuật");
        CATEGORY_MAPPING.put("Công nghệ thông tin", "Công nghệ thông tin");
        CATEGORY_MAPPING.put("Lịch sử - Chính trị", "Lịch sử - Chính trị");
        CATEGORY_MAPPING.put("Văn hóa - Tôn giáo", "Văn hóa - Tôn giáo");
        CATEGORY_MAPPING.put("Tâm lý - Kỹ năng sống", "Tâm lý - Kỹ năng sống");
        CATEGORY_MAPPING.put("Thể thao - Nghệ thuật", "Thể thao - Nghệ thuật");
        CATEGORY_MAPPING.put("Ẩm thực - Nấu ăn", "Ẩm thực - Nấu ăn");
        CATEGORY_MAPPING.put("Y học", "Y học");
        CATEGORY_MAPPING.put("Tử vi - Phong thủy", "Tử vi - Phong thủy");
        CATEGORY_MAPPING.put("Triết học", "Triết học");
        CATEGORY_MAPPING.put("Nông - Lâm - Ngư", "Nông - Lâm - Ngư");
        CATEGORY_MAPPING.put("Ngoại ngữ", "Ngoại ngữ");
        CATEGORY_MAPPING.put("Kiến trúc - Xây dựng", "Kiến trúc - Xây dựng");
        CATEGORY_MAPPING.put("Pháp luật", "Pháp luật");
        CATEGORY_MAPPING.put("Sách giáo khoa", "Sách giáo khoa");
    }
    
    /**
     * Tạo contentUrl cho một chapter dựa trên thông tin sách
     */
    public String generateContentUrl(Ebook ebook, Chapter chapter) {
        String category = CATEGORY_MAPPING.getOrDefault(ebook.getReleaseType(), "Văn học Việt Nam");
        String bookTitle = ebook.getTitle();
        int chapterNumber = (int) chapter.getNumber();
        
        return String.format("bookreal/%s/%s/chuong_%03d.txt", 
                           category, bookTitle, chapterNumber);
    }
    
    /**
     * Cập nhật contentUrl cho tất cả chapter của một sách
     */
    public void updateContentUrlsForBook(int bookId) throws SQLException {
        Ebook ebook = ebookDAO.getEbookById(bookId);
        if (ebook == null) {
            throw new IllegalArgumentException("Không tìm thấy sách với ID: " + bookId);
        }
        
        List<Chapter> chapters = chapterDAO.getChaptersByBookId(bookId);
        int updatedCount = 0;
        
        for (Chapter chapter : chapters) {
            String newContentUrl = generateContentUrl(ebook, chapter);
            chapter.setContentUrl(newContentUrl);
            
            if (chapterDAO.updateChapter(chapter)) {
                updatedCount++;
                System.out.println("✅ Cập nhật chapter " + chapter.getNumber() + 
                                 " -> " + newContentUrl);
            }
        }
        
        System.out.println("📊 Đã cập nhật " + updatedCount + "/" + chapters.size() + 
                          " chapter cho sách: " + ebook.getTitle());
    }
    
    /**
     * Cập nhật contentUrl cho tất cả chapter trong hệ thống
     */
    public void updateAllContentUrls() throws SQLException {
        // Lấy tất cả sách có chapter
        List<Ebook> ebooks = getAllEbooksWithChapters();
        int totalUpdated = 0;
        
        for (Ebook ebook : ebooks) {
            try {
                updateContentUrlsForBook(ebook.getId());
                totalUpdated++;
            } catch (Exception e) {
                System.err.println("❌ Lỗi cập nhật sách " + ebook.getId() + ": " + e.getMessage());
            }
        }
        
        System.out.println("🎉 Hoàn thành! Đã cập nhật " + totalUpdated + " sách");
    }
    
    /**
     * Lấy danh sách sách có chapter
     */
    private List<Ebook> getAllEbooksWithChapters() throws SQLException {
        // Query để lấy sách có chapter
        String sql = "SELECT DISTINCT e.* FROM Ebooks e " +
                    "INNER JOIN Chapters c ON e.id = c.ebook_id " +
                    "ORDER BY e.id";
        
        List<Ebook> ebooks = new java.util.ArrayList<>();
        try (java.sql.Connection conn = com.mycompany.ebookwebsite.dao.DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Ebook ebook = new Ebook();
                ebook.setId(rs.getInt("id"));
                ebook.setTitle(rs.getString("title"));
                ebook.setReleaseType(rs.getString("release_type"));
                ebooks.add(ebook);
            }
        }
        
        return ebooks;
    }
    
    /**
     * Kiểm tra xem file chapter có tồn tại không
     */
    public boolean checkChapterFileExists(String contentUrl) {
        if (contentUrl == null || contentUrl.trim().isEmpty()) {
            return false;
        }
        
        // Tạo đường dẫn đầy đủ
        String webappPath = System.getProperty("catalina.home") + "/webapps/EbookWebsite/";
        String fullPath = webappPath + contentUrl;
        
        java.io.File file = new java.io.File(fullPath);
        return file.exists() && file.isFile();
    }
    
    /**
     * Kiểm tra và báo cáo chapter nào có file, chapter nào không có
     */
    public void validateChapterFiles(int bookId) throws SQLException {
        Ebook ebook = ebookDAO.getEbookById(bookId);
        if (ebook == null) {
            System.err.println("❌ Không tìm thấy sách với ID: " + bookId);
            return;
        }
        
        List<Chapter> chapters = chapterDAO.getChaptersByBookId(bookId);
        System.out.println("📚 Kiểm tra file cho sách: " + ebook.getTitle());
        System.out.println("📊 Tổng số chapter: " + chapters.size());
        
        int existingFiles = 0;
        int missingFiles = 0;
        
        for (Chapter chapter : chapters) {
            String contentUrl = chapter.getContentUrl();
            boolean exists = checkChapterFileExists(contentUrl);
            
            if (exists) {
                existingFiles++;
                System.out.println("✅ Chapter " + chapter.getNumber() + " -> " + contentUrl);
            } else {
                missingFiles++;
                System.out.println("❌ Chapter " + chapter.getNumber() + " -> " + contentUrl + " (KHÔNG TỒN TẠI)");
            }
        }
        
        System.out.println("📈 Kết quả: " + existingFiles + " file tồn tại, " + missingFiles + " file thiếu");
    }
    
    /**
     * Test method để chạy thử
     */
    public static void main(String[] args) {
        ChapterContentUrlService service = new ChapterContentUrlService();
        
        try {
            // Test với sách cụ thể (ID 385)
            System.out.println("🔄 Bắt đầu cập nhật contentUrl...");
            service.updateContentUrlsForBook(385);
            
            // Kiểm tra file
            System.out.println("\n🔍 Kiểm tra file tồn tại...");
            service.validateChapterFiles(385);
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 