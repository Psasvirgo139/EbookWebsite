package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Tag;
import com.mycompany.ebookwebsite.service.TagService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/tag")
public class TagServlet extends HttpServlet {
    private TagService tagService;

    @Override
    public void init() {
        tagService = new TagService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        try {
            List<Tag> tags = tagService.getAllTags();
            request.setAttribute("tags", tags);
            request.getRequestDispatcher("/admin/tag/list.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Không thể tải danh sách tag", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String name = request.getParameter("name");
        try {
            Tag tag = new Tag();
            tag.setName(name.trim());
            tagService.createTag(tag);
            response.sendRedirect(request.getContextPath() + "/tag");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }
    }
}
