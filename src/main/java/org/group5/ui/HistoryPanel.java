package org.group5.ui;

import org.group5.model.BorrowRecord;
import org.group5.service.LibraryData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HistoryPanel extends JPanel {
    private LibraryData library;

    private JTextField txtSearch;
    private JComboBox<String> cmbStatusFilter;
    private JTable historyTable;
    private DefaultTableModel tableModel;

    public HistoryPanel(LibraryData library) {
        this.library = library;
        initUI();
        refreshHistoryTable();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UiUtils.COLOR_BACKGROUND);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("Borrowing History & Transaction Records");
        title.setFont(UiUtils.FONT_HEADER_LARGE);
        title.setForeground(UiUtils.COLOR_TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Complete log of all book issue and return transactions with timestamps");
        subtitle.setFont(UiUtils.FONT_REGULAR);
        subtitle.setForeground(UiUtils.COLOR_TEXT_MUTED);

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Center Card Panel
        JPanel cardTable = UiUtils.createCardPanel();

        // Top Filter Bar
        JPanel filterBar = new JPanel(new BorderLayout(10, 0));
        filterBar.setOpaque(false);
        filterBar.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel searchInputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchInputs.setOpaque(false);

        JLabel lblSearch = new JLabel("Search History:");
        lblSearch.setFont(UiUtils.FONT_REGULAR_BOLD);
        lblSearch.setForeground(UiUtils.COLOR_TEXT_PRIMARY);

        txtSearch = UiUtils.createStyledTextField(18);
        JButton btnSearch = UiUtils.createStyledButton("Search", UiUtils.COLOR_PRIMARY, Color.WHITE);
        JButton btnReset = UiUtils.createStyledButton("Reset", UiUtils.COLOR_SECONDARY, Color.WHITE);

        searchInputs.add(lblSearch);
        searchInputs.add(txtSearch);
        searchInputs.add(btnSearch);
        searchInputs.add(btnReset);

        filterBar.add(searchInputs, BorderLayout.WEST);

        // Status filter
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        statusPanel.setOpaque(false);
        JLabel lblStatus = new JLabel("Filter Status:");
        lblStatus.setFont(UiUtils.FONT_REGULAR_BOLD);
        lblStatus.setForeground(UiUtils.COLOR_TEXT_MUTED);

        String[] statuses = {"All Statuses", "Borrowed", "Returned"};
        cmbStatusFilter = new JComboBox<String>(statuses);
        cmbStatusFilter.setFont(UiUtils.FONT_REGULAR);
        cmbStatusFilter.setBackground(Color.WHITE);

        statusPanel.add(lblStatus);
        statusPanel.add(cmbStatusFilter);
        filterBar.add(statusPanel, BorderLayout.EAST);

        cardTable.add(filterBar, BorderLayout.NORTH);

        // Table
        String[] columns = {"Record ID", "Book ID", "Book Title", "Member ID", "Member Name", "Borrow Date", "Return Date", "Record Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(tableModel);
        UiUtils.formatTable(historyTable);
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(75);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(170);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(75);
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(125);
        historyTable.getColumnModel().getColumn(5).setPreferredWidth(85);
        historyTable.getColumnModel().getColumn(6).setPreferredWidth(85);
        historyTable.getColumnModel().getColumn(7).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        cardTable.add(scrollPane, BorderLayout.CENTER);

        // Bottom Actions
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        JLabel lblTotalRecords = new JLabel("Records are updated in real-time upon book issue and return.");
        lblTotalRecords.setFont(UiUtils.FONT_SMALL);
        lblTotalRecords.setForeground(UiUtils.COLOR_TEXT_MUTED);
        bottomPanel.add(lblTotalRecords, BorderLayout.WEST);

        JButton btnRefresh = UiUtils.createStyledButton("Refresh Log", UiUtils.COLOR_SECONDARY, Color.WHITE);
        bottomPanel.add(btnRefresh, BorderLayout.EAST);
        cardTable.add(bottomPanel, BorderLayout.SOUTH);

        add(cardTable, BorderLayout.CENTER);

        // Events
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilter();
            }
        });

        txtSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilter();
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearch.setText("");
                cmbStatusFilter.setSelectedIndex(0);
                refreshHistoryTable();
            }
        });

        cmbStatusFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilter();
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshHistoryTable();
            }
        });
    }

    private void applyFilter() {
        String query = txtSearch.getText().trim().toLowerCase();
        String status = (String) cmbStatusFilter.getSelectedItem();

        List<BorrowRecord> all = library.getAllBorrowRecords();
        tableModel.setRowCount(0);

        for (BorrowRecord r : all) {
            boolean matchesStatus = (status == null || "All Statuses".equals(status) || r.getStatus().equalsIgnoreCase(status));
            if (!matchesStatus) continue;

            if (query.isEmpty() ||
                r.getRecordId().toLowerCase().contains(query) ||
                r.getBook().getBookId().toLowerCase().contains(query) ||
                r.getBook().getTitle().toLowerCase().contains(query) ||
                r.getMember().getMemberId().toLowerCase().contains(query) ||
                r.getMember().getName().toLowerCase().contains(query) ||
                r.getBorrowDate().toLowerCase().contains(query)) {

                tableModel.addRow(new Object[]{
                        r.getRecordId(),
                        r.getBook().getBookId(),
                        r.getBook().getTitle(),
                        r.getMember().getMemberId(),
                        r.getMember().getName(),
                        r.getBorrowDate(),
                        r.getReturnDate(),
                        r.getStatus()
                });
            }
        }
    }

    public void refreshHistoryTable() {
        tableModel.setRowCount(0);
        List<BorrowRecord> all = library.getAllBorrowRecords();
        for (BorrowRecord r : all) {
            tableModel.addRow(new Object[]{
                    r.getRecordId(),
                    r.getBook().getBookId(),
                    r.getBook().getTitle(),
                    r.getMember().getMemberId(),
                    r.getMember().getName(),
                    r.getBorrowDate(),
                    r.getReturnDate(),
                    r.getStatus()
            });
        }
    }
}

