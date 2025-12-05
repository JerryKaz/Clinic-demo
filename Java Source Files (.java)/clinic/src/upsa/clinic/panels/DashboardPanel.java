package upsa.clinic.panels;

import upsa.clinic.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class DashboardPanel extends JPanel {

    private JLabel welcomeLabel;
    private JLabel dateLabel;
    private Timer animationTimer;
    private int animationStep = 0;

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(Colors.BACKGROUND);
        initializeUI();
    }

    private void initializeUI() {
        // Header with welcome and date
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Main content with stats and charts
        add(createMainContentPanel(), BorderLayout.CENTER);

        // Start animations
        startAnimations();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Colors.BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Welcome message
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        welcomePanel.setBackground(Colors.BACKGROUND);

        JLabel iconLabel = new JLabel("🏥");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        welcomeLabel = new JLabel("Welcome to UPSA Clinic Dashboard");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Colors.TEXT_PRIMARY);

        welcomePanel.add(iconLabel);
        welcomePanel.add(welcomeLabel);

        // Date and time
        dateLabel = new JLabel();
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(Colors.TEXT_SECONDARY);
        updateDateTime();

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        datePanel.setBackground(Colors.BACKGROUND);
        datePanel.add(dateLabel);

        headerPanel.add(welcomePanel, BorderLayout.WEST);
        headerPanel.add(datePanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainContentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(Colors.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));

        // Statistics cards
        mainPanel.add(createStatsPanel(), BorderLayout.NORTH);

        // Charts and additional info
        mainPanel.add(createChartsPanel(), BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        statsPanel.setBackground(Colors.BACKGROUND);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Medical statistics cards
        statsPanel.add(createModernStatCard("📊", "Today's Appointments", "24", "Scheduled", Colors.INFO));
        statsPanel.add(createModernStatCard("👥", "Total Patients", "1,248", "Registered", Colors.PRIMARY));
        statsPanel.add(createModernStatCard("⚕️", "Active Doctors", "18", "On Duty", Colors.SUCCESS));
        statsPanel.add(createModernStatCard("💊", "Pharmacy Stock", "92%", "Available", Colors.WARNING));
        statsPanel.add(createModernStatCard("💰", "Monthly Revenue", "₵16,968", "This Month", Colors.MEDICAL_GREEN));
        statsPanel.add(createModernStatCard("🛏️", "Bed Occupancy", "78%", "Occupied", Colors.MEDICAL_BLUE));
        statsPanel.add(createModernStatCard("❤️", "Patient Vitals", "156", "Monitored", Colors.MEDICAL_RED));
        statsPanel.add(createModernStatCard("📋", "Pending Tests", "34", "Awaiting Results", Colors.MEDICAL_PURPLE));

        return statsPanel;
    }

    private JPanel createModernStatCard(String icon, String title, String value, String subtitle, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setForeground(color);

        // Text content
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Colors.TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitleLabel.setForeground(Colors.TEXT_SECONDARY);

        textPanel.add(valueLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        textPanel.add(subtitleLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createChartsPanel() {
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        chartsPanel.setBackground(Colors.BACKGROUND);

        // Patient visits chart
        chartsPanel.add(createChartPanel("📈 Patient Visits Trend", "Monthly patient visit statistics", createSampleChart()));

        // Revenue chart
        chartsPanel.add(createChartPanel("💰 Revenue Analysis", "Monthly revenue breakdown", createRevenueChart()));

        return chartsPanel;
    }

    private JPanel createChartPanel(String title, String description, JPanel chartContent) {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Colors.BORDER, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Colors.TEXT_PRIMARY);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(Colors.TEXT_SECONDARY);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(descLabel, BorderLayout.SOUTH);

        chartPanel.add(headerPanel, BorderLayout.NORTH);
        chartPanel.add(chartContent, BorderLayout.CENTER);

        return chartPanel;
    }

    private JPanel createSampleChart() {
        JPanel chart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 40;

                // Draw grid
                g2d.setColor(Colors.BORDER_LIGHT);
                for (int i = 1; i <= 4; i++) {
                    int y = padding + (height - 2 * padding) * i / 5;
                    g2d.drawLine(padding, y, width - padding, y);
                }

                // Draw data points
                int[] visits = {120, 180, 220, 190, 250, 300, 280, 320, 350, 380, 400, 420};
                int maxVisits = 500;
                int pointCount = visits.length;

                // Draw line
                g2d.setColor(Colors.PRIMARY);
                g2d.setStroke(new BasicStroke(3));
                for (int i = 0; i < pointCount - 1; i++) {
                    int x1 = padding + (width - 2 * padding) * i / (pointCount - 1);
                    int y1 = height - padding - (height - 2 * padding) * visits[i] / maxVisits;
                    int x2 = padding + (width - 2 * padding) * (i + 1) / (pointCount - 1);
                    int y2 = height - padding - (height - 2 * padding) * visits[i + 1] / maxVisits;
                    g2d.drawLine(x1, y1, x2, y2);
                }

                // Draw points
                g2d.setColor(Colors.ACCENT);
                for (int i = 0; i < pointCount; i++) {
                    int x = padding + (width - 2 * padding) * i / (pointCount - 1);
                    int y = height - padding - (height - 2 * padding) * visits[i] / maxVisits;
                    g2d.fillOval(x - 4, y - 4, 8, 8);
                }

                // Draw labels
                g2d.setColor(Colors.TEXT_SECONDARY);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                for (int i = 0; i < pointCount; i++) {
                    int x = padding + (width - 2 * padding) * i / (pointCount - 1);
                    g2d.drawString(months[i], x - 10, height - padding + 15);
                }
            }
        };
        chart.setPreferredSize(new Dimension(400, 250));
        chart.setBackground(Color.WHITE);
        return chart;
    }

    private JPanel createRevenueChart() {
        JPanel chart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 40;

                // Draw bars
                double[] revenue = {12000, 14000, 13000, 16000, 15500, 16968, 18000, 17500, 19000, 18500, 20000, 19500};
                double maxRevenue = 25000;
                int barCount = revenue.length;
                int barWidth = (width - 2 * padding) / barCount;

                for (int i = 0; i < barCount; i++) {
                    int barHeight = (int) ((height - 2 * padding) * revenue[i] / maxRevenue);
                    int x = padding + i * barWidth + barWidth / 4;
                    int y = height - padding - barHeight;

                    g2d.setColor(Colors.CHART_COLORS[i % Colors.CHART_COLORS.length]);
                    g2d.fillRect(x, y, barWidth / 2, barHeight);

                    // Draw value on top
                    g2d.setColor(Colors.TEXT_PRIMARY);
                    g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                    String value = "₵" + (int)(revenue[i] / 1000) + "K";
                    g2d.drawString(value, x - 5, y - 5);
                }

                // Draw labels
                g2d.setColor(Colors.TEXT_SECONDARY);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                String[] months = {"J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D"};
                for (int i = 0; i < barCount; i++) {
                    int x = padding + i * barWidth + barWidth / 4;
                    g2d.drawString(months[i], x + 5, height - padding + 15);
                }
            }
        };
        chart.setPreferredSize(new Dimension(400, 250));
        chart.setBackground(Color.WHITE);
        return chart;
    }

    private void updateDateTime() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        String formattedDate = currentDate.format(formatter);
        dateLabel.setText(formattedDate);
    }

    private void startAnimations() {
        // Update date every minute
        Timer dateTimer = new Timer(60000, e -> updateDateTime());
        dateTimer.start();

        // Welcome message animation
        animationTimer = new Timer(100, new ActionListener() {
            private String[] welcomeMessages = {
                    "Welcome to UPSA Clinic Dashboard",
                    "Managing Healthcare Excellence",
                    "Patient Care at Its Best",
                    "Innovative Medical Solutions"
            };
            private int messageIndex = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                animationStep++;
                if (animationStep % 50 == 0) { // Change message every 5 seconds
                    messageIndex = (messageIndex + 1) % welcomeMessages.length;
                    welcomeLabel.setText(welcomeMessages[messageIndex]);
                }
            }
        });
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Add subtle background pattern for medical theme
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw subtle medical pattern in background
        g2d.setColor(new Color(245, 248, 255));
        for (int i = 0; i < getWidth(); i += 50) {
            for (int j = 0; j < getHeight(); j += 50) {
                g2d.drawString("⚕", i, j);
            }
        }
    }

    // Clean up timers when panel is removed
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
}