package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet để xử lý việc nạp tiền và nâng cấp premium
 * @author ADMIN
 */
@WebServlet("/coin/payment")
public class PaymentServlet extends HttpServlet {

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
        
        // Chuyển hướng đến trang nạp tiền
        // Trong thực tế, đây có thể là trang thanh toán với payment gateway
        request.setAttribute("user", user);
        request.setAttribute("premiumPrice", 10000); // 10k VND
        request.setAttribute("pageTitle", "Nạp tiền & Premium");
        
        request.getRequestDispatcher("/user/payment.jsp").forward(request, response);
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
        
        if ("upgrade_premium".equals(action)) {
            // Xử lý nâng cấp premium
            // Trong thực tế, đây sẽ tích hợp với payment gateway
            
            // Giả lập payment thành công
            session.setAttribute("isPremium", true);
            session.setAttribute("premiumExpiry", System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000)); // 30 ngày
            
            response.sendRedirect(request.getContextPath() + "/book/read?success=premium_upgraded");
        } else if ("buy_coins".equals(action)) {
            // Xử lý mua coins
            String coinPackage = request.getParameter("coinPackage");
            
            // Giả lập mua coins thành công
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
            
            // Trong thực tế, sẽ cập nhật vào database
            Integer currentCoins = (Integer) session.getAttribute("userCoins");
            if (currentCoins == null) currentCoins = 0;
            session.setAttribute("userCoins", currentCoins + coinsToAdd);
            
            response.sendRedirect(request.getContextPath() + "/user/profile?success=coins_purchased&coins=" + coinsToAdd);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }
} 