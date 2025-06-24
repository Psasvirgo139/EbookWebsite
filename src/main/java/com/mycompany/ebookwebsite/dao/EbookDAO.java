package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Ebook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EbookDAO {

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
        List<Ebook> ebooks = new ArrayList<>();
        String sql = "SELECT * FROM Ebooks ORDER BY id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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
                ebooks.add(ebook);
            }
        }

        return ebooks;
    }

}
