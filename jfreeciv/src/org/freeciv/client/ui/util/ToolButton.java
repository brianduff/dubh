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

  private static final Border EMPTY_BORDER = new EmptyBorder(1, 1, 1, 1);
  private static final Border RAISED_BORDER = 
    new ThinBevelBorder(ThinBevelBorder.RAISED);
  private static final Border LOWERED_BORDER =
    new ThinBevelBorder(ThinBevelBorder.LOWERED);

  public ToolButton()
  {
    super();
    setFocusPainted( false );
    setMargin( new Insets(0, 0, 0, 0) );
    addMouseListener( new RolloverListener() );
    setBorder( EMPTY_BORDER );
  }

  private class RolloverListener extends MouseAdapter
  {
    private boolean m_isInside = false;
    private boolean m_isDepressed = false;
  
    public void mouseEntered( MouseEvent e )
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

    public void mouseExited( MouseEvent e )
    {
      setBorder( EMPTY_BORDER );
      m_isInside = false;
    }

    public void mousePressed( MouseEvent e )
    {
      setBorder( LOWERED_BORDER );
      m_isDepressed = true;
    }

    public void mouseReleased( MouseEvent e )
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
  }
}