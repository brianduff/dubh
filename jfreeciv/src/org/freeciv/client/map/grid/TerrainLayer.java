package org.freeciv.client.map.grid;

import java.awt.Graphics;
import java.awt.Point;
import javax.swing.Icon;

import org.freeciv.client.Colors;
import org.freeciv.client.Constants;
import org.freeciv.client.map.MapViewInfo;
import org.freeciv.common.City;
import org.freeciv.common.Logger;
import org.freeciv.common.Map;
import org.freeciv.common.TerrainType;
import org.freeciv.common.Tile;
import org.freeciv.common.Unit;

/**
 * This class is responsible for painting the terrian in a grid based map
 *
 * @author Brian Duff
 */
public final class TerrainLayer extends GridMapTileLayer implements Constants
{

  //
  // These behave like boolean values, e.g. NESW_STRINGS[5] = n0e1s0w1
  // (5d = 0101b)
  //
  private final static String[] NSEW_STRINGS =
  {
    "n0s0e0w0", "n0s0e0w1", "n0s0e1w0", "n0s0e1w1", "n0s1e0w0",
    "n0s1e0w1", "n0s1e1w0", "n0s1e1w1", "n1s0e0w0", "n1s0e0w1",
    "n1s0e1w0", "n1s0e1w1", "n1s1e0w0", "n1s1e0w1", "n1s1e1w0",
    "n1s1e1w1"
  };

  private static final int DIR_NORTH = 8;
  private static final int DIR_EAST = 2;
  private static final int DIR_SOUTH = 4;
  private static final int DIR_WEST = 1;

  TerrainLayer( GridMapView gmv )
  {
    super( gmv );
  }

  /**
   * Get a number between 0 and 15 which represents the variation of
   * terrain based on squares around it.
   *
   * @param north whether the tile to the north matches the current tile
   * @param east  whether the tile to the east matches the current tile
   * @param south whether the tile to the south matches the current tile
   * @param west  whether the tile to the west matches the current tile
   * @return a value between 0-15 which encodes the four conditions
   *   in binary
   */
  private int getVariationID( 
    boolean north, boolean south, boolean east, boolean west )
  {
    // INDEX_NSEW in tilespec.h
    return ( north ? DIR_NORTH : 0 ) + ( south ? DIR_SOUTH : 0 ) + 
           ( east ? DIR_EAST : 0 ) + ( west ? DIR_WEST : 0 );
  }

  private void paintIcon( MapViewInfo info, String icon, Graphics g, Point gPos )
  {
    Icon i = info.getImage( icon );
    if ( i == null )
    {
      Logger.log( Logger.LOG_ERROR, "Missing Icon "+icon );
    }
    else
    {
      i.paintIcon( null, g, gPos.x, gPos.y );
    }
  }


  public void paintTile(Graphics g, Point mapPos, Point gPos, MapViewInfo info)
  {
    // TODO: Split this up into smaller methods ( :) )
    // fill_tile_sprite_array in tilespec.c

    Map map = info.getMap();

    int ttype, ttype_north, ttype_south, ttype_east, ttype_west;
    int ttype_north_east, ttype_south_east, ttype_south_west, ttype_north_west;
    int tspecial, tspecial_north, tspecial_south, tspecial_east, tspecial_west;
    int tspecial_north_east, tspecial_south_east, tspecial_south_west, tspecial_north_west;
    int rail_card_tileno=0, rail_semi_tileno=0, road_card_tileno=0, road_semi_tileno=0;
    int rail_card_count=0, rail_semi_count=0, road_card_count=0, road_semi_count=0;

    int tileno;
    Tile tile;
    Icon mySprite;
    City city;
    Unit focus;
    Unit unit;
    int den_y = map.getHeight() * 24;




    int abs_x0 = mapPos.x;
    int abs_y0 = mapPos.y;

    tile = info.getTileAtMapPos( mapPos );

    if ( tile.getKnown() == TILE_UNKNOWN )
    {
      g.setColor( Colors.getStandardColor( Colors.COLOR_STD_BLACK ) );
      g.fillRect( gPos.x, gPos.y, info.getTileSize().width,
        info.getTileSize().height );
      return;
    }

    city = map.getCity( abs_x0, abs_y0 );
    focus = info.getUnitInFocus();

    
    if ( !info.areFlagsTransparent() || info.getOptions().solidColorBehindUnits )
    {
      // Unit stuff handled in the unit layer...
      /*
      unit = getDrawableUnit( abs_x0, abs_y0, cityMode );

      if ( unit != null &&
          ( getClient().getOptions().drawUnits ||
            (getClient().getOptions().drawFocusUnit && focus == unit ) ))
      {
        fillUnitSprites( l, unit, solidBg );

        player[0] = unit.getOwner();
        if ( tile.hasUnitStack() )
        {
          l.add( getImage( "unit.stack" ) );
        }

        return l;
      }
      */

      if ( city != null && info.getOptions().drawCities )
      {
        /*
         todo
        fillCitySprites( l, city, solidBg );
        player[0] = city.getOwner();
        return l;
        */
      }
    }

    ttype = map.getTile( abs_x0, abs_y0 ).getTerrain();
    ttype_east = map.getTile( abs_x0+1, abs_y0 ).getTerrain();
    ttype_west = map.getTile( abs_x0-1, abs_y0 ).getTerrain();

    if ( abs_y0 == 0 )
    {
      ttype_north = ttype;
      ttype_north_east = ttype_east;
      ttype_north_west = ttype_west;
    }
    else
    {
      ttype_north = map.getTile( abs_x0, abs_y0-1 ).getTerrain();
      ttype_north_east = map.getTile( abs_x0+1, abs_y0-1).getTerrain();
      ttype_north_west = map.getTile( abs_x0-1, abs_y0-1).getTerrain();
    }

    if ( abs_y0 == map.getHeight()-1)
    {
      ttype_south = ttype;
      ttype_south_east = ttype_east;
      ttype_south_west = ttype_west;
    }
    else
    {
      ttype_south = map.getTile( abs_x0, abs_y0+1).getTerrain();
      ttype_south_east = map.getTile( abs_x0+1, abs_y0+1).getTerrain();
      ttype_south_west = map.getTile( abs_x0-1, abs_y0+1).getTerrain();
    }

    tspecial = map.getSpecial( abs_x0, abs_y0 );
    tspecial_north = map.getSpecial( abs_x0, abs_y0-1 );
    tspecial_east = map.getSpecial( abs_x0+1, abs_y0 );
    tspecial_south = map.getSpecial( abs_x0, abs_y0+1);
    tspecial_west = map.getSpecial( abs_x0-1, abs_y0 );
    tspecial_north_east = map.getSpecial( abs_x0 + 1, abs_y0 - 1 );
    tspecial_south_east = map.getSpecial( abs_x0 + 1, abs_y0 + 1 );
    tspecial_south_west = map.getSpecial( abs_x0 - 1, abs_y0 + 1 );
    tspecial_north_west = map.getSpecial( abs_x0 - 1, abs_y0 - 1 );

    // Evil hack for denmark. Sheesh.
    if ( map.isEarth() &&
        abs_x0 >= 34 && abs_x0 <= 36 && abs_y0 >= den_y && abs_y0 <= den_y+1 )
    {
      mySprite = info.getImage( "tx.denmark_" + (abs_y0 - den_y) + (abs_x0 - 34 ) );
    }
    else
    {
      tileno = getVariationID(
        (ttype_north == ttype), (ttype_south == ttype),
        (ttype_east == ttype), (ttype_west == ttype)
      );

      if ( ttype == T_RIVER )
      {
        tileno |= getVariationID(
          (ttype_north == T_OCEAN), (ttype_south == T_OCEAN),
          (ttype_east == T_OCEAN), (ttype_west == T_OCEAN)
        );
      }

      TerrainType tt = info.getTerrainType( ttype );

      mySprite = tt.getSprite( tileno );
    }

    if ( info.getOptions().drawTerrain )
    { 
      mySprite.paintIcon( null, g, gPos.x, gPos.y );
    }


    if ( ttype == T_OCEAN && info.getOptions().drawTerrain )
    {
      tileno = getVariationID(
        (ttype_north == T_OCEAN && ttype_east == T_OCEAN && ttype_north_east != T_OCEAN),
        (ttype_south == T_OCEAN && ttype_west == T_OCEAN && ttype_south_west != T_OCEAN),
        (ttype_east == T_OCEAN && ttype_south == T_OCEAN && ttype_south_east != T_OCEAN),
        (ttype_north == T_OCEAN && ttype_west == T_OCEAN && ttype_north_west != T_OCEAN)
      );

      if ( tileno != 0 )
      {
        info.getImage( "tx.coast_cape_"+NSEW_STRINGS[ tileno ] ).paintIcon(
          null, g, gPos.x, gPos.y);
      }

      if ( (tspecial_north & S_RIVER) != 0 || ttype_north == T_RIVER )
      {
        paintIcon( info, "tx.river_outlet_n" , g, gPos );
      }
      if ( (tspecial_west&S_RIVER) != 0 || ttype_west == T_RIVER )
      {
        paintIcon( info, "tx.river_outlet_w" , g, gPos );
      }
      if ( (tspecial_south&S_RIVER) != 0 || ttype_south == T_RIVER )
      {
        paintIcon ( info, "tx.river_outlet_s" , g, gPos );
      }
      if ( (tspecial_east&S_RIVER) != 0 || ttype_east == T_RIVER )
      {
        paintIcon( info, "tx.river_outlet_e" , g, gPos );
      }


    }

    if ( (tspecial&S_RIVER) != 0 && info.getOptions().drawTerrain )
    {
      tileno = getVariationID(
        ((tspecial_north&S_RIVER) != 0 || ttype_north == T_OCEAN),
        ((tspecial_south&S_RIVER) != 0 || ttype_south == T_OCEAN),
        ((tspecial_east&S_RIVER) != 0 || ttype_east == T_OCEAN),
        ((tspecial_west&S_RIVER) != 0 || ttype_west == T_OCEAN)
      );

      info.getImage( "tx.s_river_"+NSEW_STRINGS[ tileno ] ).paintIcon(
        null, g, gPos.x, gPos.y
      );
    }

    if ( (tspecial& S_IRRIGATION) != 0 && info.getOptions().drawIrrigation )
    {
      if ( (tspecial&S_FARMLAND) != 0 )
      {
        info.getImage( "tx.farmland" ).paintIcon( null, g, gPos.x, gPos.y );
      }
      else
      {
        info.getImage( "tx.irrigation" ).paintIcon( null, g, gPos.x, gPos.y );
      }
    }

    if ((( (tspecial&S_ROAD) != 0 ) || ( (tspecial&S_RAILROAD) != 0 )) && info.getOptions().drawRoadsRails )
    {
      boolean n, s, e, w;

      n = ((tspecial_north&S_RAILROAD) != 0);
      s = ((tspecial_south&S_RAILROAD) != 0);
      e = ((tspecial_east&S_RAILROAD) != 0);
      w = ((tspecial_west&S_RAILROAD) != 0);

      rail_card_count = (n ? 1 : 0) + (s ? 1 : 0) + (e ? 1 : 0) + (w ? 1 : 0);
      rail_card_tileno = getVariationID( n, s, e, w );

      n = ((tspecial_north&S_ROAD) != 0);
      s = ((tspecial_south&S_ROAD) != 0);
      e = ((tspecial_east&S_ROAD) != 0);
      w = ((tspecial_west&S_ROAD) != 0);

      road_card_count = (n?1:0) + (s?1:0) + (e?1:0) + (w?1:0);
      road_card_tileno = getVariationID( n, s, e, w );


      n = ((tspecial_north_east&S_RAILROAD) != 0);
      s = ((tspecial_south_west&S_RAILROAD) != 0);
      e = ((tspecial_south_east&S_RAILROAD) != 0);
      w = ((tspecial_north_west&S_RAILROAD) != 0);

      rail_semi_count = (n?1:0) + (s?1:0) + (e?1:0) + (w?1:0);
      rail_semi_tileno = getVariationID( n, s, e, w );

      n = ((tspecial_north_east&S_ROAD) != 0);
      s = ((tspecial_south_west&S_ROAD) != 0);
      e = ((tspecial_south_east&S_ROAD) != 0);
      w = ((tspecial_north_west&S_ROAD) != 0);

      road_semi_count = (n?1:0) + (s?1:0) + (e?1:0) + (w?1:0);
      road_semi_tileno = getVariationID( n, s, e, w );


      if ( (tspecial&S_RAILROAD) != 0 )
      {
        road_card_tileno &= ~rail_card_tileno;
        road_semi_tileno &= ~rail_semi_tileno;
      }
      else if ( (tspecial&S_ROAD) != 0 )
      {
        rail_card_tileno &= ~road_card_tileno;
        rail_semi_tileno &= ~road_semi_tileno;
      }

      if ( road_semi_count > road_card_count )
      {
        if ( road_card_tileno != 0 )
        {
          info.getImage( "r.c_road_"+NSEW_STRINGS[ road_card_tileno ] ).paintIcon(
            null, g, gPos.x, gPos.y);
        }
        if ( road_semi_tileno!= 0 && info.getOptions().drawDiagonalRoads )
        {
          info.getImage( "r.d_road_"+NSEW_STRINGS[ road_semi_tileno ] ).paintIcon(
            null, g, gPos.x, gPos.y
          );
        }
      }
      else
      {
        if ( road_semi_tileno != 0 && info.getOptions().drawDiagonalRoads )
        {
          info.getImage( "r.d_road_"+NSEW_STRINGS[ road_semi_tileno ] ).paintIcon(
            null, g, gPos.x, gPos.y
          );
        }
        if ( road_card_tileno != 0 )
        {
          info.getImage( "r.c_road_"+NSEW_STRINGS[ road_card_tileno ] ).paintIcon(
            null, g, gPos.x, gPos.y
          );
        }
      }

      if ( rail_semi_count > rail_card_count )
      {
        if ( rail_card_tileno != 0 )
        {
          info.getImage( "r.c_rail_"+NSEW_STRINGS[ rail_card_tileno ] ).paintIcon(
            null, g, gPos.x, gPos.y
          );
        }
        if ( rail_semi_tileno!= 0 && info.getOptions().drawDiagonalRoads )
        {
          info.getImage( "r.d_rail_"+NSEW_STRINGS[ rail_semi_tileno ] ).paintIcon(
            null, g, gPos.x, gPos.y
          );
        }
      }
      else
      {
        if ( rail_semi_tileno != 0 && info.getOptions().drawDiagonalRoads )
        {
          info.getImage( "r.d_rail_"+NSEW_STRINGS[ rail_semi_tileno ] ).paintIcon(
            null, g, gPos.x, gPos.y
          );
        }
        if ( rail_card_tileno != 0 )
        {
          info.getImage( "r.c_rail_"+NSEW_STRINGS[ rail_card_tileno ] ).paintIcon(
            null, g, gPos.x, gPos.y
          );
        }
      }
    }

    if ( info.getOptions().drawSpecials )
    {
      TerrainType tt = info.getTerrainType( ttype );    
      if ( (tspecial&S_SPECIAL_1) != 0 )
      {
        tt.getSpecialSprite( 0 ).paintIcon( null, g, gPos.x, gPos.y );
      }
      else if ( (tspecial&S_SPECIAL_2) != 0 )
      {
        tt.getSpecialSprite( 1 ).paintIcon( null, g, gPos.x, gPos.y );
      }
    }

    if ( ( (tspecial&S_MINE) != 0) && info.getOptions().drawMines )
    {
      if ( ttype == T_HILLS || ttype == T_MOUNTAINS )
      {
        info.getImage( "tx.mine" ).paintIcon( null, g, gPos.x, gPos.y );
      }
      else
      {
        info.getImage( "tx.oil_mine" ).paintIcon( null, g, gPos.x, gPos.y );
      }
    }

    if ( info.getOptions().drawRoadsRails )
    {
      if ( (tspecial&S_RAILROAD) != 0 )
      {
        int adjacent = rail_card_tileno;
        if ( info.getOptions().drawDiagonalRoads )
        {
          adjacent |= rail_semi_tileno;
        }
        if ( adjacent == 0 )
        {
          info.getImage("r.rail_isolated").paintIcon( null, g, gPos.x, gPos.y );
        }
      }
      else if ( (tspecial&S_ROAD) != 0 )
      {
        int adjacent = road_card_tileno;
        if ( info.getOptions().drawDiagonalRoads )
        {
          adjacent |= road_semi_tileno;
        }
        if ( adjacent == 0 )
        {
          info.getImage("r.road_isolated").paintIcon( null, g, gPos.x, gPos.y );
        }
      }
    }

    if (( (tspecial&S_HUT) != 0) && info.getOptions().drawSpecials )
    {
      info.getImage( "tx.village" ).paintIcon( null, g, gPos.x, gPos.y );
    }
    if (( (tspecial&S_FORTRESS) != 0) && info.getOptions().drawFortressAirbase )
    {
      info.getImage( "tx.fortress" ).paintIcon( null, g, gPos.x, gPos.y );
    }
    if (( (tspecial&S_AIRBASE) != 0) && info.getOptions().drawFortressAirbase )
    {
      info.getImage( "tx.airbase" ).paintIcon( null, g, gPos.x, gPos.y );
    }
    if (( (tspecial&S_POLLUTION) != 0) && info.getOptions().drawPollution )
    {
      info.getImage( "tx.pollution" ).paintIcon( null, g, gPos.x, gPos.y );
    }
    if (( (tspecial&S_FALLOUT) != 0) && info.getOptions().drawFallout )
    {
      info.getImage( "tx.fallout" ).paintIcon( null, g, gPos.x, gPos.y );
    }


    // Make this nicer one day using gradients and alpha blending
    tileno = getVariationID(
      !map.getTile( abs_x0, abs_y0-1 ).isKnown(),
      !map.getTile( abs_x0, abs_y0+1 ).isKnown(),
      !map.getTile( abs_x0+1, abs_y0 ).isKnown(),
      !map.getTile( abs_x0-1, abs_y0 ).isKnown()
    );

    if ( tileno != 0 )
    {
      info.getImage ("tx.darkness_"+NSEW_STRINGS[ tileno ] ).paintIcon(
        null, g, gPos.x, gPos.y
      );
    }


    if (info.areFlagsTransparent())
    {
      // I'm gonna draw cities here for now, although maybe in future these
      // would be in a different layer. Actually, maybe that'd just be excessive

      if ( city != null && info.getOptions().drawCities )
      {
        drawCity( g, gPos, city, info );
      }
      
      //unit = findVisibleUnit( tile );

      /* handle in unit layer
      if ( unit !=  null )
      {
        if ( !cityMode || !unit.isOwner(getClient().getGame().getCurrentPlayer()) )
        {
          if (( true  || focus != unit ) &&
            ( getOptions().drawUnits || (getOptions().drawFocusUnit && true && unit == focus )))
          {
            //no_backdrop = (city != null);
            fillUnitSprites( l, unit, dummy );
            //no_backdrop = false;
            if ( tile.hasUnitStack() )
            {
              l.add( getImage( "unit.stack" ) );
            }
          }
        }
      }
      */
    }

  }

  private void drawCity( Graphics g, Point gPos, City city, MapViewInfo info )
  {
    Tile tile = info.getMap().getTile( city.getX(), city.getY() );

    city.getNationalFlagSprite().paintIcon( null, g, gPos.x, gPos.y );

    if ( tile.hasUnits() )
    {
      city.getOccupiedSprite().paintIcon( null, g, gPos.x, gPos.y );
    }

    city.getSprite( false ).paintIcon( null, g, gPos.x, gPos.y );

    if ( city.hasWalls() )
    {
      city.getCityWallSprite().paintIcon( null, g, gPos.x, gPos.y );
    }

    if ( tile.isPolluted() )
    {
      paintIcon( info, "tx.pollution", g, gPos );
    }

    if ( tile.hasFallout() )
    {
      paintIcon( info, "tx.fallout", g, gPos );
    }

    if ( city.isUnhappy() )
    {
      paintIcon( info, "city.disorder", g, gPos );
    }

    // Don't do fog or war or city size; both are handled elsewhere in the
    // java client.
  
  }
}
