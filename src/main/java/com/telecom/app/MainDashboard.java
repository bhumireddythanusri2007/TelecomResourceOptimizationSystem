package com.telecom.app;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    public MainDashboard() {

        setTitle("Telecom Resource Optimization System");

        setSize(800, 550);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel(
                "Telecom Resource Optimization System",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();

       buttonPanel.setLayout(
        new GridLayout(6, 2, 15, 15)
);
        // Buttons
        JButton userManagementButton =
        new JButton("User Management");

JButton deviceManagementButton =
        new JButton("Device Management");

JButton networkSliceButton =
        new JButton("Network Slice Management");        

JButton edgeComputingButton =
        new JButton("Edge Computing Management");

edgeComputingButton.addActionListener(e -> {
    EdgeComputingManagementScreen screen =
            new EdgeComputingManagementScreen();
    screen.setVisible(true);
});


JButton trafficFlowButton =
        new JButton("Traffic Flow Management");

trafficFlowButton.addActionListener(e -> {
    TrafficFlowManagementScreen screen =
            new TrafficFlowManagementScreen();
    screen.setVisible(true);
});

JButton optimizationJobButton =
        new JButton("Optimization Job Management");

optimizationJobButton.addActionListener(e -> {
    OptimizationJobManagementScreen screen =
            new OptimizationJobManagementScreen();
    screen.setVisible(true);
});

JButton auditLogButton =
        new JButton("Audit Log Management");

auditLogButton.addActionListener(e -> {
    AuditLogManagementScreen screen =
            new AuditLogManagementScreen();
    screen.setVisible(true);
});

JButton subscriberManagementButton =
        new JButton("Subscriber Management");

subscriberManagementButton.addActionListener(e -> {
    SubscriberManagementScreen screen =
            new SubscriberManagementScreen();
    screen.setVisible(true);
});

JButton networkCellsButton =
        new JButton("Network Cells");

        JButton resourceAllocationButton =
                new JButton("Resource Allocation");

        JButton trafficMonitoringButton =
                new JButton("Traffic Monitoring");

        JButton optimizationButton =
                new JButton("Optimization");

        JButton optimizationResultsButton =
        new JButton("Optimization Results");

JButton kpiMonitoringButton =
        new JButton("KPI Monitoring");

JButton alertsButton =
        new JButton("Alerts");

JButton reportsButton =
        new JButton("Reports");

JButton exitButton =
        new JButton("Exit");

        // Add buttons
       buttonPanel.add(userManagementButton);
buttonPanel.add(deviceManagementButton);
buttonPanel.add(networkSliceButton);
buttonPanel.add(edgeComputingButton);
buttonPanel.add(subscriberManagementButton);
buttonPanel.add(trafficFlowButton);
buttonPanel.add(optimizationJobButton);
buttonPanel.add(auditLogButton);
buttonPanel.add(networkCellsButton);
        buttonPanel.add(resourceAllocationButton);
        buttonPanel.add(trafficMonitoringButton);
        buttonPanel.add(optimizationButton);
buttonPanel.add(optimizationResultsButton);
buttonPanel.add(kpiMonitoringButton);
buttonPanel.add(alertsButton);
buttonPanel.add(reportsButton);
buttonPanel.add(exitButton);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Button actions

        userManagementButton.addActionListener(e -> {

            UserManagementScreen screen =
                    new UserManagementScreen();

            screen.setVisible(true);
        });

        deviceManagementButton.addActionListener(e -> {
    DeviceManagementScreen screen =
            new DeviceManagementScreen();
    screen.setVisible(true);
});

networkSliceButton.addActionListener(e -> {
    NetworkSliceManagementScreen screen =
            new NetworkSliceManagementScreen();
    screen.setVisible(true);
});

edgeComputingButton.addActionListener(e -> {
    EdgeComputingManagementScreen screen =
            new EdgeComputingManagementScreen();
    screen.setVisible(true);
});

        networkCellsButton.addActionListener(e -> {

            NetworkCellScreen screen =
                    new NetworkCellScreen();

            screen.setVisible(true);
        });

        resourceAllocationButton.addActionListener(e -> {

            ResourceAllocationScreen screen =
                    new ResourceAllocationScreen();

            screen.setVisible(true);
        });

        trafficMonitoringButton.addActionListener(e -> {

            NetworkTrafficScreen screen =
                    new NetworkTrafficScreen();

            screen.setVisible(true);
        });

        optimizationButton.addActionListener(e -> {

            OptimizationScreen screen =
                    new OptimizationScreen();

            screen.setVisible(true);
        });

        optimizationResultsButton.addActionListener(e -> {

            OptimizationResultsScreen screen =
                    new OptimizationResultsScreen();

            screen.setVisible(true);
        });

        kpiMonitoringButton.addActionListener(e -> {

    NetworkKPIScreen screen =
            new NetworkKPIScreen();

    screen.setVisible(true);
});

alertsButton.addActionListener(e -> {

    AlertScreen screen =
            new AlertScreen();

    screen.setVisible(true);
});

reportsButton.addActionListener(e -> {
    ReportScreen screen =
            new ReportScreen();
    screen.setVisible(true);
});

        exitButton.addActionListener(e -> {

            System.exit(0);
        });

        // Add main panel to frame
        add(mainPanel);
    }

   public static void main(String[] args) {

    SwingUtilities.invokeLater(() -> {

        LoginScreen loginScreen =
                new LoginScreen();

        loginScreen.setVisible(true);
    });
}
}