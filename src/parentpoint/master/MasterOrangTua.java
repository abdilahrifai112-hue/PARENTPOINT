package parentpoint.master;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import parentpoint.koneksi.koneksi;
import parentpoint.util.DesignUtil;

public class MasterOrangTua extends JFrame {

    private int selectedId = -1;

    public MasterOrangTua() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        styleComponents();
        loadData();
        loadSiswa();
    }

    private void styleComponents() {
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setTitle("PARENT POINT - Master Data Orang Tua");
        setLocationRelativeTo(null);
        getContentPane().setBackground(DesignUtil.BG_MAIN);
        
        // Panels styling
        jPanelHeader.setBackground(DesignUtil.PRIMARY);
        jPanelMain.setBackground(DesignUtil.BG_MAIN);
        jPanelForm.setBackground(DesignUtil.BG_CARD);
        jPanelFields.setBackground(DesignUtil.BG_CARD);
        jPanelFieldSiswa.setBackground(DesignUtil.BG_CARD);
        jPanelFieldNamaOrtu.setBackground(DesignUtil.BG_CARD);
        jPanelFieldNoTelp.setBackground(DesignUtil.BG_CARD);
        jPanelFieldEmail.setBackground(DesignUtil.BG_CARD);
        jPanelFieldAlamat.setBackground(DesignUtil.BG_CARD);
        jPanelFieldBlank.setBackground(DesignUtil.BG_CARD);
        jPanelButtons.setBackground(DesignUtil.BG_CARD);
        
        // Labels styling
        jLabelTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jLabelTitle.setForeground(Color.WHITE);
        jLabelSiswa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabelSiswa.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabelNamaOrtu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabelNamaOrtu.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabelNoTelp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabelNoTelp.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabelEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabelEmail.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabelAlamat.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabelAlamat.setForeground(DesignUtil.TEXT_PRIMARY);
        
        // Inputs styling
        JTextField[] fields = {tfNamaOrtu, tfNoTelp, tfEmail, tfAlamat};
        for (JTextField tf : fields) {
            tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }
        
        // JComboBox styling
        cbSiswa.setBackground(Color.WHITE);
        
        // Table styling
        DesignUtil.styleTable(jTable1);
        jScrollPane1.getViewport().setBackground(Color.WHITE);
        
        // Buttons styling with BasicButtonUI
        JButton[] buttons = {btnSimpan, btnUpdate, btnHapus, btnBatal};
        Color[] bgColors = {DesignUtil.SUCCESS, DesignUtil.ACCENT, DesignUtil.DANGER, DesignUtil.WARNING};
        String[] btnLabels = {"Simpan", "Update", "Hapus", "Batal"};
        
        for (int i = 0; i < buttons.length; i++) {
            JButton btn = buttons[i];
            btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            btn.setText(btnLabels[i]);
            btn.setBackground(bgColors[i]);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new java.awt.Dimension(110, 35));
            
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
        
        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);
        
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() >= 0) {
                int row = jTable1.getSelectedRow();
                selectedId = Integer.parseInt(jTable1.getValueAt(row, 0).toString());
                String siswaName = jTable1.getValueAt(row, 1).toString();
                for (int i = 0; i < cbSiswa.getItemCount(); i++) {
                    if (cbSiswa.getItemAt(i).contains(siswaName)) {
                        cbSiswa.setSelectedIndex(i);
                        break;
                    }
                }
                tfNamaOrtu.setText(jTable1.getValueAt(row, 2).toString());
                tfNoTelp.setText(jTable1.getValueAt(row, 3) != null ? jTable1.getValueAt(row, 3).toString() : "");
                tfEmail.setText(jTable1.getValueAt(row, 4) != null ? jTable1.getValueAt(row, 4).toString() : "");
                tfAlamat.setText(jTable1.getValueAt(row, 5) != null ? jTable1.getValueAt(row, 5).toString() : "");
                btnUpdate.setEnabled(true);
                btnHapus.setEnabled(true);
                btnSimpan.setEnabled(false);
            }
        });
// setSize removed for responsiveness
        // ── Tombol Kembali ke Dashboard ──
        addKembaliButton();
    }

    private void addKembaliButton() {
        jPanelHeader.setLayout(new java.awt.BorderLayout());
        javax.swing.JButton btnKembali = new javax.swing.JButton("⬅  Kembali");
        btnKembali.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnKembali.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        btnKembali.setBackground(new java.awt.Color(71, 85, 105));
        btnKembali.setForeground(java.awt.Color.WHITE);
        btnKembali.setFocusPainted(false);
        btnKembali.setBorderPainted(false);
        btnKembali.setOpaque(true);
        btnKembali.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKembali.setPreferredSize(new java.awt.Dimension(130, 40));
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
        jPanelHeader.add(btnKembali, java.awt.BorderLayout.EAST);
        jPanelHeader.revalidate();
        jPanelHeader.repaint();
        
        // RBAC: If guru, disable edit buttons
        if ("guru".equalsIgnoreCase(parentpoint.util.Session.getRole())) {
            btnSimpan.setVisible(false);
            btnUpdate.setVisible(false);
            btnHapus.setVisible(false);
            btnBatal.setVisible(false);
            cbSiswa.setEnabled(false);
            tfNamaOrtu.setEnabled(false);
            tfNoTelp.setEnabled(false);
            tfEmail.setEnabled(false);
            tfAlamat.setEnabled(false);
            jPanelForm.setVisible(false); // Hide form
        }
    }

    private void loadSiswa() {
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                cbSiswa.removeAllItems();
                ResultSet rs = conn.createStatement().executeQuery("SELECT id, nis, nama FROM siswa ORDER BY nama");
                while (rs.next()) {
                    cbSiswa.addItem(rs.getInt("id") + " - " + rs.getString("nis") + " - " + rs.getString("nama"));
                }
                rs.close(); conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void loadData() {
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);
                
                String sql = "SELECT o.id, s.nama AS siswa, o.nama_ortu, o.no_telp, o.email, o.alamat " +
                             "FROM orang_tua o JOIN siswa s ON o.siswa_id = s.id ";
                             
                if ("guru".equalsIgnoreCase(parentpoint.util.Session.getRole())) {
                    String g = parentpoint.util.Session.getGuruNama();
                    sql += " LEFT JOIN kelas k ON s.kelas_id = k.id ";
                    sql += " LEFT JOIN jadwal_kelas j ON j.kelas_id = k.id ";
                    sql += " WHERE k.wali_kelas = '" + g + "' OR j.guru = '" + g + "' ";
                    sql += " GROUP BY o.id, s.nama, o.nama_ortu, o.no_telp, o.email, o.alamat ";
                }
                
                sql += " ORDER BY s.nama";
                
                ResultSet rs = conn.createStatement().executeQuery(sql);
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("siswa"), rs.getString("nama_ortu"),
                        rs.getString("no_telp"), rs.getString("email"), rs.getString("alamat")
                    });
                }
                rs.close(); conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void simpan() {
        if (tfNamaOrtu.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Orang Tua harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cbSiswa.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan data siswa terlebih dahulu di modul Siswa!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO orang_tua (siswa_id, nama_ortu, no_telp, email, alamat) VALUES (?,?,?,?,?)");
                ps.setInt(1, getSelectedSiswaId());
                ps.setString(2, tfNamaOrtu.getText().trim());
                ps.setString(3, tfNoTelp.getText().trim());
                ps.setString(4, tfEmail.getText().trim());
                ps.setString(5, tfAlamat.getText().trim());
                ps.executeUpdate(); ps.close(); conn.close();
                JOptionPane.showMessageDialog(this, "Data orang tua berhasil disimpan!");
                batal(); loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void update() {
        if (selectedId < 0) return;
        if (cbSiswa.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan data siswa terlebih dahulu di modul Siswa!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE orang_tua SET siswa_id=?, nama_ortu=?, no_telp=?, email=?, alamat=? WHERE id=?");
                ps.setInt(1, getSelectedSiswaId());
                ps.setString(2, tfNamaOrtu.getText().trim());
                ps.setString(3, tfNoTelp.getText().trim());
                ps.setString(4, tfEmail.getText().trim());
                ps.setString(5, tfAlamat.getText().trim());
                ps.setInt(6, selectedId);
                ps.executeUpdate(); ps.close(); conn.close();
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
                batal(); loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void hapus() {
        if (selectedId < 0) return;
        if (JOptionPane.showConfirmDialog(this, "Yakin hapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Connection conn = koneksi.getConnection();
            if (conn != null) {
                try {
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM orang_tua WHERE id=?");
                    ps.setInt(1, selectedId);
                    ps.executeUpdate(); ps.close(); conn.close();
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                    batal(); loadData();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void batal() {
        tfNamaOrtu.setText(""); tfNoTelp.setText(""); tfEmail.setText(""); tfAlamat.setText("");
        if (cbSiswa.getItemCount() > 0) cbSiswa.setSelectedIndex(0);
        selectedId = -1; jTable1.clearSelection();
        btnSimpan.setEnabled(true); btnUpdate.setEnabled(false); btnHapus.setEnabled(false);
    }

    private int getSelectedSiswaId() {
        String sel = (String) cbSiswa.getSelectedItem();
        if (sel != null && sel.contains(" - ")) {
            return Integer.parseInt(sel.split(" - ")[0].trim());
        }
        return 1;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelHeader = new javax.swing.JPanel();
        jLabelTitle = new javax.swing.JLabel();
        jPanelMain = new javax.swing.JPanel();
        jPanelForm = new javax.swing.JPanel();
        jPanelFields = new javax.swing.JPanel();
        jPanelFieldSiswa = new javax.swing.JPanel();
        jLabelSiswa = new javax.swing.JLabel();
        cbSiswa = new javax.swing.JComboBox<>();
        jPanelFieldNamaOrtu = new javax.swing.JPanel();
        jLabelNamaOrtu = new javax.swing.JLabel();
        tfNamaOrtu = new javax.swing.JTextField();
        jPanelFieldNoTelp = new javax.swing.JPanel();
        jLabelNoTelp = new javax.swing.JLabel();
        tfNoTelp = new javax.swing.JTextField();
        jPanelFieldEmail = new javax.swing.JPanel();
        jLabelEmail = new javax.swing.JLabel();
        tfEmail = new javax.swing.JTextField();
        jPanelFieldAlamat = new javax.swing.JPanel();
        jLabelAlamat = new javax.swing.JLabel();
        tfAlamat = new javax.swing.JTextField();
        jPanelFieldBlank = new javax.swing.JPanel();
        jPanelButtons = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Siswa", "Nama Orang Tua", "No. Telp", "Email", "Alamat"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PARENT POINT - Master Data Orang Tua");

        jPanelHeader.setBackground(new java.awt.Color(25, 55, 109));
        jPanelHeader.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 15));

        jLabelTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelTitle.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitle.setText("👪  Master Data Orang Tua");
        jPanelHeader.add(jLabelTitle);

        getContentPane().add(jPanelHeader, java.awt.BorderLayout.NORTH);

        jPanelMain.setBackground(new java.awt.Color(236, 240, 245));
        jPanelMain.setLayout(new java.awt.BorderLayout(0, 15));

        jPanelForm.setBackground(new java.awt.Color(255, 255, 255));
        jPanelForm.setLayout(new java.awt.BorderLayout(0, 10));

        jPanelFields.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFields.setLayout(new java.awt.GridLayout(3, 2, 15, 8));

        jPanelFieldSiswa.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFieldSiswa.setLayout(new java.awt.BorderLayout(0, 4));

        jLabelSiswa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelSiswa.setText("Siswa :");
        jPanelFieldSiswa.add(jLabelSiswa, java.awt.BorderLayout.NORTH);

        cbSiswa.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanelFieldSiswa.add(cbSiswa, java.awt.BorderLayout.CENTER);

        jPanelFields.add(jPanelFieldSiswa);

        jPanelFieldNamaOrtu.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFieldNamaOrtu.setLayout(new java.awt.BorderLayout(0, 4));

        jLabelNamaOrtu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelNamaOrtu.setText("Nama Orang Tua :");
        jPanelFieldNamaOrtu.add(jLabelNamaOrtu, java.awt.BorderLayout.NORTH);

        tfNamaOrtu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanelFieldNamaOrtu.add(tfNamaOrtu, java.awt.BorderLayout.CENTER);

        jPanelFields.add(jPanelFieldNamaOrtu);

        jPanelFieldNoTelp.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFieldNoTelp.setLayout(new java.awt.BorderLayout(0, 4));

        jLabelNoTelp.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelNoTelp.setText("No. Telepon :");
        jPanelFieldNoTelp.add(jLabelNoTelp, java.awt.BorderLayout.NORTH);

        tfNoTelp.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanelFieldNoTelp.add(tfNoTelp, java.awt.BorderLayout.CENTER);

        jPanelFields.add(jPanelFieldNoTelp);

        jPanelFieldEmail.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFieldEmail.setLayout(new java.awt.BorderLayout(0, 4));

        jLabelEmail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelEmail.setText("Email :");
        jPanelFieldEmail.add(jLabelEmail, java.awt.BorderLayout.NORTH);

        tfEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanelFieldEmail.add(tfEmail, java.awt.BorderLayout.CENTER);

        jPanelFields.add(jPanelFieldEmail);

        jPanelFieldAlamat.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFieldAlamat.setLayout(new java.awt.BorderLayout(0, 4));

        jLabelAlamat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelAlamat.setText("Alamat :");
        jPanelFieldAlamat.add(jLabelAlamat, java.awt.BorderLayout.NORTH);

        tfAlamat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanelFieldAlamat.add(tfAlamat, java.awt.BorderLayout.CENTER);

        jPanelFields.add(jPanelFieldAlamat);

        jPanelFieldBlank.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFields.add(jPanelFieldBlank);

        jPanelForm.add(jPanelFields, java.awt.BorderLayout.CENTER);

        jPanelButtons.setBackground(new java.awt.Color(255, 255, 255));
        jPanelButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 5));

        btnSimpan.setBackground(new java.awt.Color(39, 174, 96));
        btnSimpan.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan");
        btnSimpan.setContentAreaFilled(false);
        btnSimpan.setOpaque(true);
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });
        jPanelButtons.add(btnSimpan);

        btnUpdate.setBackground(new java.awt.Color(52, 152, 219));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setText("Update");
        btnUpdate.setContentAreaFilled(false);
        btnUpdate.setOpaque(true);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        jPanelButtons.add(btnUpdate);

        btnHapus.setBackground(new java.awt.Color(192, 57, 43));
        btnHapus.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.setToolTipText("");
        btnHapus.setContentAreaFilled(false);
        btnHapus.setOpaque(true);
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });
        jPanelButtons.add(btnHapus);

        btnBatal.setBackground(new java.awt.Color(243, 162, 58));
        btnBatal.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnBatal.setForeground(new java.awt.Color(255, 255, 255));
        btnBatal.setText("Batal");
        btnBatal.setContentAreaFilled(false);
        btnBatal.setOpaque(true);
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        jPanelButtons.add(btnBatal);

        jPanelForm.add(jPanelButtons, java.awt.BorderLayout.SOUTH);

        jPanelMain.add(jPanelForm, java.awt.BorderLayout.NORTH);
        jPanelMain.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanelMain, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        simpan();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        hapus();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        batal();
    }//GEN-LAST:event_btnBatalActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new MasterOrangTua().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cbSiswa;
    private javax.swing.JLabel jLabelAlamat;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelNamaOrtu;
    private javax.swing.JLabel jLabelNoTelp;
    private javax.swing.JLabel jLabelSiswa;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelFieldAlamat;
    private javax.swing.JPanel jPanelFieldBlank;
    private javax.swing.JPanel jPanelFieldEmail;
    private javax.swing.JPanel jPanelFieldNamaOrtu;
    private javax.swing.JPanel jPanelFieldNoTelp;
    private javax.swing.JPanel jPanelFieldSiswa;
    private javax.swing.JPanel jPanelFields;
    private javax.swing.JPanel jPanelForm;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tfAlamat;
    private javax.swing.JTextField tfEmail;
    private javax.swing.JTextField tfNamaOrtu;
    private javax.swing.JTextField tfNoTelp;
    // End of variables declaration//GEN-END:variables
}

