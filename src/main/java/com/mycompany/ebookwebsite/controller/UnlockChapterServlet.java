package com.mycompany.ebookwebsite.controller;

import com.mycompany.ebookwebsite.dao.ChapterDAO;
import com.mycompany.ebookwebsite.model.Chapter;
import com.mycompany.ebookwebsite.model.User;
import com.mycompany.ebookwebsite.service.CoinService;
import com.mycompany.ebookwebsite.utils.CoinUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet để xử lý unlock chapter premium bằng coin
 * @author ADMIN
 */
// @WebServlet(name = "UnlockChapterServlet", urlPatterns = {"/unlock-chapter"})
// DISABLED: Functionality moved to BookReadServlet for pure JSP approach
public class UnlockChapterServlet extends HttpServlet {
    
    private final CoinService coinService = new CoinService();
    private final ChapterDAO chapterDAO = new ChapterDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }
        
        try {
            int chapterId = Integer.parseInt(request.getParameter("chapterId"));
            String action = request.getParameter("action");
            
            if ("unlock".equals(action)) {
                handleUnlockChapter(request, response, user, chapterId);
            } else if ("check".equals(action)) {
                handleCheckAccess(request, response, user, chapterId);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid chapter ID");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
    
    private void handleUnlockChapter(HttpServletRequest request, HttpServletResponse response, 
                                   User user, int chapterId) throws SQLException, IOException {
        try {
            // Lấy thông tin chapter
            Chapter chapter = chapterDAO.getChapterById(chapterId);
            if (chapter == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Chapter not found");
                return;
            }
            
            // Kiểm tra chapter có phải premium không
            if (!"premium".equals(chapter.getAccessLevel())) {
                response.getWriter().write("{\"success\": false, \"message\": \"Chapter này không cần unlock\"}");
                return;
            }
            
            // Thực hiện unlock
            coinService.unlockChapter(user.getId(), chapterId, user);
            
            // Lấy số coin còn lại
            int remainingCoins = coinService.getUserCoins(user.getId());
            
            // Cập nhật coin trong session
            CoinUtil.refreshUserCoins(request.getSession());
            
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"success\": true, \"message\": \"Unlock chapter thành công!\", \"remainingCoins\": %d}", 
                remainingCoins
            ));
            
        } catch (IllegalStateException e) {
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"success\": false, \"message\": \"%s\"}", 
                e.getMessage()
            ));
        } catch (IllegalArgumentException e) {
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"success\": false, \"message\": \"%s\"}", 
                e.getMessage()
            ));
        } catch (SecurityException e) {
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"success\": false, \"message\": \"%s\"}", 
                e.getMessage()
            ));
        }
    }
    
    private void handleCheckAccess(HttpServletRequest request, HttpServletResponse response,
                                 User user, int chapterId) throws SQLException, IOException {
        
        boolean isAccessible = coinService.isChapterAccessible(user.getId(), chapterId);
        int userCoins = coinService.getUserCoins(user.getId());
        int unlockCost = CoinService.getUnlockChapterCost();
        
        // Lấy thông tin chapter
        Chapter chapter = chapterDAO.getChapterById(chapterId);
        boolean isPremium = chapter != null && "premium".equals(chapter.getAccessLevel());
        
        response.setContentType("application/json");
        response.getWriter().write(String.format(
            "{\"accessible\": %b, \"userCoins\": %d, \"unlockCost\": %d, \"isPremium\": %b}", 
            isAccessible, userCoins, unlockCost, isPremium
        ));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GET request để check access
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }
        
        try {
            int chapterId = Integer.parseInt(request.getParameter("chapterId"));
            handleCheckAccess(request, response, user, chapterId);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid chapter ID");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
} 