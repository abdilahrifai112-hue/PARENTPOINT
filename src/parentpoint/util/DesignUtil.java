/*
 * DesignUtil - Utility class untuk standarisasi desain UI seluruh aplikasi
 * Warna, font, border, dan styling yang konsisten
 */
package parentpoint.util;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * @author HP
 */
public class DesignUtil {

    // ==================== WARNA (2025 SaaS Theme) ====================
    public static final Color PRIMARY = new Color(37, 99, 235);       // #2563EB
    public static final Color PRIMARY_LIGHT = new Color(59, 130, 246); // varian primary-light
    public static final Color ACCENT = new Color(37, 99, 235);        // #2563EB (accent utama)
    public static final Color ACCENT_HOVER = new Color(29, 78, 216); // hover blue

    public static final Color SUCCESS = new Color(16, 185, 129);       // #10B981
    public static final Color WARNING = new Color(245, 158, 11);       // #F59E0B
    public static final Color DANGER = new Color(239, 68, 68);        // #EF4444
    public static final Color INFO = new Color(37, 99, 235);          // bisa dipakai untuk accent informasi

    public static final Color BG_MAIN = new Color(248, 250, 252);      // #F8FAFC
    public static final Color BG_SIDEBAR = new Color(30, 41, 59);     // #1E293B
    public static final Color BG_CARD = Color.WHITE;
    public static final Color BG_HEADER = Color.WHITE;               // header putih sesuai spec

    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);      // #0F172A
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    public static final Color TEXT_WHITE = Color.WHITE;
    public static final Color TEXT_LIGHT = new Color(148, 163, 184);

    public static final Color BORDER_COLOR = new Color(226, 232, 240);
    public static final Color TABLE_HEADER_BG = new Color(37, 99, 235); // primary
    public static final Color TABLE_ALT_ROW = new Color(248, 250, 252);

    
    // ==================== FONT ====================
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SIDEBAR = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font FONT_CARD_NUMBER = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font FONT_CARD_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    
    // ==================== STYLING METHODS ====================
    
    /**
     * Setup JFrame standar
     */
    public static void setupFrame(JFrame frame, String title) {
        frame.setTitle("PARENT POINT - " + title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(BG_MAIN);
        frame.setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
    }
    
    /**
     * Buat tombol dengan style standar
     */
    public static JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(bgColor);
        btn.setForeground(TEXT_WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 38));
        
        Color hoverColor = bgColor.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }
    
    /**
     * Buat text field dengan style standar
     */
    public static JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_BODY);
        tf.setPreferredSize(new Dimension(250, 35));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return tf;
    }
    
    /**
     * Buat password field dengan style standar
     */
    public static JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(FONT_BODY);
        pf.setPreferredSize(new Dimension(250, 35));
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return pf;
    }
    
    /**
     * Buat label standar
     */
    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BODY);
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }
    
    /**
     * Buat panel kartu (card) dengan shadow
     */
    public static JPanel createCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        card.setPreferredSize(new Dimension(200, 120));
        
        // Accent bar di atas
        JPanel accentBar = new JPanel();
        accentBar.setBackground(accentColor);
        accentBar.setPreferredSize(new Dimension(0, 4));
        card.add(accentBar, BorderLayout.NORTH);
        
        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_CARD);
        content.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_CARD_LABEL);
        lblTitle.setForeground(TEXT_SECONDARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(FONT_CARD_NUMBER);
        lblValue.setForeground(accentColor);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        content.add(lblTitle);
        content.add(Box.createVerticalStrut(5));
        content.add(lblValue);
        
        card.add(content, BorderLayout.CENTER);
        
        return card;
    }
    
    /**
     * Style JTable dengan desain modern
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(210, 227, 252));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(240, 240, 240));
        table.setShowHorizontalLines(true);
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 40));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                setBackground(TABLE_HEADER_BG);
                setForeground(TEXT_WHITE);
                setFont(FONT_TABLE_HEADER);
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY),
                    BorderFactory.createEmptyBorder(0, 10, 0, 10)
                ));
                return this;
            }
        });
        
        // Alternate row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT_ROW);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
    }
    
    /**
     * Buat sidebar button
     */
    public static JButton createSidebarButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_SIDEBAR);
        btn.setForeground(isActive ? TEXT_WHITE : TEXT_LIGHT);
        btn.setBackground(isActive ? PRIMARY_LIGHT : BG_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setPreferredSize(new Dimension(220, 50));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 10));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!isActive) btn.setBackground(PRIMARY_LIGHT);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!isActive) btn.setBackground(BG_SIDEBAR);
            }
        });
        
        return btn;
    }
    
    /**
     * Buat header panel
     */
    public static JPanel createHeaderPanel(String title) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_HEADER);
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_SUBTITLE);
        lblTitle.setForeground(TEXT_WHITE);
        header.add(lblTitle, BorderLayout.WEST);
        
        return header;
    }
    
    /**
     * Buat ComboBox styled
     */
    public static JComboBox<String> createComboBox() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setFont(FONT_BODY);
        cb.setPreferredSize(new Dimension(250, 35));
        cb.setBackground(Color.WHITE);
        return cb;
    }
}
