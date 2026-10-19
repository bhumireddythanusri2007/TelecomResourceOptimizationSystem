package com.telecom.dao;

import com.telecom.model.OptimizationResult;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OptimizationResultDAO {

    // READ
    public List<OptimizationResult> getAllOptimizationResults() {

        List<OptimizationResult> results = new ArrayList<>();

        String sql =
                "SELECT * FROM optimization_results " +
                "ORDER BY result_id ASC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                OptimizationResult result =
                        new OptimizationResult();

                result.setResultId(
                        resultSet.getInt("result_id"));

                result.setCellId(
                        resultSet.getInt("cell_id"));

                result.setPreviousLoad(
                        resultSet.getDouble("previous_load"));

                result.setOptimizedLoad(
                        resultSet.getDouble("optimized_load"));

                result.setAllocatedBandwidth(
                        resultSet.getInt("allocated_bandwidth"));

                result.setPerformanceScore(
                        resultSet.getDouble("performance_score"));

                result.setOptimizationTime(
                        resultSet.getTimestamp("optimization_time"));

                results.add(result);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving optimization results.");

            e.printStackTrace();
        }

        return results;
    }


    // CREATE
    public boolean saveOptimizationResult(
            OptimizationResult result) {

        String sql =
                "INSERT INTO optimization_results " +
                "(cell_id, previous_load, optimized_load, " +
                "allocated_bandwidth, performance_score) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1, result.getCellId());

            statement.setDouble(
                    2, result.getPreviousLoad());

            statement.setDouble(
                    3, result.getOptimizedLoad());

            statement.setInt(
                    4, result.getAllocatedBandwidth());

            statement.setDouble(
                    5, result.getPerformanceScore());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error saving optimization result.");

            e.printStackTrace();

            return false;
        }
    }


    // UPDATE
    public boolean updateOptimizationResult(
            OptimizationResult result) {

        String sql =
                "UPDATE optimization_results SET " +
                "cell_id = ?, " +
                "previous_load = ?, " +
                "optimized_load = ?, " +
                "allocated_bandwidth = ?, " +
                "performance_score = ? " +
                "WHERE result_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1, result.getCellId());

            statement.setDouble(
                    2, result.getPreviousLoad());

            statement.setDouble(
                    3, result.getOptimizedLoad());

            statement.setInt(
                    4, result.getAllocatedBandwidth());

            statement.setDouble(
                    5, result.getPerformanceScore());

            statement.setInt(
                    6, result.getResultId());

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error updating optimization result.");

            e.printStackTrace();

            return false;
        }
    }


    // DELETE
    public boolean deleteOptimizationResult(
            int resultId) {

        String sql =
                "DELETE FROM optimization_results " +
                "WHERE result_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, resultId);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error deleting optimization result.");

            e.printStackTrace();

            return false;
        }
    }
}