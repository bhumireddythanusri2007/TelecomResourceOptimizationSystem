package com.telecom.dao;

import com.telecom.model.ResourceAllocation;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ResourceAllocationDAO {

    // Get all resource allocations
    public List<ResourceAllocation> getAllResourceAllocations() {

        List<ResourceAllocation> allocations =
                new ArrayList<>();

        String sql =
                "SELECT * FROM resource_allocation " +
                "ORDER BY allocation_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                ResourceAllocation allocation =
                        new ResourceAllocation();

                allocation.setAllocationId(
                        resultSet.getInt("allocation_id"));

                allocation.setCellId(
                        resultSet.getInt("cell_id"));

                allocation.setBandwidth(
                        resultSet.getInt("bandwidth"));

                allocation.setFrequency(
                        resultSet.getInt("frequency"));

                allocation.setAllocatedUsers(
                        resultSet.getInt("allocated_users"));

                allocations.add(allocation);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving resource allocations.");

            e.printStackTrace();
        }

        return allocations;
    }


    // Add a new resource allocation
    public boolean addResourceAllocation(
            ResourceAllocation allocation) {

        String sql =
                "INSERT INTO resource_allocation " +
                "(cell_id, bandwidth, frequency, allocated_users) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    allocation.getCellId());

            statement.setInt(
                    2,
                    allocation.getBandwidth());

            statement.setInt(
                    3,
                    allocation.getFrequency());

            statement.setInt(
                    4,
                    allocation.getAllocatedUsers());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error adding resource allocation.");

            e.printStackTrace();

            return false;
        }
    }


    // Update an existing resource allocation
    public boolean updateResourceAllocation(
            ResourceAllocation allocation) {

        String sql =
                "UPDATE resource_allocation SET " +
                "cell_id = ?, " +
                "bandwidth = ?, " +
                "frequency = ?, " +
                "allocated_users = ? " +
                "WHERE allocation_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    allocation.getCellId());

            statement.setInt(
                    2,
                    allocation.getBandwidth());

            statement.setInt(
                    3,
                    allocation.getFrequency());

            statement.setInt(
                    4,
                    allocation.getAllocatedUsers());

            statement.setInt(
                    5,
                    allocation.getAllocationId());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error updating resource allocation.");

            e.printStackTrace();

            return false;
        }
    }


    // Delete a resource allocation
    public boolean deleteResourceAllocation(
            int allocationId) {

        String sql =
                "DELETE FROM resource_allocation " +
                "WHERE allocation_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    allocationId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error deleting resource allocation.");

            e.printStackTrace();

            return false;
        }
    }
}