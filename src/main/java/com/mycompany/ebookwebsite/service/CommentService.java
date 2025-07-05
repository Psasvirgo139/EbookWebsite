package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.CommentDAO;
import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.Comment;
import java.time.LocalDateTime;

import java.util.List;

public class CommentService {
    private final CommentDAO commentDAO;
    private final EbookDAO ebookDAO;

    public CommentService() {
        this.commentDAO = new CommentDAO();
        this.ebookDAO = new EbookDAO();
    }

    public List<Comment> getCommentsByEbookId(int ebookId) {
        return commentDAO.getCommentsByEbookId(ebookId);
    }

    public List<Comment> getCommentsByEbookIdWithPagination(int ebookId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return commentDAO.getCommentsByEbookIdWithPagination(ebookId, offset, pageSize);
    }

    public int getCommentCountByEbookId(int ebookId) {
        return commentDAO.getCommentCountByEbookId(ebookId);
    }

    public void addComment(Comment comment) {
        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(LocalDateTime.now());
        }
        commentDAO.insertComment(comment);
    }

    public void deleteComment(int commentId, int userId) {
        commentDAO.deleteCommentByIdAndUser(commentId, userId);
    }

    public List<Comment> getCommentsByChapter(int ebookId, int chapterId) {
        return commentDAO.getCommentsByChapter(ebookId, chapterId);
    }

    public Comment getCommentById(int commentId) {
        return commentDAO.getCommentById(commentId);
    }

    public boolean canUserDeleteComment(int commentId, int userId) {
        return commentDAO.canUserDeleteComment(commentId, userId);
    }

    public List<Comment> getRecentComments(int limit) {
        return commentDAO.getRecentComments(limit);
    }

    public boolean isEbookExists(int ebookId) {
        try {
            return ebookDAO.getEbookById(ebookId) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
