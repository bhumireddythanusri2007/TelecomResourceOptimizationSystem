package com.telecom.app;

import com.telecom.dao.UserDAO;
import com.telecom.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    private UserDAO userDAO;

    public LoginScreen() {

        userDAO = new UserDAO();

        setTitle("Telecom Resource Optimization System - Login");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createInterface();
    }

    private void createInterface() {

        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel =
                new JLabel(
                        "Telecom Resource Optimization System",
                        SwingConstants.CENTER);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18));

        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel =
                new JPanel(
                        new GridLayout(2, 2, 10, 10));

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 40, 20, 40));

        emailField = new JTextField();

        passwordField =
                new JPasswordField();

        formPanel.add(
                new JLabel("Email:"));

        formPanel.add(emailField);

        formPanel.add(
                new JLabel("Password:"));

        formPanel.add(passwordField);

        add(
                formPanel,
                BorderLayout.CENTER);

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout());

        JButton loginButton =
                new JButton("Login");

        JButton clearButton =
                new JButton("Clear");

        JButton exitButton =
                new JButton("Exit");

        buttonPanel.add(loginButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        add(
                buttonPanel,
                BorderLayout.SOUTH);

        loginButton.addActionListener(
                e -> login());

        clearButton.addActionListener(
                e -> clearFields());

        exitButton.addActionListener(
                e -> System.exit(0));
    }

    private void login() {

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword());

        if (email.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password.");

            return;
        }

        User user =
                userDAO.loginUser(
                        email,
                        password);

        if (user != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful.\n"
                            + "Welcome "
                            + user.getUsername()
                            + "\nRole: "
                            + user.getRole());

            MainDashboard dashboard =
                    new MainDashboard();

            dashboard.setVisible(true);

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid email or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {

        emailField.setText("");
        passwordField.setText("");
        emailField.requestFocus();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LoginScreen screen =
                    new LoginScreen();

            screen.setVisible(true);
        });
    }
}