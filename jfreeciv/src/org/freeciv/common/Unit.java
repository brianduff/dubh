package org.freeciv.common;

import javax.swing.Icon;
import java.util.Iterator;

import org.freeciv.net.PktUnitInfo;

/**
 * A freeciv unit
 */
public final class Unit implements CommonConstants
{
  // enum unit_focus_status in unit.h
  public static final int FOCUS_AVAIL = 0;
  public static final int FOCUS_WAIT = 1;
  public static final int FOCUS_DONE = 2;


  private int m_type;
  private int m_id;
  private int m_owner;
  private int m_x, m_y;
  private boolean m_isVeteran;
  private int m_homeCity;
  private int m_movesLeft;
  private int m_hitPoints;
  private int m_unhappiness;
  private int m_upkeep;
  private int m_upkeepGold;
  private int m_upkeepFood;
  private int m_foul;
  private int m_fuel;
  private int m_bribeCost;
  private UnitAI m_ai;
  private int m_activity;
  private int m_gotoDestX;
  private int m_gotoDestY;
  private int m_activityCount;
  private int m_activityTarget;
  private int m_focusStatus;
  private int m_ordMap;
  private int m_ordCity;
  private boolean m_isMoved;
  private boolean m_isParadropped;
  private boolean m_connecting;
  private int m_transportedBy;
  private Game m_game;

  public Unit(Game g)
  {
    m_game = g;
    m_ai = new UnitAI();
  }

  public void setFocusStatus( int status )
  {
    m_focusStatus = status;
  }

  public int getFocusStatus()
  {
    return m_focusStatus;
  }

  public void unpackage( PktUnitInfo pkt )
  {
    m_id = pkt.id;
    m_owner = pkt.owner;
    m_x = pkt.x;
    m_y = pkt.y;
    m_homeCity = pkt.homecity;
    m_isVeteran = pkt.veteran;
    m_type = pkt.type;
    m_movesLeft = pkt.movesleft;
    m_hitPoints = pkt.hp;
    m_activity = pkt.activity;
    m_activityCount = pkt.activity_count;
    m_unhappiness = pkt.unhappiness;
    m_upkeep = pkt.upkeep;
    m_upkeepFood = pkt.upkeep_food;
    m_upkeepGold = pkt.upkeep_gold;
    m_ai.setControlled( pkt.ai );
    m_fuel = pkt.fuel;
    m_gotoDestX = pkt.goto_dest_x;
    m_gotoDestY = pkt.goto_dest_y;
    m_activityTarget = pkt.activity_target;
    m_isParadropped = pkt.paradropped;
    m_connecting = pkt.connecting;

    m_focusStatus = FOCUS_AVAIL;
    m_bribeCost = 0;
    m_foul = 0;
    m_ordMap = 0;
    m_ordCity = 0;
    m_isMoved = false;
    m_transportedBy = 0;
  }

  public int getId()
  {
    return m_id;
  }

  public UnitAI getAI()
  {
    return m_ai;
  }

  /**
   * How many moves does this unit still have?
   *
   * @return the number of moves this unit has still not taken this turn
   */
  public int getMovesLeft()
  {
    return m_movesLeft;
  }

  public int getActivity()
  {
    return m_activity;
  }

  public void setActivity( int activity )
  {
    m_activity = activity;
  }

  public int getActivityTarget()
  {
    return m_activityTarget;
  }

  public void setActivityTarget( int activityTarget )
  {
    m_activityTarget = activityTarget;
  }

  public Player getOwner()
  {
    return (Player) m_game.getFactories().getPlayerFactory().findById( m_owner );
  }

  public boolean isOwner( Player p )
  {
    return getOwner() == p;
  }

  public City getHomeCity()
  {
    return City.findById( m_homeCity );
  }

  public int getHomeCityId()
  {
    return m_homeCity;
  }

  public void setHomeCity( int city )
  {
    m_homeCity = city;
  }

  public void setHomeCity( City city )
  {
    m_homeCity = city.getId();
  }

  public int getHitPoints()
  {
    return m_hitPoints;
  }

  public void setHitPoints( int hp )
  {
    m_hitPoints = hp;
  }

  public int getType()
  {
    return m_type;
  }

  public UnitType getUnitType()
  {
    return (UnitType) m_game.getFactories().getUnitTypeFactory().findById( getType() );
  }

  public void setType( int type )
  {
    m_type = type;
  }

  public int getX()
  {
    return m_x;
  }

  public int getY()
  {
    return m_y;
  }

  public int getGotoDestX()
  {
    return m_gotoDestX;
  }

  public int getGotoDestY()
  {
    return m_gotoDestY;
  }

  public int getUnhappiness()
  {
    return m_unhappiness;
  }

  public void setUnhappiness( int unhappy )
  {
    m_unhappiness = unhappy;
  }

  public int getUpkeep()
  {
    return m_upkeep;
  }

  public void setUpkeep( int upkeep )
  {
    m_upkeep = upkeep;
  }

  public int getUpkeepGold()
  {
    return m_upkeepGold;
  }

  public void setUpkeepGold( int gold )
  {
    m_upkeepGold = gold;
  }

  public int getUpkeepFood()
  {
    return m_upkeepFood;
  }

  public void setUpkeepFood( int food )
  {
    m_upkeepFood = food;
  }

  public boolean isVeteran()
  {
    return m_isVeteran;
  }

  public void setVeteran( boolean isVeteran )
  {
    m_isVeteran = isVeteran;
  }

  public void setMovesLeft( int movesLeft )
  {
    m_movesLeft = movesLeft;
  }

  public void setBribeCost( int bribeCost )
  {
    m_bribeCost = bribeCost;
  }

  public void setFuel( int fuel )
  {
    m_fuel = fuel;
  }

  public void setGotoDestX( int gotox )
  {
    m_gotoDestX = gotox;
  }

  public void setGotoDestY( int gotoy )
  {
    m_gotoDestY = gotoy;
  }

  public void setParadropped( boolean isParadropped )
  {
    m_isParadropped = isParadropped;
  }

  public void setConnecting( boolean isConnecting )
  {
    m_connecting = isConnecting;
  }
  public void setActivityCount( int activity )
  {
    m_activityCount = activity;
  }

  public boolean isMilitary()
  {
    return !isFlagSet( F_NONMIL );
  }

  /**
   * Can this unit transport other units?
   */
  public boolean isTransporter()
  {
    return getTransportCapacity() > 0;
  }

  /**
   * How many units can this unit transport?
   */
  public int getTransportCapacity()
  {
    return getUnitType().getTransportCapacity();
  }
  
  /**
   * Does this unit move on the ground?
   */
  public boolean isGroundUnit()
  {
    return getUnitType().getMoveType() == LAND_MOVING;
  }

  /**
   * Does this unit sail on the seas?
   */
  public boolean isSailingUnit()
  {
    return getUnitType().getMoveType() == SEA_MOVING;
  }

  /**
   * Is the specified unit flag set for this unit type?
   */
  public boolean isFlagSet( int flag )
  {
    Assert.that( flag >= 0 && flag < F_LAST );
    return ((getUnitType().getFlags() &  ( 1 << flag )) != 0);
  }

  public boolean canHelpBuildWonderHere()
  {
    City city = m_game.getMap().getCity( getX(), getY() );
    return (city != null && canHelpBuildWonder( city ) );
  }

  private boolean canHelpBuildWonder(City c)
  {
    return false;
    /*
    return isFlagSet( F_CARAVAN ) &&
      m_game.getMap().isTilesAdjacent( getX(), getY(), c.getX(), c.getY() ) &&
      m_owner == c.getOwner().getId() &&
      c.getCurrentlyBuilding().isWonder() &&
      c.getShieldStock() < c.getCurrentlyBuilding().getValue();
    */
  }
  
  public boolean canBuildCity()
  {
    if ( !isFlagSet( F_CITIES ) )
    {
      return false;
    }
    if ( getMovesLeft() == 0 )
    {
      return false;
    }
    if ( !m_game.getMap().canCityBeBuiltAt( getX(), getY() ) )
    {
      return false;
    }
    return true;
  }

  public boolean canSetTraderouteHere()
  {
    /// TODO
    return false;
  }

  public boolean isConnecting()
  {
    // toDO 
    return false;
  }
  
  public boolean canChangeHomecity()
  {
    City city = m_game.getMap().getCity( getX(), getY() );
    return city != null && city.getOwner() == this.getOwner();
  }
  
  public boolean canDoAutoSettler()
  {
    return isFlagSet( CommonConstants.F_SETTLERS );
  }
  
  public boolean canDoAutoAttack()
  {
    return isMilitary() && m_game.getMap().getCity( getX(), getY() ) != null;
  }
  
  public boolean canDoActivity( int activity )
  {
    return canDoActivity( activity, 0 );
  }

  /**
   * Checks whether this unit can perform an activity.
   * 
   * All these large, unreadable code blocks are courtesy of:
   * unit.c:can_unit_do_activity_targeted()
   * 
   * @return true if this unit can perform the specified activity
   */
  public boolean canDoActivity( int activity, int target )
  {
    Map map = m_game.getMap();
    Tile tile = map.getTile( getX(), getY() );
    TerrainType tType = tile.getTerrainType();
    
    if( activity == ACTIVITY_IDLE || activity == ACTIVITY_GOTO
       || activity == ACTIVITY_PATROL )
    {
      return true;
    }
    if( activity == ACTIVITY_POLLUTION )
    {
      return isFlagSet( F_SETTLERS ) 
        && ( tile.getSpecial() & S_POLLUTION ) != 0;
    }
    if( activity == ACTIVITY_FALLOUT )
    {
      return isFlagSet( F_SETTLERS )
        && ( tile.getSpecial() & S_FALLOUT ) != 0;
    }
    if( activity == ACTIVITY_ROAD )
    {
      return m_game.getTerrainRules().isRoadAllowed()
        && isFlagSet( F_SETTLERS )
        && ( tile.getSpecial() & S_ROAD ) == 0
        && tType.getRoadTime() != 0
        && ( ( tile.getTerrain() != T_RIVER 
              || ( tile.getSpecial() & S_RIVER ) != 0 )
              || getOwner().knowsTechsWithFlag( TF_BRIDGE ) );
    }
    if( activity == ACTIVITY_MINE )
    {
      if( m_game.getTerrainRules().isMineAllowed()         && isFlagSet( F_SETTLERS )         && ( ( tile.getTerrain() == tType.getMiningResult()               && ( tile.getSpecial() & S_MINE ) == 0 )
             || ( tile.getTerrain() != tType.getMiningResult()                 && tType.getMiningResult() != T_LAST                 && ( tile.getTerrain() != T_OCEAN                     || tType.getMiningResult() == T_OCEAN                     || map.canReclaimOcean( getX(), getY() ) )                 && ( tile.getTerrain() == T_OCEAN                     || tType.getMiningResult() != T_OCEAN                     || map.canChannelLand( getX(), getY() ) )
                 && ( tType.getMiningResult() != T_OCEAN
                     || map.getCity( getX(), getY() ) != null ) ) ) )
      {        // Don't allow it if someone else is irrigating this tile.
        // *Do* allow it if they're transforming - the mine may survive        for ( Iterator i = tile.getUnits(); i.hasNext(); )        {
          if ( ((Unit)i.next()).getActivity() == ACTIVITY_IRRIGATE )
          {            return false;
          }        }        return true;
      }
      else
      {        return false;
      }    }
    if( activity == ACTIVITY_IRRIGATE )
    {
      if( m_game.getTerrainRules().isIrrigateAllowed()         && isFlagSet( F_SETTLERS )         && ( ( tile.getTerrain() == tType.getIrrigationResult()               && ( ( tile.getSpecial() & S_IRRIGATION ) == 0
                   || ( ( tile.getSpecial() & S_FARMLAND ) == 0
                       && getOwner().knowsTechsWithFlag( TF_FARMLAND ) ) ) )             && ( ( tile.getTerrain() == tType.getIrrigationResult()                   && map.isWaterAdjacentTo( getX(), getY() ) )
                 || ( tile.getTerrain() != tType.getIrrigationResult()                     && tType.getIrrigationResult() != T_LAST                     && ( tile.getTerrain() != T_OCEAN                         || tType.getIrrigationResult() == T_OCEAN                         || map.canReclaimOcean( getX(), getY() ) )                     && ( tile.getTerrain() == T_OCEAN                         || tType.getIrrigationResult() != T_OCEAN                         || map.canChannelLand( getX(), getY() ) )
                     && ( tType.getIrrigationResult() != T_OCEAN
                         || map.getCity( getX(), getY() ) != null ) ) ) ) )
      {        // Don't allow it if someone else is mining this tile.
        // *Do* allow it if they're transforming - the irrigation may survive        for ( Iterator i = tile.getUnits(); i.hasNext(); )        {
          if ( ((Unit)i.next()).getActivity() == ACTIVITY_MINE )
          {            return false;
          }        }        return true;
      }
      else
      {        return false;
      }    }
    if( activity == ACTIVITY_FORTIFYING )
    {
      return isGroundUnit() && getActivity() != ACTIVITY_FORTIFIED
        && !isFlagSet( F_SETTLERS ) && tile.getTerrain() != T_OCEAN;
    }
    if( activity == ACTIVITY_FORTIFIED )
    {
      return false;
    }
    if( activity == ACTIVITY_FORTRESS )
    {
      return  isFlagSet( F_SETTLERS ) && map.getCity( getX(), getY() ) == null
        && getOwner().knowsTechsWithFlag( TF_FORTRESS )
        && ( tile.getSpecial() & S_FORTRESS ) == 0
        && tile.getTerrain() != T_OCEAN;
    }
    if( activity == ACTIVITY_AIRBASE )
    {
      return  isFlagSet( F_AIRBASE )
        && getOwner().knowsTechsWithFlag( TF_AIRBASE )
        && ( tile.getSpecial() & S_AIRBASE ) == 0
        && tile.getTerrain() != T_OCEAN;
    }
    if( activity == ACTIVITY_SENTRY )
    {
      return true;
    }
    if( activity == ACTIVITY_RAILROAD )
    {
      // TODO
      return false;
    }
    if( activity == ACTIVITY_PILLAGE )
    {
      // TODO (I don't understand this one in the c code --ben)
      return false;
    }
    if( activity == ACTIVITY_EXPLORE )
    {
      return isGroundUnit() || isSailingUnit();
    }
    if( activity == ACTIVITY_TRANSFORM )
    {
      // TODO
      return false;
    }
    
    
    //TODO : the rest

    
    return false;
  }

  /**
   * Get the national flag sprite for this unit
   */
  public Icon getNationalFlagSprite()
  {
    return getOwner().getNation().getFlagSprite();
  }

  public Icon getSprite()
  {
    return getUnitType().getSprite();
  }
  
}