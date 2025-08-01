package com.mycompany.ebookwebsite.model;

public class BookMetadata {
    private String title;
    private String author;
    private String genre;
    private String summary;

    // Constructor
    public BookMetadata() {}

    public BookMetadata(String title, String author, String genre, String summary) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.summary = summary;
    }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setSummary(String summary) { this.summary = summary; }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getSummary() { return summary; }
} 