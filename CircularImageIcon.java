package Rent_Rover;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class CircularImageIcon {

    public static void setCircularImage(JLabel label, String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        int diameter = Math.min(label.getWidth(), label.getHeight());

        // Create a buffered image for circular mask
        BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = masked.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a circle
        g2.fillOval(0, 0, diameter, diameter);

        // Set the clipping to the circle
        g2.setComposite(AlphaComposite.SrcIn);

        // Scale original image to fit the circle
        Image scaled = icon.getImage().getScaledInstance(diameter, diameter, Image.SCALE_SMOOTH);
        g2.drawImage(scaled, 0, 0, null);

        g2.dispose();

        label.setIcon(new ImageIcon(masked));
    }
}
