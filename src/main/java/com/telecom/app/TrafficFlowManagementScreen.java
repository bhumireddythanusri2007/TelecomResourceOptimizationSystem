package com.telecom.app;

import com.telecom.dao.TrafficFlowDAO;
import com.telecom.model.TrafficFlow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TrafficFlowManagementScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField cellIdField;
    private JTextField sourceIpField;
    private JTextField destinationIpField;
    private JTextField dataVolumeField;
    private JTextField flowDurationField;
    private JTextField packetCountField;

    private JComboBox<String> protocolBox;
    private JComboBox<String> statusBox;

    private TrafficFlowDAO trafficFlowDAO;

    public TrafficFlowManagementScreen() {

        trafficFlowDAO = new TrafficFlowDAO();

        setTitle("Traffic Flow Management");
        setSize(1250, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createInterface();
        loadTrafficFlows();
    }

    private void createInterface() {

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel =
                new JPanel(new GridLayout(8, 2, 8, 8));

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Traffic Flow Details"));

        cellIdField = new JTextField();
        sourceIpField = new JTextField();
        destinationIpField = new JTextField();
        dataVolumeField = new JTextField();
        flowDurationField = new JTextField();
        packetCountField = new JTextField();

        protocolBox =
                new JComboBox<>(
                        new String[]{
                                "TCP",
                                "UDP",
                                "HTTP",
                                "MQTT"
                        });

        statusBox =
                new JComboBox<>(
                        new String[]{
                                "Active",
                                "Completed",
                                "Inactive"
                        });

        formPanel.add(new JLabel("Cell ID:"));
        formPanel.add(cellIdField);

        formPanel.add(new JLabel("Source IP:"));
        formPanel.add(sourceIpField);

        formPanel.add(new JLabel("Destination IP:"));
        formPanel.add(destinationIpField);

        formPanel.add(new JLabel("Protocol:"));
        formPanel.add(protocolBox);

        formPanel.add(new JLabel("Data Volume:"));
        formPanel.add(dataVolumeField);

        formPanel.add(new JLabel("Flow Duration:"));
        formPanel.add(flowDurationField);

        formPanel.add(new JLabel("Packet Count:"));
        formPanel.add(packetCountField);

        formPanel.add(new JLabel("Flow Status:"));
        formPanel.add(statusBox);

        add(formPanel, BorderLayout.NORTH);

        tableModel =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Cell ID",
                                "Source IP",
                                "Destination IP",
                                "Protocol",
                                "Data Volume",
                                "Duration",
                                "Packets",
                                "Status",
                                "Recorded Time"
                        }, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        table = new JTable(tableModel);

        JScrollPane scrollPane =
                new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel =
                new JPanel(new FlowLayout());

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

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e ->
                addTrafficFlow());

        updateButton.addActionListener(e ->
                updateTrafficFlow());

        deleteButton.addActionListener(e ->
                deleteTrafficFlow());

        refreshButton.addActionListener(e ->
                loadTrafficFlows());

        closeButton.addActionListener(e ->
                dispose());

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        loadSelectedTrafficFlow();
                    }
                });
    }

    private void loadTrafficFlows() {

        tableModel.setRowCount(0);

        List<TrafficFlow> flows =
                trafficFlowDAO.getAllTrafficFlows();

        for (TrafficFlow flow : flows) {

            tableModel.addRow(
                    new Object[]{
                            flow.getFlowId(),
                            flow.getCellId(),
                            flow.getSourceIp(),
                            flow.getDestinationIp(),
                            flow.getProtocol(),
                            flow.getDataVolume(),
                            flow.getFlowDuration(),
                            flow.getPacketCount(),
                            flow.getFlowStatus(),
                            flow.getRecordedTime()
                    });
        }
    }

    private void loadSelectedTrafficFlow() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        cellIdField.setText(
                tableModel.getValueAt(
                        selectedRow, 1).toString());

        sourceIpField.setText(
                tableModel.getValueAt(
                        selectedRow, 2).toString());

        destinationIpField.setText(
                tableModel.getValueAt(
                        selectedRow, 3).toString());

        protocolBox.setSelectedItem(
                tableModel.getValueAt(
                        selectedRow, 4).toString());

        dataVolumeField.setText(
                tableModel.getValueAt(
                        selectedRow, 5).toString());

        flowDurationField.setText(
                tableModel.getValueAt(
                        selectedRow, 6).toString());

        packetCountField.setText(
                tableModel.getValueAt(
                        selectedRow, 7).toString());

        statusBox.setSelectedItem(
                tableModel.getValueAt(
                        selectedRow, 8).toString());
    }

    private TrafficFlow getTrafficFlowFromForm(
            int flowId) {

        return new TrafficFlow(
                flowId,
                Integer.parseInt(
                        cellIdField.getText().trim()),
                sourceIpField.getText().trim(),
                destinationIpField.getText().trim(),
                protocolBox.getSelectedItem().toString(),
                Double.parseDouble(
                        dataVolumeField.getText().trim()),
                Double.parseDouble(
                        flowDurationField.getText().trim()),
                Integer.parseInt(
                        packetCountField.getText().trim()),
                statusBox.getSelectedItem().toString(),
                ""
        );
    }

    private void addTrafficFlow() {

        try {

            TrafficFlow flow =
                    getTrafficFlowFromForm(0);

            if (trafficFlowDAO.addTrafficFlow(flow)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Traffic flow added successfully.");

                clearFields();
                loadTrafficFlows();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add traffic flow.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cell ID, data volume, flow duration " +
                    "and packet count must contain valid numbers.");
        }
    }

    private void updateTrafficFlow() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a traffic flow.");

            return;
        }

        try {

            int flowId =
                    Integer.parseInt(
                            tableModel.getValueAt(
                                    selectedRow, 0)
                                    .toString());

            TrafficFlow flow =
                    getTrafficFlowFromForm(flowId);

            if (trafficFlowDAO.updateTrafficFlow(flow)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Traffic flow updated successfully.");

                clearFields();
                loadTrafficFlows();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update traffic flow.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cell ID, data volume, flow duration " +
                    "and packet count must contain valid numbers.");
        }
    }

    private void deleteTrafficFlow() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a traffic flow.");

            return;
        }

        int flowId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                selectedRow, 0)
                                .toString());

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete selected traffic flow?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        if (trafficFlowDAO.deleteTrafficFlow(flowId)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Traffic flow deleted successfully.");

            clearFields();
            loadTrafficFlows();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete traffic flow.");
        }
    }

    private void clearFields() {

        cellIdField.setText("");
        sourceIpField.setText("");
        destinationIpField.setText("");
        dataVolumeField.setText("");
        flowDurationField.setText("");
        packetCountField.setText("");

        protocolBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);

        table.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            TrafficFlowManagementScreen screen =
                    new TrafficFlowManagementScreen();

            screen.setVisible(true);
        });
    }
}