package com.mycompany.ebookwebsite.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.service.OpenAIContentSummaryService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ReadBookServlet", urlPatterns = {"/read"})
public class ReadBookServlet extends HttpServlet {
    
    private EbookDAO ebookDAO;
    
    // Cấu hình đường dẫn uploads - tương tự như BookServlet
    private static final String UPLOAD_FOLDER = "uploads";
    
    @Override
    public void init() throws ServletException {
        ebookDAO = new EbookDAO();
    }
    
    /**
     * Lấy đường dẫn thư mục uploads một cách thông minh
     */
    private String getUploadsPath() {
        // Thử các đường dẫn khác nhau theo thứ tự ưu tiên
        String[] possiblePaths = {
            // 1. UU TIEN: Đường dẫn project root (D:\\EbookWebsite\\uploads)
            System.getProperty("user.dir") + File.separator + UPLOAD_FOLDER,
            
            // 2. Thư mục uploads trong thư mục hiện tại
            UPLOAD_FOLDER,
            
            // 3. Đường dẫn trong webapp (development mode)
            getServletContext().getRealPath("/") + UPLOAD_FOLDER,
            
            // 4. Đường dẫn Tomcat webapps
            System.getProperty("catalina.base") + File.separator + "webapps" + 
            File.separator + "EbookWebsite" + File.separator + UPLOAD_FOLDER,
            
            // 5. Đường dẫn trong thư mục bin của Tomcat (hiện tại)
            System.getProperty("catalina.base") + File.separator + "bin" + 
            File.separator + UPLOAD_FOLDER
        };
        
        // Kiểm tra thư mục nào tồn tại và có file
        for (String path : possiblePaths) {
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                // Kiểm tra xem có file nào trong thư mục không
                File[] files = dir.listFiles();
                if (files != null && files.length > 0) {
                    System.out.println("✅ Sử dụng uploads path: " + path);
                    return path;
                }
            }
        }
        
        // Nếu không tìm thấy, sử dụng đường dẫn đầu tiên và tạo thư mục
        String defaultPath = possiblePaths[0];
        new File(defaultPath).mkdirs();
        System.out.println("📁 Tạo uploads path mặc định: " + defaultPath);
        return defaultPath;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String bookIdStr = request.getParameter("id");
        
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        try {
            int bookId = Integer.parseInt(bookIdStr);
            Ebook book = ebookDAO.selectEbook(bookId);
            
            if (book == null) {
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }
            
            // GHI LOG LỊCH SỬ ĐỌC SÁCH
            jakarta.servlet.http.HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                com.mycompany.ebookwebsite.model.User user = (com.mycompany.ebookwebsite.model.User) session.getAttribute("user");
                try {
                    com.mycompany.ebookwebsite.dao.UserReadDAO userReadDAO = new com.mycompany.ebookwebsite.dao.UserReadDAO();
                    userReadDAO.insertOrUpdateUserRead(new com.mycompany.ebookwebsite.model.UserRead(
                        user.getId(), bookId, null, java.time.LocalDate.now()
                    ));
                } catch (Exception ex) {
                    System.err.println("[UserRead LOG] Lỗi ghi lịch sử đọc sách: " + ex.getMessage());
                }
            }
            
            // Tăng lượt xem
            ebookDAO.incrementViewCount(bookId);
            
            // Đọc nội dung file sách
            String bookContent = readBookContent(book);
            
            // Tích hợp AI summary nếu chưa có
            if (book.getSummary() == null || book.getSummary().trim().isEmpty()) {
            try {
                OpenAIContentSummaryService summaryService = new OpenAIContentSummaryService();
                    String summary = summaryService.summarize(bookContent);
                    book.setSummary(summary);
            } catch (Exception ex) {
                    book.setSummary("Không thể tạo tóm tắt AI: " + ex.getMessage());
                }
            }
            
            // Set attributes cho JSP
            request.setAttribute("book", book);
            request.setAttribute("bookContent", bookContent);
            
            // Forward đến trang đọc sách
            request.getRequestDispatcher("/book/read.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi database");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi server");
        }
    }
    
    private String readBookContent(Ebook book) {
        try {
            String uploadsPath = getUploadsPath();
            String bookTitle = book.getTitle();
            
            // ✅ PHƯƠNG PHÁP CHÍNH: Sử dụng fileName từ database
            String fileName = book.getFileName();
            
            if (fileName != null && !fileName.trim().isEmpty()) {
                Path filePath = Paths.get(uploadsPath, fileName);
                
            if (Files.exists(filePath)) {
                    System.out.println("✅ Load file từ DB mapping: " + fileName);
                    
                    // ✅ FIX: Sử dụng Utils.readAnyTextFile thay vì Files.readString
                    try {
                        String content = com.mycompany.ebookwebsite.utils.Utils.readAnyTextFile(filePath.toString(), getFileExtension(fileName));
                        
                        // Validate content không empty
                        if (content != null && !content.trim().isEmpty()) {
                            return content;
                        } else {
                            System.out.println("⚠️ File rỗng: " + fileName);
                            return "📝 File sách tồn tại nhưng không có nội dung: " + fileName;
                        }
                    } catch (Exception e) {
                        System.out.println("⚠️ Lỗi đọc file: " + e.getMessage());
                        return "💥 Lỗi đọc file: " + e.getMessage();
                    }
                } else {
                    System.out.println("❌ File không tồn tại: " + filePath);
                    // Fallback to fuzzy search nếu file không tồn tại
                }
            } else {
                System.out.println("⚠️ Sách chưa có fileName trong database: " + bookTitle);
                // Fallback to fuzzy search
            }
            
            // ✅ FALLBACK: Fuzzy search chỉ khi fileName không có hoặc file không tồn tại
            System.out.println("🔍 Fallback to fuzzy search cho sách: " + bookTitle);
            
            // Danh sách các tên file có thể dựa trên title
            String[] possibleFileNames = {
                bookTitle + ".txt",
                bookTitle.replace("–", "-") + ".txt",  // Thay em dash bằng hyphen
                bookTitle.replace("—", "-") + ".txt",  // Thay em dash bằng hyphen
                bookTitle.replaceAll("[^a-zA-Z0-9\\s]", "-") + ".txt",  // Thay ký tự đặc biệt
                bookTitle.replaceAll("\\s+", "_") + ".txt",  // Thay space bằng underscore
                bookTitle.toLowerCase().replaceAll("[–—]", "-").replaceAll("[^a-z0-9\\s\\-]", "").replaceAll("\\s+", "") + ".txt"  // Normalize như EbookDAO
            };
            
            // Thử tìm file với các tên khác nhau
            for (String possibleFileName : possibleFileNames) {
                Path filePath = Paths.get(uploadsPath, possibleFileName);
                if (Files.exists(filePath)) {
                    System.out.println("🔍 Tìm thấy file bằng fuzzy search: " + possibleFileName);
                    
                    // ✅ CẬP NHẬT DATABASE với fileName tìm được
                    try {
                        book.setFileName(possibleFileName);
                        ebookDAO.updateEbook(book);
                        System.out.println("💾 Đã cập nhật fileName vào database: " + possibleFileName);
                    } catch (Exception e) {
                        System.out.println("⚠️ Không thể cập nhật fileName vào database: " + e.getMessage());
                    }
                    
                    // ✅ FIX: Sử dụng Utils.readAnyTextFile thay vì Files.readString
                    try {
                        return com.mycompany.ebookwebsite.utils.Utils.readAnyTextFile(filePath.toString(), getFileExtension(possibleFileName));
                    } catch (Exception e) {
                        System.out.println("⚠️ Lỗi đọc file: " + e.getMessage());
                        return "💥 Lỗi đọc file: " + e.getMessage();
                    }
                }
            }
            
            // ✅ ADVANCED FUZZY SEARCH: Tìm file theo timestamp prefix và original filename
            File uploadsDir = new File(uploadsPath);
            if (uploadsDir.exists() && uploadsDir.isDirectory()) {
                File[] files = uploadsDir.listFiles();
                if (files != null) {
                    String normalizedTitle = normalizeFileName(bookTitle);
                    String originalFileName = book.getOriginalFileName();
                    
                    System.out.println("🔍 Advanced search cho: " + bookTitle);
                    System.out.println("📝 Original filename: " + originalFileName);
                    
                    for (File file : files) {
                        if (file.isFile()) {
                            String currentFileName = file.getName();
                            
                            // CASE 1: Match theo original filename (bỏ qua timestamp prefix)
                            if (originalFileName != null && !originalFileName.trim().isEmpty()) {
                                // Remove timestamp prefix nếu có
                                String cleanFileName = currentFileName.replaceAll("^\\d+_", "");
                                
                                if (cleanFileName.equalsIgnoreCase(originalFileName)) {
                                    System.out.println("🎯 Tìm thấy file theo original filename: " + currentFileName);
                                    
                                    // ✅ CẬP NHẬT DATABASE với fileName tìm được
                                    try {
                                        book.setFileName(currentFileName);
                                        ebookDAO.updateEbook(book);
                                        System.out.println("💾 Đã cập nhật fileName vào database: " + currentFileName);
                                    } catch (Exception e) {
                                        System.out.println("⚠️ Không thể cập nhật fileName vào database: " + e.getMessage());
                                    }
                                    
                                    try {
                                        return com.mycompany.ebookwebsite.utils.Utils.readAnyTextFile(file.getAbsolutePath(), getFileExtension(currentFileName));
                                    } catch (Exception e) {
                                        System.out.println("⚠️ Lỗi đọc file: " + e.getMessage());
                                        return "💥 Lỗi đọc file: " + e.getMessage();
                                    }
                                }
                            }
                            
                            // CASE 2: Match theo title similarity (support cả .txt và .pdf)
                            if (currentFileName.endsWith(".txt") || currentFileName.endsWith(".pdf") || currentFileName.endsWith(".doc") || currentFileName.endsWith(".docx")) {
                                // Remove extension và timestamp prefix
                                String baseFileName = currentFileName.replaceAll("\\.(txt|pdf|doc|docx)$", "").replaceAll("^\\d+_", "");
                                String normalizedFileName = normalizeFileName(baseFileName);
                                
                                // Kiểm tra độ tương tự
                                if (isSimilar(normalizedTitle, normalizedFileName)) {
                                    System.out.println("🎯 Tìm thấy file bằng similarity matching: " + currentFileName);
                                    
                                    // ✅ CẬP NHẬT DATABASE với fileName tìm được
                                    try {
                                        book.setFileName(currentFileName);
                                        ebookDAO.updateEbook(book);
                                        System.out.println("💾 Đã cập nhật fileName vào database: " + currentFileName);
                                    } catch (Exception e) {
                                        System.out.println("⚠️ Không thể cập nhật fileName vào database: " + e.getMessage());
                                    }
                                    
                                    try {
                                        return com.mycompany.ebookwebsite.utils.Utils.readAnyTextFile(file.getAbsolutePath(), getFileExtension(currentFileName));
                                    } catch (Exception e) {
                                        System.out.println("⚠️ Lỗi đọc file: " + e.getMessage());
                                        return "💥 Lỗi đọc file: " + e.getMessage();
                                    }
                                }
                            }
                        }
                    }
                    
                    // Debug: liệt kê tất cả file trong thư mục
                    System.out.println("📁 Danh sách file trong " + uploadsPath + ":");
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".txt")) {
                            System.out.println("   📄 " + file.getName());
                        }
                    }
                }
            }
            
            // Fallback: thử đọc file mẫu
            Path fallbackPath = Paths.get(uploadsPath, "sample_book.txt");
                if (Files.exists(fallbackPath)) {
                return "📝 Nội dung mẫu (không tìm thấy file gốc):\n\n" + 
                       Files.readString(fallbackPath, java.nio.charset.StandardCharsets.UTF_8);
            }
            
            // Trả về thông báo chi tiết để debug
            StringBuilder debugInfo = new StringBuilder();
            debugInfo.append("❌ Không tìm thấy file nội dung cho sách.\n\n");
            debugInfo.append("📋 Thông tin debug:\n");
            debugInfo.append("- ID sách: ").append(book.getId()).append("\n");
            debugInfo.append("- Tên sách: ").append(bookTitle).append("\n");
            debugInfo.append("- FileName từ DB: ").append(fileName != null ? fileName : "NULL").append("\n");
            debugInfo.append("- Thư mục uploads: ").append(uploadsPath).append("\n\n");
            
            debugInfo.append("📝 Các tên file đã thử:\n");
            for (String possibleFileName : possibleFileNames) {
                debugInfo.append("  • ").append(possibleFileName).append("\n");
            }
            
            // Liệt kê file có sẵn
            File dir = new File(uploadsPath);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null && files.length > 0) {
                    debugInfo.append("\n📁 File có sẵn trong uploads:\n");
                    for (File file : files) {
                        if (file.isFile()) {
                            debugInfo.append("  📄 ").append(file.getName()).append("\n");
                        }
                    }
                } else {
                    debugInfo.append("\n📂 Thư mục uploads trống\n");
                }
            } else {
                debugInfo.append("\n❌ Thư mục uploads không tồn tại\n");
            }
            
            debugInfo.append("\n💡 Hướng dẫn khắc phục:\n");
            debugInfo.append("1. Kiểm tra file có tồn tại trong thư mục uploads\n");
            debugInfo.append("2. Chạy script database_update.sql để cập nhật fileName\n");
            debugInfo.append("3. Upload lại file sách nếu cần thiết\n");
            
            return debugInfo.toString();
            
        } catch (IOException e) {
            e.printStackTrace();
            return "💥 Lỗi đọc file: " + e.getMessage() + 
                   "\n\nThông tin sách:" +
                   "\n- ID: " + book.getId() +
                   "\n- Title: " + book.getTitle() +
                   "\n- FileName: " + book.getFileName();
        }
    }
    
    /**
     * Chuẩn hóa tên file để so sánh
     */
    private String normalizeFileName(String fileName) {
        return fileName.toLowerCase()
                .replaceAll("[–—]", "-")  // Thay em dash, en dash bằng hyphen
                .replaceAll("[^a-z0-9\\s\\-]", "")  // Chỉ giữ chữ, số, space, hyphen
                .replaceAll("\\s+", " ")  // Gộp nhiều space thành 1
                .trim();
    }
    
    /**
     * Kiểm tra 2 tên file có tương tự không
     */
    private boolean isSimilar(String title1, String title2) {
        // So sánh chính xác
        if (title1.equals(title2)) {
            return true;
        }
        
        // Kiểm tra chứa nhau (ít nhất 70% độ tương tự)
        int minLength = Math.min(title1.length(), title2.length());
        if (minLength < 5) return false;  // Quá ngắn để so sánh
        
        String shorter = title1.length() < title2.length() ? title1 : title2;
        String longer = title1.length() >= title2.length() ? title1 : title2;
        
        return longer.contains(shorter) || calculateSimilarity(title1, title2) > 0.7;
    }
    
    /**
     * Tính độ tương tự giữa 2 chuỗi (Levenshtein distance)
     */
    private double calculateSimilarity(String s1, String s2) {
        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) return 1.0;
        
        return (maxLength - levenshteinDistance(s1, s2)) / (double) maxLength;
    }
    
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        
        return dp[s1.length()][s2.length()];
    }
    
    /**
     * Lấy file extension từ tên file
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "";
        }
        
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot == -1 || lastDot == fileName.length() - 1) {
            return "";
        }
        
        return fileName.substring(lastDot + 1).toLowerCase();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
} 