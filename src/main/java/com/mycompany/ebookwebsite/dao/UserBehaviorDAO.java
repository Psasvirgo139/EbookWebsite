package com.mycompany.ebookwebsite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mycompany.ebookwebsite.model.UserBehavior;

public class UserBehaviorDAO {

    public boolean trackUserAction(UserBehavior behavior) {
        String sql = "INSERT INTO UserBehavior (user_id, action_type, target_id, target_type, action_data) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, behavior.getUserId());
            stmt.setString(2, behavior.getActionType());
            if (behavior.getTargetId() != null) {
                stmt.setInt(3, behavior.getTargetId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, behavior.getTargetType());
            stmt.setString(5, behavior.getActionData());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UserBehavior> getUserRecentActions(int userId, int days) {
        List<UserBehavior> actions = new ArrayList<>();
        String sql = "SELECT * FROM UserBehavior WHERE user_id = ? AND " +
                    "created_at >= DATEADD(DAY, ?, GETDATE()) ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, -days);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserBehavior behavior = new UserBehavior();
                behavior.setId(rs.getInt("id"));
                behavior.setUserId(rs.getInt("user_id"));
                behavior.setActionType(rs.getString("action_type"));
                behavior.setTargetId(rs.getObject("target_id", Integer.class));
                behavior.setTargetType(rs.getString("target_type"));
                behavior.setActionData(rs.getString("action_data"));
                behavior.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                actions.add(behavior);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return actions;
    }

    public Map<String, Integer> getUserPreferences(int userId, int days) {
        Map<String, Integer> preferences = new HashMap<>();
        
        // Get genre preferences
        String genreSql = "SELECT b.genre, COUNT(*) as count FROM UserBehavior ub " +
                         "JOIN Ebooks b ON ub.target_id = b.id " +
                         "WHERE ub.user_id = ? AND ub.target_type = 'book' AND " +
                         "ub.created_at >= DATEADD(DAY, ?, GETDATE()) " +
                         "GROUP BY b.genre ORDER BY count DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(genreSql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, -days);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                preferences.put("genre_" + rs.getString("genre"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Get author preferences
        String authorSql = "SELECT b.author, COUNT(*) as count FROM UserBehavior ub " +
                          "JOIN Ebooks b ON ub.target_id = b.id " +
                          "WHERE ub.user_id = ? AND ub.target_type = 'book' AND " +
                          "ub.created_at >= DATEADD(DAY, ?, GETDATE()) " +
                          "GROUP BY b.author ORDER BY count DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(authorSql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, -days);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                preferences.put("author_" + rs.getString("author"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return preferences;
    }

    public List<Integer> getUserFavoriteBooks(int userId, int limit) {
        List<Integer> favoriteBooks = new ArrayList<>();
        String sql = "SELECT target_id, COUNT(*) as interaction_count FROM UserBehavior " +
                    "WHERE user_id = ? AND target_type = 'book' AND action_type IN ('view_book', 'read', 'favorite') " +
                    "GROUP BY target_id ORDER BY interaction_count DESC, MAX(created_at) DESC";
        
        if (limit > 0) {
            sql = "SELECT TOP (" + limit + ") target_id, COUNT(*) as interaction_count FROM UserBehavior " +
                 "WHERE user_id = ? AND target_type = 'book' AND action_type IN ('view_book', 'read', 'favorite') " +
                 "GROUP BY target_id ORDER BY interaction_count DESC, MAX(created_at) DESC";
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Integer bookId = rs.getObject("target_id", Integer.class);
                if (bookId != null) {
                    favoriteBooks.add(bookId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return favoriteBooks;
    }

    public Map<String, Object> getUserChatBehavior(int userId, int days) {
        Map<String, Object> chatBehavior = new HashMap<>();
        
        String sql = "SELECT action_data, COUNT(*) as count FROM UserBehavior " +
                    "WHERE user_id = ? AND action_type = 'chat_question' AND " +
                    "created_at >= DATEADD(DAY, ?, GETDATE()) " +
                    "GROUP BY action_data ORDER BY count DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, -days);
            
            ResultSet rs = stmt.executeQuery();
            Map<String, Integer> topics = new HashMap<>();
            while (rs.next()) {
                String data = rs.getString("action_data");
                int count = rs.getInt("count");
                topics.put(data, count);
            }
            chatBehavior.put("frequent_topics", topics);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return chatBehavior;
    }

    public boolean cleanOldBehaviorData(int daysToKeep) {
        String sql = "DELETE FROM UserBehavior WHERE created_at < DATEADD(DAY, ?, GETDATE())";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, -daysToKeep);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 