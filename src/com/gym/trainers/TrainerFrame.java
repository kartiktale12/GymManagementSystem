package com.gym.trainers;

import com.gym.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class TrainerFrame extends JFrame {

    private JTable trainerTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public TrainerFrame() {

        setTitle("FITZONE GYM - Trainer Management");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createUI();
        loadTrainers();
    }

    // =====================================================
    // CREATE UI
    // =====================================================

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18, 18, 24));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(18, 18, 24));
        header.setBorder(new EmptyBorder(25, 30, 20, 30));

        JLabel title = new JLabel("TRAINER MANAGEMENT");

        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JButton addButton = new JButton("+ ADD TRAINER");
        styleOrangeButton(addButton);

        header.add(title, BorderLayout.WEST);
        header.add(addButton, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // CENTER
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(18, 18, 24));
        centerPanel.setBorder(new EmptyBorder(0, 30, 30, 30));

        // SEARCH
        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 10));

        searchPanel.setBackground(new Color(30, 30, 40));

        JLabel searchLabel = new JLabel("Search:");

        searchLabel.setFont(
                new Font("Arial", Font.BOLD, 14));

        searchLabel.setForeground(Color.WHITE);

        searchField = new JTextField();

        searchField.setPreferredSize(
                new Dimension(300, 38));

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
                        "Trainer Name",
                        "Phone",
                        "Specialization",
                        "Experience (Years)"
                },
                0);

        trainerTable = new JTable(tableModel);

        trainerTable.setRowHeight(35);

        trainerTable.setFont(
                new Font("Arial", Font.PLAIN, 14));

        trainerTable.setBackground(
                new Color(35, 35, 45));

        trainerTable.setForeground(Color.WHITE);

        trainerTable.setGridColor(
                new Color(65, 65, 75));

        trainerTable.setSelectionBackground(
                new Color(255, 105, 30));

        trainerTable.setSelectionForeground(Color.WHITE);

        trainerTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14));

        trainerTable.getTableHeader().setBackground(
                new Color(45, 45, 55));

        trainerTable.getTableHeader().setForeground(
                Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(trainerTable);

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER);

        // =================================================
        // BOTTOM BUTTONS
        // =================================================

        JPanel bottomPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT));

        bottomPanel.setBackground(
                new Color(18, 18, 24));

        JButton editButton = new JButton("EDIT");
        styleOrangeButton(editButton);

        JButton deleteButton = new JButton("DELETE");

        deleteButton.setBackground(
                new Color(190, 55, 55));

        deleteButton.setForeground(Color.WHITE);
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
        // BUTTON ACTIONS
        // =================================================

        addButton.addActionListener(
                e -> showTrainerForm(null));

        searchButton.addActionListener(
                e -> searchTrainers());

        refreshButton.addActionListener(
                e -> {
                    searchField.setText("");
                    loadTrainers();
                });

        editButton.addActionListener(
                e -> editTrainer());

        deleteButton.addActionListener(
                e -> deleteTrainer());
    }

    // =====================================================
    // LOAD TRAINERS
    // =====================================================

    private void loadTrainers() {

        String sql = "SELECT trainer_id, trainer_name, " +
                "phone, specialization, experience_years " +
                "FROM trainer " +
                "ORDER BY trainer_id";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql);

                ResultSet rs = ps.executeQuery()) {

            tableModel.setRowCount(0);

            while (rs.next()) {

                tableModel.addRow(
                        new Object[] {
                                rs.getInt("trainer_id"),
                                rs.getString("trainer_name"),
                                rs.getString("phone"),
                                rs.getString("specialization"),
                                rs.getInt("experience_years")
                        });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // SEARCH TRAINERS
    // =====================================================

    private void searchTrainers() {

        String search = searchField.getText().trim();

        if (search.isEmpty()) {

            loadTrainers();
            return;
        }

        String sql = "SELECT trainer_id, trainer_name, " +
                "phone, specialization, experience_years " +
                "FROM trainer " +
                "WHERE trainer_name LIKE ? " +
                "OR phone LIKE ? " +
                "OR specialization LIKE ? " +
                "ORDER BY trainer_id";

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
                                rs.getInt("trainer_id"),
                                rs.getString("trainer_name"),
                                rs.getString("phone"),
                                rs.getString("specialization"),
                                rs.getInt("experience_years")
                        });
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // ADD / EDIT FORM
    // =====================================================

    private void showTrainerForm(
            Integer trainerId) {

        JDialog dialog = new JDialog(
                this,
                trainerId == null
                        ? "Add Trainer"
                        : "Edit Trainer",
                true);

        dialog.setSize(500, 520);
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
                trainerId == null
                        ? "ADD NEW TRAINER"
                        : "EDIT TRAINER");

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

        JTextField specializationField = createTextField();

        JTextField experienceField = createTextField();

        addField(
                panel,
                "Trainer Name",
                nameField);

        addField(
                panel,
                "Phone",
                phoneField);

        addField(
                panel,
                "Specialization",
                specializationField);

        addField(
                panel,
                "Experience (Years)",
                experienceField);

        JButton saveButton = new JButton(
                trainerId == null
                        ? "ADD TRAINER"
                        : "UPDATE TRAINER");

        styleOrangeButton(saveButton);

        saveButton.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        panel.add(
                Box.createVerticalStrut(10));

        panel.add(saveButton);

        dialog.add(panel);

        // =================================================
        // LOAD EXISTING DATA
        // =================================================

        if (trainerId != null) {

            loadTrainerData(
                    trainerId,
                    nameField,
                    phoneField,
                    specializationField,
                    experienceField);
        }

        // =================================================
        // SAVE
        // =================================================

        saveButton.addActionListener(e -> {

            String name = nameField.getText().trim();

            String phone = phoneField.getText().trim();

            String specialization = specializationField
                    .getText()
                    .trim();

            String experienceText = experienceField
                    .getText()
                    .trim();

            if (name.isEmpty()
                    ||
                    phone.isEmpty()
                    ||
                    specialization.isEmpty()
                    ||
                    experienceText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Please fill all fields.");

                return;
            }

            try {

                int experience = Integer.parseInt(
                        experienceText);

                if (experience < 0) {

                    JOptionPane.showMessageDialog(
                            dialog,
                            "Experience cannot be negative.");

                    return;
                }

                if (trainerId == null) {

                    addTrainer(
                            name,
                            phone,
                            specialization,
                            experience);

                } else {

                    updateTrainer(
                            trainerId,
                            name,
                            phone,
                            specialization,
                            experience);
                }

                dialog.dispose();

                loadTrainers();

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Experience must be a valid number.");
            }
        });

        dialog.setVisible(true);
    }

    // =====================================================
    // ADD TRAINER
    // =====================================================

    private void addTrainer(
            String name,
            String phone,
            String specialization,
            int experience) {

        String sql = "INSERT INTO trainer " +
                "(trainer_name, phone, specialization, experience_years) " +
                "VALUES (?, ?, ?, ?)";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, specialization);
            ps.setInt(4, experience);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Trainer added successfully!");

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // UPDATE TRAINER
    // =====================================================

    private void updateTrainer(
            int id,
            String name,
            String phone,
            String specialization,
            int experience) {

        String sql = "UPDATE trainer SET " +
                "trainer_name=?, " +
                "phone=?, " +
                "specialization=?, " +
                "experience_years=? " +
                "WHERE trainer_id=?";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, specialization);
            ps.setInt(4, experience);
            ps.setInt(5, id);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Trainer updated successfully!");

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // DELETE TRAINER
    // =====================================================

    private void deleteTrainer() {

        int row = trainerTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a trainer first.");

            return;
        }

        int id = (int) trainerTable
                .getValueAt(row, 0);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this trainer?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {

            return;
        }

        String sql = "DELETE FROM trainer " +
                "WHERE trainer_id=?";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Trainer deleted successfully!");

            loadTrainers();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cannot delete this trainer.\n" +
                            "The trainer may be assigned to members.",
                    "Delete Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================
    // EDIT TRAINER
    // =====================================================

    private void editTrainer() {

        int row = trainerTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a trainer first.");

            return;
        }

        int id = (int) trainerTable
                .getValueAt(row, 0);

        showTrainerForm(id);
    }

    // =====================================================
    // LOAD TRAINER DATA
    // =====================================================

    private void loadTrainerData(
            int id,
            JTextField nameField,
            JTextField phoneField,
            JTextField specializationField,
            JTextField experienceField) {

        String sql = "SELECT trainer_id, trainer_name, " +
                "phone, specialization, experience_years " +
                "FROM trainer " +
                "WHERE trainer_id=?";

        try (
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                nameField.setText(
                        rs.getString(
                                "trainer_name"));

                phoneField.setText(
                        rs.getString(
                                "phone"));

                specializationField.setText(
                        rs.getString(
                                "specialization"));

                experienceField.setText(
                        String.valueOf(
                                rs.getInt(
                                        "experience_years")));
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =====================================================
    // CREATE TEXT FIELD
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
    // ADD FIELD
    // =====================================================

    private void addField(
            JPanel panel,
            String labelText,
            JTextField field) {

        JLabel label = new JLabel(labelText);

        label.setForeground(Color.WHITE);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13));

        panel.add(label);

        panel.add(
                Box.createVerticalStrut(5));

        panel.add(field);

        panel.add(
                Box.createVerticalStrut(12));
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

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR));
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