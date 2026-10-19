package com.telecom.app;

import com.telecom.dao.ReportDAO;

import javax.swing.*;
import java.awt.*;

public class ReportScreen extends JFrame {

    private JLabel totalCellsLabel;
    private JLabel totalTrafficLabel;
    private JLabel totalAllocationsLabel;
    private JLabel totalOptimizationLabel;
    private JLabel totalKPILabel;
    private JLabel totalAlertsLabel;
    private JLabel openAlertsLabel;
    private JLabel resolvedAlertsLabel;
    private JLabel averageTrafficLabel;
    private JLabel averageLatencyLabel;
    private JLabel averageAvailabilityLabel;

    public ReportScreen() {

        setTitle("Network Resource Optimization Report");

        setSize(700, 650);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel =
                new JLabel(
                        "Network Resource Optimization Report",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        // Report panel
        JPanel reportPanel =
                new JPanel(
                        new GridLayout(11, 2, 10, 10)
                );

        reportPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 30, 15, 30
                )
        );

        totalCellsLabel = new JLabel();
        totalTrafficLabel = new JLabel();
        totalAllocationsLabel = new JLabel();
        totalOptimizationLabel = new JLabel();
        totalKPILabel = new JLabel();
        totalAlertsLabel = new JLabel();
        openAlertsLabel = new JLabel();
        resolvedAlertsLabel = new JLabel();
        averageTrafficLabel = new JLabel();
        averageLatencyLabel = new JLabel();
        averageAvailabilityLabel = new JLabel();

        reportPanel.add(
                new JLabel("Total Network Cells:")
        );
        reportPanel.add(totalCellsLabel);

        reportPanel.add(
                new JLabel("Total Traffic Records:")
        );
        reportPanel.add(totalTrafficLabel);

        reportPanel.add(
                new JLabel("Total Resource Allocations:")
        );
        reportPanel.add(totalAllocationsLabel);

        reportPanel.add(
                new JLabel("Total Optimization Results:")
        );
        reportPanel.add(totalOptimizationLabel);

        reportPanel.add(
                new JLabel("Total KPI Records:")
        );
        reportPanel.add(totalKPILabel);

        reportPanel.add(
                new JLabel("Total Alerts:")
        );
        reportPanel.add(totalAlertsLabel);

        reportPanel.add(
                new JLabel("Open Alerts:")
        );
        reportPanel.add(openAlertsLabel);

        reportPanel.add(
                new JLabel("Resolved Alerts:")
        );
        reportPanel.add(resolvedAlertsLabel);

        reportPanel.add(
                new JLabel("Average Traffic Load (%):")
        );
        reportPanel.add(averageTrafficLabel);

        reportPanel.add(
                new JLabel("Average Latency:")
        );
        reportPanel.add(averageLatencyLabel);

        reportPanel.add(
                new JLabel("Average Availability (%):")
        );
        reportPanel.add(averageAvailabilityLabel);

        mainPanel.add(
                reportPanel,
                BorderLayout.CENTER
        );

        // Buttons
        JPanel buttonPanel =
                new JPanel();

        JButton refreshButton =
                new JButton("Refresh");

        JButton closeButton =
                new JButton("Close");

        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        refreshButton.addActionListener(e -> {
            loadReport();
        });

        closeButton.addActionListener(e -> {
            dispose();
        });

        add(mainPanel);

        loadReport();
    }

    private void loadReport() {

        ReportDAO dao =
                new ReportDAO();

        totalCellsLabel.setText(
                String.valueOf(
                        dao.getTotalNetworkCells()
                )
        );

        totalTrafficLabel.setText(
                String.valueOf(
                        dao.getTotalTrafficRecords()
                )
        );

        totalAllocationsLabel.setText(
                String.valueOf(
                        dao.getTotalResourceAllocations()
                )
        );

        totalOptimizationLabel.setText(
                String.valueOf(
                        dao.getTotalOptimizationResults()
                )
        );

        totalKPILabel.setText(
                String.valueOf(
                        dao.getTotalKPIRecords()
                )
        );

        totalAlertsLabel.setText(
                String.valueOf(
                        dao.getTotalAlerts()
                )
        );

        openAlertsLabel.setText(
                String.valueOf(
                        dao.getOpenAlerts()
                )
        );

        resolvedAlertsLabel.setText(
                String.valueOf(
                        dao.getResolvedAlerts()
                )
        );

        averageTrafficLabel.setText(
                String.format(
                        "%.2f",
                        dao.getAverageTrafficLoad()
                )
        );

        averageLatencyLabel.setText(
                String.format(
                        "%.2f",
                        dao.getAverageLatency()
                )
        );

        averageAvailabilityLabel.setText(
                String.format(
                        "%.2f",
                        dao.getAverageAvailability()
                )
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ReportScreen screen =
                    new ReportScreen();

            screen.setVisible(true);
        });
    }
}