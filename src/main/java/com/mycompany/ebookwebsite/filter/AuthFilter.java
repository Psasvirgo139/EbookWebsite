package com.mycompany.ebookwebsite.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")                // áp cho mọi URL
public class AuthFilter implements Filter {

    @Override public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();

        // Các đường dẫn không cần đăng nhập (public access)
        if (
            uri.endsWith("/login") ||
            uri.endsWith("/register") ||
            uri.endsWith("/logout") ||           // Cho phép logout
            uri.endsWith("/forgot-password") ||  // Cho phép quên mật khẩu
            uri.endsWith("/reset-password") ||   // Cho phép đặt lại mật khẩu
            uri.endsWith("/") ||
            uri.endsWith("/index.jsp") ||
            uri.endsWith("/index.html") ||
            uri.endsWith("/book/home") ||        // Trang chủ sách - user có thể browse
            uri.endsWith("/book/detail") ||      // Chi tiết sách - xem thông tin trước khi đăng ký
            uri.endsWith("/book/read") ||        // Đọc sách - cho phép xem chapter public, premium chapter tự redirect
            uri.endsWith("/tag") ||              // Tags - để filter/search sách
            uri.endsWith("/comment/list") ||     // Xem comments - đọc review từ users khác
            uri.equals(ctx + "") ||              // Landing page
            uri.startsWith(ctx + "/assets/") ||
            uri.matches(".*(\\.css|\\.js|\\.png|\\.jpg|\\.gif|\\.woff2|\\.woff|\\.ttf)$")
        ) {
            chain.doFilter(req, res);
            return;
        }

        // Kiểm tra session
        HttpSession session = request.getSession(false);
        Object userObj = (session != null) ? session.getAttribute("user") : null;

        if (userObj == null) {                       // Chưa login
            // Lưu URL gốc vào session để chuyển hướng lại sau đăng nhập
            String original = uri.substring(ctx.length()); // bỏ context path
            if (request.getQueryString() != null) {
                original += "?" + request.getQueryString();
            }
            request.getSession(true).setAttribute("redirectAfterLogin", original);
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Phân quyền đơn giản
        String role = ((com.mycompany.ebookwebsite.model.User) userObj).getRole();
        if (uri.startsWith(ctx + "/admin") && !"admin".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(req, res);                   // cho chạy tiếp
    }
}
