package com.mycompany.ebookwebsite.utils;

import com.mycompany.ebookwebsite.model.Favorite;
import com.mycompany.ebookwebsite.model.User;

/**
 * Validation utilities for Favorite operations
 */
public class FavoriteValidation {
    
    /**
     * Validate favorite data before adding
     */
    public static void validateAddFavorite(Favorite favorite, User user) throws IllegalArgumentException {
        if (favorite == null) {
            throw new IllegalArgumentException("Favorite không được null");
        }
        
        if (user == null) {
            throw new IllegalArgumentException("User không được null");
        }
        
        if (favorite.getUserID() <= 0) {
            throw new IllegalArgumentException("User ID không hợp lệ");
        }
        
        if (favorite.getEbookID() <= 0) {
            throw new IllegalArgumentException("Ebook ID không hợp lệ");
        }
        
        if (favorite.getUserID() != user.getId() && !"admin".equals(user.getRole())) {
            throw new IllegalArgumentException("Bạn không có quyền thêm favorite cho user khác");
        }
    }
    
    /**
     * Validate favorite deletion permissions
     */
    public static void validateDeleteFavorite(int userId, int ebookId, User user) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("User không được null");
        }
        
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID không hợp lệ");
        }
        
        if (ebookId <= 0) {
            throw new IllegalArgumentException("Ebook ID không hợp lệ");
        }
        
        if (userId != user.getId() && !"admin".equals(user.getRole())) {
            throw new IllegalArgumentException("Bạn không có quyền xóa favorite của user khác");
        }
    }
    
    /**
     * Validate user ID for getting favorites
     */
    public static void validateGetUserFavorites(int userId, User user) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("User không được null");
        }
        
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID không hợp lệ");
        }
        
        if (userId != user.getId() && !"admin".equals(user.getRole())) {
            throw new IllegalArgumentException("Bạn không có quyền xem favorites của user khác");
        }
    }
} 