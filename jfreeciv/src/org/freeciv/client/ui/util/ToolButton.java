package org.freeciv.client.ui.util;

import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;

/**
 * ToolButton is a rollover-effect button with no focus painting; intended
 * for toolbars etc.
 *
 * @author Brian.Duff@oracle.com
 */
public class ToolButton extends JButton
{

  // NB: It's better to implement a PLAF implementation here, because we
  // don't really want this to apply to all L&F. In Java 1.4, though, we can
  // use JToolbarButton to achieve the same effect as this class, so I'm not
  // going to fix it for now.

  protected static final Border EMPTY_BORDER = new EmptyBorder(1, 1, 1, 1);
  protected static final Border RAISED_BORDER = 
    new ThinBevelBorder(ThinBevelBorder.RAISED);
  protected static final Border LOWERED_BORDER =
    new ThinBevelBorder(ThinBevelBorder.LOWERED);

  protected boolean m_isInside = false;
  protected boolean m_isDepressed = false;

  public ToolButton()
  {
    super();
    setFocusPainted( false );
    setMargin( new Insets(0, 0, 0, 0) );
    addMouseListener( new RolloverListener() );
    setBorder( EMPTY_BORDER );
  }

  protected void mouseEntered( MouseEvent e )
  {
    if ( m_isDepressed )
    {
      setBorder( LOWERED_BORDER );
    }
    else
    {
      setBorder( RAISED_BORDER );
    }
    m_isInside = true;
  }

  protected void mouseExited( MouseEvent e)
  {
    setBorder( EMPTY_BORDER );
    m_isInside = false;
  }

  protected void mousePressed(MouseEvent e)
  {
    setBorder( LOWERED_BORDER );
    m_isDepressed = true;
  }

  protected void mouseReleased( MouseEvent e )
  {
    m_isDepressed = false;
    if ( m_isInside )
    {
      setBorder( RAISED_BORDER );
    }
    else
    {
      setBorder( EMPTY_BORDER );
    }
  }

  protected boolean isValidButtonEvent( MouseEvent e )
  {
    return ( (e.getModifiers() & e.BUTTON1_MASK) != 0);
  }

  private class RolloverListener extends MouseAdapter
  {
    public void mouseEntered( MouseEvent e )
    {
      ToolButton.this.mouseEntered( e );
    }

    public void mouseExited( MouseEvent e )
    {
      ToolButton.this.mouseExited( e );
    }

    public void mousePressed( MouseEvent e )
    {
      if ( isValidButtonEvent( e ) )
      {
        ToolButton.this.mousePressed( e );
      }
    }

    public void mouseReleased( MouseEvent e )
    {
      if ( isValidButtonEvent( e ) )
      {
        ToolButton.this.mouseReleased( e );
      }
    }
  }
}