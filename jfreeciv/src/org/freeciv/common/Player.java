package org.freeciv.common;

import org.freeciv.net.Packet;
import org.freeciv.net.WorkList; // move me

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class representing a player in Freeciv. You can't directly instantiate this
 * class: instances of this object are
 * created and retrieved using the player factory on Client: use
 * Client.getPlayerFactory().
 */
public class Player implements GameObject, CommonConstants
{
  private int m_playerNumber;
  private String m_name;
  private String m_username;
  private boolean m_isMale;
  private Nation m_nation;
  private boolean m_isTurnDone;
  private int m_nTurnsIdle;
  private boolean m_isAlive;
  private boolean m_gotTech;
  private int m_revolution;
  private int m_capital;
  private int m_embassy;
  private int m_reputation;
  private DiplomacyState[] m_diplomacyState;
  private int m_cityStyle;
  // private Collection cities;
  // private Score score;
  private Economy m_economy;
  private Research m_research;
  // private PlayerSpaceship spaceship;
  private int m_futureTech;
  private AI m_ai;
  private boolean m_isConnected;
  private Connection m_connection;
  private Collection m_connections;
  private WorkList[] m_worklists;
  // private PlayerTile[] privateMap;
  private int m_givesSharedVision;
  private int m_reallyGivesSharedVision;
  private Government m_government;
  private GameObjectFactory m_playerFactory;
  private Spaceship m_spaceship;

  private HashMap m_units;

  private ArrayList m_cities;

  /**
   * The constructor is package protected: only the player factory instantiates
   * players
   */
  Player(GameObjectFactory playerFactory)
  {
    m_connections = new ArrayList();
    m_playerFactory = playerFactory;
    m_economy = new Economy();
    m_diplomacyState =
      new DiplomacyState[ MAX_NUM_PLAYERS + MAX_NUM_BARBARIANS ];
    m_worklists =
      new WorkList[ MAX_NUM_WORKLISTS ];
    m_research = new Research();
    m_ai = new AI();
    m_cities = new ArrayList();
    m_spaceship = new Spaceship();
  // TODO: other defaults? (player.c)
    m_units = new HashMap();
  }

  /**
   * Initialize the focus status of all units belonging to this player
   * to FOCUS_AVAIL
   */
  public void initUnitFocusStatus()
  {
    // player.c: player_set_unit_focus_status()
    Iterator i = getUnits();
    while ( i.hasNext() )
    {
      ((Unit)i.next()).setFocusStatus( Unit.FOCUS_AVAIL );
    }
  }

  /**
   * Find a city belonging to this player by Id
   *
   * @param id the city id
   * @return the specified City, if it belongs to this player, or null
   *  if the specified city id doesn't exist, or does not belong to this
   *  player.
   */
  public City getCity( int id )
  {

    City c = City.findById( id );

    if ( c != null && ( c.getOwner() == this ) )
    {
      return c;
    }
    return null;
  }

  /**
   * Find a city belonging to this player by Id
   *
   * @param id the city id
   * @return the specified City, if it belongs to this player, or null
   *  if the specified city id doesn't exist, or does not belong to this
   *  player.
   * @deprecated use getCity( int ) instead.
   */
  public City findCityById( int id )
  {
    return getCity( id ); //infinate loop here-somewhere the REAL city has to be called up.
  }

  /**
   * Find the city belonging to this player which has a palace (i.e. is the
   * capital city
   *
   * @return the capital city for this player, null if there is no capital
   */
  public City findPalace()
  {
    Iterator i = getCities();
    while ( i.hasNext() )
    {
      City c = (City)i.next();
      if ( c.hasBuilding( B_PALACE ) )
      {
        return c;
      }
    }
    return null;
  }

  public Unit getUnit( int id )
  {
    Unit unit = (Unit) m_units.get( new Integer( id ) );

    return unit;
  }

  /**
   * Get an iterator over all units belonging to this player
   */
  public Iterator getUnits()
  {
    return m_units.values().iterator();
  }

  public int getUnitCount()
  {
    return m_units.keySet().size();
  }

  public void addUnit( Unit u )
  {
    m_units.put( new Integer( u.getId() ), u );
  }

  public void addCity( City c )
  {
    m_cities.add( c );
  }

  public Spaceship getSpaceship()
  {
    return m_spaceship;
  }

  /**
   * Get an iterator of cities that belong to this player.
   *
   * @return an iterator. Each element in the iterator is a City.
   */
  public Iterator getCities()
  {
    return m_cities.iterator();
  }

  public int getCityCount()
  {
    return m_cities.size();
  }

  public void initFromPacket(Packet p)
  {

  }

  public int getId()
  {
    return getPlayerNumber();
  }

  public int getPlayerNumber()
  {
    return m_playerNumber;
  }

  public void setPlayerNumber( int number )
  {
    m_playerNumber = number;
  }

  /**
   * Get the connections for this player
   *
   * @return a collection of connections
   */
  public Collection getConnections()
  {
    return m_connections;
  }

  public void setNation(Nation n)
  {
    m_nation = n;
  }

  public Nation getNation()
  {
    return m_nation;
  }

  public String getName()
  {
    return m_name;
  }

  public void setName(String name)
  {
    m_name = name;
  }

  public boolean isMale()
  {
    return m_isMale;
  }

  public void setMale(boolean isMale)
  {
    m_isMale = isMale;
  }

  public void setGovernment( Government g )
  {
    m_government = g;
  }

  public Government getGovernment()
  {
    return m_government;
  }

  public void setEmbassy( int embassy )
  {
    m_embassy = embassy;
  }

  public int getEmbassy()
  {
    return m_embassy;
  }

  public void setGivesSharedVision( int givesShared )
  {
    m_givesSharedVision = givesShared;
  }

  public int getGivesSharedVision()
  {
    return m_givesSharedVision;
  }

  public void setCityStyle( int cityStyle )
  {
    m_cityStyle = cityStyle;
  }

  public CityStyle getCityStyle()
  {
    return (CityStyle)
      m_playerFactory.getParent().getCityStyleFactory().findById( m_cityStyle );
  }

  public Economy getEconomy()
  {
    return m_economy;
  }

  public DiplomacyState getDiplomacyState( int idx )
  {
    DiplomacyState s = m_diplomacyState[ idx ];
    if (s == null)
    {
      s = new DiplomacyState();
      m_diplomacyState[ idx ] = s;
    }
    return s;
  }

  public void setReputation( int reputation )
  {
    m_reputation = reputation;
  }

  public int getReputation()
  {
    return m_reputation;
  }

  public WorkList getWorkList( int idx )
  {
    return m_worklists[ idx ];
  }

  public void setWorkList( int idx, WorkList w )
  {
    m_worklists[ idx ] = w;
  }

  public Research getResearch()
  {
    return m_research;
  }

  public void setFutureTech( int futureTech)
  {
    m_futureTech = futureTech;
  }

  public int getFutureTech()
  {
    return m_futureTech;
  }

  public AI getAI()
  {
    return m_ai;
  }

  public void setTurnDone( boolean isTurnDone )
  {
    m_isTurnDone = isTurnDone;
  }

  public boolean isTurnDone()
  {
    return m_isTurnDone;
  }

  public void setNumberOfIdleTurns( int idleTurns )
  {
    m_nTurnsIdle = idleTurns;
  }

  public int getNumberOfIdleTurns()
  {
    return m_nTurnsIdle;
  }

  public void setAlive( boolean isAlive )
  {
    m_isAlive = isAlive;
  }

  public boolean isAlive()
  {
    return m_isAlive;
  }

  public void setConnected( boolean isConnected )
  {
    m_isConnected = isConnected;
  }

  public boolean isConnected()
  {
    return m_isConnected;
  }

  public void setRevolution( int revolution )
  {
    m_revolution = revolution;
  }

  public int getRevolution()
  {
    return m_revolution;
  }

  public boolean canSeeUnit( Unit u )
  {
    // todo: player.c: player_can_see_unit()
    return true;
  }

  private boolean m_foundCity = false;

  /**
   * Returns true if the tile at the specified location is inside the city
   * radius of one of this player's cities
   *
   * @param x a horizontal tile coordinate
   * @param y a vertical tile coordinate
   * @return true if the specified tile is inside the radius of one of this
   * player's cities
   */
  public boolean inCityRadius( int x, int y )
  {
    // player.c:player_in_city_radius()
    final Map map = m_playerFactory.getParent().getGame().getMap();

    m_foundCity = false;
    map.iterateMapCityRadius( x, y, new MapPositionIterator() {
      public void iteratePosition( MapPosition mp )
      {
        City c = map.getCity( mp.x, mp.y );
        if ( c != null && c.getOwner() == Player.this )
        {
          m_foundCity = true;
          setFinished( true );
        }
      }
    });

    return m_foundCity;

  }





  public static final class Economy
  {
    private int m_gold;
    private int m_tax;
    private int m_science;
    private int m_luxury;

    public int getGold()
    {
      return m_gold;
    }

    public void setGold(int gold)
    {
      m_gold = gold;
    }

    public int getTax()
    {
      return m_tax;
    }

    public void setTax(int tax)
    {
      m_tax = tax;
    }

    public int getScience()
    {
      return m_science;
    }

    public void setScience(int science)
    {
      m_science = science;
    }

    public int getLuxury()
    {
      return m_luxury;
    }

    public void setLuxury( int lux )
    {
      m_luxury = lux;
    }
  }

  public class Research
  {
    private int m_researched;
    private int m_researchPoints;
    private int m_currentlyResearching;
    private boolean[] m_hasInvention;

    private Research()
    {
      m_hasInvention = new boolean[MAX_NUM_ITEMS]; // BAD BAD BAD
    }

    public void setResearched(int researched)
    {
      m_researched = researched;
    }

    public int getResearched()
    {
      return m_researched;
    }

    public void setResearchPoints(int points)
    {
      m_researchPoints = points;
    }

    public int getResearchPoints()
    {
      return m_researchPoints;
    }

    public void setCurrentlyResearching(int idx)
    {
      m_currentlyResearching = idx;
    }

    public void setCurrentlyResearching(Advance a)
    {
      m_currentlyResearching = a.getId();
    }

    public Advance getCurrentlyResearching()
    {
      return (Advance) m_playerFactory.getParent().
        getAdvanceFactory().findById( m_currentlyResearching );
    }

    public boolean hasInvention( int idx )
    {
      return m_hasInvention[ idx ];
    }

    public boolean hasInvention( Advance adv )
    {
      return hasInvention( adv.getId() );
    }

    public void setHasInvention( int idx, boolean has )
    {
      m_hasInvention[ idx ] = has;
    }

    public void setHasInvention( Advance adv, boolean has )
    {
      setHasInvention( adv.getId(), has );
    }

  }
}
