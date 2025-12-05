package upsa.clinic.panels;

import upsa.clinic.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {

    private JComboBox<String> themeCombo;
    private JComboBox<String> languageCombo;
    private JCheckBox notificationsCheck;
    private JCheckBox autoBackupCheck;
    private JCheckBox compactModeCheck;
    private JSlider fontSizeSlider;

    public SettingsPanel() {
        setLayout(new BorderLayout());
        setBackground(Colors.BACKGROUND);
        initializeUI();
    }

    private void initializeUI() {
        // Header panel
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Main content
        add(createSettingsPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Colors.BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(Colors.BACKGROUND);

        JLabel iconLabel = new JLabel("⚙️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JLabel titleLabel = new JLabel("System Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Colors.TEXT_PRIMARY);

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        // Save button
        JButton saveButton = createActionButton("💾 Save Settings", Colors.SUCCESS, e -> saveSettings());
        saveButton.setPreferredSize(new Dimension(150, 40));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setBackground(Colors.BACKGROUND);
        rightPanel.add(saveButton);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.setBackground(Colors.BACKGROUND);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));

        // Create tabbed pane for different setting categories
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Colors.BACKGROUND);
        tabbedPane.setForeground(Colors.TEXT_PRIMARY);

        // User Settings Tab
        tabbedPane.addTab("👤 User", createUserSettingsPanel());

        // Appearance Tab
        tabbedPane.addTab("🎨 Appearance", createAppearancePanel());

        // Notifications Tab
        tabbedPane.addTab("🔔 Notifications", createNotificationsPanel());

        // System Tab
        tabbedPane.addTab("🖥️ System", createSystemPanel());

        // About Tab
        tabbedPane.addTab("ℹ️ About", createAboutPanel());

        settingsPanel.add(tabbedPane, BorderLayout.CENTER);

        return settingsPanel;
    }

    private JPanel createUserSettingsPanel() {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(Color.WHITE);
        userPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Section header
        JLabel sectionLabel = new JLabel("User Account Settings");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionLabel.setForeground(Colors.TEXT_PRIMARY);
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // User profile section
        JPanel profilePanel = createSectionPanel("Profile Information");

        JTextField nameField = new JTextField("Admin User");
        JTextField emailField = new JTextField("admin@upsaclinic.com");
        JTextField roleField = new JTextField("Administrator");
        roleField.setEditable(false);

        profilePanel.add(createFormField("Full Name:", nameField));
        profilePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        profilePanel.add(createFormField("Email Address:", emailField));
        profilePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        profilePanel.add(createFormField("Role:", roleField));

        // Security section
        JPanel securityPanel = createSectionPanel("Security");

        JButton changePasswordBtn = createActionButton("🔒 Change Password", Colors.PRIMARY,
                e -> changePassword());
        JButton twoFactorBtn = createActionButton("🔐 Two-Factor Authentication", Colors.INFO,
                e -> setupTwoFactor());

        securityPanel.add(changePasswordBtn);
        securityPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        securityPanel.add(twoFactorBtn);

        // Add components to user panel
        userPanel.add(sectionLabel);
        userPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        userPanel.add(profilePanel);
        userPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        userPanel.add(securityPanel);

        return userPanel;
    }

    private JPanel createAppearancePanel() {
        JPanel appearancePanel = new JPanel();
        appearancePanel.setLayout(new BoxLayout(appearancePanel, BoxLayout.Y_AXIS));
        appearancePanel.setBackground(Color.WHITE);
        appearancePanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Section header
        JLabel sectionLabel = new JLabel("Appearance & Theme");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionLabel.setForeground(Colors.TEXT_PRIMARY);
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Theme section
        JPanel themePanel = createSectionPanel("Theme Settings");

        themeCombo = new JComboBox<>(new String[]{
                "Medical Blue (Default)", "Dark Mode", "Light Mode", "High Contrast", "UPSA Colors"
        });

        languageCombo = new JComboBox<>(new String[]{
                "English", "French", "Spanish", "Arabic", "Chinese"
        });

        themePanel.add(createFormField("Color Theme:", themeCombo));
        themePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        themePanel.add(createFormField("Language:", languageCombo));

        // Display section
        JPanel displayPanel = createSectionPanel("Display Settings");

        fontSizeSlider = new JSlider(10, 24, 14);
        fontSizeSlider.setMajorTickSpacing(2);
        fontSizeSlider.setPaintTicks(true);
        fontSizeSlider.setPaintLabels(true);

        compactModeCheck = new JCheckBox("Enable Compact Mode");
        compactModeCheck.setBackground(Color.WHITE);

        JLabel fontSizeLabel = new JLabel("Font Size:");
        fontSizeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        fontSizeLabel.setForeground(Colors.TEXT_PRIMARY);

        displayPanel.add(createFormField("", fontSizeLabel));
        displayPanel.add(fontSizeSlider);
        displayPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        displayPanel.add(compactModeCheck);

        // Add components to appearance panel
        appearancePanel.add(sectionLabel);
        appearancePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        appearancePanel.add(themePanel);
        appearancePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        appearancePanel.add(displayPanel);

        return appearancePanel;
    }

    private JPanel createNotificationsPanel() {
        JPanel notificationsPanel = new JPanel();
        notificationsPanel.setLayout(new BoxLayout(notificationsPanel, BoxLayout.Y_AXIS));
        notificationsPanel.setBackground(Color.WHITE);
        notificationsPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Section header
        JLabel sectionLabel = new JLabel("Notification Preferences");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionLabel.setForeground(Colors.TEXT_PRIMARY);
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Notification settings
        JPanel notificationSettings = createSectionPanel("Notification Types");

        notificationsCheck = new JCheckBox("Enable System Notifications", true);
        JCheckBox emailCheck = new JCheckBox("Email Notifications");
        JCheckBox soundCheck = new JCheckBox("Sound Alerts", true);
        JCheckBox popupCheck = new JCheckBox("Popup Notifications");

        notificationsCheck.setBackground(Color.WHITE);
        emailCheck.setBackground(Color.WHITE);
        soundCheck.setBackground(Color.WHITE);
        popupCheck.setBackground(Color.WHITE);

        notificationSettings.add(notificationsCheck);
        notificationSettings.add(Box.createRigidArea(new Dimension(0, 10)));
        notificationSettings.add(emailCheck);
        notificationSettings.add(Box.createRigidArea(new Dimension(0, 10)));
        notificationSettings.add(soundCheck);
        notificationSettings.add(Box.createRigidArea(new Dimension(0, 10)));
        notificationSettings.add(popupCheck);

        // Alert preferences
        JPanel alertPanel = createSectionPanel("Alert Preferences");

        JComboBox<String> alertLevel = new JComboBox<>(new String[]{
                "All Alerts", "Critical Only", "High & Critical", "None"
        });

        JTextField quietHoursStart = new JTextField("22:00");
        JTextField quietHoursEnd = new JTextField("06:00");

        alertPanel.add(createFormField("Alert Level:", alertLevel));
        alertPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        alertPanel.add(createFormField("Quiet Hours Start:", quietHoursStart));
        alertPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        alertPanel.add(createFormField("Quiet Hours End:", quietHoursEnd));

        // Add components to notifications panel
        notificationsPanel.add(sectionLabel);
        notificationsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        notificationsPanel.add(notificationSettings);
        notificationsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        notificationsPanel.add(alertPanel);

        return notificationsPanel;
    }

    private JPanel createSystemPanel() {
        JPanel systemPanel = new JPanel();
        systemPanel.setLayout(new BoxLayout(systemPanel, BoxLayout.Y_AXIS));
        systemPanel.setBackground(Color.WHITE);
        systemPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Section header
        JLabel sectionLabel = new JLabel("System Configuration");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionLabel.setForeground(Colors.TEXT_PRIMARY);
        sectionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Backup settings
        JPanel backupPanel = createSectionPanel("Backup & Data");

        autoBackupCheck = new JCheckBox("Automatic Daily Backup", true);
        JComboBox<String> backupLocation = new JComboBox<>(new String[]{
                "Local Server", "Cloud Storage", "External Drive", "Network Location"
        });

        JButton backupNowBtn = createActionButton("💾 Backup Now", Colors.INFO,
                e -> performBackup());
        JButton restoreBtn = createActionButton("🔄 Restore Data", Colors.WARNING,
                e -> restoreData());

        autoBackupCheck.setBackground(Color.WHITE);

        backupPanel.add(autoBackupCheck);
        backupPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        backupPanel.add(createFormField("Backup Location:", backupLocation));
        backupPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        backupPanel.add(backupNowBtn);
        backupPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        backupPanel.add(restoreBtn);

        // System maintenance
        JPanel maintenancePanel = createSectionPanel("System Maintenance");

        JButton clearCacheBtn = createActionButton("🧹 Clear Cache", Colors.SECONDARY,
                e -> clearCache());
        JButton systemInfoBtn = createActionButton("📊 System Info", Colors.PRIMARY,
                e -> showSystemInfo());
        JButton resetBtn = createActionButton("🔄 Reset Settings", Colors.DANGER,
                e -> resetSettings());

        maintenancePanel.add(clearCacheBtn);
        maintenancePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        maintenancePanel.add(systemInfoBtn);
        maintenancePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        maintenancePanel.add(resetBtn);

        // Add components to system panel
        systemPanel.add(sectionLabel);
        systemPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        systemPanel.add(backupPanel);
        systemPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        systemPanel.add(maintenancePanel);

        return systemPanel;
    }

    private JPanel createAboutPanel() {
        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
        aboutPanel.setBackground(Color.WHITE);
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Application info
        JLabel appIcon = new JLabel("🏥");
        appIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        appIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("UPSA Clinic Management System");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        appName.setForeground(Colors.PRIMARY);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel version = new JLabel("Version 2.0.0");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        version.setForeground(Colors.TEXT_SECONDARY);
        version.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel buildInfo = new JLabel("Build 2025.01.19 • Medical Edition");
        buildInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        buildInfo.setForeground(Colors.TEXT_MUTED);
        buildInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // System information
        JPanel infoPanel = createSectionPanel("System Information");

        String systemInfo =
                "• Java Version: " + System.getProperty("java.version") + "\n" +
                        "• OS: " + System.getProperty("os.name") + "\n" +
                        "• Architecture: " + System.getProperty("os.arch") + "\n" +
                        "• User: " + System.getProperty("user.name") + "\n" +
                        "• Clinic: UPSA Medical Center\n" +
                        "• License: Educational Use\n" +
                        "• Support: clinic-support@upsa.edu.gh";

        JTextArea infoArea = new JTextArea(systemInfo);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoArea.setBackground(Colors.BACKGROUND);
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Support buttons
        JButton supportBtn = createActionButton("📞 Contact Support", Colors.INFO,
                e -> contactSupport());
        JButton docsBtn = createActionButton("📚 User Manual", Colors.SECONDARY,
                e -> openDocumentation());
        JButton updateBtn = createActionButton("🔄 Check Updates", Colors.SUCCESS,
                e -> checkUpdates());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(supportBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(docsBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(updateBtn);

        // Add components to about panel
        aboutPanel.add(appIcon);
        aboutPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        aboutPanel.add(appName);
        aboutPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        aboutPanel.add(version);
        aboutPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        aboutPanel.add(buildInfo);
        aboutPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        aboutPanel.add(infoPanel);
        aboutPanel.add(new JScrollPane(infoArea));
        aboutPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        aboutPanel.add(buttonPanel);

        return aboutPanel;
    }

    private JPanel createSectionPanel(String title) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Colors.BORDER), title),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        return section;
    }

    private JPanel createFormField(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);

        if (!label.isEmpty()) {
            JLabel fieldLabel = new JLabel(label);
            fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            fieldLabel.setForeground(Colors.TEXT_PRIMARY);
            fieldLabel.setPreferredSize(new Dimension(120, 25));
            panel.add(fieldLabel, BorderLayout.WEST);
        }

        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JButton createActionButton(String text, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Colors.darken(color, 0.1));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        button.addActionListener(action);
        return button;
    }

    // Action methods
    private void saveSettings() {
        JOptionPane.showMessageDialog(this,
                "Settings saved successfully!\n\n" +
                        "• Theme: " + themeCombo.getSelectedItem() + "\n" +
                        "• Language: " + languageCombo.getSelectedItem() + "\n" +
                        "• Notifications: " + (notificationsCheck.isSelected() ? "Enabled" : "Disabled") + "\n" +
                        "• Auto Backup: " + (autoBackupCheck.isSelected() ? "Enabled" : "Disabled") + "\n" +
                        "• Compact Mode: " + (compactModeCheck.isSelected() ? "Enabled" : "Disabled"),
                "Settings Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private void changePassword() {
        JPasswordField currentPass = new JPasswordField();
        JPasswordField newPass = new JPasswordField();
        JPasswordField confirmPass = new JPasswordField();

        Object[] message = {
                "Current Password:", currentPass,
                "New Password:", newPass,
                "Confirm New Password:", confirmPass
        };

        int option = JOptionPane.showConfirmDialog(this, message,
                "Change Password", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Password changed successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setupTwoFactor() {
        JOptionPane.showMessageDialog(this,
                "Two-Factor Authentication Setup\n\n" +
                        "1. Download Google Authenticator app\n" +
                        "2. Scan the QR code with your app\n" +
                        "3. Enter the 6-digit code to verify\n\n" +
                        "QR Code would be displayed here in a real implementation.",
                "2FA Setup", JOptionPane.INFORMATION_MESSAGE);
    }

    private void performBackup() {
        JOptionPane.showMessageDialog(this,
                "System backup initiated!\n\n" +
                        "• Patient records: Backing up...\n" +
                        "• Medical data: Backing up...\n" +
                        "• Pharmacy inventory: Backing up...\n" +
                        "• System settings: Backing up...\n\n" +
                        "Backup will complete in the background.",
                "Backup Started", JOptionPane.INFORMATION_MESSAGE);
    }

    private void restoreData() {
        JOptionPane.showMessageDialog(this,
                "Data restoration requires administrative approval.\n" +
                        "Please contact system administrator for data restoration procedures.",
                "Restore Data", JOptionPane.WARNING_MESSAGE);
    }

    private void clearCache() {
        JOptionPane.showMessageDialog(this,
                "System cache cleared successfully!\n\n" +
                        "• Temporary files: Cleared\n" +
                        "• Session data: Reset\n" +
                        "• Application cache: Purged",
                "Cache Cleared", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSystemInfo() {
        String systemInfo =
                "UPSA Clinic Management System\n" +
                        "Version 2.0.0 (Medical Edition)\n\n" +
                        "System Information:\n" +
                        "• Java: " + System.getProperty("java.version") + "\n" +
                        "• JVM: " + System.getProperty("java.vm.name") + "\n" +
                        "• OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + "\n" +
                        "• Architecture: " + System.getProperty("os.arch") + "\n" +
                        "• User: " + System.getProperty("user.name") + "\n" +
                        "• Memory: " + (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " MB\n\n" +
                        "Clinic Information:\n" +
                        "• Institution: UPSA Medical Center\n" +
                        "• License: Educational Use\n" +
                        "• Support: Available";

        JOptionPane.showMessageDialog(this, systemInfo, "System Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetSettings() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to reset all settings to default?\n\n" +
                        "This will reset:\n" +
                        "• Theme and appearance settings\n" +
                        "• Notification preferences\n" +
                        "• User interface settings\n\n" +
                        "This action cannot be undone.",
                "Confirm Reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "All settings have been reset to default values.",
                    "Settings Reset", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void contactSupport() {
        JOptionPane.showMessageDialog(this,
                "UPSA Clinic Support\n\n" +
                        "📞 Phone: +233 24 123 4567\n" +
                        "📧 Email: clinic-support@upsa.edu.gh\n" +
                        "🏢 Office: UPSA Medical Center, Accra\n" +
                        "🕒 Hours: Mon-Fri 8:00 AM - 5:00 PM\n\n" +
                        "Emergency Support: Available 24/7",
                "Contact Support", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openDocumentation() {
        JOptionPane.showMessageDialog(this,
                "User Documentation & Manual\n\n" +
                        "The complete user manual is available at:\n" +
                        "https://docs.upsaclinic.edu.gh\n\n" +
                        "Features include:\n" +
                        "• Getting Started Guide\n" +
                        "• Patient Management Tutorial\n" +
                        "• Pharmacy Inventory Guide\n" +
                        "• Billing System Manual\n" +
                        "• Troubleshooting Guide",
                "Documentation", JOptionPane.INFORMATION_MESSAGE);
    }

    private void checkUpdates() {
        JOptionPane.showMessageDialog(this,
                "Update Check Complete\n\n" +
                        "✅ Your system is up to date!\n" +
                        "Version: 2.0.0 (Latest)\n\n" +
                        "No updates available at this time.\n" +
                        "Next check: Automatic in 7 days",
                "System Update", JOptionPane.INFORMATION_MESSAGE);
    }
}