package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.dao.EbookAIDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.EbookAI;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Service để tự động sửa các EbookAI record thiếu
 * Khi sách đã được upload nhưng EbookAI record bị thiếu
 */
public class EbookAIFixService {
    
    private static final Logger logger = Logger.getLogger(EbookAIFixService.class.getName());
    private static final String UPLOADS_FOLDER = "D:\\EbookWebsite\\uploads";
    
    private EbookDAO ebookDAO;
    private EbookAIDAO ebookAIDAO;
    
    public EbookAIFixService() {
        this.ebookDAO = new EbookDAO();
        this.ebookAIDAO = new EbookAIDAO();
    }
    
    /**
     * Tự động kiểm tra và tạo EbookAI record cho một sách cụ thể
     */
    public boolean autoFixEbookAI(int ebookId) {
        try {
            // 1. Kiểm tra sách có tồn tại không
            Ebook ebook = ebookDAO.getEbookById(ebookId);
            if (ebook == null) {
                logger.warning("❌ Ebook ID " + ebookId + " không tồn tại!");
                return false;
            }
            
            // 2. Kiểm tra EbookAI record đã có chưa
            EbookAI existingAI = ebookAIDAO.getByEbookId(ebookId);
            if (existingAI != null) {
                logger.info("✅ EbookAI record đã tồn tại cho book ID " + ebookId);
                return true;
            }
            
            // 3. Tìm file tương ứng trong uploads folder
            String fileName = findMatchingFile(ebook.getTitle());
            if (fileName == null) {
                logger.warning("❌ Không tìm thấy file cho sách: " + ebook.getTitle());
                return false;
            }
            
            // 4. Tạo EbookAI record mới
            EbookAI newAI = new EbookAI();
            newAI.setEbookId(ebookId);
            newAI.setFileName(fileName);
            newAI.setOriginalFileName(fileName);
            newAI.setSummary(null); // AI sẽ tạo khi đọc sách
            newAI.setStatus("completed");
            newAI.setCreatedAt(LocalDateTime.now());
            newAI.setUpdatedAt(LocalDateTime.now());
            
            // 5. Lưu vào database
            ebookAIDAO.insertEbookAI(newAI);
            
            logger.info("✅ Đã tạo EbookAI record cho book ID " + ebookId + " với file: " + fileName);
            return true;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Lỗi database khi fix EbookAI cho book ID " + ebookId, e);
            return false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Lỗi không xác định khi fix EbookAI cho book ID " + ebookId, e);
            return false;
        }
    }
    
    /**
     * Tự động fix tất cả EbookAI record thiếu
     */
    public int autoFixAllMissingEbookAI() {
        int fixedCount = 0;
        
        try {
            // Lấy tất cả ebooks
            List<Ebook> allEbooks = ebookDAO.selectAllEbooks();
            
            for (Ebook ebook : allEbooks) {
                if (ebook.getStatus().equals("deleted")) {
                    continue; // Bỏ qua sách đã xóa
                }
                
                // Kiểm tra và fix từng sách
                if (autoFixEbookAI(ebook.getId())) {
                    fixedCount++;
                }
            }
            
            logger.info("🎉 Hoàn thành auto-fix: " + fixedCount + " EbookAI records đã được tạo");
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Lỗi khi auto-fix tất cả EbookAI records", e);
        }
        
        return fixedCount;
    }
    
    /**
     * Tìm file phù hợp với tiêu đề sách trong uploads folder
     */
    private String findMatchingFile(String bookTitle) {
        File uploadsDir = new File(UPLOADS_FOLDER);
        if (!uploadsDir.exists() || !uploadsDir.isDirectory()) {
            logger.warning("❌ Thư mục uploads không tồn tại: " + UPLOADS_FOLDER);
            return null;
        }
        
        File[] files = uploadsDir.listFiles();
        if (files == null) {
            return null;
        }
        
        logger.info("🔍 Đang tìm file cho sách: '" + bookTitle + "'");
        logger.info("📁 Thư mục uploads: " + UPLOADS_FOLDER);
        logger.info("📄 Số file có sẵn: " + files.length);
        
        // Debug: Liệt kê tất cả files
        for (File file : files) {
            if (file.isFile()) {
                logger.info("   📄 " + file.getName());
            }
        }
        
        // 1. Thử tìm file có tên chính xác
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String fileNameWithoutExt = removeFileExtension(fileName);
                
                // So sánh chính xác
                if (fileNameWithoutExt.equals(bookTitle)) {
                    logger.info("🎯 Tìm thấy file chính xác: " + fileName + " cho sách: " + bookTitle);
                    return fileName;
                }
            }
        }
        
        // 2. Tìm file có chứa từ khóa chính (như tên tác giả)
        String[] titleWords = bookTitle.split("\\s+");
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String fileNameWithoutExt = removeFileExtension(fileName);
                
                // Kiểm tra các từ khóa quan trọng
                int matchCount = 0;
                int importantMatches = 0;
                
                for (String word : titleWords) {
                    if (word.length() > 2) {
                        String wordLower = word.toLowerCase();
                        String fileLower = fileNameWithoutExt.toLowerCase();
                        
                        if (fileLower.contains(wordLower)) {
                            matchCount++;
                            logger.info("✅ Từ khóa matched: '" + word + "' trong file: " + fileName);
                            
                            // Tên tác giả hoặc từ khóa đặc biệt có điểm số cao hơn
                            if (isImportantKeyword(wordLower)) {
                                importantMatches++;
                                logger.info("🎯 Từ khóa QUAN TRỌNG: '" + word + "' trong file: " + fileName);
                            }
                        } else {
                            logger.info("❌ Từ khóa KHÔNG match: '" + word + "' không có trong file: " + fileName);
                        }
                    }
                }
                
                // Nếu có từ khóa quan trọng hoặc match >= 40% từ khóa
                boolean hasImportantMatch = importantMatches > 0;
                boolean hasEnoughMatches = matchCount >= Math.max(1, titleWords.length * 0.4);
                
                if (hasImportantMatch || hasEnoughMatches) {
                    logger.info("🎯 Tìm thấy file phù hợp: " + fileName + " cho sách: " + bookTitle);
                    logger.info("   - Từ khóa matched: " + matchCount + "/" + titleWords.length);
                    logger.info("   - Từ khóa quan trọng: " + importantMatches);
                    return fileName;
                }
            }
        }
        
        // 3. Tìm kiếm đặc biệt cho tác giả Victor Hugo
        if (bookTitle.toLowerCase().contains("victor") && bookTitle.toLowerCase().contains("hugo")) {
            logger.info("🔍 Tìm kiếm đặc biệt cho tác giả Victor Hugo...");
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    String fileLower = fileName.toLowerCase();
                    
                    if (fileLower.contains("victor") && fileLower.contains("hugo")) {
                        logger.info("🎯 Tìm thấy file Victor Hugo: " + fileName + " cho sách: " + bookTitle);
                        return fileName;
                    }
                }
            }
        }
        
        // 4. Fallback: Tìm file có chứa bất kỳ từ nào trong tên
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String fileNameWithoutExt = removeFileExtension(fileName);
                
                for (String word : titleWords) {
                    if (word.length() > 3 && 
                        fileNameWithoutExt.toLowerCase().contains(word.toLowerCase())) {
                        logger.info("🎯 Fallback match: " + fileName + " cho sách: " + bookTitle + " (từ khóa: " + word + ")");
                        return fileName;
                    }
                }
            }
        }
        
        logger.warning("❌ Không tìm thấy file phù hợp cho sách: " + bookTitle);
        logger.warning("📁 Các file có sẵn:");
        for (File file : files) {
            if (file.isFile()) {
                logger.warning("   📄 " + file.getName());
            }
        }
        
        return null;
    }
    
    /**
     * Kiểm tra từ khóa có quan trọng không (như tên tác giả)
     */
    private boolean isImportantKeyword(String word) {
        // Danh sách tên tác giả và từ khóa quan trọng
        String[] importantKeywords = {
            "victor", "hugo", "shakespeare", "tolstoy", "hemingway", 
            "dickens", "austen", "joyce", "kafka", "orwell",
            "dostoevsky", "chekhov", "poe", "wilde", "twain",
            "verne", "dumas", "balzac", "zola", "flaubert",
            "nam", "cao", "nguyễn", "du", "hồ", "chí", "minh",
            "paris", "london", "roma", "moscow", "new", "york"
        };
        
        String wordLower = word.toLowerCase();
        for (String keyword : importantKeywords) {
            if (wordLower.equals(keyword) || wordLower.contains(keyword)) {
                logger.info("🎯 Từ khóa quan trọng được phát hiện: '" + word + "' matches '" + keyword + "'");
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Loại bỏ file extension
     */
    private String removeFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }
    
    /**
     * Kiểm tra trạng thái EbookAI cho một sách
     */
    public String checkEbookAIStatus(int ebookId) {
        try {
            Ebook ebook = ebookDAO.getEbookById(ebookId);
            if (ebook == null) {
                return "❌ Sách không tồn tại";
            }
            
            EbookAI ai = ebookAIDAO.getByEbookId(ebookId);
            if (ai == null) {
                return "❌ EbookAI record THIẾU - cần fix";
            }
            
            if (ai.getFileName() == null || ai.getFileName().trim().isEmpty()) {
                return "⚠️ EbookAI record tồn tại nhưng thiếu file_name";
            }
            
            // Kiểm tra file có tồn tại không
            File file = new File(UPLOADS_FOLDER, ai.getFileName());
            if (!file.exists()) {
                return "⚠️ EbookAI record OK nhưng file không tồn tại: " + ai.getFileName();
            }
            
            return "✅ EbookAI record hoàn chỉnh với file: " + ai.getFileName();
            
        } catch (SQLException e) {
            return "❌ Lỗi database: " + e.getMessage();
        }
    }
} 