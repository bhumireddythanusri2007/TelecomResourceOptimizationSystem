package com.telecom.gui;

import com.telecom.dao.LoginLogDAO;
import com.telecom.dao.UserDAO;
import com.telecom.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    private UserDAO userDAO;
    private LoginLogDAO loginLogDAO;

    public LoginScreen() {

        userDAO = new UserDAO();
        loginLogDAO = new LoginLogDAO();

        setTitle("Telecom Resource Optimization System");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createLoginUI();

        setVisible(true);
    }

    private void createLoginUI() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(10, 10, 10, 10);

        JLabel titleLabel =
                new JLabel(
                        "Telecom Resource Optimization System"
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        JLabel emailLabel =
                new JLabel("Email:");

        JLabel passwordLabel =
                new JLabel("Password:");

        emailField =
                new JTextField(20);

        passwordField =
                new JPasswordField(20);

        JButton loginButton =
                new JButton("Login");

        JButton clearButton =
                new JButton("Clear");

        JButton exitButton =
                new JButton("Exit");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        mainPanel.add(
                titleLabel,
                gbc
        );

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;

        mainPanel.add(
                emailLabel,
                gbc
        );

        gbc.gridx = 1;

        mainPanel.add(
                emailField,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 2;

        mainPanel.add(
                passwordLabel,
                gbc
        );

        gbc.gridx = 1;

        mainPanel.add(
                passwordField,
                gbc
        );

        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(loginButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;

        mainPanel.add(
                buttonPanel,
                gbc
        );

        add(mainPanel);

        loginButton.addActionListener(
                e -> login()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );

        exitButton.addActionListener(
                e -> System.exit(0)
        );
    }

    private void login() {

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (email.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password.",
                    "Login",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        User user =
                userDAO.loginUser(
                        email,
                        password
                );

        if (user != null) {

            loginLogDAO.addLoginLog(
                    user.getUserId(),
                    "SUCCESS",
                    "Desktop Application"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful!",
                    "Login",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

            new DashboardScreen();

        } else {

            loginLogDAO.addLoginLog(
                    null,
                    "FAILED",
                    "Desktop Application"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid email or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFields() {

        emailField.setText("");
        passwordField.setText("");

        emailField.requestFocus();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                LoginScreen::new
        );
    }
}