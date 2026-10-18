package com.shopsphere.controller;

import com.shopsphere.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {
    private final UserDAO users = new UserDAO();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("customers", users.findCustomers());
            request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
        } catch (Exception exception) { request.setAttribute("error", "Customers could not be loaded."); request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response); }
    }
}
