/*
 * SessionCounter – đếm số phiên (user) đang online.
 * © 2025 EbookWebsite
 */
package com.mycompany.ebookwebsite.listener;

import com.mycompany.ebookwebsite.bean.CounterBean;
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

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        ServletContext ctx = se.getSession().getServletContext();
        CounterBean counter = (CounterBean) ctx.getAttribute("counter");
        if (counter == null) {
            counter = new CounterBean();
            ctx.setAttribute("counter", counter);
        }
        counter.increment();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        CounterBean counter = (CounterBean) se.getSession()
                .getServletContext()
                .getAttribute("counter");
        if (counter != null) {
            counter.decrement();
        }
    }

    /**
     * Lấy (hoặc khởi tạo) biến đếm trong ServletContext, bảo đảm thread-safe
     */
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
