package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.CheckoutService;
import com.shopsphere.util.SessionKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private final CartService cart = new CartService();
    private final CheckoutService checkout = new CheckoutService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute(SessionKeys.USER);
            var items = cart.items(user.getUserId());
            request.setAttribute("items", items); request.setAttribute("total", cart.total(items));
            request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);
        } catch (Exception exception) {
            request.setAttribute("error", "Checkout could not be loaded.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute(SessionKeys.USER);
            long orderId = checkout.placeOrder(user.getUserId(), request.getParameter("addressLine"), request.getParameter("city"),
                    request.getParameter("state"), request.getParameter("pincode"), request.getParameter("coupon"));
            response.sendRedirect(request.getContextPath() + "/order-success?id=" + orderId);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            request.setAttribute("error", exception.getMessage()); doGet(request, response);
        } catch (Exception exception) {
            request.setAttribute("error", "Order could not be placed. Please try again."); doGet(request, response);
        }
    }
}
