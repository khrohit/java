package com.shopsphere.controller;

import com.shopsphere.service.CatalogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private final CatalogService catalog = new CatalogService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("products", catalog.featured());
            request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
        } catch (Exception exception) {
            getServletContext().log("ShopSphere could not load featured products.", exception);
            request.setAttribute("error", "ShopSphere could not load products. Check that MySQL is running and the ShopSphere database has been imported.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
