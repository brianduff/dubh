package org.freeciv.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.freeciv.common.CommonConstants;
import org.freeciv.common.Map;
import org.freeciv.common.Tile;
/**
 * The map overview component
 *
 * @author Brian.Duff@dubh.org
 */
final class MapOverview
{
  private MapView m_mapView;
  private int m_width;
  private int m_height;
  private int m_mapWidth;
  private int m_mapHeight;

  private static final int SCALE = 2;

  private JComponent m_overviewComponent;

  MapOverview(MapView mapView)
  {
    m_mapView = mapView;
  }

  JComponent getComponent()
  {
    if (m_overviewComponent == null)
    {
      m_overviewComponent = new OverviewComponent();
    }
    return m_overviewComponent;
  }

  public void setDimensions(int x, int y)
  {
    m_width = x*SCALE;
    m_height = y*SCALE;
    getComponent().setPreferredSize(new Dimension( m_width, m_height ));
    getComponent().setMinimumSize(new Dimension( m_width, m_height ));
    m_mapWidth = x;
    m_mapHeight = y;
  }

  /**
   * Call this to repaint the overview map for a single tile.
   *
   * @param mapx the x-coordinate of the tile that has changed
   * @param mapy the y-coordinate of the tile that has changed
   */
  public void repaint( int mapx, int mapy )
  {
    int x = mapx * SCALE;
    int y = mapy * SCALE;

    m_overviewComponent.repaint(x, y, 2, 2);
  }


  public void refresh()
  {
    getComponent().repaint();
  }

  private class OverviewComponent extends JComponent
  {
    public void paintComponent( Graphics g )
    {

      int upperLeftX = 0;
      int upperLeftY = 0;

      Rectangle r = g.getClipBounds();
      Rectangle bounds = new Rectangle( r );
      

      int align = ( r.x + upperLeftX ) % SCALE;
      r.x -= align;
      r.width += align;
      align = ( r.y + upperLeftY ) % SCALE;
      r.y -= align;
      r.height += align;
      align = ( r.width + r.x + upperLeftX ) % SCALE;
      if( align != 0 )
      {
        r.width += SCALE - align;
      }
      align = ( r.height + r.y + upperLeftY ) % SCALE;
      if( align != 0 )
      {
        r.height += SCALE - align;
      }
      r.width = Math.min( r.width, m_width - ( r.x + upperLeftX ) );
      r.height = Math.min( r.height, m_height - ( r.y + upperLeftY ) );

      bounds.setBounds( r );


      int firstXtile = ( ( upperLeftX + bounds.x ) / SCALE ) % m_mapWidth;
      int firstYtile = ( ( upperLeftY + bounds.y ) / SCALE ) % m_mapHeight;
      int columnsToPaint = bounds.width / SCALE;
      int rowsToPaint = bounds.height / SCALE;

      int currentXtile = firstXtile;
      if( currentXtile >= m_mapWidth )
      {
        currentXtile -= m_mapWidth;
      }
      int xcoord = bounds.x;   

      while( columnsToPaint > 0 )
      {
        int currentRowsToPaint = rowsToPaint;
        int currentYtile = firstYtile;
        if( currentYtile >= m_mapHeight )
        {
          currentYtile = 0;
        }
        int ycoord = bounds.y;
        
        while( currentRowsToPaint > 0 )
        {
          Color c = getTileColor( currentXtile, currentYtile );

          g.setColor( c );
          g.fillRect( xcoord, ycoord, SCALE, SCALE );
          
          currentRowsToPaint--;
          currentYtile++;
          if( currentYtile >= m_mapHeight )
          {
            currentYtile = 0;
          }
          ycoord += SCALE;
        }
        columnsToPaint--;
        currentXtile++;
        if( currentXtile >= m_mapWidth )
        {
          currentXtile = 0;
        }
        xcoord += SCALE;
      }         
    }
  }

  public Color getTileColor( int x, int y )
  {
    // tilespec.c: overview_tile_color(int x, int y)
    Map map = m_mapView.getClient().getGame().getMap();
    Tile t = map.getTile( x, y );

    if ( !t.isKnown() )
    {
      return Color.black;
    }
    else if ( t.hasCity() )
    {
      if (m_mapView.getClient().getGame().isCurrentPlayer(t.getCity().getOwner()))
      {
        return Color.white;
      }
      else
      {
        return Color.cyan;
      }
    }
    else if ( t.hasVisibleUnit() )
    {
      //if (m_mapView.getClient().getGame().isCurrentPlayer(t.getVisibleUnit().getOwner()))
      //{
      //  return Color.yellow;
      //}
      //else
      //{
      //  return Color.red;
      //}
    }
    else if ( t.getTerrain() == CommonConstants.T_OCEAN )
    {
      return Color.blue;
    }

    return Color.green;

  }
}