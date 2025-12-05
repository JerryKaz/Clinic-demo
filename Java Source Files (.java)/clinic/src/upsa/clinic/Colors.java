package upsa.clinic;

import java.awt.*;

public class Colors {
    // Primary Colors - Your existing colors
    public static final Color PRIMARY = new Color(25, 25, 60);
    public static final Color ACCENT = new Color(0, 120, 215);
    public static final Color BG1 = new Color(245, 248, 255);
    public static final Color CARD = new Color(255, 255, 255, 240);
    public static final Color TRANSLUCENT = new Color(255, 255, 255, 200);

    // Extended Primary Palette
    public static final Color PRIMARY_DARK = new Color(15, 15, 40);
    public static final Color PRIMARY_LIGHT = new Color(40, 40, 80);
    public static final Color SECONDARY = new Color(70, 130, 180);
    public static final Color SECONDARY_LIGHT = new Color(100, 160, 210);

    // Background Colors
    public static final Color BACKGROUND = BG1; // Alias for consistency
    public static final Color BACKGROUND_LIGHT = new Color(250, 252, 255);
    public static final Color SIDEBAR_BG = PRIMARY;
    public static final Color SIDEBAR_HOVER = new Color(40, 40, 90);
    public static final Color HEADER_BG = Color.WHITE;

    // Status Colors - Medical Theme
    public static final Color SUCCESS = new Color(46, 204, 113);      // Green for healthy/positive
    public static final Color SUCCESS_LIGHT = new Color(212, 237, 218);
    public static final Color WARNING = new Color(255, 159, 28);      // Orange for caution
    public static final Color WARNING_LIGHT = new Color(255, 243, 205);
    public static final Color DANGER = new Color(231, 76, 60);        // Red for critical/emergency
    public static final Color DANGER_LIGHT = new Color(248, 215, 218);
    public static final Color INFO = new Color(52, 152, 219);         // Blue for information
    public static final Color INFO_LIGHT = new Color(209, 236, 241);

    // Medical Specific Colors
    public static final Color MEDICAL_BLUE = new Color(0, 112, 192);
    public static final Color MEDICAL_GREEN = new Color(76, 175, 80);
    public static final Color MEDICAL_RED = new Color(229, 57, 53);
    public static final Color MEDICAL_PURPLE = new Color(142, 36, 170);
    public static final Color MEDICAL_TEAL = new Color(0, 150, 136);

    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(33, 37, 41);    // Dark gray for primary text
    public static final Color TEXT_SECONDARY = new Color(108, 117, 125); // Medium gray
    public static final Color TEXT_LIGHT = Color.WHITE;                // White text for dark backgrounds
    public static final Color TEXT_MUTED = new Color(144, 144, 144);   // Muted text
    public static final Color TEXT_DISABLED = new Color(200, 200, 200); // Disabled text

    // Border Colors
    public static final Color BORDER = new Color(222, 226, 230);
    public static final Color BORDER_LIGHT = new Color(233, 236, 239);
    public static final Color BORDER_FOCUS = ACCENT;
    public static final Color BORDER_MEDICAL = new Color(200, 220, 240);

    // Chart Colors for Medical Data Visualization
    public static final Color[] CHART_COLORS = {
            new Color(0, 112, 192),    // Medical Blue
            new Color(76, 175, 80),    // Medical Green
            new Color(229, 57, 53),    // Medical Red
            new Color(142, 36, 170),   // Medical Purple
            new Color(255, 159, 28),   // Warning Orange
            new Color(0, 150, 136),    // Medical Teal
            new Color(121, 85, 72),    // Brown
            new Color(156, 39, 176)    // Purple
    };

    // Patient Status Colors
    public static final Color STATUS_CRITICAL = new Color(229, 57, 53);
    public static final Color STATUS_SERIOUS = new Color(255, 159, 28);
    public static final Color STATUS_STABLE = new Color(76, 175, 80);
    public static final Color STATUS_DISCHARGED = new Color(100, 100, 100);

    // Vital Signs Colors
    public static final Color VITAL_NORMAL = new Color(76, 175, 80);
    public static final Color VITAL_WARNING = new Color(255, 193, 7);
    public static final Color VITAL_CRITICAL = new Color(229, 57, 53);

    // Gradient Methods for Modern UI
    public static GradientPaint createPrimaryGradient(int width, int height) {
        return new GradientPaint(0, 0, PRIMARY, width, height, PRIMARY_LIGHT);
    }

    public static GradientPaint createAccentGradient(int width, int height) {
        return new GradientPaint(0, 0, ACCENT, width, height, SECONDARY_LIGHT);
    }

    public static GradientPaint createMedicalGradient(int width, int height) {
        return new GradientPaint(0, 0, MEDICAL_BLUE, width, height, MEDICAL_TEAL);
    }

    public static GradientPaint createSuccessGradient(int width, int height) {
        return new GradientPaint(0, 0, SUCCESS, width, height, new Color(56, 224, 123));
    }

    public static GradientPaint createWarningGradient(int width, int height) {
        return new GradientPaint(0, 0, WARNING, width, height, new Color(255, 179, 48));
    }

    public static GradientPaint createDangerGradient(int width, int height) {
        return new GradientPaint(0, 0, DANGER, width, height, new Color(241, 96, 80));
    }

    // Utility methods for color manipulation
    public static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static Color darken(Color color, double factor) {
        return new Color(
                Math.max((int)(color.getRed() * (1 - factor)), 0),
                Math.max((int)(color.getGreen() * (1 - factor)), 0),
                Math.max((int)(color.getBlue() * (1 - factor)), 0),
                color.getAlpha()
        );
    }

    public static Color lighten(Color color, double factor) {
        return new Color(
                Math.min((int)(color.getRed() * (1 + factor)), 255),
                Math.min((int)(color.getGreen() * (1 + factor)), 255),
                Math.min((int)(color.getBlue() * (1 + factor)), 255),
                color.getAlpha()
        );
    }

    // Medical-specific color getters
    public static Color getVitalColor(double value, double minNormal, double maxNormal) {
        if (value >= minNormal && value <= maxNormal) {
            return VITAL_NORMAL;
        } else if (value >= minNormal * 0.8 && value <= maxNormal * 1.2) {
            return VITAL_WARNING;
        } else {
            return VITAL_CRITICAL;
        }
    }

    public static Color getPatientStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "critical": return STATUS_CRITICAL;
            case "serious": return STATUS_SERIOUS;
            case "stable": return STATUS_STABLE;
            case "discharged": return STATUS_DISCHARGED;
            default: return TEXT_MUTED;
        }
    }
}