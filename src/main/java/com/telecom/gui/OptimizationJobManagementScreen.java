package com.telecom.gui;

import com.telecom.dao.OptimizationJobDAO;
import com.telecom.model.OptimizationJob;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OptimizationJobManagementScreen extends JFrame {

    private JTextField txtJobId;
    private JTextField txtJobName;
    private JTextField txtTargetCellId;
    private JTextField txtObjective;
    private JTextField txtInputLoad;

    private JComboBox<String> cmbOptimizationType;
    private JComboBox<String> cmbStatus;

    private JTable jobTable;
    private DefaultTableModel tableModel;

    private OptimizationJobDAO jobDAO;

    public OptimizationJobManagementScreen() {

        jobDAO = new OptimizationJobDAO();

        setTitle("Optimization Job Management");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createUI();

        loadJobs();
    }

    private void createUI() {

        setLayout(new BorderLayout(10, 10));

        // =========================
        // FORM PANEL
        // =========================

        JPanel formPanel =
                new JPanel(new GridLayout(4, 4, 10, 10));

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Optimization Job Details"
                )
        );

        // Job ID
        formPanel.add(new JLabel("Job ID"));

        txtJobId = new JTextField();
        txtJobId.setEditable(false);

        formPanel.add(txtJobId);

        // Job Name
        formPanel.add(new JLabel("Job Name"));

        txtJobName = new JTextField();

        formPanel.add(txtJobName);

        // Optimization Type
        formPanel.add(new JLabel("Optimization Type"));

        cmbOptimizationType =
                new JComboBox<>(
                        new String[]{
                                "Load Balancing",
                                "Resource Allocation",
                                "Traffic Optimization",
                                "Network Slice Optimization"
                        }
                );

        formPanel.add(cmbOptimizationType);

        // Target Cell ID
        formPanel.add(new JLabel("Target Cell ID"));

        txtTargetCellId = new JTextField();

        formPanel.add(txtTargetCellId);

        // Objective
        formPanel.add(new JLabel("Objective"));

        txtObjective = new JTextField();

        formPanel.add(txtObjective);

        // Input Load
        formPanel.add(new JLabel("Input Load"));

        txtInputLoad = new JTextField();

        formPanel.add(txtInputLoad);

        // Status
        formPanel.add(new JLabel("Status"));

        cmbStatus =
                new JComboBox<>(
                        new String[]{
                                "Pending",
                                "Running",
                                "Completed",
                                "Failed"
                        }
                );

        formPanel.add(cmbStatus);

        // Empty cell
        formPanel.add(new JLabel(""));

        formPanel.add(new JLabel(""));

        add(formPanel, BorderLayout.NORTH);

        // =========================
        // TABLE
        // =========================

        tableModel =
                new DefaultTableModel(
                        new String[]{
                                "Job ID",
                                "Job Name",
                                "Optimization Type",
                                "Target Cell ID",
                                "Objective",
                                "Input Load",
                                "Status",
                                "Created Time"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        jobTable = new JTable(tableModel);

        jobTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        jobTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        JScrollPane scrollPane =
                new JScrollPane(jobTable);

        add(scrollPane, BorderLayout.CENTER);

        // =========================
        // BUTTON PANEL
        // =========================

        JPanel buttonPanel =
                new JPanel(new FlowLayout());

        JButton btnAdd =
                new JButton("Add");

        JButton btnUpdate =
                new JButton("Update");

        JButton btnDelete =
                new JButton("Delete");

        JButton btnRefresh =
                new JButton("Refresh");

        JButton btnClear =
                new JButton("Clear");

        JButton btnClose =
                new JButton("Close");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnClose);

        add(buttonPanel, BorderLayout.SOUTH);

        // =========================
        // BUTTON ACTIONS
        // =========================

        btnAdd.addActionListener(
                e -> addJob()
        );

        btnUpdate.addActionListener(
                e -> updateJob()
        );

        btnDelete.addActionListener(
                e -> deleteJob()
        );

        btnRefresh.addActionListener(
                e -> loadJobs()
        );

        btnClear.addActionListener(
                e -> clearForm()
        );

        btnClose.addActionListener(
                e -> dispose()
        );

        // =========================
        // TABLE ROW CLICK
        // =========================

        jobTable.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e) {

                        int selectedRow =
                                jobTable.getSelectedRow();

                        if (selectedRow >= 0) {

                            txtJobId.setText(
                                    tableModel.getValueAt(
                                            selectedRow,
                                            0
                                    ).toString()
                            );

                            txtJobName.setText(
                                    tableModel.getValueAt(
                                            selectedRow,
                                            1
                                    ).toString()
                            );

                            cmbOptimizationType.setSelectedItem(
                                    tableModel.getValueAt(
                                            selectedRow,
                                            2
                                    ).toString()
                            );

                            txtTargetCellId.setText(
                                    tableModel.getValueAt(
                                            selectedRow,
                                            3
                                    ).toString()
                            );

                            txtObjective.setText(
                                    tableModel.getValueAt(
                                            selectedRow,
                                            4
                                    ).toString()
                            );

                            txtInputLoad.setText(
                                    tableModel.getValueAt(
                                            selectedRow,
                                            5
                                    ).toString()
                            );

                            cmbStatus.setSelectedItem(
                                    tableModel.getValueAt(
                                            selectedRow,
                                            6
                                    ).toString()
                            );
                        }
                    }
                }
        );
    }

    // =========================
    // LOAD JOBS
    // =========================

    private void loadJobs() {

        tableModel.setRowCount(0);

        List<OptimizationJob> jobs =
                jobDAO.getAllOptimizationJobs();

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
                    }
            );
        }
    }

    // =========================
    // ADD JOB
    // =========================

    private void addJob() {

        if (!validateInput()) {
            return;
        }

        OptimizationJob job =
                new OptimizationJob();

        job.setJobName(
                txtJobName.getText().trim()
        );

        job.setOptimizationType(
                cmbOptimizationType
                        .getSelectedItem()
                        .toString()
        );

        job.setTargetCellId(
                Integer.parseInt(
                        txtTargetCellId
                                .getText()
                                .trim()
                )
        );

        job.setObjective(
                txtObjective.getText().trim()
        );

        job.setInputLoad(
                Double.parseDouble(
                        txtInputLoad
                                .getText()
                                .trim()
                )
        );

        job.setStatus(
                cmbStatus
                        .getSelectedItem()
                        .toString()
        );

        boolean success =
                jobDAO.addOptimizationJob(job);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Optimization job added successfully."
            );

            clearForm();
            loadJobs();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add optimization job.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // UPDATE JOB
    // =========================

    private void updateJob() {

        if (txtJobId.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a job from the table."
            );

            return;
        }

        if (!validateInput()) {
            return;
        }

        OptimizationJob job =
                new OptimizationJob();

        job.setJobId(
                Integer.parseInt(
                        txtJobId
                                .getText()
                                .trim()
                )
        );

        job.setJobName(
                txtJobName.getText().trim()
        );

        job.setOptimizationType(
                cmbOptimizationType
                        .getSelectedItem()
                        .toString()
        );

        job.setTargetCellId(
                Integer.parseInt(
                        txtTargetCellId
                                .getText()
                                .trim()
                )
        );

        job.setObjective(
                txtObjective.getText().trim()
        );

        job.setInputLoad(
                Double.parseDouble(
                        txtInputLoad
                                .getText()
                                .trim()
                )
        );

        job.setStatus(
                cmbStatus
                        .getSelectedItem()
                        .toString()
        );

        boolean success =
                jobDAO.updateOptimizationJob(job);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Optimization job updated successfully."
            );

            clearForm();
            loadJobs();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update optimization job.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // DELETE JOB
    // =========================

    private void deleteJob() {

        if (txtJobId.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a job from the table."
            );

            return;
        }

        int jobId =
                Integer.parseInt(
                        txtJobId
                                .getText()
                                .trim()
                );

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this optimization job?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success =
                jobDAO.deleteOptimizationJob(jobId);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Optimization job deleted successfully."
            );

            clearForm();
            loadJobs();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete optimization job.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================
    // VALIDATION
    // =========================

    private boolean validateInput() {

        String jobName =
                txtJobName.getText().trim();

        String targetCellText =
                txtTargetCellId
                        .getText()
                        .trim();

        String objective =
                txtObjective.getText().trim();

        String inputLoadText =
                txtInputLoad
                        .getText()
                        .trim();

        if (jobName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Job name cannot be empty."
            );

            return false;
        }

        if (targetCellText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Target Cell ID cannot be empty."
            );

            return false;
        }

        if (objective.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Objective cannot be empty."
            );

            return false;
        }

        if (inputLoadText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Input Load cannot be empty."
            );

            return false;
        }

        try {

            int targetCellId =
                    Integer.parseInt(
                            targetCellText
                    );

            if (targetCellId <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Target Cell ID must be greater than 0."
                );

                return false;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Target Cell ID must be a valid integer."
            );

            return false;
        }

        try {

            double inputLoad =
                    Double.parseDouble(
                            inputLoadText
                    );

            if (inputLoad < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Input Load cannot be negative."
                );

                return false;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Input Load must be a valid number."
            );

            return false;
        }

        return true;
    }

    // =========================
    // CLEAR FORM
    // =========================

    private void clearForm() {

        txtJobId.setText("");
        txtJobName.setText("");
        txtTargetCellId.setText("");
        txtObjective.setText("");
        txtInputLoad.setText("");

        cmbOptimizationType.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);

        jobTable.clearSelection();
    }

    // =========================
    // MAIN METHOD
    // =========================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            OptimizationJobManagementScreen screen =
                    new OptimizationJobManagementScreen();

            screen.setVisible(true);
        });
    }
}