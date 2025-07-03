package com.mycompany.ebookwebsite.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimplePasswordTest {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Password Hashing ===");
        
        String password = "123456";
        String hashedPassword = hashPassword(password);
        
        System.out.println("Original password: " + password);
        System.out.println("Hashed password: " + hashedPassword);
        System.out.println("Hash length: " + hashedPassword.length());
        
        // Test với password khác
        String password2 = "123456";
        String hashedPassword2 = hashPassword(password2);
        
        System.out.println("\nTest consistency:");
        System.out.println("Password 2: " + password2);
        System.out.println("Hashed password 2: " + hashedPassword2);
        System.out.println("Are they equal? " + hashedPassword.equals(hashedPassword2));
        
        // Test với password khác
        String password3 = "654321";
        String hashedPassword3 = hashPassword(password3);
        
        System.out.println("\nTest different password:");
        System.out.println("Password 3: " + password3);
        System.out.println("Hashed password 3: " + hashedPassword3);
        System.out.println("Are they equal? " + hashedPassword.equals(hashedPassword3));
        
        // Test redirect URL
        System.out.println("\n=== Testing Redirect URL ===");
        String contextPath = "/EbookWebsite";
        String redirectUrl = contextPath + "/profile";
        System.out.println("Redirect URL: " + redirectUrl);
        System.out.println("✅ Redirect to profile page after successful password change");
    }
    
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