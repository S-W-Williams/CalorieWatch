package com.caloriewatch.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.caloriewatch.Entry;
import com.caloriewatch.Restaurant;
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
                String category = rs.getString("title");
                categories.add(category);
            }
        } catch (Exception e) {
            return categories;
        }

        return categories;
    }

    public ArrayList<Restaurant> searchCategories(String query) {
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        Connection conn;
        PreparedStatement stmt;

        try {
            Class.forName(databaseInfo.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(databaseInfo.DB_URL, databaseInfo.DB_USERNAME, databaseInfo.DB_PASSWORD);
            stmt = conn.prepareStatement("select distinct * from categories where category like ?");
            stmt.setString(1, "%" + query + "%");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String restaurantName = rs.getString("title");
                float healthScore = getHealthScore(restaurantName);
                Restaurant restaurant = new Restaurant(restaurantName, healthScore);
                restaurant.categories = getCategories(restaurantName);
                restaurant.entries = getEntries(restaurantName);
                restaurants.add(restaurant);
            }

        } catch (Exception e) {
            return restaurants;
        }

        return restaurants;
    }
}
