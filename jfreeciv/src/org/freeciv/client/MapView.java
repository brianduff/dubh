package org.freeciv.client;

import javax.swing.JComponent;

/**
 * The external interface to a single view on the map.
 *
 * @author Brian.Duff@dubh.org
 */
public interface MapView 
{
  /**
   * Refresh the tile at the specified location if it is visible through this
   * view on to the map.
   *
   * @param x the x-coordinate of the tile to refresh
   * @param y the y-coordinate of the tile to refresh
   */
  void refreshTileMapCanvas( int x, int y );

  /**
   * Get the actual physical component for this map view
   */
  JComponent getComponent();
}