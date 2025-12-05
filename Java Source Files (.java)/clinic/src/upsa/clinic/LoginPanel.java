package upsa.clinic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Enhanced Modern Login with Icons & Additional Features
 * Uses an in-memory user store (username -> [password, role]).
 * Default demo users:
 *  admin / 1234 -> Admin
 *  nurse1 / 2222 -> Nurse
 *  doctor1 / 3333 -> Doctor
 */
public class LoginPanel extends JFrame {

    private final Map<String, String[]> users = new HashMap<>();
    private JTextField username;
    private JPasswordField password;
    private JButton login;

    public LoginPanel() {
        // demo users
        users.put("admin", new String[]{"1234", "Admin"});
        users.put("nurse1", new String[]{"2222", "Nurse"});
        users.put("doctor1", new String[]{"3333", "Doctor"});

        initializeUI();
    }

    private void initializeUI() {
        setTitle("UPSA Clinic — Login");
        setSize(520, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background with gradient
        JPanel background = createBackgroundPanel();
        setContentPane(background);
    }

    private JPanel createBackgroundPanel() {
        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(120, 80, 200), getWidth(), getHeight(), new Color(40, 140, 200));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(new GridBagLayout());

        GridBagConstraints mg = new GridBagConstraints();
        mg.gridwidth = GridBagConstraints.REMAINDER;
        mg.fill = GridBagConstraints.HORIZONTAL;

        // Header with icon
        JPanel headerPanel = createHeaderPanel();
        mg.gridy = 0;
        mg.insets = new Insets(40, 0, 20, 0);
        background.add(headerPanel, mg);

        // Login card
        JPanel card = createLoginCard();
        mg.gridy = 1;
        mg.insets = new Insets(0, 0, 0, 0);
        background.add(card, mg);

        // Footer with version info
        JLabel versionLabel = new JLabel("UPSA Clinic v2.0 • Medical Management System", SwingConstants.CENTER);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versionLabel.setForeground(new Color(255, 255, 255, 180));
        mg.gridy = 2;
        mg.insets = new Insets(20, 0, 10, 0);
        background.add(versionLabel, mg);

        return background;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hospital icon
        JLabel iconLabel = new JLabel("🏥", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("UPSA Clinic", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Medical Center Management", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(iconLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createLoginCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 240));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Add subtle shadow
                g2.setColor(new Color(0, 0, 0, 20));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
            }
        };
        card.setOpaque(false);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(380, 460));
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        // Card header
        JLabel cardHeader = new JLabel("Welcome Back", SwingConstants.CENTER);
        cardHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
        cardHeader.setForeground(Colors.PRIMARY);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(cardHeader, gbc);

        JLabel cardSubtitle = new JLabel("Sign in to continue", SwingConstants.CENTER);
        cardSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cardSubtitle.setForeground(new Color(100, 100, 100));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        card.add(cardSubtitle, gbc);

        // Username field with icon
        JPanel usernamePanel = createInputFieldWithIcon("👤", "Username");
        username = (JTextField) ((JPanel) usernamePanel.getComponent(1)).getComponent(0);
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        card.add(usernamePanel, gbc);

        // Password field with icon
        JPanel passwordPanel = createInputFieldWithIcon("🔒", "Password");
        password = (JPasswordField) ((JPanel) passwordPanel.getComponent(1)).getComponent(0);
        gbc.gridy = 3;
        card.add(passwordPanel, gbc);

        // Options row
        JPanel optionsPanel = createOptionsPanel();
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 0, 20, 0);
        card.add(optionsPanel, gbc);

        // Login button
        login = createLoginButton();
        gbc.gridy = 5;
        card.add(login, gbc);

        // Demo credentials
        JPanel demoPanel = createDemoCredentialsPanel();
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 0, 0, 0);
        card.add(demoPanel, gbc);

        return card;
    }

    private JPanel createInputFieldWithIcon(String icon, String placeholder) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setPreferredSize(new Dimension(30, 30));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Input field
        JTextField field;
        if (placeholder.equals("Password")) {
            field = new JPasswordField();
        } else {
            field = new JTextField();
        }

        setupModernTextField(field, placeholder);

        // Field container for styling
        JPanel fieldContainer = new JPanel(new BorderLayout());
        fieldContainer.setOpaque(false);
        fieldContainer.add(field, BorderLayout.CENTER);

        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(fieldContainer, BorderLayout.CENTER);

        return panel;
    }

    private void setupModernTextField(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        field.setBackground(Color.WHITE);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Colors.PRIMARY, 2),
                        BorderFactory.createEmptyBorder(11, 11, 11, 11)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)
                ));
            }
        });

        // Enter key support
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login.doClick();
                }
            }
        });
    }

    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Remember me
        JCheckBox remember = new JCheckBox(" Remember me");
        remember.setOpaque(false);
        remember.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        remember.setForeground(new Color(100, 100, 100));

        // Forgot password
        JButton forgot = new JButton("Forgot password?");
        forgot.setBorder(null);
        forgot.setContentAreaFilled(false);
        forgot.setForeground(Colors.ACCENT);
        forgot.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        forgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel.add(remember, BorderLayout.WEST);
        panel.add(forgot, BorderLayout.EAST);

        return panel;
    }

    private JButton createLoginButton() {
        JButton button = new JButton("SIGN IN");
        button.setBackground(Colors.PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Colors.PRIMARY.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Colors.PRIMARY);
            }
        });

        // Login action
        button.addActionListener(e -> performLogin());

        return button;
    }

    private JPanel createDemoCredentialsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Demo Credentials:", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 11));
        title.setForeground(new Color(120, 120, 120));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel admin = new JLabel("Admin: admin / 1234", SwingConstants.CENTER);
        admin.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        admin.setForeground(new Color(140, 140, 140));
        admin.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nurse = new JLabel("Nurse: nurse1 / 2222", SwingConstants.CENTER);
        nurse.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        nurse.setForeground(new Color(140, 140, 140));
        nurse.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel doctor = new JLabel("Doctor: doctor1 / 3333", SwingConstants.CENTER);
        doctor.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        doctor.setForeground(new Color(140, 140, 140));
        doctor.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 3)));
        panel.add(admin);
        panel.add(nurse);
        panel.add(doctor);

        return panel;
    }

    private void performLogin() {
        String u = username.getText().trim();
        String p = new String(password.getPassword()).trim();

        // Validate empty fields
        if (u.isEmpty() || u.equals("Username") || p.isEmpty() || p.equals("Password")) {
            showError("Please enter both username and password");
            return;
        }

        if (!users.containsKey(u)) {
            showError("Unknown user. Please check your username.");
            return;
        }

        String[] info = users.get(u);
        if (!info[0].equals(p)) {
            showError("Wrong password. Please try again.");
            return;
        }

        String role = info[1];
        dispose();

        // Show loading message
        JOptionPane.showMessageDialog(this,
                "Welcome " + u + "!\nRole: " + role,
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE);

        // Open dashboard with role
        new DashboardView(role).setVisible(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Failed", JOptionPane.ERROR_MESSAGE);
        password.setText("");
        password.requestFocus();
    }
}