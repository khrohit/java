package com.shopsphere.controller;

import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.User;
import com.shopsphere.util.SessionKeys;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {
    private final ReviewDAO reviews = new ReviewDAO();
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute(SessionKeys.USER);
        String productId = request.getParameter("productId");
        try {
            long id = Long.parseLong(productId); int rating = Integer.parseInt(request.getParameter("rating"));
            String text = request.getParameter("reviewText");
            if (rating < 1 || rating > 5 || text == null || text.length() > 1000) throw new IllegalArgumentException("Choose a rating and keep your review under 1000 characters.");
            reviews.add(user.getUserId(), id, rating, text.trim());
            request.getSession().setAttribute("reviewNotice", "Thanks for sharing your review.");
        } catch (Exception exception) { request.getSession().setAttribute("reviewNotice", exception instanceof IllegalArgumentException ? exception.getMessage() : "Your review could not be saved."); }
        response.sendRedirect(request.getContextPath() + "/product?id=" + productId);
    }
}
