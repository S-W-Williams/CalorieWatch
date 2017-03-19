package com.caloriewatch.servlets;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.caloriewatch.database.DatabaseInfo;
import com.caloriewatch.database.DatabaseQueries;

@WebServlet("/Register/Submit")
public class RegisterSubmitServlet extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        float lat = 0;
        float lng = 0;

        try {
            lat = Float.valueOf(request.getParameter("lat"));
            lng = Float.valueOf(request.getParameter("lng"));
        } catch (Exception e) {
            request.setAttribute("message", "Your location is invalid!");
            request.getRequestDispatcher("/Register").forward(request,response);
        }

        if (lat == 0 && lng == 0) {
            request.setAttribute("message", "You must have a valid location to use CalorieWatch.");
            request.getRequestDispatcher("/Register").forward(request,response);
        }

        InputStream input = getServletContext().getResourceAsStream("/WEB-INF/database_config.properties");
        DatabaseInfo databaseInfo = new DatabaseInfo(input);
        DatabaseQueries databaseQueries = new DatabaseQueries(databaseInfo);
        Boolean success = databaseQueries.createAccount(username, password, lat, lng);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/Home");
        } else {
            request.setAttribute("message", "Username is already taken!");
            request.getRequestDispatcher("/Register").forward(request,response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

}
