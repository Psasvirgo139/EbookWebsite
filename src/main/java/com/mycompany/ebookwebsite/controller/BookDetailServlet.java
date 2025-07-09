package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.model.Volume;
import com.mycompany.ebookwebsite.model.Chapter;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.CommentService;
import com.mycompany.ebookwebsite.service.VolumeService;
import com.mycompany.ebookwebsite.service.ChapterService;
import com.mycompany.ebookwebsite.utils.EbookValidation;
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

@WebServlet("/book/detail")
public class BookDetailServlet extends HttpServlet {

    private final EbookService ebookService = new EbookService();
    private final CommentService commentService = new CommentService();
    private final VolumeService volumeService = new VolumeService();
    private final ChapterService chapterService = new ChapterService();
    private final EbookAuthorDAO ebookAuthorDAO = new EbookAuthorDAO();
    private final AuthorDAO authorDAO = new AuthorDAO();
    private final EbookTagDAO ebookTagDAO = new EbookTagDAO();
    private final TagDAO tagDAO = new TagDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // validate và xử lý
            int id = EbookValidation.validateId(request.getParameter("id"));
            Ebook ebook = ebookService.getEbookById(id);
            if (ebook == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                return;
            }

            ebookService.incrementViewCount(id);
            List<Comment> comments = commentService.getCommentsByEbookId(id);

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
            request.setAttribute("comments", comments);
            request.setAttribute("volumes", volumes);
            request.setAttribute("chapters", chapters);
            request.setAttribute("isMultiVolume", isMultiVolume);
            request.setAttribute("authors", authors);
            request.setAttribute("tags", tags);
            request.getRequestDispatcher("/book/detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }

    }
}
