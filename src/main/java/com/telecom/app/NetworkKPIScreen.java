package com.telecom.app;

import com.telecom.dao.NetworkKPIDAO;
import com.telecom.model.NetworkKPI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NetworkKPIScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public NetworkKPIScreen() {

        setTitle("Network KPI Monitoring");

        setSize(1100, 550);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel =
                new JLabel(
                        "Network KPI Monitoring",
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
                "KPI ID",
                "Cell ID",
                "Throughput",
                "Latency",
                "Packet Loss",
                "Availability",
                "Signal Strength",
                "Recorded Time"
        };

        tableModel =
                new DefaultTableModel(columns, 0);

        table =
                new JTable(tableModel);

        table.setRowHeight(25);

        JScrollPane scrollPane =
                new JScrollPane(table);

        mainPanel.add(
                scrollPane,
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

        // Refresh button
        refreshButton.addActionListener(e -> {
            loadKPIs();
        });

        // Close button
        closeButton.addActionListener(e -> {
            dispose();
        });

        add(mainPanel);

        // Load KPI records when screen opens
        loadKPIs();
    }

    private void loadKPIs() {

        tableModel.setRowCount(0);

        NetworkKPIDAO dao =
                new NetworkKPIDAO();

        List<NetworkKPI> kpis =
                dao.getAllKPIs();

        for (NetworkKPI kpi : kpis) {

            Object[] row = {
                    kpi.getKpiId(),
                    kpi.getCellId(),
                    kpi.getThroughput(),
                    kpi.getLatency(),
                    kpi.getPacketLoss(),
                    kpi.getAvailability(),
                    kpi.getSignalStrength(),
                    kpi.getRecordedTime()
            };

            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            NetworkKPIScreen screen =
                    new NetworkKPIScreen();

            screen.setVisible(true);
        });
    }
}