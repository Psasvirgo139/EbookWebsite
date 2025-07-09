package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.UserCoin;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * DAO để quản lý coin của user
 * @author ADMIN
 */
public class UserCoinDAO {
    
    private static final String SELECT_BY_USER = "SELECT * FROM UserCoins WHERE user_id = ?";
    private static final String INSERT = "INSERT INTO UserCoins (user_id, coins, last_updated) VALUES (?, ?, ?)";
    private static final String UPDATE_COINS = "UPDATE UserCoins SET coins = ?, last_updated = ? WHERE user_id = ?";
    private static final String UPSERT = "MERGE UserCoins AS target " +
            "USING (VALUES (?, ?, ?)) AS source (user_id, coins, last_updated) " +
            "ON target.user_id = source.user_id " +
            "WHEN MATCHED THEN UPDATE SET coins = source.coins, last_updated = source.last_updated " +
            "WHEN NOT MATCHED THEN INSERT (user_id, coins, last_updated) VALUES (source.user_id, source.coins, source.last_updated);";
    
    public UserCoin getUserCoins(int userId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapUserCoin(rs);
            }
            
            // Nếu user chưa có record coin thì tạo mới với 0 coin
            UserCoin newUserCoin = new UserCoin(userId, 0, LocalDateTime.now());
            try {
                insertUserCoin(newUserCoin);
                return newUserCoin;
            } catch (SQLException e) {
                // Nếu insert fail (có thể do concurrent access), thử lấy lại
                try (Connection con2 = DBConnection.getConnection();
                     PreparedStatement ps2 = con2.prepareStatement(SELECT_BY_USER)) {
                    
                    ps2.setInt(1, userId);
                    ResultSet rs2 = ps2.executeQuery();
                    
                    if (rs2.next()) {
                        return mapUserCoin(rs2);
                    } else {
                        // Nếu vẫn không có, trả về object mặc định
                        return newUserCoin;
                    }
                }
            }
        }
    }
    
    public void insertUserCoin(UserCoin userCoin) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT)) {
            
            ps.setInt(1, userCoin.getUserId());
            ps.setInt(2, userCoin.getCoins());
            ps.setTimestamp(3, Timestamp.valueOf(userCoin.getLastUpdated()));
            
            ps.executeUpdate();
        }
    }
    
    public boolean updateCoins(int userId, int newCoins) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_COINS)) {
            
            ps.setInt(1, newCoins);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(3, userId);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean addCoins(int userId, int coinsToAdd) throws SQLException {
        UserCoin userCoin = getUserCoins(userId);
        int newCoins = userCoin.getCoins() + coinsToAdd;
        return updateCoins(userId, newCoins);
    }
    
    public boolean deductCoins(int userId, int coinsToDeduct) throws SQLException {
        UserCoin userCoin = getUserCoins(userId);
        
        if (userCoin.getCoins() < coinsToDeduct) {
            throw new IllegalArgumentException("Không đủ coin để thực hiện giao dịch!");
        }
        
        int newCoins = userCoin.getCoins() - coinsToDeduct;
        return updateCoins(userId, newCoins);
    }
    
    public void upsertUserCoin(UserCoin userCoin) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPSERT)) {
            
            ps.setInt(1, userCoin.getUserId());
            ps.setInt(2, userCoin.getCoins());
            ps.setTimestamp(3, Timestamp.valueOf(userCoin.getLastUpdated()));
            
            ps.executeUpdate();
        }
    }
    
    private UserCoin mapUserCoin(ResultSet rs) throws SQLException {
        return new UserCoin(
                rs.getInt("user_id"),
                rs.getInt("coins"),
                rs.getTimestamp("last_updated").toLocalDateTime()
        );
    }
} 