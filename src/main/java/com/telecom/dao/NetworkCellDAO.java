package com.telecom.dao;

import com.telecom.model.NetworkCell;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NetworkCellDAO {

    // Get all network cells
    public List<NetworkCell> getAllNetworkCells() {

        List<NetworkCell> cells = new ArrayList<>();

        String sql = "SELECT * FROM network_cells ORDER BY cell_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                NetworkCell cell = new NetworkCell();

                cell.setCellId(
                        resultSet.getInt("cell_id"));

                cell.setCellName(
                        resultSet.getString("cell_name"));

                cell.setLocation(
                        resultSet.getString("location"));

                cell.setTotalCapacity(
                        resultSet.getInt("total_capacity"));

                cell.setCurrentUsers(
                        resultSet.getInt("current_users"));

                cell.setStatus(
                        resultSet.getString("status"));

                cells.add(cell);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving network cells.");

            e.printStackTrace();
        }

        return cells;
    }


    // Add a new network cell
    public boolean addNetworkCell(NetworkCell cell) {

        String sql =
                "INSERT INTO network_cells " +
                "(cell_name, location, total_capacity, " +
                "current_users, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    cell.getCellName());

            statement.setString(
                    2,
                    cell.getLocation());

            statement.setInt(
                    3,
                    cell.getTotalCapacity());

            statement.setInt(
                    4,
                    cell.getCurrentUsers());

            statement.setString(
                    5,
                    cell.getStatus());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error adding network cell.");

            e.printStackTrace();

            return false;
        }
    }


    // Update an existing network cell
    public boolean updateNetworkCell(NetworkCell cell) {

        String sql =
                "UPDATE network_cells SET " +
                "cell_name = ?, " +
                "location = ?, " +
                "total_capacity = ?, " +
                "current_users = ?, " +
                "status = ? " +
                "WHERE cell_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    cell.getCellName());

            statement.setString(
                    2,
                    cell.getLocation());

            statement.setInt(
                    3,
                    cell.getTotalCapacity());

            statement.setInt(
                    4,
                    cell.getCurrentUsers());

            statement.setString(
                    5,
                    cell.getStatus());

            statement.setInt(
                    6,
                    cell.getCellId());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error updating network cell.");

            e.printStackTrace();

            return false;
        }
    }


    // Delete a network cell
    public boolean deleteNetworkCell(int cellId) {

        String sql =
                "DELETE FROM network_cells " +
                "WHERE cell_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    cellId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error deleting network cell.");

            e.printStackTrace();

            return false;
        }
    }
}