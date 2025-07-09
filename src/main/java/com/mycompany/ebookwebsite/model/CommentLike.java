package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

public class CommentLike {
    private int userId;
    private int commentId;
    private LocalDateTime likedAt;

    public CommentLike() {}

    public CommentLike(int userId, int commentId, LocalDateTime likedAt) {
        this.userId = userId;
        this.commentId = commentId;
        this.likedAt = likedAt;
    }

    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}
    public int getCommentId() {return commentId;}
    public void setCommentId(int commentId) {this.commentId = commentId;}
    public LocalDateTime getLikedAt() {return likedAt;}
    public void setLikedAt(LocalDateTime likedAt) {this.likedAt = likedAt;}
} 