package com.mycompany.ebookwebsite.model;

import java.time.LocalDateTime;

/**
 * Order model for managing premium purchases and revenue tracking
 */
public class Order {
    private int id;
    private int userId;
    private int ebookId;
    private double amount;
    private String status; // pending, completed, cancelled, refunded
    private String paymentMethod; // credit_card, paypal, etc.
    private LocalDateTime orderDate;
    private LocalDateTime paymentDate;
    private String transactionId;
    private String notes;

    public Order() {
    }

    public Order(int id, int userId, int ebookId, double amount, String status, String paymentMethod, 
                 LocalDateTime orderDate, LocalDateTime paymentDate, String transactionId, String notes) {
        this.id = id;
        this.userId = userId;
        this.ebookId = ebookId;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.orderDate = orderDate;
        this.paymentDate = paymentDate;
        this.transactionId = transactionId;
        this.notes = notes;
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

    public int getEbookId() {
        return ebookId;
    }

    public void setEbookId(int ebookId) {
        this.ebookId = ebookId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", ebookId=" + ebookId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", orderDate=" + orderDate +
                ", paymentDate=" + paymentDate +
                ", transactionId='" + transactionId + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
} 