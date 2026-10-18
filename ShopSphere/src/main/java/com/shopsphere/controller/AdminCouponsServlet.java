package com.shopsphere.controller;

import com.shopsphere.dao.CouponDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;

@WebServlet("/admin/coupons")
public class AdminCouponsServlet extends HttpServlet {
    private final CouponDAO coupons = new CouponDAO();
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("coupons", coupons.findAll()); request.setAttribute("notice", request.getSession().getAttribute("adminNotice"));
            request.getSession().removeAttribute("adminNotice"); request.getRequestDispatcher("/WEB-INF/views/admin/coupons.jsp").forward(request, response);
        } catch (Exception exception) { request.setAttribute("error", "Coupons could not be loaded."); request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response); }
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if ("archive".equals(request.getParameter("action"))) coupons.archive(Long.parseLong(request.getParameter("couponId")));
            else {
                String code = request.getParameter("code"); String type = request.getParameter("type");
                BigDecimal value = new BigDecimal(request.getParameter("value"));
                BigDecimal minimum = new BigDecimal(request.getParameter("minimumOrder"));
                String maximumRaw = request.getParameter("maximumDiscount");
                BigDecimal maximum = maximumRaw == null || maximumRaw.isBlank() ? null : new BigDecimal(maximumRaw);
                String expiryRaw = request.getParameter("expiryDate"); Date expiry = expiryRaw == null || expiryRaw.isBlank() ? null : Date.valueOf(expiryRaw);
                if (code == null || !code.matches("[A-Za-z0-9_-]{3,40}") || !("PERCENT".equals(type) || "FIXED".equals(type)) || value.signum() <= 0 || minimum.signum() < 0 || (maximum != null && maximum.signum() < 0))
                    throw new IllegalArgumentException("Enter a valid code and discount.");
                coupons.create(code, type, value, minimum, maximum, expiry);
            }
            request.getSession().setAttribute("adminNotice", "Coupon saved.");
        } catch (Exception exception) { request.getSession().setAttribute("adminNotice", exception instanceof IllegalArgumentException ? exception.getMessage() : "Coupon could not be saved."); }
        response.sendRedirect(request.getContextPath() + "/admin/coupons");
    }
}
