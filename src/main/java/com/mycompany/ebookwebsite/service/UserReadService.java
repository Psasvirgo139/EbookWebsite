package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.UserReadDAO;
import com.mycompany.ebookwebsite.model.UserRead;
import com.mycompany.ebookwebsite.model.User;
import java.sql.SQLException;
import java.util.List;

public class UserReadService {
    private final UserReadDAO userReadDAO = new UserReadDAO();

    public void addUserRead(UserRead userRead) throws SQLException {
        userReadDAO.insertUserRead(userRead);
    }
    public boolean updateUserRead(UserRead userRead) throws SQLException {
        return userReadDAO.updateUserRead(userRead);
    }
    public boolean deleteUserRead(int userId, int ebookId, User user) throws SQLException {
        if (user.getId() != userId && !"admin".equals(user.getRole())) {
            throw new SecurityException("Bạn không có quyền xóa lịch sử đọc này!");
        }
        return userReadDAO.deleteUserRead(userId, ebookId);
    }
    public List<UserRead> getUserReadByUser(int userId) throws SQLException {
        return userReadDAO.selectByUser(userId);
    }
    public List<UserRead> getUserReadByEbook(int ebookId) throws SQLException {
        return userReadDAO.selectByEbook(ebookId);
    }
} 