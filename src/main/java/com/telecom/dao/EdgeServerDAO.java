package com.telecom.dao;

import com.telecom.model.EdgeServer;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EdgeServerDAO {

    public List<EdgeServer> getAllEdgeServers() {

        List<EdgeServer> servers = new ArrayList<>();

        String sql =
                "SELECT * FROM edge_servers " +
                "ORDER BY edge_server_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                EdgeServer server = new EdgeServer();

                server.setEdgeServerId(
                        resultSet.getInt("edge_server_id"));

                server.setServerName(
                        resultSet.getString("server_name"));

                server.setLocation(
                        resultSet.getString("location"));

                server.setCpuCapacity(
                        resultSet.getInt("cpu_capacity"));

                server.setMemoryCapacity(
                        resultSet.getInt("memory_capacity"));

                server.setStorageCapacity(
                        resultSet.getInt("storage_capacity"));

                server.setConnectedUsers(
                        resultSet.getInt("connected_users"));

                server.setStatus(
                        resultSet.getString("status"));

                servers.add(server);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving edge servers.");

            e.printStackTrace();
        }

        return servers;
    }

    public boolean addEdgeServer(EdgeServer server) {

        String sql =
                "INSERT INTO edge_servers " +
                "(server_name, location, cpu_capacity, " +
                "memory_capacity, storage_capacity, " +
                "connected_users, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1, server.getServerName());

            statement.setString(
                    2, server.getLocation());

            statement.setInt(
                    3, server.getCpuCapacity());

            statement.setInt(
                    4, server.getMemoryCapacity());

            statement.setInt(
                    5, server.getStorageCapacity());

            statement.setInt(
                    6, server.getConnectedUsers());

            statement.setString(
                    7, server.getStatus());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error adding edge server.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean updateEdgeServer(EdgeServer server) {

        String sql =
                "UPDATE edge_servers SET " +
                "server_name = ?, " +
                "location = ?, " +
                "cpu_capacity = ?, " +
                "memory_capacity = ?, " +
                "storage_capacity = ?, " +
                "connected_users = ?, " +
                "status = ? " +
                "WHERE edge_server_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1, server.getServerName());

            statement.setString(
                    2, server.getLocation());

            statement.setInt(
                    3, server.getCpuCapacity());

            statement.setInt(
                    4, server.getMemoryCapacity());

            statement.setInt(
                    5, server.getStorageCapacity());

            statement.setInt(
                    6, server.getConnectedUsers());

            statement.setString(
                    7, server.getStatus());

            statement.setInt(
                    8, server.getEdgeServerId());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating edge server.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean deleteEdgeServer(int edgeServerId) {

        String sql =
                "DELETE FROM edge_servers " +
                "WHERE edge_server_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, edgeServerId);

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error deleting edge server.");

            e.printStackTrace();

            return false;
        }
    }
}