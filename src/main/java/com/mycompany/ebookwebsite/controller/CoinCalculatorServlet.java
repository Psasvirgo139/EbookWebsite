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
 * Servlet trung gian để tính toán totalBill cho VNPay
 * @author ADMIN
 */
@WebServlet("/coin/calculate")
public class CoinCalculatorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Kiểm tra user đã đăng nhập chưa
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp?error=login_required");
            return;
        }
        
        try {
            String coinAmountStr = request.getParameter("coinAmount");
            int coinAmount = Integer.parseInt(coinAmountStr);
            
            // Validate số lượng coin (1-1000)
            if (coinAmount < 1 || coinAmount > 1000) {
                response.sendRedirect(request.getContextPath() + "/coin/payment?error=invalid_coin_amount");
                return;
            }
            
            // Tính tổng tiền (1 coin = 1,000 VND)
            int totalBill = coinAmount * 1000;
            
            // Lưu thông tin vào session để VnpayReturn có thể sử dụng
            session.setAttribute("pendingCoinAmount", coinAmount);
            session.setAttribute("pendingTotalBill", totalBill);
            
            // Redirect đến ajaxServlet với totalBill parameter
            String redirectUrl = request.getContextPath() + "/vn_pay/ajax?totalBill=" + totalBill;
            response.sendRedirect(redirectUrl);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/coin/payment?error=invalid_coin_amount");
        }
    }
} 