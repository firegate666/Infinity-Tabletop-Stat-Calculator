package de.mb.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

/**
 * Klasse waarmee een JTabbedPane gemaakt kan worden met tabbladen die
 * afzonderlijk afgesloten kunnen worden.
 */
public class ClosableTabbedPane extends JTabbedPane implements ActionListener {

    private Rectangle tabBound;
    private double tabX, tabY, tabH, tabW;
    private TabbedPaneListener tabbedPaneListener;
	/**
	 * Constructor.
	 */
	public ClosableTabbedPane() {
		super();
		tabbedPaneListener = new TabbedPaneListener();
		super.addMouseListener(tabbedPaneListener);
		init();
	}



	/**
	 * Overriding van de methode. Er wordt een icon getoond waarmee het tabblad
	 * gesloten kan worden.
	 *
	 * @param title
	 *            the title to be displayed in this tab
	 * @param component
	 *            the component to be displayed when this tab is clicked
	 * @return the component
	 */
	public Component add(int index, String title, Component component) {
		super.add(title, component);
		super.setIconAt(index, new CloseIcon());
		return component;
	}
	/* (non-Javadoc)
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, java.awt.Component)
	 */
	public void addTab(String title, Component component) {
		// TODO Auto-generated method stub
		super.addTab(title, component);
		super.setIconAt(this.indexOfComponent(component), new CloseIcon());
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	private JPopupMenu popup = new JPopupMenu();

    public void init()
    {
    	JMenuItem close = new JMenuItem("Close");
    	close.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				close();

			}});
    	popup.add(close);
    	JMenuItem closeOthers = new JMenuItem("Close Others");
    	closeOthers.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				closeOthers();

			}});
       	popup.add(closeOthers);
    	JMenuItem closeAll = new JMenuItem("Close All");
    	closeAll.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				closeAll();

			}});
    	popup.add(closeAll);

        MouseListener popupListener = new PopupListener();
        addMouseListener(popupListener);  // I realize I am adding this to the entire JTabbedPane.  Where should I add this?
    }

	protected void closeAll() {
		removeAll();

	}



	protected void closeOthers() {
		Component c = getSelectedComponent();
		String title = getTitleAt(getSelectedIndex());
		removeAll();
		add(title,c);
	}



	protected void close() {
		remove(getSelectedIndex());

	}

	class PopupListener implements MouseListener
    {


        public void mousePressed(MouseEvent e)
        {
            maybeShowPopup(e);
        }
        public void mouseReleased(MouseEvent e)
        {
            maybeShowPopup(e);
        }
        private void maybeShowPopup(MouseEvent e)
        {
        	//System.out.println(e);
             if (e.isPopupTrigger() && ClosableTabbedPane.this.tabbedPaneListener.getTabByCoordinate(ClosableTabbedPane.this,e.getX(), e.getY())!=-1)
             {
                 popup.show(ClosableTabbedPane.this,e.getX(), e.getY());
                 //System.out.println("X: "+e.getX()+"  Y: "+e.getY());
             }
         }
		public void mouseClicked(MouseEvent e) {
            maybeShowPopup(e);

		}
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
    }

}



/**
 * MouseListener voor sluiten van een tabblad.
 */

class TabbedPaneListener extends MouseAdapter {

	/**
	 * Invoked when a mouse button has been released on a component.
	 */
	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		ClosableTabbedPane tabPane = (ClosableTabbedPane) e.getSource();

		if (e.getClickCount()==1) {
			if (tabPane.isEnabled()) {
				int tabIndex = getTabByCoordinate(tabPane, e.getX(),e.getY());
				if (tabIndex >= 0 && tabPane.isEnabledAt(tabIndex)) {
					CloseIcon closeIcon = (CloseIcon) tabPane.getIconAt(tabIndex);
					if (closeIcon.coordinatenInIcon(e.getX(), e.getY())) {
						tabPane.remove(tabIndex);
					}
				}
			}
		}
	}

	/**
	 * Bepaalt a.d.h.v. een coordinaat de index van het gekozen tabbblad.
	 *
	 * @param pane
	 *            Het panel met de tabbladen
	 * @param x
	 *            De X coordinaat
	 * @param y
	 *            De Y coordinaat
	 * @return De index van het tabblad
	 */
	public int getTabByCoordinate(JTabbedPane pane, int x, int y) {
		Point p = new Point(x, y);

		int tabCount = pane.getTabCount();
		for (int i = 0; i < tabCount; i++) {
			if (pane.getBoundsAt(i).contains(p.x, p.y)) {
				return i;
			}
		}
		return -1;
	}
}
/**
 * Deze klasse tekent een kruisje en houdt de absolute coordinaten van de
 * afbeelding vast.
 */

class CloseIcon implements Icon {
	/**
	 * Grootte van het kruis.
	 */
	private int SIZE = 8;

	/**
	 * De X coordinaat van de linkerbovenhoek van het icoontje waarin het kruis
	 * getoond wordt.
	 */
	private int x_coordinaat;

	/**
	 * De Y coordinaat van de linkerbovenhoek van het icoontje waarin het kruis
	 * getoond wordt.
	 */
	private int y_coordinaat;

	/**
	 * Het vierkant waarbinnen het icoontje getoond is en met de muis geklikt
	 * kan worden om het tabblad te sluiten.
	 */
	private Rectangle iconRect;

	/**
	 * De opvul kleur voor het pijltje.
	 */
	private Color COLOR_CROSS = UIManager.getColor("BLUE");

	/**
	 * Constructor.
	 */
	public CloseIcon() {
	}

	/**
	 * Tekent het icon. De links-boven hoek van het icoon is het punt (
	 * <code>x</code>,<code>y</code>)
	 *
	 * @param c
	 *            Het component waar dit icoon op geplaatst wordt
	 * @param g
	 *            De graphics
	 * @param x
	 *            De X coordinaat van de links-boven hoek van het icoon
	 * @param y
	 *            De Y coordinaat van de links-boven hoek van het icoon
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		this.x_coordinaat = x;
		this.y_coordinaat = y;
		drawCross(g, x, y);
		iconRect = new Rectangle(x, y, SIZE, SIZE);
	}

	/**
	 * Tekent het kruisje.
	 *
	 * @param g
	 *            Het graphics component
	 * @param xo
	 *            De x-coordinaat van het begin punt
	 * @param yo
	 *            De y-coordinaat van het begin punt
	 */
	private void drawCross(Graphics g, int xo, int yo) {
		//  default kleur
		g.setColor(COLOR_CROSS);
		g.drawLine(xo, yo, xo + SIZE, yo + SIZE); // =\\.\
		g.drawLine(xo, yo + 1, xo + (SIZE - 1), yo + SIZE); // =\.\\
		g.drawLine(xo, yo + SIZE, xo + SIZE, yo); // = /.//
		g.drawLine(xo, yo + (SIZE - 1), xo + (SIZE - 1), yo); // = .///
		g.drawLine(xo + 1, yo, xo + SIZE, yo + (SIZE - 1)); // =\\\.
		g.drawLine(xo + 1, yo + SIZE, xo + SIZE, yo + 1); // = ///.

		//  wit
		g.setColor(Color.WHITE);
		g.drawLine(xo + 2, yo, xo + 4, yo + 2); // =\\\.
		g.drawLine(xo + 6, yo + 4, xo + SIZE, yo + 6); // =\\\.
		g.drawLine(xo + 2, yo + SIZE, xo + 4, yo + 6); // = //./
		g.drawLine(xo + 6, yo + 4, xo + SIZE, yo + 2); // = //./
	}

	/**
	 * Bepaald of 2 coordinaten zich in de afbeelding van het icoon bevinden.
	 *
	 * @param x
	 *            Het te controleren x coordinaat
	 * @param y
	 *            Het te controleren y coordinaat
	 * @return True indien de coordinaten zich binnen het icoon bevinden
	 */
	public boolean coordinatenInIcon(int x, int y) {
		boolean isInIcon = false;
		if (iconRect != null) {
			//isInIcon = iconRect.contains(x, y);
			boolean res = (x >= iconRect.x )
									&& (x <= iconRect.x + iconRect.width)
									&& (y >= iconRect.y )
									&& (y <= iconRect.y + iconRect.height);
			isInIcon = res;
		}
		return isInIcon;
	}

	/**
	 * Vraagt de breedte van het pijltje op.
	 *
	 * @return De breedte van het pijltje;
	 */
	public int getIconWidth() {
		return SIZE;
	}

	/**
	 * Vraagt de hoogte van het pijltje op.
	 *
	 * @return De hoogte van het pijltje;
	 */
	public int getIconHeight() {
		return SIZE;
	}
}