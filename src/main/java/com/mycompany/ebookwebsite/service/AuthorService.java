package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.AuthorDAO;
import com.mycompany.ebookwebsite.model.Author;
import com.mycompany.ebookwebsite.model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuthorService {
    private final AuthorDAO authorDAO = new AuthorDAO();
    private static final Logger logger = Logger.getLogger(AuthorService.class.getName());
    private static final int TOP_AUTHOR_LIMIT = 10;
    private static List<Author> cachedTopAuthors = new ArrayList<>();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static boolean schedulerStarted = false;

    public AuthorService() {
        startTopAuthorsScheduler();
    }

    private synchronized void startTopAuthorsScheduler() {
        if (schedulerStarted) return;
        // Load ngay khi start
        reloadTopAuthors();
        // Schedule reload mỗi ngày vào 0h00
        long initialDelay = calculateInitialDelayToMidnight();
        scheduler.scheduleAtFixedRate(this::reloadTopAuthors, initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
        schedulerStarted = true;
        logger.info("[AuthorService] TopAuthors scheduler started, reload at 0h00 mỗi ngày");
    }

    private void reloadTopAuthors() {
        try {
            List<Author> top = authorDAO.getTopAuthorsByBookCount(TOP_AUTHOR_LIMIT);
            synchronized (AuthorService.class) {
                cachedTopAuthors = top;
            }
            logger.info("[AuthorService] Reloaded top authors at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } catch (Exception e) {
            logger.warning("[AuthorService] Failed to reload top authors: " + e.getMessage());
        }
    }

    private long calculateInitialDelayToMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next0000 = now.toLocalDate().plusDays(1).atTime(0, 0);
        return java.time.Duration.between(now, next0000).toSeconds();
    }

    public void addAuthor(Author author) throws SQLException {
        authorDAO.insertAuthor(author);
    }
    public boolean updateAuthor(Author author) throws SQLException {
        return authorDAO.updateAuthor(author);
    }
    public boolean deleteAuthor(int id, User user) throws SQLException {
        if (!"admin".equals(user.getRole())) {
            throw new SecurityException("Bạn không có quyền xóa tác giả!");
        }
        return authorDAO.deleteAuthor(id);
    }
    public Author getAuthorById(int id) throws SQLException {
        return authorDAO.selectAuthor(id);
    }
    public List<Author> getAllAuthors() throws SQLException {
        return authorDAO.selectAllAuthors();
    }
    public List<Author> searchAuthors(String name) throws SQLException {
        return authorDAO.search(name);
    }
    public List<Author> getTopAuthorsByBookCount(int limit) {
        // Luôn trả về cache, chỉ lấy tối đa limit
        synchronized (AuthorService.class) {
            return new ArrayList<>(cachedTopAuthors.subList(0, Math.min(limit, cachedTopAuthors.size())));
        }
    }
} 