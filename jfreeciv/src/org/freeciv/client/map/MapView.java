package org.freeciv.client.map;

import javax.swing.JComponent;

import org.freeciv.client.panel.MapOverviewJumpListener;
import org.freeciv.common.City;

/**
 * The external interface to a single view on the map.
 *
 * @author Brian.Duff@dubh.org
 */
public interface MapView extends MapOverviewJumpListener
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
   * Refresh a region of the map buffer
   */
  void updateMapBuffer( int tilex, int tiley, int tilew, int tileh, 
    boolean repaint );

  /**
   * Refresh the visible region of the map buffer
   */
  void updateMapBufferVisible();

  /**
   * Initialize the map. You should call this after the MapInfo packet
   * has been received and the game map knows what size the map is
   */
  void initialize();

  void centerOnTile( int tilex, int tiley );

  /**
   * Get the actual physical component for this map view
   */
  JComponent getComponent();

  /**
   * Is this map a city view?
   */
  boolean isCityView();
  
  /**
   * If this is a city view, set the city being viewed
   */
  void setCity( City city );
  
}