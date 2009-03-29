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

    public static void main(String[] args) {
        JDefaultFrame test = new JDefaultFrame();
        test.hide();
        test.setBounds(0, 0, 640, 480);
        test.show();
    }

    class DefaultListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            new CloseAction(e);
        }
        public DefaultListener() {}
    }

    // Internal classes
    class CloseAction {
        private String _msg =
            ResourceBundle.getBundle("de.mb.swing.jdefaultframe").getString(
                "closingdialog_message");
        private String _title =
            ResourceBundle.getBundle("de.mb.swing.jdefaultframe").getString(
                "closingdialog_title");

        private void action(AWTEvent e) {
            int test =
                JOptionPane.showConfirmDialog(
                    (Component) e.getSource(),
                    _msg,
                    _title,
                    JOptionPane.YES_NO_OPTION);
            if (test == 0)
                System.exit(0);
        }
        public CloseAction(AWTEvent e) {
            action(e);
        }
    }
}