package com.telecom.app;

import com.telecom.dao.NetworkCellDAO;
import com.telecom.model.NetworkCell;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NetworkCellScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public NetworkCellScreen() {

        setTitle("Network Cell Management");

        setSize(1000, 550);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel =
                new JLabel(
                        "Network Cell Management",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        // Table columns
        String[] columns = {
                "Cell ID",
                "Cell Name",
                "Location",
                "Total Capacity",
                "Current Users",
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

        // Button actions

        addButton.addActionListener(e -> {
            addNetworkCell();
        });

        updateButton.addActionListener(e -> {
            updateNetworkCell();
        });

        deleteButton.addActionListener(e -> {
            deleteNetworkCell();
        });

        refreshButton.addActionListener(e -> {
            loadNetworkCells();
        });

        closeButton.addActionListener(e -> {
            dispose();
        });

        add(mainPanel);

        // Load data when screen opens
        loadNetworkCells();
    }


    // Load network cells into table
    private void loadNetworkCells() {

        tableModel.setRowCount(0);

        NetworkCellDAO dao =
                new NetworkCellDAO();

        List<NetworkCell> cells =
                dao.getAllNetworkCells();

        for (NetworkCell cell : cells) {

            Object[] row = {
                    cell.getCellId(),
                    cell.getCellName(),
                    cell.getLocation(),
                    cell.getTotalCapacity(),
                    cell.getCurrentUsers(),
                    cell.getStatus()
            };

            tableModel.addRow(row);
        }
    }


    // Add network cell
    private void addNetworkCell() {

        JTextField nameField =
                new JTextField();

        JTextField locationField =
                new JTextField();

        JTextField capacityField =
                new JTextField();

        JTextField usersField =
                new JTextField();

        JTextField statusField =
                new JTextField();

        JPanel panel =
                new JPanel(new GridLayout(5, 2, 5, 5));

        panel.add(new JLabel("Cell Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Location:"));
        panel.add(locationField);

        panel.add(new JLabel("Total Capacity:"));
        panel.add(capacityField);

        panel.add(new JLabel("Current Users:"));
        panel.add(usersField);

        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Add Network Cell",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            String cellName =
                    nameField.getText().trim();

            String location =
                    locationField.getText().trim();

            int totalCapacity =
                    Integer.parseInt(
                            capacityField.getText().trim());

            int currentUsers =
                    Integer.parseInt(
                            usersField.getText().trim());

            String status =
                    statusField.getText().trim();

            if (cellName.isEmpty()
                    || location.isEmpty()
                    || status.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            NetworkCell cell =
                    new NetworkCell();

            cell.setCellName(cellName);
            cell.setLocation(location);
            cell.setTotalCapacity(totalCapacity);
            cell.setCurrentUsers(currentUsers);
            cell.setStatus(status);

            NetworkCellDAO dao =
                    new NetworkCellDAO();

            boolean saved =
                    dao.addNetworkCell(cell);

            if (saved) {

                JOptionPane.showMessageDialog(
                        this,
                        "Network cell added successfully!"
                );

                loadNetworkCells();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add network cell.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Capacity and Current Users must be numbers.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }


    // Update network cell
    private void updateNetworkCell() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a network cell to update.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int cellId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                selectedRow,
                                0
                        ).toString()
                );

        JTextField nameField =
                new JTextField(
                        tableModel.getValueAt(
                                selectedRow, 1
                        ).toString()
                );

        JTextField locationField =
                new JTextField(
                        tableModel.getValueAt(
                                selectedRow, 2
                        ).toString()
                );

        JTextField capacityField =
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

        JTextField statusField =
                new JTextField(
                        tableModel.getValueAt(
                                selectedRow, 5
                        ).toString()
                );

        JPanel panel =
                new JPanel(new GridLayout(5, 2, 5, 5));

        panel.add(new JLabel("Cell Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Location:"));
        panel.add(locationField);

        panel.add(new JLabel("Total Capacity:"));
        panel.add(capacityField);

        panel.add(new JLabel("Current Users:"));
        panel.add(usersField);

        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Update Network Cell",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {

            String cellName =
                    nameField.getText().trim();

            String location =
                    locationField.getText().trim();

            int totalCapacity =
                    Integer.parseInt(
                            capacityField.getText().trim());

            int currentUsers =
                    Integer.parseInt(
                            usersField.getText().trim());

            String status =
                    statusField.getText().trim();

            if (cellName.isEmpty()
                    || location.isEmpty()
                    || status.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            NetworkCell cell =
                    new NetworkCell();

            cell.setCellId(cellId);
            cell.setCellName(cellName);
            cell.setLocation(location);
            cell.setTotalCapacity(totalCapacity);
            cell.setCurrentUsers(currentUsers);
            cell.setStatus(status);

            NetworkCellDAO dao =
                    new NetworkCellDAO();

            boolean updated =
                    dao.updateNetworkCell(cell);

            if (updated) {

                JOptionPane.showMessageDialog(
                        this,
                        "Network cell updated successfully!"
                );

                loadNetworkCells();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update network cell.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Capacity and Current Users must be numbers.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }


    // Delete network cell
    private void deleteNetworkCell() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a network cell to delete.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int cellId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                selectedRow,
                                0
                        ).toString()
                );

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete Cell ID "
                                + cellId + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        NetworkCellDAO dao =
                new NetworkCellDAO();

        boolean deleted =
                dao.deleteNetworkCell(cellId);

        if (deleted) {

            JOptionPane.showMessageDialog(
                    this,
                    "Network cell deleted successfully!"
            );

            loadNetworkCells();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete network cell.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            NetworkCellScreen screen =
                    new NetworkCellScreen();

            screen.setVisible(true);
        });
    }
}