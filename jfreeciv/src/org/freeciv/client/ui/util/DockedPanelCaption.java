package org.freeciv.client.ui.util;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * A generic component that serves as the titlebar of docked panels. This
 * is meant to mimic the control seen in lots of Microsoft-y applications.
 *
 * @author Brian.Duff@dubh.org
 */
public class DockedPanelCaption extends JPanel
{
  private JLabel m_captionLabel;
  private JButton m_closeButton;
  private static final Color GRADIENT_COLOR1 = 
    UIManager.getDefaults().getColor( "window" );
  private static final Color GRADIENT_COLOR2 =
    UIManager.getDefaults().getColor( "control" );

  private static final boolean PAINT_GRADIENT = true;
  
  public DockedPanelCaption()
  {
    m_captionLabel = new JLabel();
    m_closeButton = new ToolButton();

    setBorder(new ThinBevelBorder( ThinBevelBorder.RAISED ) );

    m_closeButton.setIcon( new CloseIcon() );
    m_closeButton.setFocusPainted( false );
    
    setLayout(new GridBagLayout() );
    add( m_captionLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 
      GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 
      new Insets(0, 2, 0, 2), 0, 0));
    add( m_closeButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
      GridBagConstraints.EAST, GridBagConstraints.NONE,
      new Insets(0, 2, 0, 2), 3, 2));

    setOpaque( false );

  }

  public void paintComponent( Graphics g )
  {
    if ( PAINT_GRADIENT )
    {
      Graphics2D g2 = (Graphics2D)g;
      Paint save = g2.getPaint();

      int w = getWidth();

      GradientPaint titleGradient = new GradientPaint(
        0,0, 
        GRADIENT_COLOR1,
        (int)(w*.75),0, 
        GRADIENT_COLOR2
      );   

      g2.setPaint(titleGradient);

      g2.fillRect(0, 0, getWidth(), getHeight() );


      g2.setPaint( save );
    }

    super.paintComponent( g );
  }

  /**
   * Controls whether or not the close button is available on this dockpanel
   *
   * @param isCloseable can the dock panel be closed
   */
  public void setCloseable( boolean isCloseable )
  {
    m_closeButton.setVisible( isCloseable );
  }

  public boolean isCloseable()
  {
    return m_closeButton.isVisible();
  }

  /**
   * Add a listener for the close button
   *
   * @param al a listener which will be notified when the user clicks the close
   *  button
   */
  public void addCloseListener( ActionListener al )
  {
    m_closeButton.addActionListener( al );
  }

  /**
   * Set the caption text
   *
   * @param caption the caption text
   */
  public void setText( String caption )
  {
    m_captionLabel.setText( caption );
  }



  /**
   * The "close" icon.
   */
  private class CloseIcon implements Icon
  {
    private int SIZE = 8;


    public void paintIcon(Component c, Graphics g, int x, int y)
    {
      g.setColor(Color.black);
      int x2 = x + (SIZE-1);
      int y2 = y + (SIZE-2);
      g.drawLine( x, y, x2-1, y2 );
      g.drawLine( x+1, y, x2, y2 );
      g.drawLine( x2-1, y, x, y2 );
      g.drawLine( x2, y, x+1, y2 );
    }

    public int getIconWidth()
    {
      return SIZE;
    }

    public int getIconHeight()
    {
      return SIZE - 1;
    }    
    
  }
}