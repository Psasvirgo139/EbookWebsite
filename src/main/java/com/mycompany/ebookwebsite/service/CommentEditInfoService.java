package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.CommentEditInfoDAO;
import com.mycompany.ebookwebsite.model.CommentEditInfo;
import java.util.List;

public class CommentEditInfoService {
    private final CommentEditInfoDAO editInfoDAO = new CommentEditInfoDAO();

    public boolean addEditInfo(CommentEditInfo info) {
        return editInfoDAO.insertEditInfo(info);
    }

    public List<CommentEditInfo> getEditHistory(int commentId) {
        return editInfoDAO.getEditHistory(commentId);
    }
} 