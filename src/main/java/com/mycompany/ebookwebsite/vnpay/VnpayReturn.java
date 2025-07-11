/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.ebookwebsite.vnpay;

import com.mycompany.ebookwebsite.dao.OrderDAO;
import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.dao.UserCoinDAO;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.PremiumService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import com.mycompany.ebookwebsite.model.Order;
import java.sql.SQLException;

/**
 * 💳 VnpayReturn - Xử lý kết quả thanh toán VNPay
 * 
 * Updated to use PremiumService for proper expiry tracking
 * @author HP
 */
@WebServlet("/vn_pay/vnpayReturn")
public class VnpayReturn extends HttpServlet {
    OrderDAO orderDao = new OrderDAO();
    UserDAO userDao = new UserDAO();
    UserCoinDAO userCoinDao = new UserCoinDAO();
    PremiumService premiumService = new PremiumService();  // 👑 Thêm PremiumService
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }
            String signValue = Config.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {
                String paymentCode = request.getParameter("vnp_TransactionNo");
                
                String orderId = request.getParameter("vnp_TxnRef");
                
                Order order = new Order();
                order.setId(Integer.parseInt(orderId));
                
                boolean transSuccess = false;
                if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                    //update banking system
                    order.setStatus("Completed");
                    transSuccess = true;
                } else {
                     order.setStatus("Failed");
                }
                try{
                    orderDao.update(order);
                    
                    // Nếu thanh toán thành công, xử lý logic nghiệp vụ
                    if (transSuccess) {
                        jakarta.servlet.http.HttpSession session = request.getSession();
                        User currentUser = (User) session.getAttribute("user");
                        
                        if (currentUser != null) {
                            // Lấy thông tin order để xác định loại giao dịch
                            Order fullOrder = orderDao.findById(Integer.parseInt(orderId));
                            
                            if (fullOrder != null) {
                                double totalAmount = fullOrder.getTotalAmount();
                                
                                if (totalAmount == 100000.0) {
                                    // Premium upgrade bằng VND (100,000 VND)
                                    handlePremiumUpgrade(currentUser, session);
                                    request.setAttribute("transactionType", "premium_upgrade");
                                    
                                } else {
                                    // Nạp coins (số tiền khác 100,000 VND)
                                    Integer pendingCoinAmount = (Integer) session.getAttribute("pendingCoinAmount");
                                    if (pendingCoinAmount != null) {
                                        handleCoinTopup(currentUser.getId(), pendingCoinAmount);
                                        request.setAttribute("transactionType", "coin_topup");
                                        request.setAttribute("coinAmount", pendingCoinAmount);
                                        
                                        // Xóa thông tin pending khỏi session
                                        session.removeAttribute("pendingCoinAmount");
                                        session.removeAttribute("pendingTotalBill");
                                    }
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.sendRedirect("coin/payment");
                    return;
                }
                request.setAttribute("transResult", transSuccess);
                request.getRequestDispatcher("/vn_pay/paymentResult.jsp").forward(request, response);
            } else {
                //RETURN PAGE ERROR
                System.out.println("GD KO HOP LE (invalid signature)");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    /**
     * 👑 Xử lý upgrade user lên premium với proper expiry tracking
     */
    private void handlePremiumUpgrade(User user, jakarta.servlet.http.HttpSession session) throws SQLException {
        try {
            // 👑 Sử dụng PremiumService thay vì chỉ set role
            premiumService.activatePremiumForUser(user.getId(), "vnd", 100000.0);
            
            // Refresh user object từ database để có role mới
            User updatedUser = userDao.findById(user.getId());
            if (updatedUser != null) {
                session.setAttribute("user", updatedUser);
            }
            
            System.out.println("✅ User " + user.getUsername() + " upgraded to premium via VNPay with expiry tracking");
            
        } catch (SQLException e) {
            System.err.println("❌ Failed to upgrade user " + user.getUsername() + " to premium: " + e.getMessage());
            throw e; // Re-throw để caller xử lý
        }
    }
    
    /**
     * 💰 Xử lý nạp coins cho user
     */
    private void handleCoinTopup(int userId, int coinAmount) throws SQLException {
        // Thêm coins vào tài khoản user
        userCoinDao.addCoins(userId, coinAmount);
        
        System.out.println("✅ Added " + coinAmount + " coins to user ID: " + userId);
    }

}
