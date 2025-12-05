package upsa.clinic.panels;

import upsa.clinic.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PharmacyPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;
    private JLabel statsLabel;

    public PharmacyPanel() {
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

        JLabel iconLabel = new JLabel("💊");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JLabel titleLabel = new JLabel("Pharmacy Inventory");
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
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Colors.BORDER), "Search Drugs"),
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
        statsLabel = new JLabel("Loading pharmacy inventory...");
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
                new String[]{"ID", "Drug Name", "Category", "Quantity", "Unit Price", "Total Value", "Expiry Date", "Supplier", "Status"}, 0
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
        table.getColumnModel().getColumn(0).setPreferredWidth(80);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(200);  // Drug Name
        table.getColumnModel().getColumn(2).setPreferredWidth(120);  // Category
        table.getColumnModel().getColumn(3).setPreferredWidth(80);   // Quantity
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // Unit Price
        table.getColumnModel().getColumn(5).setPreferredWidth(120);  // Total Value
        table.getColumnModel().getColumn(6).setPreferredWidth(100);  // Expiry Date
        table.getColumnModel().getColumn(7).setPreferredWidth(150);  // Supplier
        table.getColumnModel().getColumn(8).setPreferredWidth(100);  // Status

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
        JButton addButton = createActionButton("➕ Add Drug", Colors.SUCCESS, e -> addDrug());
        JButton editButton = createActionButton("✏️ Edit", Colors.INFO, e -> editDrug());
        JButton removeButton = createActionButton("🗑️ Remove", Colors.DANGER, e -> removeDrug());
        JButton restockButton = createActionButton("📦 Restock", Colors.WARNING, e -> restock());
        JButton dispenseButton = createActionButton("💊 Dispense", Colors.MEDICAL_BLUE, e -> dispenseDrug());
        JButton alertsButton = createActionButton("⚠️ Alerts", Colors.MEDICAL_RED, e -> showAlerts());
        JButton suppliersButton = createActionButton("🏢 Suppliers", Colors.SECONDARY, e -> showSuppliers());
        JButton reloadButton = createActionButton("🔄 Reload", Colors.SECONDARY, e -> reload());

        actionsPanel.add(addButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(editButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(removeButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(restockButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(dispenseButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(alertsButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(suppliersButton);
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

    private void addDrug() {
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField();
        JComboBox<String> categoryField = new JComboBox<>(new String[]{
                "Analgesics", "Antibiotics", "Antivirals", "Antifungals", "Antidepressants",
                "Antihypertensives", "Vitamins", "Vaccines", "First Aid", "Medical Supplies"
        });
        JTextField quantityField = new JTextField("0");
        JTextField priceField = new JTextField("0.00");
        JTextField expiryField = new JTextField(LocalDate.now().plusYears(2).format(DateTimeFormatter.ISO_LOCAL_DATE));
        JComboBox<String> supplierField = new JComboBox<>(new String[]{
                "MediCorp Ghana", "PharmaPlus Ltd", "HealthSupplies Inc", "Local Supplier", "International Imports"
        });
        JComboBox<String> statusField = new JComboBox<>(new String[]{"In Stock", "Low Stock", "Out of Stock", "Discontinued"});

        formPanel.add(new JLabel("Drug Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Unit Price (₵):"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        formPanel.add(expiryField);
        formPanel.add(new JLabel("Supplier:"));
        formPanel.add(supplierField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Add New Drug",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate expiryDate = LocalDate.parse(expiryField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());
                double unitPrice = Double.parseDouble(priceField.getText().trim());
                double totalValue = quantity * unitPrice;

                int newId = model.getRowCount() + 1;
                model.addRow(new Object[]{
                        "DRUG-" + newId,
                        nameField.getText().trim(),
                        categoryField.getSelectedItem(),
                        quantity,
                        String.format("₵%.2f", unitPrice),
                        String.format("₵%.2f", totalValue),
                        expiryField.getText().trim(),
                        supplierField.getSelectedItem(),
                        statusField.getSelectedItem()
                });

                updateStats();
                JOptionPane.showMessageDialog(this, "Drug added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format for quantity or price.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editDrug() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a drug to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(model.getValueAt(modelRow, 1).toString());
        JComboBox<String> categoryField = new JComboBox<>(new String[]{
                "Analgesics", "Antibiotics", "Antivirals", "Antifungals", "Antidepressants",
                "Antihypertensives", "Vitamins", "Vaccines", "First Aid", "Medical Supplies"
        });
        categoryField.setSelectedItem(model.getValueAt(modelRow, 2));

        JTextField quantityField = new JTextField(model.getValueAt(modelRow, 3).toString());
        JTextField priceField = new JTextField(model.getValueAt(modelRow, 4).toString().replace("₵", ""));
        JTextField expiryField = new JTextField(model.getValueAt(modelRow, 6).toString());

        JComboBox<String> supplierField = new JComboBox<>(new String[]{
                "MediCorp Ghana", "PharmaPlus Ltd", "HealthSupplies Inc", "Local Supplier", "International Imports"
        });
        supplierField.setSelectedItem(model.getValueAt(modelRow, 7));

        JComboBox<String> statusField = new JComboBox<>(new String[]{"In Stock", "Low Stock", "Out of Stock", "Discontinued"});
        statusField.setSelectedItem(model.getValueAt(modelRow, 8));

        formPanel.add(new JLabel("Drug Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Unit Price (₵):"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Expiry Date:"));
        formPanel.add(expiryField);
        formPanel.add(new JLabel("Supplier:"));
        formPanel.add(supplierField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Edit Drug",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate expiryDate = LocalDate.parse(expiryField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());
                double unitPrice = Double.parseDouble(priceField.getText().trim());
                double totalValue = quantity * unitPrice;

                model.setValueAt(nameField.getText().trim(), modelRow, 1);
                model.setValueAt(categoryField.getSelectedItem(), modelRow, 2);
                model.setValueAt(quantity, modelRow, 3);
                model.setValueAt(String.format("₵%.2f", unitPrice), modelRow, 4);
                model.setValueAt(String.format("₵%.2f", totalValue), modelRow, 5);
                model.setValueAt(expiryField.getText().trim(), modelRow, 6);
                model.setValueAt(supplierField.getSelectedItem(), modelRow, 7);
                model.setValueAt(statusField.getSelectedItem(), modelRow, 8);

                updateStats();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeDrug() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a drug to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this drug?\nDrug: " + model.getValueAt(modelRow, 1) +
                        "\nThis action cannot be undone.",
                "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(modelRow);
            updateStats();
            JOptionPane.showMessageDialog(this, "Drug removed successfully!", "Removed", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void restock() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a drug to restock.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        String currentDrug = model.getValueAt(modelRow, 1).toString();
        String currentQty = model.getValueAt(modelRow, 3).toString();

        JTextField restockField = new JTextField("0");
        Object[] message = {
                "Drug: " + currentDrug,
                "Current Quantity: " + currentQty,
                "Add Quantity:", restockField
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Restock Drug",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int addQty = Integer.parseInt(restockField.getText().trim());
                int current = Integer.parseInt(currentQty);
                int newQty = current + addQty;

                // Update quantity
                model.setValueAt(newQty, modelRow, 3);

                // Update total value
                double unitPrice = Double.parseDouble(model.getValueAt(modelRow, 4).toString().replace("₵", ""));
                double totalValue = newQty * unitPrice;
                model.setValueAt(String.format("₵%.2f", totalValue), modelRow, 5);

                // Update status
                String newStatus = getStockStatus(newQty);
                model.setValueAt(newStatus, modelRow, 8);

                updateStats();
                JOptionPane.showMessageDialog(this,
                        "Restocked " + addQty + " units of " + currentDrug + "\nNew quantity: " + newQty,
                        "Restock Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void dispenseDrug() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a drug to dispense.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        String currentDrug = model.getValueAt(modelRow, 1).toString();
        String currentQty = model.getValueAt(modelRow, 3).toString();

        JTextField dispenseField = new JTextField("1");
        JTextField patientField = new JTextField();

        Object[] message = {
                "Drug: " + currentDrug,
                "Available Quantity: " + currentQty,
                "Dispense Quantity:", dispenseField,
                "Patient Name:", patientField
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Dispense Drug",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int dispenseQty = Integer.parseInt(dispenseField.getText().trim());
                int current = Integer.parseInt(currentQty);

                if (dispenseQty > current) {
                    JOptionPane.showMessageDialog(this,
                            "Insufficient stock! Available: " + current,
                            "Stock Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int newQty = current - dispenseQty;
                model.setValueAt(newQty, modelRow, 3);

                // Update total value
                double unitPrice = Double.parseDouble(model.getValueAt(modelRow, 4).toString().replace("₵", ""));
                double totalValue = newQty * unitPrice;
                model.setValueAt(String.format("₵%.2f", totalValue), modelRow, 5);

                // Update status
                String newStatus = getStockStatus(newQty);
                model.setValueAt(newStatus, modelRow, 8);

                updateStats();
                JOptionPane.showMessageDialog(this,
                        "Dispensed " + dispenseQty + " units of " + currentDrug +
                                " to " + patientField.getText().trim() + "\nRemaining quantity: " + newQty,
                        "Dispense Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAlerts() {
        StringBuilder alerts = new StringBuilder();
        alerts.append("⚠️ PHARMACY ALERTS\n\n");

        int lowStockCount = 0;
        int expiredCount = 0;
        LocalDate today = LocalDate.now();

        for (int i = 0; i < model.getRowCount(); i++) {
            try {
                int quantity = Integer.parseInt(model.getValueAt(i, 3).toString());
                LocalDate expiry = LocalDate.parse(model.getValueAt(i, 6).toString());

                // Check low stock
                if (quantity < 20) {
                    alerts.append("🔴 LOW STOCK: ").append(model.getValueAt(i, 1))
                            .append(" - Only ").append(quantity).append(" left\n");
                    lowStockCount++;
                }

                // Check expiry (within 30 days) - FIXED: Added missing parenthesis
                if (expiry.isBefore(today.plusDays(30))) {
                    long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(today, expiry);
                    if (daysUntilExpiry <= 0) {
                        alerts.append("🚨 EXPIRED: ").append(model.getValueAt(i, 1))
                                .append(" - Expired on ").append(expiry).append("\n");
                    } else {
                        alerts.append("🟡 EXPIRING SOON: ").append(model.getValueAt(i, 1))
                                .append(" - Expires in ").append(daysUntilExpiry).append(" days\n");
                    }
                    expiredCount++;
                }
            } catch (Exception e) {
                // Skip invalid entries
            }
        }

        if (lowStockCount == 0 && expiredCount == 0) {
            alerts.append("✅ No urgent alerts. All medications are well-stocked and within expiry dates.");
        } else {
            alerts.append("\n📊 Summary: ").append(lowStockCount).append(" low stock items, ")
                    .append(expiredCount).append(" expiry alerts");
        }

        JTextArea textArea = new JTextArea(alerts.toString(), 15, 50);
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textArea.setBackground(Colors.BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Pharmacy Alerts", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSuppliers() {
        String supplierInfo =
                "🏢 PHARMACY SUPPLIERS\n\n" +
                        "• MediCorp Ghana - Primary supplier for antibiotics and vaccines\n" +
                        "• PharmaPlus Ltd - General medications and medical supplies\n" +
                        "• HealthSupplies Inc - Specialized medications and equipment\n" +
                        "• Local Supplier - Emergency and common medications\n" +
                        "• International Imports - Specialized and rare medications\n\n" +
                        "Contact Procurement Department for new supplier registrations.";

        JOptionPane.showMessageDialog(this, supplierInfo, "Supplier Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void reload() {
        searchField.setText("");
        table.setRowSorter(null);
        updateStats();
        JOptionPane.showMessageDialog(this, "Pharmacy inventory refreshed!", "Reload", JOptionPane.INFORMATION_MESSAGE);
    }

    private String getStockStatus(int quantity) {
        if (quantity == 0) return "Out of Stock";
        if (quantity < 20) return "Low Stock";
        return "In Stock";
    }

    private void loadSampleData() {
        // Clear existing data
        model.setRowCount(0);

        // Add sample drugs with enhanced information
        model.addRow(new Object[]{
                "DRUG-1001", "Paracetamol 500mg", "Analgesics", 120, "₵0.50", "₵60.00",
                "2026-03-01", "MediCorp Ghana", "In Stock"
        });

        model.addRow(new Object[]{
                "DRUG-1002", "Amoxicillin 250mg", "Antibiotics", 50, "₵1.20", "₵60.00",
                "2025-11-11", "PharmaPlus Ltd", "Low Stock"
        });

        model.addRow(new Object[]{
                "DRUG-1003", "Vitamin C 1000mg", "Vitamins", 200, "₵2.50", "₵500.00",
                "2026-12-15", "HealthSupplies Inc", "In Stock"
        });

        model.addRow(new Object[]{
                "DRUG-1004", "Insulin Syringes", "Medical Supplies", 15, "₵0.80", "₵12.00",
                "2027-01-20", "Local Supplier", "Low Stock"
        });

        model.addRow(new Object[]{
                "DRUG-1005", "Ibuprofen 400mg", "Analgesics", 0, "₵0.75", "₵0.00",
                "2025-09-30", "MediCorp Ghana", "Out of Stock"
        });
    }

    private void updateStats() {
        int totalItems = model.getRowCount();
        int inStock = 0, lowStock = 0, outOfStock = 0;
        double totalValue = 0.0;

        for (int i = 0; i < model.getRowCount(); i++) {
            String status = model.getValueAt(i, 8).toString();
            switch (status.toLowerCase()) {
                case "in stock": inStock++; break;
                case "low stock": lowStock++; break;
                case "out of stock": outOfStock++; break;
            }

            try {
                String valueStr = model.getValueAt(i, 5).toString().replace("₵", "");
                totalValue += Double.parseDouble(valueStr);
            } catch (NumberFormatException e) {
                // Skip invalid values
            }
        }

        String stats = String.format("Items: %d | In Stock: %d | Low: %d | Out: %d | Total Value: ₵%.2f",
                totalItems, inStock, lowStock, outOfStock, totalValue);
        statsLabel.setText(stats);
    }
}