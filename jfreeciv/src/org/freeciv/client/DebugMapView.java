package org.freeciv.client;

import java.awt.Color;
import java.awt.Point;

import org.freeciv.common.CommonConstants;
import org.freeciv.common.Tile;

/**
 * DebugMapView is a subclass of GridMapView that paints blocks of color 
 * instead of icons.
 *
 * @author Brian Duff
 */
public class DebugMapView extends GridMapView
{
  DebugMapView( Client c)
  {
    super( c );
  }

  private Color getTileColor( Tile t )
  {
    if ( t.getTerrain() == CommonConstants.T_OCEAN )
    {
      return Color.blue;
    }

    return Color.green;
  }

  /**
   * This is where we do the overridding.
   */
  protected void updateMapBufferImpl( int x, int y, int width, int height )
  {
    for ( int y_itr = y; y_itr < y + height; y_itr++ )
    {
      for ( int x_itr = x; x_itr < x + width; x_itr++ )
      {
        Point p = new Point();
        getCanvasPosition( x_itr, y_itr , p );
        if ( isTileVisible( x_itr, y_itr ) )
        {
          if ( getMap().getTile( x_itr, y_itr ).isKnown() )
          {

            getGraphicsBuffer().setColor( Color.green );
            getGraphicsBuffer().setBackground( Color.blue );
            //getTileColor(
            //  getMap().getTile( x_itr, y_itr )
            //) );
            getGraphicsBuffer().fillRect(
              p.x, p.y, getNormalTileWidth(), getNormalTileHeight()
            );

            /*
            getGraphicsBuffer().setColor( Color.white );
            getGraphicsBuffer().drawRect( 
              p.x, p.y, getNormalTileWidth(), getNormalTileHeight()
            );   
            */
          }

        }
      }
    }
  }  
}