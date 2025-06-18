/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.model;

/**
 *
 * @author ADMIN
 */
public class Author {
    private int id;
    private String name;
    private String bio;
    private String avatarUrl;

    public Author() {
    }

    public Author(int id, String name, String bio, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", name=" + name + ", bio=" + bio + ", avatarUrl=" + avatarUrl + '}';
    }
}