package org.freeciv.client.ui.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

/**
 *  <CODE>ThinBevel</CODE> is the same as a {@link BevelBorder}, except that 
 *  its thickness is 1 pixel instead of 2 pixels.  The lowered bevel is 
 *  rendered with a slightly brighter inner shadow color than the corresponding
 *  outer shadow color, to give the subtle effect that the
 *  pressed button has a shallower depth than the apparent height
 *  of the unpressed button.
 */
public class ThinBevelBorder extends BevelBorder
{
  private static final Color highlightColor;
  private static final Color shadowColor;

  public ThinBevelBorder( int type )
  {
    super( type );
  }

  public Insets getBorderInsets( Component c )
  {
    return new Insets( 1, 1, 1, 1 );
  }

  public Insets getBorderInsets( Component c, Insets insets )
  {
    insets.left = insets.top = insets.right = insets.bottom = 1;
    return insets;
  }

  protected void paintRaisedBevel( Component c, Graphics g,
                                   int x, int y, int w, int h )
  {
    final Color oldColor = g.getColor();
    g.translate( x, y );

    g.setColor( highlightColor );
    g.drawLine( 0, 0, 0, h-1 );
    g.drawLine( 1, 0, w-1, 0 );

    g.setColor( shadowColor );
    g.drawLine( 1, h-1, w-1, h-1 );
    g.drawLine( w-1, 1, w-1, h-2 );

    g.translate( -x, -y );
    g.setColor( oldColor );
  }

  protected void paintLoweredBevel( Component c, Graphics g,
                                    int x, int y, int w, int h )
  {
    final Color oldColor = g.getColor();
    g.translate( x, y );

    g.setColor( shadowColor );
    g.drawLine( 0, 0, 0, h-1 );
    g.drawLine( 1, 0, w-1, 0 );

    g.setColor( highlightColor );
    g.drawLine( 1, h-1, w-1, h-1 );
    g.drawLine( w-1, 1, w-1, h-2 );

    g.translate( -x, -y );
    g.setColor( oldColor );
  }

  //
  // Static initializer
  //

  static
  {
    final UIDefaults defaults = UIManager.getLookAndFeelDefaults();
    shadowColor = defaults.getColor("controlShadow");
    highlightColor = defaults.getColor("controlLtHighlight");
  }
}
