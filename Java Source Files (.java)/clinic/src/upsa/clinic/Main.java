package upsa.clinic;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginPanel().setVisible(true);
        });
    }
}