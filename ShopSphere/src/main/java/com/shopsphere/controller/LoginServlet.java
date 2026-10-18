package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.AuthService;
import com.shopsphere.util.SessionKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService auth = new AuthService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = auth.login(request.getParameter("email"), request.getParameter("password"));
            if (user == null) {
                request.setAttribute("error", "Email or password is incorrect.");
                doGet(request, response);
                return;
            }
            request.changeSessionId();
            request.getSession().setAttribute(SessionKeys.USER, user);
            Object returnTo = request.getSession().getAttribute("returnTo");
            request.getSession().removeAttribute("returnTo");
            if (returnTo instanceof String path && path.startsWith(request.getContextPath() + "/")) response.sendRedirect(path);
            else response.sendRedirect(request.getContextPath() + (user.isAdmin() ? "/admin" : "/"));
        } catch (Exception exception) {
            request.setAttribute("error", "Sign in is unavailable. Check the MySQL connection.");
            doGet(request, response);
        }
    }
}
