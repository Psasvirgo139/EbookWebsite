package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

public class CommentDeleteInfo {
    private int id;
    private int commentId;
    private int deletedBy;
    private LocalDateTime deletedAt;

    public CommentDeleteInfo() {}

    public CommentDeleteInfo(int id, int commentId, int deletedBy, LocalDateTime deletedAt) {
        this.id = id;
        this.commentId = commentId;
        this.deletedBy = deletedBy;
        this.deletedAt = deletedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public int getDeletedBy() { return deletedBy; }
    public void setDeletedBy(int deletedBy) { this.deletedBy = deletedBy; }

    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
} 