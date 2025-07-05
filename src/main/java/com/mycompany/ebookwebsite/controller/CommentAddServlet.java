package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.model.Comment;
import com.mycompany.ebookwebsite.service.CommentService;
import com.mycompany.ebookwebsite.utils.CommentValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/add-comment")
public class CommentAddServlet extends HttpServlet {
    private final CommentService commentService = new CommentService();
    private static final ConcurrentHashMap<String, Long> userLastCommentTime = new ConcurrentHashMap<>();
    private static final long RATE_LIMIT_DELAY = 30000; // 30 giây

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
            response.sendRedirect(request.getContextPath() + "/book/detail?id=" + request.getParameter("ebookId"));
            return;
        }

        try {
            int ebookId = CommentValidation.validateId(request.getParameter("ebookId"));
            String content = request.getParameter("content");
            
            // Validation content
            content = CommentValidation.validateContent(content);
            
            // Kiểm tra ebook có tồn tại không
            if (!commentService.isEbookExists(ebookId)) {
                request.setAttribute("error", "Sách không tồn tại");
                response.sendRedirect(request.getContextPath() + "/book/list");
                return;
            }

            Comment comment = new Comment();
            comment.setUserID(userId);
            comment.setEbookID(ebookId);
            comment.setContent(content);
            comment.setCreatedAt(LocalDateTime.now());

            commentService.addComment(comment);
            
            // Cập nhật thời gian comment cuối
            userLastCommentTime.put(userKey, System.currentTimeMillis());
            
            request.setAttribute("success", "Bình luận đã được thêm thành công!");
            response.sendRedirect(request.getContextPath() + "/book/detail?id=" + ebookId);
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/book/detail?id=" + request.getParameter("ebookId"));
        } catch (Exception e) {
            request.setAttribute("error", "Không thể thêm bình luận: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/book/detail?id=" + request.getParameter("ebookId"));
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

