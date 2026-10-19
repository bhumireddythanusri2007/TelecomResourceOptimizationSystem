package com.telecom.dao;

import com.telecom.model.OptimizationJob;
import com.telecom.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OptimizationJobDAO {

    public List<OptimizationJob> getAllOptimizationJobs() {

        List<OptimizationJob> jobs =
                new ArrayList<>();

        String sql =
                "SELECT * FROM optimization_jobs " +
                "ORDER BY job_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                OptimizationJob job =
                        new OptimizationJob();

                job.setJobId(
                        resultSet.getInt("job_id"));

                job.setJobName(
                        resultSet.getString("job_name"));

                job.setOptimizationType(
                        resultSet.getString(
                                "optimization_type"));

                job.setTargetCellId(
                        resultSet.getInt(
                                "target_cell_id"));

                job.setObjective(
                        resultSet.getString("objective"));

                job.setInputLoad(
                        resultSet.getDouble("input_load"));

                job.setStatus(
                        resultSet.getString("status"));

                job.setCreatedTime(
                        resultSet.getTimestamp(
                                "created_time")
                                .toString());

                jobs.add(job);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error retrieving optimization jobs.");

            e.printStackTrace();
        }

        return jobs;
    }

    public boolean addOptimizationJob(
            OptimizationJob job) {

        String sql =
                "INSERT INTO optimization_jobs " +
                "(job_name, optimization_type, " +
                "target_cell_id, objective, input_load, " +
                "status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1, job.getJobName());

            statement.setString(
                    2, job.getOptimizationType());

            statement.setInt(
                    3, job.getTargetCellId());

            statement.setString(
                    4, job.getObjective());

            statement.setDouble(
                    5, job.getInputLoad());

            statement.setString(
                    6, job.getStatus());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error adding optimization job.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean updateOptimizationJob(
            OptimizationJob job) {

        String sql =
                "UPDATE optimization_jobs SET " +
                "job_name = ?, " +
                "optimization_type = ?, " +
                "target_cell_id = ?, " +
                "objective = ?, " +
                "input_load = ?, " +
                "status = ? " +
                "WHERE job_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1, job.getJobName());

            statement.setString(
                    2, job.getOptimizationType());

            statement.setInt(
                    3, job.getTargetCellId());

            statement.setString(
                    4, job.getObjective());

            statement.setDouble(
                    5, job.getInputLoad());

            statement.setString(
                    6, job.getStatus());

            statement.setInt(
                    7, job.getJobId());

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating optimization job.");

            e.printStackTrace();

            return false;
        }
    }

    public boolean deleteOptimizationJob(
            int jobId) {

        String sql =
                "DELETE FROM optimization_jobs " +
                "WHERE job_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, jobId);

            statement.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error deleting optimization job.");

            e.printStackTrace();

            return false;
        }
    }
}