package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

public class CommentEditInfo {
    private int id;
    private int commentId;
    private int editedBy;
    private LocalDateTime editedAt;
    private String oldContent;

    public CommentEditInfo() {}

    public CommentEditInfo(int id, int commentId, int editedBy, LocalDateTime editedAt, String oldContent) {
        this.id = id;
        this.commentId = commentId;
        this.editedBy = editedBy;
        this.editedAt = editedAt;
        this.oldContent = oldContent;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public int getEditedBy() { return editedBy; }
    public void setEditedBy(int editedBy) { this.editedBy = editedBy; }

    public LocalDateTime getEditedAt() { return editedAt; }
    public void setEditedAt(LocalDateTime editedAt) { this.editedAt = editedAt; }

    public String getOldContent() { return oldContent; }
    public void setOldContent(String oldContent) { this.oldContent = oldContent; }
} 