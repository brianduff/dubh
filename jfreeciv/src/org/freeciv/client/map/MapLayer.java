package org.freeciv.client.map;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The map is composed of multiple ordered layers, which are painted on top
 * of each other. Some layers are painted to the backbuffer, and others directly
 * to the screen.
 *
 * @author Brian Duff
 */
public interface MapLayer 
{
  /**
   * Paint this layer
   *
   * @param g a graphics context in which to paint
   * @param rect a rectangular region within g to paint to.
   * @param mvi information about the map view the layer can use to paint
   *    itself.
   */
  void paint(Graphics g, Rectangle rect, MapViewInfo mvi);
}