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
}
