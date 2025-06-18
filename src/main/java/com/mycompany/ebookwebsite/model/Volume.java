/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.model;

import java.time.LocalDate;

/**
 *
 * @author ADMIN
 */
public class Volume {
    private int id;
    private int ebookId;
    private String title;
    private int number;
    private LocalDate publishedAt;
    private String accessLevel;
    private int viewCount;
    private int likeCount;

    public Volume() {
    }

    public Volume(int id, int ebookId, String title, int number, LocalDate publishedAt, String accessLevel, int viewCount, int likeCount) {
        this.id = id;
        this.ebookId = ebookId;
        this.title = title;
        this.number = number;
        this.publishedAt = publishedAt;
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

    public int getEbookId() {
        return ebookId;
    }

    public void setEbookId(int ebookId) {
        this.ebookId = ebookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public LocalDate getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDate publishedAt) {
        this.publishedAt = publishedAt;
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

    @Override
    public String toString() {
        return "Volume{" + "id=" + id + ", ebookId=" + ebookId + ", title=" + title + ", number=" + number + ", publishedAt=" + publishedAt + ", accessLevel=" + accessLevel + ", viewCount=" + viewCount + ", likeCount=" + likeCount + '}';
    }
}
