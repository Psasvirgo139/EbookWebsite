package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.ChapterDAO;
import com.mycompany.ebookwebsite.dao.UnlockedChapterDAO;
import com.mycompany.ebookwebsite.dao.UserCoinDAO;
import com.mycompany.ebookwebsite.model.Chapter;
import com.mycompany.ebookwebsite.model.UnlockedChapter;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.model.UserCoin;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service để xử lý logic liên quan đến coin và unlock chapter
 * @author ADMIN
 */
public class CoinService {
    
    private final UserCoinDAO userCoinDAO = new UserCoinDAO();
    private final UnlockedChapterDAO unlockedChapterDAO = new UnlockedChapterDAO();
    private final ChapterDAO chapterDAO = new ChapterDAO();
    
    // Giá tiền để unlock 1 chapter premium (có thể config)
    private static final int UNLOCK_CHAPTER_COST = 10;
    
    /**
     * Lấy số coin hiện tại của user
     */
    public int getUserCoins(int userId) throws SQLException {
        try {
            UserCoin userCoin = userCoinDAO.getUserCoins(userId);
            if (userCoin != null) {
                return userCoin.getCoins();
            } else {
                // Fallback: trả về 0 nếu không có data
                return 0;
            }
        } catch (SQLException e) {
            // Log error nhưng vẫn trả về giá trị mặc định để không break UI
            System.err.println("Error getting user coins for userId " + userId + ": " + e.getMessage());
            throw e; // Re-throw để caller có thể handle
        } catch (Exception e) {
            // Log any unexpected errors
            System.err.println("Unexpected error getting user coins for userId " + userId + ": " + e.getMessage());
            return 0; // Return default value for any unexpected errors
        }
    }
    
    /**
     * Lấy số coin của user một cách an toàn (không throw exception)
     * Luôn trả về giá trị hợp lệ, mặc định là 0
     */
    public int getUserCoinsSafe(int userId) {
        try {
            return getUserCoins(userId);
        } catch (Exception e) {
            System.err.println("Safe method: Error getting user coins for userId " + userId + ": " + e.getMessage());
            return 0; // Luôn trả về 0 nếu có lỗi
        }
    }
    
    /**
     * Thêm coin cho user (admin hoặc hệ thống reward)
     */
    public boolean addCoins(int userId, int coinsToAdd, User currentUser) throws SQLException {
        // Chỉ admin mới có thể thêm coin cho user khác
        if (currentUser.getId() != userId && !"admin".equals(currentUser.getRole())) {
            throw new SecurityException("Bạn không có quyền thêm coin cho user khác!");
        }
        
        return userCoinDAO.addCoins(userId, coinsToAdd);
    }
    
    /**
     * Kiểm tra user có đủ coin để unlock chapter không
     */
    public boolean hasEnoughCoins(int userId, int requiredCoins) throws SQLException {
        return getUserCoins(userId) >= requiredCoins;
    }
    
    /**
     * Kiểm tra chapter có phải premium và đã unlock chưa
     */
    public boolean isChapterAccessible(int userId, int chapterId) throws SQLException {
        Chapter chapter = chapterDAO.getChapterById(chapterId);
        
        if (chapter == null) {
            return false;
        }
        
        // Nếu chapter không phải premium thì luôn accessible
        if (!"premium".equals(chapter.getAccessLevel())) {
            return true;
        }
        
        // Nếu là premium, check xem đã unlock chưa
        return unlockedChapterDAO.isChapterUnlocked(userId, chapterId);
    }
    
    /**
     * Unlock chapter premium bằng coin
     */
    public boolean unlockChapter(int userId, int chapterId, User currentUser) throws SQLException {
        // Kiểm tra quyền
        if (currentUser.getId() != userId && !"admin".equals(currentUser.getRole())) {
            throw new SecurityException("Bạn không có quyền unlock chapter cho user khác!");
        }
        
        // Kiểm tra chapter đã unlock chưa
        if (unlockedChapterDAO.isChapterUnlocked(userId, chapterId)) {
            throw new IllegalStateException("Chapter này đã được unlock!");
        }
        
        // Kiểm tra đủ coin
        if (!hasEnoughCoins(userId, UNLOCK_CHAPTER_COST)) {
            throw new IllegalArgumentException("Không đủ coin để unlock chapter! Cần " + UNLOCK_CHAPTER_COST + " coins.");
        }
        
        Connection conn = null;
        try {
            conn = com.mycompany.ebookwebsite.dao.DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Trừ coin
            userCoinDAO.deductCoins(userId, UNLOCK_CHAPTER_COST);
            
            // Thêm record unlock chapter
            UnlockedChapter unlockedChapter = new UnlockedChapter(
                userId, 
                chapterId, 
                UNLOCK_CHAPTER_COST, 
                LocalDateTime.now()
            );
            unlockedChapterDAO.insertUnlockedChapter(unlockedChapter);
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    
    /**
     * Lấy danh sách chapter đã unlock của user
     */
    public List<UnlockedChapter> getUserUnlockedChapters(int userId) throws SQLException {
        return unlockedChapterDAO.getUnlockedChaptersByUser(userId);
    }
    
    /**
     * Lấy thông tin chi tiết coin của user
     */
    public UserCoin getUserCoinInfo(int userId) throws SQLException {
        return userCoinDAO.getUserCoins(userId);
    }
    
    /**
     * Admin function: Set coin cho user
     */
    public boolean setUserCoins(int userId, int coins, User currentUser) throws SQLException {
        if (!"admin".equals(currentUser.getRole())) {
            throw new SecurityException("Chỉ admin mới có thể set coin!");
        }
        
        return userCoinDAO.updateCoins(userId, coins);
    }
    
    /**
     * Lấy giá để unlock chapter
     */
    public static int getUnlockChapterCost() {
        return UNLOCK_CHAPTER_COST;
    }
} 