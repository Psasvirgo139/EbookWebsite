package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.UserRead;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserReadDAO {
    
    private static final String INSERT_OR_UPDATE = "MERGE UserRead AS target " +
            "USING (VALUES (?, ?, ?, ?)) AS source (user_id, ebook_id, last_read_chapter_id, last_read_at) " +
            "ON target.user_id = source.user_id AND target.ebook_id = source.ebook_id " +
            "WHEN MATCHED THEN UPDATE SET last_read_chapter_id = source.last_read_chapter_id, last_read_at = source.last_read_at " +
            "WHEN NOT MATCHED THEN INSERT (user_id, ebook_id, last_read_chapter_id, last_read_at) " +
            "VALUES (source.user_id, source.ebook_id, source.last_read_chapter_id, source.last_read_at);";
    
    private static final String SELECT_BY_USER_EBOOK = "SELECT * FROM UserRead WHERE user_id = ? AND ebook_id = ?";
    private static final String SELECT_BY_USER = "SELECT * FROM UserRead WHERE user_id = ?";
    private static final String SELECT_BY_EBOOK = "SELECT * FROM UserRead WHERE ebook_id = ?";
    private static final String SELECT_ALL = "SELECT * FROM UserRead";
    private static final String DELETE = "DELETE FROM UserRead WHERE user_id = ? AND ebook_id = ?";
    private static final String DELETE_BY_USER = "DELETE FROM UserRead WHERE user_id = ?";
    private static final String DELETE_BY_EBOOK = "DELETE FROM UserRead WHERE ebook_id = ?";

    public void insertOrUpdateUserRead(UserRead userRead) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT_OR_UPDATE)) {
            
            ps.setInt(1, userRead.getUserID());
            ps.setInt(2, userRead.getEbookID());
            
            if (userRead.getLastReadChapterID() != null) {
                ps.setInt(3, userRead.getLastReadChapterID());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            
            if (userRead.getLastReadAt() != null) {
                ps.setDate(4, Date.valueOf(userRead.getLastReadAt()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            
            ps.executeUpdate();
        }
    }

    public UserRead selectUserRead(int userID, int ebookID) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER_EBOOK)) {
            
            ps.setInt(1, userID);
            ps.setInt(2, ebookID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapUserRead(rs);
            }
        }
        return null;
    }

    public List<UserRead> selectByUser(int userID) throws SQLException {
        List<UserRead> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER)) {
            
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapUserRead(rs));
            }
        }
        return list;
    }

    public List<UserRead> selectByEbook(int ebookID) throws SQLException {
        List<UserRead> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_EBOOK)) {
            
            ps.setInt(1, ebookID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapUserRead(rs));
            }
        }
        return list;
    }

    public List<UserRead> selectAllUserRead() throws SQLException {
        List<UserRead> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapUserRead(rs));
            }
        }
        return list;
    }

    public boolean deleteUserRead(int userID, int ebookID) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            
            ps.setInt(1, userID);
            ps.setInt(2, ebookID);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteByUser(int userID) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE_BY_USER)) {
            
            ps.setInt(1, userID);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteByEbook(int ebookID) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE_BY_EBOOK)) {
            
            ps.setInt(1, ebookID);
            return ps.executeUpdate() > 0;
        }
    }

    private UserRead mapUserRead(ResultSet rs) throws SQLException {
        return new UserRead(
                rs.getInt("user_id"),
                rs.getInt("ebook_id"),
                (rs.getObject("last_read_chapter_id") != null) ? rs.getInt("last_read_chapter_id") : null,
                (rs.getDate("last_read_at") != null) ? rs.getDate("last_read_at").toLocalDate() : null
        );
    }
}