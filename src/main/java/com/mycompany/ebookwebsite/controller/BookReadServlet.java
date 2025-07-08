package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Chapter;
import com.mycompany.ebookwebsite.model.Volume;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.ChapterService;
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

    @Override
    public void init() {
        ebookService = new EbookService();
        chapterService = new ChapterService();
        volumeService = new VolumeService();
        commentService = new CommentService();
        voteService = new CommentVoteService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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

            boolean hasAccess = checkAccess(request, chapter);

            if (hasAccess) {
                // ✅ Đọc nội dung file và set vào chapter
                String content = readChapterContent(chapter.getContentUrl());
                chapter.setContent(content);
            }

            // Lấy comment của chapter này
            List<Comment> chapterComments = commentService.getCommentsByChapter(bookId, chapter.getId());
            java.util.Set<Integer> userIds = new java.util.HashSet<>();
            for (Comment c : chapterComments) userIds.add(c.getUserID());
            java.util.Map<Integer, String> userMap = new java.util.HashMap<>();
            com.mycompany.ebookwebsite.dao.UserDAO userDAO = new com.mycompany.ebookwebsite.dao.UserDAO();
            for (Integer uid : userIds) {
                com.mycompany.ebookwebsite.model.User user = userDAO.findById(uid);
                userMap.put(uid, user != null ? user.getUsername() : "Unknown");
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

            request.setAttribute("ebook", ebook);
            request.setAttribute("chapter", chapter);
            request.setAttribute("chapters", chapters);
            request.setAttribute("currentChapter", chapterIndex);
            request.setAttribute("volumes", volumes);
            request.setAttribute("hasAccess", hasAccess);
            request.setAttribute("prevChapter", prevNum);
            request.setAttribute("nextChapter", nextNum);
            request.setAttribute("chapterComments", chapterComments);

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
