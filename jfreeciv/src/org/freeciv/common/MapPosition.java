package org.freeciv.common;

import java.awt.Point;


/**
 * A location on the Freeciv map.
 */
public class MapPosition extends Point 
{
  public MapPosition()
  {
    super();
  }

  public MapPosition(int x, int y)
  {
    super(x, y);
  }

  public MapPosition(MapPosition ml)
  {
    super( ml );
  }

  public MapPosition( Point p )
  {
    super( p );
  }
}