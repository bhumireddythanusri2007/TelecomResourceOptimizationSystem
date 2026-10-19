package com.telecom.gui;

import com.telecom.dao.ReportDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportsManagementScreen extends JFrame {

    private JTable reportTable;
    private DefaultTableModel tableModel;
    private ReportDAO reportDAO;

    public ReportsManagementScreen() {

        reportDAO = new ReportDAO();

        setTitle("Reports Management");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createUI();
        loadReports();
    }

    private void createUI() {

        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel(
                "Network Resource Reports",
                SwingConstants.CENTER
        );

        heading.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        add(heading, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{"Report Name", "Value"},
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row, int column) {
                return false;
            }
        };

        reportTable = new JTable(tableModel);

        JScrollPane scrollPane =
                new JScrollPane(reportTable);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel =
                new JPanel(new FlowLayout());

        JButton btnRefresh = new JButton("Refresh");
        JButton btnClose = new JButton("Close");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClose);

        add(buttonPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(
                e -> loadReports()
        );

        btnClose.addActionListener(
                e -> dispose()
        );
    }

    private void loadReports() {

        tableModel.setRowCount(0);

        tableModel.addRow(new Object[]{
                "Total Network Cells",
                reportDAO.getTotalNetworkCells()
        });

        tableModel.addRow(new Object[]{
                "Total Traffic Records",
                reportDAO.getTotalTrafficRecords()
        });

        tableModel.addRow(new Object[]{
                "Total Resource Allocations",
                reportDAO.getTotalResourceAllocations()
        });

        tableModel.addRow(new Object[]{
                "Total Optimization Results",
                reportDAO.getTotalOptimizationResults()
        });

        tableModel.addRow(new Object[]{
                "Total KPI Records",
                reportDAO.getTotalKPIRecords()
        });

        tableModel.addRow(new Object[]{
                "Total Alerts",
                reportDAO.getTotalAlerts()
        });

        tableModel.addRow(new Object[]{
                "Open Alerts",
                reportDAO.getOpenAlerts()
        });

        tableModel.addRow(new Object[]{
                "Resolved Alerts",
                reportDAO.getResolvedAlerts()
        });

        tableModel.addRow(new Object[]{
                "Average Traffic Load",
                String.format("%.2f",
                        reportDAO.getAverageTrafficLoad())
        });

        tableModel.addRow(new Object[]{
                "Average Latency",
                String.format("%.2f",
                        reportDAO.getAverageLatency())
        });

        tableModel.addRow(new Object[]{
                "Average Availability",
                String.format("%.2f",
                        reportDAO.getAverageAvailability())
        });
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ReportsManagementScreen screen =
                    new ReportsManagementScreen();

            screen.setVisible(true);
        });
    }
}
