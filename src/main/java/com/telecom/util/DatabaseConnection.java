
package com.telecom.util;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String URL =
            System.getenv().getOrDefault(
                    "DB_URL",
                    "jdbc:mysql://localhost:3306/telecom_optimization"
            );

    private static final String USER =
            System.getenv().getOrDefault("DB_USER", "telecom_app");

    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    static {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    try {
                        AbandonedConnectionCleanupThread.checkedShutdown();
                        System.out.println("MySQL cleanup completed.");
                    } catch (Exception e) {
                        System.out.println("MySQL cleanup error.");
                        e.printStackTrace();
                    }
                })
        );
    }

    public static Connection getConnection() {
        try {
            if (PASSWORD == null || PASSWORD.isBlank()) {
                throw new IllegalStateException(
                        "DB_PASSWORD environment variable is not configured."
                );
            }

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Database connected successfully!");

            return connection;

        } catch (Exception e) {
            System.out.println("Database connection failed!");
            System.out.println("JDBC Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
