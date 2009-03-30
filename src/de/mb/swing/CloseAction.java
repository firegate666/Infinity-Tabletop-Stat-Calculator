package de.mb.swing;

import java.awt.AWTEvent;
import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

public class CloseAction {
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