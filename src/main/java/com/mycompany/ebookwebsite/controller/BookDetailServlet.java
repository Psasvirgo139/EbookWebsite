package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.CommentService;
import com.mycompany.ebookwebsite.service.ChapterService;
import com.mycompany.ebookwebsite.utils.EbookValidation;
import com.mycompany.ebookwebsite.dao.EbookAuthorDAO;
import com.mycompany.ebookwebsite.dao.AuthorDAO;
import com.mycompany.ebookwebsite.model.Author;
import com.mycompany.ebookwebsite.model.EbookAuthor;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/book/detail")
public class BookDetailServlet extends HttpServlet {

    private final EbookService ebookService = new EbookService();
    private final CommentService commentService = new CommentService();
    private final ChapterService chapterService = new ChapterService();
    private final EbookAuthorDAO ebookAuthorDAO = new EbookAuthorDAO();
    private final AuthorDAO authorDAO = new AuthorDAO();

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
            int chapterCount = chapterService.getChaptersByBookId(id).size();
            int commentCount = commentService.getCommentCountByEbookId(id);
            ebook.setReviewCount(commentCount);
            List<com.mycompany.ebookwebsite.model.Chapter> chapters = chapterService.getChaptersByBookId(id);

            // Lấy danh sách tác giả
            List<EbookAuthor> ebookAuthors = ebookAuthorDAO.getAuthorsByEbook(id);
            List<Author> authors = new java.util.ArrayList<>();
            for (EbookAuthor ea : ebookAuthors) {
                Author author = authorDAO.selectAuthor(ea.getAuthorID());
                if (author != null) authors.add(author);
            }

            // Truyền rating mặc định (nếu chưa có logic động)
            double rating = 5.0;

            // Lấy sách liên quan (tạm thời lấy 5 sách khác)
            List<Ebook> relatedBooks = new java.util.ArrayList<>();
            try {
                List<Ebook> allBooks = ebookService.getAllEbooks();
                for (Ebook b : allBooks) {
                    if (b.getId() != id && relatedBooks.size() < 5) {
                        relatedBooks.add(b);
                    }
                }
            } catch (Exception ex) {
                // Nếu lỗi vẫn tiếp tục, relatedBooks sẽ rỗng
            }

            request.setAttribute("ebook", ebook);
            request.setAttribute("comments", comments);
            request.setAttribute("chapterCount", chapterCount);
            request.setAttribute("commentCount", commentCount);
            request.setAttribute("chapters", chapters);
            request.setAttribute("authors", authors);
            request.setAttribute("rating", rating);
            request.setAttribute("relatedBooks", relatedBooks);
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
