package upsa.clinic.panels;

import upsa.clinic.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PatientsPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;
    private JLabel statsLabel;

    public PatientsPanel() {
        setLayout(new BorderLayout());
        setBackground(Colors.BACKGROUND);
        initializeUI();
    }

    private void initializeUI() {
        // Header panel
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Main content
        add(createTablePanel(), BorderLayout.CENTER);

        // Side actions panel
        add(createActionsPanel(), BorderLayout.EAST);

        // Load sample data
        loadSampleData();
        updateStats();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Colors.BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(Colors.BACKGROUND);

        JLabel iconLabel = new JLabel("👥");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JLabel titleLabel = new JLabel("Patient Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Colors.TEXT_PRIMARY);

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Colors.BACKGROUND);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 42));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Colors.BORDER), "Search Patients"),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Search icon button
        JButton searchButton = new JButton("🔍");
        searchButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        searchButton.setPreferredSize(new Dimension(40, 40));
        searchButton.setBackground(Colors.PRIMARY);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> filterTable(searchField.getText().trim()));

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable(searchField.getText().trim());
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Stats label
        statsLabel = new JLabel("Loading patient data...");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statsLabel.setForeground(Colors.TEXT_SECONDARY);
        statsLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Colors.BACKGROUND);
        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(statsLabel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));

        // Create table model with enhanced columns including student details
        model = new DefaultTableModel(
                new String[]{
                        "ID", "Full Name", "Index No", "Program", "Level", "Gender", "DOB", "Age",
                        "Blood Group", "Genotype", "Sickling", "Phone", "Condition", "Status"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        table = new JTable(model);
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(Colors.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(Colors.PRIMARY_LIGHT);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(Colors.BORDER);
        table.setShowGrid(true);

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150);  // Full Name
        table.getColumnModel().getColumn(2).setPreferredWidth(120);  // Index No
        table.getColumnModel().getColumn(3).setPreferredWidth(120);  // Program
        table.getColumnModel().getColumn(4).setPreferredWidth(60);   // Level
        table.getColumnModel().getColumn(5).setPreferredWidth(70);   // Gender
        table.getColumnModel().getColumn(6).setPreferredWidth(100);  // DOB
        table.getColumnModel().getColumn(7).setPreferredWidth(50);   // Age
        table.getColumnModel().getColumn(8).setPreferredWidth(80);   // Blood Group
        table.getColumnModel().getColumn(9).setPreferredWidth(70);   // Genotype
        table.getColumnModel().getColumn(10).setPreferredWidth(80);  // Sickling
        table.getColumnModel().getColumn(11).setPreferredWidth(120); // Phone
        table.getColumnModel().getColumn(12).setPreferredWidth(120); // Condition
        table.getColumnModel().getColumn(13).setPreferredWidth(80);  // Status

        // Add row sorter
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createActionsPanel() {
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBackground(Colors.BACKGROUND);
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 25));
        actionsPanel.setPreferredSize(new Dimension(220, 0));

        // Action buttons
        JButton addButton = createActionButton("➕ Add Patient", Colors.SUCCESS, e -> addPatient());
        JButton editButton = createActionButton("✏️ Edit", Colors.INFO, e -> editPatient());
        JButton viewButton = createActionButton("👁️ View Details", Colors.PRIMARY, e -> viewPatient());
        JButton medicalButton = createActionButton("❤️ Medical Record", Colors.MEDICAL_RED, e -> viewMedicalRecord());
        JButton deleteButton = createActionButton("🗑️ Delete", Colors.DANGER, e -> deletePatient());
        JButton reloadButton = createActionButton("🔄 Reload", Colors.SECONDARY, e -> reload());

        actionsPanel.add(addButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(editButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(viewButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(medicalButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(deleteButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(reloadButton);
        actionsPanel.add(Box.createVerticalGlue());

        return actionsPanel;
    }

    private JButton createActionButton(String text, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Colors.darken(color, 0.1));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        button.addActionListener(action);
        return button;
    }

    private void filterTable(String query) {
        if (query.isEmpty()) {
            table.setRowSorter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
        updateStats();
    }

    private void addPatient() {
        JPanel formPanel = new JPanel(new GridLayout(14, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Personal Information
        JTextField nameField = new JTextField();
        JComboBox<String> genderField = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField dobField = new JTextField(LocalDate.now().minusYears(20).format(DateTimeFormatter.ISO_LOCAL_DATE));
        JTextField phoneField = new JTextField();

        // Student Information
        JTextField indexField = new JTextField();
        JComboBox<String> programField = new JComboBox<>(new String[]{
                "BSc Information Technology", "BSc Business Administration", "BSc Accounting",
                "BSc Nursing", "BSc Public Health", "Diploma in Management"
        });
        JComboBox<String> levelField = new JComboBox<>(new String[]{"100", "200", "300", "400", "Graduate"});

        // Medical Information
        JComboBox<String> bloodGroupField = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        JComboBox<String> genotypeField = new JComboBox<>(new String[]{"AA", "AS", "SS", "AC"});
        JComboBox<String> sicklingField = new JComboBox<>(new String[]{"Negative", "Positive"});
        JTextField conditionField = new JTextField();
        JComboBox<String> statusField = new JComboBox<>(new String[]{"Active", "Inactive", "Discharged"});

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderField);
        formPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        formPanel.add(dobField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Index Number:"));
        formPanel.add(indexField);
        formPanel.add(new JLabel("Program:"));
        formPanel.add(programField);
        formPanel.add(new JLabel("Level:"));
        formPanel.add(levelField);
        formPanel.add(new JLabel("Blood Group:"));
        formPanel.add(bloodGroupField);
        formPanel.add(new JLabel("Genotype:"));
        formPanel.add(genotypeField);
        formPanel.add(new JLabel("Sickling:"));
        formPanel.add(sicklingField);
        formPanel.add(new JLabel("Medical Condition:"));
        formPanel.add(conditionField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Add New Patient",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate birthDate = LocalDate.parse(dobField.getText().trim());
                int age = Period.between(birthDate, LocalDate.now()).getYears();

                int newId = model.getRowCount() + 1;
                model.addRow(new Object[]{
                        "PAT-" + newId,
                        nameField.getText().trim(),
                        indexField.getText().trim(),
                        programField.getSelectedItem(),
                        levelField.getSelectedItem(),
                        genderField.getSelectedItem(),
                        dobField.getText().trim(),
                        age,
                        bloodGroupField.getSelectedItem(),
                        genotypeField.getSelectedItem(),
                        sicklingField.getSelectedItem(),
                        phoneField.getText().trim(),
                        conditionField.getText().trim(),
                        statusField.getSelectedItem()
                });

                updateStats();
                JOptionPane.showMessageDialog(this, "Patient added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editPatient() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        JPanel formPanel = new JPanel(new GridLayout(12, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(model.getValueAt(modelRow, 1).toString());
        JComboBox<String> genderField = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderField.setSelectedItem(model.getValueAt(modelRow, 5));

        JTextField dobField = new JTextField(model.getValueAt(modelRow, 6).toString());
        JTextField phoneField = new JTextField(model.getValueAt(modelRow, 11).toString());
        JTextField indexField = new JTextField(model.getValueAt(modelRow, 2).toString());

        JComboBox<String> programField = new JComboBox<>(new String[]{
                "BSc Information Technology", "BSc Business Administration", "BSc Accounting",
                "BSc Nursing", "BSc Public Health", "Diploma in Management"
        });
        programField.setSelectedItem(model.getValueAt(modelRow, 3));

        JComboBox<String> levelField = new JComboBox<>(new String[]{"100", "200", "300", "400", "Graduate"});
        levelField.setSelectedItem(model.getValueAt(modelRow, 4));

        JComboBox<String> bloodGroupField = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        bloodGroupField.setSelectedItem(model.getValueAt(modelRow, 8));

        JComboBox<String> genotypeField = new JComboBox<>(new String[]{"AA", "AS", "SS", "AC"});
        genotypeField.setSelectedItem(model.getValueAt(modelRow, 9));

        JComboBox<String> sicklingField = new JComboBox<>(new String[]{"Negative", "Positive"});
        sicklingField.setSelectedItem(model.getValueAt(modelRow, 10));

        JTextField conditionField = new JTextField(model.getValueAt(modelRow, 12).toString());
        JComboBox<String> statusField = new JComboBox<>(new String[]{"Active", "Inactive", "Discharged"});
        statusField.setSelectedItem(model.getValueAt(modelRow, 13));

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderField);
        formPanel.add(new JLabel("Date of Birth:"));
        formPanel.add(dobField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Index Number:"));
        formPanel.add(indexField);
        formPanel.add(new JLabel("Program:"));
        formPanel.add(programField);
        formPanel.add(new JLabel("Level:"));
        formPanel.add(levelField);
        formPanel.add(new JLabel("Blood Group:"));
        formPanel.add(bloodGroupField);
        formPanel.add(new JLabel("Genotype:"));
        formPanel.add(genotypeField);
        formPanel.add(new JLabel("Sickling:"));
        formPanel.add(sicklingField);
        formPanel.add(new JLabel("Medical Condition:"));
        formPanel.add(conditionField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Edit Patient",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate birthDate = LocalDate.parse(dobField.getText().trim());
                int age = Period.between(birthDate, LocalDate.now()).getYears();

                model.setValueAt(nameField.getText().trim(), modelRow, 1);
                model.setValueAt(indexField.getText().trim(), modelRow, 2);
                model.setValueAt(programField.getSelectedItem(), modelRow, 3);
                model.setValueAt(levelField.getSelectedItem(), modelRow, 4);
                model.setValueAt(genderField.getSelectedItem(), modelRow, 5);
                model.setValueAt(dobField.getText().trim(), modelRow, 6);
                model.setValueAt(age, modelRow, 7);
                model.setValueAt(bloodGroupField.getSelectedItem(), modelRow, 8);
                model.setValueAt(genotypeField.getSelectedItem(), modelRow, 9);
                model.setValueAt(sicklingField.getSelectedItem(), modelRow, 10);
                model.setValueAt(phoneField.getText().trim(), modelRow, 11);
                model.setValueAt(conditionField.getText().trim(), modelRow, 12);
                model.setValueAt(statusField.getSelectedItem(), modelRow, 13);

                updateStats();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewPatient() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        StringBuilder profile = new StringBuilder();
        profile.append("🏥 PATIENT PROFILE\n\n");

        String[] fields = {
                "Patient ID", "Full Name", "Index Number", "Program", "Level", "Gender",
                "Date of Birth", "Age", "Blood Group", "Genotype", "Sickling Status",
                "Phone", "Medical Condition", "Status"
        };

        for (int i = 0; i < fields.length; i++) {
            profile.append("• ").append(fields[i]).append(": ").append(model.getValueAt(modelRow, i)).append("\n");
        }

        profile.append("\n📊 Medical Summary:\n");
        profile.append("• Last Visit: ").append(LocalDate.now().minusDays(15)).append("\n");
        profile.append("• Next Appointment: ").append(LocalDate.now().plusDays(30)).append("\n");
        profile.append("• Treatment Plan: Ongoing medication and monitoring\n");

        JTextArea textArea = new JTextArea(profile.toString(), 20, 50);
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textArea.setBackground(Colors.BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Patient Profile - " + model.getValueAt(modelRow, 1),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewMedicalRecord() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to view medical record.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        String patientName = model.getValueAt(modelRow, 1).toString();
        String medicalRecord = String.format(
                "❤️ MEDICAL RECORD FOR %s\n\n" +
                        "Blood Group: %s\n" +
                        "Genotype: %s\n" +
                        "Sickling Status: %s\n" +
                        "Current Condition: %s\n\n" +
                        "Medical History:\n" +
                        "• Allergies: None reported\n" +
                        "• Chronic Conditions: %s\n" +
                        "• Current Medications: Prescribed treatment\n" +
                        "• Last Checkup: %s\n\n" +
                        "Vital Signs (Last Reading):\n" +
                        "• Blood Pressure: 120/80 mmHg\n" +
                        "• Heart Rate: 72 bpm\n" +
                        "• Temperature: 36.8°C\n" +
                        "• Oxygen Saturation: 98%%",
                patientName,
                model.getValueAt(modelRow, 8),
                model.getValueAt(modelRow, 9),
                model.getValueAt(modelRow, 10),
                model.getValueAt(modelRow, 12),
                model.getValueAt(modelRow, 12),
                LocalDate.now().minusDays(15)
        );

        JOptionPane.showMessageDialog(this, medicalRecord,
                "Medical Record - " + patientName, JOptionPane.INFORMATION_MESSAGE);
    }

    private void deletePatient() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this patient?\nPatient: " + model.getValueAt(modelRow, 1) +
                        "\nThis action cannot be undone.",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(modelRow);
            updateStats();
            JOptionPane.showMessageDialog(this, "Patient deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void reload() {
        searchField.setText("");
        table.setRowSorter(null);
        updateStats();
        JOptionPane.showMessageDialog(this, "Patient data refreshed!", "Reload", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadSampleData() {
        // Clear existing data
        model.setRowCount(0);

        // Add sample patients with enhanced student information
        model.addRow(new Object[]{
                "PAT-1001", "Ama Mensah", "UPSA2023001", "BSc Information Technology", "300", "Female",
                "1996-05-12", Period.between(LocalDate.of(1996,5,12), LocalDate.now()).getYears(),
                "O+", "AA", "Negative", "024-111-2222", "Diabetes", "Active"
        });

        model.addRow(new Object[]{
                "PAT-1002", "Kwame Ofori", "UPSA2023002", "BSc Business Administration", "200", "Male",
                "1998-08-25", Period.between(LocalDate.of(1998,8,25), LocalDate.now()).getYears(),
                "A+", "AS", "Positive", "024-333-4444", "Hypertension", "Active"
        });

        model.addRow(new Object[]{
                "PAT-1003", "Esi Boateng", "UPSA2023003", "BSc Accounting", "400", "Female",
                "1995-12-03", Period.between(LocalDate.of(1995,12,3), LocalDate.now()).getYears(),
                "B-", "AA", "Negative", "024-555-6666", "Asthma", "Active"
        });

        model.addRow(new Object[]{
                "PAT-1004", "Yaw Bonsu", "UPSA2023004", "BSc Nursing", "100", "Male",
                "2000-03-18", Period.between(LocalDate.of(2000,3,18), LocalDate.now()).getYears(),
                "AB+", "AS", "Negative", "024-777-8888", "None", "Active"
        });

        model.addRow(new Object[]{
                "PAT-1005", "Akua Serwaa", "UPSA2023005", "Diploma in Management", "Graduate", "Female",
                "1993-07-30", Period.between(LocalDate.of(1993,7,30), LocalDate.now()).getYears(),
                "O-", "AA", "Negative", "024-999-0000", "Migraine", "Inactive"
        });
    }

    private void updateStats() {
        int total = model.getRowCount();
        int active = 0, inactive = 0, discharged = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            String status = model.getValueAt(i, 13).toString();
            switch (status.toLowerCase()) {
                case "active": active++; break;
                case "inactive": inactive++; break;
                case "discharged": discharged++; break;
            }
        }

        String stats = String.format("Total: %d | Active: %d | Inactive: %d | Discharged: %d",
                total, active, inactive, discharged);
        statsLabel.setText(stats);
    }
}