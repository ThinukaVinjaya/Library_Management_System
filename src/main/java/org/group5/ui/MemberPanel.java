package org.group5.ui;


import org.group5.model.Member;
import org.group5.service.LibraryData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class MemberPanel extends JPanel {
    private LibraryData library;
    private Runnable onDataChangedCallback;

    private JTextField txtMemberId;
    private JTextField txtName;
    private JTextField txtContact;


    private JTextField txtSearch;

    private JTable memberTable;
    private DefaultTableModel tableModel;

    public MemberPanel(LibraryData library, Runnable onDataChangedCallback) {
        this.library = library;
        this.onDataChangedCallback = onDataChangedCallback;
        initUI();
        refreshMemberTable();
    }

    private void initUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UiUtils.COLOR_BACKGROUND);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("Member Management");
        title.setFont(UiUtils.FONT_HEADER_LARGE);
        title.setForeground(UiUtils.COLOR_TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Register, search, view, and manage library members");
        subtitle.setFont(UiUtils.FONT_REGULAR);
        subtitle.setForeground(UiUtils.COLOR_TEXT_MUTED);

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel(new BorderLayout(20, 0));
        mainContent.setOpaque(false);

        JPanel formCard = UiUtils.createCardPanel();
        formCard.setPreferredSize(new Dimension(300, 0));

        JLabel formTitle = new JLabel("Register New Member");
        formTitle.setFont(UiUtils.FONT_HEADER);
        formTitle.setForeground(UiUtils.COLOR_TEXT_PRIMARY);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel formBody = new JPanel();
        formBody.setOpaque(false);
        formBody.setLayout(new BoxLayout(formBody, BoxLayout.Y_AXIS));

        txtMemberId = UiUtils.createStyledTextField(15);
        txtName = UiUtils.createStyledTextField(15);
        txtContact = UiUtils.createStyledTextField(15);

        formBody.add(createFormField("Member ID *", txtMemberId));
        formBody.add(Box.createVerticalStrut(14));
        formBody.add(createFormField("Full Name *", txtName));
        formBody.add(Box.createVerticalStrut(14));
        formBody.add(createFormField("Contact Number", txtContact));
        formBody.add(Box.createVerticalStrut(20));

        JButton btnAddMember = UiUtils.createStyledButton("+ Register Member", UiUtils.COLOR_PRIMARY, Color.WHITE);
        JButton btnClear = UiUtils.createStyledButton("Clear Form", UiUtils.COLOR_SECONDARY, Color.WHITE);

        btnAddMember.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnAddMember.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnClear.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btnClear.setAlignmentX(Component.LEFT_ALIGNMENT);

        formBody.add(btnAddMember);
        formBody.add(Box.createVerticalStrut(8));
        formBody.add(btnClear);
        formBody.add(Box.createVerticalGlue()); // Absorbs extra vertical space
        formCard.add(formBody, BorderLayout.CENTER);

        mainContent.add(formCard, BorderLayout.WEST);

        JPanel tableCard = UiUtils.createCardPanel();

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBar.setOpaque(false);
        searchBar.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel lblSearch = new JLabel("Search Member:");
        lblSearch.setFont(UiUtils.FONT_REGULAR_BOLD);
        lblSearch.setForeground(UiUtils.COLOR_TEXT_PRIMARY);

        txtSearch = UiUtils.createStyledTextField(15);
        JButton btnSearch = UiUtils.createStyledButton("Search", UiUtils.COLOR_PRIMARY, Color.WHITE);
        JButton btnReset = UiUtils.createStyledButton("View All", UiUtils.COLOR_SECONDARY, Color.WHITE);

        searchBar.add(lblSearch);
        searchBar.add(txtSearch);
        searchBar.add(btnSearch);
        searchBar.add(btnReset);

        tableCard.add(searchBar, BorderLayout.NORTH);

        String[] columns = {"Member ID", "Member Name", "Contact Number"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        memberTable = new JTable(tableModel);
        UiUtils.formatTable(memberTable);
        memberTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        memberTable.getColumnModel().getColumn(1).setPreferredWidth(230);
        memberTable.getColumnModel().getColumn(2).setPreferredWidth(140);

        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);


        JPanel actionBottom = new JPanel(new BorderLayout(10, 0));
        actionBottom.setOpaque(false);
        actionBottom.setBorder(new EmptyBorder(12, 0, 0, 0));

        JLabel lblHint = new JLabel("Tip: Select row to remove.");
        lblHint.setFont(UiUtils.FONT_SMALL);
        lblHint.setForeground(UiUtils.COLOR_TEXT_MUTED);
        actionBottom.add(lblHint, BorderLayout.WEST);

        JPanel actionBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionBtns.setOpaque(false);

        JButton btnRemove = UiUtils.createStyledButton("Remove Member", UiUtils.COLOR_DANGER, Color.WHITE);
        JButton btnRefresh = UiUtils.createStyledButton("Refresh", UiUtils.COLOR_SECONDARY, Color.WHITE);

        actionBtns.add(btnRemove);
        actionBtns.add(btnRefresh);
        actionBottom.add(actionBtns, BorderLayout.EAST);

        tableCard.add(actionBottom, BorderLayout.SOUTH);

        mainContent.add(tableCard, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);


        btnAddMember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddMember();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

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

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSearch.setText("");
                refreshMemberTable();
            }
        });

        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRemoveMember();
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshMemberTable();
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

    private void handleAddMember() {
        String id = txtMemberId.getText().trim();
        String name = txtName.getText().trim();
        String contact = txtContact.getText().trim();

        // Validations
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member ID cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtMemberId.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Member Name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtName.requestFocus();
            return;
        }

        try {
            Member member = new Member(id, name, contact);
            library.addMember(member);

            JOptionPane.showMessageDialog(this, "Member '" + name + "' (ID: " + id + ") registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            refreshMemberTable();

            if (onDataChangedCallback != null) {
                onDataChangedCallback.run();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Registering Member", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRemoveMember() {
        int selectedRow = memberTable.getSelectedRow();
        String memberIdToRemove;

        if (selectedRow != -1) {
            memberIdToRemove = (String) tableModel.getValueAt(selectedRow, 0);
        } else {
            memberIdToRemove = JOptionPane.showInputDialog(this, "Enter Member ID to remove:", "Remove Member", JOptionPane.QUESTION_MESSAGE);
        }

        if (memberIdToRemove == null || memberIdToRemove.trim().isEmpty()) {
            return;
        }

        memberIdToRemove = memberIdToRemove.trim();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove Member ID: " + memberIdToRemove + "?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean removed = library.removeMember(memberIdToRemove);
                if (removed) {
                    JOptionPane.showMessageDialog(this, "Member removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshMemberTable();
                    if (onDataChangedCallback != null) {
                        onDataChangedCallback.run();
                    }
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Removing Member", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleSearch() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            refreshMemberTable();
            return;
        }

        List<Member> results = library.searchMembers(keyword);
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No member found matching '" + keyword + "'.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
        displayMembers(results);
    }

    private void clearForm() {
        txtMemberId.setText("");
        txtName.setText("");
        txtContact.setText("");
        txtMemberId.requestFocus();
    }

    public void refreshMemberTable() {
        displayMembers(library.getAllMembers());
    }

    private void displayMembers(List<Member> members) {
        tableModel.setRowCount(0);
        for (Member m : members) {
            tableModel.addRow(new Object[]{
                    m.getMemberId(),
                    m.getName(),
                    m.getContactNumber()
            });
        }
    }
}

