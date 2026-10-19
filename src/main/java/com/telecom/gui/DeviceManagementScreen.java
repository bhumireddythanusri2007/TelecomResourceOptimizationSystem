package com.telecom.gui;

import com.telecom.dao.DeviceDAO;
import com.telecom.model.Device;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DeviceManagementScreen extends JFrame {

    private JTextField deviceIdField;
    private JTextField userIdField;
    private JTextField deviceNameField;
    private JTextField deviceTypeField;
    private JTextField ipAddressField;
    private JTextField connectionTypeField;

    private JComboBox<String> statusComboBox;

    private JTable deviceTable;
    private DefaultTableModel tableModel;

    private DeviceDAO deviceDAO;

    public DeviceManagementScreen() {

        deviceDAO = new DeviceDAO();

        setTitle("Device Management");
        setSize(1000, 650);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        loadDevices();

        setVisible(true);
    }

    private void createUI() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JLabel titleLabel =
                new JLabel(
                        "Device Management",
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
                        new GridLayout(7, 2, 10, 10)
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        deviceIdField =
                new JTextField();

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

        statusComboBox =
                new JComboBox<>(
                        new String[]{
                                "Active",
                                "Inactive",
                                "Maintenance"
                        }
                );

        deviceIdField.setEditable(false);

        formPanel.add(
                new JLabel("Device ID:")
        );

        formPanel.add(
                deviceIdField
        );

        formPanel.add(
                new JLabel("User ID:")
        );

        formPanel.add(
                userIdField
        );

        formPanel.add(
                new JLabel("Device Name:")
        );

        formPanel.add(
                deviceNameField
        );

        formPanel.add(
                new JLabel("Device Type:")
        );

        formPanel.add(
                deviceTypeField
        );

        formPanel.add(
                new JLabel("IP Address:")
        );

        formPanel.add(
                ipAddressField
        );

        formPanel.add(
                new JLabel("Connection Type:")
        );

        formPanel.add(
                connectionTypeField
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

        deviceTable =
                new JTable(tableModel);

        deviceTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(deviceTable);

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
                e -> addDevice()
        );

        updateButton.addActionListener(
                e -> updateDevice()
        );

        deleteButton.addActionListener(
                e -> deleteDevice()
        );

        refreshButton.addActionListener(
                e -> loadDevices()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        deviceTable.getSelectionModel()
                .addListSelectionListener(
                        e -> loadSelectedDevice()
                );

        add(mainPanel);
    }

    private void loadDevices() {

        tableModel.setRowCount(0);

        try {

            for (
                    Device device :
                    deviceDAO.getAllDevices()
            ) {

                tableModel.addRow(
                        new Object[]{
                                device.getDeviceId(),
                                device.getUserId(),
                                device.getDeviceName(),
                                device.getDeviceType(),
                                device.getIpAddress(),
                                device.getConnectionType(),
                                device.getStatus()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading devices.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void loadSelectedDevice() {

        int selectedRow =
                deviceTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        deviceIdField.setText(
                tableModel
                        .getValueAt(selectedRow, 0)
                        .toString()
        );

        userIdField.setText(
                tableModel
                        .getValueAt(selectedRow, 1)
                        .toString()
        );

        deviceNameField.setText(
                tableModel
                        .getValueAt(selectedRow, 2)
                        .toString()
        );

        deviceTypeField.setText(
                tableModel
                        .getValueAt(selectedRow, 3)
                        .toString()
        );

        ipAddressField.setText(
                tableModel
                        .getValueAt(selectedRow, 4)
                        .toString()
        );

        connectionTypeField.setText(
                tableModel
                        .getValueAt(selectedRow, 5)
                        .toString()
        );

        statusComboBox.setSelectedItem(
                tableModel
                        .getValueAt(selectedRow, 6)
                        .toString()
        );
    }

    private Device getDeviceFromFields(
            boolean includeId) {

        try {

            Device device =
                    new Device();

            if (includeId) {

                if (deviceIdField
                        .getText()
                        .trim()
                        .isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please select a device.",
                            "Validation",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return null;
                }

                device.setDeviceId(
                        Integer.parseInt(
                                deviceIdField
                                        .getText()
                                        .trim()
                        )
                );
            }

            String userIdText =
                    userIdField
                            .getText()
                            .trim();

            String deviceName =
                    deviceNameField
                            .getText()
                            .trim();

            String deviceType =
                    deviceTypeField
                            .getText()
                            .trim();

            String ipAddress =
                    ipAddressField
                            .getText()
                            .trim();

            String connectionType =
                    connectionTypeField
                            .getText()
                            .trim();

            if (userIdText.isEmpty()
                    || deviceName.isEmpty()
                    || deviceType.isEmpty()
                    || ipAddress.isEmpty()
                    || connectionType.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            int userId =
                    Integer.parseInt(
                            userIdText
                    );

            if (userId <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "User ID must be greater than 0.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            device.setUserId(userId);

            device.setDeviceName(
                    deviceName
            );

            device.setDeviceType(
                    deviceType
            );

            device.setIpAddress(
                    ipAddress
            );

            device.setConnectionType(
                    connectionType
            );

            device.setStatus(
                    statusComboBox
                            .getSelectedItem()
                            .toString()
            );

            return device;

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "User ID must be a number.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }
    }

    private void addDevice() {

        Device device =
                getDeviceFromFields(false);

        if (device == null) {
            return;
        }

        if (deviceDAO.addDevice(device)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Device added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadDevices();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add device.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateDevice() {

        Device device =
                getDeviceFromFields(true);

        if (device == null) {
            return;
        }

        if (deviceDAO.updateDevice(device)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Device updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadDevices();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update device.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteDevice() {

        if (deviceIdField
                .getText()
                .trim()
                .isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a device to delete.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int deviceId;

        try {

            deviceId =
                    Integer.parseInt(
                            deviceIdField
                                    .getText()
                                    .trim()
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Device ID.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete Device ID "
                                + deviceId
                                + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation !=
                JOptionPane.YES_OPTION) {

            return;
        }

        if (deviceDAO.deleteDevice(deviceId)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Device deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadDevices();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete device.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFields() {

        deviceIdField.setText("");

        userIdField.setText("");

        deviceNameField.setText("");

        deviceTypeField.setText("");

        ipAddressField.setText("");

        connectionTypeField.setText("");

        statusComboBox.setSelectedIndex(0);

        deviceTable.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                DeviceManagementScreen::new
        );
    }
}