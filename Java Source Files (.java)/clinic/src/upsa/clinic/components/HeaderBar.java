package upsa.clinic.components;

import upsa.clinic.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Modern HeaderBar with search, notifications, user info, and logout
 */
public class HeaderBar extends JPanel {

    private JLabel titleLabel;
    private JLabel userInfoLabel;
    private JTextField searchField;
    private JButton notificationButton;
    private int notificationCount = 0;

    public HeaderBar(Runnable onLogout) {
        initializeUI(onLogout);
    }

    // Additional constructor for user information
    public HeaderBar(String username, String role, Runnable onLogout) {
        initializeUI(onLogout);
        setUserInfo(username, role);
    }

    private void initializeUI(Runnable onLogout) {
        setPreferredSize(new Dimension(0, 70));
        setBackground(Colors.HEADER_BG);
        setLayout(new BorderLayout(20, 0));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Colors.BORDER),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));

        // Left section - Title and breadcrumb
        JPanel leftPanel = createLeftPanel();

        // Center section - Search
        JPanel centerPanel = createSearchPanel();

        // Right section - User info and actions
        JPanel rightPanel = createRightPanel(onLogout);

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        // Clinic icon
        JLabel iconLabel = new JLabel("🏥");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));

        // Title
        titleLabel = new JLabel("Dashboard Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Colors.TEXT_PRIMARY);

        leftPanel.add(iconLabel);
        leftPanel.add(titleLabel);

        return leftPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setMaximumSize(new Dimension(400, 40));

        // Search field with icon
        JPanel searchContainer = new JPanel(new BorderLayout(8, 0));
        searchContainer.setOpaque(false);
        searchContainer.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Search icon
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        // Search field
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.setBackground(Colors.BACKGROUND);

        // Placeholder text
        searchField.setText("Search patients, doctors, appointments...");
        searchField.setForeground(Colors.TEXT_MUTED);

        // Focus listener for placeholder behavior
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Search patients, doctors, appointments...")) {
                    searchField.setText("");
                    searchField.setForeground(Colors.TEXT_PRIMARY);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search patients, doctors, appointments...");
                    searchField.setForeground(Colors.TEXT_MUTED);
                }
            }
        });

        searchContainer.add(searchIcon, BorderLayout.WEST);
        searchContainer.add(searchField, BorderLayout.CENTER);

        searchPanel.add(searchContainer, BorderLayout.CENTER);
        return searchPanel;
    }

    private JPanel createRightPanel(Runnable onLogout) {
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightPanel.setOpaque(false);

        // Notification button
        notificationButton = createIconButton("🔔", "Notifications (0)");
        notificationButton.addActionListener(e -> showNotifications());

        // User info
        userInfoLabel = new JLabel("Welcome, User");
        userInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userInfoLabel.setForeground(Colors.TEXT_SECONDARY);

        // Logout button
        JButton logoutButton = createLogoutButton(onLogout);

        rightPanel.add(notificationButton);
        rightPanel.add(createSeparator());
        rightPanel.add(userInfoLabel);
        rightPanel.add(createSeparator());
        rightPanel.add(logoutButton);

        return rightPanel;
    }

    private JButton createIconButton(String icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        button.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Colors.BACKGROUND);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    private JButton createLogoutButton(Runnable onLogout) {
        JButton button = new JButton("Logout");
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Colors.DANGER);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.DANGER, 1),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Colors.DANGER_LIGHT);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });

        // Logout action
        button.addActionListener(e -> {
            if (onLogout != null) {
                onLogout.run();
            }
        });

        return button;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setPreferredSize(new Dimension(1, 24));
        separator.setBackground(Colors.BORDER);
        return separator;
    }

    // Public methods to update header content
    public void setTitle(String title) {
        if (titleLabel != null) {
            titleLabel.setText(title);
        }
    }

    public void setUserInfo(String username, String role) {
        if (userInfoLabel != null) {
            userInfoLabel.setText(username + " (" + role + ")");
        }
    }

    public void addSearchListener(ActionListener listener) {
        if (searchField != null) {
            searchField.addActionListener(listener);
        }
    }

    public String getSearchText() {
        return searchField != null ? searchField.getText() : "";
    }

    public void clearSearch() {
        if (searchField != null) {
            searchField.setText("Search patients, doctors, appointments...");
            searchField.setForeground(Colors.TEXT_MUTED);
        }
    }

    public void addNotification(String message) {
        notificationCount++;
        updateNotificationButton();

        // Show notification toast (you can enhance this with a proper notification system)
        showNotificationToast("New notification: " + message);
    }

    private void updateNotificationButton() {
        if (notificationButton != null) {
            notificationButton.setText("🔔");
            notificationButton.setToolTipText("Notifications (" + notificationCount + ")");

            // Add badge for high notification count
            if (notificationCount > 0) {
                notificationButton.setForeground(Colors.DANGER);
            } else {
                notificationButton.setForeground(Colors.TEXT_PRIMARY);
            }
        }
    }

    private void showNotifications() {
        if (notificationCount > 0) {
            JOptionPane.showMessageDialog(this,
                    "You have " + notificationCount + " new notifications",
                    "Notifications",
                    JOptionPane.INFORMATION_MESSAGE);

            // Reset notification count (in real app, you'd mark them as read)
            notificationCount = 0;
            updateNotificationButton();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No new notifications",
                    "Notifications",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showNotificationToast(String message) {
        // Simple toast notification - you can enhance this with a proper toast system
        JOptionPane.showMessageDialog(this, message, "New Notification", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWelcomeMessage(String username, String role) {
        setUserInfo(username, role);
        addNotification("Welcome back, " + username + "! You are logged in as " + role + ".");
    }
}