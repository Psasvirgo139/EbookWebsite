package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Favorite;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.EbookService;
import com.mycompany.ebookwebsite.service.FavoriteService;
import com.mycompany.ebookwebsite.utils.FavoriteValidation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet xử lý các thao tác với favorites
 */
@WebServlet(name = "FavoriteServlet", urlPatterns = {"/favorites", "/favorites/"})
public class FavoriteServlet extends HttpServlet {
    
    private FavoriteService favoriteService;
    private EbookService ebookService;
    
    @Override
    public void init() throws ServletException {
        this.favoriteService = new FavoriteService();
        this.ebookService = new EbookService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        
        try {
            // Validate user permissions
            FavoriteValidation.validateGetUserFavorites(currentUser.getId(), currentUser);
            
            // Get user favorites
            List<Favorite> favorites = favoriteService.getFavoritesByUser(currentUser.getId());
            
            // Tạo map ebookId -> Ebook để truyền sang JSP
            Map<Integer, Ebook> ebookMap = new java.util.HashMap<>();
            for (Favorite favorite : favorites) {
                try {
                    Ebook book = ebookService.getEbookById(favorite.getEbookID());
                    if (book != null) {
                        ebookMap.put(favorite.getEbookID(), book);
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ Cannot get book details for favorite: " + e.getMessage());
                }
            }
            
            request.setAttribute("favorites", favorites);
            request.setAttribute("ebookMap", ebookMap);
            request.setAttribute("userName", currentUser.getUsername());
            
            request.getRequestDispatcher("/favorites/listfavorites.jsp").forward(request, response);
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải danh sách favorites: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        String redirectUrl = request.getParameter("redirectUrl");
        
        try {
            if ("add".equals(action)) {
                handleAddFavorite(request, response, currentUser);
                // Nếu là form HTML, redirect về lại trang cũ
                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                    response.sendRedirect(redirectUrl);
                    return;
                }
            } else if ("delete".equals(action)) {
                handleDeleteFavorite(request, response, currentUser);
                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                    response.sendRedirect(redirectUrl);
                    return;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action không hợp lệ");
            }
            
        } catch (IllegalArgumentException e) {
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                // Có thể set thông báo lỗi vào session để hiển thị lại trên trang
                session.setAttribute("favoriteError", e.getMessage());
                response.sendRedirect(redirectUrl);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                session.setAttribute("favoriteError", "Có lỗi xảy ra khi thêm vào favorites: " + e.getMessage());
                response.sendRedirect(redirectUrl);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false, \"error\": \"Có lỗi xảy ra: " + e.getMessage() + "\"}");
            }
        }
    }
    
    /**
     * Xử lý thêm favorite
     */
    private void handleAddFavorite(HttpServletRequest request, HttpServletResponse response, User user) 
            throws SQLException, IOException {
        
        String ebookIdStr = request.getParameter("ebookId");
        
        if (ebookIdStr == null || ebookIdStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Ebook ID không được để trống");
        }
        
        int ebookId = Integer.parseInt(ebookIdStr);
        
        // Validate
        FavoriteValidation.validateAddFavorite(new Favorite(user.getId(), ebookId, null, java.time.LocalDate.now()), user);
        
        // Add to database
        favoriteService.addFavorite(user.getId(), ebookId);
        
        // Return success response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\": true, \"message\": \"Đã thêm vào favorites\"}");
    }
    
    /**
     * Xử lý xóa favorite
     */
    private void handleDeleteFavorite(HttpServletRequest request, HttpServletResponse response, User user) 
            throws SQLException, IOException {
        
        String ebookIdStr = request.getParameter("ebookId");
        
        if (ebookIdStr == null || ebookIdStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Ebook ID không được để trống");
        }
        
        int ebookId = Integer.parseInt(ebookIdStr);
        
        // Validate
        FavoriteValidation.validateDeleteFavorite(user.getId(), ebookId, user);
        
        // Delete from database
        boolean deleted = favoriteService.deleteFavorite(user.getId(), ebookId, user);
        
        // Return response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        if (deleted) {
            response.getWriter().write("{\"success\": true, \"message\": \"Đã xóa khỏi favorites\"}");
        } else {
            response.getWriter().write("{\"success\": false, \"error\": \"Không tìm thấy favorite để xóa\"}");
        }
    }
} 