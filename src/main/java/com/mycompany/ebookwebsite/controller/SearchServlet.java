package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Author;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Tag;
import com.mycompany.ebookwebsite.service.AuthorService;
import com.mycompany.ebookwebsite.service.TagService;
import com.mycompany.ebookwebsite.dao.EbookDAO;
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
    private EbookDAO ebookDAO;

    @Override
    public void init() {
        authorService = new AuthorService();
        tagService = new TagService();
        ebookDAO = new EbookDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lấy danh sách tag (thể loại)
            List<Tag> tags = tagService.getAllTags();
            request.setAttribute("tags", tags);

            // Lấy top 10 tác giả nhiều truyện nhất
            List<Author> topAuthors = authorService.getTopAuthorsByBookCount(10);
            request.setAttribute("topAuthors", topAuthors);

            // Lấy các filter từ request
            String keyword = request.getParameter("keyword");
            String genre = request.getParameter("genre");
            String author = request.getParameter("author");
            String minChapters = request.getParameter("minChapters");
            String sortBy = request.getParameter("sortBy");
            String status = request.getParameter("status");

            // TODO: Xử lý truy vấn tìm kiếm truyện dựa trên các filter trên
            // Hiện tại chỉ trả về tất cả sách (có thể mở rộng sau)
            List<Ebook> bookList = ebookDAO.selectAllEbooks();
            request.setAttribute("bookList", bookList);

            request.getRequestDispatcher("/book/search.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Lỗi khi tải dữ liệu tìm kiếm", e);
        }
    }
} 