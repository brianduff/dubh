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
 * The map overview component. There is usually only one MapOverview component
 * on the screen at a time. It provides a miniature version of the map, and 
 * provides eventing so that mouse clicks on the overview map translate to
 * jumps on the associated map view.
 *
 * @author Brian.Duff@dubh.org
 */
public final class MapOverview extends JComponent
{
  private int m_width;
  private int m_height;

  private Client m_client;

  // The radar.xpm image is 160x100. The default size of the minimap is
  // half this.
  private static final int DEFAULT_WIDTH = 80;  // Change to 80
  private static final int DEFAULT_HEIGHT = 50;

  private static final int SCALE = 2;

  MapOverview( Client c )
  {
    m_client = c;
    setMapDimensions( DEFAULT_WIDTH, DEFAULT_HEIGHT );
  }

  private Client getClient()
  {
    return m_client;
  }

  public void setMapDimensions(int x, int y)
  {
    m_width = x*SCALE;
    m_height = y*SCALE;
    setPreferredSize(new Dimension( m_width, m_height ));
    setMinimumSize(new Dimension( m_width, m_height ));
    if ( getParent() != null )
    {
      // Doesn't work...
      getParent().validate();
    }
  }

  /**
   * Call this to repaint the overview map for a single tile.
   *
   * @param mapx the x-coordinate of the tile that has changed
   * @param mapy the y-coordinate of the tile that has changed
   */
  public void refresh(int mapx, int mapy )
  {
    int x = mapx * SCALE;
    int y = mapy * SCALE;

    repaint(x, y, 2, 2);
  }


  public void refresh()
  {
    repaint();
  }

  private boolean isDrawingEnabled()
  {
    return !m_client.getGame().getMap().isEmpty();
  }

  /**
   * Actually paint the map overview component
   */
  public void paintComponent( Graphics g )
  {
    if ( !isDrawingEnabled() )
    {
      g.setColor( Colors.getStandardColor( Colors.COLOR_STD_BLACK ) );
      g.fillRect( g.getClipBounds().x, g.getClipBounds().y, 
        g.getClipBounds().width, g.getClipBounds().height );
    }
    else
    {
      int mapWidth = m_client.getGame().getMap().getWidth();
      int mapHeight = m_client.getGame().getMap().getHeight();
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


      int firstXtile = ( ( upperLeftX + bounds.x ) / SCALE ) % mapWidth;
      int firstYtile = ( ( upperLeftY + bounds.y ) / SCALE ) % mapHeight;
      int columnsToPaint = bounds.width / SCALE;
      int rowsToPaint = bounds.height / SCALE;

      int currentXtile = firstXtile;
      if( currentXtile >= mapWidth )
      {
        currentXtile -= mapWidth;
      }
      int xcoord = bounds.x;   

      while( columnsToPaint > 0 )
      {
        int currentRowsToPaint = rowsToPaint;
        int currentYtile = firstYtile;
        if( currentYtile >= mapHeight )
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
          if( currentYtile >= mapHeight )
          {
            currentYtile = 0;
          }
          ycoord += SCALE;
        }
        columnsToPaint--;
        currentXtile++;
        if( currentXtile >= mapWidth )
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
    Map map = getClient().getGame().getMap();
    Tile t = map.getTile( x, y );

    if ( !t.isKnown() )
    {
      return Color.black;
    }
    else if ( t.hasCity() )
    {
      if (getClient().getGame().isCurrentPlayer(t.getCity().getOwner()))
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
      //if (getClient().getGame().isCurrentPlayer(t.getVisibleUnit().getOwner()))
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