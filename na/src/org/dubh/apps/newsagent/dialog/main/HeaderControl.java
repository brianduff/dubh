/*   NewsAgent: A Java USENET Newsreader
 *   Copyright (C) 1997-8  Brian Duff
 *   Email: bd@dcs.st-and.ac.uk
 *   URL:   http://st-and.compsoc.org.uk/~briand/newsagent/
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package dubh.apps.newsagent.dialog.main;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;


/**
 * A header control which displays a set of titles and allows the
 * user to change the width of each one.
 * Version History: <UL>
 * <LI>0.1 [18/02/98]: Initial Revision
 * <LI>0.2 [19/02/98]: Changed internal implementation to use widths instead
 *		of xoffsets. Added image support
 * <LI>0.3 [23/02/98]: Added set/getMinimumWidth. Added tree binding.
 * <LI>0.4 [26/02/98]: Generalised so that the header can be bound to any
 *     subclass of Component.
 * <LI>0.5 [03/03/98]: Fixed colours for Swing 1.0.
 * <LI>0.6 [31/03/98]: Changed to use a HeaderBoundRenderer instead of component.
 *</UL>
 @author Brian Duff
 @version 0.5 [03/03/98]
 */
public class HeaderControl extends JPanel implements MouseListener,
	MouseMotionListener {

	public static final int DEFAULTWIDTH = 120;
  public static final int MINWIDTH = 5;
  private static final int LABELINSET = 4;
	protected int[] 		colWidth; 	 // The width the columns
  protected int[]			colMinWidth; // The minimum width of the columns
  protected String[] 	colName;     // The name of the columns
  protected Image[]		colImage;		 // The image for the column
  protected boolean[] colUseImage; // Should the column use its image?
  protected int   		numCols;     // The number of columns
  protected boolean 	overMover;	 // The mouse cursor is over a mover
  protected int				moverOffset; // Distance of the mouse pointer from the mover
  protected int				overWhich;	 // Which mover the mouse is over
  protected Component m_component; // component we are bound to
 // protected HeaderBoundRenderer m_renderer; // renderer we are bound to.

  /**
   * Constructs a HeaderControl with one column.
   */
  public HeaderControl() {
  	this(1);
  }

  /**
   * Construct a HeaderControl with the specified number of columns.
   @param nCols the number of columns to use.
   */
  public HeaderControl(int nCols) {
        super();
  	int i;

  	numCols = nCols;
    colWidth = new int[nCols];
    colMinWidth = new int[nCols];
    colName = new String[nCols];
    colImage = new Image[nCols];
    colUseImage = new boolean[nCols];
    colWidth[0] = DEFAULTWIDTH;
    colMinWidth[0] = MINWIDTH;
    colName[0] = "";
    for (i = 1; i < numCols; i++) {
    	colWidth[i] = DEFAULTWIDTH;
      colMinWidth[i] = MINWIDTH;
      colName[i] = "";
      colUseImage[i] = false;
      colImage[i] = null;
    }
    this.addMouseListener(this);
    this.addMouseMotionListener(this);
  }

  /**
   * Retrieves the position of a column.
   @param column The number of the column to retrieve (1 is the first)
   @returns the x offset in pixels from the left of the panel of the column
   */
  public int getColumnPos(int column) {
    int result = 0;
  	if (column == 1) return 0;
    for (int i=1; i<column; i++) {
    	result += getColumnWidth(i);
    }
    return result;
  }

  /**
   * Gets the width of a column
   @param column The column
   @returns The width
   */
  public int getColumnWidth(int column) {
  	return (column <= numCols? colWidth[column-1]:0);
  }

  /**
   * Sets the width of a column, redrawing the header.
   @param column The column to set
   @param width The width of the column in pixels.
   */
  public void setColumnWidth(int column, int width) {
  	if (column <= numCols) {
    	if (width >= getMinimumWidth(column)) colWidth[column-1] = width;
      else colWidth[column-1] = getMinimumWidth(column);
    	repaint();
      if (m_component != null) { m_component.repaint(); }
    }
  }

  /**
   * Gets the minimum width of a column.
   @param column The column
   @returns the minimum width
   */
  public int getMinimumWidth(int column) {
  	return (column <= numCols? colMinWidth[column-1]:0);
  }

  /**
   * Sets the minimum width of a column. This can never be less than MINWIDTH.
   * If the column is smaller than the new minimum, it is resized accordingly.
   @param column The column
   @param width The minumum width
   */
  public void setMinimumWidth(int column, int width) {
  	if (column <= numCols && width > MINWIDTH) {
    	colMinWidth[column-1] = width;
      if (getColumnWidth(column) < width)
      	setColumnWidth(column, width);
    }
  }

  /**
   * Sets the component to redraw when the width of a column changes.
   @param c Any Component or subclass which can be repainted.
   */
  public void setBoundComponent(Component c) {
        m_component = c;
  }

  /**
   * Retrieves the name of a given column.
   @param column The column whose value you wish to retrieve.
   @returns A String containing the name of the column.
   */
  public String getColumnName(int column) {
  	return (column <= numCols) ? colName[column-1] : "";
  }

  /**
   * Sets the name of a given column.
   @param column The column to set
   @param name The name to display for the column
   */
  public void setColumnName(int column, String name) {
  	if (column <= numCols) colName[column-1] = name;
  }

  /**
   * Gets the image of the given column.
   @param column The column
   @returns an Image
  */
	public Image getColumnImage(int column) {
  	return colImage[column-1];
  }

  /**
   * Determines whether an image is available for a given column.
   @param column The column
   @returns a boolean, true if an image is to be used for the spec. column.
   */
  public boolean isImageBeingUsed(int column) {
  	return colUseImage[column-1];
  }

  /**
   * Sets the image of a given column. If you decide to use an image with a
   * column, the label <strong>is not</strong> displayed.
   @param column The column whose image you want to set
   @param i The image to use. Set to null to use no image.
   */
  public void setColumnImage(int column, Image i) {
  	if (i == null) {
    	colUseImage[column-1] = false;
      colImage[column-1] = null;
    } else {
    	colUseImage[column-1] = true;
      colImage[column-1] = i;
    }
  }


// Events

	public void mouseMoved(MouseEvent e) {
  	if (numCols < 2) return;
  	for (int i=2; i<= numCols; i++) {
    	if (e.getX() >  getColumnPos(i)-5 && e.getX() < getColumnPos(i)+5) {
      	setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        overMover = true;
        moverOffset = e.getX()-getColumnPos(i);
        overWhich = i-1;
        return;
    	} else {
      	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        overMover = false;
      }
  	}
  }

  public void mousePressed(MouseEvent e) { ; }

  
  public void mouseDragged(MouseEvent e) {
  	if (overMover) {
    	int widthadjust;
      widthadjust = e.getX() - getColumnPos(overWhich+1) - moverOffset;
      setColumnWidth(overWhich, getColumnWidth(overWhich)+widthadjust);
    }
  }
  
  public void mouseReleased(MouseEvent e) { ; }
  public void mouseClicked(MouseEvent e) { ; }
  public void mouseEntered(MouseEvent e) { ; }
  public void mouseExited(MouseEvent e) { ; }

  /**
   * Paints the column header. Overrides BevelPanel.paint().
   */
  public void paint(Graphics g) {
    	g.setColor(UIManager.getColor("control"));
    	g.fillRect(0, 0, getSize().width, getSize().height);
      g.setColor(UIManager.getColor("controlBlack"));
      g.drawRect(0, 0, getSize().width-1, getSize().height-1);
      for (int i=1; i <= numCols; i++)
      	drawColumn(g, i);
  }

  /**
   * Paints an individual column header.
   */
  private void drawColumn(Graphics g, int column) {
  	int rightlimit;
  	g.setColor(UIManager.getColor("controlBlack"));
    g.drawLine(getColumnPos(column), 0, getColumnPos(column), getSize().height);
		g.setColor(UIManager.getColor("controlHighlight"));
    rightlimit = (column < numCols) ? getColumnPos(column+1): getSize().width-1;
    g.drawLine(getColumnPos(column)+1, 1, rightlimit-1, 1);
    g.drawLine(getColumnPos(column)+1, 1, getColumnPos(column)+1,
    	getSize().height-2);
    g.setColor(UIManager.getColor("controlShadow"));
    g.drawLine(getColumnPos(column)+2, getSize().height-2, rightlimit-1,
    	getSize().height-2);
    g.drawLine(rightlimit-1, 2, rightlimit-1, getSize().height-2);
    g.setColor(Color.black);
    // Clip the image / string so that they don't go over the header width
  	g.setClip(getColumnPos(column)+2, 2, getColumnWidth(column)-4,
    	getSize().height-2);
    if (isImageBeingUsed(column))
    	g.drawImage(getColumnImage(column), getColumnPos(column)+2+LABELINSET,
      	getSize().height-LABELINSET-getColumnImage(column).getHeight((ImageObserver)this),
        	(ImageObserver)this);
    else
    	g.drawString(getColumnName(column), getColumnPos(column)+2+LABELINSET,
      	getSize().height-LABELINSET);
		g.setClip(0,0, getSize().width, getSize().height);
  }

}
