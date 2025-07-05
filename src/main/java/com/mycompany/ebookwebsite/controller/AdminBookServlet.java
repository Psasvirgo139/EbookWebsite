package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Tag;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.TagService;
import com.mycompany.ebookwebsite.utils.EbookValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/book/*")
public class AdminBookServlet extends HttpServlet {

    private final EbookService ebookService = new EbookService();
    private final TagService tagService = new TagService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/";
        }

        try {
            switch (pathInfo) {
                case "/":
                case "/list":
                    listBooks(request, response);
                    break;
                case "/view":
                    viewBook(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/new":
                    showNewForm(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "/";
        }

        try {
            switch (pathInfo) {
                case "/create":
                    createBook(request, response);
                    break;
                case "/update":
                    updateBook(request, response);
                    break;
                case "/delete":
                    deleteBook(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void listBooks(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int page = 1;
        int pageSize = 10;
        String searchTerm = request.getParameter("search");
        String statusFilter = request.getParameter("status");

        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null) {
                page = EbookValidation.validatePage(pageParam);
            }
        } catch (IllegalArgumentException e) {
            // Use default page 1
        }

        int offset = (page - 1) * pageSize;
        List<Ebook> books;
        int totalBooks;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            books = ebookService.searchBooks(searchTerm, offset, pageSize);
            totalBooks = ebookService.countSearchResults(searchTerm);
        } else if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            books = ebookService.getBooksByStatus(statusFilter, offset, pageSize);
            totalBooks = ebookService.countBooksByStatus(statusFilter);
        } else {
            books = ebookService.getBooksByPage(offset, pageSize);
            totalBooks = ebookService.countAllBooks();
        }

        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

        request.setAttribute("books", books);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("totalBooks", totalBooks);

        request.getRequestDispatcher("/admin/book/list.jsp").forward(request, response);
    }

    private void viewBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int bookId = EbookValidation.validateId(request.getParameter("id"));
        Ebook book = ebookService.getEbookById(bookId);

        if (book == null) {
            request.setAttribute("error", "Book not found");
            listBooks(request, response);
            return;
        }

        request.setAttribute("book", book);
        request.getRequestDispatcher("/admin/book/view.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int bookId = EbookValidation.validateId(request.getParameter("id"));
        Ebook book = ebookService.getEbookById(bookId);

        if (book == null) {
            request.setAttribute("error", "Book not found");
            listBooks(request, response);
            return;
        }

        request.setAttribute("book", book);
        request.getRequestDispatcher("/admin/book/edit.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/admin/book/new.jsp").forward(request, response);
    }

    private void createBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        try {
            // Extract form data
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String releaseType = request.getParameter("releaseType");
            String language = request.getParameter("language");
            String status = request.getParameter("status");
            String visibility = request.getParameter("visibility");
            String coverUrl = request.getParameter("coverUrl");

            // Validate required fields
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title is required");
            }

            // Create book object
            Ebook book = new Ebook();
            book.setTitle(title.trim());
            book.setDescription(description != null ? description.trim() : "");
            book.setReleaseType(releaseType != null ? releaseType : "completed");
            book.setLanguage(language != null ? language : "Vietnamese");
            book.setStatus(status != null ? status : "active");
            book.setVisibility(visibility != null ? visibility : "public");
            book.setCoverUrl(coverUrl != null ? coverUrl.trim() : "");

            // Get tags from form
            String[] tagIds = request.getParameterValues("tagIds");
            if (tagIds != null && tagIds.length > 0) {
                List<Tag> tags = new java.util.ArrayList<>();
                for (String tagIdStr : tagIds) {
                    try {
                        int tagId = Integer.parseInt(tagIdStr);
                        Tag tag = tagService.getTagById(tagId);
                        if (tag != null) tags.add(tag);
                    } catch (NumberFormatException ignored) {}
                }
                book.setTags(tags);
            } else {
                book.setTags(null);
            }

            // Get current user as uploader
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("userId") != null) {
                book.setUploaderId((Integer) session.getAttribute("userId"));
            }

            ebookService.createBook(book);

            request.setAttribute("success", "Book created successfully");
            response.sendRedirect(request.getContextPath() + "/admin/book/list");

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            showNewForm(request, response);
        }
    }

    private void updateBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        try {
            int bookId = EbookValidation.validateId(request.getParameter("id"));
            Ebook existingBook = ebookService.getEbookById(bookId);

            if (existingBook == null) {
                request.setAttribute("error", "Book not found");
                listBooks(request, response);
                return;
            }

            // Extract form data
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String releaseType = request.getParameter("releaseType");
            String language = request.getParameter("language");
            String status = request.getParameter("status");
            String visibility = request.getParameter("visibility");
            String coverUrl = request.getParameter("coverUrl");

            // Validate required fields
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title is required");
            }

            // Update book object
            existingBook.setTitle(title.trim());
            existingBook.setDescription(description != null ? description.trim() : "");
            existingBook.setReleaseType(releaseType != null ? releaseType : "completed");
            existingBook.setLanguage(language != null ? language : "Vietnamese");
            existingBook.setStatus(status != null ? status : "active");
            existingBook.setVisibility(visibility != null ? visibility : "public");
            existingBook.setCoverUrl(coverUrl != null ? coverUrl.trim() : "");

            // Update tags from form
            String[] tagIds = request.getParameterValues("tagIds");
            if (tagIds != null && tagIds.length > 0) {
                List<Tag> tags = new java.util.ArrayList<>();
                for (String tagIdStr : tagIds) {
                    try {
                        int tagId = Integer.parseInt(tagIdStr);
                        Tag tag = tagService.getTagById(tagId);
                        if (tag != null) tags.add(tag);
                    } catch (NumberFormatException ignored) {}
                }
                existingBook.setTags(tags);
            } else {
                existingBook.setTags(null);
            }

            ebookService.updateBook(existingBook);

            request.setAttribute("success", "Book updated successfully");
            response.sendRedirect(request.getContextPath() + "/admin/book/list");

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteBook(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int bookId = EbookValidation.validateId(request.getParameter("id"));
        boolean deleted = ebookService.deleteBook(bookId);

        if (deleted) {
            request.setAttribute("success", "Book deleted successfully");
        } else {
            request.setAttribute("error", "Failed to delete book");
        }

        response.sendRedirect(request.getContextPath() + "/admin/book/list");
    }
} 