package org.group5.ui;

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
