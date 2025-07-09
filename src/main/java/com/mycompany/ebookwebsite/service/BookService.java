package com.mycompany.ebookwebsite.service;

import com.mycompany.ebookwebsite.dao.EbookDAO;
import com.mycompany.ebookwebsite.model.Ebook;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    private EbookDAO ebookDAO;

    public BookService() {
        this.ebookDAO = new EbookDAO();
    }

    public List<Ebook> getAllBooks() throws SQLException {
        return ebookDAO.selectAllEbooks();
    }

    public Ebook getBookById(int id) throws SQLException {
        return ebookDAO.selectEbook(id);
    }

    public void createBook(Ebook book) throws SQLException {
        ebookDAO.insertEbook(book);
    }

    public void updateBook(Ebook book) throws SQLException {
        ebookDAO.updateEbook(book);
    }

    public void deleteBook(int id) throws SQLException {
        ebookDAO.deleteEbook(id);
    }

    public List<Ebook> searchBooks(String keyword) throws SQLException {
        return ebookDAO.search(keyword);
    }

    public void incrementViewCount(int id) throws SQLException {
        ebookDAO.incrementViewCount(id);
    }

    public List<Ebook> getBooksByUploader(int uploaderId) throws SQLException {
        return ebookDAO.selectEbooksByUploader(uploaderId);
    }

    /**
     * Get books by category/genre
     */
    public List<Ebook> getBooksByCategory(String category) throws SQLException {
        List<Ebook> allBooks = ebookDAO.selectAllEbooks();
        return allBooks.stream()
                .filter(book -> book.getReleaseType() != null && 
                               book.getReleaseType().toLowerCase().contains(category.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Get all available genres/categories
     */
    public List<String> getAllGenres() throws SQLException {
        List<Ebook> allBooks = ebookDAO.selectAllEbooks();
        return allBooks.stream()
                .map(Ebook::getReleaseType)
                .filter(genre -> genre != null && !genre.trim().isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
} 