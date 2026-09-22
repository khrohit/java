package com.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Application Started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Application Stopped");
    }
}