package parentpoint.transaksi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import parentpoint.koneksi.koneksi;
import parentpoint.util.DesignUtil;

/**
 * Form Transaksi: Rekap Kehadiran Bulanan
 * Menampilkan rekap kehadiran tiap siswa per bulan per kelas
 *
 * @author HP
 */
public class RekapBulanan extends JFrame {

    private JComboBox<String> cbBulan, cbTahun, cbKelas;
    private JButton btnTampilkan, btnReset;
    private JTable tblRekap;
    private JScrollPane scrollPane;
    private JLabel lblTotalHadir, lblTotalSakit, lblTotalIzin, lblTotalAlpha;

    public RekapBulanan() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        styleComponents();
        loadKelas();
    }

    private void initComponents() {
        setTitle("PARENT POINT - Rekap Kehadiran Bulanan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
// setSize removed for responsiveness
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // === HEADER ===
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(DesignUtil.PRIMARY);
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lblHeaderTitle = new JLabel("📅  Rekap Kehadiran Bulanan");
        lblHeaderTitle.setFont(DesignUtil.FONT_SUBTITLE);
        lblHeaderTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblHeaderTitle, BorderLayout.WEST);

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

        // --- FILTER PANEL ---
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel lblBulan = new JLabel("Bulan:");
        lblBulan.setFont(DesignUtil.FONT_BODY_BOLD);
        lblBulan.setForeground(DesignUtil.TEXT_PRIMARY);
        pnlFilter.add(lblBulan);

        cbBulan = new JComboBox<>(new String[]{
            "01 - Januari", "02 - Februari", "03 - Maret", "04 - April",
            "05 - Mei", "06 - Juni", "07 - Juli", "08 - Agustus",
            "09 - September", "10 - Oktober", "11 - November", "12 - Desember"
        });
        cbBulan.setFont(DesignUtil.FONT_BODY);
        cbBulan.setPreferredSize(new Dimension(160, 35));
        cbBulan.setSelectedIndex(5); // default Juni
        pnlFilter.add(cbBulan);

        JLabel lblTahun = new JLabel("Tahun:");
        lblTahun.setFont(DesignUtil.FONT_BODY_BOLD);
        lblTahun.setForeground(DesignUtil.TEXT_PRIMARY);
        pnlFilter.add(lblTahun);

        cbTahun = new JComboBox<>(new String[]{"2024", "2025", "2026", "2027"});
        cbTahun.setFont(DesignUtil.FONT_BODY);
        cbTahun.setPreferredSize(new Dimension(90, 35));
        cbTahun.setSelectedItem("2026");
        pnlFilter.add(cbTahun);

        JLabel lblKelas = new JLabel("Kelas:");
        lblKelas.setFont(DesignUtil.FONT_BODY_BOLD);
        lblKelas.setForeground(DesignUtil.TEXT_PRIMARY);
        pnlFilter.add(lblKelas);

        cbKelas = new JComboBox<>();
        cbKelas.setFont(DesignUtil.FONT_BODY);
        cbKelas.setPreferredSize(new Dimension(150, 35));
        pnlFilter.add(cbKelas);

        btnTampilkan = DesignUtil.createButton("🔍 Tampilkan", DesignUtil.PRIMARY);
        btnTampilkan.setPreferredSize(new Dimension(140, 35));
        btnTampilkan.addActionListener(e -> tampilkanRekap());
        pnlFilter.add(btnTampilkan);

        btnReset = DesignUtil.createButton("↺ Reset", DesignUtil.WARNING);
        btnReset.setPreferredSize(new Dimension(100, 35));
        btnReset.addActionListener(e -> resetForm());
        pnlFilter.add(btnReset);

        pnlBody.add(pnlFilter, BorderLayout.NORTH);

        // --- SUMMARY CARDS ---
        JPanel pnlCards = new JPanel(new java.awt.GridLayout(1, 4, 15, 0));
        pnlCards.setBackground(DesignUtil.BG_MAIN);
        pnlCards.setPreferredSize(new Dimension(0, 100));

        lblTotalHadir = new JLabel("0");
        lblTotalSakit = new JLabel("0");
        lblTotalIzin = new JLabel("0");
        lblTotalAlpha = new JLabel("0");

        pnlCards.add(buatCard("TOTAL HADIR", lblTotalHadir, DesignUtil.SUCCESS));
        pnlCards.add(buatCard("TOTAL SAKIT", lblTotalSakit, DesignUtil.WARNING));
        pnlCards.add(buatCard("TOTAL IZIN", lblTotalIzin, DesignUtil.INFO));
        pnlCards.add(buatCard("TOTAL ALPHA", lblTotalAlpha, DesignUtil.DANGER));

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setBackground(DesignUtil.BG_MAIN);
        pnlCenter.add(pnlCards, BorderLayout.NORTH);

        // --- TABLE ---
        String[] cols = {"NIS", "Nama Siswa", "Kelas", "Hadir", "Sakit", "Izin", "Alpha", "Total Hari", "% Hadir"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblRekap = new JTable(model);
        DesignUtil.styleTable(tblRekap);

        // Center-align columns 3-8
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 3; i <= 8; i++) {
            tblRekap.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scrollPane = new JScrollPane(tblRekap);
        scrollPane.setBorder(BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR));
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlCenter.add(scrollPane, BorderLayout.CENTER);

        pnlBody.add(pnlCenter, BorderLayout.CENTER);

        // === FOOTER ===
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlFooter.setBackground(DesignUtil.BG_MAIN);
        JLabel lblInfo = new JLabel("ℹ️  % Hadir = (Hadir / Total Hari) × 100");
        lblInfo.setFont(DesignUtil.FONT_SMALL);
        lblInfo.setForeground(DesignUtil.TEXT_SECONDARY);
        pnlFooter.add(lblInfo);
        pnlBody.add(pnlFooter, BorderLayout.SOUTH);

        getContentPane().add(pnlBody, BorderLayout.CENTER);
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

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(DesignUtil.FONT_CARD_LABEL);
        lblTitle.setForeground(DesignUtil.TEXT_SECONDARY);
        content.add(lblTitle, BorderLayout.NORTH);

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
                ResultSet rs = st.executeQuery("SELECT nama_kelas FROM kelas ORDER BY nama_kelas");
                while (rs.next()) {
                    cbKelas.addItem(rs.getString("nama_kelas"));
                }
                rs.close(); st.close(); conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error load kelas: " + e.getMessage());
            }
        }
    }

    private void tampilkanRekap() {
        String selectedBulan = (String) cbBulan.getSelectedItem();
        String bulan = selectedBulan.substring(0, 2); // "01"-"12"
        String tahun = (String) cbTahun.getSelectedItem();
        String kelas = (String) cbKelas.getSelectedItem();

        Connection conn = koneksi.getConnection();
        if (conn == null) return;

        try {
            String sql = "SELECT s.nis, s.nama, k.nama_kelas, "
                + "SUM(CASE WHEN h.status = 'Hadir' THEN 1 ELSE 0 END) AS hadir, "
                + "SUM(CASE WHEN h.status = 'Sakit' THEN 1 ELSE 0 END) AS sakit, "
                + "SUM(CASE WHEN h.status = 'Izin' THEN 1 ELSE 0 END) AS izin, "
                + "SUM(CASE WHEN h.status = 'Alpha' THEN 1 ELSE 0 END) AS alpha, "
                + "COUNT(h.id) AS total "
                + "FROM siswa s "
                + "JOIN kelas k ON s.kelas_id = k.id "
                + "LEFT JOIN kehadiran h ON h.siswa_id = s.id "
                + "  AND MONTH(h.tanggal) = ? AND YEAR(h.tanggal) = ? ";

            if (kelas != null && !kelas.equals("Semua Kelas")) {
                sql += "AND k.nama_kelas = ? ";
            }
            sql += "GROUP BY s.id, s.nis, s.nama, k.nama_kelas ORDER BY k.nama_kelas, s.nama";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, bulan);
            ps.setString(2, tahun);
            if (kelas != null && !kelas.equals("Semua Kelas")) {
                ps.setString(3, kelas);
            }

            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) tblRekap.getModel();
            model.setRowCount(0);

            int sumHadir = 0, sumSakit = 0, sumIzin = 0, sumAlpha = 0;

            while (rs.next()) {
                int h = rs.getInt("hadir");
                int s = rs.getInt("sakit");
                int iz = rs.getInt("izin");
                int al = rs.getInt("alpha");
                int tot = rs.getInt("total");
                String persen = tot > 0 ? String.format("%.1f%%", (h * 100.0 / tot)) : "0.0%";

                model.addRow(new Object[]{
                    rs.getString("nis"),
                    rs.getString("nama"),
                    rs.getString("nama_kelas"),
                    h, s, iz, al, tot, persen
                });

                sumHadir += h;
                sumSakit += s;
                sumIzin += iz;
                sumAlpha += al;
            }

            lblTotalHadir.setText(String.valueOf(sumHadir));
            lblTotalSakit.setText(String.valueOf(sumSakit));
            lblTotalIzin.setText(String.valueOf(sumIzin));
            lblTotalAlpha.setText(String.valueOf(sumAlpha));

            rs.close(); ps.close(); conn.close();

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Tidak ada data untuk filter yang dipilih.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        cbBulan.setSelectedIndex(5);
        cbTahun.setSelectedItem("2026");
        cbKelas.setSelectedIndex(0);
        lblTotalHadir.setText("0");
        lblTotalSakit.setText("0");
        lblTotalIzin.setText("0");
        lblTotalAlpha.setText("0");
        ((DefaultTableModel) tblRekap.getModel()).setRowCount(0);
    }

    private void styleComponents() {
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        // already handled inside initComponents
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new RekapBulanan().setVisible(true));
    }
}
