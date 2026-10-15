package com.gym.attendance;

import com.gym.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AttendanceFrame extends JFrame {

    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    private JLabel presentLabel;
    private JLabel absentLabel;

    public AttendanceFrame() {

        setTitle("FITZONE GYM - Attendance Management");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createUI();
        loadAttendance();
        loadTodaySummary();
    }

    // =====================================================
    // CREATE UI
    // =====================================================

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.setBackground(
                new Color(18, 18, 24));

        // =================================================
        // HEADER
        // =================================================

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(
                new Color(18, 18, 24));

        header.setBorder(
                new EmptyBorder(
                        20, 30, 15, 30));

        JLabel title = new JLabel("ATTENDANCE MANAGEMENT");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28));

        title.setForeground(Color.WHITE);

        JButton addButton = new JButton("+ MARK ATTENDANCE");

        styleOrangeButton(addButton);

        header.add(
                title,
                BorderLayout.WEST);

        header.add(
                addButton,
                BorderLayout.EAST);

        mainPanel.add(
                header,
                BorderLayout.NORTH);

        // =================================================
        // CENTER
        // =================================================

        JPanel centerPanel = new JPanel(new BorderLayout());

        centerPanel.setBackground(
                new Color(18, 18, 24));

        centerPanel.setBorder(
                new EmptyBorder(
                        0, 30, 30, 30));

        // =================================================
        // SUMMARY
        // =================================================

        JPanel summaryPanel = new JPanel(
                new GridLayout(1, 2, 15, 0));

        summaryPanel.setBackground(
                new Color(18, 18, 24));

        JPanel presentCard = createSummaryCard(
                "PRESENT TODAY",
                "0",
                new Color(45, 150, 80));

        JPanel absentCard = createSummaryCard(
                "ABSENT TODAY",
                "0",
                new Color(190, 55, 55));

        presentLabel = (JLabel) presentCard.getClientProperty(
                "valueLabel");

        absentLabel = (JLabel) absentCard.getClientProperty(
                "valueLabel");

        summaryPanel.add(presentCard);
        summaryPanel.add(absentCard);

        centerPanel.add(
                summaryPanel,
                BorderLayout.NORTH);

        // =================================================
        // SEARCH
        // =================================================

        JPanel searchPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        10,
                        10));

        searchPanel.setBackground(
                new Color(30, 30, 40));

        JLabel searchLabel = new JLabel("Search Member:");

        searchLabel.setForeground(Color.WHITE);

        searchLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14));

        searchField = new JTextField();

        searchField.setPreferredSize(
                new Dimension(
                        280,
                        38));

        JButton searchButton = new JButton("SEARCH");

        styleOrangeButton(searchButton);

        JButton refreshButton = new JButton("REFRESH");

        styleDarkButton(refreshButton);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        // =================================================
        // TABLE
        // =================================================

        tableModel = new DefaultTableModel(
                new String[] {
                        "ID",
                        "Member",
                        "Date",
                        "Status"
                },
                0);

        attendanceTable = new JTable(tableModel);

        attendanceTable.setRowHeight(35);

        attendanceTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14));

        attendanceTable.setBackground(
                new Color(35, 35, 45));

        attendanceTable.setForeground(
                Color.WHITE);

        attendanceTable.setGridColor(
                new Color(65, 65, 75));

        attendanceTable.setSelectionBackground(
                new Color(255, 105, 30));

        attendanceTable.setSelectionForeground(
                Color.WHITE);

        attendanceTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                14));

        attendanceTable.getTableHeader()
                .setBackground(
                        new Color(45, 45, 55));

        attendanceTable.getTableHeader()
                .setForeground(
                        Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(
                attendanceTable);

        JPanel tableContainer = new JPanel(
                new BorderLayout());

        tableContainer.setBackground(
                new Color(18, 18, 24));

        tableContainer.add(
                searchPanel,
                BorderLayout.NORTH);

        tableContainer.add(
                scrollPane,
                BorderLayout.CENTER);

        centerPanel.add(
                tableContainer,
                BorderLayout.CENTER);

        // =================================================
        // BOTTOM
        // =================================================

        JPanel bottomPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT));

        bottomPanel.setBackground(
                new Color(18, 18, 24));

        JButton editButton = new JButton("EDIT");

        styleOrangeButton(editButton);

        JButton deleteButton = new JButton("DELETE");

        deleteButton.setBackground(
                new Color(190, 55, 55));

        deleteButton.setForeground(
                Color.WHITE);

        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);

        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);

        centerPanel.add(
                bottomPanel,
                BorderLayout.SOUTH);

        mainPanel.add(
                centerPanel,
                BorderLayout.CENTER);

        add(mainPanel);

        // =================================================
        // ACTIONS
        // =================================================

        addButton.addActionListener(
                e -> showAttendanceForm(null));

        searchButton.addActionListener(
                e -> searchAttendance());

        refreshButton.addActionListener(
                e -> {
                    searchField.setText("");
                    loadAttendance();
                    loadTodaySummary();
                });

        editButton.addActionListener(
                e -> editAttendance());

        deleteButton.addActionListener(
                e -> deleteAttendance());
    }

    // =====================================================
    // SUMMARY CARD
    // =====================================================

    private JPanel createSummaryCard(
            String title,
            String value,
            Color valueColor) {

        JPanel panel = new JPanel(
                new BorderLayout());

        panel.setBackground(
                new Color(35, 35, 45));

        panel.setBorder(
                new EmptyBorder(
                        15, 20, 15, 20));

        JLabel titleLabel = new JLabel(title);

        titleLabel.setForeground(
                Color.LIGHT_GRAY);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13));

        JLabel valueLabel = new JLabel(value);

        valueLabel.setForeground(
                valueColor);

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26));

        panel.add(
                titleLabel,
                BorderLayout.WEST);

        panel.add(
                valueLabel,
                BorderLayout.EAST);

        panel.putClientProperty(
                "valueLabel",
                valueLabel);

        return panel;
    }

    // =====================================================
    // LOAD ATTENDANCE
    // =====================================================

    private void loadAttendance() {

        String sql = "SELECT a.attendance_id, " +
                "m.member_name, " +
                "a.attendance_date, " +
                "a.status " +
                "FROM attendance a " +
                "JOIN member m " +
                "ON a.member_id = m.member_id " +
                "ORDER BY a.attendance_date DESC, " +
                "a.attendance_id DESC";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()) {

            tableModel.setRowCount(0);

            while (rs.next()) {

                tableModel.addRow(
                        new Object[] {
                                rs.getInt(
                                        "attendance_id"),
                                rs.getString(
                                        "member_name"),
                                rs.getDate(
                                        "attendance_date"),
                                rs.getString(
                                        "status")
                        });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // TODAY SUMMARY
    // =====================================================

    private void loadTodaySummary() {

        String sql = "SELECT " +
                "SUM(CASE WHEN status = 'Present' " +
                "THEN 1 ELSE 0 END) AS present_count, " +
                "SUM(CASE WHEN status = 'Absent' " +
                "THEN 1 ELSE 0 END) AS absent_count " +
                "FROM attendance " +
                "WHERE attendance_date = CURDATE()";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                int present = rs.getInt(
                        "present_count");

                int absent = rs.getInt(
                        "absent_count");

                presentLabel.setText(
                        String.valueOf(present));

                absentLabel.setText(
                        String.valueOf(absent));
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // SEARCH
    // =====================================================

    private void searchAttendance() {

        String search = searchField.getText().trim();

        if (search.isEmpty()) {

            loadAttendance();
            return;
        }

        String sql = "SELECT a.attendance_id, " +
                "m.member_name, " +
                "a.attendance_date, " +
                "a.status " +
                "FROM attendance a " +
                "JOIN member m " +
                "ON a.member_id = m.member_id " +
                "WHERE m.member_name LIKE ? " +
                "OR a.status LIKE ? " +
                "ORDER BY a.attendance_date DESC";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            String pattern = "%" + search + "%";

            ps.setString(1, pattern);
            ps.setString(2, pattern);

            ResultSet rs = ps.executeQuery();

            tableModel.setRowCount(0);

            while (rs.next()) {

                tableModel.addRow(
                        new Object[] {
                                rs.getInt(
                                        "attendance_id"),
                                rs.getString(
                                        "member_name"),
                                rs.getDate(
                                        "attendance_date"),
                                rs.getString(
                                        "status")
                        });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // ATTENDANCE FORM
    // =====================================================

    private void showAttendanceForm(
            Integer attendanceId) {

        JDialog dialog = new JDialog(
                this,
                attendanceId == null
                        ? "Mark Attendance"
                        : "Edit Attendance",
                true);

        dialog.setSize(
                480,
                400);

        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();

        panel.setBackground(
                new Color(30, 30, 40));

        panel.setBorder(
                new EmptyBorder(
                        25, 35, 25, 35));

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS));

        JLabel title = new JLabel(
                attendanceId == null
                        ? "MARK ATTENDANCE"
                        : "EDIT ATTENDANCE");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24));

        title.setForeground(
                new Color(255, 105, 30));

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        panel.add(title);

        panel.add(
                Box.createVerticalStrut(25));

        // MEMBER

        JLabel memberLabel = createLabel("Member");

        JComboBox<String> memberBox = new JComboBox<>();

        memberBox.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40));

        loadMembers(memberBox);

        panel.add(memberLabel);
        panel.add(memberBox);

        panel.add(
                Box.createVerticalStrut(15));

        // DATE

        JLabel dateLabel = createLabel("Attendance Date");

        JTextField dateField = createTextField();

        dateField.setText(
                java.time.LocalDate
                        .now()
                        .toString());

        panel.add(dateLabel);
        panel.add(dateField);

        panel.add(
                Box.createVerticalStrut(15));

        // STATUS

        JLabel statusLabel = createLabel("Status");

        JComboBox<String> statusBox = new JComboBox<>(
                new String[] {
                        "Present",
                        "Absent"
                });

        statusBox.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40));

        panel.add(statusLabel);
        panel.add(statusBox);

        panel.add(
                Box.createVerticalStrut(25));

        JButton saveButton = new JButton(
                attendanceId == null
                        ? "MARK ATTENDANCE"
                        : "UPDATE ATTENDANCE");

        styleOrangeButton(saveButton);

        saveButton.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        panel.add(saveButton);

        dialog.add(panel);

        // LOAD EXISTING

        if (attendanceId != null) {

            loadAttendanceData(
                    attendanceId,
                    memberBox,
                    dateField,
                    statusBox);
        }

        // SAVE

        saveButton.addActionListener(e -> {

            try {

                int memberId = getSelectedMemberId(
                        memberBox);

                Date attendanceDate = Date.valueOf(
                        dateField
                                .getText()
                                .trim());

                String status = statusBox
                        .getSelectedItem()
                        .toString();

                if (attendanceId == null) {

                    addAttendance(
                            memberId,
                            attendanceDate,
                            status);

                } else {

                    updateAttendance(
                            attendanceId,
                            memberId,
                            attendanceDate,
                            status);
                }

                dialog.dispose();

                loadAttendance();
                loadTodaySummary();

            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Date must be in YYYY-MM-DD format.");
            }
        });

        dialog.setVisible(true);
    }

    // =====================================================
    // LOAD MEMBERS
    // =====================================================

    private void loadMembers(
            JComboBox<String> box) {

        String sql = "SELECT member_id, member_name " +
                "FROM member " +
                "ORDER BY member_name";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                box.addItem(
                        rs.getInt("member_id")
                                + " - "
                                + rs.getString(
                                        "member_name"));
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // GET MEMBER ID
    // =====================================================

    private int getSelectedMemberId(
            JComboBox<String> box) {

        String value = box.getSelectedItem()
                .toString();

        return Integer.parseInt(
                value.split(" - ")[0]);
    }

    // =====================================================
    // ADD ATTENDANCE
    // =====================================================

    private void addAttendance(
            int memberId,
            Date attendanceDate,
            String status) {

        String sql = "INSERT INTO attendance " +
                "(member_id, attendance_date, status) " +
                "VALUES (?, ?, ?)";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            ps.setDate(2, attendanceDate);
            ps.setString(3, status);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Attendance marked successfully!");

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // UPDATE ATTENDANCE
    // =====================================================

    private void updateAttendance(
            int attendanceId,
            int memberId,
            Date attendanceDate,
            String status) {

        String sql = "UPDATE attendance SET " +
                "member_id=?, " +
                "attendance_date=?, " +
                "status=? " +
                "WHERE attendance_id=?";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            ps.setDate(2, attendanceDate);
            ps.setString(3, status);
            ps.setInt(4, attendanceId);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Attendance updated successfully!");

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // EDIT ATTENDANCE
    // =====================================================

    private void editAttendance() {

        int row = attendanceTable
                .getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an attendance record.");

            return;
        }

        int attendanceId = (int) attendanceTable
                .getValueAt(row, 0);

        showAttendanceForm(
                attendanceId);
    }

    // =====================================================
    // LOAD ATTENDANCE DATA
    // =====================================================

    private void loadAttendanceData(
            int attendanceId,
            JComboBox<String> memberBox,
            JTextField dateField,
            JComboBox<String> statusBox) {

        String sql = "SELECT * FROM attendance " +
                "WHERE attendance_id=?";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, attendanceId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int memberId = rs.getInt(
                        "member_id");

                dateField.setText(
                        rs.getDate(
                                "attendance_date").toString());

                statusBox.setSelectedItem(
                        rs.getString(
                                "status"));

                selectMember(
                        memberBox,
                        memberId);
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // SELECT MEMBER
    // =====================================================

    private void selectMember(
            JComboBox<String> box,
            int memberId) {

        for (int i = 0; i < box.getItemCount(); i++) {

            String value = box.getItemAt(i);

            if (value.startsWith(
                    memberId + " - ")) {

                box.setSelectedIndex(i);
                break;
            }
        }
    }

    // =====================================================
    // DELETE
    // =====================================================

    private void deleteAttendance() {

        int row = attendanceTable
                .getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an attendance record.");

            return;
        }

        int attendanceId = (int) attendanceTable
                .getValueAt(row, 0);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete this attendance record?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {

            return;
        }

        String sql = "DELETE FROM attendance " +
                "WHERE attendance_id=?";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    attendanceId);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Attendance deleted successfully!");

            loadAttendance();
            loadTodaySummary();

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // LABEL
    // =====================================================

    private JLabel createLabel(
            String text) {

        JLabel label = new JLabel(text);

        label.setForeground(Color.WHITE);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13));

        return label;
    }

    // =====================================================
    // TEXT FIELD
    // =====================================================

    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14));

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40));

        return field;
    }

    // =====================================================
    // ORANGE BUTTON
    // =====================================================

    private void styleOrangeButton(
            JButton button) {

        button.setBackground(
                new Color(255, 105, 30));

        button.setForeground(Color.WHITE);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13));

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR));
    }

    // =====================================================
    // DARK BUTTON
    // =====================================================

    private void styleDarkButton(
            JButton button) {

        button.setBackground(
                new Color(55, 55, 65));

        button.setForeground(Color.WHITE);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13));

        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    // =====================================================
    // ERROR
    // =====================================================

    private void showError(
            Exception e) {

        e.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }
}