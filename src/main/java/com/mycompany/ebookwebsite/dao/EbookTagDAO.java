/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.EbookTag;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class EbookTagDAO {
    
    private static final String INSERT = "INSERT INTO EbookTags (ebook_id, tag_id, is_primary) VALUES (?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM EbookTags";
    private static final String SELECT_BY_EBOOK = "SELECT * FROM EbookTags WHERE ebook_id = ?";
    private static final String SELECT_BY_TAG = "SELECT * FROM EbookTags WHERE tag_id = ?";
    private static final String SELECT_BY_EBOOK_AND_TAG = "SELECT * FROM EbookTags WHERE ebook_id = ? AND tag_id = ?";
    private static final String SELECT_PRIMARY_BY_EBOOK = "SELECT * FROM EbookTags WHERE ebook_id = ? AND is_primary = 1";
    private static final String UPDATE = "UPDATE EbookTags SET is_primary = ? WHERE ebook_id = ? AND tag_id = ?";
    private static final String DELETE = "DELETE FROM EbookTags WHERE ebook_id = ? AND tag_id = ?";
    private static final String DELETE_BY_EBOOK = "DELETE FROM EbookTags WHERE ebook_id = ?";
    private static final String DELETE_BY_TAG = "DELETE FROM EbookTags WHERE tag_id = ?";
    
    public void insertEbookTag(EbookTag ebookTag) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT)) {
            
            ps.setInt(1, ebookTag.getEbookId());
            ps.setInt(2, ebookTag.getTagId());
            ps.setBoolean(3, ebookTag.isIsPrimary());
            
            ps.executeUpdate();
        }
    }
    
    public EbookTag selectEbookTag(int ebookId, int tagId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_EBOOK_AND_TAG)) {
            
            ps.setInt(1, ebookId);
            ps.setInt(2, tagId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapEbookTag(rs);
            }
        }
        return null;
    }
    
    public List<EbookTag> selectAllEbookTags() throws SQLException {
        List<EbookTag> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapEbookTag(rs));
            }
        }
        return list;
    }
    
    public List<EbookTag> getTagsByEbook(int ebookId) throws SQLException {
        List<EbookTag> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_EBOOK)) {
            
            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapEbookTag(rs));
            }
        }
        return list;
    }
    
    public List<EbookTag> getEbooksByTag(int tagId) throws SQLException {
        List<EbookTag> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_TAG)) {
            
            ps.setInt(1, tagId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapEbookTag(rs));
            }
        }
        return list;
    }
    
    public List<EbookTag> getPrimaryTagsByEbook(int ebookId) throws SQLException {
        List<EbookTag> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_PRIMARY_BY_EBOOK)) {
            
            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapEbookTag(rs));
            }
        }
        return list;
    }
    
    public boolean updateEbookTag(EbookTag ebookTag) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            
            ps.setBoolean(1, ebookTag.isIsPrimary());
            ps.setInt(2, ebookTag.getEbookId());
            ps.setInt(3, ebookTag.getTagId());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean deleteEbookTag(int ebookId, int tagId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            
            ps.setInt(1, ebookId);
            ps.setInt(2, tagId);
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
    
    public boolean deleteByTag(int tagId) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(DELETE_BY_TAG)) {
            
            ps.setInt(1, tagId);
            return ps.executeUpdate() > 0;
        }
    }
    
    private EbookTag mapEbookTag(ResultSet rs) throws SQLException {
        return new EbookTag(
                rs.getInt("ebook_id"),
                rs.getInt("tag_id"),
                rs.getBoolean("is_primary")
        );
    }
}