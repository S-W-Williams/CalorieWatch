package com.caloriewatch.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
}
