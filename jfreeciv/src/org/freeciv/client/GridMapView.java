package org.freeciv.client;

import java.awt.Point;

/**
 * A map view that uses square grid tiles.
 *
 * @author Brian Duff
 */
class GridMapView extends AbstractMapView
{
  private static final int EXTRA_BOTTOM_ROW = 1;

  public GridMapView( Client c )
  {
    super( c );
  }

  /**
   * Update the visible map canvas. Called on paint events on the map component
   * when the map has been resized
   */
  protected final void updateVisibleMap()
  {
    updateMapBuffer( 
      getViewTileOriginX(), getViewTileOriginY(), 
      getBufferTileWidth(), getBufferTileHeight(), 
      true 
    ); 
    showCityDescriptions();      
  }

  protected void updateMapBufferImpl( int x, int y, int width, int height )
  {
    System.out.println( "updateMapBuffer (grid) "+x+", "+y+", "+width+", "+height);

  
  }

  protected boolean isTileVisible( int x, int y )
  {
    // tile_visible_mapcanvas
    return 
      (y >= getViewTileOriginY() && y < getViewTileOriginY()+getBufferTileHeight() &&
      ( (x >= getViewTileOriginX() && x < getViewTileOriginX() + getBufferTileWidth() ) ||
        (x + getMap().getWidth() >= getViewTileOriginX() && 
            x+ getMap().getWidth() < getViewTileOriginX() + getBufferTileWidth())
      ));
  }

  protected void centerOnTileImpl( int x, int y )
  {
    int newX, newY;

    newX = getMap().adjustX( x - getBufferTileWidth() / 2 );
    newY = getMap().adjustY( y - getBufferTileHeight() / 2 );

    if ( newY > getMap().getHeight() + EXTRA_BOTTOM_ROW - getBufferTileHeight() )
    {
      newY = getMap().adjustY( 
        getMap().getHeight() + EXTRA_BOTTOM_ROW - getBufferTileHeight() );
    }

    setViewTileOriginX( newX );
    setViewTileOriginY( newY );

    // caller is responsible for updating scrollbars
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
    // get_canvas_xy
    int x0 = getViewTileOriginX();
    int y0 = getViewTileOriginY();
    
    if ( x0 + getBufferTileWidth() <= getMap().getWidth() )
    {
      canvasPos.x = map_x - x0;
    }
    else if ( map_x >= x0 )
    {
      canvasPos.x = map_x - x0;
    }
    else if ( map_x < getMap().adjustX( x0 + getBufferTileWidth() ) )
    {
      canvasPos.x = map_x + getMap().getWidth() - x0;
    }
    else
    {
      canvasPos.x = -1;
    }

    canvasPos.y = map_y - y0;

    canvasPos.x *= getNormalTileWidth();
    canvasPos.y *= getNormalTileHeight();

    return canvasPos.x >= 0
      && canvasPos.x < getBufferTileWidth() * getNormalTileWidth()
      && canvasPos.y >= 0
      && canvasPos.y < getBufferTileWidth() * getNormalTileHeight();
  }
}