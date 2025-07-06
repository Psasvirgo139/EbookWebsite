package com.mycompany.ebookwebsite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.ebookwebsite.model.ChatMessage;

public class ChatHistoryDAO {

    public boolean saveChatMessage(ChatMessage chatMessage) {
        String sql = "INSERT INTO ChatHistory (user_id, session_id, message, response, context_type, context_id, embedding_used) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, chatMessage.getUserId());
            stmt.setString(2, chatMessage.getSessionId());
            stmt.setString(3, chatMessage.getMessage());
            stmt.setString(4, chatMessage.getResponse());
            stmt.setString(5, chatMessage.getContextType());
            if (chatMessage.getContextId() != null) {
                stmt.setInt(6, chatMessage.getContextId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setBoolean(7, chatMessage.isEmbeddingUsed());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ChatMessage> getChatHistory(int userId, String sessionId, int limit) {
        List<ChatMessage> messages = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM ChatHistory WHERE user_id = ? AND session_id = ? " +
                    "ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            stmt.setInt(2, userId);
            stmt.setString(3, sessionId);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ChatMessage msg = new ChatMessage();
                msg.setId(rs.getInt("id"));
                msg.setUserId(rs.getInt("user_id"));
                msg.setSessionId(rs.getString("session_id"));
                msg.setMessage(rs.getString("message"));
                msg.setResponse(rs.getString("response"));
                msg.setContextType(rs.getString("context_type"));
                msg.setContextId(rs.getObject("context_id", Integer.class));
                msg.setCreatedAt(rs.getTimestamp("created_at"));
                msg.setEmbeddingUsed(rs.getBoolean("embedding_used"));
                messages.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Reverse để có thứ tự chronological
        List<ChatMessage> chronological = new ArrayList<>();
        for (int i = messages.size() - 1; i >= 0; i--) {
            chronological.add(messages.get(i));
        }
        
        return chronological;
    }

    public List<ChatMessage> getRecentChatHistory(int userId, int hours) {
        List<ChatMessage> messages = new ArrayList<>();
        String sql = "SELECT * FROM ChatHistory WHERE user_id = ? AND created_at >= DATEADD(HOUR, ?, GETDATE()) " +
                    "ORDER BY created_at ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, -hours); // Negative for past hours
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ChatMessage msg = new ChatMessage();
                msg.setId(rs.getInt("id"));
                msg.setUserId(rs.getInt("user_id"));
                msg.setSessionId(rs.getString("session_id"));
                msg.setMessage(rs.getString("message"));
                msg.setResponse(rs.getString("response"));
                msg.setContextType(rs.getString("context_type"));
                msg.setContextId(rs.getObject("context_id", Integer.class));
                msg.setCreatedAt(rs.getTimestamp("created_at"));
                msg.setEmbeddingUsed(rs.getBoolean("embedding_used"));
                messages.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return messages;
    }

    public boolean deleteChatHistory(int userId, String sessionId) {
        String sql = "DELETE FROM ChatHistory WHERE user_id = ? AND session_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, sessionId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 