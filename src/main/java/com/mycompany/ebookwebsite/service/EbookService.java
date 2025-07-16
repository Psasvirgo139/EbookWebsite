package com.mycompany.ebookwebsite.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.mycompany.ebookwebsite.dao.ChapterDAO;
import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.dao.FavoriteDAO;
import com.mycompany.ebookwebsite.dao.UserReadDAO;
import com.mycompany.ebookwebsite.model.Chapter;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.LatestBookView;
import com.mycompany.ebookwebsite.model.User;

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

    /**
     * Lọc danh sách Ebook theo nhiều tiêu chí (status, genre, sortBy)
     */
    public List<Ebook> filterBooksByCriteria(List<Ebook> books, String status, String genre, String sortBy) {
        System.out.println("[DEBUG] Filter - status: '" + status + "', genre: '" + genre + "', sortBy: '" + sortBy + "'");
        return books.stream()
            .filter(book -> {
                boolean match = status == null || status.isEmpty() || status.equalsIgnoreCase(book.getStatus());
                if (!match) {
                    System.out.println("[DEBUG] Không match status: " + book.getTitle() + " - " + book.getStatus());
                }
                return match;
            })
            .filter(book -> {
                boolean match = genre == null || genre.isEmpty() ||
                    (book.getReleaseType() != null && genre.trim().equalsIgnoreCase(book.getReleaseType().trim()));
                if (!match) {
                    System.out.println("[DEBUG] Không match genre: " + book.getTitle() + " - '" + book.getReleaseType() + "'");
                }
                return match;
            })
            .sorted((a, b) -> {
                if (sortBy == null || sortBy.isEmpty()) return 0;
                switch (sortBy) {
                    case "title":
                        return a.getTitle().compareToIgnoreCase(b.getTitle());
                    case "newest":
                        return b.getId() - a.getId();
                    case "oldest":
                        return a.getId() - b.getId();
                    case "views":
                        return b.getViewCount() - a.getViewCount();
                    default:
                        return 0;
                }
            })
            .collect(Collectors.toList());
    }

    /**
     * Lọc sách cho trang book-list: ưu tiên keyword, nếu không thì lọc nâng cao (genre, status, sortBy)
     */
    public List<Ebook> searchBooksForList(String keyword, String genre, String status, String sortBy) throws SQLException {
        keyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        genre = (genre != null && !genre.trim().isEmpty()) ? genre.trim() : null;
        status = (status != null && !status.trim().isEmpty()) ? status.trim() : null;
        sortBy = (sortBy != null && !sortBy.trim().isEmpty()) ? sortBy.trim() : null;
        if (keyword != null) {
            return searchByKeyword(keyword);
        } else {
            // Không dùng author, minChapters cho book-list
            return searchWithFilters(genre, null, null, sortBy, status);
        }
    }
}
