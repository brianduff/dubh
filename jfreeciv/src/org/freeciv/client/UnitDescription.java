package org.freeciv.client;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.*;
public class UnitDescription extends JComponent
{
  Icon emptyIcon;
  Unit unit;
  Client client;
  public static final String emptyText = "";
  public UnitDescription( Client c ) 
  {
    super();
    client = c;
    setPreferredSize( new Dimension( 120, 110 ) );
    setMaximumSize( getPreferredSize() );
  }
  public void setUnit( Unit u )
  {
    unit = u;
    repaint();
  }
  public void paintComponent( Graphics g )
  {
    ( (Graphics2D)g ).setComposite( client.getComponentAlpha() );
    g.setColor( client.getComponentColor() );
    Rectangle r = g.getClipBounds();
    g.fillRect( r.x, r.y, r.width, r.height );
    g.setPaintMode();
    if( unit == null )
    {
      return ;
    }
    Insets ins = getInsets();
    Dimension d = getSize();
    int mx = ( d.width - ins.left - ins.right ) / 2;
    int ux = mx - unit.getIconWidth() / 2;
    int uy = ins.top + 5;
    unit.reallyPaintIcon( this, g, ux, uy, false );
    uy += unit.getIconHeight();
    FontMetrics f = getFontMetrics( getFont() );
    String str = unit.prototype.name + " " + unit.id;
    ux = mx - ( f.stringWidth( str ) ) / 2;
    uy += f.getHeight();
    g.setColor( Color.white );
    g.drawString( str, ux, uy );
    str = "Moves :" + unit.movesleft / 3 + "/" + unit.prototype.move_rate / 3;
    ux = mx - ( f.stringWidth( str ) ) / 2;
    uy += f.getHeight();
    g.drawString( str, ux, uy );
    str = "x" + unit.x + " y" + unit.y;
    ux = mx - ( f.stringWidth( str ) ) / 2;
    uy += f.getHeight();
    g.drawString( str, ux, uy );
  }
}
