package org.group5.ui;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.group5.model.Book;
import org.group5.model.BorrowRecord;
import org.group5.service.LibraryData;

public class BorrowPanel extends JPanel {

    private LibraryData library;
    private Runnable onDataChangedCallback;

    private JComboBox<String> cmbMembers;
    private JComboBox<String> cmbBooks;
    private JTextField txtBorrowDate;

    private JLabel lblBookTitle;
    private JLabel lblBookAuthor;
    private JLabel lblBookCategory;
    private JLabel lblBookStatus;

    private JLabel lblMemberName;
    private JLabel lblMemberContact;

    private JTable availableBooksTable;
    private DefaultTableModel tableModel;

    public BorrowPanel(Library library, Runnable onDataChangedCallback) {
        this.library = library;
        this.onDataChangedCallback = onDataChangedCallback;
        initUI();
        refreshBorrowOptions();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIUtils.COLOR_BACKGROUND);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("Issue & Borrow Book");
        title.setFont(UIUtils.FONT_HEADER_LARGE);
        title.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Select an active member and an available book to create a borrowing record");
        subtitle.setFont(UIUtils.FONT_REGULAR);
        subtitle.setForeground(UIUtils.COLOR_TEXT_MUTED);

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Center split layout: Left Form (380px), Right Available Books Table
        JPanel mainContent = new JPanel(new BorderLayout(20, 0));
        mainContent.setOpaque(false);

        // === LEFT PANEL: BORROW FORM ===
        JPanel formCard = UIUtils.createCardPanel();
        formCard.setPreferredSize(new Dimension(320, 0));

        JLabel formTitle = new JLabel("Borrowing Details");
        formTitle.setFont(UIUtils.FONT_HEADER);
        formTitle.setForeground(UIUtils.COLOR_TEXT_PRIMARY);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel formBody = new JPanel();
        formBody.setOpaque(false);
        formBody.setLayout(new BoxLayout(formBody, BoxLayout.Y_AXIS));

        // Member selection combo
        cmbMembers = new JComboBox<String>();
        cmbMembers.setFont(UIUtils.FONT_REGULAR);
        cmbMembers.setBackground(Color.WHITE);

        // Book selection combo
        cmbBooks = new JComboBox<String>();
        cmbBooks.setFont(UIUtils.FONT_REGULAR);
        cmbBooks.setBackground(Color.WHITE);

        // Date field
        txtBorrowDate = UIUtils.createStyledTextField(12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        txtBorrowDate.setText(sdf.format(new Date()));

        formBody.add(createFormField("Select Member *", cmbMembers));
        formBody.add(Box.createVerticalStrut(12));
        formBody.add(createFormField("Select Available Book *", cmbBooks));
        formBody.add(Box.createVerticalStrut(12));
        formBody.add(createFormField("Borrow Date (YYYY-MM-DD) *", txtBorrowDate));
        formBody.add(Box.createVerticalStrut(15));

        // Selection details preview card
        JPanel previewCard = new JPanel(new GridBagLayout());
        previewCard.setBackground(new Color(241, 245, 249));
        previewCard.setBorder(new EmptyBorder(10, 12, 10, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 4, 3, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblBookTitle = new JLabel("-");
        lblBookTitle.setFont(UIUtils.FONT_REGULAR_BOLD);
        lblBookAuthor = new JLabel("-");
        lblBookAuthor.setFont(UIUtils.FONT_REGULAR);
        lblBookStatus = new JLabel("-");
        lblBookStatus.setFont(UIUtils.FONT_REGULAR_BOLD);
        lblBookStatus.setForeground(UIUtils.COLOR_SUCCESS);
        lblMemberName = new JLabel("-");
        lblMemberName.setFont(UIUtils.FONT_REGULAR_BOLD);

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        previewCard.add(createDetailLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        previewCard.add(lblBookTitle, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        previewCard.add(createDetailLabel("Author:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        previewCard.add(lblBookAuthor, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        previewCard.add(createDetailLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        previewCard.add(lblBookStatus, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        previewCard.add(createDetailLabel("Member:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        previewCard.add(lblMemberName, gbc);

        formBody.add(previewCard);
        formBody.add(Box.createVerticalStrut(18));

        // Action Buttons Stacked Vertically
        JButton btnBorrow = UIUtils.createStyledButton("Confirm Borrow", UIUtils.COLOR_PRIMARY, Color.WHITE);
        JButton btnRefreshForm = UIUtils.createStyledButton("Reset Form", UIUtils.COLOR_SECONDARY, Color.WHITE);

        btnBorrow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnBorrow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRefreshForm.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btnRefreshForm.setAlignmentX(Component.LEFT_ALIGNMENT);

        formBody.add(btnBorrow);
        formBody.add(Box.createVerticalStrut(8));
        formBody.add(btnRefreshForm);
        formBody.add(Box.createVerticalGlue()); // Absorbs extra vertical space
        formCard.add(formBody, BorderLayout.CENTER);
        mainContent.add(formCard, BorderLayout.WEST);

        //RIGHT PANEL: CURRENTLY AVAILABLE BOOKS LIST 
        JPanel tableCard = UIUtils.createCardPanel();

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setOpaque(false);
        JLabel tableTitle = new JLabel("Currently Available Books");
        tableTitle.setFont(UIUtils.FONT_HEADER);
        tableTitle.setForeground(UIUtils.COLOR_TEXT_PRIMARY);

        JLabel tableSub = new JLabel("Click on any row below to quickly select it for borrowing");
        tableSub.setFont(UIUtils.FONT_SMALL);
        tableSub.setForeground(UIUtils.COLOR_TEXT_MUTED);

        tableHeader.add(tableTitle, BorderLayout.NORTH);
        tableHeader.add(tableSub, BorderLayout.SOUTH);
        tableHeader.setBorder(new EmptyBorder(0, 0, 12, 0));
        tableCard.add(tableHeader, BorderLayout.NORTH);

        String[] columns = {"Book ID", "Title", "Author", "Category", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        availableBooksTable = new JTable(tableModel);
        UIUtils.formatTable(availableBooksTable);
        availableBooksTable.getColumnModel().getColumn(0).setPreferredWidth(75);
        availableBooksTable.getColumnModel().getColumn(1).setPreferredWidth(220);
        availableBooksTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        availableBooksTable.getColumnModel().getColumn(3).setPreferredWidth(105);
        availableBooksTable.getColumnModel().getColumn(4).setPreferredWidth(90);

        JScrollPane scrollPane = new JScrollPane(availableBooksTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        mainContent.add(tableCard, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        // EVENT LISTENERS 

        cmbBooks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBookPreview();
            }
        });

        cmbMembers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMemberPreview();
            }
        });

        // Quick select from table
        availableBooksTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && availableBooksTable.getSelectedRow() != -1) {
                String bookId = (String) tableModel.getValueAt(availableBooksTable.getSelectedRow(), 0);
                selectBookInCombo(bookId);
            }
        });

        btnBorrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBorrowBook();
            }
        });

        btnRefreshForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshBorrowOptions();
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

    private void updateBookPreview() {
        String selected = (String) cmbBooks.getSelectedItem();
        if (selected == null || selected.startsWith("--")) {
            lblBookTitle.setText("-");
            lblBookAuthor.setText("-");
            lblBookStatus.setText("-");
            return;
        }

        String bookId = extractId(selected);
        Book book = library.findBookById(bookId);
        if (book != null) {
            lblBookTitle.setText(book.getTitle());
            lblBookAuthor.setText(book.getAuthor());
            lblBookStatus.setText(book.getAvailabilityStatus());
            lblBookStatus.setForeground(book.isAvailable() ? UIUtils.COLOR_SUCCESS : UIUtils.COLOR_DANGER);
        }
    }

    private void updateMemberPreview() {
        String selected = (String) cmbMembers.getSelectedItem();
        if (selected == null || selected.startsWith("--")) {
            lblMemberName.setText("-");
            return;
        }

        String memberId = extractId(selected);
        Member member = library.findMemberById(memberId);
        if (member != null) {
            lblMemberName.setText(member.getName());
        }
    }

    private void selectBookInCombo(String bookId) {
        for (int i = 0; i < cmbBooks.getItemCount(); i++) {
            String item = cmbBooks.getItemAt(i);
            if (item.startsWith("[" + bookId + "]") || item.startsWith(bookId + " -")) {
                cmbBooks.setSelectedIndex(i);
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

    private void handleBorrowBook() {
        String memberSelected = (String) cmbMembers.getSelectedItem();
        String bookSelected = (String) cmbBooks.getSelectedItem();
        String date = txtBorrowDate.getText().trim();

        if (memberSelected == null || memberSelected.startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Please select a library member.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (bookSelected == null || bookSelected.startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Please select a book to borrow.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Borrow date cannot be empty.", "Input Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookId = extractId(bookSelected);
        String memberId = extractId(memberSelected);

        try {
            BorrowRecord record = library.borrowBook(bookId, memberId, date);
            JOptionPane.showMessageDialog(
                    this,
                    "Book successfully borrowed!\n\n" +
                    "Record ID: " + record.getRecordId() + "\n" +
                    "Book: " + record.getBook().getTitle() + " (" + record.getBook().getBookId() + ")\n" +
                    "Member: " + record.getMember().getName() + " (" + record.getMember().getMemberId() + ")\n" +
                    "Borrow Date: " + record.getBorrowDate(),
                    "Borrowing Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            refreshBorrowOptions();

            if (onDataChangedCallback != null) {
                onDataChangedCallback.run();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Borrowing Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshBorrowOptions() {
        // Refresh Member Combo
        cmbMembers.removeAllItems();
        List<Member> members = library.getAllMembers();
        if (members.isEmpty()) {
            cmbMembers.addItem("-- No registered members --");
        } else {
            for (Member m : members) {
                cmbMembers.addItem("[" + m.getMemberId() + "] " + m.getName());
            }
        }

        // Refresh Available Books Combo
        cmbBooks.removeAllItems();
        List<Book> availableBooks = library.getAvailableBooks();
        if (availableBooks.isEmpty()) {
            cmbBooks.addItem("-- No available books --");
        } else {
            for (Book b : availableBooks) {
                cmbBooks.addItem("[" + b.getBookId() + "] " + b.getTitle());
            }
        }

        // Refresh Available Books Table
        tableModel.setRowCount(0);
        for (Book b : availableBooks) {
            tableModel.addRow(new Object[]{
                    b.getBookId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getCategory(),
                    b.getAvailabilityStatus()
            });
        }

        updateBookPreview();
        updateMemberPreview();
    }

}
