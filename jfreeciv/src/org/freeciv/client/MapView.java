package org.freeciv.client;

import java.awt.Component;

/**
 * An individual view onto the game map. There can be more than
 * one view on the map at any time with different co-ordinates and 
 * (zoom level??).
 *
 * @author Brian.Duff@dubh.org
 */
public final class MapView 
{
  // Mostly mapview.c

  private Client m_client;

  MapView(Client c)
  {
    m_client = c;
  }

  Client getClient()
  {
    return m_client;
  }

  /**
   * Refresh the tile at the specified location if it is visible through this
   * view on to the map.
   *
   * @param x the x-coordinate of the tile to refresh
   * @param y the y-coordinate of the tile to refresh
   */
  void refreshTileMapCanvas( int x, int y )
  {
    x = getClient().getGame().getMap().adjustX( x );
    y = getClient().getGame().getMap().adjustY( y );

    // TODO: Actually refresh the mapview
    // if ( isTileVisible( )...)
    //  updateMapCanvas()
  }

}