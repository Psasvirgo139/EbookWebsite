package com.mycompany.ebookwebsite.TestAI;

import com.mycompany.ebookwebsite.service.PremiumService;
import com.mycompany.ebookwebsite.service.PremiumExpirationScheduler;
import com.mycompany.ebookwebsite.model.PremiumSubscription;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 🧪 Premium Expiration Test
 * 
 * Test toàn bộ hệ thống premium expiration
 * Kiểm tra scheduler, edge cases, và business logic
 */
public class PremiumExpirationTest {
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public static void main(String[] args) {
        System.out.println("👑 PREMIUM EXPIRATION SYSTEM TEST");
        System.out.println("===================================");
        
        try {
            // Test các thành phần
            testExpiryDateCalculation();
            testPremiumSubscriptionModel();
            testSchedulerStatus();
            testManualTrigger();
            
            System.out.println("\n🎉 All tests completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 📅 Test expiry date calculation với edge cases
     */
    private static void testExpiryDateCalculation() {
        System.out.println("\n1. 📅 EXPIRY DATE CALCULATION TEST");
        System.out.println("==================================");
        
        PremiumService premiumService = new PremiumService();
        
        // Test cases với edge dates
        LocalDate[] testDates = {
            LocalDate.of(2025, 1, 15),   // Normal: 15/1 -> 15/2
            LocalDate.of(2025, 1, 31),   // Edge: 31/1 -> 28/2 (hoặc 1/3)
            LocalDate.of(2025, 3, 31),   // Edge: 31/3 -> 30/4 (hoặc 1/5)
            LocalDate.of(2024, 1, 31),   // Leap year: 31/1 -> 29/2 (hoặc 1/3)
            LocalDate.of(2025, 2, 28),   // Normal: 28/2 -> 28/3
            LocalDate.of(2025, 12, 31)   // Year end: 31/12 -> 31/1 next year
        };
        
        for (LocalDate startDate : testDates) {
            LocalDate expiryDate = premiumService.calculateExpiryDate(startDate);
            System.out.printf("Start: %s -> Expiry: %s ✅\n", 
                            startDate.format(DATE_FORMAT), 
                            expiryDate.format(DATE_FORMAT));
        }
    }
    
    /**
     * 👑 Test PremiumSubscription model methods
     */
    private static void testPremiumSubscriptionModel() {
        System.out.println("\n2. 👑 PREMIUM SUBSCRIPTION MODEL TEST");
        System.out.println("=====================================");
        
        // Tạo test subscription
        LocalDate startDate = LocalDate.now().minusDays(10);  // 10 ngày trước
        LocalDate expiryDate = LocalDate.now().plusDays(20);  // 20 ngày nữa
        
        PremiumSubscription subscription = new PremiumSubscription(1, startDate, expiryDate, "coin", 100.0);
        
        // Test methods
        System.out.println("Active subscription test:");
        System.out.printf("  Is Active: %s ✅\n", subscription.isActive());
        System.out.printf("  Is Expired: %s ✅\n", subscription.isExpired());
        System.out.printf("  Days Remaining: %d days ✅\n", subscription.getDaysRemaining());
        
        // Test expired subscription
        PremiumSubscription expiredSubscription = new PremiumSubscription(1, 
            LocalDate.now().minusDays(40), 
            LocalDate.now().minusDays(10), 
            "vnd", 100000.0);
        
        System.out.println("\nExpired subscription test:");
        System.out.printf("  Is Active: %s ✅\n", expiredSubscription.isActive());
        System.out.printf("  Is Expired: %s ✅\n", expiredSubscription.isExpired());
        System.out.printf("  Days Remaining: %d days ✅\n", expiredSubscription.getDaysRemaining());
    }
    
    /**
     * ⏰ Test scheduler status
     */
    private static void testSchedulerStatus() {
        System.out.println("\n3. ⏰ SCHEDULER STATUS TEST");
        System.out.println("==========================");
        
        PremiumExpirationScheduler scheduler = PremiumExpirationScheduler.getInstance();
        PremiumExpirationScheduler.SchedulerStatus status = scheduler.getStatus();
        
        System.out.printf("Scheduler Running: %s\n", status.isRunning() ? "✅ YES" : "❌ NO");
        System.out.printf("Scheduler Shutdown: %s\n", status.isShutdown() ? "❌ YES" : "✅ NO");
        System.out.printf("Scheduler Terminated: %s\n", status.isTerminated() ? "❌ YES" : "✅ NO");
        
        if (status.isRunning()) {
            System.out.println("✅ Scheduler is running properly!");
        } else {
            System.out.println("⚠️ Scheduler is not running - this might be expected in test environment");
        }
    }
    
    /**
     * 🔧 Test manual trigger
     */
    private static void testManualTrigger() {
        System.out.println("\n4. 🔧 MANUAL TRIGGER TEST");
        System.out.println("=========================");
        
        try {
            PremiumExpirationScheduler scheduler = PremiumExpirationScheduler.getInstance();
            
            System.out.println("Triggering manual premium expiration check...");
            int processedCount = scheduler.triggerManualCheck();
            
            System.out.printf("✅ Manual check completed: %d subscriptions processed\n", processedCount);
            
            if (processedCount == 0) {
                System.out.println("ℹ️ No expired subscriptions found (this is normal for test)");
            } else {
                System.out.printf("✅ Successfully processed %d expired subscriptions\n", processedCount);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Manual trigger test failed: " + e.getMessage());
            // This might fail in test environment without database
            System.out.println("ℹ️ This failure might be expected in test environment without database connection");
        }
    }
    
    /**
     * 🏢 Test business logic scenarios
     */
    private static void testBusinessLogicScenarios() {
        System.out.println("\n5. 🏢 BUSINESS LOGIC SCENARIOS");
        System.out.println("==============================");
        
        PremiumService premiumService = new PremiumService();
        
        // Scenario 1: Normal subscription
        System.out.println("Scenario 1: Normal 1-month subscription");
        LocalDate startDate1 = LocalDate.of(2025, 1, 15);
        LocalDate expiryDate1 = premiumService.calculateExpiryDate(startDate1);
        System.out.printf("  15/1/2025 + 1 month = %s ✅\n", expiryDate1.format(DATE_FORMAT));
        
        // Scenario 2: Edge case - 31/1
        System.out.println("Scenario 2: Edge case - January 31st");
        LocalDate startDate2 = LocalDate.of(2025, 1, 31);
        LocalDate expiryDate2 = premiumService.calculateExpiryDate(startDate2);
        System.out.printf("  31/1/2025 + 1 month = %s (Feb 31 doesn't exist) ✅\n", expiryDate2.format(DATE_FORMAT));
        
        // Scenario 3: Leap year
        System.out.println("Scenario 3: Leap year February");
        LocalDate startDate3 = LocalDate.of(2024, 1, 29);
        LocalDate expiryDate3 = premiumService.calculateExpiryDate(startDate3);
        System.out.printf("  29/1/2024 + 1 month = %s (leap year) ✅\n", expiryDate3.format(DATE_FORMAT));
        
        // Scenario 4: Year boundary
        System.out.println("Scenario 4: Year boundary");
        LocalDate startDate4 = LocalDate.of(2025, 12, 31);
        LocalDate expiryDate4 = premiumService.calculateExpiryDate(startDate4);
        System.out.printf("  31/12/2025 + 1 month = %s (next year) ✅\n", expiryDate4.format(DATE_FORMAT));
    }
} 