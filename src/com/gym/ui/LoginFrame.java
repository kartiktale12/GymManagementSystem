
package com.gym.ui;

import com.gym.dashboard.DashboardFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

        private JTextField usernameField;
        private JPasswordField passwordField;

        public LoginFrame() {

                setTitle("FITZONE GYM - Admin Login");
                setSize(900, 600);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setLocationRelativeTo(null);
                setResizable(false);

                // Main panel
                JPanel mainPanel = new JPanel(new BorderLayout());
                mainPanel.setBackground(new Color(18, 18, 24));

                // ==========================================
                // LEFT SIDE
                // ==========================================

                JPanel leftPanel = new JPanel();
                leftPanel.setPreferredSize(new Dimension(400, 600));
                leftPanel.setBackground(new Color(24, 24, 32));

                leftPanel.setLayout(
                                new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

                JLabel icon = new JLabel("💪");
                icon.setFont(
                                new Font("Segoe UI Emoji", Font.PLAIN, 70));
                icon.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel logo = new JLabel("FITZONE");
                logo.setFont(
                                new Font("Arial", Font.BOLD, 42));
                logo.setForeground(
                                new Color(255, 105, 30));
                logo.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel subtitle = new JLabel(
                                "GYM MANAGEMENT SYSTEM");
                subtitle.setFont(
                                new Font("Arial", Font.BOLD, 15));
                subtitle.setForeground(Color.WHITE);
                subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel slogan = new JLabel(
                                "<html><center>" +
                                                "TRAIN HARD.<br>" +
                                                "STAY STRONG.<br>" +
                                                "BE UNSTOPPABLE." +
                                                "</center></html>");

                slogan.setFont(
                                new Font("Arial", Font.BOLD, 20));

                slogan.setForeground(
                                new Color(180, 180, 190));

                slogan.setAlignmentX(
                                Component.CENTER_ALIGNMENT);

                leftPanel.add(Box.createVerticalGlue());
                leftPanel.add(icon);
                leftPanel.add(Box.createVerticalStrut(10));
                leftPanel.add(logo);
                leftPanel.add(Box.createVerticalStrut(5));
                leftPanel.add(subtitle);
                leftPanel.add(Box.createVerticalStrut(35));
                leftPanel.add(slogan);
                leftPanel.add(Box.createVerticalGlue());

                // ==========================================
                // RIGHT SIDE
                // ==========================================

                JPanel rightPanel = new JPanel(
                                new GridBagLayout());

                rightPanel.setBackground(
                                new Color(18, 18, 24));

                JPanel loginBox = new JPanel();

                loginBox.setPreferredSize(
                                new Dimension(360, 400));

                loginBox.setBackground(
                                new Color(35, 35, 45));

                loginBox.setBorder(
                                new EmptyBorder(35, 40, 35, 40));

                loginBox.setLayout(
                                new BoxLayout(
                                                loginBox,
                                                BoxLayout.Y_AXIS));

                // Login title

                JLabel loginTitle = new JLabel(
                                "ADMIN LOGIN");

                loginTitle.setFont(
                                new Font("Arial", Font.BOLD, 28));

                loginTitle.setForeground(Color.WHITE);

                loginTitle.setAlignmentX(
                                Component.CENTER_ALIGNMENT);

                // Subtitle

                JLabel welcome = new JLabel(
                                "Welcome back, Admin");

                welcome.setFont(
                                new Font("Arial", Font.PLAIN, 14));

                welcome.setForeground(
                                new Color(160, 160, 170));

                welcome.setAlignmentX(
                                Component.CENTER_ALIGNMENT);

                // Username label

                JLabel usernameLabel = new JLabel(
                                "Username");

                usernameLabel.setFont(
                                new Font("Arial", Font.BOLD, 14));

                usernameLabel.setForeground(Color.WHITE);

                // Username field

                usernameField = new JTextField();

                usernameField.setFont(
                                new Font("Arial", Font.PLAIN, 16));

                usernameField.setForeground(Color.WHITE);

                usernameField.setBackground(
                                new Color(55, 55, 68));

                usernameField.setCaretColor(Color.WHITE);

                usernameField.setBorder(
                                BorderFactory.createCompoundBorder(
                                                BorderFactory.createLineBorder(
                                                                new Color(80, 80, 95)),
                                                BorderFactory.createEmptyBorder(
                                                                8, 10, 8, 10)));

                usernameField.setMaximumSize(
                                new Dimension(
                                                Integer.MAX_VALUE,
                                                45));

                // Password label

                JLabel passwordLabel = new JLabel(
                                "Password");

                passwordLabel.setFont(
                                new Font("Arial", Font.BOLD, 14));

                passwordLabel.setForeground(Color.WHITE);

                // Password field

                passwordField = new JPasswordField();

                passwordField.setFont(
                                new Font("Arial", Font.PLAIN, 16));

                passwordField.setForeground(Color.WHITE);

                passwordField.setBackground(
                                new Color(55, 55, 68));

                passwordField.setCaretColor(Color.WHITE);

                passwordField.setBorder(
                                BorderFactory.createCompoundBorder(
                                                BorderFactory.createLineBorder(
                                                                new Color(80, 80, 95)),
                                                BorderFactory.createEmptyBorder(
                                                                8, 10, 8, 10)));

                passwordField.setMaximumSize(
                                new Dimension(
                                                Integer.MAX_VALUE,
                                                45));

                // Login button

                JButton loginButton = new JButton(
                                "LOGIN");

                loginButton.setFont(
                                new Font("Arial", Font.BOLD, 16));

                loginButton.setForeground(Color.WHITE);

                loginButton.setBackground(
                                new Color(255, 105, 30));

                loginButton.setFocusPainted(false);

                loginButton.setBorderPainted(false);

                loginButton.setCursor(
                                new Cursor(Cursor.HAND_CURSOR));

                loginButton.setAlignmentX(
                                Component.CENTER_ALIGNMENT);

                loginButton.setMaximumSize(
                                new Dimension(
                                                Integer.MAX_VALUE,
                                                50));

                // Login action

                loginButton.addActionListener(
                                e -> login());

                passwordField.addActionListener(
                                e -> login());

                // Footer

                JLabel secure = new JLabel(
                                "🔒 Secure Admin Access");

                secure.setFont(
                                new Font("Arial", Font.PLAIN, 12));

                secure.setForeground(
                                new Color(140, 140, 150));

                secure.setAlignmentX(
                                Component.CENTER_ALIGNMENT);

                // Add components

                loginBox.add(loginTitle);

                loginBox.add(
                                Box.createVerticalStrut(8));

                loginBox.add(welcome);

                loginBox.add(
                                Box.createVerticalStrut(35));

                loginBox.add(usernameLabel);

                loginBox.add(
                                Box.createVerticalStrut(7));

                loginBox.add(usernameField);

                loginBox.add(
                                Box.createVerticalStrut(18));

                loginBox.add(passwordLabel);

                loginBox.add(
                                Box.createVerticalStrut(7));

                loginBox.add(passwordField);

                loginBox.add(
                                Box.createVerticalStrut(30));

                loginBox.add(loginButton);

                loginBox.add(
                                Box.createVerticalStrut(25));

                loginBox.add(secure);

                rightPanel.add(loginBox);

                // Add panels

                mainPanel.add(
                                leftPanel,
                                BorderLayout.WEST);

                mainPanel.add(
                                rightPanel,
                                BorderLayout.CENTER);

                add(mainPanel);
        }

        // ==========================================
        // LOGIN METHOD
        // ==========================================

        private void login() {

                String username = usernameField.getText().trim();

                String password = new String(
                                passwordField.getPassword());

                if (username.equals("admin")
                                &&
                                password.equals("admin123")) {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Login Successful!",
                                        "Success",
                                        JOptionPane.INFORMATION_MESSAGE);

                        dispose();

                        DashboardFrame dashboard = new DashboardFrame();

                        dashboard.setVisible(true);

                } else {

                        JOptionPane.showMessageDialog(
                                        this,
                                        "Invalid username or password!",
                                        "Login Failed",
                                        JOptionPane.ERROR_MESSAGE);
                }
        }
}