package com.mycompany.ebookwebsite.test;

import com.mycompany.ebookwebsite.dao.DBConnection;
import java.sql.*;

public class DatabaseTest {

    public static void main(String[] args) {
        System.out.println("=== DATABASE VOTE SYSTEM TEST ===");
        
        try (Connection con = DBConnection.getConnection()) {
            System.out.println("✅ Database connection successful");
            
            // 1. Check if CommentVotes table exists
            System.out.println("\n--- Checking CommentVotes table ---");
            try {
                String checkVotesTable = "SELECT COUNT(*) FROM CommentVotes";
                try (PreparedStatement ps = con.prepareStatement(checkVotesTable)) {
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        System.out.println("✅ CommentVotes table exists with " + rs.getInt(1) + " records");
                    }
                }
            } catch (SQLException e) {
                System.out.println("❌ CommentVotes table NOT FOUND: " + e.getMessage());
                
                // Try to create the table
                System.out.println("--- Creating CommentVotes table ---");
                String createTableSQL = "CREATE TABLE CommentVotes (" +
                    "user_id INT NOT NULL, " +
                    "comment_id INT NOT NULL, " +
                    "value INT NOT NULL, " +
                    "created_at DATETIME DEFAULT GETDATE(), " +
                    "PRIMARY KEY (user_id, comment_id), " +
                    "FOREIGN KEY (comment_id) REFERENCES Comments(id) ON DELETE CASCADE" +
                    ")";
                
                try (Statement stmt = con.createStatement()) {
                    stmt.executeUpdate(createTableSQL);
                    System.out.println("✅ CommentVotes table created successfully");
                } catch (SQLException ex) {
                    System.out.println("❌ Failed to create CommentVotes table: " + ex.getMessage());
                }
            }
            
            // 2. Check if CommentLikes table exists (old system)
            System.out.println("\n--- Checking CommentLikes table (old system) ---");
            try {
                String checkLikesTable = "SELECT COUNT(*) FROM CommentLikes";
                try (PreparedStatement ps = con.prepareStatement(checkLikesTable)) {
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        System.out.println("⚠️ CommentLikes table exists with " + rs.getInt(1) + " records (old system)");
                    }
                }
            } catch (SQLException e) {
                System.out.println("✅ CommentLikes table not found (good, using new system)");
            }
            
            // 3. Test vote operations
            System.out.println("\n--- Testing Vote Operations ---");
            
            // Get a test comment ID
            String getCommentSQL = "SELECT TOP 1 id FROM Comments WHERE ebook_id IS NOT NULL";
            int testCommentId = 0;
            try (PreparedStatement ps = con.prepareStatement(getCommentSQL)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    testCommentId = rs.getInt("id");
                    System.out.println("Using test comment ID: " + testCommentId);
                } else {
                    System.out.println("❌ No comments found for testing");
                    return;
                }
            }
            
            // Test insert vote
            int testUserId = 1; // Assuming user ID 1 exists
            System.out.println("Testing vote insert for userId=" + testUserId + ", commentId=" + testCommentId);
            
            String insertVoteSQL = "INSERT INTO CommentVotes (user_id, comment_id, value) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insertVoteSQL)) {
                ps.setInt(1, testUserId);
                ps.setInt(2, testCommentId);
                ps.setInt(3, 1); // Like
                int affected = ps.executeUpdate();
                System.out.println("✅ Vote insert successful, affected rows: " + affected);
                
                // Test count votes
                String countSQL = "SELECT COUNT(*) FROM CommentVotes WHERE comment_id = ? AND value = ?";
                try (PreparedStatement countPs = con.prepareStatement(countSQL)) {
                    countPs.setInt(1, testCommentId);
                    countPs.setInt(2, 1);
                    ResultSet countRs = countPs.executeQuery();
                    if (countRs.next()) {
                        System.out.println("✅ Like count for comment " + testCommentId + ": " + countRs.getInt(1));
                    }
                }
                
                // Clean up test data
                String deleteSQL = "DELETE FROM CommentVotes WHERE user_id = ? AND comment_id = ?";
                try (PreparedStatement deletePs = con.prepareStatement(deleteSQL)) {
                    deletePs.setInt(1, testUserId);
                    deletePs.setInt(2, testCommentId);
                    deletePs.executeUpdate();
                    System.out.println("✅ Test data cleaned up");
                }
                
            } catch (SQLException e) {
                System.out.println("❌ Vote test failed: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 4. Check Comments table structure
            System.out.println("\n--- Checking Comments table structure ---");
            try {
                String metaSQL = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Comments'";
                try (PreparedStatement ps = con.prepareStatement(metaSQL)) {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        System.out.println("Column: " + rs.getString("COLUMN_NAME") + " - Type: " + rs.getString("DATA_TYPE"));
                    }
                }
            } catch (SQLException e) {
                System.out.println("❌ Error checking Comments table: " + e.getMessage());
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== TEST COMPLETED ===");
    }
} 