package org.freeciv.client.map.grid;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import org.freeciv.client.Colors;
import org.freeciv.client.Constants;
import org.freeciv.client.map.MapViewInfo;
import org.freeciv.common.City;
/**
 * Draws the names of cities underneath them
 *
 * @author Brian Duff
 */
public class CityNamesLayer extends GridMapTileLayer
{

  private final static float CITY_NAME_BG_TRANSPARENCY = 0.5f;

  /**
   * The fog o'war is black.
   */
  private static final Color BG_COLOR = new Color( 0.0f, 0.0f, 0.0f, 1.0f );
  
  /**
   * The fog o'war is 30% opaque
   */
  private static final AlphaComposite BG_COMPOSITE =
    AlphaComposite.getInstance( AlphaComposite.SRC_OVER, CITY_NAME_BG_TRANSPARENCY );

  public CityNamesLayer(GridMapView gmv)
  {
    super( gmv );
  }

  protected boolean isBuffered()
  {
    return false;
  }

  public void paintTile(Graphics g, Point mapPos, Point gPos, MapViewInfo info)
  {
    // This is not an ideal way of implementing this functionality, since
    // long city names tend to get chopped off at the edges of the map
    // *probably* the city names should not be in the buffer layer....and
    // that might just solve the problem...
    City c = info.getMap().getCity( mapPos.x, mapPos.y );
    if ( c != null )
    {
      ((Graphics2D)g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, 
      RenderingHints.VALUE_ANTIALIAS_ON );

      // Fix me: font must be changed...
      g.setFont( new Font( "Century Gothic", Font.BOLD, 9 ) );
      int cityNameWidth = g.getFontMetrics().stringWidth( c.getName() );

      String citySize = String.valueOf( c.getSize() );
      int sizeWidth = g.getFontMetrics().stringWidth( citySize );

      int boxx = gPos.x + ( info.getTileSize().width / 2 ) - (cityNameWidth + sizeWidth + 6) / 2 - 4;

      int y = gPos.y + info.getTileSize().height;

      y += g.getFontMetrics().getHeight();
      Composite oldComp = ((Graphics2D)g).getComposite();
      ((Graphics2D)g).setComposite( BG_COMPOSITE );
      g.setColor( BG_COLOR );
      g.fillRect( boxx, gPos.y + info.getTileSize().height + 1, cityNameWidth + sizeWidth + 6, g.getFontMetrics().getHeight() + 2 );
      ((Graphics2D)g).setComposite( oldComp );

      g.setColor( Colors.getPlayerColor( c.getOwner() ));
      g.fillRect( boxx, gPos.y + info.getTileSize().height + 1, sizeWidth + 2, g.getFontMetrics().getHeight() + 2 );

      g.setColor( Color.white );
      g.drawString( citySize, boxx + 2, y );
      
      g.setColor( BG_COLOR);
      g.drawRect( boxx, gPos.y + info.getTileSize().height + 1, cityNameWidth + sizeWidth + 6, g.getFontMetrics().getHeight() + 2 );

      g.setColor( Color.white );
      g.drawString( c.getName(), boxx + sizeWidth + 6, y );
    }
  }
}