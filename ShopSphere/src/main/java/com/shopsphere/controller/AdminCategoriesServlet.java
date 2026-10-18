package com.shopsphere.controller;

import com.shopsphere.dao.CategoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/categories")
public class AdminCategoriesServlet extends HttpServlet {
    private final CategoryDAO categories = new CategoryDAO();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("categories", categories.findAll()); request.setAttribute("notice", request.getSession().getAttribute("adminNotice"));
            request.getSession().removeAttribute("adminNotice"); request.getRequestDispatcher("/WEB-INF/views/admin/categories.jsp").forward(request, response);
        } catch (Exception exception) { request.setAttribute("error", "Categories could not be loaded."); request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response); }
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if ("archive".equals(request.getParameter("action"))) categories.archive(Long.parseLong(request.getParameter("categoryId")));
            else {
                long id = Long.parseLong(request.getParameter("categoryId") == null ? "0" : request.getParameter("categoryId"));
                String name = request.getParameter("name");
                if (name == null || name.isBlank() || name.length() > 100) throw new IllegalArgumentException("Category name is required.");
                categories.save(id, name.trim(), request.getParameter("description"));
            }
            request.getSession().setAttribute("adminNotice", "Category saved.");
        } catch (Exception exception) { request.getSession().setAttribute("adminNotice", exception instanceof IllegalArgumentException ? exception.getMessage() : "Category could not be saved."); }
        response.sendRedirect(request.getContextPath() + "/admin/categories");
    }
}
