package org.freeciv.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;

import org.freeciv.net.PktRulesetGame;
import org.freeciv.net.PktRulesetControl;

/**
 * A freeciv game.
 */
public final class Game implements CommonConstants
{
  // game.h struct civ_game
  
  /** Including not yet established */
  private List m_allConnections;
  /** All established client connections */
  private List m_establishedConnections;
  /** Involved in game, send map etc. */
  private List m_gameConnections;
  
  /** Game ruleset. struct rgame in game.h */
  private GameRules m_rules;

  /** Terrain rules. This really belongs on Map */
  private TerrainRules m_terrainRules;

  /** Ruleset control. Used for various properties */
  private PktRulesetControl m_rulesetControl;

  private int m_gold;
  private int m_tech;
  private int m_researchCost;
  private int m_skillLevel;
  private boolean m_timeout;

  private int m_endYear;
  private int m_year;
  private int m_minPlayers;
  private int m_maxPlayers;
  private int m_numPlayers;
  private int m_globalWarming;
  private int m_heating;
  private int m_nuclearWinter;
  private int m_cooling;

  private int[] m_globalAdvances;
  private int[] m_globalWonders;

  private int m_techPenalty;
  private int m_foodBox;
  private int m_civStyle;
  private int m_unhappySize;
  private int m_cityFactor;

  private boolean m_spaceRace;

  private Factories m_factories;

  private int m_currentPlayer;

  private Map m_map;

  /**
   * Construct the game
   *
   * @param f the factories object for this client / server
   */
  public Game( ) 
  {
    m_factories = new Factories( this );
    m_allConnections = new ArrayList();
    m_establishedConnections = new ArrayList();
    m_gameConnections = new ArrayList();
    m_rules = new GameRules();
    m_terrainRules = new TerrainRules();

    m_globalAdvances = new int[ A_LAST ];
    m_globalWonders = new int[ B_LAST ];
    m_map = new Map( this );
  }

  public Factories getFactories()
  {
    return m_factories;
  }

  /**
   * Get the map.
   */
  public Map getMap()
  {
    return m_map;
  }

  /**
   * Get the total population for the specified player.
   *
   * @param p the player to get the number of citizens for
   * @return a count of the total number of citizens in all cities for the
   *    specified player.
   */
  public int getCivilizationPopulation(Player p)
  {
    // game.c: civ_population(struct player *)
    
    Iterator cityIter = p.getCities();
    int count = 0;
    while (cityIter.hasNext())
    {
      City c = (City) cityIter.next();
      count += c.getPopulation();
    }

    return count;
  }

  /**
   * Get a player by number
   *
   * @param playerNumber the player number
   * @return a Player
   */
  public Player getPlayer(int playerNumber)
  {
    try
    {
      return (Player) getFactories().getPlayerFactory().findById( playerNumber );
    }
    catch (IllegalArgumentException ille)
    {
      // not sure about this at all.
      return (Player) getFactories().getPlayerFactory().create(playerNumber);
    }
  }


  public Player getCurrentPlayer()
  {
    return (Player)
      getFactories().getPlayerFactory().findById( m_currentPlayer );
  }

  public boolean isCurrentPlayer(int playerNo)
  {
    return m_currentPlayer == playerNo;
  }

  public boolean isCurrentPlayer(Player p)
  {
    return p == getCurrentPlayer();
  }

  public void setCurrentPlayer(int playerNo)
  {
    m_currentPlayer = playerNo;
  }

  public void setCurrentPlayer(Player p)
  {
    m_currentPlayer = p.getId();
  }

  public void setGold(int gold)
  {
    m_gold = gold;
  }

  public int getGold()
  {
    return m_gold;
  }

  public void setTech(int tech)
  {
    m_tech = tech;
  }

  public int getTech()
  {
    return m_tech;
  }

  public void setResearchCost( int researchCost )
  {
    m_researchCost = researchCost;
  }

  public int getResearchCost()
  {
    return m_researchCost;
  }

  public void setSkillLevel( int level )
  {
    m_skillLevel = level;
  }

  public int getSkillLevel()
  {
    return m_skillLevel;
  }

  public void setTimeout( boolean timeout )
  {
    m_timeout = timeout;
  }

  public boolean isTimeout()
  {
    return m_timeout;
  }

  public void setEndYear( int endYear )
  {
    m_endYear = endYear;
  }

  public int getEndYear()
  {
    return m_endYear;
  }

  public void setMinPlayers( int minPlayers)
  {
    m_minPlayers = minPlayers;
  }

  public int getMinPlayers()
  {
    return m_minPlayers;
  }

  public void setMaxPlayers( int maxPlayers )
  {
    m_maxPlayers = maxPlayers;
  }

  public int getMaxPlayers()
  {
    return m_maxPlayers;
  }

  public void setNumberOfPlayers( int numPlayers )
  {
    m_numPlayers = numPlayers;
  }

  public int getNumberOfPlayers()
  {
    return m_numPlayers;
  }

  public void setGlobalWarming( int globalWarming )
  {
    m_globalWarming = globalWarming;
  }

  public int getGlobalWarming()
  {
    return m_globalWarming;
  }

  public void setHeating( int heating )
  {
    m_heating = heating;
  }

  public int getHeating()
  {
    return m_heating;
  }

  public void setNuclearWinter( int nuclearWinter )
  {
    m_nuclearWinter = nuclearWinter;
  }

  public int getNuclearWinter()
  {
    return m_nuclearWinter;
  }

  public void setCooling( int cooling )
  {
    m_cooling = cooling;
  }

  public int getCooling()
  {
    return m_cooling;
  }

  public void setGlobalAdvance( int idx, int advanceId )
  {
    m_globalAdvances[ idx ] = advanceId;
  }

  public Advance getGlobalAdvance( int idx )
  {
    int advanceNum = m_globalAdvances[ idx ];
    return (Advance) getFactories().getAdvanceFactory().findById( advanceNum );
  }

  public void setGlobalWonder( int idx, int wonderId )
  {
    m_globalWonders[ idx ] = wonderId;
  }

  public int getGlobalWonder( int idx )
  {
    return m_globalWonders[ idx ];
  }

  public void setTechPenalty( int penalty )
  {
    m_techPenalty = penalty;
  }

  public int getTechPenalty()
  {
    return m_techPenalty;
  }

  public void setFoodBox( int foodBox )
  {
    m_foodBox = foodBox;
  }

  public int getFoodBox()
  {
    return m_foodBox;
  }

  public void setCivStyle( int civStyle )
  {
    m_civStyle = civStyle;
  }

  public int getCivStyle()
  {
    return m_civStyle;
  }

  public void setUnhappySize( int unhappy )
  {
    m_unhappySize = unhappy;
  }

  public int getUnhappySize()
  {
    return m_unhappySize;
  }

  public void setCityFactor( int cityFactor )
  {
    m_cityFactor = cityFactor;
  }

  public int getCityFactor()
  {
    return m_cityFactor;
  }

  public void setSpaceRace( boolean spaceRace )
  {
    m_spaceRace = spaceRace;
  }

  public boolean isSpaceRace()
  {
    return m_spaceRace;
  }
  
  public TerrainRules getTerrainRules()
  {
    return m_terrainRules;
  }
  
  public Collection getAllConnections()
  {
    return m_allConnections;
  }
  
  public Collection getEstablishedConnections()
  {
    return m_establishedConnections;
  }
  
  public Collection getGameConnections()
  {
    return m_gameConnections;
  }
  
  public GameRules getGameRules()
  {
    return m_rules;
  }

  public void setYear(int year)
  {
    m_year = year;
  }

  public int getYear()
  {
    return m_year;
  }
  
  /**
   * Gets a string version of the year, with B.C./A.D.
   */
  public String getYearString()
  {
    StringBuffer yearString = new StringBuffer(8);
    yearString.append( String.valueOf( Math.abs( getYear() ) ) );
    yearString.append( ' ' );
    if ( getYear() < 0 )
    {
      yearString.append( "B.C." );
    }
    else
    {
      yearString.append( "A.D." );
    }
    return yearString.toString();
  }

  /**
   * Determine what the next year after the specified year
   * is
   * 
   * @param year a year
   * @return the next game year after the specified year
   */
  public int nextYear( int year )
  {
    int spaceshipparts;
    int parts[] = { B_SCOMP, B_SMODULE, B_SSTRUCTURAL, B_LAST };

    if ( year == 1 )
    {
      year = 0;
    }

    spaceshipparts = 0;

    if ( isSpaceRace() )
    {
/*      for ( int i = 0; parts[i] < B_LAST; i++ )
      {
        // TODO: Count the number of spaceship parts 
        // see game.c: game_next_year()
      }
*/
    }

    if ( year >= 1900 || ( spaceshipparts >= 3 && year > 0 ) )
    {
      year += 1;
    }
    else if ( year >= 1750 || spaceshipparts >= 2 )
    {
      year += 2;
    }
    else if ( year >= 1500 || spaceshipparts >= 1 )
    {
      year += 5;
    }
    else if ( year >= 1000 )
    {
      year += 20;
    }
    else if ( year >= -1000 )
    {
      year += 25;
    }
    else
    {
      year += 50;
    }

    if ( year == 0 )
    {
      year = 1;
    }

    return year;
  }

  /**
   * Call this to initialize the parts of Game that are configured using 
   * rulesetControl packets.
   *
   * @param prc
   */
  public void setRulesetControl(PktRulesetControl prc)
  {
    m_rulesetControl = prc;
  }

  /**
   * Get the city size which requires aqueducts
   */
  public int getAqueductSize()
  {
    return m_rulesetControl.aqueduct_size;
  }

  /**
   * Get the city size that requires sewers
   */
  public int getSewerSize()
  {
    return m_rulesetControl.sewer_size;
  } 

  public int getAddToSizeLimit()
  {
    return m_rulesetControl.add_to_size_limit;
  }

  public int getBonusTech()
  {
    return m_rulesetControl.rtech.get_bonus_tech;
  }

  public int getCathedralPlus()
  {
    return m_rulesetControl.rtech.cathedral_plus;
  }

  public int getCathedralMinus()
  {
    return m_rulesetControl.rtech.cathedral_minus;
  }

  public int getColosseumPlus()
  {
    return m_rulesetControl.rtech.colosseum_plus;
  }

  public int getTemplePlus()
  {
    return m_rulesetControl.rtech.temple_plus;
  }

  public boolean isPartisanRequired(int techId) // Technology
  {
    return (m_rulesetControl.rtech.partisan_req[techId] != 0);
  }

  /** 
   * How many governments are there?
   */
  public int getGovernmentCount()
  {
    return m_rulesetControl.government_count;
  }

  /**
   * Which government is "anarchy"
   */
  public Government getGovernmentWhenAnarchy() // return Government
  {
    return (Government) getFactories().getGovernmentFactory().findById(
      m_rulesetControl.government_when_anarchy
    );
  }

  /**
   * Which government is the default government
   */
  public Government getDefaultGovernment() // return Government
  {
    return (Government) getFactories().getGovernmentFactory().findById(
      m_rulesetControl.default_government
    );
  }

  public int getNumberOfUnitTypes()
  {
    return m_rulesetControl.num_unit_types;
  }

  public int getNumberOfImprovementTypes()
  {
    return m_rulesetControl.num_impr_types;
  }

  public int getNumberOfTechnologyTypes()
  {
    return m_rulesetControl.num_tech_types;
  }

  public int getNationCount()
  {
    return m_rulesetControl.nation_count;
  }

  public int getPlayableNationCount()
  {
    return m_rulesetControl.playable_nation_count;
  }

  public int getCityStyleCount()
  {
    return m_rulesetControl.style_count;
  }
  
  public static class GameRules
  {
    private PktRulesetGame m_rulesetPacket;
    public void init( PktRulesetGame pkt )
    {
      m_rulesetPacket = pkt;
    }
    public int getMinCityCenterFood()
    {
      return m_rulesetPacket.min_city_center_food;
    }
    public int getMinCityCenterShield()
    {
      return m_rulesetPacket.min_city_center_shield;
    }
    public int getMinCityCenterTrade()
    {
      return m_rulesetPacket.min_city_center_trade;
    }
    public int getMinDistanceBetweenCities()
    {
      return m_rulesetPacket.min_dist_bw_cities;
    }
    public int getInitialVisualRadius()
    {
      return m_rulesetPacket.init_vis_radius_sq;
    }
    public boolean isHutOverflightAllowed()
    {
      return m_rulesetPacket.hut_overflight != 0;
    }
    public int getPillageSelect()
    {
      return m_rulesetPacket.pillage_select;
    }
    public boolean isNuclearContaminationPossible()
    {
      return m_rulesetPacket.nuke_contamination != 0;
    }
    public int getInitialGranaryFood()
    {
      return m_rulesetPacket.granary_food_ini;
    }
    public int getGranaryFoodIncrement()
    {
      return m_rulesetPacket.granary_food_inc;
    }
  }
  
  /**
   * Shortcut to factory method.  Please check unitTypeExists( id ) before
   * calling this.
   */
  public UnitType getUnitType( int id )
  {
    return (UnitType)getFactories().getUnitTypeFactory().findById( id );
  }
  
  /**
   * Shortcut to factory method.  Please check buildingExists( id ) before
   * calling this.
   */
  public Building getBuilding( int id )
  {
    return (Building)getFactories().getBuildingFactory().findById( id );
  }
  
  /**
   * Shortcut to factory method.  Please check advanceExists( id ) before
   * calling this.
   */
  public Advance getAdvance( int id )
  {
    return (Advance)getFactories().getAdvanceFactory().findById( id );
  }
  
  /**
   * Returns true if the unit type "exists", that is:
   * - the index is valid
   * - it has not been flagged as removed by setting the tech requirement
   *    to A_LAST
   * 
   * @param id the id to test
   * @returns true if this unit type can exist in the game
   */
  public boolean unitTypeExists( int id )
  {
    // unittype.c: unit_type_exists()
    if ( id < 0 || id >= U_LAST || id >= getNumberOfUnitTypes() )
    {
      return false;
    }
    return ( getUnitType( id ) ).getRequiredAdvanceId() != A_LAST;
  }

  /**
   * Returns true if the building "exists", that is:
   * - the index is valid
   * - it has not been flagged as removed by setting the tech requirement
   *    to A_LAST
   * - if it is a spaceship part, the space race is enabled
   * 
   * @param id the id to test
   * @returns true if this building can exist in the game
   */
  public boolean buildingExists( int id )
  {
    // improvement.c: improvement_exists()

    if ( id < 0 || id >= B_LAST || id >= getNumberOfImprovementTypes() )
    {
      return false;
    }

    if ( ( id == B_SCOMP || id == B_SMODULE || id == B_SSTRUCTURAL )
         && !isSpaceRace() )
    {
      return false;
    }

    return ( getBuilding( id ) ).getRequiredAdvanceId() != A_LAST;
  }
  
  /**
   * Returns true if the advance "exists", that is:
   * - the index is valid
   * - it has not been flagged as removed by setting the tech requirement
   *    to A_LAST
   * 
   * @param id the id to test
   * @returns true if this advance can exist in the game
   */
  public boolean advanceExists( int id )
  {
    // tech.c: tech_exists()

    if ( id < 0 || id >= A_LAST || id >= getNumberOfTechnologyTypes() )
    {
      return false;
    }

    for( Iterator i = ( getAdvance( id ) ).getRequiredAdvanceIds();
         i.hasNext();)
    {
      if( ( (Integer)i.next() ).intValue() == A_LAST )
      {
        return false;
      }
    }
    return true;
  }


}
