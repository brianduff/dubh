package org.freeciv.client.map;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import javax.swing.Icon;

import org.freeciv.client.Client;
import org.freeciv.client.Colors;
import org.freeciv.common.MapPosition;
import org.freeciv.common.Player;

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

  protected Painter createPainter()
  {
    return new SquareTiledPainter(this);
  }

  protected void paintTile( Graphics2D g, Point tilePos, Point screenPos )
  {
    boolean cityMode = false;
  
    if ( getMap().normalizeMapPosition( tilePos.x, tilePos.y ) != null &&
         getMap().getTile( tilePos.x, tilePos.y ).isKnown() )
    {
      Player[] player = new Player[1];
      boolean[] solidBg = new boolean[1];
      List l = getClient().getTileSpec().getSpritesAt( tilePos.x, tilePos.y, 
        false, solidBg, player
      );

      int i = 0;

      if ( solidBg[0] )
      {
        if ( player[0] != null )
        {
          g.setColor( Colors.getPlayerColor( player[0] ) );
        }
        else
        {
          g.setColor( Colors.getStandardColor( Colors.COLOR_STD_BACKGROUND ) );
        }

        g.fillRect( screenPos.x, screenPos.y, getNormalTileWidth(), 
          getNormalTileHeight() );
      }
      else
      {
        ((Icon)l.get(0)).paintIcon( null, g, screenPos.x, screenPos.y );
        i++;
      }

      for ( ; i < l.size(); i++ )
      {
        Icon icon = (Icon) l.get( i );
        if ( icon != null )
        {
          icon.paintIcon( null, g, screenPos.x, screenPos.y );
        }
      }

      if ( getClient().getOptions().drawMapGrid )
      {
        boolean inRadius = 
          getClient().getGame().getCurrentPlayer().inCityRadius( 
            tilePos.x, tilePos.y
          );

        // Left side
        if ( getTile( tilePos.x-1, tilePos.y ).isKnown() && 
             ( inRadius ||
               getClient().getGame().getCurrentPlayer().inCityRadius( 
                tilePos.x-1, tilePos.y)) )
        {
          g.setColor( Colors.getStandardColor( Colors.COLOR_STD_WHITE ) );          
        }
        else
        {
          g.setColor( Colors.getStandardColor( Colors.COLOR_STD_BLACK ) );
        }

        g.drawLine( screenPos.x, screenPos.y, 
          screenPos.x, screenPos.y + getNormalTileHeight() );

        // Top side
        if ( getTile( tilePos.x, tilePos.y-1).isKnown() &&
          ( inRadius || 
            getClient().getGame().getCurrentPlayer().inCityRadius(
              tilePos.x, tilePos.y-1)))
        {
          g.setColor( Colors.getStandardColor( Colors.COLOR_STD_WHITE ) );
        }
        else
        {
          g.setColor( Colors.getStandardColor( Colors.COLOR_STD_BLACK ) );
        }

        g.drawLine( screenPos.x, screenPos.y, 
          screenPos.x + getNormalTileWidth(), screenPos.y );
      }

      if ( getClient().getOptions().drawCoastline && 
        ! getClient().getOptions().drawTerrain )
      {
        int t1 =  getMap().getTerrain( tilePos.x, tilePos.y );
        int t2;

        int x1 = tilePos.x-1;
        int y1 = tilePos.y;

        g.setColor( Colors.getStandardColor( Colors.COLOR_STD_OCEAN ) );

        MapPosition mp = new MapPosition( x1, y1 );
        if ( getMap().normalizeMapPosition( mp ) )
        {
          t2 = getMap().getTerrain( mp.x, mp.y );
          // left side
          if ( t1 == T_OCEAN || t2 == T_OCEAN )
          {
            g.drawLine( screenPos.x, screenPos.y, 
              screenPos.x, screenPos.y + getNormalTileHeight()
            );
          }
        }
        mp.x = tilePos.x;
        mp.y = tilePos.y-1;
        if ( getMap().normalizeMapPosition( mp ) )
        {
          t2 = getMap().getTerrain( mp.x, mp.y );
          if ( t1 == T_OCEAN || t2 == T_OCEAN )
          {
            g.drawLine( screenPos.x, screenPos.y, 
              screenPos.x + getNormalTileWidth(), screenPos.y );
          }
        }
      }
      
      
    }
    else
    {
      // Tile is unknown
      putBlackTile( g, screenPos );
    }

    if ( !cityMode )
    {
      // TODO: Goto lines 
    }

  }

  private void putBlackTile( Graphics2D g, Point screenPos )
  {
    // mapview.c: pixmap_put_black_tile()

    g.setColor( Colors.getStandardColor( Colors.COLOR_STD_BLACK ) );
    g.fillRect( screenPos.x, screenPos.y, getNormalTileWidth(), 
      getNormalTileHeight() );
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