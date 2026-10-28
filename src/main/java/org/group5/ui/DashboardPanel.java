package org.group5.ui;

<<<<<<< Updated upstream
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.group5.model.BorrowRecord;

public class DashboardPanel extends JPanel {

     private LibraryData librarydata;

    private JLabel lblTotalBooks;
    private JLabel lblAvailableBooks;
    private JLabel lblBorrowedBooks;
    private JLabel lblTotalMembers;

    private JTable recentBorrowTable;
    private DefaultTableModel tableModel;

    public DashboardPanel(LibraryData librarydata) {
        this.librarydata = librarydata;
        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("System Overview & Dashboard");
        title.setFont(UIUtils.FONT_HEADER_LARGE);
        title.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Real-time summary of books, members, and active borrowing transactions");
        subtitle.setFont(UIUtils.FONT_REGULAR);
        subtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Center Content Panel
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);

        // 4 Stat Cards in a Grid
        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 18, 0));
        statsGrid.setOpaque(false);

        lblTotalBooks = new JLabel("0");
        lblAvailableBooks = new JLabel("0");
        lblBorrowedBooks = new JLabel("0");
        lblTotalMembers = new JLabel("0");

        statsGrid.add(UIUtils.createStatCard("Total Books", lblTotalBooks, "In Library Catalog", UIUtils.COLOR_PRIMARY));
        statsGrid.add(UIUtils.createStatCard("Available Books", lblAvailableBooks, "Ready to Borrow", UIUtils.COLOR_SUCCESS));
        statsGrid.add(UIUtils.createStatCard("Borrowed Books", lblBorrowedBooks, "Currently Issued", UIUtils.COLOR_DANGER));
        statsGrid.add(UIUtils.createStatCard("Registered Members", lblTotalMembers, "Active Students/Staff", UIUtils.COLOR_WARNING));

        centerPanel.add(statsGrid, BorderLayout.NORTH);

        // Bottom Table Card: Active Borrowings
        JPanel cardTable = UIUtils.createCardPanel();
        
        JPanel tableHeaderPanel = new JPanel(new BorderLayout());
        tableHeaderPanel.setOpaque(false);
        
        JLabel tableTitle = new JLabel("Active Borrowed Books & Recent Activity");
        tableTitle.setFont(UIUtils.FONT_HEADER);
        tableTitle.setForeground(UIUtils.COLOR_TEXT_PRIMARY);
        
        JLabel tableSubtitle = new JLabel("Showing recent borrowing transactions and current status");
        tableSubtitle.setFont(UIUtils.FONT_SMALL);
        tableSubtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);

        tableHeaderPanel.add(tableTitle, BorderLayout.NORTH);
        tableHeaderPanel.add(tableSubtitle, BorderLayout.SOUTH);
        tableHeaderPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        cardTable.add(tableHeaderPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Record ID", "Book ID", "Book Title", "Member Name", "Borrow Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recentBorrowTable = new JTable(tableModel);
        UIUtils.formatTable(recentBorrowTable);
        recentBorrowTable.getColumnModel().getColumn(0).setPreferredWidth(85);
        recentBorrowTable.getColumnModel().getColumn(1).setPreferredWidth(65);
        recentBorrowTable.getColumnModel().getColumn(2).setPreferredWidth(220);
        recentBorrowTable.getColumnModel().getColumn(3).setPreferredWidth(140);
        recentBorrowTable.getColumnModel().getColumn(4).setPreferredWidth(95);
        recentBorrowTable.getColumnModel().getColumn(5).setPreferredWidth(85);

        JScrollPane scrollPane = new JScrollPane(recentBorrowTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        cardTable.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(cardTable, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    /*
      Refreshes stats and table values from the Library instance.
     */
    
    public void refreshData() {
        lblTotalBooks.setText(String.valueOf(library.getTotalBooksCount()));
        lblAvailableBooks.setText(String.valueOf(library.getAvailableBooksCount()));
        lblBorrowedBooks.setText(String.valueOf(library.getBorrowedBooksCount()));
        lblTotalMembers.setText(String.valueOf(library.getTotalMembersCount()));

        tableModel.setRowCount(0);
        List<BorrowRecord> records = library.getAllBorrowRecords();
        int limit = Math.min(records.size(), 10);
        for (int i = 0; i < limit; i++) {
            BorrowRecord r = records.get(i);
            tableModel.addRow(new Object[]{
                    r.getRecordId(),
                    r.getBook().getBookId(),
                    r.getBook().getTitle(),
                    r.getMember().getName(),
                    r.getBorrowDate(),
                    r.getStatus()
            });
        }
    }

    
}
=======
import org.group5.model.BorrowRecord;
import org.group5.service.DashboardService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * DashboardPanel displays key statistics (Total Books, Total Members,
 * Active Borrows, Overdue Books) and recent borrowing activity.
 */
public class DashboardPanel extends JPanel {

    private final DashboardService dashboardService;

    // Stat Card Labels
    private JLabel totalBooksLabel;
    private JLabel totalMembersLabel;
    private JLabel activeBorrowsLabel;
    private JLabel overdueBorrowsLabel;

    // Recent Activity Table
    private JTable recentTable;
    private DefaultTableModel tableModel;

    public DashboardPanel(DashboardService dashboardService) {
        this.dashboardService = dashboardService;

        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(25, 25, 25, 25));
        setBackground(new Color(245, 247, 250)); // Soft modern background

        initComponents();
        refreshData();
    }

    private void initComponents() {
        // --- 1. Header Section ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Library Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(33, 37, 41));

        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> refreshData());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);

        // --- 2. Stat Cards Grid ---
        JPanel cardsGrid = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsGrid.setOpaque(false);

        totalBooksLabel = new JLabel("0", SwingConstants.CENTER);
        totalMembersLabel = new JLabel("0", SwingConstants.CENTER);
        activeBorrowsLabel = new JLabel("0", SwingConstants.CENTER);
        overdueBorrowsLabel = new JLabel("0", SwingConstants.CENTER);

        // Add 4 styled summary cards
        cardsGrid.add(createStatCard("Total Books", totalBooksLabel, new Color(41, 128, 185)));
        cardsGrid.add(createStatCard("Total Members", totalMembersLabel, new Color(39, 174, 96)));
        cardsGrid.add(createStatCard("Active Borrows", activeBorrowsLabel, new Color(243, 156, 18)));
        cardsGrid.add(createStatCard("Overdue Books", overdueBorrowsLabel, new Color(192, 57, 43)));

        // Combine Header and Cards into Top Section
        JPanel topSection = new JPanel(new BorderLayout(0, 20));
        topSection.setOpaque(false);
        topSection.add(headerPanel, BorderLayout.NORTH);
        topSection.add(cardsGrid, BorderLayout.CENTER);

        add(topSection, BorderLayout.NORTH);

        // --- 3. Recent Activity Section ---
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);

        JLabel tableTitle = new JLabel("Recent Borrowing Activity");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(new Color(52, 73, 94));

        String[] columnNames = {"Record ID", "Book ID", "Member ID", "Borrow Date", "Due Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        recentTable = new JTable(tableModel);
        recentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        recentTable.setRowHeight(30);
        recentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        recentTable.getTableHeader().setBackground(new Color(236, 240, 241));

        JScrollPane scrollPane = new JScrollPane(recentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230)));

        centerPanel.add(tableTitle, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Helper method to build styled summary cards
     */
    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 0, 0, accentColor), // Color accent top bar
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(127, 140, 141));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(accentColor);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Refreshes stats and table content from DashboardService
     */
    public void refreshData() {
        if (dashboardService == null) return;

        try {
            // Update Metric Cards (adjust method names if your group leader named them slightly differently)
            totalBooksLabel.setText(String.valueOf(dashboardService.getTotalBooks()));
            totalMembersLabel.setText(String.valueOf(dashboardService.getTotalMembers()));
            activeBorrowsLabel.setText(String.valueOf(dashboardService.getActiveBorrowsCount()));
            overdueBorrowsLabel.setText(String.valueOf(dashboardService.getOverdueBorrowsCount()));

            // Populate Recent Activity Table
            tableModel.setRowCount(0); // Clear existing rows
            List<BorrowRecord> recentRecords = dashboardService.getRecentBorrowRecords();
            if (recentRecords != null) {
                for (BorrowRecord record : recentRecords) {
                    tableModel.addRow(new Object[]{
                            record.getId(),
                            record.getBookId(),
                            record.getMemberId(),
                            record.getBorrowDate(),
                            record.getDueDate(),
                            record.isReturned() ? "Returned" : "Active"
                    });
                }
            }
        } catch (Exception e) {
            // Catch gently if service methods are still being written by teammates
            System.err.println("Dashboard refresh error: " + e.getMessage());
        }
    }
}
>>>>>>> Stashed changes
