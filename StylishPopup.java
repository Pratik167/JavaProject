package Rent_Rover;

import javax.swing.*;
import java.awt.*;

public class StylishPopup {
    public static void showMessage(Component parent, String message, String title, Color bgColor) {
        JLabel label = new JLabel(message);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(bgColor);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JOptionPane.showMessageDialog(parent, label, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void showError(Component parent, String message) {
        showMessage(parent, message, "Error", new Color(220, 53, 69)); // red
    }

    public static void showSuccess(Component parent, String message) {
        showMessage(parent, message, "Success", new Color(0, 158, 158)); // teal
    }
}
