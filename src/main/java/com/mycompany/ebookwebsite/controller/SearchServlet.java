package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Author;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Tag;
import com.mycompany.ebookwebsite.service.AuthorService;
import com.mycompany.ebookwebsite.service.TagService;
import com.mycompany.ebookwebsite.service.EbookService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
    private AuthorService authorService;
    private TagService tagService;
    private EbookService ebookService;

    @Override
    public void init() {
        authorService = new AuthorService();
        tagService = new TagService();
        ebookService = new EbookService();
    }

    /**
     * Clean and trim input string, handle multiple spaces and case
     */
    private String cleanAndTrimInput(String input) {
        if (input == null) return null;
        String cleaned = input.trim().replaceAll("\\s+", " "); // Remove extra spaces
        return cleaned.isEmpty() ? null : cleaned;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Set encoding
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            
            // Lấy danh sách tag (thể loại)
            List<Tag> tags = tagService.getAllTags();
            request.setAttribute("tags", tags);

            // Lấy top 10 tác giả nhiều truyện nhất
            List<Author> topAuthors = authorService.getTopAuthorsByBookCount(10);
            request.setAttribute("topAuthors", topAuthors);

            // Lấy các filter từ request và clean input
            String keyword = cleanAndTrimInput(request.getParameter("keyword"));
            String genre = cleanAndTrimInput(request.getParameter("genre"));
            String author = cleanAndTrimInput(request.getParameter("author"));
            String minChaptersStr = request.getParameter("minChapters");
            String sortBy = cleanAndTrimInput(request.getParameter("sortBy"));
            String status = cleanAndTrimInput(request.getParameter("status"));

            // Log parameters for debugging
            System.out.println("SearchServlet - Parameters:");
            System.out.println("  keyword: " + keyword);
            System.out.println("  genre: " + genre);
            System.out.println("  author: " + author);
            System.out.println("  minChapters: " + minChaptersStr);
            System.out.println("  sortBy: " + sortBy);
            System.out.println("  status: " + status);

            // Parse minChapters
            Integer minChapters = null;
            if (minChaptersStr != null && !minChaptersStr.trim().isEmpty() && !minChaptersStr.equals("0")) {
                try {
                    minChapters = Integer.parseInt(minChaptersStr);
                    if (minChapters <= 0) {
                        minChapters = null;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid minChapters value: " + minChaptersStr);
                    minChapters = null;
                }
            }

            // Logic ưu tiên: Nếu có keyword thì ưu tiên search theo keyword
            List<Ebook> bookList;
            if (keyword != null && !keyword.isEmpty()) {
                // Khi có keyword: chỉ tìm theo keyword, bỏ qua các filter khác
                System.out.println("SearchServlet - Searching by keyword only: " + keyword);
                bookList = ebookService.searchByKeyword(keyword);
            } else {
                // Khi không có keyword: dùng advanced filters
                System.out.println("SearchServlet - Using advanced filters");
                // Clean null values cho advanced search
                if (status != null && status.equals("all")) status = null;
                bookList = ebookService.searchWithFilters(genre, author, minChapters, sortBy, status);
            }
            
            request.setAttribute("bookList", bookList);

            // Debug info
            System.out.println("SearchServlet - Found " + bookList.size() + " books");

            request.getRequestDispatcher("/book/search.jsp").forward(request, response);
        } catch (SQLException e) {
            System.err.println("SearchServlet - SQL Error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tìm kiếm trong cơ sở dữ liệu. Vui lòng thử lại.");
            request.getRequestDispatcher("/book/search.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            System.err.println("SearchServlet - Invalid Parameter: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/book/search.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("SearchServlet - General Error: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Lỗi hệ thống khi tải dữ liệu tìm kiếm", e);
        }
    }
} 