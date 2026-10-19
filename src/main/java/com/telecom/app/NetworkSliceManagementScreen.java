package com.telecom.app;

import com.telecom.dao.NetworkSliceDAO;
import com.telecom.model.NetworkSlice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NetworkSliceManagementScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField sliceNameField;
    private JTextField sliceTypeField;
    private JTextField bandwidthField;
    private JTextField latencyField;
    private JTextField usersField;
    private JTextField statusField;

    private int selectedSliceId = -1;

    public NetworkSliceManagementScreen() {

        setTitle("Network Slice Management");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel =
                new JLabel(
                        "Network Slice Management",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        // Input panel
        JPanel inputPanel =
                new JPanel(
                        new GridLayout(6, 2, 10, 10)
                );

        inputPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 20, 10, 20
                )
        );

        sliceNameField = new JTextField();
        sliceTypeField = new JTextField();
        bandwidthField = new JTextField();
        latencyField = new JTextField();
        usersField = new JTextField();
        statusField = new JTextField("Active");

        inputPanel.add(new JLabel("Slice Name:"));
        inputPanel.add(sliceNameField);

        inputPanel.add(new JLabel("Slice Type:"));
        inputPanel.add(sliceTypeField);

        inputPanel.add(new JLabel("Bandwidth:"));
        inputPanel.add(bandwidthField);

        inputPanel.add(new JLabel("Latency Requirement:"));
        inputPanel.add(latencyField);

        inputPanel.add(new JLabel("Connected Users:"));
        inputPanel.add(usersField);

        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(statusField);

        mainPanel.add(
                inputPanel,
                BorderLayout.NORTH
        );

        // Table
        String[] columns = {
                "Slice ID",
                "Slice Name",
                "Slice Type",
                "Bandwidth",
                "Latency Requirement",
                "Connected Users",
                "Status"
        };

        tableModel =
                new DefaultTableModel(columns, 0);

        table =
                new JTable(tableModel);

        table.setRowHeight(25);

        JScrollPane scrollPane =
                new JScrollPane(table);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // Buttons
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

        // Table selection
        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int row =
                                table.getSelectedRow();

                        if (row >= 0) {

                            selectedSliceId =
                                    Integer.parseInt(
                                            tableModel
                                                    .getValueAt(
                                                            row, 0
                                                    )
                                                    .toString()
                                    );

                            sliceNameField.setText(
                                    tableModel
                                            .getValueAt(
                                                    row, 1
                                            )
                                            .toString()
                            );

                            sliceTypeField.setText(
                                    tableModel
                                            .getValueAt(
                                                    row, 2
                                            )
                                            .toString()
                            );

                            bandwidthField.setText(
                                    tableModel
                                            .getValueAt(
                                                    row, 3
                                            )
                                            .toString()
                            );

                            latencyField.setText(
                                    tableModel
                                            .getValueAt(
                                                    row, 4
                                            )
                                            .toString()
                            );

                            usersField.setText(
                                    tableModel
                                            .getValueAt(
                                                    row, 5
                                            )
                                            .toString()
                            );

                            statusField.setText(
                                    tableModel
                                            .getValueAt(
                                                    row, 6
                                            )
                                            .toString()
                            );
                        }
                    }
                });

        // Add
        addButton.addActionListener(e -> {

            try {

                NetworkSlice slice =
                        new NetworkSlice();

                slice.setSliceName(
                        sliceNameField.getText()
                );

                slice.setSliceType(
                        sliceTypeField.getText()
                );

                slice.setBandwidth(
                        Integer.parseInt(
                                bandwidthField.getText()
                        )
                );

                slice.setLatencyRequirement(
                        Double.parseDouble(
                                latencyField.getText()
                        )
                );

                slice.setConnectedUsers(
                        Integer.parseInt(
                                usersField.getText()
                        )
                );

                slice.setStatus(
                        statusField.getText()
                );

                NetworkSliceDAO dao =
                        new NetworkSliceDAO();

                if (dao.addNetworkSlice(slice)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Network slice added successfully."
                    );

                    loadSlices();
                    clearFields();
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Bandwidth, latency and users must contain valid numbers."
                );
            }
        });

        // Update
        updateButton.addActionListener(e -> {

            if (selectedSliceId == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a network slice first."
                );

                return;
            }

            try {

                NetworkSlice slice =
                        new NetworkSlice();

                slice.setSliceId(
                        selectedSliceId
                );

                slice.setSliceName(
                        sliceNameField.getText()
                );

                slice.setSliceType(
                        sliceTypeField.getText()
                );

                slice.setBandwidth(
                        Integer.parseInt(
                                bandwidthField.getText()
                        )
                );

                slice.setLatencyRequirement(
                        Double.parseDouble(
                                latencyField.getText()
                        )
                );

                slice.setConnectedUsers(
                        Integer.parseInt(
                                usersField.getText()
                        )
                );

                slice.setStatus(
                        statusField.getText()
                );

                NetworkSliceDAO dao =
                        new NetworkSliceDAO();

                if (dao.updateNetworkSlice(slice)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Network slice updated successfully."
                    );

                    loadSlices();
                    clearFields();
                }

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Bandwidth, latency and users must contain valid numbers."
                );
            }
        });

        // Delete
        deleteButton.addActionListener(e -> {

            if (selectedSliceId == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a network slice first."
                );

                return;
            }

            int confirmation =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete this network slice?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );

            if (confirmation ==
                    JOptionPane.YES_OPTION) {

                NetworkSliceDAO dao =
                        new NetworkSliceDAO();

                if (dao.deleteNetworkSlice(
                        selectedSliceId)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Network slice deleted successfully."
                    );

                    loadSlices();
                    clearFields();
                }
            }
        });

        // Refresh
        refreshButton.addActionListener(e -> {
            loadSlices();
        });

        // Clear
        clearButton.addActionListener(e -> {
            clearFields();
        });

        // Close
        closeButton.addActionListener(e -> {
            dispose();
        });

        add(mainPanel);

        loadSlices();
    }

    private void loadSlices() {

        tableModel.setRowCount(0);

        NetworkSliceDAO dao =
                new NetworkSliceDAO();

        List<NetworkSlice> slices =
                dao.getAllNetworkSlices();

        for (NetworkSlice slice : slices) {

            Object[] row = {

                    slice.getSliceId(),

                    slice.getSliceName(),

                    slice.getSliceType(),

                    slice.getBandwidth(),

                    slice.getLatencyRequirement(),

                    slice.getConnectedUsers(),

                    slice.getStatus()
            };

            tableModel.addRow(row);
        }
    }

    private void clearFields() {

        selectedSliceId = -1;

        sliceNameField.setText("");
        sliceTypeField.setText("");
        bandwidthField.setText("");
        latencyField.setText("");
        usersField.setText("");
        statusField.setText("Active");

        table.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            NetworkSliceManagementScreen screen =
                    new NetworkSliceManagementScreen();

            screen.setVisible(true);
        });
    }
}