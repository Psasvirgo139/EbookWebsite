package com.mycompany.ebookwebsite.utils;

import com.mycompany.ebookwebsite.dao.UserDAO;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.CoinService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Utility service for common servlet operations
 * Helps reduce code duplication across servlets
 * @author ADMIN
 */
public class SessionService {
    
    private static final UserDAO userDAO = new UserDAO();
    private static final CoinService coinService = new CoinService();
    
    /**
     * Kiểm tra user đã đăng nhập chưa, handle redirect internally
     * @return User object nếu đã đăng nhập, null nếu đã redirect (servlet should return)
     */
    public static User requireLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/user/login.jsp?error=login_required");
                return null;
            }
            
            return user;
        } catch (IOException e) {
            System.err.println("Error during login redirect: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Kiểm tra user có quyền admin không, handle redirect internally
     * @return User object với admin role nếu hợp lệ, null nếu đã redirect (servlet should return)
     */
    public static User requireAdmin(HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = requireLogin(request, response);
            if (user == null) return null;
            
            if (!"admin".equals(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/user/profile?error=access_denied");
                return null;
            }
            
            return user;
        } catch (IOException e) {
            System.err.println("Error during admin redirect: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Validate integer parameter, handle error response internally
     * @return validated value hoặc null nếu invalid (error response already sent, servlet should return)
     */
    public static Integer validateIntParameter(HttpServletRequest request, HttpServletResponse response,
                                              String paramValue, String paramName, int min, int max) {
        try {
            if (paramValue == null || paramValue.trim().isEmpty()) {
                sendJsonResponse(response, createJsonError("Parameter " + paramName + " is required"));
                return null;
            }
            
            int value = Integer.parseInt(paramValue.trim());
            if (value < min || value > max) {
                sendJsonResponse(response, createJsonError(
                    paramName + " must be between " + min + " and " + max));
                return null;
            }
            return value;
            
        } catch (NumberFormatException e) {
            try {
                sendJsonResponse(response, createJsonError("Invalid " + paramName + " format"));
            } catch (IOException ioException) {
                System.err.println("Error sending JSON response: " + ioException.getMessage());
            }
            return null;
        } catch (IOException e) {
            System.err.println("Error sending JSON response: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Validate string parameter, handle error response internally
     * @return validated string hoặc null nếu invalid (error response already sent, servlet should return)
     */
    public static String validateStringParameter(HttpServletRequest request, HttpServletResponse response,
                                               String paramValue, String paramName, boolean required) {
        try {
            if (paramValue == null || paramValue.trim().isEmpty()) {
                if (required) {
                    sendJsonResponse(response, createJsonError("Parameter " + paramName + " is required"));
                    return null;
                }
                return null;
            }
            return paramValue.trim();
        } catch (IOException e) {
            System.err.println("Error sending JSON response: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Handle SQL error internally - log và redirect
     * @return false (để servlet có thể return immediately)
     */
    public static boolean handleSQLError(HttpServletResponse response, String redirectUrl, 
                                        SQLException e, String contextPath) {
        try {
            e.printStackTrace();
            response.sendRedirect(contextPath + redirectUrl + "?error=database_error");
            return false;
        } catch (IOException ioException) {
            System.err.println("Error handling SQL error: " + ioException.getMessage());
            return false;
        }
    }
    
    /**
     * Refresh user object trong session từ database
     */
    public static void refreshUserInSession(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            User updatedUser = userDAO.findById(user.getId());
            session.setAttribute("user", updatedUser);
        }
    }
    
    /**
     * Gửi JSON response
     */
    public static void sendJsonResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
    
    /**
     * Xử lý general error và redirect
     */
    public static void handleError(HttpServletResponse response, String redirectUrl, 
                                  String errorType, String contextPath) throws IOException {
        response.sendRedirect(contextPath + redirectUrl + "?error=" + errorType);
    }
    
    /**
     * Redirect success với message
     */
    public static void handleSuccess(HttpServletResponse response, String redirectUrl, 
                                    String successType, String contextPath) throws IOException {
        response.sendRedirect(contextPath + redirectUrl + "?success=" + successType);
    }
    
    /**
     * Refresh user coins trong session attribute (for header display)
     */
    public static void refreshUserCoins(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                int coins = coinService.getUserCoinsSafe(user.getId());
                session.setAttribute("userCoins", coins);
            }
        } catch (Exception e) {
            // Không throw exception để không break servlet flow
            System.err.println("Error refreshing user coins: " + e.getMessage());
        }
    }
    
    /**
     * Create JSON success response
     */
    public static String createJsonSuccess(String message, Object data) {
        if (data != null) {
            return String.format("{\"success\": true, \"message\": \"%s\", \"data\": %s}", 
                               message, data.toString());
        } else {
            return String.format("{\"success\": true, \"message\": \"%s\"}", message);
        }
    }
    
    /**
     * Create JSON error response
     */
    public static String createJsonError(String message) {
        return String.format("{\"success\": false, \"message\": \"%s\"}", message);
    }
} 