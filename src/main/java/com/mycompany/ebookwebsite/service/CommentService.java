package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.CommentDAO;
import com.mycompany.ebookwebsite.model.Comment;
import java.time.LocalDateTime;

import java.util.List;

public class CommentService {
    private final CommentDAO commentDAO;

    public CommentService() {
        this.commentDAO = new CommentDAO();
    }

    public List<Comment> getCommentsByEbookId(int ebookId) {
        return commentDAO.getCommentsByEbookId(ebookId);
    }

    public void addComment(Comment comment) {
        commentDAO.insertComment(comment);
    }

    public void deleteComment(int commentId, int userId) {
        commentDAO.deleteCommentByIdAndUser(commentId, userId);
    }

    public List<Comment> getCommentsByChapter(int ebookId, int chapterId) {
        return commentDAO.getCommentsByChapter(ebookId, chapterId);
    }
}
