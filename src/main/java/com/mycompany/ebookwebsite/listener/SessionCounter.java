/*
 * SessionCounter – đếm số phiên (user) đang online.
 * © 2025 EbookWebsite
 */
package com.mycompany.ebookwebsite.listener;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionCounter implements HttpSessionListener {

    private static final String ONLINE_USERS_ATTR = "onlineUsers";
    private static final Logger LOG = Logger.getLogger(SessionCounter.class.getName());

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        ServletContext ctx = se.getSession().getServletContext();
        AtomicInteger counter = getCounter(ctx);
        counter.incrementAndGet();
        LOG.info("Session created. Online users: " + counter.get());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        ServletContext ctx = se.getSession().getServletContext();
        AtomicInteger counter = getCounter(ctx);
        counter.decrementAndGet();
        LOG.info("Session destroyed. Online users: " + counter.get());
    }

    /**
     * Lấy (hoặc khởi tạo) biến đếm trong ServletContext, bảo đảm thread-safe
     */
    private AtomicInteger getCounter(ServletContext ctx) {
        synchronized (ctx) {          // đồng bộ ngắn gọn cho thao tác init
            Object attr = ctx.getAttribute(ONLINE_USERS_ATTR);
            AtomicInteger counter;
            
            if (attr == null) {
                counter = new AtomicInteger(0);
                ctx.setAttribute(ONLINE_USERS_ATTR, counter);
            } else if (attr instanceof AtomicInteger) {
                counter = (AtomicInteger) attr;
            } else {
                // Nếu có giá trị cũ không phải AtomicInteger, tạo mới
                LOG.warning("Found non-AtomicInteger value for " + ONLINE_USERS_ATTR + ", creating new counter");
                counter = new AtomicInteger(0);
                ctx.setAttribute(ONLINE_USERS_ATTR, counter);
            }
            
            return counter;
        }
    }
}
