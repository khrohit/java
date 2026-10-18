package com.shopsphere.controller;

import com.shopsphere.model.Product;
import com.shopsphere.service.CatalogService;
import com.shopsphere.dao.ReviewDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/product")
public class ProductDetailServlet extends HttpServlet {
    private final CatalogService catalog = new CatalogService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Product product = catalog.find(id);
            if (product == null) { response.sendError(404); return; }
            request.setAttribute("product", product);
            request.setAttribute("reviews", new ReviewDAO().findByProduct(id));
            request.setAttribute("reviewNotice", request.getSession().getAttribute("reviewNotice"));
            request.getSession().removeAttribute("reviewNotice");
            request.getRequestDispatcher("/WEB-INF/views/product-details.jsp").forward(request, response);
        } catch (NumberFormatException exception) { response.sendError(400); }
        catch (Exception exception) {
            request.setAttribute("error", "Product details could not be loaded.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
}
