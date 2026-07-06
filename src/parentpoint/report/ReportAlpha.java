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

    private com.toedter.calendar.JDateChooser dcDari, dcSampai;
    private JComboBox<String> cbKelas;
    private JSpinner spMinAlpha;
    private JTextField tfCariSiswa;
    private JButton btnTampilkan, btnReset, btnCetak;
    private JTable tblData;
    private JLabel lblTotalSiswa, lblTotalAlpha, lblKelasTerendah;

    public ReportAlpha() {
        initComponents();
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        styleComponents();
        loadKelas();
    }

    private void initComponents() {
        setTitle("PARENT POINT - Laporan Siswa Sering Absen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
// setSize removed for responsiveness
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
        JPanel pnlFilter = new JPanel(new java.awt.GridLayout(2, 1));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel row1 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));
        row1.setBackground(Color.WHITE);
        row1.add(createLabel("Dari Tanggal:"));
        dcDari = new com.toedter.calendar.JDateChooser();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        dcDari.setDate(cal.getTime());
        dcDari.setDateFormatString("yyyy-MM-dd");
        dcDari.setFont(DesignUtil.FONT_BODY);
        dcDari.setPreferredSize(new Dimension(130, 35));
        row1.add(dcDari);

        row1.add(createLabel("Sampai:"));
        dcSampai = new com.toedter.calendar.JDateChooser(new java.util.Date());
        dcSampai.setDateFormatString("yyyy-MM-dd");
        dcSampai.setFont(DesignUtil.FONT_BODY);
        dcSampai.setPreferredSize(new Dimension(130, 35));
        row1.add(dcSampai);

        row1.add(createLabel("Kelas:"));
        cbKelas = new JComboBox<>();
        cbKelas.setFont(DesignUtil.FONT_BODY);
        cbKelas.setPreferredSize(new Dimension(130, 35));
        row1.add(cbKelas);

        row1.add(createLabel("Min. Alpha ≥"));
        spMinAlpha = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spMinAlpha.setFont(DesignUtil.FONT_BODY);
        spMinAlpha.setPreferredSize(new Dimension(60, 35));
        row1.add(spMinAlpha);
        row1.add(createLabel("hari"));

        JPanel row2 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));
        row2.setBackground(Color.WHITE);
        row2.add(createLabel("Cari Siswa:"));
        tfCariSiswa = createTextField("");
        tfCariSiswa.setPreferredSize(new Dimension(200, 35));
        row2.add(tfCariSiswa);

        btnTampilkan = DesignUtil.createButton("🔍 Cari", DesignUtil.DANGER);
        btnTampilkan.setPreferredSize(new Dimension(100, 35));
        btnTampilkan.addActionListener(e -> tampilkanLaporan());
        row2.add(btnTampilkan);

        btnReset = DesignUtil.createButton("↺ Reset", DesignUtil.WARNING);
        btnReset.setPreferredSize(new Dimension(100, 35));
        btnReset.addActionListener(e -> resetForm());
        row2.add(btnReset);

        btnCetak = DesignUtil.createButton("🖨 Cetak", DesignUtil.PRIMARY);
        btnCetak.setPreferredSize(new Dimension(100, 35));
        btnCetak.addActionListener(e -> cetakLaporan());
        row2.add(btnCetak);

        pnlFilter.add(row1);
        pnlFilter.add(row2);

        pnlBody.add(pnlFilter, BorderLayout.NORTH);
        
        String role = parentpoint.util.Session.getRole();
        if ("orang_tua".equalsIgnoreCase(role) || "siswa".equalsIgnoreCase(role)) {
            cbKelas.setEnabled(false);
            tfCariSiswa.setEnabled(false);
            tfCariSiswa.setText(parentpoint.util.Session.getNamaSiswa());
        }

        // --- SUMMARY CARDS ---
        JPanel pnlCards = new JPanel(new java.awt.GridLayout(1, 3, 15, 0));
        pnlCards.setBackground(DesignUtil.BG_MAIN);
        pnlCards.setPreferredSize(new Dimension(0, 100));

        lblTotalSiswa = new JLabel("0");
        lblTotalAlpha = new JLabel("0");
        lblKelasTerendah = new JLabel("-");

        pnlCards.add(buatCard("SISWA PERLU PERHATIAN", lblTotalSiswa, DesignUtil.DANGER));
        pnlCards.add(buatCard("TOTAL KEJADIAN ALPHA", lblTotalAlpha, new Color(180, 40, 40)));
        pnlCards.add(buatCard("KELAS TERBANYAK ALPHA", lblKelasTerendah, DesignUtil.WARNING));

        JPanel pnlCenter = new JPanel(new BorderLayout(0, 15));
        pnlCenter.setBackground(DesignUtil.BG_MAIN);
        pnlCenter.add(pnlCards, BorderLayout.NORTH);

        // --- TABLE ---
        String[] cols = {"#", "NIS", "Nama Siswa", "Kelas", "Wali Kelas", "No. HP Ortu", "Jml Alpha", "Jml Izin", "Jml Sakit", "Total Hadir", "% Alpha", "Status Risiko"};
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
        for (int i = 6; i <= 10; i++) tblData.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        // Color renderer for "Status Risiko" column (col 11)
        tblData.getColumnModel().getColumn(11).setCellRenderer(new DefaultTableCellRenderer() {
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
        String dari = dcDari.getDate() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(dcDari.getDate()) : "";
        String sampai = dcSampai.getDate() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(dcSampai.getDate()) : "";
        String kelas = (String) cbKelas.getSelectedItem();
        int minAlpha = (int) spMinAlpha.getValue();
        String cari = tfCariSiswa.getText().trim();

        if (dari.isEmpty() || sampai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal harus diisi dengan benar!", "Peringatan", JOptionPane.WARNING_MESSAGE);
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
                sql += "WHERE k.nama_kelas = ? ";
            }
            if (!cari.isEmpty()) {
                if (sql.contains("WHERE")) sql += "AND s.nama LIKE ? ";
                else sql += "WHERE s.nama LIKE ? ";
            }

            sql += "GROUP BY s.id, s.nis, s.nama, k.nama_kelas, k.wali_kelas, o.no_telp "
                + "HAVING alpha >= ? "
                + "ORDER BY alpha DESC, s.nama";

            PreparedStatement ps = conn.prepareStatement(sql);
            int paramIndex = 1;
            ps.setString(paramIndex++, dari);
            ps.setString(paramIndex++, sampai);
            if (kelas != null && !kelas.equals("Semua Kelas")) {
                ps.setString(paramIndex++, kelas);
            }
            if (!cari.isEmpty()) {
                ps.setString(paramIndex++, "%" + cari + "%");
            }
            ps.setInt(paramIndex++, minAlpha);

            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) tblData.getModel();
            model.setRowCount(0);

            int no = 1;
            int totalSiswa = 0;
            int totalAlpha = 0;
            java.util.HashMap<String, Integer> alphaPerKelas = new java.util.HashMap<>();

            while (rs.next()) {
                int al = rs.getInt("alpha");
                int iz = rs.getInt("izin");
                int sa = rs.getInt("sakit");
                int ha = rs.getInt("hadir");

                int totalHari = al + iz + sa + ha;
                String persenAlpha = "0%";
                if (totalHari > 0) {
                    double pct = ((double) al / totalHari) * 100.0;
                    persenAlpha = String.format("%.1f%%", pct);
                }

                String risiko;
                if (al >= 5)      risiko = "⛔ SANGAT TINGGI";
                else if (al >= 3) risiko = "🔴 TINGGI";
                else              risiko = "🟡 SEDANG";

                String nmKelas = rs.getString("nama_kelas");
                alphaPerKelas.put(nmKelas, alphaPerKelas.getOrDefault(nmKelas, 0) + al);

                model.addRow(new Object[]{
                    no++,
                    rs.getString("nis"),
                    rs.getString("nama"),
                    nmKelas,
                    rs.getString("wali_kelas"),
                    rs.getString("no_telp"),
                    al, iz, sa, ha,
                    persenAlpha,
                    risiko
                });

                totalSiswa++;
                totalAlpha += al;
            }

            String kelasTerendah = "-";
            int maxAlphaKelas = 0;
            for (java.util.Map.Entry<String, Integer> entry : alphaPerKelas.entrySet()) {
                if (entry.getValue() > maxAlphaKelas) {
                    maxAlphaKelas = entry.getValue();
                    kelasTerendah = entry.getKey();
                }
            }

            lblTotalSiswa.setText(String.valueOf(totalSiswa));
            lblTotalAlpha.setText(String.valueOf(totalAlpha));
            lblKelasTerendah.setText(maxAlphaKelas > 0 ? (kelasTerendah + " (" + maxAlphaKelas + " Alpha)") : "-");

            rs.close(); ps.close(); conn.close();

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                    "Tidak ada siswa dengan alpha >= " + minAlpha + " hari pada periode ini.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        dcDari.setDate(cal.getTime());
        dcSampai.setDate(new java.util.Date());
        cbKelas.setSelectedIndex(0);
        spMinAlpha.setValue(1);
        tfCariSiswa.setText("");
        lblTotalSiswa.setText("0");
        lblTotalAlpha.setText("0");
        lblKelasTerendah.setText("-");
        ((DefaultTableModel) tblData.getModel()).setRowCount(0);
    }

    private void cetakLaporan() {
        if (tblData.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk dicetak. Silakan tampilkan data terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            java.util.HashMap<String, Object> param = new java.util.HashMap<>();
            param.put("p_dari", dcDari.getDate() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(dcDari.getDate()) : "-");
            param.put("p_sampai", dcSampai.getDate() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(dcSampai.getDate()) : "-");
            
            net.sf.jasperreports.engine.data.JRTableModelDataSource dataSource = new net.sf.jasperreports.engine.data.JRTableModelDataSource(tblData.getModel());
            
            java.io.InputStream reportStream = getClass().getResourceAsStream("/parentpoint/report/report_alpha.jrxml");
            if (reportStream == null) throw new java.io.FileNotFoundException("File report_alpha.jrxml tidak ditemukan di classpath.");
            net.sf.jasperreports.engine.design.JasperDesign jd = net.sf.jasperreports.engine.xml.JRXmlLoader.load(reportStream);
            net.sf.jasperreports.engine.JasperReport jr = net.sf.jasperreports.engine.JasperCompileManager.compileReport(jd);
            
            net.sf.jasperreports.engine.JasperPrint jp = net.sf.jasperreports.engine.JasperFillManager.fillReport(jr, param, dataSource);
            net.sf.jasperreports.view.JasperViewer.viewReport(jp, false);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mencetak laporan:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleComponents() {
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ReportAlpha().setVisible(true));
    }
}
