/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parentpoint.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import parentpoint.koneksi.koneksi;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import parentpoint.util.DesignUtil;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;

/**
 *
 * @author HP
 */
public class report extends javax.swing.JFrame {

    public report() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setTitle("PARENT POINT - Laporan Kehadiran");
        setLocationRelativeTo(null);
        
        // Load data kelas ke ComboBox
        loadKelas();
        
        // Set placeholder untuk tanggal (dihapus karena JDateChooser)
        dateDari.setDate(new java.util.Date());
        dateSampai.setDate(new java.util.Date());
        
        // Action tombol Tampilkan
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tampilkanLaporan();
            }
        });
        
        // Action tombol Reset
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetForm();
            }
        });
        
        // Action tombol Cetak
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakLaporan();
            }
        });
        
        styleComponents();
    }
    
    private void cetakLaporan() {
        try {
            Connection conn = koneksi.getConnection();
            
            // Mengambil parameter dari UI
            String kelas = (String) jComboBox1.getSelectedItem();
            String dariTanggal = new SimpleDateFormat("yyyy-MM-dd").format(dateDari.getDate());
            String sampaiTanggal = new SimpleDateFormat("yyyy-MM-dd").format(dateSampai.getDate());
            String cari = txtCari.getText().trim();
            
            java.util.HashMap<String, Object> parameters = new java.util.HashMap<>();
            parameters.put("p_dari", dariTanggal);
            parameters.put("p_sampai", sampaiTanggal);
            parameters.put("p_kelas", kelas);
            parameters.put("p_cari", "%" + cari + "%");
            
            // File .jasper / .jrxml hasil desain iReport (pastikan file-nya ada)
            // Menggunakan .jrxml yang dikompilasi secara on-the-fly atau menggunakan jasper yang sudah jadi.
            // Di sini kita asumsikan menggunakan report_kehadiran.jrxml yang dikompilasi
            String path = "src/parentpoint/report/report_kehadiran.jrxml";
            java.io.File f = new java.io.File(path);
            if (!f.exists()) {
                JOptionPane.showMessageDialog(this, "File desain report tidak ditemukan di: " + path);
                return;
            }
            
            net.sf.jasperreports.engine.design.JasperDesign jd = net.sf.jasperreports.engine.xml.JRXmlLoader.load(path);
            net.sf.jasperreports.engine.JasperReport jr = net.sf.jasperreports.engine.JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, conn);
            
            JasperViewer.viewReport(jp, false);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mencetak laporan: " + e.getMessage(), "Error Cetak", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void loadKelas() {
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                jComboBox1.removeAllItems();
                jComboBox1.addItem("Semua Kelas");
                
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT nama_kelas FROM kelas ORDER BY nama_kelas");
                while (rs.next()) {
                    jComboBox1.addItem(rs.getString("nama_kelas"));
                }
                rs.close();
                st.close();
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error load kelas: " + e.getMessage());
            }
        }
    }
    
    private void tampilkanLaporan() {
        String kelas = (String) jComboBox1.getSelectedItem();
        String cari = txtCari.getText().trim();
        
        if (dateDari.getDate() == null || dateSampai.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Tanggal harus dipilih!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String dariTanggal = new SimpleDateFormat("yyyy-MM-dd").format(dateDari.getDate());
        String sampaiTanggal = new SimpleDateFormat("yyyy-MM-dd").format(dateSampai.getDate());
        
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                // Query untuk data tabel
                String sql = "SELECT s.nis, s.nama, k.nama_kelas, h.tanggal, h.status, h.keterangan "
                           + "FROM kehadiran h "
                           + "JOIN siswa s ON h.siswa_id = s.id "
                           + "LEFT JOIN kelas k ON s.kelas_id = k.id "
                           + "WHERE h.tanggal BETWEEN ? AND ? ";
                
                if (kelas != null && !kelas.equals("Semua Kelas")) {
                    sql += "AND k.nama_kelas = ? ";
                }
                if (!cari.isEmpty()) {
                    sql += "AND s.nama LIKE ? ";
                }
                sql += "ORDER BY h.tanggal, s.nama";
                
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, dariTanggal);
                ps.setString(2, sampaiTanggal);
                int pIndex = 3;
                if (kelas != null && !kelas.equals("Semua Kelas")) {
                    ps.setString(pIndex++, kelas);
                }
                if (!cari.isEmpty()) {
                    ps.setString(pIndex++, "%" + cari + "%");
                }
                
                ResultSet rs = ps.executeQuery();
                
                // Setup tabel model
                String[] columns = {"NIS", "Nama Siswa", "Kelas", "Tanggal", "Status", "Keterangan"};
                DefaultTableModel model = new DefaultTableModel(columns, 0);
                
                int totalHadir = 0, totalSakit = 0, totalIzin = 0, totalAlpha = 0;
                
                while (rs.next()) {
                    String status = rs.getString("status");
                    String ket = rs.getString("keterangan");
                    
                    model.addRow(new Object[]{
                        rs.getString("nis"),
                        rs.getString("nama"),
                        rs.getString("nama_kelas"),
                        rs.getString("tanggal"),
                        status,
                        ket != null ? ket : "-"
                    });
                    
                    // Hitung total
                    switch (status) {
                        case "Hadir": totalHadir++; break;
                        case "Sakit": totalSakit++; break;
                        case "Izin": totalIzin++; break;
                        case "Alpha": totalAlpha++; break;
                    }
                }
                
                jTable1.setModel(model);
                
                // Update label total
                jLabel5.setText(String.valueOf(totalHadir));
                jLabel7.setText(String.valueOf(totalSakit));
                jLabel9.setText(String.valueOf(totalIzin));
                jLabel11.setText(String.valueOf(totalAlpha));
                
                rs.close();
                ps.close();
                conn.close();
                
                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Tidak ada data kehadiran untuk filter ini.", 
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                }
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void resetForm() {
        jComboBox1.setSelectedIndex(0);
        dateDari.setDate(new java.util.Date());
        dateSampai.setDate(new java.util.Date());
        txtCari.setText("");
        jLabel5.setText("0");
        jLabel7.setText("0");
        jLabel9.setText("0");
        jLabel11.setText("0");
        jTable1.setModel(new DefaultTableModel());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jPanel1 = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        txtCari = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        dateDari = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        dateSampai = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnCetak = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanelCardHadirBar = new javax.swing.JPanel();
        jPanelCardHadirContent = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanelCardSakitBar = new javax.swing.JPanel();
        jPanelCardSakitContent = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanelCardIzinBar = new javax.swing.JPanel();
        jPanelCardIzinContent = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanelCardAlphaBar = new javax.swing.JPanel();
        jPanelCardAlphaContent = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NIS", "Nama Siswa", "Kelas", "Tanggal", "Status", "Keterangan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(25, 55, 109));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 60));
        jPanel1.setLayout(new java.awt.BorderLayout());

        lblHeader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblHeader.setText("Laporan Kehadiran Siswa");
        jPanel1.add(lblHeader, java.awt.BorderLayout.WEST);

        jPanel2.setBorder(new javax.swing.border.MatteBorder(null));

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel1.setText("Kelas :");
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 50));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jComboBox1.setPreferredSize(new java.awt.Dimension(100, 50));

        jLabel12.setText("Cari Siswa :");
        jLabel12.setPreferredSize(new java.awt.Dimension(80, 50));

        txtCari.setPreferredSize(new java.awt.Dimension(120, 50));

        jLabel2.setText("Dari Tanggal :");
        jLabel2.setPreferredSize(new java.awt.Dimension(80, 50));

        dateDari.setPreferredSize(new java.awt.Dimension(120, 50));
        dateDari.setDateFormatString("yyyy-MM-dd");

        jLabel3.setText("Sampai :");
        jLabel3.setPreferredSize(new java.awt.Dimension(60, 50));

        dateSampai.setPreferredSize(new java.awt.Dimension(120, 50));
        dateSampai.setDateFormatString("yyyy-MM-dd");

        jButton1.setBackground(new java.awt.Color(25, 55, 109));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Tampilkan");
        jButton1.setPreferredSize(new java.awt.Dimension(110, 35));
        
        jButton2.setBackground(new java.awt.Color(230, 126, 34));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Reset");
        jButton2.setPreferredSize(new java.awt.Dimension(90, 35));

        btnCetak.setBackground(new java.awt.Color(46, 204, 113));
        btnCetak.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnCetak.setForeground(new java.awt.Color(255, 255, 255));
        btnCetak.setText("Cetak (iReport)");
        btnCetak.setPreferredSize(new java.awt.Dimension(130, 35));
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(dateDari, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(dateSampai, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateDari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateSampai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
            javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        jPanel4.setPreferredSize(new java.awt.Dimension(200, 100));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanelCardHadirBar.setBackground(new java.awt.Color(39, 174, 96));
        jPanelCardHadirBar.setPreferredSize(new java.awt.Dimension(0, 4));
        jPanelCardHadirBar.setLayout(new java.awt.FlowLayout());
        jPanel4.add(jPanelCardHadirBar, java.awt.BorderLayout.NORTH);

        jPanelCardHadirContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCardHadirContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jPanelCardHadirContent.setLayout(new java.awt.BorderLayout(0, 5));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(127, 140, 141));
        jLabel4.setText("HADIR");
        jPanelCardHadirContent.add(jLabel4, java.awt.BorderLayout.NORTH);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(39, 174, 96));
        jLabel5.setText("0");
        jPanelCardHadirContent.add(jLabel5, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanelCardHadirContent, java.awt.BorderLayout.CENTER);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
            javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        jPanel5.setPreferredSize(new java.awt.Dimension(200, 100));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanelCardSakitBar.setBackground(new java.awt.Color(243, 156, 18));
        jPanelCardSakitBar.setPreferredSize(new java.awt.Dimension(0, 4));
        jPanelCardSakitBar.setLayout(new java.awt.FlowLayout());
        jPanel5.add(jPanelCardSakitBar, java.awt.BorderLayout.NORTH);

        jPanelCardSakitContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCardSakitContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jPanelCardSakitContent.setLayout(new java.awt.BorderLayout(0, 5));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(127, 140, 141));
        jLabel6.setText("SAKIT");
        jPanelCardSakitContent.add(jLabel6, java.awt.BorderLayout.NORTH);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(243, 156, 18));
        jLabel7.setText("0");
        jPanelCardSakitContent.add(jLabel7, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanelCardSakitContent, java.awt.BorderLayout.CENTER);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
            javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        jPanel6.setPreferredSize(new java.awt.Dimension(200, 100));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanelCardIzinBar.setBackground(new java.awt.Color(52, 152, 219));
        jPanelCardIzinBar.setPreferredSize(new java.awt.Dimension(0, 4));
        jPanelCardIzinBar.setLayout(new java.awt.FlowLayout());
        jPanel6.add(jPanelCardIzinBar, java.awt.BorderLayout.NORTH);

        jPanelCardIzinContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCardIzinContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jPanelCardIzinContent.setLayout(new java.awt.BorderLayout(0, 5));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(127, 140, 141));
        jLabel8.setText("IZIN");
        jPanelCardIzinContent.add(jLabel8, java.awt.BorderLayout.NORTH);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(52, 152, 219));
        jLabel9.setText("0");
        jPanelCardIzinContent.add(jLabel9, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanelCardIzinContent, java.awt.BorderLayout.CENTER);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED),
            javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15)));
        jPanel7.setPreferredSize(new java.awt.Dimension(200, 100));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanelCardAlphaBar.setBackground(new java.awt.Color(231, 76, 60));
        jPanelCardAlphaBar.setPreferredSize(new java.awt.Dimension(0, 4));
        jPanelCardAlphaBar.setLayout(new java.awt.FlowLayout());
        jPanel7.add(jPanelCardAlphaBar, java.awt.BorderLayout.NORTH);

        jPanelCardAlphaContent.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCardAlphaContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0));
        jPanelCardAlphaContent.setLayout(new java.awt.BorderLayout(0, 5));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(127, 140, 141));
        jLabel10.setText("ALPHA");
        jPanelCardAlphaContent.add(jLabel10, java.awt.BorderLayout.NORTH);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(231, 76, 60));
        jLabel11.setText("0");
        jPanelCardAlphaContent.add(jLabel11, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanelCardAlphaContent, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1001, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(report.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(report.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(report.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(report.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new report().setVisible(true);
            }
        });
    }

    private void styleComponents() {
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setTitle("PARENT POINT - Laporan Kehadiran");
        getContentPane().setBackground(DesignUtil.BG_MAIN);
        
        // Reset top header panel
        jPanel1.removeAll();
        jPanel1.setLayout(new BorderLayout());
        jPanel1.setBackground(DesignUtil.BG_HEADER);
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 60));
        jPanel1.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        
        JLabel lblHeader = new JLabel("Laporan Kehadiran Siswa");
        lblHeader.setFont(DesignUtil.FONT_SUBTITLE);
        lblHeader.setForeground(Color.WHITE);
        jPanel1.add(lblHeader, BorderLayout.WEST);
        
        // Style filter panel (jPanel3)
        jPanel3.setBackground(DesignUtil.BG_CARD);
        jPanel3.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // Style labels
        jLabel1.setFont(DesignUtil.FONT_BODY_BOLD);
        jLabel1.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabel1.setText("Kelas:");
        
        jLabel2.setFont(DesignUtil.FONT_BODY_BOLD);
        jLabel2.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabel2.setText("Dari Tanggal:");
        
        jLabel3.setFont(DesignUtil.FONT_BODY_BOLD);
        jLabel3.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabel3.setText("Sampai:");
        
        jLabel12.setFont(DesignUtil.FONT_BODY_BOLD);
        jLabel12.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabel12.setText("Cari Siswa:");
        
        // Style textfields and combobox
        jComboBox1.setFont(DesignUtil.FONT_BODY);
        jComboBox1.setBackground(Color.WHITE);
        jComboBox1.setPreferredSize(new java.awt.Dimension(120, 35));
        
        txtCari.setFont(DesignUtil.FONT_BODY);
        txtCari.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtCari.setPreferredSize(new java.awt.Dimension(120, 35));
        
        dateDari.setFont(DesignUtil.FONT_BODY);
        dateDari.setPreferredSize(new java.awt.Dimension(120, 35));
        
        dateSampai.setFont(DesignUtil.FONT_BODY);
        dateSampai.setPreferredSize(new java.awt.Dimension(120, 35));
        
        // Buttons
        jButton1.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        jButton1.setFont(DesignUtil.FONT_BUTTON);
        jButton1.setBackground(DesignUtil.PRIMARY);
        jButton1.setForeground(Color.WHITE);
        jButton1.setFocusPainted(false);
        jButton1.setBorderPainted(false);
        jButton1.setOpaque(true);
        jButton1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButton1.setPreferredSize(new java.awt.Dimension(110, 35));
        
        jButton2.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        jButton2.setFont(DesignUtil.FONT_BUTTON);
        jButton2.setBackground(DesignUtil.WARNING);
        jButton2.setForeground(Color.WHITE);
        jButton2.setFocusPainted(false);
        jButton2.setBorderPainted(false);
        jButton2.setOpaque(true);
        jButton2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jButton2.setPreferredSize(new java.awt.Dimension(90, 35));
        
        // Hover effects
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { jButton1.setBackground(DesignUtil.PRIMARY_LIGHT); }
            public void mouseExited(java.awt.event.MouseEvent e) { jButton1.setBackground(DesignUtil.PRIMARY); }
        });
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { jButton2.setBackground(DesignUtil.WARNING.darker()); }
            public void mouseExited(java.awt.event.MouseEvent e) { jButton2.setBackground(DesignUtil.WARNING); }
        });
        
        btnCetak.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnCetak.setFont(DesignUtil.FONT_BUTTON);
        btnCetak.setBackground(new Color(46, 204, 113));
        btnCetak.setForeground(Color.WHITE);
        btnCetak.setFocusPainted(false);
        btnCetak.setBorderPainted(false);
        btnCetak.setOpaque(true);
        btnCetak.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCetak.setPreferredSize(new java.awt.Dimension(130, 35));
        btnCetak.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnCetak.setBackground(new Color(39, 174, 96)); }
            public void mouseExited(java.awt.event.MouseEvent e) { btnCetak.setBackground(new Color(46, 204, 113)); }
        });
        
        // Cards styling (jPanel4, jPanel5, jPanel6, jPanel7)
        JPanel[] cardPanels = {jPanel4, jPanel5, jPanel6, jPanel7};
        JLabel[] cardTitles = {jLabel4, jLabel6, jLabel8, jLabel10};
        JLabel[] cardValues = {jLabel5, jLabel7, jLabel9, jLabel11};
        Color[] accentColors = {DesignUtil.SUCCESS, DesignUtil.WARNING, DesignUtil.ACCENT, DesignUtil.DANGER};
        String[] titlesText = {"HADIR", "SAKIT", "IZIN", "ALPHA"};
        
        for (int i = 0; i < cardPanels.length; i++) {
            JPanel card = cardPanels[i];
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            
            // Re-layout cards programmatically to be nice
            card.removeAll();
            card.setLayout(new BorderLayout());
            
            // Accent bar
            JPanel bar = new JPanel();
            bar.setBackground(accentColors[i]);
            bar.setPreferredSize(new java.awt.Dimension(0, 4));
            card.add(bar, BorderLayout.NORTH);
            
            JPanel content = new JPanel();
            content.setLayout(new javax.swing.BoxLayout(content, javax.swing.BoxLayout.Y_AXIS));
            content.setBackground(Color.WHITE);
            content.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            
            JLabel lblTitle = cardTitles[i];
            lblTitle.setText(titlesText[i]);
            lblTitle.setFont(DesignUtil.FONT_CARD_LABEL);
            lblTitle.setForeground(DesignUtil.TEXT_SECONDARY);
            lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel lblVal = cardValues[i];
            lblVal.setFont(DesignUtil.FONT_CARD_NUMBER);
            lblVal.setForeground(accentColors[i]);
            lblVal.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            content.add(lblTitle);
            content.add(javax.swing.Box.createVerticalStrut(5));
            content.add(lblVal);
            
            card.add(content, BorderLayout.CENTER);
        }
        
        // Table styling
        DesignUtil.styleTable(jTable1);
        jScrollPane2.setBorder(BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR));
        jScrollPane2.getViewport().setBackground(Color.WHITE);
        
        // Re-layout JFrame content pane and jPanel2 programmatically to be fully responsive
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jPanel1, BorderLayout.NORTH);
        getContentPane().add(jPanel2, BorderLayout.CENTER);
        
        jPanel2.removeAll();
        jPanel2.setLayout(new BorderLayout(0, 15));
        jPanel2.add(jPanel3, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(DesignUtil.BG_MAIN);
        
        JPanel cardsContainer = new JPanel(new java.awt.GridLayout(1, 4, 15, 0));
        cardsContainer.setBackground(DesignUtil.BG_MAIN);
        cardsContainer.add(jPanel4);
        cardsContainer.add(jPanel5);
        cardsContainer.add(jPanel6);
        cardsContainer.add(jPanel7);
        
        centerPanel.add(cardsContainer, BorderLayout.NORTH);
        centerPanel.add(jScrollPane2, BorderLayout.CENTER);
        
        jPanel2.add(centerPanel, BorderLayout.CENTER);
        
        // Main wrappers styling to prevent cutoffs
        jPanel2.setBackground(DesignUtil.BG_MAIN);
        jPanel2.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        pack();
// setSize removed for responsiveness
        setLocationRelativeTo(null);
        
        revalidate();
        repaint();

        // ── Tombol Kembali ke Dashboard ──
        addKembaliButton();
    }

    private void addKembaliButton() {
        javax.swing.JButton btnKembali = new javax.swing.JButton("⬅  Kembali ke Dashboard");
        btnKembali.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnKembali.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        btnKembali.setBackground(new java.awt.Color(71, 85, 105));
        btnKembali.setForeground(java.awt.Color.WHITE);
        btnKembali.setFocusPainted(false);
        btnKembali.setBorderPainted(false);
        btnKembali.setOpaque(true);
        btnKembali.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKembali.setPreferredSize(new java.awt.Dimension(195, 35));
        btnKembali.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 15));
        btnKembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnKembali.setBackground(new java.awt.Color(51, 65, 85)); }
            public void mouseExited(java.awt.event.MouseEvent e) { btnKembali.setBackground(new java.awt.Color(71, 85, 105)); }
        });
        btnKembali.addActionListener(e -> {
            for (java.awt.Window w : java.awt.Window.getWindows()) {
                if (w instanceof parentpoint.ds.dsmainframe && w.isDisplayable()) {
                    w.setVisible(true); w.toFront(); dispose(); return;
                }
            }
            new parentpoint.ds.dsmainframe().setVisible(true);
            dispose();
        });
        jPanel1.add(btnKembali, java.awt.BorderLayout.EAST);
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanelCardAlphaBar;
    private javax.swing.JPanel jPanelCardAlphaContent;
    private javax.swing.JPanel jPanelCardHadirBar;
    private javax.swing.JPanel jPanelCardHadirContent;
    private javax.swing.JPanel jPanelCardIzinBar;
    private javax.swing.JPanel jPanelCardIzinContent;
    private javax.swing.JPanel jPanelCardSakitBar;
    private javax.swing.JPanel jPanelCardSakitContent;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private com.toedter.calendar.JDateChooser dateDari;
    private com.toedter.calendar.JDateChooser dateSampai;
    private javax.swing.JTextField txtCari;
    private javax.swing.JLabel lblHeader;
    // End of variables declaration//GEN-END:variables
}
