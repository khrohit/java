package com.shopsphere.controller;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.SessionKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {
    private final OrderDAO orders = new OrderDAO();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute(SessionKeys.USER);
            request.setAttribute("orders", orders.findByUser(user.getUserId()));
            request.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(request, response);
        } catch (Exception exception) {
            request.setAttribute("error", "Orders could not be loaded.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
