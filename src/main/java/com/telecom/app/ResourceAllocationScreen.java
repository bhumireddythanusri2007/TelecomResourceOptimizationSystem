package com.telecom.app;

import com.telecom.dao.ResourceAllocationDAO;
import com.telecom.model.ResourceAllocation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResourceAllocationScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public ResourceAllocationScreen() {

        setTitle("Resource Allocation Management");

        setSize(1000, 550);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel =
                new JLabel(
                        "Resource Allocation Management",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        String[] columns = {
                "Allocation ID",
                "Cell ID",
                "Bandwidth",
                "Frequency",
                "Allocated Users"
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

        JButton closeButton =
                new JButton("Close");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        addButton.addActionListener(e -> {
            addResourceAllocation();
        });

        updateButton.addActionListener(e -> {
            updateResourceAllocation();
        });

        deleteButton.addActionListener(e -> {
            deleteResourceAllocation();
        });

        refreshButton.addActionListener(e -> {
            loadResourceAllocations();
        });

        closeButton.addActionListener(e -> {
            dispose();
        });

        add(mainPanel);

        loadResourceAllocations();
    }


    // Load allocations into the table
    private void loadResourceAllocations() {

        tableModel.setRowCount(0);

        ResourceAllocationDAO dao =
                new ResourceAllocationDAO();

        List<ResourceAllocation> allocations =
                dao.getAllResourceAllocations();

        for (ResourceAllocation allocation :
                allocations) {

            Object[] row = {
                    allocation.getAllocationId(),
                    allocation.getCellId(),
                    allocation.getBandwidth(),
                    allocation.getFrequency(),
                    allocation.getAllocatedUsers()
            };

            tableModel.addRow(row);
        }
    }


    // Add allocation
    private void addResourceAllocation() {

        JTextField cellIdField =
                new JTextField();

        JTextField bandwidthField =
                new JTextField();

        JTextField frequencyField =
                new JTextField();

        JTextField usersField =
                new JTextField();

        JPanel panel =
                new JPanel(new GridLayout(4, 2, 5, 5));

        panel.add(new JLabel("Cell ID:"));
        panel.add(cellIdField);

        panel.add(new JLabel("Bandwidth:"));
        panel.add(bandwidthField);

        panel.add(new JLabel("Frequency:"));
        panel.add(frequencyField);

        panel.add(new JLabel("Allocated Users:"));
        panel.add(usersField);

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Add Resource Allocation",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            int cellId =
                    Integer.parseInt(
                            cellIdField.getText().trim());

            int bandwidth =
                    Integer.parseInt(
                            bandwidthField.getText().trim());

            int frequency =
                    Integer.parseInt(
                            frequencyField.getText().trim());

            int allocatedUsers =
                    Integer.parseInt(
                            usersField.getText().trim());

            ResourceAllocation allocation =
                    new ResourceAllocation();

            allocation.setCellId(cellId);
            allocation.setBandwidth(bandwidth);
            allocation.setFrequency(frequency);
            allocation.setAllocatedUsers(allocatedUsers);

            ResourceAllocationDAO dao =
                    new ResourceAllocationDAO();

            boolean saved =
                    dao.addResourceAllocation(allocation);

            if (saved) {

                JOptionPane.showMessageDialog(
                        this,
                        "Resource allocation added successfully!"
                );

                loadResourceAllocations();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add resource allocation.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "All fields must contain valid numbers.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }


    // Update allocation
    private void updateResourceAllocation() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an allocation to update.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int allocationId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                selectedRow,
                                0
                        ).toString()
                );

        JTextField cellIdField =
                new JTextField(
                        tableModel.getValueAt(
                                selectedRow, 1
                        ).toString()
                );

        JTextField bandwidthField =
                new JTextField(
                        tableModel.getValueAt(
                                selectedRow, 2
                        ).toString()
                );

        JTextField frequencyField =
                new JTextField(
                        tableModel.getValueAt(
                                selectedRow, 3
                        ).toString()
                );

        JTextField usersField =
                new JTextField(
                        tableModel.getValueAt(
                                selectedRow, 4
                        ).toString()
                );

        JPanel panel =
                new JPanel(new GridLayout(4, 2, 5, 5));

        panel.add(new JLabel("Cell ID:"));
        panel.add(cellIdField);

        panel.add(new JLabel("Bandwidth:"));
        panel.add(bandwidthField);

        panel.add(new JLabel("Frequency:"));
        panel.add(frequencyField);

        panel.add(new JLabel("Allocated Users:"));
        panel.add(usersField);

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Update Resource Allocation",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            int cellId =
                    Integer.parseInt(
                            cellIdField.getText().trim());

            int bandwidth =
                    Integer.parseInt(
                            bandwidthField.getText().trim());

            int frequency =
                    Integer.parseInt(
                            frequencyField.getText().trim());

            int allocatedUsers =
                    Integer.parseInt(
                            usersField.getText().trim());

            ResourceAllocation allocation =
                    new ResourceAllocation();

            allocation.setAllocationId(allocationId);
            allocation.setCellId(cellId);
            allocation.setBandwidth(bandwidth);
            allocation.setFrequency(frequency);
            allocation.setAllocatedUsers(allocatedUsers);

            ResourceAllocationDAO dao =
                    new ResourceAllocationDAO();

            boolean updated =
                    dao.updateResourceAllocation(
                            allocation);

            if (updated) {

                JOptionPane.showMessageDialog(
                        this,
                        "Resource allocation updated successfully!"
                );

                loadResourceAllocations();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update resource allocation.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "All fields must contain valid numbers.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }


    // Delete allocation
    private void deleteResourceAllocation() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an allocation to delete.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int allocationId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                selectedRow,
                                0
                        ).toString()
                );

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete Allocation ID "
                                + allocationId + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        ResourceAllocationDAO dao =
                new ResourceAllocationDAO();

        boolean deleted =
                dao.deleteResourceAllocation(
                        allocationId);

        if (deleted) {

            JOptionPane.showMessageDialog(
                    this,
                    "Resource allocation deleted successfully!"
            );

            loadResourceAllocations();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete resource allocation.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ResourceAllocationScreen screen =
                    new ResourceAllocationScreen();

            screen.setVisible(true);
        });
    }
}