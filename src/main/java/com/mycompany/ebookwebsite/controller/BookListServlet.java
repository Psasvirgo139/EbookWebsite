package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.util.List;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Tag;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.TagService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "BookListServlet", urlPatterns = {"/book-list", "/books"})
public class BookListServlet extends HttpServlet {
    
    private EbookDAO ebookDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        ebookDAO = new EbookDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");

            // Lấy danh sách tag (thể loại)
            TagService tagService = new TagService();
            List<Tag> tags = tagService.getAllTags();
            request.setAttribute("tags", tags);

            // Lấy các filter từ request và clean input
            String keyword = cleanAndTrimInput(request.getParameter("keyword"));
            String genre = cleanAndTrimInput(request.getParameter("genre"));
            String author = cleanAndTrimInput(request.getParameter("author"));
            String minChaptersStr = request.getParameter("minChapters");
            String sortBy = cleanAndTrimInput(request.getParameter("sortBy"));
            String status = cleanAndTrimInput(request.getParameter("status"));

            // Parse minChapters
            Integer minChapters = null;
            if (minChaptersStr != null && !minChaptersStr.trim().isEmpty() && !minChaptersStr.equals("0")) {
                try {
                    minChapters = Integer.parseInt(minChaptersStr);
                    if (minChapters <= 0) {
                        minChapters = null;
                    }
                } catch (NumberFormatException e) {
                    minChapters = null;
                }
            }

            // Logic ưu tiên: Nếu có keyword thì ưu tiên search theo keyword
            EbookService ebookService = new EbookService();
            List<Ebook> bookList;
            if (keyword != null && !keyword.isEmpty()) {
                bookList = ebookService.searchByKeyword(keyword);
                request.setAttribute("searchKeyword", keyword);
            } else {
                if (status != null && status.equals("all")) status = null;
                bookList = ebookService.searchWithFilters(genre, author, minChapters, sortBy, status);
            }

            // Phân trang
            int page = 1;
            int pageSize = 12;
            try {
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
            int totalBooks = bookList.size();
            int totalPages = (int) Math.ceil((double) totalBooks / pageSize);
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalBooks);
            List<Ebook> pagedBooks = (startIndex < totalBooks) ? bookList.subList(startIndex, endIndex) : java.util.Collections.emptyList();

            // Đặt attributes cho JSP
            request.setAttribute("bookList", pagedBooks);
            request.setAttribute("totalBooks", totalBooks);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("selectedGenre", genre);
            request.setAttribute("status", status);
            request.setAttribute("author", author);
            request.setAttribute("minChapters", minChaptersStr);

            // Tính toán pagination info
            int startBook = totalBooks > 0 ? startIndex + 1 : 0;
            int endBookInfo = endIndex;
            request.setAttribute("startBook", startBook);
            request.setAttribute("endBook", endBookInfo);

            request.getRequestDispatcher("/book/list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.setAttribute("bookList", java.util.Collections.emptyList());
            request.setAttribute("totalBooks", 0);
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 0);
            request.getRequestDispatcher("/book/list.jsp").forward(request, response);
        }
    }

    // Hàm tiện ích clean input
    private String cleanAndTrimInput(String input) {
        if (input == null) return null;
        String cleaned = input.trim().replaceAll("\\s+", " ");
        return cleaned.isEmpty() ? null : cleaned;
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chuyển POST request thành GET request để xử lý tìm kiếm
        doGet(request, response);
    }
} 