package parentpoint.ds;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import parentpoint.koneksi.koneksi;
import parentpoint.LOGIN.LOGIN;
import parentpoint.report.report;
import parentpoint.util.DesignUtil;

public class dsmainframe extends javax.swing.JFrame {

    public dsmainframe() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        styleComponents();
        loadDashboardData();
    }

    private void styleComponents() {
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setTitle("PARENT POINT - Menu Utama");
        getContentPane().setBackground(DesignUtil.BG_MAIN);
        
        // Sidebar styling
        jPanelSidebar.setBackground(DesignUtil.BG_SIDEBAR);
        jPanelSidebarMenu.setBackground(DesignUtil.BG_SIDEBAR);
        jPanelSidebarUserInfo.setBackground(DesignUtil.PRIMARY_LIGHT);
        jPanelTextWrapper.setBackground(DesignUtil.PRIMARY_LIGHT);

        // ── Logo Sekolah di atas sidebar – Responsif ──
        JPanel pnlLogo = new JPanel();
        pnlLogo.setLayout(new BoxLayout(pnlLogo, BoxLayout.Y_AXIS));
        pnlLogo.setBackground(DesignUtil.BG_SIDEBAR);
        pnlLogo.setBorder(BorderFactory.createEmptyBorder(14, 8, 10, 8));

        JLabel lblLogo = new JLabel();
        lblLogo.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        try {
            java.net.URL logoUrl = getClass().getResource("/parentpoint/images/logo.jpg");
            if (logoUrl != null) {
                final java.awt.Image rawLogoImg = new ImageIcon(logoUrl).getImage();

                // Fungsi resize logo sesuai lebar sidebar
                Runnable resizeSidebarLogo = () -> {
                    int w = pnlLogo.getWidth();
                    if (w > 0) {
                        int size = Math.max(50, w - 30); // padding 15px kiri-kanan
                        java.awt.Image scaled = rawLogoImg.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
                        lblLogo.setIcon(new ImageIcon(scaled));
                        pnlLogo.revalidate();
                        pnlLogo.repaint();
                    }
                };

                // Dengarkan perubahan ukuran sidebar
                pnlLogo.addComponentListener(new java.awt.event.ComponentAdapter() {
                    @Override
                    public void componentResized(java.awt.event.ComponentEvent e) {
                        resizeSidebarLogo.run();
                    }
                });

                // Load setelah layout selesai
                javax.swing.SwingUtilities.invokeLater(resizeSidebarLogo);
            }
        } catch (Exception ex) { /* logo gagal load */ }

        JLabel lblSekolah = new JLabel("SMP NEGERI 175");
        lblSekolah.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblSekolah.setForeground(new Color(147, 197, 253));
        lblSekolah.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        JLabel lblKota = new JLabel("Jakarta Selatan");
        lblKota.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblKota.setForeground(new Color(148, 163, 184));
        lblKota.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

        pnlLogo.add(lblLogo);
        pnlLogo.add(Box.createVerticalStrut(6));
        pnlLogo.add(lblSekolah);
        pnlLogo.add(Box.createVerticalStrut(2));
        pnlLogo.add(lblKota);

        jPanelSidebar.add(pnlLogo, BorderLayout.NORTH);
        
        // Labels
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setText("Administrator");
        
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRole.setForeground(DesignUtil.TEXT_LIGHT);
        lblRole.setText("Role: Admin");
        
        // Sidebar Buttons styling
        JButton[] sidebarButtons = {jButton1, jButton2, jButton3, jButton4, jButton5, jButton6, jButton7, jButton9, jButton10, jButton11, jButton12};
        // Clean, cross-platform Unicode geometric icons:
        // U+25C6 is Black Diamond (◆) for active item, U+25CB is White Circle (○) for inactive items
        String[] icons = {"\u25c6  ", "\u25cb  ", "\u25cb  ", "\u25cb  ", "\u25cb  ", "\u25cb  ", "\u25cb  ", "\u25cb  ", "\u25cb  ", "\u25cb  ", "\u25cb  "};
        String[] labels = {"Dashboard", "Data Siswa", "Data Orang Tua", "Data Kelas", "Input Kehadiran", "Rekap Bulanan", "Manajemen User", "Lap. Kehadiran", "Rekap Per Siswa", "Rekap Per Kelas", "Siswa Sering Absen"};
        
        for (int i = 0; i < sidebarButtons.length; i++) {
            JButton btn = sidebarButtons[i];
            btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            btn.setText(icons[i] + labels[i]);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setOpaque(true);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Set active indicator (Dashboard is active in dsmainframe)
            if (i == 0) {
                btn.setBackground(DesignUtil.PRIMARY); // Blue background
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(147, 197, 253)), // Light blue left indicator
                    BorderFactory.createEmptyBorder(0, 10, 0, 15)
                ));
            } else {
                btn.setBackground(DesignUtil.BG_SIDEBAR); // Gray background
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 5, 0, 0, DesignUtil.BG_SIDEBAR),
                    BorderFactory.createEmptyBorder(0, 10, 0, 15)
                ));
            }
            
            final int index = i;
            final JButton fBtn = btn;
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (index != 0) {
                        fBtn.setBackground(DesignUtil.PRIMARY_LIGHT);
                    }
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (index != 0) {
                        fBtn.setBackground(DesignUtil.BG_SIDEBAR);
                    }
                }
            });
        }
        
        // Style Keluar Button
        jButton8.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        jButton8.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jButton8.setBackground(DesignUtil.DANGER);
        jButton8.setForeground(Color.WHITE);
        jButton8.setFocusPainted(false);
        jButton8.setBorderPainted(false);
        jButton8.setOpaque(true);
        jButton8.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                jButton8.setBackground(DesignUtil.DANGER.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                jButton8.setBackground(DesignUtil.DANGER);
            }
        });
        
        // Reset and redesign header panel to BorderLayout
        jPanelHeader.removeAll();
        jPanelHeader.setLayout(new BorderLayout());
        jPanelHeader.setBackground(DesignUtil.BG_HEADER);
        jPanelHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));

        // Title on the left (West)
        lblHeaderTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHeaderTitle.setForeground(DesignUtil.TEXT_PRIMARY);
        lblHeaderTitle.setText("📊  Dashboard Utama - PARENT POINT");
        jPanelHeader.add(lblHeaderTitle, BorderLayout.WEST);

        // User info + Date & Time on the right (East)
        JPanel rightHeaderPanel = new JPanel();
        rightHeaderPanel.setLayout(new BoxLayout(rightHeaderPanel, BoxLayout.Y_AXIS));
        rightHeaderPanel.setBackground(DesignUtil.BG_HEADER);

        // Subpanel for Username, Role, and Logout button (side-by-side)
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setBackground(DesignUtil.BG_HEADER);

        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsername.setForeground(DesignUtil.TEXT_PRIMARY);
        String sessionUser = parentpoint.util.Session.getUsername();
        lblUsername.setText(sessionUser != null ? sessionUser.toUpperCase() : "Administrator");
        userPanel.add(lblUsername);

        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRole.setForeground(DesignUtil.TEXT_SECONDARY);
        String sessionRole = parentpoint.util.Session.getRole();
        lblRole.setText(sessionRole != null ? "(" + sessionRole + ")" : "(Admin)");
        userPanel.add(lblRole);

        // Style Keluar button
        jButton8.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        jButton8.setFont(new Font("Segoe UI", Font.BOLD, 11));
        jButton8.setBackground(DesignUtil.DANGER);
        jButton8.setForeground(Color.WHITE);
        jButton8.setFocusPainted(false);
        jButton8.setBorderPainted(false);
        jButton8.setOpaque(true);
        jButton8.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButton8.setPreferredSize(new Dimension(75, 25));
        userPanel.add(jButton8);

        // DateTime Label
        javax.swing.JLabel lblDateTime = new javax.swing.JLabel();
        lblDateTime.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDateTime.setForeground(DesignUtil.TEXT_SECONDARY);
        lblDateTime.setHorizontalAlignment(SwingConstants.RIGHT);
        
        rightHeaderPanel.add(userPanel);
        rightHeaderPanel.add(Box.createVerticalStrut(4));
        rightHeaderPanel.add(lblDateTime);

        jPanelHeader.add(rightHeaderPanel, BorderLayout.EAST);

        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
            String dt = new java.text.SimpleDateFormat("dd MMMM yyyy HH:mm:ss", new java.util.Locale("id")).format(new java.util.Date());
            lblDateTime.setText(dt);
        });
        timer.setRepeats(true);
        timer.start();

        // Dashboard Body
        jPanelDashboardBody.setBackground(DesignUtil.BG_MAIN);
        jPanelDashboardBody.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        lblDashboardTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDashboardTitle.setForeground(DesignUtil.TEXT_PRIMARY);
        lblDashboardTitle.setText("Ringkasan Data Aplikasi");
        
        // Style Cards
        JPanel[] cards = {jPanelCardSiswa, jPanelCardKelas, jPanelCardOrtu, jPanelCardKehadiran};
        JPanel[] cardContents = {jPanelCardSiswaContent, jPanelCardKelasContent, jPanelCardOrtuContent, jPanelCardKehadiranContent};
        JPanel[] cardBars = {jPanelCardSiswaBar, jPanelCardKelasBar, jPanelCardOrtuBar, jPanelCardKehadiranBar};
        Color[] accentColors = {DesignUtil.ACCENT, DesignUtil.SUCCESS, DesignUtil.INFO, DesignUtil.WARNING};
        JLabel[] cardTitles = {lblCardSiswaTitle, lblCardKelasTitle, lblCardOrtuTitle, lblCardKehadiranTitle};
        JLabel[] cardValues = {lblCardSiswaValue, lblCardKelasValue, lblCardOrtuValue, lblCardKehadiranValue};
        String[] titlesText = {"TOTAL SISWA", "TOTAL KELAS", "TOTAL ORANG TUA", "KEHADIRAN HARI INI"};
        
        for (int i = 0; i < cards.length; i++) {
            cards[i].setBackground(Color.WHITE);
            cards[i].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
            ));
            cardContents[i].setBackground(Color.WHITE);
            cardContents[i].setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            cardBars[i].setBackground(accentColors[i]);
            cardBars[i].setPreferredSize(new Dimension(0, 4));
            
            cardTitles[i].setFont(DesignUtil.FONT_CARD_LABEL);
            cardTitles[i].setForeground(DesignUtil.TEXT_SECONDARY);
            cardTitles[i].setText(titlesText[i]);
            
            if (i == 3) {
                cardValues[i].setFont(new Font("Segoe UI", Font.BOLD, 13));
            } else {
                cardValues[i].setFont(DesignUtil.FONT_CARD_NUMBER);
            }
            cardValues[i].setForeground(accentColors[i]);
        }

        // Apply Role-Based UI Adjustments
        applyRoleBasedAccess();
        
        // System Info Styling
        lblSystemInfo.setBackground(Color.WHITE);
        lblSystemInfo.setOpaque(true);
        lblSystemInfo.setFont(DesignUtil.FONT_BODY);
        lblSystemInfo.setForeground(DesignUtil.TEXT_PRIMARY);
        lblSystemInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Bind Actions (exactly once)
        jButton1.addActionListener(e -> loadDashboardData());
        jButton2.addActionListener(e -> new parentpoint.master.MasterSiswa().setVisible(true));
        jButton3.addActionListener(e -> new parentpoint.master.MasterOrangTua().setVisible(true));
        jButton4.addActionListener(e -> new parentpoint.master.MasterKelas().setVisible(true));
        jButton5.addActionListener(e -> new parentpoint.transaksi.InputKehadiran().setVisible(true));
        jButton6.addActionListener(e -> new parentpoint.transaksi.RekapBulanan().setVisible(true));
        jButton7.addActionListener(e -> new parentpoint.master.MasterUser().setVisible(true));
        jButton8.addActionListener(evt -> jButton8ActionPerformed(evt));
        jButton9.addActionListener(e -> new parentpoint.report.report().setVisible(true));
        jButton10.addActionListener(e -> new parentpoint.report.ReportRekapSiswa().setVisible(true));
        jButton11.addActionListener(e -> new parentpoint.report.ReportRekapKelas().setVisible(true));
        jButton12.addActionListener(e -> new parentpoint.report.ReportAlpha().setVisible(true));
// setSize removed for responsiveness
        setLocationRelativeTo(null);
    }

    private void applyRoleBasedAccess() {
        String role = parentpoint.util.Session.getRole();
        if (role != null && !role.equalsIgnoreCase("admin")) {
            
            if (role.equalsIgnoreCase("orang_tua") || role.equalsIgnoreCase("siswa")) {
                // Sembunyikan Menu Master & Transaksi yang bukan hak aksesnya
                jButton2.setVisible(false); // Master Siswa
                jButton3.setVisible(false); // Master Ortu
                jButton4.setVisible(false); // Master Kelas
                jButton5.setVisible(false); // Input Kehadiran
                jButton6.setVisible(false); // Rekap Bulanan
                jButton7.setVisible(false); // Master User
                
                // Laporan yang spesifik admin juga disembunyikan
                jButton9.setVisible(false); // Laporan Umum
                jButton11.setVisible(false); // Laporan Rekap Kelas
                jButton12.setVisible(false); // Laporan Alpha
                
                // Ubah text Card Title agar relevan dengan user Siswa/Ortu
                lblCardSiswaTitle.setText("PROFIL ANAK");
                lblCardKelasTitle.setText("KELAS ANAK");
                lblCardOrtuTitle.setText("PROFIL ORTU");
                lblCardKehadiranTitle.setText("RINGKASAN HADIR");
            } else if (role.equalsIgnoreCase("guru")) {
                // Guru
                jButton4.setVisible(false); // Master Kelas disembunyikan
                jButton6.setVisible(false); // Rekap Bulanan mungkin tidak perlu
                jButton9.setVisible(false); // Laporan Umum
                jButton12.setVisible(false); // Laporan Alpha

                lblCardSiswaTitle.setText("TOTAL SISWA DIAJAR");
                lblCardKelasTitle.setText("KELAS DIAJAR");
                lblCardOrtuTitle.setText("TOTAL WALI MURID");
                lblCardKehadiranTitle.setText("JADWAL HARI INI");
            }
        }
        
        // Tambahkan tombol Jadwal Kelas secara dinamis di sidebar
        JButton btnJadwal = new JButton("📅  Jadwal Kelas");
        btnJadwal.setContentAreaFilled(false);
        btnJadwal.setOpaque(true);
        btnJadwal.setBackground(new java.awt.Color(25, 55, 109));
        btnJadwal.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btnJadwal.setForeground(new java.awt.Color(255, 255, 255));
        btnJadwal.setAlignmentX(15.0F);
        btnJadwal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        
        // Hover effect for btnJadwal
        btnJadwal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnJadwal.setBackground(DesignUtil.PRIMARY_LIGHT);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnJadwal.setBackground(new java.awt.Color(25, 55, 109));
            }
        });
        
        btnJadwal.addActionListener(e -> {
            new parentpoint.transaksi.JadwalKelas().setVisible(true);
        });
        
        // Sisipkan tombol ke sidebar (ke indeks 4 atau yang pas)
        jPanelSidebarMenu.add(btnJadwal, 1); // 1 = di bawah Dashboard (jButton1)
        jPanelSidebarMenu.revalidate();
        jPanelSidebarMenu.repaint();
    }
    
    private void loadDashboardData() {
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                Statement st = conn.createStatement();
                
                String role = parentpoint.util.Session.getRole();
                int siswaId = parentpoint.util.Session.getSiswaId();
                
                if (role != null && (role.equalsIgnoreCase("orang_tua") || role.equalsIgnoreCase("siswa")) && siswaId > 0) {
                    // Tampilan User Spesifik (Orang Tua / Siswa)
                    
                    // Card 1: Nama Siswa (Dari Session)
                    lblCardSiswaValue.setText(parentpoint.util.Session.getNamaSiswa());
                    lblCardSiswaValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
                    
                    // Card 2: Nama Kelas (Dari Session)
                    lblCardKelasValue.setText(parentpoint.util.Session.getNamaKelas() != null ? parentpoint.util.Session.getNamaKelas() : "-");
                    lblCardKelasValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
                    
                    // Card 3: Nama Ortu (Dari Session)
                    lblCardOrtuValue.setText(parentpoint.util.Session.getNamaOrtu() != null ? parentpoint.util.Session.getNamaOrtu() : "-");
                    lblCardOrtuValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
                    
                    // Card 4: Kehadiran Bulan Ini untuk Anak Ini
                    ResultSet rsHadir = st.executeQuery(
                        "SELECT status, COUNT(*) AS total FROM kehadiran "
                        + "WHERE siswa_id = " + siswaId + " AND MONTH(tanggal) = MONTH(CURDATE()) "
                        + "AND YEAR(tanggal) = YEAR(CURDATE()) GROUP BY status"
                    );
                    StringBuilder kehadiranInfo = new StringBuilder();
                    boolean adaData = false;
                    while (rsHadir.next()) {
                        kehadiranInfo.append(rsHadir.getString("status")).append(": ").append(rsHadir.getInt("total")).append("  ");
                        adaData = true;
                    }
                    if (!adaData) {
                        kehadiranInfo.append("Belum ada absen bulan ini");
                    }
                    rsHadir.close();
                    lblCardKehadiranValue.setText(kehadiranInfo.toString());
                    
                } else if (role != null && role.equalsIgnoreCase("guru")) {
                    String guruNama = parentpoint.util.Session.getGuruNama();
                    if (guruNama == null) guruNama = "";
                    
                    // Total Siswa Diajar
                    PreparedStatement ps1 = conn.prepareStatement(
                        "SELECT COUNT(DISTINCT s.id) AS total FROM siswa s "
                        + "JOIN kelas k ON s.kelas_id = k.id "
                        + "LEFT JOIN jadwal_kelas j ON j.kelas_id = k.id "
                        + "WHERE k.wali_kelas = ? OR j.guru = ?"
                    );
                    ps1.setString(1, guruNama);
                    ps1.setString(2, guruNama);
                    ResultSet rs1 = ps1.executeQuery();
                    int totalSiswa = 0;
                    if (rs1.next()) totalSiswa = rs1.getInt("total");
                    rs1.close(); ps1.close();
                    
                    // Total Kelas Diajar
                    PreparedStatement ps2 = conn.prepareStatement(
                        "SELECT COUNT(DISTINCT k.id) AS total FROM kelas k "
                        + "LEFT JOIN jadwal_kelas j ON j.kelas_id = k.id "
                        + "WHERE k.wali_kelas = ? OR j.guru = ?"
                    );
                    ps2.setString(1, guruNama);
                    ps2.setString(2, guruNama);
                    ResultSet rs2 = ps2.executeQuery();
                    int totalKelas = 0;
                    if (rs2.next()) totalKelas = rs2.getInt("total");
                    rs2.close(); ps2.close();
                    
                    // Total Wali Murid
                    PreparedStatement ps3 = conn.prepareStatement(
                        "SELECT COUNT(DISTINCT o.id) AS total FROM orang_tua o "
                        + "JOIN siswa s ON o.siswa_id = s.id "
                        + "JOIN kelas k ON s.kelas_id = k.id "
                        + "LEFT JOIN jadwal_kelas j ON j.kelas_id = k.id "
                        + "WHERE k.wali_kelas = ? OR j.guru = ?"
                    );
                    ps3.setString(1, guruNama);
                    ps3.setString(2, guruNama);
                    ResultSet rs3 = ps3.executeQuery();
                    int totalOrtu = 0;
                    if (rs3.next()) totalOrtu = rs3.getInt("total");
                    rs3.close(); ps3.close();
                    
                    // Jadwal Hari Ini
                    String hariIni = new java.text.SimpleDateFormat("EEEE", new java.util.Locale("id")).format(new java.util.Date());
                    PreparedStatement ps4 = conn.prepareStatement(
                        "SELECT COUNT(*) AS total FROM jadwal_kelas WHERE guru = ? AND hari = ?"
                    );
                    ps4.setString(1, guruNama);
                    ps4.setString(2, hariIni);
                    ResultSet rs4 = ps4.executeQuery();
                    int totalMapel = 0;
                    if (rs4.next()) totalMapel = rs4.getInt("total");
                    rs4.close(); ps4.close();
                    
                    lblCardSiswaValue.setText(String.valueOf(totalSiswa) + " Siswa");
                    lblCardKelasValue.setText(String.valueOf(totalKelas) + " Kelas");
                    lblCardOrtuValue.setText(String.valueOf(totalOrtu) + " Orang");
                    lblCardKehadiranValue.setText(totalMapel > 0 ? totalMapel + " Mapel (" + hariIni + ")" : "Tidak ada mapel");
                    
                } else {
                    // Tampilan Admin (Sama seperti sebelumnya)
                    ResultSet rs1 = st.executeQuery("SELECT COUNT(*) AS total FROM siswa");
                    int totalSiswa = 0;
                    if (rs1.next()) totalSiswa = rs1.getInt("total");
                    rs1.close();
                    
                    ResultSet rs2 = st.executeQuery("SELECT COUNT(*) AS total FROM kelas");
                    int totalKelas = 0;
                    if (rs2.next()) totalKelas = rs2.getInt("total");
                    rs2.close();
                    
                    ResultSet rs3 = st.executeQuery("SELECT COUNT(*) AS total FROM orang_tua");
                    int totalOrtu = 0;
                    if (rs3.next()) totalOrtu = rs3.getInt("total");
                    rs3.close();
                    
                    ResultSet rs4 = st.executeQuery(
                        "SELECT status, COUNT(*) AS total FROM kehadiran "
                        + "WHERE tanggal = CURDATE() GROUP BY status");
                    StringBuilder kehadiranInfo = new StringBuilder();
                    boolean adaData = false;
                    while (rs4.next()) {
                        kehadiranInfo.append(rs4.getString("status")).append(": ").append(rs4.getInt("total")).append("  ");
                        adaData = true;
                    }
                    if (!adaData) kehadiranInfo.append("Belum ada data hari ini");
                    rs4.close();
                    
                    lblCardSiswaValue.setText(String.valueOf(totalSiswa));
                    lblCardKelasValue.setText(String.valueOf(totalKelas));
                    lblCardOrtuValue.setText(String.valueOf(totalOrtu));
                    lblCardKehadiranValue.setText(kehadiranInfo.toString());
                }
                
                lblSystemInfo.setText("<html><b style='font-size:13px; color:#19376D;'>INFORMASI SISTEM &amp; DATABASE:</b><br><br>"
                    + "&bull; Status Koneksi Database: <font color='#27ae60'><b>TERHUBUNG (MySQL)</b></font><br>"
                    + "&bull; Hari Ini: <b>" + new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", new java.util.Locale("id")).format(new java.util.Date()) + "</b><br>"
                    + "&bull; Gunakan menu di sidebar sebelah kiri untuk menavigasi modul siswa, kelas, orang tua, transaksi kehadiran harian, dan pencetakan laporan.</html>");
                
                st.close();
                conn.close();
            } catch (SQLException e) {
                lblSystemInfo.setText("Error memuat data: " + e.getMessage());
            }
        } else {
            lblSystemInfo.setText("Gagal koneksi ke database!");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelSidebar = new javax.swing.JPanel();
        jPanelSidebarMenu = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        // jButton9-jButton12 are initialized later in initComponents where they are added to sidebar
        jPanelSidebarUserInfo = new javax.swing.JPanel();
        jPanelDashboard = new javax.swing.JPanel();
        jPanelHeader = new javax.swing.JPanel();
        lblHeaderTitle = new javax.swing.JLabel();
        jPanelTextWrapper = new javax.swing.JPanel();
        lblUsername = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jPanelDashboardBody = new javax.swing.JPanel();
        lblDashboardTitle = new javax.swing.JLabel();
        jPanelCards = new javax.swing.JPanel();
        jPanelCardSiswa = new javax.swing.JPanel();
        jPanelCardSiswaBar = new javax.swing.JPanel();
        jPanelCardSiswaContent = new javax.swing.JPanel();
        lblCardSiswaTitle = new javax.swing.JLabel();
        lblCardSiswaValue = new javax.swing.JLabel();
        jPanelCardKelas = new javax.swing.JPanel();
        jPanelCardKelasBar = new javax.swing.JPanel();
        jPanelCardKelasContent = new javax.swing.JPanel();
        lblCardKelasTitle = new javax.swing.JLabel();
        lblCardKelasValue = new javax.swing.JLabel();
        jPanelCardOrtu = new javax.swing.JPanel();
        jPanelCardOrtuBar = new javax.swing.JPanel();
        jPanelCardOrtuContent = new javax.swing.JPanel();
        lblCardOrtuTitle = new javax.swing.JLabel();
        lblCardOrtuValue = new javax.swing.JLabel();
        jPanelCardKehadiran = new javax.swing.JPanel();
        jPanelCardKehadiranBar = new javax.swing.JPanel();
        jPanelCardKehadiranContent = new javax.swing.JPanel();
        lblCardKehadiranTitle = new javax.swing.JLabel();
        lblCardKehadiranValue = new javax.swing.JLabel();
        lblSystemInfo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PARENT POINT - Dashboard Utama");

        jPanelSidebar.setBackground(new java.awt.Color(25, 55, 109));
        jPanelSidebar.setLayout(new java.awt.BorderLayout());

        jPanelSidebarMenu.setBackground(new java.awt.Color(25, 55, 109));
        jPanelSidebarMenu.setLayout(new java.awt.GridLayout(15, 1, 0, 2));

        jButton1.setContentAreaFilled(false);
        jButton1.setOpaque(true);
        jButton1.setBackground(new java.awt.Color(25, 55, 109));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("📊  Dashboard");
        jButton1.setAlignmentX(15.0F);
        jPanelSidebarMenu.add(jButton1);

        jButton2.setContentAreaFilled(false);
        jButton2.setOpaque(true);
        jButton2.setBackground(new java.awt.Color(25, 55, 109));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("👨‍🎓  Data Siswa");
        jButton2.setAlignmentX(15.0F);
        jPanelSidebarMenu.add(jButton2);

        jButton3.setContentAreaFilled(false);
        jButton3.setOpaque(true);
        jButton3.setBackground(new java.awt.Color(25, 55, 109));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("👪  Data Orang Tua");
        jButton3.setAlignmentX(15.0F);
        jPanelSidebarMenu.add(jButton3);

        jButton4.setContentAreaFilled(false);
        jButton4.setOpaque(true);
        jButton4.setBackground(new java.awt.Color(25, 55, 109));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("🏫  Data Kelas");
        jButton4.setAlignmentX(15.0F);
        jPanelSidebarMenu.add(jButton4);

        jButton5.setContentAreaFilled(false);
        jButton5.setOpaque(true);
        jButton5.setBackground(new java.awt.Color(25, 55, 109));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("📝  Input Kehadiran");
        jButton5.setAlignmentX(15.0F);
        jPanelSidebarMenu.add(jButton5);

        jButton6.setContentAreaFilled(false);
        jButton6.setOpaque(true);
        jButton6.setBackground(new java.awt.Color(25, 55, 109));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("📅  Rekap Bulanan");
        jButton6.setAlignmentX(15.0F);
        jPanelSidebarMenu.add(jButton6);

        jButton7.setContentAreaFilled(false);
        jButton7.setOpaque(true);
        jButton7.setBackground(new java.awt.Color(25, 55, 109));
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("👤  Manajemen User");
        jButton7.setAlignmentX(15.0F);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanelSidebarMenu.add(jButton7);

        // ---- Separator Label: LAPORAN ----
        javax.swing.JLabel lblSepLaporan = new javax.swing.JLabel("  ─── LAPORAN ───");
        lblSepLaporan.setFont(new java.awt.Font("Segoe UI", 0, 11));
        lblSepLaporan.setForeground(new java.awt.Color(148, 163, 184));
        jPanelSidebarMenu.add(lblSepLaporan);

        jButton9 = new javax.swing.JButton();
        jButton9.setContentAreaFilled(false);
        jButton9.setOpaque(true);
        jButton9.setBackground(new java.awt.Color(25, 55, 109));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("📈  Lap. Kehadiran");
        jButton9.setAlignmentX(15.0F);
        jPanelSidebarMenu.add(jButton9);

        jButton10 = new javax.swing.JButton();
        jButton10.setContentAreaFilled(false);
        jButton10.setOpaque(true);
        jButton10.setBackground(new java.awt.Color(25, 55, 109));
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("📊  Rekap Per Siswa");
        jButton10.setAlignmentX(15.0F);
        jPanelSidebarMenu.add(jButton10);

        jButton11 = new javax.swing.JButton();
        jButton11.setContentAreaFilled(false);
        jButton11.setOpaque(true);
        jButton11.setBackground(new java.awt.Color(25, 55, 109));
        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("🏫  Rekap Per Kelas");
        jButton11.setAlignmentX(15.0F);
        jPanelSidebarMenu.add(jButton11);

        jButton12 = new javax.swing.JButton();
        jButton12.setContentAreaFilled(false);
        jButton12.setOpaque(true);
        jButton12.setBackground(new java.awt.Color(25, 55, 109));
        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("⚠️  Siswa Sering Absen");
        jButton12.setAlignmentX(15.0F);
        jPanelSidebarMenu.add(jButton12);

        jPanelSidebar.add(jPanelSidebarMenu, java.awt.BorderLayout.CENTER);

        jPanelSidebarUserInfo.setBackground(new java.awt.Color(41, 82, 148));
        jPanelSidebarUserInfo.setLayout(new java.awt.BorderLayout(0, 10));
        jPanelSidebar.add(jPanelSidebarUserInfo, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanelSidebar, java.awt.BorderLayout.WEST);

        jPanelDashboard.setLayout(new java.awt.BorderLayout());

        jPanelHeader.setBackground(new java.awt.Color(25, 55, 109));
        jPanelHeader.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 15));

        lblHeaderTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblHeaderTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblHeaderTitle.setText("📊  Dashboard Utama - PARENT POINT");
        jPanelHeader.add(lblHeaderTitle);

        jPanelTextWrapper.setBackground(new java.awt.Color(41, 82, 148));
        jPanelTextWrapper.setLayout(new java.awt.GridLayout(2, 1, 0, 4));

        lblUsername.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblUsername.setText("Administrator");
        jPanelTextWrapper.add(lblUsername);

        lblRole.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblRole.setForeground(new java.awt.Color(189, 195, 199));
        lblRole.setText("Role: Admin");
        jPanelTextWrapper.add(lblRole);

        jButton8.setBackground(new java.awt.Color(231, 76, 60));
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Keluar");
        jButton8.setContentAreaFilled(false);
        jButton8.setOpaque(true);
        jPanelTextWrapper.add(jButton8);

        jPanelHeader.add(jPanelTextWrapper);

        jPanelDashboard.add(jPanelHeader, java.awt.BorderLayout.NORTH);

        jPanelDashboardBody.setBackground(new java.awt.Color(236, 240, 245));
        jPanelDashboardBody.setLayout(new java.awt.BorderLayout(0, 20));

        lblDashboardTitle.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblDashboardTitle.setForeground(new java.awt.Color(44, 62, 80));
        lblDashboardTitle.setText("Ringkasan Data Aplikasi");
        jPanelDashboardBody.add(lblDashboardTitle, java.awt.BorderLayout.NORTH);

        jPanelCards.setBackground(new java.awt.Color(236, 240, 245));
        jPanelCards.setLayout(new java.awt.GridLayout(1, 4, 15, 0));

        jPanelCardSiswa.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCardSiswa.setLayout(new java.awt.BorderLayout());

        jPanelCardSiswaBar.setBackground(new java.awt.Color(52, 152, 219));
        jPanelCardSiswa.add(jPanelCardSiswaBar, java.awt.BorderLayout.NORTH);

        jPanelCardSiswaContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCardSiswaContent.setLayout(new java.awt.BorderLayout(0, 5));

        lblCardSiswaTitle.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblCardSiswaTitle.setForeground(new java.awt.Color(127, 140, 141));
        lblCardSiswaTitle.setText("TOTAL SISWA");
        jPanelCardSiswaContent.add(lblCardSiswaTitle, java.awt.BorderLayout.NORTH);

        lblCardSiswaValue.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblCardSiswaValue.setForeground(new java.awt.Color(52, 152, 219));
        lblCardSiswaValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCardSiswaValue.setText("0");
        jPanelCardSiswaContent.add(lblCardSiswaValue, java.awt.BorderLayout.CENTER);

        jPanelCardSiswa.add(jPanelCardSiswaContent, java.awt.BorderLayout.CENTER);

        jPanelCards.add(jPanelCardSiswa);

        jPanelCardKelas.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCardKelas.setLayout(new java.awt.BorderLayout());

        jPanelCardKelasBar.setBackground(new java.awt.Color(39, 174, 96));
        jPanelCardKelas.add(jPanelCardKelasBar, java.awt.BorderLayout.NORTH);

        jPanelCardKelasContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCardKelasContent.setLayout(new java.awt.BorderLayout(0, 5));

        lblCardKelasTitle.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblCardKelasTitle.setForeground(new java.awt.Color(127, 140, 141));
        lblCardKelasTitle.setText("TOTAL KELAS");
        jPanelCardKelasContent.add(lblCardKelasTitle, java.awt.BorderLayout.NORTH);

        lblCardKelasValue.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblCardKelasValue.setForeground(new java.awt.Color(39, 174, 96));
        lblCardKelasValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCardKelasValue.setText("0");
        jPanelCardKelasContent.add(lblCardKelasValue, java.awt.BorderLayout.CENTER);

        jPanelCardKelas.add(jPanelCardKelasContent, java.awt.BorderLayout.CENTER);

        jPanelCards.add(jPanelCardKelas);

        jPanelCardOrtu.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCardOrtu.setLayout(new java.awt.BorderLayout());

        jPanelCardOrtuBar.setBackground(new java.awt.Color(142, 68, 173));
        jPanelCardOrtu.add(jPanelCardOrtuBar, java.awt.BorderLayout.NORTH);

        jPanelCardOrtuContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCardOrtuContent.setLayout(new java.awt.BorderLayout(0, 5));

        lblCardOrtuTitle.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblCardOrtuTitle.setForeground(new java.awt.Color(127, 140, 141));
        lblCardOrtuTitle.setText("TOTAL ORANG TUA");
        jPanelCardOrtuContent.add(lblCardOrtuTitle, java.awt.BorderLayout.NORTH);

        lblCardOrtuValue.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblCardOrtuValue.setForeground(new java.awt.Color(142, 68, 173));
        lblCardOrtuValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCardOrtuValue.setText("0");
        jPanelCardOrtuContent.add(lblCardOrtuValue, java.awt.BorderLayout.CENTER);

        jPanelCardOrtu.add(jPanelCardOrtuContent, java.awt.BorderLayout.CENTER);

        jPanelCards.add(jPanelCardOrtu);

        jPanelCardKehadiran.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCardKehadiran.setLayout(new java.awt.BorderLayout());

        jPanelCardKehadiranBar.setBackground(new java.awt.Color(243, 156, 18));
        jPanelCardKehadiran.add(jPanelCardKehadiranBar, java.awt.BorderLayout.NORTH);

        jPanelCardKehadiranContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCardKehadiranContent.setLayout(new java.awt.BorderLayout(0, 5));

        lblCardKehadiranTitle.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblCardKehadiranTitle.setForeground(new java.awt.Color(127, 140, 141));
        lblCardKehadiranTitle.setText("KEHADIRAN HARI INI");
        jPanelCardKehadiranContent.add(lblCardKehadiranTitle, java.awt.BorderLayout.NORTH);

        lblCardKehadiranValue.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        lblCardKehadiranValue.setForeground(new java.awt.Color(243, 156, 18));
        lblCardKehadiranValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCardKehadiranValue.setText("Loading...");
        jPanelCardKehadiranContent.add(lblCardKehadiranValue, java.awt.BorderLayout.CENTER);

        jPanelCardKehadiran.add(jPanelCardKehadiranContent, java.awt.BorderLayout.CENTER);

        jPanelCards.add(jPanelCardKehadiran);

        jPanelDashboardBody.add(jPanelCards, java.awt.BorderLayout.CENTER);

        lblSystemInfo.setBackground(new java.awt.Color(255, 255, 255));
        lblSystemInfo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblSystemInfo.setForeground(new java.awt.Color(44, 62, 80));
        lblSystemInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSystemInfo.setText("Informasi Sistem");
        lblSystemInfo.setOpaque(true);
        jPanelDashboardBody.add(lblSystemInfo, java.awt.BorderLayout.SOUTH);

        jPanelDashboard.add(jPanelDashboardBody, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanelDashboard, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin keluar?", 
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            LOGIN login = new LOGIN();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
            this.dispose();
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new dsmainframe().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JPanel jPanelCardKehadiran;
    private javax.swing.JPanel jPanelCardKehadiranBar;
    private javax.swing.JPanel jPanelCardKehadiranContent;
    private javax.swing.JPanel jPanelCardKelas;
    private javax.swing.JPanel jPanelCardKelasBar;
    private javax.swing.JPanel jPanelCardKelasContent;
    private javax.swing.JPanel jPanelCardOrtu;
    private javax.swing.JPanel jPanelCardOrtuBar;
    private javax.swing.JPanel jPanelCardOrtuContent;
    private javax.swing.JPanel jPanelCardSiswa;
    private javax.swing.JPanel jPanelCardSiswaBar;
    private javax.swing.JPanel jPanelCardSiswaContent;
    private javax.swing.JPanel jPanelCards;
    private javax.swing.JPanel jPanelDashboard;
    private javax.swing.JPanel jPanelDashboardBody;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelSidebar;
    private javax.swing.JPanel jPanelSidebarMenu;
    private javax.swing.JPanel jPanelSidebarUserInfo;
    private javax.swing.JPanel jPanelTextWrapper;
    private javax.swing.JLabel lblCardKehadiranTitle;
    private javax.swing.JLabel lblCardKehadiranValue;
    private javax.swing.JLabel lblCardKelasTitle;
    private javax.swing.JLabel lblCardKelasValue;
    private javax.swing.JLabel lblCardOrtuTitle;
    private javax.swing.JLabel lblCardOrtuValue;
    private javax.swing.JLabel lblCardSiswaTitle;
    private javax.swing.JLabel lblCardSiswaValue;
    private javax.swing.JLabel lblDashboardTitle;
    private javax.swing.JLabel lblHeaderTitle;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSystemInfo;
    private javax.swing.JLabel lblUsername;
    // End of variables declaration//GEN-END:variables
}

