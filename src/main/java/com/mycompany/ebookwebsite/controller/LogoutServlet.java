/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.mycompany.ebookwebsite.controller;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
@WebServlet(name="LogoutServlet", urlPatterns={"/logout"})
public class LogoutServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(LogoutServlet.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        LOGGER.info("LogoutServlet: GET request received");
        performLogout(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        LOGGER.info("LogoutServlet: POST request received");
        performLogout(request, response);
    }
    
    private void performLogout(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        LOGGER.info("LogoutServlet: Starting logout process");
        
        // Lấy session hiện tại
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            LOGGER.info("LogoutServlet: Session found, invalidating...");
            
            // Xóa tất cả attributes trong session
            session.removeAttribute("user");
            session.removeAttribute("userInfor");
            session.removeAttribute("favorites");
            session.removeAttribute("readingProgress");
            
            // Invalidate session để đảm bảo đăng xuất hoàn toàn
            session.invalidate();
            LOGGER.info("LogoutServlet: Session invalidated successfully");
        } else {
            LOGGER.info("LogoutServlet: No active session found");
        }
        
        // Thêm header để ngăn cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        // Chuyển hướng về trang đăng nhập
        String loginUrl = request.getContextPath() + "/login";
        LOGGER.info("LogoutServlet: Redirecting to: " + loginUrl);
        response.sendRedirect(loginUrl);
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý đăng xuất người dùng";
    }
}
