package com.telecom.dao;

import com.telecom.model.NetworkSlice;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NetworkSliceDAO {

    public List<NetworkSlice> getAllNetworkSlices() {

        List<NetworkSlice> slices =
                new ArrayList<>();

        String sql =
                "SELECT * FROM network_slices " +
                "ORDER BY slice_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                NetworkSlice slice =
                        new NetworkSlice();

                slice.setSliceId(
                        resultSet.getInt("slice_id"));

                slice.setSliceName(
                        resultSet.getString("slice_name"));

                slice.setSliceType(
                        resultSet.getString("slice_type"));

                slice.setBandwidth(
                        resultSet.getInt("bandwidth"));

                slice.setLatencyRequirement(
                        resultSet.getDouble(
                                "latency_requirement"));

                slice.setConnectedUsers(
                        resultSet.getInt(
                                "connected_users"));

                slice.setStatus(
                        resultSet.getString("status"));

                slices.add(slice);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving network slices.");

            e.printStackTrace();
        }

        return slices;
    }

    public boolean addNetworkSlice(
            NetworkSlice slice) {

        String sql =
                "INSERT INTO network_slices " +
                "(slice_name, slice_type, bandwidth, " +
                "latency_requirement, connected_users, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    slice.getSliceName());

            statement.setString(
                    2,
                    slice.getSliceType());

            statement.setInt(
                    3,
                    slice.getBandwidth());

            statement.setDouble(
                    4,
                    slice.getLatencyRequirement());

            statement.setInt(
                    5,
                    slice.getConnectedUsers());

            statement.setString(
                    6,
                    slice.getStatus());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error adding network slice.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean updateNetworkSlice(
            NetworkSlice slice) {

        String sql =
                "UPDATE network_slices SET " +
                "slice_name = ?, " +
                "slice_type = ?, " +
                "bandwidth = ?, " +
                "latency_requirement = ?, " +
                "connected_users = ?, " +
                "status = ? " +
                "WHERE slice_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    slice.getSliceName());

            statement.setString(
                    2,
                    slice.getSliceType());

            statement.setInt(
                    3,
                    slice.getBandwidth());

            statement.setDouble(
                    4,
                    slice.getLatencyRequirement());

            statement.setInt(
                    5,
                    slice.getConnectedUsers());

            statement.setString(
                    6,
                    slice.getStatus());

            statement.setInt(
                    7,
                    slice.getSliceId());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating network slice.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean deleteNetworkSlice(
            int sliceId) {

        String sql =
                "DELETE FROM network_slices " +
                "WHERE slice_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    sliceId);

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error deleting network slice.");

            e.printStackTrace();

            return false;
        }
    }
}