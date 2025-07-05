package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.Ebook;

import java.sql.SQLException;
import java.util.List;

public class EbookService {
    private final EbookDAO ebookDAO = new EbookDAO();

    public List<Ebook> getBooksByPage(int offset, int pageSize) throws SQLException {
        return ebookDAO.selectBooksByPage(offset, pageSize);
    }

    public int countAllBooks() throws SQLException {
        return ebookDAO.countAllBooks();
    }

    public Ebook getEbookById(int id) throws SQLException {
        return ebookDAO.getEbookById(id);
    }

    public void incrementViewCount(int id) throws SQLException {
        ebookDAO.incrementViewCount(id);
    }

    public List<Ebook> getAllEbooks() throws SQLException {
        return ebookDAO.getAllEbooks();
    }

    public List<Ebook> searchBooks(String searchTerm, int offset, int pageSize) throws SQLException {
        return ebookDAO.searchBooks(searchTerm, offset, pageSize);
    }

    public int countSearchResults(String searchTerm) throws SQLException {
        return ebookDAO.countSearchResults(searchTerm);
    }

    public List<Ebook> getBooksByStatus(String status, int offset, int pageSize) throws SQLException {
        return ebookDAO.getBooksByStatus(status, offset, pageSize);
    }

    public int countBooksByStatus(String status) throws SQLException {
        return ebookDAO.countBooksByStatus(status);
    }

    public void createBook(Ebook book) throws SQLException {
        ebookDAO.createBook(book);
    }

    public boolean updateBook(Ebook book) throws SQLException {
        return ebookDAO.updateBook(book);
    }

    public boolean deleteBook(int id) throws SQLException {
        return ebookDAO.deleteBook(id);
    }

    public void updateBookPremiumStatus(int bookId, boolean isPremium, double price) throws SQLException {
        ebookDAO.updateBookPremiumStatus(bookId, isPremium, price);
    }

    public List<Ebook> getTopPremiumBooks(int limit) throws SQLException {
        return ebookDAO.getTopPremiumBooks(limit);
    }

    public List<Ebook> getTopFreeBooks(int limit) throws SQLException {
        return ebookDAO.getTopFreeBooks(limit);
    }

    public List<Ebook> getRecentBooks(int limit) throws SQLException {
        return ebookDAO.getRecentBooks(limit);
    }
}
