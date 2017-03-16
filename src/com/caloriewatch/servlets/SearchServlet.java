package com.caloriewatch.servlets;

import com.caloriewatch.yelpapi.YelpAPI;
import com.caloriewatch.yelpapi.YelpAPIRequest;
import com.caloriewatch.yelpapi.Restaurant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bumbl on 3/15/2017.
 */

@WebServlet("/Search")
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(true);
            if (session.getAttribute("authenticated") == null) {
                response.sendRedirect(request.getContextPath() + "/Login");
                return;
            }
            else {
                try {
                    String query = request.getParameter("query");
                    String location = request.getParameter("location");

                    // To pass previous query to next jsp page
                    request.setAttribute("query", query);
                    request.setAttribute("location", location);

                    YelpAPI yelpAPI = new YelpAPI();
                    YelpAPIRequest yelpReq = new YelpAPIRequest(query, location); //DEBUG
                    ArrayList<Restaurant> restaurantList = yelpAPI.queryAPIForBusinessList(yelpReq);
                    request.setAttribute("restaurantResults", restaurantList);

                } catch (Exception e) {
                    request.setAttribute("error", "Error in Yelp Req: " + e.toString());
                }

                request.getRequestDispatcher("/WEB-INF/search.jsp").forward(request,response);
                return;
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
