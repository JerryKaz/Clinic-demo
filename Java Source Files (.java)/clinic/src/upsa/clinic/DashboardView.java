package upsa.clinic;

import upsa.clinic.panels.*;
import upsa.clinic.components.*;

import javax.swing.*;
import java.awt.*;

/**
 * Enhanced DashboardView with modern design, role-based access control,
 * and improved UI/UX features.
 */
public class DashboardView extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private HeaderBar headerBar;
    private SidebarMenu sidebarMenu;

    private final String role;
    private final String username;

    // Role permissions - UPDATED: Added vitals and beds panels
    private static final String[] ADMIN_PERMISSIONS = {
            "dashboard", "patients", "doctors", "appointments", "billing",
            "pharmacy", "messages", "settings", "vitals", "beds"
    };
    private static final String[] DOCTOR_PERMISSIONS = {
            "dashboard", "patients", "appointments", "messages", "vitals"
    };
    private static final String[] NURSE_PERMISSIONS = {
            "dashboard", "patients", "appointments", "messages", "vitals", "beds"
    };

    public DashboardView(String role) {
        this.role = role == null ? "Unknown" : role;
        this.username = "User"; // Default username

        initializeUI();
    }

    private void initializeUI() {
        setTitle("UPSA Clinic — " + this.role + " Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen dashboard
        setLocationRelativeTo(null);

        initComponents();
        setupLayout();
        applyModernStyling();
    }

    private void initComponents() {
        // Header with user info and logout callback
        headerBar = new HeaderBar(() -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginPanel().setVisible(true);
            }
        });

        // Sidebar with role-based menu items
        sidebarMenu = new SidebarMenu(this::switchPanel);

        // Initialize all panels
        initializePanels();
    }

    private void initializePanels() {
        // Create and register all panels
        cards.add(new DashboardPanel(), "dashboard");
        cards.add(new PatientsPanel(), "patients");
        cards.add(new DoctorsPanel(), "doctors");
        cards.add(new AppointmentPanel(), "appointments");
        cards.add(new BillingPanel(), "billing");
        cards.add(new PharmacyPanel(), "pharmacy");
        cards.add(new MessagesPanel(), "messages");
        cards.add(new SettingsPanel(), "settings");

        // Add the new panels
        cards.add(new PatientVitalsPanel(), "vitals");
        cards.add(new BedManagementPanel(), "beds");
    }

    private void setupLayout() {
        // Create main container with modern layout
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(Colors.BACKGROUND);

        // Add components to main container
        mainContainer.add(headerBar, BorderLayout.NORTH);
        mainContainer.add(sidebarMenu, BorderLayout.WEST);

        // Cards panel with modern styling
        JPanel cardsContainer = new JPanel(new BorderLayout());
        cardsContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cardsContainer.add(cards, BorderLayout.CENTER);

        mainContainer.add(cardsContainer, BorderLayout.CENTER);

        setContentPane(mainContainer);

        // Show dashboard by default
        switchToPanel("dashboard");
    }

    private void applyModernStyling() {
        // Set modern UI properties
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel
            System.out.println("Using default look and feel");
        }
    }

    public void switchPanel(String panelName) {
        switchToPanel(panelName);
    }

    private void switchToPanel(String panelName) {
        if (!hasPermission(panelName)) {
            showAccessDeniedMessage(panelName);
            return;
        }

        // Show the selected panel
        cardLayout.show(cards, panelName);

        // Refresh panel data if needed
        refreshPanel(panelName);
    }

    private boolean hasPermission(String panelName) {
        String[] permissions = getPermissionsForRole();
        for (String permission : permissions) {
            if (permission.equals(panelName)) {
                return true;
            }
        }
        return false;
    }

    private String[] getPermissionsForRole() {
        switch (this.role.toLowerCase()) {
            case "admin":
                return ADMIN_PERMISSIONS;
            case "doctor":
                return DOCTOR_PERMISSIONS;
            case "nurse":
                return NURSE_PERMISSIONS;
            default:
                return new String[]{"dashboard"}; // Minimal access for unknown roles
        }
    }

    private void showAccessDeniedMessage(String panelName) {
        String panelTitle = getPanelTitle(panelName);
        JOptionPane.showMessageDialog(
                this,
                "Access to " + panelTitle + " is restricted.\n\n" +
                        "Your role: " + this.role + "\n" +
                        "Required: " + getRequiredRole(panelName),
                "Access Denied",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private String getRequiredRole(String panelName) {
        switch (panelName) {
            case "billing":
            case "settings":
                return "Admin";
            case "doctors":
                return "Admin, Doctor";
            case "pharmacy":
                return "Admin, Nurse";
            case "vitals":
                return "Doctor, Nurse";
            case "beds":
                return "Admin, Nurse";
            default:
                return "All Roles";
        }
    }

    private String getPanelTitle(String panelName) {
        switch (panelName) {
            case "dashboard":
                return "Dashboard Overview";
            case "patients":
                return "Patient Management";
            case "doctors":
                return "Medical Staff";
            case "appointments":
                return "Appointment Scheduling";
            case "billing":
                return "Billing & Payments";
            case "pharmacy":
                return "Pharmacy Inventory";
            case "messages":
                return "Messages & Notifications";
            case "settings":
                return "System Settings";
            case "vitals":
                return "Patient Vitals";
            case "beds":
                return "Bed Management";
            default:
                return "UPSA Clinic";
        }
    }

    private void refreshPanel(String panelName) {
        // Refresh panel data when switched to
        System.out.println("Switched to panel: " + panelName);
        // Individual panels can implement refresh logic if needed
    }

    // Public methods for external access
    public String getCurrentRole() {
        return role;
    }

    public String getCurrentUsername() {
        return username;
    }

    // Simple notification method (you can enhance this later)
    public void showNotification(String message) {
        JOptionPane.showMessageDialog(this, message, "Notification", JOptionPane.INFORMATION_MESSAGE);
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DashboardView("Admin").setVisible(true);
        });
    }
}