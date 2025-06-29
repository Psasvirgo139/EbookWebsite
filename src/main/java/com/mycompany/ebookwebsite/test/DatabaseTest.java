package com.mycompany.ebookwebsite.test;

import com.mycompany.ebookwebsite.dao.DBConnection;
import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.dao.UserInforDAO;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.model.UserInfor;
import java.sql.*;

public class DatabaseTest {
    
    public static void main(String[] args) {
        testDatabaseConnection();
        testUserInforTable();
        testUserTable();
    }
    
    public static void testDatabaseConnection() {
        System.out.println("=== Testing Database Connection ===");
        try (Connection con = DBConnection.getConnection()) {
            if (con != null) {
                System.out.println("✅ Database connection successful!");
                
                // Test UserInfor table structure
                DatabaseMetaData metaData = con.getMetaData();
                ResultSet columns = metaData.getColumns(null, null, "UserInfor", null);
                
                System.out.println("\n=== UserInfor Table Structure ===");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String dataType = columns.getString("TYPE_NAME");
                    System.out.println(columnName + " - " + dataType);
                }
                
                // Test Users table structure
                ResultSet userColumns = metaData.getColumns(null, null, "Users", null);
                
                System.out.println("\n=== Users Table Structure ===");
                while (userColumns.next()) {
                    String columnName = userColumns.getString("COLUMN_NAME");
                    String dataType = userColumns.getString("TYPE_NAME");
                    System.out.println(columnName + " - " + dataType);
                }
                
            } else {
                System.out.println("❌ Database connection failed!");
            }
        } catch (Exception e) {
            System.out.println("❌ Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void testUserInforTable() {
        System.out.println("\n=== Testing UserInfor Table ===");
        try {
            UserInforDAO userInforDAO = new UserInforDAO();
            
            // Test select all
            System.out.println("Testing select all UserInfor...");
            var allUserInfor = userInforDAO.selectAllUserInfor();
            System.out.println("Found " + allUserInfor.size() + " UserInfor records");
            
            for (UserInfor ui : allUserInfor) {
                System.out.println("ID: " + ui.getId() + 
                                 ", Phone: " + ui.getPhone() + 
                                 ", Birthday: " + ui.getBirthDay() + 
                                 ", Gender: " + ui.getGender());
            }
            
            // Test select by ID
            if (!allUserInfor.isEmpty()) {
                System.out.println("\nTesting select UserInfor by ID...");
                UserInfor userInfor = userInforDAO.selectUserInfor(allUserInfor.get(0).getId());
                if (userInfor != null) {
                    System.out.println("✅ Found UserInfor: " + userInfor.getPhone());
                } else {
                    System.out.println("❌ UserInfor not found");
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ UserInfor test error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void testUserTable() {
        System.out.println("\n=== Testing Users Table ===");
        try {
            UserDAO userDAO = new UserDAO();
            
            // Test select all
            System.out.println("Testing select all Users...");
            var allUsers = userDAO.findAll();
            System.out.println("Found " + allUsers.size() + " User records");
            
            for (User user : allUsers) {
                System.out.println("ID: " + user.getId() + 
                                 ", Username: " + user.getUsername() + 
                                 ", Email: " + user.getEmail() + 
                                 ", UserInforID: " + user.getUserinforId());
            }
            
            // Test select by ID
            if (!allUsers.isEmpty()) {
                System.out.println("\nTesting select User by ID...");
                User user = userDAO.findById(allUsers.get(0).getId());
                if (user != null) {
                    System.out.println("✅ Found User: " + user.getUsername());
                } else {
                    System.out.println("❌ User not found");
                }
            }
            
        } catch (Exception e) {
            System.out.println("❌ Users test error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 