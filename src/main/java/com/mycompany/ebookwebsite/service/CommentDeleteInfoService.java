package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.CommentDeleteInfoDAO;
import com.mycompany.ebookwebsite.model.CommentDeleteInfo;
import java.util.Optional;

public class CommentDeleteInfoService {
    private final CommentDeleteInfoDAO deleteInfoDAO = new CommentDeleteInfoDAO();

    public boolean softDelete(CommentDeleteInfo info) {
        return deleteInfoDAO.insertDeleteInfo(info);
    }

    public boolean isDeleted(int commentId) {
        return deleteInfoDAO.isDeleted(commentId);
    }

    public Optional<CommentDeleteInfo> getDeleteInfo(int commentId) {
        return deleteInfoDAO.getDeleteInfo(commentId);
    }
} 