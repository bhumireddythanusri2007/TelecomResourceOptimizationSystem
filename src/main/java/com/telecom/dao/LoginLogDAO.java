package com.telecom.dao;

import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class LoginLogDAO {

    // Record a login attempt
    public boolean addLoginLog(
            Integer userId,
            String loginStatus,
            String ipAddress) {

        String sql =
                "INSERT INTO login_log " +
                "(user_id, login_status, ip_address) " +
                "VALUES (?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            if (userId == null) {
                statement.setNull(1, Types.INTEGER);
            } else {
                statement.setInt(1, userId);
            }

            statement.setString(2, loginStatus);
            statement.setString(3, ipAddress);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error recording login log."
            );

            e.printStackTrace();

            return false;
        }
    }

    // Retrieve all login history records
    public List<Object[]> getAllLoginLogs() {

        List<Object[]> logs = new ArrayList<>();

        String sql =
                "SELECT login_id, user_id, login_time, " +
                "login_status, ip_address " +
                "FROM login_log " +
                "ORDER BY login_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                logs.add(new Object[]{
                        resultSet.getInt("login_id"),
                        resultSet.getObject("user_id"),
                        resultSet.getTimestamp("login_time"),
                        resultSet.getString("login_status"),
                        resultSet.getString("ip_address")
                });
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving login history."
            );

            e.printStackTrace();
        }

        return logs;
    }
}
