package Rent_Rover;

import Rent_Rover.DataBases.DB;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Admin_Dash extends JFrame {

    private JPanel mainPanel;
    private JLabel dashboardLabel;
    private JLabel customersLabel;
    private JLabel carsLabel;
    private JLabel bookingsLabel;
    private JLabel RR_logo, RentRover, RRR, pfp, adminName, adminRole;
    private JPanel bottomPanel, topPanel, profilePanel, logosPanel;
    private JScrollPane scrollPane;

    public Admin_Dash() {
        initComponents();
        loadDashboard();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top gradient panel
        topPanel = new GradientPanel(new Color(0, 128, 0), new Color(144, 238, 144), GradientPanel.Direction.HORIZONTAL);
        topPanel.setPreferredSize(new Dimension(0, 30));
        add(topPanel, BorderLayout.NORTH);

        // Bottom navigation panel
        bottomPanel = new JPanel();
bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setPreferredSize(new Dimension(200, 0));
        bottomPanel.setLayout(new GridLayout(4, 1, 0, 20));

        dashboardLabel = createNavLabel("DASHBOARD");
        customersLabel = createNavLabel("CUSTOMERS");
        carsLabel = createNavLabel("CARS");
        bookingsLabel = createNavLabel("BOOKINGS");

        bottomPanel.add(dashboardLabel);
        bottomPanel.add(customersLabel);
        bottomPanel.add(carsLabel);
        bottomPanel.add(bookingsLabel);

        add(bottomPanel, BorderLayout.WEST);

// -------- ADMIN PANEL --------
JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
adminPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
adminPanel.setBackground(Color.WHITE);
adminPanel.setOpaque(true);
adminPanel.setPreferredSize(new Dimension(180, 100)); // match RR panel height

JLabel pfp = new JLabel();
ImageIcon profileImg = new ImageIcon("C:\\Users\\PC MOD NEPAL\\OneDrive\\Desktop\\ProjectImages\\gengar.png");
Image imgProfile = profileImg.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
pfp.setIcon(new ImageIcon(imgProfile));

// Text panel for admin name and role
JPanel adminTextPanel = new JPanel();
adminTextPanel.setLayout(new BoxLayout(adminTextPanel, BoxLayout.Y_AXIS));
adminTextPanel.setOpaque(false);

JLabel adminName = new JLabel("John Cena");
adminName.setFont(new Font("Perpetua", Font.BOLD, 16));
adminName.setAlignmentX(Component.LEFT_ALIGNMENT);

JLabel adminRole = new JLabel("ADMIN");
adminRole.setFont(new Font("Perpetua", Font.BOLD, 14));
adminRole.setAlignmentX(Component.LEFT_ALIGNMENT);

adminTextPanel.add(adminName);
adminTextPanel.add(Box.createVerticalStrut(3));
adminTextPanel.add(adminRole);

// Add logo and text panel to admin panel
adminPanel.add(pfp);
adminPanel.add(adminTextPanel);

// -------- RR PANEL (for reference, unchanged) --------
JPanel rrPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
rrPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
rrPanel.setBackground(Color.WHITE);
rrPanel.setOpaque(true);
rrPanel.setPreferredSize(new Dimension(250, 100));

JLabel RR_logo = new JLabel();
ImageIcon logoImg = new ImageIcon("C:\\Users\\PC MOD NEPAL\\OneDrive\\Desktop\\ProjectImages\\RR logo.png");
Image imgLogo = logoImg.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
RR_logo.setIcon(new ImageIcon(imgLogo));

JPanel rrTextPanel = new JPanel();
rrTextPanel.setLayout(new BoxLayout(rrTextPanel, BoxLayout.Y_AXIS));
rrTextPanel.setOpaque(false);

JLabel RentRover = new JLabel("RentRover");
RentRover.setFont(new Font("Perpetua", Font.BOLD, 20));
RentRover.setAlignmentX(Component.LEFT_ALIGNMENT);

JLabel RRR = new JLabel("Rent, Ride, Repeat");
RRR.setFont(new Font("SansSerif", Font.ITALIC, 14));
RRR.setAlignmentX(Component.LEFT_ALIGNMENT);

rrTextPanel.add(RentRover);
rrTextPanel.add(Box.createVerticalStrut(3));
rrTextPanel.add(RRR);

rrPanel.add(RR_logo);
rrPanel.add(rrTextPanel);

// -------- TOP CONTAINER --------
JPanel topContainer = new JPanel(new BorderLayout());
topContainer.setOpaque(false);
topContainer.add(adminPanel, BorderLayout.WEST);
topContainer.add(rrPanel, BorderLayout.EAST);

// -------- ADD TO FRAME --------
JPanel topPanel = new JPanel(new BorderLayout());
topPanel.setOpaque(true);
topPanel.setBackground(Color.WHITE);
topPanel.setPreferredSize(new Dimension(0, 110)); 
topPanel.add(topContainer, BorderLayout.CENTER);

add(topPanel, BorderLayout.NORTH);








        // Main scrollable panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        scrollPane = new JScrollPane(mainPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Navigation listeners
        dashboardLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { loadDashboard(); }
        });
        customersLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { loadCustomers(); }
        });
        carsLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { loadCars(); }
        });
        bookingsLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { loadBookings(); }
        });
    }
private void setLabelImage(JLabel label, String path, int width, int height) {
    try {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(img));
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private JLabel createNavLabel(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 24));
        lbl.setBorder(new LineBorder(Color.BLACK));
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return lbl;
    }

    // ---------- LOAD DASHBOARD ----------
    private void loadDashboard() {
        mainPanel.removeAll();
        try (Connection con = DB.getConnection()) {
            Statement stmt = con.createStatement();

            int customers = 0, cars = 0, bookings = 0;

            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) FROM customer");
            if (rs1.next()) customers = rs1.getInt(1);

            ResultSet rs2 = stmt.executeQuery("SELECT COUNT(DISTINCT brand, model, username) FROM bookings");
            if (rs2.next()) cars = rs2.getInt(1);

            ResultSet rs3 = stmt.executeQuery("SELECT COUNT(*) FROM bookings");
            if (rs3.next()) bookings = rs3.getInt(1);

            mainPanel.setLayout(new GridLayout(1, 3, 50, 50));

            mainPanel.add(createDashboardCard("Customers", customers, "C:\\Users\\PC MOD NEPAL\\OneDrive\\Desktop\\ProjectImages\\customer.png"));
mainPanel.add(createDashboardCard("Cars", cars, "C:\\Users\\PC MOD NEPAL\\OneDrive\\Desktop\\ProjectImages\\car.png"));
mainPanel.add(createDashboardCard("Bookings", bookings, "C:\\Users\\PC MOD NEPAL\\OneDrive\\Desktop\\ProjectImages\\booking.png"));



        } catch (SQLException e) {
            e.printStackTrace();
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createDashboardCard(String title, int count, String iconPath) {
    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(250, 150));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    // Icon
    JLabel lblIcon = new JLabel();
    lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
    try {
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // bigger icon
        lblIcon.setIcon(new ImageIcon(img));
    } catch (Exception e) {
        e.printStackTrace();
    }

    // Count
    JLabel lblCount = new JLabel(String.valueOf(count));
    lblCount.setFont(new Font("SansSerif", Font.BOLD, 36));
    lblCount.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblCount.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    // Title
    JLabel lblTitle = new JLabel(title);
    lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
    lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    lblTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    panel.add(Box.createVerticalGlue()); // pushes content to center nicely
    panel.add(lblIcon);
    panel.add(lblCount);
    panel.add(lblTitle);
    panel.add(Box.createVerticalGlue());

    return panel;
}



    // ---------- LOAD CUSTOMERS ----------
    private void loadCustomers() {
        mainPanel.removeAll();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        try (Connection con = DB.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                int balance = rs.getInt("balance");
                Blob imgBlob = rs.getBlob("profile_picture");

                ImageIcon img = null;
                if (imgBlob != null) {
                    img = new ImageIcon(imgBlob.getBytes(1, (int) imgBlob.length()));
                    Image image = img.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    img = new ImageIcon(image);
                }

                mainPanel.add(createCustomerCard(id, username, email, balance, img));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createCustomerCard(int id, String username, String email, int balance, ImageIcon img) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200, 250));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.setLayout(new BorderLayout());

        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        if (img != null) lblImg.setIcon(img);

        JLabel lblUser = new JLabel(username, SwingConstants.CENTER);
        JLabel lblEmail = new JLabel(email, SwingConstants.CENTER);
        JLabel lblBalance = new JLabel("Balance: " + balance, SwingConstants.CENTER);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this, "Delete user and all their bookings?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) deleteCustomer(id);
        });

        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(lblUser);
        infoPanel.add(lblEmail);
        infoPanel.add(lblBalance);
        infoPanel.add(deleteBtn);

        panel.add(lblImg, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void deleteCustomer(int id) {
        try (Connection con = DB.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM bookings WHERE username=(SELECT username FROM customer WHERE id=?)");
            ps.setInt(1, id);
            ps.executeUpdate();

            ps = con.prepareStatement("DELETE FROM customer WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();

            loadCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------- LOAD CARS ----------
    private void loadCars() {
        mainPanel.removeAll();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        try (Connection con = DB.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT brand, model, price_per_day, vehicle_image, username FROM bookings");
            while (rs.next()) {
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                int price = rs.getInt("price_per_day");
                String username = rs.getString("username");
                Blob imgBlob = rs.getBlob("vehicle_image");

                ImageIcon img = null;
                if (imgBlob != null) {
                    img = new ImageIcon(imgBlob.getBytes(1, (int) imgBlob.length()));
                    Image image = img.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                    img = new ImageIcon(image);
                }

                mainPanel.add(createCarCard(brand, model, price, username, img));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createCarCard(String brand, String model, int price, String username, ImageIcon img) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200, 200));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.setLayout(new BorderLayout());

        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        if (img != null) lblImg.setIcon(img);

        JLabel lblInfo = new JLabel("<html>" + brand + " " + model + "<br/>Price: " + price + "</html>", SwingConstants.CENTER);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this, "Delete this car booking?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) deleteCar(username, brand, model);
        });

        panel.add(lblImg, BorderLayout.CENTER);
        panel.add(lblInfo, BorderLayout.NORTH);
        panel.add(deleteBtn, BorderLayout.SOUTH);

        return panel;
    }

    private void deleteCar(String username, String brand, String model) {
        try (Connection con = DB.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM bookings WHERE username=? AND brand=? AND model=? LIMIT 1");
            ps.setString(1, username);
            ps.setString(2, brand);
            ps.setString(3, model);
            ps.executeUpdate();

            loadCars();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------- LOAD BOOKINGS ----------
    private void loadBookings() {
        mainPanel.removeAll();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        try (Connection con = DB.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT booking_id, username, brand, model, booked_by, vehicle_image FROM bookings");
            while (rs.next()) {
                int id = rs.getInt("booking_id");
                String username = rs.getString("username");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String bookedBy = rs.getString("booked_by");
                Blob imgBlob = rs.getBlob("vehicle_image");

                ImageIcon img = null;
                if (imgBlob != null) {
                    img = new ImageIcon(imgBlob.getBytes(1, (int) imgBlob.length()));
                    Image image = img.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                    img = new ImageIcon(image);
                }

                mainPanel.add(createBookingCard(id, username, brand, model, bookedBy, img));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createBookingCard(int id, String username, String brand, String model, String bookedBy, ImageIcon img) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(220, 220));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        panel.setLayout(new BorderLayout());

        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        if (img != null) lblImg.setIcon(img);

        JLabel lblInfo = new JLabel("<html>User: " + username + "<br/>Car: " + brand + " " + model + "<br/>Booked By: " + bookedBy + "</html>", SwingConstants.CENTER);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this, "Delete this booking?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) deleteBooking(id);
        });

        panel.add(lblImg, BorderLayout.CENTER);
        panel.add(lblInfo, BorderLayout.NORTH);
        panel.add(deleteBtn, BorderLayout.SOUTH);

        return panel;
    }

    private void deleteBooking(int id) {
        try (Connection con = DB.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM bookings WHERE booking_id=?");
            ps.setInt(1, id);
            ps.executeUpdate();

            loadBookings();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Admin_DashBoard().setVisible(true));
    }
}
