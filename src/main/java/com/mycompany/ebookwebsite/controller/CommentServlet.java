package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.service.CommentService;
import com.mycompany.ebookwebsite.utils.CommentValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
    private CommentService commentService;
    private static final ConcurrentHashMap<String, Long> userLastCommentTime = new ConcurrentHashMap<>();
    private static final long RATE_LIMIT_DELAY = 30000; // 30 giây

    @Override
    public void init() {
        commentService = new CommentService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        try {
            int ebookId = CommentValidation.validateId(request.getParameter("ebookId"));
            
            // Pagination
            int page = 1;
            int pageSize = 10;
            try {
                page = Integer.parseInt(request.getParameter("page"));
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                // Sử dụng giá trị mặc định
            }

            // Lấy tổng số comment và tính pagination
            int totalComments = commentService.getCommentCountByEbookId(ebookId);
            int totalPages = (int) Math.ceil((double) totalComments / pageSize);
            
            // Đảm bảo page không vượt quá totalPages
            if (page > totalPages && totalPages > 0) {
                page = totalPages;
            }

            List<Comment> comments = commentService.getCommentsByEbookIdWithPagination(ebookId, page, pageSize);
            
            request.setAttribute("comments", comments);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalComments", totalComments);
            request.setAttribute("ebookId", ebookId);
            
            request.getRequestDispatcher("/book/comments.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Tham số không hợp lệ: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Không thể tải bình luận: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        String userKey = userId.toString();

        // Rate limiting
        if (isRateLimited(userKey)) {
            request.setAttribute("error", "Bạn đã bình luận quá nhanh. Vui lòng đợi 30 giây.");
            doGet(request, response);
            return;
        }

        try {
            int ebookId = CommentValidation.validateId(request.getParameter("ebookId"));
            String content = request.getParameter("content");
            
            // Validation content
            content = CommentValidation.validateContent(content);
            
            Comment comment = new Comment();
            comment.setUserID(userId);
            comment.setEbookID(ebookId);
            comment.setContent(content);

            commentService.addComment(comment);
            
            // Cập nhật thời gian comment cuối
            userLastCommentTime.put(userKey, System.currentTimeMillis());
            
            response.sendRedirect(request.getContextPath() + "/comment?ebookId=" + ebookId);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Không thể thêm bình luận: " + e.getMessage());
            doGet(request, response);
        }
    }
    
    private boolean isRateLimited(String userKey) {
        Long lastCommentTime = userLastCommentTime.get(userKey);
        if (lastCommentTime == null) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastCommentTime) < RATE_LIMIT_DELAY;
    }
}
