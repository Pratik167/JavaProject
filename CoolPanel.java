package Rent_Rover;

import java.awt.*;
import javax.swing.*;

public class CoolPanel extends JPanel {
    private int cornerRadius;

    public CoolPanel(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false); // allow transparent corners
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // ---- 1. Draw shadow ----
        int shadowGap = 5;
        Color shadowColor = new Color(0, 0, 0, 80); // semi-transparent black
        g2.setColor(shadowColor);
        g2.fillRoundRect(shadowGap, shadowGap, width - shadowGap, height - shadowGap, cornerRadius, cornerRadius);

        // ---- 2. Draw background ----
        g2.setColor(getBackground()); // use panel background color
        g2.fillRoundRect(0, 0, width - shadowGap, height - shadowGap, cornerRadius, cornerRadius);

        // ---- 3. Optional border ----
        g2.setColor(new Color(0, 0, 0, 60));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(0, 0, width - shadowGap, height - shadowGap, cornerRadius, cornerRadius);

        g2.dispose();
    }
}
