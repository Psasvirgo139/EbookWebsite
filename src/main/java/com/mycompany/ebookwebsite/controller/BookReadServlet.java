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
import com.mycompany.ebookwebsite.service.PremiumService;
import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.utils.EbookValidation;
import com.mycompany.ebookwebsite.utils.PathManager;
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
    private PremiumService premiumService;  // 👑 Thêm PremiumService

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
        premiumService = new PremiumService();  // 👑 Khởi tạo PremiumService
        
        // 🗂️ Log PathManager info for debugging
        System.out.println("📁 BookReadServlet initialized with uploads path: " + PathManager.getUploadsPath());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleChapterReading(request, response);
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
                
                // 👑 Sử dụng PremiumService thay vì session attribute
                boolean isPremiumUser = premiumService.isPremiumUser(user.getId());
                if (isPremiumUser) {
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
     * Read chapter content from file path
     */
    private String readChapterContent(String relativePath) throws IOException {
        String fullPath = getServletContext().getRealPath("/") + relativePath;
        return Files.readString(Paths.get(fullPath), StandardCharsets.UTF_8);
    }
}
