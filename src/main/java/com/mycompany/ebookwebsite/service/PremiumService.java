package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.PremiumSubscriptionDAO;
import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.model.PremiumSubscription;
import com.mycompany.ebookwebsite.model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 👑 Premium Service
 * 
 * Xử lý logic nghiệp vụ cho premium subscriptions
 * Includes smart expiry date calculation với edge cases
 */
public class PremiumService {
    
    private static final Logger logger = Logger.getLogger(PremiumService.class.getName());
    
    private final PremiumSubscriptionDAO subscriptionDAO;
    private final UserDAO userDAO;
    
    public PremiumService() {
        this.subscriptionDAO = new PremiumSubscriptionDAO();
        this.userDAO = new UserDAO();
    }
    
    public PremiumService(PremiumSubscriptionDAO subscriptionDAO, UserDAO userDAO) {
        this.subscriptionDAO = subscriptionDAO;
        this.userDAO = userDAO;
    }
    
    /**
     * 🎯 Kiểm tra user có premium active không
     */
    public boolean isPremiumUser(int userId) {
        try {
            PremiumSubscription activeSubscription = subscriptionDAO.getActiveSubscription(userId);
            return activeSubscription != null && activeSubscription.isActive();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking premium status for user " + userId, e);
            return false;
        }
    }
    
    /**
     * 📅 Lấy thông tin premium subscription của user
     */
    public PremiumSubscription getUserPremiumSubscription(int userId) {
        try {
            return subscriptionDAO.getActiveSubscription(userId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting premium subscription for user " + userId, e);
            return null;
        }
    }
    
    /**
     * 💳 Tạo premium subscription mới
     */
    public PremiumSubscription createPremiumSubscription(int userId, String paymentMethod, double amount) throws SQLException {
        LocalDate startDate = LocalDate.now();
        LocalDate expiryDate = calculateExpiryDate(startDate);
        
        PremiumSubscription subscription = new PremiumSubscription(userId, startDate, expiryDate, paymentMethod, amount);
        
        // Tắt subscription cũ nếu có
        deactivateOldSubscriptions(userId);
        
        // Tạo subscription mới
        int subscriptionId = subscriptionDAO.insertSubscription(subscription);
        
        // Update user role thành premium
        updateUserRole(userId, "premium");
        
        logger.info(String.format("Created premium subscription: userId=%d, expiryDate=%s, paymentMethod=%s", 
                                  userId, expiryDate, paymentMethod));
        
        return subscription;
    }
    
    /**
     * 🎯 Kích hoạt Premium cho User (Đồng bộ 2 tables)
     * 
     * Flow: User đăng ký → Ghi PremiumSubscription → Update Users role
     * Đảm bảo consistency giữa 2 tables
     */
    public PremiumSubscription activatePremiumForUser(int userId, String paymentMethod, double amount) throws SQLException {
        try {
            logger.info(String.format("🎯 Starting premium activation for user: %d, method: %s, amount: %.2f", 
                                      userId, paymentMethod, amount));
            
            // Step 1: Validate user exists
            User user = userDAO.findById(userId);
            if (user == null) {
                throw new SQLException("User not found with ID: " + userId);
            }
            
            // Step 2: Deactivate old subscriptions
            deactivateOldSubscriptions(userId);
            
            // Step 3: Calculate dates
            LocalDate startDate = LocalDate.now();
            LocalDate expiryDate = calculateExpiryDate(startDate);
            
            // Step 4: Create new subscription record
            PremiumSubscription subscription = new PremiumSubscription(userId, startDate, expiryDate, paymentMethod, amount);
            int subscriptionId = subscriptionDAO.insertSubscription(subscription);
            subscription.setId(subscriptionId);
            
            // Step 5: Update user role to premium (synchronize tables)
            synchronizeUserRoleToPremium(userId);
            
            logger.info(String.format("✅ Premium activated successfully: userId=%d, subscriptionId=%d, expiryDate=%s", 
                                      userId, subscriptionId, expiryDate));
            
            return subscription;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("❌ Failed to activate premium for user %d: %s", userId, e.getMessage()), e);
            throw e;
        }
    }
    
    /**
     * ⏰ Xử lý Premium Expiration cho User (Đồng bộ 2 tables)
     * 
     * Flow: Subscription expired → Update PremiumSubscription status → Downgrade Users role
     * Đảm bảo consistency giữa 2 tables
     */
    public boolean expirePremiumForUser(int userId, int subscriptionId) throws SQLException {
        try {
            logger.info(String.format("⏰ Starting premium expiration for user: %d, subscription: %d", userId, subscriptionId));
            
            // Step 1: Update subscription status to expired
            boolean subscriptionUpdated = subscriptionDAO.updateStatus(subscriptionId, "expired");
            if (!subscriptionUpdated) {
                logger.warning(String.format("Failed to update subscription status for subscriptionId: %d", subscriptionId));
                return false;
            }
            
            // Step 2: Check if user has any other active subscriptions
            PremiumSubscription activeSubscription = subscriptionDAO.getActiveSubscription(userId);
            
            // Step 3: If no active subscriptions, downgrade user role
            if (activeSubscription == null) {
                synchronizeUserRoleToRegular(userId);
                logger.info(String.format("✅ User %d downgraded to regular (no active subscriptions)", userId));
            } else {
                logger.info(String.format("ℹ️ User %d still has active subscription until %s", userId, activeSubscription.getExpiryDate()));
            }
            
            logger.info(String.format("✅ Premium expiration processed: userId=%d, subscriptionId=%d", userId, subscriptionId));
            return true;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("❌ Failed to expire premium for user %d: %s", userId, e.getMessage()), e);
            throw e;
        }
    }
    
    /**
     * 🔄 Đồng bộ User Role thành Premium
     * Cập nhật Users table khi có premium subscription active
     */
    private void synchronizeUserRoleToPremium(int userId) throws SQLException {
        User user = userDAO.findById(userId);
        if (user != null) {
            if (!"premium".equals(user.getRole())) {
                user.setRole("premium");
                userDAO.update(user);
                logger.info(String.format("🔄 Synchronized user %d role to PREMIUM", userId));
            } else {
                logger.info(String.format("ℹ️ User %d already has PREMIUM role", userId));
            }
        } else {
            throw new SQLException("Cannot synchronize role: User not found with ID " + userId);
        }
    }
    
    /**
     * 🔄 Đồng bộ User Role thành Regular
     * Cập nhật Users table khi không có premium subscription active
     */
    private void synchronizeUserRoleToRegular(int userId) throws SQLException {
        User user = userDAO.findById(userId);
        if (user != null) {
            if (!"user".equals(user.getRole()) && !"admin".equals(user.getRole())) {
                user.setRole("user");
                userDAO.update(user);
                logger.info(String.format("🔄 Synchronized user %d role to USER", userId));
            } else {
                logger.info(String.format("ℹ️ User %d role unchanged: %s", userId, user.getRole()));
            }
        } else {
            throw new SQLException("Cannot synchronize role: User not found with ID " + userId);
        }
    }
    
    /**
     * 📆 Tính expiry date thông minh (xử lý edge cases)
     * 
     * Logic:
     * - Thêm 1 tháng từ start date
     * - Nếu ngày không tồn tại (31/1 + 1 tháng = 31/2), lấy ngày đầu tháng tiếp theo
     * - Ví dụ: 31/1 -> 1/3, 30/1 -> 1/3, 29/1 -> 1/3
     * 
     * Updated: Xử lý chính xác edge cases theo yêu cầu
     */
    public LocalDate calculateExpiryDate(LocalDate startDate) {
        int startDay = startDate.getDayOfMonth();
        int startMonth = startDate.getMonthValue();
        int startYear = startDate.getYear();
        
        // Tính tháng và năm đích
        int targetMonth = startMonth + 1;
        int targetYear = startYear;
        
        if (targetMonth > 12) {
            targetMonth = 1;
            targetYear++;
        }
        
        // Kiểm tra ngày có hợp lệ trong tháng đích không
        try {
            LocalDate targetDate = LocalDate.of(targetYear, targetMonth, startDay);
            
            // Nếu tạo được date thành công, return
            return targetDate;
            
        } catch (Exception e) {
            // Ngày không hợp lệ (ví dụ: 31/2, 31/4, 31/6, 31/9, 31/11)
            // → Chuyển thành ngày đầu tháng tiếp theo
            
            int nextMonth = targetMonth + 1;
            int nextYear = targetYear;
            
            if (nextMonth > 12) {
                nextMonth = 1;
                nextYear++;
            }
            
            LocalDate firstDayNextMonth = LocalDate.of(nextYear, nextMonth, 1);
            
            logger.info(String.format("🔧 Edge case handling: %s + 1 month → %s (original target %d/%d/%d invalid)", 
                                    startDate, firstDayNextMonth, startDay, targetMonth, targetYear));
            
            return firstDayNextMonth;
        }
    }
    
    /**
     * 🛡️ Tính expiry date an toàn cho edge cases
     * 
     * @deprecated Thay thế bằng logic mới trong calculateExpiryDate()
     */
    private LocalDate calculateSafeExpiryDate(LocalDate startDate) {
        // Legacy method - giữ lại để tương thích, nhưng không sử dụng
        return calculateExpiryDate(startDate);
    }
    
    /**
     * ✨ Gia hạn premium subscription
     */
    public boolean extendPremiumSubscription(int userId, String paymentMethod, double amount) throws SQLException {
        PremiumSubscription currentSubscription = subscriptionDAO.getActiveSubscription(userId);
        
        if (currentSubscription != null && currentSubscription.isActive()) {
            // Extend từ ngày hiện tại expiry
            LocalDate newExpiryDate = calculateExpiryDate(currentSubscription.getExpiryDate());
            boolean extended = subscriptionDAO.extendSubscription(currentSubscription.getId(), newExpiryDate);
            
            if (extended) {
                logger.info(String.format("Extended premium subscription: userId=%d, newExpiryDate=%s", 
                                          userId, newExpiryDate));
            }
            
            return extended;
            
        } else {
            // Tạo subscription mới
            createPremiumSubscription(userId, paymentMethod, amount);
            return true;
        }
    }
    
    /**
     * ⏰ Xử lý expired subscriptions (chạy định kỳ)
     * 
     * Được gọi bởi scheduler để tự động xử lý các subscription hết hạn
     * Đồng bộ status giữa PremiumSubscription và Users tables
     */
    public int processExpiredSubscriptions() {
        try {
            List<PremiumSubscription> expiredSubscriptions = subscriptionDAO.getExpiredSubscriptions();
            int processedCount = 0;
            int failedCount = 0;
            
            logger.info(String.format("🔍 Found %d expired subscriptions to process", expiredSubscriptions.size()));
            
            for (PremiumSubscription subscription : expiredSubscriptions) {
                try {
                    // Sử dụng method mới để đồng bộ 2 tables
                    boolean success = expirePremiumForUser(subscription.getUserId(), subscription.getId());
                    
                    if (success) {
                        processedCount++;
                        logger.info(String.format("✅ Processed expired subscription: userId=%d, subscriptionId=%d", 
                                                  subscription.getUserId(), subscription.getId()));
                    } else {
                        failedCount++;
                        logger.warning(String.format("⚠️ Failed to process subscription: userId=%d, subscriptionId=%d", 
                                                    subscription.getUserId(), subscription.getId()));
                    }
                    
                } catch (SQLException e) {
                    failedCount++;
                    logger.log(Level.SEVERE, String.format("❌ Error processing subscription: userId=%d, subscriptionId=%d", 
                                                         subscription.getUserId(), subscription.getId()), e);
                }
            }
            
            // Summary logging
            if (processedCount > 0 || failedCount > 0) {
                logger.info(String.format("📊 Expiration processing completed: %d successful, %d failed", 
                                        processedCount, failedCount));
            } else {
                logger.info("ℹ️ No expired subscriptions found");
            }
            
            return processedCount;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Error retrieving expired subscriptions", e);
            return 0;
        }
    }
    
    /**
     * 📊 Lấy thống kê premium của user
     */
    public PremiumStats getUserPremiumStats(int userId) {
        try {
            PremiumSubscription activeSubscription = subscriptionDAO.getActiveSubscription(userId);
            List<PremiumSubscription> allSubscriptions = subscriptionDAO.getSubscriptionsByUser(userId);
            
            return new PremiumStats(activeSubscription, allSubscriptions);
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting premium stats for user " + userId, e);
            return new PremiumStats(null, List.of());
        }
    }
    
    /**
     * 🔄 Tắt subscription cũ khi tạo mới
     */
    private void deactivateOldSubscriptions(int userId) throws SQLException {
        List<PremiumSubscription> oldSubscriptions = subscriptionDAO.getSubscriptionsByUser(userId);
        
        for (PremiumSubscription subscription : oldSubscriptions) {
            if ("active".equals(subscription.getStatus())) {
                subscriptionDAO.updateStatus(subscription.getId(), "replaced");
            }
        }
    }
    
    /**
     * 👤 Update user role (Legacy method)
     * 
     * @deprecated Use synchronizeUserRoleToPremium() or synchronizeUserRoleToRegular() instead
     * for better consistency and logging
     */
    private void updateUserRole(int userId, String role) throws SQLException {
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setRole(role);
            userDAO.update(user);
        }
    }
    
    /**
     * 🔍 Validate Data Consistency giữa Users và PremiumSubscriptions tables
     * 
     * Method này để admin check xem có mismatch nào giữa 2 tables không
     */
    public ConsistencyReport validateDataConsistency() {
        ConsistencyReport report = new ConsistencyReport();
        
        try {
            // TODO: Implement full consistency check
            // 1. Users với role='premium' nhưng không có active subscription
            // 2. Active subscriptions nhưng user role ≠ 'premium'  
            // 3. Expired subscriptions nhưng user vẫn premium
            // 4. Multiple active subscriptions per user
            
            logger.info("🔍 Data consistency validation completed");
            report.setValid(true);
            report.addMessage("Consistency check passed (placeholder implementation)");
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Error during consistency validation", e);
            report.setValid(false);
            report.addMessage("Consistency check failed: " + e.getMessage());
        }
        
        return report;
    }
    
    /**
     * 📊 Consistency Report class
     */
    public static class ConsistencyReport {
        private boolean valid = true;
        private final List<String> messages = new ArrayList<>();
        private final List<String> warnings = new ArrayList<>();
        private final List<String> errors = new ArrayList<>();
        
        public void addMessage(String message) { messages.add(message); }
        public void addWarning(String warning) { warnings.add(warning); }
        public void addError(String error) { errors.add(error); valid = false; }
        
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getMessages() { return messages; }
        public List<String> getWarnings() { return warnings; }
        public List<String> getErrors() { return errors; }
        
        @Override
        public String toString() {
            return String.format("ConsistencyReport{valid=%s, messages=%d, warnings=%d, errors=%d}", 
                               valid, messages.size(), warnings.size(), errors.size());
        }
    }
    
    /**
     * 📊 Premium Statistics class
     */
    public static class PremiumStats {
        private final PremiumSubscription activeSubscription;
        private final List<PremiumSubscription> allSubscriptions;
        
        public PremiumStats(PremiumSubscription activeSubscription, List<PremiumSubscription> allSubscriptions) {
            this.activeSubscription = activeSubscription;
            this.allSubscriptions = allSubscriptions;
        }
        
        public boolean isPremium() {
            return activeSubscription != null && activeSubscription.isActive();
        }
        
        public long getDaysRemaining() {
            return activeSubscription != null ? activeSubscription.getDaysRemaining() : 0;
        }
        
        public LocalDate getExpiryDate() {
            return activeSubscription != null ? activeSubscription.getExpiryDate() : null;
        }
        
        public int getTotalSubscriptions() {
            return allSubscriptions.size();
        }
        
        public PremiumSubscription getActiveSubscription() {
            return activeSubscription;
        }
        
        public List<PremiumSubscription> getAllSubscriptions() {
            return allSubscriptions;
        }
    }
} 