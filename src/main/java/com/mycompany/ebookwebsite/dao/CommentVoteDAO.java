package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.CommentVote;
import java.sql.*;
import java.util.Optional;

public class CommentVoteDAO {
    private Connection getConnection() throws SQLException {
        // Sử dụng DataSource hoặc DriverManager tuỳ dự án
        return DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=EbookWebsite", "sa", "123456");
    }

    public Optional<CommentVote> getVote(int userId, int commentId) {
        System.out.println("[DEBUG] getVote called: userId=" + userId + ", commentId=" + commentId);
        String sql = "SELECT * FROM CommentVotes WHERE user_id = ? AND comment_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, commentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new CommentVote(
                    rs.getInt("user_id"),
                    rs.getInt("comment_id"),
                    rs.getInt("value")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean upsertVote(int userId, int commentId, int value) {
        System.out.println("[DEBUG] upsertVote called: userId=" + userId + ", commentId=" + commentId + ", value=" + value);
        String updateSql = "UPDATE CommentVotes SET value = ? WHERE user_id = ? AND comment_id = ?";
        String insertSql = "INSERT INTO CommentVotes (user_id, comment_id, value) VALUES (?, ?, ?)";
        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, value);
                ps.setInt(2, userId);
                ps.setInt(3, commentId);
                int affected = ps.executeUpdate();
                if (affected > 0) return true;
            }
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, userId);
                ps.setInt(2, commentId);
                ps.setInt(3, value);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteVote(int userId, int commentId) {
        System.out.println("[DEBUG] deleteVote called: userId=" + userId + ", commentId=" + commentId);
        String sql = "DELETE FROM CommentVotes WHERE user_id = ? AND comment_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, commentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countVotes(int commentId, int value) {
        System.out.println("[DEBUG] countVotes called: commentId=" + commentId + ", value=" + value);
        String sql = "SELECT COUNT(*) FROM CommentVotes WHERE comment_id = ? AND value = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commentId);
            ps.setInt(2, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
} 