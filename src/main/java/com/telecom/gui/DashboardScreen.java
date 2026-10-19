package com.telecom.gui;

import com.telecom.dao.AlertDAO;
import com.telecom.dao.DeviceDAO;
import com.telecom.dao.NetworkCellDAO;
import com.telecom.dao.NetworkTrafficDAO;
import com.telecom.dao.OptimizationResultDAO;
import com.telecom.dao.SubscriberDAO;

import javax.swing.*;
import java.awt.*;

public class DashboardScreen extends JFrame {

    private JLabel cellsLabel;
    private JLabel devicesLabel;
    private JLabel subscribersLabel;
    private JLabel trafficLabel;
    private JLabel alertsLabel;
    private JLabel optimizationLabel;

    public DashboardScreen() {

        setTitle("Telecom Resource Optimization System - Dashboard");
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createDashboardUI();
        loadDashboardData();

        setVisible(true);
    }

    private void createDashboardUI() {

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JLabel titleLabel = new JLabel(
                "Telecom Network Dashboard",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel =
                new JPanel(new BorderLayout(10, 10));

        // Dashboard statistics
        JPanel dataPanel =
                new JPanel(new GridLayout(3, 2, 15, 15));

        cellsLabel = createDataLabel("Network Cells");
        devicesLabel = createDataLabel("Devices");
        subscribersLabel = createDataLabel("Subscribers");
        trafficLabel = createDataLabel("Traffic Records");
        alertsLabel = createDataLabel("Alerts");
        optimizationLabel = createDataLabel("Optimization Results");

        dataPanel.add(cellsLabel);
        dataPanel.add(devicesLabel);
        dataPanel.add(subscribersLabel);
        dataPanel.add(trafficLabel);
        dataPanel.add(alertsLabel);
        dataPanel.add(optimizationLabel);

        centerPanel.add(dataPanel, BorderLayout.NORTH);

        // Management screen navigation
        JPanel managementPanel =
                new JPanel(new GridLayout(0, 3, 10, 10));

        managementPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Management Screens"
                )
        );

        addNavigationButton(
                managementPanel,
                "User Management",
                UserManagementScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Network Cell Management",
                NetworkCellManagementScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Device Management",
                DeviceManagementScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Subscriber Management",
                SubscriberManagementScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Network Slice Management",
                NetworkSliceManagementScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Edge Server Management",
                EdgeServerManagementScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Traffic Flow Management",
                TrafficFlowManagementScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Resource Allocation",
                ResourceAllocationManagementScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Network Traffic Management",
                NetworkTrafficManagementScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Optimization Results",
                OptimizationResultManagementScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Optimization Job Management",
                OptimizationJobManagementScreen.class
        );

        addNavigationButton(
                managementPanel,
                "KPI Monitoring",
                NetworkKPIMonitoringScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Alert Monitoring",
                AlertMonitoringScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Audit Log Management",
                AuditLogManagementScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Reports Management",
                ReportsManagementScreen.class
        );

        addNavigationButton(
                managementPanel,
                "Login History",
                LoginHistoryManagementScreen.class
        );

        centerPanel.add(managementPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Dashboard controls
        JPanel buttonPanel =
                new JPanel(new FlowLayout());

        JButton refreshButton = new JButton("Refresh");
        JButton closeButton = new JButton("Close");

        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(
                e -> loadDashboardData()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        add(mainPanel);
    }

    private void addNavigationButton(
            JPanel panel,
            String buttonText,
            Class<? extends JFrame> screenClass) {

        JButton button = new JButton(buttonText);

        button.addActionListener(e -> {
            try {
                JFrame screen = screenClass
                        .getDeclaredConstructor()
                        .newInstance();

                screen.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Unable to open " + buttonText +
                                ". Please check the screen class.",
                        "Navigation Error",
                        JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        });

        panel.add(button);
    }

    private JLabel createDataLabel(String title) {

        JLabel label = new JLabel(
                title + ": Loading...",
                SwingConstants.CENTER
        );

        label.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        label.setBorder(
                BorderFactory.createLineBorder(Color.GRAY)
        );

        return label;
    }

    private void loadDashboardData() {

        try {

            NetworkCellDAO cellDAO = new NetworkCellDAO();
            DeviceDAO deviceDAO = new DeviceDAO();
            SubscriberDAO subscriberDAO = new SubscriberDAO();
            NetworkTrafficDAO trafficDAO = new NetworkTrafficDAO();
            AlertDAO alertDAO = new AlertDAO();
            OptimizationResultDAO optimizationDAO =
                    new OptimizationResultDAO();

            cellsLabel.setText(
                    "Network Cells: " +
                            cellDAO.getAllNetworkCells().size()
            );

            devicesLabel.setText(
                    "Devices: " +
                            deviceDAO.getAllDevices().size()
            );

            subscribersLabel.setText(
                    "Subscribers: " +
                            subscriberDAO.getAllSubscribers().size()
            );

            trafficLabel.setText(
                    "Traffic Records: " +
                            trafficDAO.getAllNetworkTraffic().size()
            );

            alertsLabel.setText(
                    "Alerts: " +
                            alertDAO.getAllAlerts().size()
            );

            optimizationLabel.setText(
                    "Optimization Results: " +
                            optimizationDAO
                                    .getAllOptimizationResults()
                                    .size()
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading dashboard data.",
                    "Dashboard Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                DashboardScreen::new
        );
    }
}
