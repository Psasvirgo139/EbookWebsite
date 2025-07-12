package com.mycompany.ebookwebsite.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.dao.FavoriteDAO;
import com.mycompany.ebookwebsite.dao.UserBehaviorDAO;
import com.mycompany.ebookwebsite.dao.UserReadDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import com.mycompany.ebookwebsite.model.Favorite;
import com.mycompany.ebookwebsite.model.UserBehavior;
import com.mycompany.ebookwebsite.model.UserRead;

/**
 * 🎯 PERSONALIZED RECOMMENDATION SERVICE
 * 
 * Tự động đề xuất sách dựa trên lịch sử đọc và hành vi người dùng
 */
public class PersonalizedRecommendationService {
    
    private final UserReadDAO userReadDAO;
    private final FavoriteDAO favoriteDAO;
    private final UserBehaviorDAO userBehaviorDAO;
    private final EbookDAO ebookDAO;
    
    public PersonalizedRecommendationService() {
        this.userReadDAO = new UserReadDAO();
        this.favoriteDAO = new FavoriteDAO();
        this.userBehaviorDAO = new UserBehaviorDAO();
        this.ebookDAO = new EbookDAO();
    }
    
    /**
     * 🎯 Get personalized recommendations for user
     */
    public List<Ebook> getPersonalizedRecommendations(int userId, int limit) throws SQLException {
        // Get user preferences
        UserPreferences preferences = analyzeUserPreferences(userId);
        
        // Get all available books
        List<Ebook> allBooks = ebookDAO.selectAllEbooks();
        
        // Filter and score books based on preferences
        List<ScoredBook> scoredBooks = scoreBooksByPreferences(allBooks, preferences);
        
        // Sort by score and return top recommendations
        return scoredBooks.stream()
                .sorted(Comparator.comparing(ScoredBook::getScore).reversed())
                .limit(limit)
                .map(ScoredBook::getBook)
                .collect(Collectors.toList());
    }
    
    /**
     * 📊 Analyze user preferences from reading history
     */
    private UserPreferences analyzeUserPreferences(int userId) throws SQLException {
        UserPreferences preferences = new UserPreferences();
        
        // Analyze reading history
        List<UserRead> readingHistory = userReadDAO.selectByUser(userId);
        analyzeReadingHistory(readingHistory, preferences);
        
        // Analyze favorites
        List<Favorite> favorites = favoriteDAO.getFavoritesByUser(userId);
        analyzeFavorites(favorites, preferences);
        
        // Analyze user behavior
        List<UserBehavior> behaviors = userBehaviorDAO.getUserRecentActions(userId, 30);
        analyzeUserBehavior(behaviors, preferences);
        
        return preferences;
    }
    
    /**
     * 📚 Analyze reading history for preferences
     */
    private void analyzeReadingHistory(List<UserRead> readingHistory, UserPreferences preferences) throws SQLException {
        for (UserRead read : readingHistory) {
            Ebook book = ebookDAO.getEbookById(read.getEbookID());
            if (book != null) {
                // Track genre preferences
                if (book.getReleaseType() != null) {
                    preferences.addGenrePreference(book.getReleaseType(), 1);
                }
                
                // Track reading recency (recent reads get higher weight)
                long daysSinceRead = java.time.temporal.ChronoUnit.DAYS.between(
                    read.getLastReadAt(), LocalDate.now());
                int recencyWeight = Math.max(1, 10 - (int)daysSinceRead);
                preferences.addGenrePreference(book.getReleaseType(), recencyWeight);
            }
        }
    }
    
    /**
     * ❤️ Analyze favorite books for preferences
     */
    private void analyzeFavorites(List<Favorite> favorites, UserPreferences preferences) throws SQLException {
        for (Favorite favorite : favorites) {
            Ebook book = ebookDAO.getEbookById(favorite.getEbookID());
            if (book != null) {
                // Favorites get higher weight
                if (book.getReleaseType() != null) {
                    preferences.addGenrePreference(book.getReleaseType(), 3);
                }
            }
        }
    }
    
    /**
     * 🧠 Analyze user behavior patterns
     */
    private void analyzeUserBehavior(List<UserBehavior> behaviors, UserPreferences preferences) {
        for (UserBehavior behavior : behaviors) {
            switch (behavior.getActionType()) {
                case "view_book":
                    preferences.addViewPreference(behavior.getTargetId(), 1);
                    break;
                case "search":
                    if (behavior.getActionData() != null) {
                        preferences.addSearchKeyword(behavior.getActionData());
                    }
                    break;
                case "favorite":
                    preferences.addFavoritePreference(behavior.getTargetId(), 2);
                    break;
            }
        }
    }
    
    /**
     * 🎯 Score books based on user preferences
     */
    private List<ScoredBook> scoreBooksByPreferences(List<Ebook> books, UserPreferences preferences) {
        List<ScoredBook> scoredBooks = new ArrayList<>();
        
        for (Ebook book : books) {
            double score = calculateBookScore(book, preferences);
            scoredBooks.add(new ScoredBook(book, score));
        }
        
        return scoredBooks;
    }
    
    /**
     * 📊 Calculate score for a book based on user preferences
     */
    private double calculateBookScore(Ebook book, UserPreferences preferences) {
        double score = 0.0;
        
        // Genre preference score (highest weight)
        if (book.getReleaseType() != null) {
            Integer genreWeight = preferences.getGenrePreferences().get(book.getReleaseType());
            if (genreWeight != null) {
                score += genreWeight * 10.0;
            }
        }
        
        // Popularity score (view count)
        score += Math.min(book.getViewCount() / 100.0, 5.0);
        
        // Recency score (newer books get slight boost)
        if (book.getCreatedAt() != null) {
            long daysSinceCreation = java.time.temporal.ChronoUnit.DAYS.between(
                book.getCreatedAt().toLocalDate(), LocalDate.now());
            if (daysSinceCreation < 30) {
                score += 2.0;
            }
        }
        
        // Random factor to add variety
        score += Math.random();
        
        return score;
    }
    
    /**
     * 📈 Get user reading statistics
     */
    public UserReadingStats getUserReadingStats(int userId) throws SQLException {
        List<UserRead> readingHistory = userReadDAO.selectByUser(userId);
        List<Favorite> favorites = favoriteDAO.getFavoritesByUser(userId);
        
        UserReadingStats stats = new UserReadingStats();
        stats.setTotalBooksRead(readingHistory.size());
        stats.setTotalFavorites(favorites.size());
        
        // Get most read genres
        Map<String, Integer> genreCounts = new HashMap<>();
        for (UserRead read : readingHistory) {
            Ebook book = ebookDAO.getEbookById(read.getEbookID());
            if (book != null && book.getReleaseType() != null) {
                genreCounts.merge(book.getReleaseType(), 1, Integer::sum);
            }
        }
        
        stats.setTopGenres(genreCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()));
        
        return stats;
    }
    
    /**
     * 🔄 Track user behavior
     */
    public void trackUserBehavior(int userId, String actionType, Integer targetId, String actionData) {
        try {
            UserBehavior behavior = new UserBehavior();
            behavior.setUserId(userId);
            behavior.setActionType(actionType);
            behavior.setTargetId(targetId);
            behavior.setActionData(actionData);
            behavior.setTargetType("book");
            
            userBehaviorDAO.trackUserAction(behavior);
        } catch (SQLException e) {
            System.err.println("Failed to track user behavior: " + e.getMessage());
        }
    }
    
    // ======================== INNER CLASSES ========================
    
    /**
     * 📊 User preferences container
     */
    public static class UserPreferences {
        private final Map<String, Integer> genrePreferences = new HashMap<>();
        private final Map<Integer, Integer> viewPreferences = new HashMap<>();
        private final Map<Integer, Integer> favoritePreferences = new HashMap<>();
        private final List<String> searchKeywords = new ArrayList<>();
        
        public void addGenrePreference(String genre, int weight) {
            genrePreferences.merge(genre, weight, Integer::sum);
        }
        
        public void addViewPreference(Integer bookId, int weight) {
            viewPreferences.merge(bookId, weight, Integer::sum);
        }
        
        public void addFavoritePreference(Integer bookId, int weight) {
            favoritePreferences.merge(bookId, weight, Integer::sum);
        }
        
        public void addSearchKeyword(String keyword) {
            searchKeywords.add(keyword);
        }
        
        // Getters
        public Map<String, Integer> getGenrePreferences() { return genrePreferences; }
        public Map<Integer, Integer> getViewPreferences() { return viewPreferences; }
        public Map<Integer, Integer> getFavoritePreferences() { return favoritePreferences; }
        public List<String> getSearchKeywords() { return searchKeywords; }
    }
    
    /**
     * 📚 Scored book container
     */
    public static class ScoredBook {
        private final Ebook book;
        private final double score;
        
        public ScoredBook(Ebook book, double score) {
            this.book = book;
            this.score = score;
        }
        
        public Ebook getBook() { return book; }
        public double getScore() { return score; }
    }
    
    /**
     * 📈 User reading statistics
     */
    public static class UserReadingStats {
        private int totalBooksRead;
        private int totalFavorites;
        private List<String> topGenres;
        
        // Getters and Setters
        public int getTotalBooksRead() { return totalBooksRead; }
        public void setTotalBooksRead(int totalBooksRead) { this.totalBooksRead = totalBooksRead; }
        
        public int getTotalFavorites() { return totalFavorites; }
        public void setTotalFavorites(int totalFavorites) { this.totalFavorites = totalFavorites; }
        
        public List<String> getTopGenres() { return topGenres; }
        public void setTopGenres(List<String> topGenres) { this.topGenres = topGenres; }
    }
} 