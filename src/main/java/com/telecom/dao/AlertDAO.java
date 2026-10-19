package com.telecom.dao;

import com.telecom.model.Alert;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AlertDAO {

    public List<Alert> getAllAlerts() {

        List<Alert> alerts =
                new ArrayList<>();

        String sql =
                "SELECT * FROM alerts " +
                "ORDER BY alert_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Alert alert =
                        new Alert();

                alert.setAlertId(
                        resultSet.getInt("alert_id"));

                alert.setCellId(
                        resultSet.getInt("cell_id"));

                alert.setAlertType(
                        resultSet.getString("alert_type"));

                alert.setAlertMessage(
                        resultSet.getString("alert_message"));

                alert.setSeverity(
                        resultSet.getString("severity"));

                alert.setStatus(
                        resultSet.getString("status"));

                alert.setCreatedTime(
                        resultSet.getTimestamp("created_time"));

                alerts.add(alert);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving alerts.");

            e.printStackTrace();
        }

        return alerts;
    }
}