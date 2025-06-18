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
public class UserRead {
    private int userID;
    private int ebookID;
    private Integer lastReadChapterID;
    private LocalDate lastReadAt;

    public UserRead() {
    }

    public UserRead(int userID, int ebookID, Integer lastReadChapterID, LocalDate lastReadAt) {
        this.userID = userID;
        this.ebookID = ebookID;
        this.lastReadChapterID = lastReadChapterID;
        this.lastReadAt = lastReadAt;
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

    public Integer getLastReadChapterID() {
        return lastReadChapterID;
    }

    public void setLastReadChapterID(Integer lastReadChapterID) {
        this.lastReadChapterID = lastReadChapterID;
    }

    public LocalDate getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(LocalDate lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    @Override
    public String toString() {
        return "UserRead{" + "userID=" + userID + ", ebookID=" + ebookID + ", lastReadChapterID=" + lastReadChapterID + ", lastReadAt=" + lastReadAt + '}';
    }
}
