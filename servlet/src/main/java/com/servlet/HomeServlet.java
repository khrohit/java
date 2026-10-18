package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        HttpSession session = request.getSession(false);

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<body>");

        out.println("<h1>Welcome to Home Page</h1>");

        if (session != null) {
            String username = (String) session.getAttribute("username");
            out.println("<h2>Hello, " + username + "!</h2>");
        }

        out.println("<p>You are successfully logged in.</p>");

        out.println("</body>");
        out.println("</html>");
    }
}