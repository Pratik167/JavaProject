/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Rent_Rover;

/**
 *
 * @author 97798
 */
import javax.swing.*;
import java.awt.*;
public class ImageScalerP extends JPanel{
    private Image image;
    public ImageScalerP(String imagePath)
    {
        ImageIcon icon= new ImageIcon(imagePath);
        this.image = icon.getImage();
    }
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if(image != null)
        {
            g.drawImage(image,0,0,getWidth(), getHeight(),this);
        }
    }
}
