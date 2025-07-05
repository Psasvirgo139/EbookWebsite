package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Order;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderDAO for managing premium purchases and revenue tracking
 */
public class OrderDAO {

    private static final String INSERT = "INSERT INTO Orders (user_id, ebook_id, amount, status, payment_method, order_date, payment_date, transaction_id, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT * FROM Orders ORDER BY order_date DESC";
    private static final String SELECT_BY_ID = "SELECT * FROM Orders WHERE id = ?";
    private static final String SELECT_BY_USER_ID = "SELECT * FROM Orders WHERE user_id = ? ORDER BY order_date DESC";
    private static final String SELECT_BY_STATUS = "SELECT * FROM Orders WHERE status = ? ORDER BY order_date DESC";
    private static final String SELECT_COMPLETED = "SELECT * FROM Orders WHERE status = 'completed' ORDER BY order_date DESC";
    private static final String UPDATE_STATUS = "UPDATE Orders SET status = ?, payment_date = ? WHERE id = ?";
    private static final String COUNT_ALL = "SELECT COUNT(*) FROM Orders";
    private static final String COUNT_BY_STATUS = "SELECT COUNT(*) FROM Orders WHERE status = ?";
    private static final String SUM_COMPLETED_AMOUNT = "SELECT SUM(amount) FROM Orders WHERE status = 'completed'";
    private static final String SUM_COMPLETED_AMOUNT_BY_DATE = "SELECT SUM(amount) FROM Orders WHERE status = 'completed' AND order_date >= ?";

    public void insert(Order order) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getUserId());
            ps.setInt(2, order.getEbookId());
            ps.setDouble(3, order.getAmount());
            ps.setString(4, order.getStatus());
            ps.setString(5, order.getPaymentMethod());
            
            if (order.getOrderDate() != null) {
                ps.setTimestamp(6, Timestamp.valueOf(order.getOrderDate()));
            } else {
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            }
            
            if (order.getPaymentDate() != null) {
                ps.setTimestamp(7, Timestamp.valueOf(order.getPaymentDate()));
            } else {
                ps.setNull(7, Types.TIMESTAMP);
            }
            
            ps.setString(8, order.getTransactionId());
            ps.setString(9, order.getNotes());

            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Order> findAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                orders.add(mapOrder(rs));
            }
        }
        return orders;
    }

    public Order findById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {

            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapOrder(rs);
                }
            }
        }
        return null;
    }

    public List<Order> findByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER_ID)) {

            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        return orders;
    }

    public List<Order> findByStatus(String status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_BY_STATUS)) {

            ps.setString(1, status);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        return orders;
    }

    public List<Order> findCompletedOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SELECT_COMPLETED);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                orders.add(mapOrder(rs));
            }
        }
        return orders;
    }

    public boolean updateStatus(int orderId, String status) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(UPDATE_STATUS)) {

            ps.setString(1, status);
            if ("completed".equals(status)) {
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            } else {
                ps.setNull(2, Types.TIMESTAMP);
            }
            ps.setInt(3, orderId);
            
            return ps.executeUpdate() > 0;
        }
    }

    public int countAllOrders() throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(COUNT_ALL);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int countByStatus(String status) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(COUNT_BY_STATUS)) {

            ps.setString(1, status);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public double getTotalRevenue() throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SUM_COMPLETED_AMOUNT);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    public double getRevenueSince(LocalDateTime since) throws SQLException {
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(SUM_COMPLETED_AMOUNT_BY_DATE)) {

            ps.setTimestamp(1, Timestamp.valueOf(since));
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }

    public List<Order> getRecentOrders(int limit) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders ORDER BY order_date DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        return orders;
    }

    public List<Order> getRecentCompletedOrders(int limit) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE status = 'completed' ORDER BY payment_date DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        return orders;
    }

    public double getMonthlyRevenue(int year, int month) throws SQLException {
        String sql = "SELECT SUM(amount) FROM Orders WHERE status = 'completed' AND YEAR(order_date) = ? AND MONTH(order_date) = ?";
        
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, year);
            ps.setInt(2, month);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }

    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE order_date BETWEEN ? AND ? ORDER BY order_date DESC";
        
        try (Connection con = DBConnection.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        return orders;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setEbookId(rs.getInt("ebook_id"));
        order.setAmount(rs.getDouble("amount"));
        order.setStatus(rs.getString("status"));
        order.setPaymentMethod(rs.getString("payment_method"));
        
        if (rs.getTimestamp("order_date") != null) {
            order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        }
        
        if (rs.getTimestamp("payment_date") != null) {
            order.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
        }
        
        order.setTransactionId(rs.getString("transaction_id"));
        order.setNotes(rs.getString("notes"));
        
        return order;
    }
} 