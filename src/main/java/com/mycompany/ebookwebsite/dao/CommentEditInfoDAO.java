package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.CommentEditInfo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentEditInfoDAO {
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=EbookWebsite", "sa", "123456");
    }

    public boolean insertEditInfo(CommentEditInfo info) {
        String sql = "INSERT INTO CommentEditInfo (commentId, editedBy, editedAt, oldContent) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, info.getCommentId());
            ps.setInt(2, info.getEditedBy());
            ps.setTimestamp(3, Timestamp.valueOf(info.getEditedAt()));
            ps.setString(4, info.getOldContent());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CommentEditInfo> getEditHistory(int commentId) {
        List<CommentEditInfo> list = new ArrayList<>();
        String sql = "SELECT * FROM CommentEditInfo WHERE commentId = ? ORDER BY editedAt DESC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CommentEditInfo info = new CommentEditInfo(
                    rs.getInt("id"),
                    rs.getInt("commentId"),
                    rs.getInt("editedBy"),
                    rs.getTimestamp("editedAt").toLocalDateTime(),
                    rs.getString("oldContent")
                );
                list.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
} 