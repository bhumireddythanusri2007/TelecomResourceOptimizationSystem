package com.telecom.gui;

import com.telecom.dao.OptimizationResultDAO;
import com.telecom.model.OptimizationResult;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OptimizationResultManagementScreen extends JFrame {

    private JTextField resultIdField;
    private JTextField cellIdField;
    private JTextField previousLoadField;
    private JTextField optimizedLoadField;
    private JTextField bandwidthField;
    private JTextField performanceScoreField;

    private JTable resultTable;
    private DefaultTableModel tableModel;

    private OptimizationResultDAO resultDAO;


    public OptimizationResultManagementScreen() {

        resultDAO = new OptimizationResultDAO();

        setTitle("Optimization Results Management");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeUI();
        loadResults();
    }


    private void initializeUI() {

        setLayout(new BorderLayout(10, 10));


        // ---------------- FORM PANEL ----------------

        JPanel formPanel =
                new JPanel(new GridLayout(3, 4, 10, 10));

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 10, 10, 10
                )
        );


        formPanel.add(new JLabel("Result ID"));

        resultIdField = new JTextField();
        resultIdField.setEditable(false);

        formPanel.add(resultIdField);


        formPanel.add(new JLabel("Cell ID"));

        cellIdField = new JTextField();

        formPanel.add(cellIdField);


        formPanel.add(new JLabel("Previous Load"));

        previousLoadField = new JTextField();

        formPanel.add(previousLoadField);


        formPanel.add(new JLabel("Optimized Load"));

        optimizedLoadField = new JTextField();

        formPanel.add(optimizedLoadField);


        formPanel.add(new JLabel("Allocated Bandwidth"));

        bandwidthField = new JTextField();

        formPanel.add(bandwidthField);


        formPanel.add(new JLabel("Performance Score"));

        performanceScoreField = new JTextField();

        formPanel.add(performanceScoreField);


        add(formPanel, BorderLayout.NORTH);


        // ---------------- TABLE ----------------

        String[] columns = {
                "Result ID",
                "Cell ID",
                "Previous Load",
                "Optimized Load",
                "Allocated Bandwidth",
                "Performance Score",
                "Optimization Time"
        };


        tableModel =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };


        resultTable = new JTable(tableModel);


        resultTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );


        // Table column widths
        resultTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        resultTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(80);

        resultTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(70);

        resultTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(120);

        resultTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(120);

        resultTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(140);

        resultTable.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(130);

        resultTable.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(180);


        JScrollPane scrollPane =
                new JScrollPane(resultTable);


        add(scrollPane, BorderLayout.CENTER);


        // ---------------- BUTTON PANEL ----------------

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                10
                        )
                );


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


        add(buttonPanel, BorderLayout.SOUTH);


        // ---------------- BUTTON ACTIONS ----------------

        addButton.addActionListener(
                e -> addResult()
        );

        updateButton.addActionListener(
                e -> updateResult()
        );

        deleteButton.addActionListener(
                e -> deleteResult()
        );

        refreshButton.addActionListener(
                e -> loadResults()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        closeButton.addActionListener(
                e -> dispose()
        );


        // ---------------- TABLE SELECTION ----------------

        resultTable.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int selectedRow =
                                resultTable.getSelectedRow();

                        if (selectedRow >= 0) {

                            resultIdField.setText(
                                    tableModel.getValueAt(
                                            selectedRow,
                                            0
                                    ).toString()
                            );

                            cellIdField.setText(
                                    tableModel.getValueAt(
                                            selectedRow,
                                            1
                                    ).toString()
                            );

                            previousLoadField.setText(
                                    tableModel.getValueAt(
                                            selectedRow,
                                            2
                                    ).toString()
                            );

                            optimizedLoadField.setText(
                                    tableModel.getValueAt(
                                            selectedRow,
                                            3
                                    ).toString()
                            );

                            bandwidthField.setText(
                                    tableModel.getValueAt(
                                            selectedRow,
                                            4
                                    ).toString()
                            );

                            performanceScoreField.setText(
                                    tableModel.getValueAt(
                                            selectedRow,
                                            5
                                    ).toString()
                            );
                        }
                    }
                });
    }


    // ---------------- LOAD RESULTS ----------------

    private void loadResults() {

        tableModel.setRowCount(0);

        for (OptimizationResult result :
                resultDAO.getAllOptimizationResults()) {

            tableModel.addRow(
                    new Object[]{
                            result.getResultId(),
                            result.getCellId(),
                            result.getPreviousLoad(),
                            result.getOptimizedLoad(),
                            result.getAllocatedBandwidth(),
                            result.getPerformanceScore(),
                            result.getOptimizationTime()
                    }
            );
        }
    }


    // ---------------- ADD ----------------

    private void addResult() {

        try {

            int cellId =
                    Integer.parseInt(
                            cellIdField
                                    .getText()
                                    .trim()
                    );


            double previousLoad =
                    Double.parseDouble(
                            previousLoadField
                                    .getText()
                                    .trim()
                    );


            double optimizedLoad =
                    Double.parseDouble(
                            optimizedLoadField
                                    .getText()
                                    .trim()
                    );


            int bandwidth =
                    Integer.parseInt(
                            bandwidthField
                                    .getText()
                                    .trim()
                    );


            double performanceScore =
                    Double.parseDouble(
                            performanceScoreField
                                    .getText()
                                    .trim()
                    );


            if (cellId <= 0 ||
                    previousLoad < 0 ||
                    optimizedLoad < 0 ||
                    bandwidth < 0 ||
                    performanceScore < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid values."
                );

                return;
            }


            OptimizationResult result =
                    new OptimizationResult();


            result.setCellId(cellId);
            result.setPreviousLoad(previousLoad);
            result.setOptimizedLoad(optimizedLoad);
            result.setAllocatedBandwidth(bandwidth);
            result.setPerformanceScore(performanceScore);


            if (resultDAO.saveOptimizationResult(result)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Optimization result added successfully."
                );

                loadResults();
                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add optimization result."
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

    private void updateResult() {

        try {

            int resultId =
                    Integer.parseInt(
                            resultIdField
                                    .getText()
                                    .trim()
                    );


            int cellId =
                    Integer.parseInt(
                            cellIdField
                                    .getText()
                                    .trim()
                    );


            double previousLoad =
                    Double.parseDouble(
                            previousLoadField
                                    .getText()
                                    .trim()
                    );


            double optimizedLoad =
                    Double.parseDouble(
                            optimizedLoadField
                                    .getText()
                                    .trim()
                    );


            int bandwidth =
                    Integer.parseInt(
                            bandwidthField
                                    .getText()
                                    .trim()
                    );


            double performanceScore =
                    Double.parseDouble(
                            performanceScoreField
                                    .getText()
                                    .trim()
                    );


            if (resultId <= 0 ||
                    cellId <= 0 ||
                    previousLoad < 0 ||
                    optimizedLoad < 0 ||
                    bandwidth < 0 ||
                    performanceScore < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid values."
                );

                return;
            }


            OptimizationResult result =
                    new OptimizationResult();


            result.setResultId(resultId);
            result.setCellId(cellId);
            result.setPreviousLoad(previousLoad);
            result.setOptimizedLoad(optimizedLoad);
            result.setAllocatedBandwidth(bandwidth);
            result.setPerformanceScore(performanceScore);


            if (resultDAO.updateOptimizationResult(result)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Optimization result updated successfully."
                );

                loadResults();
                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update optimization result."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a valid record."
            );
        }
    }


    // ---------------- DELETE ----------------

    private void deleteResult() {

        try {

            int resultId =
                    Integer.parseInt(
                            resultIdField
                                    .getText()
                                    .trim()
                    );


            int choice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete this result?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );


            if (choice != JOptionPane.YES_OPTION) {

                return;
            }


            if (resultDAO.deleteOptimizationResult(
                    resultId)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Optimization result deleted successfully."
                );

                loadResults();
                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete optimization result."
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

        resultIdField.setText("");
        cellIdField.setText("");
        previousLoadField.setText("");
        optimizedLoadField.setText("");
        bandwidthField.setText("");
        performanceScoreField.setText("");

        resultTable.clearSelection();
    }


    // ---------------- MAIN ----------------

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            OptimizationResultManagementScreen screen =
                    new OptimizationResultManagementScreen();

            screen.setVisible(true);
        });
    }
}