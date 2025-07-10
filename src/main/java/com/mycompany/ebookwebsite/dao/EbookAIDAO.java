package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.EbookAI;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho AI-related data của Ebook
 * Tách riêng khỏi EbookDAO để tuân thủ Single Responsibility Principle
 * 
 * @author ADMIN
 */
public class EbookAIDAO {

    private static final String INSERT = 
        "INSERT INTO EbookAI (ebook_id, file_name, original_file_name, summary, created_at, updated_at, status) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_EBOOK_ID = 
        "SELECT * FROM EbookAI WHERE ebook_id = ?";
    
    private static final String SELECT_BY_ID = 
        "SELECT * FROM EbookAI WHERE id = ?";
    
    private static final String UPDATE = 
        "UPDATE EbookAI SET file_name=?, original_file_name=?, summary=?, updated_at=?, status=? WHERE ebook_id=?";
    
    private static final String UPDATE_SUMMARY = 
        "UPDATE EbookAI SET summary=?, updated_at=? WHERE ebook_id=?";
    
    private static final String UPDATE_FILE_INFO = 
        "UPDATE EbookAI SET file_name=?, original_file_name=?, updated_at=? WHERE ebook_id=?";
    
    private static final String DELETE_BY_EBOOK_ID = 
        "DELETE FROM EbookAI WHERE ebook_id = ?";
    
    private static final String UPSERT = 
        "MERGE EbookAI AS target " +
        "USING (VALUES (?, ?, ?, ?, ?, ?, ?)) AS source (ebook_id, file_name, original_file_name, summary, created_at, updated_at, status) " +
        "ON target.ebook_id = source.ebook_id " +
        "WHEN MATCHED THEN " +
        "    UPDATE SET file_name = source.file_name, original_file_name = source.original_file_name, " +
        "               summary = source.summary, updated_at = source.updated_at, status = source.status " +
        "WHEN NOT MATCHED THEN " +
        "    INSERT (ebook_id, file_name, original_file_name, summary, created_at, updated_at, status) " +
        "    VALUES (source.ebook_id, source.file_name, source.original_file_name, source.summary, source.created_at, source.updated_at, source.status);";

    /**
     * Tạo hoặc cập nhật AI data cho ebook
     */
    public void upsertEbookAI(EbookAI ebookAI) throws SQLException {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(UPSERT)) {
            
            ps.setInt(1, ebookAI.getEbookId());
            ps.setString(2, ebookAI.getFileName());
            ps.setString(3, ebookAI.getOriginalFileName());
            ps.setString(4, ebookAI.getSummary());
            ps.setTimestamp(5, Timestamp.valueOf(ebookAI.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(ebookAI.getUpdatedAt()));
            ps.setString(7, ebookAI.getStatus());
            
            ps.executeUpdate();
        }
    }

    /**
     * Lấy AI data theo ebook ID
     */
    public EbookAI getByEbookId(int ebookId) throws SQLException {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_EBOOK_ID)) {
            
            ps.setInt(1, ebookId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Lấy AI data theo ID
     */
    public EbookAI getById(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Cập nhật summary cho ebook
     */
    public boolean updateSummary(int ebookId, String summary) throws SQLException {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(UPDATE_SUMMARY)) {
            
            ps.setString(1, summary);
            ps.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setInt(3, ebookId);
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin file cho ebook
     */
    public boolean updateFileInfo(int ebookId, String fileName, String originalFileName) throws SQLException {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(UPDATE_FILE_INFO)) {
            
            ps.setString(1, fileName);
            ps.setString(2, originalFileName);
            ps.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.setInt(4, ebookId);
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa AI data theo ebook ID
     */
    public boolean deleteByEbookId(int ebookId) throws SQLException {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(DELETE_BY_EBOOK_ID)) {
            
            ps.setInt(1, ebookId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Map ResultSet to EbookAI object
     */
    private EbookAI mapRow(ResultSet rs) throws SQLException {
        EbookAI ebookAI = new EbookAI();
        ebookAI.setId(rs.getInt("id"));
        ebookAI.setEbookId(rs.getInt("ebook_id"));
        ebookAI.setFileName(rs.getString("file_name"));
        ebookAI.setOriginalFileName(rs.getString("original_file_name"));
        ebookAI.setSummary(rs.getString("summary"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            ebookAI.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            ebookAI.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        ebookAI.setStatus(rs.getString("status"));
        
        return ebookAI;
    }

    /**
     * Tạo AI data mới cho ebook (nếu chưa có)
     */
    public void insertEbookAI(EbookAI ebookAI) throws SQLException {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(INSERT)) {
            
            ps.setInt(1, ebookAI.getEbookId());
            ps.setString(2, ebookAI.getFileName());
            ps.setString(3, ebookAI.getOriginalFileName());
            ps.setString(4, ebookAI.getSummary());
            ps.setTimestamp(5, Timestamp.valueOf(ebookAI.getCreatedAt()));
            ps.setTimestamp(6, Timestamp.valueOf(ebookAI.getUpdatedAt()));
            ps.setString(7, ebookAI.getStatus());
            
            ps.executeUpdate();
        }
    }

    /**
     * Cập nhật toàn bộ AI data
     */
    public boolean updateEbookAI(EbookAI ebookAI) throws SQLException {
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            
            ps.setString(1, ebookAI.getFileName());
            ps.setString(2, ebookAI.getOriginalFileName());
            ps.setString(3, ebookAI.getSummary());
            ps.setTimestamp(4, Timestamp.valueOf(ebookAI.getUpdatedAt()));
            ps.setString(5, ebookAI.getStatus());
            ps.setInt(6, ebookAI.getEbookId());
            
            return ps.executeUpdate() > 0;
        }
    }
} 