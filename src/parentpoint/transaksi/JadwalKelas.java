package parentpoint.transaksi;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import parentpoint.koneksi.koneksi;
import parentpoint.util.DesignUtil;
import parentpoint.util.Session;

public class JadwalKelas extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cbKelas, cbHari, cbMapel, cbGuru;
    private JTextField txtJamMulai, txtJamSelesai;
    private JButton btnSimpan, btnHapus, btnReset, btnKembali;
    private String selectedId = "";
    private boolean isAdmin;

    public JadwalKelas() {
        isAdmin = "admin".equalsIgnoreCase(Session.getRole());
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        loadData();
    }

    private JLabel createBlueLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(25, 55, 109));
        return lbl;
    }

    private void initComponents() {
        setTitle("PARENT POINT - Jadwal Kelas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
// setSize removed for responsiveness
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DesignUtil.BG_MAIN);

        // Header Panel
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(DesignUtil.BG_HEADER);
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel lblTitle = new JLabel("📅  JADWAL KELAS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        btnKembali = new JButton("Kembali");
        btnKembali.setBackground(DesignUtil.PRIMARY);
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setFocusPainted(false);
        btnKembali.addActionListener(e -> dispose());
        pnlHeader.add(btnKembali, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // Main Panel (Split into Form and Table)
        JPanel pnlMain = new JPanel(new BorderLayout(15, 15));
        pnlMain.setBackground(DesignUtil.BG_MAIN);
        pnlMain.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        // Form Panel (Only for Admin)
        if (isAdmin) {
            JPanel pnlForm = new JPanel(new GridBagLayout());
            pnlForm.setBackground(Color.WHITE);
            pnlForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            gbc.gridx = 0; gbc.gridy = 0;
            pnlForm.add(createBlueLabel("Pilih Kelas:"), gbc);
            cbKelas = new JComboBox<>();
            loadComboBoxKelas();
            gbc.gridx = 1;
            pnlForm.add(cbKelas, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            pnlForm.add(createBlueLabel("Pilih Hari:"), gbc);
            cbHari = new JComboBox<>(new String[]{"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"});
            gbc.gridx = 1;
            pnlForm.add(cbHari, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            pnlForm.add(createBlueLabel("Jam Mulai (HH:MM):"), gbc);
            txtJamMulai = new JTextField(10);
            gbc.gridx = 1;
            pnlForm.add(txtJamMulai, gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            pnlForm.add(createBlueLabel("Jam Selesai (HH:MM):"), gbc);
            txtJamSelesai = new JTextField(10);
            gbc.gridx = 1;
            pnlForm.add(txtJamSelesai, gbc);

            gbc.gridx = 0; gbc.gridy = 4;
            pnlForm.add(createBlueLabel("Nama Mata Pelajaran:"), gbc);
            cbMapel = new JComboBox<>();
            loadComboBoxMapel();
            gbc.gridx = 1;
            pnlForm.add(cbMapel, gbc);

            gbc.gridx = 0; gbc.gridy = 5;
            pnlForm.add(createBlueLabel("Nama Guru Pengajar:"), gbc);
            cbGuru = new JComboBox<>();
            loadComboBoxGuru();
            gbc.gridx = 1;
            pnlForm.add(cbGuru, gbc);

            JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
            pnlButtons.setBackground(Color.WHITE);
            btnSimpan = new JButton("Simpan");
            btnSimpan.setBackground(DesignUtil.SUCCESS);
            btnSimpan.setForeground(Color.WHITE);
            btnHapus = new JButton("Hapus");
            btnHapus.setBackground(DesignUtil.DANGER);
            btnHapus.setForeground(Color.WHITE);
            btnReset = new JButton("Reset");

            pnlButtons.add(btnSimpan);
            pnlButtons.add(btnHapus);
            pnlButtons.add(btnReset);

            gbc.gridx = 1; gbc.gridy = 6;
            pnlForm.add(pnlButtons, gbc);

            pnlMain.add(pnlForm, BorderLayout.WEST);

            btnSimpan.addActionListener(e -> simpanData());
            btnHapus.addActionListener(e -> hapusData());
            btnReset.addActionListener(e -> resetForm());
        }

        // Table Panel
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.setBorder(BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR));

        model = new DefaultTableModel(new String[]{"ID", "Kelas", "Hari", "Jam", "Mata Pelajaran", "Guru"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setBackground(DesignUtil.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        if (isAdmin) {
            table.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    int row = table.getSelectedRow();
                    selectedId = model.getValueAt(row, 0).toString();
                    String kls = model.getValueAt(row, 1).toString();
                    for (int i=0; i<cbKelas.getItemCount(); i++) {
                        if (cbKelas.getItemAt(i).contains(kls)) cbKelas.setSelectedIndex(i);
                    }
                    cbHari.setSelectedItem(model.getValueAt(row, 2).toString());
                    String jam = model.getValueAt(row, 3).toString();
                    String[] jams = jam.split(" - ");
                    if (jams.length == 2) {
                        txtJamMulai.setText(jams[0]);
                        txtJamSelesai.setText(jams[1]);
                    }
                    String mapel = model.getValueAt(row, 4).toString();
                    cbMapel.setSelectedItem(mapel);
                    String guru = model.getValueAt(row, 5).toString();
                    cbGuru.setSelectedItem(guru);
                }
            });
        }

        pnlTable.add(new JScrollPane(table), BorderLayout.CENTER);
        pnlMain.add(pnlTable, BorderLayout.CENTER);

        add(pnlMain, BorderLayout.CENTER);
    }

    private void loadComboBoxKelas() {
        try (Connection conn = koneksi.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM kelas");
            while (rs.next()) {
                cbKelas.addItem(rs.getInt("id") + " - " + rs.getString("nama_kelas"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadComboBoxMapel() {
        cbMapel.addItem("- Pilih Mata Pelajaran -");
        try (Connection conn = koneksi.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT nama_mapel FROM mata_pelajaran ORDER BY nama_mapel");
            while (rs.next()) {
                cbMapel.addItem(rs.getString("nama_mapel"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadComboBoxGuru() {
        cbGuru.addItem("- Pilih Guru -");
        try (Connection conn = koneksi.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT nama_guru FROM guru ORDER BY nama_guru");
            while (rs.next()) {
                cbGuru.addItem(rs.getString("nama_guru"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void loadData() {
        model.setRowCount(0);
        String sql = "SELECT j.*, k.nama_kelas FROM jadwal_kelas j JOIN kelas k ON j.kelas_id = k.id";
        
        String role = Session.getRole();
        if ("guru".equalsIgnoreCase(role)) {
            sql += " WHERE j.guru = '" + Session.getGuruNama() + "'";
        } else if (!isAdmin) {
            int kId = Session.getKelasId();
            sql += " WHERE j.kelas_id = " + kId;
        }
        
        sql += " ORDER BY FIELD(j.hari, 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu'), j.jam_mulai";

        try (Connection conn = koneksi.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nama_kelas"),
                    rs.getString("hari"),
                    rs.getString("jam_mulai").substring(0, 5) + " - " + rs.getString("jam_selesai").substring(0, 5),
                    rs.getString("mata_pelajaran"),
                    rs.getString("guru")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error memuat data: " + ex.getMessage());
        }
    }

    private void simpanData() {
        if (cbKelas.getSelectedIndex() == -1 || txtJamMulai.getText().isEmpty() || 
            cbMapel.getSelectedIndex() <= 0 || cbGuru.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Data belum lengkap!");
            return;
        }

        int kelasId = Integer.parseInt(cbKelas.getSelectedItem().toString().split(" - ")[0]);
        String sql;
        if (selectedId.isEmpty()) {
            sql = "INSERT INTO jadwal_kelas (kelas_id, hari, jam_mulai, jam_selesai, mata_pelajaran, guru) VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE jadwal_kelas SET kelas_id=?, hari=?, jam_mulai=?, jam_selesai=?, mata_pelajaran=?, guru=? WHERE id=?";
        }

        try (Connection conn = koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kelasId);
            ps.setString(2, cbHari.getSelectedItem().toString());
            ps.setString(3, txtJamMulai.getText());
            ps.setString(4, txtJamSelesai.getText());
            ps.setString(5, cbMapel.getSelectedItem().toString());
            ps.setString(6, cbGuru.getSelectedItem().toString());
            if (!selectedId.isEmpty()) {
                ps.setInt(7, Integer.parseInt(selectedId));
            }
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            resetForm();
            loadData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error menyimpan data: " + ex.getMessage());
        }
    }

    private void hapusData() {
        if (selectedId.isEmpty()) return;
        int conf = JOptionPane.showConfirmDialog(this, "Hapus data terpilih?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try (Connection conn = koneksi.getConnection(); 
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM jadwal_kelas WHERE id=?")) {
                ps.setInt(1, Integer.parseInt(selectedId));
                ps.executeUpdate();
                resetForm();
                loadData();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void resetForm() {
        selectedId = "";
        cbKelas.setSelectedIndex(0);
        cbHari.setSelectedIndex(0);
        txtJamMulai.setText("");
        txtJamSelesai.setText("");
        cbMapel.setSelectedIndex(0);
        cbGuru.setSelectedIndex(0);
        table.clearSelection();
    }
}
