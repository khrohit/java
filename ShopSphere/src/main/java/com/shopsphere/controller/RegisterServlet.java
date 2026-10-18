package com.shopsphere.controller;

import com.shopsphere.service.AuthService;
import com.shopsphere.util.SessionKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final AuthService auth = new AuthService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            auth.register(request.getParameter("name"), request.getParameter("email"), request.getParameter("mobile"), request.getParameter("password"));
            var user = auth.login(request.getParameter("email"), request.getParameter("password"));
            request.getSession(true).setAttribute(SessionKeys.USER, user);
            response.sendRedirect(request.getContextPath() + "/");
        } catch (IllegalArgumentException exception) {
            request.setAttribute("error", exception.getMessage()); doGet(request, response);
        } catch (Exception exception) {
            getServletContext().log("ShopSphere account creation failed.", exception);
            request.setAttribute("error", "Account creation is unavailable. Check the MySQL connection."); doGet(request, response);
        }
    }
}
