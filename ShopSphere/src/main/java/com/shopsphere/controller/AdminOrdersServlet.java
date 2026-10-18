package com.shopsphere.controller;

import com.shopsphere.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/orders")
public class AdminOrdersServlet extends HttpServlet {
    private final AdminService admin = new AdminService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("orders", admin.orders());
            request.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(request, response);
        } catch (Exception exception) {
            request.setAttribute("error", "Order management could not load.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try { admin.updateOrder(Long.parseLong(request.getParameter("orderId")), request.getParameter("status")); }
        catch (Exception ignored) { request.getSession().setAttribute("adminNotice", "Order status could not be changed."); }
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}
