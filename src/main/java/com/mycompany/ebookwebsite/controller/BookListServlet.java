package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.Ebook;

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
        
        // Thiết lập encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        try {
            // Lấy tham số tìm kiếm
            String searchKeyword = request.getParameter("search");
            String sortBy = request.getParameter("sortBy");
            String genreFilter = request.getParameter("genre");
            
            // Lấy tham số phân trang
            int page = 1;
            int pageSize = 12; // Hiển thị 12 sách mỗi trang
            
            try {
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
            
            List<Ebook> allBooks;
            List<Ebook> bookList;
            
            // Xử lý tìm kiếm
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                allBooks = ebookDAO.search(searchKeyword.trim());
                request.setAttribute("searchKeyword", searchKeyword.trim());
            } else {
                allBooks = ebookDAO.selectAllEbooks();
            }
            
            // Lọc theo thể loại
            if (genreFilter != null && !genreFilter.trim().isEmpty() && !"all".equals(genreFilter)) {
                allBooks = allBooks.stream()
                    .filter(book -> genreFilter.equals(book.getReleaseType()))
                    .collect(Collectors.toList());
                request.setAttribute("selectedGenre", genreFilter);
            }
            
            // Sắp xếp
            if (sortBy != null) {
                switch (sortBy) {
                    case "title":
                        allBooks.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
                        break;
                    case "newest":
                        allBooks.sort((a, b) -> b.getId() - a.getId());
                        break;
                    case "oldest":
                        allBooks.sort((a, b) -> a.getId() - b.getId());
                        break;
                    case "views":
                        allBooks.sort((a, b) -> b.getViewCount() - a.getViewCount());
                        break;
                    default:
                        break;
                }
            }
            
            // Tính toán phân trang
            int totalBooks = allBooks.size();
            int totalPages = (int) Math.ceil((double) totalBooks / pageSize);
            
            // Lấy sách cho trang hiện tại
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalBooks);
            
            if (startIndex < totalBooks) {
                bookList = allBooks.subList(startIndex, endIndex);
            } else {
                bookList = Collections.emptyList();
            }
            
            // Lấy danh sách thể loại để hiển thị filter
            List<String> genres = allBooks.stream()
                .map(Ebook::getReleaseType)
                .filter(genre -> genre != null && !genre.trim().isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
            
            // Đặt attributes cho JSP
            request.setAttribute("bookList", bookList);
            request.setAttribute("totalBooks", totalBooks);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("genres", genres);
            request.setAttribute("sortBy", sortBy);
            
            // Tính toán pagination info
            int startBook = totalBooks > 0 ? startIndex + 1 : 0;
            int endBook = endIndex;
            request.setAttribute("startBook", startBook);
            request.setAttribute("endBook", endBook);
            
            // Forward đến JSP
            request.getRequestDispatcher("/book/bookList.jsp").forward(request, response);
            
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách sách: " + e.getMessage());
            request.setAttribute("bookList", Collections.emptyList());
            request.setAttribute("totalBooks", 0);
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 0);
            request.getRequestDispatcher("/book/bookList.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            request.setAttribute("bookList", Collections.emptyList());
            request.setAttribute("totalBooks", 0);
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 0);
            request.getRequestDispatcher("/book/bookList.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chuyển POST request thành GET request để xử lý tìm kiếm
        doGet(request, response);
    }
} 