package upsa.clinic.panels;

import upsa.clinic.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BillingPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;
    private JLabel statsLabel;
    private DecimalFormat currencyFormat = new DecimalFormat("₵#,##0.00");
    private double totalRevenue = 0.0;
    private double pendingAmount = 0.0;

    public BillingPanel() {
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

        JLabel iconLabel = new JLabel("💰");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JLabel titleLabel = new JLabel("Billing & Invoices");
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
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Colors.BORDER), "Search Invoices"),
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
        statsLabel = new JLabel("Loading billing information...");
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
                new String[]{"Invoice#", "Patient", "Service", "Date", "Amount", "Status", "Due Date", "Insurance"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
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
        table.getColumnModel().getColumn(0).setPreferredWidth(80);   // Invoice#
        table.getColumnModel().getColumn(1).setPreferredWidth(150);  // Patient
        table.getColumnModel().getColumn(2).setPreferredWidth(180);  // Service
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // Date
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // Amount
        table.getColumnModel().getColumn(5).setPreferredWidth(100);  // Status
        table.getColumnModel().getColumn(6).setPreferredWidth(100);  // Due Date
        table.getColumnModel().getColumn(7).setPreferredWidth(120);  // Insurance

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
        JButton newInvoiceButton = createActionButton("➕ New Invoice", Colors.SUCCESS, e -> createInvoice());
        JButton markPaidButton = createActionButton("✅ Mark Paid", Colors.SUCCESS, e -> markPaid());
        JButton markPendingButton = createActionButton("⏳ Mark Pending", Colors.WARNING, e -> markPending());
        JButton printButton = createActionButton("🖨️ Print", Colors.INFO, e -> printInvoice());
        JButton exportButton = createActionButton("📤 Export", Colors.SECONDARY, e -> exportInvoices());
        JButton deleteButton = createActionButton("🗑️ Delete", Colors.DANGER, e -> deleteInvoice());
        JButton reloadButton = createActionButton("🔄 Reload", Colors.SECONDARY, e -> reload());

        actionsPanel.add(newInvoiceButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(markPaidButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(markPendingButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(printButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(exportButton);
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

    private void filter(String query) {
        if (query.isEmpty()) {
            table.setRowSorter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
        }
        updateStats();
    }

    private void createInvoice() {
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField invoiceField = new JTextField(String.valueOf(1000 + model.getRowCount() + 1));
        invoiceField.setEditable(false);
        JTextField patientField = new JTextField();
        JComboBox<String> serviceField = new JComboBox<>(new String[]{
                "Consultation", "Laboratory Test", "X-Ray", "Ultrasound", "Surgery",
                "Medication", "Therapy", "Emergency Care", "Check-up", "Vaccination"
        });
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JTextField amountField = new JTextField();
        JComboBox<String> statusField = new JComboBox<>(new String[]{"Unpaid", "Paid", "Pending", "Partially Paid"});
        JTextField dueDateField = new JTextField(LocalDate.now().plusDays(30).toString());
        JComboBox<String> insuranceField = new JComboBox<>(new String[]{"None", "NHIS", "Private", "Corporate"});

        formPanel.add(new JLabel("Invoice #:"));
        formPanel.add(invoiceField);
        formPanel.add(new JLabel("Patient:"));
        formPanel.add(patientField);
        formPanel.add(new JLabel("Service:"));
        formPanel.add(serviceField);
        formPanel.add(new JLabel("Date:"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Amount (₵):"));
        formPanel.add(amountField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);
        formPanel.add(new JLabel("Due Date:"));
        formPanel.add(dueDateField);
        formPanel.add(new JLabel("Insurance:"));
        formPanel.add(insuranceField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Create New Invoice",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate.parse(dateField.getText().trim());
                LocalDate.parse(dueDateField.getText().trim());

                double amount = Double.parseDouble(amountField.getText().trim());
                String formattedAmount = currencyFormat.format(amount);

                model.addRow(new Object[]{
                        invoiceField.getText().trim(),
                        patientField.getText().trim(),
                        serviceField.getSelectedItem(),
                        dateField.getText().trim(),
                        formattedAmount,
                        statusField.getSelectedItem(),
                        dueDateField.getText().trim(),
                        insuranceField.getSelectedItem()
                });

                updateStats();
                JOptionPane.showMessageDialog(this, "Invoice created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void markPaid() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an invoice to mark as paid.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Mark this invoice as paid?\nInvoice: " + model.getValueAt(modelRow, 0) +
                        "\nAmount: " + model.getValueAt(modelRow, 4),
                "Confirm Payment", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            model.setValueAt("Paid", modelRow, 5);
            updateStats();
            JOptionPane.showMessageDialog(this, "Invoice marked as paid!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void markPending() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an invoice to mark as pending.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        model.setValueAt("Pending", modelRow, 5);
        updateStats();
    }

    private void printInvoice() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an invoice to print.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        String invoiceDetails = String.format(
                "UPSA CLINIC - INVOICE\n\n" +
                        "Invoice #: %s\n" +
                        "Patient: %s\n" +
                        "Service: %s\n" +
                        "Date: %s\n" +
                        "Amount: %s\n" +
                        "Status: %s\n" +
                        "Due Date: %s\n" +
                        "Insurance: %s\n\n" +
                        "Thank you for choosing UPSA Clinic!",
                model.getValueAt(modelRow, 0),
                model.getValueAt(modelRow, 1),
                model.getValueAt(modelRow, 2),
                model.getValueAt(modelRow, 3),
                model.getValueAt(modelRow, 4),
                model.getValueAt(modelRow, 5),
                model.getValueAt(modelRow, 6),
                model.getValueAt(modelRow, 7)
        );

        JOptionPane.showMessageDialog(this, invoiceDetails, "Print Invoice - " + model.getValueAt(modelRow, 0), JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportInvoices() {
        JOptionPane.showMessageDialog(this,
                "Exporting all invoices to CSV format...\n" +
                        "This feature would save invoice data to a file for external use.",
                "Export Invoices", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteInvoice() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an invoice to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this invoice?\nInvoice: " + model.getValueAt(modelRow, 0) +
                        "\nThis action cannot be undone.",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(modelRow);
            updateStats();
            JOptionPane.showMessageDialog(this, "Invoice deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void reload() {
        searchField.setText("");
        table.setRowSorter(null);
        updateStats();
        JOptionPane.showMessageDialog(this, "Billing data refreshed!", "Reload", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadSampleData() {
        // Clear existing data
        model.setRowCount(0);

        // Add sample invoices
        model.addRow(new Object[]{"INV-1001", "Ama Mensah", "Consultation", "2025-05-12", "₵420.00", "Unpaid", "2025-06-12", "NHIS"});
        model.addRow(new Object[]{"INV-1002", "Kwame Ofori", "Laboratory Test", "2025-05-11", "₵120.00", "Paid", "2025-06-11", "Private"});
        model.addRow(new Object[]{"INV-1003", "Esi Boateng", "X-Ray", "2025-05-10", "₵350.00", "Pending", "2025-06-10", "Corporate"});
        model.addRow(new Object[]{"INV-1004", "Yaa Addae", "Ultrasound", "2025-05-09", "₵280.00", "Paid", "2025-06-09", "NHIS"});
        model.addRow(new Object[]{"INV-1005", "Kofi Johnson", "Surgery", "2025-05-08", "₵1,500.00", "Partially Paid", "2025-06-08", "Private"});
    }

    private void updateStats() {
        int total = model.getRowCount();
        int paid = 0, unpaid = 0, pending = 0;
        totalRevenue = 0.0;
        pendingAmount = 0.0;

        for (int i = 0; i < model.getRowCount(); i++) {
            String status = model.getValueAt(i, 5).toString();
            String amountStr = model.getValueAt(i, 4).toString().replace("₵", "").replace(",", "");

            try {
                double amount = Double.parseDouble(amountStr);

                switch (status.toLowerCase()) {
                    case "paid":
                        paid++;
                        totalRevenue += amount;
                        break;
                    case "unpaid":
                        unpaid++;
                        pendingAmount += amount;
                        break;
                    case "pending":
                        pending++;
                        pendingAmount += amount;
                        break;
                    case "partially paid":
                        pending++;
                        pendingAmount += amount * 0.5; // Assume half paid
                        totalRevenue += amount * 0.5;
                        break;
                }
            } catch (NumberFormatException e) {
                // Skip invalid amounts
            }
        }

        String stats = String.format("Invoices: %d | Paid: %d | Unpaid: %d | Pending: %d | Revenue: %s | Pending: %s",
                total, paid, unpaid, pending, currencyFormat.format(totalRevenue), currencyFormat.format(pendingAmount));
        statsLabel.setText(stats);
    }
}