package org.freeciv.client.map.grid;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import org.freeciv.client.Constants;
import org.freeciv.client.map.MapViewInfo;

/**
 * The fog of war. We draw the fog of war using composite alpha blending, which
 * java makes very convenient.
 *
 * @author Brian Duff
 */
public class FogOfWarLayer extends GridMapTileLayer
{
  private final static int GRADIENT_FOG_SIZE = 8;
  private final static float FULL_FOG_TRANSPARENCY = 0.4f;

  /**
   * The fog o'war is black.
   */
  private static final Color FOG_COLOR = new Color( 0.0f, 0.0f, 0.0f, 1.0f );
  
  /**
   * The fog o'war is 30% opaque
   */
  private static final AlphaComposite FOG_COMPOSITE =
    AlphaComposite.getInstance( AlphaComposite.SRC_OVER, FULL_FOG_TRANSPARENCY );

  public FogOfWarLayer(GridMapView mv)
  {
    super( mv );
  }

  public void paintTile(Graphics g, Point mapPos, Point gPos, MapViewInfo info)
  {
    if ( info.getTile( mapPos ).getKnown() == Constants.TILE_KNOWN_FOGGED )
    {

      Graphics2D g2d = (Graphics2D)g;

      Color oldColor = g2d.getColor();
      Composite oldComposite = g2d.getComposite();

      g2d.setColor( FOG_COLOR );
      g2d.setComposite( FOG_COMPOSITE );

      g2d.fillRect( gPos.x, gPos.y, 
        info.getTileSize().width, info.getTileSize().height
      );

      g2d.setColor( oldColor );
      g2d.setComposite( oldComposite );


      
    }


    
  }
}