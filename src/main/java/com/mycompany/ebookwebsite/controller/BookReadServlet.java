package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Chapter;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.ChapterService;
import com.mycompany.ebookwebsite.utils.ChapterValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/book/read")
public class BookReadServlet extends HttpServlet {

    private final EbookService ebookService = new EbookService();
    private final ChapterService chapterService = new ChapterService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Validate parameters
            int bookId = ChapterValidation.validateId(request.getParameter("bookId"));
            String chapterParam = request.getParameter("chapter");

            // Get book information
            Ebook ebook = ebookService.getEbookById(bookId);
            if (ebook == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                return;
            }

            // Get all chapters for this book
            List<Chapter> chapters = chapterService.getChaptersByBookId(bookId);
            
            // Determine current chapter
            Chapter currentChapter = null;
            int currentChapterIndex = 1;
            
            if (chapterParam != null) {
                try {
                    currentChapterIndex = Integer.parseInt(chapterParam);
                    if (currentChapterIndex > 0 && currentChapterIndex <= chapters.size()) {
                        currentChapter = chapters.get(currentChapterIndex - 1);
                    }
                } catch (NumberFormatException e) {
                    // Use default chapter
                }
            }
            
            // If no specific chapter or invalid chapter, use first chapter
            if (currentChapter == null && !chapters.isEmpty()) {
                currentChapter = chapters.get(0);
                currentChapterIndex = 1;
            }

            // Increment view count
            ebookService.incrementViewCount(bookId);

            // Set attributes for JSP
            request.setAttribute("ebook", ebook);
            request.setAttribute("chapters", chapters);
            request.setAttribute("currentChapter", currentChapter);
            request.setAttribute("currentChapterIndex", currentChapterIndex);

            request.getRequestDispatcher("/book/read.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle AJAX requests for navigation
        String action = request.getParameter("action");
        
        try {
            if ("next".equals(action)) {
                handleNextChapter(request, response);
            } else if ("prev".equals(action)) {
                handlePrevChapter(request, response);
            } else if ("goto".equals(action)) {
                handleGoToChapter(request, response);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

    private void handleNextChapter(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int currentChapterIndex = Integer.parseInt(request.getParameter("chapterIndex"));
        
        List<Chapter> chapters = chapterService.getChaptersByBookId(bookId);
        
        if (currentChapterIndex < chapters.size()) {
            Chapter nextChapter = chapters.get(currentChapterIndex);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"chapterIndex\": " + (currentChapterIndex + 1) + 
                                     ", \"chapterId\": " + nextChapter.getId() + "}");
        } else {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"No next chapter\"}");
        }
    }

    private void handlePrevChapter(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int currentChapterIndex = Integer.parseInt(request.getParameter("chapterIndex"));
        
        List<Chapter> chapters = chapterService.getChaptersByBookId(bookId);
        
        if (currentChapterIndex > 1) {
            Chapter prevChapter = chapters.get(currentChapterIndex - 2);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"chapterIndex\": " + (currentChapterIndex - 1) + 
                                     ", \"chapterId\": " + prevChapter.getId() + "}");
        } else {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"No previous chapter\"}");
        }
    }

    private void handleGoToChapter(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        int chapterIndex = Integer.parseInt(request.getParameter("chapterIndex"));
        
        List<Chapter> chapters = chapterService.getChaptersByBookId(bookId);
        
        if (chapterIndex > 0 && chapterIndex <= chapters.size()) {
            Chapter chapter = chapters.get(chapterIndex - 1);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"chapterIndex\": " + chapterIndex + 
                                     ", \"chapterId\": " + chapter.getId() + "}");
        } else {
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": false, \"message\": \"Chapter not found\"}");
        }
    }
}
