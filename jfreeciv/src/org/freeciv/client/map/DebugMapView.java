package org.freeciv.client.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import org.freeciv.client.Client;
import org.freeciv.common.CommonConstants;
import org.freeciv.common.Tile;

/**
 * DebugMapView is a subclass of GridMapView that paints blocks of color
 * instead of icons.
 *
 * @author Brian Duff
 */
public class DebugMapView 
{
  DebugMapView( Client c)
  {
    //super( c );
  }

  private Color getTileColor( Tile t )
  {
    if ( t.getTerrain() == CommonConstants.T_OCEAN )
    {
      return Color.blue;
    }

    return Color.green;
  }
/*
  protected void paintTile( Graphics2D g, Point tilePos, Point screenPos )
  {
    Tile t = getMap().getTile( tilePos.x, tilePos.y );
    g.setColor( Color.black );
    if ( t != null )
    {
      if ( t.isKnown() )
      {
        if (t.getTerrain() == CommonConstants.T_OCEAN)
        {
          g.setColor( Color.blue );
        }
        else
        {
          g.setColor( Color.green );
        }
      }
    }

    g.fillRect( screenPos.x, screenPos.y, 
      getNormalTileWidth(), getNormalTileHeight()
    );    
  }
  */
}