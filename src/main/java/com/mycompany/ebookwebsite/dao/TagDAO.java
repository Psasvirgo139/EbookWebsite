package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Tag;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDAO {
    
    private static final String INSERT = "INSERT INTO Tags (name) VALUES (?)";
    private static final String SELECT_ALL = "SELECT * FROM Tags";
    private static final String SELECT_BY_ID = "SELECT * FROM Tags WHERE id = ?";
    private static final String SEARCH_BY_NAME = "SELECT * FROM Tags WHERE name LIKE ?";
    private static final String UPDATE = "UPDATE Tags SET name = ? WHERE id = ?";
    private static final String DELETE = "UPDATE Tags SET status = 'deleted' WHERE id = ?";
    private static final String SELECT_ACTIVE = "SELECT * FROM Tags WHERE status != 'deleted' OR status IS NULL";

    public void insertTag(Tag tag) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, tag.getName());
            ps.executeUpdate();
            
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                tag.setId(generatedKeys.getInt(1));
            }
        }
    }

    public List<Tag> search(String searchName) throws SQLException {
        List<Tag> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SEARCH_BY_NAME)) {
            
            ps.setString(1, "%" + searchName + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapTag(rs));
            }
        }
        return list;
    }

    public Tag selectTag(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapTag(rs);
            }
        }
        return null;
    }

    public List<Tag> selectAllTags() throws SQLException {
        List<Tag> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapTag(rs));
            }
        }
        return list;
    }

    public List<Tag> selectActiveTags() throws SQLException {
        List<Tag> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ACTIVE)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapTag(rs));
            }
        }
        return list;
    }

    public boolean deleteTag(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateTag(Tag tag) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            
            ps.setString(1, tag.getName());
            ps.setInt(2, tag.getId());
            return ps.executeUpdate() > 0;
        }
    }

    private Tag mapTag(ResultSet rs) throws SQLException {
        return new Tag(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}