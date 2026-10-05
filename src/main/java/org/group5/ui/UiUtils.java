package org.group5.ui;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UiUtils {

    // Color Palette
    public static final Color COLOR_PRIMARY = new Color(37, 99, 235);       // Royal Blue #2563EB
    public static final Color COLOR_PRIMARY_DARK = new Color(29, 78, 216);  // Darker Blue
    public static final Color COLOR_SECONDARY = new Color(71, 85, 105);     // Slate Gray
    public static final Color COLOR_BACKGROUND = new Color(248, 250, 252);  // Very light gray/blue #F8FAFC
    public static final Color COLOR_SURFACE = Color.WHITE;
    public static final Color COLOR_SIDEBAR = new Color(15, 23, 42);        // Deep Navy #0F172A
    public static final Color COLOR_SIDEBAR_HOVER = new Color(30, 41, 59);  // Slate Dark #1E293B
    public static final Color COLOR_SIDEBAR_ACTIVE = new Color(37, 99, 235);
    public static final Color COLOR_BORDER = new Color(226, 232, 240);       // Soft border #E2E8F0
    public static final Color COLOR_TEXT_PRIMARY = new Color(15, 23, 42);   // Dark Slate #0F172A
    public static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);  // Slate #64748B
    public static final Color COLOR_SUCCESS = new Color(16, 185, 129);      // Emerald Green
    public static final Color COLOR_DANGER = new Color(239, 68, 68);        // Rose Red
    public static final Color COLOR_WARNING = new Color(245, 158, 11);      // Amber Orange

    // Modern Fonts (Standard on Windows/Java)
    public static final Font FONT_HEADER_LARGE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_REGULAR_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_STAT_NUMBER = new Font("Segoe UI", Font.BOLD, 28);

    /*
      Creates a modern styled button with custom painting, hover effects, and rounded corners.
     */

    public static JButton createStyledButton(final String text, final Color bgColor, final Color fgColor) {
        final JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (!isEnabled()) {
                    g2.setColor(new Color(203, 213, 225));
                } else if (getModel().isPressed()) {
                    g2.setColor(darken(bgColor, 0.75f));
                } else if (getModel().isRollover()) {
                    g2.setColor(darken(bgColor, 0.88f));
                } else {
                    g2.setColor(bgColor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(FONT_REGULAR_BOLD);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 18, 8, 18));

        return button;
    }

    /*
      Creates a styled text field with comfortable padding and clean border.
     */

    public static JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(FONT_REGULAR);
        textField.setForeground(COLOR_TEXT_PRIMARY);
        textField.setBackground(Color.WHITE);
        Border line = new LineBorder(COLOR_BORDER, 1);
        Border margin = new EmptyBorder(6, 10, 6, 10);
        textField.setBorder(new CompoundBorder(line, margin));
        return textField;
    }

    /*
      Creates a clean container panel with white background and subtle rounded border.
     */

    public static JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(COLOR_SURFACE);
        card.setLayout(new BorderLayout(15, 15));
        card.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(18, 20, 18, 20)
        ));
        return card;
    }

    /*
      Creates a Dashboard Stat Card.
     */
    public static JPanel createStatCard(String title, JLabel valueLabel, String subtitle, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 8));
        card.setBackground(COLOR_SURFACE);
        card.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1),
                new EmptyBorder(16, 20, 16, 20)
        ));

        // Left color accent bar
        JPanel bar = new JPanel();
        bar.setPreferredSize(new Dimension(5, 0));
        bar.setBackground(accentColor);
        card.add(bar, BorderLayout.WEST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_REGULAR_BOLD);
        lblTitle.setForeground(COLOR_TEXT_MUTED);

        valueLabel.setFont(FONT_STAT_NUMBER);
        valueLabel.setForeground(COLOR_TEXT_PRIMARY);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(FONT_SMALL);
        lblSub.setForeground(COLOR_TEXT_MUTED);

        content.add(lblTitle);
        content.add(Box.createVerticalStrut(4));
        content.add(valueLabel);
        content.add(Box.createVerticalStrut(2));
        content.add(lblSub);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    /*
      Formats JTable to look modern and readable.
     */

    public static void formatTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(34);
        table.setSelectionBackground(new Color(224, 231, 255)); // Soft indigo highlight
        table.setSelectionForeground(COLOR_TEXT_PRIMARY);
        table.setGridColor(new Color(241, 245, 249));
        table.setShowGrid(true);
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_REGULAR_BOLD);
        header.setBackground(new Color(241, 245, 249));
        header.setForeground(COLOR_TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));
        header.setReorderingAllowed(false);

        // Custom cell renderer for alternating row background & badge highlighting
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
                setBorder(new EmptyBorder(0, 10, 0, 10));

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(249, 250, 251));
                    }
                }

                // Check for Status column rendering (Available / Borrowed)
                String str = (value != null) ? value.toString() : "";
                if ("Available".equalsIgnoreCase(str)) {
                    c.setForeground(new Color(13, 148, 136)); // Teal/Emerald
                    setFont(FONT_REGULAR_BOLD);
                } else if ("Borrowed".equalsIgnoreCase(str)) {
                    c.setForeground(new Color(225, 29, 72)); // Rose red
                    setFont(FONT_REGULAR_BOLD);
                } else if ("Returned".equalsIgnoreCase(str)) {
                    c.setForeground(new Color(37, 99, 235)); // Blue
                    setFont(FONT_REGULAR_BOLD);
                } else {
                    if (!isSelected) {
                        c.setForeground(COLOR_TEXT_PRIMARY);
                    }
                    setFont(FONT_REGULAR);
                }

                return c;
            }
        });
    }

    /*
      Helper to darken a color for hover state.
     */

    public static Color darken(Color color, float factor) {
        return new Color(
                Math.max((int)(color.getRed() * factor), 0),
                Math.max((int)(color.getGreen() * factor), 0),
                Math.max((int)(color.getBlue() * factor), 0)
        );
    }
}
