package org.freeciv.client.map;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Iterator;

/**
 * A buffer layer is a layer which uses a back buffer to store the visible
 * part of the map. A BufferLayer is made up of a series of MapLayers which
 * are painted into the back buffer rather than directly painted when paint()
 * is called. This is useful when the paint() method has to do a lot of work, 
 * but not ideal for layers which change often (e.g. on a timer)
 *
 * When paint() is called on the bufferlayer, it just blits the background
 * buffer to the graphics context.
 *
 * @author Brian Duff
 * @author Like Lindsay
 */
public class BufferLayer implements MapLayer 
{
  private Collection m_layers;

  /**
   * This is used to create images that are compatible with the default
   * graphics configuration. Such images can be drawn to the screen quickly
   * because no conversion is required.
   */
  protected GraphicsConfiguration m_defaultConfiguration =
    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
      getDefaultConfiguration();

  /**
   * The backbuffer graphics context
   */
  private Graphics2D m_bufferGraphics;

  /**
   * The bounds and location of the map region ( in pixels ) that is stored
   * in the offscreen backbuffer
   */
  private Rectangle m_bufferRect = new Rectangle();

  /**
   * An offscreen image storing a region of the map
   */
  private Image m_backgroundBuffer;


  /**
   * Construct a buffer layer with the specified "sub" layers
   *
   * @param layers a collection of MapLayer implementations. The back buffer
   *    is painted using the layers in the collection
   */
  public BufferLayer( Collection layers )
  {
    m_layers = layers;
  }

  /**
   * Blits the background buffer to the graphics context.
   */
  public void paint(Graphics outGraphics, Rectangle rect, MapViewInfo mvi) 
  {
    Rectangle visibleRect = mvi.getVisibleRectangle();

    // If this is the first call, or the component has been resized, create
    // a new backbuffer
    if (m_backgroundBuffer == null ||
            visibleRect.height != m_bufferRect.height ||
            visibleRect.width != m_bufferRect.width) {
        setBackgroundBufferSize(visibleRect.width, visibleRect.height);
        paintWholeBuffer( mvi );
    }

    // Has the visible rectangle moved since the last paint?
    if (m_bufferRect.x != visibleRect.x || m_bufferRect.y != visibleRect.y) {
        int dx = m_bufferRect.x - visibleRect.x;
        int dy = m_bufferRect.y - visibleRect.y;
        scrollbackgroundBuffer(dx, dy, mvi );

    }
    outGraphics.drawImage(m_backgroundBuffer, visibleRect.x, visibleRect.y,
            null
    );
  }  

  /**
   * Set the background buffer to a new buffer of the specified size. After
   * calling this method, the buffer is completely empty.
   *
   * @param w the width of the back buffer (in pixels)
   * @param h the height of the back buffer (in pixels)
   */
  private void setBackgroundBufferSize(int w, int h) {
      m_backgroundBuffer = m_defaultConfiguration.createCompatibleImage(w, h);
      m_bufferRect.height = m_backgroundBuffer.getHeight(null);
      m_bufferRect.width = m_backgroundBuffer.getWidth(null);
      m_bufferGraphics = (Graphics2D) m_backgroundBuffer.getGraphics();
      m_bufferGraphics.clearRect(0, 0, w, h);
  }  

  /**
   * Repaint all layers into the background buffer.
   */
  private void paintWholeBuffer( MapViewInfo mvi ) 
  {
    paintBuffer( 0, 0, m_bufferRect.width, m_bufferRect.height, mvi );
  }

  /**
   * Repaint the specified region into the background buffer. You can use this
   * to update the background buffer without actually repainting the screen.
   * To actually repaint the screen, you need to call paint().
   *
   * @param r the rectangular region to paint
   */
  public void paintBuffer( int x, int y, int w, int h, MapViewInfo mvi )
  {
    Iterator i = m_layers.iterator();
    Rectangle screenRect = new Rectangle( x , y ,  w, h );
    while ( i.hasNext() )
    {
      ((MapLayer)i.next()).paint( m_bufferGraphics, screenRect, mvi );
    }    
  }


  /**
   * Scroll the contents of the background buffer by the specified 
   * distance. The existing contents of the buffer are blitted to a new
   * location, and the remaining area is repainted using the layers.
   *
   * @param dx the distance to scroll the background buffer horizontally
   * @param dy the distance to scroll the background buffer vertically.
   */
  private void scrollbackgroundBuffer(int dx, int dy, MapViewInfo mvi) {
    int copyWidth = m_bufferRect.width;
    int copyHeight = m_bufferRect.height;
    int copySourceX = 0;
    int copySourceY = 0;
    if (dx > 0) {
        copyWidth -= dx;
    } else {
        copyWidth += dx;
        copySourceX = -dx;
    }
    if (dy > 0) {
        copyHeight -= dy;
    } else {
        copyHeight += dy;
        copySourceY = -dy;
    }
    m_bufferGraphics.copyArea(copySourceX, copySourceY, copyWidth, copyHeight, dx, dy);
    m_bufferRect.x -= dx;
    m_bufferRect.y -= dy;

    // paint exposed areas
    if (dx > 0) {

        clipClearAndPaint(0, 0, dx, m_bufferRect.height, mvi);
    } else {
        if (dx < 0) {

            clipClearAndPaint(m_bufferRect.width + dx, 0, -dx, m_bufferRect.height, mvi);
        }
    }
    if (dy > 0) {

        clipClearAndPaint(0, 0, m_bufferRect.width, dy, mvi);
    } else {
        if (dy < 0) {

            clipClearAndPaint(0, m_bufferRect.height + dy, m_bufferRect.width, -dy, mvi);
        }
    }
    m_bufferGraphics.setClip(0, 0, m_bufferRect.width, m_bufferRect.height);
  }  

  /**
   * Utility method. Clips, clears and paints the specified region of the
   * back buffer.
   */
  private void clipClearAndPaint(int x, int y, int w, int h, MapViewInfo mvi) 
  {
    m_bufferGraphics.setClip( x, y, w, h );
    m_bufferGraphics.clearRect( x, y, w, h );
    paintBuffer( x, y, w, h, mvi );
  }  
}