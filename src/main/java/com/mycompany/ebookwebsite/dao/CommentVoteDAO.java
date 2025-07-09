package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.CommentVote;
import java.sql.*;
import java.util.Optional;

public class CommentVoteDAO {
    // Sử dụng DBConnection như các DAO khác để đảm bảo nhất quán

    public Optional<CommentVote> getVote(int userId, int commentId) {
        String sql = "SELECT * FROM CommentVotes WHERE user_id = ? AND comment_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
        System.out.println("[DEBUG DAO] upsertVote called: userId=" + userId + ", commentId=" + commentId + ", value=" + value);
        String updateSql = "UPDATE CommentVotes SET value = ? WHERE user_id = ? AND comment_id = ?";
        String insertSql = "INSERT INTO CommentVotes (user_id, comment_id, value) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("[DEBUG DAO] Database connection obtained");
            
            // Try UPDATE first
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, value);
                ps.setInt(2, userId);
                ps.setInt(3, commentId);
                int affected = ps.executeUpdate();
                System.out.println("[DEBUG DAO] UPDATE affected " + affected + " rows");
                if (affected > 0) {
                    System.out.println("[DEBUG DAO] upsertVote UPDATE successful");
                    return true;
                }
            }
            
            // If UPDATE failed, try INSERT
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, userId);
                ps.setInt(2, commentId);
                ps.setInt(3, value);
                int inserted = ps.executeUpdate();
                System.out.println("[DEBUG DAO] INSERT affected " + inserted + " rows");
                boolean success = inserted > 0;
                System.out.println("[DEBUG DAO] upsertVote INSERT " + (success ? "successful" : "failed"));
                return success;
            }
        } catch (SQLException e) {
            System.out.println("[ERROR DAO] upsertVote SQLException: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteVote(int userId, int commentId) {
        String sql = "DELETE FROM CommentVotes WHERE user_id = ? AND comment_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, commentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countVotes(int commentId, int value) {
        String sql = "SELECT COUNT(*) FROM CommentVotes WHERE comment_id = ? AND value = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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