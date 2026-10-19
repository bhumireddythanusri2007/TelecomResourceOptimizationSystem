package com.telecom.app;

import com.telecom.dao.UserDAO;
import com.telecom.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementScreen extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public UserManagementScreen() {

        setTitle("User Management");

        setSize(1100, 600);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        JPanel mainPanel =
                new JPanel(new BorderLayout(10, 10));

        JLabel titleLabel =
                new JLabel(
                        "User Management",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        mainPanel.add(
                titleLabel,
                BorderLayout.NORTH
        );

        String[] columns = {
                "User ID",
                "Username",
                "Email",
                "Password",
                "Role",
                "Status",
                "Created At"
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

        addButton.addActionListener(e -> {
            addUser();
        });

        updateButton.addActionListener(e -> {
            updateUser();
        });

        deleteButton.addActionListener(e -> {
            deleteUser();
        });

        refreshButton.addActionListener(e -> {
            loadUsers();
        });

        closeButton.addActionListener(e -> {
            dispose();
        });

        add(mainPanel);

        loadUsers();
    }

    private void loadUsers() {

        tableModel.setRowCount(0);

        UserDAO dao =
                new UserDAO();

        List<User> users =
                dao.getAllUsers();

        for (User user : users) {

            Object[] row = {
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole(),
                    user.getStatus(),
                    user.getCreatedAt()
            };

            tableModel.addRow(row);
        }
    }

    private void addUser() {

        JTextField usernameField =
                new JTextField();

        JTextField emailField =
                new JTextField();

        JPasswordField passwordField =
                new JPasswordField();

        JTextField roleField =
                new JTextField();

        JTextField statusField =
                new JTextField("Active");

        JPanel panel =
                new JPanel(new GridLayout(0, 2, 5, 5));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        panel.add(roleField);

        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Add User",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String username =
                usernameField.getText().trim();

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                ).trim();

        String role =
                roleField.getText().trim();

        String status =
                statusField.getText().trim();

        if (username.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                || role.isEmpty()
                || status.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "All fields are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        User user =
                new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setStatus(status);

        UserDAO dao =
                new UserDAO();

        boolean saved =
                dao.addUser(user);

        if (saved) {

            JOptionPane.showMessageDialog(
                    this,
                    "User added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadUsers();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to add user.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateUser() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a user to update.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int userId =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        selectedRow,
                                        0
                                )
                                .toString()
                );

        String currentUsername =
                tableModel
                        .getValueAt(
                                selectedRow,
                                1
                        )
                        .toString();

        String currentEmail =
                tableModel
                        .getValueAt(
                                selectedRow,
                                2
                        )
                        .toString();

        String currentPassword =
                tableModel
                        .getValueAt(
                                selectedRow,
                                3
                        )
                        .toString();

        String currentRole =
                tableModel
                        .getValueAt(
                                selectedRow,
                                4
                        )
                        .toString();

        String currentStatus =
                tableModel
                        .getValueAt(
                                selectedRow,
                                5
                        )
                        .toString();

        JTextField usernameField =
                new JTextField(currentUsername);

        JTextField emailField =
                new JTextField(currentEmail);

        JPasswordField passwordField =
                new JPasswordField(currentPassword);

        JTextField roleField =
                new JTextField(currentRole);

        JTextField statusField =
                new JTextField(currentStatus);

        JPanel panel =
                new JPanel(new GridLayout(0, 2, 5, 5));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        panel.add(roleField);

        panel.add(new JLabel("Status:"));
        panel.add(statusField);

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Update User",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String username =
                usernameField.getText().trim();

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                ).trim();

        String role =
                roleField.getText().trim();

        String status =
                statusField.getText().trim();

        if (username.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                || role.isEmpty()
                || status.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "All fields are required.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        User user =
                new User();

        user.setUserId(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        user.setStatus(status);

        UserDAO dao =
                new UserDAO();

        boolean updated =
                dao.updateUser(user);

        if (updated) {

            JOptionPane.showMessageDialog(
                    this,
                    "User updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadUsers();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to update user.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteUser() {

        int selectedRow =
                table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a user to delete.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int userId =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        selectedRow,
                                        0
                                )
                                .toString()
                );

        String username =
                tableModel
                        .getValueAt(
                                selectedRow,
                                1
                        )
                        .toString();

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete user: " + username + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        UserDAO dao =
                new UserDAO();

        boolean deleted =
                dao.deleteUser(userId);

        if (deleted) {

            JOptionPane.showMessageDialog(
                    this,
                    "User deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadUsers();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete user.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            UserManagementScreen screen =
                    new UserManagementScreen();

            screen.setVisible(true);
        });
    }
}