package com.telecom.gui;

import com.telecom.dao.NetworkKPIDAO;
import com.telecom.model.NetworkKPI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NetworkKPIMonitoringScreen extends JFrame {

    private JTable kpiTable;
    private DefaultTableModel tableModel;

    private NetworkKPIDAO kpiDAO;

    public NetworkKPIMonitoringScreen() {

        kpiDAO = new NetworkKPIDAO();

        setTitle("Network KPI Monitoring");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createUI();

        loadKPIs();
    }

    private void createUI() {

        setLayout(new BorderLayout(10, 10));

        // =========================
        // TITLE
        // =========================

        JLabel titleLabel =
                new JLabel(
                        "Network KPI Monitoring",
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
                        "KPI ID",
                        "Cell ID",
                        "Throughput",
                        "Latency",
                        "Packet Loss",
                        "Availability",
                        "Signal Strength",
                        "Recorded Time"
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

        kpiTable = new JTable(tableModel);

        kpiTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        kpiTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        JScrollPane scrollPane =
                new JScrollPane(kpiTable);

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
                e -> loadKPIs()
        );

        btnClose.addActionListener(
                e -> dispose()
        );
    }

    // =========================
    // LOAD KPIs
    // =========================

    private void loadKPIs() {

        tableModel.setRowCount(0);

        List<NetworkKPI> kpis =
                kpiDAO.getAllKPIs();

        for (NetworkKPI kpi : kpis) {

            tableModel.addRow(
                    new Object[]{
                            kpi.getKpiId(),
                            kpi.getCellId(),
                            kpi.getThroughput(),
                            kpi.getLatency(),
                            kpi.getPacketLoss(),
                            kpi.getAvailability(),
                            kpi.getSignalStrength(),
                            kpi.getRecordedTime()
                    }
            );
        }
    }

    // =========================
    // MAIN METHOD
    // =========================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            NetworkKPIMonitoringScreen screen =
                    new NetworkKPIMonitoringScreen();

            screen.setVisible(true);
        });
    }
}