package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.ReportedCommentView;
import java.sql.*;
import java.util.*;

public class CommentReportDAO {
    public void insertReport(int commentId, int reporterId, String reason) throws SQLException {
        String sql = "INSERT INTO CommentReports (comment_id, reporter_id, reason, reported_at, status) VALUES (?, ?, ?, GETDATE(), 'pending')";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, commentId);
            ps.setInt(2, reporterId);
            ps.setString(3, reason);
            ps.executeUpdate();
        }
    }

    public List<ReportedCommentView> getAllReports() throws SQLException {
        String sql = "SELECT cr.id, cr.comment_id, cr.reporter_id, u1.username AS reporter_username, cr.reported_at, cr.reason, cr.status, c.content AS comment_content, c.user_id AS comment_user_id, u2.username AS comment_username " +
                "FROM CommentReports cr " +
                "JOIN Comments c ON cr.comment_id = c.id " +
                "JOIN Users u1 ON cr.reporter_id = u1.id " +
                "JOIN Users u2 ON c.user_id = u2.id " +
                "ORDER BY cr.reported_at DESC";
        List<ReportedCommentView> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ReportedCommentView(
                    rs.getInt("id"),
                    rs.getInt("comment_id"),
                    rs.getInt("reporter_id"),
                    rs.getString("reporter_username"),
                    rs.getTimestamp("reported_at").toLocalDateTime(),
                    rs.getString("reason"),
                    rs.getString("status"),
                    rs.getString("comment_content"),
                    rs.getInt("comment_user_id"),
                    rs.getString("comment_username")
                ));
            }
        }
        return list;
    }

    public void updateStatus(int reportId, String status) throws SQLException {
        String sql = "UPDATE CommentReports SET status = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, reportId);
            ps.executeUpdate();
        }
    }
} 