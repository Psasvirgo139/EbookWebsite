package com.mycompany.ebookwebsite.service;

import java.sql.SQLException;
import java.util.List;

import com.mycompany.ebookwebsite.dao.CommentDAO;
import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.model.User;

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

    public int countAllComments() throws SQLException {
        return commentDAO.countAllComments();
    }

    public java.util.List<com.mycompany.ebookwebsite.model.AdminCommentView> getAdminCommentViews() throws java.sql.SQLException {
        java.util.List<com.mycompany.ebookwebsite.model.Comment> comments = commentDAO.findAll();
        com.mycompany.ebookwebsite.dao.UserDAO userDAO = new com.mycompany.ebookwebsite.dao.UserDAO();
        com.mycompany.ebookwebsite.dao.EbookDAO ebookDAO = new com.mycompany.ebookwebsite.dao.EbookDAO();
        java.util.List<com.mycompany.ebookwebsite.model.AdminCommentView> views = new java.util.ArrayList<>();
        for (com.mycompany.ebookwebsite.model.Comment c : comments) {
            String username = "";
            String ebookTitle = "";
            com.mycompany.ebookwebsite.model.User u = userDAO.findById(c.getUserID());
            if (u != null) username = u.getUsername();
            com.mycompany.ebookwebsite.model.Ebook e = ebookDAO.getEbookById(c.getEbookID());
            if (e != null) ebookTitle = e.getTitle();
            views.add(new com.mycompany.ebookwebsite.model.AdminCommentView(
                c.getId(), c.getUserID(), username, c.getEbookID(), ebookTitle, c.getChapterID(), c.getContent(), c.getCreatedAt()
            ));
        }
        return views;
    }

    public java.util.List<com.mycompany.ebookwebsite.model.AdminCommentView> getAdminCommentViewsReportedOnly() throws java.sql.SQLException {
        // Lấy danh sách commentId đã bị báo cáo
        java.util.Set<Integer> reportedIds = new java.util.HashSet<>();
        try (java.sql.Connection con = com.mycompany.ebookwebsite.dao.DBConnection.getConnection();
             java.sql.PreparedStatement ps = con.prepareStatement("SELECT DISTINCT comment_id FROM CommentReports")) {
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reportedIds.add(rs.getInt(1));
            }
        }
        java.util.List<com.mycompany.ebookwebsite.model.Comment> comments = commentDAO.findAll();
        com.mycompany.ebookwebsite.dao.UserDAO userDAO = new com.mycompany.ebookwebsite.dao.UserDAO();
        com.mycompany.ebookwebsite.dao.EbookDAO ebookDAO = new com.mycompany.ebookwebsite.dao.EbookDAO();
        java.util.List<com.mycompany.ebookwebsite.model.AdminCommentView> views = new java.util.ArrayList<>();
        for (com.mycompany.ebookwebsite.model.Comment c : comments) {
            if (!reportedIds.contains(c.getId())) continue;
            String username = "";
            String ebookTitle = "";
            com.mycompany.ebookwebsite.model.User u = userDAO.findById(c.getUserID());
            if (u != null) username = u.getUsername();
            com.mycompany.ebookwebsite.model.Ebook e = ebookDAO.getEbookById(c.getEbookID());
            if (e != null) ebookTitle = e.getTitle();
            views.add(new com.mycompany.ebookwebsite.model.AdminCommentView(
                c.getId(), c.getUserID(), username, c.getEbookID(), ebookTitle, c.getChapterID(), c.getContent(), c.getCreatedAt()
            ));
        }
        return views;
    }
}
