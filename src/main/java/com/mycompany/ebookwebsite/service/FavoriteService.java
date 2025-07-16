package com.mycompany.ebookwebsite.service;

import java.sql.SQLException;
import java.util.List;

import com.mycompany.ebookwebsite.dao.FavoriteDAO;
import com.mycompany.ebookwebsite.model.Favorite;
import com.mycompany.ebookwebsite.model.User;

public class FavoriteService {
    private final FavoriteDAO favoriteDAO = new FavoriteDAO();

    public void addFavorite(int userId, int ebookId) throws SQLException {
        if (!favoriteDAO.isFavorite(userId, ebookId)) {
            favoriteDAO.insertFavorite(new Favorite(userId, ebookId, null, java.time.LocalDate.now()));
        }
    }
    public boolean deleteFavorite(int userId, int ebookId, User user) throws SQLException {
        if (user.getId() != userId && !"admin".equals(user.getRole())) {
            throw new SecurityException("Bạn không có quyền xóa favorite này!");
        }
        return favoriteDAO.deleteFavorite(userId, ebookId);
    }
    public List<Favorite> getFavoritesByUser(int userId) throws SQLException {
        return favoriteDAO.getFavoritesByUser(userId);
    }

    /**
     * Đếm số lượng favorite của một ebook
     * @param ebookId id sách
     * @return số lượng favorite
     * @throws SQLException nếu có lỗi database
     */
    public int countFavoritesByEbook(int ebookId) throws SQLException {
        List<Favorite> list = favoriteDAO.getFavoritesByEbook(ebookId);
        return (list != null) ? list.size() : 0;
    }
} 