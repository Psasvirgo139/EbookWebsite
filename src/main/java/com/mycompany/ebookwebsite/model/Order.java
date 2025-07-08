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
public class Order {
    private int id;
    private int userId;
    private LocalDateTime orderDate;
    private double totalAmount;
    private String status;

    public Order() {
    }

    public Order(int id, int userId, LocalDateTime orderDate, double totalAmount, String status) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", userId=" + userId + ", orderDate=" + orderDate + ", totalAmount=" + totalAmount + ", status=" + status + '}';
    }
}
