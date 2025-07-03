package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.CommentDAO;
import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.model.User;
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

    public boolean isCommentOwner(int commentId, int userId) {
        Comment comment = commentDAO.getCommentById(commentId);
        return comment != null && comment.getUserID() == userId;
    }

    public void deleteComment(int commentId, User user) {
        Comment comment = commentDAO.getCommentById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("Comment không tồn tại!");
        }
        boolean isAdmin = "admin".equalsIgnoreCase(user.getRole());
        boolean isOwner = comment.getUserID() == user.getId();
        if (!isAdmin && !isOwner) {
            throw new SecurityException("Bạn không có quyền xóa comment này!");
        }
        commentDAO.deleteCommentByIdAndUser(commentId, comment.getUserID());
    }

    public List<Comment> getCommentsByChapter(int ebookId, int chapterId) {
        return commentDAO.getCommentsByChapter(ebookId, chapterId);
    }
}
