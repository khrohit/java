package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.util.SessionKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/order-success")
public class OrderSuccessServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(SessionKeys.USER);
        request.setAttribute("orderId", request.getParameter("id")); request.setAttribute("name", user.getName());
        request.getRequestDispatcher("/WEB-INF/views/order-success.jsp").forward(request, response);
    }
}
