package org.freeciv.client.map.grid;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import org.freeciv.client.map.MapViewInfo;
import org.freeciv.client.map.MapLayer;

/**
 * Superclass for layers on the grid map which deal with tiles. Provides some
 * useful translations between screen and map coordinates
 *
 * @author Brian Duff
 */
abstract class GridMapTileLayer implements MapLayer
{
  private GridMapView m_view;

  /**
   * Constrcut the grid map tile layer
   */
  protected GridMapTileLayer(GridMapView view)
  {
    m_view = view;
  }

  /**
   * Nasty hack. Your subclass should return true from this if you
   * are being displayed via the BufferLayer. Returns true by default, so
   * layers that are not in the buffer layer should return false from this
   * (or they will repaint incorrectly)
   */
  protected boolean isBuffered()
  {
    return true;
  }

  /**
   * GridMapTileLayer implements paint() by converting screen coordinates
   * to tile coordinates and asking the subclass to paint each affected tile.
   *
   * @param g the graphics context
   * @param rect a rectangular region within g to paint to
   * @param mvi information about the view
   */
  public void paint( Graphics g, Rectangle rect, MapViewInfo mvi )
  {
    // Figure out which tiles are visible, and only paint those tiles which
    // are both visible and fall within the specified rectangle.
    Dimension tileSize = mvi.getTileSize();
    Dimension mapSize = mvi.getMapSizeInTiles();

    int tileWidth = tileSize.width;
    int tileHeight = tileSize.height;

    int mapXOffset = isBuffered() ? mvi.getVisibleRectangle().x / tileWidth : 0;
    int mapYOffset = isBuffered() ? mvi.getVisibleRectangle().y / tileHeight : 0;

    int xOffset = isBuffered() ?  mvi.getVisibleRectangle().x % tileWidth : 0;
    int yOffset = isBuffered() ? mvi.getVisibleRectangle().y % tileHeight : 0;

    Point viewRectOrigin = mvi.getVisibleRectangle().getLocation();

    for ( int i = rect.x / tileWidth; i < (( rect.x + rect.width ) / tileWidth + 2 ); i++ )
    {
      for ( int j = rect.y / tileHeight; j < (( rect.y + rect.height ) / tileHeight + 2 ); j++ )
      {
        if ( i >= 0 && j >= 0 && i < mapSize.width && j < mapSize.height ) 
        {
          Point mapPosition = new Point( mapXOffset + i, mapYOffset + j );
          Point gPosition = new Point( 
            tileWidth * i - xOffset, tileHeight * j - yOffset
          );
          paintTile( g, mapPosition, gPosition, mvi );
        }
      }
    }
  }

  /**
   * Paint the specified tile at the specified location
   *
   * @param g the graphics context to paint into
   * @param mapPosition the map ( tile ) coordinates of the tile to paint
   * @param graphicsPosition where to paint the tile in the graphics context
   */
  protected abstract void paintTile( 
    Graphics g, Point mapPosition, Point graphicsPosition, MapViewInfo info
  );
  
}