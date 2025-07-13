package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.service.LangChain4jAIChatService;
import com.mycompany.ebookwebsite.utils.PathManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import com.mycompany.ebookwebsite.utils.Utils;

/**
 * 📚 Book Upload Checker sử dụng LangChain4j AI
 * - Chọn đường dẫn file sách cần upload
 * - Đọc nội dung file (txt, pdf, docx)
 * - Kiểm duyệt nội dung bằng AI (LangChain4j)
 * - Copy file vào uploads nếu hợp lệ
 * - AI tự động nhận biết metadata (tiêu đề, thể loại, mô tả, tóm tắt)
 * - Lưu vào database và update listBook
 * 
 * Updated to use PathManager for better path management
 */
public class BookUploadCheckerLangChain4jTest {
    
    // 🗂️ Sử dụng PathManager thay vì hard-coded path
    private static String getUploadsFolder() {
        return PathManager.getUploadsPath();
    }
    
    private static LangChain4jAIChatService aiService;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        System.out.println("📚 BOOK UPLOAD CHECKER (LangChain4j AI)");
        System.out.println("=========================================");
        System.out.println("📁 Using uploads path: " + getUploadsFolder());
        System.out.println("Chọn đường dẫn file sách cần upload\n");
        
        aiService = new LangChain4jAIChatService();
        scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("Nhập đường dẫn file sách (hoặc 'exit' để thoát): ");
            String filePath = scanner.nextLine().trim();
            
            if ("exit".equalsIgnoreCase(filePath)) {
                System.out.println("👋 Tạm biệt!");
                break;
            }
            
            File bookFile = new File(filePath);
            if (!bookFile.exists()) {
                System.out.println("❌ File không tồn tại: " + filePath);
                continue;
            }
            
            if (!isSupportedExtension(bookFile.getName())) {
                System.out.println("❌ Định dạng file không được hỗ trợ. Chỉ hỗ trợ: .txt, .pdf, .docx");
                continue;
            }
            
            processBookFile(bookFile);
        }
        scanner.close();
    }

    private static boolean isSupportedExtension(String fileName) {
        String name = fileName.toLowerCase();
        return name.endsWith(".txt") || name.endsWith(".pdf") || name.endsWith(".docx");
    }

    private static void processBookFile(File file) {
        System.out.println("\n📖 Đang xử lý file: " + file.getName());
        System.out.println("📁 Đường dẫn: " + file.getAbsolutePath());
        System.out.println("📏 Kích thước: " + formatFileSize(file.length()));
        
        // Bước 1: Đọc nội dung file
        String content = readFileContent(file);
        if (content == null) {
            return;
        }
        
        // Bước 2: Kiểm duyệt nội dung bằng AI
        if (!checkContentModeration(content)) {
            return;
        }
        
        // Bước 3: Copy file vào uploads
        File uploadedFile = copyFileToUploads(file);
        if (uploadedFile == null) {
            return;
        }
        
        // Bước 4: AI tự động nhận biết metadata
        BookMetadata metadata = extractMetadata(content, file.getName());
        
        // Bước 5: Lưu vào database và update listBook
        saveToDatabase(uploadedFile, metadata);
        
        System.out.println("\n✅ Upload thành công!");
        System.out.println("📚 Tiêu đề: " + metadata.title);
        System.out.println("🏷️ Thể loại: " + metadata.genre);
        System.out.println("📝 Mô tả: " + metadata.description);
        System.out.println("📄 Tóm tắt: " + metadata.summary);
        System.out.println("💾 Đã lưu vào database và cập nhật listBook");
        System.out.println("=".repeat(60));
    }

    private static String readFileContent(File file) {
        System.out.println("\n📖 Đang đọc nội dung file...");
        String ext = getFileExtension(file.getName());
        
        try {
            String content = Utils.readAnyTextFile(file.getAbsolutePath(), ext);
            if (content == null || content.trim().isEmpty()) {
                System.out.println("❌ File rỗng hoặc không đọc được nội dung!");
                return null;
            }
            
            System.out.println("✅ Đọc thành công: " + content.length() + " ký tự");
            return content;
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi đọc file: " + e.getMessage());
            return null;
        }
    }

    private static boolean checkContentModeration(String content) {
        System.out.println("\n🤖 Đang kiểm duyệt nội dung bằng AI...");
        
        String moderationPrompt = "Hãy kiểm duyệt nội dung sách sau. " +
            "Nếu có nội dung vi phạm (bạo lực, sex, thù ghét, vi phạm pháp luật, v.v.), " +
            "trả về 'REJECT' và giải thích lý do. Nếu hợp lệ, trả về 'ACCEPT'.\n" +
            "Nội dung: " + content.substring(0, Math.min(2000, content.length()));
        
        try {
            String aiResult = aiService.processChat(0, "upload-check", moderationPrompt, null);
            System.out.println("🤖 Kết quả kiểm duyệt AI: " + aiResult);
            
            if (aiResult != null && aiResult.trim().toUpperCase().contains("ACCEPT")) {
                System.out.println("✅ Nội dung hợp lệ, có thể upload!");
                return true;
            } else {
                System.out.println("❌ Nội dung bị từ chối!");
                System.out.println("📝 Lý do: " + aiResult);
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi AI kiểm duyệt: " + e.getMessage());
            return false;
        }
    }

    private static File copyFileToUploads(File sourceFile) {
        System.out.println("\n📁 Đang copy file vào uploads...");
        
        try {
            // Tạo thư mục uploads nếu chưa có
            File uploadsDir = new File(getUploadsFolder());
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs();
                System.out.println("📁 Đã tạo thư mục uploads");
            }
            
            // Copy file
            Path source = sourceFile.toPath();
            Path target = Paths.get(getUploadsFolder(), sourceFile.getName());
            
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("✅ Đã copy file vào: " + target.toString());
            return target.toFile();
            
        } catch (IOException e) {
            System.out.println("❌ Lỗi copy file: " + e.getMessage());
            return null;
        }
    }

    private static BookMetadata extractMetadata(String content, String fileName) {
        System.out.println("\n🤖 Đang trích xuất metadata bằng AI...");
        
        String metadataPrompt = "Hãy phân tích nội dung sách sau và trả về metadata theo format JSON:\n" +
            "{\n" +
            "  \"title\": \"Tiêu đề sách\",\n" +
            "  \"genre\": \"Thể loại (tiểu thuyết, khoa học viễn tưởng, fantasy, v.v.)\",\n" +
            "  \"description\": \"Mô tả ngắn gọn về sách (2-3 câu)\",\n" +
            "  \"summary\": \"Tóm tắt nội dung chính (5-7 câu)\"\n" +
            "}\n" +
            "Nội dung: " + content.substring(0, Math.min(3000, content.length()));
        
        try {
            String aiResult = aiService.processChat(0, "metadata-extraction", metadataPrompt, null);
            System.out.println("🤖 Kết quả trích xuất metadata: " + aiResult);
            
            // Parse JSON result (simplified)
            return parseMetadataFromAI(aiResult, fileName);
            
        } catch (Exception e) {
            System.out.println("❌ Lỗi trích xuất metadata: " + e.getMessage());
            return createDefaultMetadata(fileName);
        }
    }

    private static BookMetadata parseMetadataFromAI(String aiResult, String fileName) {
        // Simplified JSON parsing - in real implementation, use proper JSON parser
        BookMetadata metadata = new BookMetadata();
        
        // Extract title
        if (aiResult.contains("\"title\":")) {
            int start = aiResult.indexOf("\"title\":") + 8;
            int end = aiResult.indexOf("\"", start);
            if (end > start) {
                metadata.title = aiResult.substring(start, end).trim();
            }
        }
        
        // Extract genre
        if (aiResult.contains("\"genre\":")) {
            int start = aiResult.indexOf("\"genre\":") + 8;
            int end = aiResult.indexOf("\"", start);
            if (end > start) {
                metadata.genre = aiResult.substring(start, end).trim();
            }
        }
        
        // Extract description
        if (aiResult.contains("\"description\":")) {
            int start = aiResult.indexOf("\"description\":") + 14;
            int end = aiResult.indexOf("\"", start);
            if (end > start) {
                metadata.description = aiResult.substring(start, end).trim();
            }
        }
        
        // Extract summary
        if (aiResult.contains("\"summary\":")) {
            int start = aiResult.indexOf("\"summary\":") + 10;
            int end = aiResult.indexOf("\"", start);
            if (end > start) {
                metadata.summary = aiResult.substring(start, end).trim();
            }
        }
        
        // Set defaults if parsing failed
        if (metadata.title == null || metadata.title.isEmpty()) {
            metadata.title = fileName.replaceFirst("[.][^.]+$", ""); // Remove extension
        }
        if (metadata.genre == null || metadata.genre.isEmpty()) {
            metadata.genre = "Tiểu thuyết";
        }
        if (metadata.description == null || metadata.description.isEmpty()) {
            metadata.description = "Sách được upload tự động";
        }
        if (metadata.summary == null || metadata.summary.isEmpty()) {
            metadata.summary = "Nội dung sách đang được xử lý";
        }
        
        return metadata;
    }

    private static BookMetadata createDefaultMetadata(String fileName) {
        BookMetadata metadata = new BookMetadata();
        metadata.title = fileName.replaceFirst("[.][^.]+$", "");
        metadata.genre = "Tiểu thuyết";
        metadata.description = "Sách được upload tự động";
        metadata.summary = "Nội dung sách đang được xử lý";
        return metadata;
    }

    private static void saveToDatabase(File uploadedFile, BookMetadata metadata) {
        System.out.println("\n💾 Đang lưu vào database...");
        
        try {
            // Import required DAO classes
            com.mycompany.ebookwebsite.dao.EbookDAO ebookDAO = new com.mycompany.ebookwebsite.dao.EbookDAO();
            com.mycompany.ebookwebsite.dao.EbookAIDAO ebookAIDAO = new com.mycompany.ebookwebsite.dao.EbookAIDAO();
            
            // Create Ebook entity
            com.mycompany.ebookwebsite.model.Ebook ebook = new com.mycompany.ebookwebsite.model.Ebook();
            ebook.setTitle(metadata.title);
            ebook.setDescription(metadata.description);
            ebook.setReleaseType(metadata.genre);
            ebook.setLanguage("Tiếng Việt");
            ebook.setStatus("active");
            ebook.setVisibility("public");
            ebook.setUploaderId(1); // Default uploader ID - should be passed from user session
            ebook.setViewCount(0);
            ebook.setCreatedAt(java.time.LocalDateTime.now());
            ebook.setCoverUrl(null); // Will be set later if needed
            
            // Insert Ebook and get generated ID
            ebookDAO.insertEbook(ebook);
            
            // Get the inserted ebook to retrieve the ID
            java.util.List<com.mycompany.ebookwebsite.model.Ebook> books = ebookDAO.selectAllEbooks();
            com.mycompany.ebookwebsite.model.Ebook insertedBook = books.get(books.size() - 1); // Get the last inserted book
            int ebookId = insertedBook.getId();
            
            System.out.println("✅ Ebook inserted with ID: " + ebookId);
            
            // Create EbookAI entity
            com.mycompany.ebookwebsite.model.EbookAI ebookAI = new com.mycompany.ebookwebsite.model.EbookAI();
            ebookAI.setEbookId(ebookId);
            ebookAI.setFileName(uploadedFile.getName());
            ebookAI.setOriginalFileName(uploadedFile.getName());
            ebookAI.setSummary(metadata.summary);
            ebookAI.setStatus("completed");
            ebookAI.setCreatedAt(java.time.LocalDateTime.now());
            ebookAI.setUpdatedAt(java.time.LocalDateTime.now());
            
            // Insert EbookAI
            ebookAIDAO.insertEbookAI(ebookAI);
            
            System.out.println("✅ EbookAI record created successfully");
            System.out.println("📊 Database Summary:");
            System.out.println("   📚 Ebook ID: " + ebookId);
            System.out.println("   📝 Title: " + metadata.title);
            System.out.println("   🏷️ Genre: " + metadata.genre);
            System.out.println("   📁 File: " + uploadedFile.getName());
            System.out.println("   📏 Size: " + formatFileSize(uploadedFile.length()));
            System.out.println("   📍 Path: uploads/" + uploadedFile.getName());
            System.out.println("   🤖 AI Summary: " + metadata.summary.substring(0, Math.min(100, metadata.summary.length())) + "...");
            System.out.println("   📊 AI Status: completed");
            
            // Note: Additional fields like metadata_json, filterStatus, fileSize, etc.
            // are available in the database schema but not in the current EbookAI model.
            // These can be added later if needed by extending the model.
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi lưu database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String getFileExtension(String fileName) {
        int idx = fileName.lastIndexOf('.');
        return (idx > 0) ? fileName.substring(idx + 1).toLowerCase() : "";
    }

    private static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    // Inner class to hold book metadata
    static class BookMetadata {
        String title;
        String genre;
        String description;
        String summary;
    }
} 