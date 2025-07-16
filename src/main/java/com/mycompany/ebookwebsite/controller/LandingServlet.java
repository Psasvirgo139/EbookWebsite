package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Tag;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.TagService;
import com.mycompany.ebookwebsite.service.FavoriteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LandingServlet", urlPatterns = {""})
public class LandingServlet extends HttpServlet {

    private final EbookService ebookService = new EbookService();
    private static final int FEATURE_SIZE = 6;
    private final TagService tagService = new TagService();
    private final FavoriteService favoriteService = new FavoriteService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Lấy danh sách sách nổi bật theo lượt xem
            List<Ebook> featured = ebookService.getFeaturedBooks(FEATURE_SIZE);
            Map<Integer, Integer> favoriteMap = new HashMap<>();
            for (Ebook ebook : featured) {
                int count = 0;
                try {
                    count = favoriteService.countFavoritesByEbook(ebook.getId());
                } catch (SQLException ex) {
                    count = 0;
                }
                favoriteMap.put(ebook.getId(), count);
            }
            req.setAttribute("favoriteMap", favoriteMap);
            req.setAttribute("featuredBooks", featured);

            // Lấy danh sách truyện mới nhất
            List<Ebook> latestBooks = ebookService.getLatestBooks(FEATURE_SIZE);
            Map<Integer, Integer> latestFavoriteMap = new HashMap<>();
            for (Ebook ebook : latestBooks) {
                int count = 0;
                try {
                    count = favoriteService.countFavoritesByEbook(ebook.getId());
                } catch (SQLException ex) {
                    count = 0;
                }
                latestFavoriteMap.put(ebook.getId(), count);
            }
            req.setAttribute("latestBooks", latestBooks);
            req.setAttribute("latestFavoriteMap", latestFavoriteMap);

            // Lấy danh sách sách mới cập nhật (dựa trên chương mới nhất, không trùng ebook_id)
            List<Ebook> lastUpdateList = ebookService.getRecentlyUpdatedBooks(FEATURE_SIZE);
            Map<Integer, Integer> lastUpdateFavoriteMap = new HashMap<>();
            for (Ebook ebook : lastUpdateList) {
                int count = 0;
                try {
                    count = favoriteService.countFavoritesByEbook(ebook.getId());
                } catch (SQLException ex) {
                    count = 0;
                }
                lastUpdateFavoriteMap.put(ebook.getId(), count);
            }
            req.setAttribute("lastUpdateList", lastUpdateList);
            req.setAttribute("lastUpdateFavoriteMap", lastUpdateFavoriteMap);

            List<Tag> tags = tagService.getAllTags();
            req.setAttribute("tags", tags);
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Database error", e);
        }
    }
} 