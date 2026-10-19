package com.telecom.app;

import com.telecom.dao.SubscriberDAO;
import com.telecom.model.Subscriber;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SubscriberManagementScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField monthlyLimitField;
    private JTextField currentUsageField;

    private JComboBox<String> subscriptionTypeBox;
    private JComboBox<String> dataPlanBox;
    private JComboBox<String> statusBox;

    private SubscriberDAO subscriberDAO;

    public SubscriberManagementScreen() {

        subscriberDAO = new SubscriberDAO();

        setTitle("Subscriber Management");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createInterface();
        loadSubscribers();
    }

    private void createInterface() {

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel =
                new JPanel(new GridLayout(8, 2, 8, 8));

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Subscriber Details"));

        nameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();
        monthlyLimitField = new JTextField();
        currentUsageField = new JTextField();

        subscriptionTypeBox =
                new JComboBox<>(
                        new String[]{
                                "Prepaid",
                                "Postpaid"
                        });

        dataPlanBox =
                new JComboBox<>(
                        new String[]{
                                "4G Basic",
                                "4G Standard",
                                "5G Basic",
                                "5G Standard",
                                "5G Premium"
                        });

        statusBox =
                new JComboBox<>(
                        new String[]{
                                "Active",
                                "Inactive",
                                "Suspended"
                        });

        formPanel.add(new JLabel("Subscriber Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Subscription Type:"));
        formPanel.add(subscriptionTypeBox);

        formPanel.add(new JLabel("Data Plan:"));
        formPanel.add(dataPlanBox);

        formPanel.add(new JLabel("Monthly Data Limit:"));
        formPanel.add(monthlyLimitField);

        formPanel.add(new JLabel("Current Data Usage:"));
        formPanel.add(currentUsageField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusBox);

        add(formPanel, BorderLayout.NORTH);

        tableModel =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Name",
                                "Phone",
                                "Email",
                                "Subscription",
                                "Data Plan",
                                "Monthly Limit",
                                "Current Usage",
                                "Status"
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
                addSubscriber());

        updateButton.addActionListener(e ->
                updateSubscriber());

        deleteButton.addActionListener(e ->
                deleteSubscriber());

        refreshButton.addActionListener(e ->
                loadSubscribers());

        closeButton.addActionListener(e ->
                dispose());

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        loadSelectedSubscriber();
                    }
                });
    }

    private void loadSubscribers() {

        tableModel.setRowCount(0);

        List<Subscriber> subscribers =
                subscriberDAO.getAllSubscribers();

        for (Subscriber subscriber : subscribers) {

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
                    });
        }
    }

    private void loadSelectedSubscriber() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        nameField.setText(
                tableModel.getValueAt(
                        selectedRow, 1).toString());

        phoneField.setText(
                tableModel.getValueAt(
                        selectedRow, 2).toString());

        emailField.setText(
                tableModel.getValueAt(
                        selectedRow, 3).toString());

        subscriptionTypeBox.setSelectedItem(
                tableModel.getValueAt(
                        selectedRow, 4).toString());

        dataPlanBox.setSelectedItem(
                tableModel.getValueAt(
                        selectedRow, 5).toString());

        monthlyLimitField.setText(
                tableModel.getValueAt(
                        selectedRow, 6).toString());

        currentUsageField.setText(
                tableModel.getValueAt(
                        selectedRow, 7).toString());

        statusBox.setSelectedItem(
                tableModel.getValueAt(
                        selectedRow, 8).toString());
    }

    private Subscriber getSubscriberFromForm(
            int subscriberId) {

        return new Subscriber(
                subscriberId,
                nameField.getText().trim(),
                phoneField.getText().trim(),
                emailField.getText().trim(),
                subscriptionTypeBox
                        .getSelectedItem()
                        .toString(),
                dataPlanBox
                        .getSelectedItem()
                        .toString(),
                Double.parseDouble(
                        monthlyLimitField
                                .getText()
                                .trim()),
                Double.parseDouble(
                        currentUsageField
                                .getText()
                                .trim()),
                statusBox
                        .getSelectedItem()
                        .toString()
        );
    }

    private void addSubscriber() {

        try {

            Subscriber subscriber =
                    getSubscriberFromForm(0);

            if (subscriberDAO.addSubscriber(
                    subscriber)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Subscriber added successfully.");

                clearFields();
                loadSubscribers();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add subscriber.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Monthly data limit and current " +
                    "data usage must be valid numbers.");
        }
    }

    private void updateSubscriber() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a subscriber.");

            return;
        }

        try {

            int subscriberId =
                    Integer.parseInt(
                            tableModel.getValueAt(
                                    selectedRow, 0)
                                    .toString());

            Subscriber subscriber =
                    getSubscriberFromForm(
                            subscriberId);

            if (subscriberDAO.updateSubscriber(
                    subscriber)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Subscriber updated successfully.");

                clearFields();
                loadSubscribers();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update subscriber.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Monthly data limit and current " +
                    "data usage must be valid numbers.");
        }
    }

    private void deleteSubscriber() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a subscriber.");

            return;
        }

        int subscriberId =
                Integer.parseInt(
                        tableModel.getValueAt(
                                selectedRow, 0)
                                .toString());

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete selected subscriber?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        if (subscriberDAO.deleteSubscriber(
                subscriberId)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Subscriber deleted successfully.");

            clearFields();
            loadSubscribers();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete subscriber.");
        }
    }

    private void clearFields() {

        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        monthlyLimitField.setText("");
        currentUsageField.setText("");

        subscriptionTypeBox.setSelectedIndex(0);
        dataPlanBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);

        table.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            SubscriberManagementScreen screen =
                    new SubscriberManagementScreen();

            screen.setVisible(true);
        });
    }
}