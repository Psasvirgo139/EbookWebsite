package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Chapter;
import com.mycompany.ebookwebsite.model.Volume;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.ChapterService;
import com.mycompany.ebookwebsite.service.CoinService;
import com.mycompany.ebookwebsite.service.VolumeService;
import com.mycompany.ebookwebsite.service.CommentService;
import com.mycompany.ebookwebsite.service.CommentVoteService;
import com.mycompany.ebookwebsite.service.OpenAIContentSummaryService;
import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.service.EbookWithAIService;
import com.mycompany.ebookwebsite.service.EbookWithAIService.EbookWithAI;
import com.mycompany.ebookwebsite.service.EbookAIFixService;
import com.mycompany.ebookwebsite.utils.EbookValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Combined BookReadServlet - handles both chapter reading and full book reading
 * Merged from ReadBookServlet + BookReadServlet
 */
@WebServlet(urlPatterns = {"/book/read", "/read"})
public class BookReadServlet extends HttpServlet {
    private EbookService ebookService;
    private ChapterService chapterService;
    private CoinService coinService;
    private VolumeService volumeService;
    private CommentService commentService;
    private CommentVoteService voteService;
    private EbookDAO ebookDAO;
    private EbookWithAIService ebookWithAIService;
    private EbookAIFixService ebookAIFixService;
    
    // Upload folder configuration from ReadBookServlet
    private static final String UPLOAD_FOLDER = "uploads";

    @Override
    public void init() {
        ebookService = new EbookService();
        chapterService = new ChapterService();
        coinService = new CoinService();
        volumeService = new VolumeService();
        commentService = new CommentService();
        coinService = new CoinService();
        voteService = new CommentVoteService();
        ebookDAO = new EbookDAO();
        ebookWithAIService = new EbookWithAIService();
        ebookAIFixService = new EbookAIFixService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Determine reading mode based on URL and parameters
        String requestURI = request.getRequestURI();
        String mode = determineReadingMode(request, requestURI);
        
        if ("full_book".equals(mode)) {
            handleFullBookReading(request, response);
        } else {
            handleChapterReading(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("unlock".equals(action)) {
            handleUnlockAction(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    /**
     * Determine reading mode based on URL and parameters
     */
    private String determineReadingMode(HttpServletRequest request, String requestURI) {
        // If URL is /read (old ReadBookServlet pattern), use full book mode
        if (requestURI.endsWith("/read")) {
            return "full_book";
        }
        
        // If has chapter parameters, use chapter mode
        String chapterId = request.getParameter("chapterId");
        String chapterParam = request.getParameter("chapter");
        
        if (chapterId != null || chapterParam != null) {
            return "chapter";
        }
        
        // Default to full book mode for backward compatibility
        return "full_book";
    }

    /**
     * Handle full book reading (from ReadBookServlet)
     */
    private void handleFullBookReading(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get book ID from parameter (support both 'id' and 'bookId')
        String bookIdStr = request.getParameter("id");
        if (bookIdStr == null) {
            bookIdStr = request.getParameter("bookId");
        }
        
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        try {
            int bookId = Integer.parseInt(bookIdStr);
            EbookWithAI book = ebookWithAIService.getEbookWithAI(bookId);
            
            if (book == null) {
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }
            
            // Log reading history
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("user") != null) {
                User user = (User) session.getAttribute("user");
                try {
                    com.mycompany.ebookwebsite.dao.UserReadDAO userReadDAO = new com.mycompany.ebookwebsite.dao.UserReadDAO();
                    userReadDAO.insertOrUpdateUserRead(new com.mycompany.ebookwebsite.model.UserRead(
                        user.getId(), bookId, null, java.time.LocalDate.now()
                    ));
                } catch (Exception ex) {
                    System.err.println("[UserRead LOG] Lỗi ghi lịch sử đọc sách: " + ex.getMessage());
                }
            }
            
            // Increment view count
            ebookWithAIService.incrementViewCount(bookId);
            
            // 🔧 AUTO-FIX: Kiểm tra và tạo EbookAI record nếu thiếu
            if (book.getAiData() == null) {
                System.out.println("⚠️ EbookAI record thiếu cho book ID: " + bookId + " - Đang auto-fix...");
                boolean fixed = ebookAIFixService.autoFixEbookAI(bookId);
                if (fixed) {
                    System.out.println("✅ Auto-fix thành công - Reload book data...");
                    // Reload book data với EbookAI record mới
                    book = ebookWithAIService.getEbookWithAI(bookId);
                } else {
                    System.out.println("❌ Auto-fix thất bại cho book ID: " + bookId);
                }
            }
            
            // Read book content
            String bookContent = readBookContent(book);
            System.out.println("📖 Book content result: " + (bookContent != null ? bookContent.length() + " chars" : "NULL"));
            System.out.println("📝 First 200 chars: " + (bookContent != null && bookContent.length() > 200 ? bookContent.substring(0, 200) + "..." : bookContent));
            
            // AI summary integration with database persistence
            String currentSummary = book.getSummary();
            System.out.println("🔍 Current summary in database: " + (currentSummary != null ? "'" + currentSummary + "'" : "NULL"));
            
            if (currentSummary == null || currentSummary.trim().isEmpty()) {
                System.out.println("🤖 Starting AI summary generation...");
                
                // Check if we have valid content to summarize
                if (bookContent == null || bookContent.trim().isEmpty()) {
                    String errorMsg = "❌ Không thể tạo tóm tắt AI: Không có nội dung sách để tóm tắt";
                    System.err.println(errorMsg);
                    book.setSummary(errorMsg);
                } else if (bookContent.startsWith("❌ Không tìm thấy file") || bookContent.startsWith("💥 Lỗi đọc file")) {
                    String errorMsg = "❌ Không thể tạo tóm tắt AI: " + bookContent;
                    System.err.println(errorMsg);
                    book.setSummary(errorMsg);
                } else {
                    try {
                        System.out.println("🤖 Generating AI summary for book: " + book.getTitle());
                        System.out.println("📄 Content to summarize: " + bookContent.length() + " characters");
                        
                        OpenAIContentSummaryService summaryService = new OpenAIContentSummaryService();
                        String summary = summaryService.summarize(bookContent);
                        
                        System.out.println("✅ AI summary generated: " + (summary != null ? summary.length() + " chars" : "NULL"));
                        System.out.println("📝 Generated summary: " + summary);
                        
                        // Save AI summary to database using service
                        boolean saved = ebookWithAIService.updateSummary(book.getId(), summary);
                        if (saved) {
                            System.out.println("✅ AI summary saved to database for book ID: " + book.getId());
                            book.setSummary(summary); // Update in-memory object for JSP
                        } else {
                            System.err.println("⚠️ Failed to save AI summary to database. Check migration status.");
                            // Still set the summary for display even if save failed
                            book.setSummary(summary + " (⚠️ Lưu database thất bại)");
                        }
                        
                    } catch (Exception ex) {
                        String errorMsg = "❌ Không thể tạo tóm tắt AI: " + ex.getMessage();
                        System.err.println("❌ AI Summary Error: " + errorMsg);
                        ex.printStackTrace(); // Print full stack trace for debugging
                        book.setSummary(errorMsg);
                    }
                }
            } else {
                System.out.println("✅ Summary already exists: " + currentSummary.substring(0, Math.min(100, currentSummary.length())) + "...");
            }
            
            // Set attributes for JSP
            request.setAttribute("book", book);
            request.setAttribute("ebook", book); // for compatibility
            request.setAttribute("bookContent", bookContent);
            request.setAttribute("readingMode", "full_book");
            
            // Forward to read page
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

    /**
     * Handle chapter reading (original BookReadServlet logic)
     */
    private void handleChapterReading(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get bookId (support both parameter names)
            String bookIdParam = request.getParameter("bookId");
            if (bookIdParam == null) {
                bookIdParam = request.getParameter("id");
            }
            
            int bookId = EbookValidation.validateId(bookIdParam);
            
            // Get chapter parameter
            String chapParam = request.getParameter("chapterId");
            if (chapParam == null) {
                chapParam = request.getParameter("chapter");
            }
            
            int chapterIndex;
            if (chapParam == null) {
                chapterIndex = 1;
            } else {
                try {
                    double val = Double.parseDouble(chapParam);
                    if (val < 1) throw new NumberFormatException();
                    chapterIndex = (int) val;
                } catch (NumberFormatException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid chapter number");
                    return;
                }
            }

            double chapterNumber = chapterIndex;

            Ebook ebook = ebookService.getEbookById(bookId);
            Chapter chapter = chapterService.getChapterByBookAndIndex(bookId, chapterNumber);
            List<Chapter> chapters = chapterService.getChaptersByBookId(bookId);
            List<Volume> volumes = volumeService.getVolumesByEbook(bookId);

            // Find previous and next chapters
            Integer prevNum = null, nextNum = null;
            for (Chapter ch : chapters) {
                int num = (int) ch.getNumber();
                if (num < chapterIndex) {
                    if (prevNum == null || num > prevNum) prevNum = num;
                } else if (num > chapterIndex) {
                    if (nextNum == null || num < nextNum) nextNum = num;
                }
            }

            if (ebook == null || chapter == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy chương hoặc sách");
                return;
            }

            // Check chapter access rights
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            boolean hasAccess = false;
            boolean needUnlock = false;
            
            if ("premium".equals(chapter.getAccessLevel())) {
                if (user == null) {
                    response.sendRedirect(request.getContextPath() + "/user/login.jsp?error=login_required");
                    return;
                }
                
                Boolean isPremiumUser = (Boolean) session.getAttribute("isPremium");
                if (isPremiumUser != null && isPremiumUser) {
                    hasAccess = true;
                } else {
                    if (coinService.isChapterAccessible(user.getId(), chapter.getId())) {
                        hasAccess = true;
                    } else {
                        needUnlock = true;
                        hasAccess = false;
                    }
                }
            } else {
                hasAccess = true;
            }

            // Read chapter content if user has access
            if (hasAccess) {
                String content = readChapterContent(chapter.getContentUrl());
                chapter.setContent(content);
            }

            // Handle success/error messages
            handleMessages(request, user);

            // Get chapter comments
            loadChapterComments(request, bookId, chapter);

            // Set attributes for JSP
            request.setAttribute("ebook", ebook);
            request.setAttribute("chapter", chapter);
            request.setAttribute("chapters", chapters);
            request.setAttribute("volumes", volumes);
            request.setAttribute("currentChapter", chapterIndex);
            request.setAttribute("hasAccess", hasAccess);
            request.setAttribute("needUnlock", needUnlock);
            request.setAttribute("prevChapter", prevNum);
            request.setAttribute("nextChapter", nextNum);
            request.setAttribute("readingMode", "chapter");
            
            // Add coin information for logged in users
            if (user != null) {
                request.setAttribute("userCoins", coinService.getUserCoins(user.getId()));
                request.setAttribute("userUnlockedChapters", coinService.getUserUnlockedChapters(user.getId()));
                request.setAttribute("user", user);
            }
            request.setAttribute("unlockCost", CoinService.getUnlockChapterCost());

            request.getRequestDispatcher("/book/read.jsp").forward(request, response);
            
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void handleUnlockAction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp?error=login_required");
            return;
        }
        
        try {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            double chapterNum = Double.parseDouble(request.getParameter("chapterNum"));
            int chapterId = Integer.parseInt(request.getParameter("chapterId"));
            
            coinService.unlockChapter(user.getId(), chapterId, user);
            
            response.sendRedirect(request.getContextPath() + "/book/read?bookId=" + bookId + 
                "&chapterId=" + (int)chapterNum + "&success=unlocked");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/book/read?bookId=" + 
                request.getParameter("bookId") + "&chapterId=" + request.getParameter("chapterNum") + 
                "&error=invalid_params");
        } catch (Exception e) {
            String errorType = "system_error";
            if (e instanceof IllegalStateException) errorType = "already_unlocked";
            else if (e instanceof IllegalArgumentException) errorType = "insufficient_coins";
            else if (e instanceof SecurityException) errorType = "unauthorized";
            
            response.sendRedirect(request.getContextPath() + "/book/read?bookId=" + 
                request.getParameter("bookId") + "&chapterId=" + request.getParameter("chapterNum") + 
                "&error=" + errorType);
        }
    }

    private void handleMessages(HttpServletRequest request, User user) throws SQLException {
        String successMsg = request.getParameter("success");
        String errorMsg = request.getParameter("error");
        
        if ("unlocked".equals(successMsg)) {
            request.setAttribute("successMessage", "Mở khóa chapter thành công! Số coin còn lại: " + 
                (user != null ? coinService.getUserCoins(user.getId()) : 0));
        } else if ("premium_upgraded".equals(successMsg)) {
            request.setAttribute("successMessage", "Chúc mừng! Bạn đã nâng cấp thành Premium User thành công. " +
                "Giờ bạn có thể xem tất cả chapter premium miễn phí!");
        } else if (errorMsg != null) {
            switch (errorMsg) {
                case "insufficient_coins":
                    int currentCoins = user != null ? coinService.getUserCoins(user.getId()) : 0;
                    int needed = CoinService.getUnlockChapterCost() - currentCoins;
                    request.setAttribute("errorMessage", "Không đủ coin để mở khóa chapter! Bạn cần thêm " + 
                        needed + " coins nữa (hiện có " + currentCoins + "/" + CoinService.getUnlockChapterCost() + ").");
                    break;
                case "already_unlocked":
                    request.setAttribute("errorMessage", "Chapter này đã được mở khóa!");
                    break;
                case "unauthorized":
                    request.setAttribute("errorMessage", "Bạn không có quyền thực hiện thao tác này!");
                    break;
                case "system_error":
                    request.setAttribute("errorMessage", "Có lỗi hệ thống xảy ra. Vui lòng thử lại!");
                    break;
                default:
                    request.setAttribute("errorMessage", "Có lỗi xảy ra!");
            }
        }
    }

    private void loadChapterComments(HttpServletRequest request, int bookId, Chapter chapter) throws SQLException {
        List<Comment> chapterComments = commentService.getCommentsByChapter(bookId, chapter.getId());
        java.util.Set<Integer> userIds = new java.util.HashSet<>();
        for (Comment c : chapterComments) userIds.add(c.getUserID());
        
        java.util.Map<Integer, String> userMap = new java.util.HashMap<>();
        com.mycompany.ebookwebsite.dao.UserDAO userDAO = new com.mycompany.ebookwebsite.dao.UserDAO();
        for (Integer uid : userIds) {
            User commentUser = userDAO.findById(uid);
            userMap.put(uid, commentUser != null ? commentUser.getUsername() : "Unknown");
        }
        request.setAttribute("userMap", userMap);

        Map<Integer, Integer> likeMap = new HashMap<>();
        Map<Integer, Integer> dislikeMap = new HashMap<>();
        for (Comment c : chapterComments) {
            likeMap.put(c.getId(), voteService.getLikeCount(c.getId()));
            dislikeMap.put(c.getId(), voteService.getDislikeCount(c.getId()));
        }
        request.setAttribute("likeMap", likeMap);
        request.setAttribute("dislikeMap", dislikeMap);
        request.setAttribute("chapterComments", chapterComments);
    }



    /**
     * Read full book content (simplified approach using EbookAI)
     */
    private String readBookContent(Ebook book) {
        String uploadsPath = getUploadsPath();
        String bookTitle = book.getTitle();
        
        try {
            // Get EbookAI data
            EbookWithAI ebookWithAI = ebookWithAIService.getEbookWithAI(book.getId());
            
            if (ebookWithAI != null && ebookWithAI.getAiData() != null) {
                String fileName = ebookWithAI.getAiData().getFileName();
                String originalFileName = ebookWithAI.getAiData().getOriginalFileName();
                
                // Try file_name first
                if (fileName != null && !fileName.trim().isEmpty()) {
                    Path filePath = Paths.get(uploadsPath, fileName);
                    if (Files.exists(filePath)) {
                        System.out.println("✅ Đọc file từ file_name: " + fileName);
                        return readFileContent(filePath.toString(), getFileExtension(fileName));
                    } else {
                        System.out.println("⚠️ File không tồn tại: " + filePath);
                        System.out.println("🔍 Đang tìm file với encoding khác...");
                        
                        // 🔧 Try to find file with correct encoding
                        String correctFileName = findFileWithCorrectEncoding(uploadsPath, fileName, bookTitle);
                        if (correctFileName != null) {
                            System.out.println("✅ Tìm thấy file với encoding đúng: " + correctFileName);
                            Path correctFilePath = Paths.get(uploadsPath, correctFileName);
                            return readFileContent(correctFilePath.toString(), getFileExtension(correctFileName));
                        }
                    }
                }
                
                // Try original_file_name as fallback
                if (originalFileName != null && !originalFileName.trim().isEmpty()) {
                    Path filePath = Paths.get(uploadsPath, originalFileName);
                    if (Files.exists(filePath)) {
                        System.out.println("✅ Đọc file từ original_file_name: " + originalFileName);
                        return readFileContent(filePath.toString(), getFileExtension(originalFileName));
                    } else {
                        System.out.println("⚠️ Original file không tồn tại: " + filePath);
                        System.out.println("🔍 Đang tìm file với encoding khác...");
                        
                        // 🔧 Try to find file with correct encoding
                        String correctFileName = findFileWithCorrectEncoding(uploadsPath, originalFileName, bookTitle);
                        if (correctFileName != null) {
                            System.out.println("✅ Tìm thấy file với encoding đúng: " + correctFileName);
                            Path correctFilePath = Paths.get(uploadsPath, correctFileName);
                            return readFileContent(correctFilePath.toString(), getFileExtension(correctFileName));
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("⚠️ Lỗi lấy thông tin EbookAI: " + e.getMessage());
        }
        
        // Generate debug info if no file found
        return generateDebugInfo(book, uploadsPath);
    }
    
    /**
     * Helper method to read file content with proper error handling
     */
    private String readFileContent(String filePath, String extension) {
        try {
            String content = com.mycompany.ebookwebsite.utils.Utils.readAnyTextFile(filePath, extension);
            
            if (content != null && !content.trim().isEmpty()) {
                return content;
            } else {
                return "📝 File tồn tại nhưng không có nội dung: " + filePath;
            }
        } catch (Exception e) {
            System.err.println("⚠️ Lỗi đọc file: " + e.getMessage());
            return "💥 Lỗi đọc file: " + e.getMessage();
        }
    }

    private String generateDebugInfo(Ebook book, String uploadsPath) {
        StringBuilder debugInfo = new StringBuilder();
        debugInfo.append("❌ Không tìm thấy file nội dung cho sách.\n\n");
        debugInfo.append("📋 Thông tin debug:\n");
        debugInfo.append("- ID sách: ").append(book.getId()).append("\n");
        debugInfo.append("- Tên sách: ").append(book.getTitle()).append("\n");
        
        try {
            EbookWithAI ebookWithAI = ebookWithAIService.getEbookWithAI(book.getId());
            if (ebookWithAI != null && ebookWithAI.getAiData() != null) {
                String fileName = ebookWithAI.getAiData().getFileName();
                String originalFileName = ebookWithAI.getAiData().getOriginalFileName();
                
                debugInfo.append("- file_name từ EbookAI: ").append(fileName != null ? fileName : "NULL").append("\n");
                debugInfo.append("- original_file_name từ EbookAI: ").append(originalFileName != null ? originalFileName : "NULL").append("\n");
            } else {
                debugInfo.append("- EbookAI record: KHÔNG TỒN TẠI\n");
            }
        } catch (Exception e) {
            debugInfo.append("- Lỗi truy vấn EbookAI: ").append(e.getMessage()).append("\n");
        }
        
        debugInfo.append("- Thư mục uploads: ").append(uploadsPath).append("\n\n");
        
        File dir = new File(uploadsPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                debugInfo.append("📁 File có sẵn trong uploads:\n");
                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        debugInfo.append("  📄 ").append(fileName);
                        
                        // Highlight if this could be the target file based on book ID
                        if (fileName.startsWith("book_" + book.getId() + "_")) {
                            debugInfo.append(" ⭐ (KHỚP BOOK ID)");
                        }
                        
                        debugInfo.append("\n");
                    }
                }
            } else {
                debugInfo.append("📂 Thư mục uploads trống\n");
            }
        } else {
            debugInfo.append("❌ Thư mục uploads không tồn tại\n");
        }
        
        debugInfo.append("\n💡 Giải pháp:\n");
        debugInfo.append("1. Kiểm tra bảng EbookAI có record cho book ID ").append(book.getId()).append("\n");
        debugInfo.append("2. Đảm bảo file_name hoặc original_file_name được điền đúng\n");
        debugInfo.append("3. Kiểm tra file thực tế tồn tại trong thư mục uploads\n");
        
        return debugInfo.toString();
    }

    private String getUploadsPath() {
        String[] possiblePaths = {
            // 🎯 PRIORITY: Project uploads directory (user confirmed location)
            "D:\\EbookWebsite\\uploads",
            System.getProperty("user.dir") + File.separator + UPLOAD_FOLDER,
            UPLOAD_FOLDER,
            getServletContext().getRealPath("/") + UPLOAD_FOLDER,
            System.getProperty("catalina.base") + File.separator + "webapps" + 
            File.separator + "EbookWebsite" + File.separator + UPLOAD_FOLDER,
            System.getProperty("catalina.base") + File.separator + "bin" + 
            File.separator + UPLOAD_FOLDER
        };
        
        System.out.println("🔍 Tìm kiếm uploads directory...");
        
        for (String path : possiblePaths) {
            File dir = new File(path);
            System.out.println("📁 Kiểm tra: " + path);
            
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                System.out.println("   ✅ Thư mục tồn tại với " + (files != null ? files.length : 0) + " files");
                
                if (files != null && files.length > 0) {
                    // Log some files for debugging
                    System.out.println("   📄 Sample files:");
                    for (int i = 0; i < Math.min(5, files.length); i++) {
                        System.out.println("     - " + files[i].getName());
                    }
                    
                    // Special check for our target file
                    boolean hasTargetFile = false;
                    for (File file : files) {
                        if (file.getName().equals("Nhà Thờ Đức Bà Paris.pdf")) {
                            hasTargetFile = true;
                            System.out.println("   🎯 FOUND TARGET FILE: " + file.getName() + " (" + file.length() + " bytes)");
                            break;
                        }
                    }
                    
                    System.out.println("✅ Sử dụng uploads path: " + path);
                    return path;
                }
            } else {
                System.out.println("   ❌ Thư mục không tồn tại hoặc không phải thư mục");
            }
        }
        
        String defaultPath = possiblePaths[0];
        new File(defaultPath).mkdirs();
        System.out.println("📁 Tạo uploads path mặc định: " + defaultPath);
        return defaultPath;
    }

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

    /**
     * Tìm file với encoding đúng khi file name trong database bị lỗi encoding
     */
    private String findFileWithCorrectEncoding(String uploadsPath, String corruptedFileName, String bookTitle) {
        File uploadsDir = new File(uploadsPath);
        if (!uploadsDir.exists() || !uploadsDir.isDirectory()) {
            return null;
        }
        
        File[] files = uploadsDir.listFiles();
        if (files == null) {
            return null;
        }
        
        System.out.println("🔍 Tìm file cho corrupted name: " + corruptedFileName);
        System.out.println("📚 Book title: " + bookTitle);
        
        // 1. Tìm file có tên gần giống với book title
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String fileNameWithoutExt = removeFileExtension(fileName);
                
                // So sánh với book title
                if (isSimilarIgnoreEncoding(fileNameWithoutExt, bookTitle)) {
                    System.out.println("🎯 Tìm thấy file match với book title: " + fileName);
                    return fileName;
                }
            }
        }
        
        // 2. Tìm file có structure tương tự với corrupted name
        String normalizedCorrupted = normalizeForEncoding(corruptedFileName);
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                String normalizedFile = normalizeForEncoding(fileName);
                
                if (isSimilarIgnoreEncoding(normalizedFile, normalizedCorrupted)) {
                    System.out.println("🎯 Tìm thấy file match với corrupted name: " + fileName);
                    return fileName;
                }
            }
        }
        
        System.out.println("❌ Không tìm thấy file phù hợp");
        return null;
    }
    
    /**
     * Loại bỏ file extension
     */
    private String removeFileExtension(String fileName) {
        if (fileName == null) return "";
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(0, lastDot) : fileName;
    }
    
    /**
     * Kiểm tra hai string có tương tự không (ignore encoding issues)
     */
    private boolean isSimilarIgnoreEncoding(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }
        
        String normalized1 = normalizeForEncoding(str1);
        String normalized2 = normalizeForEncoding(str2);
        
        // Tính độ tương tự
        return calculateStringSimilarity(normalized1, normalized2) > 0.6;
    }
    
    /**
     * Normalize string để so sánh (loại bỏ encoding issues)
     */
    private String normalizeForEncoding(String str) {
        if (str == null) return "";
        
        return str.toLowerCase()
                  .replaceAll("[^a-z0-9\\s]", "") // Chỉ giữ chữ, số và space
                  .replaceAll("\\s+", " ")       // Normalize space
                  .trim();
    }
    
    /**
     * Tính độ tương tự giữa hai string
     */
    private double calculateStringSimilarity(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return 0.0;
        }
        
        String[] words1 = str1.split("\\s+");
        String[] words2 = str2.split("\\s+");
        
        int matches = 0;
        for (String word1 : words1) {
            for (String word2 : words2) {
                if (word1.length() > 2 && word2.length() > 2 && 
                    (word1.contains(word2) || word2.contains(word1))) {
                    matches++;
                    break;
                }
            }
        }
        
        int maxWords = Math.max(words1.length, words2.length);
        return maxWords > 0 ? (double) matches / maxWords : 0.0;
    }

    /**
     * Read chapter content from file path
     */
    private String readChapterContent(String relativePath) throws IOException {
        String fullPath = getServletContext().getRealPath("/") + relativePath;
        return Files.readString(Paths.get(fullPath), StandardCharsets.UTF_8);
    }
}
