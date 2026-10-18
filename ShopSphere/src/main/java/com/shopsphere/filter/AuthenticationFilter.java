package com.shopsphere.filter;

import com.shopsphere.model.User;
import com.shopsphere.util.SessionKeys;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/cart", "/checkout", "/orders", "/profile", "/wishlist", "/review"})
public class AuthenticationFilter implements Filter {
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest http = (HttpServletRequest) request;
        HttpServletResponse out = (HttpServletResponse) response;
        HttpSession session = http.getSession(false);
        User user = session == null ? null : (User) session.getAttribute(SessionKeys.USER);
        if (user == null) {
            HttpSession loginSession = http.getSession(true);
            loginSession.setAttribute("returnTo", http.getRequestURI());
            out.sendRedirect(http.getContextPath() + "/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
