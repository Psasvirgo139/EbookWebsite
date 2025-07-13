package com.mycompany.ebookwebsite.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.dao.FavoriteDAO;
import com.mycompany.ebookwebsite.dao.UserReadDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.LatestBookView;
import com.mycompany.ebookwebsite.model.User;

public class EbookService {
    private final EbookDAO ebookDAO = new EbookDAO();
    private final FavoriteDAO favoriteDAO = new FavoriteDAO();
    private final UserReadDAO userReadDAO = new UserReadDAO();

    public List<Ebook> getBooksByPage(int offset, int pageSize) throws SQLException {
        return ebookDAO.getBooksByPage(offset, pageSize);
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
        return ebookDAO.getBooksByPage(0, limit);
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
}
