package org.group5;

import org.group5.ui.MainFrame;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {

        // Enable font anti-aliasing for smooth rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Try setting system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {

        }

        // Launch UI on Event Dispatch Thread (Thread safety)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });

    }

}
