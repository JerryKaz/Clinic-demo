package upsa.clinic.panels;

import upsa.clinic.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class DoctorsPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;
    private JLabel statsLabel;

    public DoctorsPanel() {
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

        JLabel iconLabel = new JLabel("⚕️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JLabel titleLabel = new JLabel("Medical Staff Management");
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
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Colors.BORDER), "Search Doctors"),
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
        searchButton.addActionListener(e -> filter(searchField.getText().trim()));

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filter(searchField.getText().trim());
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Stats label
        statsLabel = new JLabel("Loading medical staff...");
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

        // Create table model with enhanced columns
        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Specialty", "Department", "Phone", "Email", "Availability", "Status", "Experience"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        table = new JTable(model);
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Colors.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(Colors.PRIMARY_LIGHT);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(Colors.BORDER);
        table.setShowGrid(true);

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(180);  // Name
        table.getColumnModel().getColumn(2).setPreferredWidth(150);  // Specialty
        table.getColumnModel().getColumn(3).setPreferredWidth(120);  // Department
        table.getColumnModel().getColumn(4).setPreferredWidth(120);  // Phone
        table.getColumnModel().getColumn(5).setPreferredWidth(200);  // Email
        table.getColumnModel().getColumn(6).setPreferredWidth(100);  // Availability
        table.getColumnModel().getColumn(7).setPreferredWidth(100);  // Status
        table.getColumnModel().getColumn(8).setPreferredWidth(100);  // Experience

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
        JButton addButton = createActionButton("➕ Add Doctor", Colors.SUCCESS, e -> addDoctor());
        JButton editButton = createActionButton("✏️ Edit", Colors.INFO, e -> editDoctor());
        JButton viewButton = createActionButton("👁️ View Profile", Colors.PRIMARY, e -> viewDoctor());
        JButton scheduleButton = createActionButton("📅 Schedule", Colors.WARNING, e -> viewSchedule());
        JButton deactivateButton = createActionButton("⏸️ Deactivate", Colors.DANGER, e -> toggleStatus());
        JButton reloadButton = createActionButton("🔄 Reload", Colors.SECONDARY, e -> reload());

        actionsPanel.add(addButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(editButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(viewButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(scheduleButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(deactivateButton);
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

    private void filter(String query) {
        if (query.isEmpty()) {
            table.setRowSorter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
        updateStats();
    }

    private void addDoctor() {
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField("Dr. ");
        JComboBox<String> specialtyField = new JComboBox<>(new String[]{
                "General Practice", "Cardiology", "Pediatrics", "Orthopedics", "Dermatology",
                "Neurology", "Oncology", "Psychiatry", "Surgery", "Emergency Medicine"
        });
        JComboBox<String> departmentField = new JComboBox<>(new String[]{
                "General Medicine", "Cardiology", "Pediatrics", "Orthopedics", "Dermatology",
                "Neurology", "Oncology", "Psychiatry", "Surgery", "Emergency"
        });
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JComboBox<String> availabilityField = new JComboBox<>(new String[]{
                "Mon-Fri 8AM-5PM", "Mon-Sat 9AM-6PM", "24/7 On-call", "Weekends Only", "Flexible"
        });
        JComboBox<String> statusField = new JComboBox<>(new String[]{"Active", "On Leave", "Inactive"});
        JComboBox<String> experienceField = new JComboBox<>(new String[]{
                "0-2 years", "3-5 years", "6-10 years", "11-15 years", "15+ years"
        });

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Specialty:"));
        formPanel.add(specialtyField);
        formPanel.add(new JLabel("Department:"));
        formPanel.add(departmentField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Availability:"));
        formPanel.add(availabilityField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);
        formPanel.add(new JLabel("Experience:"));
        formPanel.add(experienceField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Add New Doctor",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int newId = model.getRowCount() + 1;
            model.addRow(new Object[]{
                    "DOC-" + newId,
                    nameField.getText().trim(),
                    specialtyField.getSelectedItem(),
                    departmentField.getSelectedItem(),
                    phoneField.getText().trim(),
                    emailField.getText().trim(),
                    availabilityField.getSelectedItem(),
                    statusField.getSelectedItem(),
                    experienceField.getSelectedItem()
            });
            updateStats();
            JOptionPane.showMessageDialog(this, "Doctor added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editDoctor() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(model.getValueAt(modelRow, 1).toString());
        JComboBox<String> specialtyField = new JComboBox<>(new String[]{
                "General Practice", "Cardiology", "Pediatrics", "Orthopedics", "Dermatology",
                "Neurology", "Oncology", "Psychiatry", "Surgery", "Emergency Medicine"
        });
        specialtyField.setSelectedItem(model.getValueAt(modelRow, 2));

        JComboBox<String> departmentField = new JComboBox<>(new String[]{
                "General Medicine", "Cardiology", "Pediatrics", "Orthopedics", "Dermatology",
                "Neurology", "Oncology", "Psychiatry", "Surgery", "Emergency"
        });
        departmentField.setSelectedItem(model.getValueAt(modelRow, 3));

        JTextField phoneField = new JTextField(model.getValueAt(modelRow, 4).toString());
        JTextField emailField = new JTextField(model.getValueAt(modelRow, 5).toString());

        JComboBox<String> availabilityField = new JComboBox<>(new String[]{
                "Mon-Fri 8AM-5PM", "Mon-Sat 9AM-6PM", "24/7 On-call", "Weekends Only", "Flexible"
        });
        availabilityField.setSelectedItem(model.getValueAt(modelRow, 6));

        JComboBox<String> statusField = new JComboBox<>(new String[]{"Active", "On Leave", "Inactive"});
        statusField.setSelectedItem(model.getValueAt(modelRow, 7));

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Specialty:"));
        formPanel.add(specialtyField);
        formPanel.add(new JLabel("Department:"));
        formPanel.add(departmentField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Availability:"));
        formPanel.add(availabilityField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Edit Doctor",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            model.setValueAt(nameField.getText().trim(), modelRow, 1);
            model.setValueAt(specialtyField.getSelectedItem(), modelRow, 2);
            model.setValueAt(departmentField.getSelectedItem(), modelRow, 3);
            model.setValueAt(phoneField.getText().trim(), modelRow, 4);
            model.setValueAt(emailField.getText().trim(), modelRow, 5);
            model.setValueAt(availabilityField.getSelectedItem(), modelRow, 6);
            model.setValueAt(statusField.getSelectedItem(), modelRow, 7);
            updateStats();
        }
    }

    private void viewDoctor() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        StringBuilder profile = new StringBuilder();
        profile.append("🏥 DOCTOR PROFILE\n\n");

        String[] fields = {"ID", "Name", "Specialty", "Department", "Phone", "Email", "Availability", "Status", "Experience"};
        for (int i = 0; i < fields.length; i++) {
            profile.append("• ").append(fields[i]).append(": ").append(model.getValueAt(modelRow, i)).append("\n");
        }

        profile.append("\n📊 Recent Activity:\n");
        profile.append("• Appointments this week: ").append(new Random().nextInt(20) + 5).append("\n");
        profile.append("• Patients treated: ").append(new Random().nextInt(100) + 50).append("\n");
        profile.append("• Specializations: ").append(model.getValueAt(modelRow, 2)).append("\n");

        JTextArea textArea = new JTextArea(profile.toString(), 15, 40);
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textArea.setBackground(Colors.BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 300));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Doctor Profile - " + model.getValueAt(modelRow, 1),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewSchedule() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor to view schedule.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        String doctorName = model.getValueAt(modelRow, 1).toString();
        String scheduleInfo = String.format(
                "📅 SCHEDULE FOR %s\n\n" +
                        "Availability: %s\n" +
                        "Status: %s\n\n" +
                        "Upcoming Appointments:\n" +
                        "• Monday: 3 appointments\n" +
                        "• Tuesday: 5 appointments\n" +
                        "• Wednesday: 2 appointments\n" +
                        "• Thursday: 4 appointments\n" +
                        "• Friday: 6 appointments\n\n" +
                        "Next Available Slot: Tomorrow 10:00 AM",
                doctorName, model.getValueAt(modelRow, 6), model.getValueAt(modelRow, 7)
        );

        JOptionPane.showMessageDialog(this, scheduleInfo,
                "Schedule - " + doctorName, JOptionPane.INFORMATION_MESSAGE);
    }

    private void toggleStatus() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        String currentStatus = model.getValueAt(modelRow, 7).toString();
        String newStatus = currentStatus.equals("Active") ? "Inactive" : "Active";

        int confirm = JOptionPane.showConfirmDialog(this,
                "Change status of " + model.getValueAt(modelRow, 1) + " from " +
                        currentStatus + " to " + newStatus + "?", "Confirm Status Change",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            model.setValueAt(newStatus, modelRow, 7);
            updateStats();
        }
    }

    private void reload() {
        searchField.setText("");
        table.setRowSorter(null);
        updateStats();
        JOptionPane.showMessageDialog(this, "Doctors list refreshed!", "Reload", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadSampleData() {
        // Clear existing data
        model.setRowCount(0);

        // Add sample doctors with enhanced information
        model.addRow(new Object[]{"DOC-1001", "Dr. Ama Mensah", "General Surgery", "Surgery", "024-111-2222", "amensah@upsaclinic.com", "Mon-Fri 8AM-5PM", "Active", "15+ years"});
        model.addRow(new Object[]{"DOC-1002", "Dr. Kwame Asante", "Pediatrics", "Pediatrics", "024-333-4444", "kasante@upsaclinic.com", "Mon-Sat 9AM-6PM", "Active", "11-15 years"});
        model.addRow(new Object[]{"DOC-1003", "Dr. Esi Boateng", "Cardiology", "Cardiology", "024-555-6666", "eboateng@upsaclinic.com", "24/7 On-call", "Active", "15+ years"});
        model.addRow(new Object[]{"DOC-1004", "Dr. Yaw Bonsu", "Orthopedics", "Orthopedics", "024-777-8888", "ybonsu@upsaclinic.com", "Mon-Fri 8AM-5PM", "On Leave", "6-10 years"});
        model.addRow(new Object[]{"DOC-1005", "Dr. Akua Serwaa", "Dermatology", "Dermatology", "024-999-0000", "aserwaa@upsaclinic.com", "Weekends Only", "Active", "3-5 years"});
    }

    private void updateStats() {
        int total = model.getRowCount();
        int active = 0, onLeave = 0, inactive = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            String status = model.getValueAt(i, 7).toString();
            switch (status.toLowerCase()) {
                case "active": active++; break;
                case "on leave": onLeave++; break;
                case "inactive": inactive++; break;
            }
        }

        String stats = String.format("Total: %d | Active: %d | On Leave: %d | Inactive: %d",
                total, active, onLeave, inactive);
        statsLabel.setText(stats);
    }
}