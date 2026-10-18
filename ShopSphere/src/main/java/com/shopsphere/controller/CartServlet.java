package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.util.SessionKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private final CartService cart = new CartService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute(SessionKeys.USER);
            var items = cart.items(user.getUserId());
            request.setAttribute("items", items); request.setAttribute("total", cart.total(items));
            HttpSession session = request.getSession();
            request.setAttribute("notice", session.getAttribute("cartNotice")); request.setAttribute("error", session.getAttribute("cartError"));
            session.removeAttribute("cartNotice"); session.removeAttribute("cartError");
            request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
        } catch (Exception exception) {
            request.setAttribute("error", "Your cart could not be loaded.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute(SessionKeys.USER);
        try {
            String action = request.getParameter("action"); long productId = Long.parseLong(request.getParameter("productId"));
            if ("add".equals(action)) cart.add(user.getUserId(), productId, parseQuantity(request.getParameter("quantity"), 1));
            else if ("update".equals(action)) cart.update(user.getUserId(), productId, parseQuantity(request.getParameter("quantity"), 0));
            else if ("remove".equals(action)) cart.remove(user.getUserId(), productId);
            else throw new IllegalArgumentException("Unknown cart action.");
            request.getSession().setAttribute("cartNotice", "Cart updated.");
        } catch (Exception exception) {
            request.getSession().setAttribute("cartError", exception instanceof IllegalArgumentException ? exception.getMessage() : "Cart could not be updated.");
        }
        response.sendRedirect(request.getContextPath() + "/cart");
    }
    private int parseQuantity(String value, int fallback) {
        try { return value == null ? fallback : Integer.parseInt(value); }
        catch (NumberFormatException exception) { return fallback; }
    }
}
