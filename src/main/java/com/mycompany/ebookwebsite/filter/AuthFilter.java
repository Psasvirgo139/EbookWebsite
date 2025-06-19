package com.mycompany.ebookwebsite.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")                // áp cho mọi URL
public class AuthFilter implements Filter {

    @Override public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Bỏ qua đường dẫn tĩnh & trang login
        String uri = request.getRequestURI();
        if (uri.contains("/login") || uri.matches(".*(\\.css|\\.js|\\.png|\\.jpg)$")) {
            chain.doFilter(req, res);
            return;
        }

        // Kiểm tra session
        HttpSession session = request.getSession(false);
        Object userObj = (session != null) ? session.getAttribute("user") : null;

        if (userObj == null) {                       // Chưa login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Phân quyền đơn giản
        String role = ((com.mycompany.ebookwebsite.model.User) userObj).getRole();
        if (uri.startsWith("/admin") && !"admin".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(req, res);                   // cho chạy tiếp
    }
}
