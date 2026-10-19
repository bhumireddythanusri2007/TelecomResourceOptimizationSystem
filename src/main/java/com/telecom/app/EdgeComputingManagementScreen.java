package com.telecom.app;

import com.telecom.dao.EdgeServerDAO;
import com.telecom.model.EdgeServer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EdgeComputingManagementScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField serverNameField;
    private JTextField locationField;
    private JTextField cpuField;
    private JTextField memoryField;
    private JTextField storageField;
    private JTextField usersField;

    private JComboBox<String> statusBox;

    private EdgeServerDAO edgeServerDAO;

    public EdgeComputingManagementScreen() {

        edgeServerDAO = new EdgeServerDAO();

        setTitle("Edge Computing Management");
        setSize(1050, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createInterface();
        loadEdgeServers();
    }

    private void createInterface() {

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(
                new GridLayout(7, 2, 8, 8));

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Edge Server Details"));

        serverNameField = new JTextField();
        locationField = new JTextField();
        cpuField = new JTextField();
        memoryField = new JTextField();
        storageField = new JTextField();
        usersField = new JTextField();

        statusBox = new JComboBox<>(
                new String[]{
                        "Active",
                        "Inactive",
                        "Maintenance"
                });

        formPanel.add(new JLabel("Server Name:"));
        formPanel.add(serverNameField);

        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationField);

        formPanel.add(new JLabel("CPU Capacity:"));
        formPanel.add(cpuField);

        formPanel.add(new JLabel("Memory Capacity:"));
        formPanel.add(memoryField);

        formPanel.add(new JLabel("Storage Capacity:"));
        formPanel.add(storageField);

        formPanel.add(new JLabel("Connected Users:"));
        formPanel.add(usersField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusBox);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Server Name",
                        "Location",
                        "CPU",
                        "Memory",
                        "Storage",
                        "Users",
                        "Status"
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

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(
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

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e ->
                addEdgeServer());

        updateButton.addActionListener(e ->
                updateEdgeServer());

        deleteButton.addActionListener(e ->
                deleteEdgeServer());

        refreshButton.addActionListener(e ->
                loadEdgeServers());
        
        closeButton.addActionListener(e ->
        dispose());

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        loadSelectedServer();
                    }
                });
    }

    private void loadEdgeServers() {

        tableModel.setRowCount(0);

        List<EdgeServer> servers =
                edgeServerDAO.getAllEdgeServers();

        for (EdgeServer server : servers) {

            tableModel.addRow(new Object[]{
                    server.getEdgeServerId(),
                    server.getServerName(),
                    server.getLocation(),
                    server.getCpuCapacity(),
                    server.getMemoryCapacity(),
                    server.getStorageCapacity(),
                    server.getConnectedUsers(),
                    server.getStatus()
            });
        }
    }

    private void loadSelectedServer() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        serverNameField.setText(
                tableModel.getValueAt(
                        selectedRow, 1).toString());

        locationField.setText(
                tableModel.getValueAt(
                        selectedRow, 2).toString());

        cpuField.setText(
                tableModel.getValueAt(
                        selectedRow, 3).toString());

        memoryField.setText(
                tableModel.getValueAt(
                        selectedRow, 4).toString());

        storageField.setText(
                tableModel.getValueAt(
                        selectedRow, 5).toString());

        usersField.setText(
                tableModel.getValueAt(
                        selectedRow, 6).toString());

        statusBox.setSelectedItem(
                tableModel.getValueAt(
                        selectedRow, 7).toString());
    }

    private EdgeServer getServerFromForm(
            int serverId) {

        return new EdgeServer(
                serverId,
                serverNameField.getText().trim(),
                locationField.getText().trim(),
                Integer.parseInt(
                        cpuField.getText().trim()),
                Integer.parseInt(
                        memoryField.getText().trim()),
                Integer.parseInt(
                        storageField.getText().trim()),
                Integer.parseInt(
                        usersField.getText().trim()),
                statusBox.getSelectedItem().toString()
        );
    }

    private void addEdgeServer() {

        try {

            EdgeServer server =
                    getServerFromForm(0);

            if (edgeServerDAO.addEdgeServer(server)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Edge server added successfully.");

                clearFields();
                loadEdgeServers();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add edge server.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "CPU, memory, storage and users " +
                    "must contain valid numbers.");
        }
    }

    private void updateEdgeServer() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an edge server.");

            return;
        }

        try {

            int serverId =
                    Integer.parseInt(
                            tableModel.getValueAt(
                                    selectedRow, 0)
                                    .toString());

            EdgeServer server =
                    getServerFromForm(serverId);

            if (edgeServerDAO.updateEdgeServer(server)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Edge server updated successfully.");

                clearFields();
                loadEdgeServers();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update edge server.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "CPU, memory, storage and users " +
                    "must contain valid numbers.");
        }
    }

    private void deleteEdgeServer() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an edge server.");

            return;
        }

        int serverId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                selectedRow, 0)
                                .toString());

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete selected edge server?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        if (edgeServerDAO.deleteEdgeServer(serverId)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Edge server deleted successfully.");

            clearFields();
            loadEdgeServers();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete edge server.");
        }
    }

    private void clearFields() {

        serverNameField.setText("");
        locationField.setText("");
        cpuField.setText("");
        memoryField.setText("");
        storageField.setText("");
        usersField.setText("");

        statusBox.setSelectedIndex(0);

        table.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            EdgeComputingManagementScreen screen =
                    new EdgeComputingManagementScreen();

            screen.setVisible(true);
        });
    }
}