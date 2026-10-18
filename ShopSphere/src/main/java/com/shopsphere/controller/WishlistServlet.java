package com.shopsphere.controller;

import com.shopsphere.dao.WishlistDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.SessionKeys;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {
    private final WishlistDAO wishlist = new WishlistDAO();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute(SessionKeys.USER);
            request.setAttribute("products", wishlist.findByUser(user.getUserId()));
            request.setAttribute("notice", request.getSession().getAttribute("wishlistNotice"));
            request.getSession().removeAttribute("wishlistNotice");
            request.getRequestDispatcher("/WEB-INF/views/wishlist.jsp").forward(request, response);
        } catch (Exception exception) {
            request.setAttribute("error", "Your wishlist could not be loaded.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute(SessionKeys.USER);
        try {
            long productId = Long.parseLong(request.getParameter("productId"));
            if ("remove".equals(request.getParameter("action"))) wishlist.remove(user.getUserId(), productId);
            else wishlist.add(user.getUserId(), productId);
            request.getSession().setAttribute("wishlistNotice", "Wishlist updated.");
        } catch (Exception exception) { request.getSession().setAttribute("wishlistNotice", "Wishlist could not be updated."); }
        response.sendRedirect(request.getContextPath() + "/wishlist");
    }
}
