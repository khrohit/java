package com.shopsphere.controller;

import com.shopsphere.model.Product;
import com.shopsphere.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/admin/products")
public class AdminProductsServlet extends HttpServlet {
    private final AdminService admin = new AdminService();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("products", admin.products()); request.setAttribute("categories", admin.categories());
            request.setAttribute("notice", request.getSession().getAttribute("adminNotice"));
            request.getSession().removeAttribute("adminNotice");
            request.getRequestDispatcher("/WEB-INF/views/admin/products.jsp").forward(request, response);
        } catch (Exception exception) {
            request.setAttribute("error", "Product management could not load.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String action = request.getParameter("action");
            if ("archive".equals(action)) admin.archive(Long.parseLong(request.getParameter("productId")));
            else {
                Product p = new Product();
                p.setProductId(parseLong(request.getParameter("productId"), 0));
                p.setCategoryId(parseLong(request.getParameter("categoryId"), 0));
                p.setName(required(request.getParameter("name"), "Product name"));
                p.setBrand(request.getParameter("brand")); p.setDescription(request.getParameter("description"));
                p.setPrice(new BigDecimal(request.getParameter("price")));
                p.setDiscount(new BigDecimal(request.getParameter("discount")));
                p.setStock(Integer.parseInt(request.getParameter("stock"))); p.setImageUrl(request.getParameter("imageUrl"));
                if (p.getPrice().signum() < 0 || p.getDiscount().signum() < 0 || p.getDiscount().compareTo(new BigDecimal("100")) > 0 || p.getStock() < 0)
                    throw new IllegalArgumentException("Enter a valid price, discount and stock quantity.");
                admin.save(p);
            }
            request.getSession().setAttribute("adminNotice", "Product saved.");
        } catch (Exception exception) {
            request.getSession().setAttribute("adminNotice", exception instanceof IllegalArgumentException ? exception.getMessage() : "Product could not be saved.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
    private long parseLong(String value, long fallback) { try { return Long.parseLong(value); } catch (Exception ex) { return fallback; } }
    private String required(String value, String label) { if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " is required."); return value.trim(); }
}
