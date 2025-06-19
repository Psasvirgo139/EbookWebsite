/*
 * SessionCounter – đếm số phiên (user) đang online.
 * © 2025 EbookWebsite
 */
package com.mycompany.ebookwebsite.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@WebListener
public class SessionCounter implements HttpSessionListener {

    private static final String ONLINE_USERS_ATTR = "onlineUsers";
    private static final Logger LOG = Logger.getLogger(SessionCounter.class.getName());

    /** Mỗi khi tạo session mới */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        ServletContext ctx = se.getSession().getServletContext();
        AtomicInteger counter = getCounter(ctx);
        int current = counter.incrementAndGet();
        LOG.fine("Session CREATED – onlineUsers = " + current);
    }

    /** Mỗi khi session bị huỷ (timeout, logout…) */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        ServletContext ctx = se.getSession().getServletContext();
        AtomicInteger counter = getCounter(ctx);
        int current = counter.decrementAndGet();
        if (current < 0) {            // bảo vệ trong trường hợp ngoại lệ
            counter.set(0);
            current = 0;
        }
        LOG.fine("Session DESTROYED – onlineUsers = " + current);
    }

    /** Lấy (hoặc khởi tạo) biến đếm trong ServletContext, bảo đảm thread-safe */
    private AtomicInteger getCounter(ServletContext ctx) {
        synchronized (ctx) {          // đồng bộ ngắn gọn cho thao tác init
            AtomicInteger counter = (AtomicInteger) ctx.getAttribute(ONLINE_USERS_ATTR);
            if (counter == null) {
                counter = new AtomicInteger(0);
                ctx.setAttribute(ONLINE_USERS_ATTR, counter);
            }
            return counter;
        }
    }
}
