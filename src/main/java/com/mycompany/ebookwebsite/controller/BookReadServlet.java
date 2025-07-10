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
    
    // Upload folder configuration from ReadBookServlet
    private static final String UPLOAD_FOLDER = "uploads";

    @Override
    public void init() {
        ebookService = new EbookService();
        chapterService = new ChapterService();
        coinService = new CoinService();
        volumeService = new VolumeService();
        commentService = new CommentService();
        voteService = new CommentVoteService();
        ebookDAO = new EbookDAO();
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
            Ebook book = ebookDAO.selectEbook(bookId);
            
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
            ebookDAO.incrementViewCount(bookId);
            
            // Read book content
            String bookContent = readBookContent(book);
            
            // AI summary integration
            if (book.getSummary() == null || book.getSummary().trim().isEmpty()) {
                try {
                    OpenAIContentSummaryService summaryService = new OpenAIContentSummaryService();
                    String summary = summaryService.summarize(bookContent);
                    book.setSummary(summary);
                } catch (Exception ex) {
                    book.setSummary("Không thể tạo tóm tắt AI: " + ex.getMessage());
                }
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
     * Read full book content (from ReadBookServlet)
     */
    private String readBookContent(Ebook book) {
        String uploadsPath = getUploadsPath();
        String bookTitle = book.getTitle();
        
        // Primary method: use fileName from database
        String fileName = book.getFileName();
        
        if (fileName != null && !fileName.trim().isEmpty()) {
            Path filePath = Paths.get(uploadsPath, fileName);
            
            if (Files.exists(filePath)) {
                System.out.println("✅ Load file từ DB mapping: " + fileName);
                
                try {
                    String content = com.mycompany.ebookwebsite.utils.Utils.readAnyTextFile(filePath.toString(), getFileExtension(fileName));
                    
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
            }
        } else {
            System.out.println("⚠️ Sách chưa có fileName trong database: " + bookTitle);
        }
        
        // Fallback: fuzzy search
        System.out.println("🔍 Fallback to fuzzy search cho sách: " + bookTitle);
        return performFuzzySearch(book, uploadsPath, bookTitle);
    }

    private String performFuzzySearch(Ebook book, String uploadsPath, String bookTitle) {
        // Implement fuzzy search logic from ReadBookServlet
        String[] possibleFileNames = {
            bookTitle + ".txt",
            bookTitle.replace("–", "-") + ".txt",
            bookTitle.replace("—", "-") + ".txt",
            bookTitle.replaceAll("[^a-zA-Z0-9\\s]", "-") + ".txt",
            bookTitle.replaceAll("\\s+", "_") + ".txt",
            bookTitle.toLowerCase().replaceAll("[–—]", "-").replaceAll("[^a-z0-9\\s\\-]", "").replaceAll("\\s+", "") + ".txt"
        };
        
        for (String possibleFileName : possibleFileNames) {
            Path filePath = Paths.get(uploadsPath, possibleFileName);
            if (Files.exists(filePath)) {
                System.out.println("🔍 Tìm thấy file bằng fuzzy search: " + possibleFileName);
                
                try {
                    book.setFileName(possibleFileName);
                    ebookDAO.updateEbook(book);
                    System.out.println("💾 Đã cập nhật fileName vào database: " + possibleFileName);
                } catch (Exception e) {
                    System.out.println("⚠️ Không thể cập nhật fileName vào database: " + e.getMessage());
                }
                
                try {
                    return com.mycompany.ebookwebsite.utils.Utils.readAnyTextFile(filePath.toString(), getFileExtension(possibleFileName));
                } catch (Exception e) {
                    System.out.println("⚠️ Lỗi đọc file: " + e.getMessage());
                    return "💥 Lỗi đọc file: " + e.getMessage();
                }
            }
        }
        
        return generateDebugInfo(book, uploadsPath, possibleFileNames);
    }

    private String generateDebugInfo(Ebook book, String uploadsPath, String[] possibleFileNames) {
        StringBuilder debugInfo = new StringBuilder();
        debugInfo.append("❌ Không tìm thấy file nội dung cho sách.\n\n");
        debugInfo.append("📋 Thông tin debug:\n");
        debugInfo.append("- ID sách: ").append(book.getId()).append("\n");
        debugInfo.append("- Tên sách: ").append(book.getTitle()).append("\n");
        debugInfo.append("- FileName từ DB: ").append(book.getFileName() != null ? book.getFileName() : "NULL").append("\n");
        debugInfo.append("- Thư mục uploads: ").append(uploadsPath).append("\n\n");
        
        debugInfo.append("📝 Các tên file đã thử:\n");
        for (String possibleFileName : possibleFileNames) {
            debugInfo.append("  • ").append(possibleFileName).append("\n");
        }
        
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
        
        return debugInfo.toString();
    }

    private String getUploadsPath() {
        String[] possiblePaths = {
            System.getProperty("user.dir") + File.separator + UPLOAD_FOLDER,
            UPLOAD_FOLDER,
            getServletContext().getRealPath("/") + UPLOAD_FOLDER,
            System.getProperty("catalina.base") + File.separator + "webapps" + 
            File.separator + "EbookWebsite" + File.separator + UPLOAD_FOLDER,
            System.getProperty("catalina.base") + File.separator + "bin" + 
            File.separator + UPLOAD_FOLDER
        };
        
        for (String path : possiblePaths) {
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null && files.length > 0) {
                    System.out.println("✅ Sử dụng uploads path: " + path);
                    return path;
                }
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
     * Read chapter content from file path
     */
    private String readChapterContent(String relativePath) throws IOException {
        String fullPath = getServletContext().getRealPath("/") + relativePath;
        return Files.readString(Paths.get(fullPath), StandardCharsets.UTF_8);
    }
}
