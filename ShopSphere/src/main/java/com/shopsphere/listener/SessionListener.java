package com.shopsphere.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

@WebListener
public class SessionListener implements HttpSessionListener {
    private static final AtomicInteger ACTIVE_SESSIONS = new AtomicInteger();
    @Override public void sessionCreated(HttpSessionEvent event) {
        int count = ACTIVE_SESSIONS.incrementAndGet();
        event.getSession().getServletContext().setAttribute("activeSessions", count);
    }
    @Override public void sessionDestroyed(HttpSessionEvent event) {
        int count = Math.max(0, ACTIVE_SESSIONS.decrementAndGet());
        event.getSession().getServletContext().setAttribute("activeSessions", count);
    }
}
