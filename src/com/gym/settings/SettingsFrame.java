package com.gym.settings;

import com.gym.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class SettingsFrame extends JFrame {

    public SettingsFrame() {

        setTitle("FITZONE GYM - Settings");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // ================= TITLE =================

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        mainPanel.add(title, BorderLayout.NORTH);

        // ================= SETTINGS PANEL =================

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(
                new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));

        // Database
        JPanel databasePanel = new JPanel(
                new BorderLayout(15, 10));

        databasePanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Database Connection"));

        JLabel databaseStatus = new JLabel(
                "Checking database connection...");

        databaseStatus.setFont(
                new Font("Arial", Font.BOLD, 16));

        JButton testButton = new JButton(
                "Test Connection");

        databasePanel.add(
                databaseStatus,
                BorderLayout.CENTER);

        databasePanel.add(
                testButton,
                BorderLayout.EAST);

        settingsPanel.add(databasePanel);

        settingsPanel.add(Box.createVerticalStrut(20));

        // Application Information
        JPanel infoPanel = new JPanel(
                new GridLayout(4, 2, 10, 10));

        infoPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Application Information"));

        infoPanel.add(new JLabel("Application Name:"));
        infoPanel.add(new JLabel("FITZONE GYM"));

        infoPanel.add(new JLabel("Version:"));
        infoPanel.add(new JLabel("1.0"));

        infoPanel.add(new JLabel("Technology:"));
        infoPanel.add(new JLabel("Java Swing + MySQL"));

        infoPanel.add(new JLabel("Project Type:"));
        infoPanel.add(new JLabel("Gym Management System"));

        settingsPanel.add(infoPanel);

        settingsPanel.add(Box.createVerticalStrut(20));

        // Database information
        JPanel dbInfoPanel = new JPanel(
                new GridLayout(3, 2, 10, 10));

        dbInfoPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Database Information"));

        dbInfoPanel.add(new JLabel("Database:"));
        dbInfoPanel.add(
                new JLabel("gym_management"));

        dbInfoPanel.add(new JLabel("DBMS:"));
        dbInfoPanel.add(
                new JLabel("MySQL"));

        dbInfoPanel.add(new JLabel("Connection:"));
        dbInfoPanel.add(
                new JLabel("JDBC"));

        settingsPanel.add(dbInfoPanel);

        mainPanel.add(
                settingsPanel,
                BorderLayout.CENTER);

        // ================= BOTTOM =================

        JButton closeButton = new JButton("Close");

        JPanel bottomPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT));

        bottomPanel.add(closeButton);

        mainPanel.add(
                bottomPanel,
                BorderLayout.SOUTH);

        add(mainPanel);

        // ================= TEST CONNECTION =================

        testButton.addActionListener(e -> {

            try {

                Connection con = DBConnection.getConnection();

                if (con != null && !con.isClosed()) {

                    databaseStatus.setText(
                            "● Connected Successfully");

                    databaseStatus.setForeground(
                            new Color(0, 150, 70));

                    JOptionPane.showMessageDialog(
                            this,
                            "Database connection is working!");

                    con.close();
                }

            } catch (Exception ex) {

                databaseStatus.setText(
                        "● Connection Failed");

                databaseStatus.setForeground(Color.RED);

                JOptionPane.showMessageDialog(
                        this,
                        "Database connection failed:\n"
                                + ex.getMessage(),
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        closeButton.addActionListener(e -> dispose());

        // Test automatically
        testButton.doClick();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            SettingsFrame frame = new SettingsFrame();

            frame.setVisible(true);
        });
    }
}
