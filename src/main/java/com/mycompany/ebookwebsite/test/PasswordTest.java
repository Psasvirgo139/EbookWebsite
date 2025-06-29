package com.mycompany.ebookwebsite.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.UserService;

public class PasswordTest {
    
    public static void main(String[] args) {
        testPasswordHashing();
        testPasswordValidation();
        testPasswordChange();
    }
    
    public static void testPasswordHashing() {
        System.out.println("=== Testing Password Hashing ===");
        
        String password = "123456";
        String hashedPassword = hashPassword(password);
        
        System.out.println("Original password: " + password);
        System.out.println("Hashed password: " + hashedPassword);
        System.out.println("Hash length: " + hashedPassword.length());
        
        // Test verification
        boolean isValid = hashedPassword.equals(hashPassword(password));
        System.out.println("Password verification: " + isValid);
        
        // Test wrong password
        boolean isWrongValid = hashedPassword.equals(hashPassword("wrongpassword"));
        System.out.println("Wrong password verification: " + isWrongValid);
    }
    
    public static void testPasswordValidation() {
        System.out.println("\n=== Testing Password Validation ===");
        
        try {
            UserService userService = new UserService();
            UserDAO userDAO = new UserDAO();
            
            // Test with existing user (assuming user exists)
            String username = "giangtran";
            String password = "123456";
            
            // Lấy user từ database
            User user = userDAO.findByUsername(username);
            if (user != null) {
                System.out.println("✅ Found user: " + user.getUsername());
                System.out.println("Password hash in database: " + user.getPasswordHash());
                
                // Băm mật khẩu để đối chiếu
                String hashedPassword = hashPassword(password);
                System.out.println("Hashed password for comparison: " + hashedPassword);
                
                // So sánh
                if (hashedPassword.equals(user.getPasswordHash())) {
                    System.out.println("✅ Password validation successful");
                } else {
                    System.out.println("❌ Password validation failed");
                }
            } else {
                System.out.println("❌ User not found: " + username);
            }
            
            // Test login
            User loginUser = userService.authenticateUserByUsernameOrEmail(username, password);
            if (loginUser != null) {
                System.out.println("✅ Login successful for user: " + loginUser.getUsername());
            } else {
                System.out.println("❌ Login failed for user: " + username);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void testPasswordChange() {
        System.out.println("\n=== Testing Password Change ===");
        
        try {
            UserService userService = new UserService();
            UserDAO userDAO = new UserDAO();
            
            // Get existing user
            User user = userDAO.findByUsername("giangtran");
            if (user == null) {
                System.out.println("User not found for testing");
                return;
            }
            
            System.out.println("Testing password change for user: " + user.getUsername());
            System.out.println("Current password hash: " + user.getPasswordHash());
            
            // Test new password
            String newPassword = "newpassword123";
            
            // Update password
            boolean success = userService.updatePassword(user.getId(), newPassword);
            
            if (success) {
                System.out.println("✅ Password updated successfully");
                
                // Verify the new password
                User updatedUser = userDAO.findById(user.getId());
                System.out.println("New password hash: " + updatedUser.getPasswordHash());
                System.out.println("Hash length: " + updatedUser.getPasswordHash().length());
                
                // Test login with new password
                User loginUser = userService.authenticateUserByUsernameOrEmail(user.getUsername(), newPassword);
                if (loginUser != null) {
                    System.out.println("✅ Login with new password successful");
                } else {
                    System.out.println("❌ Login with new password failed");
                }
                
                // Test login with old password
                User oldLoginUser = userService.authenticateUserByUsernameOrEmail(user.getUsername(), "123456");
                if (oldLoginUser != null) {
                    System.out.println("❌ Login with old password still works (should not)");
                } else {
                    System.out.println("✅ Login with old password correctly failed");
                }
                
            } else {
                System.out.println("❌ Password update failed");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Hash password bằng SHA-256
     */
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
} 