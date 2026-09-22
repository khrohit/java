package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        PrintWriter out = response.getWriter();

        if ("admin".equals(username) && "1234".equals(password)) {

            HttpSession session = request.getSession();

            session.setAttribute("username", username);

            out.println("<h1>Login Successful</h1>");
            out.println("<a href='home'>Go to Home</a>");

        } else {

            out.println("<h1>Invalid Login</h1>");
        }
    }
}