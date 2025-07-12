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

}
