package org.freeciv.client.map.grid;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Iterator;
import javax.swing.Icon;

import org.freeciv.client.Constants;
import org.freeciv.client.map.MapViewInfo;
import org.freeciv.common.Tile;
import org.freeciv.common.Map;
import org.freeciv.common.Unit;
import org.freeciv.common.Logger;

/**
 * The unit layer. There is some debate about whether units should get blitted
 * into the back buffer or not
 *
 * @author Brian Duff
 */
class UnitLayer extends GridMapTileLayer implements Constants
{
  private static final int NUM_TILES_HP_BAR = 11;


  UnitLayer( GridMapView mv )
  {
    super( mv );
  }

  protected boolean isBuffered()
  {
    return false;
  }  

  /**
   * Find the unit that is visible at the specified tile.
   *
   * @param tile the tile to find the unit for
   * @return the unit which is visible at the specified tile, null if there
   *    are no visible units on the tile.
   */
  private Unit findVisibleUnit( Tile tile, MapViewInfo info )
  {
    // candidate for moving elsewhere (e.g. Tile.java, but is client specific...)
    // PROBABLY MOVE INTO DOM'S MANAGERS
    // from control.c: find_visible_unit(): recheck this.

    if ( !tile.hasUnits() )
    {
      return null;
    }

    Map map = info.getMap();

    Unit unit;

    // If a unit is attacking, show it on top
    //unit = getClient().getAttackingUnit();
    //if ( unit != null && map.getTile( unit.getX(), unit.getY() ) == tile )
    //{
    //  return unit;   // c weirdness here...
    //}

    // If a unit is defending, show it on top.
    //unit = getClient().getDefendingUnit();
    //if ( unit != null && map.getTile( unit.getX(), unit.getY() ) == tile )
    //{
    //  return unit;
    // }

    // If the unit is in focus at this tile, show it on top.
    //unit = getClient().getUnitInFocus();
    //if ( unit != null && map.getTile( unit.getX(), unit.getY() ) == tile )
    //{
    //  return unit;
    //}

    // If a city is here, show nothing ( unit hidden by city )
    if ( tile.hasCity() )
    {
      return null;
    }

    // Iterate through the units to find the best one. We prioritize like this:
    // 1. Owned transported
    // 2. Any owned unit
    // 3. Any Transporter
    // 4. Any Unit

    Unit anyowned = null, tpother = null, anyother = null;

    Iterator unitIter = tile.getUnits();
    while (unitIter.hasNext())
    {
      unit = (org.freeciv.common.Unit) unitIter.next();

      if ( unit.isOwner( info.getCurrentPlayer() ) )
      {
        if ( unit.isTransporter() )
        {
          return unit;
        }
        else if ( anyowned == null )
        {
          anyowned = unit;
        }
      }
      else if ( tpother == null && info.getCurrentPlayer().canSeeUnit( unit ) )
      {
        if ( unit.isTransporter() )
        {
          tpother = unit;
        }
        else
        {
          anyother = unit;
        }
      }

    }

    return ( anyowned != null ? anyowned :  ( tpother != null ? tpother : anyother ) );

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

  private int clip(int lower, int thisOne, int upper)
  {
    return ( thisOne < lower ? lower : ( thisOne > upper ? upper : thisOne ) );
  }

  public void paintTile(Graphics g, Point mapPos, Point gPos, MapViewInfo info)
  {
    Tile tile = info.getTile( mapPos );
    Unit unit = findVisibleUnit( tile, info );

    if ( unit != null )
    {
      // nb in city mode, we draw enemy units but not our own TODO

      unit.getNationalFlagSprite().paintIcon( null, g, gPos.x, gPos.y );
      unit.getSprite().paintIcon( null, g, gPos.x, gPos.y );

      if ( unit.getActivity() != ACTIVITY_IDLE )
      {
        int a = unit.getActivity();
        if ( ACTIVITY_MINE == a )
          paintIcon( info, "unit.mine", g, gPos );
        else if ( ACTIVITY_POLLUTION == a )
          paintIcon( info, "unit.pollution" , g, gPos );
        else if ( ACTIVITY_FALLOUT  ==  a )
          paintIcon( info, "unit.fallout" , g, gPos );
        else if ( ACTIVITY_PILLAGE == a )
          paintIcon( info, "unit.pillage" , g, gPos );
        else if ( ACTIVITY_ROAD == a || ACTIVITY_RAILROAD == a )
          paintIcon( info, "unit.road" , g, gPos );
        else if ( ACTIVITY_IRRIGATE == a )
          paintIcon( info, "unit.irrigate", g, gPos );
        else if ( ACTIVITY_EXPLORE == a )
          paintIcon( info, "unit.auto_explore" , g, gPos );
        else if ( ACTIVITY_FORTIFIED == a )
          paintIcon( info, "unit.fortified" , g, gPos );
        else if ( ACTIVITY_FORTRESS == a )
          paintIcon( info, "unit.fortress" , g, gPos );
        else if ( ACTIVITY_FORTIFYING == a )
          paintIcon( info, "unit.fortifying" , g, gPos );
        else if ( ACTIVITY_AIRBASE == a )
          paintIcon( info, "unit.airbase" , g, gPos );
        else if ( ACTIVITY_SENTRY == a )
          paintIcon( info, "unit.sentry" , g, gPos );
        else if ( ACTIVITY_GOTO == a )
          paintIcon( info, "unit.goto" , g, gPos );
        else if ( ACTIVITY_TRANSFORM == a )
          paintIcon( info, "unit.transform" , g, gPos );

      }

      if ( unit.getAI().isControlled() )
      {
        if ( unit.isMilitary() )
        {
          paintIcon( info, "unit.auto_attack", g, gPos );
        }
        else
        {
          paintIcon( info, "unit.auto_settler", g, gPos );
        }
      }

      if ( unit.isConnecting() )
      {
        paintIcon( info, "unit.connect", g, gPos );
      }

      if ( unit.getActivity() == ACTIVITY_PATROL )
      {
        paintIcon( info, "unit.patrol", g, gPos );
      }

      int ihp = (( NUM_TILES_HP_BAR - 1 ) * unit.getHitPoints() )  / unit.getUnitType().getHitPoints();
      ihp = clip( 0, ihp, NUM_TILES_HP_BAR-1 );

      paintIcon( info, "unit.hp_"+ihp*10, g, gPos ); 


      if ( tile.hasUnitStack() )
      {
        paintIcon( info, "unit.stack", g, gPos );
      }
    }
  }
}