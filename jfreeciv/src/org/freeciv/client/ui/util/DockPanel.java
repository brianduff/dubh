package org.freeciv.client.ui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

/**
 * A dockpanel has a dock panel caption
 */
public class DockPanel extends JPanel
{
  private DockedPanelCaption m_caption;
  private JComponent m_content;
  private Border m_oldBorder;
  
  public DockPanel( )
  {
    setLayout( new BorderLayout() );
    m_caption = new DockedPanelCaption();
    add( m_caption, BorderLayout.NORTH );
  }

  public DockPanel( String caption, JComponent contentArea )
  {
    this();
    setText( caption );
    setContentArea( contentArea );
  }

  public void setText( String caption )
  {
    m_caption.setText( caption );
  }

  public void setContentArea( JComponent c )
  {
    if ( m_content != null )
    {
      remove( m_content );
      if ( m_oldBorder != null )
      {
        m_content.setBorder( m_oldBorder );
      }
      m_content = null;
    }
    if ( c != null )
    {
      m_oldBorder = c.getBorder();
      c.setBorder( new DockTargetBorder() );
      add( c, BorderLayout.CENTER );
    }
    invalidate();
  }

  /**
   * DockTarget Border: A simple three sided color border.
   */
  public class DockTargetBorder extends AbstractBorder
  {

    private Color shadowColor = 
      UIManager.getLookAndFeelDefaults().getColor("controlShadow");

    public Insets getBorderInsets( Component c )
    {
      return new Insets( 1, 1, 1, 1 );
    }

    public Insets getBorderInsets( Component c, Insets insets )
    {
      insets.left = insets.top = insets.right = insets.bottom = 1;
      return insets;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, 
      int w, int h) 
    {
      final Color oldColor = g.getColor();
      g.translate( x, y );

      g.setColor( shadowColor );
      g.drawLine( 0, 0, 0, h-1 );
      g.drawLine( 0, h-1, w-1, h-1 );
      g.drawLine( w-1, h-1, w-1, 0 ); 

      g.translate( -x, -y );
      g.setColor( oldColor );
    }

  }  
  
}