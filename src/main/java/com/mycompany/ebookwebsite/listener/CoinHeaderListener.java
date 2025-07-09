package com.mycompany.ebookwebsite.listener;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.CoinService;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listener để tự động thêm thông tin coin vào session khi user login
 * @author ADMIN
 */
@WebListener
public class CoinHeaderListener implements HttpSessionAttributeListener {
    
    private static final Logger LOGGER = Logger.getLogger(CoinHeaderListener.class.getName());
    private final CoinService coinService = new CoinService();
    
    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        // Khi attribute "user" được thêm vào session (user login)
        if ("user".equals(event.getName())) {
            User user = (User) event.getValue();
            if (user != null) {
                // Lấy coin của user và thêm vào session một cách an toàn
                int userCoins = coinService.getUserCoinsSafe(user.getId());
                event.getSession().setAttribute("userCoins", userCoins);
                LOGGER.info("Added coin info to session for user: " + user.getUsername() + " (Coins: " + userCoins + ")");
            }
        }
    }
    
    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        // Khi user session được update
        if ("user".equals(event.getName())) {
            User user = (User) event.getValue();
            if (user != null) {
                // Cập nhật coin info một cách an toàn
                int userCoins = coinService.getUserCoinsSafe(user.getId());
                event.getSession().setAttribute("userCoins", userCoins);
                LOGGER.info("Updated coin info in session for user: " + user.getUsername() + " (Coins: " + userCoins + ")");
            }
        }
    }
    
    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if ("user".equals(event.getName())) {
            try {
                event.getSession().removeAttribute("userCoins");
                LOGGER.info("Removed coin info from session");
            } catch (IllegalStateException e) {
                LOGGER.warning("Session already invalidated; cannot remove userCoins.");
            }
        }
    }
} 