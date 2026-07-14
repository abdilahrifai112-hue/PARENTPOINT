import parentpoint.koneksi.koneksi;
import java.sql.Connection;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImportSQL2 {
    public static void main(String[] args) {
        try {
            System.out.println("Reading SQL file...");
            String content = new String(Files.readAllBytes(Paths.get("parentpoint_db.sql")));
            
            // Remove CREATE DATABASE and USE lines
            content = content.replaceAll("(?i)CREATE DATABASE.*", "");
            content = content.replaceAll("(?i)USE .*", "");
            
            String[] queries = content.split(";");
            
            System.out.println("Connecting to Aiven Database...");
            Connection conn = koneksi.getConnection();
            if (conn != null) {
                Statement stmt = conn.createStatement();
                stmt.execute("SET FOREIGN_KEY_CHECKS=0");
                
                // Drop existing tables just in case they are corrupted
                try { stmt.execute("DROP TABLE IF EXISTS users, siswa, guru, jadwal_kelas, kelas, kehadiran_harian, orang_tua, rekap_bulanan, sesi_kelas CASCADE"); } catch(Exception ignore) {}
                
                for (String q : queries) {
                    if (q.trim().length() > 0) {
                        try {
                            stmt.execute(q.trim());
                            System.out.println("Executed successfully.");
                        } catch (Exception e) {
                            System.out.println("Failed query: " + e.getMessage() + "\nQuery was: " + q.trim().substring(0, Math.min(q.trim().length(), 50)));
                        }
                    }
                }
                stmt.execute("SET FOREIGN_KEY_CHECKS=1");
                stmt.close();
                conn.close();
                System.out.println("Database import finished successfully!");
            } else {
                System.out.println("Failed to connect!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
