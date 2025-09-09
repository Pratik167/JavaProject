package Rent_Rover;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageScaler {

    // ---------------------------
    // For JLabel
    public static void setScaledImage(JLabel label, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(
                label.getWidth(),
                label.getHeight(),
                Image.SCALE_SMOOTH
        );
        label.setIcon(new ImageIcon(img));
    }

    // ---------------------------
    // For JPanel as background
    public static JPanel setPanelBackground(JPanel panel, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(
                panel.getWidth(),
                panel.getHeight(),
                Image.SCALE_SMOOTH
        );

        // Create a JPanel with overridden paintComponent
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(panel.getLayout()); // preserve original layout
        return bgPanel;
    }
}
