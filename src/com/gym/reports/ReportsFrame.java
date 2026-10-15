package com.gym.reports;

import com.gym.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ReportsFrame extends JFrame {

    private JLabel membersLabel;
    private JLabel trainersLabel;
    private JLabel revenueLabel;
    private JLabel attendanceLabel;

    private JTable table;
    private DefaultTableModel model;

    public ReportsFrame() {

        setTitle("FITZONE GYM - Reports & Analytics");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ================= HEADER =================

        JLabel title = new JLabel("Reports & Analytics");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton refreshButton = new JButton("Refresh");

        JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.WEST);
        header.add(refreshButton, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // ================= SUMMARY CARDS =================

        JPanel cards = new JPanel(
                new GridLayout(1, 4, 15, 15));

        membersLabel = new JLabel("0");
        trainersLabel = new JLabel("0");
        revenueLabel = new JLabel("₹0");
        attendanceLabel = new JLabel("0");

        cards.add(createCard("TOTAL MEMBERS", membersLabel));
        cards.add(createCard("TOTAL TRAINERS", trainersLabel));
        cards.add(createCard("TOTAL REVENUE", revenueLabel));
        cards.add(createCard("TODAY'S ATTENDANCE", attendanceLabel));

        // ================= REPORT BUTTONS =================

        JButton membershipButton = new JButton("Membership Report");
        JButton paymentButton = new JButton("Payment Report");
        JButton attendanceButton = new JButton("Attendance Report");

        membershipButton.setFont(new Font("Arial", Font.BOLD, 14));
        paymentButton.setFont(new Font("Arial", Font.BOLD, 14));
        attendanceButton.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel reportButtons = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 10));

        reportButtons.add(membershipButton);
        reportButtons.add(paymentButton);
        reportButtons.add(attendanceButton);

        JPanel topContent = new JPanel(new BorderLayout());
        topContent.add(cards, BorderLayout.NORTH);
        topContent.add(reportButtons, BorderLayout.SOUTH);

        mainPanel.add(topContent, BorderLayout.CENTER);

        // ================= TABLE =================

        model = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout());

        JLabel reportTitle = new JLabel("Membership Report");
        reportTitle.setFont(
                new Font("Arial", Font.BOLD, 20));

        tablePanel.add(reportTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.SOUTH);

        add(mainPanel);

        // ================= LOAD =================

        loadSummary();
        loadMembershipReport();

        // ================= EVENTS =================

        refreshButton.addActionListener(e -> {
            loadSummary();
            loadMembershipReport();
        });

        membershipButton.addActionListener(e -> {
            reportTitle.setText("Membership Report");
            loadMembershipReport();
        });

        paymentButton.addActionListener(e -> {
            reportTitle.setText("Payment Report");
            loadPaymentReport();
        });

        attendanceButton.addActionListener(e -> {
            reportTitle.setText("Attendance Report");
            loadAttendanceReport();
        });
    }

    // =========================================================
    // CARD
    // =========================================================

    private JPanel createCard(String title, JLabel value) {

        JPanel card = new JPanel(new BorderLayout());

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(70, 70, 80)),
                        BorderFactory.createEmptyBorder(
                                15, 15, 15, 15)));

        JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 14));

        value.setFont(
                new Font("Arial", Font.BOLD, 28));

        value.setForeground(
                new Color(255, 100, 30));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);

        return card;
    }

    // =========================================================
    // SUMMARY
    // =========================================================

    private void loadSummary() {

        try (
                Connection con = DBConnection.getConnection()) {

            // TOTAL MEMBERS
            String sql1 = "SELECT COUNT(*) FROM member";

            try (
                    PreparedStatement ps = con.prepareStatement(sql1);
                    ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    membersLabel.setText(
                            String.valueOf(rs.getInt(1)));
                }
            }

            // TOTAL TRAINERS
            String sql2 = "SELECT COUNT(*) FROM trainer";

            try (
                    PreparedStatement ps = con.prepareStatement(sql2);
                    ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    trainersLabel.setText(
                            String.valueOf(rs.getInt(1)));
                }
            }

            // TOTAL REVENUE
            String sql3 = "SELECT COALESCE(SUM(amount),0) " +
                    "FROM payment " +
                    "WHERE payment_status='Paid'";

            try (
                    PreparedStatement ps = con.prepareStatement(sql3);
                    ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    double revenue = rs.getDouble(1);

                    revenueLabel.setText(
                            String.format("₹%.0f", revenue));
                }
            }

            // TODAY ATTENDANCE
            String sql4 = "SELECT COUNT(*) FROM attendance " +
                    "WHERE attendance_date=CURDATE()";

            try (
                    PreparedStatement ps = con.prepareStatement(sql4);
                    ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    attendanceLabel.setText(
                            String.valueOf(rs.getInt(1)));
                }
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =========================================================
    // MEMBERSHIP REPORT
    // =========================================================

    private void loadMembershipReport() {

        model.setColumnIdentifiers(
                new String[] {
                        "Plan Name",
                        "Duration",
                        "Fee",
                        "Total Members"
                });

        model.setRowCount(0);

        String sql = """
                SELECT
                    m.plan_name,
                    m.duration_months,
                    m.fee,
                    COUNT(mem.member_id) AS total_members
                FROM membership m
                LEFT JOIN member mem
                    ON m.membership_id = mem.membership_id
                GROUP BY
                    m.membership_id,
                    m.plan_name,
                    m.duration_months,
                    m.fee
                ORDER BY m.membership_id
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                model.addRow(new Object[] {
                        rs.getString("plan_name"),
                        rs.getInt("duration_months") + " Months",
                        "₹" + rs.getDouble("fee"),
                        rs.getInt("total_members")
                });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =========================================================
    // PAYMENT REPORT
    // =========================================================

    private void loadPaymentReport() {

        model.setColumnIdentifiers(
                new String[] {
                        "Payment ID",
                        "Member Name",
                        "Amount",
                        "Payment Date",
                        "Method",
                        "Status"
                });

        model.setRowCount(0);

        String sql = """
                SELECT
                    p.payment_id,
                    m.member_name,
                    p.amount,
                    p.payment_date,
                    p.payment_method,
                    p.payment_status
                FROM payment p
                INNER JOIN member m
                    ON p.member_id = m.member_id
                ORDER BY p.payment_date DESC
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                model.addRow(new Object[] {
                        rs.getInt("payment_id"),
                        rs.getString("member_name"),
                        "₹" + rs.getDouble("amount"),
                        rs.getDate("payment_date"),
                        rs.getString("payment_method"),
                        rs.getString("payment_status")
                });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =========================================================
    // ATTENDANCE REPORT
    // =========================================================

    private void loadAttendanceReport() {

        model.setColumnIdentifiers(
                new String[] {
                        "Attendance ID",
                        "Member Name",
                        "Attendance Date",
                        "Status"
                });

        model.setRowCount(0);

        String sql = """
                SELECT
                    a.attendance_id,
                    m.member_name,
                    a.attendance_date,
                    a.status
                FROM attendance a
                INNER JOIN member m
                    ON a.member_id = m.member_id
                ORDER BY a.attendance_date DESC
                """;

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                model.addRow(new Object[] {
                        rs.getInt("attendance_id"),
                        rs.getString("member_name"),
                        rs.getDate("attendance_date"),
                        rs.getString("status")
                });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =========================================================
    // ERROR
    // =========================================================

    private void showError(SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Database Error:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ReportsFrame frame = new ReportsFrame();
            frame.setVisible(true);
        });
    }
}