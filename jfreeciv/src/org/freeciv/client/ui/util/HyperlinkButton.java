package org.freeciv.client.ui.util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.freeciv.client.action.AbstractClientAction;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * A hyperlink button is a JButton which behaves like a hyperlink.
 */
public class HyperlinkButton extends JLabel
{
  private Action m_action = null;
  private Font m_normalFont;
  private Font m_underlineFont;
  private boolean m_mouseInside = false;
  private boolean m_armed = false;

  private List m_listeners;

  private boolean m_enabled = true;

  public HyperlinkButton( String text )
  {
    this();
    setText( text );
  }

  public HyperlinkButton()
  {
    setBorder( null );

    // Avoid ugly bold fonts - hack for metal laf 
    Font thisFont = getFont();
    setFont(new Font(thisFont.getName(), Font.PLAIN, thisFont.getSize()));

    setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );

    addMouseListener( new MouseMotionListener() );

    m_listeners = new ArrayList();
  }

  public HyperlinkButton(AbstractClientAction aca)
  {
    this();
    setText( aca.getName() );
    setIcon( (Icon) aca.getValue( Action.SMALL_ICON ) );
    setEnabled( aca.isEnabled() );

    aca.addPropertyChangeListener( new ActionPropertyChangeListener() );
    addActionListener( aca );    
  }

  public void addActionListener(ActionListener al)
  {
    m_listeners.add( al );
  }

  public void removeActionListener( ActionListener al )
  {
    m_listeners.remove( al );
  }

  private static Rectangle paintIconR = new Rectangle();
  private static Rectangle paintTextR = new Rectangle();
  private static Rectangle paintViewR = new Rectangle();
  private static Insets paintViewInsets = new Insets(0, 0, 0, 0);

  /**
   * We completely override paint, because we need to underline text, and
   * would have to override all the PLAF implementations to make this work
   * in a nice way
   *
   * @param g the graphics context to paint in
   */
  public void paintComponent( Graphics g )
  {
    Icon icon = isEnabled() ? getIcon() : getDisabledIcon();

    // Now paint the text
    Color saveColor = g.getColor();
    Font saveFont = g.getFont();
    g.setColor( getForeground() );
    g.setFont( getFont() );

    FontMetrics metrics = g.getFontMetrics( getFont() );
    int height = metrics.getHeight();

    paintViewR.x = 0;
    paintViewR.y = 0;
    paintViewR.width = getWidth();
    paintViewR.height = getHeight(); 

    paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
    paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
    
    String clippedText = SwingUtilities.layoutCompoundLabel(
      this,
      metrics,
      getText(),
      getIcon(),
      SwingConstants.CENTER,
      SwingConstants.LEFT,
      SwingConstants.CENTER,
      SwingConstants.RIGHT,
      paintViewR,
      paintIconR,
      paintTextR,
      4);

    if ( icon != null )
    {
      icon.paintIcon(this, g, paintIconR.x, paintIconR.y);
    }

		int textX = paintTextR.x;
		int textY = paintTextR.y + metrics.getAscent();

    if ( !isEnabled() )
    {
      Color background = getBackground();
      g.setColor( background.brighter() );
      g.drawString( clippedText, textX + 1, textY + 1 );
      g.setColor( background.darker() );
      g.drawString( clippedText, textX, textY );
    }
    else
    {
      g.setColor( getForeground() );
      g.drawString( clippedText, textX, textY );
    }

    if ( m_mouseInside && isEnabled() )
    {
      int width = metrics.stringWidth( clippedText );
      int linePos = textY + 1;
      g.drawLine( textX, linePos, textX + width-1, linePos );
    }

    g.setColor( saveColor );
    g.setFont( saveFont );
    
  }

  private void doAction()
  {
    ActionEvent ae = null;
    if ( m_listeners.size() != 0 )
    {
      ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");
    }
    Iterator l = m_listeners.iterator();
    while( l.hasNext() )
    {
      ((ActionListener)l.next()).actionPerformed( ae );
    }
  }


  private class MouseMotionListener extends MouseAdapter
  {
    public void mouseEntered( MouseEvent e )
    {
      m_mouseInside = true;
      repaint();
    }

    public void mouseExited( MouseEvent e )
    {
      m_mouseInside = false;
      repaint();
    }

    public void mousePressed( MouseEvent e )
    {
      m_armed = true;
    }

    public void mouseReleased( MouseEvent e )
    {
      if ( m_mouseInside )
      {
        if ( isEnabled() && (e.getModifiers() & e.BUTTON1_MASK) != 0 )
        {
          doAction();
        }
      }
      m_armed = false;
    }
  }

  private class ActionPropertyChangeListener implements PropertyChangeListener
  {
    public void propertyChange( PropertyChangeEvent pce )
    {
      if ( "enabled".equals( pce.getPropertyName() ) )
      {
        setEnabled(  ((Boolean) pce.getNewValue() ).booleanValue() );
      }
      else if ( Action.NAME.equals( pce.getPropertyName() ) )
      {
        setText( (String) pce.getNewValue() );
      }
      else if ( Action.SMALL_ICON.equals( pce.getPropertyName() ) )
      {
        setIcon( (Icon) pce.getNewValue() );
      }
    }
  }
}
