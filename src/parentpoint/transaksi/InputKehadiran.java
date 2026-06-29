package parentpoint.transaksi;

import java.awt.Component;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import parentpoint.koneksi.koneksi;
import parentpoint.util.DesignUtil;

public class InputKehadiran extends JFrame {

    public InputKehadiran() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        styleComponents();
        loadKelas();
        tfTanggal.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelFilter = new javax.swing.JPanel();
        jLabelKelas = new javax.swing.JLabel();
        cbKelas = new javax.swing.JComboBox<>();
        jLabelTanggal = new javax.swing.JLabel();
        tfTanggal = new javax.swing.JTextField();
        btnLoad = new javax.swing.JButton();
        btnHadirSemua = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanelActions = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PARENT POINT - Input Kehadiran Siswa");

        jPanelFilter.setBackground(new java.awt.Color(236, 240, 245));
        jPanelFilter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 0));

        jLabelKelas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelKelas.setText("Kelas :");
        jPanelFilter.add(jLabelKelas);

        cbKelas.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanelFilter.add(cbKelas);

        jLabelTanggal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabelTanggal.setText("Tanggal :");
        jPanelFilter.add(jLabelTanggal);

        tfTanggal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tfTanggal.setMargin(new java.awt.Insets(2, 100, 2, 2));
        jPanelFilter.add(tfTanggal);

        btnLoad.setContentAreaFilled(false);
        btnLoad.setOpaque(true);
        btnLoad.setBackground(new java.awt.Color(52, 152, 219));
        btnLoad.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnLoad.setForeground(new java.awt.Color(255, 255, 255));
        btnLoad.setText("📋 Load Siswa");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });
        jPanelFilter.add(btnLoad);

        btnHadirSemua.setContentAreaFilled(false);
        btnHadirSemua.setOpaque(true);
        btnHadirSemua.setBackground(new java.awt.Color(39, 174, 96));
        btnHadirSemua.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnHadirSemua.setForeground(new java.awt.Color(255, 255, 255));
        btnHadirSemua.setText("✅ Hadir Semua");
        btnHadirSemua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHadirSemuaActionPerformed(evt);
            }
        });
        jPanelFilter.add(btnHadirSemua);

        getContentPane().add(jPanelFilter, java.awt.BorderLayout.NORTH);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Siswa", "NIS", "Nama Siswa", "Status Kehadiran", "Keterangan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMaxWidth(70);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(100);
        }

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanelActions.setBackground(new java.awt.Color(236, 240, 245));
        jPanelActions.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));

        btnSimpan.setContentAreaFilled(false);
        btnSimpan.setOpaque(true);
        btnSimpan.setBackground(new java.awt.Color(39, 174, 96));
        btnSimpan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("💾 Simpan Kehadiran");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        jPanelActions.add(btnSimpan);

        getContentPane().add(jPanelActions, java.awt.BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed
        loadSiswa();
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnHadirSemuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHadirSemuaActionPerformed
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt("Hadir", i, 3);
        }
    }//GEN-LAST:event_btnHadirSemuaActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        simpanKehadiran();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void loadKelas() {
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                cbKelas.removeAllItems();
                
                String sql = "SELECT id, nama_kelas FROM kelas ";
                if ("guru".equalsIgnoreCase(parentpoint.util.Session.getRole())) {
                    String g = parentpoint.util.Session.getGuruNama();
                    sql = "SELECT DISTINCT k.id, k.nama_kelas FROM kelas k " +
                          "LEFT JOIN jadwal_kelas j ON j.kelas_id = k.id " +
                          "WHERE k.wali_kelas = '" + g + "' OR j.guru = '" + g + "' ";
                }
                sql += " ORDER BY nama_kelas";
                
                ResultSet rs = conn.createStatement().executeQuery(sql);
                while (rs.next()) {
                    cbKelas.addItem(rs.getInt("id") + " - " + rs.getString("nama_kelas"));
                }
                rs.close(); conn.close();
            } catch (SQLException e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
        }
    }

    private void loadSiswa() {
        String sel = (String) cbKelas.getSelectedItem();
        if (sel == null) return;
        int kelasId = Integer.parseInt(sel.split(" - ")[0].trim());
        String tanggal = tfTanggal.getText().trim();

        if (tanggal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                PreparedStatement psCheck = conn.prepareStatement(
                    "SELECT s.id, s.nis, s.nama, h.status, h.keterangan " +
                    "FROM siswa s LEFT JOIN kehadiran h ON s.id = h.siswa_id AND h.tanggal = ? " +
                    "WHERE s.kelas_id = ? ORDER BY s.nama");
                psCheck.setString(1, tanggal);
                psCheck.setInt(2, kelasId);
                ResultSet rs = psCheck.executeQuery();

                while (rs.next()) {
                    String status = rs.getString("status");
                    String ket = rs.getString("keterangan");
                    model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nis"),
                        rs.getString("nama"),
                        status != null ? status : "Hadir",
                        ket != null ? ket : ""
                    });
                }
                rs.close(); psCheck.close(); conn.close();

                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Tidak ada siswa di kelas ini.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void simpanKehadiran() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Load data siswa terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (jTable1.isEditing()) jTable1.getCellEditor().stopCellEditing();

        String tanggal = tfTanggal.getText().trim();
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                conn.setAutoCommit(false);

                for (int i = 0; i < model.getRowCount(); i++) {
                    int siswaId = Integer.parseInt(model.getValueAt(i, 0).toString());
                    String status = model.getValueAt(i, 3).toString();
                    String keterangan = model.getValueAt(i, 4) != null ? model.getValueAt(i, 4).toString() : "";

                    PreparedStatement psCheck = conn.prepareStatement(
                        "SELECT id FROM kehadiran WHERE siswa_id = ? AND tanggal = ?");
                    psCheck.setInt(1, siswaId);
                    psCheck.setString(2, tanggal);
                    ResultSet rs = psCheck.executeQuery();

                    if (rs.next()) {
                        PreparedStatement psUpdate = conn.prepareStatement(
                            "UPDATE kehadiran SET status=?, keterangan=? WHERE siswa_id=? AND tanggal=?");
                        psUpdate.setString(1, status);
                        psUpdate.setString(2, keterangan);
                        psUpdate.setInt(3, siswaId);
                        psUpdate.setString(4, tanggal);
                        psUpdate.executeUpdate();
                        psUpdate.close();
                    } else {
                        PreparedStatement psInsert = conn.prepareStatement(
                            "INSERT INTO kehadiran (siswa_id, tanggal, status, keterangan) VALUES (?,?,?,?)");
                        psInsert.setInt(1, siswaId);
                        psInsert.setString(2, tanggal);
                        psInsert.setString(3, status);
                        psInsert.setString(4, keterangan);
                        psInsert.executeUpdate();
                        psInsert.close();
                    }
                    rs.close(); psCheck.close();
                }

                conn.commit();
                conn.close();

                JOptionPane.showMessageDialog(this,
                    "Data kehadiran berhasil disimpan!\n" +
                    "Total: " + model.getRowCount() + " siswa",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException e) {
                try { conn.rollback(); } catch (SQLException ex) {}
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void styleComponents() {
        // Apply BasicButtonUI for cross-platform visual consistency
        JButton[] buttons = {btnLoad, btnHadirSemua, btnSimpan};
        Color[] bgColors = {DesignUtil.PRIMARY, DesignUtil.SUCCESS, DesignUtil.SUCCESS};
        String[] btnLabels = {"Load Siswa", "Hadir Semua", "Simpan Kehadiran"};
        Dimension[] btnSizes = {new Dimension(130, 35), new Dimension(130, 35), new Dimension(160, 35)};
        Font[] btnFonts = {
            new Font("Segoe UI", Font.BOLD, 13),
            new Font("Segoe UI", Font.BOLD, 13),
            new Font("Segoe UI", Font.BOLD, 14)
        };
        
        for (int i = 0; i < buttons.length; i++) {
            JButton btn = buttons[i];
            btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            btn.setText(btnLabels[i]);
            btn.setBackground(bgColors[i]);
            btn.setForeground(Color.WHITE);
            btn.setFont(btnFonts[i]);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
            btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            btn.setPreferredSize(btnSizes[i]);
            
            final Color normalColor = bgColors[i];
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    btn.setBackground(normalColor.darker());
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    btn.setBackground(normalColor);
                }
            });
        }

        // Style Table
        DesignUtil.styleTable(jTable1);
        jTable1.setRowHeight(40);
        jScrollPane1.getViewport().setBackground(Color.WHITE);

        // Setup combobox cell editor for Column 3 (Status Kehadiran)
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Hadir", "Sakit", "Izin", "Alpha"});
        cbStatus.setFont(DesignUtil.FONT_TABLE);
        jTable1.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(cbStatus));

        // Setup custom cell renderer for Column 3 status coloring
        jTable1.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if (!isSelected && value != null) {
                    switch (value.toString()) {
                        case "Hadir": c.setBackground(new Color(212, 245, 212)); c.setForeground(new Color(30, 130, 76)); break;
                        case "Sakit": c.setBackground(new Color(252, 243, 207)); c.setForeground(new Color(183, 149, 11)); break;
                        case "Izin": c.setBackground(new Color(214, 234, 248)); c.setForeground(new Color(41, 128, 185)); break;
                        case "Alpha": c.setBackground(new Color(250, 219, 216)); c.setForeground(new Color(192, 57, 43)); break;
                        default: c.setBackground(Color.WHITE); c.setForeground(DesignUtil.TEXT_PRIMARY);
                    }
                }
                setHorizontalAlignment(CENTER);
                setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                return c;
            }
        });

        // Set dimensions to prevent cutoffs
        setSize(900, 650);
        setLocationRelativeTo(null);

        // ── Tombol Kembali ke Dashboard ──
        addKembaliButton();
    }

    private void addKembaliButton() {
        // Cari panel header (jPanelFilter biasanya di NORTH), tambahkan tombol Kembali di jPanelActions
        JButton btnKembali = new JButton("⬅  Kembali ke Dashboard");
        btnKembali.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnKembali.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnKembali.setBackground(new Color(71, 85, 105));
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setFocusPainted(false);
        btnKembali.setBorderPainted(false);
        btnKembali.setOpaque(true);
        btnKembali.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKembali.setPreferredSize(new java.awt.Dimension(195, 35));
        btnKembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btnKembali.setBackground(new Color(51, 65, 85)); }
            public void mouseExited(java.awt.event.MouseEvent e) { btnKembali.setBackground(new Color(71, 85, 105)); }
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
        jPanelActions.add(btnKembali, java.awt.BorderLayout.EAST);
        jPanelActions.revalidate();
        jPanelActions.repaint();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {}
        java.awt.EventQueue.invokeLater(() -> new InputKehadiran().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHadirSemua;
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cbKelas;
    private javax.swing.JLabel jLabelKelas;
    private javax.swing.JLabel jLabelTanggal;
    private javax.swing.JPanel jPanelActions;
    private javax.swing.JPanel jPanelFilter;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tfTanggal;
    // End of variables declaration//GEN-END:variables
}

