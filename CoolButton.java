package Rent_Rover;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CoolButton extends JButton {

    private Color startColor = new Color(0, 102, 102);    // Dark Teal
private Color endColor = new Color(0, 153, 153);      // Slightly lighter Teal
private Color hoverStartColor = new Color(0, 120, 120); 
private Color hoverEndColor = new Color(0, 180, 180);
    private boolean hover = false;
    private int radius = 10;

    public CoolButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(new EmptyBorder(10, 20, 10, 20));
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Mouse hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient depending on hover
        GradientPaint gp = new GradientPaint(
                0, 0,
                hover ? hoverStartColor : startColor,
                0, getHeight(),
                hover ? hoverEndColor : endColor
        );

        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // Optional shadow
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);

        super.paintComponent(g2);
        g2.dispose();
    }
}
