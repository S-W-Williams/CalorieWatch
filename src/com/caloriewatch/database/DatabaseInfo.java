package com.caloriewatch.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseInfo {
    public String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public String DB_URL;
    public String DB_USERNAME;
    public String DB_PASSWORD;

    public DatabaseInfo(InputStream input) {
        Properties properties = new Properties();
        try {
            properties.load(input);
            DB_URL = properties.getProperty("jdbc.url");
            DB_USERNAME = properties.getProperty("jdbc.username");
            DB_PASSWORD = properties.getProperty("jdbc.password");
        } catch (IOException e) {

        }
    }

}