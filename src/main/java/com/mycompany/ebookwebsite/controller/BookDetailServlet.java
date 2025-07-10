package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.model.Volume;
import com.mycompany.ebookwebsite.model.Chapter;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.EbookWithAIService;
import com.mycompany.ebookwebsite.service.EbookWithAIService.EbookWithAI;
import com.mycompany.ebookwebsite.service.CommentService;
import com.mycompany.ebookwebsite.service.VolumeService;
import com.mycompany.ebookwebsite.service.ChapterService;
import com.mycompany.ebookwebsite.service.CommentVoteService;
import com.mycompany.ebookwebsite.utils.EbookValidation;
import com.mycompany.ebookwebsite.dao.EbookDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.mycompany.ebookwebsite.dao.AuthorDAO;
import com.mycompany.ebookwebsite.dao.EbookAuthorDAO;
import com.mycompany.ebookwebsite.dao.EbookTagDAO;
import com.mycompany.ebookwebsite.dao.TagDAO;
import com.mycompany.ebookwebsite.model.Author;
import com.mycompany.ebookwebsite.model.Tag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@WebServlet("/book/detail")
public class BookDetailServlet extends HttpServlet {

    private final EbookService ebookService = new EbookService();
    private final EbookWithAIService ebookWithAIService = new EbookWithAIService();
    private final CommentService commentService = new CommentService();
    private final VolumeService volumeService = new VolumeService();
    private final ChapterService chapterService = new ChapterService();
    private final EbookAuthorDAO ebookAuthorDAO = new EbookAuthorDAO();
    private final AuthorDAO authorDAO = new AuthorDAO();
    private final EbookTagDAO ebookTagDAO = new EbookTagDAO();
    private final TagDAO tagDAO = new TagDAO();
    private final EbookDAO ebookDAO = new EbookDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        
        try {
            if ("editSummary".equals(action)) {
                showEditSummaryForm(request, response);
            } else if ("delete".equals(action)) {
                showDeleteConfirm(request, response);
            } else {
                showBookDetail(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            if ("editSummary".equals(action)) {
                updateSummary(request, response);
            } else if ("delete".equals(action)) {
                deleteBook(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void showBookDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

            // validate và xử lý
            int id = EbookValidation.validateId(request.getParameter("id"));
        EbookWithAI ebook = ebookWithAIService.getEbookWithAI(id);
            if (ebook == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                return;
            }

        ebookWithAIService.incrementViewCount(id);
            
            // Get book comments
            List<Comment> bookComments = commentService.getCommentsByBook(id);
            if (bookComments == null) {
                bookComments = new java.util.ArrayList<>();
            }
            
            // Get top chapter comments
            List<Comment> aggregatedComments;
            try {
                aggregatedComments = commentService.getTopChapterComments(id, 10);
            } catch (SQLException e) {
                e.printStackTrace();
                aggregatedComments = new java.util.ArrayList<>();
            }

            // Lấy userId từ cả 2 list
            java.util.Set<Integer> userIds = new java.util.HashSet<>();
            for (Comment c : bookComments) userIds.add(c.getUserID());
            for (Comment c : aggregatedComments) userIds.add(c.getUserID());
            java.util.Map<Integer, String> userMap = new java.util.HashMap<>();
            com.mycompany.ebookwebsite.dao.UserDAO userDAO = new com.mycompany.ebookwebsite.dao.UserDAO();
            for (Integer uid : userIds) {
                com.mycompany.ebookwebsite.model.User user = userDAO.findById(uid);
                userMap.put(uid, user != null ? user.getUsername() : "Unknown");
            }
            request.setAttribute("userMap", userMap);

            // Create vote maps
            CommentVoteService voteService = new CommentVoteService();
            Map<Integer, Integer> likeMap = new HashMap<>();
            Map<Integer, Integer> dislikeMap = new HashMap<>();
            
            // Process book comments
            for (Comment c : bookComments) {
                likeMap.put(c.getId(), voteService.getLikeCount(c.getId()));
                dislikeMap.put(c.getId(), voteService.getDislikeCount(c.getId()));
            }
            
            // Process aggregated comments
            for (Comment c : aggregatedComments) {
                likeMap.put(c.getId(), voteService.getLikeCount(c.getId()));
                dislikeMap.put(c.getId(), voteService.getDislikeCount(c.getId()));
            }
            
            request.setAttribute("likeMap", likeMap);
            request.setAttribute("dislikeMap", dislikeMap);

            // Authors
            List<com.mycompany.ebookwebsite.model.EbookAuthor> eaList = ebookAuthorDAO.getAuthorsByEbook(id);
            List<Author> authors = new java.util.ArrayList<>();
            for (com.mycompany.ebookwebsite.model.EbookAuthor ea : eaList) {
                Author a = authorDAO.selectAuthor(ea.getAuthorID());
                if (a != null) authors.add(a);
            }

            // Tags
            List<com.mycompany.ebookwebsite.model.EbookTag> etList = ebookTagDAO.getTagsByEbook(id);
            List<Tag> tags = new java.util.ArrayList<>();
            for (com.mycompany.ebookwebsite.model.EbookTag et : etList) {
                Tag t = tagDAO.getTagById(et.getTagId());
                if (t != null) tags.add(t);
            }

            // Lấy danh sách volumes & chapters
            List<Volume> volumes = volumeService.getVolumesByEbook(id);
            List<Chapter> chapters = chapterService.getChaptersByBookId(id);

            boolean isMultiVolume = volumes != null && volumes.size() > 1;

            request.setAttribute("ebook", ebook);
            // Convert LocalDateTime -> java.util.Date for JSTL fmt
            if (ebook.getCreatedAt() != null) {
                java.util.Date cDate = java.sql.Timestamp.valueOf(ebook.getCreatedAt());
                request.setAttribute("ebookCreatedDate", cDate);
            }
            request.setAttribute("bookComments", bookComments);
            request.setAttribute("aggregatedComments", aggregatedComments);
            request.setAttribute("volumes", volumes);
            request.setAttribute("chapters", chapters);
            request.setAttribute("isMultiVolume", isMultiVolume);
            request.setAttribute("authors", authors);
            request.setAttribute("tags", tags);
            request.getRequestDispatcher("/book/detail.jsp").forward(request, response);
    }

    private void showEditSummaryForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            EbookWithAI book = ebookWithAIService.getEbookWithAI(id);
            if (book == null) {
                request.setAttribute("error", "Không tìm thấy sách với ID: " + id);
                response.sendRedirect(request.getContextPath() + "/book-list");
                return;
            }
            request.setAttribute("book", book);
            request.getRequestDispatcher("/book/editSummary.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID sách không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/book-list");
        }
    }

    private void showDeleteConfirm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Ebook book = ebookDAO.selectEbook(id);
            if (book == null) {
                request.setAttribute("error", "Không tìm thấy sách với ID: " + id);
                response.sendRedirect(request.getContextPath() + "/book-list");
                return;
            }
            request.setAttribute("book", book);
            request.getRequestDispatcher("/book/bookDeleteConfirm.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID sách không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/book-list");
        }
    }

    private void updateSummary(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String summary = request.getParameter("summary");
            
            boolean updated = ebookWithAIService.updateSummary(id, summary);
            if (updated) {
                response.sendRedirect(request.getContextPath() + "/book/detail?id=" + id + "&success=summary_updated");
            } else {
                request.setAttribute("error", "Không thể cập nhật tóm tắt");
                showEditSummaryForm(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID sách không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/book-list");
        }
    }

    private void deleteBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            ebookDAO.deleteEbook(id);
            response.sendRedirect(request.getContextPath() + "/book-list?success=book_deleted");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID sách không hợp lệ");
            response.sendRedirect(request.getContextPath() + "/book-list");
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi khi xóa sách: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/book-list");
        }
    }
}
