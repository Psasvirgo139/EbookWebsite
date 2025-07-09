package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.CommentDeleteInfo;
import java.sql.*;
import java.util.Optional;

public class CommentDeleteInfoDAO {
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=EbookWebsite", "sa", "123456");
    }

    public boolean insertDeleteInfo(CommentDeleteInfo info) {
        String sql = "INSERT INTO CommentDeleteInfo (commentId, deletedBy, deletedAt) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, info.getCommentId());
            ps.setInt(2, info.getDeletedBy());
            ps.setTimestamp(3, Timestamp.valueOf(info.getDeletedAt()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<CommentDeleteInfo> getDeleteInfo(int commentId) {
        String sql = "SELECT * FROM CommentDeleteInfo WHERE commentId = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CommentDeleteInfo info = new CommentDeleteInfo(
                    rs.getInt("id"),
                    rs.getInt("commentId"),
                    rs.getInt("deletedBy"),
                    rs.getTimestamp("deletedAt").toLocalDateTime()
                );
                return Optional.of(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean isDeleted(int commentId) {
        String sql = "SELECT COUNT(*) FROM CommentDeleteInfo WHERE commentId = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
} 