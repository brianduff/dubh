package org.freeciv.client.map.grid;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import org.freeciv.client.map.MapViewInfo;

/**
 * The grid layer just draws a grid round all tiles on the map.
 *
 * @author Brian Duff
 */
public class GridLayer  extends GridMapTileLayer
{
 
  /** The font that is used to draw the strings */
  private Font textfont = null;
  /** The color that is used to draw the strings and the grid */
  private Color textcolor = Color.white;
  
  /**
   * Default constructor
   */
  public GridLayer( GridMapView mv )
  {
    super( mv );
  }

  /**
   * Paint the grid for a tile
   */
  public void paintTile(Graphics g, Point mapPos, Point gPos, MapViewInfo info)
  {
    Dimension size = info.getTileSize();
    
    Font oldfont = g.getFont();
    if (textfont==null)
      textfont = oldfont.deriveFont(9.0F);
     
    g.setColor(textcolor);
    g.setFont(textfont);
    g.drawRect(gPos.x, gPos.y, size.width, size.height);
    g.drawString(""+mapPos.x+","+mapPos.y, gPos.x+2, gPos.y+size.height/2);
    g.setFont(oldfont);
  }

}
