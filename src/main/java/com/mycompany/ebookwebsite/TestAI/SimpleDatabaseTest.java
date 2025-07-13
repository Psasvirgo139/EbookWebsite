package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.dao.DBConnection;
import com.mycompany.ebookwebsite.utils.Utils;
import com.mycompany.ebookwebsite.model.Ebook;

import java.sql.Connection;
import java.util.List;

/**
 * 🧪 Test đơn giản để kiểm tra database connection
 */
public class SimpleDatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("🚀 Testing Database Connection");
        System.out.println("==============================");
        
        try {
            // Test 1: Kiểm tra kết nối database
            System.out.println("\n📚 Test 1: Kiểm tra kết nối database");
            Connection con = DBConnection.getConnection();
            if (con != null && !con.isClosed()) {
                System.out.println("✅ Database connection successful!");
                con.close();
            } else {
                System.out.println("❌ Database connection failed!");
            }
            
            // Test 2: Lấy sách có sẵn
            System.out.println("\n📚 Test 2: Lấy sách có sẵn từ database");
            List<Ebook> availableBooks = Utils.getAvailableBooks(5);
            System.out.println("✅ Found " + availableBooks.size() + " books in database:");
            for (Ebook book : availableBooks) {
                System.out.println("  • " + book.getTitle() + " (ID: " + book.getId() + ")");
            }
            
            System.out.println("\n✅ All database tests completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 