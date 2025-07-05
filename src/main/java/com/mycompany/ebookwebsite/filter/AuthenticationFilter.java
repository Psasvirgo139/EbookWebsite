package com.mycompany.ebookwebsite.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    // Danh sách các URL không cần đăng nhập
    private static final List<String> PUBLIC_URLS = Arrays.asList(
        "/",
        "/home",
        "/index.jsp",
        "/login",
        "/register",
        "/logout",
        "/css/",
        "/js/",
        "/images/",
        "/fonts/",
        "/favicon.ico",
        "/error.jsp",
        "/unauthorized.jsp",
        "/forgot-password",
        "/reset-password",
        "/book/detail"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần khởi tạo gì
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // Loại bỏ context path để so sánh
        String path = requestURI.substring(contextPath.length());
        
        // Kiểm tra xem có phải URL công khai không
        if (isPublicUrl(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Kiểm tra session
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            // Chưa đăng nhập, redirect về trang login
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }
        
        // Đã đăng nhập, tiếp tục
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Không cần cleanup gì
    }
    
    /**
     * Kiểm tra xem URL có phải là public không
     */
    private boolean isPublicUrl(String path) {
        // Cho phép truy cập nếu là trang chủ hoặc rỗng
        if (path == null || path.isEmpty() || "/".equals(path)) {
            return true;
        }
        // Kiểm tra các URL công khai
        for (String publicUrl : PUBLIC_URLS) {
            if (path.startsWith(publicUrl)) {
                return true;
            }
        }
        // Kiểm tra các file tĩnh
        if (path.contains(".")) {
            String extension = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
            return Arrays.asList("css", "js", "png", "jpg", "jpeg", "gif", "ico", "svg", "woff", "woff2", "ttf").contains(extension);
        }
        return false;
    }
} 