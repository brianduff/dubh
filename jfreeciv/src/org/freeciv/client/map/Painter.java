package org.freeciv.client.map;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;

/**
 * The Painter interface describes how a particular type of map is painted
 *
 * @author Brian Duff
 * @author Luke Lindsay
 */
interface Painter 
{
  /**
   * Paint the background map at the specified location into the specified
   * graphics context
   *
   * @param g the graphics context to paint into
   * @param x the pixel offset horizontally to start painting from
   * @param y the pixel offset vertically to start painting from
   * @param w the pixel width to paint
   * @param h the pixel height to paint
   */
  public void paintBackground( Graphics g, int x, int y, int width, int height );

  /**
   * Update the tile at the specified map location (?)
   *
   * @param mapCoord the map (tile) co-ordinates of the tile to update
   */
  public void updateTile( Point mapCoord );

  /**
   * Refresh the entire background
   */
  public void refreshBackground();

  /**
   * Paint the buffer rectangle??
   */
  public void paintBufferRectangle( int x, int y, int width, int height );

  /**
   * Set the size of the background buffer
   * 
   * @param width the new width of the background buffer
   * @param heigh the new height of the background buffer
   */
  public void setBackgroundBufferSize( int width, int height );

  /**
   * Paint the background buffer into the output graphics context.
   *
   * @param outGraphics the output graphics context
   * @param visibleRect the current visible rectangle.
   */
  public void paint( Graphics outGraphics, Rectangle visibleRect );
  
  
}