package org.freeciv.client;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
public class Terrain implements org.freeciv.tile.FlashingIcon,Constants
{
  protected int id;
  protected int variation;
  protected Icon icon;
  protected Terrain() 
  {
    
  }
  public Terrain( Icon anIcon, int anId, int aVariation ) 
  {
    id = anId;
    icon = anIcon;
    variation = aVariation;
  }
  public void setVisible( boolean aVisible )
  {
    
  }
  public void paintIcon( Component c, Graphics g, int x, int y )
  {
    icon.paintIcon( c, g, x, y );
  }
  public int getIconWidth()
  {
    return icon.getIconWidth();
  }
  public int getIconHeight()
  {
    return icon.getIconHeight();
  }
  public boolean isKnown()
  {
    return true;
  }
  public int getId()
  {
    return id;
  }
  private String getNSEWString()
  {
    StringBuffer b = new StringBuffer( 20 );
    b.append( "Var: " );
    if( ( variation & 8 ) > 0 )
    {
      b.append( "N " );
    }
    if( ( variation & 4 ) > 0 )
    {
      b.append( "S " );
    }
    if( ( variation & 2 ) > 0 )
    {
      b.append( "E " );
    }
    if( ( variation & 1 ) > 0 )
    {
      b.append( "W" );
    }
    return b.toString();
  }
  public String toString()
  {
    return "Terrain " + id + ( isKnown() ? "" : "U" ) + " " + getNSEWString();
  }
  public boolean equals( Object obj )
  {
    return ( ( obj instanceof Terrain ) && ( (Terrain)obj ).id == id && ( (Terrain)obj ).variation == variation );
  }
}
