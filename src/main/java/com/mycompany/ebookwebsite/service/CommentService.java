package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.CommentDAO;
import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.model.User;
import java.time.LocalDateTime;

import java.sql.SQLException;
import java.util.List;

public class CommentService {
    private final CommentDAO commentDAO = new CommentDAO();

    public List<Comment> getCommentsByEbookId(int ebookId) {
        List<Comment> comments = commentDAO.getCommentsByEbookId(ebookId);
        CommentVoteService voteService = new CommentVoteService();
        for (Comment c : comments) {
            if (c.getCreatedAt() != null) {
                c.setContentDate(java.util.Date.from(c.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant()));
            }
            c.setLikeCount(voteService.getLikeCount(c.getId()));
        }
        return comments;
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
        List<Comment> chapterComments = commentDAO.getCommentsByChapter(ebookId, chapterId);
        CommentVoteService voteService = new CommentVoteService();
        for (Comment c : chapterComments) {
            if (c.getCreatedAt() != null) {
                c.setContentDate(java.util.Date.from(c.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant()));
            }
            c.setLikeCount(voteService.getLikeCount(c.getId()));
        }
        return chapterComments;
    }

    public void addBookComment(Comment c) throws SQLException {
        commentDAO.insertComment(c);
    }

    public void addReply(Comment c) throws SQLException {
        commentDAO.insertReply(c);
    }

    public List<Comment> getBookComments(int ebookId) throws SQLException {
        List<Comment> bookComments = commentDAO.getBookComments(ebookId);
        CommentVoteService voteService = new CommentVoteService();
        for (Comment c : bookComments) {
            if (c.getCreatedAt() != null) {
                c.setContentDate(java.util.Date.from(c.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant()));
            }
            c.setLikeCount(voteService.getLikeCount(c.getId()));
        }
        return bookComments;
    }

    public List<Comment> getReplies(int parentId) throws SQLException {
        List<Comment> replies = commentDAO.getReplies(parentId);
        CommentVoteService voteService = new CommentVoteService();
        for (Comment c : replies) {
            if (c.getCreatedAt() != null) {
                c.setContentDate(java.util.Date.from(c.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant()));
            }
            c.setLikeCount(voteService.getLikeCount(c.getId()));
        }
        return replies;
    }

    public List<Comment> getTopChapterComments(int ebookId, int limit) throws SQLException {
        List<Comment> topComments = commentDAO.getTopChapterComments(ebookId, limit);
        CommentVoteService voteService = new CommentVoteService();
        for (Comment c : topComments) {
            if (c.getCreatedAt() != null) {
                c.setContentDate(java.util.Date.from(c.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant()));
            }
            c.setLikeCount(voteService.getLikeCount(c.getId()));
        }
        return topComments;
    }

    public boolean toggleLike(int userId, int commentId) throws java.sql.SQLException {
        CommentVoteService voteService = new CommentVoteService();
        Integer current = voteService.getVoteValue(userId, commentId);
        if (current != null && current == 1) {
            voteService.removeVote(userId, commentId);
            return false;
        } else {
            voteService.setVote(userId, commentId, 1);
            return true;
        }
    }

    public void updateCommentContent(int commentId, String content) throws SQLException {
        commentDAO.updateCommentContent(commentId, content);
    }

    public List<Comment> getCommentsByBook(int ebookId) {
        List<Comment> bookComments = commentDAO.getCommentsByBook(ebookId);
        CommentVoteService voteService = new CommentVoteService();
        for (Comment c : bookComments) {
            if (c.getCreatedAt() != null) {
                c.setContentDate(java.util.Date.from(c.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant()));
            }
            c.setLikeCount(voteService.getLikeCount(c.getId()));
        }
        return bookComments;
    }
}
