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

  private static final int PAD = 2;

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
    City c = info.getMap().getCity( mapPos.x, mapPos.y );
    if ( c != null && info.getOptions().drawCityNames )
    {
      ((Graphics2D)g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, 
      RenderingHints.VALUE_ANTIALIAS_ON );

      // Fix me: font must be changed...
      g.setFont( new Font( "Century Gothic", Font.BOLD, 9 ) );
      int cityNameWidth = g.getFontMetrics().stringWidth( c.getName() );

      String citySize = String.valueOf( c.getSize() );
      int sizeWidth = g.getFontMetrics().stringWidth( citySize );

      String buildString = "";
      int buildWidth = 0;
      if ( c.getOwner() == info.getCurrentPlayer() && info.getOptions().drawCityProductions )
      {
        buildString = c.getCurrentlyBuildingDescription() + " : " + c.getTurnsToBuild();
        buildWidth = g.getFontMetrics().stringWidth( buildString );
      }

      int fontHeight = g.getFontMetrics().getHeight();
      int longestString = Math.max( buildWidth, cityNameWidth );
      int boxW = longestString + sizeWidth + 4*PAD;
      int boxX = gPos.x + (( info.getTileSize().width - boxW )/ 2);
      int boxY = gPos.y + info.getTileSize().height + PAD;
      int boxH = fontHeight * (buildWidth == 0 ? 1 : 2) + (buildWidth==0 ? 2 : 4)*PAD;

      // Draw the alpha composite (translucent) background
      Graphics2D g2d = (Graphics2D)g;
      Composite oldComp = g2d.getComposite();
      g2d.setComposite( BG_COMPOSITE );
      g.setColor( BG_COLOR );
      g.fillRect( boxX, boxY, boxW, boxH );
      g2d.setComposite( oldComp );

      // Draw a line through the center of the box
      g.setColor( Colors.getPlayerColor( c.getOwner() ) );
      if ( buildWidth > 0 )
      {
        g.drawLine( boxX, boxY + fontHeight + 3*PAD, boxX + boxW, boxY + fontHeight + 3*PAD );
      }
      
      // Draw the city size in a colored box
      g.fillRect( boxX, boxY, sizeWidth + (2*PAD), boxH );
      g.setColor( Color.white );
      g.drawString( citySize, boxX + PAD, boxY + fontHeight );  // prob lower

      // Draw the city name
      int cnameX = boxX + PAD*3 + sizeWidth + ( longestString /2 ) - (cityNameWidth / 2 );
      g.drawString( c.getName(), cnameX, boxY + fontHeight );

      // Draw the city production
      if ( buildWidth > 0 )
      {
        int prodX = boxX + PAD*3 + sizeWidth + ( longestString /2 ) - (buildWidth / 2 );
        g.drawString( buildString, prodX, boxY + PAD*3 + fontHeight*2 );
      }

      // Finally, draw a nice black outline.
      g.setColor( BG_COLOR );
      g.drawRect( boxX, boxY, boxW, boxH );
    }
  }
}