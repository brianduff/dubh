package org.freeciv.client.map;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Concrete implementation of a painter that uses square tiles
 *
 * @author Brian Duff
 * @author Like Lindsay
 */
class SquareTiledPainter extends TiledPainter
{
  private AbstractMapView m_mapView;

  SquareTiledPainter( AbstractMapView mapView )
  {
    m_mapView = mapView;
  }


  public void updateTile( Point mapCoord )
  {
    Dimension tileSize = m_mapView.getTileSize();
    Dimension mapSize = m_mapView.getMapSizeInTiles();

    Rectangle rectToRefresh = new Rectangle( 
      (mapCoord.x - 1 ) * tileSize.width,
      (mapCoord.y - 1 ) * tileSize.height,
      tileSize.width * 3,
      tileSize.height * 3   // BD: ??
    );

    if ( getBufferRect().intersects( rectToRefresh ) )
    {
      int  tileWidth = tileSize.width;
      int  tileHeight = tileSize.height;
      
      int  mapXOffset = getBufferRect().x / tileWidth;
      int  mapYOffset = getBufferRect().y / tileHeight;
      
      int  xOffset = getBufferRect().x % tileWidth;
      int  yOffset = getBufferRect().y % tileHeight;
      
      for( int  x = mapCoord.x - 1; x < mapCoord.x + 2; x++ ) 
      {
        for( int y = mapCoord.y - 1; y < mapCoord.y + 2; y++ ) 
        {
          Point  tileMapCoordinate = new Point( x, y );
          Point  screenPosition = new Point( 
            ( x - mapXOffset ) * tileWidth - xOffset, 
            ( y - mapYOffset ) * tileWidth - yOffset 
          );
          m_mapView.paintTile( 
            getBufferGraphics(), tileMapCoordinate, screenPosition 
          );
        }
      }      
    }
  }

  public synchronized void paintBufferRectangle( int x, int y, int 
    width, int height ) 
  {
    paintRect( getBufferGraphics(), x, y, width, height );
  }

  public synchronized void paintRect( Graphics g, int x, int y,
    int width, int height )
  {
    Dimension tileSize = m_mapView.getTileSize();
    Dimension mapSize = m_mapView.getMapSizeInTiles();

    int tileWidth = tileSize.width;
    int tileHeight = tileSize.height;

    int mapXOffset = getBufferRect().x / tileWidth;
    int mapYOffset = getBufferRect().y / tileHeight;

    int xOffset = getBufferRect().x % tileWidth;
    int yOffset = getBufferRect().y % tileHeight;

    Point bufferOrigin = getBufferRect().getLocation();

    for ( int i = x / tileWidth; i < (( x + width ) / tileWidth + 2 ); i++ )
    {
      for ( int j = y / tileHeight; j < (( y + height ) / tileHeight + 2 ); j++ )
      {
        if ( i >= 0 && j >= 0 && i < mapSize.width && j < mapSize.height ) 
        {
          Point mapPosition = new Point( mapXOffset + i, mapYOffset + j );
          Point screenPosition = new Point( 
            tileWidth * i - xOffset, tileHeight * j - yOffset
          );
          m_mapView.paintTile( (Graphics2D)g, mapPosition, screenPosition );
        }
      }
    }
    
  }
  
}