package org.freeciv.client;

import java.awt.Dimension;
import java.awt.Point;

/**
 * A map view that uses isometric tiles. 
 *
 * @author Brian Duff
 */
class IsometricMapView extends AbstractMapView
{
  private static final int EXTRA_BOTTOM_ROW = 6;

  IsometricMapView( Client c )
  {
    super( c );
  }

  /**
   * Update the visible map canvas. Called on paint events on the map component
   * when the map has been resized
   */
  protected final void updateVisibleMap()
  {
    int width, height;
    width = height = getBufferTileWidth() + getBufferTileHeight();
    updateMapBuffer( 
      getViewTileOriginX(), getViewTileOriginY() - getBufferTileWidth(), 
      width, height, true 
    );

    showCityDescriptions();      
  }
  
  protected void updateMapBufferImpl( int x, int y, int width, int height )
  {
    System.out.println( "updateMapBuffer (isometric) "+x+", "+y+", "+width+", "+height );
    // TODO
  }

  protected boolean isTileVisible( int x, int y )
  {
    return getCanvasPosition(x, y, new Point());
  } 

  protected void centerOnTileImpl( int x, int y )
  {
  }


  /**
   * Find the pixel co-ordinates of a tile.
   *
   * @param map_x the x-coordinate of the tile
   * @param map_y the y-coordinate of the tile
   * @param canvasPos on exit, contains the pixel co-ordinates of the tile.
   * @return true if the specified tile is inside the visible map
   */
  protected boolean getCanvasPosition( int map_x, int map_y, Point canvasPos )
  {
    int x0 = getViewTileOriginX();
  
    // canvasPos is the top corner of the tile.
    int diff_xy, diff_x, diff_y;
    int width, height;

    // BD: Check this
    Dimension visRect = getViewArea();
    width = visRect.width;
    height = visRect.height;

    map_x %= getMap().getWidth();
    if ( map_x < x0 )
    {
      map_x += getMap().getWidth();
    }

    diff_xy = ( map_x + map_y ) - ( getViewTileOriginX() + getViewTileOriginY() );
    canvasPos.y = diff_xy/2 * getNormalTileHeight() + (diff_xy%2) +
      (getNormalTileHeight()/2);

    map_x -= diff_xy/2;
    map_y -= diff_xy/2;

    diff_x = map_x - getViewTileOriginX();
    diff_y = getViewTileOriginY() - map_y;

    canvasPos.x = (diff_x - 1) * getNormalTileWidth()
      + ( diff_x == diff_y ? getNormalTileWidth() : getNormalTileWidth() / 2)
      + ( diff_y > diff_x ? getNormalTileWidth() : 0 );

    canvasPos.x -= getNormalTileWidth()/2;

    return ( canvasPos.x > -getNormalTileWidth())
      && canvasPos.x < (width + getNormalTileWidth()/2)
      && (canvasPos.y > -getNormalTileHeight())
      && ( canvasPos.y < height );
  }  
  
}