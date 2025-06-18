/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Comment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class CommentDAO {
    
    private static final String INSERT = "INSERT INTO Comments (user_id, ebook_id, chapter_id, content, parent_comment_id, like_count) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Comments WHERE status != 'deleted'";
    private static final String SELECT_BY_ID = "SELECT * FROM Comments WHERE id = ? AND status != 'deleted'";
    private static final String SELECT_BY_EBOOK = "SELECT * FROM Comments WHERE ebook_id = ? AND status != 'deleted' ORDER BY created_at DESC";
    private static final String SELECT_BY_CHAPTER = "SELECT * FROM Comments WHERE chapter_id = ? AND status != 'deleted' ORDER BY created_at DESC";
    private static final String SELECT_BY_USER = "SELECT * FROM Comments WHERE user_id = ? AND status != 'deleted' ORDER BY created_at DESC";
    private static final String SELECT_REPLIES = "SELECT * FROM Comments WHERE parent_comment_id = ? AND status != 'deleted' ORDER BY created_at ASC";
    private static final String UPDATE = "UPDATE Comments SET content = ?, like_count = ? WHERE id = ?";
    private static final String DELETE = "UPDATE Comments SET status = 'deleted' WHERE id = ?";
    private static final String UPDATE_LIKE_COUNT = "UPDATE Comments SET like_count = ? WHERE id = ?";
    
    public void insertComment(Comment comment) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT)) {
            
            ps.setInt(1, comment.getUserID());
            ps.setInt(2, comment.getEbookID());
            
            if (comment.getChapterID() != null) {
                ps.setInt(3, comment.getChapterID());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            ps.setString(4, comment.getContent());
            
            if (comment.getParentCommentID() != null) {
                ps.setInt(5, comment.getParentCommentID());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            
            ps.setInt(6, comment.getLikeCount());
            ps.executeUpdate();
        }
    }
    
    public Comment selectComment(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapComment(rs);
            }
        }
        return null;
    }
    
    public List<Comment> selectAllComments() throws SQLException {
        List<Comment> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapComment(rs));
            }
        }
        return list;
    }
    
    public List<Comment> getCommentsByEbook(int ebookId) throws SQLException {
        List<Comment> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_EBOOK)) {
            
            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapComment(rs));
            }
        }
        return list;
    }
    
    public List<Comment> getCommentsByChapter(int chapterId) throws SQLException {
        List<Comment> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_CHAPTER)) {
            
            ps.setInt(1, chapterId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapComment(rs));
            }
        }
        return list;
    }
    
    public List<Comment> getCommentsByUser(int userId) throws SQLException {
        List<Comment> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapComment(rs));
            }
        }
        return list;
    }
    
    public List<Comment> getReplies(int parentCommentId) throws SQLException {
        List<Comment> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_REPLIES)) {
            
            ps.setInt(1, parentCommentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapComment(rs));
            }
        }
        return list;
    }
    
    public boolean updateComment(Comment comment) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            
            ps.setString(1, comment.getContent());
            ps.setInt(2, comment.getLikeCount());
            ps.setInt(3, comment.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean updateLikeCount(int commentId, int likeCount) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE_LIKE_COUNT)) {
            
            ps.setInt(1, likeCount);
            ps.setInt(2, commentId);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteComment(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    private Comment mapComment(ResultSet rs) throws SQLException {
        return new Comment(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("ebook_id"),
                (rs.getObject("chapter_id") != null) ? rs.getInt("chapter_id") : null,
                rs.getString("content"),
                (rs.getDate("created_at") != null) ? rs.getDate("created_at").toLocalDate() : null,
                (rs.getObject("parent_comment_id") != null) ? rs.getInt("parent_comment_id") : null,
                rs.getInt("like_count")
        );
    }
}