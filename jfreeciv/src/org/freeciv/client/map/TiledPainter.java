package org.freeciv.client.map;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * A tiled painter implements the Painter interface for a tile-based map
 *
 * @author Brian Duff
 * @author Luke Lindsay
 */
abstract class TiledPainter implements Painter
{
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

  protected final Rectangle getBufferRect()
  {
    return m_bufferRect;
  }

  protected final Graphics2D getBufferGraphics()
  {
    return m_bufferGraphics;
  }


  public void paintBackground( Graphics g, int x, int y, int width, int height )
  {
    Rectangle r = g.getClipBounds();

    // If the clip region is not the same size as the buffer rectangle
    if ( m_bufferRect.width != r.width || m_bufferRect.height != r.height )
    {
      paintBufferRectangle( r.x, r.y, r.width, r.height );      
    }
    
    g.drawImage( m_backgroundBuffer, 
      x, y, x + width, y + height,
      x, y, x + width, y + height, 
      null
    );

  }

  public void paint( Graphics outGraphics, Rectangle visibleRect )
  {
    boolean paintBufferRect = false;
    // If this is the first call, or the component has been resized, create
    // a new backbuffer
    if (  m_backgroundBuffer == null || 
          visibleRect.height != m_bufferRect.height ||
          visibleRect.width != m_bufferRect.width )
    {
      setBackgroundBufferSize( visibleRect.width, visibleRect.height );
      paintBufferRect = true;
    }

    // Has the visible rectangle moved since the last paint?
    if ( m_bufferRect.x != visibleRect.x || m_bufferRect.y != visibleRect.y )
    {
      int dx = m_bufferRect.x - visibleRect.x;
      int dy = m_bufferRect.y - visibleRect.y;

      scrollBackgroundBuffer( dx, dy );
      m_bufferRect.setBounds( visibleRect );
    }

    if ( paintBufferRect )
    {
      paintBufferRectangle(
        visibleRect.x - m_bufferRect.x, visibleRect.y - m_bufferRect.y,
        visibleRect.width, visibleRect.height
      );
    }

    outGraphics.drawImage( m_backgroundBuffer, visibleRect.x, visibleRect.y, 
      null
    );

    m_bufferRect.setBounds( visibleRect );
    
  }

  public void refreshBackground()
  {
    paintBufferRectangle( 0, 0, m_bufferRect.width, m_bufferRect.height );
  }

  public void setBackgroundBufferSize( int w, int h )
  {
    m_backgroundBuffer = m_defaultConfiguration.createCompatibleImage( w, h );
    m_bufferRect.height = m_backgroundBuffer.getHeight( null );
    m_bufferRect.width = m_backgroundBuffer.getWidth( null );
    m_bufferGraphics = (Graphics2D) m_backgroundBuffer.getGraphics();
    m_bufferGraphics.clearRect( 0, 0, w, h );
    refreshBackground();
  }
  
  public abstract void paintBufferRectangle( int x, int y, int width, int height);
  public abstract void updateTile( Point mapCoord );

  private void scrollBackgroundBuffer( int dx, int dy )
  {
    int copyWidth = m_bufferRect.width;
    int copyHeight = m_bufferRect.height;
    int copySourceX = 0;
    int copySourceY = 0;

    if ( dx > 0 )
    {
      copyWidth -= dx;
    }
    else
    {
      copyWidth += dx;
      copySourceX = -dy;
    }

    if ( dy > 0 )
    {
      copyHeight -= dy;
    }
    else
    {
      copyHeight += dy;
      copySourceY = -dy;
    }

    m_bufferGraphics.copyArea( 
      copySourceX, copySourceY, copyWidth, copyHeight, dx, dy 
    );
    m_bufferRect.x -= dx;
    m_bufferRect.y -= dy;

    if ( dx > 0 )
    {
      clipClearAndPaint( m_bufferGraphics, 0, 0, dx, m_bufferRect.height );
    }
    else if ( dx < 0 )
    {
      clipClearAndPaint( m_bufferGraphics, 
        m_bufferRect.width + dx, 0, -dx, m_bufferRect.height
      );
    }

    if ( dy > 0 )
    {
      clipClearAndPaint( m_bufferGraphics, 0, 0, m_bufferRect.width, dy );
    }
    else if ( dy < 0 )
    {
      clipClearAndPaint( m_bufferGraphics, 
        0, m_bufferRect.height + dy, m_bufferRect.width, -dy 
      );
    }

    m_bufferGraphics.setClip( 0, 0, m_bufferRect.width, m_bufferRect.height );
  }

  /**
   * Utility method
   */
  private void clipClearAndPaint( 
    Graphics g, int x, int y, int w, int h)
  {
    g.setClip( x, y, w, h );
    g.clearRect( x, y, w, h );
    paintBufferRectangle( x, y, w, h );
  }
  
  
  

}