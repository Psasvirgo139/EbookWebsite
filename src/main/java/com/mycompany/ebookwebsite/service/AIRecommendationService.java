package com.mycompany.ebookwebsite.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mycompany.ebookwebsite.dao.AIRecommendationDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Favorite;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.model.UserRead;

public class AIRecommendationService {
    
    private AIRecommendationDAO aiRecommendationDAO;
    
    public AIRecommendationService() {
        this.aiRecommendationDAO = new AIRecommendationDAO();
    }
    
    /**
     * Lấy danh sách ebook được đề xuất cho user
     */
    public List<Ebook> getRecommendedEbooks(int userId) throws SQLException {
        List<Ebook> recommendations = new ArrayList<>();
        
        // 1. Lấy lịch sử đọc và yêu thích của user
        List<UserRead> readHistory = aiRecommendationDAO.getUserReadHistory(userId);
        List<Favorite> favorites = aiRecommendationDAO.getUserFavorites(userId);
        
        // 2. Phân tích sở thích từ lịch sử
        Set<Integer> readEbookIds = readHistory.stream()
                .map(UserRead::getEbookID)
                .collect(Collectors.toSet());
        
        Set<Integer> favoriteEbookIds = favorites.stream()
                .map(Favorite::getEbookID)
                .collect(Collectors.toSet());
        
        // 3. Tìm ebook tương tự dựa trên tác giả và thể loại
        for (Integer ebookId : readEbookIds) {
            Ebook readEbook = aiRecommendationDAO.getEbookById(ebookId);
            if (readEbook != null) {
                // Tìm ebook cùng tác giả
                List<Ebook> sameAuthorBooks = aiRecommendationDAO.getEbooksByAuthor(readEbook.getTitle());
                recommendations.addAll(sameAuthorBooks.stream()
                        .filter(book -> !readEbookIds.contains(book.getId()))
                        .limit(3)
                        .collect(Collectors.toList()));
            }
        }
        
        // 4. Loại bỏ trùng lặp và giới hạn số lượng
        return recommendations.stream()
                .distinct()
                .limit(10)
                .collect(Collectors.toList());
    }
    
    /**
     * Tìm kiếm ebook theo từ khóa
     */
    public List<Ebook> searchEbooks(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return aiRecommendationDAO.searchEbooks(keyword.trim());
    }
    
    /**
     * Lấy ebook theo tác giả
     */
    public List<Ebook> getEbooksByAuthor(String authorName) throws SQLException {
        if (authorName == null || authorName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return aiRecommendationDAO.getEbooksByAuthor(authorName.trim());
    }
    
    /**
     * Lấy ebook theo thể loại
     */
    public List<Ebook> getEbooksByCategory(String category) throws SQLException {
        if (category == null || category.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return aiRecommendationDAO.getEbooksByTag(category.trim());
    }
    
    /**
     * Lấy thông tin user
     */
    public User getUserById(int userId) throws SQLException {
        return aiRecommendationDAO.getUserById(userId);
    }
    
    /**
     * Lấy lịch sử đọc của user
     */
    public List<UserRead> getUserReadHistory(int userId) throws SQLException {
        return aiRecommendationDAO.getUserReadHistory(userId);
    }
    
    /**
     * Lấy danh sách yêu thích của user
     */
    public List<Favorite> getUserFavorites(int userId) throws SQLException {
        return aiRecommendationDAO.getUserFavorites(userId);
    }
    
    /**
     * Lấy ebook theo ID
     */
    public Ebook getEbookById(int ebookId) throws SQLException {
        return aiRecommendationDAO.getEbookById(ebookId);
    }
} 