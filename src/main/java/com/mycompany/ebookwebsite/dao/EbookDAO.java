package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Ebook;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EbookDAO {

    private static final String SELECT_BY_PAGE = "SELECT * FROM Ebooks WHERE status = 'active' ORDER BY created_at DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    private static final String COUNT_ALL = "SELECT COUNT(*) FROM Ebooks WHERE status = 'active'";
    private static final String SELECT_BY_ID = "SELECT * FROM Ebooks WHERE id = ?";
    private static final String INCREMENT_VIEW = "UPDATE Ebooks SET view_count = view_count + 1 WHERE id = ?";
    private static final String SELECT_ALL_BOOKS = "SELECT * FROM Ebooks ORDER BY created_at DESC";
    private static final String SEARCH_BOOKS = "SELECT * FROM Ebooks WHERE (title LIKE ? OR description LIKE ?) AND status = 'active' ORDER BY created_at DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    private static final String COUNT_SEARCH_RESULTS = "SELECT COUNT(*) FROM Ebooks WHERE (title LIKE ? OR description LIKE ?) AND status = 'active'";
    private static final String SELECT_BY_STATUS = "SELECT * FROM Ebooks WHERE status = ? ORDER BY created_at DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    private static final String COUNT_BY_STATUS = "SELECT COUNT(*) FROM Ebooks WHERE status = ?";
    private static final String INSERT_BOOK = "INSERT INTO Ebooks (title, description, release_type, language, status, visibility, uploader_id, cover_url, is_premium, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_BOOK = "UPDATE Ebooks SET title = ?, description = ?, release_type = ?, language = ?, status = ?, visibility = ?, cover_url = ?, is_premium = ?, price = ? WHERE id = ?";
    private static final String DELETE_BOOK = "UPDATE Ebooks SET status = 'deleted' WHERE id = ?";
    private static final String UPDATE_PREMIUM_STATUS = "UPDATE Ebooks SET is_premium = ?, price = ? WHERE id = ?";

    public List<Ebook> getBooksByPage(int offset, int limit) throws SQLException {
        List<Ebook> ebooks = new ArrayList<>();
        String sql = "SELECT * FROM Ebooks ORDER BY created_at DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ebooks.add(mapRow(rs));
                }
            }
        }
        return ebooks;
    }

    public int countAllBooks() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Ebooks";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public Ebook getBookById(int id) throws SQLException {
        String sql = "SELECT * FROM Ebooks WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    private Ebook mapRow(ResultSet rs) throws SQLException {
        Ebook ebook = new Ebook();
        ebook.setId(rs.getInt("id"));
        ebook.setTitle(rs.getString("title"));
        ebook.setDescription(rs.getString("description"));
        ebook.setReleaseType(rs.getString("release_type"));
        ebook.setLanguage(rs.getString("language"));
        ebook.setStatus(rs.getString("status"));
        ebook.setVisibility(rs.getString("visibility"));
        ebook.setUploaderId(rs.getInt("uploader_id"));
        ebook.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        ebook.setViewCount(rs.getInt("view_count"));
        ebook.setCoverUrl(rs.getString("cover_url"));
        return ebook;
    }

    public Ebook getEbookById(int id) throws SQLException {
        String sql = "SELECT * FROM Ebooks WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
               return mapRow(rs);
            }
            return null;
        }
    }

    public void incrementViewCount(int id) throws SQLException {
        String sql = "UPDATE Ebooks SET view_count = view_count + 1 WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Ebook> selectBooksByPage(int offset, int pageSize) throws SQLException {
        List<Ebook> books = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_PAGE)) {
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        }
        return books;
    }

    public List<Ebook> getAllEbooks() throws SQLException {
        List<Ebook> books = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_BOOKS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        }
        return books;
    }

    // New methods for premium content management
    public List<Ebook> getPremiumBooks() throws SQLException {
        List<Ebook> ebooks = new ArrayList<>();
        String sql = "SELECT * FROM Ebooks WHERE is_premium = 1 AND status = 'active' ORDER BY created_at DESC";

        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ebooks.add(mapRow(rs));
            }
        }

        return ebooks;
    }

    public List<Ebook> getFreeBooks() throws SQLException {
        List<Ebook> ebooks = new ArrayList<>();
        String sql = "SELECT * FROM Ebooks WHERE is_premium = 0 AND status = 'active' ORDER BY created_at DESC";

        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ebooks.add(mapRow(rs));
            }
        }

        return ebooks;
    }

    public int countPremiumBooks() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Ebooks WHERE is_premium = 1 AND status = 'active'";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int countFreeBooks() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Ebooks WHERE is_premium = 0 AND status = 'active'";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public double getTotalPremiumRevenue() throws SQLException {
        String sql = "SELECT SUM(price) FROM Ebooks WHERE is_premium = 1 AND status = 'active'";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        }
    }

    public List<Ebook> getTopPremiumBooks(int limit) throws SQLException {
        List<Ebook> ebooks = new ArrayList<>();
        String sql = "SELECT * FROM Ebooks WHERE is_premium = 1 AND status = 'active' ORDER BY view_count DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ebooks.add(mapRow(rs));
            }
        }

        return ebooks;
    }

    public List<Ebook> getTopFreeBooks(int limit) throws SQLException {
        List<Ebook> ebooks = new ArrayList<>();
        String sql = "SELECT * FROM Ebooks WHERE is_premium = 0 AND status = 'active' ORDER BY view_count DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ebooks.add(mapRow(rs));
            }
        }

        return ebooks;
    }

    public List<Ebook> getRecentBooks(int limit) throws SQLException {
        List<Ebook> ebooks = new ArrayList<>();
        String sql = "SELECT * FROM Ebooks WHERE status = 'active' ORDER BY created_at DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ebooks.add(mapRow(rs));
            }
        }

        return ebooks;
    }

    public void updateBookPremiumStatus(int bookId, boolean isPremium, double price) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE_PREMIUM_STATUS)) {
            ps.setBoolean(1, isPremium);
            ps.setDouble(2, price);
            ps.setInt(3, bookId);
            ps.executeUpdate();
        }
    }

    public List<Ebook> searchBooks(String searchTerm, int offset, int pageSize) throws SQLException {
        List<Ebook> books = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SEARCH_BOOKS)) {
            String searchPattern = "%" + searchTerm + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setInt(3, offset);
            ps.setInt(4, pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        }
        return books;
    }

    public int countSearchResults(String searchTerm) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(COUNT_SEARCH_RESULTS)) {
            String searchPattern = "%" + searchTerm + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Ebook> getBooksByStatus(String status, int offset, int pageSize) throws SQLException {
        List<Ebook> books = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_STATUS)) {
            ps.setString(1, status);
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        }
        return books;
    }

    public int countBooksByStatus(String status) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(COUNT_BY_STATUS)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public void createBook(Ebook book) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT_BOOK, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getDescription());
            ps.setString(3, book.getReleaseType());
            ps.setString(4, book.getLanguage());
            ps.setString(5, book.getStatus());
            ps.setString(6, book.getVisibility());
            ps.setInt(7, book.getUploaderId());
            ps.setString(8, book.getCoverUrl());
            
            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public boolean updateBook(Ebook book) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE_BOOK)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getDescription());
            ps.setString(3, book.getReleaseType());
            ps.setString(4, book.getLanguage());
            ps.setString(5, book.getStatus());
            ps.setString(6, book.getVisibility());
            ps.setString(7, book.getCoverUrl());
            ps.setInt(10, book.getId());
            
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteBook(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE_BOOK)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
