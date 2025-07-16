package com.mycompany.ebookwebsite.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.dao.FavoriteDAO;
import com.mycompany.ebookwebsite.dao.UserReadDAO;
import com.mycompany.ebookwebsite.dao.ChapterDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.LatestBookView;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.model.Chapter;

public class EbookService {
    private final EbookDAO ebookDAO = new EbookDAO();
    private final FavoriteDAO favoriteDAO = new FavoriteDAO();
    private final UserReadDAO userReadDAO = new UserReadDAO();
    private final ChapterDAO chapterDAO = new ChapterDAO();

    public List<Ebook> getBooksByPage(int offset, int pageSize) throws SQLException {
        return ebookDAO.getBooksByPage(offset, pageSize);
    }

    /**
     * Lấy danh sách sách nổi bật theo lượt xem
     * @param limit Số lượng sách cần lấy
     * @return Danh sách sách nổi bật
     * @throws SQLException Nếu có lỗi database
     */
    public List<Ebook> getFeaturedBooks(int limit) throws SQLException {
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit phải lớn hơn 0");
        }
        return ebookDAO.getFeaturedBooksByViewCount(limit);
    }

    public int countAllBooks() throws SQLException {
        return ebookDAO.countAllBooks();
    }

    public Ebook getEbookById(int id) throws SQLException {
        return ebookDAO.getEbookById(id);
    }

    public void incrementViewCount(int id) throws SQLException {
        ebookDAO.incrementViewCount(id);
    }

    public void addEbook(Ebook ebook) throws SQLException {
        ebookDAO.insertEbook(ebook);
    }

    public boolean updateEbook(Ebook ebook) throws SQLException {
        return ebookDAO.updateEbook(ebook);
    }

    public void deleteEbookAndRelated(int ebookId, User user) throws SQLException {
        Ebook ebook = ebookDAO.getEbookById(ebookId);
        if (ebook == null) throw new IllegalArgumentException("Ebook không tồn tại!");
        // Admin có thể xóa mọi lúc, user chỉ được xóa ebook của mình và chỉ khi visibility là 'approving'
        boolean isAdmin = "admin".equals(user.getRole());
        boolean isOwner = user.getId() == ebook.getUploaderId();
        boolean canUserDelete = isOwner && "approving".equalsIgnoreCase(ebook.getVisibility());
        if (!isAdmin && !canUserDelete) {
            throw new SecurityException("Bạn không có quyền xóa sách này!");
        }
        Connection conn = null;
        try {
            conn = com.mycompany.ebookwebsite.dao.DBConnection.getConnection();
            conn.setAutoCommit(false);
            ebookDAO.deleteEbook(ebookId); // soft delete
            favoriteDAO.deleteByEbook(ebookId);
            userReadDAO.deleteByEbook(ebookId);
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<Ebook> getLatestBooks(int limit) throws SQLException {
        return ebookDAO.getLatestBooks(limit);
    }
    
    /**
     * Lấy danh sách truyện mới nhất dưới dạng LatestBookView
     * @param limit Số lượng truyện cần lấy
     * @return Danh sách LatestBookView
     * @throws SQLException
     */
    public List<LatestBookView> getLatestBookViews(int limit) throws SQLException {
        List<Ebook> books = getLatestBooks(limit);
        return books.stream()
                .map(LatestBookView::new)
                .collect(Collectors.toList());
    }

    public List<Ebook> getAllBooks() throws SQLException {
        return ebookDAO.selectAllEbooks();
    }

    public List<com.mycompany.ebookwebsite.model.AdminBookView> getAdminBookViews() throws java.sql.SQLException {
        List<Ebook> ebooks = getAllBooks();
        List<com.mycompany.ebookwebsite.model.AdminBookView> views = new java.util.ArrayList<>();
        for (Ebook e : ebooks) {
            views.add(new com.mycompany.ebookwebsite.model.AdminBookView(
                e.getId(), e.getTitle(), e.getReleaseType(), e.getCreatedAt(), e.getStatus(), e.getViewCount()
            ));
        }
        return views;
    }

    /**
     * Enhanced keyword search - Service layer for SearchServlet
     */
    public List<Ebook> searchByKeyword(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword không được để trống");
        }
        return ebookDAO.searchByKeywordOnly(keyword);
    }

    /**
     * Advanced search with filters - Service layer for SearchServlet
     */
    public List<Ebook> searchWithFilters(String genre, String author, Integer minChapters, String sortBy, String status) throws SQLException {
        return ebookDAO.searchWithFilters(null, genre, author, minChapters, sortBy, status);
    }

    /**
     * Lấy danh sách sách mới cập nhật dựa trên chương mới nhất (unique ebook_id)
     */
    public List<Ebook> getRecentlyUpdatedBooks(int limit) throws SQLException {
        List<Chapter> chapters = chapterDAO.getLatestUniqueChapters(limit);
        List<Ebook> ebooks = new java.util.ArrayList<>();
        for (Chapter c : chapters) {
            Ebook ebook = ebookDAO.getEbookById(c.getEbookID());
            if (ebook != null) ebooks.add(ebook);
        }
        return ebooks;
    }
}
