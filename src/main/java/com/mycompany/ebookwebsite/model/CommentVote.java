package com.mycompany.ebookwebsite.model;

public class CommentVote {
    private int userId;
    private int commentId;
    private int value; // 1: like, -1: dislike

    public CommentVote() {}

    public CommentVote(int userId, int commentId, int value) {
        this.userId = userId;
        this.commentId = commentId;
        this.value = value;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
} 