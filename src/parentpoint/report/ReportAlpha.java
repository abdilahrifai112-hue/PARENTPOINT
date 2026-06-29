package parentpoint.report;

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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import parentpoint.koneksi.koneksi;
import parentpoint.util.DesignUtil;

/**
 * Report 4: Laporan Siswa Sering Absen (Alpha)
 * Menampilkan daftar siswa yang alpha melebihi batas minimum
 *
 * @author HP
 */
public class ReportAlpha extends JFrame {

    private JTextField tfDari, tfSampai;
    private JComboBox<String> cbKelas;
    private JSpinner spMinAlpha;
    private JButton btnTampilkan, btnReset;
    private JTable tblData;
    private JLabel lblTotalSiswa, lblTotalAlpha;

    public ReportAlpha() {
        initComponents();
        styleComponents();
        loadKelas();
    }

    private void initComponents() {
        setTitle("PARENT POINT - Laporan Siswa Sering Absen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // === HEADER ===
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(DesignUtil.DANGER);
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lblTitle = new JLabel("⚠️  Laporan Siswa Sering Absen (Alpha)");
        lblTitle.setFont(DesignUtil.FONT_SUBTITLE);
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        JLabel lblSubtitle = new JLabel("Pantau siswa bermasalah kehadiran");
        lblSubtitle.setFont(DesignUtil.FONT_SMALL);
        lblSubtitle.setForeground(new Color(255, 200, 200));

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

        JPanel pnlRight = new JPanel(new java.awt.GridLayout(2, 1, 0, 3));
        pnlRight.setBackground(DesignUtil.DANGER);
        pnlRight.add(lblSubtitle);
        pnlRight.add(btnKembali);
        pnlHeader.add(pnlRight, BorderLayout.EAST);

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
        cbKelas.setPreferredSize(new Dimension(140, 35));
        pnlFilter.add(cbKelas);

        pnlFilter.add(createLabel("Min. Alpha ≥"));
        spMinAlpha = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spMinAlpha.setFont(DesignUtil.FONT_BODY);
        spMinAlpha.setPreferredSize(new Dimension(70, 35));
        pnlFilter.add(spMinAlpha);
        pnlFilter.add(createLabel("hari"));

        btnTampilkan = DesignUtil.createButton("🔍 Cari Siswa", DesignUtil.DANGER);
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
        lblTotalAlpha = new JLabel("0");

        pnlCards.add(buatCard("SISWA PERLU PERHATIAN", lblTotalSiswa, DesignUtil.DANGER));
        pnlCards.add(buatCard("TOTAL KEJADIAN ALPHA", lblTotalAlpha, new Color(180, 40, 40)));

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setBackground(DesignUtil.BG_MAIN);
        pnlCenter.add(pnlCards, BorderLayout.NORTH);

        // --- TABLE ---
        String[] cols = {"#", "NIS", "Nama Siswa", "Kelas", "Wali Kelas", "No. HP Ortu", "Jml Alpha", "Jml Izin", "Jml Sakit", "Total Hadir", "Status Risiko"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblData = new JTable(model);
        DesignUtil.styleTable(tblData);

        // Center-align numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i <= 1; i++) tblData.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        for (int i = 6; i <= 9; i++) tblData.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        // Color renderer for "Status Risiko" column (col 10)
        tblData.getColumnModel().getColumn(10).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (!sel && val != null) {
                    String status = val.toString();
                    if (status.contains("SANGAT TINGGI")) {
                        setForeground(Color.WHITE);
                        setBackground(new Color(180, 0, 0));
                    } else if (status.contains("TINGGI")) {
                        setForeground(Color.WHITE);
                        setBackground(DesignUtil.DANGER);
                    } else {
                        setForeground(Color.WHITE);
                        setBackground(DesignUtil.WARNING);
                    }
                }
                return this;
            }
        });

        // Color renderer for Alpha column (col 6) - highlight red
        tblData.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setHorizontalAlignment(JLabel.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                if (!sel) {
                    setForeground(DesignUtil.DANGER);
                    setBackground(row % 2 == 0 ? Color.WHITE : DesignUtil.TABLE_ALT_ROW);
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblData);
        scrollPane.setBorder(BorderFactory.createLineBorder(DesignUtil.BORDER_COLOR));
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlCenter.add(scrollPane, BorderLayout.CENTER);
        pnlBody.add(pnlCenter, BorderLayout.CENTER);

        // === FOOTER ===
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pnlFooter.setBackground(DesignUtil.BG_MAIN);

        addLegend(pnlFooter, "■ SANGAT TINGGI: Alpha ≥ 5 hari", new Color(180, 0, 0));
        addLegend(pnlFooter, "■ TINGGI: Alpha 3-4 hari", DesignUtil.DANGER);
        addLegend(pnlFooter, "■ SEDANG: Alpha 1-2 hari", DesignUtil.WARNING);

        pnlBody.add(pnlFooter, BorderLayout.SOUTH);
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
                ResultSet rs = st.executeQuery("SELECT nama_kelas FROM kelas ORDER BY nama_kelas");
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
        int minAlpha = (int) spMinAlpha.getValue();

        if (dari.isEmpty() || sampai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal harus diisi! Format: yyyy-MM-dd", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = koneksi.getConnection();
        if (conn == null) return;

        try {
            String sql = "SELECT s.nis, s.nama, k.nama_kelas, k.wali_kelas, "
                + "IFNULL(o.no_telp, '-') AS no_telp, "
                + "SUM(CASE WHEN h.status='Alpha' THEN 1 ELSE 0 END) AS alpha, "
                + "SUM(CASE WHEN h.status='Izin'  THEN 1 ELSE 0 END) AS izin, "
                + "SUM(CASE WHEN h.status='Sakit' THEN 1 ELSE 0 END) AS sakit, "
                + "SUM(CASE WHEN h.status='Hadir' THEN 1 ELSE 0 END) AS hadir "
                + "FROM siswa s "
                + "JOIN kelas k ON s.kelas_id = k.id "
                + "LEFT JOIN orang_tua o ON o.siswa_id = s.id "
                + "LEFT JOIN kehadiran h ON h.siswa_id = s.id AND h.tanggal BETWEEN ? AND ? ";

            if (kelas != null && !kelas.equals("Semua Kelas")) {
                sql += "AND k.nama_kelas = ? ";
            }
            sql += "GROUP BY s.id, s.nis, s.nama, k.nama_kelas, k.wali_kelas, o.no_telp "
                + "HAVING alpha >= ? "
                + "ORDER BY alpha DESC, s.nama";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dari);
            ps.setString(2, sampai);
            int paramIdx = 3;
            if (kelas != null && !kelas.equals("Semua Kelas")) {
                ps.setString(paramIdx++, kelas);
            }
            ps.setInt(paramIdx, minAlpha);

            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) tblData.getModel();
            model.setRowCount(0);

            int no = 1;
            int totalSiswa = 0;
            int totalAlpha = 0;

            while (rs.next()) {
                int al = rs.getInt("alpha");
                int iz = rs.getInt("izin");
                int sa = rs.getInt("sakit");
                int ha = rs.getInt("hadir");

                String risiko;
                if (al >= 5)      risiko = "⛔ SANGAT TINGGI";
                else if (al >= 3) risiko = "🔴 TINGGI";
                else              risiko = "🟡 SEDANG";

                model.addRow(new Object[]{
                    no++,
                    rs.getString("nis"),
                    rs.getString("nama"),
                    rs.getString("nama_kelas"),
                    rs.getString("wali_kelas"),
                    rs.getString("no_telp"),
                    al, iz, sa, ha,
                    risiko
                });

                totalSiswa++;
                totalAlpha += al;
            }

            lblTotalSiswa.setText(String.valueOf(totalSiswa));
            lblTotalAlpha.setText(String.valueOf(totalAlpha));

            rs.close(); ps.close(); conn.close();

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                    "Tidak ada siswa dengan alpha ≥ " + minAlpha + " hari pada periode ini.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        tfDari.setText("2026-06-01");
        tfSampai.setText("2026-06-30");
        cbKelas.setSelectedIndex(0);
        spMinAlpha.setValue(1);
        lblTotalSiswa.setText("0");
        lblTotalAlpha.setText("0");
        ((DefaultTableModel) tblData.getModel()).setRowCount(0);
    }

    private void styleComponents() {}

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ReportAlpha().setVisible(true));
    }
}
