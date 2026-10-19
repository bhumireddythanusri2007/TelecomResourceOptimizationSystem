package com.telecom.app;

import com.telecom.dao.AlertDAO;
import com.telecom.model.Alert;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AlertScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public AlertScreen() {

        setTitle("Network Alerts Monitoring");

        setSize(1100, 550);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel =
                new JLabel(
                        "Network Alerts Monitoring",
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
                "Alert ID",
                "Cell ID",
                "Alert Type",
                "Alert Message",
                "Severity",
                "Status",
                "Created Time"
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
            loadAlerts();
        });

        // Close button
        closeButton.addActionListener(e -> {
            dispose();
        });

        add(mainPanel);

        // Load alerts when screen opens
        loadAlerts();
    }

    private void loadAlerts() {

        tableModel.setRowCount(0);

        AlertDAO dao =
                new AlertDAO();

        List<Alert> alerts =
                dao.getAllAlerts();

        for (Alert alert : alerts) {

            Object[] row = {
                    alert.getAlertId(),
                    alert.getCellId(),
                    alert.getAlertType(),
                    alert.getAlertMessage(),
                    alert.getSeverity(),
                    alert.getStatus(),
                    alert.getCreatedTime()
            };

            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            AlertScreen screen =
                    new AlertScreen();

            screen.setVisible(true);
        });
    }
}