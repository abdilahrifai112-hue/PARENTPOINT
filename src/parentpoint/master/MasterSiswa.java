package parentpoint.master;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import parentpoint.koneksi.koneksi;
import parentpoint.util.DesignUtil;

public class MasterSiswa extends JFrame {

    private int selectedId = -1;

    public MasterSiswa() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        styleComponents();
        loadData();
        loadKelas();
    }

    private void styleComponents() {
        if (tfCari != null) {
            tfCari.setPreferredSize(new java.awt.Dimension(250, 30));
        }
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setTitle("PARENT POINT - Master Data Siswa");
        setLocationRelativeTo(null);
        getContentPane().setBackground(DesignUtil.BG_MAIN);
        
        // Panels styling
        jPanelHeader.setBackground(DesignUtil.PRIMARY);
        jPanelMain.setBackground(DesignUtil.BG_MAIN);
        jPanelForm.setBackground(DesignUtil.BG_CARD);
        jPanelFields.setBackground(DesignUtil.BG_CARD);
        jPanelFieldNIS.setBackground(DesignUtil.BG_CARD);
        jPanelFieldNama.setBackground(DesignUtil.BG_CARD);
        jPanelFieldKelas.setBackground(DesignUtil.BG_CARD);
        jPanelFieldJK.setBackground(DesignUtil.BG_CARD);
        jPanelFieldAlamat.setBackground(DesignUtil.BG_CARD);
        jPanelFieldBlank.setBackground(DesignUtil.BG_CARD);
        jPanelButtons.setBackground(DesignUtil.BG_CARD);
        jPanelTable.setBackground(DesignUtil.BG_MAIN);
        jPanelSearch.setBackground(DesignUtil.BG_MAIN);
        
        // Labels styling
        jLabelTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jLabelTitle.setForeground(Color.WHITE);
        jLabelNIS.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabelNIS.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabelNama.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabelNama.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabelKelas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabelKelas.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabelJK.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabelJK.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabelAlamat.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jLabelAlamat.setForeground(DesignUtil.TEXT_PRIMARY);
        jLabelCari.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jLabelCari.setForeground(DesignUtil.TEXT_PRIMARY);
        
        // Inputs styling
        JTextField[] fields = {tfNIS, tfNama, tfAlamat, tfCari};
        for (JTextField tf : fields) {
            tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }
        
        // JComboBox styling
        cbKelas.setBackground(Color.WHITE);
        cbJK.setBackground(Color.WHITE);
        cbJK.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"Laki-laki", "Perempuan"}));
        
        // Table styling
        DesignUtil.styleTable(jTable1);
        jScrollPane1.getViewport().setBackground(Color.WHITE);
        
        // Buttons styling with BasicButtonUI
        JButton[] buttons = {btnSimpan, btnUpdate, btnHapus, btnBatal, btnCari};
        Color[] bgColors = {DesignUtil.SUCCESS, DesignUtil.ACCENT, DesignUtil.DANGER, DesignUtil.WARNING, DesignUtil.PRIMARY};
        String[] btnLabels = {"Simpan", "Update", "Hapus", "Batal", "Cari"};
        
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
            
            if (i == 4) { // btnCari
                btn.setPreferredSize(new Dimension(80, 35));
            } else {
                btn.setPreferredSize(new Dimension(110, 35));
            }
            
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
                tfNIS.setText(jTable1.getValueAt(row, 1).toString());
                tfNama.setText(jTable1.getValueAt(row, 2).toString());
                String kelasName = jTable1.getValueAt(row, 3).toString();
                for (int i = 0; i < cbKelas.getItemCount(); i++) {
                    if (cbKelas.getItemAt(i).contains(kelasName)) {
                        cbKelas.setSelectedIndex(i);
                        break;
                    }
                }
                cbJK.setSelectedIndex(jTable1.getValueAt(row, 4).toString().equals("L") ? 0 : 1);
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
            tfNIS.setEnabled(false);
            tfNama.setEnabled(false);
            cbKelas.setEnabled(false);
            cbJK.setEnabled(false);
            tfAlamat.setEnabled(false);
            jPanelForm.setVisible(false); // Hide the whole form to save space and show only table
        }
    }

    private void loadKelas() {
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                cbKelas.removeAllItems();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT id, nama_kelas FROM kelas ORDER BY nama_kelas");
                while (rs.next()) {
                    cbKelas.addItem(rs.getInt("id") + " - " + rs.getString("nama_kelas"));
                }
                rs.close(); st.close(); conn.close();
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
                Statement st = conn.createStatement();
                
                String sql = "SELECT s.id, s.nis, s.nama, IFNULL(k.nama_kelas,'-') AS kelas, s.jenis_kelamin, s.alamat " +
                    "FROM siswa s LEFT JOIN kelas k ON s.kelas_id = k.id ";
                    
                if ("guru".equalsIgnoreCase(parentpoint.util.Session.getRole())) {
                    String g = parentpoint.util.Session.getGuruNama();
                    sql += " LEFT JOIN jadwal_kelas j ON j.kelas_id = k.id ";
                    sql += " WHERE k.wali_kelas = '" + g + "' OR j.guru = '" + g + "' ";
                    // group by s.id to avoid duplicate rows
                    sql += " GROUP BY s.id, s.nis, s.nama, kelas, s.jenis_kelamin, s.alamat ";
                }
                
                sql += " ORDER BY s.nama";
                
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("nis"), rs.getString("nama"),
                        rs.getString("kelas"), rs.getString("jenis_kelamin"), rs.getString("alamat")
                    });
                }
                rs.close(); st.close(); conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void simpan() {
        if (tfNIS.getText().trim().isEmpty() || tfNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIS dan Nama harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cbKelas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan data kelas terlebih dahulu di modul Kelas!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO siswa (nis, nama, kelas_id, jenis_kelamin, alamat) VALUES (?,?,?,?,?)");
                ps.setString(1, tfNIS.getText().trim());
                ps.setString(2, tfNama.getText().trim());
                ps.setInt(3, getSelectedKelasId());
                ps.setString(4, cbJK.getSelectedIndex() == 0 ? "L" : "P");
                ps.setString(5, tfAlamat.getText().trim());
                ps.executeUpdate();
                ps.close(); conn.close();
                JOptionPane.showMessageDialog(this, "Data siswa berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                batal();
                loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void update() {
        if (selectedId < 0) return;
        if (cbKelas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan data kelas terlebih dahulu di modul Kelas!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE siswa SET nis=?, nama=?, kelas_id=?, jenis_kelamin=?, alamat=? WHERE id=?");
                ps.setString(1, tfNIS.getText().trim());
                ps.setString(2, tfNama.getText().trim());
                ps.setInt(3, getSelectedKelasId());
                ps.setString(4, cbJK.getSelectedIndex() == 0 ? "L" : "P");
                ps.setString(5, tfAlamat.getText().trim());
                ps.setInt(6, selectedId);
                ps.executeUpdate();
                ps.close(); conn.close();
                JOptionPane.showMessageDialog(this, "Data siswa berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                batal();
                loadData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void hapus() {
        if (selectedId < 0) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data siswa ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = koneksi.getConnection();
            if (conn != null) {
                try {
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM siswa WHERE id=?");
                    ps.setInt(1, selectedId);
                    ps.executeUpdate();
                    ps.close(); conn.close();
                    JOptionPane.showMessageDialog(this, "Data siswa berhasil dihapus!");
                    batal();
                    loadData();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage() +
                        "\nPastikan data kehadiran & orang tua siswa ini sudah dihapus.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void batal() {
        tfNIS.setText(""); tfNama.setText(""); tfAlamat.setText("");
        if (cbKelas.getItemCount() > 0) cbKelas.setSelectedIndex(0);
        cbJK.setSelectedIndex(0);
        selectedId = -1;
        jTable1.clearSelection();
        btnSimpan.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    private void cariData() {
        String keyword = tfCari.getText().trim();
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);
                PreparedStatement ps = conn.prepareStatement(
                    "SELECT s.id, s.nis, s.nama, IFNULL(k.nama_kelas,'-') AS kelas, s.jenis_kelamin, s.alamat " +
                    "FROM siswa s LEFT JOIN kelas k ON s.kelas_id = k.id " +
                    "WHERE s.nis LIKE ? OR s.nama LIKE ? OR k.nama_kelas LIKE ? ORDER BY s.nama");
                String kw = "%" + keyword + "%";
                ps.setString(1, kw); ps.setString(2, kw); ps.setString(3, kw);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("id"), rs.getString("nis"), rs.getString("nama"),
                        rs.getString("kelas"), rs.getString("jenis_kelamin"), rs.getString("alamat")
                    });
                }
                rs.close(); ps.close(); conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private int getSelectedKelasId() {
        String sel = (String) cbKelas.getSelectedItem();
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
        jPanelFieldNIS = new javax.swing.JPanel();
        jLabelNIS = new javax.swing.JLabel();
        tfNIS = new javax.swing.JTextField();
        jPanelFieldNama = new javax.swing.JPanel();
        jLabelNama = new javax.swing.JLabel();
        tfNama = new javax.swing.JTextField();
        jPanelFieldKelas = new javax.swing.JPanel();
        jLabelKelas = new javax.swing.JLabel();
        cbKelas = new javax.swing.JComboBox<>();
        jPanelFieldJK = new javax.swing.JPanel();
        jLabelJK = new javax.swing.JLabel();
        cbJK = new javax.swing.JComboBox<>();
        jPanelFieldAlamat = new javax.swing.JPanel();
        jLabelAlamat = new javax.swing.JLabel();
        tfAlamat = new javax.swing.JTextField();
        jPanelFieldBlank = new javax.swing.JPanel();
        jPanelButtons = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jPanelTable = new javax.swing.JPanel();
        jPanelSearch = new javax.swing.JPanel();
        jLabelCari = new javax.swing.JLabel();
        tfCari = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PARENT POINT - Master Data Siswa");

        jPanelHeader.setBackground(new java.awt.Color(25, 55, 109));
        jPanelHeader.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 25, 15));

        jLabelTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabelTitle.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitle.setText("📋  Master Data Siswa");
        jPanelHeader.add(jLabelTitle);

        getContentPane().add(jPanelHeader, java.awt.BorderLayout.NORTH);

        jPanelMain.setBackground(new java.awt.Color(236, 240, 245));
        jPanelMain.setLayout(new java.awt.BorderLayout(0, 15));

        jPanelForm.setBackground(new java.awt.Color(255, 255, 255));
        jPanelForm.setLayout(new java.awt.BorderLayout(0, 10));

        jPanelFields.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFields.setLayout(new java.awt.GridLayout(3, 2, 15, 8));

        jPanelFieldNIS.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFieldNIS.setLayout(new java.awt.BorderLayout(0, 4));

        jLabelNIS.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelNIS.setText("NIS :");
        jPanelFieldNIS.add(jLabelNIS, java.awt.BorderLayout.NORTH);

        tfNIS.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanelFieldNIS.add(tfNIS, java.awt.BorderLayout.CENTER);

        jPanelFields.add(jPanelFieldNIS);

        jPanelFieldNama.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFieldNama.setLayout(new java.awt.BorderLayout(0, 4));

        jLabelNama.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelNama.setText("Nama Siswa :");
        jPanelFieldNama.add(jLabelNama, java.awt.BorderLayout.NORTH);

        tfNama.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPanelFieldNama.add(tfNama, java.awt.BorderLayout.CENTER);

        jPanelFields.add(jPanelFieldNama);

        jPanelFieldKelas.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFieldKelas.setLayout(new java.awt.BorderLayout(0, 4));

        jLabelKelas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelKelas.setText("Kelas :");
        jPanelFieldKelas.add(jLabelKelas, java.awt.BorderLayout.NORTH);

        cbKelas.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanelFieldKelas.add(cbKelas, java.awt.BorderLayout.CENTER);

        jPanelFields.add(jPanelFieldKelas);

        jPanelFieldJK.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFieldJK.setLayout(new java.awt.BorderLayout(0, 4));

        jLabelJK.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabelJK.setText("Jenis Kelamin :");
        jPanelFieldJK.add(jLabelJK, java.awt.BorderLayout.NORTH);

        cbJK.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jPanelFieldJK.add(cbJK, java.awt.BorderLayout.CENTER);

        jPanelFields.add(jPanelFieldJK);

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

        jPanelTable.setBackground(new java.awt.Color(236, 240, 245));
        jPanelTable.setLayout(new java.awt.BorderLayout(0, 10));

        jPanelSearch.setBackground(new java.awt.Color(236, 240, 245));
        jPanelSearch.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        jLabelCari.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabelCari.setText("Cari :");
        jPanelSearch.add(jLabelCari);

        tfCari.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tfCari.setMargin(new java.awt.Insets(3, 100, 3, 10));
        tfCari.setMinimumSize(new java.awt.Dimension(15, 30));
        tfCari.setName(""); // NOI18N
        tfCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfCariActionPerformed(evt);
            }
        });
        jPanelSearch.add(tfCari);

        btnCari.setContentAreaFilled(false);
        btnCari.setOpaque(true);
        btnCari.setBackground(new java.awt.Color(25, 55, 109));
        btnCari.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnCari.setForeground(new java.awt.Color(255, 255, 255));
        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });
        jPanelSearch.add(btnCari);

        jPanelTable.add(jPanelSearch, java.awt.BorderLayout.NORTH);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NIS", "Nama", "Kelas", "JK", "Alamat"
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

        jPanelTable.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanelMain.add(jPanelTable, java.awt.BorderLayout.CENTER);

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

    private void tfCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfCariActionPerformed
        cariData();
    }//GEN-LAST:event_tfCariActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        cariData();
    }//GEN-LAST:event_btnCariActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new MasterSiswa().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cbJK;
    private javax.swing.JComboBox<String> cbKelas;
    private javax.swing.JLabel jLabelAlamat;
    private javax.swing.JLabel jLabelCari;
    private javax.swing.JLabel jLabelJK;
    private javax.swing.JLabel jLabelKelas;
    private javax.swing.JLabel jLabelNIS;
    private javax.swing.JLabel jLabelNama;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelFieldAlamat;
    private javax.swing.JPanel jPanelFieldBlank;
    private javax.swing.JPanel jPanelFieldJK;
    private javax.swing.JPanel jPanelFieldKelas;
    private javax.swing.JPanel jPanelFieldNIS;
    private javax.swing.JPanel jPanelFieldNama;
    private javax.swing.JPanel jPanelFields;
    private javax.swing.JPanel jPanelForm;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelSearch;
    private javax.swing.JPanel jPanelTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tfAlamat;
    private javax.swing.JTextField tfCari;
    private javax.swing.JTextField tfNIS;
    private javax.swing.JTextField tfNama;
    // End of variables declaration//GEN-END:variables
}

