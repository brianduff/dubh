package org.freeciv.common;

import org.freeciv.net.Packet;
import org.freeciv.net.WorkList; // move me

import java.util.ArrayList;
import java.util.Collection;
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
  // private Collection units;
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
  // TODO: other defaults? (player.c)
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

  public int getCityStyle()
  {
    return m_cityStyle;
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
