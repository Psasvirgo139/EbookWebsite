package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    public List<Comment> getCommentsByEbookId(int ebookId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.*, u.username FROM Comments c " +
                    "JOIN Users u ON c.user_id = u.id " +
                    "WHERE c.ebook_id = ? ORDER BY c.created_at DESC";
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
    
    public List<Comment> getCommentsByEbookIdWithPagination(int ebookId, int offset, int pageSize) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.*, u.username FROM Comments c " +
                    "JOIN Users u ON c.user_id = u.id " +
                    "WHERE c.ebook_id = ? ORDER BY c.created_at DESC " +
                    "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                comments.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
    
    public int getCommentCountByEbookId(int ebookId) {
        String sql = "SELECT COUNT(*) FROM Comments WHERE ebook_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void insertComment(Comment comment) {
        String sql = "INSERT INTO Comments (user_id, ebook_id, chapter_id, content, created_at) VALUES (?, ?, ?, ?, ?)";
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
            ps.setTimestamp(5, Timestamp.valueOf(comment.getCreatedAt()));

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
        String sql = "SELECT c.*, u.username FROM Comments c " +
                    "JOIN Users u ON c.user_id = u.id " +
                    "WHERE c.ebook_id = ? AND c.chapter_id = ? ORDER BY c.created_at DESC";
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
    
    public Comment getCommentById(int commentId) {
        String sql = "SELECT c.*, u.username FROM Comments c " +
                    "JOIN Users u ON c.user_id = u.id " +
                    "WHERE c.id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, commentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean canUserDeleteComment(int commentId, int userId) {
        String sql = "SELECT COUNT(*) FROM Comments WHERE id = ? AND user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, commentId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Comment> getRecentComments(int limit) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.*, u.username, e.title as book_title FROM Comments c " +
                    "JOIN Users u ON c.user_id = u.id " +
                    "JOIN Ebooks e ON c.ebook_id = e.id " +
                    "ORDER BY c.created_at DESC " +
                    "OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                comments.add(mapRowWithBookTitle(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public int getTotalComments() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Comments";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getApprovedCommentsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Comments WHERE status = 'approved'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getPendingCommentsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Comments WHERE status = 'pending'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
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

    private Comment mapRowWithBookTitle(ResultSet rs) throws SQLException {
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
