package org.freeciv.client.map;

import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import org.freeciv.client.Constants;
import org.freeciv.client.Localize;

/**
 * The actual map component. This component simply provides the required 
 * interface for a scrollable JComponent and delgates all painting to a set
 * of MapLayers.
 *
 * @author Brian Duff
 * @author Luke Lindsay
 */
class MapComponent extends JComponent implements Scrollable
{

  private Collection m_layers;
  private MapViewInfo m_mvi;

  private int m_tileWidth;
  private int m_tileHeight;

  private MapLayer m_singleLayer = null;

  /**
   * Construct the map component.
   *
   * @param layers a collection of MapLayers for painting the map
   * @param tileWidth the width of a tile
   * @param tileHeight the height of a tile.
   */
  MapComponent( Collection layers, MapViewInfo mvi )
  {
    m_layers = layers;
    m_mvi = mvi;

    m_tileWidth = mvi.getTileSize().width;
    m_tileHeight = mvi.getTileSize().height;
  }

  /**
   * Request a repaint of only one layer
   */
  public void repaintOneLayer( MapLayer l )
  {
    m_singleLayer = l;
    repaint();
    m_singleLayer = null;
  }
  
  /**
   * Paint the map component. We simply request that all layers paint
   * themselves.
   */
  public void paint( Graphics g )
  {
    Graphics2D g2 = (Graphics2D)g;
    Rectangle r = getVisibleRect();
    //Rectangle r = g.getClipRect();

    if ( m_singleLayer != null )
    {
      m_singleLayer.paint( g2, r, m_mvi );
    }
    
    Iterator layerIter = m_layers.iterator();

    while ( layerIter.hasNext() )
    {
      ((MapLayer)layerIter.next()).paint( g2, r, m_mvi );
    }
  }

  public boolean getScrollableTracksViewportHeight()
  {
    return false;
  }

  public boolean getScrollableTracksViewportWidth()
  {
    return false;
  }

  public int getScrollableBlockIncrement( Rectangle rect, 
    int orientation, int direction ) 
  {
    if ( SwingConstants.VERTICAL == orientation )
    {
      int best = (( rect.height / m_tileHeight - 2 ) * m_tileHeight);
      if ( best > 0 )
      {
        return best;
      }
      return rect.height;
    }
    else
    {
      int best = (( rect.width / m_tileWidth - 2 ) *  m_tileWidth);
      if ( best > 0 )
      {
        return best;
      }
      return rect.width;
    }
  }

  public Dimension getPreferredScrollableViewportSize()
  {
    return getPreferredSize();
  }


  public int getScrollableUnitIncrement( Rectangle rectangle,
    int orientation, int direction )
  {
    if ( SwingConstants.VERTICAL == orientation )
    {
      return m_tileHeight;
    }
    else
    {
      return m_tileWidth;
    }      
  }
}  
