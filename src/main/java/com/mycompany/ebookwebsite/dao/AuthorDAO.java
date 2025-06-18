package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Author;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {
    
    private static final String INSERT = "INSERT INTO Authors (name, bio, avatar_url) VALUES (?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Authors";
    private static final String SELECT_BY_ID = "SELECT * FROM Authors WHERE id = ?";
    private static final String SEARCH_BY_NAME = "SELECT * FROM Authors WHERE name LIKE ?";
    private static final String UPDATE = "UPDATE Authors SET name = ?, bio = ?, avatar_url = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM Authors WHERE id = ?";
    
    public void insertAuthor(Author author) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT)) {
            
            ps.setString(1, author.getName());
            ps.setString(2, author.getBio());
            ps.setString(3, author.getAvatarUrl());
            
            ps.executeUpdate();
        }
    }
    
    public List<Author> selectAllAuthors() throws SQLException {
        List<Author> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAuthor(rs));
            }
        }
        return list;
    }
    
    public Author selectAuthor(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapAuthor(rs);
            }
        }
        return null;
    }
    
    public List<Author> search(String searchName) throws SQLException {
        List<Author> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SEARCH_BY_NAME)) {
            
            ps.setString(1, "%" + searchName + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapAuthor(rs));
            }
        }
        return list;
    }
    
    public boolean updateAuthor(Author author) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            
            ps.setString(1, author.getName());
            ps.setString(2, author.getBio());
            ps.setString(3, author.getAvatarUrl());
            ps.setInt(4, author.getId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteAuthor(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    private Author mapAuthor(ResultSet rs) throws SQLException {
        return new Author(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("bio"),
            rs.getString("avatar_url")
        );
    }
}