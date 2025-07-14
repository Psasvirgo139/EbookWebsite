package com.mycompany.ebookwebsite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.ebookwebsite.model.Ebook;

public class EbookDAO {

    // ===== CORE SQL QUERIES (AI FIELDS REMOVED) =====
    private static final String INSERT = "INSERT INTO Ebooks (title, description, release_type, language, status, visibility, uploader_id, created_at, view_count, cover_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM Ebooks WHERE id = ?";
    private static final String COUNT_ALL = "SELECT COUNT(*) FROM Ebooks";
    private static final String UPDATE = "UPDATE Ebooks SET title=?, description=?, release_type=?, language=?, status=?, visibility=?, uploader_id=?, created_at=?, view_count=?, cover_url=? WHERE id=?";
    private static final String SOFT_DELETE = "UPDATE Ebooks SET status = 'deleted' WHERE id = ?";
    private static final String INCREMENT_VIEW = "UPDATE Ebooks SET view_count = view_count + 1 WHERE id = ?";
    private static final String SELECT_ALL_BY_CREATED_TIME = "SELECT * FROM Ebooks ORDER BY created_at DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    public List<Ebook> getBooksByPage(int offset, int limit) throws SQLException {
        List<Ebook> ebooks = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SELECT_ALL_BY_CREATED_TIME)) {
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
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(COUNT_ALL); ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public Ebook getBookById(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    // ===== CORE MAPROW (AI FIELDS REMOVED) =====
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
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
               return mapRow(rs);
            }
            return null;
        }
    }

    public void incrementViewCount(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(INCREMENT_VIEW)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ===== CORE INSERT (AI FIELDS REMOVED) =====
    public void insertEbook(Ebook ebook) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(INSERT)) {
            ps.setString(1, ebook.getTitle());
            ps.setString(2, ebook.getDescription());
            ps.setString(3, ebook.getReleaseType());
            ps.setString(4, ebook.getLanguage());
            ps.setString(5, ebook.getStatus());
            ps.setString(6, ebook.getVisibility());
            ps.setInt(7, ebook.getUploaderId());
            ps.setTimestamp(8, Timestamp.valueOf(ebook.getCreatedAt()));
            ps.setInt(9, ebook.getViewCount());
            ps.setString(10, ebook.getCoverUrl());
            ps.executeUpdate();
        }
    }

    // ===== CORE UPDATE (AI FIELDS REMOVED) =====
    public boolean updateEbook(Ebook ebook) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(UPDATE)) {
            ps.setString(1, ebook.getTitle());
            ps.setString(2, ebook.getDescription());
            ps.setString(3, ebook.getReleaseType());
            ps.setString(4, ebook.getLanguage());
            ps.setString(5, ebook.getStatus());
            ps.setString(6, ebook.getVisibility());
            ps.setInt(7, ebook.getUploaderId());
            ps.setTimestamp(8, Timestamp.valueOf(ebook.getCreatedAt()));
            ps.setInt(9, ebook.getViewCount());
            ps.setString(10, ebook.getCoverUrl());
            ps.setInt(11, ebook.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteEbook(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(SOFT_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ===== AI Compatibility aliases (redirect to existing methods) =====
    
    public Ebook selectEbook(int id) throws SQLException {
        return getEbookById(id); // Use existing method
    }
    
    // Note: selectAllEbooks(), search(), selectEbooksByVisibility() 
    // should use BookService layer instead of duplicating here
    
    // ===== Temporary compatibility methods - should refactor to use BookService =====
    
    public List<Ebook> selectAllEbooks() throws SQLException {
        // TODO: Refactor to use BookService.getAllBooks() instead
        String sql = "SELECT * FROM Ebooks WHERE status != 'deleted' OR status IS NULL ORDER BY created_at DESC";
        List<Ebook> ebooks = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ebooks.add(mapRow(rs));
            }
        }
        return ebooks;
    }
    
    public List<Ebook> selectEbooksByVisibility(String visibility) throws SQLException {
        // TODO: Refactor to use BookService.getBooksByCategory() instead
        String sql = "SELECT * FROM Ebooks WHERE visibility = ? AND (status != 'deleted' OR status IS NULL) ORDER BY created_at DESC";
        List<Ebook> ebooks = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, visibility);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ebooks.add(mapRow(rs));
                }
            }
        }
        return ebooks;
    }
    
    public List<Ebook> search(String keyword) throws SQLException {
        // TODO: Refactor to use BookService.searchBooks() instead
        String sql = "SELECT * FROM Ebooks WHERE (title LIKE ? OR description LIKE ?) AND (status != 'deleted' OR status IS NULL) ORDER BY created_at DESC";
        List<Ebook> ebooks = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ebooks.add(mapRow(rs));
                }
            }
        }
        return ebooks;
    }

    /**
     * Enhanced keyword-only search with better text matching
     */
    public List<Ebook> searchByKeywordOnly(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        // Clean keyword: remove extra spaces, convert to lowercase for case-insensitive search
        String cleanKeyword = keyword.trim().toLowerCase().replaceAll("\\s+", " ");
        
        // Use LOWER function for case-insensitive search in SQL Server
        String sql = "SELECT * FROM Ebooks WHERE " +
                     "(LOWER(title) LIKE ? OR LOWER(description) LIKE ?) " +
                     "AND (status != 'deleted' OR status IS NULL) " +
                     "AND visibility = 'public' " +
                     "ORDER BY " +
                     "CASE " +
                     "  WHEN LOWER(title) = ? THEN 1 " +              // Exact match first
                     "  WHEN LOWER(title) LIKE ? THEN 2 " +          // Title starts with keyword
                     "  WHEN LOWER(title) LIKE ? THEN 3 " +          // Title contains keyword
                     "  ELSE 4 " +                                   // Description contains keyword
                     "END, " +
                     "view_count DESC";
        
        List<Ebook> ebooks = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + cleanKeyword + "%";
            String startsWithPattern = cleanKeyword + "%";
            
            ps.setString(1, searchPattern);     // title LIKE
            ps.setString(2, searchPattern);     // description LIKE
            ps.setString(3, cleanKeyword);      // exact title match
            ps.setString(4, startsWithPattern); // title starts with
            ps.setString(5, searchPattern);     // title contains
            
            System.out.println("EbookDAO.searchByKeywordOnly - SQL: " + sql);
            System.out.println("EbookDAO.searchByKeywordOnly - Keyword: " + cleanKeyword);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ebooks.add(mapRow(rs));
                }
            }
            
            System.out.println("EbookDAO.searchByKeywordOnly - Found " + ebooks.size() + " books");
        } catch (SQLException e) {
            System.err.println("EbookDAO.searchByKeywordOnly - SQL Error: " + e.getMessage());
            throw e;
        }
        return ebooks;
    }
    
    public List<Ebook> selectEbooksByUploader(int uploaderId) throws SQLException {
        String sql = "SELECT * FROM Ebooks WHERE uploader_id = ? AND (status != 'deleted' OR status IS NULL) ORDER BY created_at DESC";
        List<Ebook> ebooks = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, uploaderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ebooks.add(mapRow(rs));
                }
            }
        }
        return ebooks;
    }

    /**
     * Advanced search with multiple filters
     */
    public List<Ebook> searchWithFilters(String keyword, String genre, String author, Integer minChapters, String sortBy, String status) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT e.* FROM Ebooks e ");
        List<Object> params = new ArrayList<>();
        
        // JOIN với các bảng cần thiết
        if (genre != null && !genre.trim().isEmpty()) {
            sql.append("JOIN EbookTags et ON e.id = et.ebook_id ");
            sql.append("JOIN Tags t ON et.tag_id = t.id ");
        }
        if (author != null && !author.trim().isEmpty()) {
            sql.append("JOIN EbookAuthors ea ON e.id = ea.ebook_id ");
            sql.append("JOIN Authors a ON ea.author_id = a.id ");
        }
        if (minChapters != null && minChapters > 0) {
            sql.append("LEFT JOIN (SELECT ebook_id, COUNT(*) as chapter_count FROM Chapters GROUP BY ebook_id) cc ON e.id = cc.ebook_id ");
        }
        
        sql.append("WHERE (e.status != 'deleted' OR e.status IS NULL) ");
        sql.append("AND e.visibility = 'public' ");
        
        // Keyword filter
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (e.title LIKE ? OR e.description LIKE ?) ");
            String searchPattern = "%" + keyword.trim() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
        }
        
        // Genre filter
        if (genre != null && !genre.trim().isEmpty()) {
            sql.append("AND t.name = ? ");
            params.add(genre.trim());
        }
        
        // Author filter  
        if (author != null && !author.trim().isEmpty()) {
            sql.append("AND a.name LIKE ? ");
            params.add("%" + author.trim() + "%");
        }
        
        // Status filter
        if (status != null && !status.equals("all") && !status.trim().isEmpty()) {
            sql.append("AND e.status = ? ");
            params.add(status);
        }
        
        // MinChapters filter
        if (minChapters != null && minChapters > 0) {
            sql.append("AND cc.chapter_count >= ? ");
            params.add(minChapters);
        }
        
        // Sort
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            switch (sortBy) {
                case "view_total":
                    sql.append("ORDER BY e.view_count DESC ");
                    break;
                case "view_week":
                case "view_month":
                    // Giả định sắp xếp theo view_count (có thể mở rộng với bảng analytics)
                    sql.append("ORDER BY e.view_count DESC ");
                    break;
                case "like":
                    // Giả định sắp xếp theo view_count (có thể join với bảng likes)
                    sql.append("ORDER BY e.view_count DESC ");
                    break;
                default:
                    sql.append("ORDER BY e.created_at DESC ");
            }
        } else {
            sql.append("ORDER BY e.created_at DESC ");
        }
        
        List<Ebook> ebooks = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            // Log SQL for debugging
            System.out.println("EbookDAO.searchWithFilters - SQL: " + sql.toString());
            System.out.println("EbookDAO.searchWithFilters - Params: " + params);
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ebooks.add(mapRow(rs));
                }
            }
            
            System.out.println("EbookDAO.searchWithFilters - Found " + ebooks.size() + " books");
        } catch (SQLException e) {
            System.err.println("EbookDAO.searchWithFilters - SQL Error: " + e.getMessage());
            throw e;
        }
        return ebooks;
    }

}
