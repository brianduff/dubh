package org.freeciv.client.map;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.Icon;

import org.freeciv.client.Options;
import org.freeciv.common.Map;
import org.freeciv.common.Player;
import org.freeciv.common.TerrainType;
import org.freeciv.common.Tile;
import org.freeciv.common.Unit;

/**
 * Information about the map view made available to layers while painting
 *
 * @author Brian Duff
 */
public interface MapViewInfo 
{
  /**
   * Get the portion of the map component which is visible to the user through
   * this view.
   *
   * @return a rectangle indicating (in pixels) the portion of the map 
   *    component which the user can currently see
   */
  Rectangle getVisibleRectangle();

  /**
   * Get the size of a tile
   *
   * @return a Dimension indicating the pixel width and height of a tile
   */
  Dimension getTileSize();

  /**
   * Get the size of the map in tiles.
   *
   * @return a Dimension indicating how big the map is in tiles
   */
  Dimension getMapSizeInTiles();

  /**
   * Get options for the view.
   *
   * @return an Options object that provides information about the options
   *    set for this view
   */
  Options getOptions();

  /**
   * Get an image from the tilespec used by this view
   *
   * @param imageKey the key of an image
   * @return an image
   */
  Icon getImage( String key );

  /**
   * Get information about a tile.
   *
   * @param mapPos the map position to get tile information for
   * @return a Tile object for the specified position
   */
  Tile getTileAtMapPos( Point mapPos );

  /**
   * Get the map
   *
   * @return Map information about the map
   */
  Map getMap();

  /**
   * Get terrain type of the specified id
   */
  TerrainType getTerrainType( int id );

  /**
   * Get the unit in focus
   */
  Unit getUnitInFocus();

  /**
   * Are flags in the tilespec transparent?
   */
  boolean areFlagsTransparent();

  /**
   * Get the current player. Only used by the UnitLayer at the moment, could
   * get rid of this once Dom's changes have landed
   */
  Player getCurrentPlayer();

}
