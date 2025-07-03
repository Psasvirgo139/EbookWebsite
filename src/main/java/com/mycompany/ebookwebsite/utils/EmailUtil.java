package com.mycompany.ebookwebsite.utils;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

    // Có thể truyền động các tham số này (hoặc dùng config)
    private static final String FROM_EMAIL = "trankimthang0207@gmail.com";     // Địa chỉ gửi
    private static final String FROM_NAME  = "Scroll Team";            // Tên gửi
    private static final String APP_PASSWORD = "mphh pmec vpmy clhl";    // App Password Gmail (không dùng mật khẩu thường!)

    public static void sendEmail(String to, String subject, String content) throws MessagingException {
        // Thiết lập thông tin cấu hình SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");  // Gmail SMTP server
        props.put("mail.smtp.port", "587");             // TLS port
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Kích hoạt TLS

        // Xác thực người gửi
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        // Soạn email
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
        } catch (UnsupportedEncodingException e) {
            message.setFrom(new InternetAddress(FROM_EMAIL));
        }
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(content, "text/html; charset=UTF-8"); // Hỗ trợ HTML

        // Gửi mail
        Transport.send(message);
    }
}
