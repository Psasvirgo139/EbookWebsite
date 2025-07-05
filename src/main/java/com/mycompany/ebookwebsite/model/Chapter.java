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
public class Chapter {

    private int id;
    private int ebookID;
    private Integer volumeID;
    private String title;
    private double number;
    private String contentUrl;
    private LocalDateTime createdAt;
    private String accessLevel;
    private int viewCount;
    private int likeCount;
    private String content;

    public Chapter() {
    }

    public Chapter(int id, int ebookID, Integer volumeID, String title, double number, String contentUrl, LocalDateTime createdAt, String accessLevel, int viewCount, int likeCount, boolean isFree) {
        this.id = id;
        this.ebookID = ebookID;
        this.volumeID = volumeID;
        this.title = title;
        this.number = number;
        this.contentUrl = contentUrl;
        this.createdAt = createdAt;
        this.accessLevel = accessLevel;
        this.viewCount = viewCount;
        this.likeCount = likeCount;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEbookID() {
        return ebookID;
    }

    public void setEbookID(int ebookID) {
        this.ebookID = ebookID;
    }

    public Integer getVolumeID() {
        return volumeID;
    }

    public void setVolumeID(Integer volumeID) {
        this.volumeID = volumeID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Chapter{"
                + "id=" + id
                + ", ebookID=" + ebookID
                + ", volumeID=" + volumeID
                + ", title='" + title + '\''
                + ", number=" + number
                + ", contentUrl='" + contentUrl + '\''
                + ", createdAt=" + createdAt
                + ", accessLevel='" + accessLevel + '\''
                + ", viewCount=" + viewCount
                + ", likeCount=" + likeCount
                + '}';
    }
}
