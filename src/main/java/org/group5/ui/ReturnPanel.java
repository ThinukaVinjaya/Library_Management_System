package org.group5.ui;

import org.group5.model.Book;
import org.group5.model.BorrowRecord;
import org.group5.service.Library;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;




public class ReturnPanel extends JPanel {
    private Library library;
    private Runnable onDataChangedCallback;

    private JComboBox<String> cmbBorrowedBooks;
    private JTextField txtReturnDate;

    private JLabel lblRecordId;
    private JLabel lblBookTitle;
    private JLabel lblBorrower;
    private JLabel lblBorrowDate;

    private JTable borrowedBooksTable;
    private DefaultTableModel tableModel;

    public ReturnPanel(Library library, Runnable onDataChangedCallback) {
        this.library = library;
        this.onDataChangedCallback = onDataChangedCallback;
        initUI();
        refreshReturnOptions();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("Return Book");
        title.setFont(UIUtils.FONT_HEADER_LARGE);
        title.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Select an issued book to process return and mark it Available in the catalog");
        subtitle.setFont(UIUtils.FONT_REGULAR);
        subtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Center split layout: Left Form (380px), Right Borrowed Books Table
        JPanel mainContent = new JPanel(new BorderLayout(20, 0));
        mainContent.setOpaque(false);

        // === LEFT PANEL: RETURN FORM ===
        JPanel formCard = UIUtils.createCardPanel();
        formCard.setPreferredSize(new Dimension(320, 0));

        JLabel formTitle = new JLabel("Return Processing");
        formTitle.setFont(UIUtils.FONT_HEADER);
        formTitle.setForeground(UIUtils.COLOR_TEXT_PRIMARY);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel formBody = new JPanel();
        formBody.setOpaque(false);
        formBody.setLayout(new BoxLayout(formBody, BoxLayout.Y_AXIS));

        // Borrowed book selection combo
        cmbBorrowedBooks = new JComboBox<String>();
        cmbBorrowedBooks.setFont(UIUtils.FONT_REGULAR);
        cmbBorrowedBooks.setBackground(Color.WHITE);

        // Return Date field
        txtReturnDate = UIUtils.createStyledTextField(12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        txtReturnDate.setText(sdf.format(new Date()));

        formBody.add(createFormField("Select Borrowed Book *", cmbBorrowedBooks));
        formBody.add(Box.createVerticalStrut(12));
        formBody.add(createFormField("Return Date (YYYY-MM-DD) *", txtReturnDate));
        formBody.add(Box.createVerticalStrut(15));

        // Loan information preview card
        JPanel previewCard = new JPanel(new GridBagLayout());
        previewCard.setBackground(new Color(241, 245, 249));
        previewCard.setBorder(new EmptyBorder(10, 12, 10, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 4, 3, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblRecordId = new JLabel("-");
        lblRecordId.setFont(UIUtils.FONT_REGULAR_BOLD);
        lblBookTitle = new JLabel("-");
        lblBookTitle.setFont(UIUtils.FONT_REGULAR_BOLD);
        lblBorrower = new JLabel("-");
        lblBorrower.setFont(UIUtils.FONT_REGULAR);
        lblBorrowDate = new JLabel("-");
        lblBorrowDate.setFont(UIUtils.FONT_REGULAR);

        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        previewCard.add(createDetailLabel("Record ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        previewCard.add(lblRecordId, gbc);

        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        previewCard.add(createDetailLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        previewCard.add(lblBookTitle, gbc);

        
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        previewCard.add(createDetailLabel("Borrower:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        previewCard.add(lblBorrower, gbc);

        
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        previewCard.add(createDetailLabel("Date:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        previewCard.add(lblBorrowDate, gbc);

        formBody.add(previewCard);
        formBody.add(Box.createVerticalStrut(18));

        // Action Buttons Stacked Vertically
        JButton btnReturn = UIUtils.createStyledButton("Confirm Return", UIUtils.COLOR_SUCCESS, Color.WHITE);
        JButton btnReset = UIUtils.createStyledButton("Reset Form", UIUtils.COLOR_SECONDARY, Color.WHITE);

        btnReturn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnReturn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnReset.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btnReset.setAlignmentX(Component.LEFT_ALIGNMENT);

        formBody.add(btnReturn);
        formBody.add(Box.createVerticalStrut(8));
        formBody.add(btnReset);
        formBody.add(Box.createVerticalGlue()); // Absorbs extra vertical space
        formCard.add(formBody, BorderLayout.CENTER);
        mainContent.add(formCard, BorderLayout.WEST);

        // === RIGHT PANEL: CURRENTLY BORROWED BOOKS TABLE ===
        JPanel tableCard = UIUtils.createCardPanel();

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setOpaque(false);
        JLabel tableTitle = new JLabel("Currently Borrowed Books Awaiting Return");
        tableTitle.setFont(UIUtils.FONT_HEADER);
        tableTitle.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        JLabel tableSub = new JLabel("Click any row below to quickly load it into the return form");
        tableSub.setFont(UIUtils.FONT_SMALL);
        tableSub.setForeground(UIUtils.COLOR_TEXT_MUTED);

        tableHeader.add(tableTitle, BorderLayout.NORTH);
        tableHeader.add(tableSub, BorderLayout.SOUTH);
        tableHeader.setBorder(new EmptyBorder(0, 0, 12, 0));
        tableCard.add(tableHeader, BorderLayout.NORTH);

        String[] columns = {"Record ID", "Book ID", "Title", "Member ID", "Member Name", "Borrow Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        borrowedBooksTable = new JTable(tableModel);
        UIUtils.formatTable(borrowedBooksTable);
        borrowedBooksTable.getColumnModel().getColumn(0).setPreferredWidth(85);
        borrowedBooksTable.getColumnModel().getColumn(1).setPreferredWidth(85);
        borrowedBooksTable.getColumnModel().getColumn(2).setPreferredWidth(170);
        borrowedBooksTable.getColumnModel().getColumn(3).setPreferredWidth(95);
        borrowedBooksTable.getColumnModel().getColumn(4).setPreferredWidth(130);
        borrowedBooksTable.getColumnModel().getColumn(5).setPreferredWidth(105);

        JScrollPane scrollPane = new JScrollPane(borrowedBooksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        mainContent.add(tableCard, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        // === EVENT LISTENERS ===

        cmbBorrowedBooks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLoanPreview();
            }
        });

        // Quick select from table row click
        borrowedBooksTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && borrowedBooksTable.getSelectedRow() != -1) {
                String bookId = (String) tableModel.getValueAt(borrowedBooksTable.getSelectedRow(), 1);
                selectBookInCombo(bookId);
            }
        });

        btnReturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleReturnBook();
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshReturnOptions();
            }
        });
    }

    private JPanel createFormField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(UIUtils.FONT_REGULAR_BOLD);
        label.setForeground(UIUtils.COLOR_TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        panel.add(label);
        panel.add(Box.createVerticalStrut(4));
        panel.add(field);
        return panel;
    }

    private JLabel createDetailLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIUtils.FONT_SMALL);
        l.setForeground(UIUtils.COLOR_TEXT_MUTED);
        return l;
    }

    private void updateLoanPreview() {
        String selected = (String) cmbBorrowedBooks.getSelectedItem();
        if (selected == null || selected.startsWith("--")) {
            lblRecordId.setText("-");
            lblBookTitle.setText("-");
            lblBorrower.setText("-");
            lblBorrowDate.setText("-");
            return;
        }

        String bookId = extractId(selected);
        Book book = library.findBookById(bookId);
        BorrowRecord record = library.getActiveRecordForBook(bookId);

        if (book != null) {
            lblBookTitle.setText(book.getTitle());
        }
        if (record != null) {
            lblRecordId.setText(record.getRecordId());
            lblBorrower.setText(record.getMember().getName() + " (" + record.getMember().getMemberId() + ")");
            lblBorrowDate.setText(record.getBorrowDate());
        } else {
            lblRecordId.setText("-");
            lblBorrower.setText("-");
            lblBorrowDate.setText("-");
        }
    }

    private void selectBookInCombo(String bookId) {
        for (int i = 0; i < cmbBorrowedBooks.getItemCount(); i++) {
            String item = cmbBorrowedBooks.getItemAt(i);
            if (item.startsWith("[" + bookId + "]") || item.startsWith(bookId + " -")) {
                cmbBorrowedBooks.setSelectedIndex(i);
                break;
            }
        }
    }

    private String extractId(String itemText) {
        if (itemText == null) return "";
        if (itemText.startsWith("[")) {
            int end = itemText.indexOf("]");
            if (end != -1) {
                return itemText.substring(1, end).trim();
            }
        }
        int dash = itemText.indexOf(" -");
        if (dash != -1) {
            return itemText.substring(0, dash).trim();
        }
        return itemText.trim();
    }

    private void handleReturnBook() {
        String bookSelected = (String) cmbBorrowedBooks.getSelectedItem();
        String returnDate = txtReturnDate.getText().trim();

        if (bookSelected == null || bookSelected.startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Please select a borrowed book to return.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (returnDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Return date cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookId = extractId(bookSelected);

        try {
            BorrowRecord record = library.returnBook(bookId, returnDate);
            String bookTitle = (record != null && record.getBook() != null) ? record.getBook().getTitle() : bookId;
            String memberName = (record != null && record.getMember() != null) ? record.getMember().getName() : "-";

            JOptionPane.showMessageDialog(
                    this,
                    "Book returned successfully!\n\n" +
                    "Book: " + bookTitle + " (ID: " + bookId + ")\n" +
                    "Borrower: " + memberName + "\n" +
                    "Return Date: " + returnDate + "\n" +
                    "Book Availability: AVAILABLE",
                    "Return Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            refreshReturnOptions();

            if (onDataChangedCallback != null) {
                onDataChangedCallback.run();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Return Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshReturnOptions() {
        cmbBorrowedBooks.removeAllItems();
        List<Book> borrowedBooks = library.getBorrowedBooks();

        if (borrowedBooks.isEmpty()) {
            cmbBorrowedBooks.addItem("-- No books currently borrowed --");
        } else {
            for (Book b : borrowedBooks) {
                cmbBorrowedBooks.addItem("[" + b.getBookId() + "] " + b.getTitle());
            }
        }

        // Refresh Table
        tableModel.setRowCount(0);
        List<BorrowRecord> allRecords = library.getAllBorrowRecords();
        for (BorrowRecord r : allRecords) {
            if (r.isActive()) {
                tableModel.addRow(new Object[]{
                        r.getRecordId(),
                        r.getBook().getBookId(),
                        r.getBook().getTitle(),
                        r.getMember().getMemberId(),
                        r.getMember().getName(),
                        r.getBorrowDate()
                });
            }
        }

        updateLoanPreview();
    }
}
