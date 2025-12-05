package upsa.clinic.panels;

import upsa.clinic.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*; // ADD THIS IMPORT
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BedManagementPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;
    private JLabel statsLabel;

    public BedManagementPanel() {
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

        JLabel iconLabel = new JLabel("🛏️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JLabel titleLabel = new JLabel("Bed Management");
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
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Colors.BORDER), "Search Beds/Patients"),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JButton searchButton = new JButton("🔍");
        searchButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        searchButton.setPreferredSize(new Dimension(40, 40));
        searchButton.setBackground(Colors.PRIMARY);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Stats label
        statsLabel = new JLabel("Loading bed information...");
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

        // Create table model for bed management
        model = new DefaultTableModel(
                new String[]{
                        "Bed No", "Ward", "Patient ID", "Patient Name", "Admission Date",
                        "Condition", "Doctor", "Status", "Priority"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);
        table.getColumnModel().getColumn(8).setPreferredWidth(80);

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
        JButton assignButton = createActionButton("➕ Assign Bed", Colors.SUCCESS, e -> assignBed());
        JButton dischargeButton = createActionButton("🏥 Discharge", Colors.INFO, e -> dischargePatient());
        JButton transferButton = createActionButton("🔄 Transfer", Colors.PRIMARY, e -> transferBed());
        JButton availableButton = createActionButton("📊 Available Beds", Colors.WARNING, e -> showAvailableBeds());
        JButton maintenanceButton = createActionButton("🔧 Maintenance", Colors.SECONDARY, e -> maintenanceMode());
        JButton reloadButton = createActionButton("🔄 Reload", Colors.SECONDARY, e -> reload());

        actionsPanel.add(assignButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(dischargeButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(transferButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(availableButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(maintenanceButton);
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

    private void assignBed() {
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField bedNoField = new JTextField();
        JComboBox<String> wardField = new JComboBox<>(new String[]{
                "General Ward", "ICU", "Maternity", "Pediatrics", "Surgical", "Private Room"
        });
        JTextField patientIdField = new JTextField();
        JTextField patientNameField = new JTextField();
        JTextField conditionField = new JTextField();
        JTextField doctorField = new JTextField();
        JComboBox<String> priorityField = new JComboBox<>(new String[]{"Low", "Medium", "High", "Critical"});

        formPanel.add(new JLabel("Bed Number:"));
        formPanel.add(bedNoField);
        formPanel.add(new JLabel("Ward:"));
        formPanel.add(wardField);
        formPanel.add(new JLabel("Patient ID:"));
        formPanel.add(patientIdField);
        formPanel.add(new JLabel("Patient Name:"));
        formPanel.add(patientNameField);
        formPanel.add(new JLabel("Condition:"));
        formPanel.add(conditionField);
        formPanel.add(new JLabel("Doctor:"));
        formPanel.add(doctorField);
        formPanel.add(new JLabel("Priority:"));
        formPanel.add(priorityField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Assign Bed to Patient",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            model.addRow(new Object[]{
                    bedNoField.getText().trim(),
                    wardField.getSelectedItem(),
                    patientIdField.getText().trim(),
                    patientNameField.getText().trim(),
                    LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    conditionField.getText().trim(),
                    doctorField.getText().trim(),
                    "Occupied",
                    priorityField.getSelectedItem()
            });

            updateStats();
            JOptionPane.showMessageDialog(this, "Bed assigned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void dischargePatient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to discharge.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String patientName = model.getValueAt(row, 3).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Discharge " + patientName + " from bed " + model.getValueAt(row, 0) + "?",
                "Confirm Discharge", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            updateStats();
            JOptionPane.showMessageDialog(this, "Patient discharged successfully!", "Discharged", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void transferBed() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to transfer.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<String> newWardField = new JComboBox<>(new String[]{
                "General Ward", "ICU", "Maternity", "Pediatrics", "Surgical", "Private Room"
        });
        JTextField newBedField = new JTextField();

        Object[] message = {
                "Current Bed: " + model.getValueAt(row, 0),
                "Current Ward: " + model.getValueAt(row, 1),
                "New Ward:", newWardField,
                "New Bed Number:", newBedField
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Transfer Patient",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            model.setValueAt(newBedField.getText().trim(), row, 0);
            model.setValueAt(newWardField.getSelectedItem(), row, 1);
            JOptionPane.showMessageDialog(this, "Patient transferred successfully!", "Transfer Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showAvailableBeds() {
        int totalBeds = 50; // Example total beds
        int occupiedBeds = model.getRowCount();
        int availableBeds = totalBeds - occupiedBeds;

        String availability = "🏥 BED AVAILABILITY\n\n" +
                "Total Beds: " + totalBeds + "\n" +
                "Occupied: " + occupiedBeds + "\n" +
                "Available: " + availableBeds + "\n" +
                "Occupancy Rate: " + String.format("%.1f", (occupiedBeds * 100.0 / totalBeds)) + "%\n\n" +
                "Available Wards:\n" +
                "• General Ward: " + (15 - getWardCount("General Ward")) + " beds\n" +
                "• ICU: " + (8 - getWardCount("ICU")) + " beds\n" +
                "• Maternity: " + (10 - getWardCount("Maternity")) + " beds\n" +
                "• Pediatrics: " + (12 - getWardCount("Pediatrics")) + " beds";

        JOptionPane.showMessageDialog(this, availability, "Bed Availability", JOptionPane.INFORMATION_MESSAGE);
    }

    private int getWardCount(String ward) {
        int count = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1).equals(ward)) {
                count++;
            }
        }
        return count;
    }

    private void maintenanceMode() {
        JOptionPane.showMessageDialog(this,
                "Maintenance mode activated.\n\n" +
                        "This feature allows you to:\n" +
                        "• Mark beds for cleaning\n" +
                        "• Schedule maintenance\n" +
                        "• Track bed repairs\n" +
                        "• Update bed status",
                "Maintenance Mode", JOptionPane.INFORMATION_MESSAGE);
    }

    private void reload() {
        searchField.setText("");
        updateStats();
        JOptionPane.showMessageDialog(this, "Bed data refreshed!", "Reload", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadSampleData() {
        // Clear existing data
        model.setRowCount(0);

        // Add sample bed data
        model.addRow(new Object[]{
                "B-101", "General Ward", "PAT-1001", "Ama Mensah",
                LocalDate.now().minusDays(2).format(DateTimeFormatter.ISO_LOCAL_DATE),
                "Pneumonia", "Dr. Kwesi Mensah", "Occupied", "Medium"
        });

        model.addRow(new Object[]{
                "ICU-05", "ICU", "PAT-1003", "Esi Boateng",
                LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE),
                "High Fever", "Dr. Abena Osei", "Occupied", "Critical"
        });

        model.addRow(new Object[]{
                "P-201", "Pediatrics", "PAT-1004", "Yaw Bonsu",
                LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                "Asthma", "Dr. Kofi Asare", "Occupied", "High"
        });

        model.addRow(new Object[]{
                "M-301", "Maternity", "PAT-1005", "Akua Serwaa",
                LocalDate.now().minusDays(3).format(DateTimeFormatter.ISO_LOCAL_DATE),
                "Delivery", "Dr. Nana Ama", "Occupied", "Medium"
        });
    }

    private void updateStats() {
        int totalBeds = 50;
        int occupied = model.getRowCount();
        int available = totalBeds - occupied;
        double occupancyRate = (occupied * 100.0) / totalBeds;

        String stats = String.format("Beds: %d/%d (%.1f%%) | Available: %d",
                occupied, totalBeds, occupancyRate, available);
        statsLabel.setText(stats);
    }
}