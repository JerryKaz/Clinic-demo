package upsa.clinic.panels;

import upsa.clinic.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AppointmentPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;
    private JLabel statsLabel;

    public AppointmentPanel() {
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

        JLabel iconLabel = new JLabel("📅");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JLabel titleLabel = new JLabel("Appointment Management");
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
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Colors.BORDER), "Search Appointments"),
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
        statsLabel = new JLabel("Loading appointments...");
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
                new String[]{"ID", "Date", "Time", "Patient", "Doctor", "Department", "Status", "Notes"}, 0
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
        table.getColumnModel().getColumn(0).setPreferredWidth(60);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Date
        table.getColumnModel().getColumn(2).setPreferredWidth(80);  // Time
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Patient
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // Doctor
        table.getColumnModel().getColumn(5).setPreferredWidth(120); // Department
        table.getColumnModel().getColumn(6).setPreferredWidth(100); // Status
        table.getColumnModel().getColumn(7).setPreferredWidth(200); // Notes

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
        JButton addButton = createActionButton("➕ New Appointment", Colors.SUCCESS, e -> newAppointment());
        JButton editButton = createActionButton("✏️ Edit", Colors.INFO, e -> editAppointment());
        JButton cancelButton = createActionButton("❌ Cancel", Colors.DANGER, e -> cancelAppointment());
        JButton completeButton = createActionButton("✅ Complete", Colors.SUCCESS, e -> markComplete());
        JButton rescheduleButton = createActionButton("🔄 Reschedule", Colors.WARNING, e -> rescheduleAppointment());
        JButton reloadButton = createActionButton("📥 Reload", Colors.SECONDARY, e -> reload());

        actionsPanel.add(addButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(editButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(cancelButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(completeButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(rescheduleButton);
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

    private void newAppointment() {
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField dateField = new JTextField(LocalDate.now().toString());
        JTextField timeField = new JTextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        JTextField patientField = new JTextField();
        JTextField doctorField = new JTextField();
        JComboBox<String> departmentField = new JComboBox<>(new String[]{"General", "Cardiology", "Pediatrics", "Orthopedics", "Dermatology"});
        JTextArea notesArea = new JTextArea(3, 20);

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Time (HH:MM):"));
        formPanel.add(timeField);
        formPanel.add(new JLabel("Patient:"));
        formPanel.add(patientField);
        formPanel.add(new JLabel("Doctor:"));
        formPanel.add(doctorField);
        formPanel.add(new JLabel("Department:"));
        formPanel.add(departmentField);
        formPanel.add(new JLabel("Notes:"));
        formPanel.add(new JScrollPane(notesArea));

        int result = JOptionPane.showConfirmDialog(this, formPanel, "New Appointment",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate.parse(dateField.getText().trim());
                int newId = model.getRowCount() + 1;
                model.addRow(new Object[]{
                        newId,
                        dateField.getText().trim(),
                        timeField.getText().trim(),
                        patientField.getText().trim(),
                        doctorField.getText().trim(),
                        departmentField.getSelectedItem(),
                        "Scheduled",
                        notesArea.getText().trim()
                });
                updateStats();
                JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editAppointment() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        // Similar to newAppointment but with existing values
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField dateField = new JTextField(model.getValueAt(modelRow, 1).toString());
        JTextField timeField = new JTextField(model.getValueAt(modelRow, 2).toString());
        JTextField patientField = new JTextField(model.getValueAt(modelRow, 3).toString());
        JTextField doctorField = new JTextField(model.getValueAt(modelRow, 4).toString());
        JComboBox<String> departmentField = new JComboBox<>(new String[]{"General", "Cardiology", "Pediatrics", "Orthopedics", "Dermatology"});
        departmentField.setSelectedItem(model.getValueAt(modelRow, 5));
        JTextArea notesArea = new JTextArea(model.getValueAt(modelRow, 7).toString(), 3, 20);

        formPanel.add(new JLabel("Date:"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Time:"));
        formPanel.add(timeField);
        formPanel.add(new JLabel("Patient:"));
        formPanel.add(patientField);
        formPanel.add(new JLabel("Doctor:"));
        formPanel.add(doctorField);
        formPanel.add(new JLabel("Department:"));
        formPanel.add(departmentField);
        formPanel.add(new JLabel("Notes:"));
        formPanel.add(new JScrollPane(notesArea));

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Edit Appointment",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate.parse(dateField.getText().trim());
                model.setValueAt(dateField.getText().trim(), modelRow, 1);
                model.setValueAt(timeField.getText().trim(), modelRow, 2);
                model.setValueAt(patientField.getText().trim(), modelRow, 3);
                model.setValueAt(doctorField.getText().trim(), modelRow, 4);
                model.setValueAt(departmentField.getSelectedItem(), modelRow, 5);
                model.setValueAt(notesArea.getText().trim(), modelRow, 7);
                updateStats();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cancelAppointment() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to cancel.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this appointment?", "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            model.setValueAt("Cancelled", modelRow, 6);
            updateStats();
        }
    }

    private void markComplete() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to mark as complete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        model.setValueAt("Completed", modelRow, 6);
        updateStats();
    }

    private void rescheduleAppointment() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment to reschedule.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        JTextField newDate = new JTextField(LocalDate.now().plusDays(1).toString());
        JTextField newTime = new JTextField("10:00");

        Object[] fields = {"New Date (YYYY-MM-DD):", newDate, "New Time (HH:MM):", newTime};
        int result = JOptionPane.showConfirmDialog(this, fields, "Reschedule Appointment",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate.parse(newDate.getText().trim());
                model.setValueAt(newDate.getText().trim(), modelRow, 1);
                model.setValueAt(newTime.getText().trim(), modelRow, 2);
                model.setValueAt("Rescheduled", modelRow, 6);
                updateStats();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void reload() {
        searchField.setText("");
        table.setRowSorter(null);
        updateStats();
        JOptionPane.showMessageDialog(this, "Appointments refreshed!", "Reload", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadSampleData() {
        // Clear existing data
        model.setRowCount(0);

        // Add sample appointments
        model.addRow(new Object[]{1, LocalDate.now().toString(), "09:00", "Ama Mensah", "Dr. Kwame Asante", "General", "Scheduled", "Routine checkup"});
        model.addRow(new Object[]{2, LocalDate.now().plusDays(1).toString(), "11:30", "Kofi Johnson", "Dr. Esi Boateng", "Cardiology", "Scheduled", "Heart consultation"});
        model.addRow(new Object[]{3, LocalDate.now().minusDays(1).toString(), "14:15", "Yaa Addae", "Dr. Ama Mensah", "Pediatrics", "Completed", "Child vaccination"});
        model.addRow(new Object[]{4, LocalDate.now().plusDays(2).toString(), "10:45", "Kwame Ofori", "Dr. Yaw Bonsu", "Orthopedics", "Scheduled", "Knee pain evaluation"});
        model.addRow(new Object[]{5, LocalDate.now().minusDays(2).toString(), "13:20", "Akua Serwaa", "Dr. Esi Boateng", "Cardiology", "Cancelled", "Patient rescheduled"});
    }

    private void updateStats() {
        int total = model.getRowCount();
        int scheduled = 0, completed = 0, cancelled = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            String status = model.getValueAt(i, 6).toString();
            switch (status.toLowerCase()) {
                case "scheduled": scheduled++; break;
                case "completed": completed++; break;
                case "cancelled": cancelled++; break;
            }
        }

        String stats = String.format("Total: %d | Scheduled: %d | Completed: %d | Cancelled: %d",
                total, scheduled, completed, cancelled);
        statsLabel.setText(stats);
    }
}