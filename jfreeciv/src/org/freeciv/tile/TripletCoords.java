package org.freeciv.tile;
public class TripletCoords
{
  public int a;
  public int b;
  public int c;
  public TripletCoords() 
  {
    
  }
  public TripletCoords( int _a, int _b, int _c ) 
  {
    a = _a;
    b = _b;
    c = _c;
  }
  public int distanceTo( TripletCoords other )
  {
    return distanceTo( other.a, other.b, other.c );
  }
  public int distanceTo( int oa, int ob, int oc )
  {
    return ( Math.abs( a - oa ) + Math.abs( b - ob ) + Math.abs( c - oc ) ) / 2;
  }
  public int hashCode()
  {
    return a + b * 1234 + c * 5678921;
  }
  public boolean equals( Object obj )
  {
    return ( obj instanceof TripletCoords && equals( (TripletCoords)obj ) );
  }
  public boolean equals( TripletCoords tc )
  {
    return ( tc != null && tc.a == a && tc.b == b && tc.c == c );
  }
  public String toString()
  {
    return "TripletCoords A=" + a + " B=" + b + " C=" + c;
  }
}
