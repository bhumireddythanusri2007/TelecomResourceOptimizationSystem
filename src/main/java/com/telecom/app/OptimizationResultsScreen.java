package com.telecom.app;

import com.telecom.dao.OptimizationResultDAO;
import com.telecom.model.OptimizationResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OptimizationResultsScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public OptimizationResultsScreen() {

        setTitle("Optimization Results");

        setSize(1000, 550);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel(
                "Network Resource Optimization Results",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        // Table columns
        String[] columns = {
                "Result ID",
                "Cell ID",
                "Previous Load (%)",
                "Optimized Load (%)",
                "Allocated Bandwidth",
                "Performance Score",
                "Optimization Time"
        };

        tableModel = new DefaultTableModel(columns, 0);

        table = new JTable(tableModel);

        table.setRowHeight(25);

        JScrollPane scrollPane =
                new JScrollPane(table);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // Bottom panel
        JPanel bottomPanel = new JPanel();

        JButton refreshButton =
                new JButton("Refresh");

        JButton closeButton =
                new JButton("Close");

        bottomPanel.add(refreshButton);
        bottomPanel.add(closeButton);

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH
        );

        // Refresh button
        refreshButton.addActionListener(e -> {
            loadOptimizationResults();
        });

        // Close button
        closeButton.addActionListener(e -> {
            dispose();
        });

        add(mainPanel);

        // Load results when screen opens
        loadOptimizationResults();
    }

    private void loadOptimizationResults() {

        tableModel.setRowCount(0);

        OptimizationResultDAO dao =
                new OptimizationResultDAO();

        List<OptimizationResult> results =
                dao.getAllOptimizationResults();

        for (OptimizationResult result : results) {

            Object[] row = {
                    result.getResultId(),
                    result.getCellId(),
                    result.getPreviousLoad(),
                    result.getOptimizedLoad(),
                    result.getAllocatedBandwidth(),
                    result.getPerformanceScore(),
                    result.getOptimizationTime()
            };

            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            OptimizationResultsScreen screen =
                    new OptimizationResultsScreen();

            screen.setVisible(true);
        });
    }
}