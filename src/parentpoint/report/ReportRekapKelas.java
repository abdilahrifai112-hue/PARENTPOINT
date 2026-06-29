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
 * Report 3: Rekap Kehadiran Per Kelas
 * Menampilkan statistik kehadiran dikelompokkan per kelas
 *
 * @author HP
 */
public class ReportRekapKelas extends JFrame {

    private JTextField tfDari, tfSampai;
    private JButton btnTampilkan, btnReset;
    private JTable tblData;
    private JLabel lblTotalKelas, lblKelasUnggul;

    public ReportRekapKelas() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        styleComponents();
    }

    private void initComponents() {
        setTitle("PARENT POINT - Laporan Rekap Per Kelas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
// setSize removed for responsiveness
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // === HEADER ===
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(124, 58, 237)); // Purple
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lblTitle = new JLabel("🏫  Laporan Rekap Kehadiran Per Kelas");
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

        btnTampilkan = DesignUtil.createButton("🔍 Tampilkan", new Color(124, 58, 237));
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

        lblTotalKelas = new JLabel("0");
        lblKelasUnggul = new JLabel("-");

        pnlCards.add(buatCard("TOTAL KELAS TERDAFTAR", lblTotalKelas, new Color(124, 58, 237)));
        pnlCards.add(buatCard("KELAS KEHADIRAN TERTINGGI", lblKelasUnggul, DesignUtil.SUCCESS));

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setBackground(DesignUtil.BG_MAIN);
        pnlCenter.add(pnlCards, BorderLayout.NORTH);

        // --- TABLE ---
        String[] cols = {"Kelas", "Wali Kelas", "Jml Siswa", "Total Hadir", "Total Sakit", "Total Izin", "Total Alpha", "Total Hari", "Rata-rata Hadir (%)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblData = new JTable(model);
        DesignUtil.styleTable(tblData);

        // Center-align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 2; i <= 7; i++) {
            tblData.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Color renderer for rata-rata column (col 8)
        tblData.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                if (!sel && val != null) {
                    String pct = val.toString().replace("%", "");
                    try {
                        double v = Double.parseDouble(pct);
                        if (v >= 80) { setForeground(Color.WHITE); setBackground(DesignUtil.SUCCESS); }
                        else if (v >= 60) { setForeground(Color.WHITE); setBackground(DesignUtil.WARNING); }
                        else { setForeground(Color.WHITE); setBackground(DesignUtil.DANGER); }
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

        // === FOOTER INFO ===
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pnlFooter.setBackground(DesignUtil.BG_MAIN);

        JLabel lblInfo = new JLabel("ℹ️  Rata-rata Hadir dihitung dari: Total Hadir ÷ Total Hari Kehadiran × 100");
        lblInfo.setFont(DesignUtil.FONT_SMALL);
        lblInfo.setForeground(DesignUtil.TEXT_SECONDARY);
        pnlFooter.add(lblInfo);
        pnlBody.add(pnlFooter, BorderLayout.SOUTH);

        getContentPane().add(pnlBody, BorderLayout.CENTER);
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

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValue.setForeground(color);
        content.add(lblValue, BorderLayout.CENTER);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private void tampilkanLaporan() {
        String dari = tfDari.getText().trim();
        String sampai = tfSampai.getText().trim();

        if (dari.isEmpty() || sampai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal harus diisi! Format: yyyy-MM-dd", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = koneksi.getConnection();
        if (conn == null) return;

        try {
            String sql = "SELECT k.nama_kelas, k.wali_kelas, "
                + "COUNT(DISTINCT s.id) AS jml_siswa, "
                + "SUM(CASE WHEN h.status='Hadir' THEN 1 ELSE 0 END) AS hadir, "
                + "SUM(CASE WHEN h.status='Sakit' THEN 1 ELSE 0 END) AS sakit, "
                + "SUM(CASE WHEN h.status='Izin' THEN 1 ELSE 0 END) AS izin, "
                + "SUM(CASE WHEN h.status='Alpha' THEN 1 ELSE 0 END) AS alpha, "
                + "COUNT(h.id) AS total "
                + "FROM kelas k "
                + "LEFT JOIN siswa s ON s.kelas_id = k.id "
                + "LEFT JOIN kehadiran h ON h.siswa_id = s.id AND h.tanggal BETWEEN ? AND ? "
                + "GROUP BY k.id, k.nama_kelas, k.wali_kelas "
                + "ORDER BY k.nama_kelas";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dari);
            ps.setString(2, sampai);

            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) tblData.getModel();
            model.setRowCount(0);

            int totalKelas = 0;
            double maxPct = -1;
            String kelasUnggul = "-";

            while (rs.next()) {
                int h = rs.getInt("hadir");
                int tot = rs.getInt("total");
                double pct = tot > 0 ? (h * 100.0 / tot) : 0;

                model.addRow(new Object[]{
                    rs.getString("nama_kelas"),
                    rs.getString("wali_kelas"),
                    rs.getInt("jml_siswa"),
                    h, rs.getInt("sakit"), rs.getInt("izin"), rs.getInt("alpha"),
                    tot,
                    String.format("%.1f%%", pct)
                });

                if (pct > maxPct) {
                    maxPct = pct;
                    kelasUnggul = rs.getString("nama_kelas") + " (" + String.format("%.1f%%", pct) + ")";
                }
                totalKelas++;
            }

            lblTotalKelas.setText(String.valueOf(totalKelas));
            lblKelasUnggul.setText(kelasUnggul);

            rs.close(); ps.close(); conn.close();

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Tidak ada data untuk periode ini.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        tfDari.setText("2026-06-01");
        tfSampai.setText("2026-06-30");
        lblTotalKelas.setText("0");
        lblKelasUnggul.setText("-");
        ((DefaultTableModel) tblData.getModel()).setRowCount(0);
    }

    private void styleComponents() {
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);}

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ReportRekapKelas().setVisible(true));
    }
}
