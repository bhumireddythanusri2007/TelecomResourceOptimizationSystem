package com.telecom.gui;

import com.telecom.dao.LoginLogDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LoginHistoryManagementScreen extends JFrame {

    private JTable loginTable;
    private DefaultTableModel tableModel;
    private LoginLogDAO loginLogDAO;

    public LoginHistoryManagementScreen() {

        loginLogDAO = new LoginLogDAO();

        setTitle("Login History Management");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        createUI();
        loadLoginHistory();
    }

    private void createUI() {

        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel(
                "Login History Management",
                SwingConstants.CENTER
        );

        heading.setFont(
                new Font("Arial", Font.BOLD, 22)
        );

        add(heading, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{
                        "Login ID",
                        "User ID",
                        "Login Time",
                        "Login Status",
                        "IP Address"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row, int column) {
                return false;
            }
        };

        loginTable = new JTable(tableModel);

        JScrollPane scrollPane =
                new JScrollPane(loginTable);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel =
                new JPanel(new FlowLayout());

        JButton btnRefresh = new JButton("Refresh");
        JButton btnClose = new JButton("Close");

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnClose);

        add(buttonPanel, BorderLayout.SOUTH);

        btnRefresh.addActionListener(
                e -> loadLoginHistory()
        );

        btnClose.addActionListener(
                e -> dispose()
        );
    }

    private void loadLoginHistory() {

        tableModel.setRowCount(0);

        List<Object[]> logs =
                loginLogDAO.getAllLoginLogs();

        for (Object[] log : logs) {
            tableModel.addRow(log);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LoginHistoryManagementScreen screen =
                    new LoginHistoryManagementScreen();

            screen.setVisible(true);
        });
    }
}
