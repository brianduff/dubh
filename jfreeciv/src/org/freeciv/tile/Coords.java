package org.freeciv.tile;
public class Coords
{
  public int x;
  public int y;
  public Coords() 
  {
    
  }
  public Coords( int aX, int aY ) 
  {
    x = aX;
    y = aY;
  }
  public int hashCode()
  {
    return x * 1234 + y;
  }
  public boolean equals( Object obj )
  {
    return obj instanceof Coords && equals( (Coords)obj );
  }
  public boolean equals( Coords c )
  {
    return c != null && c.x == x && c.y == y;
  }
  public String toString()
  {
    return "Coords X=" + x + " Y=" + y;
  }
}
