package parentpoint.LOGIN;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import parentpoint.koneksi.koneksi;
import parentpoint.ds.dsmainframe;
import parentpoint.util.DesignUtil;

public class LOGIN extends JFrame {

    private java.awt.Image logoRawImage; // simpan image asli untuk rescaling

    public LOGIN() {
        initComponents();
        styleComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelLeft = new javax.swing.JPanel();
        lblIcon = new javax.swing.JLabel();
        lblBrand = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        jPanelRight = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        lblLogin = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        lblPass = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PARENT POINT - Login");
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridLayout(1, 2));

        jPanelLeft.setBackground(new java.awt.Color(25, 55, 109));
        jPanelLeft.setLayout(new java.awt.GridBagLayout());

        lblIcon.setFont(new java.awt.Font("Segoe UI", 0, 72)); // NOI18N
        lblIcon.setForeground(new java.awt.Color(255, 255, 255));
        lblIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIcon.setText("🎓");
        jPanelLeft.add(lblIcon, new java.awt.GridBagConstraints());

        lblBrand.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        lblBrand.setForeground(new java.awt.Color(255, 255, 255));
        lblBrand.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBrand.setText("PARENT POINT");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanelLeft.add(lblBrand, gridBagConstraints);

        lblSub.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblSub.setForeground(new java.awt.Color(189, 215, 238));
        lblSub.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSub.setText("Aplikasi Monitoring Siswa");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanelLeft.add(lblSub, gridBagConstraints);

        getContentPane().add(jPanelLeft);

        jPanelRight.setBackground(new java.awt.Color(255, 255, 255));
        jPanelRight.setLayout(new java.awt.GridBagLayout());

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(25, 55, 109));
        lblWelcome.setText("Selamat Datang");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 5, 40);
        jPanelRight.add(lblWelcome, gridBagConstraints);

        lblLogin.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblLogin.setForeground(new java.awt.Color(127, 140, 141));
        lblLogin.setText("Silakan login untuk melanjutkan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 30, 40);
        jPanelRight.add(lblLogin, gridBagConstraints);

        lblUser.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblUser.setForeground(new java.awt.Color(127, 140, 141));
        lblUser.setText("USERNAME");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 3, 40);
        jPanelRight.add(lblUser, gridBagConstraints);

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 15, 40);
        jPanelRight.add(jTextField1, gridBagConstraints);

        lblPass.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblPass.setForeground(new java.awt.Color(127, 140, 141));
        lblPass.setText("PASSWORD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 3, 40);
        jPanelRight.add(lblPass, gridBagConstraints);

        jPasswordField1.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 5, 40);
        jPanelRight.add(jPasswordField1, gridBagConstraints);

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(127, 140, 141));
        jCheckBox1.setText("Tampilkan Password");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 20, 40);
        jPanelRight.add(jCheckBox1, gridBagConstraints);

        jButton1.setContentAreaFilled(false);
        jButton1.setOpaque(true);
        jButton1.setBackground(new java.awt.Color(25, 55, 109));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("MASUK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 40);
        jPanelRight.add(jButton1, gridBagConstraints);

        getContentPane().add(jPanelRight);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (jCheckBox1.isSelected()) {
            jPasswordField1.setEchoChar((char) 0);
        } else {
            jPasswordField1.setEchoChar('●');
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String username = jTextField1.getText().trim();
        String password = new String(jPasswordField1.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username dan Password harus diisi!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                String sql = "SELECT u.*, s.nama AS nama_siswa, k.id AS kelas_id, k.nama_kelas, o.nama_ortu " +
                             "FROM users u " +
                             "LEFT JOIN siswa s ON u.siswa_id = s.id " +
                             "LEFT JOIN kelas k ON s.kelas_id = k.id " +
                             "LEFT JOIN orang_tua o ON s.id = o.siswa_id " +
                             "WHERE u.username = ? AND u.password = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    String role = rs.getString("role");
                    int siswaId = rs.getInt("siswa_id"); // 0 if null in DB
                    String guruNama = rs.getString("guru_nama");
                    String namaSiswa = rs.getString("nama_siswa");
                    String namaOrtu = rs.getString("nama_ortu");
                    int kelasId = rs.getInt("kelas_id");
                    String namaKelas = rs.getString("nama_kelas");
                    
                    // Set Global Session
                    parentpoint.util.Session.setSession(username, role, siswaId, namaSiswa, namaOrtu, kelasId, namaKelas, guruNama);

                    JOptionPane.showMessageDialog(this, 
                        "Login Berhasil!\nSelamat datang, " + username + " (" + role + ")", 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    
                    dsmainframe mainFrame = new dsmainframe();
                    mainFrame.setVisible(true);
                    mainFrame.setLocationRelativeTo(null);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Username atau Password salah!", 
                        "Login Gagal", JOptionPane.ERROR_MESSAGE);
                    jPasswordField1.setText("");
                }
                
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error: " + e.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void styleComponents() {
        // Apply BasicButtonUI to bypass Windows default theme white rendering on buttons
        jButton1.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        jButton1.setBackground(new Color(25, 55, 109));
        jButton1.setForeground(Color.WHITE);
        jButton1.setFocusPainted(false);
        jButton1.setBorderPainted(false);
        jButton1.setOpaque(true);
        jButton1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                jButton1.setBackground(new Color(41, 82, 148));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                jButton1.setBackground(new Color(25, 55, 109));
            }
        });
        
        // --- Add Register Link Dynamically ---
        javax.swing.JLabel lblRegister = new javax.swing.JLabel("<html>Belum punya akun? <font color='#3498db'><b>Daftar di sini</b></font></html>");
        lblRegister.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        lblRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblRegister.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        lblRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new parentpoint.LOGIN.Register().setVisible(true);
                dispose(); // Tutup form login
            }
        });
        
        java.awt.GridBagConstraints gc = new java.awt.GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 8;
        gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gc.insets = new java.awt.Insets(15, 40, 0, 40);
        jPanelRight.add(lblRegister, gc);
        jPanelRight.revalidate();
        jPanelRight.repaint();
        // -------------------------------------
        
        // Premium borders for textfields
        jTextField1.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 224, 230), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        jPasswordField1.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 224, 230), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // ── Pasang Logo Sekolah – Responsif mengikuti ukuran panel ──
        try {
            java.net.URL logoUrl = getClass().getResource("/parentpoint/images/logo.jpg");
            if (logoUrl != null) {
                logoRawImage = new ImageIcon(logoUrl).getImage();
                lblIcon.setText(""); // hapus emoji

                // Fungsi untuk rescale logo
                Runnable resizeLogo = () -> {
                    int pw = jPanelLeft.getWidth();
                    int ph = jPanelLeft.getHeight();
                    if (pw > 0 && ph > 0) {
                        int size = (int)(Math.min(pw, ph) * 0.45);
                        size = Math.max(size, 80); // minimal 80px
                        java.awt.Image scaled = logoRawImage.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
                        lblIcon.setIcon(new ImageIcon(scaled));
                    }
                };

                // Resize saat panel berubah ukuran
                jPanelLeft.addComponentListener(new java.awt.event.ComponentAdapter() {
                    @Override
                    public void componentResized(java.awt.event.ComponentEvent e) {
                        resizeLogo.run();
                    }
                });

                // Resize pertama kali setelah layout selesai
                javax.swing.SwingUtilities.invokeLater(resizeLogo);
            }
        } catch (Exception ex) {
            // Tetap pakai emoji kalau gagal load
        }

        // Teks brand dan nama sekolah
        lblBrand.setText("<html><div style='text-align:center'>PARENT POINT</div></html>");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 26));

        lblSub.setText("<html><div style='text-align:center; color:#BDD7EE'>SMP Negeri 175<br>Jakarta Selatan</div></html>");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Set ukuran default
        setSize(950, 580);
        setLocationRelativeTo(null);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {}
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LOGIN().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JPanel jPanelLeft;
    private javax.swing.JPanel jPanelRight;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblPass;
    private javax.swing.JLabel lblSub;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblWelcome;
    // End of variables declaration//GEN-END:variables
}
