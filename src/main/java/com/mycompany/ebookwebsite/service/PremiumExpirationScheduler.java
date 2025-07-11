package com.mycompany.ebookwebsite.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ⏰ Premium Expiration Scheduler
 * 
 * Tự động kiểm tra và xử lý expired premium subscriptions định kỳ
 * Chạy mỗi ngày vào lúc 00:30 để kiểm tra và downgrade expired users
 */
public class PremiumExpirationScheduler {
    
    private static final Logger logger = Logger.getLogger(PremiumExpirationScheduler.class.getName());
    
    private final ScheduledExecutorService scheduler;
    private final PremiumService premiumService;
    private boolean isRunning = false;
    
    // Singleton instance
    private static PremiumExpirationScheduler instance;
    
    private PremiumExpirationScheduler() {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.premiumService = new PremiumService();
        
        logger.info("⏰ Premium Expiration Scheduler initialized");
    }
    
    /**
     * 🔄 Get singleton instance
     */
    public static synchronized PremiumExpirationScheduler getInstance() {
        if (instance == null) {
            instance = new PremiumExpirationScheduler();
        }
        return instance;
    }
    
    /**
     * 🚀 Start scheduler
     */
    public void start() {
        if (isRunning) {
            logger.warning("⚠️ Premium Expiration Scheduler already running");
            return;
        }
        
        // Chạy immediate check đầu tiên
        scheduleImmediateCheck();
        
        // Lên lịch chạy hàng ngày vào 00:00
        scheduleDailyCheck();
        
        // Backup check đã được tắt theo yêu cầu
        // scheduleBackupCheck();
        
        isRunning = true;
        logger.info("🚀 Premium Expiration Scheduler started successfully");
    }
    
    /**
     * 🛑 Stop scheduler
     */
    public void stop() {
        if (!isRunning) {
            return;
        }
        
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        isRunning = false;
        logger.info("🛑 Premium Expiration Scheduler stopped");
    }
    
    /**
     * ⚡ Schedule immediate check (chạy ngay khi start)
     */
    private void scheduleImmediateCheck() {
        scheduler.schedule(() -> {
            logger.info("⚡ Running immediate premium expiration check...");
            performExpirationCheck();
        }, 5, TimeUnit.SECONDS); // Chạy sau 5 giây
    }
    
    /**
     * 🌅 Schedule daily check (mỗi ngày 00:00)
     */
    private void scheduleDailyCheck() {
        // Tính thời gian đến lần chạy đầu tiên (00:00 hôm nay hoặc mai)
        long initialDelay = calculateInitialDelayToMidnight();
        
        scheduler.scheduleAtFixedRate(() -> {
            logger.info("🌅 Running daily premium expiration check at 00:00...");
            performExpirationCheck();
        }, initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
        
        logger.info("🌅 Daily premium expiration check scheduled for 00:00");
    }
    
    /**
     * 🔄 Schedule backup check (mỗi 6 giờ)
     */
    private void scheduleBackupCheck() {
        scheduler.scheduleAtFixedRate(() -> {
            logger.info("🔄 Running backup premium expiration check (every 6 hours)...");
            performExpirationCheck();
        }, 6, 6, TimeUnit.HOURS);
        
        logger.info("🔄 Backup premium expiration check scheduled (every 6 hours)");
    }
    
    /**
     * 🎯 Perform actual expiration check
     */
    private void performExpirationCheck() {
        try {
            LocalDateTime startTime = LocalDateTime.now();
            
            logger.info("🔍 Starting premium expiration check at " + 
                       startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            // Gọi PremiumService để xử lý expired subscriptions
            int processedCount = premiumService.processExpiredSubscriptions();
            
            LocalDateTime endTime = LocalDateTime.now();
            long durationSeconds = java.time.Duration.between(startTime, endTime).toSeconds();
            
            if (processedCount > 0) {
                logger.info(String.format("✅ Premium expiration check completed: %d subscriptions processed in %d seconds", 
                                        processedCount, durationSeconds));
            } else {
                logger.info(String.format("✅ Premium expiration check completed: No expired subscriptions found (completed in %d seconds)", 
                                        durationSeconds));
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Error during premium expiration check", e);
        }
    }
    
    /**
     * 🕐 Calculate delay to next 00:00
     */
    private long calculateInitialDelayToMidnight() {
        LocalDateTime now = LocalDateTime.now();
        
        // Luôn schedule cho 00:00 của ngày mai
        LocalDateTime next0000 = now.toLocalDate().plusDays(1).atTime(0, 0);
        
        long seconds = java.time.Duration.between(now, next0000).toSeconds();
        
        logger.info(String.format("🕐 Next daily check scheduled for: %s (in %d seconds)", 
                                  next0000.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 
                                  seconds));
        
        return seconds;
    }
    
    /**
     * 🔄 Manual trigger (for testing/admin)
     */
    public int triggerManualCheck() {
        logger.info("🔧 Manual premium expiration check triggered");
        
        try {
            return premiumService.processExpiredSubscriptions();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Error during manual premium expiration check", e);
            return 0;
        }
    }
    
    /**
     * 📊 Get scheduler status
     */
    public SchedulerStatus getStatus() {
        return new SchedulerStatus(isRunning, scheduler.isShutdown(), scheduler.isTerminated());
    }
    
    /**
     * 📊 Scheduler Status class
     */
    public static class SchedulerStatus {
        private final boolean running;
        private final boolean shutdown;
        private final boolean terminated;
        
        public SchedulerStatus(boolean running, boolean shutdown, boolean terminated) {
            this.running = running;
            this.shutdown = shutdown;
            this.terminated = terminated;
        }
        
        public boolean isRunning() { return running; }
        public boolean isShutdown() { return shutdown; }
        public boolean isTerminated() { return terminated; }
        
        @Override
        public String toString() {
            return String.format("SchedulerStatus{running=%s, shutdown=%s, terminated=%s}", 
                               running, shutdown, terminated);
        }
    }
} 