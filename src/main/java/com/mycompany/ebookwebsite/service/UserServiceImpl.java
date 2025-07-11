package com.mycompany.ebookwebsite.service;

import java.sql.SQLException;
import java.util.List;

import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.dao.UserDAO;

public class UserServiceImpl {

    private final UserDAO UserDAO;

    public UserServiceImpl() {
        this.UserDAO = new UserDAO();
    }

    public void addUser(User user) throws SQLException {
        UserDAO.insert(user);
    }

    public User getUserById(int id) throws SQLException {
        return UserDAO.findById(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return UserDAO.findAll();
    }

    public boolean modifyUser(User user) throws SQLException {
        return UserDAO.update(user);
    }

    public boolean removeUser(int id) throws SQLException {
        return UserDAO.delete(id);
    }

    public User checkLogin(String username, String password) throws SQLException {
        return UserDAO.login(username, password);
    }
} 
