package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.AdminBookView;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.model.Ebook;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "BookAdminServlet", urlPatterns = {"/admin/books"})
public class BookAdminServlet extends HttpServlet {
    private final EbookService ebookService = new EbookService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        com.mycompany.ebookwebsite.model.User user =
                (session != null) ? (com.mycompany.ebookwebsite.model.User) session.getAttribute("user") : null;
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String editId = req.getParameter("editId");
        if (editId != null) {
            try {
                Ebook ebook = new EbookService().getEbookById(Integer.parseInt(editId));
                req.setAttribute("editBook", ebook);
                req.getRequestDispatcher("/admin/edit_book.jsp").forward(req, resp);
                return;
            } catch (Exception e) {
                throw new ServletException("Lỗi lấy thông tin sách để sửa", e);
            }
        }
        try {
            List<AdminBookView> books = new EbookService().getAdminBookViews();
            req.setAttribute("books", books);
        } catch (Exception e) {
            throw new ServletException("Lỗi lấy danh sách sách cho admin", e);
        }
        req.getRequestDispatcher("/admin/books.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("update".equals(action)) {
            int bookId = Integer.parseInt(req.getParameter("bookId"));
            String title = req.getParameter("title");
            String releaseType = req.getParameter("releaseType");
            String status = req.getParameter("status");
            // ... các trường khác nếu cần
            if (!com.mycompany.ebookwebsite.utils.EbookValidation.isValidTitle(title)) {
                try {
                    req.setAttribute("error", "Tên sách không hợp lệ!");
                    req.setAttribute("editBook", new EbookService().getEbookById(bookId));
                    req.getRequestDispatcher("/admin/edit_book.jsp").forward(req, resp);
                } catch (Exception e) {
                    throw new ServletException("Lỗi khi forward trang sửa sách", e);
                }
                return;
            }
            try {
                EbookService ebookService = new EbookService();
                Ebook ebook = null;
                try {
                    ebook = ebookService.getEbookById(bookId);
                } catch (java.sql.SQLException ex) {
                    throw new ServletException("Lỗi truy vấn sách khi cập nhật", ex);
                }
                if (ebook == null) {
                    throw new ServletException("Không tìm thấy sách để cập nhật");
                }
                ebook.setTitle(title);
                ebook.setReleaseType(releaseType);
                ebook.setStatus(status);
                // ... set các trường khác nếu cần (giữ nguyên uploaderId, createdAt, ...)
                try {
                    ebookService.updateEbook(ebook);
                } catch (java.sql.SQLException ex) {
                    throw new ServletException("Lỗi cập nhật sách vào DB", ex);
                }
                resp.sendRedirect(req.getContextPath() + "/admin/books");
            } catch (Exception e) {
                throw new ServletException("Lỗi cập nhật sách", e);
            }
            return;
        }
        if ("delete".equals(action)) {
            int bookId = Integer.parseInt(req.getParameter("bookId"));
            HttpSession session = req.getSession(false);
            com.mycompany.ebookwebsite.model.User user =
                (session != null) ? (com.mycompany.ebookwebsite.model.User) session.getAttribute("user") : null;
            if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            try {
                try {
                    ebookService.deleteEbookAndRelated(bookId, user);
                } catch (java.sql.SQLException ex) {
                    throw new ServletException("Lỗi xóa sách trong DB", ex);
                }
                resp.sendRedirect(req.getContextPath() + "/admin/books");
            } catch (Exception e) {
                throw new ServletException("Lỗi xóa sách", e);
            }
            return;
        }
        // ... xử lý các action khác như xóa ...
    }
} 