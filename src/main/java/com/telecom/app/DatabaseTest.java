package com.telecom.app;

import com.telecom.util.DatabaseConnection;
import java.sql.Connection;

public class DatabaseTest {

    public static void main(String[] args) {

        Connection connection = DatabaseConnection.getConnection();

        if (connection != null) {
            System.out.println("Java is successfully connected to MySQL!");

            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}