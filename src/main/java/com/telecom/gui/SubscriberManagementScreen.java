package com.telecom.gui;

import com.telecom.dao.SubscriberDAO;
import com.telecom.model.Subscriber;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SubscriberManagementScreen extends JFrame {

    private JTextField subscriberIdField;
    private JTextField subscriberNameField;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JTextField subscriptionTypeField;
    private JTextField dataPlanField;
    private JTextField monthlyDataLimitField;
    private JTextField currentDataUsageField;

    private JComboBox<String> statusComboBox;

    private JTable subscriberTable;
    private DefaultTableModel tableModel;

    private SubscriberDAO subscriberDAO;

    public SubscriberManagementScreen() {

        subscriberDAO = new SubscriberDAO();

        setTitle("Subscriber Management");
        setSize(1100, 700);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        createUI();

        loadSubscribers();

        setVisible(true);
    }

    private void createUI() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        JLabel titleLabel =
                new JLabel(
                        "Subscriber Management",
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
                        new GridLayout(9, 2, 10, 10)
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        subscriberIdField =
                new JTextField();

        subscriberNameField =
                new JTextField();

        phoneNumberField =
                new JTextField();

        emailField =
                new JTextField();

        subscriptionTypeField =
                new JTextField();

        dataPlanField =
                new JTextField();

        monthlyDataLimitField =
                new JTextField();

        currentDataUsageField =
                new JTextField();

        statusComboBox =
                new JComboBox<>(
                        new String[]{
                                "Active",
                                "Inactive",
                                "Suspended"
                        }
                );

        subscriberIdField.setEditable(false);

        formPanel.add(
                new JLabel("Subscriber ID:")
        );

        formPanel.add(
                subscriberIdField
        );

        formPanel.add(
                new JLabel("Subscriber Name:")
        );

        formPanel.add(
                subscriberNameField
        );

        formPanel.add(
                new JLabel("Phone Number:")
        );

        formPanel.add(
                phoneNumberField
        );

        formPanel.add(
                new JLabel("Email:")
        );

        formPanel.add(
                emailField
        );

        formPanel.add(
                new JLabel("Subscription Type:")
        );

        formPanel.add(
                subscriptionTypeField
        );

        formPanel.add(
                new JLabel("Data Plan:")
        );

        formPanel.add(
                dataPlanField
        );

        formPanel.add(
                new JLabel("Monthly Data Limit:")
        );

        formPanel.add(
                monthlyDataLimitField
        );

        formPanel.add(
                new JLabel("Current Data Usage:")
        );

        formPanel.add(
                currentDataUsageField
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
                "Subscriber ID",
                "Subscriber Name",
                "Phone Number",
                "Email",
                "Subscription Type",
                "Data Plan",
                "Monthly Limit",
                "Current Usage",
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

        subscriberTable =
                new JTable(tableModel);

        subscriberTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        subscriberTable
                );

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
                e -> addSubscriber()
        );

        updateButton.addActionListener(
                e -> updateSubscriber()
        );

        deleteButton.addActionListener(
                e -> deleteSubscriber()
        );

        refreshButton.addActionListener(
                e -> loadSubscribers()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        subscriberTable.getSelectionModel()
                .addListSelectionListener(
                        e -> loadSelectedSubscriber()
                );

        add(mainPanel);
    }

    private void loadSubscribers() {

        tableModel.setRowCount(0);

        try {

            for (
                    Subscriber subscriber :
                    subscriberDAO.getAllSubscribers()
            ) {

                tableModel.addRow(
                        new Object[]{
                                subscriber.getSubscriberId(),
                                subscriber.getSubscriberName(),
                                subscriber.getPhoneNumber(),
                                subscriber.getEmail(),
                                subscriber.getSubscriptionType(),
                                subscriber.getDataPlan(),
                                subscriber.getMonthlyDataLimit(),
                                subscriber.getCurrentDataUsage(),
                                subscriber.getStatus()
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error loading subscribers.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    private void loadSelectedSubscriber() {

        int selectedRow =
                subscriberTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        subscriberIdField.setText(
                tableModel
                        .getValueAt(selectedRow, 0)
                        .toString()
        );

        subscriberNameField.setText(
                tableModel
                        .getValueAt(selectedRow, 1)
                        .toString()
        );

        phoneNumberField.setText(
                tableModel
                        .getValueAt(selectedRow, 2)
                        .toString()
        );

        emailField.setText(
                tableModel
                        .getValueAt(selectedRow, 3)
                        .toString()
        );

        subscriptionTypeField.setText(
                tableModel
                        .getValueAt(selectedRow, 4)
                        .toString()
        );

        dataPlanField.setText(
                tableModel
                        .getValueAt(selectedRow, 5)
                        .toString()
        );

        monthlyDataLimitField.setText(
                tableModel
                        .getValueAt(selectedRow, 6)
                        .toString()
        );

        currentDataUsageField.setText(
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

    private Subscriber getSubscriberFromFields(
            boolean includeId) {

        try {

            Subscriber subscriber =
                    new Subscriber();

            if (includeId) {

                if (subscriberIdField
                        .getText()
                        .trim()
                        .isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Please select a subscriber.",
                            "Validation",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return null;
                }

                subscriber.setSubscriberId(
                        Integer.parseInt(
                                subscriberIdField
                                        .getText()
                                        .trim()
                        )
                );
            }

            String subscriberName =
                    subscriberNameField
                            .getText()
                            .trim();

            String phoneNumber =
                    phoneNumberField
                            .getText()
                            .trim();

            String email =
                    emailField
                            .getText()
                            .trim();

            String subscriptionType =
                    subscriptionTypeField
                            .getText()
                            .trim();

            String dataPlan =
                    dataPlanField
                            .getText()
                            .trim();

            String monthlyLimitText =
                    monthlyDataLimitField
                            .getText()
                            .trim();

            String currentUsageText =
                    currentDataUsageField
                            .getText()
                            .trim();

            if (subscriberName.isEmpty()
                    || phoneNumber.isEmpty()
                    || email.isEmpty()
                    || subscriptionType.isEmpty()
                    || dataPlan.isEmpty()
                    || monthlyLimitText.isEmpty()
                    || currentUsageText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            double monthlyDataLimit =
                    Double.parseDouble(
                            monthlyLimitText
                    );

            double currentDataUsage =
                    Double.parseDouble(
                            currentUsageText
                    );

            if (monthlyDataLimit <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Monthly data limit must be greater than 0.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            if (currentDataUsage < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Current data usage cannot be negative.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            if (currentDataUsage > monthlyDataLimit) {

                JOptionPane.showMessageDialog(
                        this,
                        "Current data usage cannot exceed monthly data limit.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE
                );

                return null;
            }

            subscriber.setSubscriberName(
                    subscriberName
            );

            subscriber.setPhoneNumber(
                    phoneNumber
            );

            subscriber.setEmail(
                    email
            );

            subscriber.setSubscriptionType(
                    subscriptionType
            );

            subscriber.setDataPlan(
                    dataPlan
            );

            subscriber.setMonthlyDataLimit(
                    monthlyDataLimit
            );

            subscriber.setCurrentDataUsage(
                    currentDataUsage
            );

            subscriber.setStatus(
                    statusComboBox
                            .getSelectedItem()
                            .toString()
            );

            return subscriber;

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Monthly data limit and current data usage must be numbers.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }
    }

    private void addSubscriber() {

        Subscriber subscriber =
                getSubscriberFromFields(false);

        if (subscriber == null) {
            return;
        }

        if (subscriberDAO.addSubscriber(subscriber)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Subscriber added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadSubscribers();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add subscriber.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateSubscriber() {

        Subscriber subscriber =
                getSubscriberFromFields(true);

        if (subscriber == null) {
            return;
        }

        if (subscriberDAO.updateSubscriber(subscriber)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Subscriber updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadSubscribers();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update subscriber.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteSubscriber() {

        if (subscriberIdField
                .getText()
                .trim()
                .isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a subscriber to delete.",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int subscriberId;

        try {

            subscriberId =
                    Integer.parseInt(
                            subscriberIdField
                                    .getText()
                                    .trim()
                    );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Subscriber ID.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete Subscriber ID "
                                + subscriberId
                                + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation !=
                JOptionPane.YES_OPTION) {

            return;
        }

        if (subscriberDAO.deleteSubscriber(
                subscriberId)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Subscriber deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadSubscribers();

            clearFields();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete subscriber.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFields() {

        subscriberIdField.setText("");

        subscriberNameField.setText("");

        phoneNumberField.setText("");

        emailField.setText("");

        subscriptionTypeField.setText("");

        dataPlanField.setText("");

        monthlyDataLimitField.setText("");

        currentDataUsageField.setText("");

        statusComboBox.setSelectedIndex(0);

        subscriberTable.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                SubscriberManagementScreen::new
        );
    }
}