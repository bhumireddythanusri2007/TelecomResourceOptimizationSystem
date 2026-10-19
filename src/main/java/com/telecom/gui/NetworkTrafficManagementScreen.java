package com.telecom.gui;

import com.telecom.dao.NetworkTrafficDAO;
import com.telecom.model.NetworkTraffic;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NetworkTrafficManagementScreen extends JFrame {

    private JTextField trafficIdField;
    private JTextField cellIdField;
    private JTextField dataUsageField;
    private JTextField activeUsersField;
    private JTextField trafficLoadField;

    private JTable trafficTable;
    private DefaultTableModel tableModel;

    private NetworkTrafficDAO trafficDAO;

    public NetworkTrafficManagementScreen() {

        trafficDAO = new NetworkTrafficDAO();

        setTitle("Network Traffic Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeUI();
        loadTrafficData();
    }

    private void initializeUI() {

        setLayout(new BorderLayout(10, 10));

        // ---------------- FORM PANEL ----------------

        JPanel formPanel = new JPanel(
                new GridLayout(3, 4, 10, 10)
        );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        formPanel.add(new JLabel("Traffic ID"));
        trafficIdField = new JTextField();
        trafficIdField.setEditable(false);
        formPanel.add(trafficIdField);

        formPanel.add(new JLabel("Cell ID"));
        cellIdField = new JTextField();
        formPanel.add(cellIdField);

        formPanel.add(new JLabel("Data Usage"));
        dataUsageField = new JTextField();
        formPanel.add(dataUsageField);

        formPanel.add(new JLabel("Active Users"));
        activeUsersField = new JTextField();
        formPanel.add(activeUsersField);

        formPanel.add(new JLabel("Traffic Load"));
        trafficLoadField = new JTextField();
        formPanel.add(trafficLoadField);

        formPanel.add(new JLabel(""));
        formPanel.add(new JLabel(""));

        add(formPanel, BorderLayout.NORTH);


        // ---------------- TABLE ----------------

        String[] columns = {
                "Traffic ID",
                "Cell ID",
                "Data Usage",
                "Active Users",
                "Traffic Load",
                "Recorded Time"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        trafficTable = new JTable(tableModel);

        trafficTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(trafficTable);

        add(scrollPane, BorderLayout.CENTER);


        // ---------------- BUTTON PANEL ----------------

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");
        JButton clearButton = new JButton("Clear");
        JButton closeButton = new JButton("Close");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);


        // ---------------- BUTTON ACTIONS ----------------

        addButton.addActionListener(e -> addTraffic());

        updateButton.addActionListener(e -> updateTraffic());

        deleteButton.addActionListener(e -> deleteTraffic());

        refreshButton.addActionListener(e -> loadTrafficData());

        clearButton.addActionListener(e -> clearFields());

        closeButton.addActionListener(e -> dispose());


        // ---------------- TABLE SELECTION ----------------

        trafficTable.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int selectedRow =
                                trafficTable.getSelectedRow();

                        if (selectedRow >= 0) {

                            trafficIdField.setText(
                                    tableModel.getValueAt(
                                            selectedRow, 0
                                    ).toString()
                            );

                            cellIdField.setText(
                                    tableModel.getValueAt(
                                            selectedRow, 1
                                    ).toString()
                            );

                            dataUsageField.setText(
                                    tableModel.getValueAt(
                                            selectedRow, 2
                                    ).toString()
                            );

                            activeUsersField.setText(
                                    tableModel.getValueAt(
                                            selectedRow, 3
                                    ).toString()
                            );

                            trafficLoadField.setText(
                                    tableModel.getValueAt(
                                            selectedRow, 4
                                    ).toString()
                            );
                        }
                    }
                });
    }


    // ---------------- LOAD DATA ----------------

    private void loadTrafficData() {

        tableModel.setRowCount(0);

        for (NetworkTraffic traffic :
                trafficDAO.getAllNetworkTraffic()) {

            tableModel.addRow(new Object[]{
                    traffic.getTrafficId(),
                    traffic.getCellId(),
                    traffic.getDataUsage(),
                    traffic.getActiveUsers(),
                    traffic.getTrafficLoad(),
                    traffic.getRecordedTime()
            });
        }
    }


    // ---------------- ADD ----------------

    private void addTraffic() {

        try {

            int cellId =
                    Integer.parseInt(
                            cellIdField.getText().trim()
                    );

            double dataUsage =
                    Double.parseDouble(
                            dataUsageField.getText().trim()
                    );

            int activeUsers =
                    Integer.parseInt(
                            activeUsersField.getText().trim()
                    );

            double trafficLoad =
                    Double.parseDouble(
                            trafficLoadField.getText().trim()
                    );

            if (cellId <= 0 ||
                    dataUsage < 0 ||
                    activeUsers < 0 ||
                    trafficLoad < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid positive values."
                );

                return;
            }

            NetworkTraffic traffic =
                    new NetworkTraffic();

            traffic.setCellId(cellId);
            traffic.setDataUsage(dataUsage);
            traffic.setActiveUsers(activeUsers);
            traffic.setTrafficLoad(trafficLoad);

            if (trafficDAO.addNetworkTraffic(traffic)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Network traffic added successfully."
                );

                loadTrafficData();
                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add network traffic."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values."
            );
        }
    }


    // ---------------- UPDATE ----------------

    private void updateTraffic() {

        try {

            int trafficId =
                    Integer.parseInt(
                            trafficIdField.getText().trim()
                    );

            int cellId =
                    Integer.parseInt(
                            cellIdField.getText().trim()
                    );

            double dataUsage =
                    Double.parseDouble(
                            dataUsageField.getText().trim()
                    );

            int activeUsers =
                    Integer.parseInt(
                            activeUsersField.getText().trim()
                    );

            double trafficLoad =
                    Double.parseDouble(
                            trafficLoadField.getText().trim()
                    );

            if (trafficId <= 0 ||
                    cellId <= 0 ||
                    dataUsage < 0 ||
                    activeUsers < 0 ||
                    trafficLoad < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid values."
                );

                return;
            }

            NetworkTraffic traffic =
                    new NetworkTraffic();

            traffic.setTrafficId(trafficId);
            traffic.setCellId(cellId);
            traffic.setDataUsage(dataUsage);
            traffic.setActiveUsers(activeUsers);
            traffic.setTrafficLoad(trafficLoad);

            if (trafficDAO.updateNetworkTraffic(traffic)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Network traffic updated successfully."
                );

                loadTrafficData();
                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update network traffic."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values."
            );
        }
    }


    // ---------------- DELETE ----------------

    private void deleteTraffic() {

        try {

            int trafficId =
                    Integer.parseInt(
                            trafficIdField.getText().trim()
                    );

            int choice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete this record?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            if (trafficDAO.deleteNetworkTraffic(trafficId)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Network traffic deleted successfully."
                );

                loadTrafficData();
                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete network traffic."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a record to delete."
            );
        }
    }


    // ---------------- CLEAR ----------------

    private void clearFields() {

        trafficIdField.setText("");
        cellIdField.setText("");
        dataUsageField.setText("");
        activeUsersField.setText("");
        trafficLoadField.setText("");

        trafficTable.clearSelection();
    }


    // ---------------- MAIN ----------------

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            NetworkTrafficManagementScreen screen =
                    new NetworkTrafficManagementScreen();

            screen.setVisible(true);
        });
    }
}