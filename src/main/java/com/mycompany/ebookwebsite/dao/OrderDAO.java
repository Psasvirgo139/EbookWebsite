package com.mycompany.ebookwebsite.dao;

import com.mycompany.ebookwebsite.model.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO để thao tác với bảng Orders trong database
 * Chỉ chứa các method CRUD cơ bản, không xử lý logic nghiệp vụ
 * @author ADMIN
 */
public class OrderDAO {
    
    // SQL Queries
    private static final String INSERT_ORDER = 
        "INSERT INTO Orders (UserId, OrderDate, TotalAmount, Status) VALUES (?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID = 
        "SELECT Id, UserId, OrderDate, TotalAmount, Status FROM Orders WHERE Id = ?";
    
    private static final String SELECT_ALL = 
        "SELECT Id, UserId, OrderDate, TotalAmount, Status FROM Orders ORDER BY OrderDate DESC";
    
    private static final String SELECT_BY_USER_ID = 
        "SELECT Id, UserId, OrderDate, TotalAmount, Status FROM Orders WHERE UserId = ? ORDER BY OrderDate DESC";
    
    private static final String SELECT_BY_STATUS = 
        "SELECT Id, UserId, OrderDate, TotalAmount, Status FROM Orders WHERE Status = ? ORDER BY OrderDate DESC";
    
    private static final String SELECT_BY_USER_AND_STATUS = 
        "SELECT Id, UserId, OrderDate, TotalAmount, Status FROM Orders WHERE UserId = ? AND Status = ? ORDER BY OrderDate DESC";
    
    private static final String UPDATE_ORDER = 
        "UPDATE Orders SET Status = ? WHERE Id = ?";
    
    private static final String UPDATE_STATUS = 
        "UPDATE Orders SET Status = ? WHERE Id = ?";
    
    private static final String DELETE_ORDER = 
        "DELETE FROM Orders WHERE Id = ?";
    
    private static final String COUNT_BY_USER = 
        "SELECT COUNT(*) FROM Orders WHERE UserId = ?";
    
    private static final String COUNT_BY_STATUS = 
        "SELECT COUNT(*) FROM Orders WHERE Status = ?";
    
    private static final String SELECT_BY_DATE_RANGE = 
        "SELECT Id, UserId, OrderDate, TotalAmount, Status FROM Orders WHERE OrderDate BETWEEN ? AND ? ORDER BY OrderDate DESC";

    /**
     * Thêm order mới vào database
     * @param order Order cần thêm
     * @return ID của order vừa được tạo
     * @throws SQLException nếu có lỗi database
     */
    public int insert(Order order) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, order.getUserId());
            ps.setTimestamp(2, order.getOrderDate() != null ? 
                Timestamp.valueOf(order.getOrderDate()) : Timestamp.valueOf(LocalDateTime.now()));
            ps.setDouble(3, order.getTotalAmount());
            ps.setString(4, order.getStatus());
            
            int rows = ps.executeUpdate();
            
            // Lấy ID được tạo tự động
            if (rows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        order.setId(generatedId);
                        return generatedId;
                    }
                }
            }
            
            throw new SQLException("Tạo order thất bại, không có ID được tạo");
        }
    }

    /**
     * Tìm order theo ID
     * @param id ID của order
     * @return Order nếu tìm thấy, null nếu không tìm thấy
     * @throws SQLException nếu có lỗi database
     */
    public Order findById(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapOrder(rs);
                }
            }
        }
        return null;
    }

    /**
     * Lấy tất cả orders
     * @return List các Order
     * @throws SQLException nếu có lỗi database
     */
    public List<Order> findAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                orders.add(mapOrder(rs));
            }
        }
        
        return orders;
    }

    /**
     * Tìm orders theo user ID
     * @param userId ID của user
     * @return List các Order của user
     * @throws SQLException nếu có lỗi database
     */
    public List<Order> findByUserId(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER_ID)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        
        return orders;
    }

    /**
     * Tìm orders theo status
     * @param status Trạng thái order
     * @return List các Order có status tương ứng
     * @throws SQLException nếu có lỗi database
     */
    public List<Order> findByStatus(String status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_STATUS)) {
            
            ps.setString(1, status);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        
        return orders;
    }

    /**
     * Tìm orders theo user ID và status
     * @param userId ID của user
     * @param status Trạng thái order
     * @return List các Order thỏa mãn điều kiện
     * @throws SQLException nếu có lỗi database
     */
    public List<Order> findByUserIdAndStatus(int userId, String status) throws SQLException {
        List<Order> orders = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_USER_AND_STATUS)) {
            
            ps.setInt(1, userId);
            ps.setString(2, status);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        
        return orders;
    }

    /**
     * Tìm orders trong khoảng thời gian
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return List các Order trong khoảng thời gian
     * @throws SQLException nếu có lỗi database
     */
    public List<Order> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        List<Order> orders = new ArrayList<>();
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_DATE_RANGE)) {
            
            ps.setTimestamp(1, Timestamp.valueOf(startDate));
            ps.setTimestamp(2, Timestamp.valueOf(endDate));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }
        
        return orders;
    }

    /**
     * Cập nhật order
     * @param order Order cần cập nhật
     * @return true nếu cập nhật thành công
     * @throws SQLException nếu có lỗi database
     */
    public boolean update(Order order) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_ORDER)) {
            ps.setString(1, order.getStatus());
            ps.setInt(2, order.getId());
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật status của order
     * @param orderId ID của order
     * @param status Status mới
     * @return true nếu cập nhật thành công
     * @throws SQLException nếu có lỗi database
     */
    public boolean updateStatus(int orderId, String status) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_STATUS)) {
            
            ps.setString(1, status);
            ps.setInt(2, orderId);
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa order
     * @param id ID của order cần xóa
     * @return true nếu xóa thành công
     * @throws SQLException nếu có lỗi database
     */
    public boolean delete(int id) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_ORDER)) {
            
            ps.setInt(1, id);
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Đếm số orders của user
     * @param userId ID của user
     * @return Số lượng orders
     * @throws SQLException nếu có lỗi database
     */
    public int countByUserId(int userId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(COUNT_BY_USER)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Đếm số orders theo status
     * @param status Trạng thái order
     * @return Số lượng orders
     * @throws SQLException nếu có lỗi database
     */
    public int countByStatus(String status) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(COUNT_BY_STATUS)) {
            
            ps.setString(1, status);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Map ResultSet thành Order object
     * @param rs ResultSet
     * @return Order object
     * @throws SQLException nếu có lỗi khi đọc dữ liệu
     */
    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("Id"));
        order.setUserId(rs.getInt("UserId"));
        
        Timestamp orderDate = rs.getTimestamp("OrderDate");
        if (orderDate != null) {
            order.setOrderDate(orderDate.toLocalDateTime());
        }
        
        order.setTotalAmount(rs.getDouble("TotalAmount"));
        order.setStatus(rs.getString("Status"));
        
        return order;
    }
} 