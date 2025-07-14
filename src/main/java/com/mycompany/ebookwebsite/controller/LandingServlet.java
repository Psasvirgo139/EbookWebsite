package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Tag;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.TagService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "LandingServlet", urlPatterns = {""})
public class LandingServlet extends HttpServlet {

    private final EbookService ebookService = new EbookService();
    private static final int FEATURE_SIZE = 6;
    private final TagService tagService = new TagService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Ebook> featured = ebookService.getBooksByPage(0, FEATURE_SIZE);
            req.setAttribute("featuredBooks", featured);
            List<Tag> tags = tagService.getAllTags();
            req.setAttribute("tags", tags);
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Database error", e);
        }
    }
} 