package com.telecom.gui;

import com.telecom.dao.AlertDAO;
import com.telecom.model.Alert;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AlertMonitoringScreen extends JFrame {

    private JTable alertTable;
    private DefaultTableModel tableModel;

    private AlertDAO alertDAO;

    public AlertMonitoringScreen() {

        alertDAO = new AlertDAO();

        setTitle("Alert Monitoring");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createUI();

        loadAlerts();
    }

    private void createUI() {

        setLayout(new BorderLayout(10, 10));

        // =========================
        // TITLE
        // =========================

        JLabel titleLabel =
                new JLabel(
                        "Alert Monitoring",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        add(titleLabel, BorderLayout.NORTH);

        // =========================
        // TABLE
        // =========================

        tableModel = new DefaultTableModel(
                new String[]{
                        "Alert ID",
                        "Cell ID",
                        "Alert Type",
                        "Alert Message",
                        "Severity",
                        "Status",
                        "Created Time"
                },
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        alertTable = new JTable(tableModel);

        alertTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        alertTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        JScrollPane scrollPane =
                new JScrollPane(alertTable);

        add(scrollPane, BorderLayout.CENTER);

        // =========================
        // BUTTON PANEL
        // =========================

        JPanel buttonPanel =
                new JPanel(new FlowLayout());

        JButton btnRefresh =
                new JButton("Refresh");

        JButton btnClose =
                new JButton("Close");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClose);

        add(buttonPanel, BorderLayout.SOUTH);

        // =========================
        // BUTTON ACTIONS
        // =========================

        btnRefresh.addActionListener(
                e -> loadAlerts()
        );

        btnClose.addActionListener(
                e -> dispose()
        );
    }

    // =========================
    // LOAD ALERTS
    // =========================

    private void loadAlerts() {

        tableModel.setRowCount(0);

        List<Alert> alerts =
                alertDAO.getAllAlerts();

        for (Alert alert : alerts) {

            tableModel.addRow(
                    new Object[]{
                            alert.getAlertId(),
                            alert.getCellId(),
                            alert.getAlertType(),
                            alert.getAlertMessage(),
                            alert.getSeverity(),
                            alert.getStatus(),
                            alert.getCreatedTime()
                    }
            );
        }
    }

    // =========================
    // MAIN METHOD
    // =========================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            AlertMonitoringScreen screen =
                    new AlertMonitoringScreen();

            screen.setVisible(true);
        });
    }
}