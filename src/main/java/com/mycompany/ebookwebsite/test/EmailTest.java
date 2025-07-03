package com.mycompany.ebookwebsite.test;

import java.sql.SQLException;
import java.util.logging.Logger;

import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.utils.EmailUtil;
import com.mycompany.ebookwebsite.utils.TokenUtil;

/**
 * Class test để kiểm tra chức năng gửi email và luồng quên mật khẩu
 */
public class EmailTest {
    
    private static final Logger LOGGER = Logger.getLogger(EmailTest.class.getName());
    
    public static void main(String[] args) {
        System.out.println("🧪 BẮT ĐẦU TEST CHỨC NĂNG QUÊN MẬT KHẨU");
        System.out.println("==========================================");
        
        // Test 1: Kiểm tra kết nối database
        testDatabaseConnection();
        
        // Test 2: Kiểm tra tìm user bằng email
        testFindUserByEmail();
        
        // Test 3: Kiểm tra tạo token
        testTokenCreation();
        
        // Test 4: Kiểm tra gửi email
        testEmailSending();
        
        // Test 5: Test toàn bộ luồng quên mật khẩu
        testFullForgotPasswordFlow();
        
        System.out.println("\n✅ HOÀN THÀNH TẤT CẢ CÁC TEST");
    }
    
    /**
     * Test kết nối database
     */
    private static void testDatabaseConnection() {
        System.out.println("\n📊 Test 1: Kiểm tra kết nối database");
        try {
            UserDAO userDAO = new UserDAO();
            // Thử lấy một user để kiểm tra kết nối
            User testUser = userDAO.findByEmail("test@example.com");
            System.out.println("✅ Kết nối database thành công");
            if (testUser != null) {
                System.out.println("   - Tìm thấy user: " + testUser.getUsername());
            } else {
                System.out.println("   - Không tìm thấy user test@example.com (bình thường)");
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test tìm user bằng email
     */
    private static void testFindUserByEmail() {
        System.out.println("\n🔍 Test 2: Kiểm tra tìm user bằng email");
        try {
            UserDAO userDAO = new UserDAO();
            
            // Test với email không tồn tại
            User user1 = userDAO.findByEmail("nonexistent@example.com");
            System.out.println("   - Email không tồn tại: " + (user1 == null ? "✅ Đúng" : "❌ Sai"));
            
            // Test với email có thể tồn tại (thay bằng email thật trong DB)
            User user2 = userDAO.findByEmail("admin@example.com");
            if (user2 != null) {
                System.out.println("   - Tìm thấy user: " + user2.getUsername() + " (ID: " + user2.getId() + ")");
                System.out.println("   - Email: " + user2.getEmail());
                System.out.println("   - Role: " + user2.getRole());
            } else {
                System.out.println("   - Không tìm thấy user admin@example.com");
                System.out.println("   - Hãy thay bằng email thật trong database của bạn");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Lỗi tìm user: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test tạo token
     */
    private static void testTokenCreation() {
        System.out.println("\n🔑 Test 3: Kiểm tra tạo token");
        try {
            String email = "test@example.com";
            String token = TokenUtil.createAndSaveToken(email);
            System.out.println("✅ Tạo token thành công");
            System.out.println("   - Email: " + email);
            System.out.println("   - Token: " + token);
            System.out.println("   - Token length: " + token.length());
            
            // Test kiểm tra token
            try {
                int userId = TokenUtil.checkResetToken(token);
                System.out.println("   - Token hợp lệ, User ID: " + userId);
            } catch (Exception e) {
                System.err.println("   - Token không hợp lệ: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi tạo token: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test gửi email
     */
    private static void testEmailSending() {
        System.out.println("\n📧 Test 4: Kiểm tra gửi email");
        
        // Thay đổi email này thành email thật để test
        String testEmail = "your-email@gmail.com"; // Thay bằng email thật
        
        System.out.println("   - Email gửi đến: " + testEmail);
        System.out.println("   - Email gửi từ: " + EmailUtil.class.getDeclaredFields()[0].getName());
        
        try {
            String subject = "Test Email - Scroll Website";
            String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<h2 style='color: #3e2f92;'>Test Email</h2>"
                    + "<p>Đây là email test từ hệ thống Scroll.</p>"
                    + "<p>Nếu bạn nhận được email này, chức năng gửi email đã hoạt động!</p>"
                    + "<div style='background: #f0f0f0; padding: 15px; border-radius: 5px; margin: 20px 0;'>"
                    + "<strong>Thông tin test:</strong><br>"
                    + "- Thời gian: " + java.time.LocalDateTime.now() + "<br>"
                    + "- Hệ thống: Scroll Website<br>"
                    + "- Chức năng: Quên mật khẩu"
                    + "</div>"
                    + "<p style='color: #666; font-size: 12px;'>Email test - vui lòng bỏ qua.</p>"
                    + "</div>";
            
            EmailUtil.sendEmail(testEmail, subject, content);
            System.out.println("✅ Gửi email thành công!");
            System.out.println("   - Hãy kiểm tra hộp thư của " + testEmail);
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi gửi email: " + e.getMessage());
            e.printStackTrace();
            
            // Gợi ý khắc phục
            System.out.println("\n💡 Gợi ý khắc phục:");
            System.out.println("1. Kiểm tra cấu hình SMTP trong EmailUtil.java");
            System.out.println("2. Đảm bảo App Password Gmail đúng");
            System.out.println("3. Kiểm tra kết nối internet");
            System.out.println("4. Thử với email khác");
        }
    }
    
    /**
     * Test toàn bộ luồng quên mật khẩu
     */
    private static void testFullForgotPasswordFlow() {
        System.out.println("\n🔄 Test 5: Test toàn bộ luồng quên mật khẩu");
        
        // Thay đổi email này thành email thật có trong database
        String testEmail = "admin@example.com"; // Thay bằng email thật trong DB
        
        try {
            System.out.println("   - Email test: " + testEmail);
            
            // Bước 1: Tìm user
            UserDAO userDAO = new UserDAO();
            User user = userDAO.findByEmail(testEmail);
            
            if (user == null) {
                System.out.println("   ❌ Không tìm thấy user với email: " + testEmail);
                System.out.println("   💡 Hãy thay đổi testEmail thành email thật trong database");
                return;
            }
            
            System.out.println("   ✅ Tìm thấy user: " + user.getUsername());
            
            // Bước 2: Tạo token
            String token = TokenUtil.createAndSaveToken(testEmail);
            System.out.println("   ✅ Tạo token thành công: " + token.substring(0, 10) + "...");
            
            // Bước 3: Tạo link reset
            String resetLink = "http://localhost:8080/EbookWebsite/reset-password?token=" + token;
            System.out.println("   ✅ Tạo link reset: " + resetLink);
            
            // Bước 4: Gửi email
            String subject = "Test - Yêu cầu đặt lại mật khẩu - Scroll";
            String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<h2 style='color: #3e2f92;'>Xin chào " + user.getUsername() + "!</h2>"
                    + "<p>Đây là email test cho chức năng quên mật khẩu.</p>"
                    + "<p>Nhấn vào nút bên dưới để test link đặt lại mật khẩu:</p>"
                    + "<div style='text-align: center; margin: 30px 0;'>"
                    + "<a href='" + resetLink + "' style='background: #3e2f92; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; display: inline-block;'>Test Đặt lại mật khẩu</a>"
                    + "</div>"
                    + "<p><strong>Thông tin test:</strong></p>"
                    + "<ul>"
                    + "<li>Token: " + token + "</li>"
                    + "<li>User ID: " + user.getId() + "</li>"
                    + "<li>Thời gian: " + java.time.LocalDateTime.now() + "</li>"
                    + "</ul>"
                    + "<hr style='margin: 30px 0; border: none; border-top: 1px solid #eee;'>"
                    + "<p style='color: #666; font-size: 12px;'>Email test - vui lòng bỏ qua nếu không phải bạn yêu cầu.</p>"
                    + "</div>";
            
            EmailUtil.sendEmail(testEmail, subject, content);
            System.out.println("   ✅ Gửi email thành công!");
            System.out.println("   📧 Hãy kiểm tra hộp thư của " + testEmail);
            
            // Bước 5: Test kiểm tra token
            try {
                int userId = TokenUtil.checkResetToken(token);
                System.out.println("   ✅ Token hợp lệ, User ID: " + userId);
            } catch (Exception e) {
                System.err.println("   ❌ Token không hợp lệ: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("   ❌ Lỗi trong luồng test: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 