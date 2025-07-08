package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.dao.UserCoinDAO;
import com.mycompany.ebookwebsite.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet để xử lý việc nạp tiền và nâng cấp premium
 * @author ADMIN
 */
@WebServlet("/coin/payment")
public class PaymentServlet extends HttpServlet {
    
    private UserDAO userDao = new UserDAO();
    private UserCoinDAO userCoinDao = new UserCoinDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Kiểm tra user đã đăng nhập chưa
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp?error=login_required");
            return;
        }
        
        try {
            // Lấy số coin hiện tại của user
            int currentCoins = userCoinDao.getUserCoins(user.getId()).getCoins();
            request.setAttribute("currentCoins", currentCoins);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("currentCoins", 0); // Default value nếu có lỗi
        }
        
        // Chuyển hướng đến trang nạp tiền
        // Trong thực tế, đây có thể là trang thanh toán với payment gateway
        request.setAttribute("user", user);
        request.setAttribute("premiumPrice", 100000); // 100k VND
        request.setAttribute("pageTitle", "Nạp tiền & Premium");
        
        request.getRequestDispatcher("/coin/payment.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp?error=login_required");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("upgrade_premium".equals(action) || "upgrade_premium_vnd".equals(action)) {
            // Xử lý nâng cấp premium bằng VND
            // Trong thực tế, đây sẽ tích hợp với payment gateway
            
            // Giả lập payment thành công
            session.setAttribute("isPremium", true);
            session.setAttribute("premiumExpiry", System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000)); // 30 ngày
            
            response.sendRedirect(request.getContextPath() + "/user/profile?success=premium_upgraded_vnd");
        } else if ("upgrade_premium_coin".equals(action)) {
            // Xử lý nâng cấp premium bằng coin
            try {
                // Kiểm tra số coin hiện tại từ database
                int currentCoins = userCoinDao.getUserCoins(user.getId()).getCoins();
                
                if (currentCoins >= 100) {
                    // Trừ 100 coins từ database
                    userCoinDao.deductCoins(user.getId(), 100);
                    
                    // Nâng cấp user thành premium
                    user.setRole("premium");
                    userDao.update(user);
                    
                    // Cập nhật session
                    session.setAttribute("user", user);
                    
                    response.sendRedirect(request.getContextPath() + "/profile?success=premium_upgraded_coin");
                } else {
                    response.sendRedirect(request.getContextPath() + "/coin/payment?error=insufficient_coins");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/coin/payment?error=database_error");
            }
        } else if ("buy_coins".equals(action)) {
            // Xử lý mua coins với gói cố định
            String coinPackage = request.getParameter("coinPackage");
            
            try {
                int coinsToAdd = 0;
                switch (coinPackage) {
                    case "pack_50":
                        coinsToAdd = 50;
                        break;
                    case "pack_100":
                        coinsToAdd = 100;
                        break;
                    case "pack_200":
                        coinsToAdd = 200;
                        break;
                    default:
                        coinsToAdd = 20; // default package
                }
                
                // Thêm coins vào database
                userCoinDao.addCoins(user.getId(), coinsToAdd);
                
                response.sendRedirect(request.getContextPath() + "/user/profile?success=coins_purchased&coins=" + coinsToAdd);
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/coin/payment?error=database_error");
            }
        } else if ("buy_custom_coins".equals(action)) {
            // Xử lý mua coins với số lượng tùy chỉnh (chỉ xử lý logic validation, VNPay sẽ xử lý thanh toán thực)
            try {
                String coinAmountStr = request.getParameter("coinAmount");
                int coinAmount = Integer.parseInt(coinAmountStr);
                
                // Validate số lượng coin (1-1000)
                if (coinAmount < 1 || coinAmount > 1000) {
                    response.sendRedirect(request.getContextPath() + "/coin/payment?error=invalid_coin_amount");
                    return;
                }
                
                // Note: Việc thêm coins sẽ được xử lý trong VnpayReturn khi thanh toán thành công
                // Ở đây chỉ redirect để thông báo thành công tạm thời (nếu không dùng VNPay)
                
                response.sendRedirect(request.getContextPath() + "/user/profile?success=custom_coins_processing&coins=" + coinAmount);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/coin/payment?error=invalid_coin_amount");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/coin/payment?error=processing_error");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }
} 