package org.freeciv.client.map.grid;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.Icon;

import org.freeciv.client.Client;
import org.freeciv.client.Colors;
import org.freeciv.client.map.AbstractMapView;
import org.freeciv.client.map.BufferLayer;
import org.freeciv.client.map.MapLayer;
import org.freeciv.common.MapPosition;
import org.freeciv.common.Player;

/**
 * A map view that uses square grid tiles.
 *
 * @author Brian Duff
 */
public class GridMapView extends AbstractMapView
{
  private static final int EXTRA_BOTTOM_ROW = 1;

  /**
   * For some reason, NOT using the back buffer appears to be faster... ??
   */
  private static final boolean USE_BACK_BUFFER = true;

  private Collection m_layers;

  public GridMapView( Client c )
  {
    super( c );
  }

  public void repaintTile( int tilex, int tiley )
  {
    if ( isTileVisible(tilex, tiley) )
    {
      Point pos = new Point();
      getCanvasPosition( tilex, tiley , pos );

      //getMapComponent().repaint( pos.x, pos.y, getNormalTileWidth(), getNormalTileHeight() );
      
    }
  }

  /**
   * Paint a single layer
   */
  void paintLayer( MapLayer l )
  {
    //getMapComponent().paintSingleLayer( l );
  }

  protected Collection getLayers()
  {  
    if ( m_layers == null )
    {
      ArrayList l = new ArrayList();

      if ( USE_BACK_BUFFER )
      {
        m_layers = new ArrayList();
        BufferLayer bl = new BufferLayer(l);
        m_layers.add( bl );
      }
      else
      {
        m_layers = l;
      }
      l.add( new TerrainLayer( this ) );   
      l.add( new FogOfWarLayer( this ) );

    }
    m_layers.add( new CityNamesLayer( this ) );
    m_layers.add( new UnitLayer( this ) );
    return m_layers;
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