package com.caloriewatch.servlets;

import com.caloriewatch.database.DatabaseInfo;
import com.caloriewatch.database.DatabaseQueries;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Login/Submit")
public class LoginSubmitServlet extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        InputStream input = getServletContext().getResourceAsStream("/WEB-INF/database_config.properties");
        DatabaseInfo databaseInfo = new DatabaseInfo(input);
        DatabaseQueries databaseQueries = new DatabaseQueries(databaseInfo);
        Boolean success = databaseQueries.validateLogin(username, password);

        if (success) {
            HttpSession session = request.getSession(true);
            session.setAttribute("authenticated", true);
            response.sendRedirect(request.getContextPath() + "/Home");
        } else {
            request.setAttribute("message", "Invalid credentials!");
            request.getRequestDispatcher("/Login").forward(request,response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
