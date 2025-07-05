package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Tag;
import com.mycompany.ebookwebsite.service.TagService;
import com.mycompany.ebookwebsite.utils.TagValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/tag/*")
public class TagServlet extends HttpServlet {
    private TagService tagService;

    @Override
    public void init() {
        tagService = new TagService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // Kiểm tra quyền admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userRole") == null || 
            !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        String action = (pathInfo != null) ? pathInfo.substring(1) : "list";

        try {
            switch (action) {
                case "list":
                    listTags(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteTag(request, response);
                    break;
                default:
                    listTags(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            try {
                listTags(request, response);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            try {
                listTags(request, response);
            } catch (Exception ex) {
                throw new ServletException(ex);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // Kiểm tra quyền admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userRole") == null || 
            !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        String action = (pathInfo != null) ? pathInfo.substring(1) : "create";

        try {
            switch (action) {
                case "create":
                    createTag(request, response);
                    break;
                case "update":
                    updateTag(request, response);
                    break;
                default:
                    createTag(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            doGet(request, response);
        }
    }

    private void listTags(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
        
        // Pagination
        int page = 1;
        int pageSize = 10;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            // Sử dụng giá trị mặc định
        }

        List<Tag> tags = tagService.getAllTags();
        int totalTags = tags.size();
        int totalPages = (int) Math.ceil((double) totalTags / pageSize);
        
        // Tính toán offset cho pagination
        int offset = (page - 1) * pageSize;
        int endIndex = Math.min(offset + pageSize, totalTags);
        
        List<Tag> paginatedTags = tags.subList(offset, endIndex);

        request.setAttribute("tags", paginatedTags);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalTags", totalTags);
        request.getRequestDispatcher("/admin/tag/list.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
        
        int tagId = TagValidation.validateId(request.getParameter("id"));
        Tag tag = tagService.getTagById(tagId);
        
        if (tag == null) {
            request.setAttribute("error", "Không tìm thấy tag với ID: " + tagId);
            listTags(request, response);
            return;
        }
        
        request.setAttribute("tag", tag);
        request.getRequestDispatcher("/admin/tag/edit.jsp").forward(request, response);
    }

    private void createTag(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
        
        String name = request.getParameter("name");
        
        // Validation
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Tên tag không được để trống");
            doGet(request, response);
            return;
        }

        name = name.trim();
        if (name.length() > 50) {
            request.setAttribute("error", "Tên tag không được vượt quá 50 ký tự");
            doGet(request, response);
            return;
        }

        // Kiểm tra tag đã tồn tại
        if (tagService.isTagExists(name)) {
            request.setAttribute("error", "Tag '" + name + "' đã tồn tại");
            doGet(request, response);
            return;
        }

        Tag tag = new Tag();
        tag.setName(name);
        tagService.createTag(tag);
        
        request.setAttribute("success", "Tạo tag thành công: " + name);
        response.sendRedirect(request.getContextPath() + "/admin/tag/list");
    }

    private void updateTag(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
        
        int tagId = TagValidation.validateId(request.getParameter("id"));
        String name = request.getParameter("name");
        
        // Validation
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Tên tag không được để trống");
            showEditForm(request, response);
            return;
        }

        name = name.trim();
        if (name.length() > 50) {
            request.setAttribute("error", "Tên tag không được vượt quá 50 ký tự");
            showEditForm(request, response);
            return;
        }

        Tag existingTag = tagService.getTagById(tagId);
        if (existingTag == null) {
            request.setAttribute("error", "Không tìm thấy tag với ID: " + tagId);
            listTags(request, response);
            return;
        }

        // Kiểm tra tag đã tồn tại (trừ tag hiện tại)
        if (!name.equals(existingTag.getName()) && tagService.isTagExists(name)) {
            request.setAttribute("error", "Tag '" + name + "' đã tồn tại");
            showEditForm(request, response);
            return;
        }

        existingTag.setName(name);
        tagService.updateTag(existingTag);
        
        request.setAttribute("success", "Cập nhật tag thành công");
        response.sendRedirect(request.getContextPath() + "/admin/tag/list");
    }

    private void deleteTag(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
        
        int tagId = TagValidation.validateId(request.getParameter("id"));
        
        Tag tag = tagService.getTagById(tagId);
        if (tag == null) {
            request.setAttribute("error", "Không tìm thấy tag với ID: " + tagId);
            listTags(request, response);
            return;
        }

        // Kiểm tra xem tag có đang được sử dụng không
        if (tagService.isTagInUse(tagId)) {
            request.setAttribute("error", "Không thể xóa tag '" + tag.getName() + "' vì đang được sử dụng");
            listTags(request, response);
            return;
        }

        tagService.deleteTag(tagId);
        request.setAttribute("success", "Xóa tag thành công: " + tag.getName());
        response.sendRedirect(request.getContextPath() + "/admin/tag/list");
    }
}
