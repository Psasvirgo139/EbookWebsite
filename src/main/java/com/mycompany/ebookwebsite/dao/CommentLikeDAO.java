package com.mycompany.ebookwebsite.dao;

import java.sql.*;

public class CommentLikeDAO {
    private static final String INSERT = "INSERT INTO CommentLikes (user_id, comment_id, liked_at) VALUES (?,?,GETDATE())";
    private static final String DELETE = "DELETE FROM CommentLikes WHERE user_id=? AND comment_id=?";
    private static final String CHECK = "SELECT COUNT(*) FROM CommentLikes WHERE user_id=? AND comment_id=?";

    public boolean like(int userId, int commentId) throws SQLException {
        try(Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(INSERT)){
            ps.setInt(1,userId); ps.setInt(2,commentId);
            return ps.executeUpdate()>0;
        }
    }
    public boolean unlike(int userId,int commentId) throws SQLException {
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(DELETE)){
            ps.setInt(1,userId);ps.setInt(2,commentId);
            return ps.executeUpdate()>0;
        }
    }
    public boolean isLiked(int userId,int commentId) throws SQLException{
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(CHECK)){
            ps.setInt(1,userId);ps.setInt(2,commentId);
            ResultSet rs=ps.executeQuery();
            return rs.next() && rs.getInt(1)>0;
        }
    }
} 