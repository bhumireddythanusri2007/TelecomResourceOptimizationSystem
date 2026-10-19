package com.telecom.gui;

import com.telecom.dao.NetworkCellDAO;
import com.telecom.model.NetworkCell;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NetworkCellManagementScreen extends JFrame {

    private JTextField cellIdField;
    private JTextField cellNameField;
    private JTextField locationField;
    private JTextField totalCapacityField;
    private JTextField currentUsersField;

    private JComboBox<String> statusComboBox;

    private JTable cellTable;
    private DefaultTableModel tableModel;

    private NetworkCellDAO networkCellDAO;

    public NetworkCellManagementScreen() {

        networkCellDAO =
                new NetworkCellDAO();

        setTitle(
                "Network Cell Management"
        );

        setSize(900, 600);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        loadNetworkCells();

        setVisible(true);
    }

    private void createUI() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JLabel titleLabel =
                new JLabel(
                        "Network Cell Management",
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
                        new GridLayout(6, 2, 10, 10)
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        cellIdField =
                new JTextField();

        cellNameField =
                new JTextField();

        locationField =
                new JTextField();

        totalCapacityField =
                new JTextField();

        currentUsersField =
                new JTextField();

        statusComboBox =
                new JComboBox<>(
                        new String[]{
                                "Active",
                                "Inactive",
                                "Maintenance"
                        }
                );

        cellIdField.setEditable(false);

        formPanel.add(
                new JLabel("Cell ID:")
        );

        formPanel.add(
                cellIdField
        );

        formPanel.add(
                new JLabel("Cell Name:")
        );

        formPanel.add(
                cellNameField
        );

        formPanel.add(
                new JLabel("Location:")
        );

        formPanel.add(
                locationField
        );

        formPanel.add(
                new JLabel("Total Capacity:")
        );

        formPanel.add(
                totalCapacityField
        );

        formPanel.add(
                new JLabel("Current Users:")
        );

        formPanel.add(
                currentUsersField
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
                "Cell ID",
                "Cell Name",
                "Location",
                "Total Capacity",
                "Current Users",
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

        cellTable =
                new JTable(tableModel);

        cellTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(cellTable);

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
                e -> addNetworkCell()
        );

        updateButton.addActionListener(
                e -> updateNetworkCell()
        );

        deleteButton.addActionListener(
                e -> deleteNetworkCell()
        );

        refreshButton.addActionListener(
                e -> loadNetworkCells()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        cellTable.getSelectionModel()
                .addListSelectionListener(
                        e -> loadSelectedCell()
                );

        add(mainPanel);
    }

    private void loadNetworkCells() {

        tableModel.setRowCount(0);

        try {

            for (
                    NetworkCell cell :
                    networkCellDAO
                            .getAllNetworkCells()
            ) {

                tableModel.addRow(
                        new Object[]{
                                cell.getCellId(),
                                cell.getCellName(),
                                cell.getLocation(),
                                cell.getTotalCapacity(),
                                cell.getCurrentUsers(),
                                cell.getStatus()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading network cells.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void loadSelectedCell() {

        int selectedRow =
                cellTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        cellIdField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                0
                        )
                        .toString()
        );

        cellNameField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                1
                        )
                        .toString()
        );

        locationField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                2
                        )
                        .toString()
        );

        totalCapacityField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                3
                        )
                        .toString()
        );

        currentUsersField.setText(
                tableModel
                        .getValueAt(
                                selectedRow,
                                4
                        )
                        .toString()
        );

        statusComboBox.setSelectedItem(
                tableModel
                        .getValueAt(
                                selectedRow,
                                5
                        )
                        .toString()
        );
    }

    private NetworkCell getCellFromFields(
            boolean includeId) {

        try {

            NetworkCell cell =
                    new NetworkCell();

            if (includeId) {

                if (cellIdField
                        .getText()
                        .trim()
                        .isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please select a network cell.",
                            "Validation",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return null;
                }

                cell.setCellId(
                        Integer.parseInt(
                                cellIdField
                                        .getText()
                                        .trim()
                        )
                );
            }

            String cellName =
                    cellNameField
                            .getText()
                            .trim();

            String location =
                    locationField
                            .getText()
                            .trim();

            String totalCapacityText =
                    totalCapacityField
                            .getText()
                            .trim();

            String currentUsersText =
                    currentUsersField
                            .getText()
                            .trim();

            if (cellName.isEmpty()
                    || location.isEmpty()
                    || totalCapacityText.isEmpty()
                    || currentUsersText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            int totalCapacity =
                    Integer.parseInt(
                            totalCapacityText
                    );

            int currentUsers =
                    Integer.parseInt(
                            currentUsersText
                    );

            if (totalCapacity <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Total capacity must be greater than 0.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            if (currentUsers < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Current users cannot be negative.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            if (currentUsers > totalCapacity) {

                JOptionPane.showMessageDialog(
                        this,
                        "Current users cannot exceed total capacity.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            cell.setCellName(cellName);

            cell.setLocation(location);

            cell.setTotalCapacity(
                    totalCapacity
            );

            cell.setCurrentUsers(
                    currentUsers
            );

            cell.setStatus(
                    statusComboBox
                            .getSelectedItem()
                            .toString()
            );

            return cell;

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Capacity and current users must be numbers.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }
    }

    private void addNetworkCell() {

        NetworkCell cell =
                getCellFromFields(false);

        if (cell == null) {
            return;
        }

        if (
                networkCellDAO
                        .addNetworkCell(cell)
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Network cell added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadNetworkCells();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add network cell.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateNetworkCell() {

        NetworkCell cell =
                getCellFromFields(true);

        if (cell == null) {
            return;
        }

        if (
                networkCellDAO
                        .updateNetworkCell(cell)
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Network cell updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadNetworkCells();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update network cell.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteNetworkCell() {

        if (
                cellIdField
                        .getText()
                        .trim()
                        .isEmpty()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a network cell to delete.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int cellId;

        try {

            cellId =
                    Integer.parseInt(
                            cellIdField
                                    .getText()
                                    .trim()
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Cell ID.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete Cell ID "
                                + cellId
                                + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                confirmation
                        != JOptionPane.YES_OPTION
        ) {

            return;
        }

        if (
                networkCellDAO
                        .deleteNetworkCell(cellId)
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Network cell deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadNetworkCells();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete network cell.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFields() {

        cellIdField.setText("");

        cellNameField.setText("");

        locationField.setText("");

        totalCapacityField.setText("");

        currentUsersField.setText("");

        statusComboBox.setSelectedIndex(0);

        cellTable.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                NetworkCellManagementScreen::new
        );
    }
}