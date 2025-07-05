package com.mycompany.ebookwebsite.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần khởi tạo gì
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Kiểm tra session và quyền admin
        if (session == null || session.getAttribute("userId") == null) {
            // Chưa đăng nhập
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");
        if (userRole == null || !"admin".equals(userRole)) {
            // Không có quyền admin
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/unauthorized");
            return;
        }

        // Có quyền admin, tiếp tục
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Không cần cleanup gì
    }
} 