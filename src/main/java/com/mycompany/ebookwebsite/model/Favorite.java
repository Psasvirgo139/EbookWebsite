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
public class Favorite {
    private int userID;
    private int ebookID;
    private Integer chapterID;
    private LocalDate createdAt;

    public Favorite() {
    }

    public Favorite(int userID, int ebookID, Integer chapterID, LocalDate createdAt) {
        this.userID = userID;
        this.ebookID = ebookID;
        this.chapterID = chapterID;
        this.createdAt = createdAt;
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Favorite{" + "userID=" + userID + ", ebookID=" + ebookID + ", chapterID=" + chapterID + ", createdAt=" + createdAt + '}';
    }
}
