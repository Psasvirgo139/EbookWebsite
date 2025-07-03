package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.AuthorDAO;
import com.mycompany.ebookwebsite.model.Author;
import com.mycompany.ebookwebsite.model.User;
import java.sql.SQLException;
import java.util.List;

public class AuthorService {
    private final AuthorDAO authorDAO = new AuthorDAO();

    public void addAuthor(Author author) throws SQLException {
        authorDAO.insertAuthor(author);
    }
    public boolean updateAuthor(Author author) throws SQLException {
        return authorDAO.updateAuthor(author);
    }
    public boolean deleteAuthor(int id, User user) throws SQLException {
        if (!"admin".equals(user.getRole())) {
            throw new SecurityException("Bạn không có quyền xóa tác giả!");
        }
        return authorDAO.deleteAuthor(id);
    }
    public Author getAuthorById(int id) throws SQLException {
        return authorDAO.selectAuthor(id);
    }
    public List<Author> getAllAuthors() throws SQLException {
        return authorDAO.selectAllAuthors();
    }
    public List<Author> searchAuthors(String name) throws SQLException {
        return authorDAO.search(name);
    }
} 