package org.freeciv.client;

import java.awt.Component;

/**
 * View on to the map.
 */
public final class MapView 
{
  // Mostly mapview.c

  private MapOverview m_mapOverview;
  private Client m_client;

  MapView(Client c)
  {
    m_client = c;
  }

  Client getClient()
  {
    return m_client;
  }

  Component getOverviewComponent()
  {
    return m_mapOverview.getComponent();
  }

  /**
   * Set the dimensions of the map overview
   *
   * @param x the width of the map
   * @param y the height of the map
   */
  public void setOverviewDimensions( int x, int y )
  {
    m_mapOverview = new MapOverview( this );
    m_mapOverview.setDimensions( x, y ); 
  }

  /**
   * Refresh the whole overview canvas
   */
  public void refreshOverviewCanvas()
  {
    m_mapOverview.refresh();
  }

  /**
   * Refresh a tile on the map canvas
   */
  void refreshTileMapCanvas( int x, int y, boolean writeToScreen )
  {
    x = getClient().getGame().getMap().adjustX( x );
    y = getClient().getGame().getMap().adjustY( y );

    // if ( isTileVisible( )...)
    //  updateMapCanvas()

    updateOverviewTile( x, y );
  }

  private void updateOverviewTile( int x, int y )
  {
    m_mapOverview.repaint( x, y );
  }
}