package Rent_Rover;

import javax.swing.*;
import java.awt.*;

public class CustomPopup {

    public static void showInfo(Component parent, String message, String title) {
        showPopup(parent, message, title, Color.GREEN, "info.png");
    }

    public static void showError(Component parent, String message, String title) {
        showPopup(parent, message, title, Color.RED, "error.png");
    }

    public static void showWarning(Component parent, String message, String title) {
        showPopup(parent, message, title, Color.ORANGE, "warning.png");
    }

    private static void showPopup(Component parent, String message, String title, Color fgColor, String iconPath) {
        // Custom label
        JLabel label = new JLabel(message);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(fgColor);

        // Custom panel
        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 45, 45));
        panel.add(label);

        // Load icon if exists
        ImageIcon icon = null;
        try {
            icon = new ImageIcon("src/Rent_Rover/images/" + iconPath);
        } catch (Exception e) {
            System.out.println("Icon not found: " + iconPath);
        }

        JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.PLAIN_MESSAGE, icon);
    }
}
