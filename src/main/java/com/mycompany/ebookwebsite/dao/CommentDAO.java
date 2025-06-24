package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    public List<Comment> getCommentsByEbookId(int ebookId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM Comments WHERE ebook_id = ? ORDER BY created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                comments.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // DAO không throw
        }
        return comments;
    }

    public void insertComment(Comment comment) {
        String sql = "INSERT INTO Comments (user_id, ebook_id, chapter_id, content, created_at) VALUES (?, ?, ?, ?, GETDATE())";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, comment.getUserID());
            ps.setInt(2, comment.getEbookID());
            if (comment.getChapterID() != null) {
                ps.setInt(3, comment.getChapterID());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, comment.getContent());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Không throw từ DAO
        }
    }

    public void deleteComment(int commentId) {
        String sql = "DELETE FROM Comments WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, commentId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Không throw
        }
    }
    
    public List<Comment> getCommentsByChapter(int ebookId, int chapterId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM Comments WHERE ebook_id = ? AND chapter_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ps.setInt(2, chapterId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                comments.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public void deleteCommentByIdAndUser(int commentId, int userId) {
        String sql = "DELETE FROM Comments WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, commentId);
            ps.setInt(2, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Comment mapRow(ResultSet rs) throws SQLException {
        Comment c = new Comment();
        c.setId(rs.getInt("id"));
        c.setUserID(rs.getInt("user_id"));
        c.setEbookID(rs.getInt("ebook_id"));
        int chapterId = rs.getInt("chapter_id");
        c.setChapterID(rs.wasNull() ? null : chapterId);
        c.setContent(rs.getString("content"));
        c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        c.setParentCommentID(rs.getInt("parent_comment_id"));
        if (rs.wasNull()) c.setParentCommentID(null);
        c.setLikeCount(rs.getInt("like_count"));
        return c;
    }
}
