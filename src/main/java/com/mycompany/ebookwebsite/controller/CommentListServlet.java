package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.service.CommentService;
import com.mycompany.ebookwebsite.utils.EbookValidation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import com.mycompany.ebookwebsite.service.CommentVoteService;

@WebServlet("/comment/list")
public class CommentListServlet extends HttpServlet {
    private final CommentService commentService = new CommentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        try {
            String type = request.getParameter("type"); // "book", "chapter", "aggregated"
            int bookId = EbookValidation.validateId(request.getParameter("bookId"));
            
            List<?> comments = null;
            
            switch (type) {
                case "book":
                    // Lấy comment thẳng ở book detail (không có chapterId, không có parentId)
                    comments = commentService.getBookComments(bookId);
                    break;
                    
                case "chapter":
                    // Lấy comment ở chapter cụ thể
                    int chapterId = EbookValidation.validateId(request.getParameter("chapterId"));
                    comments = commentService.getCommentsByChapter(bookId, chapterId);
                    break;
                    
                case "aggregated":
                    // Lấy comment tổng hợp từ các chapter (top comments)
                    int limit = 10; // Số lượng comment tổng hợp
                    String limitStr = request.getParameter("limit");
                    if (limitStr != null && !limitStr.trim().isEmpty()) {
                        limit = Integer.parseInt(limitStr);
                    }
                    comments = commentService.getTopChapterComments(bookId, limit);
                    break;
                    
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Loại comment không hợp lệ");
                    return;
            }
            
            // Set attribute để JSP có thể hiển thị
            request.setAttribute("bookComments", comments);
            request.setAttribute("commentType", type);
            request.setAttribute("bookId", bookId);

            // Format ngày và truyền map dateMap, timeMap
            Map<Integer, String> dateMap = new HashMap<>();
            Map<Integer, String> timeMap = new HashMap<>();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            Map<Integer, Integer> likeMap = new HashMap<>();
            Map<Integer, Integer> dislikeMap = new HashMap<>();
            CommentVoteService voteService = new CommentVoteService();
            if (comments != null) {
                for (Object obj : comments) {
                    if (obj instanceof com.mycompany.ebookwebsite.model.Comment) {
                        com.mycompany.ebookwebsite.model.Comment comment = (com.mycompany.ebookwebsite.model.Comment) obj;
                        if (comment.getCreatedAt() != null) {
                            dateMap.put(comment.getId(), comment.getCreatedAt().format(dateFormatter));
                            timeMap.put(comment.getId(), comment.getCreatedAt().format(timeFormatter));
                        }
                        likeMap.put(comment.getId(), voteService.getLikeCount(comment.getId()));
                        dislikeMap.put(comment.getId(), voteService.getDislikeCount(comment.getId()));
                    }
                }
            }
            request.setAttribute("dateMap", dateMap);
            request.setAttribute("timeMap", timeMap);
            request.setAttribute("likeMap", likeMap);
            request.setAttribute("dislikeMap", dislikeMap);
            
            // Forward đến JSP tương ứng
            if ("chapter".equals(type)) {
                request.getRequestDispatcher("/book/comments-chapter.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/book/comments-book.jsp").forward(request, response);
            }
            
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Có lỗi xảy ra khi tải bình luận");
        }
    }
} 