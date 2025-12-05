package upsa.clinic.panels;

import upsa.clinic.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*; // ADD THIS IMPORT
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PatientVitalsPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;

    public PatientVitalsPanel() {
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
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Colors.BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(Colors.BACKGROUND);

        JLabel iconLabel = new JLabel("❤️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JLabel titleLabel = new JLabel("Patient Vitals Management");
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

        JButton searchButton = new JButton("🔍");
        searchButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        searchButton.setPreferredSize(new Dimension(40, 40));
        searchButton.setBackground(Colors.PRIMARY);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Colors.BACKGROUND);
        rightPanel.add(searchPanel, BorderLayout.NORTH);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));

        // Create table model for vitals
        model = new DefaultTableModel(
                new String[]{
                        "Patient ID", "Patient Name", "Temperature (°C)", "Blood Pressure",
                        "Heart Rate", "Oxygen Saturation", "Respiratory Rate", "Weight (kg)",
                        "Height (cm)", "BMI", "Last Updated", "Status"
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
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);
        table.getColumnModel().getColumn(7).setPreferredWidth(80);
        table.getColumnModel().getColumn(8).setPreferredWidth(80);
        table.getColumnModel().getColumn(9).setPreferredWidth(70);
        table.getColumnModel().getColumn(10).setPreferredWidth(120);
        table.getColumnModel().getColumn(11).setPreferredWidth(100);

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
        JButton recordButton = createActionButton("📝 Record Vitals", Colors.SUCCESS, e -> recordVitals());
        JButton viewButton = createActionButton("👁️ View History", Colors.INFO, e -> viewVitalsHistory());
        JButton updateButton = createActionButton("✏️ Update", Colors.PRIMARY, e -> updateVitals());
        JButton alertsButton = createActionButton("⚠️ Critical Alerts", Colors.MEDICAL_RED, e -> showCriticalAlerts());
        JButton reloadButton = createActionButton("🔄 Reload", Colors.SECONDARY, e -> reload());

        actionsPanel.add(recordButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(viewButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(updateButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(alertsButton);
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

    private void recordVitals() {
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField patientIdField = new JTextField();
        JTextField patientNameField = new JTextField();
        JTextField tempField = new JTextField("36.8");
        JTextField bpField = new JTextField("120/80");
        JTextField hrField = new JTextField("72");
        JTextField oxygenField = new JTextField("98");
        JTextField respRateField = new JTextField("16");
        JTextField weightField = new JTextField();
        JTextField heightField = new JTextField();

        formPanel.add(new JLabel("Patient ID:"));
        formPanel.add(patientIdField);
        formPanel.add(new JLabel("Patient Name:"));
        formPanel.add(patientNameField);
        formPanel.add(new JLabel("Temperature (°C):"));
        formPanel.add(tempField);
        formPanel.add(new JLabel("Blood Pressure:"));
        formPanel.add(bpField);
        formPanel.add(new JLabel("Heart Rate (bpm):"));
        formPanel.add(hrField);
        formPanel.add(new JLabel("Oxygen Saturation (%):"));
        formPanel.add(oxygenField);
        formPanel.add(new JLabel("Respiratory Rate:"));
        formPanel.add(respRateField);
        formPanel.add(new JLabel("Weight (kg):"));
        formPanel.add(weightField);
        formPanel.add(new JLabel("Height (cm):"));
        formPanel.add(heightField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Record Patient Vitals",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double weight = Double.parseDouble(weightField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim()) / 100; // Convert to meters
                double bmi = weight / (height * height);

                String status = "Normal";
                double temp = Double.parseDouble(tempField.getText().trim());
                if (temp > 38.0) status = "Fever";
                if (temp > 39.5) status = "High Fever";

                model.addRow(new Object[]{
                        patientIdField.getText().trim(),
                        patientNameField.getText().trim(),
                        tempField.getText().trim(),
                        bpField.getText().trim(),
                        hrField.getText().trim(),
                        oxygenField.getText().trim() + "%",
                        respRateField.getText().trim(),
                        weightField.getText().trim(),
                        heightField.getText().trim(),
                        String.format("%.1f", bmi),
                        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        status
                });

                JOptionPane.showMessageDialog(this, "Vitals recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewVitalsHistory() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to view history.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String patientName = model.getValueAt(row, 1).toString();
        String history = "Vitals History for " + patientName + "\n\n" +
                "• Last Reading: " + model.getValueAt(row, 10) + "\n" +
                "• Temperature: " + model.getValueAt(row, 2) + " °C\n" +
                "• Blood Pressure: " + model.getValueAt(row, 3) + "\n" +
                "• Heart Rate: " + model.getValueAt(row, 4) + " bpm\n" +
                "• Oxygen Saturation: " + model.getValueAt(row, 5) + "\n" +
                "• BMI: " + model.getValueAt(row, 9) + "\n" +
                "• Status: " + model.getValueAt(row, 11) + "\n\n" +
                "Trend: Stable\n" +
                "Next Checkup: " + LocalDate.now().plusDays(7);

        JOptionPane.showMessageDialog(this, history, "Vitals History - " + patientName, JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateVitals() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient to update vitals.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField tempField = new JTextField(model.getValueAt(row, 2).toString());
        JTextField bpField = new JTextField(model.getValueAt(row, 3).toString());
        JTextField hrField = new JTextField(model.getValueAt(row, 4).toString());
        JTextField oxygenField = new JTextField(model.getValueAt(row, 5).toString().replace("%", ""));
        JTextField respRateField = new JTextField(model.getValueAt(row, 6).toString());
        JTextField weightField = new JTextField(model.getValueAt(row, 7).toString());
        JTextField heightField = new JTextField(model.getValueAt(row, 8).toString());

        formPanel.add(new JLabel("Temperature (°C):"));
        formPanel.add(tempField);
        formPanel.add(new JLabel("Blood Pressure:"));
        formPanel.add(bpField);
        formPanel.add(new JLabel("Heart Rate (bpm):"));
        formPanel.add(hrField);
        formPanel.add(new JLabel("Oxygen Saturation (%):"));
        formPanel.add(oxygenField);
        formPanel.add(new JLabel("Respiratory Rate:"));
        formPanel.add(respRateField);
        formPanel.add(new JLabel("Weight (kg):"));
        formPanel.add(weightField);
        formPanel.add(new JLabel("Height (cm):"));
        formPanel.add(heightField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Update Vitals",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double weight = Double.parseDouble(weightField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim()) / 100;
                double bmi = weight / (height * height);

                String status = "Normal";
                double temp = Double.parseDouble(tempField.getText().trim());
                if (temp > 38.0) status = "Fever";
                if (temp > 39.5) status = "High Fever";

                model.setValueAt(tempField.getText().trim(), row, 2);
                model.setValueAt(bpField.getText().trim(), row, 3);
                model.setValueAt(hrField.getText().trim(), row, 4);
                model.setValueAt(oxygenField.getText().trim() + "%", row, 5);
                model.setValueAt(respRateField.getText().trim(), row, 6);
                model.setValueAt(weightField.getText().trim(), row, 7);
                model.setValueAt(heightField.getText().trim(), row, 8);
                model.setValueAt(String.format("%.1f", bmi), row, 9);
                model.setValueAt(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), row, 10);
                model.setValueAt(status, row, 11);

                JOptionPane.showMessageDialog(this, "Vitals updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showCriticalAlerts() {
        StringBuilder alerts = new StringBuilder();
        alerts.append("⚠️ CRITICAL VITALS ALERTS\n\n");

        int criticalCount = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            String status = model.getValueAt(i, 11).toString();
            if (status.equals("High Fever")) {
                alerts.append("🔥 HIGH FEVER: ").append(model.getValueAt(i, 1))
                        .append(" - ").append(model.getValueAt(i, 2)).append("°C\n");
                criticalCount++;
            }
        }

        if (criticalCount == 0) {
            alerts.append("✅ No critical alerts. All patient vitals are within normal ranges.");
        } else {
            alerts.append("\nTotal critical cases: ").append(criticalCount);
        }

        JOptionPane.showMessageDialog(this, alerts.toString(), "Critical Alerts", JOptionPane.WARNING_MESSAGE);
    }

    private void reload() {
        searchField.setText("");
        JOptionPane.showMessageDialog(this, "Vitals data refreshed!", "Reload", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadSampleData() {
        // Clear existing data
        model.setRowCount(0);

        // Add sample vitals data
        model.addRow(new Object[]{
                "PAT-1001", "Ama Mensah", "36.8", "120/80", "72", "98%", "16", "65.5", "165", "24.1",
                LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), "Normal"
        });

        model.addRow(new Object[]{
                "PAT-1002", "Kwame Ofori", "37.2", "135/85", "68", "96%", "18", "78.2", "175", "25.5",
                LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), "Normal"
        });

        model.addRow(new Object[]{
                "PAT-1003", "Esi Boateng", "39.8", "140/90", "85", "92%", "22", "61.8", "160", "24.1",
                LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE), "High Fever"
        });

        model.addRow(new Object[]{
                "PAT-1004", "Yaw Bonsu", "36.5", "118/75", "65", "99%", "15", "70.1", "172", "23.7",
                LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), "Normal"
        });
    }
}