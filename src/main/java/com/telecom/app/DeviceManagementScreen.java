package com.telecom.app;

import com.telecom.dao.DeviceDAO;
import com.telecom.model.Device;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DeviceManagementScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField userIdField;
    private JTextField deviceNameField;
    private JTextField deviceTypeField;
    private JTextField ipAddressField;
    private JTextField connectionTypeField;
    private JTextField statusField;

    private int selectedDeviceId = -1;

    public DeviceManagementScreen() {

        setTitle("User Device Management");

        setSize(1100, 650);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel =
                new JLabel(
                        "User Device Management",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        // -----------------------------
        // Input Panel
        // -----------------------------

        JPanel inputPanel =
                new JPanel(
                        new GridLayout(6, 2, 10, 10)
                );

        inputPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 20, 10, 20
                )
        );

        userIdField =
                new JTextField();

        deviceNameField =
                new JTextField();

        deviceTypeField =
                new JTextField();

        ipAddressField =
                new JTextField();

        connectionTypeField =
                new JTextField();

        statusField =
                new JTextField("Active");

        inputPanel.add(
                new JLabel("User ID:")
        );
        inputPanel.add(userIdField);

        inputPanel.add(
                new JLabel("Device Name:")
        );
        inputPanel.add(deviceNameField);

        inputPanel.add(
                new JLabel("Device Type:")
        );
        inputPanel.add(deviceTypeField);

        inputPanel.add(
                new JLabel("IP Address:")
        );
        inputPanel.add(ipAddressField);

        inputPanel.add(
                new JLabel("Connection Type:")
        );
        inputPanel.add(connectionTypeField);

        inputPanel.add(
                new JLabel("Status:")
        );
        inputPanel.add(statusField);

        mainPanel.add(
                inputPanel,
                BorderLayout.NORTH
        );

        // -----------------------------
        // Table
        // -----------------------------

        String[] columns = {
                "Device ID",
                "User ID",
                "Device Name",
                "Device Type",
                "IP Address",
                "Connection Type",
                "Status"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                );

        table =
                new JTable(tableModel);

        table.setRowHeight(25);

        JScrollPane scrollPane =
                new JScrollPane(table);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // -----------------------------
        // Buttons
        // -----------------------------

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

        // -----------------------------
        // Table Selection
        // -----------------------------

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int row =
                                table.getSelectedRow();

                        if (row >= 0) {

                            selectedDeviceId =
                                    Integer.parseInt(
                                            tableModel
                                                    .getValueAt(
                                                            row,
                                                            0
                                                    )
                                                    .toString()
                                    );

                            userIdField.setText(
                                    tableModel
                                            .getValueAt(
                                                    row,
                                                    1
                                            )
                                            .toString()
                            );

                            deviceNameField.setText(
                                    tableModel
                                            .getValueAt(
                                                    row,
                                                    2
                                            )
                                            .toString()
                            );

                            deviceTypeField.setText(
                                    tableModel
                                            .getValueAt(
                                                    row,
                                                    3
                                            )
                                            .toString()
                            );

                            ipAddressField.setText(
                                    tableModel
                                            .getValueAt(
                                                    row,
                                                    4
                                            )
                                            .toString()
                            );

                            connectionTypeField.setText(
                                    tableModel
                                            .getValueAt(
                                                    row,
                                                    5
                                            )
                                            .toString()
                            );

                            statusField.setText(
                                    tableModel
                                            .getValueAt(
                                                    row,
                                                    6
                                            )
                                            .toString()
                            );
                        }
                    }
                });

        // -----------------------------
        // Add Button
        // -----------------------------

        addButton.addActionListener(e -> {

            try {

                Device device =
                        new Device();

                device.setUserId(
                        Integer.parseInt(
                                userIdField.getText()
                        )
                );

                device.setDeviceName(
                        deviceNameField.getText()
                );

                device.setDeviceType(
                        deviceTypeField.getText()
                );

                device.setIpAddress(
                        ipAddressField.getText()
                );

                device.setConnectionType(
                        connectionTypeField.getText()
                );

                device.setStatus(
                        statusField.getText()
                );

                DeviceDAO dao =
                        new DeviceDAO();

                if (dao.addDevice(device)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Device added successfully."
                    );

                    loadDevices();

                    clearFields();
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "User ID must be a number."
                );
            }
        });

        // -----------------------------
        // Update Button
        // -----------------------------

        updateButton.addActionListener(e -> {

            if (selectedDeviceId == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a device first."
                );

                return;
            }

            try {

                Device device =
                        new Device();

                device.setDeviceId(
                        selectedDeviceId
                );

                device.setUserId(
                        Integer.parseInt(
                                userIdField.getText()
                        )
                );

                device.setDeviceName(
                        deviceNameField.getText()
                );

                device.setDeviceType(
                        deviceTypeField.getText()
                );

                device.setIpAddress(
                        ipAddressField.getText()
                );

                device.setConnectionType(
                        connectionTypeField.getText()
                );

                device.setStatus(
                        statusField.getText()
                );

                DeviceDAO dao =
                        new DeviceDAO();

                if (dao.updateDevice(device)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Device updated successfully."
                    );

                    loadDevices();

                    clearFields();
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "User ID must be a number."
                );
            }
        });

        // -----------------------------
        // Delete Button
        // -----------------------------

        deleteButton.addActionListener(e -> {

            if (selectedDeviceId == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a device first."
                );

                return;
            }

            int confirmation =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete this device?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirmation ==
                    JOptionPane.YES_OPTION) {

                DeviceDAO dao =
                        new DeviceDAO();

                if (dao.deleteDevice(
                        selectedDeviceId)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Device deleted successfully."
                    );

                    loadDevices();

                    clearFields();
                }
            }
        });

        // -----------------------------
        // Refresh Button
        // -----------------------------

        refreshButton.addActionListener(e -> {
            loadDevices();
        });

        // -----------------------------
        // Clear Button
        // -----------------------------

        clearButton.addActionListener(e -> {
            clearFields();
        });

        // -----------------------------
        // Close Button
        // -----------------------------

        closeButton.addActionListener(e -> {
            dispose();
        });

        add(mainPanel);

        loadDevices();
    }

    // -----------------------------
    // Load Devices
    // -----------------------------

    private void loadDevices() {

        tableModel.setRowCount(0);

        DeviceDAO dao =
                new DeviceDAO();

        List<Device> devices =
                dao.getAllDevices();

        for (Device device : devices) {

            Object[] row = {

                    device.getDeviceId(),

                    device.getUserId(),

                    device.getDeviceName(),

                    device.getDeviceType(),

                    device.getIpAddress(),

                    device.getConnectionType(),

                    device.getStatus()
            };

            tableModel.addRow(row);
        }
    }

    // -----------------------------
    // Clear Fields
    // -----------------------------

    private void clearFields() {

        selectedDeviceId = -1;

        userIdField.setText("");

        deviceNameField.setText("");

        deviceTypeField.setText("");

        ipAddressField.setText("");

        connectionTypeField.setText("");

        statusField.setText("Active");

        table.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            DeviceManagementScreen screen =
                    new DeviceManagementScreen();

            screen.setVisible(true);
        });
    }
}