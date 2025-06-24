package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Tag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDAO {

    public List<Tag> getAllTags() throws SQLException {
        String sql = "SELECT * FROM Tags";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Tag> tags = new ArrayList<>();
            while (rs.next()) {
                Tag tag = new Tag();
                tag.setId(rs.getInt("id"));
                tag.setName(rs.getString("name"));
                tags.add(tag);
            }
            return tags;
        }
    }

    public Tag getTagById(int id) throws SQLException {
        String sql = "SELECT * FROM Tags WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Tag tag = new Tag();
                    tag.setId(rs.getInt("id"));
                    tag.setName(rs.getString("name"));
                    return tag;
                }
            }
        }
        return null;
    }

    public void insertTag(Tag tag) throws SQLException {
        String sql = "INSERT INTO Tags (name) VALUES (?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tag.getName());
            ps.executeUpdate();
        }
    }

    public void updateTag(Tag tag) throws SQLException {
        String sql = "UPDATE Tags SET name = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tag.getName());
            ps.setInt(2, tag.getId());
            ps.executeUpdate();
        }
    }

    public void deleteTag(int id) throws SQLException {
        String sql = "DELETE FROM Tags WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
