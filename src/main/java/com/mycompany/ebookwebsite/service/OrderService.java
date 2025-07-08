package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.OrderDAO;
import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.dao.UserCoinDAO;
import com.mycompany.ebookwebsite.model.Order;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.model.UserCoin;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service để xử lý logic nghiệp vụ cho Order
 * Throws exceptions cho servlet xử lý
 * @author ADMIN
 */
public class OrderService {
    
    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());
    
    // Order status constants
    public static final String STATUS_PROCESSING = "Processing";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_FAILED = "Failed";
    
    // Order types
    public static final String TYPE_COIN_PURCHASE = "coin_purchase";
    public static final String TYPE_PREMIUM_UPGRADE = "premium_upgrade";
    
    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final UserCoinDAO userCoinDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.userDAO = new UserDAO();
        this.userCoinDAO = new UserCoinDAO();
    }

    public OrderService(OrderDAO orderDAO, UserDAO userDAO, UserCoinDAO userCoinDAO) {
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
        this.userCoinDAO = userCoinDAO;
    }

    /**
     * Tạo order mua coins với số lượng tùy chỉnh
     * @param userId ID của user
     * @param coinAmount Số lượng coins muốn mua
     * @return Order đã tạo
     * @throws SQLException nếu có lỗi database
     * @throws IllegalArgumentException nếu parameters không hợp lệ
     */
    public Order createCoinPurchaseOrder(int userId, int coinAmount) throws SQLException, IllegalArgumentException {
        // Validate input
        validateUserId(userId);
        validateCoinAmount(coinAmount);
        
        // Tính tổng tiền (1 coin = 1,000 VND)
        double totalAmount = coinAmount * 1000.0;
        
        // Tạo order mới
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        order.setStatus(STATUS_PROCESSING);
        
        // Lưu vào database và lấy ID
        int orderId = orderDAO.insert(order);
        
        LOGGER.log(Level.INFO, "Created coin purchase order: userId={0}, coinAmount={1}, totalAmount={2}, orderId={3}", 
                new Object[]{userId, coinAmount, totalAmount, orderId});
        
        return order;
    }

    /**
     * Tạo order nâng cấp premium
     * @param userId ID của user
     * @param paymentMethod Phương thức thanh toán ("vnd" hoặc "coin")
     * @return Order đã tạo
     * @throws SQLException nếu có lỗi database
     * @throws IllegalArgumentException nếu parameters không hợp lệ
     */
    public Order createPremiumUpgradeOrder(int userId, String paymentMethod) throws SQLException, IllegalArgumentException {
        // Validate input
        validateUserId(userId);
        validatePaymentMethod(paymentMethod);
        
        double totalAmount;
        
        if ("vnd".equals(paymentMethod)) {
            totalAmount = 100000.0; // 100,000 VND
        } else if ("coin".equals(paymentMethod)) {
            // Kiểm tra user có đủ 100 coins không
            UserCoin userCoin = userCoinDAO.getUserCoins(userId);
            if (userCoin.getCoins() < 100) {
                throw new IllegalArgumentException("Không đủ coins để nâng cấp premium. Cần 100 coins, hiện có: " + userCoin.getCoins());
            }
            totalAmount = 100.0; // 100 coins
        } else {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ: " + paymentMethod);
        }
        
        // Tạo order mới
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        order.setStatus(STATUS_PROCESSING);
        
        // Lưu vào database và lấy ID
        int orderId = orderDAO.insert(order);
        
        LOGGER.log(Level.INFO, "Created premium upgrade order: userId={0}, paymentMethod={1}, totalAmount={2}, orderId={3}", 
                new Object[]{userId, paymentMethod, totalAmount, orderId});
        
        return order;
    }

    /**
     * Xử lý hoàn tất order mua coins
     * @param orderId ID của order
     * @param coinAmount Số lượng coins sẽ được thêm vào tài khoản
     * @return true nếu xử lý thành công
     * @throws SQLException nếu có lỗi database
     * @throws IllegalArgumentException nếu order không hợp lệ
     */
    public boolean completeCoinPurchaseOrder(int orderId, int coinAmount) throws SQLException, IllegalArgumentException {
        // Lấy order
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Không tìm thấy order với ID: " + orderId);
        }
        
        if (!STATUS_PROCESSING.equals(order.getStatus())) {
            throw new IllegalArgumentException("Order không ở trạng thái xử lý: " + order.getStatus());
        }
        
        try {
            // Thêm coins cho user
            userCoinDAO.addCoins(order.getUserId(), coinAmount);
            
            // Cập nhật status order
            boolean updated = orderDAO.updateStatus(orderId, STATUS_COMPLETED);
            if (!updated) {
                throw new SQLException("Không thể cập nhật status order");
            }
            
            LOGGER.log(Level.INFO, "Completed coin purchase order: orderId={0}, userId={1}, coinAmount={2}", 
                    new Object[]{orderId, order.getUserId(), coinAmount});
            
            return true;
            
        } catch (Exception e) {
            // Nếu có lỗi, đánh dấu order failed
            orderDAO.updateStatus(orderId, STATUS_FAILED);
            throw e;
        }
    }

    /**
     * Xử lý hoàn tất order nâng cấp premium bằng coin
     * @param orderId ID của order
     * @return true nếu xử lý thành công
     * @throws SQLException nếu có lỗi database
     * @throws IllegalArgumentException nếu order không hợp lệ
     */
    public boolean completePremiumUpgradeOrderWithCoin(int orderId) throws SQLException, IllegalArgumentException {
        // Lấy order
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Không tìm thấy order với ID: " + orderId);
        }
        
        if (!STATUS_PROCESSING.equals(order.getStatus())) {
            throw new IllegalArgumentException("Order không ở trạng thái xử lý: " + order.getStatus());
        }
        
        try {
            // Trừ 100 coins từ user
            UserCoin currentUserCoin = userCoinDAO.getUserCoins(order.getUserId());
            if (currentUserCoin.getCoins() < 100) {
                throw new IllegalArgumentException("Không đủ coins để hoàn tất order");
            }
            
            userCoinDAO.deductCoins(order.getUserId(), 100);
            
            // Cập nhật status order
            boolean updated = orderDAO.updateStatus(orderId, STATUS_COMPLETED);
            if (!updated) {
                throw new SQLException("Không thể cập nhật status order");
            }
            
            LOGGER.log(Level.INFO, "Completed premium upgrade order with coin: orderId={0}, userId={1}", 
                    new Object[]{orderId, order.getUserId()});
            
            return true;
            
        } catch (Exception e) {
            // Nếu có lỗi, đánh dấu order failed
            orderDAO.updateStatus(orderId, STATUS_FAILED);
            throw e;
        }
    }

    /**
     * Đánh dấu order failed
     * @param orderId ID của order
     * @param reason Lý do thất bại
     * @return true nếu cập nhật thành công
     * @throws SQLException nếu có lỗi database
     */
    public boolean failOrder(int orderId, String reason) throws SQLException {
        boolean updated = orderDAO.updateStatus(orderId, STATUS_FAILED);
        
        if (updated) {
            LOGGER.log(Level.WARNING, "Order failed: orderId={0}, reason={1}", new Object[]{orderId, reason});
        }
        
        return updated;
    }

    /**
     * Lấy order theo ID
     * @param orderId ID của order
     * @return Order nếu tìm thấy
     * @throws SQLException nếu có lỗi database
     */
    public Order getOrderById(int orderId) throws SQLException {
        return orderDAO.findById(orderId);
    }

    /**
     * Lấy danh sách orders của user
     * @param userId ID của user
     * @return List các Order của user
     * @throws SQLException nếu có lỗi database
     */
    public List<Order> getOrdersByUserId(int userId) throws SQLException {
        validateUserId(userId);
        return orderDAO.findByUserId(userId);
    }

    /**
     * Lấy danh sách orders của user theo status
     * @param userId ID của user
     * @param status Status của order
     * @return List các Order thỏa mãn điều kiện
     * @throws SQLException nếu có lỗi database
     */
    public List<Order> getOrdersByUserIdAndStatus(int userId, String status) throws SQLException {
        validateUserId(userId);
        validateOrderStatus(status);
        return orderDAO.findByUserIdAndStatus(userId, status);
    }

    /**
     * Lấy tất cả orders theo status (cho admin)
     * @param status Status của order
     * @return List các Order có status tương ứng
     * @throws SQLException nếu có lỗi database
     */
    public List<Order> getOrdersByStatus(String status) throws SQLException {
        validateOrderStatus(status);
        return orderDAO.findByStatus(status);
    }

    /**
     * Lấy tất cả orders (cho admin)
     * @return List tất cả Order
     * @throws SQLException nếu có lỗi database
     */
    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.findAll();
    }

    /**
     * Lấy orders trong khoảng thời gian (cho báo cáo)
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return List các Order trong khoảng thời gian
     * @throws SQLException nếu có lỗi database
     */
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date và end date không được null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date không được sau end date");
        }
        
        return orderDAO.findByDateRange(startDate, endDate);
    }

    /**
     * Đếm số orders của user
     * @param userId ID của user
     * @return Số lượng orders
     * @throws SQLException nếu có lỗi database
     */
    public int countOrdersByUserId(int userId) throws SQLException {
        validateUserId(userId);
        return orderDAO.countByUserId(userId);
    }

    /**
     * Đếm số orders theo status
     * @param status Status của order
     * @return Số lượng orders
     * @throws SQLException nếu có lỗi database
     */
    public int countOrdersByStatus(String status) throws SQLException {
        validateOrderStatus(status);
        return orderDAO.countByStatus(status);
    }

    // Validation methods
    
    private void validateUserId(int userId) throws IllegalArgumentException, SQLException {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID phải là số dương: " + userId);
        }
        
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Không tìm thấy user với ID: " + userId);
        }
    }

    private void validateCoinAmount(int coinAmount) throws IllegalArgumentException {
        if (coinAmount < 1 || coinAmount > 1000) {
            throw new IllegalArgumentException("Số lượng coin phải từ 1 đến 1000: " + coinAmount);
        }
    }

    private void validatePaymentMethod(String paymentMethod) throws IllegalArgumentException {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("Phương thức thanh toán không được null hoặc rỗng");
        }
        
        if (!"vnd".equals(paymentMethod) && !"coin".equals(paymentMethod)) {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ. Chỉ chấp nhận 'vnd' hoặc 'coin': " + paymentMethod);
        }
    }

    private void validateOrderStatus(String status) throws IllegalArgumentException {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status không được null hoặc rỗng");
        }
        
        if (!STATUS_PROCESSING.equals(status) && !STATUS_COMPLETED.equals(status) && !STATUS_FAILED.equals(status)) {
            throw new IllegalArgumentException("Status không hợp lệ: " + status);
        }
    }
} 