package com.gym.members;

import com.gym.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MemberFrame extends JFrame {

    private JTable memberTable;
    private DefaultTableModel tableModel;

    private JTextField searchField;

    public MemberFrame() {

        setTitle("FITZONE GYM - Member Management");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createUI();
        loadMembers();
    }

    // =====================================================
    // MAIN UI
    // =====================================================

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.setBackground(
                new Color(18, 18, 24));

        // =================================================
        // HEADER
        // =================================================

        JPanel header = new JPanel(
                new BorderLayout());

        header.setBackground(
                new Color(18, 18, 24));

        header.setBorder(
                new EmptyBorder(
                        25, 30, 20, 30));

        JLabel title = new JLabel(
                "👤  MEMBER MANAGEMENT");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28));

        title.setForeground(Color.WHITE);

        JButton addButton = new JButton("+ ADD MEMBER");

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

        JPanel centerPanel = new JPanel(
                new BorderLayout());

        centerPanel.setBackground(
                new Color(18, 18, 24));

        centerPanel.setBorder(
                new EmptyBorder(
                        0, 30, 30, 30));

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

        JLabel searchLabel = new JLabel(
                "🔍 Search:");

        searchLabel.setForeground(
                Color.WHITE);

        searchLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14));

        searchField = new JTextField();

        searchField.setPreferredSize(
                new Dimension(
                        300, 38));

        searchField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14));

        JButton searchButton = new JButton("SEARCH");

        styleOrangeButton(searchButton);

        JButton refreshButton = new JButton("REFRESH");

        styleDarkButton(refreshButton);

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        centerPanel.add(
                searchPanel,
                BorderLayout.NORTH);

        // =================================================
        // TABLE
        // =================================================

        tableModel = new DefaultTableModel(
                new String[] {
                        "ID",
                        "Name",
                        "Phone",
                        "Email",
                        "Gender",
                        "Join Date",
                        "Membership",
                        "Trainer"
                },
                0);

        memberTable = new JTable(tableModel);

        memberTable.setRowHeight(32);

        memberTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13));

        memberTable.setBackground(
                new Color(35, 35, 45));

        memberTable.setForeground(
                Color.WHITE);

        memberTable.setGridColor(
                new Color(65, 65, 75));

        memberTable.setSelectionBackground(
                new Color(255, 105, 30));

        memberTable.setSelectionForeground(
                Color.WHITE);

        memberTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13));

        memberTable.getTableHeader()
                .setBackground(
                        new Color(45, 45, 55));

        memberTable.getTableHeader()
                .setForeground(
                        Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(
                memberTable);

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER);

        // =================================================
        // BOTTOM BUTTONS
        // =================================================

        JPanel bottomPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT));

        bottomPanel.setBackground(
                new Color(18, 18, 24));

        JButton editButton = new JButton("✏ EDIT");

        styleOrangeButton(editButton);

        JButton deleteButton = new JButton("🗑 DELETE");

        deleteButton.setBackground(
                new Color(190, 55, 55));

        deleteButton.setForeground(
                Color.WHITE);

        deleteButton.setFocusPainted(false);

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
        // BUTTON ACTIONS
        // =================================================

        addButton.addActionListener(
                e -> showMemberForm(null));

        searchButton.addActionListener(
                e -> searchMembers());

        refreshButton.addActionListener(
                e -> {
                    searchField.setText("");
                    loadMembers();
                });

        editButton.addActionListener(
                e -> editSelectedMember());

        deleteButton.addActionListener(
                e -> deleteSelectedMember());
    }

    // =====================================================
    // LOAD ALL MEMBERS
    // =====================================================

    private void loadMembers() {

        String sql = "SELECT m.member_id, " +
                "m.member_name, " +
                "m.phone, " +
                "m.email, " +
                "m.gender, " +
                "m.join_date, " +
                "ms.plan_name, " +
                "t.trainer_name " +
                "FROM member m " +
                "LEFT JOIN membership ms " +
                "ON m.membership_id = ms.membership_id " +
                "LEFT JOIN trainer t " +
                "ON m.trainer_id = t.trainer_id " +
                "ORDER BY m.member_id";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()) {

            tableModel.setRowCount(0);

            while (rs.next()) {

                tableModel.addRow(
                        new Object[] {
                                rs.getInt("member_id"),
                                rs.getString("member_name"),
                                rs.getString("phone"),
                                rs.getString("email"),
                                rs.getString("gender"),
                                rs.getDate("join_date"),
                                rs.getString("plan_name"),
                                rs.getString("trainer_name")
                        });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // SEARCH
    // =====================================================

    private void searchMembers() {

        String search = searchField.getText().trim();

        if (search.isEmpty()) {

            loadMembers();
            return;
        }

        String sql = "SELECT m.member_id, " +
                "m.member_name, " +
                "m.phone, " +
                "m.email, " +
                "m.gender, " +
                "m.join_date, " +
                "ms.plan_name, " +
                "t.trainer_name " +
                "FROM member m " +
                "LEFT JOIN membership ms " +
                "ON m.membership_id = ms.membership_id " +
                "LEFT JOIN trainer t " +
                "ON m.trainer_id = t.trainer_id " +
                "WHERE m.member_name LIKE ? " +
                "OR m.phone LIKE ? " +
                "OR m.email LIKE ? " +
                "ORDER BY m.member_id";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            String pattern = "%" + search + "%";

            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);

            ResultSet rs = ps.executeQuery();

            tableModel.setRowCount(0);

            while (rs.next()) {

                tableModel.addRow(
                        new Object[] {
                                rs.getInt("member_id"),
                                rs.getString("member_name"),
                                rs.getString("phone"),
                                rs.getString("email"),
                                rs.getString("gender"),
                                rs.getDate("join_date"),
                                rs.getString("plan_name"),
                                rs.getString("trainer_name")
                        });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // ADD / EDIT FORM
    // =====================================================

    private void showMemberForm(
            Integer memberId) {

        JDialog dialog = new JDialog(
                this,
                memberId == null
                        ? "Add Member"
                        : "Edit Member",
                true);

        dialog.setSize(500, 650);

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
                memberId == null
                        ? "ADD NEW MEMBER"
                        : "EDIT MEMBER");

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

        JTextField nameField = createTextField();

        JTextField phoneField = createTextField();

        JTextField emailField = createTextField();

        JComboBox<String> genderBox = new JComboBox<>(
                new String[] {
                        "Male",
                        "Female",
                        "Other"
                });

        JComboBox<String> membershipBox = new JComboBox<>();

        JComboBox<String> trainerBox = new JComboBox<>();

        panel.add(
                createLabel("Member Name"));

        panel.add(nameField);

        panel.add(
                Box.createVerticalStrut(12));

        panel.add(
                createLabel("Phone"));

        panel.add(phoneField);

        panel.add(
                Box.createVerticalStrut(12));

        panel.add(
                createLabel("Email"));

        panel.add(emailField);

        panel.add(
                Box.createVerticalStrut(12));

        panel.add(
                createLabel("Gender"));

        panel.add(genderBox);

        panel.add(
                Box.createVerticalStrut(12));

        panel.add(
                createLabel("Membership"));

        panel.add(membershipBox);

        panel.add(
                Box.createVerticalStrut(12));

        panel.add(
                createLabel("Trainer"));

        panel.add(trainerBox);

        panel.add(
                Box.createVerticalStrut(25));

        loadMemberships(
                membershipBox);

        loadTrainers(
                trainerBox);

        JButton saveButton = new JButton(
                memberId == null
                        ? "ADD MEMBER"
                        : "UPDATE MEMBER");

        styleOrangeButton(saveButton);

        saveButton.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        panel.add(saveButton);

        dialog.add(panel);

        // ================================================
        // LOAD EXISTING DATA
        // ================================================

        if (memberId != null) {

            loadMemberData(
                    memberId,
                    nameField,
                    phoneField,
                    emailField,
                    genderBox,
                    membershipBox,
                    trainerBox);
        }

        // ================================================
        // SAVE
        // ================================================

        saveButton.addActionListener(e -> {

            if (nameField.getText().trim().isEmpty()
                    ||
                    phoneField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Name and Phone are required.");

                return;
            }

            if (memberId == null) {

                addMember(
                        nameField.getText().trim(),
                        phoneField.getText().trim(),
                        emailField.getText().trim(),
                        genderBox.getSelectedItem().toString(),
                        membershipBox,
                        trainerBox);

            } else {

                updateMember(
                        memberId,
                        nameField.getText().trim(),
                        phoneField.getText().trim(),
                        emailField.getText().trim(),
                        genderBox.getSelectedItem().toString(),
                        membershipBox,
                        trainerBox);
            }

            dialog.dispose();

            loadMembers();
        });

        dialog.setVisible(true);
    }

    // =====================================================
    // ADD MEMBER
    // =====================================================

    private void addMember(
            String name,
            String phone,
            String email,
            String gender,
            JComboBox<String> membershipBox,
            JComboBox<String> trainerBox) {

        String sql = "INSERT INTO member " +
                "(member_name, phone, email, gender, " +
                "join_date, membership_id, trainer_id) " +
                "VALUES (?, ?, ?, ?, CURDATE(), ?, ?)";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, gender);

            ps.setInt(
                    5,
                    getSelectedId(
                            membershipBox));

            ps.setInt(
                    6,
                    getSelectedId(
                            trainerBox));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Member added successfully!");

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // UPDATE MEMBER
    // =====================================================

    private void updateMember(
            int memberId,
            String name,
            String phone,
            String email,
            String gender,
            JComboBox<String> membershipBox,
            JComboBox<String> trainerBox) {

        String sql = "UPDATE member SET " +
                "member_name=?, " +
                "phone=?, " +
                "email=?, " +
                "gender=?, " +
                "membership_id=?, " +
                "trainer_id=? " +
                "WHERE member_id=?";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, gender);

            ps.setInt(
                    5,
                    getSelectedId(
                            membershipBox));

            ps.setInt(
                    6,
                    getSelectedId(
                            trainerBox));

            ps.setInt(7, memberId);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Member updated successfully!");

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // DELETE
    // =====================================================

    private void deleteSelectedMember() {

        int row = memberTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a member first.");

            return;
        }

        int memberId = (int) memberTable
                .getValueAt(row, 0);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete this member?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {

            return;
        }

        String sql = "DELETE FROM member " +
                "WHERE member_id=?";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    memberId);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Member deleted successfully!");

            loadMembers();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cannot delete this member.\n" +
                            "The member may have payment or attendance records.",
                    "Delete Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================
    // EDIT SELECTED MEMBER
    // =====================================================

    private void editSelectedMember() {

        int row = memberTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a member first.");

            return;
        }

        int memberId = (int) memberTable
                .getValueAt(row, 0);

        showMemberForm(memberId);
    }

    // =====================================================
    // LOAD MEMBER DATA
    // =====================================================

    private void loadMemberData(
            int memberId,
            JTextField nameField,
            JTextField phoneField,
            JTextField emailField,
            JComboBox<String> genderBox,
            JComboBox<String> membershipBox,
            JComboBox<String> trainerBox) {

        String sql = "SELECT * FROM member " +
                "WHERE member_id=?";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    memberId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                nameField.setText(
                        rs.getString(
                                "member_name"));

                phoneField.setText(
                        rs.getString(
                                "phone"));

                emailField.setText(
                        rs.getString(
                                "email"));

                genderBox.setSelectedItem(
                        rs.getString(
                                "gender"));

                selectComboById(
                        membershipBox,
                        rs.getInt(
                                "membership_id"));

                selectComboById(
                        trainerBox,
                        rs.getInt(
                                "trainer_id"));
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // LOAD MEMBERSHIP DROPDOWN
    // =====================================================

    private void loadMemberships(
            JComboBox<String> box) {

        String sql = "SELECT membership_id, plan_name " +
                "FROM membership";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                box.addItem(
                        rs.getInt(
                                "membership_id")
                                + " - "
                                + rs.getString(
                                        "plan_name"));
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // LOAD TRAINERS DROPDOWN
    // =====================================================

    private void loadTrainers(
            JComboBox<String> box) {

        String sql = "SELECT trainer_id, trainer_name " +
                "FROM trainer";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                box.addItem(
                        rs.getInt(
                                "trainer_id")
                                + " - "
                                + rs.getString(
                                        "trainer_name"));
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // GET SELECTED ID
    // =====================================================

    private int getSelectedId(
            JComboBox<String> box) {

        String value = box.getSelectedItem()
                .toString();

        return Integer.parseInt(
                value.split(" - ")[0]);
    }

    // =====================================================
    // SELECT COMBO BY ID
    // =====================================================

    private void selectComboById(
            JComboBox<String> box,
            int id) {

        for (int i = 0; i < box.getItemCount(); i++) {

            String value = box.getItemAt(i);

            if (value.startsWith(
                    id + " - ")) {

                box.setSelectedIndex(i);

                break;
            }
        }
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
    // LABEL
    // =====================================================

    private JLabel createLabel(
            String text) {

        JLabel label = new JLabel(text);

        label.setForeground(
                Color.WHITE);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13));

        return label;
    }

    // =====================================================
    // ORANGE BUTTON
    // =====================================================

    private void styleOrangeButton(
            JButton button) {

        button.setBackground(
                new Color(255, 105, 30));

        button.setForeground(
                Color.WHITE);

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

        button.setForeground(
                Color.WHITE);

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
