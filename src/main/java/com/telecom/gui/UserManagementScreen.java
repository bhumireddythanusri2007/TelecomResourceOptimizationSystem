package com.telecom.gui;

import com.telecom.dao.UserDAO;
import com.telecom.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class UserManagementScreen extends JFrame {

    private JTextField txtUserId;
    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    private JComboBox<String> cmbRole;
    private JComboBox<String> cmbStatus;

    private JTable userTable;
    private DefaultTableModel tableModel;

    private UserDAO userDAO;

    public UserManagementScreen() {

        userDAO = new UserDAO();

        setTitle("User Management");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createUI();

        loadUsers();
    }

    private void createUI() {

        setLayout(new BorderLayout(10, 10));

        // =========================
        // FORM PANEL
        // =========================

        JPanel formPanel =
                new JPanel(new GridLayout(3, 4, 10, 10));

        formPanel.setBorder(
                BorderFactory.createTitledBorder("User Details"));

        // User ID
        formPanel.add(new JLabel("User ID"));

        txtUserId = new JTextField();
        txtUserId.setEditable(false);
        formPanel.add(txtUserId);

        // Username
        formPanel.add(new JLabel("Username"));

        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        // Email
        formPanel.add(new JLabel("Email"));

        txtEmail = new JTextField();
        formPanel.add(txtEmail);

        // Password
        formPanel.add(new JLabel("Password"));

        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        // Role
        formPanel.add(new JLabel("Role"));

        cmbRole = new JComboBox<>(
                new String[]{
                        "Admin",
                        "Network Engineer",
                        "Operator"
                });

        formPanel.add(cmbRole);

        // Status
        formPanel.add(new JLabel("Status"));

        cmbStatus = new JComboBox<>(
                new String[]{
                        "Active",
                        "Inactive"
                });

        formPanel.add(cmbStatus);

        add(formPanel, BorderLayout.NORTH);

        // =========================
        // TABLE
        // =========================

        tableModel = new DefaultTableModel(
                new String[]{
                        "User ID",
                        "Username",
                        "Email",
                        "Password",
                        "Role",
                        "Status",
                        "Created At"
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

        userTable = new JTable(tableModel);

        userTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane =
                new JScrollPane(userTable);

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

        btnAdd.addActionListener(e -> addUser());

        btnUpdate.addActionListener(e -> updateUser());

        btnDelete.addActionListener(e -> deleteUser());

        btnRefresh.addActionListener(e -> loadUsers());

        btnClear.addActionListener(e -> clearForm());

        btnClose.addActionListener(e -> dispose());

        // =========================
        // TABLE ROW CLICK
        // =========================

        userTable.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        int selectedRow =
                                userTable.getSelectedRow();

                        if (selectedRow >= 0) {

                            txtUserId.setText(
                                    tableModel.getValueAt(
                                            selectedRow, 0
                                    ).toString()
                            );

                            txtUsername.setText(
                                    tableModel.getValueAt(
                                            selectedRow, 1
                                    ).toString()
                            );

                            txtEmail.setText(
                                    tableModel.getValueAt(
                                            selectedRow, 2
                                    ).toString()
                            );

                            txtPassword.setText(
                                    tableModel.getValueAt(
                                            selectedRow, 3
                                    ).toString()
                            );

                            cmbRole.setSelectedItem(
                                    tableModel.getValueAt(
                                            selectedRow, 4
                                    ).toString()
                            );

                            cmbStatus.setSelectedItem(
                                    tableModel.getValueAt(
                                            selectedRow, 5
                                    ).toString()
                            );
                        }
                    }
                }
        );
    }

    // =========================
    // LOAD USERS
    // =========================

    private void loadUsers() {

        tableModel.setRowCount(0);

        List<User> users =
                userDAO.getAllUsers();

        for (User user : users) {

            tableModel.addRow(
                    new Object[]{
                            user.getUserId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getRole(),
                            user.getStatus(),
                            user.getCreatedAt()
                    }
            );
        }
    }

    // =========================
    // ADD USER
    // =========================

    private void addUser() {

        if (!validateInput()) {
            return;
        }

        User user = new User();

        user.setUsername(
                txtUsername.getText().trim());

        user.setEmail(
                txtEmail.getText().trim());

        user.setPassword(
                new String(txtPassword.getPassword()));

        user.setRole(
                cmbRole.getSelectedItem().toString());

        user.setStatus(
                cmbStatus.getSelectedItem().toString());

        boolean success =
                userDAO.addUser(user);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "User added successfully."
            );

            clearForm();
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

    // =========================
    // UPDATE USER
    // =========================

    private void updateUser() {

        if (txtUserId.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a user from the table."
            );

            return;
        }

        if (!validateInput()) {
            return;
        }

        User user = new User();

        user.setUserId(
                Integer.parseInt(
                        txtUserId.getText().trim()
                )
        );

        user.setUsername(
                txtUsername.getText().trim());

        user.setEmail(
                txtEmail.getText().trim());

        user.setPassword(
                new String(txtPassword.getPassword()));

        user.setRole(
                cmbRole.getSelectedItem().toString());

        user.setStatus(
                cmbStatus.getSelectedItem().toString());

        boolean success =
                userDAO.updateUser(user);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "User updated successfully."
            );

            clearForm();
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

    // =========================
    // DELETE USER
    // =========================

    private void deleteUser() {

        if (txtUserId.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a user from the table."
            );

            return;
        }

        int userId =
                Integer.parseInt(
                        txtUserId.getText().trim()
                );

        int confirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this user?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success =
                userDAO.deleteUser(userId);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "User deleted successfully."
            );

            clearForm();
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

    // =========================
    // VALIDATION
    // =========================

    private boolean validateInput() {

        String username =
                txtUsername.getText().trim();

        String email =
                txtEmail.getText().trim();

        String password =
                new String(txtPassword.getPassword()).trim();

        if (username.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Username cannot be empty."
            );

            return false;
        }

        if (email.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Email cannot be empty."
            );

            return false;
        }

        if (password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Password cannot be empty."
            );

            return false;
        }

        if (!email.contains("@")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email."
            );

            return false;
        }

        return true;
    }

    // =========================
    // CLEAR FORM
    // =========================

    private void clearForm() {

        txtUserId.setText("");
        txtUsername.setText("");
        txtEmail.setText("");
        txtPassword.setText("");

        cmbRole.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);

        userTable.clearSelection();
    }

    // =========================
    // MAIN METHOD
    // =========================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            UserManagementScreen screen =
                    new UserManagementScreen();

            screen.setVisible(true);
        });
    }
}