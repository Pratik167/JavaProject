package Rent_Rover.DataBases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {
    private static String serverName = "localhost";
    private static String userName = "root";
    private static String dbName = "rentrover_admin_db";
    private static int portNumber = 3306;
    private static String pass = "";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName,
                userName,
                pass
            );
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }
}
