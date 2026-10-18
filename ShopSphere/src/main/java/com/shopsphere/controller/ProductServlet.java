package com.shopsphere.controller;

import com.shopsphere.service.CatalogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private final CatalogService catalog = new CatalogService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("products", catalog.search(request.getParameter("q"), parseLong(request.getParameter("category")), request.getParameter("sort")));
            request.setAttribute("categories", catalog.categories());
            request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);
        } catch (Exception exception) {
            request.setAttribute("error", "Products could not be loaded. Check the database setup and try again.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    private Long parseLong(String value) {
        try { return value == null || value.isBlank() ? null : Long.valueOf(value); }
        catch (NumberFormatException exception) { return null; }
    }
}
