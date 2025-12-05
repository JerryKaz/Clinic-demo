package upsa.clinic.components;

import upsa.clinic.Colors;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Modern Vertical Sidebar with icons, active state, and role-based menu items.
 * Calls consumer with panel key (e.g. "patients").
 */
public class SidebarMenu extends JPanel {

    private Consumer<String> onSelect;
    private JButton selectedButton;
    private String currentRole;

    public SidebarMenu(Consumer<String> onSelect) {
        this(onSelect, "Admin"); // Default role
    }

    public SidebarMenu(Consumer<String> onSelect, String role) {
        this.onSelect = onSelect;
        this.currentRole = role;
        initializeUI();
    }

    private void initializeUI() {
        setPreferredSize(new Dimension(280, 0));
        setBackground(Colors.SIDEBAR_BG);
        setLayout(new BorderLayout());

        // Header with clinic info
        add(createHeader(), BorderLayout.NORTH);

        // Main menu items
        add(createMenuPanel(), BorderLayout.CENTER);

        // Footer with user info
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Colors.PRIMARY_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        header.setPreferredSize(new Dimension(280, 140));

        // Clinic icon and name
        JLabel iconLabel = new JLabel("🏥");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("UPSA Clinic");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Colors.TEXT_LIGHT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Medical Center");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(iconLabel);
        header.add(Box.createRigidArea(new Dimension(0, 10)));
        header.add(titleLabel);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(subtitleLabel);

        return header;
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Colors.SIDEBAR_BG);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Main navigation items
        addMenuButton(menuPanel, "📊", "Dashboard", "dashboard", true);
        addMenuButton(menuPanel, "👥", "Patients", "patients", false);
        addMenuButton(menuPanel, "⚕️", "Doctors", "doctors", false);
        addMenuButton(menuPanel, "📅", "Appointments", "appointments", false);
        addMenuButton(menuPanel, "💰", "Billing", "billing", false);
        addMenuButton(menuPanel, "💊", "Pharmacy", "pharmacy", false);
        addMenuButton(menuPanel, "💬", "Messages", "messages", false);

        // Medical-specific items (conditionally shown based on role)
        if (hasMedicalAccess()) {
            addMenuButton(menuPanel, "❤️", "Patient Vitals", "vitals", false);
            addMenuButton(menuPanel, "🛏️", "Bed Management", "beds", false);
        }

        addMenuButton(menuPanel, "⚙️", "Settings", "settings", false);

        return menuPanel;
    }

    private void addMenuButton(JPanel parent, String icon, String text, String key, boolean selected) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(15, 0));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(280, 50));
        button.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setPreferredSize(new Dimension(24, 24));

        // Text
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setHorizontalAlignment(SwingConstants.LEFT);

        button.add(iconLabel, BorderLayout.WEST);
        button.add(textLabel, BorderLayout.CENTER);

        // Set initial state
        if (selected) {
            setButtonSelected(button);
            selectedButton = button;
        } else {
            setButtonNormal(button);
        }

        // Check if button should be enabled based on role permissions
        if (!hasPermission(key)) {
            button.setEnabled(false);
            textLabel.setForeground(Colors.TEXT_MUTED);
            button.setToolTipText("Access restricted for " + currentRole + " role");
        }

        // Action listener
        button.addActionListener(e -> {
            if (button.isEnabled()) {
                if (selectedButton != null) {
                    setButtonNormal(selectedButton);
                }
                setButtonSelected(button);
                selectedButton = button;

                if (onSelect != null) {
                    onSelect.accept(key);
                }
            }
        });

        parent.add(button);
        parent.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void setButtonSelected(JButton button) {
        button.setBackground(Colors.PRIMARY_LIGHT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, Colors.ACCENT),
                BorderFactory.createEmptyBorder(0, 21, 0, 15)
        ));

        Component[] components = button.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setForeground(Colors.TEXT_LIGHT);
            }
        }
    }

    private void setButtonNormal(JButton button) {
        button.setBackground(Colors.SIDEBAR_BG);
        button.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 15));

        Component[] components = button.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                if (button.isEnabled()) {
                    ((JLabel) comp).setForeground(new Color(200, 200, 200));
                } else {
                    ((JLabel) comp).setForeground(Colors.TEXT_MUTED);
                }
            }
        }
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBackground(Colors.SIDEBAR_BG);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        footer.setPreferredSize(new Dimension(280, 80));

        // User role info
        JLabel roleLabel = new JLabel("Logged in as: " + currentRole);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleLabel.setForeground(new Color(180, 180, 180));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Version info
        JLabel versionLabel = new JLabel("v2.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        versionLabel.setForeground(new Color(150, 150, 150));
        versionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        footer.add(roleLabel);
        footer.add(Box.createRigidArea(new Dimension(0, 5)));
        footer.add(versionLabel);

        return footer;
    }

    private boolean hasMedicalAccess() {
        return currentRole.equalsIgnoreCase("admin") ||
                currentRole.equalsIgnoreCase("doctor") ||
                currentRole.equalsIgnoreCase("nurse");
    }

    private boolean hasPermission(String panelKey) {
        switch (panelKey) {
            case "billing":
            case "settings":
                return currentRole.equalsIgnoreCase("admin");
            case "doctors":
                return currentRole.equalsIgnoreCase("admin") || currentRole.equalsIgnoreCase("doctor");
            case "pharmacy":
                return currentRole.equalsIgnoreCase("admin") || currentRole.equalsIgnoreCase("nurse");
            case "vitals":
            case "beds":
                return hasMedicalAccess();
            default:
                return true; // dashboard, patients, appointments, messages are available to all
        }
    }

    // Public methods
    public void setCurrentRole(String role) {
        this.currentRole = role;
        // Refresh the menu to update permissions
        refreshMenu();
    }

    public void selectPanel(String panelKey) {
        // Programmatically select a panel
        if (onSelect != null) {
            onSelect.accept(panelKey);
        }
    }

    public void refreshMenu() {
        // Remove all components and rebuild
        removeAll();
        initializeUI();
        revalidate();
        repaint();
    }

    public void updateUserInfo(String username, String role) {
        setCurrentRole(role);
        // You could enhance this to show username in footer
    }

    // Utility method to check if a panel is accessible
    public boolean isPanelAccessible(String panelKey) {
        return hasPermission(panelKey);
    }
}