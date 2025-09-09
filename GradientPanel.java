package Rent_Rover;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import javax.swing.JPanel;

public class GradientPanel extends JPanel {

    private Color startColor;
    private Color endColor;
    private Direction direction;

    // Enum for gradient directions
    public enum Direction {
        VERTICAL,
        HORIZONTAL,
        DIAGONAL
    }

    // Constructor
    public GradientPanel(Color startColor, Color endColor, Direction direction) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.direction = direction;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        GradientPaint gp;

        switch (direction) {
            case HORIZONTAL:
                gp = new GradientPaint(0, 0, startColor, width, 0, endColor);
                break;
            case DIAGONAL:
                gp = new GradientPaint(0, 0, startColor, width, height, endColor);
                break;
            case VERTICAL:
            default:
                gp = new GradientPaint(0, 0, startColor, 0, height, endColor);
                break;
        }

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
}
