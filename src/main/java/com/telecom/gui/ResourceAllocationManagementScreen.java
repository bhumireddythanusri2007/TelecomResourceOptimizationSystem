package com.telecom.gui;

import com.telecom.dao.ResourceAllocationDAO;
import com.telecom.model.ResourceAllocation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ResourceAllocationManagementScreen extends JFrame {

    private JTextField allocationIdField;
    private JTextField cellIdField;
    private JTextField bandwidthField;
    private JTextField frequencyField;
    private JTextField allocatedUsersField;

    private JTable allocationTable;
    private DefaultTableModel tableModel;

    private ResourceAllocationDAO resourceAllocationDAO;

    public ResourceAllocationManagementScreen() {

        resourceAllocationDAO =
                new ResourceAllocationDAO();

        setTitle("Resource Allocation Management");
        setSize(900, 600);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        loadResourceAllocations();

        setVisible(true);
    }

    private void createUI() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JLabel titleLabel =
                new JLabel(
                        "Resource Allocation Management",
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

        // =========================
        // FORM
        // =========================

        JPanel formPanel =
                new JPanel(
                        new GridLayout(3, 4, 10, 10)
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        allocationIdField =
                new JTextField();

        cellIdField =
                new JTextField();

        bandwidthField =
                new JTextField();

        frequencyField =
                new JTextField();

        allocatedUsersField =
                new JTextField();

        allocationIdField.setEditable(false);

        // Row 1

        formPanel.add(
                new JLabel("Allocation ID:")
        );

        formPanel.add(
                allocationIdField
        );

        formPanel.add(
                new JLabel("Cell ID:")
        );

        formPanel.add(
                cellIdField
        );

        // Row 2

        formPanel.add(
                new JLabel("Bandwidth:")
        );

        formPanel.add(
                bandwidthField
        );

        formPanel.add(
                new JLabel("Frequency:")
        );

        formPanel.add(
                frequencyField
        );

        // Row 3

        formPanel.add(
                new JLabel("Allocated Users:")
        );

        formPanel.add(
                allocatedUsersField
        );

        formPanel.add(
                new JLabel("")
        );

        formPanel.add(
                new JLabel("")
        );

        mainPanel.add(
                formPanel,
                BorderLayout.NORTH
        );

        // =========================
        // TABLE
        // =========================

        String[] columnNames = {
                "Allocation ID",
                "Cell ID",
                "Bandwidth",
                "Frequency",
                "Allocated Users"
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

        allocationTable =
                new JTable(tableModel);

        allocationTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        allocationTable
                );

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        // =========================
        // BUTTONS
        // =========================

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

        // =========================
        // EVENTS
        // =========================

        addButton.addActionListener(
                e -> addResourceAllocation()
        );

        updateButton.addActionListener(
                e -> updateResourceAllocation()
        );

        deleteButton.addActionListener(
                e -> deleteResourceAllocation()
        );

        refreshButton.addActionListener(
                e -> loadResourceAllocations()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        allocationTable
                .getSelectionModel()
                .addListSelectionListener(
                        e -> loadSelectedAllocation()
                );

        add(mainPanel);
    }

    // =========================
    // LOAD DATA
    // =========================

    private void loadResourceAllocations() {

        tableModel.setRowCount(0);

        try {

            for (
                    ResourceAllocation allocation :
                    resourceAllocationDAO
                            .getAllResourceAllocations()
            ) {

                tableModel.addRow(
                        new Object[]{
                                allocation.getAllocationId(),
                                allocation.getCellId(),
                                allocation.getBandwidth(),
                                allocation.getFrequency(),
                                allocation.getAllocatedUsers()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading resource allocations.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =========================
    // SELECT ROW
    // =========================

    private void loadSelectedAllocation() {

        int selectedRow =
                allocationTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        allocationIdField.setText(
                tableModel
                        .getValueAt(selectedRow, 0)
                        .toString()
        );

        cellIdField.setText(
                tableModel
                        .getValueAt(selectedRow, 1)
                        .toString()
        );

        bandwidthField.setText(
                tableModel
                        .getValueAt(selectedRow, 2)
                        .toString()
        );

        frequencyField.setText(
                tableModel
                        .getValueAt(selectedRow, 3)
                        .toString()
        );

        allocatedUsersField.setText(
                tableModel
                        .getValueAt(selectedRow, 4)
                        .toString()
        );
    }

    // =========================
    // READ FORM
    // =========================

    private ResourceAllocation
    getAllocationFromFields(
            boolean includeId) {

        try {

            ResourceAllocation allocation =
                    new ResourceAllocation();

            if (includeId) {

                if (allocationIdField
                        .getText()
                        .trim()
                        .isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please select a resource allocation.",
                            "Validation",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return null;
                }

                allocation.setAllocationId(
                        Integer.parseInt(
                                allocationIdField
                                        .getText()
                                        .trim()
                        )
                );
            }

            String cellIdText =
                    cellIdField
                            .getText()
                            .trim();

            String bandwidthText =
                    bandwidthField
                            .getText()
                            .trim();

            String frequencyText =
                    frequencyField
                            .getText()
                            .trim();

            String usersText =
                    allocatedUsersField
                            .getText()
                            .trim();

            if (cellIdText.isEmpty()
                    || bandwidthText.isEmpty()
                    || frequencyText.isEmpty()
                    || usersText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            int cellId =
                    Integer.parseInt(cellIdText);

            int bandwidth =
                    Integer.parseInt(
                            bandwidthText
                    );

            int frequency =
                    Integer.parseInt(
                            frequencyText
                    );

            int allocatedUsers =
                    Integer.parseInt(
                            usersText
                    );

            if (cellId <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Cell ID must be greater than 0.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            if (bandwidth <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Bandwidth must be greater than 0.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            if (frequency <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Frequency must be greater than 0.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            if (allocatedUsers < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Allocated users cannot be negative.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            allocation.setCellId(cellId);

            allocation.setBandwidth(
                    bandwidth
            );

            allocation.setFrequency(
                    frequency
            );

            allocation.setAllocatedUsers(
                    allocatedUsers
            );

            return allocation;

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cell ID, bandwidth, frequency, and allocated users must be whole numbers.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }
    }

    // =========================
    // ADD
    // =========================

    private void addResourceAllocation() {

        ResourceAllocation allocation =
                getAllocationFromFields(false);

        if (allocation == null) {
            return;
        }

        if (resourceAllocationDAO
                .addResourceAllocation(allocation)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Resource allocation added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadResourceAllocations();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add resource allocation.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // UPDATE
    // =========================

    private void updateResourceAllocation() {

        ResourceAllocation allocation =
                getAllocationFromFields(true);

        if (allocation == null) {
            return;
        }

        if (resourceAllocationDAO
                .updateResourceAllocation(allocation)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Resource allocation updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadResourceAllocations();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update resource allocation.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // DELETE
    // =========================

    private void deleteResourceAllocation() {

        if (allocationIdField
                .getText()
                .trim()
                .isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a resource allocation to delete.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int allocationId;

        try {

            allocationId =
                    Integer.parseInt(
                            allocationIdField
                                    .getText()
                                    .trim()
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Allocation ID.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete Allocation ID "
                                + allocationId
                                + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation !=
                JOptionPane.YES_OPTION) {

            return;
        }

        if (resourceAllocationDAO
                .deleteResourceAllocation(
                        allocationId
                )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Resource allocation deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadResourceAllocations();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete resource allocation.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // CLEAR
    // =========================

    private void clearFields() {

        allocationIdField.setText("");
        cellIdField.setText("");
        bandwidthField.setText("");
        frequencyField.setText("");
        allocatedUsersField.setText("");

        allocationTable.clearSelection();
    }

    // =========================
    // MAIN
    // =========================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                ResourceAllocationManagementScreen::new
        );
    }
}