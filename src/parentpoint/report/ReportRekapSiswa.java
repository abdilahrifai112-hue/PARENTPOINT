package parentpoint.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
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

/**
 * Report 2: Rekap Kehadiran Per Siswa
 * Menampilkan total hadir/sakit/izin/alpha setiap siswa dalam rentang tanggal
 *
 * @author HP
 */
public class ReportRekapSiswa extends JFrame {

    private JTextField tfDari, tfSampai;
    private JComboBox<String> cbKelas;
    private JButton btnTampilkan, btnReset;
    private JTable tblData;
    private JLabel lblTotalSiswa, lblRataHadir;

    public ReportRekapSiswa() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        styleComponents();
        loadKelas();
    }

    private void initComponents() {
        setTitle("PARENT POINT - Laporan Rekap Per Siswa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1050, 680);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // === HEADER ===
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(DesignUtil.SUCCESS);
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lblTitle = new JLabel("📊  Laporan Rekap Kehadiran Per Siswa");
        lblTitle.setFont(DesignUtil.FONT_SUBTITLE);
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        JButton btnKembali = new JButton("⬅  Kembali ke Dashboard");
        btnKembali.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnKembali.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnKembali.setBackground(new Color(71, 85, 105));
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setFocusPainted(false);
        btnKembali.setBorderPainted(false);
        btnKembali.setOpaque(true);
        btnKembali.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKembali.setPreferredSize(new Dimension(195, 35));
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
        pnlHeader.add(btnKembali, BorderLayout.EAST);

        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        // === BODY ===
        JPanel pnlBody = new JPanel(new BorderLayout(0, 15));
        pnlBody.setBackground(DesignUtil.BG_MAIN);
        pnlBody.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- FILTER ---
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        pnlFilter.add(createLabel("Dari Tanggal:"));
        tfDari = createTextField("2026-06-01");
        pnlFilter.add(tfDari);

        pnlFilter.add(createLabel("Sampai:"));
        tfSampai = createTextField("2026-06-30");
        pnlFilter.add(tfSampai);

        pnlFilter.add(createLabel("Kelas:"));
        cbKelas = new JComboBox<>();
        cbKelas.setFont(DesignUtil.FONT_BODY);
        cbKelas.setPreferredSize(new Dimension(150, 35));
        pnlFilter.add(cbKelas);

        btnTampilkan = DesignUtil.createButton("🔍 Tampilkan", DesignUtil.SUCCESS);
        btnTampilkan.setPreferredSize(new Dimension(140, 35));
        btnTampilkan.addActionListener(e -> tampilkanLaporan());
        pnlFilter.add(btnTampilkan);

        btnReset = DesignUtil.createButton("↺ Reset", DesignUtil.WARNING);
        btnReset.setPreferredSize(new Dimension(100, 35));
        btnReset.addActionListener(e -> resetForm());
        pnlFilter.add(btnReset);

        pnlBody.add(pnlFilter, BorderLayout.NORTH);

        // --- SUMMARY CARDS ---
        JPanel pnlCards = new JPanel(new java.awt.GridLayout(1, 2, 15, 0));
        pnlCards.setBackground(DesignUtil.BG_MAIN);
        pnlCards.setPreferredSize(new Dimension(0, 100));

        lblTotalSiswa = new JLabel("0");
        lblRataHadir = new JLabel("0%");

        pnlCards.add(buatCard("TOTAL SISWA TERCATAT", lblTotalSiswa, DesignUtil.PRIMARY));
        pnlCards.add(buatCard("RATA-RATA KEHADIRAN", lblRataHadir, DesignUtil.SUCCESS));

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setBackground(DesignUtil.BG_MAIN);
        pnlCenter.add(pnlCards, BorderLayout.NORTH);

        // --- TABLE ---
        String[] cols = {"NIS", "Nama Siswa", "Kelas", "Wali Kelas", "Hadir", "Sakit", "Izin", "Alpha", "Total", "% Hadir", "Keterangan"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblData = new JTable(model);
        DesignUtil.styleTable(tblData);

        // Center-align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 4; i <= 9; i++) {
            tblData.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Color renderer for % Hadir column (col 9)
        tblData.getColumnModel().getColumn(9).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                if (!sel && val != null) {
                    String pct = val.toString().replace("%", "");
                    try {
                        double v = Double.parseDouble(pct);
                        if (v >= 80) setForeground(DesignUtil.SUCCESS);
                        else if (v >= 60) setForeground(DesignUtil.WARNING);
                        else setForeground(DesignUtil.DANGER);
                    } catch (NumberFormatException ex) {
                        setForeground(DesignUtil.TEXT_PRIMARY);
                    }
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblData);
        scrollPane.setBorder(BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR));
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlCenter.add(scrollPane, BorderLayout.CENTER);

        pnlBody.add(pnlCenter, BorderLayout.CENTER);

        // === LEGEND ===
        JPanel pnlLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pnlLegend.setBackground(DesignUtil.BG_MAIN);

        addLegend(pnlLegend, "● ≥ 80%: Baik", DesignUtil.SUCCESS);
        addLegend(pnlLegend, "● 60–79%: Cukup", DesignUtil.WARNING);
        addLegend(pnlLegend, "● < 60%: Perlu Perhatian", DesignUtil.DANGER);

        pnlBody.add(pnlLegend, BorderLayout.SOUTH);
        getContentPane().add(pnlBody, BorderLayout.CENTER);
    }

    private void addLegend(JPanel panel, String text, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(DesignUtil.FONT_SMALL);
        lbl.setForeground(color);
        panel.add(lbl);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(DesignUtil.FONT_BODY_BOLD);
        lbl.setForeground(DesignUtil.TEXT_PRIMARY);
        return lbl;
    }

    private JTextField createTextField(String placeholder) {
        JTextField tf = new JTextField(placeholder, 11);
        tf.setFont(DesignUtil.FONT_BODY);
        tf.setPreferredSize(new Dimension(130, 35));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return tf;
    }

    private JPanel buatCard(String title, JLabel lblValue, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel bar = new JPanel();
        bar.setBackground(color);
        bar.setPreferredSize(new Dimension(0, 4));
        card.add(bar, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 5));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JLabel lblT = new JLabel(title);
        lblT.setFont(DesignUtil.FONT_CARD_LABEL);
        lblT.setForeground(DesignUtil.TEXT_SECONDARY);
        content.add(lblT, BorderLayout.NORTH);

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(color);
        content.add(lblValue, BorderLayout.CENTER);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private void loadKelas() {
        Connection conn = koneksi.getConnection();
        if (conn != null) {
            try {
                cbKelas.removeAllItems();
                cbKelas.addItem("Semua Kelas");
                java.sql.Statement st = conn.createStatement();
                String sql = "SELECT nama_kelas FROM kelas ";
                
                String role = parentpoint.util.Session.getRole();
                if ("guru".equalsIgnoreCase(role)) {
                    String g = parentpoint.util.Session.getGuruNama();
                    sql = "SELECT DISTINCT k.nama_kelas FROM kelas k " +
                          "LEFT JOIN jadwal_kelas j ON j.kelas_id = k.id " +
                          "WHERE k.wali_kelas = '" + g + "' OR j.guru = '" + g + "' ";
                }
                sql += " ORDER BY nama_kelas";
                
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) cbKelas.addItem(rs.getString("nama_kelas"));
                rs.close(); st.close(); conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error load kelas: " + e.getMessage());
            }
        }
    }

    private void tampilkanLaporan() {
        String dari = tfDari.getText().trim();
        String sampai = tfSampai.getText().trim();
        String kelas = (String) cbKelas.getSelectedItem();

        if (dari.isEmpty() || sampai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal harus diisi! Format: yyyy-MM-dd", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = koneksi.getConnection();
        if (conn == null) return;

        try {
            String sql = "SELECT s.nis, s.nama, k.nama_kelas, k.wali_kelas, "
                + "SUM(CASE WHEN h.status='Hadir' THEN 1 ELSE 0 END) AS hadir, "
                + "SUM(CASE WHEN h.status='Sakit' THEN 1 ELSE 0 END) AS sakit, "
                + "SUM(CASE WHEN h.status='Izin' THEN 1 ELSE 0 END) AS izin, "
                + "SUM(CASE WHEN h.status='Alpha' THEN 1 ELSE 0 END) AS alpha, "
                + "COUNT(h.id) AS total "
                + "FROM siswa s "
                + "JOIN kelas k ON s.kelas_id = k.id "
                + "LEFT JOIN kehadiran h ON h.siswa_id = s.id AND h.tanggal BETWEEN ? AND ? "
                + "WHERE 1=1 ";

            if (kelas != null && !kelas.equals("Semua Kelas")) sql += "AND k.nama_kelas = ? ";
            
            String role = parentpoint.util.Session.getRole();
            if ("orang_tua".equalsIgnoreCase(role) || "siswa".equalsIgnoreCase(role)) {
                sql += "AND s.id = " + parentpoint.util.Session.getSiswaId() + " ";
                cbKelas.setEnabled(false); // Disable combobox for user
            } else if ("guru".equalsIgnoreCase(role)) {
                // If guru, only show students from classes they teach
                String g = parentpoint.util.Session.getGuruNama();
                sql += "AND k.nama_kelas IN (SELECT DISTINCT k2.nama_kelas FROM kelas k2 LEFT JOIN jadwal_kelas j ON j.kelas_id = k2.id WHERE k2.wali_kelas = '" + g + "' OR j.guru = '" + g + "') ";
            }

            sql += "GROUP BY s.id, s.nis, s.nama, k.nama_kelas, k.wali_kelas ORDER BY k.nama_kelas, s.nama";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dari);
            ps.setString(2, sampai);
            if (kelas != null && !kelas.equals("Semua Kelas")) ps.setString(3, kelas);

            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) tblData.getModel();
            model.setRowCount(0);

            int totalSiswa = 0;
            double sumPersen = 0;

            while (rs.next()) {
                int h = rs.getInt("hadir");
                int s = rs.getInt("sakit");
                int iz = rs.getInt("izin");
                int al = rs.getInt("alpha");
                int tot = rs.getInt("total");
                double pct = tot > 0 ? (h * 100.0 / tot) : 0;
                String keterangan = pct >= 80 ? "Baik" : pct >= 60 ? "Cukup" : "Perlu Perhatian";

                model.addRow(new Object[]{
                    rs.getString("nis"), rs.getString("nama"),
                    rs.getString("nama_kelas"), rs.getString("wali_kelas"),
                    h, s, iz, al, tot,
                    String.format("%.1f%%", pct),
                    keterangan
                });

                totalSiswa++;
                sumPersen += pct;
            }

            lblTotalSiswa.setText(String.valueOf(totalSiswa));
            lblRataHadir.setText(totalSiswa > 0 ? String.format("%.1f%%", sumPersen / totalSiswa) : "0%");

            rs.close(); ps.close(); conn.close();

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Tidak ada data untuk filter ini.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        tfDari.setText("2026-06-01");
        tfSampai.setText("2026-06-30");
        cbKelas.setSelectedIndex(0);
        lblTotalSiswa.setText("0");
        lblRataHadir.setText("0%");
        ((DefaultTableModel) tblData.getModel()).setRowCount(0);
    }

    private void styleComponents() {}

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ReportRekapSiswa().setVisible(true));
    }
}
