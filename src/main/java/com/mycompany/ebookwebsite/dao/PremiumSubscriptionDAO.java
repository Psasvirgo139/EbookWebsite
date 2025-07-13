package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.dao.DBConnection;
import com.mycompany.ebookwebsite.model.PremiumSubscription;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 👑 Premium Subscription DAO
 * 
 * Quản lý database operations cho premium subscriptions
 */
public class PremiumSubscriptionDAO {
    
    // SQL Statements
    private static final String INSERT = 
        "INSERT INTO PremiumSubscriptions (user_id, start_date, expiry_date, payment_method, amount, status, created_at, updated_at) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID = 
        "SELECT * FROM PremiumSubscriptions WHERE id = ?";
    
    private static final String SELECT_ACTIVE_BY_USER = 
        "SELECT * FROM PremiumSubscriptions WHERE user_id = ? AND status = 'active' AND expiry_date >= ? ORDER BY expiry_date DESC";
    
    private static final String SELECT_ALL_BY_USER = 
        "SELECT * FROM PremiumSubscriptions WHERE user_id = ? ORDER BY created_at DESC";
    
    private static final String UPDATE_STATUS = 
        "UPDATE PremiumSubscriptions SET status = ?, updated_at = ? WHERE id = ?";
    
    private static final String SELECT_EXPIRED = 
        "SELECT * FROM PremiumSubscriptions WHERE status = 'active' AND expiry_date < ?";
    
    private static final String EXTEND_SUBSCRIPTION = 
        "UPDATE PremiumSubscriptions SET expiry_date = ?, updated_at = ? WHERE id = ?";
    
    private static final String SELECT_ALL_ACTIVE = 
        "SELECT * FROM PremiumSubscriptions WHERE status = 'active' AND expiry_date >= ? ORDER BY created_at DESC";
    
    /**
     * 💾 Tạo subscription mới
     */
    public int insertSubscription(PremiumSubscription subscription) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, subscription.getUserId());
            ps.setDate(2, Date.valueOf(subscription.getStartDate()));
            ps.setDate(3, Date.valueOf(subscription.getExpiryDate()));
            ps.setString(4, subscription.getPaymentMethod());
            ps.setDouble(5, subscription.getAmount());
            ps.setString(6, subscription.getStatus());
            ps.setTimestamp(7, Timestamp.valueOf(subscription.getCreatedAt()));
            ps.setTimestamp(8, Timestamp.valueOf(subscription.getUpdatedAt()));
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        subscription.setId(generatedId);
                        return generatedId;
                    }
                }
            }
            
            throw new SQLException("Failed to create subscription, no ID obtained");
        }
    }
    
    /**
     * 🔍 Tìm subscription theo ID
     */
    public PremiumSubscription findById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSubscription(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * ✅ Lấy subscription active hiện tại của user
     */
    public PremiumSubscription getActiveSubscription(int userId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ACTIVE_BY_USER)) {
            
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSubscription(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * 📋 Lấy tất cả subscription của user
     */
    public List<PremiumSubscription> getSubscriptionsByUser(int userId) throws SQLException {
        List<PremiumSubscription> subscriptions = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_BY_USER)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    subscriptions.add(mapResultSetToSubscription(rs));
                }
            }
        }
        
        return subscriptions;
    }
    
    /**
     * 🔄 Cập nhật status subscription
     */
    public boolean updateStatus(int subscriptionId, String status) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_STATUS)) {
            
            ps.setString(1, status);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, subscriptionId);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    /**
     * ⏰ Lấy tất cả subscription đã expired
     */
    public List<PremiumSubscription> getExpiredSubscriptions() throws SQLException {
        List<PremiumSubscription> expiredSubscriptions = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_EXPIRED)) {
            
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    expiredSubscriptions.add(mapResultSetToSubscription(rs));
                }
            }
        }
        
        return expiredSubscriptions;
    }
    
    /**
     * 📅 Gia hạn subscription (extend expiry date)
     */
    public boolean extendSubscription(int subscriptionId, LocalDate newExpiryDate) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(EXTEND_SUBSCRIPTION)) {
            
            ps.setDate(1, Date.valueOf(newExpiryDate));
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, subscriptionId);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    /**
     * 🗺️ Map ResultSet to PremiumSubscription object
     */
    private PremiumSubscription mapResultSetToSubscription(ResultSet rs) throws SQLException {
        PremiumSubscription subscription = new PremiumSubscription();
        
        subscription.setId(rs.getInt("id"));
        subscription.setUserId(rs.getInt("user_id"));
        subscription.setStartDate(rs.getDate("start_date").toLocalDate());
        subscription.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
        subscription.setPaymentMethod(rs.getString("payment_method"));
        subscription.setAmount(rs.getDouble("amount"));
        subscription.setStatus(rs.getString("status"));
        subscription.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        subscription.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        
        return subscription;
    }
    
    /**
     * 📋 Lấy tất cả active subscriptions (for testing/admin)
     */
    public List<PremiumSubscription> getActiveSubscriptions() throws SQLException {
        List<PremiumSubscription> activeSubscriptions = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_ACTIVE)) {
            
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    activeSubscriptions.add(mapResultSetToSubscription(rs));
                }
            }
        }
        
        return activeSubscriptions;
    }
    
    /**
     * 🔍 Lấy subscription theo ID (alias for findById for consistency)
     */
    public PremiumSubscription getSubscriptionById(int id) throws SQLException {
        return findById(id);
    }
} 