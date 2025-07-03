package com.mycompany.ebookwebsite.test;

import java.sql.SQLException;
import java.time.LocalDate;

import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.dao.UserInforDAO;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.model.UserInfor;

public class EditProfileTest {
    
    public static void main(String[] args) {
        testLoadUserInforForEdit();
        testUpdateUserProfile();
    }
    
    public static void testLoadUserInforForEdit() {
        System.out.println("=== Testing Load UserInfor for Edit ===");
        try {
            UserDAO userDAO = new UserDAO();
            UserInforDAO userInforDAO = new UserInforDAO();
            
            // Lấy user đầu tiên để test
            var allUsers = userDAO.findAll();
            if (allUsers.isEmpty()) {
                System.out.println("No users found to test");
                return;
            }
            
            User user = allUsers.get(0);
            System.out.println("Testing with user: " + user.getUsername());
            System.out.println("UserInfor ID: " + user.getUserinforId());
            
            // Load UserInfor
            UserInfor userInfor = null;
            if (user.getUserinforId() != null) {
                userInfor = userInforDAO.selectUserInfor(user.getUserinforId());
                if (userInfor != null) {
                    System.out.println("✅ Loaded UserInfor:");
                    System.out.println("  - Phone: " + userInfor.getPhone());
                    System.out.println("  - Gender: " + userInfor.getGender());
                    System.out.println("  - Birthday: " + userInfor.getBirthDay());
                    System.out.println("  - Address: " + userInfor.getAddress());
                    System.out.println("  - Introduction: " + userInfor.getIntroduction());
                } else {
                    System.out.println("❌ UserInfor not found for ID: " + user.getUserinforId());
                }
            } else {
                System.out.println("ℹ️ User has no UserInfor yet");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void testUpdateUserProfile() {
        System.out.println("\n=== Testing Update User Profile ===");
        try {
            UserDAO userDAO = new UserDAO();
            UserInforDAO userInforDAO = new UserInforDAO();
            
            // Lấy user đầu tiên để test
            var allUsers = userDAO.findAll();
            if (allUsers.isEmpty()) {
                System.out.println("No users found to test");
                return;
            }
            
            User user = allUsers.get(0);
            System.out.println("Testing update for user: " + user.getUsername());
            
            // Load hoặc tạo UserInfor
            UserInfor userInfor = null;
            if (user.getUserinforId() != null) {
                userInfor = userInforDAO.selectUserInfor(user.getUserinforId());
            }
            
            if (userInfor == null) {
                userInfor = new UserInfor();
                System.out.println("Creating new UserInfor");
            } else {
                System.out.println("Updating existing UserInfor ID: " + userInfor.getId());
            }
            
            // Cập nhật thông tin test
            String originalPhone = userInfor.getPhone();
            userInfor.setPhone("0987654321");
            userInfor.setGender("Nam");
            userInfor.setBirthDay(LocalDate.of(1990, 1, 1));
            userInfor.setAddress("Hà Nội - Test");
            userInfor.setIntroduction("Test introduction");
            
            // Lưu vào database
            boolean success = false;
            if (userInfor.getId() == 0) {
                userInforDAO.insertUserInfor(userInfor);
                System.out.println("✅ Created new UserInfor with ID: " + userInfor.getId());
                
                // Cập nhật userinforId trong User
                user.setUserinforId(userInfor.getId());
                success = userDAO.update(user);
            } else {
                success = userInforDAO.updateUserInfor(userInfor);
            }
            
            if (success) {
                System.out.println("✅ Profile updated successfully");
                
                // Verify update
                UserInfor retrieved = userInforDAO.selectUserInfor(userInfor.getId());
                if (retrieved != null && "0987654321".equals(retrieved.getPhone())) {
                    System.out.println("✅ Update verified successfully");
                } else {
                    System.out.println("❌ Update verification failed");
                }
                
                // Restore original phone
                userInfor.setPhone(originalPhone);
                userInforDAO.updateUserInfor(userInfor);
                System.out.println("✅ Restored original phone number");
                
            } else {
                System.out.println("❌ Failed to update profile");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 