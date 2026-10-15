package com.gym.dashboard;

import com.gym.DBConnection;
import com.gym.members.MemberFrame;
import com.gym.trainers.TrainerFrame;
import com.gym.payments.PaymentFrame;
import com.gym.attendance.AttendanceFrame;
import com.gym.reports.ReportsFrame;
import com.gym.settings.SettingsFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DashboardFrame extends JFrame {

    private JLabel membersValue;
    private JLabel trainersValue;
    private JLabel revenueValue;
    private JLabel attendanceValue;

    private JTable membersTable;

    public DashboardFrame() {

        setTitle("FITZONE GYM - Dashboard");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createUI();

        loadDashboardData();
        loadRecentMembers();
    }

    // =====================================================
    // MAIN UI
    // =====================================================

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18, 18, 24));

        // =================================================
        // SIDEBAR
        // =================================================

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 750));
        sidebar.setBackground(new Color(25, 25, 33));

        sidebar.setLayout(
                new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Logo

        JLabel logo = new JLabel("💪 FITZONE");

        logo.setFont(
                new Font("Arial", Font.BOLD, 25));

        logo.setForeground(
                new Color(255, 105, 30));

        logo.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        sidebar.add(Box.createVerticalStrut(30));
        sidebar.add(logo);

        JLabel systemText = new JLabel(
                "GYM MANAGEMENT");

        systemText.setFont(
                new Font("Arial", Font.PLAIN, 11));

        systemText.setForeground(
                new Color(150, 150, 160));

        systemText.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(systemText);

        sidebar.add(Box.createVerticalStrut(40));

        // Sidebar buttons

        JButton dashboardBtn = createSidebarButton("🏠  Dashboard");

        JButton membersBtn = createSidebarButton("👤  Members");

        JButton trainersBtn = createSidebarButton("🏋  Trainers");
        trainersBtn.addActionListener(e -> {

            TrainerFrame trainerFrame = new TrainerFrame();

            trainerFrame.setVisible(true);
        });

        JButton paymentsBtn = createSidebarButton("💳  Payments");
        paymentsBtn.addActionListener(e -> {

            PaymentFrame paymentFrame = new PaymentFrame();

            paymentFrame.setVisible(true);
        });

        JButton attendanceBtn = createSidebarButton("📅  Attendance");
        attendanceBtn.addActionListener(e -> {

            AttendanceFrame attendanceFrame = new AttendanceFrame();

            attendanceFrame.setVisible(true);
        });

        JButton reportsBtn = createSidebarButton("📊  Reports");
        reportsBtn.addActionListener(e -> {
            ReportsFrame reportsFrame = new ReportsFrame();
            reportsFrame.setVisible(true);
        });

        JButton settingsBtn = createSidebarButton("⚙  Settings");
        settingsBtn.addActionListener(e -> {
            SettingsFrame settingsFrame = new SettingsFrame();
            settingsFrame.setVisible(true);
        });

        sidebar.add(dashboardBtn);
        sidebar.add(membersBtn);
        sidebar.add(trainersBtn);
        sidebar.add(paymentsBtn);
        sidebar.add(attendanceBtn);
        sidebar.add(reportsBtn);
        sidebar.add(settingsBtn);

        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = createSidebarButton("🚪  Logout");

        sidebar.add(logoutBtn);

        sidebar.add(Box.createVerticalStrut(20));

        // =================================================
        // HEADER
        // =================================================

        JPanel content = new JPanel(
                new BorderLayout());

        content.setBackground(
                new Color(18, 18, 24));

        JPanel header = new JPanel(
                new BorderLayout());

        header.setBackground(
                new Color(18, 18, 24));

        header.setBorder(
                new EmptyBorder(
                        25, 30, 15, 30));

        JLabel title = new JLabel(
                "Dashboard");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30));

        title.setForeground(Color.WHITE);

        JLabel admin = new JLabel(
                "Admin  ●");

        admin.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14));

        admin.setForeground(
                new Color(100, 220, 130));

        header.add(
                title,
                BorderLayout.WEST);

        header.add(
                admin,
                BorderLayout.EAST);

        content.add(
                header,
                BorderLayout.NORTH);

        // =================================================
        // DASHBOARD CONTENT
        // =================================================

        JPanel dashboard = new JPanel();

        dashboard.setBackground(
                new Color(18, 18, 24));

        dashboard.setLayout(
                new BoxLayout(
                        dashboard,
                        BoxLayout.Y_AXIS));

        dashboard.setBorder(
                new EmptyBorder(
                        5, 30, 30, 30));

        // Welcome text

        JLabel welcome = new JLabel(
                "Welcome back, Admin 👋");

        welcome.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20));

        welcome.setForeground(
                new Color(200, 200, 210));

        dashboard.add(welcome);

        dashboard.add(
                Box.createVerticalStrut(20));

        // =================================================
        // STAT CARDS
        // =================================================

        JPanel cards = new JPanel(
                new GridLayout(
                        1, 4, 15, 0));

        cards.setBackground(
                new Color(18, 18, 24));

        membersValue = new JLabel("0");

        trainersValue = new JLabel("0");

        revenueValue = new JLabel("₹0");

        attendanceValue = new JLabel("0");

        cards.add(
                createCard(
                        "👤  TOTAL MEMBERS",
                        membersValue));

        cards.add(
                createCard(
                        "🏋  TRAINERS",
                        trainersValue));

        cards.add(
                createCard(
                        "💰  TOTAL REVENUE",
                        revenueValue));

        cards.add(
                createCard(
                        "📅  TODAY'S ATTENDANCE",
                        attendanceValue));

        dashboard.add(cards);

        dashboard.add(
                Box.createVerticalStrut(25));

        // =================================================
        // RECENT MEMBERS
        // =================================================

        JLabel recentTitle = new JLabel(
                "Recent Members");

        recentTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20));

        recentTitle.setForeground(
                Color.WHITE);

        dashboard.add(recentTitle);

        dashboard.add(
                Box.createVerticalStrut(12));

        membersTable = new JTable();

        membersTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13));

        membersTable.setRowHeight(32);

        membersTable.setBackground(
                new Color(35, 35, 45));

        membersTable.setForeground(
                Color.WHITE);

        membersTable.setGridColor(
                new Color(65, 65, 75));

        membersTable.setSelectionBackground(
                new Color(255, 105, 30));

        membersTable.setSelectionForeground(
                Color.WHITE);

        membersTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13));

        membersTable.getTableHeader()
                .setBackground(
                        new Color(45, 45, 55));

        membersTable.getTableHeader()
                .setForeground(
                        Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(
                membersTable);

        scrollPane.setPreferredSize(
                new Dimension(
                        900, 250));

        scrollPane.getViewport()
                .setBackground(
                        new Color(35, 35, 45));

        dashboard.add(scrollPane);

        content.add(
                dashboard,
                BorderLayout.CENTER);

        mainPanel.add(
                sidebar,
                BorderLayout.WEST);

        mainPanel.add(
                content,
                BorderLayout.CENTER);

        add(mainPanel);

        // =================================================
        // BUTTON ACTIONS
        // =================================================

        logoutBtn.addActionListener(e -> {

            dispose();

            JOptionPane.showMessageDialog(
                    null,
                    "You have been logged out.");

        });

        dashboardBtn.addActionListener(e -> loadDashboardData());

        membersBtn.addActionListener(e -> {

            MemberFrame memberFrame = new MemberFrame();

            memberFrame.setVisible(true);
        });
    }

    // =====================================================
    // SIDEBAR BUTTON
    // =====================================================

    private JButton createSidebarButton(
            String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14));

        button.setForeground(
                new Color(210, 210, 220));

        button.setBackground(
                new Color(25, 25, 33));

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setHorizontalAlignment(
                SwingConstants.LEFT);

        button.setMaximumSize(
                new Dimension(
                        220, 50));

        button.setBorder(
                new EmptyBorder(
                        10, 25, 10, 10));

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR));

        return button;
    }

    // =====================================================
    // STAT CARD
    // =====================================================

    private JPanel createCard(
            String title,
            JLabel value) {

        JPanel card = new JPanel();

        card.setBackground(
                new Color(35, 35, 45));

        card.setBorder(
                new EmptyBorder(
                        18, 18, 18, 18));

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12));

        titleLabel.setForeground(
                new Color(160, 160, 170));

        value.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30));

        value.setForeground(
                new Color(255, 105, 30));

        card.add(titleLabel);

        card.add(
                Box.createVerticalStrut(12));

        card.add(value);

        return card;
    }

    // =====================================================
    // LOAD DASHBOARD DATA
    // =====================================================

    private void loadDashboardData() {

        String membersSQL = "SELECT COUNT(*) FROM member";

        String trainersSQL = "SELECT COUNT(*) FROM trainer";

        String revenueSQL = "SELECT COALESCE(SUM(amount),0) " +
                "FROM payment " +
                "WHERE payment_status='Paid'";

        String attendanceSQL = "SELECT COUNT(*) FROM attendance " +
                "WHERE attendance_date = CURDATE()";

        try (
                Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return;
            }

            // Members

            try (
                    PreparedStatement ps = con.prepareStatement(
                            membersSQL);

                    ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    membersValue.setText(
                            rs.getString(1));
                }
            }

            // Trainers

            try (
                    PreparedStatement ps = con.prepareStatement(
                            trainersSQL);

                    ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    trainersValue.setText(
                            rs.getString(1));
                }
            }

            // Revenue

            try (
                    PreparedStatement ps = con.prepareStatement(
                            revenueSQL);

                    ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    double revenue = rs.getDouble(1);

                    revenueValue.setText(
                            String.format(
                                    "₹%.0f",
                                    revenue));
                }
            }

            // Attendance

            try (
                    PreparedStatement ps = con.prepareStatement(
                            attendanceSQL);

                    ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    attendanceValue.setText(
                            rs.getString(1));
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load dashboard data.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================
    // LOAD RECENT MEMBERS
    // =====================================================

    private void loadRecentMembers() {

        String sql = "SELECT m.member_id, " +
                "m.member_name, " +
                "m.phone, " +
                "ms.plan_name, " +
                "t.trainer_name " +
                "FROM member m " +
                "LEFT JOIN membership ms " +
                "ON m.membership_id = ms.membership_id " +
                "LEFT JOIN trainer t " +
                "ON m.trainer_id = t.trainer_id " +
                "ORDER BY m.member_id DESC " +
                "LIMIT 8";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()) {

            DefaultTableModel model = new DefaultTableModel(
                    new String[] {
                            "ID",
                            "Member Name",
                            "Phone",
                            "Membership",
                            "Trainer"
                    },
                    0);

            while (rs.next()) {

                model.addRow(
                        new Object[] {
                                rs.getInt("member_id"),
                                rs.getString("member_name"),
                                rs.getString("phone"),
                                rs.getString("plan_name"),
                                rs.getString("trainer_name")
                        });
            }

            membersTable.setModel(model);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}