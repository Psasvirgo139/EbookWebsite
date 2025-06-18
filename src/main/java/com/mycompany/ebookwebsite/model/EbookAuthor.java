/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.model;

/**
 *
 * @author ADMIN
 */
public class EbookAuthor {
    private int ebookID;
    private int authorID;
    private String role;

    public EbookAuthor() {
    }

    public EbookAuthor(int ebookID, int authorID, String role) {
        this.ebookID = ebookID;
        this.authorID = authorID;
        this.role = role;
    }

    public int getEbookID() {
        return ebookID;
    }

    public void setEbookID(int ebookID) {
        this.ebookID = ebookID;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "EbookAuthor{" + "ebookID=" + ebookID + ", authorID=" + authorID + ", role=" + role + '}';
    }
}
