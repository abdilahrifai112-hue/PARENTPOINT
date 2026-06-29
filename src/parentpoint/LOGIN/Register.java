package parentpoint.LOGIN;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import parentpoint.koneksi.koneksi;
import parentpoint.util.DesignUtil;

public class Register extends JFrame {

    private JComboBox<String> cbRole;
    private JTextField tfUsername;
    private JPasswordField tfPassword;
    
    // Guru fields
    private JLabel lblNamaGuru;
    private JTextField tfNamaGuru;
    private JLabel lblKodeGuru;
    private JPasswordField tfKodeGuru;
    
    // Ortu fields
    private JLabel lblNamaOrtu;
    private JTextField tfNamaOrtu;
    private JLabel lblNamaAnak;
    private JTextField tfNamaAnak;
    
    private JButton btnRegister;
    private JButton btnKembali;
    
    private JPanel jpFields;

    public Register() {
        setTitle("PARENT POINT - Pendaftaran Akun");
        setMinimumSize(new Dimension(550, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initUI();
        pack(); // Auto adjust to fit content
    }
    
    private void initUI() {
        JPanel jpMain = new JPanel(new BorderLayout());
        jpMain.setBackground(Color.WHITE);
        
        // Header
        JPanel jpHeader = new JPanel(new BorderLayout());
        jpHeader.setBackground(DesignUtil.PRIMARY);
        jpHeader.setPreferredSize(new Dimension(500, 80));
        
        JLabel lblTitle = new JLabel("Pendaftaran Akun", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        jpHeader.add(lblTitle, BorderLayout.CENTER);
        
        jpMain.add(jpHeader, BorderLayout.NORTH);
        
        // Form Content
        JPanel jpForm = new JPanel(new GridBagLayout());
        jpForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Role
        gbc.gridy = 0;
        JLabel lblRole = new JLabel("Mendaftar Sebagai:");
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jpForm.add(lblRole, gbc);
        
        gbc.gridy++;
        cbRole = new JComboBox<>(new String[]{"Guru", "Orang Tua"});
        cbRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbRole.addActionListener(e -> updateFields());
        jpForm.add(cbRole, gbc);
        
        // Username
        gbc.gridy++;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jpForm.add(lblUser, gbc);
        
        gbc.gridy++;
        tfUsername = new JTextField();
        tfUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jpForm.add(tfUsername, gbc);
        
        // Password
        gbc.gridy++;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jpForm.add(lblPass, gbc);
        
        gbc.gridy++;
        tfPassword = new JPasswordField();
        tfPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jpForm.add(tfPassword, gbc);
        
        // Dynamic Fields Panel
        gbc.gridy++;
        jpFields = new JPanel(new GridBagLayout());
        jpFields.setBackground(Color.WHITE);
        jpForm.add(jpFields, gbc);
        
        // Initialize dynamic fields
        lblNamaGuru = new JLabel("Nama Lengkap Anda:");
        lblNamaGuru.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tfNamaGuru = new JTextField();
        tfNamaGuru.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        lblKodeGuru = new JLabel("Kode Registrasi Guru:");
        lblKodeGuru.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tfKodeGuru = new JPasswordField();
        tfKodeGuru.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        lblNamaOrtu = new JLabel("Nama Lengkap Anda (Sesuai Data Sekolah):");
        lblNamaOrtu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tfNamaOrtu = new JTextField();
        tfNamaOrtu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        lblNamaAnak = new JLabel("Nama Lengkap Anak (Sesuai Data Sekolah):");
        lblNamaAnak.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tfNamaAnak = new JTextField();
        tfNamaAnak.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        updateFields(); // Show initial fields (Guru)
        
        // Buttons
        gbc.gridy++;
        gbc.insets = new Insets(20, 20, 10, 20);
        btnRegister = new JButton("Daftar Sekarang");
        btnRegister.setBackground(DesignUtil.ACCENT);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setFocusPainted(false);
        btnRegister.addActionListener(e -> prosesDaftar());
        jpForm.add(btnRegister, gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(0, 20, 20, 20);
        btnKembali = new JButton("Kembali ke Login");
        btnKembali.setBackground(Color.LIGHT_GRAY);
        btnKembali.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnKembali.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKembali.setFocusPainted(false);
        btnKembali.addActionListener(e -> {
            new LOGIN().setVisible(true);
            dispose();
        });
        jpForm.add(btnKembali, gbc);
        
        // Wrap form in a scroll pane to make it flexible and prevent cutoff
        javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(jpForm);
        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        jpMain.add(scrollPane, BorderLayout.CENTER);
        add(jpMain);
    }
    
    private void updateFields() {
        jpFields.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        if (cbRole.getSelectedIndex() == 0) {
            // Guru
            gbc.gridy = 0; jpFields.add(lblNamaGuru, gbc);
            gbc.gridy++; jpFields.add(tfNamaGuru, gbc);
            gbc.gridy++; jpFields.add(lblKodeGuru, gbc);
            gbc.gridy++; jpFields.add(tfKodeGuru, gbc);
        } else {
            // Ortu
            gbc.gridy = 0; jpFields.add(lblNamaOrtu, gbc);
            gbc.gridy++; jpFields.add(tfNamaOrtu, gbc);
            gbc.gridy++; jpFields.add(lblNamaAnak, gbc);
            gbc.gridy++; jpFields.add(tfNamaAnak, gbc);
        }
        
        jpFields.revalidate();
        jpFields.repaint();
    }
    
    private void prosesDaftar() {
        String username = tfUsername.getText().trim();
        String password = new String(tfPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password wajib diisi!");
            return;
        }
        
        Connection conn = koneksi.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Gagal koneksi ke database!");
            return;
        }
        
        try {
            // Cek username duplikat
            PreparedStatement psCek = conn.prepareStatement("SELECT id FROM users WHERE username = ?");
            psCek.setString(1, username);
            ResultSet rsCek = psCek.executeQuery();
            if (rsCek.next()) {
                JOptionPane.showMessageDialog(this, "Username sudah digunakan, silakan pilih yang lain.");
                rsCek.close(); psCek.close(); conn.close();
                return;
            }
            rsCek.close(); psCek.close();
            
            if (cbRole.getSelectedIndex() == 0) {
                // Proses Guru
                String namaGuru = tfNamaGuru.getText().trim();
                String kodeGuru = new String(tfKodeGuru.getPassword());
                
                if (namaGuru.isEmpty() || kodeGuru.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nama Lengkap dan Kode Registrasi Guru wajib diisi!");
                    conn.close(); return;
                }
                
                if (!"GURU2026".equals(kodeGuru)) {
                    JOptionPane.showMessageDialog(this, "Kode Registrasi Guru tidak valid!");
                    conn.close(); return;
                }
                
                // Insert Guru
                PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, role, guru_nama) VALUES (?, ?, 'guru', ?)");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, namaGuru);
                ps.executeUpdate();
                ps.close();
                
                JOptionPane.showMessageDialog(this, "Registrasi Guru Berhasil! Silakan Login.");
                
            } else {
                // Proses Ortu
                String namaOrtu = tfNamaOrtu.getText().trim();
                String namaAnak = tfNamaAnak.getText().trim();
                
                if (namaOrtu.isEmpty() || namaAnak.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nama Lengkap Anda dan Nama Anak wajib diisi!");
                    conn.close(); return;
                }
                
                // Validasi data ortu dan anak
                PreparedStatement psVal = conn.prepareStatement(
                    "SELECT o.id, s.id AS siswa_id FROM orang_tua o " +
                    "JOIN siswa s ON o.siswa_id = s.id " +
                    "WHERE o.nama_ortu = ? AND s.nama = ?"
                );
                psVal.setString(1, namaOrtu);
                psVal.setString(2, namaAnak);
                ResultSet rsVal = psVal.executeQuery();
                
                if (!rsVal.next()) {
                    JOptionPane.showMessageDialog(this, "Data tidak ditemukan! Pastikan Nama Anda dan Nama Anak diketik sesuai data di sekolah.");
                    rsVal.close(); psVal.close(); conn.close();
                    return;
                }
                int siswaId = rsVal.getInt("siswa_id");
                rsVal.close(); psVal.close();
                
                // Insert Ortu
                PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username, password, role, siswa_id) VALUES (?, ?, 'orang_tua', ?)");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setInt(3, siswaId);
                ps.executeUpdate();
                ps.close();
                
                JOptionPane.showMessageDialog(this, "Registrasi Orang Tua Berhasil! Silakan Login.");
            }
            
            conn.close();
            new LOGIN().setVisible(true);
            dispose();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
        }
    }
}
