/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Rent_Rover;

/**
 *
 * @author PC MOD NEPAL
 */
import Rent_Rover.DataBases.DB;
import java.awt.Color;
import Rent_Rover.GradientPanel;
import Rent_Rover.GradientPanel.Direction;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Customer_DashBoard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Customer_DashBoard.class.getName());
    private javax.swing.JPopupMenu suggestionPopup = new javax.swing.JPopupMenu();

    /**
     * Creates new form Primary_Screen
     */
    public Customer_DashBoard(String pickup, String dropoff, String pickupDate, String returnDate) {
    initComponents(); // keep NetBeans auto-generated GUI

    // Set the textfields
    pickuplocation.setText(pickup);
    dropofflocation.setText(dropoff);
    dateField1.setText(pickupDate);
    dateField2.setText(returnDate);

    // ✅ Set scrollable panel properly
    scrollablepanel.setLayout(new BoxLayout(scrollablepanel, BoxLayout.Y_AXIS));
    scrollablepanel.setBackground(Color.WHITE);
    jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    jScrollPane1.setViewportView(scrollablepanel);

    // Load bookings
    loadBookings();
}

    public Customer_DashBoard() {
    initComponents();

    ImageScaler.setScaledImage(RR_logo, "C:\\Users\\97798\\Desktop\\ProjectImages\\RR logo.png");

    // Force vertical stacking for cards
    // after initComponents()
scrollablepanel.setLayout(new BoxLayout(scrollablepanel, BoxLayout.Y_AXIS));
scrollablepanel.setBackground(Color.WHITE);
jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
jScrollPane1.setViewportView(scrollablepanel);

// then call loadBookings() (you probably already do this)
loadBookings();

}


// Updated createBookingPanel to include hover effect and full click functionality
private JPanel createBookingPanel(String vehicleName, byte[] vehicleImage,
                                  String location, String createdAt,
                                  String pricePerDay, String ownerUsername,
                                  String bookedBy) { // no extra parameters

    final int CARD_HEIGHT = 150;
    final int CARD_WIDTH = 1000;

    JPanel panel = new JPanel(null);
    panel.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, CARD_HEIGHT));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

    // Vehicle image
    JLabel imageLabel = new JLabel();
    imageLabel.setBounds(10, 10, 130, 130);
    if (vehicleImage != null && vehicleImage.length > 0) {
        ImageIcon icon = new ImageIcon(vehicleImage);
        Image scaled = icon.getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaled));
    } else {
        imageLabel.setText("No Image");
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
    panel.add(imageLabel);

    // Vehicle name
    JLabel nameLabel = new JLabel(vehicleName);
    nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
    nameLabel.setBounds(150, 10, 500, 26);
    panel.add(nameLabel);

    // Owner username
    JLabel ownerLabel = new JLabel("Owner: " + (ownerUsername == null ? "unknown" : ownerUsername));
    ownerLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
    ownerLabel.setBounds(150, 40, 300, 20);
    panel.add(ownerLabel);

    // Location + price
    JLabel locPrice = new JLabel("Location: " + location + "   |   Price/day: Rs " + pricePerDay);
    locPrice.setFont(new Font("SansSerif", Font.PLAIN, 13));
    locPrice.setBounds(150, 65, 600, 20);
    panel.add(locPrice);

    // Created at
    JLabel dateLabel = new JLabel("Listed On: " + createdAt);
    dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    dateLabel.setForeground(Color.GRAY);
    dateLabel.setBounds(150, 95, 400, 18);
    panel.add(dateLabel);

    // Booking status label
    JLabel bookedByLabel = new JLabel();
    bookedByLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
    bookedByLabel.setForeground(Color.BLUE);
    bookedByLabel.setBounds(150, 115, 400, 20);
    panel.add(bookedByLabel);

    Border defaultBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
    Border hoverBorder = BorderFactory.createLineBorder(new Color(0, 120, 215), 2);
    panel.setBorder(defaultBorder);

    // If already booked, show info and disable
    if (bookedBy != null && !bookedBy.trim().isEmpty()) {
        bookedByLabel.setText("Booked by: " + bookedBy);
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setEnabled(false);
        return panel;
    }

    

    return panel;
}



// Updated loadBookings: no filtering by username, display all
public void loadBookings() {
    scrollablepanel.removeAll();

    final int CARD_HEIGHT = 150;
    final int GAP = 10;
    int count = 0;

    try {
        Connection con = DB.getConnection();

        String query = "SELECT username, brand, model, location, price_per_day, vehicle_image, created_at, booked_by FROM bookings ORDER BY created_at DESC";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            count++;
            String vehicleName = rs.getString("brand") + " " + rs.getString("model");
            byte[] imageBytes = rs.getBytes("vehicle_image");
            String location = rs.getString("location");
            String createdAt = rs.getString("created_at");
            String price = rs.getString("price_per_day");
            String owner = rs.getString("username");
            String bookedBy = rs.getString("booked_by");

            JPanel card = createBookingPanel(vehicleName, imageBytes, location, createdAt, price, owner, bookedBy);

            scrollablepanel.add(card);
            scrollablepanel.add(javax.swing.Box.createRigidArea(new Dimension(0, GAP)));
        }

        rs.close();
        ps.close();
        con.close();
    } catch (Exception e) {
        e.printStackTrace();
    }

    int totalHeight = Math.max(1, count) * (CARD_HEIGHT + GAP) + 20;
    int width = jScrollPane1.getViewport().getWidth();
    if (width <= 0) width = 1000;

    scrollablepanel.setPreferredSize(new Dimension(width, totalHeight));
    scrollablepanel.revalidate();
    scrollablepanel.repaint();

    System.out.println("loadBookings -> cards: " + count + " totalHeight: " + totalHeight + " viewportH: " + jScrollPane1.getViewport().getHeight());
}







    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bottom = new javax.swing.JPanel();
        whitepanel = new ImageScalerP("C:\\Users\\97798\\Desktop\\ProjectImages\\whitebg.jpg");
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        RR_logo = new javax.swing.JLabel();
        Login_button = new javax.swing.JButton();
        SignUp_button = new Rent_Rover.CoolButton("SignUP");
        homee = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        book = new ImageScalerP("C:\\Users\\97798\\Desktop\\ProjectImages\\whitebg.jpg");
        pickuploc = new RoundedPanel(25);
        pickuplocation = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        pickupdate = new RoundedPanel(25);
        dateField1 = new RoundedTextField(25);
        jLabel5 = new javax.swing.JLabel();
        dropoffdate = new RoundedPanel(25);
        dateField2 = new RoundedTextField(25);
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new RoundedPanel(25);
        dropofflocation = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scrollablepanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        bottom.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel2.setText("Rent, Ride, Repeat");

        jLabel1.setFont(new java.awt.Font("Perpetua", 3, 36)); // NOI18N
        jLabel1.setText("RentRover");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
        });

        RR_logo.setMaximumSize(new java.awt.Dimension(76, 51));

        Login_button.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        Login_button.setText("Login");
        Login_button.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Login_button.setBorderPainted(false);
        Login_button.setFocusPainted(false);
        Login_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Login_buttonMouseClicked(evt);
            }
        });
        Login_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Login_buttonActionPerformed(evt);
            }
        });

        SignUp_button.setText("SignUp");
        SignUp_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SignUp_buttonMouseClicked(evt);
            }
        });
        SignUp_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SignUp_buttonActionPerformed(evt);
            }
        });

        homee.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        homee.setText("Home");
        homee.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        homee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeeMouseClicked(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel8.setText("Become a Renter");
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout whitepanelLayout = new javax.swing.GroupLayout(whitepanel);
        whitepanel.setLayout(whitepanelLayout);
        whitepanelLayout.setHorizontalGroup(
            whitepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(whitepanelLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(RR_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(whitepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(whitepanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel2))
                    .addComponent(jLabel1))
                .addGap(418, 418, 418)
                .addComponent(homee)
                .addGap(206, 206, 206)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Login_button, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SignUp_button)
                .addGap(19, 19, 19))
        );
        whitepanelLayout.setVerticalGroup(
            whitepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(whitepanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(whitepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Login_button, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SignUp_button, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(homee, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, whitepanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(whitepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(whitepanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(RR_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        book.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        book.setOpaque(false);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Pick up Location");

        javax.swing.GroupLayout pickuplocLayout = new javax.swing.GroupLayout(pickuploc);
        pickuploc.setLayout(pickuplocLayout);
        pickuplocLayout.setHorizontalGroup(
            pickuplocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pickuplocLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pickuplocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pickuplocation, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                    .addGroup(pickuplocLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pickuplocLayout.setVerticalGroup(
            pickuplocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pickuplocLayout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pickuplocation, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        dateField1.setFont(new java.awt.Font("Perpetua", 0, 14)); // NOI18N
        dateField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dateField1MouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Pick up Date");

        javax.swing.GroupLayout pickupdateLayout = new javax.swing.GroupLayout(pickupdate);
        pickupdate.setLayout(pickupdateLayout);
        pickupdateLayout.setHorizontalGroup(
            pickupdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pickupdateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pickupdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateField1, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                    .addGroup(pickupdateLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pickupdateLayout.setVerticalGroup(
            pickupdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pickupdateLayout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateField1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        dateField2.setFont(new java.awt.Font("Perpetua", 0, 14)); // NOI18N
        dateField2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dateField2MouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Return Date");

        javax.swing.GroupLayout dropoffdateLayout = new javax.swing.GroupLayout(dropoffdate);
        dropoffdate.setLayout(dropoffdateLayout);
        dropoffdateLayout.setHorizontalGroup(
            dropoffdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dropoffdateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dropoffdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateField2, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                    .addGroup(dropoffdateLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        dropoffdateLayout.setVerticalGroup(
            dropoffdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dropoffdateLayout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateField2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Drop off Location");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dropofflocation, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dropofflocation, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel7.setText("Details");

        javax.swing.GroupLayout bookLayout = new javax.swing.GroupLayout(book);
        book.setLayout(bookLayout);
        bookLayout.setHorizontalGroup(
            bookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pickuploc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pickupdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dropoffdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(bookLayout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bookLayout.setVerticalGroup(
            bookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bookLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(pickuploc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pickupdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dropoffdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(129, 129, 129))
        );

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setViewportView(scrollablepanel);

        javax.swing.GroupLayout scrollablepanelLayout = new javax.swing.GroupLayout(scrollablepanel);
        scrollablepanel.setLayout(scrollablepanelLayout);
        scrollablepanelLayout.setHorizontalGroup(
            scrollablepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1014, Short.MAX_VALUE)
        );
        scrollablepanelLayout.setVerticalGroup(
            scrollablepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 642, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(scrollablepanel);

        javax.swing.GroupLayout bottomLayout = new javax.swing.GroupLayout(bottom);
        bottom.setLayout(bottomLayout);
        bottomLayout.setHorizontalGroup(
            bottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(whitepanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bottomLayout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(book, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1016, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        bottomLayout.setVerticalGroup(
            bottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(whitepanel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(bottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(book, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Login_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Login_buttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Login_buttonActionPerformed

    private void SignUp_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SignUp_buttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SignUp_buttonActionPerformed

    private void Login_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Login_buttonMouseClicked
      new Form_Login().setVisible(true);
       this.dispose();             
    }//GEN-LAST:event_Login_buttonMouseClicked

    private void SignUp_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SignUp_buttonMouseClicked
    new Form_SignUp().setVisible(true);
               
    }//GEN-LAST:event_SignUp_buttonMouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        loadBookings();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void homeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeeMouseClicked
        loadBookings();
    }//GEN-LAST:event_homeeMouseClicked

    private void dateField2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateField2MouseClicked
        com.toedter.calendar.JCalendar calendar = new com.toedter.calendar.JCalendar();

        // Create a dialog to hold the calendar
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle("Select Date");
        dialog.getContentPane().add(calendar);
        dialog.pack();
        dialog.setLocationRelativeTo(dateField2);

        // Add a listener to detect date selection
        calendar.addPropertyChangeListener("calendar", evt2 -> {
            java.util.Calendar selected = calendar.getCalendar();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            dateField2.setText(sdf.format(selected.getTime()));
            dialog.dispose(); // close the dialog after selection
        });

        dialog.setVisible(true);
    }//GEN-LAST:event_dateField2MouseClicked

    private void dateField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateField1MouseClicked
        // Create a calendar dialog
        com.toedter.calendar.JCalendar calendar = new com.toedter.calendar.JCalendar();

        // Create a dialog to hold the calendar
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle("Select Date");
        dialog.getContentPane().add(calendar);
        dialog.pack();
        dialog.setLocationRelativeTo(dateField1);

        // Add a listener to detect date selection
        calendar.addPropertyChangeListener("calendar", evt2 -> {
            java.util.Calendar selected = calendar.getCalendar();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            dateField1.setText(sdf.format(selected.getTime()));
            dialog.dispose(); // close the dialog after selection
        });

        dialog.setVisible(true);
    }//GEN-LAST:event_dateField1MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
    CustomPopup.showError(this, "Please Login/SignUp to Rent Your Vehicle.", "Login");
                return;
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseEntered
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
       
        java.awt.EventQueue.invokeLater(() -> new Customer_DashBoard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Login_button;
    private javax.swing.JLabel RR_logo;
    private javax.swing.JButton SignUp_button;
    private javax.swing.JPanel book;
    private javax.swing.JPanel bottom;
    private javax.swing.JTextField dateField1;
    private javax.swing.JTextField dateField2;
    private javax.swing.JPanel dropoffdate;
    private javax.swing.JTextField dropofflocation;
    private javax.swing.JLabel homee;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pickupdate;
    private javax.swing.JPanel pickuploc;
    private javax.swing.JTextField pickuplocation;
    private javax.swing.JPanel scrollablepanel;
    private javax.swing.JPanel whitepanel;
    // End of variables declaration//GEN-END:variables
}
