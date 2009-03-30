package de.mb.swing;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.Document;

/**
 * @author Marco Behnke
 *
 */
public class JAdvancedTextArea extends JTextArea {

    private TextPopupMenu _popup;

    /**
     * Public constructor
     */
    public JAdvancedTextArea() {
        super();
        initialize();
    }

    /**
     * Public constructor
     * 
     * @param text
     */
    public JAdvancedTextArea(String text) {
        super(text);
        initialize();
    }

    /**
     * Public constructor
     * 
     * @param rows
     * @param columns
     */
    public JAdvancedTextArea(int rows, int columns) {
        super(rows, columns);
        initialize();
    }

    /**
     * Public constructor
     * 
     * @param text
     * @param rows
     * @param columns
     */
    public JAdvancedTextArea(String text, int rows, int columns) {
        super(text, rows, columns);
        initialize();
    }

    /**
     * Public constructor
     * 
     * @param doc
     */
    public JAdvancedTextArea(Document doc) {
        super(doc);
        initialize();
    }

    /**
     * Public constructor
     * .
     * @param doc
     * @param text
     * @param rows
     * @param columns
     */
    public JAdvancedTextArea(
        Document doc,
        String text,
        int rows,
        int columns) {
        super(doc, text, rows, columns);
        initialize();
    }

    /**
     * Adds content to text area
     * @param s text to be added
     * @param newline if true, line break is added before new content
     */
    public void addText(String s, boolean newline) {
        String oldText = this.getText();
        if (newline)
            oldText += "\n";
        this.setText(oldText + s);
    }

    /**
     * Adds content to text area
     * @param s text to be added
     */
    public void addText(String s) {
        this.addText(s, true);
    }

    /**
     * Selects the given line and highlights all contents of it.
     * @param linenumber line to select
     */
    public void selectLine(int linenumber) {
        if (linenumber > this.getLineCount());
        ArrayList lf = new ArrayList();
        int index = 0;
        lf.add(new Integer(index++));
        while (index != -1) {
            index = this.getText().indexOf("\n", index);
            if (index != -1) {
                lf.add(new Integer(index++));
            }

        }
        lf.add(new Integer(this.getText().length()));

        this.setSelectionStart(((Integer) lf.get(linenumber - 1)).intValue());
        this.setSelectionEnd(((Integer) lf.get(linenumber)).intValue());
        this.grabFocus();
    }

    /**
     * Get JAdvancedTextArea as ScrollablePane.
     */
    public JScrollPane getScrollPane() {
        JScrollPane temp = new JScrollPane(this);
        temp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        temp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return temp;
    }

    private void initialize() {
        this.add(_popup = new TextPopupMenu());
        this.addMouseListener(new PopupMenuListener(this));
    }

    // internal classes
    class PopupMenuListener implements MouseListener {

        private JAdvancedTextArea _app;

        public PopupMenuListener(JAdvancedTextArea app) {
            _app = app;
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == 3) {
                Point p = e.getPoint();
                _app._popup.show(_app, p.x, p.y);
            }
        }

        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
    }

    class TextPopupMenu extends JPopupMenu {

        private JMenuItem _copy;
        private JMenuItem _paste;
        private JMenuItem _cut;
        private JMenuItem _delete;

        public TextPopupMenu() {
            super();
            initialize();
        }

        public TextPopupMenu(String label) {
            super(label);
            initialize();
        }

        private void initialize() {
            _copy = new JMenuItem("Kopieren");
            _paste = new JMenuItem("Einf�gen");
            _cut = new JMenuItem("Ausschneiden");
            _delete = new JMenuItem("L�schen");

            this.add(_copy);
            this.add(_paste);
            this.add(_cut);
            this.add(_delete);
        }

    }
}
