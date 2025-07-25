package com.mycompany.ebookwebsite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.ebookwebsite.model.Comment;

public class CommentDAO {

    private static final String SELECT_BY_EBOOK = "SELECT * FROM Comments WHERE ebook_id = ? AND chapter_id IS NULL ORDER BY created_at DESC";
    private static final String INSERT = "INSERT INTO Comments (user_id, ebook_id, chapter_id, content, created_at) VALUES (?, ?, ?, ?, GETDATE())";
    private static final String DELETE = "DELETE FROM Comments WHERE id = ?";
    private static final String SELECT_BY_CHAPTER = "SELECT * FROM Comments WHERE ebook_id = ? AND chapter_id = ? ORDER BY created_at DESC";
    private static final String DELETE_BY_ID_AND_USER = "DELETE FROM Comments WHERE id = ? AND user_id = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM Comments WHERE id = ?";


    public List<Comment> getCommentsByEbookId(int ebookId) {
        List<Comment> comments = new ArrayList<>();
        String sql = SELECT_BY_EBOOK;
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
        String sql = INSERT;
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
        String sql = DELETE;
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
        String sql = SELECT_BY_CHAPTER;
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
        String sql = DELETE_BY_ID_AND_USER;
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
        String sql = SELECT_BY_ID;
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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

    // === New methods for book detail comments ===
    public List<Comment> getBookComments(int ebookId) throws SQLException {
        String sql = "SELECT * FROM Comments WHERE ebook_id=? AND chapter_id IS NULL AND parent_comment_id IS NULL ORDER BY created_at DESC";
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1,ebookId);
            ResultSet rs=ps.executeQuery();
            java.util.List<Comment> list=new java.util.ArrayList<>();
            while(rs.next()) {
                Comment c = mapRow(rs);
                list.add(c);
            }
            return list;
        }
    }
    public List<Comment> getReplies(int parentId) throws SQLException {
        String sql="SELECT * FROM Comments WHERE parent_comment_id=? ORDER BY created_at";
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1,parentId);
            ResultSet rs=ps.executeQuery();
            java.util.List<Comment> list=new java.util.ArrayList<>();
            while(rs.next()) {
                Comment c = mapRow(rs);
                list.add(c);
            }
            return list;
        }
    }
    public List<Comment> getTopChapterComments(int ebookId,int limit) throws SQLException {
        String sql="SELECT TOP("+limit+") * FROM Comments WHERE ebook_id=? AND chapter_id IS NOT NULL AND parent_comment_id IS NULL ORDER BY like_count DESC";
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1,ebookId);
            ResultSet rs=ps.executeQuery();
            java.util.List<Comment> list=new java.util.ArrayList<>();
            while(rs.next()) {
                Comment c = mapRow(rs);
                list.add(c);
            }
            return list;
        }
    }
    public void insertReply(Comment c) throws SQLException {
        String sql = "INSERT INTO Comments (user_id, ebook_id, chapter_id, parent_comment_id, content, created_at, like_count) VALUES (?,?,?,?,?,?,0)";
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1,c.getUserID());
            ps.setInt(2,c.getEbookID());
            if (c.getChapterID() != null) {
                ps.setInt(3, c.getChapterID());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setInt(4,c.getParentCommentID());
            ps.setString(5,c.getContent());
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.executeUpdate();
        }
    }

    public void updateCommentContent(int commentId, String content) throws SQLException {
        String sql = "UPDATE Comments SET content = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, content);
            ps.setInt(2, commentId);
            ps.executeUpdate();
        }
    }

    public List<Comment> getCommentsByBook(int ebookId) {
        List<Comment> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM Comments WHERE ebook_id = ? AND (chapter_id IS NULL) ORDER BY created_at ASC";
        try (java.sql.Connection con = DBConnection.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ebookId);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Đếm tổng số comment
    public int countAllComments() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Comments";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public java.util.List<Comment> findAll() throws java.sql.SQLException {
        String sql = "SELECT * FROM Comments ORDER BY created_at DESC";
        java.util.List<Comment> list = new java.util.ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }
}
