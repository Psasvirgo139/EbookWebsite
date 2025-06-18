/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ebookwebsite.model;

/**
 *
 * @author ADMIN
 */
public class EbookTag {
    private int ebookId;
    private int tagId;
    private boolean isPrimary;

    public EbookTag() {
    }

    public EbookTag(int ebookId, int tagId, boolean isPrimary) {
        this.ebookId = ebookId;
        this.tagId = tagId;
        this.isPrimary = isPrimary;
    }

    public int getEbookId() {
        return ebookId;
    }

    public void setEbookId(int ebookId) {
        this.ebookId = ebookId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public boolean isIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    @Override
    public String toString() {
        return "EbookTag{" + "ebookId=" + ebookId + ", tagId=" + tagId + ", isPrimary=" + isPrimary + '}';
    }
}
