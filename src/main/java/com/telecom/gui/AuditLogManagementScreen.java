package com.telecom.gui;

import com.telecom.dao.AuditLogDAO;
import com.telecom.model.AuditLog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AuditLogManagementScreen extends JFrame {

    private JTable auditTable;
    private DefaultTableModel tableModel;
    private AuditLogDAO auditLogDAO;

    public AuditLogManagementScreen() {

        auditLogDAO = new AuditLogDAO();

        setTitle("Audit Log Management");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createUI();
        loadAuditLogs();
    }

    private void createUI() {

        setLayout(new BorderLayout(10, 10));

        JLabel heading =
                new JLabel("Audit Log Management",
                        SwingConstants.CENTER);

        heading.setFont(new Font("Arial", Font.BOLD, 22));

        add(heading, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{
                        "Audit ID",
                        "User ID",
                        "Action Type",
                        "Action Description",
                        "Table Name",
                        "Record ID",
                        "Action Time"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row, int column) {
                return false;
            }
        };

        auditTable = new JTable(tableModel);
        auditTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane =
                new JScrollPane(auditTable);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel =
                new JPanel(new FlowLayout());

        JButton btnRefresh = new JButton("Refresh");
        JButton btnClose = new JButton("Close");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClose);

        add(buttonPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(
                e -> loadAuditLogs());

        btnClose.addActionListener(
                e -> dispose());
    }

    private void loadAuditLogs() {

        tableModel.setRowCount(0);

        List<AuditLog> logs =
                auditLogDAO.getAllAuditLogs();

        for (AuditLog log : logs) {

            tableModel.addRow(
                    new Object[]{
                            log.getAuditId(),
                            log.getUserId(),
                            log.getActionType(),
                            log.getActionDescription(),
                            log.getTableName(),
                            log.getRecordId(),
                            log.getActionTime()
                    }
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            AuditLogManagementScreen screen =
                    new AuditLogManagementScreen();

            screen.setVisible(true);
        });
    }
}
