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
import org.freeciv.common.Tile;
import org.freeciv.common.TerrainType;
/**
 * Display the production being generated from each square surrounding
 * a city in the city view.
 * 
 * @author Ben Mazur
 */
public class CityProductionLayer extends GridMapTileLayer
{

  public CityProductionLayer(GridMapView gmv)
  {
    super( gmv );
  }

  protected boolean isBuffered()
  {
    return false;
  }

  public void paintTile(Graphics g, Point mapPos, Point gPos, MapViewInfo info)
  {
    Tile tile = info.getTile( mapPos );
    if ( tile.getWorkedBy() == getCity() )
    {
      TerrainType tType = tile.getTerrainType();
      
      //TODO: determine the three production values
      
      //TODO: paint them
      
    }
    
  }
}