package org.freeciv.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import javax.swing.Icon;

import org.freeciv.net.PktCityInfo;
import org.freeciv.net.PktShortCity;
import org.freeciv.net.WorkList;

/**
 * Implement me.
 */
public final class City implements CommonConstants
{
  private static HashMap s_cities = new HashMap();

  private static final int
    TYPE_UNIT = 0,
    TYPE_NORMAL_IMPROVEMENT = 1,
    TYPE_WONDER = 2;

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
  
  public int getFoodProduction()
  {
    return m_foodProduction;
  }  
  public int getFoodSurplus()
  {
    return m_foodSurplus;
  }
  
  public int getShieldProduction()
  {
    return m_shieldProduction;
  }  
  public int getShieldSurplus()
  {
    return m_shieldSurplus;
  }
  
  public int getTradeProduction()
  {
    return m_tradeProduction;
  }  
  public int getCorruption()
  {
    return m_corruption;
  }
  
  public int getTaxTotal()
  {
    return m_taxTotal;
  }  
  public int getScienceTotal()
  {
    return m_scienceTotal;
  }  
  public int getLuxuryTotal()
  {
    return m_luxuryTotal;
  }

  
  public int getFoodStock()
  {
    return m_foodStock;
  }
  public int getShieldStock()
  {
    return m_shieldStock;
  }
  public int getPollution()
  {
    return m_pollution;
  }
  
  public int getHappyCitizens( int x )
  {
    return m_pplHappy[ x ];
  }
  public int getContentCitizens( int x )
  {
    return m_pplContent[ x ];
  }
  public int getUnhappyCitizens( int x )
  {
    return m_pplUnhappy[ x ];
  }
  
  public int getElvises() 
  {
    return m_pplElvis;
  }
  public int getScientists() 
  {
    return m_pplScientist;
  }
  public int getTaxmen() 
  {
    return m_pplTaxman;
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

  public Iterator getSupportedUnits()
  {
    return m_supportedUnits.iterator();
  }

  public void clearAllPresentUnits()
  {
    m_presentUnits = new ArrayList();
  }

  public void addPresentUnit( Unit u )
  {
    m_presentUnits.add( u );
  }

  public Iterator getPresentUnits()
  {
    return m_presentUnits.iterator();
  }

  /**
   * Get the total population, i.e. number of citizens, in the city
   */
  public int getPopulation()
  {
    return m_size * ( m_size + 1 ) * 5000;
  }

  /**
   * Get the player who owns this city
   */
  public Player getOwner()
  {
    return (Player)
      m_game.getFactories().getPlayerFactory().findById( m_owner );
  }

  /**
   * Get the id of the player who owns this city
   */
  public int getOwnerId()
  {
    return m_owner;
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
    if ( !m_game.buildingExists( id ) )
    {
      return false;
    }
    return m_improvements[ id ];
  }

  /**
   * Determine the amount of gold this city "earns" (tax - upkeep)
   * 
   * @return the amount of gold this city "earns" per turn
   */
  public int getGoldSurplus()
  {
    boolean asmiths = isCityAffectedByWonder( B_ASMITHS );
    int cost = 0;
    for( int i = 0 ; i < m_game.getNumberOfImprovementTypes(); i++ )     {
      if( hasBuilding( i ) )       {
        cost += getBuildingUpkeep( i, asmiths );
      }    }
    return getTaxTotal() - cost;
  }
  
  /**
   * Returns the amount of gold that this building costs to upkeep
   * 
   * @param buildingId the id of the building to check
   * @return the amount of gold that this building costs to upkeep
   */
  public int getBuildingUpkeep( int buildingId )
  {
    return getBuildingUpkeep( buildingId, 
                              isCityAffectedByWonder( B_ASMITHS ) );
  }
                                                          
  /**
   * Returns the amount of gold that this building costs to upkeep
   * 
   * @param buildingId the id of the building to check
   * @param asmiths whether this city is affected by Adam Smith's
   * @return the amount of gold that this building costs to upkeep
   */
  public int getBuildingUpkeep( int id, boolean asmiths )
  {
    if( !m_game.buildingExists( id ) )
    {
      return 0;
    }
    if( getBuilding( id ).isWonder() )
    {
      return 0;
    }
    if( asmiths && getBuilding( id ).getUpkeep() == 1 )
    {
      return 0;
    }
    return getBuilding( id ).getUpkeep();
  }
                                                          
  /**
   * Returns true if this city is affected by the specified wonder.
   *
   * @param id the improvement id of a wonder
   * @return true if the specified wonder affects this city
   */
  public boolean isCityAffectedByWonder( int id )
  {
    if ( !m_game.buildingExists( id ) )
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

    City tmpCity = getOwner().getCity(
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
  
  /**
   * Returns true if either this city has the specified building, or 
   * the city is affected by a wonder that provides that building
   * 
   * @param buildingId a building type that is not a wonder.
   * @return true if this city has that building or is affected by 
   *    a wonder that provides that effect.
   * @see #hasWalls()
   */
  public boolean hasEffect( int buildingId )
  {
    return hasBuilding( buildingId) || hasWonderReplacement( buildingId );
  }
  
  /**
   * Returns true if the city is affected by a wonder that 
   * provides the effect of that building
   * 
   * @param buildingId a building type that is not a wonder.
   * @return true if the city is affected by a wonder that provides 
   *    the effect of that building
   */
  public boolean hasWonderReplacement( int buildingId )
  {
    if( getBuilding( buildingId ).isWonder() )    {
      //throw new IllegalArgumentException();
      return false;    }    if( buildingId == B_BARRACKS
       || buildingId == B_BARRACKS2
       || buildingId == B_BARRACKS2 )    {      return isCityAffectedByWonder( B_SUNTZU );    }
    if( buildingId == B_GRANARY )    {      return isCityAffectedByWonder( B_PYRAMIDS );      /* && improvement_variant( B_PYRAMIDS ) == 0 */  //TODO
    }
    if( buildingId == B_CATHEDRAL )    {      return isCityAffectedByWonder( B_MICHELANGELO );      /* && improvement_variant( B_MICHELANGELO ) == 0 */  //TODO
    }
    if( buildingId == B_CITY )    {      return isCityAffectedByWonder( B_WALL );    }    if( buildingId == B_HYDRO
       || buildingId == B_POWER       || buildingId == B_NUCLEAR )    {      return isCityAffectedByWonder( B_HOOVER );    }
    if( buildingId == B_POLICE )    {      return isCityAffectedByWonder( B_WOMENS );    }
    if( buildingId == B_RESEARCH )    {      return isCityAffectedByWonder( B_SETI );    }
    return false;
  }

  private Building getBuilding( int buildingId )
  {
    return (Building)
      m_game.getBuilding( buildingId );
  }

  private UnitType getUnitType( int unitId )
  {
    return (UnitType)
      m_game.getUnitType( unitId );
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
   * Formula used to caclulate granary size
   * 
   * @return the amount of food that the city needs to reach for growth
   */
  public int getGranarySize()
  {
    return m_game.getGameRules().getInitialGranaryFood() * m_game.getFoodBox()
      + ( m_game.getGameRules().getGranaryFoodIncrement() * getSize() 
          * m_game.getFoodBox() ) / 100;
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
   * Compute the change production penalty for the given production change
   * (target, is_unit) in this city.
   *
   */
  private int getChangeProductionCharge( int target, boolean isUnit )
  {
    int shieldStockAfterAdjustment;
    int origClass;
    int newClass;
    boolean putPenalty;

    if ( m_changedFromIsUnit )
    {
      origClass = TYPE_UNIT;
    }
    else if ( getBuilding( m_changedFromId ).isWonder() )
    {
      origClass = TYPE_WONDER;
    }
    else
    {
      origClass = TYPE_NORMAL_IMPROVEMENT;
    }

    if ( isUnit )
    {
      newClass = TYPE_UNIT;
    }
    else if ( getBuilding( target ).isWonder() )
    {
      newClass = TYPE_WONDER;
    }
    else
    {
      newClass = TYPE_NORMAL_IMPROVEMENT;
    }

    putPenalty = ( origClass != newClass &&
      m_game.nextYear( m_turnLastBuilt ) < m_game.getYear() );

    if ( putPenalty )
    {
      shieldStockAfterAdjustment =
        m_beforeChangeShields / 2;
    }
    else
    {
      shieldStockAfterAdjustment =
        m_beforeChangeShields;
    }

    shieldStockAfterAdjustment += m_disbandedShields;

    if ( newClass == TYPE_WONDER )
    {
      shieldStockAfterAdjustment
        += m_caravanShields;
    }
    else
    {
      shieldStockAfterAdjustment +=
        m_caravanShields / 2;
    }

    return shieldStockAfterAdjustment;

  }
  
  /**
   * Gets the cost, in shields of the unit or building being built
   */
  public int getCurrentProductionCost()
  {
    return isBuildingUnit() ?
      getUnitType( getCurrentlyBuildingId() ).getBuildCost() :
      getBuilding( getCurrentlyBuildingId() ).getBuildCost();
  }
  
  /**
   * Gets the cost, in gold, to buy the current unit or building
   */
  public int getBuyCost()
  {
    int total = getCurrentProductionCost();
    int cost;
    int built = getShieldStock();
    // if we're done building it, then it's free!
    if( built >= total )
    {
      return 0;
    }
    // otherwise, it'll cost you.
    if( isBuildingUnit() )
    {
      cost = ( total - built ) * 2 + ( total - built ) * ( total - built ) / 20;
    }
    else
    {
      cost =  ( total - built ) * 2;
      if( getBuilding( getCurrentlyBuildingId() ).isWonder() )
      {
        cost *= 2;
      }
    }
    // also, if there's nothing built, it's more expensive
    if( built == 0 )
    {
      cost *= 2;
    }
    
    return cost;
  }
  
  /**
   * Get the number of turns before the unit or building being built will
   * be complete
   *
   * @return the number of turns until the building is complete
   */
  public int getTurnsToBuild()
  {

    int improvement_cost = getCurrentProductionCost();

    if ( m_shieldStock >= improvement_cost )
    {
      return 1;
    }

    int city_shield_surplus = m_shieldSurplus;
    int city_shield_stock = getChangeProductionCharge(
      getCurrentlyBuildingId(), isBuildingUnit()
    );

    if ( city_shield_surplus > 0 )
    {
      return
        (improvement_cost - city_shield_stock +
          city_shield_surplus - 1 ) /
            city_shield_surplus;
    }
    else
    {
      return 999;
    }

  }
  
  /**
   * Get the number of turns before the given unit or building will
   * be complete
   *
   * @return the number of turns until the given unit or building is complete
   */
  public int getTurnsToBuild( int id, boolean isUnit )
  {

    int improvement_cost = isUnit ? getUnitType( id ).getBuildCost()
                                    : getBuilding( id ).getBuildCost();

    if ( m_shieldStock >= improvement_cost )
    {
      return 1;
    }

    int city_shield_surplus = m_shieldSurplus;
    int city_shield_stock = getChangeProductionCharge( id, isUnit );

    if ( city_shield_surplus > 0 )
    {
      return
        (improvement_cost - city_shield_stock +
          city_shield_surplus - 1 ) /
            city_shield_surplus;
    }
    else
    {
      return 999;
    }

  }

  /**
   * Get a description of what is currently being built in this city
   */
  public String getCurrentlyBuildingDescription()
  {
    if ( isBuildingUnit() )
    {
      UnitType ut =
        (UnitType)m_game.getFactories().getUnitTypeFactory().findById( getCurrentlyBuildingId() );

      return ut.getName();
    }
    else
    {
      Building b =
        (Building) m_game.getFactories().getBuildingFactory().findById( getCurrentlyBuildingId() );
      return b.getName();
    }
  }
  
  /**
   * Could the city build this right now if it were available?
   */
  public boolean couldBuildImprovement( int id )
  {
    if( !m_game.buildingExists( id ) )    {
      return false;    }
    if( hasBuilding( id ) )    {
      return false;    }    if( ( hasBuilding( B_HYDRO ) || hasBuilding( B_POWER )           || hasBuilding( B_NUCLEAR ) ) && ( id == B_HYDRO || id == B_POWER                                            || id == B_NUCLEAR ) )    {
      return false;    }    if( id == B_RESEARCH && !hasBuilding( B_UNIVERSITY ) )    {      return false;    }
    if( id == B_UNIVERSITY && !hasBuilding( B_LIBRARY ) )    {      return false;    }
    if( id == B_STOCK && !hasBuilding( B_BANK ) )    {      return false;    }
    if( id == B_SEWER && !hasBuilding( B_AQUEDUCT ) )    {      return false;    }
    if( id == B_BANK && !hasBuilding( B_MARKETPLACE ) )    {      return false;    }
    if( id == B_MFG && !hasBuilding( B_FACTORY ) )    {      return false;    }    // TODO: terrain stuff    /*
    if ((id==B_HARBOUR || id==B_COASTAL || id == B_OFFSHORE || id == B_PORT) && !is_terrain_near_tile(pcity->x, pcity->y, T_OCEAN))
      return 0;
    if (id == B_HYDRO
        && !(map_get_terrain(pcity->x, pcity->y) == T_RIVER)
        && !(map_get_special(pcity->x, pcity->y) & S_RIVER)
        && !(map_get_terrain(pcity->x, pcity->y) == T_MOUNTAINS)
        && !is_terrain_near_tile(pcity->x, pcity->y, T_MOUNTAINS)
        && !is_terrain_near_tile(pcity->x, pcity->y, T_RIVER)
        && !is_special_near_tile(pcity->x, pcity->y, S_RIVER)
       )
      return 0;    */    // TODO: spaceship stuff    /*
    if (id == B_SSTRUCTURAL || id == B_SCOMP || id == B_SMODULE) {
      if (!game.global_wonders[B_APOLLO]) {
        return 0;
      } else {
        struct player *p=city_owner(pcity);
        if (p->spaceship.state >= SSHIP_LAUNCHED)
	  return 0;
        if (id == B_SSTRUCTURAL && p->spaceship.structurals >= NUM_SS_STRUCTURALS)
	  return 0;
        if (id == B_SCOMP && p->spaceship.components >= NUM_SS_COMPONENTS)
	  return 0;
        if (id == B_SMODULE && p->spaceship.modules >= NUM_SS_MODULES)
	  return 0;
      }
    }    */
    // TODO: wonder stuff    /*
    if (is_wonder(id)) {
      if (game.global_wonders[id]) return 0;
    } else {
      struct player *pplayer=city_owner(pcity);
      if (improvement_obsolete(pplayer, id)) return 0;
    }
    */    return !hasWonderReplacement( id );
  }

  /**
   * Can we build this right now?  We have the techs and everything?
   */
  public boolean canBuildImprovement( int id )
  {
    if( !m_game.buildingExists( id ) )    {
      return false;    }    if( !getOwner().hasInvention( getBuilding( id ).getRequiredAdvanceId() ) )    {
      return false;    }
    return couldBuildImprovement( id );
  }

  /**
   * Whether the city can build the given unit, ignoring if the unit is
   * obsolete and whether the owener can build these units
   */
  public boolean canBuildUnitTypeDirect( int id )
  {
    if( !m_game.unitTypeExists( id ) ) 
    {
      return false;
    }
    final UnitType unt = getUnitType( id );
    if( ( unt.getFlags() & ( 1 << F_NUCLEAR ) ) != 0 && m_game.getGlobalWonder( B_MANHATTEN ) == 0 )    {
      return false;    }    if( !getOwner().hasInvention( unt.getRequiredAdvanceId() ) )    {
      return false;    }    //TODO: check terrain
    //if (!is_terrain_near_tile(pcity->x, pcity->y, T_OCEAN) && is_water_unit(id))
    //  return 0;
    
    return true;
  }

  /**
   * Whether the city can build the given unit, checking if it is obsolete
   */
  public boolean canBuildUnitType( int id )
  {
    if( !canBuildUnitTypeDirect( id ) )
     {
      return false;
    }
    return true;
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


  /**
   * Unpackage a city info packet into this object
   */
  public void unpackage( PktShortCity pkt, boolean newCity )
  {
    m_owner = pkt.owner;
    m_x = pkt.x;
    m_y = pkt.y;
    m_name = pkt.name;

    m_size = pkt.size;

    if( pkt.happy )
    {
      m_pplHappy[4] = m_size;
      m_pplUnhappy[4] = 0;
    }
    else
    {
      m_pplUnhappy[4] = m_size;
      m_pplHappy[4] = 0;
    }

    m_improvements[B_PALACE] = pkt.capital;
    m_improvements[B_CITY] = pkt.walls;

    if( newCity )
    {
      m_worklist = null; // is this valid?
    }

    // just set dummy values for everything else, to be on the safe side
    m_pplElvis = m_size;
    m_pplScientist = 0;
    m_pplTaxman = 0;

    m_cityOptions = 0;

    for ( int i=0; i < 4; i++ )
    {
      m_trade[i] = 0;
      m_tradeValue[i] = 0;
    }

    m_foodProduction = 0;
    m_foodSurplus = 0;
    m_shieldProduction = 0;
    m_shieldSurplus = 0;
    m_tradeProduction = 0;
    m_corruption = 0;

    m_luxuryTotal = 0;
    m_taxTotal = 0;
    m_scienceTotal = 0;

    m_foodStock = 0;
    m_shieldStock = 0;
    m_pollution = 0;

    m_isBuildingUnit = false;
    m_currentlyBuilding = 0;

    //m_worklist = pkt.worklist;
    m_didBuy = false;
    m_didSell = false;
    m_wasHappy = false;
    m_airlift = false;

    m_turnLastBuilt = 0;
    m_turnChangedTarget = 0;
    m_changedFromId = 0;
    m_changedFromIsUnit = false;
    m_beforeChangeShields = 0;
    m_disbandedShields = 0;
    m_caravanShields = 0;

    int i= 0;
    for ( int y = 0; y < CITY_MAP_SIZE; y++ )
    {
      for ( int x = 0; x < CITY_MAP_SIZE; x++ )
      {
          m_cityMap[x][y] = C_TILE_EMPTY;
      }
    }

    for ( i=0; i < m_game.getNumberOfImprovementTypes(); i++ )
    {
      if( i != B_PALACE && i != B_CITY )
      {
        m_improvements[i] = false;
      }
    }
    // end dummy values
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

  // Sprites for the client. Don't really belong here, don't really belong
  // anywhere else.

  public Icon getNationalFlagSprite()
  {
    return getOwner().getNation().getFlagSprite();
  }

  public Icon getOccupiedSprite()
  {
    return getStyle().getTileSprite( getStyle().getNumTiles() + 1 );
  }

  public Icon getSprite( boolean isometric )
  {
    CityStyle cs = getStyle();
    int size;
    for( size=0; size < cs.getNumTiles(); size++)
    {
      if ( getSize() < cs.getThreshold( size ) )
      {
        break;
      }
    }

    if ( isometric )
    {
      if ( hasWalls() )
      {
        return cs.getWallSprite( size-1 );
      }
      else
      {
        return cs.getTileSprite( size - 1);
      }
    }
    else
    {
      return cs.getTileSprite( size - 1 );
    }
  }

  public Icon getCityWallSprite( )
  {
    return getStyle().getTileSprite( getStyle().getNumTiles() );
  }


  public class CityAI
  {
    // TODO
  }

}