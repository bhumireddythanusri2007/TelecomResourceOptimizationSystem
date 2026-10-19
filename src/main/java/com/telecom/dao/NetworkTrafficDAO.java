package com.telecom.dao;

import com.telecom.model.NetworkTraffic;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NetworkTrafficDAO {

    // READ
    public List<NetworkTraffic> getAllNetworkTraffic() {

        List<NetworkTraffic> trafficList = new ArrayList<>();

        String sql = "SELECT * FROM network_traffic ORDER BY traffic_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                NetworkTraffic traffic = new NetworkTraffic();

                traffic.setTrafficId(
                        resultSet.getInt("traffic_id"));

                traffic.setCellId(
                        resultSet.getInt("cell_id"));

                traffic.setDataUsage(
                        resultSet.getDouble("data_usage"));

                traffic.setActiveUsers(
                        resultSet.getInt("active_users"));

                traffic.setTrafficLoad(
                        resultSet.getDouble("traffic_load"));

                traffic.setRecordedTime(
                        resultSet.getTimestamp("recorded_time"));

                trafficList.add(traffic);
            }

        } catch (Exception e) {
            System.out.println("Error retrieving network traffic.");
            e.printStackTrace();
        }

        return trafficList;
    }


    // CREATE
    public boolean addNetworkTraffic(NetworkTraffic traffic) {

        String sql =
                "INSERT INTO network_traffic " +
                "(cell_id, data_usage, active_users, traffic_load) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, traffic.getCellId());
            statement.setDouble(2, traffic.getDataUsage());
            statement.setInt(3, traffic.getActiveUsers());
            statement.setDouble(4, traffic.getTrafficLoad());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error adding network traffic.");
            e.printStackTrace();
            return false;
        }
    }


    // UPDATE
    public boolean updateNetworkTraffic(NetworkTraffic traffic) {

        String sql =
                "UPDATE network_traffic SET " +
                "cell_id = ?, " +
                "data_usage = ?, " +
                "active_users = ?, " +
                "traffic_load = ? " +
                "WHERE traffic_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, traffic.getCellId());
            statement.setDouble(2, traffic.getDataUsage());
            statement.setInt(3, traffic.getActiveUsers());
            statement.setDouble(4, traffic.getTrafficLoad());
            statement.setInt(5, traffic.getTrafficId());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error updating network traffic.");
            e.printStackTrace();
            return false;
        }
    }


    // DELETE
    public boolean deleteNetworkTraffic(int trafficId) {

        String sql =
                "DELETE FROM network_traffic " +
                "WHERE traffic_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, trafficId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error deleting network traffic.");
            e.printStackTrace();
            return false;
        }
    }
}