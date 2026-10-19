package com.telecom.service;

import com.telecom.dao.NetworkTrafficDAO;
import com.telecom.dao.ResourceAllocationDAO;
import com.telecom.dao.OptimizationResultDAO;

import com.telecom.model.NetworkTraffic;
import com.telecom.model.ResourceAllocation;
import com.telecom.model.OptimizationResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class ResourceOptimizationService {

    private NetworkTrafficDAO trafficDAO;
    private ResourceAllocationDAO allocationDAO;
    private OptimizationResultDAO optimizationResultDAO;

    public ResourceOptimizationService() {

        trafficDAO = new NetworkTrafficDAO();
        allocationDAO = new ResourceAllocationDAO();
        optimizationResultDAO = new OptimizationResultDAO();
    }

    public void performOptimization() {

        List<NetworkTraffic> trafficList =
                trafficDAO.getAllNetworkTraffic();

        List<ResourceAllocation> allocationList =
                allocationDAO.getAllResourceAllocations();

        System.out.println(
                "Starting network resource optimization...");

        System.out.println();

        String sql =
                "INSERT INTO optimization_results " +
                "(cell_id, previous_load, optimized_load, " +
                "allocated_bandwidth, performance_score) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection =
                     com.telecom.util.DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            for (NetworkTraffic traffic : trafficList) {

                int cellId =
                        traffic.getCellId();

                double currentLoad =
                        traffic.getTrafficLoad();

                int currentBandwidth = 0;

                for (ResourceAllocation allocation :
                        allocationList) {

                    if (allocation.getCellId() == cellId) {

                        currentBandwidth =
                                allocation.getBandwidth();

                        break;
                    }
                }

                double optimizedLoad;

                int recommendedBandwidth;

                double performanceScore;

                if (currentLoad >= 80) {

                    optimizedLoad =
                            currentLoad - 12;

                    recommendedBandwidth =
                            currentBandwidth + 20;

                } else if (currentLoad >= 70) {

                    optimizedLoad =
                            currentLoad - 8;

                    recommendedBandwidth =
                            currentBandwidth + 10;

                } else {

                    optimizedLoad =
                            currentLoad - 5;

                    recommendedBandwidth =
                            currentBandwidth;
                }

                if (optimizedLoad < 0) {
                    optimizedLoad = 0;
                }

                performanceScore =
                        100 - (optimizedLoad * 0.2);

                statement.setInt(
                        1,
                        cellId);

                statement.setDouble(
                        2,
                        currentLoad);

                statement.setDouble(
                        3,
                        optimizedLoad);

                statement.setInt(
                        4,
                        recommendedBandwidth);

                statement.setDouble(
                        5,
                        performanceScore);

                int rowsInserted =
                        statement.executeUpdate();

                System.out.println(
                        "Cell ID: " + cellId);

                System.out.println(
                        "Previous Load: "
                                + currentLoad + "%");

                System.out.println(
                        "Optimized Load: "
                                + optimizedLoad + "%");

                System.out.println(
                        "Recommended Bandwidth: "
                                + recommendedBandwidth);

                System.out.println(
                        "Performance Score: "
                                + performanceScore);

                if (rowsInserted > 0) {

                    System.out.println(
                            "Optimization result saved successfully.");

                } else {

                    System.out.println(
                            "Failed to save optimization result.");
                }

                System.out.println(
                        "-----------------------------------");
            }

            System.out.println();
            System.out.println(
                    "Total traffic records analyzed: "
                            + trafficList.size());

            System.out.println(
                    "Total resource allocations checked: "
                            + allocationList.size());

            System.out.println(
                    "Optimization process completed.");

        } catch (Exception e) {

            System.out.println(
                    "Error during optimization process.");

            e.printStackTrace();
        }
    }
}