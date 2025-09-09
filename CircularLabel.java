package Rent_Rover;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class CircularLabel {

    /**
     * Converts the image inside a JLabel into a circular form.
     * @param label The JLabel containing the image.
     */
    public static void makeCircular(JLabel label) {
        Icon icon = label.getIcon();

        if (icon instanceof ImageIcon) {
            Image img = ((ImageIcon) icon).getImage();
            int size = Math.min(label.getWidth(), label.getHeight());

            // Create a circular buffered image
            Image circularImage = createCircularImage(img, size);

            // Set it back to the JLabel
            label.setIcon(new ImageIcon(circularImage));
        }
    }

    /**
     * Creates a circular cropped version of the given image.
     * @param srcImage The original image.
     * @param size The diameter of the circle.
     * @return Circular image.
     */
    private static Image createCircularImage(Image srcImage, int size) {
        BufferedImage circularImg = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circularImg.createGraphics();

        // Enable antialiasing for smooth edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Clip in a circle
        g2.setClip(new Ellipse2D.Float(0, 0, size, size));

        // Draw the original image scaled to fit
        g2.drawImage(srcImage, 0, 0, size, size, null);
        g2.dispose();

        return circularImg;
    }
}
