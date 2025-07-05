package com.mycompany.ebookwebsite.utils;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.CoinService;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class để quản lý coin trong session
 * @author ADMIN
 */
public class CoinUtil {
    
    private static final Logger LOGGER = Logger.getLogger(CoinUtil.class.getName());
    private static final CoinService coinService = new CoinService();
    
    /**
     * Cập nhật coin trong session cho user hiện tại
     */
    public static void refreshUserCoins(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Sử dụng method safe để luôn có giá trị hợp lệ
            int userCoins = coinService.getUserCoinsSafe(user.getId());
            session.setAttribute("userCoins", userCoins);
            LOGGER.info("Refreshed coin info for user: " + user.getUsername() + " (Coins: " + userCoins + ")");
        }
    }
    
    /**
     * Cập nhật coin cho user cụ thể
     */
    public static void refreshUserCoins(HttpSession session, int userId) {
        // Sử dụng method safe để luôn có giá trị hợp lệ
        int userCoins = coinService.getUserCoinsSafe(userId);
        session.setAttribute("userCoins", userCoins);
        LOGGER.info("Refreshed coin info for user ID: " + userId + " (Coins: " + userCoins + ")");
    }
    
    /**
     * Lấy coin từ session, trả về 0 nếu không có
     */
    public static int getUserCoinsFromSession(HttpSession session) {
        Integer coins = (Integer) session.getAttribute("userCoins");
        return coins != null ? coins : 0;
    }
    
    /**
     * Kiểm tra user có đủ coin không
     */
    public static boolean hasEnoughCoins(HttpSession session, int requiredCoins) {
        return getUserCoinsFromSession(session) >= requiredCoins;
    }
} 