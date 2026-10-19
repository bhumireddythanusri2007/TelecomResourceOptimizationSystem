package com.telecom.dao;

import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportDAO {

    public int getTotalNetworkCells() {

        return getCount(
                "SELECT COUNT(*) FROM network_cells"
        );
    }

    public int getTotalTrafficRecords() {

        return getCount(
                "SELECT COUNT(*) FROM network_traffic"
        );
    }

    public int getTotalResourceAllocations() {

        return getCount(
                "SELECT COUNT(*) FROM resource_allocation"
        );
    }

    public int getTotalOptimizationResults() {

        return getCount(
                "SELECT COUNT(*) FROM optimization_results"
        );
    }

    public int getTotalKPIRecords() {

        return getCount(
                "SELECT COUNT(*) FROM network_kpis"
        );
    }

    public int getTotalAlerts() {

        return getCount(
                "SELECT COUNT(*) FROM alerts"
        );
    }

    public int getOpenAlerts() {

        return getCount(
                "SELECT COUNT(*) FROM alerts " +
                "WHERE status = 'Open'"
        );
    }

    public int getResolvedAlerts() {

        return getCount(
                "SELECT COUNT(*) FROM alerts " +
                "WHERE status = 'Resolved'"
        );
    }

    public double getAverageTrafficLoad() {

        return getAverage(
                "SELECT AVG(traffic_load) " +
                "FROM network_traffic"
        );
    }

    public double getAverageLatency() {

        return getAverage(
                "SELECT AVG(latency) " +
                "FROM network_kpis"
        );
    }

    public double getAverageAvailability() {

        return getAverage(
                "SELECT AVG(availability) " +
                "FROM network_kpis"
        );
    }

    private int getCount(String sql) {

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {

                return resultSet.getInt(1);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving report count."
            );

            e.printStackTrace();
        }

        return 0;
    }

    private double getAverage(String sql) {

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {

                return resultSet.getDouble(1);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving report average."
            );

            e.printStackTrace();
        }

        return 0.0;
    }
}