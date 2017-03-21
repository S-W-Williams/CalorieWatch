package com.caloriewatch.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;

import com.caloriewatch.Entry;
import com.caloriewatch.Restaurant;
import com.caloriewatch.yelpapi.YelpAPI;
import com.caloriewatch.yelpapi.YelpAPIRequest;
import org.mindrot.jbcrypt.BCrypt;

public class DatabaseQueries {
    DatabaseInfo databaseInfo;

    public DatabaseQueries(DatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }

    public boolean createAccount(String username, String password, float lat, float lng) {
        Connection conn;
        PreparedStatement stmt;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(databaseInfo.DB_URL, databaseInfo.DB_USERNAME, databaseInfo.DB_PASSWORD);
            stmt = conn.prepareStatement("select * from users where username = ?");
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                //If user with username already exists, return false
                return false;
            }

            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
            stmt = conn.prepareStatement("insert into users (username, password, lat, lng) values (?, ?, ?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, hashed);
            stmt.setFloat(3, lat);
            stmt.setFloat( 4, lng);
            stmt.executeUpdate();

            conn.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean validateLogin(String username, String password) {
        Connection conn;
        PreparedStatement stmt;

        try {
            Class.forName(databaseInfo.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(databaseInfo.DB_URL, databaseInfo.DB_USERNAME, databaseInfo.DB_PASSWORD);
            stmt = conn.prepareStatement("select * from users where username = ?");
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                String hashed = rs.getString("password");
                if (BCrypt.checkpw(password, hashed)) {
                    return true;
                } else {
                    return false;
                }
            }

            conn.close();
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public float getHealthScore(String restaurant) {
        Connection conn;
        PreparedStatement stmt;

        try {
            Class.forName(databaseInfo.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(databaseInfo.DB_URL, databaseInfo.DB_USERNAME, databaseInfo.DB_PASSWORD);
            stmt = conn.prepareStatement("select * from healthScores where title = ?");
            stmt.setString(1, restaurant);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return rs.getFloat("score");
            }
            conn.close();
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    public ArrayList<Entry> getEntries(String restaurant) {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        Connection conn;
        PreparedStatement stmt;

        try {
            Class.forName(databaseInfo.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(databaseInfo.DB_URL, databaseInfo.DB_USERNAME, databaseInfo.DB_PASSWORD);
            stmt = conn.prepareStatement("select * from menuItems where title = ?");
            stmt.setString(1, restaurant);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String item = rs.getString("item");
                int calories = rs.getInt("calories");
                entries.add(new Entry(item, calories));
            }
            conn.close();
        } catch (Exception e) {
            return entries;
        }
        return entries;
    }

    public ArrayList<String> getCategories(String restaurant) {
        ArrayList<String> categories = new ArrayList<String>();
        Connection conn;
        PreparedStatement stmt;

        try {
            Class.forName(databaseInfo.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(databaseInfo.DB_URL, databaseInfo.DB_USERNAME, databaseInfo.DB_PASSWORD);
            stmt = conn.prepareStatement("select * from categories where title = ?");
            stmt.setString(1, restaurant);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String category = rs.getString("category");
                categories.add(category);
            }
            conn.close();
        } catch (Exception e) {
            return categories;
        }

        return categories;
    }

    public ArrayList<Restaurant> searchCategories(String query, float lat, float lng) {
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        Connection conn;
        PreparedStatement stmt;

        try {
            Class.forName(databaseInfo.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(databaseInfo.DB_URL, databaseInfo.DB_USERNAME, databaseInfo.DB_PASSWORD);
            //stmt = conn.prepareStatement("select distinct * from categories where category like ?");
            stmt = conn.prepareStatement("SELECT DISTINCT(c.title), score FROM categories c JOIN healthScores h WHERE c.title = h.title AND c.category LIKE ? AND NOT score = 0 GROUP BY (score) LIMIT 50");
            stmt.setString(1, "%" + query + "%");

            ResultSet rs = stmt.executeQuery();

            int success = 0;
            LinkedList<String> notAvailable = new LinkedList<String>();
            while (rs.next()) {
                String restaurantName = rs.getString("title");
                if (success == 10) {
                    break;
                }

                com.caloriewatch.yelpapi.Restaurant yelpRestaurant = checkAvailability(restaurantName, lat, lng);
                if (yelpRestaurant != null) {
                    success += 1;
                    float healthScore = getHealthScore(restaurantName);
                    Restaurant restaurant = new Restaurant(restaurantName.trim(), healthScore);
                    restaurant.categories = getCategories(restaurantName);
                    restaurant.entries = getEntries(restaurantName);
                    restaurant.available = true;
                    restaurant.copyFromYelp(yelpRestaurant);
                    restaurants.add(restaurant);
                }
                else {
                    notAvailable.add(restaurantName);
                }

            }
            //Fill in empty slots
            for (int i = restaurants.size(); i < 11; i++) {
                String restaurantName = notAvailable.removeFirst();
                float healthScore = getHealthScore(restaurantName);
                Restaurant restaurant = new Restaurant(restaurantName.trim(), healthScore);
                restaurant.categories = getCategories(restaurantName);
                restaurant.entries = getEntries(restaurantName);
                restaurants.add(restaurant);
            }
            conn.close();
        } catch (Exception e) {
            return restaurants;
        }
        return restaurants;
    }

    public com.caloriewatch.yelpapi.Restaurant checkAvailability(String restaurantName, float lat, float lng) {
        YelpAPI yelpAPI = new YelpAPI();
        YelpAPIRequest yelpReq = new YelpAPIRequest(restaurantName); //DEBUG
        yelpReq.setLocation(lat, lng);
        String yelpName = "";
        com.caloriewatch.yelpapi.Restaurant restaurant = null;
        try {
            restaurant = yelpAPI.queryAPIForBusiness(yelpReq);
            yelpName = restaurant.getName();
        } catch (Exception e) {
            return null;
        }
        yelpName = yelpName.replaceAll("[^a-zA-Z0-9]", "");
        restaurantName = restaurantName.replaceAll("[^a-zA-Z0-9]", "");
        if (restaurantName.equalsIgnoreCase(yelpName)) {
            return restaurant;
        }
        else {
            return null;
        }
    }
}
