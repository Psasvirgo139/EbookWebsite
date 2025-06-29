package com.mycompany.ebookwebsite.test;

import java.sql.SQLException;
import java.time.LocalDate;

import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.dao.UserInforDAO;
import com.mycompany.ebookwebsite.model.UserInfor;

public class ProfileTest {
    
    public static void main(String[] args) {
        testCreateUserInfor();
        testUpdateUserInfor();
    }
    
    public static void testCreateUserInfor() {
        System.out.println("=== Testing Create UserInfor ===");
        try {
            UserInforDAO userInforDAO = new UserInforDAO();
            UserDAO userDAO = new UserDAO();
            
            // Tạo UserInfor mới
            UserInfor userInfor = new UserInfor();
            userInfor.setPhone("0123456789");
            userInfor.setBirthDay(LocalDate.of(1990, 1, 1));
            userInfor.setGender("Nam");
            userInfor.setAddress("Hà Nội");
            userInfor.setIntroduction("Test user");
            
            System.out.println("Before insert - ID: " + userInfor.getId());
            
            // Insert UserInfor
            userInforDAO.insertUserInfor(userInfor);
            
            System.out.println("After insert - ID: " + userInfor.getId());
            
            if (userInfor.getId() > 0) {
                System.out.println("✅ UserInfor created successfully with ID: " + userInfor.getId());
                
                // Test select by ID
                UserInfor retrieved = userInforDAO.selectUserInfor(userInfor.getId());
                if (retrieved != null) {
                    System.out.println("✅ Retrieved UserInfor: " + retrieved.getPhone());
                } else {
                    System.out.println("❌ Failed to retrieve UserInfor");
                }
            } else {
                System.out.println("❌ Failed to create UserInfor - ID is 0");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ SQL Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void testUpdateUserInfor() {
        System.out.println("\n=== Testing Update UserInfor ===");
        try {
            UserInforDAO userInforDAO = new UserInforDAO();
            
            // Lấy UserInfor đầu tiên để test update
            var allUserInfor = userInforDAO.selectAllUserInfor();
            if (allUserInfor.isEmpty()) {
                System.out.println("No UserInfor found to test update");
                return;
            }
            
            UserInfor userInfor = allUserInfor.get(0);
            System.out.println("Testing update for UserInfor ID: " + userInfor.getId());
            
            // Update thông tin
            String originalPhone = userInfor.getPhone();
            userInfor.setPhone("0987654321");
            
            boolean success = userInforDAO.updateUserInfor(userInfor);
            
            if (success) {
                System.out.println("✅ UserInfor updated successfully");
                
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
                System.out.println("❌ Failed to update UserInfor");
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