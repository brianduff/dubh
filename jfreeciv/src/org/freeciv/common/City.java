package org.freeciv.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.freeciv.net.PktCityInfo;
import org.freeciv.net.WorkList;

/**
 * Implement me.
 */
public final class City implements CommonConstants
{
  private static HashMap s_cities = new HashMap();


  private List m_presentUnits;
  private int m_id;
  private int m_x;
  private int m_y;

  private int m_owner;
  private String m_name;
  private int m_size;
  private int[] m_pplHappy = new int[5];
  private int[] m_pplContent = new int[5];
  private int[] m_pplUnhappy = new int[5];

  private int m_pplElvis;
  private int m_pplScientist;
  private int m_pplTaxman;

  private int[] m_trade = new int[4];
  private int[] m_tradeValue = new int[4];

  private int m_foodProduction, m_foodSurplus;
  private int m_shieldProduction, m_shieldSurplus;
  private int m_tradeProduction, m_corruption, m_tileTrade;
  private int m_shieldBonus, m_taxBonus, m_scienceBonus;

  private int m_luxuryTotal;
  private int m_taxTotal;
  private int m_scienceTotal;

  private int m_foodStock;
  private int m_shieldStock;
  private int m_pollution;
  private int m_inciteRevoltCost;

  private boolean m_isBuildingUnit;
  private int m_currentlyBuilding;

  private boolean[] m_improvements = new boolean[ B_LAST ];

  private WorkList m_worklist;

  private int[][] m_cityMap = new int[CITY_MAP_SIZE][CITY_MAP_SIZE];

  private List m_supportedUnits;

  private int m_steal;

  private boolean m_didBuy, m_didSell, m_isUpdated;

  // The last year in which somethign was built
  private int m_turnLastBuilt;
  // Suffer shield loss at most once per turn
  private int m_turnChangedTarget;
  // If changed this turn, what changed from (id)
  private int m_changedFromId;
  private boolean m_changedFromIsUnit;

  private int m_disbandedShields;
  private int m_caravanShields;
  private int m_beforeChangeShields;
  private int m_anarchy;
  private int m_rapture;
  private boolean m_wasHappy;
  private boolean m_airlift;
  private int m_original;
  private int m_cityOptions;

  private int m_synched; // server only?

  // Info for diplomat / spy
  private List m_infoUnitsSupported;
  private List m_infoUnitsPresent;

  private CityAI m_ai;
  
  
  

  // city.h: CITY_MAP_SIZE
  public static final int MAP_SIZE = 5;

  private Game m_game;

  /**
   * Find the city with the specified id
   */
  public static City findById(int id)
  {
    return (City) s_cities.get( new Integer(id) );
  }

  public City(Game g, int id)
  {
    m_game = g;
    s_cities.put( new Integer( id ), this );
    m_supportedUnits = new ArrayList();
    m_presentUnits = new ArrayList();
    m_id = id;
  }

  /**
   * Remove this city from the game, freeing up any memory being used. Any
   * references to this city are no longer guaranteed to work properly after
   * calling this method
   */
  public void removeFromGame()
  {
    m_supportedUnits = null;
    m_presentUnits = null;
    m_game = null;
    s_cities.remove( this );
  }

  public String getName()
  {
    return m_name;
  }

  public int getX()
  {
    return m_x;
  }

  public int getY()
  {
    return m_y;
  }

  public void clearAllSupportedUnits()
  {
    m_supportedUnits = new ArrayList();
  }

  public void addSupportedUnit( Unit u )
  {
    m_supportedUnits.add( u );
  }

  public void removeSupportedUnit( Unit u )
  {
    m_supportedUnits.remove( u );
  }

  public void clearAllPresentUnits()
  {
    m_presentUnits = new ArrayList();
  }

  public void addPresentUnit( Unit u )
  {
    m_presentUnits.add( u );
  }

  /**
   * Get the total population, i.e. number of citizens, in the city
   */
  public int getPopulation()
  {
    return m_size * m_size+1 * 5000;
  }

  /**
   * Get the player who owns this city
   */
  public Player getOwner()
  {
    return (Player)
      m_game.getFactories().getPlayerFactory().findById( m_owner );
  }

  public int getId()
  {
    return m_id;
  }

  /**
   * Returns true if this city has the speicifed building (B_*)
   *
   * @param a B_ constant referring to a type of building
   * @return true if the specified building exists in this city
   */
  public boolean hasBuilding( int id )
  {
    if ( !improvementExists( id ) )
    {
      return false;
    }
    return m_improvements[ id ];
  }

  private boolean improvementExists( int id )
  {
    // improvement.c: improvement_exists()
  
    if ( id < 0 || id >= B_LAST || id >= m_game.getNumberOfImprovementTypes() )
    {
      return false;
    }

    if ( (id == B_SCOMP || id == B_SMODULE || id == B_SSTRUCTURAL )
         && !m_game.isSpaceRace() )
    {
      return false;
    }

    // TODO
    return true;
  }

  /**
   * Returns true if this city is affected by the specified wonder.
   *
   * @param id the improvement id of a wonder
   * @return true if the specified wonder affects this city
   */
  public boolean isCityAffectedByWonder( int id )
  {
    if ( !improvementExists( id ) )
    {
      return false;
    }
    if ( !getBuilding( id ).isWonder()
      || getBuilding( id ).isWonderObsolete() )
    {
      return false;
    }

    if ( hasBuilding( id ) )
    {
      return true;
    }

    if ( id == B_MANHATTEN ) 
    {
      return ( m_game.getGlobalWonder( id ) != 0 );
    }

    City tmpCity = getOwner().findCityById( 
      m_game.getGlobalWonder( id )
    );

    if ( tmpCity == null )
    {
      return false;
    }
    /*
    switch (id) {
      case B_ASMITHS:
      case B_APOLLO:
      case B_CURE:
      case B_GREAT:
      case B_WALL:
      case B_HANGING:
      case B_ORACLE:
      case B_UNITED:
      case B_WOMENS:
      case B_DARWIN:
      case B_LIGHTHOUSE:
      case B_MAGELLAN:
      case B_MICHELANGELO:
      case B_SETI:
      case B_PYRAMIDS:
      case B_LIBERTY:
      case B_SUNTZU:
        return true;
      case B_ISAAC:
      case B_COPERNICUS:
      case B_SHAKESPEARE:
      case B_COLLOSSUS:
      case B_RICHARDS:
        return false;
      case B_HOOVER:
      case B_BACH:
        if ( getBuilding( id ).getVariant() == 1 )
        {
          // TODO: return true iff the cities are on the same continent
          return true;
        }
        else
        {
          return true;
        }
      default:
        return false;
  
    } */
    return true; // TODO;
    
  }

  private Building getBuilding( int buildingId )
  {
    return (Building) 
      m_game.getFactories().getBuildingFactory().findById( buildingId );
  }

  /**
   * Returns true if either this city has city walls, or the city is affected
   * by a wonder that provides it with city walls
   *
   * @return true if this city has walls or appears to have walls as an effect
   *    of a wonder
   */
  public boolean hasWalls() 
  {
    if ( hasBuilding( B_CITY ) )
    {
      return true;
    }
    return isCityAffectedByWonder( B_WALL );
  }

  public boolean isUnhappy() 
  {
    return (m_pplHappy[4] < m_pplUnhappy[4]);
  }

  /**
   * Get the size of the city (the number of squares it occupies)
   *
   * @return the size of the city
   */
  public int getSize()
  {
    return m_size;
  }

  public CityStyle getStyle()
  {
    return getOwner().getCityStyle();
  }

  /**
   * Is this city currently building a unit?
   *
   * @return true if the city is in the process of building a unit
   */
  public boolean isBuildingUnit()
  {
    return m_isBuildingUnit;
  }

  /**
   * Get the id of the unit type or building type being constructed in this
   * city
   *
   * @return the id of the thing being built in this city
   */
  public int getCurrentlyBuildingId()
  {
    return m_currentlyBuilding;
  }

  /**
   * Unpackage a city info packet into this object
   */
  public void unpackage( PktCityInfo pkt, boolean newCity )
  {
    m_owner = pkt.owner;
    m_x = pkt.x;
    m_y = pkt.y;
    m_name = pkt.name;

    m_size = pkt.size;
    for (int i=0; i < 5; i++)
    {
      m_pplHappy[i] = pkt.ppl_happy[i];
      m_pplContent[i] = pkt.ppl_content[i];
      m_pplUnhappy[i] = pkt.ppl_unhappy[i];
    }

    m_pplElvis = pkt.ppl_elvis;
    m_pplScientist = pkt.ppl_scientist;
    m_pplTaxman = pkt.ppl_taxman;

    m_cityOptions = pkt.city_options;

    for ( int i=0; i < 4; i++ )
    {
      m_trade[i] = pkt.trade[i];
      m_tradeValue[i] = pkt.trade_value[i];
    }

    m_foodProduction = pkt.food_prod;
    m_foodSurplus = pkt.food_surplus;
    m_shieldProduction = pkt.shield_prod;
    m_shieldSurplus = pkt.shield_surplus;
    m_tradeProduction = pkt.trade_prod;
    m_corruption = pkt.corruption;

    m_luxuryTotal = pkt.luxury_total;
    m_taxTotal = pkt.tax_total;
    m_scienceTotal = pkt.science_total;

    m_foodStock = pkt.food_stock;
    m_shieldStock = pkt.shield_stock;
    m_pollution = pkt.pollution;

    m_isBuildingUnit = pkt.is_building_unit;
    m_currentlyBuilding = pkt.currently_building;

    m_worklist = pkt.worklist;
    m_didBuy = pkt.did_buy;
    m_didSell = pkt.did_sell;
    m_wasHappy = pkt.was_happy;
    m_airlift = pkt.airlift;

    m_turnLastBuilt = pkt.turn_last_built;
    m_turnChangedTarget = pkt.turn_changed_target;
    m_changedFromId = pkt.changed_from_id;
    m_changedFromIsUnit = pkt.changed_from_is_unit;
    m_beforeChangeShields = pkt.before_change_shields;
    m_disbandedShields = pkt.disbanded_shields;
    m_caravanShields = pkt.caravan_shields;

    int i= 0;
    for ( int y = 0; y < CITY_MAP_SIZE; y++ )
    {
      for ( int x = 0; x < CITY_MAP_SIZE; x++ )
      {
        if ( newCity )
        {
          m_cityMap[x][y] = C_TILE_EMPTY;
        }
        setWorkerCity( x, y, pkt.city_map[i++] );
      }
    }

    for ( i=0; i < m_game.getNumberOfImprovementTypes(); i++ )
    {
      m_improvements[i] = pkt.improvements[i];
    }
  }


  private void setWorkerCity( int x, int y, char type )
  {
    int map_x = getX() + x - CITY_MAP_SIZE/2;
    int map_y = getY() + y - CITY_MAP_SIZE/2;

    MapPosition mp = new MapPosition( map_x, map_y );
    if ( m_game.getMap().normalizeMapPosition( mp ) )
    {
      Tile tile = m_game.getMap().getTile( mp.x, mp.y );
      if ( m_cityMap[x][y] == C_TILE_WORKER )
      {
        if ( tile.getWorkedBy() == this )
        {
          tile.setWorkedBy( null );
        }
      }
      m_cityMap[x][y] = type;
      if ( type == C_TILE_WORKER )
      {
        tile.setWorkedBy( this );
      }
      else
      {
        m_cityMap[x][y] = type;
      }
    }
  }


  public class CityAI
  {
    // TODO
  }
  
}