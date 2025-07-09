package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.FavoriteDAO;
import com.mycompany.ebookwebsite.model.Favorite;
import com.mycompany.ebookwebsite.model.User;
import java.sql.SQLException;
import java.util.List;

public class FavoriteService {
    private final FavoriteDAO favoriteDAO = new FavoriteDAO();

    public void addFavorite(Favorite favorite) throws SQLException {
        favoriteDAO.insertFavorite(favorite);
    }
    public boolean deleteFavorite(int userId, int ebookId, Integer chapterId, User user) throws SQLException {
        if (user.getId() != userId && !"admin".equals(user.getRole())) {
            throw new SecurityException("Bạn không có quyền xóa favorite này!");
        }
        return favoriteDAO.deleteFavorite(userId, ebookId, chapterId);
    }
    public List<Favorite> getFavoritesByUser(int userId) throws SQLException {
        return favoriteDAO.getFavoritesByUser(userId);
    }
} 