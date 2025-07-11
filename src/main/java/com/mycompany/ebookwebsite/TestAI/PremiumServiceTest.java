package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.service.PremiumService;
import java.time.LocalDate;

/**
 * 🧪 Premium Service Test
 * 
 * Test logic tính expiry date với edge cases
 */
public class PremiumServiceTest {
    
    public static void main(String[] args) {
        System.out.println("👑 PREMIUM SERVICE TEST");
        System.out.println("======================");
        
        PremiumService premiumService = new PremiumService();
        
        // Test cases
        testExpiryDateCalculation(premiumService);
        testEdgeCases(premiumService);
        
        System.out.println("\n🎉 All tests completed!");
    }
    
    /**
     * 📅 Test normal expiry date calculation
     */
    private static void testExpiryDateCalculation(PremiumService service) {
        System.out.println("\n1. 📅 NORMAL EXPIRY DATE CALCULATION:");
        System.out.println("====================================");
        
        // Test cases bình thường
        LocalDate[] testDates = {
            LocalDate.of(2025, 1, 15),  // 15/1/2025
            LocalDate.of(2025, 2, 28),  // 28/2/2025
            LocalDate.of(2025, 6, 10),  // 10/6/2025
            LocalDate.of(2025, 12, 25)  // 25/12/2025
        };
        
        for (LocalDate startDate : testDates) {
            LocalDate expiryDate = service.calculateExpiryDate(startDate);
            System.out.printf("Start: %s -> Expiry: %s ✅\n", startDate, expiryDate);
        }
    }
    
    /**
     * ⚠️ Test edge cases
     */
    private static void testEdgeCases(PremiumService service) {
        System.out.println("\n2. ⚠️ EDGE CASES TESTING:");
        System.out.println("========================");
        
        // Edge cases: ngày không tồn tại
        EdgeCaseTest[] edgeTests = {
            new EdgeCaseTest(LocalDate.of(2025, 1, 31), "Jan 31 -> Feb 31 doesn't exist"),
            new EdgeCaseTest(LocalDate.of(2025, 3, 31), "Mar 31 -> Apr 31 doesn't exist"),
            new EdgeCaseTest(LocalDate.of(2025, 5, 31), "May 31 -> Jun 31 doesn't exist"),
            new EdgeCaseTest(LocalDate.of(2025, 8, 31), "Aug 31 -> Sep 31 doesn't exist"),
            new EdgeCaseTest(LocalDate.of(2025, 10, 31), "Oct 31 -> Nov 31 doesn't exist"),
            new EdgeCaseTest(LocalDate.of(2025, 12, 31), "Dec 31 -> Jan 31 (next year)"),
            
            // Leap year cases
            new EdgeCaseTest(LocalDate.of(2024, 1, 31), "Jan 31 -> Feb 31 (leap year)"),
            new EdgeCaseTest(LocalDate.of(2025, 1, 30), "Jan 30 -> Feb 30 doesn't exist"),
            new EdgeCaseTest(LocalDate.of(2025, 1, 29), "Jan 29 -> Feb 29 doesn't exist")
        };
        
        for (EdgeCaseTest test : edgeTests) {
            LocalDate calculated = service.calculateExpiryDate(test.startDate);
            
            // Validate result
            String status = validateEdgeCase(test.startDate, calculated);
            
            System.out.printf("Start: %s -> Expiry: %s %s\n", 
                             test.startDate, calculated, status);
            System.out.printf("   📝 %s\n", test.description);
            System.out.println();
        }
    }
    
    /**
     * ✅ Validate edge case result
     */
    private static String validateEdgeCase(LocalDate startDate, LocalDate calculatedExpiry) {
        // Kiểm tra expiry date có hợp lệ không
        if (calculatedExpiry == null) {
            return "❌ INVALID";
        }
        
        // Kiểm tra expiry date phải sau start date
        if (!calculatedExpiry.isAfter(startDate)) {
            return "❌ EXPIRY NOT AFTER START";
        }
        
        // Kiểm tra khoảng cách hợp lý (28-40 ngày)
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, calculatedExpiry);
        if (daysBetween < 28 || daysBetween > 40) {
            return String.format("⚠️ UNUSUAL DURATION (%d days)", daysBetween);
        }
        
        // Kiểm tra logic đặc biệt cho edge cases
        if (isEdgeCase(startDate)) {
            if (isFirstDayOfMonth(calculatedExpiry)) {
                return String.format("✅ EDGE CASE HANDLED (%d days)", daysBetween);
            } else {
                return String.format("⚠️ EDGE CASE CHECK (%d days)", daysBetween);
            }
        }
        
        return String.format("✅ NORMAL (%d days)", daysBetween);
    }
    
    /**
     * 🔍 Check if date is edge case (31st of certain months)
     */
    private static boolean isEdgeCase(LocalDate date) {
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        
        // Ngày 31 của các tháng mà tháng tiếp theo không có ngày 31
        return day == 31 && (month == 1 || month == 3 || month == 5 || 
                            month == 8 || month == 10 || month == 12);
    }
    
    /**
     * 📅 Check if date is first day of month
     */
    private static boolean isFirstDayOfMonth(LocalDate date) {
        return date.getDayOfMonth() == 1;
    }
    
    /**
     * 📊 Edge case test data
     */
    private static class EdgeCaseTest {
        final LocalDate startDate;
        final String description;
        
        EdgeCaseTest(LocalDate startDate, String description) {
            this.startDate = startDate;
            this.description = description;
        }
    }
} 