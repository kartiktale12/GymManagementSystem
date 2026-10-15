package com.gym.payments;

import com.gym.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PaymentFrame extends JFrame {

    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JLabel revenueLabel;

    public PaymentFrame() {

        setTitle("FITZONE GYM - Payment Management");
        setSize(1150, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createUI();
        loadPayments();
        loadRevenue();
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

        JLabel title = new JLabel("PAYMENT MANAGEMENT");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28));

        title.setForeground(Color.WHITE);

        JButton addButton = new JButton("+ ADD PAYMENT");

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
        // REVENUE CARD
        // =================================================

        JPanel revenuePanel = new JPanel(
                new BorderLayout());

        revenuePanel.setBackground(
                new Color(35, 35, 45));

        revenuePanel.setBorder(
                new EmptyBorder(
                        15, 20, 15, 20));

        JLabel revenueTitle = new JLabel("TOTAL PAID REVENUE");

        revenueTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14));

        revenueTitle.setForeground(
                Color.LIGHT_GRAY);

        revenueLabel = new JLabel("₹0.00");

        revenueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        25));

        revenueLabel.setForeground(
                new Color(255, 105, 30));

        revenuePanel.add(
                revenueTitle,
                BorderLayout.WEST);

        revenuePanel.add(
                revenueLabel,
                BorderLayout.EAST);

        centerPanel.add(
                revenuePanel,
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
                        "Payment ID",
                        "Member",
                        "Amount",
                        "Date",
                        "Method",
                        "Status"
                },
                0);

        paymentTable = new JTable(tableModel);

        paymentTable.setRowHeight(35);

        paymentTable.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14));

        paymentTable.setBackground(
                new Color(35, 35, 45));

        paymentTable.setForeground(
                Color.WHITE);

        paymentTable.setGridColor(
                new Color(65, 65, 75));

        paymentTable.setSelectionBackground(
                new Color(255, 105, 30));

        paymentTable.setSelectionForeground(
                Color.WHITE);

        paymentTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                14));

        paymentTable.getTableHeader()
                .setBackground(
                        new Color(45, 45, 55));

        paymentTable.getTableHeader()
                .setForeground(
                        Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(
                paymentTable);

        // =================================================
        // CENTER CONTAINER
        // =================================================

        JPanel tableContainer = new JPanel(new BorderLayout());

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
                e -> showPaymentForm(null));

        searchButton.addActionListener(
                e -> searchPayments());

        refreshButton.addActionListener(
                e -> {
                    searchField.setText("");
                    loadPayments();
                    loadRevenue();
                });

        editButton.addActionListener(
                e -> editPayment());

        deleteButton.addActionListener(
                e -> deletePayment());
    }

    // =====================================================
    // LOAD PAYMENTS
    // =====================================================

    private void loadPayments() {

        String sql = "SELECT p.payment_id, " +
                "m.member_name, " +
                "p.amount, " +
                "p.payment_date, " +
                "p.payment_method, " +
                "p.payment_status " +
                "FROM payment p " +
                "JOIN member m " +
                "ON p.member_id = m.member_id " +
                "ORDER BY p.payment_id DESC";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()) {

            tableModel.setRowCount(0);

            while (rs.next()) {

                tableModel.addRow(
                        new Object[] {
                                rs.getInt("payment_id"),
                                rs.getString("member_name"),
                                rs.getDouble("amount"),
                                rs.getDate("payment_date"),
                                rs.getString("payment_method"),
                                rs.getString("payment_status")
                        });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // LOAD REVENUE
    // =====================================================

    private void loadRevenue() {

        String sql = "SELECT SUM(amount) AS total_revenue " +
                "FROM payment " +
                "WHERE payment_status = 'Paid'";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                double revenue = rs.getDouble(
                        "total_revenue");

                revenueLabel.setText(
                        "₹" +
                                String.format(
                                        "%.2f",
                                        revenue));
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // SEARCH PAYMENTS
    // =====================================================

    private void searchPayments() {

        String search = searchField.getText().trim();

        if (search.isEmpty()) {

            loadPayments();
            return;
        }

        String sql = "SELECT p.payment_id, " +
                "m.member_name, " +
                "p.amount, " +
                "p.payment_date, " +
                "p.payment_method, " +
                "p.payment_status " +
                "FROM payment p " +
                "JOIN member m " +
                "ON p.member_id = m.member_id " +
                "WHERE m.member_name LIKE ? " +
                "OR p.payment_method LIKE ? " +
                "OR p.payment_status LIKE ? " +
                "ORDER BY p.payment_id DESC";

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
                                rs.getInt("payment_id"),
                                rs.getString("member_name"),
                                rs.getDouble("amount"),
                                rs.getDate("payment_date"),
                                rs.getString("payment_method"),
                                rs.getString("payment_status")
                        });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // PAYMENT FORM
    // =====================================================

    private void showPaymentForm(
            Integer paymentId) {

        JDialog dialog = new JDialog(
                this,
                paymentId == null
                        ? "Add Payment"
                        : "Edit Payment",
                true);

        dialog.setSize(500, 550);
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
                paymentId == null
                        ? "ADD NEW PAYMENT"
                        : "EDIT PAYMENT");

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

        JLabel amountLabel = createLabel("Amount");

        JTextField amountField = createTextField();

        panel.add(amountLabel);
        panel.add(amountField);

        panel.add(
                Box.createVerticalStrut(15));

        JLabel dateLabel = createLabel("Payment Date");

        JTextField dateField = createTextField();

        dateField.setText(
                java.time.LocalDate
                        .now()
                        .toString());

        panel.add(dateLabel);
        panel.add(dateField);

        panel.add(
                Box.createVerticalStrut(15));

        JLabel methodLabel = createLabel("Payment Method");

        JComboBox<String> methodBox = new JComboBox<>(
                new String[] {
                        "UPI",
                        "Cash",
                        "Card",
                        "Net Banking"
                });

        methodBox.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40));

        panel.add(methodLabel);
        panel.add(methodBox);

        panel.add(
                Box.createVerticalStrut(15));

        JLabel statusLabel = createLabel("Payment Status");

        JComboBox<String> statusBox = new JComboBox<>(
                new String[] {
                        "Paid",
                        "Pending"
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
                paymentId == null
                        ? "ADD PAYMENT"
                        : "UPDATE PAYMENT");

        styleOrangeButton(saveButton);

        saveButton.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        panel.add(saveButton);

        dialog.add(panel);

        // =================================================
        // LOAD EXISTING PAYMENT
        // =================================================

        if (paymentId != null) {

            loadPaymentData(
                    paymentId,
                    memberBox,
                    amountField,
                    dateField,
                    methodBox,
                    statusBox);
        }

        // =================================================
        // SAVE
        // =================================================

        saveButton.addActionListener(e -> {

            try {

                int memberId = getSelectedMemberId(
                        memberBox);

                double amount = Double.parseDouble(
                        amountField
                                .getText()
                                .trim());

                if (amount <= 0) {

                    JOptionPane.showMessageDialog(
                            dialog,
                            "Amount must be greater than 0.");

                    return;
                }

                Date paymentDate = Date.valueOf(
                        dateField
                                .getText()
                                .trim());

                String method = methodBox
                        .getSelectedItem()
                        .toString();

                String status = statusBox
                        .getSelectedItem()
                        .toString();

                if (paymentId == null) {

                    addPayment(
                            memberId,
                            amount,
                            paymentDate,
                            method,
                            status);

                } else {

                    updatePayment(
                            paymentId,
                            memberId,
                            amount,
                            paymentDate,
                            method,
                            status);
                }

                dialog.dispose();

                loadPayments();
                loadRevenue();

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Enter a valid amount.");

            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Date must be in YYYY-MM-DD format.");
            }
        });

        dialog.setVisible(true);
    }

    // =====================================================
    // LOAD MEMBERS DROPDOWN
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
                                + rs.getString("member_name"));
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

        String selected = box.getSelectedItem()
                .toString();

        return Integer.parseInt(
                selected.split(" - ")[0]);
    }

    // =====================================================
    // ADD PAYMENT
    // =====================================================

    private void addPayment(
            int memberId,
            double amount,
            Date date,
            String method,
            String status) {

        String sql = "INSERT INTO payment " +
                "(member_id, amount, payment_date, " +
                "payment_method, payment_status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            ps.setDouble(2, amount);
            ps.setDate(3, date);
            ps.setString(4, method);
            ps.setString(5, status);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Payment added successfully!");

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // UPDATE PAYMENT
    // =====================================================

    private void updatePayment(
            int paymentId,
            int memberId,
            double amount,
            Date date,
            String method,
            String status) {

        String sql = "UPDATE payment SET " +
                "member_id=?, " +
                "amount=?, " +
                "payment_date=?, " +
                "payment_method=?, " +
                "payment_status=? " +
                "WHERE payment_id=?";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, memberId);
            ps.setDouble(2, amount);
            ps.setDate(3, date);
            ps.setString(4, method);
            ps.setString(5, status);
            ps.setInt(6, paymentId);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Payment updated successfully!");

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // EDIT PAYMENT
    // =====================================================

    private void editPayment() {

        int row = paymentTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a payment first.");

            return;
        }

        int paymentId = (int) paymentTable
                .getValueAt(row, 0);

        showPaymentForm(paymentId);
    }

    // =====================================================
    // LOAD PAYMENT DATA
    // =====================================================

    private void loadPaymentData(
            int paymentId,
            JComboBox<String> memberBox,
            JTextField amountField,
            JTextField dateField,
            JComboBox<String> methodBox,
            JComboBox<String> statusBox) {

        String sql = "SELECT * FROM payment " +
                "WHERE payment_id=?";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, paymentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int memberId = rs.getInt("member_id");

                amountField.setText(
                        String.valueOf(
                                rs.getDouble("amount")));

                dateField.setText(
                        rs.getDate(
                                "payment_date").toString());

                methodBox.setSelectedItem(
                        rs.getString(
                                "payment_method"));

                statusBox.setSelectedItem(
                        rs.getString(
                                "payment_status"));

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
    // DELETE PAYMENT
    // =====================================================

    private void deletePayment() {

        int row = paymentTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a payment first.");

            return;
        }

        int paymentId = (int) paymentTable
                .getValueAt(row, 0);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete this payment?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {

            return;
        }

        String sql = "DELETE FROM payment " +
                "WHERE payment_id=?";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    paymentId);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Payment deleted successfully!");

            loadPayments();
            loadRevenue();

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