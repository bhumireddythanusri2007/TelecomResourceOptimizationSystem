package com.telecom.app;

import com.telecom.dao.NetworkTrafficDAO;
import com.telecom.dao.ResourceAllocationDAO;
import com.telecom.dao.OptimizationResultDAO;

import com.telecom.model.NetworkTraffic;
import com.telecom.model.ResourceAllocation;
import com.telecom.model.OptimizationResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OptimizationScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public OptimizationScreen() {

        setTitle("Network Resource Optimization");

        setSize(1000, 550);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel(
                "Network Resource Optimization",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        String[] columns = {
                "Cell ID",
                "Previous Load (%)",
                "Optimized Load (%)",
                "Recommended Bandwidth",
                "Performance Score"
        };

        tableModel =
                new DefaultTableModel(columns, 0);

        table = new JTable(tableModel);

        table.setRowHeight(25);

        JScrollPane scrollPane =
                new JScrollPane(table);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        JPanel buttonPanel =
                new JPanel();

        JButton optimizeButton =
                new JButton("Run Optimization");

        JButton clearButton =
                new JButton("Clear");

        JButton closeButton =
                new JButton("Close");

        buttonPanel.add(optimizeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(closeButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        optimizeButton.addActionListener(e -> {
            runOptimization();
        });

        clearButton.addActionListener(e -> {
            tableModel.setRowCount(0);
        });

        closeButton.addActionListener(e -> {
            dispose();
        });

        add(mainPanel);
    }

    private void runOptimization() {

        tableModel.setRowCount(0);

        NetworkTrafficDAO trafficDAO =
                new NetworkTrafficDAO();

        ResourceAllocationDAO allocationDAO =
                new ResourceAllocationDAO();

        OptimizationResultDAO resultDAO =
                new OptimizationResultDAO();

        List<NetworkTraffic> trafficList =
                trafficDAO.getAllNetworkTraffic();

        List<ResourceAllocation> allocationList =
                allocationDAO.getAllResourceAllocations();

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

            OptimizationResult result =
                    new OptimizationResult();

            result.setCellId(cellId);

            result.setPreviousLoad(
                    currentLoad);

            result.setOptimizedLoad(
                    optimizedLoad);

            result.setAllocatedBandwidth(
                    recommendedBandwidth);

            result.setPerformanceScore(
                    performanceScore);

            boolean saved =
                    resultDAO.saveOptimizationResult(
                            result);

            if (saved) {

                Object[] row = {
                        cellId,
                        currentLoad,
                        optimizedLoad,
                        recommendedBandwidth,
                        performanceScore
                };

                tableModel.addRow(row);
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Optimization completed successfully!",
                "Optimization",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            OptimizationScreen screen =
                    new OptimizationScreen();

            screen.setVisible(true);
        });
    }
}