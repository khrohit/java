package com.shopsphere.controller;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.SessionKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private final UserDAO users = new UserDAO();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User current = (User) request.getSession().getAttribute(SessionKeys.USER);
        String name = request.getParameter("name") == null ? "" : request.getParameter("name").trim();
        String mobile = request.getParameter("mobile") == null ? "" : request.getParameter("mobile").trim();
        try {
            if (name.length() < 2 || name.length() > 120 || mobile.length() > 30) throw new IllegalArgumentException("Enter a valid name and phone number.");
            users.updateProfile(current.getUserId(), name, mobile);
            request.getSession().setAttribute(SessionKeys.USER, new User(current.getUserId(), name, current.getEmail(), mobile, current.getRole()));
            request.setAttribute("notice", "Your profile has been updated.");
        } catch (Exception exception) {
            request.setAttribute("error", exception instanceof IllegalArgumentException ? exception.getMessage() : "Your profile could not be updated.");
        }
        doGet(request, response);
    }
}
