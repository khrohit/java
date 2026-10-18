package com.shopsphere.controller;

import com.shopsphere.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin")
public class AdminDashboardServlet extends HttpServlet {
    private final AdminService admin = new AdminService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("counts", admin.counts());
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
        } catch (Exception exception) {
            request.setAttribute("error", "Admin dashboard could not load.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
