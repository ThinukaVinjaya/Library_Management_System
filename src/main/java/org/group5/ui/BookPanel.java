package org.group5.ui;

import org.group5.model.Book;
import org.group5.service.LibraryData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List; 

public class BookPanel extends JPanel {
    private LibraryData library;
    private Runnable onDataChangedCallback;

    // Form inputs
    private JTextField txtBookId;
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JComboBox<String> cmbCategory;

    // Search input
    private JTextField txtSearch;
    private JComboBox<String> cmbFilterCategory;

    // Table
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public BookPanel(LibraryData library, Runnable onDataChangedCallback) {
        this.library = library;
        this.onDataChangedCallback = onDataChangedCallback;
        initUI();
        refreshBookTable();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UiUtils.COLOR_BACKGROUND);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("Book Management");
        title.setFont(UiUtils.FONT_HEADER_LARGE);
        title.setForeground(UiUtils.COLOR_TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Add, search, inspect, and remove books in the library catalog");
        subtitle.setFont(UiUtils.FONT_REGULAR);
        subtitle.setForeground(UiUtils.COLOR_TEXT_MUTED);

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Center split layout: Left Form (320px width), Right Table
        JPanel mainContent = new JPanel(new BorderLayout(20, 0));
        mainContent.setOpaque(false);

        // LEFT PANEL: ADD BOOK FORM 
        JPanel formCard = UiUtils.createCardPanel();
        formCard.setPreferredSize(new Dimension(300, 0));

        JLabel formTitle = new JLabel("Add New Book");
        formTitle.setFont(UiUtils.FONT_HEADER);
        formTitle.setForeground(UiUtils.COLOR_TEXT_PRIMARY);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel formBody = new JPanel();
        formBody.setOpaque(false);
        formBody.setLayout(new BoxLayout(formBody, BoxLayout.Y_AXIS));

        txtBookId = UiUtils.createStyledTextField(15);
        txtTitle = UiUtils.createStyledTextField(15);
        txtAuthor = UiUtils.createStyledTextField(15);

        String[] categories = {"Technology", "Computer Science", "Software Engineering", "Database", "AI & Data", "Mathematics", "Literature", "General"};
        cmbCategory = new JComboBox<String>(categories);
        cmbCategory.setFont(UiUtils.FONT_REGULAR);
        cmbCategory.setBackground(Color.WHITE);

        formBody.add(createFormField("Book ID *", txtBookId));
        formBody.add(Box.createVerticalStrut(12));
        formBody.add(createFormField("Book Title *", txtTitle));
        formBody.add(Box.createVerticalStrut(12));
        formBody.add(createFormField("Author *", txtAuthor));
        formBody.add(Box.createVerticalStrut(12));
        formBody.add(createFormField("Category", cmbCategory));
        formBody.add(Box.createVerticalStrut(18));

        // Form Buttons Stacked Vertically
        JButton btnAddBook = UiUtils.createStyledButton("+ Add New Book", UiUtils.COLOR_PRIMARY, Color.WHITE);
        JButton btnClear = UiUtils.createStyledButton("Clear Form", UiUtils.COLOR_SECONDARY, Color.WHITE);

        btnAddBook.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnAddBook.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnClear.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btnClear.setAlignmentX(Component.LEFT_ALIGNMENT);

        formBody.add(btnAddBook);
        formBody.add(Box.createVerticalStrut(8));
        formBody.add(btnClear);
        formBody.add(Box.createVerticalGlue()); // Absorbs extra vertical space
        formCard.add(formBody, BorderLayout.CENTER);

        mainContent.add(formCard, BorderLayout.WEST);

        //RIGHT PANEL: BOOK LIST & SEARCH 
        JPanel tableCard = UiUtils.createCardPanel();

        // Top Search Bar inside Table Card
        JPanel searchBar = new JPanel(new BorderLayout(10, 0));
        searchBar.setOpaque(false);
        searchBar.setBorder(new EmptyBorder(0, 0, 12, 0));

        JPanel searchInputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchInputs.setOpaque(false);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(UiUtils.FONT_REGULAR_BOLD);
        lblSearch.setForeground(UiUtils.COLOR_TEXT_PRIMARY);

        txtSearch = UiUtils.createStyledTextField(13);
        JButton btnSearch = UiUtils.createStyledButton("Search", UiUtils.COLOR_PRIMARY, Color.WHITE);
        JButton btnReset = UiUtils.createStyledButton("View All", UiUtils.COLOR_SECONDARY, Color.WHITE);

        searchInputs.add(lblSearch);
        searchInputs.add(txtSearch);
        searchInputs.add(btnSearch);
        searchInputs.add(btnReset);

        searchBar.add(searchInputs, BorderLayout.WEST);

        // Category Filter on the right
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filterPanel.setOpaque(false);
        JLabel lblFilter = new JLabel("Category:");
        lblFilter.setFont(UiUtils.FONT_REGULAR_BOLD);
        lblFilter.setForeground(UiUtils.COLOR_TEXT_MUTED);

        String[] filterOptions = {"All Categories", "Technology", "Computer Science", "Software Engineering", "Database", "AI & Data", "Mathematics", "Literature", "General"};
        cmbFilterCategory = new JComboBox<String>(filterOptions);
        cmbFilterCategory.setFont(UiUtils.FONT_REGULAR);
        cmbFilterCategory.setBackground(Color.WHITE);

        filterPanel.add(lblFilter);
        filterPanel.add(cmbFilterCategory);
        searchBar.add(filterPanel, BorderLayout.EAST);

        tableCard.add(searchBar, BorderLayout.NORTH);

        // Table
        String[] columns = {"Book ID", "Title", "Author", "Category", "Availability"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);
        UiUtils.formatTable(bookTable);
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(220);
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(105);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // Table Actions Bottom
        JPanel actionBottom = new JPanel(new BorderLayout(10, 0));
        actionBottom.setOpaque(false);
        actionBottom.setBorder(new EmptyBorder(12, 0, 0, 0));

        JLabel lblHint = new JLabel("Tip: Select row to remove.");
        lblHint.setFont(UiUtils.FONT_SMALL);
        lblHint.setForeground(UiUtils.COLOR_TEXT_MUTED);
        actionBottom.add(lblHint, BorderLayout.WEST);

        JPanel actionBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionBtns.setOpaque(false);

        JButton btnRemove = UiUtils.createStyledButton("Remove Book", UiUtils.COLOR_DANGER, Color.WHITE);
        JButton btnRefresh = UiUtils.createStyledButton("Refresh", UiUtils.COLOR_SECONDARY, Color.WHITE);

        actionBtns.add(btnRemove);
        actionBtns.add(btnRefresh);
        actionBottom.add(actionBtns, BorderLayout.EAST);

        tableCard.add(actionBottom, BorderLayout.SOUTH);

        mainContent.add(tableCard, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        // Add Book Action
        btnAddBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddBook();
            }
        });

        // Clear Form Action
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // Search Action
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });

        txtSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });

        // View All / Reset Action
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearch.setText("");
                cmbFilterCategory.setSelectedIndex(0);
                refreshBookTable();
            }
        });

        // Category Filter Changed
        cmbFilterCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCategoryFilter();
            }
        });

        // Remove Book Action
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRemoveBook();
            }
        });

        // Refresh Action
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshBookTable();
            }
        });
    }

    private JPanel createFormField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(UiUtils.FONT_REGULAR_BOLD);
        label.setForeground(UiUtils.COLOR_TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        panel.add(label);
        panel.add(Box.createVerticalStrut(4));
        panel.add(field);
        return panel;
    }

    private void handleAddBook() {
        String bookId = txtBookId.getText().trim();
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String category = (String) cmbCategory.getSelectedItem();

        // Validations
        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Book ID cannot be empty. Please enter a valid Book ID.", "Input Validation", JOptionPane.WARNING_MESSAGE);
            txtBookId.requestFocus();
            return;
        }

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Book Title cannot be empty. Please enter the title.", "Input Validation", JOptionPane.WARNING_MESSAGE);
            txtTitle.requestFocus();
            return;
        }

        try {
            Book newBook = new Book(bookId, title, author, category, Book.STATUS_AVAILABLE);
            library.addBook(newBook);

            JOptionPane.showMessageDialog(this, "Book '" + title + "' (ID: " + bookId + ") added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            refreshBookTable();

            if (onDataChangedCallback != null) {
                onDataChangedCallback.run();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Adding Book", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRemoveBook() {
        int selectedRow = bookTable.getSelectedRow();
        String bookIdToRemove;

        if (selectedRow != -1) {
            bookIdToRemove = (String) tableModel.getValueAt(selectedRow, 0);
        } else {
            bookIdToRemove = JOptionPane.showInputDialog(this, "Enter Book ID to remove:", "Remove Book", JOptionPane.QUESTION_MESSAGE);
        }

        if (bookIdToRemove == null || bookIdToRemove.trim().isEmpty()) {
            return;
        }

        bookIdToRemove = bookIdToRemove.trim();

        // Confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove Book ID: " + bookIdToRemove + "?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean removed = library.removeBook(bookIdToRemove);
                if (removed) {
                    JOptionPane.showMessageDialog(this, "Book removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshBookTable();
                    if (onDataChangedCallback != null) {
                        onDataChangedCallback.run();
                    }
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Removing Book", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            refreshBookTable();
            return;
        }

        List<Book> results = library.searchBooks(keyword);
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books found matching '" + keyword + "'.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
        displayBooks(results);
    }

    private void handleCategoryFilter() {
        String category = (String) cmbFilterCategory.getSelectedItem();
        if (category == null || "All Categories".equals(category)) {
            refreshBookTable();
            return;
        }

        List<Book> all = library.getAllBooks();
        tableModel.setRowCount(0);
        for (Book b : all) {
            if (category.equalsIgnoreCase(b.getCategory())) {
                tableModel.addRow(new Object[]{
                        b.getBookId(),
                        b.getTitle(),
                        b.getAuthor(),
                        b.getCategory(),
                        b.getAvailabilityStatus()
                });
            }
        }
    }

    private void clearForm() {
        txtBookId.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        cmbCategory.setSelectedIndex(0);
        txtBookId.requestFocus();
    }

    public void refreshBookTable() {
        displayBooks(library.getAllBooks());
    }

    private void displayBooks(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book b : books) {
            tableModel.addRow(new Object[]{
                    b.getBookId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getCategory(),
                    b.getAvailabilityStatus()
            });
        }
    }
}
