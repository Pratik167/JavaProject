package Rent_Rover;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedImagePanel extends JPanel {
    private Image backgroundImage;
    private int cornerRadius = 30; // default, can change

    // Constructor with image path and optional corner radius
    public RoundedImagePanel(String imagePath, int radius) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            backgroundImage = icon.getImage();
        } catch (Exception e) {
            System.out.println("Image not found: " + imagePath);
        }
        cornerRadius = radius;
        setOpaque(false); // important for rounded corners
        setLayout(null);  // optional: allows you to place components freely
    }

    // Overloaded constructor with default radius
    public RoundedImagePanel(String imagePath) {
        this(imagePath, 30);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background image scaled to panel size
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        }

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getForeground());
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius));

        g2.dispose();
    }
}
