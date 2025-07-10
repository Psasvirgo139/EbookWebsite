package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@WebServlet("/book/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class BookUploadServlet extends HttpServlet {

    private EbookDAO ebookDAO;

    @Override
    public void init() throws ServletException {
        ebookDAO = new EbookDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        // Hiển thị form upload
        request.getRequestDispatcher("/book/bookForm.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiểm tra đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        try {
            // Lấy thông tin từ form
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String releaseType = request.getParameter("releaseType");
            String language = request.getParameter("language");
            String status = request.getParameter("status");
            String visibility = request.getParameter("visibility");

            // Validate cơ bản
            if (title == null || title.trim().isEmpty()) {
                request.setAttribute("error", "Tiêu đề không được để trống");
                request.getRequestDispatcher("/book/bookForm.jsp").forward(request, response);
                return;
            }

            // Tạo Ebook object
            Ebook ebook = new Ebook();
            ebook.setTitle(title.trim());
            ebook.setDescription(description != null ? description.trim() : "");
            ebook.setReleaseType(releaseType != null ? releaseType.trim() : "Khác");
            ebook.setLanguage(language != null ? language.trim() : "Tiếng Việt");
            ebook.setStatus(status != null ? status.trim() : "Đang ra");
            ebook.setVisibility(visibility != null ? visibility.trim() : "public");
            ebook.setUploaderId(user.getId());
            ebook.setViewCount(0);
            ebook.setCreatedAt(LocalDateTime.now());

            // Lưu vào database
            ebookDAO.insertEbook(ebook);

            // Redirect về danh sách với thông báo thành công
            response.sendRedirect(request.getContextPath() + "/book-list?success=book_created");

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi khi tạo sách: " + e.getMessage());
            request.getRequestDispatcher("/book/bookForm.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/book/bookForm.jsp").forward(request, response);
        }
    }
} 