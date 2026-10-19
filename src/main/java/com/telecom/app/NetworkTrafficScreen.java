package com.telecom.app;

import com.telecom.dao.NetworkTrafficDAO;
import com.telecom.model.NetworkTraffic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NetworkTrafficScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public NetworkTrafficScreen() {

        setTitle("Network Traffic Monitoring");

        setSize(950, 500);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel(
                "Network Traffic Monitoring",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table columns
        String[] columns = {
                "Traffic ID",
                "Cell ID",
                "Data Usage",
                "Active Users",
                "Traffic Load (%)",
                "Recorded Time"
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
            loadNetworkTraffic();
        });

        // Close button
        closeButton.addActionListener(e -> {
            dispose();
        });

        add(mainPanel);

        // Load data when screen opens
        loadNetworkTraffic();
    }

    private void loadNetworkTraffic() {

        tableModel.setRowCount(0);

        NetworkTrafficDAO dao =
                new NetworkTrafficDAO();

        List<NetworkTraffic> trafficList =
                dao.getAllNetworkTraffic();

        for (NetworkTraffic traffic : trafficList) {

            Object[] row = {
                    traffic.getTrafficId(),
                    traffic.getCellId(),
                    traffic.getDataUsage(),
                    traffic.getActiveUsers(),
                    traffic.getTrafficLoad(),
                    traffic.getRecordedTime()
            };

            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            NetworkTrafficScreen screen =
                    new NetworkTrafficScreen();

            screen.setVisible(true);
        });
    }
}