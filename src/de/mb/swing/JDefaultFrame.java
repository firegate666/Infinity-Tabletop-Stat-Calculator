package de.mb.swing;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author Marco Behnke
 * 
 */

public class JDefaultFrame extends JFrame {

    /**
     * Public constructor
     */
    public JDefaultFrame() {
        initialize();
    }

    private void initialize() {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new DefaultListener());
    }

    class DefaultListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            new CloseAction(e);
        }
        public DefaultListener() {}
    }

    // Internal classes
    
}