package com.telecom.gui;

import com.telecom.dao.TrafficFlowDAO;
import com.telecom.model.TrafficFlow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TrafficFlowManagementScreen extends JFrame {

    private JTextField flowIdField;
    private JTextField cellIdField;
    private JTextField sourceIpField;
    private JTextField destinationIpField;
    private JTextField dataVolumeField;
    private JTextField flowDurationField;
    private JTextField packetCountField;

    private JComboBox<String> protocolComboBox;
    private JComboBox<String> statusComboBox;

    private JTable flowTable;
    private DefaultTableModel tableModel;

    private TrafficFlowDAO trafficFlowDAO;

    public TrafficFlowManagementScreen() {

        trafficFlowDAO = new TrafficFlowDAO();

        setTitle("Traffic Flow Management");
        setSize(1150, 700);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        loadTrafficFlows();

        setVisible(true);
    }

    private void createUI() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JLabel titleLabel =
                new JLabel(
                        "Traffic Flow Management",
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
        // FORM PANEL
        // =========================

        JPanel formPanel =
                new JPanel(
                        new GridLayout(5, 4, 10, 10)
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        flowIdField = new JTextField();
        cellIdField = new JTextField();
        sourceIpField = new JTextField();
        destinationIpField = new JTextField();
        dataVolumeField = new JTextField();
        flowDurationField = new JTextField();
        packetCountField = new JTextField();

        protocolComboBox =
                new JComboBox<>(
                        new String[]{
                                "TCP",
                                "UDP",
                                "HTTP",
                                "HTTPS",
                                "ICMP",
                                "MQTT"
                        }
                );

        statusComboBox =
                new JComboBox<>(
                        new String[]{
                                "Active",
                                "Completed",
                                "Dropped",
                                "Blocked"
                        }
                );

        flowIdField.setEditable(false);

        // Row 1
        formPanel.add(
                new JLabel("Flow ID:")
        );

        formPanel.add(
                flowIdField
        );

        formPanel.add(
                new JLabel("Cell ID:")
        );

        formPanel.add(
                cellIdField
        );

        // Row 2
        formPanel.add(
                new JLabel("Source IP:")
        );

        formPanel.add(
                sourceIpField
        );

        formPanel.add(
                new JLabel("Destination IP:")
        );

        formPanel.add(
                destinationIpField
        );

        // Row 3
        formPanel.add(
                new JLabel("Protocol:")
        );

        formPanel.add(
                protocolComboBox
        );

        formPanel.add(
                new JLabel("Data Volume:")
        );

        formPanel.add(
                dataVolumeField
        );

        // Row 4
        formPanel.add(
                new JLabel("Flow Duration:")
        );

        formPanel.add(
                flowDurationField
        );

        formPanel.add(
                new JLabel("Packet Count:")
        );

        formPanel.add(
                packetCountField
        );

        // Row 5
        formPanel.add(
                new JLabel("Flow Status:")
        );

        formPanel.add(
                statusComboBox
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
                "Flow ID",
                "Cell ID",
                "Source IP",
                "Destination IP",
                "Protocol",
                "Data Volume",
                "Flow Duration",
                "Packet Count",
                "Flow Status",
                "Recorded Time"
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

        flowTable =
                new JTable(tableModel);

        flowTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(flowTable);

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
                e -> addTrafficFlow()
        );

        updateButton.addActionListener(
                e -> updateTrafficFlow()
        );

        deleteButton.addActionListener(
                e -> deleteTrafficFlow()
        );

        refreshButton.addActionListener(
                e -> loadTrafficFlows()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        flowTable.getSelectionModel()
                .addListSelectionListener(
                        e -> loadSelectedFlow()
                );

        add(mainPanel);
    }

    private void loadTrafficFlows() {

        tableModel.setRowCount(0);

        try {

            for (
                    TrafficFlow flow :
                    trafficFlowDAO.getAllTrafficFlows()
            ) {

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
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading traffic flows.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void loadSelectedFlow() {

        int selectedRow =
                flowTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        flowIdField.setText(
                tableModel
                        .getValueAt(selectedRow, 0)
                        .toString()
        );

        cellIdField.setText(
                tableModel
                        .getValueAt(selectedRow, 1)
                        .toString()
        );

        sourceIpField.setText(
                tableModel
                        .getValueAt(selectedRow, 2)
                        .toString()
        );

        destinationIpField.setText(
                tableModel
                        .getValueAt(selectedRow, 3)
                        .toString()
        );

        protocolComboBox.setSelectedItem(
                tableModel
                        .getValueAt(selectedRow, 4)
                        .toString()
        );

        dataVolumeField.setText(
                tableModel
                        .getValueAt(selectedRow, 5)
                        .toString()
        );

        flowDurationField.setText(
                tableModel
                        .getValueAt(selectedRow, 6)
                        .toString()
        );

        packetCountField.setText(
                tableModel
                        .getValueAt(selectedRow, 7)
                        .toString()
        );

        statusComboBox.setSelectedItem(
                tableModel
                        .getValueAt(selectedRow, 8)
                        .toString()
        );
    }

    private TrafficFlow getFlowFromFields(
            boolean includeId) {

        try {

            TrafficFlow flow =
                    new TrafficFlow();

            if (includeId) {

                if (flowIdField
                        .getText()
                        .trim()
                        .isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please select a traffic flow.",
                            "Validation",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return null;
                }

                flow.setFlowId(
                        Integer.parseInt(
                                flowIdField
                                        .getText()
                                        .trim()
                        )
                );
            }

            String cellIdText =
                    cellIdField
                            .getText()
                            .trim();

            String sourceIp =
                    sourceIpField
                            .getText()
                            .trim();

            String destinationIp =
                    destinationIpField
                            .getText()
                            .trim();

            String dataVolumeText =
                    dataVolumeField
                            .getText()
                            .trim();

            String flowDurationText =
                    flowDurationField
                            .getText()
                            .trim();

            String packetCountText =
                    packetCountField
                            .getText()
                            .trim();

            if (cellIdText.isEmpty()
                    || sourceIp.isEmpty()
                    || destinationIp.isEmpty()
                    || dataVolumeText.isEmpty()
                    || flowDurationText.isEmpty()
                    || packetCountText.isEmpty()) {

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

            double dataVolume =
                    Double.parseDouble(
                            dataVolumeText
                    );

            double flowDuration =
                    Double.parseDouble(
                            flowDurationText
                    );

            int packetCount =
                    Integer.parseInt(
                            packetCountText
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

            if (dataVolume < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data volume cannot be negative.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            if (flowDuration < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Flow duration cannot be negative.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            if (packetCount < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Packet count cannot be negative.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            flow.setCellId(cellId);

            flow.setSourceIp(sourceIp);

            flow.setDestinationIp(
                    destinationIp
            );

            flow.setProtocol(
                    protocolComboBox
                            .getSelectedItem()
                            .toString()
            );

            flow.setDataVolume(
                    dataVolume
            );

            flow.setFlowDuration(
                    flowDuration
            );

            flow.setPacketCount(
                    packetCount
            );

            flow.setFlowStatus(
                    statusComboBox
                            .getSelectedItem()
                            .toString()
            );

            return flow;

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cell ID and packet count must be whole numbers. Data volume and flow duration must be numbers.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }
    }

    private void addTrafficFlow() {

        TrafficFlow flow =
                getFlowFromFields(false);

        if (flow == null) {
            return;
        }

        if (trafficFlowDAO.addTrafficFlow(flow)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Traffic flow added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadTrafficFlows();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add traffic flow.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateTrafficFlow() {

        TrafficFlow flow =
                getFlowFromFields(true);

        if (flow == null) {
            return;
        }

        if (trafficFlowDAO.updateTrafficFlow(flow)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Traffic flow updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadTrafficFlows();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update traffic flow.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteTrafficFlow() {

        if (flowIdField
                .getText()
                .trim()
                .isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a traffic flow to delete.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int flowId;

        try {

            flowId =
                    Integer.parseInt(
                            flowIdField
                                    .getText()
                                    .trim()
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Flow ID.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete Flow ID "
                                + flowId
                                + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation !=
                JOptionPane.YES_OPTION) {

            return;
        }

        if (trafficFlowDAO.deleteTrafficFlow(flowId)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Traffic flow deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadTrafficFlows();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete traffic flow.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFields() {

        flowIdField.setText("");
        cellIdField.setText("");
        sourceIpField.setText("");
        destinationIpField.setText("");
        dataVolumeField.setText("");
        flowDurationField.setText("");
        packetCountField.setText("");

        protocolComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);

        flowTable.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                TrafficFlowManagementScreen::new
        );
    }
}