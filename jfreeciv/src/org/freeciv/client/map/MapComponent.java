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

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import org.freeciv.client.Constants;
import org.freeciv.client.Localize;

/**
 * The actual map component. This component simply provides the required 
 * interface for a scrollable JComponent and delgates all painting to a 
 * supplied Painter object.
 *
 * @author Brian Duff
 * @author Luke Lindsay
 */
class MapComponent extends JComponent implements Scrollable
{
  private AbstractMapView m_mapView;
  private Painter m_painter;

  MapComponent( Painter painter, AbstractMapView view )
  {
    m_mapView = view;
    m_painter = painter;
  }

  /**
   * update the tile at the specified grid co-ordinates
   */
  void updateTileAt( int tilex, int tiley, int width, int height,
    boolean repaint )
  {
    Point p = new Point();

    for ( int i=tilex; i < tilex + width; i++ )
    {
      for ( int j=tiley; j < tiley + height; j++ )
      {
        p.x = i;
        p.y = j;

        m_painter.updateTile( p );
      }
    }

    if ( repaint )
    {
      // Make this more efficient by only repainting the affected region
      repaint();
      
    }
  }

  public void paint( Graphics g )
  {
    Graphics2D g2 = (Graphics2D)g;
    Rectangle r = getVisibleRect();
    m_painter.paint( g2, r );
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
      int best = (( rect.height / m_mapView.getNormalTileHeight() - 2 ) * 
        m_mapView.getNormalTileHeight());
      if ( best > 0 )
      {
        return best;
      }
      return rect.height;
    }
    else
    {
      int best = (( rect.width / m_mapView.getNormalTileWidth() - 2 ) * 
        m_mapView.getNormalTileWidth());
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
      return m_mapView.getNormalTileHeight();
    }
    else
    {
      return m_mapView.getNormalTileWidth();
    }      
  }
}  
