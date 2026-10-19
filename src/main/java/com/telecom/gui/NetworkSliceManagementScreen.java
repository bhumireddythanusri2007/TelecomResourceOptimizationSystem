package com.telecom.gui;

import com.telecom.dao.NetworkSliceDAO;
import com.telecom.model.NetworkSlice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NetworkSliceManagementScreen extends JFrame {

    private JTextField sliceIdField;
    private JTextField sliceNameField;
    private JTextField sliceTypeField;
    private JTextField bandwidthField;
    private JTextField latencyRequirementField;
    private JTextField connectedUsersField;

    private JComboBox<String> statusComboBox;

    private JTable sliceTable;
    private DefaultTableModel tableModel;

    private NetworkSliceDAO networkSliceDAO;

    public NetworkSliceManagementScreen() {

        networkSliceDAO = new NetworkSliceDAO();

        setTitle("Network Slice Management");
        setSize(1000, 650);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        loadNetworkSlices();

        setVisible(true);
    }

    private void createUI() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JLabel titleLabel =
                new JLabel(
                        "Network Slice Management",
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

        sliceIdField = new JTextField();
        sliceNameField = new JTextField();
        sliceTypeField = new JTextField();
        bandwidthField = new JTextField();
        latencyRequirementField = new JTextField();
        connectedUsersField = new JTextField();

        statusComboBox =
                new JComboBox<>(
                        new String[]{
                                "Active",
                                "Inactive",
                                "Maintenance"
                        }
                );

        sliceIdField.setEditable(false);

        formPanel.add(
                new JLabel("Slice ID:")
        );

        formPanel.add(
                sliceIdField
        );

        formPanel.add(
                new JLabel("Slice Name:")
        );

        formPanel.add(
                sliceNameField
        );

        formPanel.add(
                new JLabel("Slice Type:")
        );

        formPanel.add(
                sliceTypeField
        );

        formPanel.add(
                new JLabel("Bandwidth:")
        );

        formPanel.add(
                bandwidthField
        );

        formPanel.add(
                new JLabel("Latency Requirement:")
        );

        formPanel.add(
                latencyRequirementField
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
                "Slice ID",
                "Slice Name",
                "Slice Type",
                "Bandwidth",
                "Latency Requirement",
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

        sliceTable =
                new JTable(tableModel);

        sliceTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(sliceTable);

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
                e -> addNetworkSlice()
        );

        updateButton.addActionListener(
                e -> updateNetworkSlice()
        );

        deleteButton.addActionListener(
                e -> deleteNetworkSlice()
        );

        refreshButton.addActionListener(
                e -> loadNetworkSlices()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        sliceTable.getSelectionModel()
                .addListSelectionListener(
                        e -> loadSelectedSlice()
                );

        add(mainPanel);
    }

    private void loadNetworkSlices() {

        tableModel.setRowCount(0);

        try {

            for (
                    NetworkSlice slice :
                    networkSliceDAO.getAllNetworkSlices()
            ) {

                tableModel.addRow(
                        new Object[]{
                                slice.getSliceId(),
                                slice.getSliceName(),
                                slice.getSliceType(),
                                slice.getBandwidth(),
                                slice.getLatencyRequirement(),
                                slice.getConnectedUsers(),
                                slice.getStatus()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading network slices.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void loadSelectedSlice() {

        int selectedRow =
                sliceTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        sliceIdField.setText(
                tableModel
                        .getValueAt(selectedRow, 0)
                        .toString()
        );

        sliceNameField.setText(
                tableModel
                        .getValueAt(selectedRow, 1)
                        .toString()
        );

        sliceTypeField.setText(
                tableModel
                        .getValueAt(selectedRow, 2)
                        .toString()
        );

        bandwidthField.setText(
                tableModel
                        .getValueAt(selectedRow, 3)
                        .toString()
        );

        latencyRequirementField.setText(
                tableModel
                        .getValueAt(selectedRow, 4)
                        .toString()
        );

        connectedUsersField.setText(
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

    private NetworkSlice getSliceFromFields(
            boolean includeId) {

        try {

            NetworkSlice slice =
                    new NetworkSlice();

            if (includeId) {

                if (sliceIdField
                        .getText()
                        .trim()
                        .isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please select a network slice.",
                            "Validation",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return null;
                }

                slice.setSliceId(
                        Integer.parseInt(
                                sliceIdField
                                        .getText()
                                        .trim()
                        )
                );
            }

            String sliceName =
                    sliceNameField
                            .getText()
                            .trim();

            String sliceType =
                    sliceTypeField
                            .getText()
                            .trim();

            String bandwidthText =
                    bandwidthField
                            .getText()
                            .trim();

            String latencyText =
                    latencyRequirementField
                            .getText()
                            .trim();

            String connectedUsersText =
                    connectedUsersField
                            .getText()
                            .trim();

            if (sliceName.isEmpty()
                    || sliceType.isEmpty()
                    || bandwidthText.isEmpty()
                    || latencyText.isEmpty()
                    || connectedUsersText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            int bandwidth =
                    Integer.parseInt(
                            bandwidthText
                    );

            double latencyRequirement =
                    Double.parseDouble(
                            latencyText
                    );

            int connectedUsers =
                    Integer.parseInt(
                            connectedUsersText
                    );

            if (bandwidth <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Bandwidth must be greater than 0.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            if (latencyRequirement <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Latency requirement must be greater than 0.",
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

            slice.setSliceName(
                    sliceName
            );

            slice.setSliceType(
                    sliceType
            );

            slice.setBandwidth(
                    bandwidth
            );

            slice.setLatencyRequirement(
                    latencyRequirement
            );

            slice.setConnectedUsers(
                    connectedUsers
            );

            slice.setStatus(
                    statusComboBox
                            .getSelectedItem()
                            .toString()
            );

            return slice;

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bandwidth and connected users must be whole numbers, and latency must be a number.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }
    }

    private void addNetworkSlice() {

        NetworkSlice slice =
                getSliceFromFields(false);

        if (slice == null) {
            return;
        }

        if (networkSliceDAO.addNetworkSlice(slice)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Network slice added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadNetworkSlices();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add network slice.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateNetworkSlice() {

        NetworkSlice slice =
                getSliceFromFields(true);

        if (slice == null) {
            return;
        }

        if (networkSliceDAO.updateNetworkSlice(slice)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Network slice updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadNetworkSlices();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update network slice.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteNetworkSlice() {

        if (sliceIdField
                .getText()
                .trim()
                .isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a network slice to delete.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int sliceId;

        try {

            sliceId =
                    Integer.parseInt(
                            sliceIdField
                                    .getText()
                                    .trim()
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Slice ID.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete Slice ID "
                                + sliceId
                                + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation !=
                JOptionPane.YES_OPTION) {

            return;
        }

        if (networkSliceDAO.deleteNetworkSlice(
                sliceId)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Network slice deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadNetworkSlices();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete network slice.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFields() {

        sliceIdField.setText("");

        sliceNameField.setText("");

        sliceTypeField.setText("");

        bandwidthField.setText("");

        latencyRequirementField.setText("");

        connectedUsersField.setText("");

        statusComboBox.setSelectedIndex(0);

        sliceTable.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                NetworkSliceManagementScreen::new
        );
    }
}