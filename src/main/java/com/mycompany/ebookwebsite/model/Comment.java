/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

/**
 *
 * @author ADMIN
 */
public class Comment {
    private int id;
    private int userID;
    private int ebookID;
    private Integer chapterID;
    private String content;
    private LocalDateTime createdAt;
    private Integer parentCommentID;
    private int likeCount;

    public Comment() {
    }

    public Comment(int id, int userID, int ebookID, Integer chapterID, String content, LocalDateTime createdAt, Integer parentCommentID, int likeCount) {
        this.id = id;
        this.userID = userID;
        this.ebookID = ebookID;
        this.chapterID = chapterID;
        this.content = content;
        this.createdAt = createdAt;
        this.parentCommentID = parentCommentID;
        this.likeCount = likeCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getEbookID() {
        return ebookID;
    }

    public void setEbookID(int ebookID) {
        this.ebookID = ebookID;
    }

    public Integer getChapterID() {
        return chapterID;
    }

    public void setChapterID(Integer chapterID) {
        this.chapterID = chapterID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getParentCommentID() {
        return parentCommentID;
    }

    public void setParentCommentID(Integer parentCommentID) {
        this.parentCommentID = parentCommentID;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    @Override
    public String toString() {
        return "Comment{" + "id=" + id + ", userID=" + userID + ", ebookID=" + ebookID + ", chapterID=" + chapterID + ", content=" + content + ", createdAt=" + createdAt + ", parentCommentID=" + parentCommentID + ", likeCount=" + likeCount + '}';
    }
}
