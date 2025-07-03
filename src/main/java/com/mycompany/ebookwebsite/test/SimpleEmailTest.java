package com.mycompany.ebookwebsite.test;

import com.mycompany.ebookwebsite.utils.EmailUtil;

import jakarta.mail.MessagingException;

/**
 * Test đơn giản để kiểm tra chức năng gửi email
 */
public class SimpleEmailTest {
    
    public static void main(String[] args) {
        System.out.println("🧪 TEST GỬI EMAIL ĐƠN GIẢN");
        System.out.println("============================");
        
        // Thay đổi email này thành email thật của bạn
        String testEmail = "trankimthang857@gmail.com"; // ⚠️ THAY BẰNG EMAIL THẬT
        
        System.out.println("📧 Email gửi đến: " + testEmail);
        System.out.println("📧 Email gửi từ: trankimthang0207@gmail.com");
        System.out.println("🔑 App Password: mphh pmec vpmy clhl");
        
        System.out.println("\n💡 HƯỚNG DẪN:");
        System.out.println("1. Thay đổi 'your-email@gmail.com' thành email thật của bạn");
        System.out.println("2. Chạy lại main method");
        System.out.println("3. Kiểm tra hộp thư (cả Spam/Junk)");
        
        // Kiểm tra xem email đã được thay đổi chưa
        if (testEmail.equals("your-email@gmail.com")) {
            System.out.println("\n⚠️ CHÚ Ý: Bạn chưa thay đổi email!");
            System.out.println("Hãy thay đổi dòng 15 thành email thật của bạn");
            return;
        }
        
        try {
            String subject = "Test Email - " + java.time.LocalDateTime.now();
            String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                    + "<h1 style='color: #3e2f92;'>Test Email - Scroll Website</h1>"
                    + "<p>Nếu bạn nhận được email này, chức năng gửi email đã hoạt động!</p>"
                    + "<div style='background: #f0f0f0; padding: 15px; border-radius: 5px; margin: 20px 0;'>"
                    + "<strong>Thông tin test:</strong><br>"
                    + "- Thời gian: " + java.time.LocalDateTime.now() + "<br>"
                    + "- Hệ thống: Scroll Website<br>"
                    + "- Chức năng: Quên mật khẩu<br>"
                    + "- Email nhận: " + testEmail
                    + "</div>"
                    + "<p style='color: #666; font-size: 12px;'>Email test - vui lòng bỏ qua.</p>"
                    + "</div>";
            
            System.out.println("\n🔄 Đang gửi email...");
            EmailUtil.sendEmail(testEmail, subject, content);
            
            System.out.println("✅ GỬI EMAIL THÀNH CÔNG!");
            System.out.println("📬 Hãy kiểm tra hộp thư của " + testEmail);
            System.out.println("📬 Kiểm tra cả thư mục Spam/Junk nếu không thấy");
            
            System.out.println("\n🎯 TIẾP THEO:");
            System.out.println("1. Nếu nhận được email → Chức năng email hoạt động tốt");
            System.out.println("2. Test luồng quên mật khẩu trên web");
            System.out.println("3. Sử dụng email có trong database để test");
            
        } catch (MessagingException e) {
            System.err.println("❌ LỖI GỬI EMAIL:");
            System.err.println("   - Lỗi: " + e.getMessage());
            e.printStackTrace();
            
            System.out.println("\n💡 GỢI Ý KHẮC PHỤC:");
            System.out.println("1. Kiểm tra App Password Gmail có đúng không");
            System.out.println("2. Đảm bảo 2FA đã được bật cho Gmail");
            System.out.println("3. Kiểm tra kết nối internet");
            System.out.println("4. Thử với email khác");
            System.out.println("5. Kiểm tra firewall/antivirus có chặn không");
        } catch (Exception e) {
            System.err.println("❌ LỖI KHÁC:");
            System.err.println("   - Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 