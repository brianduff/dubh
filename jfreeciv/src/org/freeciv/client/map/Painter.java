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
interface Painter {
    /**
     * Updates the cached icon of the tile at specified map location if it is
     * cached.  Use to prevent the offscreen buffer becoming out of sync with the map.
     * Note, Painter.paint(..) must
     * be called before the result of the update is seen.
     *
     * @param mapCoord the map (tile) co-ordinates of the tile to update
     */
    public void updateTile(Point mapCoord);

    /**
     * Paint the offscreen buffer into the output graphics context.
     *
     * @param outGraphics the output graphics context
     * @param visibleRect the current visible rectangle.
     */
    public void paint(Graphics outGraphics, Rectangle visibleRect);
}