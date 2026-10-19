package com.telecom.app;

import com.telecom.dao.AuditLogDAO;
import com.telecom.model.AuditLog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AuditLogManagementScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField userIdField;
    private JTextField actionDescriptionField;
    private JTextField tableNameField;
    private JTextField recordIdField;

    private JComboBox<String> actionTypeBox;

    private AuditLogDAO auditLogDAO;

    public AuditLogManagementScreen() {

        auditLogDAO = new AuditLogDAO();

        setTitle("Audit Log Management");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE);

        createInterface();
        loadAuditLogs();
    }

    private void createInterface() {

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel =
                new JPanel(
                        new GridLayout(5, 2, 8, 8));

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Audit Log Details"));

        userIdField = new JTextField();
        actionDescriptionField = new JTextField();
        tableNameField = new JTextField();
        recordIdField = new JTextField();

        actionTypeBox =
                new JComboBox<>(
                        new String[]{
                                "LOGIN",
                                "INSERT",
                                "UPDATE",
                                "DELETE"
                        });

        formPanel.add(
                new JLabel("User ID:"));
        formPanel.add(userIdField);

        formPanel.add(
                new JLabel("Action Type:"));
        formPanel.add(actionTypeBox);

        formPanel.add(
                new JLabel("Action Description:"));
        formPanel.add(actionDescriptionField);

        formPanel.add(
                new JLabel("Table Name:"));
        formPanel.add(tableNameField);

        formPanel.add(
                new JLabel("Record ID:"));
        formPanel.add(recordIdField);

        add(
                formPanel,
                BorderLayout.NORTH);

        tableModel =
                new DefaultTableModel(
                        new String[]{
                                "Audit ID",
                                "User ID",
                                "Action Type",
                                "Action Description",
                                "Table Name",
                                "Record ID",
                                "Action Time"
                        }, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        table = new JTable(tableModel);

        JScrollPane scrollPane =
                new JScrollPane(table);

        add(
                scrollPane,
                BorderLayout.CENTER);

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout());

        JButton addButton =
                new JButton("Add");

        JButton updateButton =
                new JButton("Update");

        JButton deleteButton =
                new JButton("Delete");

        JButton refreshButton =
                new JButton("Refresh");

        JButton closeButton =
                new JButton("Close");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        add(
                buttonPanel,
                BorderLayout.SOUTH);

        addButton.addActionListener(
                e -> addAuditLog());

        updateButton.addActionListener(
                e -> updateAuditLog());

        deleteButton.addActionListener(
                e -> deleteAuditLog());

        refreshButton.addActionListener(
                e -> loadAuditLogs());

        closeButton.addActionListener(
                e -> dispose());

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        loadSelectedAuditLog();
                    }
                });
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
                    });
        }
    }

    private void loadSelectedAuditLog() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        userIdField.setText(
                tableModel.getValueAt(
                        selectedRow, 1)
                        .toString());

        actionTypeBox.setSelectedItem(
                tableModel.getValueAt(
                        selectedRow, 2)
                        .toString());

        actionDescriptionField.setText(
                tableModel.getValueAt(
                        selectedRow, 3)
                        .toString());

        tableNameField.setText(
                tableModel.getValueAt(
                        selectedRow, 4)
                        .toString());

        recordIdField.setText(
                tableModel.getValueAt(
                        selectedRow, 5)
                        .toString());
    }

    private AuditLog getAuditLogFromForm(
            int auditId) {

        return new AuditLog(
                auditId,
                Integer.parseInt(
                        userIdField.getText().trim()),
                actionTypeBox
                        .getSelectedItem()
                        .toString(),
                actionDescriptionField
                        .getText()
                        .trim(),
                tableNameField
                        .getText()
                        .trim(),
                Integer.parseInt(
                        recordIdField
                                .getText()
                                .trim()),
                ""
        );
    }

    private void addAuditLog() {

        try {

            AuditLog log =
                    getAuditLogFromForm(0);

            if (auditLogDAO.addAuditLog(log)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Audit log added successfully.");

                clearFields();
                loadAuditLogs();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add audit log.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "User ID and Record ID " +
                    "must contain valid numbers.");
        }
    }

    private void updateAuditLog() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an audit log.");

            return;
        }

        try {

            int auditId =
                    Integer.parseInt(
                            tableModel.getValueAt(
                                    selectedRow, 0)
                                    .toString());

            AuditLog log =
                    getAuditLogFromForm(auditId);

            if (auditLogDAO.updateAuditLog(log)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Audit log updated successfully.");

                clearFields();
                loadAuditLogs();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update audit log.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "User ID and Record ID " +
                    "must contain valid numbers.");
        }
    }

    private void deleteAuditLog() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an audit log.");

            return;
        }

        int auditId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                selectedRow, 0)
                                .toString());

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete selected audit log?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        if (auditLogDAO.deleteAuditLog(auditId)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Audit log deleted successfully.");

            clearFields();
            loadAuditLogs();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete audit log.");
        }
    }

    private void clearFields() {

        userIdField.setText("");
        actionDescriptionField.setText("");
        tableNameField.setText("");
        recordIdField.setText("");

        actionTypeBox.setSelectedIndex(0);

        table.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            AuditLogManagementScreen screen =
                    new AuditLogManagementScreen();

            screen.setVisible(true);
        });
    }
}