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
import com.mycompany.ebookwebsite.utils.EbookValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/book/read")
public class BookReadServlet extends HttpServlet {
    private EbookService ebookService;
    private ChapterService chapterService;
    private VolumeService volumeService;
    private CommentService commentService;
    private CommentVoteService voteService;
    private CoinService coinService;
    
    @Override
    public void init() {
        ebookService = new EbookService();
        chapterService = new ChapterService();
        volumeService = new VolumeService();
        commentService = new CommentService();
        coinService = new CoinService();
        voteService = new CommentVoteService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("unlock".equals(action)) {
            handleUnlockAction(request, response);
        } else {
            handleRequest(request, response);
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
            
            // Thực hiện unlock trực tiếp
            coinService.unlockChapter(user.getId(), chapterId, user);
            
            // Redirect về trang đọc với thông báo thành công
            response.sendRedirect(request.getContextPath() + "/book/read?id=" + bookId + 
                "&chapter=" + chapterNum + "&success=unlocked");
            
        } catch (NumberFormatException e) {
            // Lỗi tham số
            response.sendRedirect(request.getContextPath() + "/book/read?id=" + 
                request.getParameter("bookId") + "&chapter=" + request.getParameter("chapterNum") + 
                "&error=invalid_params");
        } catch (IllegalStateException e) {
            // Chapter đã unlock
            response.sendRedirect(request.getContextPath() + "/book/read?id=" + 
                request.getParameter("bookId") + "&chapter=" + request.getParameter("chapterNum") + 
                "&error=already_unlocked");
        } catch (IllegalArgumentException e) {
            // Không đủ coin
            response.sendRedirect(request.getContextPath() + "/book/read?id=" + 
                request.getParameter("bookId") + "&chapter=" + request.getParameter("chapterNum") + 
                "&error=insufficient_coins");
        } catch (SecurityException e) {
            // Không có quyền
            response.sendRedirect(request.getContextPath() + "/book/read?id=" + 
                request.getParameter("bookId") + "&chapter=" + request.getParameter("chapterNum") + 
                "&error=unauthorized");
        } catch (SQLException e) {
            // Lỗi hệ thống
            response.sendRedirect(request.getContextPath() + "/book/read?id=" + 
                request.getParameter("bookId") + "&chapter=" + request.getParameter("chapterNum") + 
                "&error=system_error");
        }
    }



    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int bookId = EbookValidation.validateId(request.getParameter("bookId"));
            String chapParam = request.getParameter("chapterId");
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

            double chapterNumber = chapterIndex; // convert to match DB decimal format

            Ebook ebook = ebookService.getEbookById(bookId);
            Chapter chapter = chapterService.getChapterByBookAndIndex(bookId, chapterNumber);
            List<Chapter> chapters = chapterService.getChaptersByBookId(bookId);
            List<Volume> volumes = volumeService.getVolumesByEbook(bookId);

            // Tìm chương trước và sau
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

            // Kiểm tra quyền truy cập chapter
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            boolean hasAccess = false;
            boolean needUnlock = false;
            
            // Kiểm tra chapter có phải premium không
            if ("premium".equals(chapter.getAccessLevel())) {
                // Chapter là premium
                if (user == null) {
                    // User chưa đăng nhập -> yêu cầu đăng nhập
                    response.sendRedirect(request.getContextPath() + "/user/login.jsp?error=login_required");
                    return;
                }
                
                // Kiểm tra user có phải premium user không
                Boolean isPremiumUser = (Boolean) session.getAttribute("isPremium");
                if (isPremiumUser != null && isPremiumUser) {
                    // User là premium -> có quyền xem
                    hasAccess = true;
                } else {
                    // User là normal -> kiểm tra đã unlock chapter chưa
                    if (coinService.isChapterAccessible(user.getId(), chapter.getId())) {
                        // Đã unlock -> có quyền xem
                        hasAccess = true;
                    } else {
                        // Chưa unlock -> hiển thị trang yêu cầu unlock
                        needUnlock = true;
                        hasAccess = false;
                    }
                }
            } else {
                // Chapter là public -> mọi user đều có quyền xem
                hasAccess = true;
            }

            // Đọc nội dung chapter nếu user có quyền truy cập
            if (hasAccess) {
                String content = readChapterContent(chapter.getContentUrl());
                chapter.setContent(content);
            }

            // Xử lý thông báo từ URL parameters
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

            // Lấy comment của chapter này
            List<Comment> chapterComments = commentService.getCommentsByChapter(bookId, chapter.getId());
            java.util.Set<Integer> userIds = new java.util.HashSet<>();
            for (Comment c : chapterComments) userIds.add(c.getUserID());
            java.util.Map<Integer, String> userMap = new java.util.HashMap<>();
            com.mycompany.ebookwebsite.dao.UserDAO userDAO = new com.mycompany.ebookwebsite.dao.UserDAO();
            for (Integer uid : userIds) {
                com.mycompany.ebookwebsite.model.User user1 = userDAO.findById(uid);
                userMap.put(uid, user1 != null ? user1.getUsername() : "Unknown");
            }
            request.setAttribute("userMap", userMap);

            CommentVoteService voteService = new CommentVoteService();
            Map<Integer, Integer> likeMap = new HashMap<>();
            Map<Integer, Integer> dislikeMap = new HashMap<>();
            for (Comment c : chapterComments) {
                likeMap.put(c.getId(), voteService.getLikeCount(c.getId()));
                dislikeMap.put(c.getId(), voteService.getDislikeCount(c.getId()));
            }
            request.setAttribute("likeMap", likeMap);
            request.setAttribute("dislikeMap", dislikeMap);

            // Set attributes cho JSP
            request.setAttribute("ebook", ebook);
            request.setAttribute("chapter", chapter);
            request.setAttribute("chapters", chapters);
            request.setAttribute("volumes", volumes);
            request.setAttribute("currentChapter", chapterIndex);
            request.setAttribute("hasAccess", hasAccess);
            request.setAttribute("needUnlock", needUnlock);
            request.setAttribute("prevChapter", prevNum);
            request.setAttribute("nextChapter", nextNum);
            
            // Thêm thông tin coin cho user nếu đã login
            if (user != null) {
                request.setAttribute("userCoins", coinService.getUserCoins(user.getId()));
                request.setAttribute("userUnlockedChapters", coinService.getUserUnlockedChapters(user.getId()));
                request.setAttribute("user", user);
            }
            request.setAttribute("unlockCost", CoinService.getUnlockChapterCost());
            request.setAttribute("chapterComments", chapterComments);

            request.getRequestDispatcher("/book/read.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    // Hàm đọc nội dung file .txt từ đường dẫn
    private String readChapterContent(String relativePath) throws IOException {
        String fullPath = getServletContext().getRealPath("/") + relativePath;
        return Files.readString(Paths.get(fullPath), StandardCharsets.UTF_8);
    }
}
