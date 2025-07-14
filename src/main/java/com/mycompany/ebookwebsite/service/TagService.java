package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.TagDAO;
import com.mycompany.ebookwebsite.model.Tag;
import com.mycompany.ebookwebsite.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagService {
    private TagDAO tagDAO = new TagDAO();
    private static List<Tag> cachedTags = new ArrayList<>();
    private static boolean loaded = false;

    public TagService() {}

    public synchronized List<Tag> getAllTags() throws SQLException {
        if (!loaded) {
            cachedTags = tagDAO.getAllTags();
            loaded = true;
        }
        return new ArrayList<>(cachedTags);
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

    public void deleteTag(int id, User user) throws SQLException {
        if (!"admin".equals(user.getRole())) {
            throw new SecurityException("Bạn không có quyền xóa tag!");
        }
        tagDAO.deleteTag(id);
    }
}
