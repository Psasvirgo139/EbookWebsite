package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.UserReadDAO;
import com.mycompany.ebookwebsite.model.UserRead;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class UserReadService {
    private final UserReadDAO userReadDAO = new UserReadDAO();

    public void updateReadingProgress(int userId, int ebookId, int chapterId) throws SQLException {
        UserRead userRead = new UserRead();
        userRead.setUserID(userId);
        userRead.setEbookID(ebookId);
        userRead.setLastReadChapterID(chapterId);
        userRead.setLastReadAt(LocalDate.now());
        userReadDAO.insertOrUpdateUserRead(userRead);
    }

    public UserRead getUserReadProgress(int userId, int ebookId) throws SQLException {
        return userReadDAO.selectUserRead(userId, ebookId);
    }

    public List<UserRead> getUserReadHistory(int userId) throws SQLException {
        return userReadDAO.selectByUser(userId);
    }

    public List<UserRead> getEbookReadHistory(int ebookId) throws SQLException {
        return userReadDAO.selectByEbook(ebookId);
    }

    public void deleteUserRead(int userId, int ebookId) throws SQLException {
        userReadDAO.deleteUserRead(userId, ebookId);
    }

    public void deleteAllUserReads(int userId) throws SQLException {
        userReadDAO.deleteByUser(userId);
    }

    public void deleteAllEbookReads(int ebookId) throws SQLException {
        userReadDAO.deleteByEbook(ebookId);
    }
} 