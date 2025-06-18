package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Volume;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VolumeDAO {
    
    private static final String INSERT = "INSERT INTO Volumes (ebook_id, title, number, published_at, access_level, view_count, like_count) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Volumes ORDER BY ebook_id, number";
    private static final String SELECT_BY_ID = "SELECT * FROM Volumes WHERE id = ?";
    private static final String SELECT_BY_EBOOK = "SELECT * FROM Volumes WHERE ebook_id = ? ORDER BY number";
    private static final String SEARCH_BY_TITLE = "SELECT * FROM Volumes WHERE title LIKE ? ORDER BY ebook_id, number";
    private static final String SELECT_BY_ACCESS_LEVEL = "SELECT * FROM Volumes WHERE access_level = ? ORDER BY ebook_id, number";
    private static final String UPDATE = "UPDATE Volumes SET ebook_id = ?, title = ?, number = ?, published_at = ?, access_level = ?, view_count = ?, like_count = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM Volumes WHERE id = ?";
    private static final String INCREMENT_VIEW = "UPDATE Volumes SET view_count = view_count + 1 WHERE id = ?";
    private static final String INCREMENT_LIKE = "UPDATE Volumes SET like_count = like_count + 1 WHERE id = ?";
    private static final String DECREMENT_LIKE = "UPDATE Volumes SET like_count = like_count - 1 WHERE id = ? AND like_count > 0";
    
    public void insertVolume(Volume volume) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT)) {
            
            ps.setInt(1, volume.getEbookId());
            ps.setString(2, volume.getTitle());
            ps.setInt(3, volume.getNumber());
            
            if (volume.getPublishedAt() != null) {
                ps.setDate(4, Date.valueOf(volume.getPublishedAt()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            
            ps.setString(5, volume.getAccessLevel());
            ps.setInt(6, volume.getViewCount());
            ps.setInt(7, volume.getLikeCount());
            
            ps.executeUpdate();
        }
    }
    
    public List<Volume> selectAllVolumes() throws SQLException {
        List<Volume> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapVolume(rs));
            }
        }
        return list;
    }
    
    public Volume selectVolume(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapVolume(rs);
            }
        }
        return null;
    }
    
    public List<Volume> selectVolumesByEbook(int ebookId) throws SQLException {
        List<Volume> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_EBOOK)) {
            
            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapVolume(rs));
            }
        }
        return list;
    }
    
    public List<Volume> search(String searchTitle) throws SQLException {
        List<Volume> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SEARCH_BY_TITLE)) {
            
            ps.setString(1, "%" + searchTitle + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapVolume(rs));
            }
        }
        return list;
    }
    
    public List<Volume> selectVolumesByAccessLevel(String accessLevel) throws SQLException {
        List<Volume> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ACCESS_LEVEL)) {
            
            ps.setString(1, accessLevel);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapVolume(rs));
            }
        }
        return list;
    }
    
    public boolean updateVolume(Volume volume) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            
            ps.setInt(1, volume.getEbookId());
            ps.setString(2, volume.getTitle());
            ps.setInt(3, volume.getNumber());
            
            if (volume.getPublishedAt() != null) {
                ps.setDate(4, Date.valueOf(volume.getPublishedAt()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            
            ps.setString(5, volume.getAccessLevel());
            ps.setInt(6, volume.getViewCount());
            ps.setInt(7, volume.getLikeCount());
            ps.setInt(8, volume.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteVolume(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean incrementViewCount(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INCREMENT_VIEW)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean incrementLikeCount(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INCREMENT_LIKE)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean decrementLikeCount(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DECREMENT_LIKE)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    private Volume mapVolume(ResultSet rs) throws SQLException {
        return new Volume(
            rs.getInt("id"),
            rs.getInt("ebook_id"),
            rs.getString("title"),
            rs.getInt("number"),
            (rs.getDate("published_at") != null) ? rs.getDate("published_at").toLocalDate() : null,
            rs.getString("access_level"),
            rs.getInt("view_count"),
            rs.getInt("like_count")
        );
    }
}