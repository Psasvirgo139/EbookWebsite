package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Chapter;
import com.mycompany.ebookwebsite.model.Volume;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.ChapterService;
import com.mycompany.ebookwebsite.service.VolumeService;
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

@WebServlet("/book/read")
public class BookReadServlet extends HttpServlet {
    private EbookService ebookService;
    private ChapterService chapterService;
    private CoinService coinService;
    private VolumeService volumeService;

    @Override
    public void init() {
        ebookService = new EbookService();
        chapterService = new ChapterService();
        coinService = new CoinService();
        volumeService = new VolumeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int bookId = EbookValidation.validateId(request.getParameter("id"));
            double chapterIndex;
            if (request.getParameter("chapter") != null) {
                try {
                    chapterIndex = Double.parseDouble(request.getParameter("chapter"));
                    if (chapterIndex < 1) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid chapter number");
                    return;
                }
            } else {
                chapterIndex = 1;
            }

            Ebook ebook = ebookService.getEbookById(bookId);
            Chapter chapter = chapterService.getChapterByBookAndIndex(bookId, chapterIndex);
            List<Chapter> chapters = chapterService.getChaptersByBookId(bookId);
            List<Volume> volumes = volumeService.getVolumesByEbook(bookId);

            // Tìm chương trước và sau
            Double prevNum = null, nextNum = null;
            for (Chapter ch : chapters) {
                double num = ch.getNumber();
                if (num < chapter.getNumber()) {
                    if (prevNum == null || num > prevNum) prevNum = num;
                } else if (num > chapter.getNumber()) {
                    if (nextNum == null || num < nextNum) nextNum = num;
                }
            }

            if (ebook == null || chapter == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy chương hoặc sách");
                return;
            }

            // Kiểm tra quyền truy cập chapter premium
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            if ("premium".equals(chapter.getAccessLevel())) {
                if (user == null) {
                    response.sendRedirect(request.getContextPath() + "/user/login.jsp?error=login_required");
                    return;
                }
                
                // Kiểm tra user đã unlock chapter chưa
                if (!coinService.isChapterAccessible(user.getId(), chapter.getId())) {
                    // Chuyển đến trang yêu cầu unlock
                    request.setAttribute("ebook", ebook);
                    request.setAttribute("chapter", chapter);
                    request.setAttribute("chapters", chapters);
                    request.setAttribute("currentChapter", chapterIndex);
                    request.setAttribute("userCoins", coinService.getUserCoins(user.getId()));
                    request.setAttribute("unlockCost", CoinService.getUnlockChapterCost());
                    request.setAttribute("needUnlock", true);
                    
                    request.getRequestDispatcher("/book/read.jsp").forward(request, response);
                    return;
                }
            }

            // ✅ Đọc nội dung file và set vào chapter
            String content = readChapterContent(chapter.getContentUrl());
            chapter.setContent(content);

//             boolean hasAccess = checkAccess(request, chapter);

//             if (hasAccess) {
//                 // ✅ Đọc nội dung file và set vào chapter
//                 String content = readChapterContent(chapter.getContentUrl());
//                 chapter.setContent(content);
//             }

            request.setAttribute("ebook", ebook);
            request.setAttribute("chapter", chapter);
            request.setAttribute("chapters", chapters);
            request.setAttribute("currentChapter", chapterIndex);
            
            // Thêm thông tin coin cho user nếu đã login
            if (user != null) {
                request.setAttribute("userCoins", coinService.getUserCoins(user.getId()));
            }
            request.setAttribute("unlockCost", CoinService.getUnlockChapterCost());
            request.setAttribute("volumes", volumes);
            request.setAttribute("hasAccess", hasAccess);
            request.setAttribute("prevChapter", prevNum);
            request.setAttribute("nextChapter", nextNum);

            request.getRequestDispatcher("/book/read.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    // ✅ Hàm đọc nội dung file .txt từ đường dẫn
    private String readChapterContent(String relativePath) throws IOException {
        String fullPath = getServletContext().getRealPath("/") + relativePath;
        return Files.readString(Paths.get(fullPath), StandardCharsets.UTF_8);
    }

    // Kiểm tra quyền truy cập chapter
    private boolean checkAccess(HttpServletRequest request, Chapter chapter) {
        String level = chapter.getAccessLevel();
        if (level == null || "free".equalsIgnoreCase(level) || "public".equalsIgnoreCase(level)) {
            return true;
        }
        HttpSession session = request.getSession(false);
        if (session == null) return false;

        // Giả định session có flag premium hoặc purchasedChapters list
        Boolean isPremium = (Boolean) session.getAttribute("isPremium");
        if (isPremium != null && isPremium) return true;

        // Kiểm tra đã mua chapter chưa (để đơn giản, giả định list id trong session)
        java.util.Set<Integer> purchased = (java.util.Set<Integer>) session.getAttribute("purchasedChapters");
        if (purchased != null && purchased.contains(chapter.getId())) return true;

        return false;
    }
}
