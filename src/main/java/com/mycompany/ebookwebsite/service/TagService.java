package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.TagDAO;
import com.mycompany.ebookwebsite.model.Tag;

import java.sql.SQLException;
import java.util.List;

public class TagService {
    private TagDAO tagDAO = new TagDAO();

    public List<Tag> getAllTags() throws SQLException {
        return tagDAO.getAllTags();
    }

    public Tag getTagById(int id) throws SQLException {
        return tagDAO.getTagById(id);
    }

    public void createTag(Tag tag) throws SQLException {
        tagDAO.insertTag(tag);
    }

    public void updateTag(Tag tag) throws SQLException {
        tagDAO.updateTag(tag);
    }

    public void deleteTag(int id) throws SQLException {
        tagDAO.deleteTag(id);
    }
    
    public boolean isTagExists(String name) throws SQLException {
        return tagDAO.isTagExists(name);
    }
    
    public boolean isTagInUse(int tagId) throws SQLException {
        return tagDAO.isTagInUse(tagId);
    }
    
    public List<Tag> searchTags(String keyword) throws SQLException {
        return tagDAO.searchTags(keyword);
    }
    
    public int getTagCount() throws SQLException {
        return tagDAO.getTagCount();
    }
}
