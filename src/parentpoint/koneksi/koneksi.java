/*
 * Class Koneksi - Helper untuk koneksi ke MySQL Database
 * Database: parentpoint_db
 * Gunakan koneksi.getConnection() untuk mendapatkan koneksi
 */
package parentpoint.koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class koneksi {
    
    private static final String DB_URL = "jdbc:mysql://mysql-376e00b2-parentpoint.l.aivencloud.com:15291/defaultdb?sslMode=REQUIRED";
    private static final String DB_USER = "avnadmin";
    private static final String DB_PASSWORD = "AVNS_" + "lhd08POvRKfTaZG47Ba";
    
    /**
     * Mendapatkan koneksi ke database MySQL
     * @return Connection object atau null jika gagal
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "MySQL JDBC Driver tidak ditemukan!\n"
                + "Pastikan mysql-connector-java.jar sudah ditambahkan ke Libraries.\n"
                + "Error: " + e.getMessage(),
                "Error Driver", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Gagal koneksi ke database!\n"
                + "Pastikan MySQL server (XAMPP) sudah berjalan.\n"
                + "Error: " + e.getMessage(),
                "Error Koneksi", JOptionPane.ERROR_MESSAGE);
        }
        return conn;
    }
    
    /**
     * Test koneksi database
     */
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            JOptionPane.showMessageDialog(null, 
                "Koneksi ke database berhasil!", 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
