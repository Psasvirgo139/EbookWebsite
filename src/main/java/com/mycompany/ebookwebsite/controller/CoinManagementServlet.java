package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.model.UserCoin;
import com.mycompany.ebookwebsite.service.CoinService;
import com.mycompany.ebookwebsite.utils.CoinUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet để admin quản lý coin của user
 * @author ADMIN
 */
@WebServlet(name = "CoinManagementServlet", urlPatterns = {"/admin/coin-management"})
public class CoinManagementServlet extends HttpServlet {
    
    private final CoinService coinService = new CoinService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }
        
        if (!"admin".equals(currentUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }
        
        try {
            String action = request.getParameter("action");
            
            if ("addCoins".equals(action)) {
                handleAddCoins(request, response, currentUser);
            } else if ("setCoins".equals(action)) {
                handleSetCoins(request, response, currentUser);
            } else if ("getUserCoins".equals(action)) {
                handleGetUserCoins(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
    
    private void handleAddCoins(HttpServletRequest request, HttpServletResponse response, 
                               User currentUser) throws SQLException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int coinsToAdd = Integer.parseInt(request.getParameter("coins"));
            
            if (coinsToAdd <= 0) {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Số coin phải lớn hơn 0\"}");
                return;
            }
            
            coinService.addCoins(userId, coinsToAdd, currentUser);
            
            // Lấy số coin hiện tại sau khi thêm
            int currentCoins = coinService.getUserCoins(userId);
            
            // Nếu là chính user hiện tại, cập nhật session
            User sessionUser = (User) request.getSession().getAttribute("user");
            if (sessionUser != null && sessionUser.getId() == userId) {
                CoinUtil.refreshUserCoins(request.getSession());
            }
            
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"success\": true, \"message\": \"Đã thêm %d coins\", \"currentCoins\": %d}", 
                coinsToAdd, currentCoins
            ));
            
        } catch (SecurityException e) {
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"success\": false, \"message\": \"%s\"}", 
                e.getMessage()
            ));
        }
    }
    
    private void handleSetCoins(HttpServletRequest request, HttpServletResponse response,
                               User currentUser) throws SQLException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int coins = Integer.parseInt(request.getParameter("coins"));
            
            if (coins < 0) {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Số coin không thể âm\"}");
                return;
            }
            
            coinService.setUserCoins(userId, coins, currentUser);
            
            // Nếu là chính user hiện tại, cập nhật session
            User sessionUser = (User) request.getSession().getAttribute("user");
            if (sessionUser != null && sessionUser.getId() == userId) {
                CoinUtil.refreshUserCoins(request.getSession());
            }
            
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"success\": true, \"message\": \"Đã set coins thành %d\", \"currentCoins\": %d}", 
                coins, coins
            ));
            
        } catch (SecurityException e) {
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"success\": false, \"message\": \"%s\"}", 
                e.getMessage()
            ));
        }
    }
    
    private void handleGetUserCoins(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        
        int userId = Integer.parseInt(request.getParameter("userId"));
        UserCoin userCoin = coinService.getUserCoinInfo(userId);
        
        response.setContentType("application/json");
        response.getWriter().write(String.format(
            "{\"userId\": %d, \"coins\": %d, \"lastUpdated\": \"%s\"}", 
            userCoin.getUserId(), userCoin.getCoins(), userCoin.getLastUpdated().toString()
        ));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
} 