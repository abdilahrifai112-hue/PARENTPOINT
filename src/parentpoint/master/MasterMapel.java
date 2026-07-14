package parentpoint.master;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import parentpoint.koneksi.koneksi;
import parentpoint.util.DesignUtil;

public class MasterMapel extends JFrame {

    private int selectedId = -1;

    public MasterMapel() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        styleComponents();
        loadData();
    }

    private void styleComponents() {
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setTitle("PARENT POINT - Master Data Mata Pelajaran");
        setLocationRelativeTo(null);
        getContentPane().setBackground(DesignUtil.BG_MAIN);
        
        // Panels styling
        jPanelHeader.setBackground(DesignUtil.PRIMARY);
        jPanelMain.setBackground(DesignUtil.BG_MAIN);
        jPanelForm.setBackground(DesignUtil.BG_CARD);
        jPanelFields.setBackground(DesignUtil.BG_CARD);
        jPanelField1.setBackground(DesignUtil.BG_CARD);
        jPanelField2.setBackground(DesignUtil.BG_CARD);
        jPanelButtons.setBackground(DesignUtil.BG_CARD);
        
        // Labels styling
        jLabelTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jLabelTitle.setForeground(Color.WHITE);
        jLabelNamaMapel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabelNamaMapel.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabelKeterangan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabelKeterangan.setForeground(DesignUtil.TEXT_PRIMARY);
        
        // Inputs styling
        tfNamaMapel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        tfKeterangan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
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
                tfNamaMapel.setText(jTable1.getValueAt(row, 1).toString());
                tfKeterangan.setText(jTable1.getValueAt(row, 2) != null ? jTable1.getValueAt(row, 2).toString() : "");
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
        JButton btnKembali = new JButton("⬅  Kembali");
        btnKembali.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnKembali.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnKembali.setBackground(new Color(71, 85, 105));
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setFocusPainted(false);
        btnKembali.setBorderPainted(false);
        btnKembali.setOpaque(true);
        btnKembali.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKembali.setPreferredSize(new java.awt.Dimension(130, 40));
        btnKembali.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
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
        jPanelHeader.add(btnKembali, java.awt.BorderLayout.EAST);
        jPanelHeader.revalidate();
        jPanelHeader.repaint();
    }

    private void loadData() {
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(
                    "SELECT id, nama_mapel, '' AS keterangan, 0 AS jml FROM mata_pelajaran ORDER BY nama_mapel");
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("nama_mapel"),
                        rs.getString("keterangan"), rs.getInt("jml")
                    });
                }
                rs.close(); st.close(); conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void simpan() {
        if (tfNamaMapel.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Mapel harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO mata_pelajaran (nama_mapel) VALUES (?)");
                ps.setString(1, tfNamaMapel.getText().trim());
                ps.executeUpdate(); ps.close(); conn.close();
                JOptionPane.showMessageDialog(this, "Data kelas berhasil disimpan!");
                batal(); loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void update() {
        if (selectedId < 0) return;
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement("UPDATE mata_pelajaran SET nama_mapel=? WHERE id=?");
                ps.setString(1, tfNamaMapel.getText().trim());
                ps.setInt(2, selectedId);
                ps.executeUpdate(); ps.close(); conn.close();
                JOptionPane.showMessageDialog(this, "Data kelas berhasil diupdate!");
                batal(); loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void hapus() {
        if (selectedId < 0) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus kelas ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = koneksi.getConnection();
            if (conn != null) {
                try {
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM mata_pelajaran WHERE id=?");
                    ps.setInt(1, selectedId);
                    ps.executeUpdate(); ps.close(); conn.close();
                    JOptionPane.showMessageDialog(this, "Data kelas berhasil dihapus!");
                    batal(); loadData();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage() +
                        "\nPastikan tidak ada siswa di kelas ini.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void batal() {
        tfNamaMapel.setText(""); tfKeterangan.setText("");
        selectedId = -1; jTable1.clearSelection();
        btnSimpan.setEnabled(true); btnUpdate.setEnabled(false); btnHapus.setEnabled(false);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelHeader = new javax.swing.JPanel();
        jLabelTitle = new javax.swing.JLabel();
        jPanelMain = new javax.swing.JPanel();
        jPanelForm = new javax.swing.JPanel();
        jPanelFields = new javax.swing.JPanel();
        jPanelField1 = new javax.swing.JPanel();
        jLabelNamaMapel = new javax.swing.JLabel();
        tfNamaMapel = new javax.swing.JTextField();
        jPanelField2 = new javax.swing.JPanel();
        jLabelKeterangan = new javax.swing.JLabel();
        tfKeterangan = new javax.swing.JTextField();
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
                "ID", "Nama Mapel", "Keterangan", "Jumlah Siswa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PARENT POINT - Master Data Mata Pelajaran");

        jPanelHeader.setBackground(new java.awt.Color(25, 55, 109));
        jPanelHeader.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 15));

        jLabelTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelTitle.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitle.setText("◬️  Master Data Mata Pelajaran");
        jPanelHeader.add(jLabelTitle);

        getContentPane().add(jPanelHeader, java.awt.BorderLayout.NORTH);

        jPanelMain.setBackground(new java.awt.Color(236, 240, 245));
        jPanelMain.setLayout(new java.awt.BorderLayout(0, 15));

        jPanelForm.setBackground(new java.awt.Color(255, 255, 255));
        jPanelForm.setLayout(new java.awt.BorderLayout(0, 10));

        jPanelFields.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFields.setLayout(new java.awt.GridLayout(2, 2, 15, 8));

        jPanelField1.setBackground(new java.awt.Color(255, 255, 255));
        jPanelField1.setLayout(new java.awt.BorderLayout(0, 4));

        jLabelNamaMapel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelNamaMapel.setText("Nama Mapel :");
        jPanelField1.add(jLabelNamaMapel, java.awt.BorderLayout.NORTH);

        tfNamaMapel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanelField1.add(tfNamaMapel, java.awt.BorderLayout.CENTER);

        jPanelFields.add(jPanelField1);

        jPanelField2.setBackground(new java.awt.Color(255, 255, 255));
        jPanelField2.setLayout(new java.awt.BorderLayout(0, 4));

        jLabelKeterangan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelKeterangan.setText("Keterangan :");
        jPanelField2.add(jLabelKeterangan, java.awt.BorderLayout.NORTH);

        tfKeterangan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanelField2.add(tfKeterangan, java.awt.BorderLayout.CENTER);

        jPanelFields.add(jPanelField2);

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

        btnHapus.setBackground(new java.awt.Color(192, 43, 43));
        btnHapus.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
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
        java.awt.EventQueue.invokeLater(() -> new MasterMapel().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabelNamaMapel;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelKeterangan;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelField1;
    private javax.swing.JPanel jPanelField2;
    private javax.swing.JPanel jPanelFields;
    private javax.swing.JPanel jPanelForm;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tfNamaMapel;
    private javax.swing.JTextField tfKeterangan;
    // End of variables declaration//GEN-END:variables
}

