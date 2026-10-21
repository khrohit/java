package com.shopsphere.controller;

import com.shopsphere.service.CatalogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private static final int PAGE_SIZE = 12;
    private final CatalogService catalog = new CatalogService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String query = request.getParameter("q");
            Long category = parseLong(request.getParameter("category"));
            int totalProducts = catalog.countMatching(query, category);
            int totalPages = Math.max(1, (int) Math.ceil(totalProducts / (double) PAGE_SIZE));
            int page = Math.min(parsePage(request.getParameter("page")), totalPages);
            int offset = (page - 1) * PAGE_SIZE;
            request.setAttribute("products", catalog.searchPage(query, category, request.getParameter("sort"), PAGE_SIZE, offset));
            request.setAttribute("categories", catalog.categories());
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
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
    private int parsePage(String value) {
        try { return Math.max(1, Integer.parseInt(value)); }
        catch (NumberFormatException exception) { return 1; }
    }
}
