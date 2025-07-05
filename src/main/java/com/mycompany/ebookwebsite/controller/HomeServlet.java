package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.TagService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Tag;

@WebServlet(urlPatterns = {"/", "/home"})
public class HomeServlet extends HttpServlet {
    private final EbookService ebookService = new EbookService();
    private final TagService tagService = new TagService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("=== HomeServlet: Bắt đầu lấy dữ liệu ===");
            
            List<Ebook> topBooks = ebookService.getTopPremiumBooks(8);
            System.out.println("Top books count: " + (topBooks != null ? topBooks.size() : "null"));
            
            List<Ebook> newBooks = ebookService.getRecentBooks(8);
            System.out.println("New books count: " + (newBooks != null ? newBooks.size() : "null"));
            
            List<Ebook> freeBooks = ebookService.getTopFreeBooks(8);
            System.out.println("Free books count: " + (freeBooks != null ? freeBooks.size() : "null"));
            
            List<Tag> categories = tagService.getAllTags();
            System.out.println("Categories count: " + (categories != null ? categories.size() : "null"));
            
            req.setAttribute("topBooks", topBooks);
            req.setAttribute("newBooks", newBooks);
            req.setAttribute("freeBooks", freeBooks);
            req.setAttribute("categories", categories);
            
            System.out.println("=== HomeServlet: Hoàn thành lấy dữ liệu ===");
            
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("=== HomeServlet ERROR: " + e.getMessage() + " ===");
            e.printStackTrace();
            req.setAttribute("error", "Không thể tải dữ liệu sách: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}


