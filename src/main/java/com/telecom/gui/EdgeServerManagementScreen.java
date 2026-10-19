package com.telecom.gui;

import com.telecom.dao.EdgeServerDAO;
import com.telecom.model.EdgeServer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EdgeServerManagementScreen extends JFrame {

    private JTextField edgeServerIdField;
    private JTextField serverNameField;
    private JTextField locationField;
    private JTextField cpuCapacityField;
    private JTextField memoryCapacityField;
    private JTextField storageCapacityField;
    private JTextField connectedUsersField;

    private JComboBox<String> statusComboBox;

    private JTable serverTable;
    private DefaultTableModel tableModel;

    private EdgeServerDAO edgeServerDAO;

    public EdgeServerManagementScreen() {

        edgeServerDAO = new EdgeServerDAO();

        setTitle("Edge Computing Management");
        setSize(1050, 680);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        loadEdgeServers();

        setVisible(true);
    }

    private void createUI() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JLabel titleLabel =
                new JLabel(
                        "Edge Computing Management",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        JPanel formPanel =
                new JPanel(
                        new GridLayout(8, 2, 10, 10)
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        edgeServerIdField = new JTextField();
        serverNameField = new JTextField();
        locationField = new JTextField();
        cpuCapacityField = new JTextField();
        memoryCapacityField = new JTextField();
        storageCapacityField = new JTextField();
        connectedUsersField = new JTextField();

        statusComboBox =
                new JComboBox<>(
                        new String[]{
                                "Active",
                                "Inactive",
                                "Maintenance"
                        }
                );

        edgeServerIdField.setEditable(false);

        formPanel.add(
                new JLabel("Edge Server ID:")
        );

        formPanel.add(
                edgeServerIdField
        );

        formPanel.add(
                new JLabel("Server Name:")
        );

        formPanel.add(
                serverNameField
        );

        formPanel.add(
                new JLabel("Location:")
        );

        formPanel.add(
                locationField
        );

        formPanel.add(
                new JLabel("CPU Capacity:")
        );

        formPanel.add(
                cpuCapacityField
        );

        formPanel.add(
                new JLabel("Memory Capacity:")
        );

        formPanel.add(
                memoryCapacityField
        );

        formPanel.add(
                new JLabel("Storage Capacity:")
        );

        formPanel.add(
                storageCapacityField
        );

        formPanel.add(
                new JLabel("Connected Users:")
        );

        formPanel.add(
                connectedUsersField
        );

        formPanel.add(
                new JLabel("Status:")
        );

        formPanel.add(
                statusComboBox
        );

        mainPanel.add(
                formPanel,
                BorderLayout.NORTH
        );

        String[] columnNames = {
                "Edge Server ID",
                "Server Name",
                "Location",
                "CPU Capacity",
                "Memory Capacity",
                "Storage Capacity",
                "Connected Users",
                "Status"
        };

        tableModel =
                new DefaultTableModel(
                        columnNames,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        serverTable =
                new JTable(tableModel);

        serverTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(serverTable);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        JPanel buttonPanel =
                new JPanel();

        JButton addButton =
                new JButton("Add");

        JButton updateButton =
                new JButton("Update");

        JButton deleteButton =
                new JButton("Delete");

        JButton refreshButton =
                new JButton("Refresh");

        JButton clearButton =
                new JButton("Clear");

        JButton closeButton =
                new JButton("Close");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(closeButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        addButton.addActionListener(
                e -> addEdgeServer()
        );

        updateButton.addActionListener(
                e -> updateEdgeServer()
        );

        deleteButton.addActionListener(
                e -> deleteEdgeServer()
        );

        refreshButton.addActionListener(
                e -> loadEdgeServers()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        serverTable.getSelectionModel()
                .addListSelectionListener(
                        e -> loadSelectedServer()
                );

        add(mainPanel);
    }

    private void loadEdgeServers() {

        tableModel.setRowCount(0);

        try {

            for (
                    EdgeServer server :
                    edgeServerDAO.getAllEdgeServers()
            ) {

                tableModel.addRow(
                        new Object[]{
                                server.getEdgeServerId(),
                                server.getServerName(),
                                server.getLocation(),
                                server.getCpuCapacity(),
                                server.getMemoryCapacity(),
                                server.getStorageCapacity(),
                                server.getConnectedUsers(),
                                server.getStatus()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading edge servers.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void loadSelectedServer() {

        int selectedRow =
                serverTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        edgeServerIdField.setText(
                tableModel
                        .getValueAt(selectedRow, 0)
                        .toString()
        );

        serverNameField.setText(
                tableModel
                        .getValueAt(selectedRow, 1)
                        .toString()
        );

        locationField.setText(
                tableModel
                        .getValueAt(selectedRow, 2)
                        .toString()
        );

        cpuCapacityField.setText(
                tableModel
                        .getValueAt(selectedRow, 3)
                        .toString()
        );

        memoryCapacityField.setText(
                tableModel
                        .getValueAt(selectedRow, 4)
                        .toString()
        );

        storageCapacityField.setText(
                tableModel
                        .getValueAt(selectedRow, 5)
                        .toString()
        );

        connectedUsersField.setText(
                tableModel
                        .getValueAt(selectedRow, 6)
                        .toString()
        );

        statusComboBox.setSelectedItem(
                tableModel
                        .getValueAt(selectedRow, 7)
                        .toString()
        );
    }

    private EdgeServer getServerFromFields(
            boolean includeId) {

        try {

            EdgeServer server =
                    new EdgeServer();

            if (includeId) {

                if (edgeServerIdField
                        .getText()
                        .trim()
                        .isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please select an edge server.",
                            "Validation",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return null;
                }

                server.setEdgeServerId(
                        Integer.parseInt(
                                edgeServerIdField
                                        .getText()
                                        .trim()
                        )
                );
            }

            String serverName =
                    serverNameField
                            .getText()
                            .trim();

            String location =
                    locationField
                            .getText()
                            .trim();

            String cpuText =
                    cpuCapacityField
                            .getText()
                            .trim();

            String memoryText =
                    memoryCapacityField
                            .getText()
                            .trim();

            String storageText =
                    storageCapacityField
                            .getText()
                            .trim();

            String usersText =
                    connectedUsersField
                            .getText()
                            .trim();

            if (serverName.isEmpty()
                    || location.isEmpty()
                    || cpuText.isEmpty()
                    || memoryText.isEmpty()
                    || storageText.isEmpty()
                    || usersText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            int cpuCapacity =
                    Integer.parseInt(cpuText);

            int memoryCapacity =
                    Integer.parseInt(memoryText);

            int storageCapacity =
                    Integer.parseInt(storageText);

            int connectedUsers =
                    Integer.parseInt(usersText);

            if (cpuCapacity <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "CPU capacity must be greater than 0.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            if (memoryCapacity <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Memory capacity must be greater than 0.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            if (storageCapacity <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Storage capacity must be greater than 0.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            if (connectedUsers < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Connected users cannot be negative.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            server.setServerName(
                    serverName
            );

            server.setLocation(
                    location
            );

            server.setCpuCapacity(
                    cpuCapacity
            );

            server.setMemoryCapacity(
                    memoryCapacity
            );

            server.setStorageCapacity(
                    storageCapacity
            );

            server.setConnectedUsers(
                    connectedUsers
            );

            server.setStatus(
                    statusComboBox
                            .getSelectedItem()
                            .toString()
            );

            return server;

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "CPU, memory, storage, and connected users must be whole numbers.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }
    }

    private void addEdgeServer() {

        EdgeServer server =
                getServerFromFields(false);

        if (server == null) {
            return;
        }

        if (edgeServerDAO.addEdgeServer(server)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Edge server added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadEdgeServers();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add edge server.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateEdgeServer() {

        EdgeServer server =
                getServerFromFields(true);

        if (server == null) {
            return;
        }

        if (edgeServerDAO.updateEdgeServer(server)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Edge server updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadEdgeServers();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update edge server.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteEdgeServer() {

        if (edgeServerIdField
                .getText()
                .trim()
                .isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an edge server to delete.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int edgeServerId;

        try {

            edgeServerId =
                    Integer.parseInt(
                            edgeServerIdField
                                    .getText()
                                    .trim()
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Edge Server ID.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete Edge Server ID "
                                + edgeServerId
                                + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation !=
                JOptionPane.YES_OPTION) {

            return;
        }

        if (edgeServerDAO.deleteEdgeServer(
                edgeServerId)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Edge server deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadEdgeServers();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete edge server.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFields() {

        edgeServerIdField.setText("");

        serverNameField.setText("");

        locationField.setText("");

        cpuCapacityField.setText("");

        memoryCapacityField.setText("");

        storageCapacityField.setText("");

        connectedUsersField.setText("");

        statusComboBox.setSelectedIndex(0);

        serverTable.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                EdgeServerManagementScreen::new
        );
    }
}