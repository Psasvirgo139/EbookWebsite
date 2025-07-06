package com.mycompany.ebookwebsite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.ebookwebsite.model.Ebook;

public class EbookDAO {
    
    private static final String INSERT = "INSERT INTO Ebooks (title, description, release_type, language, status, visibility, uploader_id, cover_url, view_count, file_name, original_file_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Ebooks WHERE status != 'deleted'";
    private static final String SELECT_BY_ID = "SELECT * FROM Ebooks WHERE id = ? AND status != 'deleted'";
    private static final String SEARCH_BY_TITLE = "SELECT * FROM Ebooks WHERE title LIKE ? AND status != 'deleted'";
    private static final String SELECT_BY_UPLOADER = "SELECT * FROM Ebooks WHERE uploader_id = ? AND status != 'deleted'";
    private static final String SELECT_BY_STATUS = "SELECT * FROM Ebooks WHERE status = ?";
    private static final String SELECT_BY_VISIBILITY = "SELECT * FROM Ebooks WHERE visibility = ? AND status != 'deleted'";
    private static final String UPDATE = "UPDATE Ebooks SET title = ?, description = ?, release_type = ?, language = ?, status = ?, visibility = ?, uploader_id = ?, cover_url = ?, view_count = ?, file_name = ?, original_file_name = ? WHERE id = ?";
    private static final String DELETE = "UPDATE Ebooks SET status = 'deleted' WHERE id = ?";
    private static final String INCREMENT_VIEW = "UPDATE Ebooks SET view_count = view_count + 1 WHERE id = ?";
    


    
    public void insertEbook(Ebook ebook) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT)) {
            
            ps.setString(1, ebook.getTitle());
            ps.setString(2, ebook.getDescription());
            ps.setString(3, ebook.getReleaseType());
            ps.setString(4, ebook.getLanguage());
            ps.setString(5, ebook.getStatus());
            ps.setString(6, ebook.getVisibility());
            
            if (ebook.getUploaderId() != null) {
                ps.setInt(7, ebook.getUploaderId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            
            ps.setString(8, ebook.getCoverUrl());
            ps.setInt(9, ebook.getViewCount());
            ps.setString(10, ebook.getFileName());              // Normalized filename
            ps.setString(11, ebook.getOriginalFileName());      // Original filename from user
            
            ps.executeUpdate();
        }
    }
    
    /**
     * 🔥 Insert Ebook and return generated ID
     */
    public int insertEbookAndGetId(Ebook ebook) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, ebook.getTitle());
            ps.setString(2, ebook.getDescription());
            ps.setString(3, ebook.getReleaseType());
            ps.setString(4, ebook.getLanguage());
            ps.setString(5, ebook.getStatus());
            ps.setString(6, ebook.getVisibility());
            
            if (ebook.getUploaderId() != null) {
                ps.setInt(7, ebook.getUploaderId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            
            ps.setString(8, ebook.getCoverUrl());
            ps.setInt(9, ebook.getViewCount());
            ps.setString(10, ebook.getFileName());              // Normalized filename
            ps.setString(11, ebook.getOriginalFileName());      // Original filename from user
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
            throw new SQLException("Failed to insert ebook, no ID generated");
        }
    }
    
    public List<Ebook> selectAllEbooks() throws SQLException {
        List<Ebook> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapEbook(rs));
            }
        }
        return list;
    }
    
    public Ebook selectEbook(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapEbook(rs);
            }
        }
        return null;
    }
    
    public List<Ebook> search(String searchTitle) throws SQLException {
        List<Ebook> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SEARCH_BY_TITLE)) {
            
            ps.setString(1, "%" + searchTitle + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapEbook(rs));
            }
        }
        return list;
    }
    
    public List<Ebook> selectEbooksByUploader(int uploaderId) throws SQLException {
        List<Ebook> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_UPLOADER)) {
            
            ps.setInt(1, uploaderId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapEbook(rs));
            }
        }
        return list;
    }
    
    public List<Ebook> selectEbooksByStatus(String status) throws SQLException {
        List<Ebook> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_STATUS)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapEbook(rs));
            }
        }
        return list;
    }
    
    public List<Ebook> selectEbooksByVisibility(String visibility) throws SQLException {
        List<Ebook> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_VISIBILITY)) {
            
            ps.setString(1, visibility);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapEbook(rs));
            }
        }
        return list;
    }
    
    public boolean updateEbook(Ebook ebook) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            
            ps.setString(1, ebook.getTitle());
            ps.setString(2, ebook.getDescription());
            ps.setString(3, ebook.getReleaseType());
            ps.setString(4, ebook.getLanguage());
            ps.setString(5, ebook.getStatus());
            ps.setString(6, ebook.getVisibility());
            
            if (ebook.getUploaderId() != null) {
                ps.setInt(7, ebook.getUploaderId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }
            
            ps.setString(8, ebook.getCoverUrl());
            ps.setInt(9, ebook.getViewCount());
            ps.setString(10, ebook.getFileName());              // Normalized filename
            ps.setString(11, ebook.getOriginalFileName());      // Original filename from user
            ps.setInt(12, ebook.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteEbook(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean incrementViewCount(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INCREMENT_VIEW)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    private Ebook mapEbook(ResultSet rs) throws SQLException {
        Ebook ebook = new Ebook(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("release_type"),
            rs.getString("language"),
            rs.getString("status"),
            rs.getString("visibility"),
            (rs.getObject("uploader_id") != null) ? rs.getInt("uploader_id") : null,
            (rs.getDate("created_at") != null) ? rs.getDate("created_at").toLocalDate() : null,
            rs.getInt("view_count"),
            rs.getString("cover_url")
        );
        
        ebook.setFileName(rs.getString("file_name"));
        
        // Map original_file_name safely (backward compatibility)
        try {
            ebook.setOriginalFileName(rs.getString("original_file_name"));
        } catch (SQLException e) {
            // Bỏ qua nếu column original_file_name không tồn tại
            ebook.setOriginalFileName(null);
        }
        
        try {
            ebook.setSummary(rs.getString("summary"));
        } catch (SQLException e) {
            ebook.setSummary(null);
        }
        
        return ebook;
    }
    
    public static String normalizeFileName(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "untitled.txt";
        }
        
        String result = input.toLowerCase();
        
        result = result.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
                      .replaceAll("[èéẹẻẽêềếệểễ]", "e")
                      .replaceAll("[ìíịỉĩ]", "i")
                      .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
                      .replaceAll("[ùúụủũưừứựửữ]", "u")
                      .replaceAll("[ỳýỵỷỹ]", "y")
                      .replaceAll("[đ]", "d");
        
        result = result.replaceAll("[–—]", "-")
                      .replaceAll("\\s+", "")
                      .replaceAll("[^a-z0-9-]", "")
                      .replaceAll("-+", "-");
        
        result = result.replaceAll("^-+|-+$", "");
        
        if (!result.endsWith(".txt")) {
            result += ".txt";
        }
        
        return result;
    }
}