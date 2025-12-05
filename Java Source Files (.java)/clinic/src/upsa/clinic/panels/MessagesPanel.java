package upsa.clinic.panels;

import upsa.clinic.Colors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessagesPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JTextField toField;
    private JTextField subjectField;
    private JTextArea bodyArea;
    private JComboBox<String> priorityCombo;
    private JComboBox<String> typeCombo;
    private JLabel statsLabel;

    public MessagesPanel() {
        setLayout(new BorderLayout());
        setBackground(Colors.BACKGROUND);
        initializeUI();
    }

    private void initializeUI() {
        // Header panel
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Main content
        add(createMainContentPanel(), BorderLayout.CENTER);

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

        JLabel iconLabel = new JLabel("💬");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JLabel titleLabel = new JLabel("Messages & Notifications");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Colors.TEXT_PRIMARY);

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        // Stats label
        statsLabel = new JLabel("Loading messages...");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statsLabel.setForeground(Colors.TEXT_SECONDARY);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setBackground(Colors.BACKGROUND);
        rightPanel.add(statsLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainContentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(Colors.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));

        // Messages list
        mainPanel.add(createMessagesPanel(), BorderLayout.CENTER);

        // Compose panel
        mainPanel.add(createComposePanel(), BorderLayout.EAST);

        return mainPanel;
    }

    private JPanel createMessagesPanel() {
        JPanel messagesPanel = new JPanel(new BorderLayout());
        messagesPanel.setBackground(Colors.BACKGROUND);

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Colors.BACKGROUND);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Colors.BORDER), "Search Messages"),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JButton searchButton = new JButton("🔍");
        searchButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        searchButton.setPreferredSize(new Dimension(40, 40));
        searchButton.setBackground(Colors.PRIMARY);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton refreshButton = createActionButton("🔄 Refresh", Colors.SECONDARY, e -> refreshMessages());
        refreshButton.setPreferredSize(new Dimension(120, 40));

        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchControls.setBackground(Colors.BACKGROUND);
        searchControls.add(searchField);
        searchControls.add(searchButton);
        searchControls.add(refreshButton);

        searchPanel.add(searchControls, BorderLayout.WEST);

        // Messages table
        model = new DefaultTableModel(
                new String[]{"", "From", "To", "Subject", "Priority", "Date", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Icon.class : String.class;
            }
        };

        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Colors.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(Colors.PRIMARY_LIGHT);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(Colors.BORDER);
        table.setShowGrid(true);

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(30);  // Icon
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // From
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // To
        table.getColumnModel().getColumn(3).setPreferredWidth(200); // Subject
        table.getColumnModel().getColumn(4).setPreferredWidth(80);  // Priority
        table.getColumnModel().getColumn(5).setPreferredWidth(120); // Date
        table.getColumnModel().getColumn(6).setPreferredWidth(80);  // Status

        // Add double-click listener to view messages
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewSelectedMessage();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Action buttons for messages
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(Colors.BACKGROUND);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton viewButton = createActionButton("👁️ View", Colors.INFO, e -> viewSelectedMessage());
        JButton replyButton = createActionButton("↩️ Reply", Colors.PRIMARY, e -> replyToMessage());
        JButton deleteButton = createActionButton("🗑️ Delete", Colors.DANGER, e -> deleteMessage());
        JButton markReadButton = createActionButton("✅ Mark Read", Colors.SUCCESS, e -> markAsRead());

        actionPanel.add(viewButton);
        actionPanel.add(replyButton);
        actionPanel.add(deleteButton);
        actionPanel.add(markReadButton);

        messagesPanel.add(searchPanel, BorderLayout.NORTH);
        messagesPanel.add(scrollPane, BorderLayout.CENTER);
        messagesPanel.add(actionPanel, BorderLayout.SOUTH);

        return messagesPanel;
    }

    private JPanel createComposePanel() {
        JPanel composePanel = new JPanel(new BorderLayout(10, 10));
        composePanel.setBackground(Colors.BACKGROUND);
        composePanel.setPreferredSize(new Dimension(400, 0));
        composePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Compose header
        JLabel composeLabel = new JLabel("✏️ Compose Message");
        composeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        composeLabel.setForeground(Colors.TEXT_PRIMARY);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // To field
        JPanel toPanel = new JPanel(new BorderLayout(10, 0));
        toPanel.setBackground(Color.WHITE);
        toPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        toField = new JTextField();
        toField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        toPanel.add(toLabel, BorderLayout.WEST);
        toPanel.add(toField, BorderLayout.CENTER);

        // Subject field
        JPanel subjectPanel = new JPanel(new BorderLayout(10, 0));
        subjectPanel.setBackground(Color.WHITE);
        subjectPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        subjectField = new JTextField();
        subjectField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        subjectPanel.add(subjectLabel, BorderLayout.WEST);
        subjectPanel.add(subjectField, BorderLayout.CENTER);

        // Type and priority
        JPanel optionsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel typePanel = new JPanel(new BorderLayout(5, 0));
        typePanel.setBackground(Color.WHITE);
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        typeCombo = new JComboBox<>(new String[]{"Message", "Notification", "Alert", "Reminder"});
        typePanel.add(typeLabel, BorderLayout.WEST);
        typePanel.add(typeCombo, BorderLayout.CENTER);

        JPanel priorityPanel = new JPanel(new BorderLayout(5, 0));
        priorityPanel.setBackground(Color.WHITE);
        JLabel priorityLabel = new JLabel("Priority:");
        priorityLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        priorityCombo = new JComboBox<>(new String[]{"Low", "Normal", "High", "Urgent"});
        priorityPanel.add(priorityLabel, BorderLayout.WEST);
        priorityPanel.add(priorityCombo, BorderLayout.CENTER);

        optionsPanel.add(typePanel);
        optionsPanel.add(priorityPanel);

        // Message body
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBackground(Color.WHITE);
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel bodyLabel = new JLabel("Message:");
        bodyLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bodyArea = new JTextArea(8, 20);
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        bodyArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        bodyPanel.add(bodyLabel, BorderLayout.NORTH);
        bodyPanel.add(new JScrollPane(bodyArea), BorderLayout.CENTER);

        // Send button
        JButton sendButton = createActionButton("📤 Send Message", Colors.SUCCESS, e -> sendMessage());
        sendButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(toPanel);
        formPanel.add(subjectPanel);
        formPanel.add(optionsPanel);
        formPanel.add(bodyPanel);
        formPanel.add(sendButton);

        composePanel.add(composeLabel, BorderLayout.NORTH);
        composePanel.add(formPanel, BorderLayout.CENTER);

        return composePanel;
    }

    private JButton createActionButton(String text, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
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

    private void sendMessage() {
        String to = toField.getText().trim();
        String subject = subjectField.getText().trim();
        String body = bodyArea.getText().trim();
        String type = typeCombo.getSelectedItem().toString();
        String priority = priorityCombo.getSelectedItem().toString();

        if (to.isEmpty() || subject.isEmpty() || body.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields: To, Subject, and Message.",
                    "Incomplete Message", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Add message to table
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String status = "Sent";
        String preview = body.length() > 40 ? body.substring(0, 40) + "..." : body;

        // Determine icon based on type
        Icon icon = getIconForType(type);

        model.addRow(new Object[]{
                icon,
                "You",
                to,
                subject,
                priority,
                timestamp,
                status
        });

        // Clear form
        toField.setText("");
        subjectField.setText("");
        bodyArea.setText("");
        priorityCombo.setSelectedIndex(1); // Reset to Normal

        updateStats();
        JOptionPane.showMessageDialog(this,
                "Message sent successfully to " + to, "Message Sent",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private Icon getIconForType(String type) {
        return new ImageIcon(); // You can add custom icons here
    }

    private void viewSelectedMessage() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a message to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        String messageDetails = String.format(
                "💬 MESSAGE DETAILS\n\n" +
                        "From: %s\n" +
                        "To: %s\n" +
                        "Subject: %s\n" +
                        "Priority: %s\n" +
                        "Date: %s\n" +
                        "Status: %s\n\n" +
                        "Message Preview:\n%s",
                model.getValueAt(modelRow, 1),
                model.getValueAt(modelRow, 2),
                model.getValueAt(modelRow, 3),
                model.getValueAt(modelRow, 4),
                model.getValueAt(modelRow, 5),
                model.getValueAt(modelRow, 6),
                "This is a preview of the selected message. In a real application, " +
                        "this would show the full message content from your database."
        );

        JOptionPane.showMessageDialog(this, messageDetails,
                "View Message - " + model.getValueAt(modelRow, 3),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void replyToMessage() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a message to reply to.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        String originalSender = model.getValueAt(modelRow, 1).toString();
        String originalSubject = model.getValueAt(modelRow, 3).toString();

        toField.setText(originalSender);
        subjectField.setText("Re: " + originalSubject);
        bodyArea.setText("\n\n--- Original Message ---\n[Message content would appear here]");
        bodyArea.requestFocus();
    }

    private void deleteMessage() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a message to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this message?\nSubject: " + model.getValueAt(modelRow, 3),
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(modelRow);
            updateStats();
            JOptionPane.showMessageDialog(this, "Message deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void markAsRead() {
        int viewRow = table.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a message to mark as read.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);

        model.setValueAt("Read", modelRow, 6);
        JOptionPane.showMessageDialog(this, "Message marked as read!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshMessages() {
        updateStats();
        JOptionPane.showMessageDialog(this, "Messages refreshed!", "Refresh", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadSampleData() {
        // Clear existing data
        model.setRowCount(0);

        // Add sample messages
        model.addRow(new Object[]{new ImageIcon(), "System", "All Staff", "Low Stock Alert", "High", "2025-01-19 09:30", "Unread"});
        model.addRow(new Object[]{new ImageIcon(), "Dr. Ama Mensah", "Admin", "Patient Consultation", "Normal", "2025-01-19 08:15", "Read"});
        model.addRow(new Object[]{new ImageIcon(), "Pharmacy", "Inventory Manager", "Medication Restocked", "Normal", "2025-01-18 16:45", "Read"});
        model.addRow(new Object[]{new ImageIcon(), "System", "Nursing Staff", "New Admission", "Urgent", "2025-01-18 14:20", "Unread"});
        model.addRow(new Object[]{new ImageIcon(), "HR Department", "All Doctors", "Staff Meeting", "Low", "2025-01-18 10:00", "Read"});
    }

    private void updateStats() {
        int total = model.getRowCount();
        int unread = 0, urgent = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            String status = model.getValueAt(i, 6).toString();
            String priority = model.getValueAt(i, 4).toString();

            if ("Unread".equals(status)) {
                unread++;
            }
            if ("Urgent".equals(priority)) {
                urgent++;
            }
        }

        String stats = String.format("Total: %d | Unread: %d | Urgent: %d", total, unread, urgent);
        statsLabel.setText(stats);
    }
}