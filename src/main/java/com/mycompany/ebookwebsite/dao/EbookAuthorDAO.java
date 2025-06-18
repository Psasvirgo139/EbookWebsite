/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.EbookAuthor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class EbookAuthorDAO {
    
    private static final String INSERT = "INSERT INTO EbookAuthors (ebook_id, author_id, role) VALUES (?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM EbookAuthors";
    private static final String SELECT_BY_EBOOK = "SELECT * FROM EbookAuthors WHERE ebook_id = ?";
    private static final String SELECT_BY_AUTHOR = "SELECT * FROM EbookAuthors WHERE author_id = ?";
    private static final String SELECT_BY_EBOOK_AND_AUTHOR = "SELECT * FROM EbookAuthors WHERE ebook_id = ? AND author_id = ?";
    private static final String UPDATE = "UPDATE EbookAuthors SET role = ? WHERE ebook_id = ? AND author_id = ?";
    private static final String DELETE = "DELETE FROM EbookAuthors WHERE ebook_id = ? AND author_id = ?";
    private static final String DELETE_BY_EBOOK = "DELETE FROM EbookAuthors WHERE ebook_id = ?";
    private static final String DELETE_BY_AUTHOR = "DELETE FROM EbookAuthors WHERE author_id = ?";
    
    public void insertEbookAuthor(EbookAuthor ebookAuthor) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT)) {
            
            ps.setInt(1, ebookAuthor.getEbookID());
            ps.setInt(2, ebookAuthor.getAuthorID());
            ps.setString(3, ebookAuthor.getRole());
            
            ps.executeUpdate();
        }
    }
    
    public EbookAuthor selectEbookAuthor(int ebookId, int authorId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_EBOOK_AND_AUTHOR)) {
            
            ps.setInt(1, ebookId);
            ps.setInt(2, authorId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapEbookAuthor(rs);
            }
        }
        return null;
    }
    
    public List<EbookAuthor> selectAllEbookAuthors() throws SQLException {
        List<EbookAuthor> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapEbookAuthor(rs));
            }
        }
        return list;
    }
    
    public List<EbookAuthor> getAuthorsByEbook(int ebookId) throws SQLException {
        List<EbookAuthor> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_EBOOK)) {
            
            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapEbookAuthor(rs));
            }
        }
        return list;
    }
    
    public List<EbookAuthor> getEbooksByAuthor(int authorId) throws SQLException {
        List<EbookAuthor> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_AUTHOR)) {
            
            ps.setInt(1, authorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapEbookAuthor(rs));
            }
        }
        return list;
    }
    
    public boolean updateEbookAuthor(EbookAuthor ebookAuthor) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            
            ps.setString(1, ebookAuthor.getRole());
            ps.setInt(2, ebookAuthor.getEbookID());
            ps.setInt(3, ebookAuthor.getAuthorID());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteEbookAuthor(int ebookId, int authorId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            
            ps.setInt(1, ebookId);
            ps.setInt(2, authorId);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteByEbook(int ebookId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE_BY_EBOOK)) {
            
            ps.setInt(1, ebookId);
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteByAuthor(int authorId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE_BY_AUTHOR)) {
            
            ps.setInt(1, authorId);
            return ps.executeUpdate() > 0;
        }
    }
    
    private EbookAuthor mapEbookAuthor(ResultSet rs) throws SQLException {
        return new EbookAuthor(
                rs.getInt("ebook_id"),
                rs.getInt("author_id"),
                rs.getString("role")
        );
    }
}