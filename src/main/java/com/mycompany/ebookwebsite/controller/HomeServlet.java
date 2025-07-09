package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.utils.EbookValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/book/home")
public class HomeServlet extends HttpServlet {
    private final EbookService service = new EbookService();
    private static final int PAGE_SIZE = 8;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = 1;
        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null) {
                currentPage = EbookValidation.validatePage(pageParam);
            }

            int offset = (currentPage - 1) * PAGE_SIZE;
            List<Ebook> ebooks = service.getBooksByPage(offset, PAGE_SIZE);
            int totalBooks = service.countAllBooks();
            int totalPages = (int) Math.ceil((double) totalBooks / PAGE_SIZE);

            req.setAttribute("ebookList", ebooks);
            req.setAttribute("currentPage", currentPage);
            req.setAttribute("totalPages", totalPages);
            req.getRequestDispatcher("/book/list.jsp").forward(req, resp);
        } catch (IllegalArgumentException | SQLException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}


