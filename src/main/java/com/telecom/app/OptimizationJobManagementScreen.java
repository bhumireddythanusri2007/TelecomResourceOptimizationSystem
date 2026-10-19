package com.telecom.app;

import com.telecom.dao.OptimizationJobDAO;
import com.telecom.model.OptimizationJob;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OptimizationJobManagementScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField jobNameField;
    private JTextField cellIdField;
    private JTextField objectiveField;
    private JTextField inputLoadField;

    private JComboBox<String> optimizationTypeBox;
    private JComboBox<String> statusBox;

    private OptimizationJobDAO optimizationJobDAO;

    public OptimizationJobManagementScreen() {

        optimizationJobDAO =
                new OptimizationJobDAO();

        setTitle("Optimization Job Management");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE);

        createInterface();
        loadOptimizationJobs();
    }

    private void createInterface() {

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel =
                new JPanel(
                        new GridLayout(6, 2, 8, 8));

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Optimization Job Details"));

        jobNameField = new JTextField();
        cellIdField = new JTextField();
        objectiveField = new JTextField();
        inputLoadField = new JTextField();

        optimizationTypeBox =
                new JComboBox<>(
                        new String[]{
                                "Load Balancing",
                                "Bandwidth Allocation",
                                "Resource Allocation"
                        });

        statusBox =
                new JComboBox<>(
                        new String[]{
                                "Pending",
                                "Running",
                                "Completed"
                        });

        formPanel.add(
                new JLabel("Job Name:"));
        formPanel.add(jobNameField);

        formPanel.add(
                new JLabel("Optimization Type:"));
        formPanel.add(optimizationTypeBox);

        formPanel.add(
                new JLabel("Target Cell ID:"));
        formPanel.add(cellIdField);

        formPanel.add(
                new JLabel("Objective:"));
        formPanel.add(objectiveField);

        formPanel.add(
                new JLabel("Input Load:"));
        formPanel.add(inputLoadField);

        formPanel.add(
                new JLabel("Status:"));
        formPanel.add(statusBox);

        add(formPanel, BorderLayout.NORTH);

        tableModel =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Job Name",
                                "Optimization Type",
                                "Cell ID",
                                "Objective",
                                "Input Load",
                                "Status",
                                "Created Time"
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
                addOptimizationJob());

        updateButton.addActionListener(e ->
                updateOptimizationJob());

        deleteButton.addActionListener(e ->
                deleteOptimizationJob());

        refreshButton.addActionListener(e ->
                loadOptimizationJobs());

        closeButton.addActionListener(e ->
                dispose());

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        loadSelectedJob();
                    }
                });
    }

    private void loadOptimizationJobs() {

        tableModel.setRowCount(0);

        List<OptimizationJob> jobs =
                optimizationJobDAO
                        .getAllOptimizationJobs();

        for (OptimizationJob job : jobs) {

            tableModel.addRow(
                    new Object[]{
                            job.getJobId(),
                            job.getJobName(),
                            job.getOptimizationType(),
                            job.getTargetCellId(),
                            job.getObjective(),
                            job.getInputLoad(),
                            job.getStatus(),
                            job.getCreatedTime()
                    });
        }
    }

    private void loadSelectedJob() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        jobNameField.setText(
                tableModel.getValueAt(
                        selectedRow, 1).toString());

        optimizationTypeBox.setSelectedItem(
                tableModel.getValueAt(
                        selectedRow, 2).toString());

        cellIdField.setText(
                tableModel.getValueAt(
                        selectedRow, 3).toString());

        objectiveField.setText(
                tableModel.getValueAt(
                        selectedRow, 4).toString());

        inputLoadField.setText(
                tableModel.getValueAt(
                        selectedRow, 5).toString());

        statusBox.setSelectedItem(
                tableModel.getValueAt(
                        selectedRow, 6).toString());
    }

    private OptimizationJob getJobFromForm(
            int jobId) {

        return new OptimizationJob(
                jobId,
                jobNameField.getText().trim(),
                optimizationTypeBox
                        .getSelectedItem()
                        .toString(),
                Integer.parseInt(
                        cellIdField.getText().trim()),
                objectiveField.getText().trim(),
                Double.parseDouble(
                        inputLoadField.getText().trim()),
                statusBox
                        .getSelectedItem()
                        .toString(),
                ""
        );
    }

    private void addOptimizationJob() {

        try {

            OptimizationJob job =
                    getJobFromForm(0);

            if (optimizationJobDAO
                    .addOptimizationJob(job)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Optimization job added successfully.");

                clearFields();
                loadOptimizationJobs();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add optimization job.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Target Cell ID and Input Load " +
                    "must contain valid numbers.");
        }
    }

    private void updateOptimizationJob() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an optimization job.");

            return;
        }

        try {

            int jobId =
                    Integer.parseInt(
                            tableModel.getValueAt(
                                    selectedRow, 0)
                                    .toString());

            OptimizationJob job =
                    getJobFromForm(jobId);

            if (optimizationJobDAO
                    .updateOptimizationJob(job)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Optimization job updated successfully.");

                clearFields();
                loadOptimizationJobs();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update optimization job.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Target Cell ID and Input Load " +
                    "must contain valid numbers.");
        }
    }

    private void deleteOptimizationJob() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an optimization job.");

            return;
        }

        int jobId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                selectedRow, 0)
                                .toString());

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete selected optimization job?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        if (optimizationJobDAO
                .deleteOptimizationJob(jobId)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Optimization job deleted successfully.");

            clearFields();
            loadOptimizationJobs();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete optimization job.");
        }
    }

    private void clearFields() {

        jobNameField.setText("");
        cellIdField.setText("");
        objectiveField.setText("");
        inputLoadField.setText("");

        optimizationTypeBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);

        table.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            OptimizationJobManagementScreen screen =
                    new OptimizationJobManagementScreen();

            screen.setVisible(true);
        });
    }
}