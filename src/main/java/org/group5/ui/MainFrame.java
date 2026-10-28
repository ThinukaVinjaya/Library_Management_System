package org.group5.ui;

import com.library.service.Library;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame{
    private Library library;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private List<NavButton> navButtons;

    // View Panels
    private DashboardPanel dashboardPanel;
    private BookPanel bookPanel;
    private MemberPanel memberPanel;
    private BorrowPanel borrowPanel;
    private ReturnPanel returnPanel;
    private HistoryPanel historyPanel;

    // Card Names
    private static final String CARD_DASHBOARD = "DASHBOARD";
    private static final String CARD_BOOKS = "BOOKS";
    private static final String CARD_MEMBERS = "MEMBERS";
    private static final String CARD_BORROW = "BORROW";
    private static final String CARD_RETURN = "RETURN";
    private static final String CARD_HISTORY = "HISTORY";

    public MainFrame() {
        this.library = new Library();
        this.navButtons = new ArrayList<NavButton>();
        initUI();
    }

    private void initUI() {
        setTitle("Library Management System - CST 124-2 Object Oriented Programming");
        setSize(1260, 780);
        setMinimumSize(new Dimension(1080, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar Navigation on the Left
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Content Area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIUtils.COLOR_BACKGROUND);

        // Data synchronization callback
        Runnable syncCallback = new Runnable() {
            @Override
            public void run() {
                refreshAllPanels();
            }
        };

        dashboardPanel = new DashboardPanel(library);
        bookPanel = new BookPanel(library, syncCallback);
        memberPanel = new MemberPanel(library, syncCallback);
        borrowPanel = new BorrowPanel(library, syncCallback);
        returnPanel = new ReturnPanel(library, syncCallback);
        historyPanel = new HistoryPanel(library);

        contentPanel.add(dashboardPanel, CARD_DASHBOARD);
        contentPanel.add(bookPanel, CARD_BOOKS);
        contentPanel.add(memberPanel, CARD_MEMBERS);
        contentPanel.add(borrowPanel, CARD_BORROW);
        contentPanel.add(returnPanel, CARD_RETURN);
        contentPanel.add(historyPanel, CARD_HISTORY);

        add(contentPanel, BorderLayout.CENTER);

        // Select dashboard by default
        selectNav(0, CARD_DASHBOARD);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(245, 0));
        sidebar.setBackground(UIUtils.COLOR_SIDEBAR);
        sidebar.setBorder(new EmptyBorder(22, 16, 22, 16));

        // Brand / Title Header
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(0, 8, 25, 8));

        JLabel titleBadge = new JLabel("LMS PORTAL");
        titleBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        titleBadge.setForeground(UIUtils.COLOR_PRIMARY);

        JLabel brandTitle = new JLabel("Library System");
        brandTitle.setFont(UIUtils.FONT_HEADER);
        brandTitle.setForeground(Color.WHITE);

        JLabel brandSubtitle = new JLabel("Uva Wellassa University");
        brandSubtitle.setFont(UIUtils.FONT_SMALL);
        brandSubtitle.setForeground(new Color(148, 163, 184)); // Muted light slate

        brandPanel.add(titleBadge);
        brandPanel.add(Box.createVerticalStrut(2));
        brandPanel.add(brandTitle);
        brandPanel.add(Box.createVerticalStrut(2));
        brandPanel.add(brandSubtitle);

        sidebar.add(brandPanel, BorderLayout.NORTH);

        // Nav Links
        JPanel navList = new JPanel();
        navList.setLayout(new BoxLayout(navList, BoxLayout.Y_AXIS));
        navList.setOpaque(false);

        JButton btnDash = createNavButton(" Dashboard", CARD_DASHBOARD, 0);
        JButton btnBooks = createNavButton(" Book Management", CARD_BOOKS, 1);
        JButton btnMembers = createNavButton(" Member Management", CARD_MEMBERS, 2);
        JButton btnBorrow = createNavButton(" Borrow Book", CARD_BORROW, 3);
        JButton btnReturn = createNavButton(" Return Book", CARD_RETURN, 4);
        JButton btnHistory = createNavButton(" Borrow History", CARD_HISTORY, 5);

        navList.add(btnDash);
        navList.add(Box.createVerticalStrut(6));
        navList.add(btnBooks);
        navList.add(Box.createVerticalStrut(6));
        navList.add(btnMembers);
        navList.add(Box.createVerticalStrut(6));
        navList.add(btnBorrow);
        navList.add(Box.createVerticalStrut(6));
        navList.add(btnReturn);
        navList.add(Box.createVerticalStrut(6));
        navList.add(btnHistory);

        sidebar.add(navList, BorderLayout.CENTER);

        // Sidebar Footer (Exit Button)
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setOpaque(false);

        JButton btnExit = UIUtils.createStyledButton("Exit System", new Color(51, 65, 85), Color.WHITE);
        btnExit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnExit.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        MainFrame.this,
                        "Are you sure you want to exit the Library Management System?",
                        "Confirm Exit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        footerPanel.add(btnExit);
        sidebar.add(footerPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private NavButton createNavButton(String text, final String cardName, final int index) {
        final NavButton button = new NavButton(text);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectNav(index, cardName);
            }
        });
        navButtons.add(button);
        return button;
    }

    private void selectNav(int activeIndex, String cardName) {
        for (int i = 0; i < navButtons.size(); i++) {
            navButtons.get(i).setActive(i == activeIndex);
        }
        cardLayout.show(contentPanel, cardName);

        // Always refresh target view to make sure data is fresh
        if (CARD_DASHBOARD.equals(cardName)) {
            dashboardPanel.refreshData();
        } else if (CARD_BOOKS.equals(cardName)) {
            bookPanel.refreshBookTable();
        } else if (CARD_MEMBERS.equals(cardName)) {
            memberPanel.refreshMemberTable();
        } else if (CARD_BORROW.equals(cardName)) {
            borrowPanel.refreshBorrowOptions();
        } else if (CARD_RETURN.equals(cardName)) {
            returnPanel.refreshReturnOptions();
        } else if (CARD_HISTORY.equals(cardName)) {
            historyPanel.refreshHistoryTable();
        }
    }

    
    //Refreshes all child panels across the entire application.
     
    public void refreshAllPanels() {
        dashboardPanel.refreshData();
        bookPanel.refreshBookTable();
        memberPanel.refreshMemberTable();
        borrowPanel.refreshBorrowOptions();
        returnPanel.refreshReturnOptions();
        historyPanel.refreshHistoryTable();
    }

    
    // Custom navigation button with smooth hover highlights and rounded active indicator.
     
    private static class NavButton extends JButton {
        private boolean isActive = false;

        public NavButton(String text) {
            super(text);
            setFont(UIUtils.FONT_REGULAR_BOLD);
            setForeground(new Color(203, 213, 225));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(new EmptyBorder(10, 14, 10, 14));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        public void setActive(boolean active) {
            this.isActive = active;
            if (active) {
                setForeground(Color.WHITE);
            } else {
                setForeground(new Color(203, 213, 225));
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isActive) {
                g2.setColor(UIUtils.COLOR_SIDEBAR_ACTIVE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            } else if (getModel().isRollover()) {
                g2.setColor(UIUtils.COLOR_SIDEBAR_HOVER);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }


}
